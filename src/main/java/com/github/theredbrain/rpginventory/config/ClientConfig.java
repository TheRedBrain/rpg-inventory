package com.github.theredbrain.rpginventory.config;

import com.github.theredbrain.rpginventory.RPGInventory;
import me.fzzyhmstrs.fzzy_config.annotations.ConvertFrom;
import me.fzzyhmstrs.fzzy_config.config.Config;
import me.fzzyhmstrs.fzzy_config.config.ConfigSection;
import me.fzzyhmstrs.fzzy_config.util.Walkable;
import me.fzzyhmstrs.fzzy_config.validation.collection.ValidatedMap;
import me.fzzyhmstrs.fzzy_config.validation.misc.ValidatedAny;
import me.fzzyhmstrs.fzzy_config.validation.misc.ValidatedBoolean;
import me.fzzyhmstrs.fzzy_config.validation.misc.ValidatedColor;
import me.fzzyhmstrs.fzzy_config.validation.misc.ValidatedString;
import me.fzzyhmstrs.fzzy_config.validation.number.ValidatedInt;

import java.util.HashMap;

@ConvertFrom(fileName = "client.json5", folder = "rpginventory")
public class ClientConfig extends Config {

	public ClientConfig() {
		super(RPGInventory.identifier("client"));
	}

	public HotBarOverhaul hotBarOverhaul = new HotBarOverhaul();

	public static class HotBarOverhaul extends ConfigSection {

		public ValidatedBoolean enable_hotbar_overhaul = new ValidatedBoolean(true);

		public ValidatedBoolean always_show_selected_hotbar_slot = new ValidatedBoolean(false);
		public ValidatedBoolean always_show_all_hotbar_slots = new ValidatedBoolean(false);
		public ValidatedBoolean is_hotbar_centered = new ValidatedBoolean(false);

		public ValidatedBoolean show_empty_hand_slots = new ValidatedBoolean(true);
		public ValidatedInt hand_slots_offset_x = new ValidatedInt(-140);
		public ValidatedInt hand_slots_offset_y = new ValidatedInt(-23);
		public ValidatedBoolean offhand_item_is_right = new ValidatedBoolean(true);

		public ValidatedBoolean show_empty_alternative_hand_slots = new ValidatedBoolean(true);
		public ValidatedInt alternative_hand_slots_offset_x = new ValidatedInt(91);
		public ValidatedInt alternative_hand_slots_offset_y = new ValidatedInt(-23);
		public ValidatedBoolean alternative_offhand_item_is_right = new ValidatedBoolean(true);
	}

	public ValidatedBoolean show_armor_bar = new ValidatedBoolean(false);

	public RPGInventoryScreenSection rpgInventoryScreenSection = new RPGInventoryScreenSection();

	public static class RPGInventoryScreenSection extends ConfigSection {

		//		@ConfigEntry.Gui.PrefixText
		public ValidatedBoolean show_attribute_screen_when_opening_inventory_screen = new ValidatedBoolean(false);
		public ValidatedBoolean can_hide_status_effect_screen = new ValidatedBoolean(false);
		public ValidatedBoolean show_effect_screen_when_opening_inventory_screen = new ValidatedBoolean(true);

		//		@ConfigEntry.Gui.PrefixText
		public ValidatedBoolean show_inactive_inventory_slots = new ValidatedBoolean(true);

		//		@ConfigEntry.Gui.PrefixText
		public ValidatedBoolean enable_open_backpack_button = new ValidatedBoolean(false);
		public ValidatedInt open_backpack_button_offset_x = new ValidatedInt(99);
		public ValidatedInt open_backpack_button_offset_y = new ValidatedInt(35);

		//		@ConfigEntry.Gui.PrefixText
		public ValidatedBoolean enable_open_hand_crafting_button = new ValidatedBoolean(false);
		public ValidatedInt open_hand_crafting_button_offset_x = new ValidatedInt(99);
		public ValidatedInt open_hand_crafting_button_offset_y = new ValidatedInt(57);

		public ValidatedBoolean show_slot_tooltips = new ValidatedBoolean(true);
	}

	//		@ConfigEntry.Gui.PrefixText
	public ValidatedBoolean slots_with_unusable_items_have_overlay = new ValidatedBoolean(true);
	public ValidatedColor first_overlay_colour_for_slots_with_unusable_items = new ValidatedColor(200, 5, 5, 50);
	public ValidatedColor second_overlay_colour_for_slots_with_unusable_items = new ValidatedColor(200, 5, 5, 50);

