package com.github.theredbrain.betteradventuremode.network.packet;

import com.github.theredbrain.betteradventuremode.BetterAdventureMode;
import net.fabricmc.fabric.api.networking.v1.FabricPacket;
import net.fabricmc.fabric.api.networking.v1.PacketType;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.math.BlockPos;

public class SetManualResetLocationControlBlockPacket implements FabricPacket {
    public static final PacketType<SetManualResetLocationControlBlockPacket> TYPE = PacketType.create(
            BetterAdventureMode.identifier("set_manual_reset_location_control_block"),
            SetManualResetLocationControlBlockPacket::new
    );

    public final BlockPos locationControlBlockPosition;

    public final boolean manualReset;

    public SetManualResetLocationControlBlockPacket(BlockPos locationControlBlockPosition, boolean manualReset) {
        this.locationControlBlockPosition = locationControlBlockPosition;
        this.manualReset = manualReset;
    }

    public SetManualResetLocationControlBlockPacket(PacketByteBuf buf) {
        this(buf.readBlockPos(), buf.readBoolean());
    }
    @Override
    public PacketType<?> getType() {
        return TYPE;
    }
    @Override
    public void write(PacketByteBuf buf) {
        buf.writeBlockPos(this.locationControlBlockPosition);
        buf.writeBoolean(this.manualReset);
    }

}
