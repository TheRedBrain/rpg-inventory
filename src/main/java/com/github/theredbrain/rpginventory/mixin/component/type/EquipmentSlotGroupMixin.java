package com.github.theredbrain.rpginventory.mixin.component.type;

import com.github.theredbrain.rpginventory.component.type.ExtendedEquipmentSlotGroup;
import com.github.theredbrain.rpginventory.entity.ExtendedEquipmentSlot;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlotGroup;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.function.Predicate;

@Mixin(EquipmentSlotGroup.class)
enum EquipmentSlotGroupMixin {
	RPG_INVENTORY_BELT(11, "rpg_inventory_belt", ExtendedEquipmentSlot.BELT),
	RPG_INVENTORY_GLOVES(12, "rpg_inventory_gloves", ExtendedEquipmentSlot.GLOVES),
	RPG_INVENTORY_NECKLACE(13, "rpg_inventory_necklace", ExtendedEquipmentSlot.NECKLACE),
	RPG_INVENTORY_RING_1(14, "rpg_inventory_ring_1", ExtendedEquipmentSlot.RING_1),
	RPG_INVENTORY_RING_2(15, "rpg_inventory_ring_2", ExtendedEquipmentSlot.RING_2),
	RPG_INVENTORY_SHOULDERS(16, "rpg_inventory_shoulders", ExtendedEquipmentSlot.SHOULDERS),
	RPG_INVENTORY_SPELL_1(17, "rpg_inventory_spell_1", ExtendedEquipmentSlot.SPELL_1),
	RPG_INVENTORY_SPELL_2(18, "rpg_inventory_spell_2", ExtendedEquipmentSlot.SPELL_2),
	RPG_INVENTORY_SPELL_3(19, "rpg_inventory_spell_3", ExtendedEquipmentSlot.SPELL_3),
	RPG_INVENTORY_SPELL_4(20, "rpg_inventory_spell_4", ExtendedEquipmentSlot.SPELL_4),
	RPG_INVENTORY_SPELL_5(21, "rpg_inventory_spell_5", ExtendedEquipmentSlot.SPELL_5),
	RPG_INVENTORY_SPELL_6(22, "rpg_inventory_spell_6", ExtendedEquipmentSlot.SPELL_6),
	RPG_INVENTORY_SPELL_7(23, "rpg_inventory_spell_7", ExtendedEquipmentSlot.SPELL_7),
	RPG_INVENTORY_SPELL_8(24, "rpg_inventory_spell_8", ExtendedEquipmentSlot.SPELL_8),
	RPG_INVENTORY_RINGS(25, "rpg_inventory_rings", ExtendedEquipmentSlot::isRingsSlot),
	RPG_INVENTORY_SPELLS(26, "rpg_inventory_spells", ExtendedEquipmentSlot::isSpellsSlot),
	RPG_INVENTORY_RELIC(27, "rpg_inventory_relic", ExtendedEquipmentSlot.RELIC),
	RPG_INVENTORY_CLASS_ITEM(28, "rpg_inventory_class_item", ExtendedEquipmentSlot.CLASS_ITEM),
	RPG_INVENTORY_EMPTY_HAND(29, "rpg_inventory_empty_hand", ExtendedEquipmentSlot.EMPTY_HAND),
	RPG_INVENTORY_EMPTY_OFF_HAND(30, "rpg_inventory_empty_off_hand", ExtendedEquipmentSlot.EMPTY_OFF_HAND),
	RPG_INVENTORY_SHEATHED_HAND(31, "rpg_inventory_sheathed_hand", ExtendedEquipmentSlot.SHEATHED_HAND),
	RPG_INVENTORY_SHEATHED_OFF_HAND(32, "rpg_inventory_sheathed_off_hand", ExtendedEquipmentSlot.SHEATHED_OFF_HAND),
	RPG_INVENTORY_ALTERNATIVE_HAND(33, "rpg_inventory_alternative_hand", ExtendedEquipmentSlot.ALTERNATIVE_HAND),
	RPG_INVENTORY_ALTERNATIVE_OFF_HAND(34, "rpg_inventory_alternative_off_hand", ExtendedEquipmentSlot.ALTERNATIVE_OFF_HAND);

	@Shadow
	EquipmentSlotGroupMixin(final int id, final String key, final Predicate<EquipmentSlot> predicate) {
	}

	@Shadow
	EquipmentSlotGroupMixin(final int id, final String key, final EquipmentSlot slot) {
	}

