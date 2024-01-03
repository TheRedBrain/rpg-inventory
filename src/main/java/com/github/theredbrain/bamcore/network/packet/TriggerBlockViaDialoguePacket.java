package com.github.theredbrain.bamcore.network.packet;

import com.github.theredbrain.bamcore.BetterAdventureMode;
import net.fabricmc.fabric.api.networking.v1.FabricPacket;
import net.fabricmc.fabric.api.networking.v1.PacketType;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.math.BlockPos;

public class TriggerBlockViaDialoguePacket implements FabricPacket {
    public static final PacketType<TriggerBlockViaDialoguePacket> TYPE = PacketType.create(
            BetterAdventureMode.identifier("trigger_block_via_dialogue"),
            TriggerBlockViaDialoguePacket::new
    );

    public final BlockPos blockPos;

    public TriggerBlockViaDialoguePacket(BlockPos blockPos) {
        this.blockPos = blockPos;
    }

    public TriggerBlockViaDialoguePacket(PacketByteBuf buf) {
        this(buf.readBlockPos());
    }
    @Override
    public PacketType<?> getType() {
        return TYPE;
    }
    @Override
    public void write(PacketByteBuf buf) {
        buf.writeBlockPos(this.blockPos);
    }

}
