package com.github.theredbrain.rpgmod.network.packet.c2s.play;

import com.github.theredbrain.rpgmod.network.listener.CustomServerPlayPacketListener;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketByteBuf;

public class ConsumeItemC2SPacket
        implements Packet<CustomServerPlayPacketListener> {
    private final int sequence;

    public ConsumeItemC2SPacket(int sequence) {
        this.sequence = sequence;
    }

    public ConsumeItemC2SPacket(PacketByteBuf buf) {
        this.sequence = buf.readVarInt();
    }

    @Override
    public void write(PacketByteBuf buf) {
        buf.writeVarInt(this.sequence);
    }

    @Override
    public void apply(CustomServerPlayPacketListener customServerPlayPacketListener) {
        customServerPlayPacketListener.onConsumeItem(this);
    }

    public int getSequence() {
        return this.sequence;
    }
}

