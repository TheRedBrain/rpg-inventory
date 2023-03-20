package com.github.theredbrain.rpgmod.item;

import net.minecraft.item.ItemStack;

public interface DuckArmorItemMixin {
    boolean isProtecting(ItemStack stack);
    boolean isDamageable();
}
