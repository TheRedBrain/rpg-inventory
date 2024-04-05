package com.github.theredbrain.betteradventuremode.item;

import com.github.theredbrain.betteradventuremode.registry.StatusEffectsRegistry;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.FoodComponent;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;

public class AdventureFoodComponents {

    // modified vanilla items
    public static final FoodComponent APPLE = (new FoodComponent.Builder()).hunger(0).saturationModifier(0.0F).statusEffect(new StatusEffectInstance(StatusEffectsRegistry.APPLE_FOOD_EFFECT, 12000, 0, false, false, true), 1.0F).build();
    
    public static final FoodComponent BAKED_POTATO = (new FoodComponent.Builder()).hunger(0).saturationModifier(0.0F).statusEffect(new StatusEffectInstance(StatusEffectsRegistry.BAKED_POTATO_FOOD_EFFECT, 12000, 0, false, false, true), 1.0F).build();
    
    public static final FoodComponent BEEF = (new FoodComponent.Builder()).hunger(0).saturationModifier(0.0F).statusEffect(new StatusEffectInstance(StatusEffectsRegistry.BEEF_FOOD_EFFECT, 12000, 0, false, false, true), 1.0F).build();
    
    public static final FoodComponent BEETROOT = (new FoodComponent.Builder()).hunger(0).saturationModifier(0.0F).statusEffect(new StatusEffectInstance(StatusEffectsRegistry.BEETROOT_FOOD_EFFECT, 12000, 0, false, false, true), 1.0F).build();
    
    public static final FoodComponent BEETROOT_SOUP = (new FoodComponent.Builder()).hunger(0).saturationModifier(0.0F).statusEffect(new StatusEffectInstance(StatusEffectsRegistry.BEETROOT_SOUP_FOOD_EFFECT, 12000, 0, false, false, true), 1.0F).build();
    
    public static final FoodComponent BREAD = (new FoodComponent.Builder()).hunger(0).saturationModifier(0.0F).statusEffect(new StatusEffectInstance(StatusEffectsRegistry.BREAD_FOOD_EFFECT, 12000, 0, false, false, true), 1.0F).build();
    
    public static final FoodComponent CARROT = (new FoodComponent.Builder()).hunger(0).saturationModifier(0.0F).statusEffect(new StatusEffectInstance(StatusEffectsRegistry.CARROT_FOOD_EFFECT, 12000, 0, false, false, true), 1.0F).build();
    
    public static final FoodComponent CHICKEN = (new FoodComponent.Builder()).hunger(0).saturationModifier(0.0F).statusEffect(new StatusEffectInstance(StatusEffectsRegistry.CHICKEN_FOOD_EFFECT, 12000, 0, false, false, true), 1.0F).build();
    
    public static final FoodComponent CHORUS_FRUIT = (new FoodComponent.Builder()).hunger(0).saturationModifier(0.0F).statusEffect(new StatusEffectInstance(StatusEffectsRegistry.CHORUS_FRUIT_FOOD_EFFECT, 12000, 0, false, false, true), 1.0F).build();
    
    public static final FoodComponent COD = (new FoodComponent.Builder()).hunger(0).saturationModifier(0.0F).statusEffect(new StatusEffectInstance(StatusEffectsRegistry.COD_FOOD_EFFECT, 12000, 0, false, false, true), 1.0F).build();
    
    public static final FoodComponent COOKED_BEEF = (new FoodComponent.Builder()).hunger(0).saturationModifier(0.0F).statusEffect(new StatusEffectInstance(StatusEffectsRegistry.COOKED_BEEF_FOOD_EFFECT, 12000, 0, false, false, true), 1.0F).build();
    
    public static final FoodComponent COOKED_CHICKEN = (new FoodComponent.Builder()).hunger(0).saturationModifier(0.0F).statusEffect(new StatusEffectInstance(StatusEffectsRegistry.COOKED_CHICKEN_FOOD_EFFECT, 12000, 0, false, false, true), 1.0F).build();
    
    public static final FoodComponent COOKED_COD = (new FoodComponent.Builder()).hunger(0).saturationModifier(0.0F).statusEffect(new StatusEffectInstance(StatusEffectsRegistry.COOKED_COD_FOOD_EFFECT, 12000, 0, false, false, true), 1.0F).build();
    
    public static final FoodComponent COOKED_MUTTON = (new FoodComponent.Builder()).hunger(0).saturationModifier(0.0F).statusEffect(new StatusEffectInstance(StatusEffectsRegistry.COOKED_MUTTON_FOOD_EFFECT, 12000, 0, false, false, true), 1.0F).build();
    
    public static final FoodComponent COOKED_PORKCHOP = (new FoodComponent.Builder()).hunger(0).saturationModifier(0.0F).statusEffect(new StatusEffectInstance(StatusEffectsRegistry.COOKED_PORKCHOP_FOOD_EFFECT, 12000, 0, false, false, true), 1.0F).build();
    
