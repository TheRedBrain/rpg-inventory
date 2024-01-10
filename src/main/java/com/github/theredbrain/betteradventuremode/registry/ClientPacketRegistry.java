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
        ClientPlayNetworking.registerGlobalReceiver(ServerPacketRegistry.SWAPPED_HAND_ITEMS_PACKET, (client, handler, buffer, responseSender) -> { // TODO convert to packet
            int entityId = buffer.readInt();
            boolean mainHand = buffer.readBoolean();
            client.execute(() -> {
                if (client.player != null && client.player.getWorld().getEntityById(entityId) != null) {
                    PlayerEntity player = (PlayerEntity) client.player.getWorld().getEntityById(entityId);
                    ItemStack alternativeItemStack;
                    ItemStack itemStack;
                    if (player != null && player != client.player) {
                        if (mainHand) {
                            alternativeItemStack = ((DuckPlayerInventoryMixin) player.getInventory()).betteradventuremode$getAlternativeMainHand().copy();
                            itemStack = ((DuckPlayerInventoryMixin) player.getInventory()).betteradventuremode$getMainHand().copy();
                            ((DuckPlayerInventoryMixin) player.getInventory()).betteradventuremode$setAlternativeMainHand(itemStack);
                            ((DuckPlayerInventoryMixin) player.getInventory()).betteradventuremode$setMainHand(alternativeItemStack);
                        } else {
                            alternativeItemStack = ((DuckPlayerInventoryMixin) player.getInventory()).betteradventuremode$getAlternativeOffHand().copy();
                            itemStack = ((DuckPlayerInventoryMixin) player.getInventory()).betteradventuremode$getOffHand().copy();
                            ((DuckPlayerInventoryMixin) player.getInventory()).betteradventuremode$setAlternativeOffHand(itemStack);
                            ((DuckPlayerInventoryMixin) player.getInventory()).betteradventuremode$setOffHand(alternativeItemStack);
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
        ClientPlayNetworking.registerGlobalReceiver(ServerPacketRegistry.SYNC_CRAFTING_RECIPES, (client, handler, buffer, responseSender) -> { // TODO convert to packet
            CraftingRecipeRegistry.decodeRegistry(buffer);
        });
        ClientPlayNetworking.registerGlobalReceiver(ServerPacketRegistry.SYNC_DIALOGUES, (client, handler, buffer, responseSender) -> { // TODO convert to packet
            DialoguesRegistry.decodeRegistry(buffer);
        });
        ClientPlayNetworking.registerGlobalReceiver(ServerPacketRegistry.SYNC_LOCATIONS, (client, handler, buffer, responseSender) -> { // TODO convert to packet
            LocationsRegistry.decodeRegistry(buffer);
        });
        ClientPlayNetworking.registerGlobalReceiver(ServerPacketRegistry.SYNC_SHOPS, (client, handler, buffer, responseSender) -> { // TODO convert to packet
            ShopsRegistry.decodeRegistry(buffer);
        });
    }
}