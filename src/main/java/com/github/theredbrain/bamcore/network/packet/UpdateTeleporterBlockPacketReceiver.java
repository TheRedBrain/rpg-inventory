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

        BlockPos teleportBlockPosition = packet.teleportBlockPosition;

        boolean showActivationArea = packet.showActivationArea;
        boolean showAdventureScreen = packet.showAdventureScreen;

        Vec3i activationAreaDimensions = packet.activationAreaDimensions;
        BlockPos activationAreaPositionOffset = packet.activationAreaPositionOffset;
        BlockPos accessPositionOffset = packet.accessPositionOffset;

        boolean setAccessPosition = packet.setAccessPosition;

        TeleporterBlockBlockEntity.TeleportationMode teleportationMode = packet.teleportationMode;

        BlockPos directTeleportPositionOffset = packet.directTeleportPositionOffset;
        double directTeleportOrientationYaw = packet.directTeleportOrientationYaw;
        double directTeleportOrientationPitch = packet.directTeleportOrientationPitch;

        TeleporterBlockBlockEntity.SpawnPointType spawnPointType = packet.spawnPointType;

        List<Pair<String, String>> locationsList = packet.locationsList;

        boolean consumeKeyItemStack = packet.consumeKeyItemStack;

        String teleporterName = packet.teleporterName;
        String currentTargetOwnerLabel = packet.currentTargetOwnerLabel;
        String currentTargetIdentifierLabel = packet.currentTargetIdentifierLabel;
        String currentTargetEntranceLabel = packet.currentTargetEntranceLabel;
        String teleportButtonLabel = packet.teleportButtonLabel;
        String cancelTeleportButtonLabel = packet.cancelTeleportButtonLabel;

        World world = player.getWorld();

        boolean updateSuccessful = true;

        BlockEntity blockEntity = world.getBlockEntity(teleportBlockPosition);
        BlockState blockState = world.getBlockState(teleportBlockPosition);

        if (blockEntity instanceof TeleporterBlockBlockEntity teleporterBlockBlockEntity) {
            if (!teleporterBlockBlockEntity.setShowAdventureScreen(showAdventureScreen)) {
                player.sendMessage(Text.translatable("teleporter_block.showAdventureScreen.invalid"), false);
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
            if (!teleporterBlockBlockEntity.setAccessPositionOffset(accessPositionOffset)) {
                player.sendMessage(Text.translatable("teleporter_block.accessPositionOffset.invalid"), false);
                updateSuccessful = false;
            }
            if (!teleporterBlockBlockEntity.setSetAccessPosition(setAccessPosition)) {
                player.sendMessage(Text.translatable("teleporter_block.setAccessPosition.invalid"), false);
                updateSuccessful = false;
            }
            if (!teleporterBlockBlockEntity.setTeleportationMode(teleportationMode)) {
                player.sendMessage(Text.translatable("teleporter_block.teleportationMode.invalid"), false);
                updateSuccessful = false;
            }
            if (teleportationMode == TeleporterBlockBlockEntity.TeleportationMode.DIRECT) {
                if (!teleporterBlockBlockEntity.setDirectTeleportPositionOffset(directTeleportPositionOffset)) {
                    player.sendMessage(Text.translatable("teleporter_block.directTeleportPositionOffset.invalid"), false);
                    updateSuccessful = false;
                }
                if (!teleporterBlockBlockEntity.setDirectTeleportOrientationYaw(directTeleportOrientationYaw)) {
                    player.sendMessage(Text.translatable("teleporter_block.directTeleportOrientationYaw.invalid"), false);
                    updateSuccessful = false;
                }
                if (!teleporterBlockBlockEntity.setDirectTeleportOrientationPitch(directTeleportOrientationPitch)) {
                    player.sendMessage(Text.translatable("teleporter_block.directTeleportOrientationPitch.invalid"), false);
                    updateSuccessful = false;
                }
            } else if (teleportationMode == TeleporterBlockBlockEntity.TeleportationMode.SPAWN_POINTS) {
                if (!teleporterBlockBlockEntity.setLocationType(spawnPointType)) {
                    player.sendMessage(Text.translatable("teleporter_block.specificLocationType.invalid"), false);
                    updateSuccessful = false;
                }
            } else if (teleportationMode == TeleporterBlockBlockEntity.TeleportationMode.PLAYER_LOCATIONS) {
                if (!teleporterBlockBlockEntity.setLocationsList(locationsList)) {
                    player.sendMessage(Text.translatable("teleporter_block.locationsList.invalid"), false);
                    updateSuccessful = false;
                }
            }
            if (!teleporterBlockBlockEntity.setConsumeKeyItemStack(consumeKeyItemStack)) {
                player.sendMessage(Text.translatable("teleporter_block.consumeKeyItemStack.invalid"), false);
                updateSuccessful = false;
            }
            if (!teleporterBlockBlockEntity.setTeleporterName(teleporterName)) {
                player.sendMessage(Text.translatable("teleporter_block.teleporterName.invalid"), false);
                updateSuccessful = false;
            }
            if (!teleporterBlockBlockEntity.setCurrentTargetOwnerLabel(currentTargetOwnerLabel)) {
                player.sendMessage(Text.translatable("teleporter_block.currentTargetOwnerLabel.invalid"), false);
                updateSuccessful = false;
            }
            if (!teleporterBlockBlockEntity.setCurrentTargetIdentifierLabel(currentTargetIdentifierLabel)) {
                player.sendMessage(Text.translatable("teleporter_block.currentTargetIdentifierLabel.invalid"), false);
                updateSuccessful = false;
            }
            if (!teleporterBlockBlockEntity.setCurrentTargetEntranceLabel(currentTargetEntranceLabel)) {
                player.sendMessage(Text.translatable("teleporter_block.currentTargetEntranceLabel.invalid"), false);
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
