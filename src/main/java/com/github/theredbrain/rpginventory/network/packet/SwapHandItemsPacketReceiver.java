package com.github.theredbrain.rpginventory.network.packet;

import com.github.theredbrain.rpginventory.RPGInventory;
import com.github.theredbrain.rpginventory.config.ServerConfig;
import com.github.theredbrain.rpginventory.entity.DuckLivingEntityMixin;
import com.github.theredbrain.rpginventory.entity.ExtendedEquipmentSlot;
import com.github.theredbrain.rpginventory.entity.player.DuckPlayerEntityMixin;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;

public class SwapHandItemsPacketReceiver implements ServerPlayNetworking.PlayPayloadHandler<SwapHandItemsPacket> {
	@Override
	public void receive(SwapHandItemsPacket payload, ServerPlayNetworking.Context context) {

		ServerPlayer player = context.player();

		ServerConfig serverConfig = RPGInventory.SERVER_CONFIG;

		if (RPGInventory.isHandSlotOverhaulActive() && serverConfig.handSlotOverhaul.enable_alternative_hand_slots.get()) {

			boolean mainHand = payload.mainHand();
			boolean offHand = payload.offHand();

			ItemStack handItemStack = ItemStack.EMPTY;
			ItemStack alternativeHandItemStack = ItemStack.EMPTY;
			ItemStack offhandItemStack = ItemStack.EMPTY;
			ItemStack alternativeOffhandItemStack = ItemStack.EMPTY;

			boolean handIsSheathed = ((DuckLivingEntityMixin) player).rpginventory$isHandStackSheathed();
			boolean offHandIsSheathed = ((DuckLivingEntityMixin) player).rpginventory$isOffhandStackSheathed();

			float staminaCost = 0.0F;
			boolean actionIsNotPossible = RPGInventory.doesCurrentPlayerStatusPreventHandSlotAction(player);

			if (mainHand) {
				handItemStack = handIsSheathed ? player.getItemBySlot(ExtendedEquipmentSlot.SHEATHED_HAND).copy() : player.getItemBySlot(EquipmentSlot.MAINHAND).copy();
				alternativeHandItemStack = player.getItemBySlot(ExtendedEquipmentSlot.ALTERNATIVE_HAND).copy();
				actionIsNotPossible = actionIsNotPossible || player.getCooldowns().isOnCooldown(handItemStack) || player.getCooldowns().isOnCooldown(alternativeHandItemStack);
				staminaCost += RPGInventory.isStaminaAttributesLoaded ? serverConfig.handSlotOverhaul.staminaAttributesCompat.swapping_main_hand_items_stamina_cost.get() : 0.0F;
			}
			if (offHand) {
				offhandItemStack = offHandIsSheathed ? player.getItemBySlot(ExtendedEquipmentSlot.SHEATHED_OFF_HAND).copy() : player.getItemBySlot(EquipmentSlot.OFFHAND).copy();
				alternativeOffhandItemStack = player.getItemBySlot(ExtendedEquipmentSlot.ALTERNATIVE_OFF_HAND).copy();
				actionIsNotPossible = actionIsNotPossible || player.getCooldowns().isOnCooldown(offhandItemStack) || player.getCooldowns().isOnCooldown(alternativeOffhandItemStack);
				staminaCost += RPGInventory.isStaminaAttributesLoaded ? serverConfig.handSlotOverhaul.staminaAttributesCompat.swapping_off_hand_items_stamina_cost.get() : 0.0F;
			}
			if (actionIsNotPossible) {
				player.sendSystemMessage(Component.translatable("hud.message.handSlotActionWasPrevented"), true);
				return;
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
				player.sendSystemMessage(Component.translatable("hud.message.staminaTooLow"), true);
				return;
			}

			if (mainHand) {
				if (handIsSheathed) {
					player.setItemSlot(ExtendedEquipmentSlot.SHEATHED_HAND, alternativeHandItemStack);
				} else {
					player.setItemSlot(EquipmentSlot.MAINHAND, alternativeHandItemStack);
				}
				player.setItemSlot(ExtendedEquipmentSlot.ALTERNATIVE_HAND, handItemStack);
			}
			if (offHand) {
				if (offHandIsSheathed) {
					player.setItemSlot(ExtendedEquipmentSlot.SHEATHED_OFF_HAND, alternativeOffhandItemStack);
				} else {
					player.setItemSlot(EquipmentSlot.OFFHAND, alternativeOffhandItemStack);
				}
				player.setItemSlot(ExtendedEquipmentSlot.ALTERNATIVE_OFF_HAND, offhandItemStack);
			}
			if (staminaCost != 0.0F && !player.isCreative()) {
				RPGInventory.addStamina(player, -staminaCost);
			}
			if (serverConfig.handSlotOverhaul.enable_item_cooldown_after_hand_swapping.get()) {
				if (mainHand) {
					player.getCooldowns().addCooldown(handItemStack, serverConfig.handSlotOverhaul.swapping_main_hand_cooldown.get());
					player.getCooldowns().addCooldown(alternativeHandItemStack, serverConfig.handSlotOverhaul.swapping_main_hand_cooldown.get());
				}
				if (offHand) {
					player.getCooldowns().addCooldown(offhandItemStack, serverConfig.handSlotOverhaul.swapping_offhand_cooldown.get());
					player.getCooldowns().addCooldown(alternativeOffhandItemStack, serverConfig.handSlotOverhaul.swapping_offhand_cooldown.get());
				}
			}
			player.level().playSound(null, player.blockPosition().getX(), player.blockPosition().getY(), player.blockPosition().getZ(), SoundEvents.ARMOR_EQUIP_GENERIC, SoundSource.PLAYERS, 1.0F, 1.0F);
		} else {
			if (serverConfig.handSlotOverhaul.enable_alternative_hand_slots.get()) {
				player.sendSystemMessage(Component.translatable("hud.message.alternativeHandSlotsDisabledByServer"), true);
			} else {
				player.sendSystemMessage(Component.translatable("hud.message.handSlotOverhaulIsDisabledByServer"), true);
			}
		}
	}
}
