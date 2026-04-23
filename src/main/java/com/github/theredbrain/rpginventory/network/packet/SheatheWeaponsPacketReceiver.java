package com.github.theredbrain.rpginventory.network.packet;

import com.github.theredbrain.rpginventory.RPGInventory;
import com.github.theredbrain.rpginventory.config.ServerConfig;
import com.github.theredbrain.rpginventory.entity.player.DuckPlayerEntityMixin;
import com.github.theredbrain.rpginventory.entity.player.DuckPlayerInventoryMixin;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.ItemStack;

public class SheatheWeaponsPacketReceiver implements ServerPlayNetworking.PlayPayloadHandler<SheatheWeaponsPacket> {

	@Override
	public void receive(SheatheWeaponsPacket payload, ServerPlayNetworking.Context context) {

		ServerPlayer player = context.player();

		if (RPGInventory.isHandSlotOverhaulActive()) {

			ServerConfig serverConfig = RPGInventory.SERVER_CONFIG;

			ItemStack handItemStack = ((DuckPlayerInventoryMixin) player.getInventory()).rpginventory$getHand().copy();
			if (handItemStack.isEmpty()) {
				handItemStack = ((DuckPlayerInventoryMixin) player.getInventory()).rpginventory$getSheathedHand().copy();
			}
			ItemStack offHandItemStack = player.getInventory().offhand.get(0).copy();
			if (offHandItemStack.isEmpty()) {
				offHandItemStack = ((DuckPlayerInventoryMixin) player.getInventory()).rpginventory$getSheathedOffhand().copy();
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
			if (((DuckPlayerEntityMixin) player).rpginventory$isHandStackSheathed() && ((DuckPlayerEntityMixin) player).rpginventory$isOffhandStackSheathed()) {
				((DuckPlayerEntityMixin) player).rpginventory$setIsHandStackSheathed(false);
				((DuckPlayerEntityMixin) player).rpginventory$setIsOffhandStackSheathed(false);
				((DuckPlayerInventoryMixin) player.getInventory()).rpginventory$setHand(handItemStack);
				((DuckPlayerInventoryMixin) player.getInventory()).rpginventory$setSheathedHand(ItemStack.EMPTY);
				player.getInventory().offhand.set(0, offHandItemStack);
				((DuckPlayerInventoryMixin) player.getInventory()).rpginventory$setSheathedOffhand(ItemStack.EMPTY);
			} else {
				((DuckPlayerEntityMixin) player).rpginventory$setIsHandStackSheathed(true);
				((DuckPlayerEntityMixin) player).rpginventory$setIsOffhandStackSheathed(true);
				((DuckPlayerInventoryMixin) player.getInventory()).rpginventory$setHand(ItemStack.EMPTY);
				((DuckPlayerInventoryMixin) player.getInventory()).rpginventory$setSheathedHand(handItemStack);
				player.getInventory().offhand.set(0, ItemStack.EMPTY);
				((DuckPlayerInventoryMixin) player.getInventory()).rpginventory$setSheathedOffhand(offHandItemStack);
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
