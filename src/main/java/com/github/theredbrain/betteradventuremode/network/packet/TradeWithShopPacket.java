package com.github.theredbrain.betteradventuremode.network.packet;

import com.github.theredbrain.betteradventuremode.BetterAdventureMode;
import net.fabricmc.fabric.api.networking.v1.FabricPacket;
import net.fabricmc.fabric.api.networking.v1.PacketType;
import net.minecraft.network.PacketByteBuf;

public class TradeWithShopPacket implements FabricPacket {
    public static final PacketType<TradeWithShopPacket> TYPE = PacketType.create(
            BetterAdventureMode.identifier("trade_with_shop"),
            TradeWithShopPacket::new
    );

    public final String shopIdentifier;
    public final int id;

    public TradeWithShopPacket(String shopIdentifier, int id) {
        this.shopIdentifier = shopIdentifier;
        this.id = id;
    }

    public TradeWithShopPacket(PacketByteBuf buf) {
        this(buf.readString(), buf.readInt());
    }

    @Override
    public PacketType<?> getType() {
        return TYPE;
    }

    @Override
    public void write(PacketByteBuf buf) {
        buf.writeString(this.shopIdentifier);
        buf.writeInt(this.id);
    }
}
