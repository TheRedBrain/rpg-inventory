package com.github.theredbrain.rpginventory.network.packet;

import com.github.theredbrain.rpginventory.entity.player.DuckPlayerEntityMixin;
import com.github.theredbrain.rpginventory.entity.player.DuckPlayerInventoryMixin;
import com.github.theredbrain.staminaattributes.entity.StaminaUsingEntity;
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
            mainHandItemStack = ((DuckPlayerInventoryMixin) player.getInventory()).rpginventory$getMainHand();
            if (mainHandItemStack.isEmpty()) {
                mainHandItemStack = ((DuckPlayerInventoryMixin) player.getInventory()).rpginventory$getSheathedMainHand();
            }
            offHandItemStack = player.getEquippedStack(EquipmentSlot.OFFHAND);
            if (offHandItemStack.isEmpty()) {
                offHandItemStack = ((DuckPlayerInventoryMixin) player.getInventory()).rpginventory$getSheathedOffHand();
            }
        }

        // TODO server config sheatheWeaponsRequiresStamina and sheatheWeaponsStaminaCost
        if (((StaminaUsingEntity) player).staminaattributes$getStamina() <= 0 && !player.isCreative()) {
            player.sendMessageToClient(Text.translatable("hud.message.staminaTooLow"), true);
            return;
        }
        if (((DuckPlayerEntityMixin) player).rpginventory$isMainHandStackSheathed() && ((DuckPlayerEntityMixin) player).rpginventory$isOffHandStackSheathed()) {
            ((DuckPlayerEntityMixin) player).rpginventory$setIsMainHandStackSheathed(false);
            ((DuckPlayerEntityMixin) player).rpginventory$setIsOffHandStackSheathed(false);
            ((DuckPlayerInventoryMixin)player.getInventory()).rpginventory$setMainHand(mainHandItemStack);
            ((DuckPlayerInventoryMixin)player.getInventory()).rpginventory$setSheathedMainHand(ItemStack.EMPTY);
            player.equipStack(EquipmentSlot.OFFHAND, offHandItemStack);
            ((DuckPlayerInventoryMixin)player.getInventory()).rpginventory$setSheathedOffHand(ItemStack.EMPTY);
        } else {
            ((DuckPlayerEntityMixin) player).rpginventory$setIsMainHandStackSheathed(true);
            ((DuckPlayerEntityMixin) player).rpginventory$setIsOffHandStackSheathed(true);
            ((DuckPlayerInventoryMixin)player.getInventory()).rpginventory$setMainHand(ItemStack.EMPTY);
            ((DuckPlayerInventoryMixin)player.getInventory()).rpginventory$setSheathedMainHand(mainHandItemStack);
            player.equipStack(EquipmentSlot.OFFHAND, ItemStack.EMPTY);
            ((DuckPlayerInventoryMixin)player.getInventory()).rpginventory$setSheathedOffHand(offHandItemStack);
        }
    }
}
