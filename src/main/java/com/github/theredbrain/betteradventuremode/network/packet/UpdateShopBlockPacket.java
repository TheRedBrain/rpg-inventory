package com.github.theredbrain.betteradventuremode.network.packet;

import com.github.theredbrain.betteradventuremode.BetterAdventureMode;
import net.fabricmc.fabric.api.networking.v1.FabricPacket;
import net.fabricmc.fabric.api.networking.v1.PacketType;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.math.BlockPos;

public class UpdateShopBlockPacket implements FabricPacket {
    public static final PacketType<UpdateShopBlockPacket> TYPE = PacketType.create(
            BetterAdventureMode.identifier("update_shop_block"),
            UpdateShopBlockPacket::new
    );

    public final BlockPos shopBlockPosition;

    public final String shopIdentifier;

    public UpdateShopBlockPacket(BlockPos shopBlockPosition, String shopIdentifier) {
        this.shopBlockPosition = shopBlockPosition;
        this.shopIdentifier = shopIdentifier;
    }

    public UpdateShopBlockPacket(PacketByteBuf buf) {
        this(
                buf.readBlockPos(),
                buf.readString()
        );
    }
    @Override
    public PacketType<?> getType() {
        return TYPE;
    }
    @Override
    public void write(PacketByteBuf buf) {
        buf.writeBlockPos(this.shopBlockPosition);
        buf.writeString(this.shopIdentifier);
    }

}
