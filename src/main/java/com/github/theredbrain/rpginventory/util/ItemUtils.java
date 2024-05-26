package com.github.theredbrain.rpginventory.util;

import com.github.theredbrain.rpginventory.registry.Tags;
import net.minecraft.item.ItemStack;

public class ItemUtils {

    /**
     * {@return whether this item should provide its attribute modifiers and if should be rendered} // TODO only items in a tag?
     */
    public static boolean isUsable(ItemStack stack) {
        return stack.getDamage() < stack.getMaxDamage() - 1 || stack.isIn(Tags.EMPTY_HAND_WEAPONS);
    }
}
