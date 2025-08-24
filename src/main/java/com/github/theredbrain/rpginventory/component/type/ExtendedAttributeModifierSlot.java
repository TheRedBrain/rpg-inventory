package com.github.theredbrain.rpginventory.component.type;

import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.entity.EquipmentSlot;

public class ExtendedAttributeModifierSlot {
	public static AttributeModifierSlot BELT = AttributeModifierSlot.valueOf("BELT");
	public static AttributeModifierSlot GLOVES = AttributeModifierSlot.valueOf("GLOVES");
	public static AttributeModifierSlot NECKLACE = AttributeModifierSlot.valueOf("NECKLACE");
	public static AttributeModifierSlot RING_1 = AttributeModifierSlot.valueOf("RING_1");
	public static AttributeModifierSlot RING_2 = AttributeModifierSlot.valueOf("RING_2");
	public static AttributeModifierSlot SHOULDERS = AttributeModifierSlot.valueOf("SHOULDERS");
	public static AttributeModifierSlot SPELL_1 = AttributeModifierSlot.valueOf("SPELL_1");
	public static AttributeModifierSlot SPELL_2 = AttributeModifierSlot.valueOf("SPELL_2");
	public static AttributeModifierSlot SPELL_3 = AttributeModifierSlot.valueOf("SPELL_3");
	public static AttributeModifierSlot SPELL_4 = AttributeModifierSlot.valueOf("SPELL_4");
	public static AttributeModifierSlot SPELL_5 = AttributeModifierSlot.valueOf("SPELL_5");
	public static AttributeModifierSlot SPELL_6 = AttributeModifierSlot.valueOf("SPELL_6");
	public static AttributeModifierSlot SPELL_7 = AttributeModifierSlot.valueOf("SPELL_7");
	public static AttributeModifierSlot SPELL_8 = AttributeModifierSlot.valueOf("SPELL_8");
	public static AttributeModifierSlot RINGS = AttributeModifierSlot.valueOf("RINGS");
	public static AttributeModifierSlot SPELLS = AttributeModifierSlot.valueOf("SPELLS");
	public static AttributeModifierSlot RELIC = AttributeModifierSlot.valueOf("RELIC");

	public static boolean isRingsSlot(EquipmentSlot slot) {
		return slot.name().equals("ring_1") || slot.name().equals("ring_2");
	}

	public static boolean isSpellsSlot(EquipmentSlot slot) {
		return slot.name().equals("spell_1") || slot.name().equals("spell_2") || slot.name().equals("spell_3") || slot.name().equals("spell_4") || slot.name().equals("spell_5") || slot.name().equals("spell_6") || slot.name().equals("spell_7") || slot.name().equals("spell_8");
	}
}
