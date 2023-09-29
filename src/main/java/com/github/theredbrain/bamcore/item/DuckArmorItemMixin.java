package com.github.theredbrain.bamcore.item;

import net.minecraft.item.ItemStack;

public interface DuckArmorItemMixin {
    boolean isProtecting(ItemStack stack);
    boolean isDamageable();
}
