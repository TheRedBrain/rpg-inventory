package com.github.theredbrain.bamcore.network.packet;

import com.github.theredbrain.bamcore.entity.player.DuckPlayerInventoryMixin;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

@Environment(EnvType.CLIENT)
public class BetterAdventureModCoreClientPacket {

    public static void init() {
        ClientPlayNetworking.registerGlobalReceiver(BetterAdventureModCoreServerPacket.SWAPPED_HAND_ITEMS_PACKET, (client, handler, buffer, responseSender) -> {
            int entityId = buffer.readInt();
            boolean mainHand = buffer.readBoolean();
//            ItemStack itemStack = buffer.readItemStack();
            client.execute(() -> {
                if (client.player != null && client.player.getWorld().getEntityById(entityId) != null) {
                    PlayerEntity player = (PlayerEntity) client.player.getWorld().getEntityById(entityId);
                    ItemStack alternativeItemStack;
                    ItemStack itemStack;
                    if (player != client.player) {
                        if (mainHand) {
                            alternativeItemStack = ((DuckPlayerInventoryMixin) player.getInventory()).bamcore$getAlternativeMainHand().copy();
                            itemStack = ((DuckPlayerInventoryMixin) player.getInventory()).bamcore$getMainHand().copy();
                            ((DuckPlayerInventoryMixin) player.getInventory()).bamcore$setAlternativeMainHand(itemStack);
                            ((DuckPlayerInventoryMixin) player.getInventory()).bamcore$setMainHand(alternativeItemStack);
                        } else {
                            alternativeItemStack = ((DuckPlayerInventoryMixin) player.getInventory()).bamcore$getAlternativeOffHand().copy();
                            itemStack = player.getInventory().offHand.get(0).copy();
                            ((DuckPlayerInventoryMixin) player.getInventory()).bamcore$setAlternativeOffHand(itemStack);
                            player.getInventory().offHand.set(0, alternativeItemStack);
                        }
                    }
                }
            });
        });
//        ClientPlayNetworking.registerGlobalReceiver(RPGModServerPacket.IS_USING_HOTBAR_ITEM_CLIENT_PACKET, (client, handler, buffer, responseSender) -> {
//            int entityId = buffer.readInt();
//            boolean isUsingHotbarItem = buffer.readBoolean();
////            client.execute(() -> {
////                ((DuckPlayerEntityMixin)player).setUsingHotbarItem(isUsingHotbarItem);
////            });
////            boolean mainHand = buffer.readBoolean();
////            ItemStack itemStack = buffer.readItemStack();
//            client.execute(() -> {
//                if (client.player != null/* && client.player.getWorld().getEntityById(entityId) != null*/) {
//                    PlayerEntity player = (PlayerEntity) client.player.getWorld().getEntityById(entityId);
////                    ItemStack alternativeItemStack;
////                    ItemStack itemStack;
////                    if (player != client.player) {
//                    if (player != null) {
//                        ((DuckPlayerEntityMixin) player).bamcore$setUsingHotbarItem(isUsingHotbarItem);
//                    }
////                        if (mainHand) {
////                            alternativeItemStack = ((DuckPlayerInventoryMixin) player.getInventory()).bam$getAlternativeMainHand().copy();
////                            itemStack = ((DuckPlayerInventoryMixin) player.getInventory()).bam$getMainHand().copy();
////                            ((DuckPlayerInventoryMixin) player.getInventory()).bam$setAlternativeMainHand(itemStack);
////                            ((DuckPlayerInventoryMixin) player.getInventory()).bam$setMainHand(alternativeItemStack);
////                        } else {
////                            alternativeItemStack = ((DuckPlayerInventoryMixin) player.getInventory()).bam$getAlternativeOffHand().copy();
////                            itemStack = player.getInventory().offHand.get(0).copy();
////                            ((DuckPlayerInventoryMixin) player.getInventory()).bam$setAlternativeOffHand(itemStack);
////                            player.getInventory().offHand.set(0, alternativeItemStack);
////                        }
////                    }
//                }
//            });
//        });
    }
}
