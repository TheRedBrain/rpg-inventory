package com.github.theredbrain.rpginventory.network.packet;

import com.github.theredbrain.rpginventory.RPGInventory;
import com.github.theredbrain.rpginventory.config.ServerConfig;
import com.github.theredbrain.rpginventory.entity.player.DuckPlayerEntityMixin;
import com.github.theredbrain.rpginventory.entity.player.DuckPlayerInventoryMixin;
import com.github.theredbrain.rpginventory.registry.Tags;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;

public class ToggleTwoHandedStancePacketReceiver implements ServerPlayNetworking.PlayPayloadHandler<ToggleTwoHandedStancePacket> {
	@Override
	public void receive(ToggleTwoHandedStancePacket payload, ServerPlayNetworking.Context context) {

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

			boolean mainHandCanNotBeTwoHanded = player.getMainHandStack().isIn(Tags.NON_TWO_HANDED_ITEMS);

			float staminaCost = RPGInventory.isStaminaAttributesLoaded ? serverConfig.handSlotOverhaul.staminaAttributesCompat.toggling_two_handed_stance_stamina_cost.get() : 0.0F;

			if (staminaCost > 0.0F && !player.isCreative() && serverConfig.handSlotOverhaul.staminaAttributesCompat.toggling_two_handed_stance_requires_stamina.get() && RPGInventory.getCurrentStamina(player) <= 0 && (!serverConfig.handSlotOverhaul.staminaAttributesCompat.toggling_two_handed_stance_requires_stamina_cost.get() || RPGInventory.getCurrentStamina(player) < staminaCost)) {
				player.sendMessageToClient(Text.translatable("hud.message.staminaTooLow"), true);
				return;
			} else if (((DuckPlayerEntityMixin) player).rpginventory$isHandStackSheathed() && ((DuckPlayerEntityMixin) player).rpginventory$isOffhandStackSheathed()) {
				if (!serverConfig.handSlotOverhaul.always_allow_toggling_two_handed_stance.get()) {
					player.sendMessageToClient(Text.translatable("hud.message.weaponsAreSheathed"), true);
					return;
				} else {
					if (mainHandCanNotBeTwoHanded) {
						player.sendMessageToClient(Text.translatable("hud.message.nonTwoHandedWeaponEquipped"), true);
						return;
					}
					((DuckPlayerEntityMixin) player).rpginventory$setIsHandStackSheathed(false);
					((DuckPlayerInventoryMixin) player.getInventory()).rpginventory$setHand(handItemStack);
					((DuckPlayerInventoryMixin) player.getInventory()).rpginventory$setSheathedHand(ItemStack.EMPTY);
				}
			} else if (((DuckPlayerEntityMixin) player).rpginventory$isOffhandStackSheathed()) {
				((DuckPlayerEntityMixin) player).rpginventory$setIsOffhandStackSheathed(false);
				player.getInventory().offHand.set(0, offHandItemStack);
				((DuckPlayerInventoryMixin) player.getInventory()).rpginventory$setSheathedOffhand(ItemStack.EMPTY);
			} else {
				if (mainHandCanNotBeTwoHanded) {
					player.sendMessageToClient(Text.translatable("hud.message.nonTwoHandedWeaponEquipped"), true);
					return;
				}
				((DuckPlayerEntityMixin) player).rpginventory$setIsOffhandStackSheathed(true);
				player.getInventory().offHand.set(0, ItemStack.EMPTY);
				((DuckPlayerInventoryMixin) player.getInventory()).rpginventory$setSheathedOffhand(offHandItemStack);
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
