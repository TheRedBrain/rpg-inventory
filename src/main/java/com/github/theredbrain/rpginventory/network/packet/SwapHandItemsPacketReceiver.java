package com.github.theredbrain.rpginventory.network.packet;

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

public class SwapHandItemsPacketReceiver implements ServerPlayNetworking.PlayPacketHandler<SwapHandItemsPacket> {
    @Override
    public void receive(SwapHandItemsPacket packet, ServerPlayerEntity player, PacketSender responseSender) {

        boolean mainHand = packet.mainHand;

        ItemStack itemStack = ItemStack.EMPTY;
        ItemStack alternativeItemStack = ItemStack.EMPTY;

        Optional<TrinketComponent> trinkets = TrinketsApi.getTrinketComponent(player);
        if (trinkets.isPresent()) {
            if (mainHand) {
                itemStack = ((DuckPlayerInventoryMixin)player.getInventory()).rpginventory$getMainHand();
                alternativeItemStack = ((DuckPlayerInventoryMixin)player.getInventory()).rpginventory$getAlternativeMainHand();
            } else {
                itemStack = player.getEquippedStack(EquipmentSlot.OFFHAND);
                alternativeItemStack = ((DuckPlayerInventoryMixin)player.getInventory()).rpginventory$getAlternativeOffHand();
            }
        }

        if (itemStack.isEmpty() && alternativeItemStack.isEmpty()) {
            return;
        }
        if (((StaminaUsingEntity) player).staminaattributes$getStamina() <= 0 && !player.isCreative()) {
            player.sendMessageToClient(Text.translatable("hud.message.staminaTooLow"), true);
            return;
        }

        if (mainHand) {
            ((DuckPlayerInventoryMixin)player.getInventory()).rpginventory$setMainHand(alternativeItemStack);
            ((DuckPlayerInventoryMixin)player.getInventory()).rpginventory$setAlternativeMainHand(itemStack);
        } else {
            player.equipStack(EquipmentSlot.OFFHAND, alternativeItemStack);
            ((DuckPlayerInventoryMixin)player.getInventory()).rpginventory$setAlternativeOffHand(itemStack);
        }
        // TODO play sounds
    }
}
