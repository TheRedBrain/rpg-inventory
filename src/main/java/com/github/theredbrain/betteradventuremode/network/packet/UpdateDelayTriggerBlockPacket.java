package com.github.theredbrain.betteradventuremode.network.packet;

import com.github.theredbrain.betteradventuremode.BetterAdventureMode;
import net.fabricmc.fabric.api.networking.v1.FabricPacket;
import net.fabricmc.fabric.api.networking.v1.PacketType;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.math.BlockPos;

public class UpdateDelayTriggerBlockPacket implements FabricPacket {
    public static final PacketType<UpdateDelayTriggerBlockPacket> TYPE = PacketType.create(
            BetterAdventureMode.identifier("update_delay_trigger_block"),
            UpdateDelayTriggerBlockPacket::new
    );

    public final BlockPos delayTriggerBlockPosition;
    public final BlockPos triggeredBlockPositionOffset;
    public final int triggerDelay;

    public UpdateDelayTriggerBlockPacket(BlockPos delayTriggerBlockPosition, BlockPos triggeredBlockPositionOffset, int triggerDelay) {
        this.delayTriggerBlockPosition = delayTriggerBlockPosition;
        this.triggeredBlockPositionOffset = triggeredBlockPositionOffset;
        this.triggerDelay = triggerDelay;
    }

    public UpdateDelayTriggerBlockPacket(PacketByteBuf buf) {
        this(buf.readBlockPos(), buf.readBlockPos(), buf.readInt());
    }
    @Override
    public PacketType<?> getType() {
        return TYPE;
    }
    @Override
    public void write(PacketByteBuf buf) {
        buf.writeBlockPos(this.delayTriggerBlockPosition);
        buf.writeBlockPos(this.triggeredBlockPositionOffset);
        buf.writeInt(this.triggerDelay);
    }

}
