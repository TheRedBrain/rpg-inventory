package com.github.theredbrain.rpgmod.mixin.bettercombat;

import net.bettercombat.api.WeaponAttributes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.ArrayList;
import java.util.Arrays;

@Mixin(WeaponAttributes.Condition.class)
public class WeaponAttributesConditionMixin {
    // access the private constructor
    // the first two args (name and id) are added to enum constructors by java
    // if you are using McDev plugin add @SuppressWarnings("InvokerTarget")
    @Invoker("<init>")
    public static WeaponAttributes.Condition init(String enumName, int id) {
        throw new AssertionError(); // unreachable statement
    }

    // synthetic field, find the name in bytecode
    // if you are using McDev plugin add @SuppressWarnings("ShadowTarget")
    @Shadow
    @Final
    @Mutable
    private static WeaponAttributes.Condition[] $VALUES;

    // add new property from the static constructor
    // static blocks are merged into the target class (at the end)
    static {
        ArrayList<WeaponAttributes.Condition> values =  new ArrayList<>(Arrays.asList($VALUES));
        WeaponAttributes.Condition last = values.get(values.size() - 1);

        // add new value
        values.add( init("OFFHAND_ITEM", last.ordinal() + 1) );

        $VALUES = values.toArray(new WeaponAttributes.Condition[0]);
    }
}
