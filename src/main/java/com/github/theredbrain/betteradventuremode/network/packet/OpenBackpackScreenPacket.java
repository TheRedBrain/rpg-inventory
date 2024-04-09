package com.github.theredbrain.betteradventuremode.network.packet;

import com.github.theredbrain.betteradventuremode.BetterAdventureMode;
import net.fabricmc.fabric.api.networking.v1.FabricPacket;
import net.fabricmc.fabric.api.networking.v1.PacketType;
import net.minecraft.network.PacketByteBuf;

public class OpenBackpackScreenPacket implements FabricPacket {
    public static final PacketType<OpenBackpackScreenPacket> TYPE = PacketType.create(
            BetterAdventureMode.identifier("open_backpack_screen"),
            OpenBackpackScreenPacket::new
    );

    public OpenBackpackScreenPacket() {
    }

    public OpenBackpackScreenPacket(PacketByteBuf buf) {
        this();
    }
    @Override
    public PacketType<?> getType() {
        return TYPE;
    }
    @Override
    public void write(PacketByteBuf buf) {
    }
}
