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

public class SheatheWeaponsPacketReceiver implements ServerPlayNetworking.PlayPayloadHandler<SheatheWeaponsPacket> {

	@Override
	public void receive(SheatheWeaponsPacket payload, ServerPlayNetworking.Context context) {

		ServerPlayerEntity player = context.player();

		if (RPGInventory.isHandSlotOverhaulActive()) {

			ServerConfig serverConfig = RPGInventory.SERVER_CONFIG;

			ItemStack handItemStack = ((DuckPlayerInventoryMixin) player.getInventory()).rpginventory$getHand().copy();
			if (handItemStack.isEmpty()) {
				handItemStack = ((DuckPlayerInventoryMixin) player.getInventory()).rpginventory$getSheathedHand().copy();
			}
			ItemStack offHandItemStack = player.getInventory().offHand.get(0).copy();
			if (offHandItemStack.isEmpty()) {
				offHandItemStack = ((DuckPlayerInventoryMixin) player.getInventory()).rpginventory$getSheathedOffhand().copy();
			}

			if (RPGInventory.doesCurrentPlayerStatusPreventHandSlotAction(player) ||
					player.getItemCooldownManager().isCoolingDown(handItemStack.getItem()) ||
					player.getItemCooldownManager().isCoolingDown(offHandItemStack.getItem())
			) {
				player.sendMessageToClient(Text.translatable("hud.message.handSlotActionWasPrevented"), true);
				return;
			}

			float staminaCost = RPGInventory.isStaminaAttributesLoaded ? serverConfig.handSlotOverhaul.staminaAttributesCompat.sheathing_hand_items_stamina_cost.get() : 0.0F;

			if (staminaCost > 0.0F && !player.isCreative() && serverConfig.handSlotOverhaul.staminaAttributesCompat.sheathing_hand_items_requires_stamina.get() && RPGInventory.getCurrentStamina(player) <= 0 && (!serverConfig.handSlotOverhaul.staminaAttributesCompat.sheathing_hand_items_requires_stamina_cost.get() || RPGInventory.getCurrentStamina(player) < staminaCost)) {
				player.sendMessageToClient(Text.translatable("hud.message.staminaTooLow"), true);
				return;
			}
			if (((DuckPlayerEntityMixin) player).rpginventory$isHandStackSheathed() && ((DuckPlayerEntityMixin) player).rpginventory$isOffhandStackSheathed()) {
				((DuckPlayerEntityMixin) player).rpginventory$setIsHandStackSheathed(false);
				((DuckPlayerEntityMixin) player).rpginventory$setIsOffhandStackSheathed(false);
				((DuckPlayerInventoryMixin) player.getInventory()).rpginventory$setHand(handItemStack);
				((DuckPlayerInventoryMixin) player.getInventory()).rpginventory$setSheathedHand(ItemStack.EMPTY);
				player.getInventory().offHand.set(0, offHandItemStack);
				((DuckPlayerInventoryMixin) player.getInventory()).rpginventory$setSheathedOffhand(ItemStack.EMPTY);
			} else {
				((DuckPlayerEntityMixin) player).rpginventory$setIsHandStackSheathed(true);
				((DuckPlayerEntityMixin) player).rpginventory$setIsOffhandStackSheathed(true);
				((DuckPlayerInventoryMixin) player.getInventory()).rpginventory$setHand(ItemStack.EMPTY);
				((DuckPlayerInventoryMixin) player.getInventory()).rpginventory$setSheathedHand(handItemStack);
				player.getInventory().offHand.set(0, ItemStack.EMPTY);
				((DuckPlayerInventoryMixin) player.getInventory()).rpginventory$setSheathedOffhand(offHandItemStack);
			}
			if (staminaCost != 0.0F && !player.isCreative()) {
				RPGInventory.addStamina(player, -staminaCost);
			}
//			if (serverConfig.handSlotOverhaul.enable_item_cooldown_after_hand_sheathing.get()) {
//				player.getItemCooldownManager().set(player.getMainHandStack().getItem(), serverConfig.handSlotOverhaul.sheathing_main_hand_cooldown.get());
//				player.getItemCooldownManager().set(player.getOffHandStack().getItem(), serverConfig.handSlotOverhaul.sheathing_offhand_cooldown.get());
//			}
			player.getServerWorld().playSound(null, player.getBlockPos().getX(), player.getBlockPos().getY(), player.getBlockPos().getZ(), SoundEvents.ITEM_ARMOR_EQUIP_GENERIC, SoundCategory.PLAYERS, 1.0F, 1.0F);
		} else {
			player.sendMessageToClient(Text.translatable("hud.message.handSlotOverhaulIsDisabledByServer"), true);
		}
	}
}
