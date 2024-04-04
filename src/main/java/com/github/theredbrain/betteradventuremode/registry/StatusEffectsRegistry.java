package com.github.theredbrain.betteradventuremode.registry;

import com.github.theredbrain.betteradventuremode.BetterAdventureMode;
import com.github.theredbrain.betteradventuremode.effect.AuraStatusEffect;
import com.github.theredbrain.betteradventuremode.effect.FoodStatusEffect;
import com.github.theredbrain.betteradventuremode.spell_engine.ExtendedEntityActionsAllowedSemanticType;
import com.github.theredbrain.betteradventuremode.util.AttributeModifierUUIDs;
import com.github.theredbrain.betteradventuremode.effect.*;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.spell_engine.api.effect.ActionImpairing;
import net.spell_engine.api.effect.EntityActionsAllowed;
import net.spell_engine.api.effect.Synchronized;

public class StatusEffectsRegistry {

    //region Food Effects
    public static final StatusEffect APPLE_FOOD_EFFECT = new FoodStatusEffect()
//            .addAttributeModifier(EntityAttributes.GENERIC_MAX_HEALTH, AttributeModifierUUIDs.APPLE_FOOD_EFFECT, 1.0F, EntityAttributeModifier.Operation.ADDITION)
//            .addAttributeModifier(EntityAttributesRegistry.HEALTH_REGENERATION, AttributeModifierUUIDs.APPLE_FOOD_EFFECT, 1.0F, EntityAttributeModifier.Operation.ADDITION)
            .addAttributeModifier(EntityAttributesRegistry.MAX_STAMINA, AttributeModifierUUIDs.APPLE_FOOD_EFFECT, 5.0F, EntityAttributeModifier.Operation.ADDITION)
//            .addAttributeModifier(EntityAttributesRegistry.STAMINA_REGENERATION, AttributeModifierUUIDs.APPLE_FOOD_EFFECT, 1.0F, EntityAttributeModifier.Operation.ADDITION)
            ;

    public static final StatusEffect BAKED_POTATO_FOOD_EFFECT = new FoodStatusEffect()
//            .addAttributeModifier(EntityAttributes.GENERIC_MAX_HEALTH, AttributeModifierUUIDs.BAKED_POTATO_FOOD_EFFECT, 1.0F, EntityAttributeModifier.Operation.ADDITION)
//            .addAttributeModifier(EntityAttributesRegistry.HEALTH_REGENERATION, AttributeModifierUUIDs.BAKED_POTATO_FOOD_EFFECT, 1.0F, EntityAttributeModifier.Operation.ADDITION)
            .addAttributeModifier(EntityAttributesRegistry.MAX_STAMINA, AttributeModifierUUIDs.BAKED_POTATO_FOOD_EFFECT, 7.0F, EntityAttributeModifier.Operation.ADDITION)
            .addAttributeModifier(EntityAttributesRegistry.STAMINA_REGENERATION, AttributeModifierUUIDs.BAKED_POTATO_FOOD_EFFECT, 1.0F, EntityAttributeModifier.Operation.ADDITION)
            ;

    public static final StatusEffect BEEF_FOOD_EFFECT = new FoodStatusEffect()
            .addAttributeModifier(EntityAttributes.GENERIC_MAX_HEALTH, AttributeModifierUUIDs.BEEF_FOOD_EFFECT, 5.0F, EntityAttributeModifier.Operation.ADDITION)
//            .addAttributeModifier(EntityAttributesRegistry.HEALTH_REGENERATION, AttributeModifierUUIDs.BEEF_FOOD_EFFECT, 1.0F, EntityAttributeModifier.Operation.ADDITION)
//            .addAttributeModifier(EntityAttributesRegistry.MAX_STAMINA, AttributeModifierUUIDs.BEEF_FOOD_EFFECT, 2.0F, EntityAttributeModifier.Operation.ADDITION)
//            .addAttributeModifier(EntityAttributesRegistry.STAMINA_REGENERATION, AttributeModifierUUIDs.BEEF_FOOD_EFFECT, 2.0F, EntityAttributeModifier.Operation.ADDITION)
            ;

    public static final StatusEffect BEETROOT_FOOD_EFFECT = new FoodStatusEffect()
//            .addAttributeModifier(EntityAttributes.GENERIC_MAX_HEALTH, AttributeModifierUUIDs.BEETROOT_FOOD_EFFECT, 1.0F, EntityAttributeModifier.Operation.ADDITION)
//            .addAttributeModifier(EntityAttributesRegistry.HEALTH_REGENERATION, AttributeModifierUUIDs.BEETROOT_FOOD_EFFECT, 1.0F, EntityAttributeModifier.Operation.ADDITION)
            .addAttributeModifier(EntityAttributesRegistry.MAX_STAMINA, AttributeModifierUUIDs.BEETROOT_FOOD_EFFECT, 5.0F, EntityAttributeModifier.Operation.ADDITION)
//            .addAttributeModifier(EntityAttributesRegistry.STAMINA_REGENERATION, AttributeModifierUUIDs.BEETROOT_FOOD_EFFECT, 2.0F, EntityAttributeModifier.Operation.ADDITION)
            ;

    public static final StatusEffect BEETROOT_SOUP_FOOD_EFFECT = new FoodStatusEffect()
//            .addAttributeModifier(EntityAttributes.GENERIC_MAX_HEALTH, AttributeModifierUUIDs.BEETROOT_SOUP_FOOD_EFFECT, 1.0F, EntityAttributeModifier.Operation.ADDITION)
//            .addAttributeModifier(EntityAttributesRegistry.HEALTH_REGENERATION, AttributeModifierUUIDs.BEETROOT_SOUP_FOOD_EFFECT, 1.0F, EntityAttributeModifier.Operation.ADDITION)
            .addAttributeModifier(EntityAttributesRegistry.MAX_STAMINA, AttributeModifierUUIDs.BEETROOT_SOUP_FOOD_EFFECT, 10.0F, EntityAttributeModifier.Operation.ADDITION)
            .addAttributeModifier(EntityAttributesRegistry.STAMINA_REGENERATION, AttributeModifierUUIDs.BEETROOT_SOUP_FOOD_EFFECT, 2.0F, EntityAttributeModifier.Operation.ADDITION)
            .addAttributeModifier(EntityAttributesRegistry.FROST_RESISTANCE, AttributeModifierUUIDs.BEETROOT_SOUP_FOOD_EFFECT, 5.0F, EntityAttributeModifier.Operation.ADDITION)
            ;

    public static final StatusEffect BREAD_FOOD_EFFECT = new FoodStatusEffect()
//            .addAttributeModifier(EntityAttributes.GENERIC_MAX_HEALTH, AttributeModifierUUIDs.BREAD_FOOD_EFFECT, 1.0F, EntityAttributeModifier.Operation.ADDITION)
//            .addAttributeModifier(EntityAttributesRegistry.HEALTH_REGENERATION, AttributeModifierUUIDs.BREAD_FOOD_EFFECT, 1.0F, EntityAttributeModifier.Operation.ADDITION)
            .addAttributeModifier(EntityAttributesRegistry.MAX_STAMINA, AttributeModifierUUIDs.BREAD_FOOD_EFFECT, 10.0F, EntityAttributeModifier.Operation.ADDITION)
            .addAttributeModifier(EntityAttributesRegistry.STAMINA_REGENERATION, AttributeModifierUUIDs.BREAD_FOOD_EFFECT, 2.0F, EntityAttributeModifier.Operation.ADDITION)
            ;

    public static final StatusEffect BROWN_MUSHROOM_FOOD_EFFECT = new FoodStatusEffect()
            .addAttributeModifier(EntityAttributes.GENERIC_MAX_HEALTH, AttributeModifierUUIDs.BROWN_MUSHROOM_FOOD_EFFECT, 1.0F, EntityAttributeModifier.Operation.ADDITION)
            .addAttributeModifier(EntityAttributesRegistry.HEALTH_REGENERATION, AttributeModifierUUIDs.BROWN_MUSHROOM_FOOD_EFFECT, 1.0F, EntityAttributeModifier.Operation.ADDITION)
            .addAttributeModifier(EntityAttributesRegistry.MAX_STAMINA, AttributeModifierUUIDs.BROWN_MUSHROOM_FOOD_EFFECT, 2.0F, EntityAttributeModifier.Operation.ADDITION)
            .addAttributeModifier(EntityAttributesRegistry.STAMINA_REGENERATION, AttributeModifierUUIDs.BROWN_MUSHROOM_FOOD_EFFECT, 2.0F, EntityAttributeModifier.Operation.ADDITION)
            ;

