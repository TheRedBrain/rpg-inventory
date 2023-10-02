package com.github.theredbrain.bamcore.registry;

import com.github.theredbrain.bamcore.BetterAdventureModeCore;
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

    public static final StatusEffect ADVENTURE_BUILDING_EFFECT = new AdventureBuildingStatusEffect();
    public static final StatusEffect CIVILISATION_EFFECT = new CivilisationStatusEffect();
    public static final StatusEffect NEED_EMPTY_OFFHAND_EFFECT = new NeedEmptyOffhandStatusEffect();
    public static final StatusEffect NO_ATTACK_ITEMS_EFFECT = new NoAttackItemsStatusEffect();
    public static final StatusEffect OVERBURDENED_EFFECT = new OverburdenedStatusEffect().addAttributeModifier(EntityAttributes.GENERIC_MOVEMENT_SPEED, AttributeModifierUUIDs.OVERBURDENED_EFFECT, -0.25, EntityAttributeModifier.Operation.MULTIPLY_TOTAL);
    public static final StatusEffect PERMANENT_MOUNT_EFFECT = new PermanentMountStatusEffect();
    public static final StatusEffect PORTAL_RESISTANCE_EFFECT = new PortalResistanceStatusEffect();
    public static final StatusEffect WEAPONS_SHEATHED_EFFECT = new WeaponsSheathedStatusEffect();

    public static void registerEffects() {
        ActionImpairing.configure(NO_ATTACK_ITEMS_EFFECT, new EntityActionsAllowed(true, true, new EntityActionsAllowed.PlayersAllowed(false, true, true), new EntityActionsAllowed.MobsAllowed(true), EntityActionsAllowed.SemanticType.NONE));
        ActionImpairing.configure(NEED_EMPTY_OFFHAND_EFFECT, new EntityActionsAllowed(true, true, new EntityActionsAllowed.PlayersAllowed(false, false, false), new EntityActionsAllowed.MobsAllowed(true), EntityActionsAllowed.SemanticType.NONE));
        ActionImpairing.configure(OVERBURDENED_EFFECT, new EntityActionsAllowed(false, true, new EntityActionsAllowed.PlayersAllowed(true, true, true), new EntityActionsAllowed.MobsAllowed(true), EntityActionsAllowed.SemanticType.NONE));
        Synchronized.configure(WEAPONS_SHEATHED_EFFECT, true);
        RemoveOnHit.configure(WEAPONS_SHEATHED_EFFECT, true);

        // utility effects
        Registry.register(Registries.STATUS_EFFECT, BetterAdventureModeCore.identifier("adventure_building_effect"), ADVENTURE_BUILDING_EFFECT);
        Registry.register(Registries.STATUS_EFFECT, BetterAdventureModeCore.identifier("civilisation_effect"), CIVILISATION_EFFECT);
        Registry.register(Registries.STATUS_EFFECT, BetterAdventureModeCore.identifier("need_empty_offhand_effect"), NEED_EMPTY_OFFHAND_EFFECT);
        Registry.register(Registries.STATUS_EFFECT, BetterAdventureModeCore.identifier("no_attack_items_effect"), NO_ATTACK_ITEMS_EFFECT);
        Registry.register(Registries.STATUS_EFFECT, BetterAdventureModeCore.identifier("overburdened_effect"), OVERBURDENED_EFFECT);
        Registry.register(Registries.STATUS_EFFECT, BetterAdventureModeCore.identifier("permanent_mount_effect"), PERMANENT_MOUNT_EFFECT);
        Registry.register(Registries.STATUS_EFFECT, BetterAdventureModeCore.identifier("portal_resistance_effect"), PORTAL_RESISTANCE_EFFECT);
        Registry.register(Registries.STATUS_EFFECT, BetterAdventureModeCore.identifier("weapons_sheathed_effect"), WEAPONS_SHEATHED_EFFECT);
    }
}
