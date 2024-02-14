package com.github.theredbrain.betteradventuremode.registry;

import com.github.theredbrain.betteradventuremode.BetterAdventureMode;
import com.github.theredbrain.betteradventuremode.effect.AuraStatusEffect;
import com.github.theredbrain.betteradventuremode.effect.FoodStatusEffect;
import com.github.theredbrain.betteradventuremode.util.AttributeModifierUUIDs;
import com.github.theredbrain.betteradventuremode.effect.*;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
//import net.spell_engine.api.effect.ActionImpairing;
//import net.spell_engine.api.effect.EntityActionsAllowed;
//import net.spell_engine.api.effect.Synchronized;

public class StatusEffectsRegistry {

    //region Food Effects
    public static final StatusEffect SWEET_BERRIES_FOOD_EFFECT = new FoodStatusEffect()
            .addAttributeModifier(EntityAttributes.GENERIC_MAX_HEALTH, AttributeModifierUUIDs.SWEET_BERRY_FOOD_EFFECT, 30.0F, EntityAttributeModifier.Operation.ADDITION)
            .addAttributeModifier(EntityAttributesRegistry.HEALTH_REGENERATION, AttributeModifierUUIDs.SWEET_BERRY_FOOD_EFFECT, 2.0F, EntityAttributeModifier.Operation.ADDITION)
//            .addAttributeModifier(EntityAttributesRegistry.MAX_STAMINA, BetterAdventureModCoreAttributeModifierUUIDs.SWEET_BERRY_FOOD_EFFECT, 25.0F, EntityAttributeModifier.Operation.ADDITION)
//            .addAttributeModifier(EntityAttributesRegistry.STAMINA_REGENERATION, BetterAdventureModCoreAttributeModifierUUIDs.SWEET_BERRY_FOOD_EFFECT, 1.0F, EntityAttributeModifier.Operation.ADDITION)
            ;

    public static final StatusEffect BROWN_MUSHROOM_FOOD_EFFECT = new FoodStatusEffect()
//            .addAttributeModifier(EntityAttributes.GENERIC_MAX_HEALTH, BetterAdventureModCoreAttributeModifierUUIDs.BROWN_MUSHROOM_FOOD_EFFECT, 10.0F, EntityAttributeModifier.Operation.ADDITION)
//            .addAttributeModifier(EntityAttributesRegistry.BAM_HEALTH_REGENERATION, BetterAdventureModCoreAttributeModifierUUIDs.BROWN_MUSHROOM_FOOD_EFFECT, 1.0F, EntityAttributeModifier.Operation.ADDITION)
            .addAttributeModifier(EntityAttributesRegistry.MAX_STAMINA, AttributeModifierUUIDs.BROWN_MUSHROOM_FOOD_EFFECT, 30.0F, EntityAttributeModifier.Operation.ADDITION)
            .addAttributeModifier(EntityAttributesRegistry.STAMINA_REGENERATION, AttributeModifierUUIDs.BROWN_MUSHROOM_FOOD_EFFECT, 2.0F, EntityAttributeModifier.Operation.ADDITION);

    public static final StatusEffect CHICKEN_MEAL_FOOD_EFFECT = new FoodStatusEffect()
            .addAttributeModifier(EntityAttributes.GENERIC_MAX_HEALTH, AttributeModifierUUIDs.CHICKEN_MEAL_FOOD_EFFECT, 60.0F, EntityAttributeModifier.Operation.ADDITION)
//            .addAttributeModifier(EntityAttributesRegistry.BAM_HEALTH_REGENERATION, BetterAdventureModCoreAttributeModifierUUIDs.CHICKEN_MEAL_FOOD_EFFECT, 3.0F, EntityAttributeModifier.Operation.ADDITION)
            .addAttributeModifier(EntityAttributesRegistry.MAX_STAMINA, AttributeModifierUUIDs.CHICKEN_MEAL_FOOD_EFFECT, 50.0F, EntityAttributeModifier.Operation.ADDITION)
            .addAttributeModifier(EntityAttributesRegistry.STAMINA_REGENERATION, AttributeModifierUUIDs.CHICKEN_MEAL_FOOD_EFFECT, 3.0F, EntityAttributeModifier.Operation.ADDITION);

