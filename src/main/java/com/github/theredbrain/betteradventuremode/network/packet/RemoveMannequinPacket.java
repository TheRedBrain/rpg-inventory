package com.github.theredbrain.betteradventuremode.network.packet;

import com.github.theredbrain.betteradventuremode.BetterAdventureMode;
import net.fabricmc.fabric.api.networking.v1.FabricPacket;
import net.fabricmc.fabric.api.networking.v1.PacketType;
import net.minecraft.network.PacketByteBuf;

public class RemoveMannequinPacket implements FabricPacket {
    public static final PacketType<RemoveMannequinPacket> TYPE = PacketType.create(
            BetterAdventureMode.identifier("remove_mannequin"),
            RemoveMannequinPacket::new
    );

    public final int mannequinEntityId;
    public final int flag;

    public RemoveMannequinPacket(int mannequinEntityId, int flag) {
        this.mannequinEntityId = mannequinEntityId;
        this.flag = flag;
    }

    public RemoveMannequinPacket(PacketByteBuf buf) {
        this(buf.readInt(), buf.readInt());
    }
    @Override
    public PacketType<?> getType() {
        return TYPE;
    }
    @Override
    public void write(PacketByteBuf buf) {
        buf.writeInt(this.mannequinEntityId);
        buf.writeInt(this.flag);
    }

}
