package com.github.theredbrain.bamcore.registry;

import com.github.theredbrain.bamcore.BetterAdventureModeCore;
import com.github.theredbrain.bamcore.api.effect.AuraStatusEffect;
import com.github.theredbrain.bamcore.api.effect.FoodStatusEffect;
import com.github.theredbrain.bamcore.api.util.BetterAdventureModCoreAttributeModifierUUIDs;
import com.github.theredbrain.bamcore.effect.*;
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
    public static final StatusEffect SWEET_BERRY_FOOD_EFFECT = new FoodStatusEffect()
            .addAttributeModifier(EntityAttributes.GENERIC_MAX_HEALTH, BetterAdventureModCoreAttributeModifierUUIDs.SWEET_BERRY_FOOD_EFFECT, 30.0F, EntityAttributeModifier.Operation.ADDITION)
            .addAttributeModifier(EntityAttributesRegistry.HEALTH_REGENERATION, BetterAdventureModCoreAttributeModifierUUIDs.SWEET_BERRY_FOOD_EFFECT, 2.0F, EntityAttributeModifier.Operation.ADDITION)
//            .addAttributeModifier(EntityAttributesRegistry.MAX_STAMINA, BetterAdventureModCoreAttributeModifierUUIDs.SWEET_BERRY_FOOD_EFFECT, 25.0F, EntityAttributeModifier.Operation.ADDITION)
//            .addAttributeModifier(EntityAttributesRegistry.STAMINA_REGENERATION, BetterAdventureModCoreAttributeModifierUUIDs.SWEET_BERRY_FOOD_EFFECT, 1.0F, EntityAttributeModifier.Operation.ADDITION)
            ;

    public static final StatusEffect BROWN_MUSHROOM_FOOD_EFFECT = new FoodStatusEffect()
//            .addAttributeModifier(EntityAttributes.GENERIC_MAX_HEALTH, BetterAdventureModCoreAttributeModifierUUIDs.BROWN_MUSHROOM_FOOD_EFFECT, 10.0F, EntityAttributeModifier.Operation.ADDITION)
//            .addAttributeModifier(EntityAttributesRegistry.BAM_HEALTH_REGENERATION, BetterAdventureModCoreAttributeModifierUUIDs.BROWN_MUSHROOM_FOOD_EFFECT, 1.0F, EntityAttributeModifier.Operation.ADDITION)
            .addAttributeModifier(EntityAttributesRegistry.MAX_STAMINA, BetterAdventureModCoreAttributeModifierUUIDs.BROWN_MUSHROOM_FOOD_EFFECT, 30.0F, EntityAttributeModifier.Operation.ADDITION)
            .addAttributeModifier(EntityAttributesRegistry.STAMINA_REGENERATION, BetterAdventureModCoreAttributeModifierUUIDs.BROWN_MUSHROOM_FOOD_EFFECT, 2.0F, EntityAttributeModifier.Operation.ADDITION);

    public static final StatusEffect CHICKEN_MEAL_FOOD_EFFECT = new FoodStatusEffect()
            .addAttributeModifier(EntityAttributes.GENERIC_MAX_HEALTH, BetterAdventureModCoreAttributeModifierUUIDs.CHICKEN_MEAL_FOOD_EFFECT, 60.0F, EntityAttributeModifier.Operation.ADDITION)
