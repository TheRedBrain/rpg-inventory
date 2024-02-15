package com.github.theredbrain.betteradventuremode.network.packet;

import com.github.theredbrain.betteradventuremode.BetterAdventureMode;
import net.fabricmc.fabric.api.networking.v1.FabricPacket;
import net.fabricmc.fabric.api.networking.v1.PacketType;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.math.BlockPos;

public class UpdateRedstoneTriggerBlockPacket implements FabricPacket {
    public static final PacketType<UpdateRedstoneTriggerBlockPacket> TYPE = PacketType.create(
            BetterAdventureMode.identifier("update_redstone_trigger_block"),
            UpdateRedstoneTriggerBlockPacket::new
    );

    public final BlockPos redstoneTriggerBlockPosition;

    public final BlockPos triggeredBlockPositionOffset;

    public final boolean triggeredBlockResets;

    public UpdateRedstoneTriggerBlockPacket(BlockPos redstoneTriggerBlockPosition, BlockPos triggeredBlockPositionOffset, boolean triggeredBlockResets) {
        this.redstoneTriggerBlockPosition = redstoneTriggerBlockPosition;
        this.triggeredBlockPositionOffset = triggeredBlockPositionOffset;
        this.triggeredBlockResets = triggeredBlockResets;
    }

    public UpdateRedstoneTriggerBlockPacket(PacketByteBuf buf) {
        this(buf.readBlockPos(), buf.readBlockPos(), buf.readBoolean());
    }
    @Override
    public PacketType<?> getType() {
        return TYPE;
    }
    @Override
    public void write(PacketByteBuf buf) {
        buf.writeBlockPos(this.redstoneTriggerBlockPosition);
        buf.writeBlockPos(this.triggeredBlockPositionOffset);
        buf.writeBoolean(this.triggeredBlockResets);
    }

}