	@WrapMethod(method = "bySlot")
	private static EquipmentSlotGroup rpginventory$wrap_bySlot(EquipmentSlot slot, Operation<EquipmentSlotGroup> original) {
		if (slot == EquipmentSlot.MAINHAND) {
			return EquipmentSlotGroup.MAINHAND;
		} else if (slot == EquipmentSlot.OFFHAND) {
			return EquipmentSlotGroup.OFFHAND;
		} else if (slot == EquipmentSlot.FEET) {
			return EquipmentSlotGroup.FEET;
		} else if (slot == EquipmentSlot.LEGS) {
			return EquipmentSlotGroup.LEGS;
		} else if (slot == EquipmentSlot.CHEST) {
			return EquipmentSlotGroup.CHEST;
		} else if (slot == EquipmentSlot.HEAD) {
			return EquipmentSlotGroup.HEAD;
		} else if (slot == EquipmentSlot.BODY) {
			return EquipmentSlotGroup.BODY;
		} else if (slot == ExtendedEquipmentSlot.BELT) {
			return ExtendedEquipmentSlotGroup.BELT;
		} else if (slot == ExtendedEquipmentSlot.GLOVES) {
			return ExtendedEquipmentSlotGroup.GLOVES;
		} else if (slot == ExtendedEquipmentSlot.NECKLACE) {
			return ExtendedEquipmentSlotGroup.NECKLACE;
		} else if (slot == ExtendedEquipmentSlot.RING_1) {
			return ExtendedEquipmentSlotGroup.RING_1;
		} else if (slot == ExtendedEquipmentSlot.RING_2) {
			return ExtendedEquipmentSlotGroup.RING_2;
		} else if (slot == ExtendedEquipmentSlot.SHOULDERS) {
			return ExtendedEquipmentSlotGroup.SHOULDERS;
		} else if (slot == ExtendedEquipmentSlot.SPELL_1) {
			return ExtendedEquipmentSlotGroup.SPELL_1;
		} else if (slot == ExtendedEquipmentSlot.SPELL_2) {
			return ExtendedEquipmentSlotGroup.SPELL_2;
		} else if (slot == ExtendedEquipmentSlot.SPELL_3) {
			return ExtendedEquipmentSlotGroup.SPELL_3;
		} else if (slot == ExtendedEquipmentSlot.SPELL_4) {
			return ExtendedEquipmentSlotGroup.SPELL_4;
		} else if (slot == ExtendedEquipmentSlot.SPELL_5) {
			return ExtendedEquipmentSlotGroup.SPELL_5;
		} else if (slot == ExtendedEquipmentSlot.SPELL_6) {
			return ExtendedEquipmentSlotGroup.SPELL_6;
		} else if (slot == ExtendedEquipmentSlot.SPELL_7) {
			return ExtendedEquipmentSlotGroup.SPELL_7;
		} else if (slot == ExtendedEquipmentSlot.SPELL_8) {
			return ExtendedEquipmentSlotGroup.SPELL_8;
		} else if (slot == ExtendedEquipmentSlot.RELIC) {
			return ExtendedEquipmentSlotGroup.RELIC;
		} else if (slot == ExtendedEquipmentSlot.CLASS_ITEM) {
			return ExtendedEquipmentSlotGroup.CLASS_ITEM;
		} else if (slot == ExtendedEquipmentSlot.EMPTY_HAND) {
			return ExtendedEquipmentSlotGroup.EMPTY_HAND;
		} else if (slot == ExtendedEquipmentSlot.EMPTY_OFF_HAND) {
			return ExtendedEquipmentSlotGroup.EMPTY_OFF_HAND;
		} else if (slot == ExtendedEquipmentSlot.SHEATHED_HAND) {
			return ExtendedEquipmentSlotGroup.SHEATHED_HAND;
		} else if (slot == ExtendedEquipmentSlot.SHEATHED_OFF_HAND) {
			return ExtendedEquipmentSlotGroup.SHEATHED_OFF_HAND;
		} else if (slot == ExtendedEquipmentSlot.ALTERNATIVE_HAND) {
			return ExtendedEquipmentSlotGroup.ALTERNATIVE_HAND;
		} else if (slot == ExtendedEquipmentSlot.ALTERNATIVE_OFF_HAND) {
			return ExtendedEquipmentSlotGroup.ALTERNATIVE_OFF_HAND;
		} else {
			return original.call(slot);
		}
	}
}
