package com.github.theredbrain.betteradventuremode.network.packet;

import com.github.theredbrain.betteradventuremode.BetterAdventureMode;
import com.github.theredbrain.betteradventuremode.block.entity.AreaBlockEntity;
import net.fabricmc.fabric.api.networking.v1.FabricPacket;
import net.fabricmc.fabric.api.networking.v1.PacketType;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;

public class UpdateAreaBlockPacket implements FabricPacket {
    public static final PacketType<UpdateAreaBlockPacket> TYPE = PacketType.create(
            BetterAdventureMode.identifier("update_area_block"),
            UpdateAreaBlockPacket::new
    );

    public final BlockPos areaBlockPosition;

    public final boolean showArea;
    public final Vec3i applicationAreaDimensions;
    public final BlockPos applicationAreaPositionOffset;

    public final String appliedStatusEffectIdentifier;
    public final int appliedStatusEffectAmplifier;
    public final boolean appliedStatusEffectAmbient;
    public final boolean appliedStatusEffectShowParticles;
    public final boolean appliedStatusEffectShowIcon;


    public final BlockPos triggeredBlockPositionOffset;

    public final boolean triggeredBlockResets;
    public final boolean wasTriggered;

    public final String joinMessage;
    public final String leaveMessage;
    public final String triggeredMessage;
    public final AreaBlockEntity.MessageMode messageMode;
    public final AreaBlockEntity.TriggerMode triggerMode;
    public final AreaBlockEntity.TriggeredMode triggeredMode;
    public final int timer;

    public UpdateAreaBlockPacket(BlockPos areaBlockPosition, boolean showArea, Vec3i applicationAreaDimensions, BlockPos applicationAreaPositionOffset, String appliedStatusEffectIdentifier, int appliedStatusEffectAmplifier, boolean appliedStatusEffectAmbient, boolean appliedStatusEffectShowParticles, boolean appliedStatusEffectShowIcon, BlockPos triggeredBlockPositionOffset, boolean triggeredBlockResets, boolean wasTriggered, String joinMessage, String leaveMessage, String triggeredMessage, String messageMode, String triggerMode, String triggeredMode, int timer) {
        this.areaBlockPosition = areaBlockPosition;
        this.showArea = showArea;
        this.applicationAreaDimensions = applicationAreaDimensions;
        this.applicationAreaPositionOffset = applicationAreaPositionOffset;
        this.appliedStatusEffectIdentifier = appliedStatusEffectIdentifier;
        this.appliedStatusEffectAmplifier = appliedStatusEffectAmplifier;
        this.appliedStatusEffectAmbient = appliedStatusEffectAmbient;
        this.appliedStatusEffectShowParticles = appliedStatusEffectShowParticles;
        this.appliedStatusEffectShowIcon = appliedStatusEffectShowIcon;
        this.triggeredBlockPositionOffset = triggeredBlockPositionOffset;
        this.triggeredBlockResets = triggeredBlockResets;
        this.wasTriggered = wasTriggered;
        this.joinMessage = joinMessage;
        this.leaveMessage = leaveMessage;
        this.triggeredMessage = triggeredMessage;
        this.messageMode = AreaBlockEntity.MessageMode.byName(messageMode).orElse(AreaBlockEntity.MessageMode.OVERLAY);
        this.triggerMode = AreaBlockEntity.TriggerMode.byName(triggerMode).orElse(AreaBlockEntity.TriggerMode.ALWAYS);
        this.triggeredMode = AreaBlockEntity.TriggeredMode.byName(triggeredMode).orElse(AreaBlockEntity.TriggeredMode.ONCE);
        this.timer = timer;
    }

    public UpdateAreaBlockPacket(PacketByteBuf buf) {
        this(
                buf.readBlockPos(),
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
                buf.readBoolean(),
                buf.readBlockPos(),
                buf.readBoolean(),
                buf.readBoolean(),
                buf.readString(),
                buf.readString(),
                buf.readString(),
                buf.readString(),
                buf.readString(),
                buf.readString(),
                buf.readInt()
        );
    }
    @Override
    public PacketType<?> getType() {
        return TYPE;
    }
    @Override
    public void write(PacketByteBuf buf) {
        buf.writeBlockPos(this.areaBlockPosition);
        buf.writeBoolean(this.showArea);
        buf.writeInt(this.applicationAreaDimensions.getX());
        buf.writeInt(this.applicationAreaDimensions.getY());
        buf.writeInt(this.applicationAreaDimensions.getZ());
        buf.writeBlockPos(this.applicationAreaPositionOffset);
        buf.writeString(this.appliedStatusEffectIdentifier);
        buf.writeInt(this.appliedStatusEffectAmplifier);
        buf.writeBoolean(this.appliedStatusEffectAmbient);
        buf.writeBoolean(this.appliedStatusEffectShowParticles);
        buf.writeBoolean(this.appliedStatusEffectShowIcon);
        buf.writeBlockPos(this.triggeredBlockPositionOffset);
        buf.writeBoolean(this.triggeredBlockResets);
        buf.writeBoolean(this.wasTriggered);
        buf.writeString(this.joinMessage);
        buf.writeString(this.leaveMessage);
        buf.writeString(this.triggeredMessage);
        buf.writeString(this.messageMode.asString());
        buf.writeString(this.triggerMode.asString());
        buf.writeString(this.triggeredMode.asString());
        buf.writeInt(this.timer);
    }

}
