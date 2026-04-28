package com.github.theredbrain.rpginventory.entity;

import com.github.theredbrain.rpginventory.registry.Tags;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;

public class ExtendedEquipmentSlot {
	public static EquipmentSlot BELT = EquipmentSlot.valueOf("RPG_INVENTORY_BELT");
	public static EquipmentSlot GLOVES = EquipmentSlot.valueOf("RPG_INVENTORY_GLOVES");
	public static EquipmentSlot NECKLACE = EquipmentSlot.valueOf("RPG_INVENTORY_NECKLACE");
	public static EquipmentSlot RING_1 = EquipmentSlot.valueOf("RPG_INVENTORY_RING_1");
	public static EquipmentSlot RING_2 = EquipmentSlot.valueOf("RPG_INVENTORY_RING_2");
	public static EquipmentSlot SHOULDERS = EquipmentSlot.valueOf("RPG_INVENTORY_SHOULDERS");
	public static EquipmentSlot SPELL_1 = EquipmentSlot.valueOf("RPG_INVENTORY_SPELL_1");
	public static EquipmentSlot SPELL_2 = EquipmentSlot.valueOf("RPG_INVENTORY_SPELL_2");
	public static EquipmentSlot SPELL_3 = EquipmentSlot.valueOf("RPG_INVENTORY_SPELL_3");
	public static EquipmentSlot SPELL_4 = EquipmentSlot.valueOf("RPG_INVENTORY_SPELL_4");
	public static EquipmentSlot SPELL_5 = EquipmentSlot.valueOf("RPG_INVENTORY_SPELL_5");
	public static EquipmentSlot SPELL_6 = EquipmentSlot.valueOf("RPG_INVENTORY_SPELL_6");
	public static EquipmentSlot SPELL_7 = EquipmentSlot.valueOf("RPG_INVENTORY_SPELL_7");
	public static EquipmentSlot SPELL_8 = EquipmentSlot.valueOf("RPG_INVENTORY_SPELL_8");
	public static EquipmentSlot RELIC = EquipmentSlot.valueOf("RPG_INVENTORY_RELIC");
	public static EquipmentSlot CLASS_ITEM = EquipmentSlot.valueOf("RPG_INVENTORY_CLASS_ITEM");
	public static EquipmentSlot EMPTY_HAND = EquipmentSlot.valueOf("RPG_INVENTORY_EMPTY_HAND");
	public static EquipmentSlot EMPTY_OFF_HAND = EquipmentSlot.valueOf("RPG_INVENTORY_EMPTY_OFF_HAND");
	public static EquipmentSlot SHEATHED_HAND = EquipmentSlot.valueOf("RPG_INVENTORY_SHEATHED_HAND");
	public static EquipmentSlot SHEATHED_OFF_HAND = EquipmentSlot.valueOf("RPG_INVENTORY_SHEATHED_OFF_HAND");
	public static EquipmentSlot ALTERNATIVE_HAND = EquipmentSlot.valueOf("RPG_INVENTORY_ALTERNATIVE_HAND");
	public static EquipmentSlot ALTERNATIVE_OFF_HAND = EquipmentSlot.valueOf("RPG_INVENTORY_ALTERNATIVE_OFF_HAND");
	public static EquipmentSlot SELECTED_HOTBAR_SLOT = EquipmentSlot.valueOf("RPG_INVENTORY_SELECTED_HOTBAR_SLOT");

	public static boolean isRingsSlot(EquipmentSlot slot) {
		return slot.equals(RING_1) || slot.equals(RING_2);
	}

	public static boolean isSpellsSlot(EquipmentSlot slot) {
		return slot.equals(SPELL_1) || slot.equals(SPELL_2) || slot.equals(SPELL_3) || slot.equals(SPELL_4) || slot.equals(SPELL_5) || slot.equals(SPELL_6) || slot.equals(SPELL_7) || slot.equals(SPELL_8);
	}

	public static boolean isEmptyHandSlot(EquipmentSlot slot) {
		return slot.equals(EMPTY_HAND) || slot.equals(EMPTY_OFF_HAND);
	}

	public static boolean ignoredByExclusiveEquipmentCheck(EquipmentSlot slot) {
		return slot.equals(EMPTY_HAND) || slot.equals(EMPTY_OFF_HAND) || slot.equals(CLASS_ITEM) || slot.equals(ALTERNATIVE_HAND) || slot.equals(ALTERNATIVE_OFF_HAND);
	}

	public static boolean rpginventory$isOfEquipmentTag(ItemStack itemStack, EquipmentSlot slot) {
		if (slot == EquipmentSlot.MAINHAND || slot == SHEATHED_HAND || slot == ALTERNATIVE_HAND) {
			return itemStack.is(Tags.HAND_ITEMS);
		} else if (slot == EquipmentSlot.OFFHAND || slot == SHEATHED_OFF_HAND || slot == ALTERNATIVE_OFF_HAND) {
			return itemStack.is(Tags.OFFHAND_ITEMS);
		} else if (slot == EquipmentSlot.FEET) {
			return itemStack.is(Tags.BOOTS);
		} else if (slot == EquipmentSlot.LEGS) {
			return itemStack.is(Tags.LEGGINGS);
		} else if (slot == EquipmentSlot.CHEST) {
			return itemStack.is(Tags.CHEST_PLATES);
		} else if (slot == EquipmentSlot.HEAD) {
			return itemStack.is(Tags.HELMETS);
		} else if (slot == ExtendedEquipmentSlot.BELT) {
			return itemStack.is(Tags.BELTS);
		} else if (slot == ExtendedEquipmentSlot.GLOVES) {
			return itemStack.is(Tags.GLOVES);
		} else if (slot == ExtendedEquipmentSlot.NECKLACE) {
			return itemStack.is(Tags.NECKLACES);
		} else if (slot == ExtendedEquipmentSlot.RING_1) {
			return itemStack.is(Tags.RINGS_1);
		} else if (slot == ExtendedEquipmentSlot.RING_2) {
			return itemStack.is(Tags.RINGS_2);
		} else if (slot == ExtendedEquipmentSlot.SHOULDERS) {
			return itemStack.is(Tags.SHOULDERS);
		} else if (slot == ExtendedEquipmentSlot.SPELL_1) {
			return itemStack.is(Tags.SPELLS_1);
		} else if (slot == ExtendedEquipmentSlot.SPELL_2) {
			return itemStack.is(Tags.SPELLS_2);
		} else if (slot == ExtendedEquipmentSlot.SPELL_3) {
			return itemStack.is(Tags.SPELLS_3);
		} else if (slot == ExtendedEquipmentSlot.SPELL_4) {
			return itemStack.is(Tags.SPELLS_4);
		} else if (slot == ExtendedEquipmentSlot.SPELL_5) {
			return itemStack.is(Tags.SPELLS_5);
		} else if (slot == ExtendedEquipmentSlot.SPELL_6) {
			return itemStack.is(Tags.SPELLS_6);
		} else if (slot == ExtendedEquipmentSlot.SPELL_7) {
			return itemStack.is(Tags.SPELLS_7);
		} else if (slot == ExtendedEquipmentSlot.SPELL_8) {
			return itemStack.is(Tags.SPELLS_8);
		} else if (slot == ExtendedEquipmentSlot.RELIC) {
			return itemStack.is(Tags.RELICS);
		} else {
			return false;
		}
	}
}
