package com.github.theredbrain.bamcore.network.packet;

import com.github.theredbrain.bamcore.block.entity.TeleporterBlockBlockEntity;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Pair;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class UpdateTeleporterBlockPacketReceiver implements ServerPlayNetworking.PlayChannelHandler {

    @Override
    public void receive(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {

        if (!player.isCreativeLevelTwoOp()) {
            return;
        }
        // teleporter block position
        BlockPos teleporterBlockPos = buf.readBlockPos();

        // teleporter block information
        String teleporterName = buf.readString();
        boolean showActivationArea = buf.readBoolean();

        int activationAreaDimensionsX = buf.readInt();
        int activationAreaDimensionsY = buf.readInt();
        int activationAreaDimensionsZ = buf.readInt();
        Vec3i activationAreaDimensions = new Vec3i(activationAreaDimensionsX, activationAreaDimensionsY, activationAreaDimensionsZ);
        BlockPos activationAreaPositionOffset = buf.readBlockPos();

        boolean showAdventureScreen = buf.readBoolean();

        int teleportationMode = buf.readInt();

        BlockPos directTeleportPositionOffset = buf.readBlockPos();
        double directTeleportPositionOffsetYaw = buf.readDouble();
        double directTeleportPositionOffsetPitch = buf.readDouble();

        int specificLocationType = buf.readInt();

        int locationsListSize = buf.readInt();
        List<Pair<String, String>> locationsList = new ArrayList<>(List.of());
        for (int i = 0; i < locationsListSize; i++) {
            locationsList.add(new Pair<>(buf.readString(), buf.readString()));
        }

        boolean consumeKeyItemStack = buf.readBoolean();

        String teleportButtonLabel = buf.readString();
        String cancelTeleportButtonLabel = buf.readString();

        server.execute(() -> {
            World world = player.method_48926();

            boolean updateSuccessful = true;

            BlockEntity blockEntity = world.getBlockEntity(teleporterBlockPos);
            BlockState blockState = world.getBlockState(teleporterBlockPos);

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
                world.updateListeners(teleporterBlockPos, blockState, blockState, Block.NOTIFY_ALL);
            }
        });
    }
}
