package com.github.theredbrain.rpginventory.network.packet;

import com.github.theredbrain.rpginventory.RPGInventory;
import com.github.theredbrain.rpginventory.entity.player.DuckPlayerEntityMixin;
import com.github.theredbrain.rpginventory.entity.player.DuckPlayerInventoryMixin;
import com.github.theredbrain.staminaattributes.entity.StaminaUsingEntity;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
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
			itemStack = mainHandIsSheathed ? ((DuckPlayerInventoryMixin) player.getInventory()).rpginventory$getSheathedMainHand().copy() : ((DuckPlayerInventoryMixin) player.getInventory()).rpginventory$getMainHand().copy();
			alternativeItemStack = ((DuckPlayerInventoryMixin) player.getInventory()).rpginventory$getAlternativeMainHand().copy();
		} else {
			itemStack = offHandIsSheathed ? ((DuckPlayerInventoryMixin) player.getInventory()).rpginventory$getSheathedOffhand().copy() : player.getInventory().offHand.get(0).copy();
			alternativeItemStack = ((DuckPlayerInventoryMixin) player.getInventory()).rpginventory$getAlternativeOffhand().copy();
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
			if (offHandIsSheathed) {
				((DuckPlayerInventoryMixin) player.getInventory()).rpginventory$setSheathedOffhand(alternativeItemStack);
			} else {
				player.getInventory().offHand.set(0, alternativeItemStack);
			}
			((DuckPlayerInventoryMixin) player.getInventory()).rpginventory$setAlternativeOffhand(itemStack);
		}
		if (RPGInventory.isStaminaAttributesLoaded && !player.isCreative()) {
			((StaminaUsingEntity) player).staminaattributes$addStamina(-RPGInventory.serverConfig.swapping_hand_items_stamina_cost);
		}
		player.getServerWorld().playSound(null, player.getBlockPos(), SoundEvents.ITEM_ARMOR_EQUIP_GENERIC, SoundCategory.PLAYERS, 1.0F, 1.0F);
	}
}
