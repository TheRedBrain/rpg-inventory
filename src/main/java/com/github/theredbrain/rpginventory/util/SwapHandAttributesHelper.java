package com.github.theredbrain.rpginventory.util;

import com.github.theredbrain.rpginventory.entity.player.DuckPlayerEntityMixin;
import com.github.theredbrain.rpginventory.entity.player.DuckPlayerInventoryMixin;
import com.google.common.collect.Multimap;
import net.bettercombat.utils.AttributeModifierHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;

public class SwapHandAttributesHelper {
	public static void swapHandAttributes(PlayerEntity player, Runnable runnable) {

		synchronized (player) {
			if (!((DuckPlayerEntityMixin) player).rpginventory$isHandStackSheathed() && !((DuckPlayerEntityMixin) player).rpginventory$isOffhandStackSheathed()) {
				boolean isMainHandEmpty = ((DuckPlayerInventoryMixin) player.getInventory()).rpginventory$getHand().isEmpty();
				boolean isOffhandEmpty = player.getInventory().offHand.get(0).isEmpty();
				PlayerInventory inventory = player.getInventory();
				ItemStack mainHandStack = isMainHandEmpty ? ((DuckPlayerInventoryMixin) player.getInventory()).rpginventory$getEmptyHand() : ((DuckPlayerInventoryMixin) player.getInventory()).rpginventory$getHand();
				ItemStack offHandStack = isOffhandEmpty ? ((DuckPlayerInventoryMixin) player.getInventory()).rpginventory$getEmptyOffhand() : inventory.offHand.get(0);

				setAttributesForOffHandAttack(player, true, mainHandStack, offHandStack);
				if (isMainHandEmpty) {
					((DuckPlayerInventoryMixin) player.getInventory()).rpginventory$setEmptyHand(offHandStack);
				} else {
					((DuckPlayerInventoryMixin) player.getInventory()).rpginventory$setHand(offHandStack);
				}
				if (isOffhandEmpty) {
					((DuckPlayerInventoryMixin) player.getInventory()).rpginventory$setEmptyOffhand(offHandStack);
				} else {
					inventory.offHand.set(0, offHandStack);
				}

				runnable.run();

				if (isMainHandEmpty) {
					((DuckPlayerInventoryMixin) player.getInventory()).rpginventory$setEmptyHand(mainHandStack);
				} else {
					((DuckPlayerInventoryMixin) player.getInventory()).rpginventory$setHand(mainHandStack);
				}
				if (isOffhandEmpty) {
					((DuckPlayerInventoryMixin) player.getInventory()).rpginventory$setEmptyOffhand(offHandStack);
				} else {
					inventory.offHand.set(0, offHandStack);
				}
				setAttributesForOffHandAttack(player, false, mainHandStack, offHandStack);
			}
		}
	}

	private static void setAttributesForOffHandAttack(PlayerEntity player, boolean useOffHand, ItemStack mainHandStack, ItemStack offHandStack) {
		ItemStack add;
		ItemStack remove;
		if (useOffHand) {
			remove = mainHandStack;
			add = offHandStack;
		} else {
			remove = offHandStack;
			add = mainHandStack;
		}

		Multimap modifiersMap;
		if (remove != null) {
			modifiersMap = AttributeModifierHelper.modifierMultimap(remove);
			player.getAttributes().removeModifiers(modifiersMap);
		}

		if (add != null) {
			modifiersMap = AttributeModifierHelper.modifierMultimap(add);
			player.getAttributes().addTemporaryModifiers(modifiersMap);
		}

	}

}
