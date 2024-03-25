package com.github.theredbrain.betteradventuremode.network.packet;

import com.github.theredbrain.betteradventuremode.BetterAdventureMode;
import com.github.theredbrain.betteradventuremode.block.entity.RelayTriggerBlockEntity;
import com.github.theredbrain.betteradventuremode.util.PacketByteBufUtils;
import net.fabricmc.fabric.api.networking.v1.FabricPacket;
import net.fabricmc.fabric.api.networking.v1.PacketType;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import org.apache.commons.lang3.tuple.MutablePair;

import java.util.List;

public class UpdateRelayTriggerBlockPacket implements FabricPacket {
    public static final PacketType<UpdateRelayTriggerBlockPacket> TYPE = PacketType.create(
            BetterAdventureMode.identifier("update_relay_trigger_block"),
            UpdateRelayTriggerBlockPacket::new
    );

    public final BlockPos relayTriggerBlockPosition;
    public final RelayTriggerBlockEntity.SelectionMode selectionMode;
    public final boolean showArea;
    public final boolean resetsArea;
    public final Vec3i areaDimensions;
    public final BlockPos areaPositionOffset;

    public final List<MutablePair<MutablePair<BlockPos, Boolean>, Integer>> triggeredBlocks;

    public final RelayTriggerBlockEntity.TriggerMode triggerMode;

    public final int triggerAmount;

    public UpdateRelayTriggerBlockPacket(BlockPos relayTriggerBlockPosition, String selectionMode, boolean showArea, boolean resetsArea, Vec3i areaDimensions, BlockPos areaPositionOffset, List<MutablePair<MutablePair<BlockPos, Boolean>, Integer>> triggeredBlocks, String triggerMode, int triggerAmount) {
        this.relayTriggerBlockPosition = relayTriggerBlockPosition;
        this.selectionMode = RelayTriggerBlockEntity.SelectionMode.byName(selectionMode).orElseGet(() -> RelayTriggerBlockEntity.SelectionMode.LIST);
        this.showArea = showArea;
        this.resetsArea = resetsArea;
        this.areaDimensions = areaDimensions;
        this.areaPositionOffset = areaPositionOffset;
        this.triggeredBlocks = triggeredBlocks;
        this.triggerMode = RelayTriggerBlockEntity.TriggerMode.byName(triggerMode).orElseGet(() -> RelayTriggerBlockEntity.TriggerMode.NORMAL);
        this.triggerAmount = triggerAmount;
    }

    public UpdateRelayTriggerBlockPacket(PacketByteBuf buf) {
        this(
                buf.readBlockPos(),
                buf.readString(),
                buf.readBoolean(),
                buf.readBoolean(),
                new Vec3i(
                        buf.readInt(),
                        buf.readInt(),
                        buf.readInt()
                ),
                buf.readBlockPos(),
                buf.readList(new PacketByteBufUtils.MutablePairMutablePairBlockPosBooleanIntegerReader()),
                buf.readString(),
                buf.readInt());
    }
    @Override
    public PacketType<?> getType() {
        return TYPE;
    }
    @Override
    public void write(PacketByteBuf buf) {
        buf.writeBlockPos(this.relayTriggerBlockPosition);
        buf.writeString(this.selectionMode.asString());
        buf.writeBoolean(this.showArea);
        buf.writeBoolean(this.resetsArea);

        buf.writeInt(this.areaDimensions.getX());
        buf.writeInt(this.areaDimensions.getY());
        buf.writeInt(this.areaDimensions.getZ());
        buf.writeBlockPos(this.areaPositionOffset);

        buf.writeCollection(this.triggeredBlocks, new PacketByteBufUtils.MutablePairMutablePairBlockPosBooleanIntegerWriter());
        buf.writeString(this.triggerMode.asString());
        buf.writeInt(this.triggerAmount);
    }

}
