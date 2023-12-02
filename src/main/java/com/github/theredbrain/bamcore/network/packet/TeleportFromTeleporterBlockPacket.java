package com.github.theredbrain.bamcore.network.packet;

import com.github.theredbrain.bamcore.BetterAdventureModeCore;
import com.github.theredbrain.bamcore.block.entity.TeleporterBlockBlockEntity;
import net.fabricmc.fabric.api.networking.v1.FabricPacket;
import net.fabricmc.fabric.api.networking.v1.PacketType;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.math.BlockPos;

public class TeleportFromTeleporterBlockPacket implements FabricPacket {
    public static final PacketType<TeleportFromTeleporterBlockPacket> TYPE = PacketType.create(
            BetterAdventureModeCore.identifier("teleport_from_teleporter_block"),
            TeleportFromTeleporterBlockPacket::new
    );

    public final BlockPos teleportBlockPosition;

    public final TeleporterBlockBlockEntity.TeleportationMode teleportationMode;

    public final BlockPos directTeleportPositionOffset;
    public final double directTeleportOrientationYaw;
    public final double directTeleportOrientationPitch;

    public final TeleporterBlockBlockEntity.LocationType locationType;

    public final String targetDimensionOwnerName;
    public final String targetLocation;
    public final String targetLocationEntrance;

    public TeleportFromTeleporterBlockPacket(BlockPos teleportBlockPosition, String teleportationMode, BlockPos directTeleportPositionOffset, double directTeleportOrientationYaw, double directTeleportOrientationPitch, String locationType, String targetDimensionOwnerName, String targetLocation, String targetLocationEntrance) {
        this.teleportBlockPosition = teleportBlockPosition;
        this.teleportationMode = TeleporterBlockBlockEntity.TeleportationMode.byName(teleportationMode).orElseGet(() -> TeleporterBlockBlockEntity.TeleportationMode.DIRECT);
        this.directTeleportPositionOffset = directTeleportPositionOffset;
        this.directTeleportOrientationYaw = directTeleportOrientationYaw;
        this.directTeleportOrientationPitch = directTeleportOrientationPitch;
        this.locationType = TeleporterBlockBlockEntity.LocationType.byName(locationType).orElseGet(() -> TeleporterBlockBlockEntity.LocationType.WORLD_SPAWN);
        this.targetDimensionOwnerName = targetDimensionOwnerName;
        this.targetLocation = targetLocation;
        this.targetLocationEntrance = targetLocationEntrance;
    }

    public TeleportFromTeleporterBlockPacket(PacketByteBuf buf) {
        this(buf.readBlockPos(), buf.readString(), buf.readBlockPos(), buf.readDouble(), buf.readDouble(), buf.readString(), buf.readString(), buf.readString(), buf.readString());
    }
    @Override
    public PacketType<?> getType() {
        return TYPE;
    }
    @Override
    public void write(PacketByteBuf buf) {
        buf.writeBlockPos(this.teleportBlockPosition);
        buf.writeString(this.teleportationMode.asString());
        buf.writeBlockPos(this.directTeleportPositionOffset);
        buf.writeDouble(this.directTeleportOrientationYaw);
        buf.writeDouble(this.directTeleportOrientationPitch);
        buf.writeString(this.locationType.asString());
        buf.writeString(this.targetDimensionOwnerName);
        buf.writeString(this.targetLocation);
        buf.writeString(this.targetLocationEntrance);
    }
}
