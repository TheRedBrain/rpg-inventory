package com.github.theredbrain.betteradventuremode.network.packet;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.network.ClientPlayerEntity;

public class SuccessfulTeleportPacketReceiver implements ClientPlayNetworking.PlayPacketHandler<SuccessfulTeleportPacket> {
    @Override
    public void receive(SuccessfulTeleportPacket packet, ClientPlayerEntity player, PacketSender responseSender) {
        player.closeHandledScreen();
    }
}