    public static final StatusEffect CAKE_FOOD_EFFECT = new FoodStatusEffect()
            .addAttributeModifier(EntityAttributes.GENERIC_MAX_HEALTH, AttributeModifierUUIDs.CAKE_FOOD_EFFECT, 30.0F, EntityAttributeModifier.Operation.ADDITION)
            .addAttributeModifier(EntityAttributesRegistry.HEALTH_REGENERATION, AttributeModifierUUIDs.CAKE_FOOD_EFFECT, 2.0F, EntityAttributeModifier.Operation.ADDITION)
            .addAttributeModifier(EntityAttributesRegistry.MAX_STAMINA, AttributeModifierUUIDs.CAKE_FOOD_EFFECT, 25.0F, EntityAttributeModifier.Operation.ADDITION)
            .addAttributeModifier(EntityAttributesRegistry.STAMINA_REGENERATION, AttributeModifierUUIDs.CAKE_FOOD_EFFECT, 1.0F, EntityAttributeModifier.Operation.ADDITION)
            ;

    public static final StatusEffect CARROT_FOOD_EFFECT = new FoodStatusEffect()
//            .addAttributeModifier(EntityAttributes.GENERIC_MAX_HEALTH, AttributeModifierUUIDs.CARROT_FOOD_EFFECT, 1.0F, EntityAttributeModifier.Operation.ADDITION)
//            .addAttributeModifier(EntityAttributesRegistry.HEALTH_REGENERATION, AttributeModifierUUIDs.CARROT_FOOD_EFFECT, 1.0F, EntityAttributeModifier.Operation.ADDITION)
            .addAttributeModifier(EntityAttributesRegistry.MAX_STAMINA, AttributeModifierUUIDs.CARROT_FOOD_EFFECT, 5.0F, EntityAttributeModifier.Operation.ADDITION)
//            .addAttributeModifier(EntityAttributesRegistry.STAMINA_REGENERATION, AttributeModifierUUIDs.CARROT_FOOD_EFFECT, 1.0F, EntityAttributeModifier.Operation.ADDITION)
            ;

    public static final StatusEffect CHICKEN_FOOD_EFFECT = new FoodStatusEffect()
            .addAttributeModifier(EntityAttributes.GENERIC_MAX_HEALTH, AttributeModifierUUIDs.CHICKEN_FOOD_EFFECT, 5.0F, EntityAttributeModifier.Operation.ADDITION)
            .addAttributeModifier(EntityAttributesRegistry.POISON_BUILD_UP_REDUCTION, AttributeModifierUUIDs.CHICKEN_FOOD_EFFECT, -1.0F, EntityAttributeModifier.Operation.ADDITION)
//            .addAttributeModifier(EntityAttributesRegistry.MAX_STAMINA, AttributeModifierUUIDs.CHICKEN_FOOD_EFFECT, 2.0F, EntityAttributeModifier.Operation.ADDITION)
//            .addAttributeModifier(EntityAttributesRegistry.STAMINA_REGENERATION, AttributeModifierUUIDs.CHICKEN_FOOD_EFFECT, 2.0F, EntityAttributeModifier.Operation.ADDITION)
            ;

    public static final StatusEffect CHORUS_FRUIT_FOOD_EFFECT = new FoodStatusEffect()
            .addAttributeModifier(EntityAttributes.GENERIC_MAX_HEALTH, AttributeModifierUUIDs.CHORUS_FRUIT_FOOD_EFFECT, 1.0F, EntityAttributeModifier.Operation.ADDITION)
            .addAttributeModifier(EntityAttributesRegistry.HEALTH_REGENERATION, AttributeModifierUUIDs.CHORUS_FRUIT_FOOD_EFFECT, 1.0F, EntityAttributeModifier.Operation.ADDITION)
            .addAttributeModifier(EntityAttributesRegistry.MAX_STAMINA, AttributeModifierUUIDs.CHORUS_FRUIT_FOOD_EFFECT, 2.0F, EntityAttributeModifier.Operation.ADDITION)
            .addAttributeModifier(EntityAttributesRegistry.STAMINA_REGENERATION, AttributeModifierUUIDs.CHORUS_FRUIT_FOOD_EFFECT, 2.0F, EntityAttributeModifier.Operation.ADDITION)
            ;

    public static final StatusEffect COCOA_BEANS_FOOD_EFFECT = new FoodStatusEffect()
//            .addAttributeModifier(EntityAttributes.GENERIC_MAX_HEALTH, AttributeModifierUUIDs.COCOA_BEANS_FOOD_EFFECT, 30.0F, EntityAttributeModifier.Operation.ADDITION)
//            .addAttributeModifier(EntityAttributesRegistry.HEALTH_REGENERATION, AttributeModifierUUIDs.COCOA_BEANS_FOOD_EFFECT, 2.0F, EntityAttributeModifier.Operation.ADDITION)
            .addAttributeModifier(EntityAttributesRegistry.MAX_STAMINA, AttributeModifierUUIDs.COCOA_BEANS_FOOD_EFFECT, 3.0F, EntityAttributeModifier.Operation.ADDITION)
//            .addAttributeModifier(EntityAttributesRegistry.STAMINA_REGENERATION, AttributeModifierUUIDs.COCOA_BEANS_FOOD_EFFECT, 1.0F, EntityAttributeModifier.Operation.ADDITION)
            ;

    public static final StatusEffect COD_FOOD_EFFECT = new FoodStatusEffect()
            .addAttributeModifier(EntityAttributes.GENERIC_MAX_HEALTH, AttributeModifierUUIDs.COD_FOOD_EFFECT, 5.0F, EntityAttributeModifier.Operation.ADDITION)
            .addAttributeModifier(EntityAttributesRegistry.POISON_BUILD_UP_REDUCTION, AttributeModifierUUIDs.COD_FOOD_EFFECT, -1.0F, EntityAttributeModifier.Operation.ADDITION)
//            .addAttributeModifier(EntityAttributesRegistry.MAX_STAMINA, AttributeModifierUUIDs.COD_FOOD_EFFECT, 2.0F, EntityAttributeModifier.Operation.ADDITION)
//            .addAttributeModifier(EntityAttributesRegistry.STAMINA_REGENERATION, AttributeModifierUUIDs.COD_FOOD_EFFECT, 2.0F, EntityAttributeModifier.Operation.ADDITION)
            ;

    public static final StatusEffect COOKED_BEEF_FOOD_EFFECT = new FoodStatusEffect()
            .addAttributeModifier(EntityAttributes.GENERIC_MAX_HEALTH, AttributeModifierUUIDs.COOKED_BEEF_FOOD_EFFECT, 10.0F, EntityAttributeModifier.Operation.ADDITION)
            .addAttributeModifier(EntityAttributesRegistry.HEALTH_REGENERATION, AttributeModifierUUIDs.COOKED_BEEF_FOOD_EFFECT, 1.0F, EntityAttributeModifier.Operation.ADDITION)
//            .addAttributeModifier(EntityAttributesRegistry.MAX_STAMINA, AttributeModifierUUIDs.COOKED_BEEF_FOOD_EFFECT, 4.0F, EntityAttributeModifier.Operation.ADDITION)
//            .addAttributeModifier(EntityAttributesRegistry.STAMINA_REGENERATION, AttributeModifierUUIDs.COOKED_BEEF_FOOD_EFFECT, 2.0F, EntityAttributeModifier.Operation.ADDITION)
            ;

    public static final StatusEffect COOKED_CHICKEN_FOOD_EFFECT = new FoodStatusEffect()
            .addAttributeModifier(EntityAttributes.GENERIC_MAX_HEALTH, AttributeModifierUUIDs.COOKED_CHICKEN_FOOD_EFFECT, 10.0F, EntityAttributeModifier.Operation.ADDITION)
            .addAttributeModifier(EntityAttributesRegistry.HEALTH_REGENERATION, AttributeModifierUUIDs.COOKED_CHICKEN_FOOD_EFFECT, 1.0F, EntityAttributeModifier.Operation.ADDITION)
//            .addAttributeModifier(EntityAttributesRegistry.MAX_STAMINA, AttributeModifierUUIDs.COOKED_CHICKEN_FOOD_EFFECT, 50.0F, EntityAttributeModifier.Operation.ADDITION)
//            .addAttributeModifier(EntityAttributesRegistry.STAMINA_REGENERATION, AttributeModifierUUIDs.COOKED_CHICKEN_FOOD_EFFECT, 3.0F, EntityAttributeModifier.Operation.ADDITION)
            ;

