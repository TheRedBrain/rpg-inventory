package com.github.theredbrain.betteradventuremode.network.packet;

import com.github.theredbrain.betteradventuremode.entity.player.DuckPlayerEntityMixin;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.network.ServerPlayerEntity;

public class ToggleUseStashForCraftingPacketReceiver implements ServerPlayNetworking.PlayPacketHandler<ToggleUseStashForCraftingPacket> {
    @Override
    public void receive(ToggleUseStashForCraftingPacket packet, ServerPlayerEntity player, PacketSender responseSender) {
        ((DuckPlayerEntityMixin)player).betteradventuremode$setUseStashForCrafting(packet.useStashForCrafting);
    }
}
