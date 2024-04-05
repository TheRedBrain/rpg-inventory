package com.github.theredbrain.betteradventuremode.mixin.item;

import net.minecraft.item.FoodComponent;
import net.minecraft.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Item.class)
public interface TemporaryItemAccessor {

    @Accessor("foodComponent")
    void betteradventuremode$setFoodComponent(FoodComponent foodComponent);
}
