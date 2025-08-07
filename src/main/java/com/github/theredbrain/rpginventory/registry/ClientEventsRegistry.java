package com.github.theredbrain.rpginventory.registry;

import com.github.theredbrain.rpginventory.RPGInventory;
import com.github.theredbrain.rpginventory.RPGInventoryClient;
import com.github.theredbrain.rpginventory.component.type.AdvancementLockedComponent;
import com.github.theredbrain.rpginventory.config.ClientConfig;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.minecraft.component.type.ProfileComponent;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.Equipment;
import net.minecraft.text.Text;

public class ClientEventsRegistry {

	public static void initializeClientEvents() {
		ItemTooltipCallback.EVENT.register((stack, context, type, lines) -> {
			ClientConfig clientConfig = RPGInventoryClient.CLIENT_CONFIG;
			boolean isLoadOutItem = stack.contains(RPGInventory.LOAD_OUT_ITEM);
			if (isLoadOutItem && clientConfig.itemTooltipSection.show_load_out_item_tooltip.get()) {
				lines.add(Text.translatable("item.additional_tooltip.load_out_item.line_1"));
				if (clientConfig.itemTooltipSection.show_load_out_item_description_tooltip.get()) {
					lines.add(Text.translatable("item.additional_tooltip.load_out_item.line_2"));
				}
			}
			ProfileComponent playerBoundComponent = stack.get(RPGInventory.PLAYER_BOUND);
			if (!isLoadOutItem && playerBoundComponent != null && clientConfig.itemTooltipSection.show_item_tooltip_bound_to_player_name.get()) {
				String formatting_config_string = clientConfig.itemTooltipSection.item_tooltip_bound_to_player_name_formatting_string.get();
				StringBuilder formatting_string = new StringBuilder();
				if (!formatting_config_string.isEmpty()) {
					for (int i = 0; i < formatting_config_string.length(); i++) {
						formatting_string.append("§").append(formatting_config_string.charAt(i));
					}
				}
				lines.add(Text.translatable("item.additional_tooltip.player_relation.bound_to", formatting_string + playerBoundComponent.gameProfile().getName()));
			}
			ProfileComponent playerCraftedComponent = stack.get(RPGInventory.PLAYER_CRAFTED);
			if (!isLoadOutItem && playerCraftedComponent != null && clientConfig.itemTooltipSection.show_item_tooltip_crafted_by_player_name.get()) {
				String formatting_config_string = clientConfig.itemTooltipSection.item_tooltip_crafted_by_player_name_formatting_string.get();
				StringBuilder formatting_string = new StringBuilder();
				if (!formatting_config_string.isEmpty()) {
					for (int i = 0; i < formatting_config_string.length(); i++) {
						formatting_string.append("§").append(formatting_config_string.charAt(i));
					}
				}
				lines.add(Text.translatable("item.additional_tooltip.player_relation.crafted_by", formatting_string + playerCraftedComponent.gameProfile().getName()));
			}
			AdvancementLockedComponent advancementLockedComponent = stack.get(RPGInventory.ADVANCEMENT_LOCKED);
			if (advancementLockedComponent != null && clientConfig.itemTooltipSection.show_item_tooltip_advancement_locked.get()) {
				Text text = Text.empty();

				if (!advancementLockedComponent.tooltip_text().isEmpty() && !(advancementLockedComponent.lock_advancement().isEmpty() && advancementLockedComponent.unlock_advancement().isEmpty())) {
					text = Text.translatable(advancementLockedComponent.tooltip_text());
				}
				if (!text.equals(Text.empty())) {
					lines.add(text);
				}
			}

			if (stack.isIn(Tags.TWO_HANDED_ITEMS) && clientConfig.itemTooltipSection.show_item_tooltip_two_handed_items.get()) {
				lines.add(Text.translatable("item.additional_tooltip.functionality.two_handed_item"));
			}

			// equipment slots
			if (!isLoadOutItem && clientConfig.itemTooltipSection.show_item_tooltip_equipment_slots.get()) {
				Equipment equipment = Equipment.fromStack(stack);
				if (stack.isIn(Tags.HELMETS) || (equipment != null && equipment.getSlotType() == EquipmentSlot.HEAD)) {
					lines.add(Text.translatable("item.additional_tooltip.equipment_slot.helmet"));
				}
				if (stack.isIn(Tags.NECKLACES)) {
					lines.add(Text.translatable("item.additional_tooltip.equipment_slot.necklace"));
				}
				if (stack.isIn(Tags.CHEST_PLATES) || (equipment != null && equipment.getSlotType() == EquipmentSlot.CHEST)) {
					lines.add(Text.translatable("item.additional_tooltip.equipment_slot.chest_plate"));
				}
				if (stack.isIn(Tags.SHOULDERS)) {
					lines.add(Text.translatable("item.additional_tooltip.equipment_slot.shoulders"));
				}
				if (stack.isIn(Tags.GLOVES)) {
					lines.add(Text.translatable("item.additional_tooltip.equipment_slot.gloves"));
				}
				boolean is_unique_ring = stack.isIn(Tags.UNIQUE_RINGS);
				if (stack.isIn(Tags.RINGS_1)) {
					if (is_unique_ring) {
						lines.add(Text.translatable("item.additional_tooltip.equipment_slot.ring_unique"));
					} else {
						lines.add(Text.translatable("item.additional_tooltip.equipment_slot.ring_1"));
					}
				} else if (stack.isIn(Tags.RINGS_2)) {
					if (is_unique_ring) {
						lines.add(Text.translatable("item.additional_tooltip.equipment_slot.ring_unique"));
					} else {
						lines.add(Text.translatable("item.additional_tooltip.equipment_slot.ring_2"));
					}
				}
				if (stack.isIn(Tags.BELTS)) {
					lines.add(Text.translatable("item.additional_tooltip.equipment_slot.belt"));
				}
				if (!stack.isIn(Tags.TWO_HANDED_ITEMS)) {
					if (stack.isIn(Tags.HAND_ITEMS) && stack.isIn(Tags.OFFHAND_ITEMS)) {
						lines.add(Text.translatable("item.additional_tooltip.equipment_slot.both_hands"));
					} else if (stack.isIn(Tags.HAND_ITEMS)) {
						lines.add(Text.translatable("item.additional_tooltip.equipment_slot.hand"));
					} else if (stack.isIn(Tags.OFFHAND_ITEMS)) {
						lines.add(Text.translatable("item.additional_tooltip.equipment_slot.offhand"));
					}
				}
				if (stack.isIn(Tags.SPELLS_1)) {
					lines.add(Text.translatable("item.additional_tooltip.equipment_slot.spell_1"));
				} else if (stack.isIn(Tags.SPELLS_2)) {
					lines.add(Text.translatable("item.additional_tooltip.equipment_slot.spell_2"));
				} else if (stack.isIn(Tags.SPELLS_3)) {
					lines.add(Text.translatable("item.additional_tooltip.equipment_slot.spell_3"));
				} else if (stack.isIn(Tags.SPELLS_4)) {
					lines.add(Text.translatable("item.additional_tooltip.equipment_slot.spell_4"));
				} else if (stack.isIn(Tags.SPELLS_5)) {
					lines.add(Text.translatable("item.additional_tooltip.equipment_slot.spell_5"));
				} else if (stack.isIn(Tags.SPELLS_6)) {
					lines.add(Text.translatable("item.additional_tooltip.equipment_slot.spell_6"));
				} else if (stack.isIn(Tags.SPELLS_7)) {
					lines.add(Text.translatable("item.additional_tooltip.equipment_slot.spell_7"));
				} else if (stack.isIn(Tags.SPELLS_8)) {
					lines.add(Text.translatable("item.additional_tooltip.equipment_slot.spell_8"));
				}
				if (stack.isIn(Tags.LEGGINGS) || (equipment != null && equipment.getSlotType() == EquipmentSlot.LEGS)) {
					lines.add(Text.translatable("item.additional_tooltip.equipment_slot.leggings"));
				}
				if (stack.isIn(Tags.BOOTS) || (equipment != null && equipment.getSlotType() == EquipmentSlot.FEET)) {
					lines.add(Text.translatable("item.additional_tooltip.equipment_slot.boots"));
				}
			}
		});
	}
}
