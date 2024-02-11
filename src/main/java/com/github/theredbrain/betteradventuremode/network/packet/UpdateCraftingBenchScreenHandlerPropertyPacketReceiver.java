package com.github.theredbrain.betteradventuremode.network.packet;

import com.github.theredbrain.betteradventuremode.screen.CraftingBenchBlockScreenHandler;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.network.ServerPlayerEntity;

public class UpdateCraftingBenchScreenHandlerPropertyPacketReceiver implements ServerPlayNetworking.PlayPacketHandler<UpdateCraftingBenchScreenHandlerPropertyPacket> {
    @Override
    public void receive(UpdateCraftingBenchScreenHandlerPropertyPacket packet, ServerPlayerEntity player, PacketSender responseSender) {
        if (player.currentScreenHandler instanceof CraftingBenchBlockScreenHandler craftingBenchBlockScreenHandler) {
            craftingBenchBlockScreenHandler.setShouldScreenCalculateCraftingStatus(packet.shouldScreenCalculateCraftingStatus);
        }
    }
}
