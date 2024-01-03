package com.github.theredbrain.betteradventuremode.network.packet;

import com.github.theredbrain.betteradventuremode.BetterAdventureMode;
import net.fabricmc.fabric.api.networking.v1.FabricPacket;
import net.fabricmc.fabric.api.networking.v1.PacketType;
import net.minecraft.network.PacketByteBuf;

public class SheatheWeaponsPacket implements FabricPacket {
    public static final PacketType<SheatheWeaponsPacket> TYPE = PacketType.create(
            BetterAdventureMode.identifier("sheathe_weapons"),
            SheatheWeaponsPacket::new
    );

    public SheatheWeaponsPacket() {
    }

    public SheatheWeaponsPacket(PacketByteBuf buf) {
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
