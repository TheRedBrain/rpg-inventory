package com.github.theredbrain.bamcore.mixin.item;
//
//import com.github.theredbrain.rpgmod.entity.ExtendedEquipmentSlot;
//import com.github.theredbrain.rpgmod.entity.ExtendedEquipmentSlotType;
//import net.minecraft.entity.EquipmentSlot;
//import net.minecraft.item.ArmorItem;
//import org.spongepowered.asm.mixin.Final;
//import org.spongepowered.asm.mixin.Mixin;
//import org.spongepowered.asm.mixin.Mutable;
//import org.spongepowered.asm.mixin.Shadow;
//import org.spongepowered.asm.mixin.gen.Invoker;
//
//import java.util.ArrayList;
//import java.util.Arrays;
//
//@Mixin(ArmorItem.Type.class)
//public class ArmorItemTypeMixin {
//    @Invoker("<init>")
//    private static ArmorItem.Type init(String enumName, int id, EquipmentSlot equipmentSlot, String name) {
//        throw new AssertionError(); // unreachable statement
//    }
//
//    // synthetic field, find the name in bytecode
//    // if you are using McDev plugin add @SuppressWarnings("ShadowTarget")
//    @Shadow
//    @Final
//    @Mutable
//    private static ArmorItem.Type[] field_41940;
//
//    // add new property from the static constructor
//    // static blocks are merged into the target class (at the end)
//    static {
//        ArrayList<ArmorItem.Type> values =  new ArrayList<>(Arrays.asList(field_41940));
//        ArmorItem.Type last = values.get(values.size() - 1);
//
//        // add new value
//        values.add( init("GLOVES", last.ordinal() + 1, ExtendedEquipmentSlot.GLOVES, "gloves") );
//        values.add( init("SHOULDERS", last.ordinal() + 2, ExtendedEquipmentSlot.SHOULDERS, "shoulders") );
//
//        field_41940 = values.toArray(new ArmorItem.Type[0]);
//    }
//}
