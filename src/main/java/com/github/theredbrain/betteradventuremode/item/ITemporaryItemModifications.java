package com.github.theredbrain.betteradventuremode.item;

import net.minecraft.item.FoodComponent;
import net.minecraft.item.Item;

// TODO remove when data-driven blocks are in vanilla
public interface ITemporaryItemModifications {
    ITemporaryItemModifications INSTANCE = new TemporaryItemModifications();
    void betteradventuremode$setFoodComponent(Item item, FoodComponent foodComponent);
}
