package com.github.theredbrain.bamcore.network.packet;

import com.github.theredbrain.bamcore.BetterAdventureMode;
import net.fabricmc.fabric.api.networking.v1.FabricPacket;
import net.fabricmc.fabric.api.networking.v1.PacketType;
import net.minecraft.network.PacketByteBuf;

public class ToggleNecklaceAbilityPacket implements FabricPacket {
    public static final PacketType<ToggleNecklaceAbilityPacket> TYPE = PacketType.create(
            BetterAdventureMode.identifier("toggle_necklace_ability"),
            ToggleNecklaceAbilityPacket::new
    );

    public ToggleNecklaceAbilityPacket() {
    }

    public ToggleNecklaceAbilityPacket(PacketByteBuf buf) {
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
