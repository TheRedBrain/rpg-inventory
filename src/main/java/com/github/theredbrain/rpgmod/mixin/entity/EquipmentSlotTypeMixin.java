package com.github.theredbrain.rpgmod.mixin.entity;

import net.minecraft.entity.EquipmentSlot;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.ArrayList;
import java.util.Arrays;

@Mixin(EquipmentSlot.Type.class)
public class EquipmentSlotTypeMixin {
    // access the private constructor
    // the first two args (name and id) are added to enum constructors by java
    // if you are using McDev plugin add @SuppressWarnings("InvokerTarget")
    @Invoker("<init>")
    public static EquipmentSlot.Type init(String enumName, int id) {
        throw new AssertionError(); // unreachable statement
    }

    // synthetic field, find the name in bytecode
    // if you are using McDev plugin add @SuppressWarnings("ShadowTarget")
    @Shadow
    @Final
    @Mutable
    private static EquipmentSlot.Type[] field_6179;

    // add new property from the static constructor
    // static blocks are merged into the target class (at the end)
    static {
        ArrayList<EquipmentSlot.Type> values =  new ArrayList<>(Arrays.asList(field_6179));
        EquipmentSlot.Type last = values.get(values.size() - 1);

        // add new value
        values.add( init("ACCESSORY", last.ordinal() + 1) );
        values.add( init("PERMANENT", last.ordinal() + 2) );
        values.add( init("MOUNT", last.ordinal() + 3) );

        field_6179 = values.toArray(new EquipmentSlot.Type[0]);
    }
}
