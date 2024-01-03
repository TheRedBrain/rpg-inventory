package com.github.theredbrain.bamcore.network.packet;

import com.github.theredbrain.bamcore.BetterAdventureMode;
import net.fabricmc.fabric.api.networking.v1.FabricPacket;
import net.fabricmc.fabric.api.networking.v1.PacketType;
import net.minecraft.network.PacketByteBuf;

public class SuccessfulTeleportPacket implements FabricPacket {
    public static final PacketType<SuccessfulTeleportPacket> TYPE = PacketType.create(
            BetterAdventureMode.identifier("successful_teleport"),
            SuccessfulTeleportPacket::new
    );

    public SuccessfulTeleportPacket() {
    }

    public SuccessfulTeleportPacket(PacketByteBuf buf) {
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
