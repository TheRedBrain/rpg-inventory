package com.github.theredbrain.betteradventuremode.spell_engine;

import net.spell_engine.api.effect.EntityActionsAllowed;

public class ExtendedEntityActionsAllowedSemanticType {
    public static EntityActionsAllowed.SemanticType NO_ATTACK_ITEM = EntityActionsAllowed.SemanticType.valueOf("NO_ATTACK_ITEM");
    public static EntityActionsAllowed.SemanticType NEEDS_TWO_HANDING = EntityActionsAllowed.SemanticType.valueOf("NEEDS_TWO_HANDING");
//    public static EntityActionsAllowed.SemanticType STAGGERED = EntityActionsAllowed.SemanticType.valueOf("STAGGERED");
    public static EntityActionsAllowed.SemanticType OVERBURDENED = EntityActionsAllowed.SemanticType.valueOf("OVERBURDENED");
}