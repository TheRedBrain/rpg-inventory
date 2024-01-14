package com.github.theredbrain.betteradventuremode.network.packet;

import com.github.theredbrain.betteradventuremode.BetterAdventureMode;
import net.fabricmc.fabric.api.networking.v1.FabricPacket;
import net.fabricmc.fabric.api.networking.v1.PacketType;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;

public class UpdateStatusEffectApplierBlockPacket implements FabricPacket {
    public static final PacketType<UpdateStatusEffectApplierBlockPacket> TYPE = PacketType.create(
            BetterAdventureMode.identifier("update_status_effect_applier_block"),
            UpdateStatusEffectApplierBlockPacket::new
    );

    public final BlockPos statusEffectApplierBlockPosition;
    public final boolean triggered;
    public final boolean showApplicationArea;
    public final Vec3i applicationAreaDimensions;
    public final BlockPos applicationAreaPositionOffset;
    public final String appliedStatusEffectIdentifier;
    public final int appliedStatusEffectAmplifier;
    public final boolean appliedStatusEffectAmbient;
    public final boolean appliedStatusEffectShowParticles;
    public final boolean appliedStatusEffectShowIcon;

    public UpdateStatusEffectApplierBlockPacket(BlockPos statusEffectApplierBlockPosition, boolean triggered, boolean showApplicationArea, Vec3i applicationAreaDimensions, BlockPos applicationAreaPositionOffset, String appliedStatusEffectIdentifier, int appliedStatusEffectAmplifier, boolean appliedStatusEffectAmbient, boolean appliedStatusEffectShowParticles, boolean appliedStatusEffectShowIcon) {
        this.statusEffectApplierBlockPosition = statusEffectApplierBlockPosition;
        this.triggered = triggered;
        this.showApplicationArea = showApplicationArea;
        this.applicationAreaDimensions = applicationAreaDimensions;
        this.applicationAreaPositionOffset = applicationAreaPositionOffset;
        this.appliedStatusEffectIdentifier = appliedStatusEffectIdentifier;
        this.appliedStatusEffectAmplifier = appliedStatusEffectAmplifier;
        this.appliedStatusEffectAmbient = appliedStatusEffectAmbient;
        this.appliedStatusEffectShowParticles = appliedStatusEffectShowParticles;
        this.appliedStatusEffectShowIcon = appliedStatusEffectShowIcon;
    }

    public UpdateStatusEffectApplierBlockPacket(PacketByteBuf buf) {
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
                buf.readString(),
                buf.readInt(),
                buf.readBoolean(),
                buf.readBoolean(),
                buf.readBoolean()
        );
    }
    @Override
    public PacketType<?> getType() {
        return TYPE;
    }
    @Override
    public void write(PacketByteBuf buf) {
        buf.writeBlockPos(this.statusEffectApplierBlockPosition);
        buf.writeBoolean(this.triggered);
        buf.writeBoolean(this.showApplicationArea);
        buf.writeInt(this.applicationAreaDimensions.getX());
        buf.writeInt(this.applicationAreaDimensions.getY());
        buf.writeInt(this.applicationAreaDimensions.getZ());
        buf.writeBlockPos(this.applicationAreaPositionOffset);
        buf.writeString(this.appliedStatusEffectIdentifier);
        buf.writeInt(this.appliedStatusEffectAmplifier);
        buf.writeBoolean(this.appliedStatusEffectAmbient);
        buf.writeBoolean(this.appliedStatusEffectShowParticles);
        buf.writeBoolean(this.appliedStatusEffectShowIcon);
    }

}
