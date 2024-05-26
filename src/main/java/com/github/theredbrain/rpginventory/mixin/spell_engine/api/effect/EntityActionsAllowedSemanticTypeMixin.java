package com.github.theredbrain.rpginventory.mixin.spell_engine.api.effect;

import net.spell_engine.api.effect.EntityActionsAllowed;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.ArrayList;
import java.util.Arrays;

@Mixin(EntityActionsAllowed.SemanticType.class)
public class EntityActionsAllowedSemanticTypeMixin {
    @Invoker("<init>")
    private static EntityActionsAllowed.SemanticType init(String enumName, int id) {
        throw new AssertionError(); // unreachable statement
    }

    // synthetic field, find the name in bytecode
    // if you are using McDev plugin add @SuppressWarnings("ShadowTarget")
    @Shadow
    @Final
    @Mutable
    private static EntityActionsAllowed.SemanticType[] $VALUES;

    // add new property from the static constructor
    // static blocks are merged into the target class (at the end)
    static {
        ArrayList<EntityActionsAllowed.SemanticType> values = new ArrayList<>(Arrays.asList($VALUES));
        EntityActionsAllowed.SemanticType last = values.get(values.size() - 1);

        // add new value
        values.add(init("NO_ATTACK_ITEM", last.ordinal() + 1));
        values.add(init("NEEDS_TWO_HANDING", last.ordinal() + 2));
        values.add(init("OVERBURDENED", last.ordinal() + 3));
//        values.add(init("STAGGERED", last.ordinal() + 4));

        $VALUES = values.toArray(new EntityActionsAllowed.SemanticType[0]);
    }
}