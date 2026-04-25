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

			ItemStack handItemStack = player.getItemBySlot(EquipmentSlot.MAINHAND).copy();
			if (handItemStack.isEmpty()) {
				handItemStack = player.getItemBySlot(ExtendedEquipmentSlot.SHEATHED_HAND).copy();
			}
			ItemStack offHandItemStack = player.getItemBySlot(EquipmentSlot.OFFHAND).copy();
			if (offHandItemStack.isEmpty()) {
				offHandItemStack = player.getItemBySlot(ExtendedEquipmentSlot.SHEATHED_OFF_HAND).copy();
			}

			if (RPGInventory.doesCurrentPlayerStatusPreventHandSlotAction(player) ||
					player.getCooldowns().isOnCooldown(handItemStack) ||
					player.getCooldowns().isOnCooldown(offHandItemStack)
			) {
				player.sendSystemMessage(Component.translatable("hud.message.handSlotActionWasPrevented"), true);
				return;
			}

			float staminaCost = RPGInventory.isStaminaAttributesLoaded ? serverConfig.handSlotOverhaul.staminaAttributesCompat.sheathing_hand_items_stamina_cost.get() : 0.0F;

			if (staminaCost > 0.0F && !player.isCreative() && serverConfig.handSlotOverhaul.staminaAttributesCompat.sheathing_hand_items_requires_stamina.get() && RPGInventory.getCurrentStamina(player) <= 0 && (!serverConfig.handSlotOverhaul.staminaAttributesCompat.sheathing_hand_items_requires_stamina_cost.get() || RPGInventory.getCurrentStamina(player) < staminaCost)) {
				player.sendSystemMessage(Component.translatable("hud.message.staminaTooLow"), true);
				return;
			}
			if (((DuckLivingEntityMixin) player).rpginventory$isHandStackSheathed() && ((DuckLivingEntityMixin) player).rpginventory$isOffhandStackSheathed()) {
				((DuckLivingEntityMixin) player).rpginventory$setIsHandStackSheathed(false);
				((DuckLivingEntityMixin) player).rpginventory$setIsOffhandStackSheathed(false);
				player.setItemSlot(EquipmentSlot.MAINHAND, handItemStack);
				player.setItemSlot(ExtendedEquipmentSlot.SHEATHED_HAND, ItemStack.EMPTY);
				player.setItemSlot(EquipmentSlot.OFFHAND, offHandItemStack);
				player.setItemSlot(ExtendedEquipmentSlot.SHEATHED_OFF_HAND, ItemStack.EMPTY);
			} else {
				((DuckLivingEntityMixin) player).rpginventory$setIsHandStackSheathed(true);
				((DuckLivingEntityMixin) player).rpginventory$setIsOffhandStackSheathed(true);
				player.setItemSlot(EquipmentSlot.MAINHAND, ItemStack.EMPTY);
				player.setItemSlot(ExtendedEquipmentSlot.SHEATHED_HAND, handItemStack);
				player.setItemSlot(EquipmentSlot.OFFHAND, ItemStack.EMPTY);
				player.setItemSlot(ExtendedEquipmentSlot.SHEATHED_OFF_HAND, offHandItemStack);
			}
			if (staminaCost != 0.0F && !player.isCreative()) {
				RPGInventory.addStamina(player, -staminaCost);
			}
			if (serverConfig.handSlotOverhaul.enable_item_cooldown_after_hand_sheathing.get()) {
				player.getCooldowns().addCooldown(player.getMainHandItem(), serverConfig.handSlotOverhaul.sheathing_main_hand_cooldown.get());
				player.getCooldowns().addCooldown(player.getOffhandItem(), serverConfig.handSlotOverhaul.sheathing_offhand_cooldown.get());
			}
			player.level().playSound(null, player.blockPosition().getX(), player.blockPosition().getY(), player.blockPosition().getZ(), SoundEvents.ARMOR_EQUIP_GENERIC, SoundSource.PLAYERS, 1.0F, 1.0F);
		} else {
			player.sendSystemMessage(Component.translatable("hud.message.handSlotOverhaulIsDisabledByServer"), true);
		}
	}
}