    public static final FoodComponent COOKED_RABBIT = (new FoodComponent.Builder()).hunger(0).saturationModifier(0.0F).statusEffect(new StatusEffectInstance(StatusEffectsRegistry.COOKED_RABBIT_FOOD_EFFECT, 12000, 0, false, false, true), 1.0F).build();
    
    public static final FoodComponent COOKED_SALMON = (new FoodComponent.Builder()).hunger(0).saturationModifier(0.0F).statusEffect(new StatusEffectInstance(StatusEffectsRegistry.COOKED_SALMON_FOOD_EFFECT, 12000, 0, false, false, true), 1.0F).build();
    
    public static final FoodComponent COOKIE = (new FoodComponent.Builder()).hunger(0).saturationModifier(0.0F).statusEffect(new StatusEffectInstance(StatusEffectsRegistry.COOKIE_FOOD_EFFECT, 12000, 0, false, false, true), 1.0F).build();
    
    public static final FoodComponent DRIED_KELP = (new FoodComponent.Builder()).hunger(0).saturationModifier(0.0F).statusEffect(new StatusEffectInstance(StatusEffectsRegistry.DRIED_KELP_FOOD_EFFECT, 12000, 0, false, false, true), 1.0F).build();
    
    public static final FoodComponent ENCHANTED_GOLDEN_APPLE = (new FoodComponent.Builder()).hunger(0).saturationModifier(0.0F).statusEffect(new StatusEffectInstance(StatusEffectsRegistry.ENCHANTED_GOLDEN_APPLE_FOOD_EFFECT, 12000, 0, false, false, true), 1.0F).build();
    
    public static final FoodComponent GOLDEN_APPLE = (new FoodComponent.Builder()).hunger(0).saturationModifier(0.0F).statusEffect(new StatusEffectInstance(StatusEffectsRegistry.GOLDEN_APPLE_FOOD_EFFECT, 12000, 0, false, false, true), 1.0F).build();
    
    public static final FoodComponent GOLDEN_CARROT = (new FoodComponent.Builder()).hunger(0).saturationModifier(0.0F).statusEffect(new StatusEffectInstance(StatusEffectsRegistry.GOLDEN_CARROT_FOOD_EFFECT, 12000, 0, false, false, true), 1.0F).build();
    
    public static final FoodComponent HONEY_BOTTLE = (new FoodComponent.Builder()).hunger(0).saturationModifier(0.0F).statusEffect(new StatusEffectInstance(StatusEffectsRegistry.HONEY_BOTTLE_FOOD_EFFECT, 12000, 0, false, false, true), 1.0F).build();
    
    public static final FoodComponent MELON_SLICE = (new FoodComponent.Builder()).hunger(0).saturationModifier(0.0F).statusEffect(new StatusEffectInstance(StatusEffectsRegistry.MELON_SLICE_FOOD_EFFECT, 12000, 0, false, false, true), 1.0F).build();
    
    public static final FoodComponent MUSHROOM_STEW = (new FoodComponent.Builder()).hunger(0).saturationModifier(0.0F).statusEffect(new StatusEffectInstance(StatusEffectsRegistry.MUSHROOM_STEW_FOOD_EFFECT, 12000, 0, false, false, true), 1.0F).build();
    
    public static final FoodComponent MUTTON = (new FoodComponent.Builder()).hunger(0).saturationModifier(0.0F).statusEffect(new StatusEffectInstance(StatusEffectsRegistry.MUTTON_FOOD_EFFECT, 12000, 0, false, false, true), 1.0F).build();
    
    public static final FoodComponent POISONOUS_POTATO = (new FoodComponent.Builder()).hunger(0).saturationModifier(0.0F).statusEffect(new StatusEffectInstance(StatusEffectsRegistry.POISONOUS_POTATO_FOOD_EFFECT, 12000, 0, false, false, true), 1.0F).build();
    
    public static final FoodComponent PORKCHOP = (new FoodComponent.Builder()).hunger(0).saturationModifier(0.0F).statusEffect(new StatusEffectInstance(StatusEffectsRegistry.PORKCHOP_FOOD_EFFECT, 12000, 0, false, false, true), 1.0F).build();
    
    public static final FoodComponent POTATO = (new FoodComponent.Builder()).hunger(0).saturationModifier(0.0F).statusEffect(new StatusEffectInstance(StatusEffectsRegistry.POTATO_FOOD_EFFECT, 12000, 0, false, false, true), 1.0F).build();
    
    public static final FoodComponent PUFFERFISH = (new FoodComponent.Builder()).hunger(0).saturationModifier(0.0F).statusEffect(new StatusEffectInstance(StatusEffectsRegistry.PUFFERFISH_FOOD_EFFECT, 12000, 0, false, false, true), 1.0F).build();
    
