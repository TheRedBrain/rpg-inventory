package com.github.theredbrain.betteradventuremode.network.packet;

import com.github.theredbrain.betteradventuremode.BetterAdventureMode;
import com.github.theredbrain.betteradventuremode.block.entity.RelayTriggerBlockEntity;
import com.github.theredbrain.betteradventuremode.block.entity.TriggeredSpawnerBlockEntity;
import com.github.theredbrain.betteradventuremode.util.PacketByteBufUtils;
import net.fabricmc.fabric.api.networking.v1.FabricPacket;
import net.fabricmc.fabric.api.networking.v1.PacketType;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.math.BlockPos;
import org.apache.commons.lang3.tuple.MutablePair;

import java.util.List;

public class UpdateRelayTriggerBlockPacket implements FabricPacket {
    public static final PacketType<UpdateRelayTriggerBlockPacket> TYPE = PacketType.create(
            BetterAdventureMode.identifier("update_relay_trigger_block"),
            UpdateRelayTriggerBlockPacket::new
    );

    public final BlockPos relayTriggerBlockPosition;

    public final List<MutablePair<MutablePair<BlockPos, Boolean>, Integer>> triggeredBlocks;

    public final RelayTriggerBlockEntity.TriggerMode triggerMode;

    public final int triggerAmount;

    public UpdateRelayTriggerBlockPacket(BlockPos relayTriggerBlockPosition, List<MutablePair<MutablePair<BlockPos, Boolean>, Integer>> triggeredBlocks, String triggerMode, int triggerAmount) {
        this.relayTriggerBlockPosition = relayTriggerBlockPosition;
        this.triggeredBlocks = triggeredBlocks;
        this.triggerMode = RelayTriggerBlockEntity.TriggerMode.byName(triggerMode).orElseGet(() -> RelayTriggerBlockEntity.TriggerMode.NORMAL);
        this.triggerAmount = triggerAmount;
    }

    public UpdateRelayTriggerBlockPacket(PacketByteBuf buf) {
        this(buf.readBlockPos(), buf.readList(new PacketByteBufUtils.MutablePairMutablePairBlockPosBooleanIntegerReader()), buf.readString(), buf.readInt());
    }
    @Override
    public PacketType<?> getType() {
        return TYPE;
    }
    @Override
    public void write(PacketByteBuf buf) {
        buf.writeBlockPos(this.relayTriggerBlockPosition);
        buf.writeCollection(this.triggeredBlocks, new PacketByteBufUtils.MutablePairMutablePairBlockPosBooleanIntegerWriter());
        buf.writeString(this.triggerMode.asString());
        buf.writeInt(this.triggerAmount);
    }

}
