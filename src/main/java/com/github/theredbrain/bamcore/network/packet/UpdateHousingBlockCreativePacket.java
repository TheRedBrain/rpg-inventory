package com.github.theredbrain.bamcore.network.packet;

import com.github.theredbrain.bamcore.BetterAdventureModeCore;
import com.github.theredbrain.bamcore.block.entity.HousingBlockBlockEntity;
import net.fabricmc.fabric.api.networking.v1.FabricPacket;
import net.fabricmc.fabric.api.networking.v1.PacketType;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;

public class UpdateHousingBlockCreativePacket implements FabricPacket {
    public static final PacketType<UpdateHousingBlockCreativePacket> TYPE = PacketType.create(
            BetterAdventureModeCore.identifier("update_housing_block_creative"),
            UpdateHousingBlockCreativePacket::new
    );

    public final BlockPos housingBlockPosition;
    public final boolean showRestrictBlockBreakingArea;
    public final Vec3i restrictBlockBreakingAreaDimensions;
    public final BlockPos restrictBlockBreakingAreaPositionOffset;
    public final BlockPos triggeredBlockPositionOffset;
    public final HousingBlockBlockEntity.OwnerMode ownerMode;

    public UpdateHousingBlockCreativePacket(BlockPos housingBlockPosition, boolean showRestrictBlockBreakingArea, Vec3i restrictBlockBreakingAreaDimensions, BlockPos restrictBlockBreakingAreaPositionOffset, BlockPos triggeredBlockPositionOffset, String ownerMode) {
        this.housingBlockPosition = housingBlockPosition;
        this.showRestrictBlockBreakingArea = showRestrictBlockBreakingArea;
        this.restrictBlockBreakingAreaDimensions = restrictBlockBreakingAreaDimensions;
        this.restrictBlockBreakingAreaPositionOffset = restrictBlockBreakingAreaPositionOffset;
        this.triggeredBlockPositionOffset = triggeredBlockPositionOffset;
        this.ownerMode = HousingBlockBlockEntity.OwnerMode.byName(ownerMode).orElseGet(() -> HousingBlockBlockEntity.OwnerMode.DIMENSION_OWNER);
    }

    public UpdateHousingBlockCreativePacket(PacketByteBuf buf) {
        this(
                buf.readBlockPos(),
                buf.readBoolean(),
                new Vec3i(
                        buf.readInt(),
                        buf.readInt(),
                        buf.readInt()
                ),
                buf.readBlockPos(),
                buf.readBlockPos(),
                buf.readString()
        );
    }
    @Override
    public PacketType<?> getType() {
        return TYPE;
    }
    @Override
    public void write(PacketByteBuf buf) {
        buf.writeBlockPos(this.housingBlockPosition);
        buf.writeBoolean(this.showRestrictBlockBreakingArea);
        buf.writeInt(this.restrictBlockBreakingAreaDimensions.getX());
        buf.writeInt(this.restrictBlockBreakingAreaDimensions.getY());
        buf.writeInt(this.restrictBlockBreakingAreaDimensions.getZ());
        buf.writeBlockPos(this.restrictBlockBreakingAreaPositionOffset);
        buf.writeBlockPos(this.triggeredBlockPositionOffset);
        buf.writeString(this.ownerMode.asString());
    }

}
