package com.github.theredbrain.bamcore.registry;

import com.github.theredbrain.bamcore.BetterAdventureModCore;
import com.github.theredbrain.bamcore.effect.*;
import com.github.theredbrain.bamcore.util.AttributeModifierUUIDs;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.spell_engine.api.effect.ActionImpairing;
import net.spell_engine.api.effect.EntityActionsAllowed;
import net.spell_engine.api.effect.RemoveOnHit;
import net.spell_engine.api.effect.Synchronized;

public class StatusEffectsRegistry {

    // food effects
    public static final StatusEffect BERRY_FOOD_EFFECT = new BerryFoodEffect()
            .addAttributeModifier(EntityAttributes.GENERIC_MAX_HEALTH, AttributeModifierUUIDs.BERRY_FOOD_EFFECT, 20.0F, EntityAttributeModifier.Operation.ADDITION)
//            .addAttributeModifier(EntityAttributesRegistry.BAM_HEALTH_REGENERATION, AttributeModifierUUIDs.BERRY_FOOD_EFFECT, 1.0F, EntityAttributeModifier.Operation.ADDITION)
            .addAttributeModifier(EntityAttributesRegistry.MAX_STAMINA, AttributeModifierUUIDs.BERRY_FOOD_EFFECT, 25.0F, EntityAttributeModifier.Operation.ADDITION)
            .addAttributeModifier(EntityAttributesRegistry.STAMINA_REGENERATION, AttributeModifierUUIDs.BERRY_FOOD_EFFECT, 1.0F, EntityAttributeModifier.Operation.ADDITION);

    public static final StatusEffect BROWN_MUSHROOM_FOOD_EFFECT = new BrownMushroomFoodEffect()
            .addAttributeModifier(EntityAttributes.GENERIC_MAX_HEALTH, AttributeModifierUUIDs.BROWN_MUSHROOM_FOOD_EFFECT, 10.0F, EntityAttributeModifier.Operation.ADDITION)
//            .addAttributeModifier(EntityAttributesRegistry.BAM_HEALTH_REGENERATION, AttributeModifierUUIDs.BROWN_MUSHROOM_FOOD_EFFECT, 1.0F, EntityAttributeModifier.Operation.ADDITION)
            .addAttributeModifier(EntityAttributesRegistry.MAX_STAMINA, AttributeModifierUUIDs.BROWN_MUSHROOM_FOOD_EFFECT, 30.0F, EntityAttributeModifier.Operation.ADDITION)
            .addAttributeModifier(EntityAttributesRegistry.STAMINA_REGENERATION, AttributeModifierUUIDs.BROWN_MUSHROOM_FOOD_EFFECT, 1.0F, EntityAttributeModifier.Operation.ADDITION);

    public static final StatusEffect CHICKEN_MEAL_FOOD_EFFECT = new ChickenMealFoodEffect()
            .addAttributeModifier(EntityAttributes.GENERIC_MAX_HEALTH, AttributeModifierUUIDs.CHICKEN_MEAL_FOOD_EFFECT, 60.0F, EntityAttributeModifier.Operation.ADDITION)
//            .addAttributeModifier(EntityAttributesRegistry.BAM_HEALTH_REGENERATION, AttributeModifierUUIDs.CHICKEN_MEAL_FOOD_EFFECT, 3.0F, EntityAttributeModifier.Operation.ADDITION)
            .addAttributeModifier(EntityAttributesRegistry.MAX_STAMINA, AttributeModifierUUIDs.CHICKEN_MEAL_FOOD_EFFECT, 50.0F, EntityAttributeModifier.Operation.ADDITION)
            .addAttributeModifier(EntityAttributesRegistry.STAMINA_REGENERATION, AttributeModifierUUIDs.CHICKEN_MEAL_FOOD_EFFECT, 3.0F, EntityAttributeModifier.Operation.ADDITION);