    public static final StatusEffect COOKED_COD_FOOD_EFFECT = new FoodStatusEffect()
            .addAttributeModifier(EntityAttributes.GENERIC_MAX_HEALTH, AttributeModifierUUIDs.COOKED_COD_FOOD_EFFECT, 10.0F, EntityAttributeModifier.Operation.ADDITION)
            .addAttributeModifier(EntityAttributesRegistry.HEALTH_REGENERATION, AttributeModifierUUIDs.COOKED_COD_FOOD_EFFECT, 1.0F, EntityAttributeModifier.Operation.ADDITION)
//            .addAttributeModifier(EntityAttributesRegistry.MAX_STAMINA, AttributeModifierUUIDs.COOKED_COD_FOOD_EFFECT, 50.0F, EntityAttributeModifier.Operation.ADDITION)
//            .addAttributeModifier(EntityAttributesRegistry.STAMINA_REGENERATION, AttributeModifierUUIDs.COOKED_COD_FOOD_EFFECT, 3.0F, EntityAttributeModifier.Operation.ADDITION)
            ;

    public static final StatusEffect COOKED_MUTTON_FOOD_EFFECT = new FoodStatusEffect()
            .addAttributeModifier(EntityAttributes.GENERIC_MAX_HEALTH, AttributeModifierUUIDs.COOKED_MUTTON_FOOD_EFFECT, 10.0F, EntityAttributeModifier.Operation.ADDITION)
            .addAttributeModifier(EntityAttributesRegistry.HEALTH_REGENERATION, AttributeModifierUUIDs.COOKED_MUTTON_FOOD_EFFECT, 1.0F, EntityAttributeModifier.Operation.ADDITION)
//            .addAttributeModifier(EntityAttributesRegistry.MAX_STAMINA, AttributeModifierUUIDs.COOKED_MUTTON_FOOD_EFFECT, 50.0F, EntityAttributeModifier.Operation.ADDITION)
//            .addAttributeModifier(EntityAttributesRegistry.STAMINA_REGENERATION, AttributeModifierUUIDs.COOKED_MUTTON_FOOD_EFFECT, 3.0F, EntityAttributeModifier.Operation.ADDITION)
            ;

    public static final StatusEffect COOKED_PORKCHOP_FOOD_EFFECT = new FoodStatusEffect()
            .addAttributeModifier(EntityAttributes.GENERIC_MAX_HEALTH, AttributeModifierUUIDs.COOKED_PORKCHOP_FOOD_EFFECT, 10.0F, EntityAttributeModifier.Operation.ADDITION)
            .addAttributeModifier(EntityAttributesRegistry.HEALTH_REGENERATION, AttributeModifierUUIDs.COOKED_PORKCHOP_FOOD_EFFECT, 1.0F, EntityAttributeModifier.Operation.ADDITION)
//            .addAttributeModifier(EntityAttributesRegistry.MAX_STAMINA, AttributeModifierUUIDs.COOKED_PORKCHOP_FOOD_EFFECT, 50.0F, EntityAttributeModifier.Operation.ADDITION)
//            .addAttributeModifier(EntityAttributesRegistry.STAMINA_REGENERATION, AttributeModifierUUIDs.COOKED_PORKCHOP_FOOD_EFFECT, 3.0F, EntityAttributeModifier.Operation.ADDITION)
            ;

    public static final StatusEffect COOKED_RABBIT_FOOD_EFFECT = new FoodStatusEffect()
            .addAttributeModifier(EntityAttributes.GENERIC_MAX_HEALTH, AttributeModifierUUIDs.COOKED_RABBIT_FOOD_EFFECT, 10.0F, EntityAttributeModifier.Operation.ADDITION)
            .addAttributeModifier(EntityAttributesRegistry.HEALTH_REGENERATION, AttributeModifierUUIDs.COOKED_RABBIT_FOOD_EFFECT, 1.0F, EntityAttributeModifier.Operation.ADDITION)
//            .addAttributeModifier(EntityAttributesRegistry.MAX_STAMINA, AttributeModifierUUIDs.COOKED_RABBIT_FOOD_EFFECT, 50.0F, EntityAttributeModifier.Operation.ADDITION)
//            .addAttributeModifier(EntityAttributesRegistry.STAMINA_REGENERATION, AttributeModifierUUIDs.COOKED_RABBIT_FOOD_EFFECT, 3.0F, EntityAttributeModifier.Operation.ADDITION)
            ;

    public static final StatusEffect COOKED_SALMON_FOOD_EFFECT = new FoodStatusEffect()
            .addAttributeModifier(EntityAttributes.GENERIC_MAX_HEALTH, AttributeModifierUUIDs.COOKED_SALMON_FOOD_EFFECT, 10.0F, EntityAttributeModifier.Operation.ADDITION)
            .addAttributeModifier(EntityAttributesRegistry.HEALTH_REGENERATION, AttributeModifierUUIDs.COOKED_SALMON_FOOD_EFFECT, 1.0F, EntityAttributeModifier.Operation.ADDITION)
//            .addAttributeModifier(EntityAttributesRegistry.MAX_STAMINA, AttributeModifierUUIDs.COOKED_SALMON_FOOD_EFFECT, 50.0F, EntityAttributeModifier.Operation.ADDITION)
//            .addAttributeModifier(EntityAttributesRegistry.STAMINA_REGENERATION, AttributeModifierUUIDs.COOKED_SALMON_FOOD_EFFECT, 3.0F, EntityAttributeModifier.Operation.ADDITION)
            ;

    public static final StatusEffect COOKIE_FOOD_EFFECT = new FoodStatusEffect()
            .addAttributeModifier(EntityAttributes.GENERIC_MAX_HEALTH, AttributeModifierUUIDs.COOKIE_FOOD_EFFECT, 60.0F, EntityAttributeModifier.Operation.ADDITION)
            .addAttributeModifier(EntityAttributesRegistry.HEALTH_REGENERATION, AttributeModifierUUIDs.COOKIE_FOOD_EFFECT, 3.0F, EntityAttributeModifier.Operation.ADDITION)
            .addAttributeModifier(EntityAttributesRegistry.MAX_STAMINA, AttributeModifierUUIDs.COOKIE_FOOD_EFFECT, 50.0F, EntityAttributeModifier.Operation.ADDITION)
            .addAttributeModifier(EntityAttributesRegistry.STAMINA_REGENERATION, AttributeModifierUUIDs.COOKIE_FOOD_EFFECT, 3.0F, EntityAttributeModifier.Operation.ADDITION)
            ;

    public static final StatusEffect DRIED_KELP_FOOD_EFFECT = new FoodStatusEffect()
//            .addAttributeModifier(EntityAttributes.GENERIC_MAX_HEALTH, AttributeModifierUUIDs.DRIED_KELP_FOOD_EFFECT, 60.0F, EntityAttributeModifier.Operation.ADDITION)
//            .addAttributeModifier(EntityAttributesRegistry.HEALTH_REGENERATION, AttributeModifierUUIDs.DRIED_KELP_FOOD_EFFECT, 3.0F, EntityAttributeModifier.Operation.ADDITION)
            .addAttributeModifier(EntityAttributesRegistry.STAMINA_REGENERATION_DELAY_THRESHOLD, AttributeModifierUUIDs.DRIED_KELP_FOOD_EFFECT, -30.0F, EntityAttributeModifier.Operation.ADDITION)
            .addAttributeModifier(EntityAttributesRegistry.STAMINA_TICK_THRESHOLD, AttributeModifierUUIDs.DRIED_KELP_FOOD_EFFECT, -10.0F, EntityAttributeModifier.Operation.ADDITION)
            ;

    public static final StatusEffect ENCHANTED_GOLDEN_APPLE_FOOD_EFFECT = new FoodStatusEffect()
            .addAttributeModifier(EntityAttributes.GENERIC_MAX_HEALTH, AttributeModifierUUIDs.ENCHANTED_GOLDEN_APPLE_FOOD_EFFECT, 15.0F, EntityAttributeModifier.Operation.ADDITION)
            .addAttributeModifier(EntityAttributesRegistry.HEALTH_REGENERATION, AttributeModifierUUIDs.ENCHANTED_GOLDEN_APPLE_FOOD_EFFECT, 3.0F, EntityAttributeModifier.Operation.ADDITION)
            .addAttributeModifier(EntityAttributesRegistry.MAX_STAMINA, AttributeModifierUUIDs.ENCHANTED_GOLDEN_APPLE_FOOD_EFFECT, 30.0F, EntityAttributeModifier.Operation.ADDITION)
            .addAttributeModifier(EntityAttributesRegistry.STAMINA_REGENERATION, AttributeModifierUUIDs.ENCHANTED_GOLDEN_APPLE_FOOD_EFFECT, 3.0F, EntityAttributeModifier.Operation.ADDITION)
            ;

