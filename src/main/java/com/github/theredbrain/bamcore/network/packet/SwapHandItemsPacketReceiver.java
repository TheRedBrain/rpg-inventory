package com.github.theredbrain.bamcore.network.packet;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;

public class SwapHandItemsPacketReceiver implements ServerPlayNetworking.PlayChannelHandler {
    @Override
    public void receive(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {

        boolean mainHand = buf.readBoolean();

        server.execute(() -> {
            int handSlotId;
            int alternateHandSlotId;

            PlayerInventory playerInventory = player.getInventory();

            if (mainHand) {
                handSlotId = 40;
                alternateHandSlotId = 42;
            } else {
                handSlotId = 41;
                alternateHandSlotId = 43;
            }

            ItemStack itemStack = playerInventory.getStack(handSlotId);
            ItemStack alternateItemStack = playerInventory.getStack(alternateHandSlotId);

            if (itemStack.isEmpty() && alternateItemStack.isEmpty()) {
                return;
            }
            playerInventory.setStack(handSlotId, alternateItemStack);
            playerInventory.setStack(alternateHandSlotId, itemStack);
            playerInventory.markDirty();

            // TODO play sounds
        });
    }
}