    public static final StatusEffect RED_MUSHROOM_FOOD_EFFECT = new RedMushroomFoodEffect()
            .addAttributeModifier(EntityAttributes.GENERIC_MAX_HEALTH, AttributeModifierUUIDs.RED_MUSHROOM_FOOD_EFFECT, 40.0F, EntityAttributeModifier.Operation.ADDITION)
//            .addAttributeModifier(EntityAttributesRegistry.BAM_HEALTH_REGENERATION, AttributeModifierUUIDs.RED_MUSHROOM_FOOD_EFFECT, 2.0F, EntityAttributeModifier.Operation.ADDITION)
            .addAttributeModifier(EntityAttributesRegistry.MAX_STAMINA, AttributeModifierUUIDs.RED_MUSHROOM_FOOD_EFFECT, 35.0F, EntityAttributeModifier.Operation.ADDITION)
            .addAttributeModifier(EntityAttributesRegistry.STAMINA_REGENERATION, AttributeModifierUUIDs.RED_MUSHROOM_FOOD_EFFECT, 2.0F, EntityAttributeModifier.Operation.ADDITION);

    public static final StatusEffect ADVENTURE_BUILDING_EFFECT = new AdventureBuildingStatusEffect();
    public static final StatusEffect CIVILISATION_EFFECT = new CivilisationStatusEffect();
    public static final StatusEffect PORTAL_RESISTANCE_EFFECT = new PortalResistanceStatusEffect();
    public static final StatusEffect PERMANENT_MOUNT_EFFECT = new PermanentMountStatusEffect();
    public static final StatusEffect NO_ATTACK_ITEMS_EFFECT = new NoAttackItemsStatusEffect();
    public static final StatusEffect NEED_EMPTY_OFFHAND_EFFECT = new NeedEmptyOffhandStatusEffect();
    public static final StatusEffect WEAPONS_SHEATHED_EFFECT = new WeaponsSheathedStatusEffect();

    public static void registerEffects() {
        ActionImpairing.configure(NO_ATTACK_ITEMS_EFFECT, new EntityActionsAllowed(true, true, new EntityActionsAllowed.PlayersAllowed(false, true, true), new EntityActionsAllowed.MobsAllowed(true), EntityActionsAllowed.SemanticType.NONE));
        ActionImpairing.configure(NEED_EMPTY_OFFHAND_EFFECT, new EntityActionsAllowed(true, true, new EntityActionsAllowed.PlayersAllowed(false, false, false), new EntityActionsAllowed.MobsAllowed(true), EntityActionsAllowed.SemanticType.NONE));
        Synchronized.configure(WEAPONS_SHEATHED_EFFECT, true);
        RemoveOnHit.configure(WEAPONS_SHEATHED_EFFECT, true);

        // food effects
        Registry.register(Registries.STATUS_EFFECT, BetterAdventureModCore.identifier("berry_food_effect"), BERRY_FOOD_EFFECT);
        Registry.register(Registries.STATUS_EFFECT, BetterAdventureModCore.identifier("brown_mushroom_food_effect"), BROWN_MUSHROOM_FOOD_EFFECT);
        Registry.register(Registries.STATUS_EFFECT, BetterAdventureModCore.identifier("chicken_meal_food_effect"), CHICKEN_MEAL_FOOD_EFFECT);
        Registry.register(Registries.STATUS_EFFECT, BetterAdventureModCore.identifier("red_mushroom_food_effect"), RED_MUSHROOM_FOOD_EFFECT);

        // utility effects
        Registry.register(Registries.STATUS_EFFECT, BetterAdventureModCore.identifier("adventure_building_effect"), ADVENTURE_BUILDING_EFFECT);
        Registry.register(Registries.STATUS_EFFECT, BetterAdventureModCore.identifier("civilisation_effect"), CIVILISATION_EFFECT);
        Registry.register(Registries.STATUS_EFFECT, BetterAdventureModCore.identifier("portal_resistance_effect"), PORTAL_RESISTANCE_EFFECT);
        Registry.register(Registries.STATUS_EFFECT, BetterAdventureModCore.identifier("permanent_mount_effect"), PERMANENT_MOUNT_EFFECT);
        Registry.register(Registries.STATUS_EFFECT, BetterAdventureModCore.identifier("no_attack_items_effect"), NO_ATTACK_ITEMS_EFFECT);
        Registry.register(Registries.STATUS_EFFECT, BetterAdventureModCore.identifier("need_empty_offhand_effect"), NEED_EMPTY_OFFHAND_EFFECT);
        Registry.register(Registries.STATUS_EFFECT, BetterAdventureModCore.identifier("weapons_sheathed_effect"), WEAPONS_SHEATHED_EFFECT);
    }
}
