package com.github.theredbrain.bamcore.network.packet;

import com.github.theredbrain.bamcore.BetterAdventureMode;
import com.github.theredbrain.bamcore.api.util.PacketByteBufUtils;
import net.fabricmc.fabric.api.networking.v1.FabricPacket;
import net.fabricmc.fabric.api.networking.v1.PacketType;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.math.BlockPos;

import java.util.List;

public class UpdateRelayTriggerBlockPacket implements FabricPacket {
    public static final PacketType<UpdateRelayTriggerBlockPacket> TYPE = PacketType.create(
            BetterAdventureMode.identifier("update_relay_trigger_block"),
            UpdateRelayTriggerBlockPacket::new
    );

    public final BlockPos relayTriggerBlockPosition;

    public final List<BlockPos> triggeredBlocks;

    public UpdateRelayTriggerBlockPacket(BlockPos relayTriggerBlockPosition, List<BlockPos> triggeredBlocks) {
        this.relayTriggerBlockPosition = relayTriggerBlockPosition;
        this.triggeredBlocks = triggeredBlocks;
    }

    public UpdateRelayTriggerBlockPacket(PacketByteBuf buf) {
        this(buf.readBlockPos(), buf.readList(new PacketByteBufUtils.BlockPosReader()));
    }
    @Override
    public PacketType<?> getType() {
        return TYPE;
    }
    @Override
    public void write(PacketByteBuf buf) {
        buf.writeBlockPos(this.relayTriggerBlockPosition);
        buf.writeCollection(this.triggeredBlocks, new PacketByteBufUtils.BlockPosWriter());
    }

}
