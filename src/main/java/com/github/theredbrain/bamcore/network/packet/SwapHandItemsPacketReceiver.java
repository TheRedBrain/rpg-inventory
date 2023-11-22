package com.github.theredbrain.bamcore.network.packet;

import com.github.theredbrain.bamcore.entity.player.DuckPlayerEntityMixin;
import com.github.theredbrain.bamcore.entity.player.DuckPlayerInventoryMixin;
import dev.emi.trinkets.api.TrinketComponent;
import dev.emi.trinkets.api.TrinketsApi;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import java.util.Optional;

public class SwapHandItemsPacketReceiver implements ServerPlayNetworking.PlayPacketHandler<SwapHandItemsPacket> {
    @Override
    public void receive(SwapHandItemsPacket packet, ServerPlayerEntity player, PacketSender responseSender) {

        boolean mainHand = packet.mainHand;

        ItemStack itemStack = ItemStack.EMPTY;
        ItemStack alternateItemStack = ItemStack.EMPTY;

        Optional<TrinketComponent> trinkets = TrinketsApi.getTrinketComponent(player);
        if (trinkets.isPresent()) {
            if (mainHand) {
                itemStack = ((DuckPlayerInventoryMixin)player.getInventory()).bamcore$getMainHand();
                alternateItemStack = ((DuckPlayerInventoryMixin)player.getInventory()).bamcore$getAlternativeMainHand();
            } else {
                itemStack = ((DuckPlayerInventoryMixin)player.getInventory()).bamcore$getOffHand();
                alternateItemStack = ((DuckPlayerInventoryMixin)player.getInventory()).bamcore$getAlternativeOffHand();
            }
        }

        if (itemStack.isEmpty() && alternateItemStack.isEmpty()) {
            return;
        }
        if (((DuckPlayerEntityMixin) player).bamcore$getStamina() <= 0) {
            player.sendMessageToClient(Text.translatable("hud.message.staminaTooLow"), true);
            return;
        }

        if (mainHand) {
            ((DuckPlayerInventoryMixin)player.getInventory()).bamcore$setMainHand(alternateItemStack);
            ((DuckPlayerInventoryMixin)player.getInventory()).bamcore$setAlternativeMainHand(itemStack);
        } else {
            ((DuckPlayerInventoryMixin)player.getInventory()).bamcore$setOffHand(alternateItemStack);
            ((DuckPlayerInventoryMixin)player.getInventory()).bamcore$setAlternativeOffHand(itemStack);
        }
        // TODO play sounds
    }
}