	public ValidatedBoolean show_item_tooltip_bound_to_player_name = new ValidatedBoolean(true);
	public ValidatedString item_tooltip_bound_to_player_name_formatting_string = new ValidatedString("");

	public ValidatedBoolean show_item_tooltip_crafted_by_player_name = new ValidatedBoolean(true);
	public ValidatedString item_tooltip_crafted_by_player_name_formatting_string = new ValidatedString("");

	public ValidatedBoolean show_item_tooltip_two_handed_items = new ValidatedBoolean(true);
	public ValidatedBoolean show_item_tooltip_equipment_slots = new ValidatedBoolean(true);

	//		@Comment("""
//				These values describe how the matrixStack is manipulated.
//
//				They are defined like so:
//
//				initial_translation_x
//				initial_translation_y
//				initial_translation_z
//				equipped_chest_offset_x
//				equipped_chest_offset_y
//				equipped_chest_offset_z
//				rotation_positive_z
//				rotation_positive_y
//				rotation_positive_x
//
//				Manipulations:
//				matrixStack.translate(initial_translation_x, initial_translation_y, initial_translation_z);
//				if (hasStackEquippedInChestSlot) {
//					matrixStack.translate(equipped_chest_offset_x, equipped_chest_offset_y, equipped_chest_offset_z);
//				}
//				matrixStack.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(rotation_positive_z));
//				matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(rotation_positive_y));
//				matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(rotation_positive_x));
//				""")
	public ValidatedMap<String, ItemConfiguration> sheathed_hand_item_positions = new ValidatedMap<>(new HashMap<>() {{
		put("minecraft:crossbow", new ItemConfiguration(-0.3F, 0.1F, 0.16F, 0.0F, 0.0F, 0.06F, 0.0F, 90.0F, -10.0F));
	}}, new ValidatedString(), new ValidatedAny<>(new ItemConfiguration()));

	public ValidatedMap<String, ItemConfiguration> sheathed_offhand_item_positions = new ValidatedMap<>(new HashMap<>() {{
		put("minecraft:shield", new ItemConfiguration(0.2F, 0.4F, 0.0F, 0.0F, 0.0F, 0.06F, 0.0F, -90.0F, 15.0F));
	}}, new ValidatedString(), new ValidatedAny<>(new ItemConfiguration()));

	public static class ItemConfiguration implements Walkable {

		public ItemConfiguration() {
			new ItemConfiguration(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
		}

		public ItemConfiguration(
				float initial_translation_x,
				float initial_translation_y,
				float initial_translation_z,
				float equipped_chest_offset_x,
				float equipped_chest_offset_y,
				float equipped_chest_offset_z,
				float rotation_positive_z,
				float rotation_positive_y,
				float rotation_positive_x
		) {
			this.initial_translation_x = initial_translation_x;
			this.initial_translation_y = initial_translation_y;
			this.initial_translation_z = initial_translation_z;
			this.equipped_chest_offset_x = equipped_chest_offset_x;
			this.equipped_chest_offset_y = equipped_chest_offset_y;
			this.equipped_chest_offset_z = equipped_chest_offset_z;
			this.rotation_positive_z = rotation_positive_z;
			this.rotation_positive_y = rotation_positive_y;
			this.rotation_positive_x = rotation_positive_x;
		}

		public float initial_translation_x;
		public float initial_translation_y;
		public float initial_translation_z;
		public float equipped_chest_offset_x;
		public float equipped_chest_offset_y;
		public float equipped_chest_offset_z;
		public float rotation_positive_z;
		public float rotation_positive_y;
		public float rotation_positive_x;

		public String toString() {
			return "initial_translation_x: " + this.initial_translation_x +
					", initial_translation_y: " + this.initial_translation_y +
					", initial_translation_z: " + this.initial_translation_z +
					", equipped_chest_offset_x: " + this.equipped_chest_offset_x +
					", equipped_chest_offset_y: " + this.equipped_chest_offset_y +
					", equipped_chest_offset_z: " + this.equipped_chest_offset_z +
					", rotation_positive_z: " + this.rotation_positive_z +
					", rotation_positive_y: " + this.rotation_positive_y +
					", rotation_positive_x: " + this.rotation_positive_x;
		}
	}
}
