package com.github.theredbrain.rpginventory.item;

import com.github.theredbrain.rpginventory.RPGInventory;
import com.github.theredbrain.rpginventory.RPGInventoryClient;
import com.github.theredbrain.rpginventory.component.type.AdvancementLockedComponent;
import com.github.theredbrain.rpginventory.registry.Tags;
import net.minecraft.component.type.ProfileComponent;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.Equipment;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;

public class ItemTooltipHelper {

	public static void addLoadOutItemsTooltipLines(List<Text> lines) {
		int index = RPGInventoryClient.CLIENT_CONFIG.itemTooltipSection.item_tooltip_load_out_item_index.get();
		List<Text> newList = new ArrayList<>();
		
		newList.add(Text.translatable("item.additional_tooltip.load_out_item.line_1"));
		if (RPGInventoryClient.CLIENT_CONFIG.itemTooltipSection.show_load_out_item_description_tooltip.get()) {
			newList.add(Text.translatable("item.additional_tooltip.load_out_item.line_2"));
		}
		
		if (index < 0) {
			lines.addAll(newList);
		} else {
			lines.addAll(Math.max(0, Math.min(lines.size() - 1, index)), newList);
		}
	}

	public static void addPlayerBoundItemTooltipLines(List<Text> lines, ItemStack stack) {
		int index = RPGInventoryClient.CLIENT_CONFIG.itemTooltipSection.item_tooltip_bound_to_player_index.get();
		List<Text> newList = new ArrayList<>();

		ProfileComponent playerBoundComponent = stack.get(RPGInventory.PLAYER_BOUND);
		if (playerBoundComponent != null) {
			String formatting_config_string = RPGInventoryClient.CLIENT_CONFIG.itemTooltipSection.item_tooltip_bound_to_player_name_formatting_string.get();
			StringBuilder formatting_string = new StringBuilder();
			if (!formatting_config_string.isEmpty()) {
				for (int i = 0; i < formatting_config_string.length(); i++) {
					formatting_string.append("§").append(formatting_config_string.charAt(i));
				}
			}
			newList.add(Text.translatable("item.additional_tooltip.player_relation.bound_to", formatting_string + playerBoundComponent.gameProfile().getName()));
		}
		
		if (index < 0) {
			lines.addAll(newList);
		} else {
			lines.addAll(Math.max(0, Math.min(lines.size() - 1, index)), newList);
		}
	}

	public static void addPlayerCraftedItemTooltipLines(List<Text> lines, ItemStack stack) {
		int index = RPGInventoryClient.CLIENT_CONFIG.itemTooltipSection.item_tooltip_bound_to_player_index.get();
		List<Text> newList = new ArrayList<>();

		ProfileComponent playerCraftedComponent = stack.get(RPGInventory.PLAYER_CRAFTED);
		if (playerCraftedComponent != null) {
			String formatting_config_string = RPGInventoryClient.CLIENT_CONFIG.itemTooltipSection.item_tooltip_crafted_by_player_name_formatting_string.get();
			StringBuilder formatting_string = new StringBuilder();
			if (!formatting_config_string.isEmpty()) {
				for (int i = 0; i < formatting_config_string.length(); i++) {
					formatting_string.append("§").append(formatting_config_string.charAt(i));
				}
			}
			newList.add(Text.translatable("item.additional_tooltip.player_relation.crafted_by", formatting_string + playerCraftedComponent.gameProfile().getName()));
		}
		
		if (index < 0) {
			lines.addAll(newList);
		} else {
			lines.addAll(Math.max(0, Math.min(lines.size() - 1, index)), newList);
		}
	}

	public static void addAdvancementLockedItemTooltipLines(List<Text> lines, ItemStack stack) {
		int index = RPGInventoryClient.CLIENT_CONFIG.itemTooltipSection.item_tooltip_bound_to_player_index.get();
		List<Text> newList = new ArrayList<>();

		AdvancementLockedComponent advancementLockedComponent = stack.get(RPGInventory.ADVANCEMENT_LOCKED);
		if (advancementLockedComponent != null) {
			Text text = Text.empty();

			if (!advancementLockedComponent.not_unlocked_tooltip_text().isEmpty() && advancementLockedComponent.status() == 0) {
				text = Text.translatable(advancementLockedComponent.not_unlocked_tooltip_text());
			} else if (!advancementLockedComponent.tooltip_text().isEmpty() && advancementLockedComponent.status() == 1) {
				text = Text.translatable(advancementLockedComponent.tooltip_text());
			} else if (!advancementLockedComponent.locked_tooltip_text().isEmpty() && advancementLockedComponent.status() == 2) {
				text = Text.translatable(advancementLockedComponent.locked_tooltip_text());
			}
			if (!text.equals(Text.empty())) {
				newList.add(text);
			}
		}
		
		if (index < 0) {
			lines.addAll(newList);
		} else {
			lines.addAll(Math.max(0, Math.min(lines.size() - 1, index)), newList);
		}
	}

