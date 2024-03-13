package com.github.theredbrain.betteradventuremode.network.packet;

import com.github.theredbrain.betteradventuremode.entity.DuckLivingEntityMixin;
import com.github.theredbrain.betteradventuremode.entity.player.DuckPlayerEntityMixin;
import com.github.theredbrain.betteradventuremode.entity.player.DuckPlayerInventoryMixin;
import com.github.theredbrain.betteradventuremode.registry.Tags;
import dev.emi.trinkets.api.TrinketComponent;
import dev.emi.trinkets.api.TrinketsApi;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import java.util.Optional;

public class TwoHandMainWeaponPacketReceiver implements ServerPlayNetworking.PlayPacketHandler<TwoHandMainWeaponPacket> {
    @Override
    public void receive(TwoHandMainWeaponPacket packet, ServerPlayerEntity player, PacketSender responseSender) {

        ItemStack offHandItemStack = ItemStack.EMPTY;

        Optional<TrinketComponent> trinkets = TrinketsApi.getTrinketComponent(player);
        if (trinkets.isPresent()) {
            offHandItemStack = player.getEquippedStack(EquipmentSlot.OFFHAND);
            if (offHandItemStack.isEmpty()) {
                offHandItemStack = ((DuckPlayerInventoryMixin) player.getInventory()).betteradventuremode$getSheathedOffHand();
            }
        }

        if (offHandItemStack.isEmpty()) {
            return;
        }

        if (((DuckLivingEntityMixin) player).betteradventuremode$getStamina() <= 0) {
            player.sendMessageToClient(Text.translatable("hud.message.staminaTooLow"), true);
        } else if (((DuckPlayerEntityMixin) player).betteradventuremode$isMainHandStackSheathed() && ((DuckPlayerEntityMixin) player).betteradventuremode$isOffHandStackSheathed()) {
            player.sendMessageToClient(Text.translatable("hud.message.weaponsAreSheathed"), true);
        } else if (((DuckPlayerEntityMixin) player).betteradventuremode$isOffHandStackSheathed()) {
            ((DuckPlayerEntityMixin) player).betteradventuremode$setIsOffHandStackSheathed(false);
            player.equipStack(EquipmentSlot.OFFHAND, offHandItemStack);
            ((DuckPlayerInventoryMixin)player.getInventory()).betteradventuremode$setSheathedOffHand(ItemStack.EMPTY);
        } else if (player.getMainHandStack().isIn(Tags.NON_TWO_HANDED_ITEMS)) {
            player.sendMessageToClient(Text.translatable("hud.message.nonTwoHandedWeaponEquipped"), true);
        } else {
            ((DuckPlayerEntityMixin) player).betteradventuremode$setIsOffHandStackSheathed(true);
            player.equipStack(EquipmentSlot.OFFHAND, ItemStack.EMPTY);
            ((DuckPlayerInventoryMixin)player.getInventory()).betteradventuremode$setSheathedOffHand(offHandItemStack);
        }
    }
}
