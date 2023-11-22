package com.github.theredbrain.bamcore.network.packet;

import com.github.theredbrain.bamcore.BetterAdventureModeCore;
import com.github.theredbrain.bamcore.api.util.PacketByteBufUtils;
import net.fabricmc.fabric.api.networking.v1.FabricPacket;
import net.fabricmc.fabric.api.networking.v1.PacketType;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Pair;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;

import java.util.ArrayList;
import java.util.List;

public class UpdateTeleporterBlockPacket implements FabricPacket {
    public static final PacketType<UpdateTeleporterBlockPacket> TYPE = PacketType.create(
            BetterAdventureModeCore.identifier("update_teleporter_block"),
            UpdateTeleporterBlockPacket::new
    );

    public final BlockPos teleportBlockPosition;

    public final String teleporterName;

    public final boolean showActivationArea;
    public final Vec3i activationAreaDimensions;
    public final BlockPos activationAreaPositionOffset;

    public final boolean showAdventureScreen;

    public final int teleportationMode;

    public final BlockPos directTeleportPositionOffset;
    public final double directTeleportPositionOffsetYaw;
    public final double directTeleportPositionOffsetPitch;

    public final int specificLocationType;

    public final List<Pair<String, String>> locationsList;

    public final boolean consumeKeyItemStack;
    public final String teleportButtonLabel;
    public final String cancelTeleportButtonLabel;

    public UpdateTeleporterBlockPacket(BlockPos teleportBlockPosition, String teleporterName, boolean showActivationArea, Vec3i activationAreaDimensions, BlockPos activationAreaPositionOffset, boolean showAdventureScreen, int teleportationMode, BlockPos directTeleportPositionOffset, double directTeleportPositionOffsetYaw, double directTeleportPositionOffsetPitch, int specificLocationType, List<Pair<String, String>> locationsList, boolean consumeKeyItemStack, String teleportButtonLabel, String cancelTeleportButtonLabel) {
        this.teleportBlockPosition = teleportBlockPosition;
        this.teleporterName = teleporterName;

        this.showActivationArea = showActivationArea;

        this.activationAreaDimensions = activationAreaDimensions;
        this.activationAreaPositionOffset = activationAreaPositionOffset;

        this.showAdventureScreen = showAdventureScreen;
        this.teleportationMode = teleportationMode;
        this.directTeleportPositionOffset = directTeleportPositionOffset;
        this.directTeleportPositionOffsetYaw = directTeleportPositionOffsetYaw;
        this.directTeleportPositionOffsetPitch = directTeleportPositionOffsetPitch;

        this.specificLocationType = specificLocationType;

        this.locationsList = locationsList;

        this.consumeKeyItemStack = consumeKeyItemStack;
        this.teleportButtonLabel = teleportButtonLabel;
        this.cancelTeleportButtonLabel = cancelTeleportButtonLabel;
    }

    public UpdateTeleporterBlockPacket(PacketByteBuf buf) {
        this(
                buf.readBlockPos(),
                buf.readString(),
                buf.readBoolean(),
                new Vec3i(
                        buf.readInt(),
                        buf.readInt(),
                        buf.readInt()
                ),
                buf.readBlockPos(),
                buf.readBoolean(),
                buf.readInt(),
                buf.readBlockPos(),
                buf.readDouble(),
                buf.readDouble(),
                buf.readInt(),
                buf.readList(new PacketByteBufUtils.PairStringStringListReader()),
                buf.readBoolean(),
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
        buf.writeString(this.teleporterName);
        buf.writeBoolean(this.showActivationArea);

        buf.writeInt(this.activationAreaDimensions.getX());
        buf.writeInt(this.activationAreaDimensions.getY());
        buf.writeInt(this.activationAreaDimensions.getZ());
        buf.writeBlockPos(this.activationAreaPositionOffset);

        buf.writeBoolean(this.showAdventureScreen);

        buf.writeInt(this.teleportationMode);

        buf.writeBlockPos(this.directTeleportPositionOffset);
        buf.writeDouble(this.directTeleportPositionOffsetYaw);
        buf.writeDouble(this.directTeleportPositionOffsetPitch);

        buf.writeInt(this.specificLocationType);

        buf.writeCollection(this.locationsList, new PacketByteBufUtils.PairStringStringListWriter());

        buf.writeBoolean(this.consumeKeyItemStack);
        buf.writeString(this.teleportButtonLabel);
        buf.writeString(this.cancelTeleportButtonLabel);
    }
}
