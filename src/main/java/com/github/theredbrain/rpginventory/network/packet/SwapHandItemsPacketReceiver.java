package com.github.theredbrain.rpginventory.network.packet;

import com.github.theredbrain.rpginventory.RPGInventory;
import com.github.theredbrain.rpginventory.config.ServerConfig;
import com.github.theredbrain.rpginventory.entity.player.DuckPlayerEntityMixin;
import com.github.theredbrain.rpginventory.entity.player.DuckPlayerInventoryMixin;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;

public class SwapHandItemsPacketReceiver implements ServerPlayNetworking.PlayPayloadHandler<SwapHandItemsPacket> {
	@Override
	public void receive(SwapHandItemsPacket payload, ServerPlayNetworking.Context context) {

		ServerPlayerEntity player = context.player();

		if (RPGInventory.isHandSlotOverhaulActive()) {

			ServerConfig serverConfig = RPGInventory.SERVER_CONFIG;

			boolean mainHand = payload.mainHand();
			boolean offHand = payload.offHand();

			ItemStack handItemStack = ItemStack.EMPTY;
			ItemStack alternativeHandItemStack = ItemStack.EMPTY;
			ItemStack offhandItemStack = ItemStack.EMPTY;
			ItemStack alternativeOffhandItemStack = ItemStack.EMPTY;

			boolean handIsSheathed = ((DuckPlayerEntityMixin) player).rpginventory$isHandStackSheathed();
			boolean offHandIsSheathed = ((DuckPlayerEntityMixin) player).rpginventory$isOffhandStackSheathed();

			float staminaCost = 0.0F;

			if (mainHand) {
				handItemStack = handIsSheathed ? ((DuckPlayerInventoryMixin) player.getInventory()).rpginventory$getSheathedHand().copy() : ((DuckPlayerInventoryMixin) player.getInventory()).rpginventory$getHand().copy();
				alternativeHandItemStack = ((DuckPlayerInventoryMixin) player.getInventory()).rpginventory$getAlternativeHand().copy();
				staminaCost += RPGInventory.isStaminaAttributesLoaded ? serverConfig.handSlotOverhaul.staminaAttributesCompat.swapping_main_hand_items_stamina_cost.get() : 0.0F;
			}
			if (offHand) {
				offhandItemStack = offHandIsSheathed ? ((DuckPlayerInventoryMixin) player.getInventory()).rpginventory$getSheathedOffhand().copy() : player.getInventory().offHand.get(0).copy();
				alternativeOffhandItemStack = ((DuckPlayerInventoryMixin) player.getInventory()).rpginventory$getAlternativeOffhand().copy();
				staminaCost += RPGInventory.isStaminaAttributesLoaded ? serverConfig.handSlotOverhaul.staminaAttributesCompat.swapping_off_hand_items_stamina_cost.get() : 0.0F;
			}
			if (mainHand && offHand) {
				staminaCost *= serverConfig.handSlotOverhaul.staminaAttributesCompat.swapping_both_hand_items_stamina_cost_multiplier.get();
			}

			if (!(mainHand || offHand)
					|| (mainHand && offHand && handItemStack.isEmpty() && alternativeHandItemStack.isEmpty() && offhandItemStack.isEmpty() && alternativeOffhandItemStack.isEmpty())
					|| (mainHand && !offHand && handItemStack.isEmpty() && alternativeHandItemStack.isEmpty())
					|| (!mainHand && offHand && offhandItemStack.isEmpty() && alternativeOffhandItemStack.isEmpty())
			) {
				return;
			}
			if (staminaCost > 0.0F && !player.isCreative() && serverConfig.handSlotOverhaul.staminaAttributesCompat.swapping_hand_items_requires_stamina.get() && RPGInventory.getCurrentStamina(player) <= 0 && (!serverConfig.handSlotOverhaul.staminaAttributesCompat.swapping_hand_items_requires_stamina_cost.get() || RPGInventory.getCurrentStamina(player) < staminaCost)) {
				player.sendMessageToClient(Text.translatable("hud.message.staminaTooLow"), true);
				return;
			}

			if (mainHand) {
				if (handIsSheathed) {
					((DuckPlayerInventoryMixin) player.getInventory()).rpginventory$setSheathedHand(alternativeHandItemStack);
				} else {
					((DuckPlayerInventoryMixin) player.getInventory()).rpginventory$setHand(alternativeHandItemStack);
				}
				((DuckPlayerInventoryMixin) player.getInventory()).rpginventory$setAlternativeHand(handItemStack);
			}
			if (offHand) {
				if (offHandIsSheathed) {
					((DuckPlayerInventoryMixin) player.getInventory()).rpginventory$setSheathedOffhand(alternativeOffhandItemStack);
				} else {
					player.getInventory().offHand.set(0, alternativeOffhandItemStack);
				}
				((DuckPlayerInventoryMixin) player.getInventory()).rpginventory$setAlternativeOffhand(offhandItemStack);
			}
			if (staminaCost != 0.0F && !player.isCreative()) {
				RPGInventory.addStamina(player, -staminaCost);
			}
			player.getServerWorld().playSound(null, player.getBlockPos().getX(), player.getBlockPos().getY(), player.getBlockPos().getZ(), SoundEvents.ITEM_ARMOR_EQUIP_GENERIC, SoundCategory.PLAYERS, 1.0F, 1.0F);
		} else {
			player.sendMessageToClient(Text.translatable("hud.message.handSlotOverhaulIsDisabledByServer"), true);
		}
	}
}
