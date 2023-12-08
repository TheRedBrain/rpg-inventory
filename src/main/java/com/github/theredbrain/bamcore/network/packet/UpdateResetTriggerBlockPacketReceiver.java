package com.github.theredbrain.bamcore.network.packet;

import com.github.theredbrain.bamcore.block.entity.ResetTriggerBlockEntity;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;

public class UpdateResetTriggerBlockPacketReceiver implements ServerPlayNetworking.PlayPacketHandler<UpdateResetTriggerBlockPacket> {

    @Override
    public void receive(UpdateResetTriggerBlockPacket packet, ServerPlayerEntity player, PacketSender responseSender) {

        if (!player.isCreativeLevelTwoOp()) {
            return;
        }

        BlockPos resetTriggerBlockPos = packet.resetTriggerBlockPosition;
        List<BlockPos> resetBlocks = packet.resetBlocks;

        World world = player.getWorld();

        boolean updateSuccessful = true;

        BlockEntity blockEntity = world.getBlockEntity(resetTriggerBlockPos);
        BlockState blockState = world.getBlockState(resetTriggerBlockPos);

        if (blockEntity instanceof ResetTriggerBlockEntity resetTriggerBlockEntity) {
            if (!resetTriggerBlockEntity.setResetBlocks(resetBlocks)) {
                player.sendMessage(Text.translatable("reset_block.resetBlocks.invalid"), false);
                updateSuccessful = false;
            }
            if (updateSuccessful) {
                player.sendMessage(Text.translatable("reset_trigger_block.update_successful"), true);
            }
            resetTriggerBlockEntity.markDirty();
            world.updateListeners(resetTriggerBlockPos, blockState, blockState, Block.NOTIFY_ALL);
        }
    }
}