    public static final StatusEffect FERMENTED_SPIDER_EYE_FOOD_EFFECT = new FoodStatusEffect()
            .addAttributeModifier(EntityAttributes.GENERIC_MAX_HEALTH, AttributeModifierUUIDs.FERMENTED_SPIDER_EYE_FOOD_EFFECT, 30.0F, EntityAttributeModifier.Operation.ADDITION)
            .addAttributeModifier(EntityAttributesRegistry.HEALTH_REGENERATION, AttributeModifierUUIDs.FERMENTED_SPIDER_EYE_FOOD_EFFECT, 2.0F, EntityAttributeModifier.Operation.ADDITION)
            .addAttributeModifier(EntityAttributesRegistry.MAX_STAMINA, AttributeModifierUUIDs.FERMENTED_SPIDER_EYE_FOOD_EFFECT, 25.0F, EntityAttributeModifier.Operation.ADDITION)
            .addAttributeModifier(EntityAttributesRegistry.STAMINA_REGENERATION, AttributeModifierUUIDs.FERMENTED_SPIDER_EYE_FOOD_EFFECT, 1.0F, EntityAttributeModifier.Operation.ADDITION)
            ;

    public static final StatusEffect GLOW_BERRIES_FOOD_EFFECT = new FoodStatusEffect()
            .addAttributeModifier(EntityAttributesRegistry.MAX_STAMINA, AttributeModifierUUIDs.GLOW_BERRIES_FOOD_EFFECT, 5.0F, EntityAttributeModifier.Operation.ADDITION)
            .addAttributeModifier(EntityAttributesRegistry.STAMINA_REGENERATION, AttributeModifierUUIDs.GLOW_BERRIES_FOOD_EFFECT, 1.0F, EntityAttributeModifier.Operation.ADDITION)
            ;

    public static final StatusEffect GOLDEN_APPLE_FOOD_EFFECT = new FoodStatusEffect()
            .addAttributeModifier(EntityAttributes.GENERIC_MAX_HEALTH, AttributeModifierUUIDs.GOLDEN_APPLE_FOOD_EFFECT, 60.0F, EntityAttributeModifier.Operation.ADDITION)
            .addAttributeModifier(EntityAttributesRegistry.HEALTH_REGENERATION, AttributeModifierUUIDs.GOLDEN_APPLE_FOOD_EFFECT, 3.0F, EntityAttributeModifier.Operation.ADDITION)
            .addAttributeModifier(EntityAttributesRegistry.MAX_STAMINA, AttributeModifierUUIDs.GOLDEN_APPLE_FOOD_EFFECT, 50.0F, EntityAttributeModifier.Operation.ADDITION)
            .addAttributeModifier(EntityAttributesRegistry.STAMINA_REGENERATION, AttributeModifierUUIDs.GOLDEN_APPLE_FOOD_EFFECT, 3.0F, EntityAttributeModifier.Operation.ADDITION)
            ;

    public static final StatusEffect GOLDEN_CARROT_FOOD_EFFECT = new FoodStatusEffect()
            .addAttributeModifier(EntityAttributes.GENERIC_MAX_HEALTH, AttributeModifierUUIDs.GOLDEN_CARROT_FOOD_EFFECT, 60.0F, EntityAttributeModifier.Operation.ADDITION)
            .addAttributeModifier(EntityAttributesRegistry.HEALTH_REGENERATION, AttributeModifierUUIDs.GOLDEN_CARROT_FOOD_EFFECT, 3.0F, EntityAttributeModifier.Operation.ADDITION)
            .addAttributeModifier(EntityAttributesRegistry.MAX_STAMINA, AttributeModifierUUIDs.GOLDEN_CARROT_FOOD_EFFECT, 50.0F, EntityAttributeModifier.Operation.ADDITION)
            .addAttributeModifier(EntityAttributesRegistry.STAMINA_REGENERATION, AttributeModifierUUIDs.GOLDEN_CARROT_FOOD_EFFECT, 3.0F, EntityAttributeModifier.Operation.ADDITION)
            ;

    public static final StatusEffect HONEY_BOTTLE_FOOD_EFFECT = new FoodStatusEffect()
//            .addAttributeModifier(EntityAttributes.GENERIC_MAX_HEALTH, AttributeModifierUUIDs.HONEY_BOTTLE_FOOD_EFFECT, 60.0F, EntityAttributeModifier.Operation.ADDITION)
//            .addAttributeModifier(EntityAttributesRegistry.HEALTH_REGENERATION, AttributeModifierUUIDs.HONEY_BOTTLE_FOOD_EFFECT, 3.0F, EntityAttributeModifier.Operation.ADDITION)
            .addAttributeModifier(EntityAttributesRegistry.MAX_POISON_BUILD_UP, AttributeModifierUUIDs.HONEY_BOTTLE_FOOD_EFFECT, 10.0F, EntityAttributeModifier.Operation.ADDITION)
            .addAttributeModifier(EntityAttributesRegistry.POISON_BUILD_UP_REDUCTION, AttributeModifierUUIDs.HONEY_BOTTLE_FOOD_EFFECT, 3.0F, EntityAttributeModifier.Operation.ADDITION)
            ;

    public static final StatusEffect MELON_SLICE_FOOD_EFFECT = new FoodStatusEffect()
//            .addAttributeModifier(EntityAttributes.GENERIC_MAX_HEALTH, AttributeModifierUUIDs.MELON_SLICE_FOOD_EFFECT, 60.0F, EntityAttributeModifier.Operation.ADDITION)
//            .addAttributeModifier(EntityAttributesRegistry.HEALTH_REGENERATION, AttributeModifierUUIDs.MELON_SLICE_FOOD_EFFECT, 3.0F, EntityAttributeModifier.Operation.ADDITION)
            .addAttributeModifier(EntityAttributesRegistry.MAX_STAMINA, AttributeModifierUUIDs.MELON_SLICE_FOOD_EFFECT, 3.0F, EntityAttributeModifier.Operation.ADDITION)
//            .addAttributeModifier(EntityAttributesRegistry.STAMINA_REGENERATION, AttributeModifierUUIDs.MELON_SLICE_FOOD_EFFECT, 3.0F, EntityAttributeModifier.Operation.ADDITION)
            ;

    public static final StatusEffect MUSHROOM_STEW_FOOD_EFFECT = new FoodStatusEffect()
            .addAttributeModifier(EntityAttributes.GENERIC_MAX_HEALTH, AttributeModifierUUIDs.MUSHROOM_STEW_FOOD_EFFECT, 60.0F, EntityAttributeModifier.Operation.ADDITION)
            .addAttributeModifier(EntityAttributesRegistry.HEALTH_REGENERATION, AttributeModifierUUIDs.MUSHROOM_STEW_FOOD_EFFECT, 3.0F, EntityAttributeModifier.Operation.ADDITION)
            .addAttributeModifier(EntityAttributesRegistry.MAX_STAMINA, AttributeModifierUUIDs.MUSHROOM_STEW_FOOD_EFFECT, 50.0F, EntityAttributeModifier.Operation.ADDITION)
            .addAttributeModifier(EntityAttributesRegistry.STAMINA_REGENERATION, AttributeModifierUUIDs.MUSHROOM_STEW_FOOD_EFFECT, 3.0F, EntityAttributeModifier.Operation.ADDITION)
            ;

    public static final StatusEffect MUTTON_FOOD_EFFECT = new FoodStatusEffect()
            .addAttributeModifier(EntityAttributes.GENERIC_MAX_HEALTH, AttributeModifierUUIDs.MUTTON_FOOD_EFFECT, 5.0F, EntityAttributeModifier.Operation.ADDITION)
//            .addAttributeModifier(EntityAttributesRegistry.HEALTH_REGENERATION, AttributeModifierUUIDs.MUTTON_FOOD_EFFECT, 3.0F, EntityAttributeModifier.Operation.ADDITION)
//            .addAttributeModifier(EntityAttributesRegistry.MAX_STAMINA, AttributeModifierUUIDs.MUTTON_FOOD_EFFECT, 50.0F, EntityAttributeModifier.Operation.ADDITION)
//            .addAttributeModifier(EntityAttributesRegistry.STAMINA_REGENERATION, AttributeModifierUUIDs.MUTTON_FOOD_EFFECT, 3.0F, EntityAttributeModifier.Operation.ADDITION)
            ;

    public static final StatusEffect POISONOUS_POTATO_FOOD_EFFECT = new FoodStatusEffect()
//            .addAttributeModifier(EntityAttributes.GENERIC_MAX_HEALTH, AttributeModifierUUIDs.POISONOUS_POTATO_FOOD_EFFECT, 60.0F, EntityAttributeModifier.Operation.ADDITION)
            .addAttributeModifier(EntityAttributesRegistry.POISON_BUILD_UP_REDUCTION, AttributeModifierUUIDs.POISONOUS_POTATO_FOOD_EFFECT, -3.0F, EntityAttributeModifier.Operation.ADDITION)
//            .addAttributeModifier(EntityAttributesRegistry.MAX_STAMINA, AttributeModifierUUIDs.POISONOUS_POTATO_FOOD_EFFECT, 50.0F, EntityAttributeModifier.Operation.ADDITION)
//            .addAttributeModifier(EntityAttributesRegistry.STAMINA_REGENERATION, AttributeModifierUUIDs.POISONOUS_POTATO_FOOD_EFFECT, 3.0F, EntityAttributeModifier.Operation.ADDITION)
            ;

