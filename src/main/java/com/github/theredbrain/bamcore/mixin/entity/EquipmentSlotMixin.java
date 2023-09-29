package com.github.theredbrain.bamcore.mixin.entity;

import com.github.theredbrain.bamcore.entity.ExtendedEquipmentSlotType;
import net.minecraft.entity.EquipmentSlot;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.ArrayList;
import java.util.Arrays;

@Mixin(EquipmentSlot.class)
public class EquipmentSlotMixin {
    @Invoker("<init>")
    private static EquipmentSlot init(String enumName, int id, EquipmentSlot.Type type, int entityId, int armorStandId, String name) {
        throw new AssertionError(); // unreachable statement
    }

    // synthetic field, find the name in bytecode
    // if you are using McDev plugin add @SuppressWarnings("ShadowTarget")
    @Shadow
    @Final
    @Mutable
    private static EquipmentSlot[] field_6176;

    // add new property from the static constructor
    // static blocks are merged into the target class (at the end)
    static {
        ArrayList<EquipmentSlot> values =  new ArrayList<>(Arrays.asList(field_6176));
        EquipmentSlot last = values.get(values.size() - 1);

        // add new value
//        values.add( init("GLOVES", last.ordinal() + 1, EquipmentSlot.Type.ARMOR, 4, 6, "gloves") );
//        values.add( init("SHOULDERS", last.ordinal() + 2, EquipmentSlot.Type.ARMOR, 5, 7, "shoulders") );
        values.add( init("ALT_MAINHAND", last.ordinal() + 1, EquipmentSlot.Type.HAND, 2, 6, "alt_mainhand") );
        values.add( init("ALT_OFFHAND", last.ordinal() + 2, EquipmentSlot.Type.HAND, 3, 7, "alt_offhand") );
        values.add( init("EMPTY_MAINHAND", last.ordinal() + 3, ExtendedEquipmentSlotType.PERMANENT, 0, 8, "empty_offhand") );
        values.add( init("EMPTY_OFFHAND", last.ordinal() + 4, ExtendedEquipmentSlotType.PERMANENT, 1, 9, "empty_offhand") );
//        values.add( init("PLAYER_SKIN_ARMOR", last.ordinal() + 11, EquipmentSlot.Type.ARMOR, 2, 16, "player_skin_armor") );
//        values.add( init("MOUNT", last.ordinal() + 7, EquipmentSlot.Type.HAND, 4, 12, "mount") );
//        values.add( init("PET", last.ordinal() + 8, EquipmentSlot.Type.HAND, 5, 13, "pet") );

        field_6176 = values.toArray(new EquipmentSlot[0]);
    }
}
