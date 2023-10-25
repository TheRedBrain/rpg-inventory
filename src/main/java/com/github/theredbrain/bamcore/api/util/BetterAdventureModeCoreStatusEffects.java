package com.github.theredbrain.bamcore.api.util;

import com.github.theredbrain.bamcore.BetterAdventureModeCore;
import com.github.theredbrain.bamcore.api.effect.AuraStatusEffect;
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

public class BetterAdventureModeCoreStatusEffects {

    public static final StatusEffect ADVENTURE_BUILDING_EFFECT = new NeutralStatusEffect();
    public static final StatusEffect CIVILISATION_EFFECT = new NeutralStatusEffect();
    public static final StatusEffect MANA_REGENERATION_EFFECT = new BeneficialStatusEffect();
    public static final StatusEffect NEED_EMPTY_OFFHAND_EFFECT = new NeutralStatusEffect();
    public static final StatusEffect NO_ATTACK_ITEMS_EFFECT = new NeutralStatusEffect();
    public static final StatusEffect OVERBURDENED_EFFECT = new HarmfulStatusEffect().addAttributeModifier(EntityAttributes.GENERIC_MOVEMENT_SPEED, BetterAdventureModCoreAttributeModifierUUIDs.OVERBURDENED_EFFECT, -0.25, EntityAttributeModifier.Operation.MULTIPLY_TOTAL);
    public static final StatusEffect WEAPONS_SHEATHED_EFFECT = new NeutralStatusEffect();
    public static final StatusEffect TWO_HANDED_EFFECT = new NeutralStatusEffect();
    public static final StatusEffect STAGGERED = new HarmfulStatusEffect();
    public static final StatusEffect BURNING = new HarmfulStatusEffect();
    public static final StatusEffect CHILLED = new HarmfulStatusEffect();
    public static final StatusEffect FROZEN = new HarmfulStatusEffect();
    public static final StatusEffect WET = new HarmfulStatusEffect();
    public static final StatusEffect TEST_AURA_EFFECT = new AuraStatusEffect(StatusEffects.GLOWING)
            .addAttributeModifier(BetterAdventureModeCoreEntityAttributes.MAX_MANA, BetterAdventureModCoreAttributeModifierUUIDs.AURA_EFFECT, -0.25F, EntityAttributeModifier.Operation.MULTIPLY_TOTAL)
            ;

    public static void registerEffects() {
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
//        RemoveOnHit.configure(WEAPONS_SHEATHED_EFFECT, true);

        // utility effects
        Registry.register(Registries.STATUS_EFFECT, BetterAdventureModeCore.identifier("adventure_building_effect"), ADVENTURE_BUILDING_EFFECT);
        Registry.register(Registries.STATUS_EFFECT, BetterAdventureModeCore.identifier("civilisation_effect"), CIVILISATION_EFFECT);
        Registry.register(Registries.STATUS_EFFECT, BetterAdventureModeCore.identifier("mana_regeneration_effect"), MANA_REGENERATION_EFFECT);
        Registry.register(Registries.STATUS_EFFECT, BetterAdventureModeCore.identifier("need_empty_offhand_effect"), NEED_EMPTY_OFFHAND_EFFECT);
        Registry.register(Registries.STATUS_EFFECT, BetterAdventureModeCore.identifier("no_attack_items_effect"), NO_ATTACK_ITEMS_EFFECT);
        Registry.register(Registries.STATUS_EFFECT, BetterAdventureModeCore.identifier("overburdened_effect"), OVERBURDENED_EFFECT);
        Registry.register(Registries.STATUS_EFFECT, BetterAdventureModeCore.identifier("weapons_sheathed_effect"), WEAPONS_SHEATHED_EFFECT);
        Registry.register(Registries.STATUS_EFFECT, BetterAdventureModeCore.identifier("two_handed_effect"), TWO_HANDED_EFFECT);

        Registry.register(Registries.STATUS_EFFECT, BetterAdventureModeCore.identifier("staggered"), STAGGERED);

        Registry.register(Registries.STATUS_EFFECT, BetterAdventureModeCore.identifier("burning"), BURNING);
        Registry.register(Registries.STATUS_EFFECT, BetterAdventureModeCore.identifier("chilled"), CHILLED);
        Registry.register(Registries.STATUS_EFFECT, BetterAdventureModeCore.identifier("frozen"), FROZEN);
        Registry.register(Registries.STATUS_EFFECT, BetterAdventureModeCore.identifier("wet"), WET);

        Registry.register(Registries.STATUS_EFFECT, BetterAdventureModeCore.identifier("test_aura_effect"), TEST_AURA_EFFECT);
    }
}
