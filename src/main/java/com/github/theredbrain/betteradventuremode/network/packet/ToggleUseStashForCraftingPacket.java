package com.github.theredbrain.betteradventuremode.network.packet;

import com.github.theredbrain.betteradventuremode.BetterAdventureMode;
import net.fabricmc.fabric.api.networking.v1.FabricPacket;
import net.fabricmc.fabric.api.networking.v1.PacketType;
import net.minecraft.network.PacketByteBuf;

public class ToggleUseStashForCraftingPacket implements FabricPacket {
    public static final PacketType<ToggleUseStashForCraftingPacket> TYPE = PacketType.create(
            BetterAdventureMode.identifier("toggle_stash_for_crafting"),
            ToggleUseStashForCraftingPacket::new
    );

    public final boolean useStashForCrafting;

    public ToggleUseStashForCraftingPacket(boolean useStashForCrafting) {
        this.useStashForCrafting = useStashForCrafting;
    }

    public ToggleUseStashForCraftingPacket(PacketByteBuf buf) {
        this(buf.readBoolean());
    }

    @Override
    public PacketType<?> getType() {
        return TYPE;
    }

    @Override
    public void write(PacketByteBuf buf) {
        buf.writeBoolean(this.useStashForCrafting);
    }

}
