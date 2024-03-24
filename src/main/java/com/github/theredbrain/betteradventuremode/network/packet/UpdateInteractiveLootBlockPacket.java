package com.github.theredbrain.betteradventuremode.network.packet;

import com.github.theredbrain.betteradventuremode.BetterAdventureMode;
import net.fabricmc.fabric.api.networking.v1.FabricPacket;
import net.fabricmc.fabric.api.networking.v1.PacketType;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.math.BlockPos;

public class UpdateInteractiveLootBlockPacket implements FabricPacket {
    public static final PacketType<UpdateInteractiveLootBlockPacket> TYPE = PacketType.create(
            BetterAdventureMode.identifier("update_interactive_loot_block"),
            UpdateInteractiveLootBlockPacket::new
    );

    public final BlockPos interactiveLootBlockPosition;

    public final String lootTableIdentifierString;

    public UpdateInteractiveLootBlockPacket(BlockPos interactiveLootBlockPosition, String lootTableIdentifierString) {
        this.interactiveLootBlockPosition = interactiveLootBlockPosition;
        this.lootTableIdentifierString = lootTableIdentifierString;
    }

    public UpdateInteractiveLootBlockPacket(PacketByteBuf buf) {
        this(buf.readBlockPos(), buf.readString());
    }
    @Override
    public PacketType<?> getType() {
        return TYPE;
    }
    @Override
    public void write(PacketByteBuf buf) {
        buf.writeBlockPos(this.interactiveLootBlockPosition);
        buf.writeString(this.lootTableIdentifierString);
    }

}
