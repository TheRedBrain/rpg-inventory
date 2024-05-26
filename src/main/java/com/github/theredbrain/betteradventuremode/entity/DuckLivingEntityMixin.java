package com.github.theredbrain.betteradventuremode.entity;

import net.minecraft.item.ItemStack;

import java.util.function.Predicate;

public interface DuckLivingEntityMixin {

    boolean betteradventuremode$hasEquipped(Predicate<ItemStack> predicate);
    int betteradventuremode$getAmountEquipped(Predicate<ItemStack> predicate);
}
