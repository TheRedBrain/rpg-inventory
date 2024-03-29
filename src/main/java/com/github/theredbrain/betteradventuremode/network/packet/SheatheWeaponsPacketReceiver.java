package com.github.theredbrain.betteradventuremode.network.packet;

import com.github.theredbrain.betteradventuremode.entity.DuckLivingEntityMixin;
import com.github.theredbrain.betteradventuremode.entity.player.DuckPlayerEntityMixin;
import com.github.theredbrain.betteradventuremode.entity.player.DuckPlayerInventoryMixin;
import dev.emi.trinkets.api.TrinketComponent;
import dev.emi.trinkets.api.TrinketsApi;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import java.util.Optional;

public class SheatheWeaponsPacketReceiver implements ServerPlayNetworking.PlayPacketHandler<SheatheWeaponsPacket> {
    @Override
    public void receive(SheatheWeaponsPacket packet, ServerPlayerEntity player, PacketSender responseSender) {

        ItemStack mainHandItemStack = ItemStack.EMPTY;
        ItemStack offHandItemStack = ItemStack.EMPTY;

        Optional<TrinketComponent> trinkets = TrinketsApi.getTrinketComponent(player);
        if (trinkets.isPresent()) {
            mainHandItemStack = ((DuckPlayerInventoryMixin) player.getInventory()).betteradventuremode$getMainHand();
            if (mainHandItemStack.isEmpty()) {
                mainHandItemStack = ((DuckPlayerInventoryMixin) player.getInventory()).betteradventuremode$getSheathedMainHand();
            }
            offHandItemStack = player.getEquippedStack(EquipmentSlot.OFFHAND);
            if (offHandItemStack.isEmpty()) {
                offHandItemStack = ((DuckPlayerInventoryMixin) player.getInventory()).betteradventuremode$getSheathedOffHand();
            }
        }

        if (((DuckLivingEntityMixin) player).betteradventuremode$getStamina() <= 0 && !player.isCreative()) {
            player.sendMessageToClient(Text.translatable("hud.message.staminaTooLow"), true);
            return;
        }
        if (((DuckPlayerEntityMixin) player).betteradventuremode$isMainHandStackSheathed() && ((DuckPlayerEntityMixin) player).betteradventuremode$isOffHandStackSheathed()) {
            ((DuckPlayerEntityMixin) player).betteradventuremode$setIsMainHandStackSheathed(false);
            ((DuckPlayerEntityMixin) player).betteradventuremode$setIsOffHandStackSheathed(false);
            ((DuckPlayerInventoryMixin)player.getInventory()).betteradventuremode$setMainHand(mainHandItemStack);
            ((DuckPlayerInventoryMixin)player.getInventory()).betteradventuremode$setSheathedMainHand(ItemStack.EMPTY);
            player.equipStack(EquipmentSlot.OFFHAND, offHandItemStack);
            ((DuckPlayerInventoryMixin)player.getInventory()).betteradventuremode$setSheathedOffHand(ItemStack.EMPTY);
        } else {
            ((DuckPlayerEntityMixin) player).betteradventuremode$setIsMainHandStackSheathed(true);
            ((DuckPlayerEntityMixin) player).betteradventuremode$setIsOffHandStackSheathed(true);
            ((DuckPlayerInventoryMixin)player.getInventory()).betteradventuremode$setMainHand(ItemStack.EMPTY);
            ((DuckPlayerInventoryMixin)player.getInventory()).betteradventuremode$setSheathedMainHand(mainHandItemStack);
            player.equipStack(EquipmentSlot.OFFHAND, ItemStack.EMPTY);
            ((DuckPlayerInventoryMixin)player.getInventory()).betteradventuremode$setSheathedOffHand(offHandItemStack);
        }
    }
}
