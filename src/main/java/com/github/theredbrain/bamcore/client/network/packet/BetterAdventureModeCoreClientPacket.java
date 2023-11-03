package com.github.theredbrain.bamcore.client.network.packet;

import com.github.theredbrain.bamcore.entity.player.DuckPlayerInventoryMixin;
import com.github.theredbrain.bamcore.network.packet.BetterAdventureModeCoreServerPacket;
import net.bettercombat.api.MinecraftClient_BetterCombat;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

@Environment(EnvType.CLIENT)
public class BetterAdventureModeCoreClientPacket {

    public static void init() {
        ClientPlayNetworking.registerGlobalReceiver(BetterAdventureModeCoreServerPacket.SWAPPED_HAND_ITEMS_PACKET, (client, handler, buffer, responseSender) -> {
            int entityId = buffer.readInt();
            boolean mainHand = buffer.readBoolean();
            client.execute(() -> {
                if (client.player != null && client.player.getWorld().getEntityById(entityId) != null) {
                    PlayerEntity player = (PlayerEntity) client.player.getWorld().getEntityById(entityId);
                    ItemStack alternativeItemStack;
                    ItemStack itemStack;
                    if (player != null && player != client.player) {
                        if (mainHand) {
                            alternativeItemStack = ((DuckPlayerInventoryMixin) player.getInventory()).bamcore$getAlternativeMainHand().copy();
                            itemStack = ((DuckPlayerInventoryMixin) player.getInventory()).bamcore$getMainHand().copy();
                            ((DuckPlayerInventoryMixin) player.getInventory()).bamcore$setAlternativeMainHand(itemStack);
                            ((DuckPlayerInventoryMixin) player.getInventory()).bamcore$setMainHand(alternativeItemStack);
                        } else {
                            alternativeItemStack = ((DuckPlayerInventoryMixin) player.getInventory()).bamcore$getAlternativeOffHand().copy();
                            itemStack = ((DuckPlayerInventoryMixin) player.getInventory()).bamcore$getOffHand().copy();
                            ((DuckPlayerInventoryMixin) player.getInventory()).bamcore$setAlternativeOffHand(itemStack);
                            ((DuckPlayerInventoryMixin) player.getInventory()).bamcore$setOffHand(alternativeItemStack);
                        }
                    }
                }
            });
        });
        ClientPlayNetworking.registerGlobalReceiver(BetterAdventureModeCoreServerPacket.CANCEL_ATTACK_PACKET, (client, handler, buffer, responseSender) -> {
            int entityId = buffer.readInt();
            client.execute(() -> {
                if (client.player != null && client.player.getWorld().getEntityById(entityId) != null) {
                    PlayerEntity player = (PlayerEntity) client.player.getWorld().getEntityById(entityId);
                    if (player != null && player == client.player) {
                        ((MinecraftClient_BetterCombat)client).cancelUpswing();
                    }
                }
            });
        });
    }
}