    public static final StatusEffect PORKCHOP_FOOD_EFFECT = new FoodStatusEffect()
            .addAttributeModifier(EntityAttributes.GENERIC_MAX_HEALTH, AttributeModifierUUIDs.PORKCHOP_FOOD_EFFECT, 5.0F, EntityAttributeModifier.Operation.ADDITION)
            .addAttributeModifier(EntityAttributesRegistry.POISON_BUILD_UP_REDUCTION, AttributeModifierUUIDs.PORKCHOP_FOOD_EFFECT, -1.0F, EntityAttributeModifier.Operation.ADDITION)
//            .addAttributeModifier(EntityAttributesRegistry.MAX_STAMINA, AttributeModifierUUIDs.PORKCHOP_FOOD_EFFECT, 50.0F, EntityAttributeModifier.Operation.ADDITION)
//            .addAttributeModifier(EntityAttributesRegistry.STAMINA_REGENERATION, AttributeModifierUUIDs.PORKCHOP_FOOD_EFFECT, 3.0F, EntityAttributeModifier.Operation.ADDITION)
            ;

    public static final StatusEffect POTATO_FOOD_EFFECT = new FoodStatusEffect()
//            .addAttributeModifier(EntityAttributes.GENERIC_MAX_HEALTH, AttributeModifierUUIDs.POTATO_FOOD_EFFECT, 60.0F, EntityAttributeModifier.Operation.ADDITION)
//            .addAttributeModifier(EntityAttributesRegistry.HEALTH_REGENERATION, AttributeModifierUUIDs.POTATO_FOOD_EFFECT, 3.0F, EntityAttributeModifier.Operation.ADDITION)
            .addAttributeModifier(EntityAttributesRegistry.MAX_STAMINA, AttributeModifierUUIDs.POTATO_FOOD_EFFECT, 5.0F, EntityAttributeModifier.Operation.ADDITION)
//            .addAttributeModifier(EntityAttributesRegistry.STAMINA_REGENERATION, AttributeModifierUUIDs.POTATO_FOOD_EFFECT, 3.0F, EntityAttributeModifier.Operation.ADDITION)
            ;

    public static final StatusEffect PUFFERFISH_FOOD_EFFECT = new FoodStatusEffect()
//            .addAttributeModifier(EntityAttributes.GENERIC_MAX_HEALTH, AttributeModifierUUIDs.PUFFERFISH_FOOD_EFFECT, 60.0F, EntityAttributeModifier.Operation.ADDITION)
            .addAttributeModifier(EntityAttributesRegistry.POISON_BUILD_UP_REDUCTION, AttributeModifierUUIDs.PUFFERFISH_FOOD_EFFECT, -5.0F, EntityAttributeModifier.Operation.ADDITION)
//            .addAttributeModifier(EntityAttributesRegistry.MAX_STAMINA, AttributeModifierUUIDs.PUFFERFISH_FOOD_EFFECT, 50.0F, EntityAttributeModifier.Operation.ADDITION)
//            .addAttributeModifier(EntityAttributesRegistry.STAMINA_REGENERATION, AttributeModifierUUIDs.PUFFERFISH_FOOD_EFFECT, 3.0F, EntityAttributeModifier.Operation.ADDITION)
            ;

    public static final StatusEffect PUMPKIN_PIE_FOOD_EFFECT = new FoodStatusEffect()
            .addAttributeModifier(EntityAttributes.GENERIC_MAX_HEALTH, AttributeModifierUUIDs.PUMPKIN_PIE_FOOD_EFFECT, 60.0F, EntityAttributeModifier.Operation.ADDITION)
            .addAttributeModifier(EntityAttributesRegistry.HEALTH_REGENERATION, AttributeModifierUUIDs.PUMPKIN_PIE_FOOD_EFFECT, 3.0F, EntityAttributeModifier.Operation.ADDITION)
            .addAttributeModifier(EntityAttributesRegistry.MAX_STAMINA, AttributeModifierUUIDs.PUMPKIN_PIE_FOOD_EFFECT, 50.0F, EntityAttributeModifier.Operation.ADDITION)
            .addAttributeModifier(EntityAttributesRegistry.STAMINA_REGENERATION, AttributeModifierUUIDs.PUMPKIN_PIE_FOOD_EFFECT, 3.0F, EntityAttributeModifier.Operation.ADDITION)
            ;

    public static final StatusEffect RABBIT_FOOD_EFFECT = new FoodStatusEffect()
            .addAttributeModifier(EntityAttributes.GENERIC_MAX_HEALTH, AttributeModifierUUIDs.RABBIT_FOOD_EFFECT, 5.0F, EntityAttributeModifier.Operation.ADDITION)
//            .addAttributeModifier(EntityAttributesRegistry.HEALTH_REGENERATION, AttributeModifierUUIDs.RABBIT_FOOD_EFFECT, 3.0F, EntityAttributeModifier.Operation.ADDITION)
//            .addAttributeModifier(EntityAttributesRegistry.MAX_STAMINA, AttributeModifierUUIDs.RABBIT_FOOD_EFFECT, 50.0F, EntityAttributeModifier.Operation.ADDITION)
//            .addAttributeModifier(EntityAttributesRegistry.STAMINA_REGENERATION, AttributeModifierUUIDs.RABBIT_FOOD_EFFECT, 3.0F, EntityAttributeModifier.Operation.ADDITION)
            ;

    public static final StatusEffect RABBIT_STEW_FOOD_EFFECT = new FoodStatusEffect()
            .addAttributeModifier(EntityAttributes.GENERIC_MAX_HEALTH, AttributeModifierUUIDs.RABBIT_STEW_FOOD_EFFECT, 6.0F, EntityAttributeModifier.Operation.ADDITION)
            .addAttributeModifier(EntityAttributesRegistry.HEALTH_REGENERATION, AttributeModifierUUIDs.RABBIT_STEW_FOOD_EFFECT, 1.0F, EntityAttributeModifier.Operation.ADDITION)
            .addAttributeModifier(EntityAttributesRegistry.MAX_STAMINA, AttributeModifierUUIDs.BEETROOT_SOUP_FOOD_EFFECT, 6.0F, EntityAttributeModifier.Operation.ADDITION)
            .addAttributeModifier(EntityAttributesRegistry.STAMINA_REGENERATION, AttributeModifierUUIDs.BEETROOT_SOUP_FOOD_EFFECT, 1.0F, EntityAttributeModifier.Operation.ADDITION)
            ;

    public static final StatusEffect RED_MUSHROOM_FOOD_EFFECT = new FoodStatusEffect()
//            .addAttributeModifier(EntityAttributes.GENERIC_MAX_HEALTH, AttributeModifierUUIDs.RED_MUSHROOM_FOOD_EFFECT, 35.0F, EntityAttributeModifier.Operation.ADDITION)
//            .addAttributeModifier(EntityAttributesRegistry.MAX_STAMINA, AttributeModifierUUIDs.RED_MUSHROOM_FOOD_EFFECT, 35.0F, EntityAttributeModifier.Operation.ADDITION)
//            .addAttributeModifier(EntityAttributesRegistry.MAX_MANA, AttributeModifierUUIDs.RED_MUSHROOM_FOOD_EFFECT, 35.0F, EntityAttributeModifier.Operation.ADDITION)
            ;

    public static final StatusEffect ROTTEN_FLESH_FOOD_EFFECT = new FoodStatusEffect()
//            .addAttributeModifier(EntityAttributes.GENERIC_MAX_HEALTH, AttributeModifierUUIDs.ROTTEN_FLESH_FOOD_EFFECT, 60.0F, EntityAttributeModifier.Operation.ADDITION)
            .addAttributeModifier(EntityAttributesRegistry.POISON_BUILD_UP_REDUCTION, AttributeModifierUUIDs.ROTTEN_FLESH_FOOD_EFFECT, -2.0F, EntityAttributeModifier.Operation.ADDITION)
//            .addAttributeModifier(EntityAttributesRegistry.MAX_STAMINA, AttributeModifierUUIDs.ROTTEN_FLESH_FOOD_EFFECT, 50.0F, EntityAttributeModifier.Operation.ADDITION)
//            .addAttributeModifier(EntityAttributesRegistry.STAMINA_REGENERATION, AttributeModifierUUIDs.ROTTEN_FLESH_FOOD_EFFECT, 3.0F, EntityAttributeModifier.Operation.ADDITION)
            ;

