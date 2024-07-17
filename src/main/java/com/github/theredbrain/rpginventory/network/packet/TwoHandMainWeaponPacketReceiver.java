package com.github.theredbrain.rpginventory.network.packet;

import com.github.theredbrain.rpginventory.RPGInventory;
import com.github.theredbrain.rpginventory.entity.player.DuckPlayerEntityMixin;
import com.github.theredbrain.rpginventory.entity.player.DuckPlayerInventoryMixin;
import com.github.theredbrain.rpginventory.registry.Tags;
import com.github.theredbrain.staminaattributes.entity.StaminaUsingEntity;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;

public class TwoHandMainWeaponPacketReceiver implements ServerPlayNetworking.PlayPacketHandler<TwoHandMainWeaponPacket> {
    @Override
    public void receive(TwoHandMainWeaponPacket packet, ServerPlayerEntity player, PacketSender responseSender) {

        ItemStack mainHandItemStack = ((DuckPlayerInventoryMixin) player.getInventory()).rpginventory$getMainHand().copy();
        if (mainHandItemStack.isEmpty()) {
            mainHandItemStack = ((DuckPlayerInventoryMixin) player.getInventory()).rpginventory$getSheathedMainHand().copy();
        }
        ItemStack offHandItemStack = player.getInventory().offHand.get(0).copy();
        if (offHandItemStack.isEmpty()) {
            offHandItemStack = ((DuckPlayerInventoryMixin) player.getInventory()).rpginventory$getSheathedOffhand().copy();
        }

        if (RPGInventory.isStaminaAttributesLoaded && RPGInventory.serverConfig.toggling_two_handed_stance_requires_stamina && ((StaminaUsingEntity) player).staminaattributes$getStamina() <= 0 && !player.isCreative()) {
            player.sendMessageToClient(Text.translatable("hud.message.staminaTooLow"), true);
            return;
        } else if (player.getMainHandStack().isIn(Tags.NON_TWO_HANDED_ITEMS)) {
            player.sendMessageToClient(Text.translatable("hud.message.nonTwoHandedWeaponEquipped"), true);
            return;
        } else if (((DuckPlayerEntityMixin) player).rpginventory$isMainHandStackSheathed() && ((DuckPlayerEntityMixin) player).rpginventory$isOffHandStackSheathed()) {
            if (!RPGInventory.serverConfig.always_allow_toggling_two_handed_stance) {
                player.sendMessageToClient(Text.translatable("hud.message.weaponsAreSheathed"), true);
                return;
            } else {
                ((DuckPlayerEntityMixin) player).rpginventory$setIsMainHandStackSheathed(false);
                ((DuckPlayerInventoryMixin) player.getInventory()).rpginventory$setMainHand(mainHandItemStack);
                ((DuckPlayerInventoryMixin) player.getInventory()).rpginventory$setSheathedMainHand(ItemStack.EMPTY);
            }
        } else if (((DuckPlayerEntityMixin) player).rpginventory$isOffHandStackSheathed()) {
            ((DuckPlayerEntityMixin) player).rpginventory$setIsOffHandStackSheathed(false);
            player.getInventory().offHand.set(0, offHandItemStack);
            ((DuckPlayerInventoryMixin) player.getInventory()).rpginventory$setSheathedOffhand(ItemStack.EMPTY);
        } else {
            ((DuckPlayerEntityMixin) player).rpginventory$setIsOffHandStackSheathed(true);
            player.getInventory().offHand.set(0, ItemStack.EMPTY);
            ((DuckPlayerInventoryMixin) player.getInventory()).rpginventory$setSheathedOffhand(offHandItemStack);
        }
        if (RPGInventory.isStaminaAttributesLoaded && !player.isCreative()) {
            ((StaminaUsingEntity) player).staminaattributes$addStamina(-RPGInventory.serverConfig.toggling_two_handed_stance_stamina_cost);
        }
        player.getServerWorld().playSound(null, player.getBlockPos(), SoundEvents.ITEM_ARMOR_EQUIP_GENERIC, SoundCategory.PLAYERS, 1.0F, 1.0F);
    }
}
