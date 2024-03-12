package com.github.theredbrain.betteradventuremode.network.packet;

import com.github.theredbrain.betteradventuremode.entity.DuckLivingEntityMixin;
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

public class SwapHandItemsPacketReceiver implements ServerPlayNetworking.PlayPacketHandler<SwapHandItemsPacket> {
    @Override
    public void receive(SwapHandItemsPacket packet, ServerPlayerEntity player, PacketSender responseSender) {

        boolean mainHand = packet.mainHand;

        ItemStack itemStack = ItemStack.EMPTY;
        ItemStack alternativeItemStack = ItemStack.EMPTY;

        Optional<TrinketComponent> trinkets = TrinketsApi.getTrinketComponent(player);
        if (trinkets.isPresent()) {
            if (mainHand) {
                itemStack = ((DuckPlayerInventoryMixin)player.getInventory()).betteradventuremode$getMainHand();
                alternativeItemStack = ((DuckPlayerInventoryMixin)player.getInventory()).betteradventuremode$getAlternativeMainHand();
            } else {
                itemStack = player.getEquippedStack(EquipmentSlot.OFFHAND);
                alternativeItemStack = ((DuckPlayerInventoryMixin)player.getInventory()).betteradventuremode$getAlternativeOffHand();
            }
        }

        if (itemStack.isEmpty() && alternativeItemStack.isEmpty()) {
            return;
        }
        if (((DuckLivingEntityMixin) player).betteradventuremode$getStamina() <= 0) {
            player.sendMessageToClient(Text.translatable("hud.message.staminaTooLow"), true);
            return;
        }

        if (mainHand) {
            ((DuckPlayerInventoryMixin)player.getInventory()).betteradventuremode$setMainHand(alternativeItemStack);
            ((DuckPlayerInventoryMixin)player.getInventory()).betteradventuremode$setAlternativeMainHand(itemStack);
        } else {
            player.equipStack(EquipmentSlot.OFFHAND, alternativeItemStack);
            ((DuckPlayerInventoryMixin)player.getInventory()).betteradventuremode$setAlternativeOffHand(itemStack);
        }
        // TODO play sounds
    }
}
