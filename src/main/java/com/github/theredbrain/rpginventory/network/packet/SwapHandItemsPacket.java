package com.github.theredbrain.rpginventory.network.packet;

import com.github.theredbrain.rpginventory.RPGInventory;
import net.fabricmc.fabric.api.networking.v1.FabricPacket;
import net.fabricmc.fabric.api.networking.v1.PacketType;
import net.minecraft.network.PacketByteBuf;

public class SwapHandItemsPacket implements FabricPacket {
    public static final PacketType<SwapHandItemsPacket> TYPE = PacketType.create(
            RPGInventory.identifier("swap_hand_items"),
            SwapHandItemsPacket::new
    );

    public final boolean mainHand;

    public SwapHandItemsPacket(boolean mainHand) {
        this.mainHand = mainHand;
    }

    public SwapHandItemsPacket(PacketByteBuf buf) {
        this(buf.readBoolean());
    }
    @Override
    public PacketType<?> getType() {
        return TYPE;
    }
    @Override
    public void write(PacketByteBuf buf) {
        buf.writeBoolean(this.mainHand);
    }

}
