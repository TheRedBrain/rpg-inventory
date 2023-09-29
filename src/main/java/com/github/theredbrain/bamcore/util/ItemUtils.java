package com.github.theredbrain.bamcore.util;

import com.github.theredbrain.bamcore.registry.Tags;
import net.minecraft.item.ItemStack;

public class ItemUtils {

    public static boolean isUsable(ItemStack stack) {
        return stack.getDamage() < stack.getMaxDamage() - 1 || stack.isIn(Tags.EMPTY_HAND_WEAPONS);
    }
}
