package com.github.theredbrain.bamcore.network.packet;

import com.github.theredbrain.bamcore.block.entity.TriggeredCounterBlockEntity;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.apache.commons.lang3.tuple.MutablePair;

import java.util.HashMap;
import java.util.List;

public class UpdateTriggeredCounterBlockPacketReceiver implements ServerPlayNetworking.PlayPacketHandler<UpdateTriggeredCounterBlockPacket> {

    @Override
    public void receive(UpdateTriggeredCounterBlockPacket packet, ServerPlayerEntity player, PacketSender responseSender) {

        if (!player.isCreativeLevelTwoOp()) {
            return;
        }

        BlockPos triggeredCounterBlockPosition = packet.triggeredCounterBlockPosition;

        List<MutablePair<Integer, BlockPos>> triggeredBlocksList = packet.triggeredBlocksList;
        HashMap<Integer, BlockPos> triggeredBlocks = new HashMap<>();
        for (MutablePair<Integer, BlockPos> usedBlock : triggeredBlocksList) {
            triggeredBlocks.put(usedBlock.getLeft(), usedBlock.getRight());
        }

        World world = player.getWorld();

        boolean updateSuccessful = true;

        BlockEntity blockEntity = world.getBlockEntity(triggeredCounterBlockPosition);
        BlockState blockState = world.getBlockState(triggeredCounterBlockPosition);

        if (blockEntity instanceof TriggeredCounterBlockEntity triggeredCounterBlockEntity) {
            if (!triggeredCounterBlockEntity.setTriggeredBlocks(triggeredBlocks)) {
                player.sendMessage(Text.translatable("triggered_block.triggeredBlocks.invalid"), false);
                updateSuccessful = false;
            }
            if (updateSuccessful) {
                player.sendMessage(Text.translatable("triggered_counter_block.update_successful"), true);
            }
            triggeredCounterBlockEntity.markDirty();
            world.updateListeners(triggeredCounterBlockPosition, blockState, blockState, Block.NOTIFY_ALL);
        }
    }
}
