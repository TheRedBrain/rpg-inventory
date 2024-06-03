package com.github.theredbrain.rpginventory.network.packet;

import com.github.theredbrain.rpginventory.RPGInventory;
import com.github.theredbrain.rpginventory.entity.player.DuckPlayerEntityMixin;
import com.github.theredbrain.rpginventory.entity.player.DuckPlayerInventoryMixin;
import com.github.theredbrain.staminaattributes.entity.StaminaUsingEntity;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

public class SheatheWeaponsPacketReceiver implements ServerPlayNetworking.PlayPacketHandler<SheatheWeaponsPacket> {
    @Override
    public void receive(SheatheWeaponsPacket packet, ServerPlayerEntity player, PacketSender responseSender) {

        ItemStack mainHandItemStack = ((DuckPlayerInventoryMixin) player.getInventory()).rpginventory$getMainHand();
        if (mainHandItemStack.isEmpty()) {
            mainHandItemStack = ((DuckPlayerInventoryMixin) player.getInventory()).rpginventory$getSheathedMainHand();
        }
        ItemStack offHandItemStack = player.getEquippedStack(EquipmentSlot.OFFHAND);
        if (offHandItemStack.isEmpty()) {
            offHandItemStack = ((DuckPlayerInventoryMixin) player.getInventory()).rpginventory$getSheathedOffHand();
        }

        if (RPGInventory.isStaminaAttributesLoaded && RPGInventory.serverConfig.sheathing_hand_items_requires_stamina && ((StaminaUsingEntity) player).staminaattributes$getStamina() <= 0 && !player.isCreative()) {
            player.sendMessageToClient(Text.translatable("hud.message.staminaTooLow"), true);
            return;
        }
        if (((DuckPlayerEntityMixin) player).rpginventory$isMainHandStackSheathed() && ((DuckPlayerEntityMixin) player).rpginventory$isOffHandStackSheathed()) {
            ((DuckPlayerEntityMixin) player).rpginventory$setIsMainHandStackSheathed(false);
            ((DuckPlayerEntityMixin) player).rpginventory$setIsOffHandStackSheathed(false);
            ((DuckPlayerInventoryMixin) player.getInventory()).rpginventory$setMainHand(mainHandItemStack);
            ((DuckPlayerInventoryMixin) player.getInventory()).rpginventory$setSheathedMainHand(ItemStack.EMPTY);
            player.equipStack(EquipmentSlot.OFFHAND, offHandItemStack);
            ((DuckPlayerInventoryMixin) player.getInventory()).rpginventory$setSheathedOffHand(ItemStack.EMPTY);
        } else {
            ((DuckPlayerEntityMixin) player).rpginventory$setIsMainHandStackSheathed(true);
            ((DuckPlayerEntityMixin) player).rpginventory$setIsOffHandStackSheathed(true);
            ((DuckPlayerInventoryMixin) player.getInventory()).rpginventory$setMainHand(ItemStack.EMPTY);
            ((DuckPlayerInventoryMixin) player.getInventory()).rpginventory$setSheathedMainHand(mainHandItemStack);
            player.equipStack(EquipmentSlot.OFFHAND, ItemStack.EMPTY);
            ((DuckPlayerInventoryMixin) player.getInventory()).rpginventory$setSheathedOffHand(offHandItemStack);
        }
        if (RPGInventory.isStaminaAttributesLoaded) {
            ((StaminaUsingEntity) player).staminaattributes$addStamina(-RPGInventory.serverConfig.sheathing_hand_items_stamina_cost);
        }
        // TODO play sounds
    }
}
