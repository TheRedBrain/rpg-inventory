package com.github.theredbrain.rpginventory.registry;

import com.github.theredbrain.rpginventory.RPGInventoryClient;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.minecraft.text.Text;

public class ClientEventsRegistry {

	public static void initializeClientEvents() {
		ItemTooltipCallback.EVENT.register((stack, context, lines) -> {
			if (stack.isIn(Tags.TWO_HANDED_ITEMS) && RPGInventoryClient.clientConfig.show_item_tooltip_two_handed_items) {
				lines.add(Text.translatable("item.additional_tooltip.functionality.two_handed_item"));
			}

			// equipment slots
			if (RPGInventoryClient.clientConfig.show_item_tooltip_equipment_slots) {
				if (stack.isIn(Tags.HELMETS)) {
					lines.add(Text.translatable("item.additional_tooltip.equipment_slot.helmet"));
				}
				if (stack.isIn(Tags.NECKLACES)) {
					lines.add(Text.translatable("item.additional_tooltip.equipment_slot.necklace"));
				}
				if (stack.isIn(Tags.CHEST_PLATES)) {
					lines.add(Text.translatable("item.additional_tooltip.equipment_slot.chest_plate"));
				}
				if (stack.isIn(Tags.SHOULDERS)) {
					lines.add(Text.translatable("item.additional_tooltip.equipment_slot.shoulders"));
				}
				if (stack.isIn(Tags.GLOVES)) {
					lines.add(Text.translatable("item.additional_tooltip.equipment_slot.gloves"));
				}
				if (stack.isIn(Tags.RINGS)) {
					if (stack.isIn(Tags.UNIQUE_RINGS)) {
						lines.add(Text.translatable("item.additional_tooltip.equipment_slot.ring_unique"));
					} else {
						lines.add(Text.translatable("item.additional_tooltip.equipment_slot.ring"));
					}
				}
				if (stack.isIn(Tags.BELTS)) {
					lines.add(Text.translatable("item.additional_tooltip.equipment_slot.belt"));
				}
				if (stack.isIn(Tags.MAIN_HAND_ITEMS) && stack.isIn(Tags.OFFHAND_ITEMS)) {
					lines.add(Text.translatable("item.additional_tooltip.equipment_slot.both_hands"));
				} else if (stack.isIn(Tags.MAIN_HAND_ITEMS)) {
					lines.add(Text.translatable("item.additional_tooltip.equipment_slot.main_hand"));
				} else if (stack.isIn(Tags.OFFHAND_ITEMS)) {
					lines.add(Text.translatable("item.additional_tooltip.equipment_slot.offhand"));
				}
				if (stack.isIn(Tags.SPELLS)) {
					lines.add(Text.translatable("item.additional_tooltip.equipment_slot.spell"));
				}
				if (stack.isIn(Tags.LEGGINGS)) {
					lines.add(Text.translatable("item.additional_tooltip.equipment_slot.leggings"));
				}
				if (stack.isIn(Tags.BOOTS)) {
					lines.add(Text.translatable("item.additional_tooltip.equipment_slot.boots"));
				}
			}
		});
	}
}
