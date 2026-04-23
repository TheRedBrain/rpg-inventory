package com.github.theredbrain.rpginventory.util;

import com.github.theredbrain.rpginventory.entity.player.DuckPlayerEntityMixin;
import com.github.theredbrain.rpginventory.entity.player.DuckPlayerInventoryMixin;
import com.google.common.collect.Multimap;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class SwapHandAttributesHelper {
	public static void swapHandAttributes(Player player, Runnable runnable) {

		synchronized (player) {
			if (!((DuckPlayerEntityMixin) player).rpginventory$isHandStackSheathed() && !((DuckPlayerEntityMixin) player).rpginventory$isOffhandStackSheathed()) {
				boolean isMainHandEmpty = ((DuckPlayerInventoryMixin) player.getInventory()).rpginventory$getHand().isEmpty();
				boolean isOffhandEmpty = player.getInventory().offhand.get(0).isEmpty();
				Inventory inventory = player.getInventory();
				ItemStack mainHandStack = isMainHandEmpty ? ((DuckPlayerInventoryMixin) player.getInventory()).rpginventory$getEmptyHand() : ((DuckPlayerInventoryMixin) player.getInventory()).rpginventory$getHand();
				ItemStack offHandStack = isOffhandEmpty ? ((DuckPlayerInventoryMixin) player.getInventory()).rpginventory$getEmptyOffhand() : inventory.offhand.get(0);

				setAttributesForOffHandAttack(player, true, mainHandStack, offHandStack);
				if (isMainHandEmpty) {
					((DuckPlayerInventoryMixin) player.getInventory()).rpginventory$setEmptyHand(offHandStack);
				} else {
					((DuckPlayerInventoryMixin) player.getInventory()).rpginventory$setHand(offHandStack);
				}
				if (isOffhandEmpty) {
					((DuckPlayerInventoryMixin) player.getInventory()).rpginventory$setEmptyOffhand(offHandStack);
				} else {
					inventory.offhand.set(0, offHandStack);
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
					inventory.offhand.set(0, offHandStack);
				}
				setAttributesForOffHandAttack(player, false, mainHandStack, offHandStack);
			}
		}
	}

	private static void setAttributesForOffHandAttack(Player player, boolean useOffHand, ItemStack mainHandStack, ItemStack offHandStack) {
		ItemStack add;
		ItemStack remove;
		if (useOffHand) {
			remove = mainHandStack;
			add = offHandStack;
		} else {
			remove = offHandStack;
			add = mainHandStack;
		}

		Multimap<Holder<Attribute>, AttributeModifier> modifiersMap;
		if (remove != null) {
			modifiersMap = AttributeModifierHelper.modifierMultimap(remove);
			player.getAttributes().removeAttributeModifiers(modifiersMap);
		}

		if (add != null) {
			modifiersMap = AttributeModifierHelper.modifierMultimap(add);
			player.getAttributes().addTransientAttributeModifiers(modifiersMap);
		}

	}

}
