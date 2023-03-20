package com.github.theredbrain.rpgmod.item;

import com.github.theredbrain.rpgmod.registry.StatusEffectsRegistry;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.FoodComponent;

public class AdventureFoodComponents {
    public static final FoodComponent BERRY_FOOD = new FoodComponent.Builder().hunger(0).saturationModifier(0.0f).statusEffect(new StatusEffectInstance(StatusEffectsRegistry.BERRY_FOOD_EFFECT, 600, 0, false, false, true), 1.0f).build();
    public static final FoodComponent BROWN_MUSHROOM_FOOD = new FoodComponent.Builder().hunger(0).saturationModifier(0.0f).statusEffect(new StatusEffectInstance(StatusEffectsRegistry.BROWN_MUSHROOM_FOOD_EFFECT, 600, 0, false, false, true), 1.0f).build();
    public static final FoodComponent RED_MUSHROOM_FOOD = new FoodComponent.Builder().hunger(0).saturationModifier(0.0f).statusEffect(new StatusEffectInstance(StatusEffectsRegistry.RED_MUSHROOM_FOOD_EFFECT, 600, 0, false, false, true), 1.0f).build();
}