//            .addAttributeModifier(EntityAttributesRegistry.BAM_HEALTH_REGENERATION, BetterAdventureModCoreAttributeModifierUUIDs.CHICKEN_MEAL_FOOD_EFFECT, 3.0F, EntityAttributeModifier.Operation.ADDITION)
            .addAttributeModifier(EntityAttributesRegistry.MAX_STAMINA, BetterAdventureModCoreAttributeModifierUUIDs.CHICKEN_MEAL_FOOD_EFFECT, 50.0F, EntityAttributeModifier.Operation.ADDITION)
            .addAttributeModifier(EntityAttributesRegistry.STAMINA_REGENERATION, BetterAdventureModCoreAttributeModifierUUIDs.CHICKEN_MEAL_FOOD_EFFECT, 3.0F, EntityAttributeModifier.Operation.ADDITION);

    public static final StatusEffect RED_MUSHROOM_FOOD_EFFECT = new FoodStatusEffect()
            .addAttributeModifier(EntityAttributes.GENERIC_MAX_HEALTH, BetterAdventureModCoreAttributeModifierUUIDs.RED_MUSHROOM_FOOD_EFFECT, 35.0F, EntityAttributeModifier.Operation.ADDITION)
            .addAttributeModifier(EntityAttributesRegistry.MAX_STAMINA, BetterAdventureModCoreAttributeModifierUUIDs.RED_MUSHROOM_FOOD_EFFECT, 35.0F, EntityAttributeModifier.Operation.ADDITION)
            .addAttributeModifier(EntityAttributesRegistry.MAX_MANA, BetterAdventureModCoreAttributeModifierUUIDs.RED_MUSHROOM_FOOD_EFFECT, 35.0F, EntityAttributeModifier.Operation.ADDITION);
    public static final StatusEffect GLOW_BERRY_FOOD_EFFECT = new FoodStatusEffect()
            .addAttributeModifier(EntityAttributesRegistry.MAX_MANA, BetterAdventureModCoreAttributeModifierUUIDs.GLOW_BERRY_FOOD_EFFECT, 30.0F, EntityAttributeModifier.Operation.ADDITION)
            .addAttributeModifier(EntityAttributesRegistry.MANA_REGENERATION, BetterAdventureModCoreAttributeModifierUUIDs.GLOW_BERRY_FOOD_EFFECT, 2.0F, EntityAttributeModifier.Operation.ADDITION);
    //endregion Food Effects
    //region Aura Effects
    public static final StatusEffect HEALTH_REGENERATION_AURA_EFFECT = new AuraStatusEffect(StatusEffects.REGENERATION)
            .addAttributeModifier(EntityAttributesRegistry.MAX_MANA, BetterAdventureModCoreAttributeModifierUUIDs.AURA_EFFECT, -0.25F, EntityAttributeModifier.Operation.MULTIPLY_TOTAL)
            ;

    //endregion Aura Effects

    public static final StatusEffect ADVENTURE_BUILDING_EFFECT = new NeutralStatusEffect();
    public static final StatusEffect CIVILISATION_EFFECT = new NeutralStatusEffect();
    public static final StatusEffect MANA_REGENERATION_EFFECT = new BeneficialStatusEffect();
    public static final StatusEffect NEED_EMPTY_OFFHAND_EFFECT = new NeutralStatusEffect();
    public static final StatusEffect NO_ATTACK_ITEMS_EFFECT = new NeutralStatusEffect();
    public static final StatusEffect OVERBURDENED_EFFECT = new HarmfulStatusEffect().addAttributeModifier(EntityAttributes.GENERIC_MOVEMENT_SPEED, BetterAdventureModCoreAttributeModifierUUIDs.OVERBURDENED_EFFECT, -0.25, EntityAttributeModifier.Operation.MULTIPLY_TOTAL);
    public static final StatusEffect WEAPONS_SHEATHED_EFFECT = new NeutralStatusEffect();
    public static final StatusEffect TWO_HANDED_EFFECT = new NeutralStatusEffect();
    public static final StatusEffect PORTAL_RESISTANCE_EFFECT = new NeutralStatusEffect();
    public static final StatusEffect STAGGERED = new HarmfulStatusEffect();
    public static final StatusEffect BURNING = new HarmfulStatusEffect();
    public static final StatusEffect CHILLED = new HarmfulStatusEffect();
    public static final StatusEffect FROZEN = new HarmfulStatusEffect();
    public static final StatusEffect WET = new HarmfulStatusEffect();
    public static final StatusEffect TEST_AURA_EFFECT = new AuraStatusEffect(StatusEffects.GLOWING)
            .addAttributeModifier(EntityAttributesRegistry.MAX_MANA, BetterAdventureModCoreAttributeModifierUUIDs.AURA_EFFECT, -0.25F, EntityAttributeModifier.Operation.MULTIPLY_TOTAL)
            ;

    public static void registerEffects() {
        // --- Configuration ---
        // utility effects
        ActionImpairing.configure(NO_ATTACK_ITEMS_EFFECT, new EntityActionsAllowed(true, true, new EntityActionsAllowed.PlayersAllowed(false, true, true), new EntityActionsAllowed.MobsAllowed(true), EntityActionsAllowed.SemanticType.NONE));
        ActionImpairing.configure(NEED_EMPTY_OFFHAND_EFFECT, new EntityActionsAllowed(true, true, new EntityActionsAllowed.PlayersAllowed(false, false, false), new EntityActionsAllowed.MobsAllowed(true), EntityActionsAllowed.SemanticType.NONE));
        ActionImpairing.configure(STAGGERED, new EntityActionsAllowed(false, false, new EntityActionsAllowed.PlayersAllowed(false, false, false), new EntityActionsAllowed.MobsAllowed(false), EntityActionsAllowed.SemanticType.STUN));
//        ActionImpairing.configure(OVERBURDENED_EFFECT, new EntityActionsAllowed(false, true, new EntityActionsAllowed.PlayersAllowed(true, true, true), new EntityActionsAllowed.MobsAllowed(true), EntityActionsAllowed.SemanticType.NONE));
        Synchronized.configure(WEAPONS_SHEATHED_EFFECT, true);
        Synchronized.configure(TWO_HANDED_EFFECT, true);
        Synchronized.configure(STAGGERED, true);
        Synchronized.configure(BURNING, true);
        Synchronized.configure(CHILLED, true);
        Synchronized.configure(FROZEN, true);
        Synchronized.configure(WET, true);
        Synchronized.configure(TEST_AURA_EFFECT, true);
        Synchronized.configure(HEALTH_REGENERATION_AURA_EFFECT, true);
//        RemoveOnHit.configure(WEAPONS_SHEATHED_EFFECT, true);

        // --- Registration ---
        // utility effects
        Registry.register(Registries.STATUS_EFFECT, BetterAdventureModeCore.identifier("adventure_building_effect"), ADVENTURE_BUILDING_EFFECT);
        Registry.register(Registries.STATUS_EFFECT, BetterAdventureModeCore.identifier("civilisation_effect"), CIVILISATION_EFFECT);
        Registry.register(Registries.STATUS_EFFECT, BetterAdventureModeCore.identifier("need_empty_offhand_effect"), NEED_EMPTY_OFFHAND_EFFECT);
        Registry.register(Registries.STATUS_EFFECT, BetterAdventureModeCore.identifier("no_attack_items_effect"), NO_ATTACK_ITEMS_EFFECT);
        Registry.register(Registries.STATUS_EFFECT, BetterAdventureModeCore.identifier("weapons_sheathed_effect"), WEAPONS_SHEATHED_EFFECT);
        Registry.register(Registries.STATUS_EFFECT, BetterAdventureModeCore.identifier("two_handed_effect"), TWO_HANDED_EFFECT);
        Registry.register(Registries.STATUS_EFFECT, BetterAdventureModeCore.identifier("portal_resistance_effect"), PORTAL_RESISTANCE_EFFECT);


        Registry.register(Registries.STATUS_EFFECT, BetterAdventureModeCore.identifier("overburdened_effect"), OVERBURDENED_EFFECT);
        Registry.register(Registries.STATUS_EFFECT, BetterAdventureModeCore.identifier("staggered"), STAGGERED);

        Registry.register(Registries.STATUS_EFFECT, BetterAdventureModeCore.identifier("burning"), BURNING);
        Registry.register(Registries.STATUS_EFFECT, BetterAdventureModeCore.identifier("chilled"), CHILLED);
        Registry.register(Registries.STATUS_EFFECT, BetterAdventureModeCore.identifier("frozen"), FROZEN);
        Registry.register(Registries.STATUS_EFFECT, BetterAdventureModeCore.identifier("wet"), WET);

        // food effects
        Registry.register(Registries.STATUS_EFFECT, BetterAdventureModeCore.identifier("sweet_berry_food_effect"), SWEET_BERRY_FOOD_EFFECT);
        Registry.register(Registries.STATUS_EFFECT, BetterAdventureModeCore.identifier("brown_mushroom_food_effect"), BROWN_MUSHROOM_FOOD_EFFECT);
        Registry.register(Registries.STATUS_EFFECT, BetterAdventureModeCore.identifier("chicken_meal_food_effect"), CHICKEN_MEAL_FOOD_EFFECT);
        Registry.register(Registries.STATUS_EFFECT, BetterAdventureModeCore.identifier("red_mushroom_food_effect"), RED_MUSHROOM_FOOD_EFFECT);
        Registry.register(Registries.STATUS_EFFECT, BetterAdventureModeCore.identifier("glow_berry_food_effect"), GLOW_BERRY_FOOD_EFFECT);

        Registry.register(Registries.STATUS_EFFECT, BetterAdventureModeCore.identifier("health_regeneration_aura_effect"), HEALTH_REGENERATION_AURA_EFFECT);

        Registry.register(Registries.STATUS_EFFECT, BetterAdventureModeCore.identifier("test_aura_effect"), TEST_AURA_EFFECT);
        Registry.register(Registries.STATUS_EFFECT, BetterAdventureModeCore.identifier("mana_regeneration_effect"), MANA_REGENERATION_EFFECT);
    }
}