    public static final StatusEffect SALMON_FOOD_EFFECT = new FoodStatusEffect()
            .addAttributeModifier(EntityAttributes.GENERIC_MAX_HEALTH, AttributeModifierUUIDs.SALMON_FOOD_EFFECT, 5.0F, EntityAttributeModifier.Operation.ADDITION)
            .addAttributeModifier(EntityAttributesRegistry.POISON_BUILD_UP_REDUCTION, AttributeModifierUUIDs.SALMON_FOOD_EFFECT, -1.0F, EntityAttributeModifier.Operation.ADDITION)
//            .addAttributeModifier(EntityAttributesRegistry.MAX_STAMINA, AttributeModifierUUIDs.SALMON_FOOD_EFFECT, 50.0F, EntityAttributeModifier.Operation.ADDITION)
//            .addAttributeModifier(EntityAttributesRegistry.STAMINA_REGENERATION, AttributeModifierUUIDs.SALMON_FOOD_EFFECT, 3.0F, EntityAttributeModifier.Operation.ADDITION)
            ;

    public static final StatusEffect SPIDER_EYE_FOOD_EFFECT = new FoodStatusEffect()
//            .addAttributeModifier(EntityAttributes.GENERIC_MAX_HEALTH, AttributeModifierUUIDs.SPIDER_EYE_FOOD_EFFECT, 60.0F, EntityAttributeModifier.Operation.ADDITION)
            .addAttributeModifier(EntityAttributesRegistry.POISON_BUILD_UP_REDUCTION, AttributeModifierUUIDs.SPIDER_EYE_FOOD_EFFECT, -3.0F, EntityAttributeModifier.Operation.ADDITION)
//            .addAttributeModifier(EntityAttributesRegistry.MAX_STAMINA, AttributeModifierUUIDs.SPIDER_EYE_FOOD_EFFECT, 50.0F, EntityAttributeModifier.Operation.ADDITION)
//            .addAttributeModifier(EntityAttributesRegistry.STAMINA_REGENERATION, AttributeModifierUUIDs.SPIDER_EYE_FOOD_EFFECT, 3.0F, EntityAttributeModifier.Operation.ADDITION)
            ;

    public static final StatusEffect SUGAR_FOOD_EFFECT = new FoodStatusEffect()
//            .addAttributeModifier(EntityAttributes.GENERIC_MAX_HEALTH, AttributeModifierUUIDs.SUGAR_FOOD_EFFECT, 30.0F, EntityAttributeModifier.Operation.ADDITION)
//            .addAttributeModifier(EntityAttributesRegistry.HEALTH_REGENERATION, AttributeModifierUUIDs.SUGAR_FOOD_EFFECT, 2.0F, EntityAttributeModifier.Operation.ADDITION)
//            .addAttributeModifier(EntityAttributesRegistry.MAX_STAMINA, AttributeModifierUUIDs.SUGAR_FOOD_EFFECT, 25.0F, EntityAttributeModifier.Operation.ADDITION)
//            .addAttributeModifier(EntityAttributesRegistry.STAMINA_REGENERATION, AttributeModifierUUIDs.SUGAR_FOOD_EFFECT, 1.0F, EntityAttributeModifier.Operation.ADDITION)
            ;

    public static final StatusEffect SUSPICIOUS_STEW_FOOD_EFFECT = new FoodStatusEffect()
            .addAttributeModifier(EntityAttributes.GENERIC_MAX_HEALTH, AttributeModifierUUIDs.SUSPICIOUS_STEW_FOOD_EFFECT, 5.0F, EntityAttributeModifier.Operation.ADDITION)
//            .addAttributeModifier(EntityAttributesRegistry.HEALTH_REGENERATION, AttributeModifierUUIDs.SUSPICIOUS_STEW_FOOD_EFFECT, 2.0F, EntityAttributeModifier.Operation.ADDITION)
            .addAttributeModifier(EntityAttributesRegistry.MAX_STAMINA, AttributeModifierUUIDs.SUSPICIOUS_STEW_FOOD_EFFECT, 5.0F, EntityAttributeModifier.Operation.ADDITION)
            .addAttributeModifier(EntityAttributesRegistry.MAX_MANA, AttributeModifierUUIDs.SUSPICIOUS_STEW_FOOD_EFFECT, 5.0F, EntityAttributeModifier.Operation.ADDITION)
            ;

    public static final StatusEffect SWEET_BERRIES_FOOD_EFFECT = new FoodStatusEffect()
//            .addAttributeModifier(EntityAttributes.GENERIC_MAX_HEALTH, AttributeModifierUUIDs.SWEET_BERRIES_FOOD_EFFECT, 30.0F, EntityAttributeModifier.Operation.ADDITION)
//            .addAttributeModifier(EntityAttributesRegistry.HEALTH_REGENERATION, AttributeModifierUUIDs.SWEET_BERRIES_FOOD_EFFECT, 2.0F, EntityAttributeModifier.Operation.ADDITION)
            .addAttributeModifier(EntityAttributesRegistry.MAX_STAMINA, AttributeModifierUUIDs.SWEET_BERRIES_FOOD_EFFECT, 5.0F, EntityAttributeModifier.Operation.ADDITION)
//            .addAttributeModifier(EntityAttributesRegistry.STAMINA_REGENERATION, AttributeModifierUUIDs.SWEET_BERRIES_FOOD_EFFECT, 1.0F, EntityAttributeModifier.Operation.ADDITION)
            ;

    public static final StatusEffect TROPICAL_FISH_FOOD_EFFECT = new FoodStatusEffect()
            .addAttributeModifier(EntityAttributes.GENERIC_MAX_HEALTH, AttributeModifierUUIDs.TROPICAL_FISH_FOOD_EFFECT, 5.0F, EntityAttributeModifier.Operation.ADDITION)
            .addAttributeModifier(EntityAttributesRegistry.POISON_BUILD_UP_REDUCTION, AttributeModifierUUIDs.TROPICAL_FISH_FOOD_EFFECT, -1.0F, EntityAttributeModifier.Operation.ADDITION)
//            .addAttributeModifier(EntityAttributesRegistry.MAX_STAMINA, AttributeModifierUUIDs.TROPICAL_FISH_FOOD_EFFECT, 25.0F, EntityAttributeModifier.Operation.ADDITION)
//            .addAttributeModifier(EntityAttributesRegistry.STAMINA_REGENERATION, AttributeModifierUUIDs.TROPICAL_FISH_FOOD_EFFECT, 1.0F, EntityAttributeModifier.Operation.ADDITION)
            ;

    //endregion Food Effects
    //region Aura Effects
    public static final StatusEffect HEALTH_REGENERATION_AURA_EFFECT = new AuraStatusEffect(StatusEffects.REGENERATION, true)
            .addAttributeModifier(EntityAttributesRegistry.MAX_MANA, AttributeModifierUUIDs.AURA_EFFECT, -0.25F, EntityAttributeModifier.Operation.MULTIPLY_TOTAL)
            ;

    //endregion Aura Effects

    //region housing effects
    public static final StatusEffect HOUSING_OWNER_EFFECT = new NeutralStatusEffect();
    public static final StatusEffect HOUSING_CO_OWNER_EFFECT = new NeutralStatusEffect();
    public static final StatusEffect HOUSING_TRUSTED_EFFECT = new NeutralStatusEffect();
    public static final StatusEffect HOUSING_GUEST_EFFECT = new NeutralStatusEffect();
    public static final StatusEffect HOUSING_STRANGER_EFFECT = new NeutralStatusEffect();
    public static final StatusEffect EDIT_HOUSING_RESISTANCE_EFFECT = new NeutralStatusEffect();
    //endregion housing effects

