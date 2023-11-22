package com.github.theredbrain.bamcore.network.packet;

import com.github.theredbrain.bamcore.block.entity.TeleporterBlockBlockEntity;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Pair;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;

import java.util.List;

public class UpdateTeleporterBlockPacketReceiver implements ServerPlayNetworking.PlayPacketHandler<UpdateTeleporterBlockPacket> {
    @Override
    public void receive(UpdateTeleporterBlockPacket packet, ServerPlayerEntity player, PacketSender responseSender) {

        if (!player.isCreativeLevelTwoOp()) {
            return;
        }
        // teleporter block position
        BlockPos teleportBlockPosition = packet.teleportBlockPosition;

        // teleporter block information
        String teleporterName = packet.teleporterName;
        boolean showActivationArea = packet.showActivationArea;

        Vec3i activationAreaDimensions = packet.activationAreaDimensions;
        BlockPos activationAreaPositionOffset = packet.activationAreaPositionOffset;

        boolean showAdventureScreen = packet.showAdventureScreen;

        int teleportationMode = packet.teleportationMode;

        BlockPos directTeleportPositionOffset = packet.directTeleportPositionOffset;
        double directTeleportPositionOffsetYaw = packet.directTeleportPositionOffsetYaw;
        double directTeleportPositionOffsetPitch = packet.directTeleportPositionOffsetPitch;

        int specificLocationType = packet.specificLocationType;

        List<Pair<String, String>> locationsList = packet.locationsList;

        boolean consumeKeyItemStack = packet.consumeKeyItemStack;

        String teleportButtonLabel = packet.teleportButtonLabel;
        String cancelTeleportButtonLabel = packet.cancelTeleportButtonLabel;

        World world = player.getWorld();

        boolean updateSuccessful = true;

        BlockEntity blockEntity = world.getBlockEntity(teleportBlockPosition);
        BlockState blockState = world.getBlockState(teleportBlockPosition);

        if (blockEntity instanceof TeleporterBlockBlockEntity teleporterBlockBlockEntity) {
            if (!teleporterBlockBlockEntity.setTeleporterName(teleporterName)) {
                player.sendMessage(Text.translatable("teleporter_block.teleporterName.invalid"), false);
                updateSuccessful = false;
            }
            if (!teleporterBlockBlockEntity.setShowActivationArea(showActivationArea)) {
                player.sendMessage(Text.translatable("teleporter_block.showActivationArea.invalid"), false);
                updateSuccessful = false;
            }
            if (!teleporterBlockBlockEntity.setActivationAreaDimensions(activationAreaDimensions)) {
                player.sendMessage(Text.translatable("teleporter_block.activationAreaDimensions.invalid"), false);
                updateSuccessful = false;
            }
            if (!teleporterBlockBlockEntity.setActivationAreaPositionOffset(activationAreaPositionOffset)) {
                player.sendMessage(Text.translatable("teleporter_block.activationAreaPositionOffset.invalid"), false);
                updateSuccessful = false;
            }
            if (!teleporterBlockBlockEntity.setShowAdventureScreen(showAdventureScreen)) {
                player.sendMessage(Text.translatable("teleporter_block.showAdventureScreen.invalid"), false);
                updateSuccessful = false;
            }
            if (!teleporterBlockBlockEntity.setTeleportationMode(teleportationMode)) {
                player.sendMessage(Text.translatable("teleporter_block.teleportationMode.invalid"), false);
                updateSuccessful = false;
            }
            if (teleportationMode == 0) {
                if (!teleporterBlockBlockEntity.setDirectTeleportPositionOffset(directTeleportPositionOffset)) {
                    player.sendMessage(Text.translatable("teleporter_block.directTeleportPositionOffset.invalid"), false);
                    updateSuccessful = false;
                }
                if (!teleporterBlockBlockEntity.setDirectTeleportPositionOffsetYaw(directTeleportPositionOffsetYaw)) {
                    player.sendMessage(Text.translatable("teleporter_block.directTeleportPositionOffsetYaw.invalid"), false);
                    updateSuccessful = false;
                }
                if (!teleporterBlockBlockEntity.setDirectTeleportPositionOffsetPitch(directTeleportPositionOffsetPitch)) {
                    player.sendMessage(Text.translatable("teleporter_block.directTeleportPositionOffsetPitch.invalid"), false);
                    updateSuccessful = false;
                }
            } else if (teleportationMode == 1) {
                if (!teleporterBlockBlockEntity.setSpecificLocationType(specificLocationType)) {
                    player.sendMessage(Text.translatable("teleporter_block.specificLocationType.invalid"), false);
                    updateSuccessful = false;
                }
            } else if (teleportationMode == 2 || teleportationMode == 3) {
                if (!teleporterBlockBlockEntity.setLocationsList(locationsList)) {
                    player.sendMessage(Text.translatable("teleporter_block.accessibleHousesList.invalid"), false);
                    updateSuccessful = false;
                }
            }
            if (!teleporterBlockBlockEntity.setConsumeKeyItemStack(consumeKeyItemStack)) {
                player.sendMessage(Text.translatable("teleporter_block.consumeKeyItemStack.invalid"), false);
                updateSuccessful = false;
            }
            if (!teleporterBlockBlockEntity.setTeleportButtonLabel(teleportButtonLabel)) {
                player.sendMessage(Text.translatable("teleporter_block.teleportButtonLabel.invalid"), false);
                updateSuccessful = false;
            }
            if (!teleporterBlockBlockEntity.setCancelTeleportButtonLabel(cancelTeleportButtonLabel)) {
                player.sendMessage(Text.translatable("teleporter_block.cancelTeleportButtonLabel.invalid"), false);
                updateSuccessful = false;
            }
            if (updateSuccessful) {
                player.sendMessage(Text.translatable("teleporter_block.update_successful"), true);
            }
            teleporterBlockBlockEntity.markDirty();
            world.updateListeners(teleportBlockPosition, blockState, blockState, Block.NOTIFY_ALL);
        }
    }
}
