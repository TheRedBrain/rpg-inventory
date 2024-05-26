package com.github.theredbrain.betteradventuremode.registry;

import com.github.theredbrain.betteradventuremode.BetterAdventureMode;
import com.github.theredbrain.betteradventuremode.spell_engine.ExtendedEntityActionsAllowedSemanticType;
import com.github.theredbrain.betteradventuremode.util.AttributeModifierUUIDs;
import com.github.theredbrain.betteradventuremode.effect.*;
import com.github.theredbrain.staminaattributes.StaminaAttributes;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.spell_engine.api.effect.ActionImpairing;
import net.spell_engine.api.effect.EntityActionsAllowed;

public class StatusEffectsRegistry {

    public static final StatusEffect CIVILISATION_EFFECT = new BeneficialStatusEffect()
//            .addAttributeModifier(EntityAttributesRegistry.MANA_REGENERATION, AttributeModifierUUIDs.CIVILISATION_EFFECT, 10.0F, EntityAttributeModifier.Operation.MULTIPLY_TOTAL)
            .addAttributeModifier(StaminaAttributes.STAMINA_REGENERATION, AttributeModifierUUIDs.CIVILISATION_EFFECT, 5.0F, EntityAttributeModifier.Operation.MULTIPLY_TOTAL)
//            .addAttributeModifier(EntityAttributesRegistry.HEALTH_REGENERATION, AttributeModifierUUIDs.CIVILISATION_EFFECT, 5.0F, EntityAttributeModifier.Operation.ADDITION)
            ;
    public static final StatusEffect WILDERNESS_EFFECT = new NeutralStatusEffect();
    public static final StatusEffect NEEDS_TWO_HANDING_EFFECT = new NeutralStatusEffect();
    public static final StatusEffect NO_ATTACK_ITEMS_EFFECT = new NeutralStatusEffect();
    public static final StatusEffect OVERBURDENED_EFFECT = new HarmfulStatusEffect()
            .addAttributeModifier(EntityAttributes.GENERIC_MOVEMENT_SPEED, AttributeModifierUUIDs.OVERBURDENED_EFFECT, -0.25, EntityAttributeModifier.Operation.MULTIPLY_TOTAL);

    public static void registerEffects() {
        // --- Configuration ---
        // utility effects
        ActionImpairing.configure(NO_ATTACK_ITEMS_EFFECT, new EntityActionsAllowed(true, true, new EntityActionsAllowed.PlayersAllowed(false, true, true), new EntityActionsAllowed.MobsAllowed(true), ExtendedEntityActionsAllowedSemanticType.NO_ATTACK_ITEM));
        ActionImpairing.configure(NEEDS_TWO_HANDING_EFFECT, new EntityActionsAllowed(true, true, new EntityActionsAllowed.PlayersAllowed(false, false, false), new EntityActionsAllowed.MobsAllowed(true), ExtendedEntityActionsAllowedSemanticType.NEEDS_TWO_HANDING));
        ActionImpairing.configure(OVERBURDENED_EFFECT, new EntityActionsAllowed(false, true, new EntityActionsAllowed.PlayersAllowed(true, true, true), new EntityActionsAllowed.MobsAllowed(true), ExtendedEntityActionsAllowedSemanticType.OVERBURDENED));

        Registry.register(Registries.STATUS_EFFECT, BetterAdventureMode.identifier("civilisation_effect"), CIVILISATION_EFFECT);
        Registry.register(Registries.STATUS_EFFECT, BetterAdventureMode.identifier("wilderness_effect"), WILDERNESS_EFFECT);
        Registry.register(Registries.STATUS_EFFECT, BetterAdventureMode.identifier("needs_two_handing_effect"), NEEDS_TWO_HANDING_EFFECT);
        Registry.register(Registries.STATUS_EFFECT, BetterAdventureMode.identifier("no_attack_items_effect"), NO_ATTACK_ITEMS_EFFECT);

        Registry.register(Registries.STATUS_EFFECT, BetterAdventureMode.identifier("overburdened_effect"), OVERBURDENED_EFFECT);
    }
}
