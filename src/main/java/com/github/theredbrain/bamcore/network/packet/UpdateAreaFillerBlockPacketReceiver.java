package com.github.theredbrain.bamcore.network.packet;

import com.github.theredbrain.bamcore.block.entity.AreaFillerBlockBlockEntity;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;

public class UpdateAreaFillerBlockPacketReceiver implements ServerPlayNetworking.PlayPacketHandler<UpdateAreaFillerBlockPacket> {
    @Override
    public void receive(UpdateAreaFillerBlockPacket packet, ServerPlayerEntity player, PacketSender responseSender) {

        if (!player.isCreativeLevelTwoOp()) {
            return;
        }

        BlockPos areaFillerBlockPos = packet.areaFillerBlockPosition;

        String fillerBlockIdentifier = packet.fillerBlockIdentifier;

        Vec3i filledAreaDimensions = packet.filledAreaDimensions;

        BlockPos filledAreaPositionOffset = packet.filledAreaPositionOffset;

        BlockPos triggeredBlockPositionOffset = packet.triggeredBlockPositionOffset;

        World world = player.getWorld();

        boolean updateSuccessful = true;

        BlockEntity blockEntity = world.getBlockEntity(areaFillerBlockPos);
        BlockState blockState = world.getBlockState(areaFillerBlockPos);

        if (blockEntity instanceof AreaFillerBlockBlockEntity areaFillerBlockBlockEntity) {
            if (!areaFillerBlockBlockEntity.setFillerBlockIdentifier(fillerBlockIdentifier)) {
                player.sendMessage(Text.translatable("area_filler_block.fillerBlockIdentifier.invalid"), false);
                updateSuccessful = false;
            }
            if (!areaFillerBlockBlockEntity.setFilledAreaDimensions(filledAreaDimensions)) {
                player.sendMessage(Text.translatable("area_filler_block.filledAreaDimensions.invalid"), false);
                updateSuccessful = false;
            }
            if (!areaFillerBlockBlockEntity.setFilledAreaPositionOffset(filledAreaPositionOffset)) {
                player.sendMessage(Text.translatable("area_filler_block.filledAreaPositionOffset.invalid"), false);
                updateSuccessful = false;
            }
            if (!areaFillerBlockBlockEntity.setTriggeredBlockPositionOffset(triggeredBlockPositionOffset)) {
                player.sendMessage(Text.translatable("triggered_block.triggeredBlockPositionOffset.invalid"), false);
                updateSuccessful = false;
            }
            if (updateSuccessful) {
                player.sendMessage(Text.translatable("area_filler_block.update_successful"), true);
            }
            areaFillerBlockBlockEntity.markDirty();
            world.updateListeners(areaFillerBlockPos, blockState, blockState, Block.NOTIFY_ALL);
        }
    }
}
