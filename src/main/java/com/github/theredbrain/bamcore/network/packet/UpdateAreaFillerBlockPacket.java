package com.github.theredbrain.bamcore.network.packet;

import com.github.theredbrain.bamcore.BetterAdventureModeCore;
import net.fabricmc.fabric.api.networking.v1.FabricPacket;
import net.fabricmc.fabric.api.networking.v1.PacketType;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;

public class UpdateAreaFillerBlockPacket implements FabricPacket {
    public static final PacketType<UpdateAreaFillerBlockPacket> TYPE = PacketType.create(
            BetterAdventureModeCore.identifier("update_area_filler_block"),
            UpdateAreaFillerBlockPacket::new
    );

    public final BlockPos areaFillerBlockPosition;
    public final String fillerBlockIdentifier;
    public final Vec3i filledAreaDimensions;
    public final BlockPos filledAreaPositionOffset;
    public final BlockPos triggeredBlockPositionOffset;

    public UpdateAreaFillerBlockPacket(BlockPos areaFillerBlockPosition, String fillerBlockIdentifier, Vec3i filledAreaDimensions, BlockPos filledAreaPositionOffset, BlockPos triggeredBlockPositionOffset) {
        this.areaFillerBlockPosition = areaFillerBlockPosition;
        this.fillerBlockIdentifier = fillerBlockIdentifier;
        this.filledAreaDimensions = filledAreaDimensions;
        this.filledAreaPositionOffset = filledAreaPositionOffset;
        this.triggeredBlockPositionOffset = triggeredBlockPositionOffset;
    }

    public UpdateAreaFillerBlockPacket(PacketByteBuf buf) {
        this(buf.readBlockPos(), buf.readString(), new Vec3i(buf.readInt(), buf.readInt(), buf.readInt()), buf.readBlockPos(), buf.readBlockPos());
    }
    @Override
    public PacketType<?> getType() {
        return TYPE;
    }
    @Override
    public void write(PacketByteBuf buf) {
        buf.writeBlockPos(this.areaFillerBlockPosition);
        buf.writeString(this.fillerBlockIdentifier);
        buf.writeInt(this.filledAreaDimensions.getX());
        buf.writeInt(this.filledAreaDimensions.getY());
        buf.writeInt(this.filledAreaDimensions.getZ());
        buf.writeBlockPos(this.filledAreaPositionOffset);
        buf.writeBlockPos(this.triggeredBlockPositionOffset);
    }

}