    public static final StatusEffect RED_MUSHROOM_FOOD_EFFECT = new FoodStatusEffect()
            .addAttributeModifier(EntityAttributes.GENERIC_MAX_HEALTH, AttributeModifierUUIDs.RED_MUSHROOM_FOOD_EFFECT, 35.0F, EntityAttributeModifier.Operation.ADDITION)
            .addAttributeModifier(EntityAttributesRegistry.MAX_STAMINA, AttributeModifierUUIDs.RED_MUSHROOM_FOOD_EFFECT, 35.0F, EntityAttributeModifier.Operation.ADDITION)
            .addAttributeModifier(EntityAttributesRegistry.MAX_MANA, AttributeModifierUUIDs.RED_MUSHROOM_FOOD_EFFECT, 35.0F, EntityAttributeModifier.Operation.ADDITION);
    public static final StatusEffect GLOW_BERRIES_FOOD_EFFECT = new FoodStatusEffect()
            .addAttributeModifier(EntityAttributesRegistry.MAX_MANA, AttributeModifierUUIDs.GLOW_BERRY_FOOD_EFFECT, 30.0F, EntityAttributeModifier.Operation.ADDITION)
            .addAttributeModifier(EntityAttributesRegistry.MANA_REGENERATION, AttributeModifierUUIDs.GLOW_BERRY_FOOD_EFFECT, 2.0F, EntityAttributeModifier.Operation.ADDITION);
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
    public static final StatusEffect CIVILISATION_EFFECT = new NeutralStatusEffect();
    public static final StatusEffect MANA_REGENERATION_EFFECT = new BeneficialStatusEffect();
    public static final StatusEffect NEED_EMPTY_OFFHAND_EFFECT = new NeutralStatusEffect();
    public static final StatusEffect NO_ATTACK_ITEMS_EFFECT = new NeutralStatusEffect();
    public static final StatusEffect OVERBURDENED_EFFECT = new HarmfulStatusEffect().addAttributeModifier(EntityAttributes.GENERIC_MOVEMENT_SPEED, AttributeModifierUUIDs.OVERBURDENED_EFFECT, -0.25, EntityAttributeModifier.Operation.MULTIPLY_TOTAL);
    public static final StatusEffect WEAPONS_SHEATHED_EFFECT = new NeutralStatusEffect();
    public static final StatusEffect TWO_HANDED_EFFECT = new NeutralStatusEffect();
    public static final StatusEffect PORTAL_RESISTANCE_EFFECT = new NeutralStatusEffect();
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
        // TODO SpellEngine 1.20.2
//        ActionImpairing.configure(NO_ATTACK_ITEMS_EFFECT, new EntityActionsAllowed(true, true, new EntityActionsAllowed.PlayersAllowed(false, true, true), new EntityActionsAllowed.MobsAllowed(true), EntityActionsAllowed.SemanticType.NONE));
//        ActionImpairing.configure(NEED_EMPTY_OFFHAND_EFFECT, new EntityActionsAllowed(true, true, new EntityActionsAllowed.PlayersAllowed(false, false, false), new EntityActionsAllowed.MobsAllowed(true), EntityActionsAllowed.SemanticType.NONE));
//        ActionImpairing.configure(STAGGERED, new EntityActionsAllowed(false, false, new EntityActionsAllowed.PlayersAllowed(false, false, false), new EntityActionsAllowed.MobsAllowed(false), EntityActionsAllowed.SemanticType.STUN));
////        ActionImpairing.configure(OVERBURDENED_EFFECT, new EntityActionsAllowed(false, true, new EntityActionsAllowed.PlayersAllowed(true, true, true), new EntityActionsAllowed.MobsAllowed(true), EntityActionsAllowed.SemanticType.NONE));
//        Synchronized.configure(WEAPONS_SHEATHED_EFFECT, true);
//        Synchronized.configure(TWO_HANDED_EFFECT, true);
//        Synchronized.configure(STAGGERED, true);
//        Synchronized.configure(BURNING, true);
//        Synchronized.configure(CHILLED, true);
//        Synchronized.configure(FROZEN, true);
//        Synchronized.configure(WET, true);
//        Synchronized.configure(TEST_AURA_EFFECT, true);
//        Synchronized.configure(HEALTH_REGENERATION_AURA_EFFECT, true);

//        RemoveOnHit.configure(WEAPONS_SHEATHED_EFFECT, true);

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
        Registry.register(Registries.STATUS_EFFECT, BetterAdventureMode.identifier("need_empty_offhand_effect"), NEED_EMPTY_OFFHAND_EFFECT);
        Registry.register(Registries.STATUS_EFFECT, BetterAdventureMode.identifier("no_attack_items_effect"), NO_ATTACK_ITEMS_EFFECT);
        Registry.register(Registries.STATUS_EFFECT, BetterAdventureMode.identifier("weapons_sheathed_effect"), WEAPONS_SHEATHED_EFFECT);
        Registry.register(Registries.STATUS_EFFECT, BetterAdventureMode.identifier("two_handed_effect"), TWO_HANDED_EFFECT);
        Registry.register(Registries.STATUS_EFFECT, BetterAdventureMode.identifier("portal_resistance_effect"), PORTAL_RESISTANCE_EFFECT);


