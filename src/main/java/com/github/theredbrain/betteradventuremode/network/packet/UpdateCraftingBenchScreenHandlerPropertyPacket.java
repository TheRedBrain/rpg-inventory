package com.github.theredbrain.betteradventuremode.network.packet;

import com.github.theredbrain.betteradventuremode.BetterAdventureMode;
import net.fabricmc.fabric.api.networking.v1.FabricPacket;
import net.fabricmc.fabric.api.networking.v1.PacketType;
import net.minecraft.network.PacketByteBuf;

public class UpdateCraftingBenchScreenHandlerPropertyPacket implements FabricPacket {
    public static final PacketType<UpdateCraftingBenchScreenHandlerPropertyPacket> TYPE = PacketType.create(
            BetterAdventureMode.identifier("update_crafting_bench_screen_handler_property"),
            UpdateCraftingBenchScreenHandlerPropertyPacket::new
    );

    public final int shouldScreenCalculateCraftingStatus;

    public UpdateCraftingBenchScreenHandlerPropertyPacket(int shouldScreenCalculateCraftingStatus) {
        this.shouldScreenCalculateCraftingStatus = shouldScreenCalculateCraftingStatus;
    }

    public UpdateCraftingBenchScreenHandlerPropertyPacket(PacketByteBuf buf) {
        this(buf.readInt());
    }

    @Override
    public PacketType<?> getType() {
        return TYPE;
    }

    @Override
    public void write(PacketByteBuf buf) {
        buf.writeInt(this.shouldScreenCalculateCraftingStatus);
    }

}
