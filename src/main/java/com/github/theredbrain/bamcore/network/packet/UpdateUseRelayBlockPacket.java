package com.github.theredbrain.bamcore.network.packet;

import com.github.theredbrain.bamcore.BetterAdventureModeCore;
import net.fabricmc.fabric.api.networking.v1.FabricPacket;
import net.fabricmc.fabric.api.networking.v1.PacketType;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.math.BlockPos;

public class UpdateUseRelayBlockPacket implements FabricPacket {
    public static final PacketType<UpdateUseRelayBlockPacket> TYPE = PacketType.create(
            BetterAdventureModeCore.identifier("update_use_relay_block"),
            UpdateUseRelayBlockPacket::new
    );

    public final BlockPos useRelayBlockPosition;
    public final BlockPos relayBlockPositionOffset;

    public UpdateUseRelayBlockPacket(BlockPos useRelayBlockPosition, BlockPos relayBlockPositionOffset) {
        this.useRelayBlockPosition = useRelayBlockPosition;
        this.relayBlockPositionOffset = relayBlockPositionOffset;
    }

    public UpdateUseRelayBlockPacket(PacketByteBuf buf) {
        this(buf.readBlockPos(), buf.readBlockPos());
    }
    @Override
    public PacketType<?> getType() {
        return TYPE;
    }
    @Override
    public void write(PacketByteBuf buf) {
        buf.writeBlockPos(this.useRelayBlockPosition);
        buf.writeBlockPos(this.relayBlockPositionOffset);
    }

}
