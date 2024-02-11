package com.github.theredbrain.betteradventuremode.network.packet;

import com.github.theredbrain.betteradventuremode.BetterAdventureMode;
import net.fabricmc.fabric.api.networking.v1.FabricPacket;
import net.fabricmc.fabric.api.networking.v1.PacketType;
import net.minecraft.network.PacketByteBuf;

public class CraftFromCraftingBenchPacket implements FabricPacket {
    public static final PacketType<CraftFromCraftingBenchPacket> TYPE = PacketType.create(
            BetterAdventureMode.identifier("craft_from_crafting_bench"),
            CraftFromCraftingBenchPacket::new
    );

    public final String recipeIdentifier;
    public final boolean useStorageInventory;

    public CraftFromCraftingBenchPacket(String recipeIdentifier, boolean useStorageInventory) {
        this.recipeIdentifier = recipeIdentifier;
        this.useStorageInventory = useStorageInventory;
    }

    public CraftFromCraftingBenchPacket(PacketByteBuf buf) {
        this(buf.readString(), buf.readBoolean());
    }

    @Override
    public PacketType<?> getType() {
        return TYPE;
    }

    @Override
    public void write(PacketByteBuf buf) {
        buf.writeString(this.recipeIdentifier);
        buf.writeBoolean(this.useStorageInventory);
    }
}
