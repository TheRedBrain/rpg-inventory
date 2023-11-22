package com.github.theredbrain.bamcore.network.packet;

import com.github.theredbrain.bamcore.BetterAdventureModeCore;
import net.fabricmc.fabric.api.networking.v1.FabricPacket;
import net.fabricmc.fabric.api.networking.v1.PacketType;
import net.minecraft.network.PacketByteBuf;

public class SheatheWeaponsPacket implements FabricPacket {
    public static final PacketType<SheatheWeaponsPacket> TYPE = PacketType.create(
            BetterAdventureModeCore.identifier("sheathe_weapons"),
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
