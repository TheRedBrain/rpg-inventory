package com.github.theredbrain.rpginventory.item;

import com.github.theredbrain.rpginventory.RPGInventory;
import com.github.theredbrain.rpginventory.RPGInventoryClient;
import com.github.theredbrain.rpginventory.component.type.AdvancementLockedComponent;
import com.github.theredbrain.rpginventory.registry.Tags;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ResolvableProfile;
import net.minecraft.world.item.equipment.Equippable;

import java.util.ArrayList;
import java.util.List;

public class ItemTooltipHelper {

	public static void addLoadOutItemsTooltipLines(List<Component> lines) {
		int index = RPGInventoryClient.CLIENT_CONFIG.itemTooltipSection.item_tooltip_load_out_item_index.get();
		List<Component> newList = new ArrayList<>();

		newList.add(Component.translatable("item.additional_tooltip.load_out_item.line_1"));
		if (RPGInventoryClient.CLIENT_CONFIG.itemTooltipSection.show_load_out_item_description_tooltip.get()) {
			newList.add(Component.translatable("item.additional_tooltip.load_out_item.line_2"));
		}

		if (index < 0) {
			lines.addAll(newList);
		} else {
			lines.addAll(Math.max(0, Math.min(lines.size() - 1, index)), newList);
		}
	}

	public static void addPlayerBoundItemTooltipLines(List<Component> lines, ItemStack stack) {
		int index = RPGInventoryClient.CLIENT_CONFIG.itemTooltipSection.item_tooltip_bound_to_player_index.get();
		List<Component> newList = new ArrayList<>();

		ResolvableProfile playerBoundComponent = stack.get(RPGInventory.PLAYER_BOUND);
		if (playerBoundComponent != null) {
			String formatting_config_string = RPGInventoryClient.CLIENT_CONFIG.itemTooltipSection.item_tooltip_bound_to_player_name_formatting_string.get();
			StringBuilder formatting_string = new StringBuilder();
			if (!formatting_config_string.isEmpty()) {
				for (int i = 0; i < formatting_config_string.length(); i++) {
					formatting_string.append("§").append(formatting_config_string.charAt(i));
				}
			}
			newList.add(Component.translatable("item.additional_tooltip.player_relation.bound_to", formatting_string + playerBoundComponent.partialProfile().name()));
		}

		if (index < 0) {
			lines.addAll(newList);
		} else {
			lines.addAll(Math.max(0, Math.min(lines.size() - 1, index)), newList);
		}
	}

	public static void addPlayerCraftedItemTooltipLines(List<Component> lines, ItemStack stack) {
		int index = RPGInventoryClient.CLIENT_CONFIG.itemTooltipSection.item_tooltip_crafted_by_player_index.get();
		List<Component> newList = new ArrayList<>();

		ResolvableProfile playerCraftedComponent = stack.get(RPGInventory.PLAYER_CRAFTED);
		if (playerCraftedComponent != null) {
			String formatting_config_string = RPGInventoryClient.CLIENT_CONFIG.itemTooltipSection.item_tooltip_crafted_by_player_name_formatting_string.get();
			StringBuilder formatting_string = new StringBuilder();
			if (!formatting_config_string.isEmpty()) {
				for (int i = 0; i < formatting_config_string.length(); i++) {
					formatting_string.append("§").append(formatting_config_string.charAt(i));
				}
			}
			newList.add(Component.translatable("item.additional_tooltip.player_relation.crafted_by", formatting_string + playerCraftedComponent.partialProfile().name()));
		}

		if (index < 0) {
			lines.addAll(newList);
		} else {
			lines.addAll(Math.max(0, Math.min(lines.size() - 1, index)), newList);
		}
	}

	public static void addAdvancementLockedItemTooltipLines(List<Component> lines, ItemStack stack) {
		int index = RPGInventoryClient.CLIENT_CONFIG.itemTooltipSection.item_tooltip_advancement_locked_index.get();
		List<Component> newList = new ArrayList<>();

		AdvancementLockedComponent advancementLockedComponent = stack.get(RPGInventory.ADVANCEMENT_LOCKED);
		if (advancementLockedComponent != null) {
			Component text = Component.empty();

			if (!advancementLockedComponent.not_unlocked_tooltip_text().isEmpty() && advancementLockedComponent.status() == 0) {
				text = Component.translatable(advancementLockedComponent.not_unlocked_tooltip_text());
			} else if (!advancementLockedComponent.tooltip_text().isEmpty() && advancementLockedComponent.status() == 1) {
				text = Component.translatable(advancementLockedComponent.tooltip_text());
			} else if (!advancementLockedComponent.locked_tooltip_text().isEmpty() && advancementLockedComponent.status() == 2) {
				text = Component.translatable(advancementLockedComponent.locked_tooltip_text());
			}
			if (!text.equals(Component.empty())) {
				newList.add(text);
			}
		}

		if (index < 0) {
			lines.addAll(newList);
		} else {
			lines.addAll(Math.max(0, Math.min(lines.size() - 1, index)), newList);
		}
	}

