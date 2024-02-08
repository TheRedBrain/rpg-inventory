package com.github.theredbrain.betteradventuremode.entity;

import net.minecraft.item.ItemStack;

public interface IRenderEquippedTrinkets {

    boolean isMainHandItemSheathed();
    boolean isOffHandItemSheathed();

    ItemStack getMainHandItemStack();
    ItemStack getOffHandItemStack();
}
