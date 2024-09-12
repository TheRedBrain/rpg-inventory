package com.github.theredbrain.rpginventory.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;

import java.util.LinkedHashMap;

@Config(
		name = "client"
)
public class ClientConfig implements ConfigData {
	public boolean always_show_selected_hotbar_slot = false;
	public boolean offhand_item_is_right = true;
	public boolean alternative_offhand_item_is_right = true;
	public boolean show_empty_hand_slots = true;
	public boolean show_empty_alternative_hand_slots = true;
	public boolean show_inactive_inventory_slots = true;
	@ConfigEntry.Gui.PrefixText
	public boolean enable_open_backpack_button = false;
	public int open_backpack_button_offset_x = 99;
	public int open_backpack_button_offset_y = 35;
	@ConfigEntry.Gui.PrefixText
	public boolean enable_open_hand_crafting_button = false;
	public int open_hand_crafting_button_offset_x = 99;
	public int open_hand_crafting_button_offset_y = 57;
	public int hand_slots_x_offset = -140;
	public int hand_slots_y_offset = -23;
	public int alternative_hand_slots_x_offset = 91;
	public int alternative_hand_slots_y_offset = -23;
	public boolean show_armor_bar = false;
	public boolean can_hide_status_effect_screen = false;
	public boolean show_attribute_screen_when_opening_inventory_screen = false;
	public boolean show_effect_screen_when_opening_inventory_screen = true;
	public boolean slots_with_unusable_items_have_overlay = true;
	public int first_overlay_colour_for_slots_with_unusable_items = -1602211792;
	public int second_overlay_colour_for_slots_with_unusable_items = -1602211792;
	public boolean show_slot_tooltips = true;
	public boolean show_item_tooltip_two_handed_items = true;
	public boolean show_item_tooltip_equipment_slots = true;
	// TODO
	//  format where only difference to base value is shown
	@Comment("""
			Defines what attributes are displayed on the attribute screen
			Each string must follow one of these formats:
			ATTRIBUTE_VALUE:<translation_key>:attribute_id_namespace:attribute_id_path
			ATTRIBUTE_RELATION:<translation_key>:attribute_id_namespace:attribute_id_path:attribute_id_namespace:attribute_id_path
			EMPTY_LINE
			STRING:string
			TRANSLATABLE_STRING:<translation_key>
			Strings with other formats are ignored
			""")
	public String[] attribute_screen_configuration = {
			"TRANSLATABLE_STRING:gui.adventureInventory.attributes",
			"EMPTY_LINE",
			"ATTRIBUTE_VALUE:minecraft:generic.max_health",
			"ATTRIBUTE_VALUE:healthregenerationoverhaul:generic.health_regeneration",
			"ATTRIBUTE_VALUE:manaattributes:generic.max_mana",
			"ATTRIBUTE_VALUE:manaattributes:generic.mana_regeneration",
			"ATTRIBUTE_VALUE:staminaattributes:generic.max_stamina",
			"ATTRIBUTE_VALUE:staminaattributes:generic.stamina_regeneration",
			"ATTRIBUTE_VALUE:minecraft:generic.armor",
			"ATTRIBUTE_VALUE:minecraft:generic.armor_toughness",
			"ATTRIBUTE_VALUE:minecraft:generic.luck"
	};
	public LinkedHashMap<String, Float[]> sheathed_hand_item_positions = new LinkedHashMap<>() {{
		put("minecraft:crossbow", new Float[]{-0.3F, 0.1F, 0.16F, 0.0F, 0.0F, 0.06F, 0.0F, 90.0F, -10.0F});
	}};
	public LinkedHashMap<String, Float[]> sheathed_offhand_item_positions = new LinkedHashMap<>() {{
		put("minecraft:shield", new Float[]{0.2F, 0.4F, 0.0F, 0.0F, 0.0F, 0.06F, 0.0F, -90.0F, 15.0F});
	}};

	public ClientConfig() {

	}
}