	public static void addTwoHandedItemTooltipLines(List<Component> lines) {
		int index = RPGInventoryClient.CLIENT_CONFIG.itemTooltipSection.item_tooltip_two_handed_items_index.get();
		if (index < 0) {
			lines.add(Component.translatable("item.additional_tooltip.functionality.two_handed_item"));
		} else {
			lines.add(Math.max(0, Math.min(lines.size() - 1, index)), Component.translatable("item.additional_tooltip.functionality.two_handed_item"));
		}
	}

	public static void addEquipmentSlotTooltipLines(List<Component> lines, ItemStack stack) {
		int index = RPGInventoryClient.CLIENT_CONFIG.itemTooltipSection.item_tooltip_equipment_slots_index.get();
		List<Component> newList = new ArrayList<>();

		Equippable equippable = stack.get(DataComponents.EQUIPPABLE);
		if (stack.is(Tags.HELMETS) || (equippable != null && equippable.slot() == EquipmentSlot.HEAD)) {
			newList.add(Component.translatable("item.additional_tooltip.equipment_slot.helmet"));
		}
		if (stack.is(Tags.NECKLACES)) {
			newList.add(Component.translatable("item.additional_tooltip.equipment_slot.necklace"));
		}
		if (stack.is(Tags.CHEST_PLATES) || (equippable != null && equippable.slot() == EquipmentSlot.CHEST)) {
			newList.add(Component.translatable("item.additional_tooltip.equipment_slot.chest_plate"));
		}
		if (stack.is(Tags.SHOULDERS)) {
			newList.add(Component.translatable("item.additional_tooltip.equipment_slot.shoulders"));
		}
		if (stack.is(Tags.GLOVES)) {
			newList.add(Component.translatable("item.additional_tooltip.equipment_slot.gloves"));
		}
//		boolean is_unique_ring = stack.isIn(Tags.UNIQUE_RINGS);
		if (stack.is(Tags.RINGS_1)) {
//			if (is_unique_ring) {
//				newList.add(Text.translatable("item.additional_tooltip.equipment_slot.ring_unique"));
//			} else {
			newList.add(Component.translatable("item.additional_tooltip.equipment_slot.ring_1"));
//			}
		} else if (stack.is(Tags.RINGS_2)) {
//			if (is_unique_ring) {
//				newList.add(Text.translatable("item.additional_tooltip.equipment_slot.ring_unique"));
//			} else {
			newList.add(Component.translatable("item.additional_tooltip.equipment_slot.ring_2"));
//			}
		}
		if (stack.is(Tags.BELTS)) {
			newList.add(Component.translatable("item.additional_tooltip.equipment_slot.belt"));
		}
		if (!stack.is(Tags.TWO_HANDED_ITEMS)) {
			if (stack.is(Tags.HAND_ITEMS) && stack.is(Tags.OFFHAND_ITEMS)) {
				newList.add(Component.translatable("item.additional_tooltip.equipment_slot.both_hands"));
			} else if (stack.is(Tags.HAND_ITEMS)) {
				newList.add(Component.translatable("item.additional_tooltip.equipment_slot.hand"));
			} else if (stack.is(Tags.OFFHAND_ITEMS)) {
				newList.add(Component.translatable("item.additional_tooltip.equipment_slot.offhand"));
			}
		}
		if (stack.is(Tags.SPELLS_1)) {
			newList.add(Component.translatable("item.additional_tooltip.equipment_slot.spell_1"));
		} else if (stack.is(Tags.SPELLS_2)) {
			newList.add(Component.translatable("item.additional_tooltip.equipment_slot.spell_2"));
		} else if (stack.is(Tags.SPELLS_3)) {
			newList.add(Component.translatable("item.additional_tooltip.equipment_slot.spell_3"));
		} else if (stack.is(Tags.SPELLS_4)) {
			newList.add(Component.translatable("item.additional_tooltip.equipment_slot.spell_4"));
		} else if (stack.is(Tags.SPELLS_5)) {
			newList.add(Component.translatable("item.additional_tooltip.equipment_slot.spell_5"));
		} else if (stack.is(Tags.SPELLS_6)) {
			newList.add(Component.translatable("item.additional_tooltip.equipment_slot.spell_6"));
		} else if (stack.is(Tags.SPELLS_7)) {
			newList.add(Component.translatable("item.additional_tooltip.equipment_slot.spell_7"));
		} else if (stack.is(Tags.SPELLS_8)) {
			newList.add(Component.translatable("item.additional_tooltip.equipment_slot.spell_8"));
		}
		if (stack.is(Tags.LEGGINGS) || (equippable != null && equippable.slot() == EquipmentSlot.LEGS)) {
			newList.add(Component.translatable("item.additional_tooltip.equipment_slot.leggings"));
		}
		if (stack.is(Tags.BOOTS) || (equippable != null && equippable.slot() == EquipmentSlot.FEET)) {
			newList.add(Component.translatable("item.additional_tooltip.equipment_slot.boots"));
		}

		if (index < 0) {
			lines.addAll(newList);
		} else {
			lines.addAll(Math.max(0, Math.min(lines.size() - 1, index)), newList);
		}
	}
}