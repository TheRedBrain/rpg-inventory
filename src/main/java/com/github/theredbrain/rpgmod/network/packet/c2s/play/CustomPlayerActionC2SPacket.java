package com.github.theredbrain.rpgmod.network.packet.c2s.play;

import com.github.theredbrain.rpgmod.network.listener.CustomServerPlayPacketListener;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.listener.ServerPlayPacketListener;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

public class CustomPlayerActionC2SPacket implements Packet<ServerPlayPacketListener> {
    private final BlockPos pos;
    private final Direction direction;
    private final CustomPlayerActionC2SPacket.Action action;
    private final int sequence;

    public CustomPlayerActionC2SPacket(CustomPlayerActionC2SPacket.Action action, BlockPos pos, Direction direction, int sequence) {
        this.action = action;
        this.pos = pos.toImmutable();
        this.direction = direction;
        this.sequence = sequence;
    }

    public CustomPlayerActionC2SPacket(CustomPlayerActionC2SPacket.Action action, BlockPos pos, Direction direction) {
        this(action, pos, direction, 0);
    }

    public CustomPlayerActionC2SPacket(PacketByteBuf buf) {
        this.action = buf.readEnumConstant(CustomPlayerActionC2SPacket.Action.class);
        this.pos = buf.readBlockPos();
        this.direction = Direction.byId(buf.readUnsignedByte());
        this.sequence = buf.readVarInt();
    }

    @Override
    public void write(PacketByteBuf buf) {
        buf.writeEnumConstant(this.action);
        buf.writeBlockPos(this.pos);
        buf.writeByte(this.direction.getId());
        buf.writeVarInt(this.sequence);
    }

    @Override
    public void apply(ServerPlayPacketListener serverPlayPacketListener) {
        ((CustomServerPlayPacketListener)serverPlayPacketListener).onCustomPlayerAction(this);
    }

    public BlockPos getPos() {
        return this.pos;
    }

    public Direction getDirection() {
        return this.direction;
    }

    public CustomPlayerActionC2SPacket.Action getAction() {
        return this.action;
    }

    public int getSequence() {
        return this.sequence;
    }

    public static enum Action {
        SWAP_OFFHAND_ITEMS,
        SWAP_MAINHAND_ITEMS;

    }
}
