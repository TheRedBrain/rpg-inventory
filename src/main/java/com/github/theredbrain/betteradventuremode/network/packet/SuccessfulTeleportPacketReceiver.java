package com.github.theredbrain.betteradventuremode.network.packet;

import com.github.theredbrain.betteradventuremode.registry.StatusEffectsRegistry;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.network.ServerPlayerEntity;

public class SuccessfulTeleportPacketReceiver implements ServerPlayNetworking.PlayPacketHandler<SuccessfulTeleportPacket> {
    @Override
    public void receive(SuccessfulTeleportPacket packet, ServerPlayerEntity player, PacketSender responseSender) {

        player.removeStatusEffect(StatusEffectsRegistry.PORTAL_RESISTANCE_EFFECT);
        player.closeHandledScreen();
    }
}