        Registry.register(Registries.STATUS_EFFECT, BetterAdventureMode.identifier("overburdened_effect"), OVERBURDENED_EFFECT);
        Registry.register(Registries.STATUS_EFFECT, BetterAdventureMode.identifier("staggered"), STAGGERED);

        Registry.register(Registries.STATUS_EFFECT, BetterAdventureMode.identifier("bleeding"), BLEEDING);
        Registry.register(Registries.STATUS_EFFECT, BetterAdventureMode.identifier("burning"), BURNING);
        Registry.register(Registries.STATUS_EFFECT, BetterAdventureMode.identifier("chilled"), CHILLED);
        Registry.register(Registries.STATUS_EFFECT, BetterAdventureMode.identifier("frozen"), FROZEN);
        Registry.register(Registries.STATUS_EFFECT, BetterAdventureMode.identifier("wet"), WET);
        Registry.register(Registries.STATUS_EFFECT, BetterAdventureMode.identifier("poison"), POISON);
        Registry.register(Registries.STATUS_EFFECT, BetterAdventureMode.identifier("shocked"), SHOCKED);

        // food effects
        Registry.register(Registries.STATUS_EFFECT, BetterAdventureMode.identifier("sweet_berry_food_effect"), SWEET_BERRIES_FOOD_EFFECT);
        Registry.register(Registries.STATUS_EFFECT, BetterAdventureMode.identifier("brown_mushroom_food_effect"), BROWN_MUSHROOM_FOOD_EFFECT);
        Registry.register(Registries.STATUS_EFFECT, BetterAdventureMode.identifier("chicken_meal_food_effect"), CHICKEN_MEAL_FOOD_EFFECT);
        Registry.register(Registries.STATUS_EFFECT, BetterAdventureMode.identifier("red_mushroom_food_effect"), RED_MUSHROOM_FOOD_EFFECT);
        Registry.register(Registries.STATUS_EFFECT, BetterAdventureMode.identifier("glow_berry_food_effect"), GLOW_BERRIES_FOOD_EFFECT);

        Registry.register(Registries.STATUS_EFFECT, BetterAdventureMode.identifier("health_regeneration_aura_effect"), HEALTH_REGENERATION_AURA_EFFECT);

        Registry.register(Registries.STATUS_EFFECT, BetterAdventureMode.identifier("test_aura_effect"), TEST_AURA_EFFECT);
        Registry.register(Registries.STATUS_EFFECT, BetterAdventureMode.identifier("mana_regeneration_effect"), MANA_REGENERATION_EFFECT);
    }
}
