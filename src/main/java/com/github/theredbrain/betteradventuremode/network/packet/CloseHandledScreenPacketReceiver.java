package com.github.theredbrain.betteradventuremode.network.packet;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.network.ClientPlayerEntity;

public class CloseHandledScreenPacketReceiver implements ClientPlayNetworking.PlayPacketHandler<CloseHandledScreenPacket> {
    @Override
    public void receive(CloseHandledScreenPacket packet, ClientPlayerEntity player, PacketSender responseSender) {
        player.closeHandledScreen();
    }
}