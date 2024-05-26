package com.github.theredbrain.rpginventory.network.packet;

import com.github.theredbrain.rpginventory.RPGInventory;
import net.fabricmc.fabric.api.networking.v1.FabricPacket;
import net.fabricmc.fabric.api.networking.v1.PacketType;
import net.minecraft.network.PacketByteBuf;

public class TwoHandMainWeaponPacket implements FabricPacket {
    public static final PacketType<TwoHandMainWeaponPacket> TYPE = PacketType.create(
            RPGInventory.identifier("two_hand_main_weapon"),
            TwoHandMainWeaponPacket::new
    );

    public TwoHandMainWeaponPacket() {
    }

    public TwoHandMainWeaponPacket(PacketByteBuf buf) {
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
