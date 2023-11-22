package com.github.theredbrain.bamcore.network.packet;

import com.github.theredbrain.bamcore.BetterAdventureModeCore;
import net.fabricmc.fabric.api.networking.v1.FabricPacket;
import net.fabricmc.fabric.api.networking.v1.PacketType;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

public class TeleportFromTeleporterBlockPacket implements FabricPacket {
    public static final PacketType<TeleportFromTeleporterBlockPacket> TYPE = PacketType.create(
            BetterAdventureModeCore.identifier("teleport_from_teleporter_block"),
            TeleportFromTeleporterBlockPacket::new
    );

    public final BlockPos teleportBlockPosition;

    public final int teleportationMode;

    public final BlockPos directTeleportPositionOffset;
    public final double directTeleportPositionOffsetYaw;
    public final double directTeleportPositionOffsetPitch;

    public final int specificLocationType;

    public final String targetDimensionOwnerName;
    public final String targetLocation;
    public final String targetLocationEntrance;

    public TeleportFromTeleporterBlockPacket(BlockPos teleportBlockPosition, int teleportationMode, BlockPos directTeleportPositionOffset, double directTeleportPositionOffsetYaw, double directTeleportPositionOffsetPitch, int specificLocationType, String targetDimensionOwnerName, String targetLocation, String targetLocationEntrance) {
        this.teleportBlockPosition = teleportBlockPosition;
        this.teleportationMode = teleportationMode;
        this.directTeleportPositionOffset = directTeleportPositionOffset;
        this.directTeleportPositionOffsetYaw = directTeleportPositionOffsetYaw;
        this.directTeleportPositionOffsetPitch = directTeleportPositionOffsetPitch;
        this.specificLocationType = specificLocationType;
        this.targetDimensionOwnerName = targetDimensionOwnerName;
        this.targetLocation = targetLocation;
        this.targetLocationEntrance = targetLocationEntrance;
    }

    public TeleportFromTeleporterBlockPacket(PacketByteBuf buf) {
        this(buf.readBlockPos(), buf.readInt(), buf.readBlockPos(), buf.readDouble(), buf.readDouble(), buf.readInt(), buf.readString(), buf.readString(), buf.readString());
    }
    @Override
    public PacketType<?> getType() {
        return TYPE;
    }
    @Override
    public void write(PacketByteBuf buf) {
        buf.writeBlockPos(this.teleportBlockPosition);
        buf.writeInt(this.teleportationMode);
        buf.writeBlockPos(this.directTeleportPositionOffset);
        buf.writeDouble(this.directTeleportPositionOffsetYaw);
        buf.writeDouble(this.directTeleportPositionOffsetPitch);
        buf.writeInt(this.specificLocationType);
        buf.writeString(this.targetDimensionOwnerName);
        buf.writeString(this.targetLocation);
        buf.writeString(this.targetLocationEntrance);
    }
}
