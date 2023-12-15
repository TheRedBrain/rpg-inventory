package com.github.theredbrain.bamcore.network.packet;

import com.github.theredbrain.bamcore.BetterAdventureModeCore;
import com.github.theredbrain.bamcore.api.util.PacketByteBufUtils;
import com.github.theredbrain.bamcore.block.entity.TeleporterBlockBlockEntity;
import net.fabricmc.fabric.api.networking.v1.FabricPacket;
import net.fabricmc.fabric.api.networking.v1.PacketType;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Pair;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;

import java.util.List;

public class UpdateTeleporterBlockPacket implements FabricPacket {
    public static final PacketType<UpdateTeleporterBlockPacket> TYPE = PacketType.create(
            BetterAdventureModeCore.identifier("update_teleporter_block"),
            UpdateTeleporterBlockPacket::new
    );

    public final BlockPos teleportBlockPosition;

    public final boolean showActivationArea;
    public final boolean showAdventureScreen;
    public final Vec3i activationAreaDimensions;
    public final BlockPos activationAreaPositionOffset;
    public final BlockPos accessPositionOffset;

    public final boolean setAccessPosition;

    public final TeleporterBlockBlockEntity.TeleportationMode teleportationMode;

    public final BlockPos directTeleportPositionOffset;
    public final double directTeleportOrientationYaw;
    public final double directTeleportOrientationPitch;

    public final TeleporterBlockBlockEntity.SpawnPointType spawnPointType;

    public final List<Pair<String, String>> locationsList;

    public final boolean consumeKeyItemStack;

    public final String teleporterName;
    public final String currentTargetOwnerLabel;
    public final String currentTargetIdentifierLabel;
    public final String currentTargetEntranceLabel;
    public final String teleportButtonLabel;
    public final String cancelTeleportButtonLabel;

    public UpdateTeleporterBlockPacket(BlockPos teleportBlockPosition, boolean showActivationArea, boolean showAdventureScreen, Vec3i activationAreaDimensions, BlockPos activationAreaPositionOffset, BlockPos accessPositionOffset, boolean setAccessPosition, String teleportationMode, BlockPos directTeleportPositionOffset, double directTeleportOrientationYaw, double directTeleportOrientationPitch, String locationType, List<Pair<String, String>> locationsList, boolean consumeKeyItemStack, String teleporterName, String currentTargetOwnerLabel, String currentTargetIdentifierLabel, String currentTargetEntranceLabel, String teleportButtonLabel, String cancelTeleportButtonLabel) {
        this.teleportBlockPosition = teleportBlockPosition;

        this.showActivationArea = showActivationArea;
        this.showAdventureScreen = showAdventureScreen;

        this.activationAreaDimensions = activationAreaDimensions;
        this.activationAreaPositionOffset = activationAreaPositionOffset;
        this.accessPositionOffset = accessPositionOffset;

        this.setAccessPosition = setAccessPosition;

        this.teleportationMode = TeleporterBlockBlockEntity.TeleportationMode.byName(teleportationMode).orElseGet(() -> TeleporterBlockBlockEntity.TeleportationMode.DIRECT);
        this.directTeleportPositionOffset = directTeleportPositionOffset;
        this.directTeleportOrientationYaw = directTeleportOrientationYaw;
        this.directTeleportOrientationPitch = directTeleportOrientationPitch;

        this.spawnPointType = TeleporterBlockBlockEntity.SpawnPointType.byName(locationType).orElseGet(() -> TeleporterBlockBlockEntity.SpawnPointType.WORLD_SPAWN);

        this.locationsList = locationsList;

        this.consumeKeyItemStack = consumeKeyItemStack;

        this.teleporterName = teleporterName;
        this.currentTargetOwnerLabel = currentTargetOwnerLabel;
        this.currentTargetIdentifierLabel = currentTargetIdentifierLabel;
        this.currentTargetEntranceLabel = currentTargetEntranceLabel;
        this.teleportButtonLabel = teleportButtonLabel;
        this.cancelTeleportButtonLabel = cancelTeleportButtonLabel;
    }

    public UpdateTeleporterBlockPacket(PacketByteBuf buf) {
        this(
                buf.readBlockPos(),
                buf.readBoolean(),
                buf.readBoolean(),
                new Vec3i(
                        buf.readInt(),
                        buf.readInt(),
                        buf.readInt()
                ),
                buf.readBlockPos(),
                buf.readBlockPos(),
                buf.readBoolean(),
                buf.readString(),
                buf.readBlockPos(),
                buf.readDouble(),
                buf.readDouble(),
                buf.readString(),
                buf.readList(new PacketByteBufUtils.PairStringStringListReader()),
                buf.readBoolean(),
                buf.readString(),
                buf.readString(),
                buf.readString(),
                buf.readString(),
                buf.readString(),
                buf.readString()
        );
    }
    @Override
    public PacketType<?> getType() {
        return TYPE;
    }
    @Override
    public void write(PacketByteBuf buf) {
        buf.writeBlockPos(this.teleportBlockPosition);

        buf.writeBoolean(this.showActivationArea);

        buf.writeBoolean(this.showAdventureScreen);

        buf.writeInt(this.activationAreaDimensions.getX());
        buf.writeInt(this.activationAreaDimensions.getY());
        buf.writeInt(this.activationAreaDimensions.getZ());
        buf.writeBlockPos(this.activationAreaPositionOffset);
        buf.writeBlockPos(this.accessPositionOffset);

        buf.writeBoolean(this.setAccessPosition);

        buf.writeString(this.teleportationMode.asString());

        buf.writeBlockPos(this.directTeleportPositionOffset);
        buf.writeDouble(this.directTeleportOrientationYaw);
        buf.writeDouble(this.directTeleportOrientationPitch);

        buf.writeString(this.spawnPointType.asString());

        buf.writeCollection(this.locationsList, new PacketByteBufUtils.PairStringStringListWriter());

        buf.writeBoolean(this.consumeKeyItemStack);

        buf.writeString(this.teleporterName);
        buf.writeString(this.currentTargetOwnerLabel);
        buf.writeString(this.currentTargetIdentifierLabel);
        buf.writeString(this.currentTargetEntranceLabel);
        buf.writeString(this.teleportButtonLabel);
        buf.writeString(this.cancelTeleportButtonLabel);
    }
}