    public static final FoodComponent PUMPKIN_PIE = (new FoodComponent.Builder()).hunger(0).saturationModifier(0.0F).statusEffect(new StatusEffectInstance(StatusEffectsRegistry.PUMPKIN_PIE_FOOD_EFFECT, 12000, 0, false, false, true), 1.0F).build();
    
    public static final FoodComponent RABBIT = (new FoodComponent.Builder()).hunger(0).saturationModifier(0.0F).statusEffect(new StatusEffectInstance(StatusEffectsRegistry.RABBIT_FOOD_EFFECT, 12000, 0, false, false, true), 1.0F).build();
    
    public static final FoodComponent RABBIT_STEW = (new FoodComponent.Builder()).hunger(0).saturationModifier(0.0F).statusEffect(new StatusEffectInstance(StatusEffectsRegistry.RABBIT_STEW_FOOD_EFFECT, 12000, 0, false, false, true), 1.0F).build();
    
    public static final FoodComponent ROTTEN_FLESH = (new FoodComponent.Builder()).hunger(0).saturationModifier(0.0F).statusEffect(new StatusEffectInstance(StatusEffectsRegistry.ROTTEN_FLESH_FOOD_EFFECT, 12000, 0, false, false, true), 1.0F).build();
    
    public static final FoodComponent SALMON = (new FoodComponent.Builder()).hunger(0).saturationModifier(0.0F).statusEffect(new StatusEffectInstance(StatusEffectsRegistry.SALMON_FOOD_EFFECT, 12000, 0, false, false, true), 1.0F).build();
    
    public static final FoodComponent SPIDER_EYE = (new FoodComponent.Builder()).hunger(0).saturationModifier(0.0F).statusEffect(new StatusEffectInstance(StatusEffectsRegistry.SPIDER_EYE_FOOD_EFFECT, 12000, 0, false, false, true), 1.0F).build();
    
    public static final FoodComponent SUSPICIOUS_STEW = (new FoodComponent.Builder()).hunger(0).saturationModifier(0.0F).statusEffect(new StatusEffectInstance(StatusEffectsRegistry.SUSPICIOUS_STEW_FOOD_EFFECT, 12000, 0, false, false, true), 1.0F).build();
    
    public static final FoodComponent SWEET_BERRIES = (new FoodComponent.Builder()).hunger(0).saturationModifier(0.0F).statusEffect(new StatusEffectInstance(StatusEffectsRegistry.SWEET_BERRIES_FOOD_EFFECT, 12000, 0, false, false, true), 1.0F).build();
    
    public static final FoodComponent GLOW_BERRIES = (new FoodComponent.Builder()).hunger(0).saturationModifier(0.0F).statusEffect(new StatusEffectInstance(StatusEffectsRegistry.GLOW_BERRIES_FOOD_EFFECT, 12000, 0, false, false, true), 1.0F).build();
    
    public static final FoodComponent TROPICAL_FISH = (new FoodComponent.Builder()).hunger(0).saturationModifier(0.0F).statusEffect(new StatusEffectInstance(StatusEffectsRegistry.TROPICAL_FISH_FOOD_EFFECT, 12000, 0, false, false, true), 1.0F).build();

    // additional vanilla items
    public static final FoodComponent BROWN_MUSHROOM = (new FoodComponent.Builder()).hunger(0).saturationModifier(0.0F).statusEffect(new StatusEffectInstance(StatusEffectsRegistry.BROWN_MUSHROOM_FOOD_EFFECT, 12000, 0, false, false, true), 1.0F).build();
    public static final FoodComponent COCOA_BEANS = (new FoodComponent.Builder()).hunger(0).saturationModifier(0.0F).statusEffect(new StatusEffectInstance(StatusEffectsRegistry.COCOA_BEANS_FOOD_EFFECT, 12000, 0, false, false, true), 1.0F).build();
    public static final FoodComponent FERMENTED_SPIDER_EYE = (new FoodComponent.Builder()).hunger(0).saturationModifier(0.0F).statusEffect(new StatusEffectInstance(StatusEffectsRegistry.FERMENTED_SPIDER_EYE_FOOD_EFFECT, 12000, 0, false, false, true), 1.0F).build();
    public static final FoodComponent RED_MUSHROOM = (new FoodComponent.Builder()).hunger(0).saturationModifier(0.0F).statusEffect(new StatusEffectInstance(StatusEffectsRegistry.RED_MUSHROOM_FOOD_EFFECT, 12000, 0, false, false, true), 1.0F).build();
    public static final FoodComponent SUGAR = (new FoodComponent.Builder()).hunger(0).saturationModifier(0.0F).statusEffect(new StatusEffectInstance(StatusEffectsRegistry.SUGAR_FOOD_EFFECT, 600, 0, false, false, true), 1.0F).build();

}
