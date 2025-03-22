package com.github.theredbrain.rpginventory.util;

import com.github.theredbrain.rpginventory.RPGInventory;
import com.github.theredbrain.rpginventory.registry.Tags;
import com.mojang.authlib.GameProfile;
import net.minecraft.component.type.ProfileComponent;
import net.minecraft.item.ItemStack;

public class ItemUtils {

	/**
	 * @return whether this item should provide its attribute modifiers and if it should be rendered
	 */
	public static boolean isUsable(ItemStack stack) {
		return stack.getMaxDamage() <= 0 || stack.getDamage() < stack.getMaxDamage() - 1 || stack.isIn(Tags.EMPTY_HAND_WEAPONS) || !stack.isIn(Tags.UNUSABLE_WHEN_LOW_DURABILITY);
	}

	/**
	 * @return whether this item is owned by the player and should be usable
	 */
	public static boolean isOwnedByPlayer(ItemStack stack, GameProfile gameProfile) {
		ProfileComponent profileComponent = stack.get(RPGInventory.PLAYER_BOUND);
		if (profileComponent != null) {
			return profileComponent.gameProfile().equals(gameProfile);
		}
		return true;
	}
}
