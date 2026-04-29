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

public class SheatheWeaponsPacketReceiver implements ServerPlayNetworking.PlayPayloadHandler<SheatheWeaponsPacket> {

	@Override
	public void receive(SheatheWeaponsPacket payload, ServerPlayNetworking.Context context) {

		ServerPlayer player = context.player();

		if (((DuckPlayerEntityMixin) player).rpginventory$isHandSlotOverhaulActive()) {

			ServerConfig serverConfig = RPGInventory.SERVER_CONFIG;

			float staminaCost = RPGInventory.isStaminaAttributesLoaded ? serverConfig.handSlotOverhaul.staminaAttributesCompat.sheathing_hand_items_stamina_cost.get() : 0.0F;

			if (staminaCost > 0.0F && !player.isCreative() && serverConfig.handSlotOverhaul.staminaAttributesCompat.sheathing_hand_items_requires_stamina.get() && RPGInventory.getCurrentStamina(player) <= 0 && (!serverConfig.handSlotOverhaul.staminaAttributesCompat.sheathing_hand_items_requires_stamina_cost.get() || RPGInventory.getCurrentStamina(player) < staminaCost)) {
				player.sendSystemMessage(Component.translatable("hud.message.staminaTooLow"), true);
				return;
			}

			boolean playerStatusPreventsHandSlotAction = RPGInventory.doesCurrentPlayerStatusPreventHandSlotAction(player);
			ItemStack handItemStack;
			ItemStack sheathedHandItemStack;
			ItemStack offHandItemStack;
			ItemStack sheathedOffHandItemStack;

			if (((DuckLivingEntityMixin) player).rpginventory$isHandStackSheathed() && ((DuckLivingEntityMixin) player).rpginventory$isOffhandStackSheathed()) {
				((DuckLivingEntityMixin) player).rpginventory$setIsHandStackSheathed(false);
				((DuckLivingEntityMixin) player).rpginventory$setIsOffhandStackSheathed(false);

				handItemStack = player.getItemBySlot(EquipmentSlot.MAINHAND).copy();
				sheathedHandItemStack = player.getItemBySlot(ExtendedEquipmentSlot.SHEATHED_HAND).copy();
				if (!handItemStack.equals(ItemStack.EMPTY)) {
					RPGInventory.info(player.getName() + " had an item in their Hand Slot while their hand was sheathed. This should not happen!");
				}

				offHandItemStack = player.getItemBySlot(EquipmentSlot.OFFHAND).copy();
				sheathedOffHandItemStack = player.getItemBySlot(ExtendedEquipmentSlot.SHEATHED_OFF_HAND).copy();
				if (!offHandItemStack.equals(ItemStack.EMPTY)) {
					RPGInventory.info(player.getName() + " had an item in their Offhand Slot while their offhand was sheathed. This should not happen!");
				}

				if (playerStatusPreventsHandSlotAction ||
						player.getCooldowns().isOnCooldown(sheathedHandItemStack) ||
						player.getCooldowns().isOnCooldown(sheathedOffHandItemStack)
				) {
					((DuckLivingEntityMixin) player).rpginventory$setIsHandStackSheathed(true);
					((DuckLivingEntityMixin) player).rpginventory$setIsOffhandStackSheathed(true);
					player.sendSystemMessage(Component.translatable("hud.message.handSlotActionWasPrevented"), true);
					return;
				}

				player.setItemSlot(EquipmentSlot.MAINHAND, sheathedHandItemStack);
				player.setItemSlot(ExtendedEquipmentSlot.SHEATHED_HAND, handItemStack);

				player.setItemSlot(EquipmentSlot.OFFHAND, sheathedOffHandItemStack);
				player.setItemSlot(ExtendedEquipmentSlot.SHEATHED_OFF_HAND, offHandItemStack);

				if (serverConfig.handSlotOverhaul.enable_item_cooldown_after_hand_sheathing.get()) {
					player.getCooldowns().addCooldown(sheathedHandItemStack, serverConfig.handSlotOverhaul.sheathing_main_hand_cooldown.get());
					player.getCooldowns().addCooldown(sheathedOffHandItemStack, serverConfig.handSlotOverhaul.sheathing_offhand_cooldown.get());
				}
			} else if (((DuckLivingEntityMixin) player).rpginventory$isHandStackSheathed()) {
				// this should never happen
				RPGInventory.info(player.getName() + "'s hand was sheathed, while their offhand was not sheathed. This should not happen!");

				((DuckLivingEntityMixin) player).rpginventory$setIsHandStackSheathed(false);

				handItemStack = player.getItemBySlot(EquipmentSlot.MAINHAND).copy();
				sheathedHandItemStack = player.getItemBySlot(ExtendedEquipmentSlot.SHEATHED_HAND).copy();
				if (!handItemStack.equals(ItemStack.EMPTY)) {
					RPGInventory.info(player.getName() + " had an item in their Hand Slot while their hand was sheathed. This should not happen!");
				}

				if (playerStatusPreventsHandSlotAction ||
						player.getCooldowns().isOnCooldown(sheathedHandItemStack)
				) {
					((DuckLivingEntityMixin) player).rpginventory$setIsHandStackSheathed(true);
					player.sendSystemMessage(Component.translatable("hud.message.handSlotActionWasPrevented"), true);
					return;
				}

				player.setItemSlot(EquipmentSlot.MAINHAND, sheathedHandItemStack);
				player.setItemSlot(ExtendedEquipmentSlot.SHEATHED_HAND, handItemStack);

				if (serverConfig.handSlotOverhaul.enable_item_cooldown_after_hand_sheathing.get()) {
					player.getCooldowns().addCooldown(sheathedHandItemStack, serverConfig.handSlotOverhaul.sheathing_main_hand_cooldown.get());
				}
			} else if (((DuckLivingEntityMixin) player).rpginventory$isOffhandStackSheathed()) {
				handItemStack = player.getItemBySlot(EquipmentSlot.MAINHAND).copy();
				sheathedHandItemStack = player.getItemBySlot(ExtendedEquipmentSlot.SHEATHED_HAND).copy();
				if (!sheathedHandItemStack.equals(ItemStack.EMPTY)) {
					RPGInventory.info(player.getName() + " had an item in their Sheathed Hand Slot while their hand was not sheathed. This should not happen!");
				}

				if (playerStatusPreventsHandSlotAction ||
						player.getCooldowns().isOnCooldown(handItemStack)
				) {
					player.sendSystemMessage(Component.translatable("hud.message.handSlotActionWasPrevented"), true);
					return;
				}

				player.setItemSlot(EquipmentSlot.MAINHAND, sheathedHandItemStack);
				player.setItemSlot(ExtendedEquipmentSlot.SHEATHED_HAND, handItemStack);

				if (serverConfig.handSlotOverhaul.enable_item_cooldown_after_toggling_2_handed_stance.get()) {
					player.getCooldowns().addCooldown(sheathedHandItemStack, serverConfig.handSlotOverhaul.toggling_2_handed_stance_main_hand_cooldown.get());
				}
				((DuckLivingEntityMixin) player).rpginventory$setIsHandStackSheathed(true);
			} else {
				handItemStack = player.getItemBySlot(EquipmentSlot.MAINHAND).copy();
				sheathedHandItemStack = player.getItemBySlot(ExtendedEquipmentSlot.SHEATHED_HAND).copy();
				if (!sheathedHandItemStack.equals(ItemStack.EMPTY)) {
					RPGInventory.info(player.getName() + " had an item in their Sheathed Hand Slot while their hand was not sheathed. This should not happen!");
				}

				offHandItemStack = player.getItemBySlot(EquipmentSlot.OFFHAND).copy();
				sheathedOffHandItemStack = player.getItemBySlot(ExtendedEquipmentSlot.SHEATHED_OFF_HAND).copy();
				if (!sheathedOffHandItemStack.equals(ItemStack.EMPTY)) {
					RPGInventory.info(player.getName() + " had an item in their Sheathed Offhand Slot while their offhand was not sheathed. This should not happen!");
				}

				if (playerStatusPreventsHandSlotAction ||
						player.getCooldowns().isOnCooldown(handItemStack) ||
						player.getCooldowns().isOnCooldown(offHandItemStack)
				) {
					player.sendSystemMessage(Component.translatable("hud.message.handSlotActionWasPrevented"), true);
					return;
				}

				player.setItemSlot(EquipmentSlot.MAINHAND, sheathedHandItemStack);
				player.setItemSlot(ExtendedEquipmentSlot.SHEATHED_HAND, handItemStack);

				player.setItemSlot(EquipmentSlot.OFFHAND, sheathedOffHandItemStack);
				player.setItemSlot(ExtendedEquipmentSlot.SHEATHED_OFF_HAND, offHandItemStack);

				if (serverConfig.handSlotOverhaul.enable_item_cooldown_after_hand_sheathing.get()) {
					player.getCooldowns().addCooldown(handItemStack, serverConfig.handSlotOverhaul.sheathing_main_hand_cooldown.get());
					player.getCooldowns().addCooldown(offHandItemStack, serverConfig.handSlotOverhaul.sheathing_offhand_cooldown.get());
				}

				((DuckLivingEntityMixin) player).rpginventory$setIsHandStackSheathed(true);
				((DuckLivingEntityMixin) player).rpginventory$setIsOffhandStackSheathed(true);
			}

			if (staminaCost != 0.0F && !player.isCreative()) {
				RPGInventory.addStamina(player, -staminaCost);
			}
			player.level().playSound(null, player.blockPosition().getX(), player.blockPosition().getY(), player.blockPosition().getZ(), SoundEvents.ARMOR_EQUIP_GENERIC, SoundSource.PLAYERS, 1.0F, 1.0F);
		} else {
			player.sendSystemMessage(Component.translatable("hud.message.handSlotOverhaulIsDisabledByServer"), true);
		}
	}
}