    public static final StatusEffect ADVENTURE_BUILDING_EFFECT = new NeutralStatusEffect();
    public static final StatusEffect CIVILISATION_EFFECT = new BeneficialStatusEffect()
//            .addAttributeModifier(EntityAttributes.GENERIC_MAX_HEALTH, AttributeModifierUUIDs.TROPICAL_FISH_FOOD_EFFECT, 5.0F, EntityAttributeModifier.Operation.ADDITION)
            .addAttributeModifier(EntityAttributesRegistry.MANA_REGENERATION, AttributeModifierUUIDs.CIVILISATION_EFFECT, 10.0F, EntityAttributeModifier.Operation.MULTIPLY_TOTAL)
            .addAttributeModifier(EntityAttributesRegistry.STAMINA_REGENERATION, AttributeModifierUUIDs.CIVILISATION_EFFECT, 5.0F, EntityAttributeModifier.Operation.MULTIPLY_TOTAL)
            .addAttributeModifier(EntityAttributesRegistry.HEALTH_REGENERATION, AttributeModifierUUIDs.CIVILISATION_EFFECT, 5.0F, EntityAttributeModifier.Operation.ADDITION)
//            .addAttributeModifier(EntityAttributesRegistry.MAX_STAMINA, AttributeModifierUUIDs.TROPICAL_FISH_FOOD_EFFECT, 25.0F, EntityAttributeModifier.Operation.ADDITION)
//            .addAttributeModifier(EntityAttributesRegistry.STAMINA_REGENERATION, AttributeModifierUUIDs.TROPICAL_FISH_FOOD_EFFECT, 1.0F, EntityAttributeModifier.Operation.ADDITION)
            ;
    public static final StatusEffect WILDERNESS_EFFECT = new NeutralStatusEffect();
    public static final StatusEffect MANA_REGENERATION_EFFECT = new BeneficialStatusEffect();
    public static final StatusEffect NEEDS_TWO_HANDING_EFFECT = new NeutralStatusEffect();
    public static final StatusEffect NO_ATTACK_ITEMS_EFFECT = new NeutralStatusEffect();
    public static final StatusEffect OVERBURDENED_EFFECT = new HarmfulStatusEffect().addAttributeModifier(EntityAttributes.GENERIC_MOVEMENT_SPEED, AttributeModifierUUIDs.OVERBURDENED_EFFECT, -0.25, EntityAttributeModifier.Operation.MULTIPLY_TOTAL);
    public static final StatusEffect PORTAL_RESISTANCE_EFFECT = new NeutralStatusEffect();

    // better 3rd person compat
    public static final StatusEffect CHANGING_PITCH_ENABLED_EFFECT = new NeutralStatusEffect();
    public static final StatusEffect FIRST_PERSON_PERSPECTIVE_ENABLED_EFFECT = new NeutralStatusEffect();

    public static final StatusEffect LAVA_IMMUNE = new BeneficialStatusEffect();
    public static final StatusEffect FALL_IMMUNE = new BeneficialStatusEffect();
    public static final StatusEffect KEEP_INVENTORY_ON_DEATH = new BeneficialStatusEffect();
    public static final StatusEffect CALAMITY = new HarmfulStatusEffect();
    public static final StatusEffect STAGGERED = new HarmfulStatusEffect();
    public static final StatusEffect BLEEDING = new BleedingStatusEffect();
    public static final StatusEffect BURNING = new BurningStatusEffect();
    public static final StatusEffect CHILLED = new HarmfulStatusEffect();
    public static final StatusEffect FROZEN = new HarmfulStatusEffect();
    public static final StatusEffect WET = new HarmfulStatusEffect();
    public static final StatusEffect POISON = new CustomPoisonStatusEffect();
    public static final StatusEffect SHOCKED = new ShockedStatusEffect();
    public static final StatusEffect TEST_AURA_EFFECT = new AuraStatusEffect(StatusEffects.GLOWING, false)
            .addAttributeModifier(EntityAttributesRegistry.MAX_MANA, AttributeModifierUUIDs.AURA_EFFECT, -0.25F, EntityAttributeModifier.Operation.MULTIPLY_TOTAL)
            ;

