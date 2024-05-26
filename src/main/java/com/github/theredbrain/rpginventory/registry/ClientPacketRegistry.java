package com.github.theredbrain.rpginventory.registry;

import com.github.theredbrain.rpginventory.RPGInventory;
import com.github.theredbrain.rpginventory.entity.player.DuckPlayerEntityMixin;
import com.github.theredbrain.rpginventory.entity.player.DuckPlayerInventoryMixin;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.entity.EquipmentSlot;
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
                            alternativeItemStack = ((DuckPlayerInventoryMixin) player.getInventory()).rpginventory$getAlternativeMainHand().copy();
                            itemStack = ((DuckPlayerInventoryMixin) player.getInventory()).rpginventory$getMainHand().copy();
                            ((DuckPlayerInventoryMixin) player.getInventory()).rpginventory$setAlternativeMainHand(itemStack);
                            ((DuckPlayerInventoryMixin) player.getInventory()).rpginventory$setMainHand(alternativeItemStack);
                        } else {
                            alternativeItemStack = ((DuckPlayerInventoryMixin) player.getInventory()).rpginventory$getAlternativeOffHand().copy();
                            itemStack = player.getEquippedStack(EquipmentSlot.OFFHAND).copy();
                            ((DuckPlayerInventoryMixin) player.getInventory()).rpginventory$setAlternativeOffHand(itemStack);
                            player.equipStack(EquipmentSlot.OFFHAND, alternativeItemStack);
                        }
                    }
                }
            });
        });

        ClientPlayNetworking.registerGlobalReceiver(ServerPacketRegistry.SHEATHED_WEAPONS_PACKET, (client, handler, buffer, responseSender) -> { // TODO convert to packet
            int entityId = buffer.readInt();
            boolean mainHand = buffer.readBoolean();
            boolean isWeaponSheathed = buffer.readBoolean();
            client.execute(() -> {
                if (client.player != null && client.player.getWorld().getEntityById(entityId) != null) {
                    PlayerEntity player = (PlayerEntity) client.player.getWorld().getEntityById(entityId);
                    ItemStack itemStack;
                    if (player != null && player != client.player) {
                        if (mainHand) {
                            ((DuckPlayerEntityMixin) player).rpginventory$setIsMainHandStackSheathed(isWeaponSheathed);
                            itemStack = ((DuckPlayerInventoryMixin) player.getInventory()).rpginventory$getMainHand().copy();
                            if (itemStack.isEmpty()) {
                                itemStack = ((DuckPlayerInventoryMixin) player.getInventory()).rpginventory$getSheathedMainHand().copy();
                            }
                            if (isWeaponSheathed) {
                                ((DuckPlayerInventoryMixin) player.getInventory()).rpginventory$setMainHand(ItemStack.EMPTY);
                                ((DuckPlayerInventoryMixin) player.getInventory()).rpginventory$setSheathedMainHand(itemStack);
                            } else {
                                ((DuckPlayerInventoryMixin) player.getInventory()).rpginventory$setMainHand(itemStack);
                                ((DuckPlayerInventoryMixin) player.getInventory()).rpginventory$setSheathedMainHand(ItemStack.EMPTY);
                            }
                        } else {
                            ((DuckPlayerEntityMixin) player).rpginventory$setIsOffHandStackSheathed(isWeaponSheathed);
                            itemStack = player.getEquippedStack(EquipmentSlot.OFFHAND).copy();
                            if (itemStack.isEmpty()) {
                                itemStack = ((DuckPlayerInventoryMixin) player.getInventory()).rpginventory$getSheathedOffHand().copy();
                            }
                            if (isWeaponSheathed) {
                                player.equipStack(EquipmentSlot.OFFHAND, ItemStack.EMPTY);
                                ((DuckPlayerInventoryMixin) player.getInventory()).rpginventory$setSheathedOffHand(itemStack);
                            } else {
                                player.equipStack(EquipmentSlot.OFFHAND, itemStack);
                                ((DuckPlayerInventoryMixin) player.getInventory()).rpginventory$setSheathedOffHand(ItemStack.EMPTY);
                            }
                        }
                    }
                }
            });
        });

        ClientPlayNetworking.registerGlobalReceiver(ServerPacketRegistry.ServerConfigSync.ID, (client, handler, buf, responseSender) -> {
            RPGInventory.serverConfig = ServerPacketRegistry.ServerConfigSync.read(buf);
        });
    }
}
