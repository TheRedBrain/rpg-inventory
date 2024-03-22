package com.github.theredbrain.betteradventuremode.registry;

import com.github.theredbrain.betteradventuremode.BetterAdventureMode;
import com.github.theredbrain.betteradventuremode.entity.player.DuckPlayerEntityMixin;
import com.github.theredbrain.betteradventuremode.entity.player.DuckPlayerInventoryMixin;
import com.github.theredbrain.betteradventuremode.network.packet.*;
import net.bettercombat.api.MinecraftClient_BetterCombat;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.option.Perspective;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

@Environment(EnvType.CLIENT)
public class ClientPacketRegistry {

    public static void init() {
        ClientPlayNetworking.registerGlobalReceiver(OpenDialogueScreenPacket.TYPE, new OpenDialogueScreenPacketReceiver());

        ClientPlayNetworking.registerGlobalReceiver(SendAnnouncementPacket.TYPE, new SendAnnouncementPacketReceiver());

        ClientPlayNetworking.registerGlobalReceiver(SuccessfulTeleportPacket.TYPE, new SuccessfulTeleportPacketReceiver());

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
                            itemStack = player.getEquippedStack(EquipmentSlot.OFFHAND).copy();
                            ((DuckPlayerInventoryMixin) player.getInventory()).betteradventuremode$setAlternativeOffHand(itemStack);
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
                            ((DuckPlayerEntityMixin) player).betteradventuremode$setIsMainHandStackSheathed(isWeaponSheathed);
                            itemStack = ((DuckPlayerInventoryMixin) player.getInventory()).betteradventuremode$getMainHand().copy();
                            if (itemStack.isEmpty()) {
                                itemStack = ((DuckPlayerInventoryMixin) player.getInventory()).betteradventuremode$getSheathedMainHand().copy();
                            }
                            if (isWeaponSheathed) {
                                ((DuckPlayerInventoryMixin) player.getInventory()).betteradventuremode$setMainHand(ItemStack.EMPTY);
                                ((DuckPlayerInventoryMixin) player.getInventory()).betteradventuremode$setSheathedMainHand(itemStack);
                            } else {
                                ((DuckPlayerInventoryMixin) player.getInventory()).betteradventuremode$setMainHand(itemStack);
                                ((DuckPlayerInventoryMixin) player.getInventory()).betteradventuremode$setSheathedMainHand(ItemStack.EMPTY);
                            }
                        } else {
                            ((DuckPlayerEntityMixin) player).betteradventuremode$setIsOffHandStackSheathed(isWeaponSheathed);
                            itemStack = player.getEquippedStack(EquipmentSlot.OFFHAND).copy();
                            if (itemStack.isEmpty()) {
                                itemStack = ((DuckPlayerInventoryMixin) player.getInventory()).betteradventuremode$getSheathedOffHand().copy();
                            }
                            if (isWeaponSheathed) {
                                player.equipStack(EquipmentSlot.OFFHAND, ItemStack.EMPTY);
                                ((DuckPlayerInventoryMixin) player.getInventory()).betteradventuremode$setSheathedOffHand(itemStack);
                            } else {
                                player.equipStack(EquipmentSlot.OFFHAND, itemStack);
                                ((DuckPlayerInventoryMixin) player.getInventory()).betteradventuremode$setSheathedOffHand(ItemStack.EMPTY);
                            }
                        }
                    }
                }
            });
        });

        ClientPlayNetworking.registerGlobalReceiver(ServerPacketRegistry.CANCEL_ATTACK_PACKET, (client, handler, buffer, responseSender) -> { // TODO convert to packet
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
        ClientPlayNetworking.registerGlobalReceiver(ServerPacketRegistry.SYNC_CRAFTING_RECIPES, (client, handler, buffer, responseSender) -> { // TODO convert to packet
            CraftingRecipeRegistry.decodeRegistry(buffer);
        });
        ClientPlayNetworking.registerGlobalReceiver(ServerPacketRegistry.SYNC_DIALOGUES, (client, handler, buffer, responseSender) -> { // TODO convert to packet
            DialoguesRegistry.decodeRegistry(buffer);
        });
        ClientPlayNetworking.registerGlobalReceiver(ServerPacketRegistry.SYNC_DIALOGUE_ANSWERS, (client, handler, buffer, responseSender) -> { // TODO convert to packet
            DialogueAnswersRegistry.decodeRegistry(buffer);
        });
        ClientPlayNetworking.registerGlobalReceiver(ServerPacketRegistry.SYNC_LOCATIONS, (client, handler, buffer, responseSender) -> { // TODO convert to packet
            LocationsRegistry.decodeRegistry(buffer);
        });
        ClientPlayNetworking.registerGlobalReceiver(ServerPacketRegistry.SYNC_SHOPS, (client, handler, buffer, responseSender) -> { // TODO convert to packet
            ShopsRegistry.decodeRegistry(buffer);
        });
        ClientPlayNetworking.registerGlobalReceiver(ServerPacketRegistry.SYNC_WEAPON_POSES, (client, handler, buffer, responseSender) -> { // TODO convert to packet
            WeaponPosesRegistry.decodeRegistry(buffer);
        });
        ClientPlayNetworking.registerGlobalReceiver(ServerPacketRegistry.ServerConfigSync.ID, (client, handler, buf, responseSender) -> {
            BetterAdventureMode.serverConfig = ServerPacketRegistry.ServerConfigSync.read(buf);
            if (BetterAdventureMode.serverConfig.disable_first_person) {
                client.options.setPerspective(Perspective.THIRD_PERSON_BACK);
            }
        });
        ClientPlayNetworking.registerGlobalReceiver(ServerPacketRegistry.GamePlayBalanceConfigSync.ID, (client, handler, buf, responseSender) -> {
            BetterAdventureMode.gamePlayBalanceConfig = ServerPacketRegistry.GamePlayBalanceConfigSync.read(buf);
        });
    }
}
