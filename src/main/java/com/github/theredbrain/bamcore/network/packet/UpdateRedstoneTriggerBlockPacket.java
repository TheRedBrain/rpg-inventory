package com.github.theredbrain.bamcore.network.packet;

import com.github.theredbrain.bamcore.BetterAdventureModeCore;
import net.fabricmc.fabric.api.networking.v1.FabricPacket;
import net.fabricmc.fabric.api.networking.v1.PacketType;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.math.BlockPos;

public class UpdateRedstoneTriggerBlockPacket implements FabricPacket {
    public static final PacketType<UpdateRedstoneTriggerBlockPacket> TYPE = PacketType.create(
            BetterAdventureModeCore.identifier("update_redstone_trigger_block"),
            UpdateRedstoneTriggerBlockPacket::new
    );

    public final BlockPos redstoneTriggerBlockPosition;

    public final BlockPos triggeredBlockPositionOffset;

    public UpdateRedstoneTriggerBlockPacket(BlockPos redstoneTriggerBlockPosition, BlockPos triggeredBlockPositionOffset) {
        this.redstoneTriggerBlockPosition = redstoneTriggerBlockPosition;
        this.triggeredBlockPositionOffset = triggeredBlockPositionOffset;
    }

    public UpdateRedstoneTriggerBlockPacket(PacketByteBuf buf) {
        this(buf.readBlockPos(), buf.readBlockPos());
    }
    @Override
    public PacketType<?> getType() {
        return TYPE;
    }
    @Override
    public void write(PacketByteBuf buf) {
        buf.writeBlockPos(this.redstoneTriggerBlockPosition);
        buf.writeBlockPos(this.triggeredBlockPositionOffset);
    }

}
