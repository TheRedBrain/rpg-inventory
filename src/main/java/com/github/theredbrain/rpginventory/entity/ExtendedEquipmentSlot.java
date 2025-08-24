package com.github.theredbrain.rpginventory.entity;

import com.github.theredbrain.rpginventory.registry.Tags;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;

public class ExtendedEquipmentSlot {
    public static EquipmentSlot BELT = EquipmentSlot.valueOf("BELT");
    public static EquipmentSlot GLOVES = EquipmentSlot.valueOf("GLOVES");
    public static EquipmentSlot NECKLACE = EquipmentSlot.valueOf("NECKLACE");
    public static EquipmentSlot RING_1 = EquipmentSlot.valueOf("RING_1");
    public static EquipmentSlot RING_2 = EquipmentSlot.valueOf("RING_2");
    public static EquipmentSlot SHOULDERS = EquipmentSlot.valueOf("SHOULDERS");
    public static EquipmentSlot SPELL_1 = EquipmentSlot.valueOf("SPELL_1");
    public static EquipmentSlot SPELL_2 = EquipmentSlot.valueOf("SPELL_2");
    public static EquipmentSlot SPELL_3 = EquipmentSlot.valueOf("SPELL_3");
    public static EquipmentSlot SPELL_4 = EquipmentSlot.valueOf("SPELL_4");
    public static EquipmentSlot SPELL_5 = EquipmentSlot.valueOf("SPELL_5");
    public static EquipmentSlot SPELL_6 = EquipmentSlot.valueOf("SPELL_6");
    public static EquipmentSlot SPELL_7 = EquipmentSlot.valueOf("SPELL_7");
    public static EquipmentSlot SPELL_8 = EquipmentSlot.valueOf("SPELL_8");
    public static EquipmentSlot RELIC = EquipmentSlot.valueOf("RELIC");

    public static boolean rpginventory$isOfEquipmentTag(ItemStack itemStack, EquipmentSlot slot) {
        if (slot == EquipmentSlot.MAINHAND) {
            return itemStack.isIn(Tags.HAND_ITEMS);
        } else if (slot == EquipmentSlot.OFFHAND) {
            return itemStack.isIn(Tags.OFFHAND_ITEMS);
        } else if (slot == EquipmentSlot.FEET) {
            return itemStack.isIn(Tags.BOOTS);
        } else if (slot == EquipmentSlot.LEGS) {
            return itemStack.isIn(Tags.LEGGINGS);
        } else if (slot == EquipmentSlot.CHEST) {
            return itemStack.isIn(Tags.CHEST_PLATES);
        } else if (slot == EquipmentSlot.HEAD) {
            return itemStack.isIn(Tags.HELMETS);
        } else if (slot == ExtendedEquipmentSlot.BELT) {
            return itemStack.isIn(Tags.BELTS);
        } else if (slot == ExtendedEquipmentSlot.GLOVES) {
            return itemStack.isIn(Tags.GLOVES);
        } else if (slot == ExtendedEquipmentSlot.NECKLACE) {
            return itemStack.isIn(Tags.NECKLACES);
        } else if (slot == ExtendedEquipmentSlot.RING_1) {
            return itemStack.isIn(Tags.RINGS_1);
        } else if (slot == ExtendedEquipmentSlot.RING_2) {
            return itemStack.isIn(Tags.RINGS_2);
        } else if (slot == ExtendedEquipmentSlot.SHOULDERS) {
            return itemStack.isIn(Tags.SHOULDERS);
        } else if (slot == ExtendedEquipmentSlot.SPELL_1) {
            return itemStack.isIn(Tags.SPELLS_1);
        } else if (slot == ExtendedEquipmentSlot.SPELL_2) {
            return itemStack.isIn(Tags.SPELLS_2);
        } else if (slot == ExtendedEquipmentSlot.SPELL_3) {
            return itemStack.isIn(Tags.SPELLS_3);
        } else if (slot == ExtendedEquipmentSlot.SPELL_4) {
            return itemStack.isIn(Tags.SPELLS_4);
        } else if (slot == ExtendedEquipmentSlot.SPELL_5) {
            return itemStack.isIn(Tags.SPELLS_5);
        } else if (slot == ExtendedEquipmentSlot.SPELL_6) {
            return itemStack.isIn(Tags.SPELLS_6);
        } else if (slot == ExtendedEquipmentSlot.SPELL_7) {
            return itemStack.isIn(Tags.SPELLS_7);
        } else if (slot == ExtendedEquipmentSlot.SPELL_8) {
            return itemStack.isIn(Tags.SPELLS_8);
        } else if (slot == ExtendedEquipmentSlot.RELIC) {
            return itemStack.isIn(Tags.RELICS);
        } else {
            return false;
        }
    }
}
