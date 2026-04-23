package com.github.theredbrain.rpginventory.component.type;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlotGroup;

public class ExtendedAttributeModifierSlot {
	public static EquipmentSlotGroup BELT = EquipmentSlotGroup.valueOf("BELT");
	public static EquipmentSlotGroup GLOVES = EquipmentSlotGroup.valueOf("GLOVES");
	public static EquipmentSlotGroup NECKLACE = EquipmentSlotGroup.valueOf("NECKLACE");
	public static EquipmentSlotGroup RING_1 = EquipmentSlotGroup.valueOf("RING_1");
	public static EquipmentSlotGroup RING_2 = EquipmentSlotGroup.valueOf("RING_2");
	public static EquipmentSlotGroup SHOULDERS = EquipmentSlotGroup.valueOf("SHOULDERS");
	public static EquipmentSlotGroup SPELL_1 = EquipmentSlotGroup.valueOf("SPELL_1");
	public static EquipmentSlotGroup SPELL_2 = EquipmentSlotGroup.valueOf("SPELL_2");
	public static EquipmentSlotGroup SPELL_3 = EquipmentSlotGroup.valueOf("SPELL_3");
	public static EquipmentSlotGroup SPELL_4 = EquipmentSlotGroup.valueOf("SPELL_4");
	public static EquipmentSlotGroup SPELL_5 = EquipmentSlotGroup.valueOf("SPELL_5");
	public static EquipmentSlotGroup SPELL_6 = EquipmentSlotGroup.valueOf("SPELL_6");
	public static EquipmentSlotGroup SPELL_7 = EquipmentSlotGroup.valueOf("SPELL_7");
	public static EquipmentSlotGroup SPELL_8 = EquipmentSlotGroup.valueOf("SPELL_8");
	public static EquipmentSlotGroup RINGS = EquipmentSlotGroup.valueOf("RINGS");
	public static EquipmentSlotGroup SPELLS = EquipmentSlotGroup.valueOf("SPELLS");
	public static EquipmentSlotGroup RELIC = EquipmentSlotGroup.valueOf("RELIC");
	public static EquipmentSlotGroup CLASS_ITEM = EquipmentSlotGroup.valueOf("CLASS_ITEM");

	public static boolean isRingsSlot(EquipmentSlot slot) {
		return slot.name().equals("ring_1") || slot.name().equals("ring_2");
	}

	public static boolean isSpellsSlot(EquipmentSlot slot) {
		return slot.name().equals("spell_1") || slot.name().equals("spell_2") || slot.name().equals("spell_3") || slot.name().equals("spell_4") || slot.name().equals("spell_5") || slot.name().equals("spell_6") || slot.name().equals("spell_7") || slot.name().equals("spell_8");
	}
}
