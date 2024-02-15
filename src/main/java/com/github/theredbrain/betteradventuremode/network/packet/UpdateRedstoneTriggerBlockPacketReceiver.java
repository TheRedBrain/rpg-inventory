package com.github.theredbrain.betteradventuremode.network.packet;

import com.github.theredbrain.betteradventuremode.block.entity.RedstoneTriggerBlockEntity;
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

public class UpdateRedstoneTriggerBlockPacketReceiver implements ServerPlayNetworking.PlayPacketHandler<UpdateRedstoneTriggerBlockPacket> {
    @Override
    public void receive(UpdateRedstoneTriggerBlockPacket packet, ServerPlayerEntity player, PacketSender responseSender) {

        if (!player.isCreativeLevelTwoOp()) {
            return;
        }

        BlockPos redstoneTriggerBlockPosition = packet.redstoneTriggerBlockPosition;

        BlockPos triggeredBlockPositionOffset = packet.triggeredBlockPositionOffset;

        boolean triggeredBlockResets = packet.triggeredBlockResets;

        World world = player.getWorld();

        BlockEntity blockEntity = world.getBlockEntity(redstoneTriggerBlockPosition);
        BlockState blockState = world.getBlockState(redstoneTriggerBlockPosition);

        if (blockEntity instanceof RedstoneTriggerBlockEntity redstoneTriggerBlockEntity) {
            redstoneTriggerBlockEntity.setTriggeredBlock(new MutablePair<>(triggeredBlockPositionOffset, triggeredBlockResets));
            player.sendMessage(Text.translatable("redstone_trigger_block.update_successful"), true);
            redstoneTriggerBlockEntity.markDirty();
            world.updateListeners(redstoneTriggerBlockPosition, blockState, blockState, Block.NOTIFY_ALL);
        }
    }
}
