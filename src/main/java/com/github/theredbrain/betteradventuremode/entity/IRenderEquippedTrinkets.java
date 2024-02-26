package com.github.theredbrain.betteradventuremode.entity;

import net.minecraft.item.ItemStack;

public interface IRenderEquippedTrinkets {

    boolean betteradventuremode$isMainHandItemSheathed();
    boolean betteradventuremode$isOffHandItemSheathed();

    ItemStack betteradventuremode$getMainHandItemStack();
    ItemStack betteradventuremode$getOffHandItemStack();
}
