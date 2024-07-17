package com.github.theredbrain.rpginventory.entity;

import net.minecraft.item.ItemStack;

import java.util.function.Predicate;

public interface DuckLivingEntityMixin {

	boolean rpginventory$hasEquipped(Predicate<ItemStack> predicate);

	int rpginventory$getAmountEquipped(Predicate<ItemStack> predicate);
}
