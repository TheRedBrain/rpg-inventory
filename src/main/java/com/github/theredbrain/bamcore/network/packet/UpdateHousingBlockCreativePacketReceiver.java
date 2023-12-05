package com.github.theredbrain.bamcore.network.packet;

import com.github.theredbrain.bamcore.block.entity.HousingBlockBlockEntity;
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
import org.apache.commons.lang3.tuple.MutablePair;

public class UpdateHousingBlockCreativePacketReceiver implements ServerPlayNetworking.PlayPacketHandler<UpdateHousingBlockCreativePacket> {
    @Override
    public void receive(UpdateHousingBlockCreativePacket packet, ServerPlayerEntity player, PacketSender responseSender) {

        if (!player.isCreativeLevelTwoOp()) {
            return;
        }

        BlockPos housingBlockPosition = packet.housingBlockPosition;

        boolean showRestrictBlockBreakingArea = packet.showRestrictBlockBreakingArea;

        Vec3i restrictBlockBreakingAreaDimensions = packet.restrictBlockBreakingAreaDimensions;
        BlockPos restrictBlockBreakingAreaPositionOffset = packet.restrictBlockBreakingAreaPositionOffset;
        BlockPos entrancePositionOffset = packet.entrancePositionOffset;
        double entranceYaw = packet.entranceYaw;
        double entrancePitch = packet.entrancePitch;
        BlockPos triggeredBlockPositionOffset = packet.triggeredBlockPositionOffset;

        HousingBlockBlockEntity.OwnerMode ownerMode = packet.ownerMode;

        World world = player.getWorld();

        boolean updateSuccessful = true;

        BlockEntity blockEntity = world.getBlockEntity(housingBlockPosition);
        BlockState blockState = world.getBlockState(housingBlockPosition);

        if (blockEntity instanceof HousingBlockBlockEntity housingBlockBlockEntity) {

            if (!housingBlockBlockEntity.setShowInfluenceArea(showRestrictBlockBreakingArea)) {
                player.sendMessage(Text.translatable("housing_block.showRestrictBlockBreakingArea.invalid"), false);
                updateSuccessful = false;
            }
            if (!housingBlockBlockEntity.setInfluenceAreaDimensions(restrictBlockBreakingAreaDimensions)) {
                player.sendMessage(Text.translatable("housing_block.restrictBlockBreakingAreaDimensions.invalid"), false);
                updateSuccessful = false;
            }
            if (!housingBlockBlockEntity.setRestrictBlockBreakingAreaPositionOffset(restrictBlockBreakingAreaPositionOffset)) {
                player.sendMessage(Text.translatable("housing_block.restrictBlockBreakingAreaPositionOffset.invalid"), false);
                updateSuccessful = false;
            }
            if (!housingBlockBlockEntity.setEntrance(new MutablePair<>(entrancePositionOffset, new MutablePair<>(entranceYaw, entrancePitch)))) {
                player.sendMessage(Text.translatable("housing_block.entrance.invalid"), false);
                updateSuccessful = false;
            }
            if (!housingBlockBlockEntity.setTriggeredBlockPositionOffset(triggeredBlockPositionOffset)) {
                player.sendMessage(Text.translatable("housing_block.triggeredBlockPositionOffset.invalid"), false);
                updateSuccessful = false;
            }
            if (!housingBlockBlockEntity.setOwnerMode(ownerMode)) {
                player.sendMessage(Text.translatable("housing_block.ownerMode.invalid"), false);
                updateSuccessful = false;
            }
            if (updateSuccessful) {
                player.sendMessage(Text.translatable("housing_block.update_successful"), true);
            }
            housingBlockBlockEntity.markDirty();
            world.updateListeners(housingBlockPosition, blockState, blockState, Block.NOTIFY_ALL);
        }
    }
}
