package com.github.theredbrain.betteradventuremode.registry;

import com.github.theredbrain.betteradventuremode.entity.player.DuckPlayerInventoryMixin;
//import net.bettercombat.api.MinecraftClient_BetterCombat;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

@Environment(EnvType.CLIENT)
public class ClientPacketRegistry {

    public static void init() {
        ClientPlayNetworking.registerGlobalReceiver(ServerPacketRegistry.SWAPPED_HAND_ITEMS_PACKET, (client, handler, buffer, responseSender) -> {
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
//        // TODO BetterCombat 1.20.4
//        ClientPlayNetworking.registerGlobalReceiver(BetterAdventureModeCoreServerPacket.CANCEL_ATTACK_PACKET, (client, handler, buffer, responseSender) -> {
//            int entityId = buffer.readInt();
//            client.execute(() -> {
//                if (client.player != null && client.player.getWorld().getEntityById(entityId) != null) {
//                    PlayerEntity player = (PlayerEntity) client.player.getWorld().getEntityById(entityId);
//                    if (player != null && player == client.player) {
//                        ((MinecraftClient_BetterCombat)client).cancelUpswing();
//                    }
//                }
//            });
//        });
        ClientPlayNetworking.registerGlobalReceiver(ServerPacketRegistry.SYNC_PLAYER_LOCATIONS, (client, handler, buffer, responseSender) -> {
            PlayerLocationsRegistry.decodeRegistry(buffer);
        });
    }
}