	public static void addTwoHandedItemTooltipLines(List<Text> lines) {
		int index = RPGInventoryClient.CLIENT_CONFIG.itemTooltipSection.item_tooltip_two_handed_items_index.get();
		if (index < 0) {
			lines.add(Text.translatable("item.additional_tooltip.functionality.two_handed_item"));
		} else {
			lines.add(Math.max(0, Math.min(lines.size() - 1, index)), Text.translatable("item.additional_tooltip.functionality.two_handed_item"));
		}
	}

	public static void addEquipmentSlotTooltipLines(List<Text> lines, ItemStack stack) {
		int index = RPGInventoryClient.CLIENT_CONFIG.itemTooltipSection.item_tooltip_equipment_slots_index.get();
		List<Text> newList = new ArrayList<>();

		Equipment equipment = Equipment.fromStack(stack);
		if (stack.isIn(Tags.HELMETS) || (equipment != null && equipment.getSlotType() == EquipmentSlot.HEAD)) {
			newList.add(Text.translatable("item.additional_tooltip.equipment_slot.helmet"));
		}
		if (stack.isIn(Tags.NECKLACES)) {
			newList.add(Text.translatable("item.additional_tooltip.equipment_slot.necklace"));
		}
		if (stack.isIn(Tags.CHEST_PLATES) || (equipment != null && equipment.getSlotType() == EquipmentSlot.CHEST)) {
			newList.add(Text.translatable("item.additional_tooltip.equipment_slot.chest_plate"));
		}
		if (stack.isIn(Tags.SHOULDERS)) {
			newList.add(Text.translatable("item.additional_tooltip.equipment_slot.shoulders"));
		}
		if (stack.isIn(Tags.GLOVES)) {
			newList.add(Text.translatable("item.additional_tooltip.equipment_slot.gloves"));
		}
		boolean is_unique_ring = stack.isIn(Tags.UNIQUE_RINGS);
		if (stack.isIn(Tags.RINGS_1)) {
			if (is_unique_ring) {
				newList.add(Text.translatable("item.additional_tooltip.equipment_slot.ring_unique"));
			} else {
				newList.add(Text.translatable("item.additional_tooltip.equipment_slot.ring_1"));
			}
		} else if (stack.isIn(Tags.RINGS_2)) {
			if (is_unique_ring) {
				newList.add(Text.translatable("item.additional_tooltip.equipment_slot.ring_unique"));
			} else {
				newList.add(Text.translatable("item.additional_tooltip.equipment_slot.ring_2"));
			}
		}
		if (stack.isIn(Tags.BELTS)) {
			newList.add(Text.translatable("item.additional_tooltip.equipment_slot.belt"));
		}
		if (!stack.isIn(Tags.TWO_HANDED_ITEMS)) {
			if (stack.isIn(Tags.HAND_ITEMS) && stack.isIn(Tags.OFFHAND_ITEMS)) {
				newList.add(Text.translatable("item.additional_tooltip.equipment_slot.both_hands"));
			} else if (stack.isIn(Tags.HAND_ITEMS)) {
				newList.add(Text.translatable("item.additional_tooltip.equipment_slot.hand"));
			} else if (stack.isIn(Tags.OFFHAND_ITEMS)) {
				newList.add(Text.translatable("item.additional_tooltip.equipment_slot.offhand"));
			}
		}
		if (stack.isIn(Tags.SPELLS_1)) {
			newList.add(Text.translatable("item.additional_tooltip.equipment_slot.spell_1"));
		} else if (stack.isIn(Tags.SPELLS_2)) {
			newList.add(Text.translatable("item.additional_tooltip.equipment_slot.spell_2"));
		} else if (stack.isIn(Tags.SPELLS_3)) {
			newList.add(Text.translatable("item.additional_tooltip.equipment_slot.spell_3"));
		} else if (stack.isIn(Tags.SPELLS_4)) {
			newList.add(Text.translatable("item.additional_tooltip.equipment_slot.spell_4"));
		} else if (stack.isIn(Tags.SPELLS_5)) {
			newList.add(Text.translatable("item.additional_tooltip.equipment_slot.spell_5"));
		} else if (stack.isIn(Tags.SPELLS_6)) {
			newList.add(Text.translatable("item.additional_tooltip.equipment_slot.spell_6"));
		} else if (stack.isIn(Tags.SPELLS_7)) {
			newList.add(Text.translatable("item.additional_tooltip.equipment_slot.spell_7"));
		} else if (stack.isIn(Tags.SPELLS_8)) {
			newList.add(Text.translatable("item.additional_tooltip.equipment_slot.spell_8"));
		}
		if (stack.isIn(Tags.LEGGINGS) || (equipment != null && equipment.getSlotType() == EquipmentSlot.LEGS)) {
			newList.add(Text.translatable("item.additional_tooltip.equipment_slot.leggings"));
		}
		if (stack.isIn(Tags.BOOTS) || (equipment != null && equipment.getSlotType() == EquipmentSlot.FEET)) {
			newList.add(Text.translatable("item.additional_tooltip.equipment_slot.boots"));
		}
		
		if (index < 0) {
			lines.addAll(newList);
		} else {
			lines.addAll(Math.max(0, Math.min(lines.size() - 1, index)), newList);
		}
	}
}