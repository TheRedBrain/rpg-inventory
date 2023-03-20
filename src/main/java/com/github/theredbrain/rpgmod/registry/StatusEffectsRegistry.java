package com.github.theredbrain.rpgmod.registry;

import com.github.theredbrain.rpgmod.RPGMod;
import com.github.theredbrain.rpgmod.effect.*;
import com.github.theredbrain.rpgmod.util.AttributeModifierUUIDs;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class StatusEffectsRegistry {

    // food effects
    public static final StatusEffect BERRY_FOOD_EFFECT = new BerryFoodEffect()
            .addAttributeModifier(EntityAttributes.GENERIC_MAX_HEALTH, AttributeModifierUUIDs.MAX_HEALTH_BERRY_FOOD_EFFECT, 20.0F, EntityAttributeModifier.Operation.ADDITION)
            .addAttributeModifier(EntityAttributesRegistry.BAM_HEALTH_REGENERATION, AttributeModifierUUIDs.HEALTH_REGENERATION_BERRY_FOOD_EFFECT, 1.0F, EntityAttributeModifier.Operation.ADDITION)
            .addAttributeModifier(EntityAttributesRegistry.BAM_MAX_STAMINA, AttributeModifierUUIDs.MAX_STAMINA_BERRY_FOOD_EFFECT, 25.0F, EntityAttributeModifier.Operation.ADDITION)
            .addAttributeModifier(EntityAttributesRegistry.BAM_STAMINA_REGENERATION, AttributeModifierUUIDs.STAMINA_REGENERATION_BERRY_FOOD_EFFECT, 1.0F, EntityAttributeModifier.Operation.ADDITION);

    public static final StatusEffect BROWN_MUSHROOM_FOOD_EFFECT = new BrownMushroomFoodEffect()
            .addAttributeModifier(EntityAttributes.GENERIC_MAX_HEALTH, AttributeModifierUUIDs.MAX_HEALTH_BROWN_MUSHROOM_FOOD_EFFECT, 10.0F, EntityAttributeModifier.Operation.ADDITION)
            .addAttributeModifier(EntityAttributesRegistry.BAM_HEALTH_REGENERATION, AttributeModifierUUIDs.HEALTH_REGENERATION_BROWN_MUSHROOM_FOOD_EFFECT, 1.0F, EntityAttributeModifier.Operation.ADDITION)
            .addAttributeModifier(EntityAttributesRegistry.BAM_MAX_STAMINA, AttributeModifierUUIDs.MAX_STAMINA_BROWN_MUSHROOM_FOOD_EFFECT, 30.0F, EntityAttributeModifier.Operation.ADDITION)
            .addAttributeModifier(EntityAttributesRegistry.BAM_STAMINA_REGENERATION, AttributeModifierUUIDs.STAMINA_REGENERATION_BROWN_MUSHROOM_FOOD_EFFECT, 1.0F, EntityAttributeModifier.Operation.ADDITION);

    public static final StatusEffect CHICKEN_MEAL_FOOD_EFFECT = new ChickenMealFoodEffect()
            .addAttributeModifier(EntityAttributes.GENERIC_MAX_HEALTH, AttributeModifierUUIDs.MAX_HEALTH_CHICKEN_MEAL_FOOD_EFFECT, 60.0F, EntityAttributeModifier.Operation.ADDITION)
            .addAttributeModifier(EntityAttributesRegistry.BAM_HEALTH_REGENERATION, AttributeModifierUUIDs.HEALTH_REGENERATION_CHICKEN_MEAL_FOOD_EFFECT, 3.0F, EntityAttributeModifier.Operation.ADDITION)
            .addAttributeModifier(EntityAttributesRegistry.BAM_MAX_STAMINA, AttributeModifierUUIDs.MAX_STAMINA_CHICKEN_MEAL_FOOD_EFFECT, 50.0F, EntityAttributeModifier.Operation.ADDITION)
            .addAttributeModifier(EntityAttributesRegistry.BAM_STAMINA_REGENERATION, AttributeModifierUUIDs.STAMINA_REGENERATION_CHICKEN_MEAL_FOOD_EFFECT, 3.0F, EntityAttributeModifier.Operation.ADDITION);

    public static final StatusEffect RED_MUSHROOM_FOOD_EFFECT = new RedMushroomFoodEffect()
            .addAttributeModifier(EntityAttributes.GENERIC_MAX_HEALTH, AttributeModifierUUIDs.MAX_HEALTH_RED_MUSHROOM_FOOD_EFFECT, 40.0F, EntityAttributeModifier.Operation.ADDITION)
            .addAttributeModifier(EntityAttributesRegistry.BAM_HEALTH_REGENERATION, AttributeModifierUUIDs.HEALTH_REGENERATION_RED_MUSHROOM_FOOD_EFFECT, 2.0F, EntityAttributeModifier.Operation.ADDITION)
            .addAttributeModifier(EntityAttributesRegistry.BAM_MAX_STAMINA, AttributeModifierUUIDs.MAX_STAMINA_RED_MUSHROOM_FOOD_EFFECT, 35.0F, EntityAttributeModifier.Operation.ADDITION)
            .addAttributeModifier(EntityAttributesRegistry.BAM_STAMINA_REGENERATION, AttributeModifierUUIDs.STAMINA_REGENERATION_RED_MUSHROOM_FOOD_EFFECT, 2.0F, EntityAttributeModifier.Operation.ADDITION);

    public static final StatusEffect ADVENTURE_BUILDING_EFFECT = new AdventureBuildingStatusEffect();
    public static final StatusEffect CIVILISATION_EFFECT = new CivilisationStatusEffect();
    public static final StatusEffect PERMANENT_MOUNT_EFFECT = new PermanentMountStatusEffect();

    public static void registerEffects() {
        // food effects
        Registry.register(Registries.STATUS_EFFECT, new Identifier(RPGMod.MOD_ID, "berry_food_effect"), BERRY_FOOD_EFFECT);
        Registry.register(Registries.STATUS_EFFECT, new Identifier(RPGMod.MOD_ID, "brown_mushroom_food_effect"), BROWN_MUSHROOM_FOOD_EFFECT);
        Registry.register(Registries.STATUS_EFFECT, new Identifier(RPGMod.MOD_ID, "chicken_meal_food_effect"), CHICKEN_MEAL_FOOD_EFFECT);
        Registry.register(Registries.STATUS_EFFECT, new Identifier(RPGMod.MOD_ID, "red_mushroom_food_effect"), RED_MUSHROOM_FOOD_EFFECT);

        // utility effects
        Registry.register(Registries.STATUS_EFFECT, new Identifier(RPGMod.MOD_ID, "adventure_building_effect"), ADVENTURE_BUILDING_EFFECT);
        Registry.register(Registries.STATUS_EFFECT, new Identifier(RPGMod.MOD_ID, "civilisation_effect"), CIVILISATION_EFFECT);
        Registry.register(Registries.STATUS_EFFECT, new Identifier(RPGMod.MOD_ID, "permanent_mount_effect"), PERMANENT_MOUNT_EFFECT);
    }
}