    public static void registerEffects() {
        // --- Configuration ---
        // utility effects
        ActionImpairing.configure(NO_ATTACK_ITEMS_EFFECT, new EntityActionsAllowed(true, true, new EntityActionsAllowed.PlayersAllowed(false, true, true), new EntityActionsAllowed.MobsAllowed(true), ExtendedEntityActionsAllowedSemanticType.NO_ATTACK_ITEM));
        ActionImpairing.configure(NEEDS_TWO_HANDING_EFFECT, new EntityActionsAllowed(true, true, new EntityActionsAllowed.PlayersAllowed(false, false, false), new EntityActionsAllowed.MobsAllowed(true), ExtendedEntityActionsAllowedSemanticType.NEEDS_TWO_HANDING));
        ActionImpairing.configure(STAGGERED, new EntityActionsAllowed(false, false, new EntityActionsAllowed.PlayersAllowed(false, false, false), new EntityActionsAllowed.MobsAllowed(false), ExtendedEntityActionsAllowedSemanticType.STAGGERED));
        ActionImpairing.configure(OVERBURDENED_EFFECT, new EntityActionsAllowed(false, true, new EntityActionsAllowed.PlayersAllowed(true, true, true), new EntityActionsAllowed.MobsAllowed(true), ExtendedEntityActionsAllowedSemanticType.OVERBURDENED));
        Synchronized.configure(STAGGERED, true);
        Synchronized.configure(BURNING, true);
        Synchronized.configure(CHILLED, true);
        Synchronized.configure(FROZEN, true);
        Synchronized.configure(WET, true);
        Synchronized.configure(TEST_AURA_EFFECT, true);
        Synchronized.configure(HEALTH_REGENERATION_AURA_EFFECT, true);

        // --- Registration ---
        // housing effects
        Registry.register(Registries.STATUS_EFFECT, BetterAdventureMode.identifier("housing_owner_effect"), HOUSING_OWNER_EFFECT);
        Registry.register(Registries.STATUS_EFFECT, BetterAdventureMode.identifier("housing_co_owner_effect"), HOUSING_CO_OWNER_EFFECT);
        Registry.register(Registries.STATUS_EFFECT, BetterAdventureMode.identifier("housing_trusted_effect"), HOUSING_TRUSTED_EFFECT);
        Registry.register(Registries.STATUS_EFFECT, BetterAdventureMode.identifier("housing_guest_effect"), HOUSING_GUEST_EFFECT);
        Registry.register(Registries.STATUS_EFFECT, BetterAdventureMode.identifier("housing_stranger_effect"), HOUSING_STRANGER_EFFECT);
        Registry.register(Registries.STATUS_EFFECT, BetterAdventureMode.identifier("edit_housing_resistance_effect"), EDIT_HOUSING_RESISTANCE_EFFECT);
        // utility effects
        Registry.register(Registries.STATUS_EFFECT, BetterAdventureMode.identifier("adventure_building_effect"), ADVENTURE_BUILDING_EFFECT);
        Registry.register(Registries.STATUS_EFFECT, BetterAdventureMode.identifier("civilisation_effect"), CIVILISATION_EFFECT);
        Registry.register(Registries.STATUS_EFFECT, BetterAdventureMode.identifier("wilderness_effect"), WILDERNESS_EFFECT);
        Registry.register(Registries.STATUS_EFFECT, BetterAdventureMode.identifier("needs_two_handing_effect"), NEEDS_TWO_HANDING_EFFECT);
        Registry.register(Registries.STATUS_EFFECT, BetterAdventureMode.identifier("no_attack_items_effect"), NO_ATTACK_ITEMS_EFFECT);
        Registry.register(Registries.STATUS_EFFECT, BetterAdventureMode.identifier("portal_resistance_effect"), PORTAL_RESISTANCE_EFFECT);

        // better 3rd person compat
        Registry.register(Registries.STATUS_EFFECT, BetterAdventureMode.identifier("changing_pitch_enabled_effect"), CHANGING_PITCH_ENABLED_EFFECT);
        Registry.register(Registries.STATUS_EFFECT, BetterAdventureMode.identifier("first_person_perspective_enabled_effect"), FIRST_PERSON_PERSPECTIVE_ENABLED_EFFECT);


        Registry.register(Registries.STATUS_EFFECT, BetterAdventureMode.identifier("lava_immune"), LAVA_IMMUNE);
        Registry.register(Registries.STATUS_EFFECT, BetterAdventureMode.identifier("fall_immune"), FALL_IMMUNE);
        Registry.register(Registries.STATUS_EFFECT, BetterAdventureMode.identifier("keep_inventory_on_death"), KEEP_INVENTORY_ON_DEATH);


        Registry.register(Registries.STATUS_EFFECT, BetterAdventureMode.identifier("calamity"), CALAMITY);
        Registry.register(Registries.STATUS_EFFECT, BetterAdventureMode.identifier("overburdened_effect"), OVERBURDENED_EFFECT);
        Registry.register(Registries.STATUS_EFFECT, BetterAdventureMode.identifier("staggered"), STAGGERED);

        Registry.register(Registries.STATUS_EFFECT, BetterAdventureMode.identifier("bleeding"), BLEEDING);
        Registry.register(Registries.STATUS_EFFECT, BetterAdventureMode.identifier("burning"), BURNING);
        Registry.register(Registries.STATUS_EFFECT, BetterAdventureMode.identifier("chilled"), CHILLED);
        Registry.register(Registries.STATUS_EFFECT, BetterAdventureMode.identifier("frozen"), FROZEN);
        Registry.register(Registries.STATUS_EFFECT, BetterAdventureMode.identifier("wet"), WET);
        Registry.register(Registries.STATUS_EFFECT, BetterAdventureMode.identifier("poison"), POISON);
        Registry.register(Registries.STATUS_EFFECT, BetterAdventureMode.identifier("shocked"), SHOCKED);

        // region food effects
        // Vanilla
        Registry.register(Registries.STATUS_EFFECT, BetterAdventureMode.identifier("apple_food_effect"), APPLE_FOOD_EFFECT);
        Registry.register(Registries.STATUS_EFFECT, BetterAdventureMode.identifier("baked_potato_food_effect"), BAKED_POTATO_FOOD_EFFECT);
        Registry.register(Registries.STATUS_EFFECT, BetterAdventureMode.identifier("beef_food_effect"), BEEF_FOOD_EFFECT);
        Registry.register(Registries.STATUS_EFFECT, BetterAdventureMode.identifier("beetroot_food_effect"), BEETROOT_FOOD_EFFECT);
        Registry.register(Registries.STATUS_EFFECT, BetterAdventureMode.identifier("beetroot_soup_food_effect"), BEETROOT_SOUP_FOOD_EFFECT);
        Registry.register(Registries.STATUS_EFFECT, BetterAdventureMode.identifier("bread_food_effect"), BREAD_FOOD_EFFECT);
        Registry.register(Registries.STATUS_EFFECT, BetterAdventureMode.identifier("brown_mushroom_food_effect"), BROWN_MUSHROOM_FOOD_EFFECT);
        Registry.register(Registries.STATUS_EFFECT, BetterAdventureMode.identifier("cake_food_effect"), CAKE_FOOD_EFFECT);
        Registry.register(Registries.STATUS_EFFECT, BetterAdventureMode.identifier("carrot_food_effect"), CARROT_FOOD_EFFECT);
        Registry.register(Registries.STATUS_EFFECT, BetterAdventureMode.identifier("chicken_food_effect"), CHICKEN_FOOD_EFFECT);
        Registry.register(Registries.STATUS_EFFECT, BetterAdventureMode.identifier("chorus_fruit_food_effect"), CHORUS_FRUIT_FOOD_EFFECT);
        Registry.register(Registries.STATUS_EFFECT, BetterAdventureMode.identifier("cocoa_beans_food_effect"), COCOA_BEANS_FOOD_EFFECT);
        Registry.register(Registries.STATUS_EFFECT, BetterAdventureMode.identifier("cod_food_effect"), COD_FOOD_EFFECT);
        Registry.register(Registries.STATUS_EFFECT, BetterAdventureMode.identifier("cooked_beef_food_effect"), COOKED_BEEF_FOOD_EFFECT);
        Registry.register(Registries.STATUS_EFFECT, BetterAdventureMode.identifier("cooked_chicken_food_effect"), COOKED_CHICKEN_FOOD_EFFECT);
        Registry.register(Registries.STATUS_EFFECT, BetterAdventureMode.identifier("cooked_cod_food_effect"), COOKED_COD_FOOD_EFFECT);
        Registry.register(Registries.STATUS_EFFECT, BetterAdventureMode.identifier("cooked_mutton_food_effect"), COOKED_MUTTON_FOOD_EFFECT);
        Registry.register(Registries.STATUS_EFFECT, BetterAdventureMode.identifier("cooked_porkchop_food_effect"), COOKED_PORKCHOP_FOOD_EFFECT);
        Registry.register(Registries.STATUS_EFFECT, BetterAdventureMode.identifier("cooked_rabbit_food_effect"), COOKED_RABBIT_FOOD_EFFECT);
        Registry.register(Registries.STATUS_EFFECT, BetterAdventureMode.identifier("cooked_salmon_food_effect"), COOKED_SALMON_FOOD_EFFECT);
        Registry.register(Registries.STATUS_EFFECT, BetterAdventureMode.identifier("cookie_food_effect"), COOKIE_FOOD_EFFECT);
        Registry.register(Registries.STATUS_EFFECT, BetterAdventureMode.identifier("dried_kelp_food_effect"), DRIED_KELP_FOOD_EFFECT);
        Registry.register(Registries.STATUS_EFFECT, BetterAdventureMode.identifier("enchanted_golden_apple_food_effect"), ENCHANTED_GOLDEN_APPLE_FOOD_EFFECT);
        Registry.register(Registries.STATUS_EFFECT, BetterAdventureMode.identifier("fermented_spider_eye_food_effect"), FERMENTED_SPIDER_EYE_FOOD_EFFECT);
        Registry.register(Registries.STATUS_EFFECT, BetterAdventureMode.identifier("glow_berries_food_effect"), GLOW_BERRIES_FOOD_EFFECT);
        Registry.register(Registries.STATUS_EFFECT, BetterAdventureMode.identifier("golden_apple_food_effect"), GOLDEN_APPLE_FOOD_EFFECT);
        Registry.register(Registries.STATUS_EFFECT, BetterAdventureMode.identifier("golden_carrot_food_effect"), GOLDEN_CARROT_FOOD_EFFECT);
        Registry.register(Registries.STATUS_EFFECT, BetterAdventureMode.identifier("honey_bottle_food_effect"), HONEY_BOTTLE_FOOD_EFFECT);
        Registry.register(Registries.STATUS_EFFECT, BetterAdventureMode.identifier("melon_slice_food_effect"), MELON_SLICE_FOOD_EFFECT);
        Registry.register(Registries.STATUS_EFFECT, BetterAdventureMode.identifier("mushroom_stew_food_effect"), MUSHROOM_STEW_FOOD_EFFECT);
        Registry.register(Registries.STATUS_EFFECT, BetterAdventureMode.identifier("mutton_food_effect"), MUTTON_FOOD_EFFECT);
        Registry.register(Registries.STATUS_EFFECT, BetterAdventureMode.identifier("poisonous_potato_food_effect"), POISONOUS_POTATO_FOOD_EFFECT);
        Registry.register(Registries.STATUS_EFFECT, BetterAdventureMode.identifier("porkchop_food_effect"), PORKCHOP_FOOD_EFFECT);
        Registry.register(Registries.STATUS_EFFECT, BetterAdventureMode.identifier("potato_food_effect"), POTATO_FOOD_EFFECT);
        Registry.register(Registries.STATUS_EFFECT, BetterAdventureMode.identifier("pufferfish_food_effect"), PUFFERFISH_FOOD_EFFECT);
        Registry.register(Registries.STATUS_EFFECT, BetterAdventureMode.identifier("pumpkin_pie_food_effect"), PUMPKIN_PIE_FOOD_EFFECT);
        Registry.register(Registries.STATUS_EFFECT, BetterAdventureMode.identifier("rabbit_food_effect"), RABBIT_FOOD_EFFECT);
        Registry.register(Registries.STATUS_EFFECT, BetterAdventureMode.identifier("rabbit_stew_food_effect"), RABBIT_STEW_FOOD_EFFECT);
        Registry.register(Registries.STATUS_EFFECT, BetterAdventureMode.identifier("red_mushroom_food_effect"), RED_MUSHROOM_FOOD_EFFECT);
        Registry.register(Registries.STATUS_EFFECT, BetterAdventureMode.identifier("rotten_flesh_food_effect"), ROTTEN_FLESH_FOOD_EFFECT);
        Registry.register(Registries.STATUS_EFFECT, BetterAdventureMode.identifier("salmon_food_effect"), SALMON_FOOD_EFFECT);
        Registry.register(Registries.STATUS_EFFECT, BetterAdventureMode.identifier("spider_eye_food_effect"), SPIDER_EYE_FOOD_EFFECT);
        Registry.register(Registries.STATUS_EFFECT, BetterAdventureMode.identifier("sugar_food_effect"), SUGAR_FOOD_EFFECT);
        Registry.register(Registries.STATUS_EFFECT, BetterAdventureMode.identifier("suspicious_stew_food_effect"), SUSPICIOUS_STEW_FOOD_EFFECT);
        Registry.register(Registries.STATUS_EFFECT, BetterAdventureMode.identifier("sweet_berries_food_effect"), SWEET_BERRIES_FOOD_EFFECT);
        Registry.register(Registries.STATUS_EFFECT, BetterAdventureMode.identifier("tropical_fish_food_effect"), TROPICAL_FISH_FOOD_EFFECT);
        // endregion food effects

        Registry.register(Registries.STATUS_EFFECT, BetterAdventureMode.identifier("health_regeneration_aura_effect"), HEALTH_REGENERATION_AURA_EFFECT);

        Registry.register(Registries.STATUS_EFFECT, BetterAdventureMode.identifier("test_aura_effect"), TEST_AURA_EFFECT);
        Registry.register(Registries.STATUS_EFFECT, BetterAdventureMode.identifier("mana_regeneration_effect"), MANA_REGENERATION_EFFECT);
    }
}
