package com.github.theredbrain.bamcore.network.packet;

import com.github.theredbrain.bamcore.block.entity.UseRelayBlockEntity;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class UpdateUseRelayBlockPacketReceiver implements ServerPlayNetworking.PlayPacketHandler<UpdateUseRelayBlockPacket> {
    @Override
    public void receive(UpdateUseRelayBlockPacket packet, ServerPlayerEntity player, PacketSender responseSender) {

        if (!player.isCreativeLevelTwoOp()) {
            return;
        }

        BlockPos useRelayBlockPosition = packet.useRelayBlockPosition;

        BlockPos relayBlockPositionOffset = packet.relayBlockPositionOffset;

        World world = player.getWorld();

        boolean updateSuccessful = true;

        BlockEntity blockEntity = world.getBlockEntity(useRelayBlockPosition);
        BlockState blockState = world.getBlockState(useRelayBlockPosition);

        if (blockEntity instanceof UseRelayBlockEntity useRelayBlockEntity) {
            if (!useRelayBlockEntity.setRelayBlockPositionOffset(relayBlockPositionOffset)) {
                player.sendMessage(Text.translatable("use_relay_block.relayBlockPositionOffset.invalid"), false);
                updateSuccessful = false;
            }
            if (updateSuccessful) {
                player.sendMessage(Text.translatable("use_relay_block.update_successful"), true);
            }
            useRelayBlockEntity.markDirty();
            world.updateListeners(useRelayBlockPosition, blockState, blockState, Block.NOTIFY_ALL);
        }
    }
}
