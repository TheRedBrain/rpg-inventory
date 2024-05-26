package com.github.theredbrain.rpginventory.network.packet;

import com.github.theredbrain.rpginventory.RPGInventory;
import com.github.theredbrain.rpginventory.entity.player.DuckPlayerEntityMixin;
import com.github.theredbrain.rpginventory.entity.player.DuckPlayerInventoryMixin;
import com.github.theredbrain.rpginventory.registry.Tags;
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

public class TwoHandMainWeaponPacketReceiver implements ServerPlayNetworking.PlayPacketHandler<TwoHandMainWeaponPacket> {
    @Override
    public void receive(TwoHandMainWeaponPacket packet, ServerPlayerEntity player, PacketSender responseSender) {

        ItemStack offHandItemStack = ItemStack.EMPTY;

        Optional<TrinketComponent> trinkets = TrinketsApi.getTrinketComponent(player);
        if (trinkets.isPresent()) {
            offHandItemStack = player.getEquippedStack(EquipmentSlot.OFFHAND);
            if (offHandItemStack.isEmpty()) {
                offHandItemStack = ((DuckPlayerInventoryMixin) player.getInventory()).rpginventory$getSheathedOffHand();
            }
        }

        if (RPGInventory.serverConfig.toggling_two_handed_stance_requires_stamina && ((StaminaUsingEntity) player).staminaattributes$getStamina() <= 0 && !player.isCreative()) {
            player.sendMessageToClient(Text.translatable("hud.message.staminaTooLow"), true);
            return;
        } else if (((DuckPlayerEntityMixin) player).rpginventory$isMainHandStackSheathed() && ((DuckPlayerEntityMixin) player).rpginventory$isOffHandStackSheathed()) {
            player.sendMessageToClient(Text.translatable("hud.message.weaponsAreSheathed"), true);
            return;
        } else if (player.getMainHandStack().isIn(Tags.NON_TWO_HANDED_ITEMS)) {
            player.sendMessageToClient(Text.translatable("hud.message.nonTwoHandedWeaponEquipped"), true);
            return;
        } else if (((DuckPlayerEntityMixin) player).rpginventory$isOffHandStackSheathed()) {
            ((DuckPlayerEntityMixin) player).rpginventory$setIsOffHandStackSheathed(false);
            player.equipStack(EquipmentSlot.OFFHAND, offHandItemStack);
            ((DuckPlayerInventoryMixin) player.getInventory()).rpginventory$setSheathedOffHand(ItemStack.EMPTY);
        } else {
            ((DuckPlayerEntityMixin) player).rpginventory$setIsOffHandStackSheathed(true);
            player.equipStack(EquipmentSlot.OFFHAND, ItemStack.EMPTY);
            ((DuckPlayerInventoryMixin) player.getInventory()).rpginventory$setSheathedOffHand(offHandItemStack);
        }
        ((StaminaUsingEntity) player).staminaattributes$addStamina(-RPGInventory.serverConfig.toggling_two_handed_stance_stamina_cost);
        // TODO play sounds
    }
}
