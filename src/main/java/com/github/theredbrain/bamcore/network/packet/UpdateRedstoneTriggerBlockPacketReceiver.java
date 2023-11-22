package com.github.theredbrain.bamcore.network.packet;

import com.github.theredbrain.bamcore.block.entity.RedstoneTriggerBlockBlockEntity;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class UpdateRedstoneTriggerBlockPacketReceiver implements ServerPlayNetworking.PlayPacketHandler<UpdateRedstoneTriggerBlockPacket> {
    @Override
    public void receive(UpdateRedstoneTriggerBlockPacket packet, ServerPlayerEntity player, PacketSender responseSender) {

        if (!player.isCreativeLevelTwoOp()) {
            return;
        }

        BlockPos redstoneTriggerBlockPosition = packet.redstoneTriggerBlockPosition;

        BlockPos triggeredBlockPositionOffset = packet.triggeredBlockPositionOffset;

        World world = player.getWorld();

        boolean updateSuccessful = true;

        BlockEntity blockEntity = world.getBlockEntity(redstoneTriggerBlockPosition);
        BlockState blockState = world.getBlockState(redstoneTriggerBlockPosition);

        if (blockEntity instanceof RedstoneTriggerBlockBlockEntity redstoneTriggerBlockBlockEntity) {
            if (!redstoneTriggerBlockBlockEntity.setTriggeredBlockPositionOffset(triggeredBlockPositionOffset)) {
                player.sendMessage(Text.translatable("triggered_block.triggeredBlockPositionOffset.invalid"), false);
                updateSuccessful = false;
            }
            if (updateSuccessful) {
                player.sendMessage(Text.translatable("redstone_trigger_block.update_successful"), true);
            }
            redstoneTriggerBlockBlockEntity.markDirty();
            world.updateListeners(redstoneTriggerBlockPosition, blockState, blockState, Block.NOTIFY_ALL);
        }
    }
}
