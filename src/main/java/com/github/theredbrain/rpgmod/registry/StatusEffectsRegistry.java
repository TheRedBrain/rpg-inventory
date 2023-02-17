package com.github.theredbrain.rpgmod.registry;

import com.github.theredbrain.rpgmod.RPGMod;
import com.github.theredbrain.rpgmod.effect.BerryFoodEffect;
import com.github.theredbrain.rpgmod.effect.BrownMushroomFoodEffect;
import com.github.theredbrain.rpgmod.effect.ChickenMealFoodEffect;
import com.github.theredbrain.rpgmod.effect.RedMushroomFoodEffect;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class StatusEffectsRegistry {

    public static final StatusEffect BERRY_FOOD_EFFECT = new BerryFoodEffect();
    public static final StatusEffect BROWN_MUSHROOM_FOOD_EFFECT = new BrownMushroomFoodEffect();
    public static final StatusEffect CHICKEN_MEAL_FOOD_EFFECT = new ChickenMealFoodEffect();
    public static final StatusEffect RED_MUSHROOM_FOOD_EFFECT = new RedMushroomFoodEffect();

    public static void registerEffects() {
        Registry.register(Registry.STATUS_EFFECT, new Identifier(RPGMod.MOD_ID, "berry_food_effect"), BERRY_FOOD_EFFECT);
        Registry.register(Registry.STATUS_EFFECT, new Identifier(RPGMod.MOD_ID, "brown_mushroom_food_effect"), BROWN_MUSHROOM_FOOD_EFFECT);
        Registry.register(Registry.STATUS_EFFECT, new Identifier(RPGMod.MOD_ID, "chicken_meal_food_effect"), CHICKEN_MEAL_FOOD_EFFECT);
        Registry.register(Registry.STATUS_EFFECT, new Identifier(RPGMod.MOD_ID, "red_mushroom_food_effect"), RED_MUSHROOM_FOOD_EFFECT);
    }
}
