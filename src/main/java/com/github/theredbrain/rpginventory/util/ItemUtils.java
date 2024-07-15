package com.github.theredbrain.rpginventory.util;

import com.github.theredbrain.rpginventory.registry.Tags;
import net.minecraft.item.ItemStack;

public class ItemUtils {

	/**
	 * @return whether this item should provide its attribute modifiers and if it should be rendered
	 */
	public static boolean isUsable(ItemStack stack) {
		return stack.getDamage() < stack.getMaxDamage() - 1 || stack.isIn(Tags.EMPTY_HAND_WEAPONS) || !stack.isIn(Tags.UNUSABLE_WHEN_LOW_DURABILITY);
	}
}
