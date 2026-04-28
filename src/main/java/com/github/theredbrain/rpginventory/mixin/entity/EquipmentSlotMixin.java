package com.github.theredbrain.rpginventory.mixin.entity;

import com.github.theredbrain.rpginventory.entity.ExtendedEquipmentSlotType;
import net.minecraft.world.entity.EquipmentSlot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(EquipmentSlot.class)
enum EquipmentSlotMixin {

	RPG_INVENTORY_BELT(ExtendedEquipmentSlotType.RPG_INVENTORY_SLOT_TYPE, 0, 1, 8, "rpg_inventory_belt"),
	RPG_INVENTORY_GLOVES(ExtendedEquipmentSlotType.RPG_INVENTORY_SLOT_TYPE, 1, 1, 9, "rpg_inventory_gloves"),
	RPG_INVENTORY_NECKLACE(ExtendedEquipmentSlotType.RPG_INVENTORY_SLOT_TYPE, 2, 1, 10, "rpg_inventory_necklace"),
	RPG_INVENTORY_RING_1(ExtendedEquipmentSlotType.RPG_INVENTORY_SLOT_TYPE, 3, 1, 11, "rpg_inventory_ring_1"),
	RPG_INVENTORY_RING_2(ExtendedEquipmentSlotType.RPG_INVENTORY_SLOT_TYPE, 4, 1, 12, "rpg_inventory_ring_2"),
	RPG_INVENTORY_SHOULDERS(ExtendedEquipmentSlotType.RPG_INVENTORY_SLOT_TYPE, 5, 1, 13, "rpg_inventory_shoulders"),
	RPG_INVENTORY_SPELL_1(ExtendedEquipmentSlotType.RPG_INVENTORY_SLOT_TYPE, 6, 1, 14, "rpg_inventory_spell_1"),
	RPG_INVENTORY_SPELL_2(ExtendedEquipmentSlotType.RPG_INVENTORY_SLOT_TYPE, 7, 1, 15, "rpg_inventory_spell_2"),
	RPG_INVENTORY_SPELL_3(ExtendedEquipmentSlotType.RPG_INVENTORY_SLOT_TYPE, 8, 1, 16, "rpg_inventory_spell_3"),
	RPG_INVENTORY_SPELL_4(ExtendedEquipmentSlotType.RPG_INVENTORY_SLOT_TYPE, 9, 1, 17, "rpg_inventory_spell_4"),
	RPG_INVENTORY_SPELL_5(ExtendedEquipmentSlotType.RPG_INVENTORY_SLOT_TYPE, 10, 1, 18, "rpg_inventory_spell_5"),
	RPG_INVENTORY_SPELL_6(ExtendedEquipmentSlotType.RPG_INVENTORY_SLOT_TYPE, 11, 1, 19, "rpg_inventory_spell_6"),
	RPG_INVENTORY_SPELL_7(ExtendedEquipmentSlotType.RPG_INVENTORY_SLOT_TYPE, 12, 1, 20, "rpg_inventory_spell_7"),
	RPG_INVENTORY_SPELL_8(ExtendedEquipmentSlotType.RPG_INVENTORY_SLOT_TYPE, 13, 1, 21, "rpg_inventory_spell_8"),
	RPG_INVENTORY_RELIC(ExtendedEquipmentSlotType.RPG_INVENTORY_SLOT_TYPE, 14, 1, 22, "rpg_inventory_relic"),
	RPG_INVENTORY_CLASS_ITEM(ExtendedEquipmentSlotType.RPG_INVENTORY_SLOT_TYPE, 15, 1, 23, "rpg_inventory_class_item"),
	RPG_INVENTORY_EMPTY_HAND(ExtendedEquipmentSlotType.RPG_INVENTORY_SLOT_TYPE, 16, 1, 24, "rpg_inventory_empty_hand"),
	RPG_INVENTORY_EMPTY_OFF_HAND(ExtendedEquipmentSlotType.RPG_INVENTORY_SLOT_TYPE, 17, 1, 25, "rpg_inventory_empty_off_hand"),
	RPG_INVENTORY_SHEATHED_HAND(ExtendedEquipmentSlotType.RPG_INVENTORY_SLOT_TYPE, 18, 0, 26, "rpg_inventory_sheathed_hand"),
	RPG_INVENTORY_SHEATHED_OFF_HAND(ExtendedEquipmentSlotType.RPG_INVENTORY_SLOT_TYPE, 19, 0, 27, "rpg_inventory_sheathed_off_hand"),
	RPG_INVENTORY_ALTERNATIVE_HAND(ExtendedEquipmentSlotType.RPG_INVENTORY_SLOT_TYPE, 20, 0, 28, "rpg_inventory_alternative_hand"),
	RPG_INVENTORY_ALTERNATIVE_OFF_HAND(ExtendedEquipmentSlotType.RPG_INVENTORY_SLOT_TYPE, 21, 0, 29, "rpg_inventory_alternative_off_hand"),
	RPG_INVENTORY_SELECTED_HOTBAR_SLOT(ExtendedEquipmentSlotType.RPG_INVENTORY_SLOT_TYPE, 22, 0, 30, "rpg_inventory_selected_hotbar_slot");

	@Shadow
	EquipmentSlotMixin(EquipmentSlot.Type type, int index, int countLimit, int id, String name) {
	}
}
