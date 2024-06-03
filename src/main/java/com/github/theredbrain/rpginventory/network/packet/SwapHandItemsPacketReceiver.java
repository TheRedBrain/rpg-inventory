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

public class SwapHandItemsPacketReceiver implements ServerPlayNetworking.PlayPacketHandler<SwapHandItemsPacket> {
    @Override
    public void receive(SwapHandItemsPacket packet, ServerPlayerEntity player, PacketSender responseSender) {

        boolean mainHand = packet.mainHand;

        ItemStack itemStack;
        ItemStack alternativeItemStack;

        boolean mainHandIsSheathed = ((DuckPlayerEntityMixin) player).rpginventory$isMainHandStackSheathed();
        boolean offHandIsSheathed = ((DuckPlayerEntityMixin) player).rpginventory$isOffHandStackSheathed();

        if (mainHand) {
            itemStack = mainHandIsSheathed ? ((DuckPlayerInventoryMixin) player.getInventory()).rpginventory$getSheathedMainHand() : ((DuckPlayerInventoryMixin) player.getInventory()).rpginventory$getMainHand();
            alternativeItemStack = ((DuckPlayerInventoryMixin) player.getInventory()).rpginventory$getAlternativeMainHand();
        } else {
            itemStack = offHandIsSheathed ? ((DuckPlayerInventoryMixin) player.getInventory()).rpginventory$getSheathedOffHand() : player.getEquippedStack(EquipmentSlot.OFFHAND);
            alternativeItemStack = ((DuckPlayerInventoryMixin) player.getInventory()).rpginventory$getAlternativeOffHand();
        }

        if (itemStack.isEmpty() && alternativeItemStack.isEmpty()) {
            return;
        }
        if (RPGInventory.isStaminaAttributesLoaded && RPGInventory.serverConfig.swapping_hand_items_requires_stamina && ((StaminaUsingEntity) player).staminaattributes$getStamina() <= 0 && !player.isCreative()) {
            player.sendMessageToClient(Text.translatable("hud.message.staminaTooLow"), true);
            return;
        }

        if (mainHand) {
            if (mainHandIsSheathed) {
                ((DuckPlayerInventoryMixin) player.getInventory()).rpginventory$setSheathedMainHand(alternativeItemStack);
            } else {
                ((DuckPlayerInventoryMixin) player.getInventory()).rpginventory$setMainHand(alternativeItemStack);
            }
            ((DuckPlayerInventoryMixin) player.getInventory()).rpginventory$setAlternativeMainHand(itemStack);
        } else {
            if (mainHandIsSheathed) {
                ((DuckPlayerInventoryMixin) player.getInventory()).rpginventory$setSheathedOffHand(alternativeItemStack);
            } else {
                player.equipStack(EquipmentSlot.OFFHAND, alternativeItemStack);
            }
            ((DuckPlayerInventoryMixin) player.getInventory()).rpginventory$setAlternativeOffHand(itemStack);
        }
        if (RPGInventory.isStaminaAttributesLoaded) {
            ((StaminaUsingEntity) player).staminaattributes$addStamina(-RPGInventory.serverConfig.swapping_hand_items_stamina_cost);
        }
        // TODO play sounds
    }
}
