package com.github.theredbrain.betteradventuremode.network.packet;

import com.github.theredbrain.betteradventuremode.block.entity.TeleporterBlockEntity;
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

        boolean onlyTeleportDimensionOwner = packet.onlyTeleportDimensionOwner;
        boolean teleportTeam = packet.teleportTeam;

        TeleporterBlockEntity.TeleportationMode teleportationMode = packet.teleportationMode;

        BlockPos directTeleportPositionOffset = packet.directTeleportPositionOffset;
        double directTeleportOrientationYaw = packet.directTeleportOrientationYaw;
        double directTeleportOrientationPitch = packet.directTeleportOrientationPitch;

        TeleporterBlockEntity.SpawnPointType spawnPointType = packet.spawnPointType;

        List<Pair<String, String>> locationsList = packet.locationsList;

        String teleporterName = packet.teleporterName;
        String currentTargetIdentifierLabel = packet.currentTargetIdentifierLabel;
        String currentTargetOwnerLabel = packet.currentTargetOwnerLabel;
        String teleportButtonLabel = packet.teleportButtonLabel;
        String cancelTeleportButtonLabel = packet.cancelTeleportButtonLabel;

        World world = player.getWorld();

        boolean updateSuccessful = true;

        BlockEntity blockEntity = world.getBlockEntity(teleportBlockPosition);
        BlockState blockState = world.getBlockState(teleportBlockPosition);

        if (blockEntity instanceof TeleporterBlockEntity teleporterBlockEntity) {
            teleporterBlockEntity.setShowAdventureScreen(showAdventureScreen);
            teleporterBlockEntity.setShowActivationArea(showActivationArea);
            teleporterBlockEntity.setActivationAreaDimensions(activationAreaDimensions);
            teleporterBlockEntity.setActivationAreaPositionOffset(activationAreaPositionOffset);
            teleporterBlockEntity.setAccessPositionOffset(accessPositionOffset);
            teleporterBlockEntity.setSetAccessPosition(setAccessPosition);
            teleporterBlockEntity.setOnlyTeleportDimensionOwner(onlyTeleportDimensionOwner);
            teleporterBlockEntity.setTeleportTeam(teleportTeam);
            teleporterBlockEntity.setTeleportationMode(teleportationMode);
            if (teleportationMode == TeleporterBlockEntity.TeleportationMode.DIRECT) {
                teleporterBlockEntity.setDirectTeleportPositionOffset(directTeleportPositionOffset);
                if (!teleporterBlockEntity.setDirectTeleportOrientationYaw(directTeleportOrientationYaw)) {
                    player.sendMessage(Text.translatable("teleporter_block.directTeleportOrientationYaw.invalid"), false);
                    updateSuccessful = false;
                }
                if (!teleporterBlockEntity.setDirectTeleportOrientationPitch(directTeleportOrientationPitch)) {
                    player.sendMessage(Text.translatable("teleporter_block.directTeleportOrientationPitch.invalid"), false);
                    updateSuccessful = false;
                }
            } else if (teleportationMode == TeleporterBlockEntity.TeleportationMode.SPAWN_POINTS) {
                teleporterBlockEntity.setLocationType(spawnPointType);
            } else if (teleportationMode == TeleporterBlockEntity.TeleportationMode.LOCATIONS) {
                if (!teleporterBlockEntity.setLocationsList(locationsList)) {
                    player.sendMessage(Text.translatable("teleporter_block.locationsList.invalid"), false);
                    updateSuccessful = false;
                }
            }
            teleporterBlockEntity.setTeleporterName(teleporterName);
            teleporterBlockEntity.setCurrentTargetIdentifierLabel(currentTargetIdentifierLabel);
            teleporterBlockEntity.setCurrentTargetOwnerLabel(currentTargetOwnerLabel);
            teleporterBlockEntity.setTeleportButtonLabel(teleportButtonLabel);
            teleporterBlockEntity.setCancelTeleportButtonLabel(cancelTeleportButtonLabel);

            if (updateSuccessful) {
                player.sendMessage(Text.translatable("teleporter_block.update_successful"), true);
            }
            teleporterBlockEntity.markDirty();
            world.updateListeners(teleportBlockPosition, blockState, blockState, Block.NOTIFY_ALL);
        }
    }
}
