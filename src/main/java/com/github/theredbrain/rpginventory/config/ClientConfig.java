package com.github.theredbrain.rpginventory.config;

import com.github.theredbrain.rpginventory.RPGInventory;
import me.fzzyhmstrs.fzzy_config.annotations.ConvertFrom;
import me.fzzyhmstrs.fzzy_config.config.Config;
import me.fzzyhmstrs.fzzy_config.config.ConfigGroup;
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
		public ValidatedBoolean hotbar_scrolling_tries_to_sheathe_hand_items = new ValidatedBoolean(false);

		public ConfigGroup hand_slots = new ConfigGroup("hand_slots");
		public ValidatedBoolean show_empty_hand_slots = new ValidatedBoolean(true);
		public ValidatedInt hand_slots_offset_x = new ValidatedInt(-140);
		public ValidatedInt hand_slots_offset_y = new ValidatedInt(-23);
		@ConfigGroup.Pop
		public ValidatedBoolean offhand_item_is_right = new ValidatedBoolean(true);

		public ConfigGroup alternative_hand_slots = new ConfigGroup("alternative_hand_slots");
		public ValidatedBoolean show_empty_alternative_hand_slots = new ValidatedBoolean(true);
		public ValidatedInt alternative_hand_slots_offset_x = new ValidatedInt(91);
		public ValidatedInt alternative_hand_slots_offset_y = new ValidatedInt(-23);
		@ConfigGroup.Pop
		public ValidatedBoolean alternative_offhand_item_is_right = new ValidatedBoolean(true);
	}

	public ValidatedBoolean show_armor_bar = new ValidatedBoolean(false);

	public RPGInventoryScreenSection rpgInventoryScreenSection = new RPGInventoryScreenSection();

	public static class RPGInventoryScreenSection extends ConfigSection {

		public ValidatedBoolean show_attribute_screen_when_opening_inventory_screen = new ValidatedBoolean(false);
		public ValidatedBoolean can_hide_status_effect_screen = new ValidatedBoolean(false);
		public ValidatedBoolean show_effect_screen_when_opening_inventory_screen = new ValidatedBoolean(true);

		public ConfigGroup spell_slots_label = new ConfigGroup("spell_slots_label");
		public ValidatedInt spell_slots_label_x_offset = new ValidatedInt(98);
		@ConfigGroup.Pop
		public ValidatedInt spell_slots_label_y_offset = new ValidatedInt(79);


		public ConfigGroup backpack_button = new ConfigGroup("backpack_button");
		public ValidatedBoolean enable_open_backpack_button = new ValidatedBoolean(false);
		public ValidatedInt open_backpack_button_offset_x = new ValidatedInt(99);
		@ConfigGroup.Pop
		public ValidatedInt open_backpack_button_offset_y = new ValidatedInt(35);

		public ConfigGroup crafting_button = new ConfigGroup("crafting_button");
		public ValidatedBoolean enable_open_hand_crafting_button = new ValidatedBoolean(false);
		public ValidatedInt open_hand_crafting_button_offset_x = new ValidatedInt(99);
		@ConfigGroup.Pop
		public ValidatedInt open_hand_crafting_button_offset_y = new ValidatedInt(57);
	}

	public ConfigGroup unusable_item_overlay = new ConfigGroup("unusable_item_overlay");
	public ValidatedBoolean slots_with_unusable_items_have_overlay = new ValidatedBoolean(true);
	public ValidatedColor first_overlay_colour_for_slots_with_unusable_items = new ValidatedColor(200, 5, 5, 50);
	@ConfigGroup.Pop
	public ValidatedColor second_overlay_colour_for_slots_with_unusable_items = new ValidatedColor(200, 5, 5, 50);

	public ConfigGroup loadout_item_overlay = new ConfigGroup("loadout_item_overlay");
	public ValidatedBoolean slots_with_loadout_items_have_overlay = new ValidatedBoolean(true);
	public ValidatedColor first_overlay_colour_for_slots_with_loadout_items = new ValidatedColor(200, 5, 5, 50);
	@ConfigGroup.Pop
	public ValidatedColor second_overlay_colour_for_slots_with_loadout_items = new ValidatedColor(200, 5, 5, 50);

	public ConfigGroup not_owned_item_overlay = new ConfigGroup("not_owned_item_overlay");
	public ValidatedBoolean slots_with_not_owned_items_have_overlay = new ValidatedBoolean(true);
	public ValidatedColor first_overlay_colour_for_slots_with_not_owned_items = new ValidatedColor(200, 5, 5, 50);
	@ConfigGroup.Pop
	public ValidatedColor second_overlay_colour_for_slots_with_not_owned_items = new ValidatedColor(200, 5, 5, 50);

	public ConfigGroup advancement_locked_item_overlay = new ConfigGroup("advancement_locked_item_overlay");
	public ValidatedBoolean slots_with_advancement_locked_items_have_overlay = new ValidatedBoolean(true);
	public ValidatedColor first_overlay_colour_for_slots_with_advancement_not_unlocked_items = new ValidatedColor(200, 5, 5, 50);
	public ValidatedColor second_overlay_colour_for_slots_with_advancement_not_unlocked_items = new ValidatedColor(200, 5, 5, 50);
	public ValidatedColor first_overlay_colour_for_slots_with_advancement_locked_items = new ValidatedColor(200, 5, 5, 50);
	@ConfigGroup.Pop
	public ValidatedColor second_overlay_colour_for_slots_with_advancement_locked_items = new ValidatedColor(200, 5, 5, 50);

	public ItemTooltipSection itemTooltipSection = new ItemTooltipSection();

	public static class ItemTooltipSection extends ConfigSection {

		public ValidatedBoolean show_load_out_item_tooltip = new ValidatedBoolean(true);
		public ValidatedBoolean show_load_out_item_description_tooltip = new ValidatedBoolean(true);
		public ValidatedInt item_tooltip_load_out_item_index = new ValidatedInt(-1);

		public ValidatedBoolean show_item_tooltip_bound_to_player_name = new ValidatedBoolean(true);
		public ValidatedString item_tooltip_bound_to_player_name_formatting_string = new ValidatedString("");
		public ValidatedInt item_tooltip_bound_to_player_index = new ValidatedInt(-1);

		public ValidatedBoolean show_item_tooltip_crafted_by_player_name = new ValidatedBoolean(true);
		public ValidatedString item_tooltip_crafted_by_player_name_formatting_string = new ValidatedString("");
		public ValidatedInt item_tooltip_crafted_by_player_index = new ValidatedInt(-1);

		public ValidatedBoolean show_item_tooltip_advancement_locked = new ValidatedBoolean(true);
		public ValidatedInt item_tooltip_advancement_locked_index = new ValidatedInt(-1);

		public ValidatedBoolean show_item_tooltip_two_handed_items = new ValidatedBoolean(true);
		public ValidatedInt item_tooltip_two_handed_items_index = new ValidatedInt(-1);
		public ValidatedBoolean show_item_tooltip_equipment_slots = new ValidatedBoolean(false);
		public ValidatedInt item_tooltip_equipment_slots_index = new ValidatedInt(-1);
	}

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
	public ConfigGroup sheathed_item_positions = new ConfigGroup("sheathed_item_positions");
	public ValidatedMap<String, ItemConfiguration> sheathed_hand_item_positions = new ValidatedMap<>(new HashMap<>() {{
		put("minecraft:crossbow", new ItemConfiguration(-0.3F, 0.2F, 0.16F, 0.0F, 0.0F, 0.06F, 165.0F, 180.0F, -90.0F));
		put("archers:rapid_crossbow", new ItemConfiguration(-0.3F, 0.2F, 0.16F, 0.0F, 0.0F, 0.06F, 165.0F, 180.0F, -90.0F));
		put("archers:netherite_rapid_crossbow", new ItemConfiguration(-0.3F, 0.2F, 0.16F, 0.0F, 0.0F, 0.06F, 165.0F, 180.0F, -90.0F));
		put("archers:ruby_rapid_crossbow", new ItemConfiguration(-0.3F, 0.2F, 0.16F, 0.0F, 0.0F, 0.06F, 165.0F, 180.0F, -90.0F));
		put("archers:aether_rapid_crossbow", new ItemConfiguration(-0.3F, 0.2F, 0.16F, 0.0F, 0.0F, 0.06F, 165.0F, 180.0F, -90.0F));
		put("archers:heavy_crossbow", new ItemConfiguration(-0.3F, 0.2F, 0.16F, 0.0F, 0.0F, 0.06F, 165.0F, 180.0F, -90.0F));
		put("archers:netherite_heavy_crossbow", new ItemConfiguration(-0.3F, 0.2F, 0.16F, 0.0F, 0.0F, 0.06F, 165.0F, 180.0F, -90.0F));
		put("archers:ruby_heavy_crossbow", new ItemConfiguration(-0.3F, 0.2F, 0.16F, 0.0F, 0.0F, 0.06F, 165.0F, 180.0F, -90.0F));
		put("archers:aether_heavy_crossbow", new ItemConfiguration(-0.3F, 0.2F, 0.16F, 0.0F, 0.0F, 0.06F, 165.0F, 180.0F, -90.0F));
		put("arsenal:unique_heavy_crossbow_1", new ItemConfiguration(-0.3F, 0.2F, 0.16F, 0.0F, 0.0F, 0.06F, 165.0F, 180.0F, -90.0F));
		put("arsenal:unique_heavy_crossbow_2", new ItemConfiguration(-0.3F, 0.2F, 0.16F, 0.0F, 0.0F, 0.06F, 165.0F, 180.0F, -90.0F));
		put("arsenal:unique_heavy_crossbow_sw", new ItemConfiguration(-0.3F, 0.2F, 0.16F, 0.0F, 0.0F, 0.06F, 165.0F, 180.0F, -90.0F));
		put("minecraft:bow", new ItemConfiguration(0.1F, 0.3F, 0.24F, 0.0F, 0.0F, 0.06F, 45.0F, -100.0F, -100.0F));
		put("bwt:composite_bow", new ItemConfiguration(0.1F, 0.3F, 0.24F, 0.0F, 0.0F, 0.06F, 45.0F, -100.0F, -100.0F));
		put("archers:mechanic_shortbow", new ItemConfiguration(0.1F, 0.3F, 0.24F, 0.0F, 0.0F, 0.06F, 45.0F, -100.0F, -100.0F));
		put("archers:netherite_shortbow", new ItemConfiguration(0.1F, 0.3F, 0.24F, 0.0F, 0.0F, 0.06F, 45.0F, -100.0F, -100.0F));
		put("archers:crystal_shortbow", new ItemConfiguration(0.1F, 0.3F, 0.24F, 0.0F, 0.0F, 0.06F, 45.0F, -100.0F, -100.0F));
		put("archers:composite_longbow", new ItemConfiguration(0.1F, 0.3F, 0.24F, 0.0F, 0.0F, 0.06F, 45.0F, -100.0F, -100.0F));
		put("archers:royal_longbow", new ItemConfiguration(0.1F, 0.3F, 0.24F, 0.0F, 0.0F, 0.06F, 45.0F, -100.0F, -100.0F));
		put("archers:netherite_longbow", new ItemConfiguration(0.1F, 0.3F, 0.24F, 0.0F, 0.0F, 0.06F, 45.0F, -100.0F, -100.0F));
		put("archers:crystal_longbow", new ItemConfiguration(0.1F, 0.3F, 0.24F, 0.0F, 0.0F, 0.06F, 45.0F, -100.0F, -100.0F));
		put("archers:aether_longbow", new ItemConfiguration(0.1F, 0.3F, 0.24F, 0.0F, 0.0F, 0.06F, 45.0F, -100.0F, -100.0F));
		put("arsenal:unique_longbow_1", new ItemConfiguration(0.1F, 0.3F, 0.24F, 0.0F, 0.0F, 0.06F, 45.0F, -100.0F, -100.0F));
		put("arsenal:unique_longbow_2", new ItemConfiguration(0.1F, 0.3F, 0.24F, 0.0F, 0.0F, 0.06F, 45.0F, -100.0F, -100.0F));
		put("arsenal:unique_longbow_sw", new ItemConfiguration(0.1F, 0.3F, 0.24F, 0.0F, 0.0F, 0.06F, 45.0F, -100.0F, -100.0F));
		put("minecrawl:spell_casting_crossbow_1", new ItemConfiguration(-0.3F, 0.2F, 0.16F, 0.0F, 0.0F, 0.06F, 165.0F, 180.0F, -90.0F));
		put("minecrawl:spell_casting_crossbow_2", new ItemConfiguration(-0.3F, 0.2F, 0.16F, 0.0F, 0.0F, 0.06F, 165.0F, 180.0F, -90.0F));
		put("minecrawl:spell_casting_crossbow_4", new ItemConfiguration(-0.3F, 0.2F, 0.16F, 0.0F, 0.0F, 0.06F, 165.0F, 180.0F, -90.0F));
		put("minecrawl:spell_casting_crossbow_3", new ItemConfiguration(-0.3F, 0.2F, 0.16F, 0.0F, 0.0F, 0.06F, 165.0F, 180.0F, -90.0F));
		put("minecrawl:spell_casting_crossbow_5", new ItemConfiguration(-0.3F, 0.2F, 0.16F, 0.0F, 0.0F, 0.06F, 165.0F, 180.0F, -90.0F));
		put("minecrawl:spell_casting_crossbow_6", new ItemConfiguration(-0.3F, 0.2F, 0.16F, 0.0F, 0.0F, 0.06F, 165.0F, 180.0F, -90.0F));
		put("minecrawl:spell_casting_crossbow_7", new ItemConfiguration(-0.3F, 0.2F, 0.16F, 0.0F, 0.0F, 0.06F, 165.0F, 180.0F, -90.0F));
		put("minecrawl:spell_casting_crossbow_8", new ItemConfiguration(-0.3F, 0.2F, 0.16F, 0.0F, 0.0F, 0.06F, 165.0F, 180.0F, -90.0F));
		put("minecrawl:spell_casting_crossbow_9", new ItemConfiguration(-0.3F, 0.2F, 0.16F, 0.0F, 0.0F, 0.06F, 165.0F, 180.0F, -90.0F));
		put("minecrawl:spell_casting_crossbow_10", new ItemConfiguration(-0.3F, 0.2F, 0.16F, 0.0F, 0.0F, 0.06F, 165.0F, 180.0F, -90.0F));
		put("minecrawl:spell_casting_crossbow_11", new ItemConfiguration(-0.3F, 0.2F, 0.16F, 0.0F, 0.0F, 0.06F, 165.0F, 180.0F, -90.0F));
		put("minecrawl:spell_casting_crossbow_12", new ItemConfiguration(-0.3F, 0.2F, 0.16F, 0.0F, 0.0F, 0.06F, 165.0F, 180.0F, -90.0F));
	}}, new ValidatedString(), new ValidatedAny<>(new ItemConfiguration()));
	@ConfigGroup.Pop
	public ValidatedMap<String, ItemConfiguration> sheathed_offhand_item_positions = new ValidatedMap<>(new HashMap<>() {{
		put("minecraft:shield", new ItemConfiguration(0.2F, 0.4F, 0.075F, 0.0F, 0.0F, 0.06F, 0.0F, -90.0F, 15.0F));
		put("paladins:iron_kite_shield", new ItemConfiguration(0.2F, 0.4F, 0.075F, 0.0F, 0.0F, 0.06F, 0.0F, -90.0F, 15.0F));
		put("paladins:golden_kite_shield", new ItemConfiguration(0.2F, 0.4F, 0.075F, 0.0F, 0.0F, 0.06F, 0.0F, -90.0F, 15.0F));
		put("paladins:diamond_kite_shield", new ItemConfiguration(0.2F, 0.4F, 0.075F, 0.0F, 0.0F, 0.06F, 0.0F, -90.0F, 15.0F));
		put("paladins:netherite_kite_shield", new ItemConfiguration(0.2F, 0.4F, 0.075F, 0.0F, 0.0F, 0.06F, 0.0F, -90.0F, 15.0F));
		put("paladins:ruby_kite_shield", new ItemConfiguration(0.2F, 0.4F, 0.075F, 0.0F, 0.0F, 0.06F, 0.0F, -90.0F, 15.0F));
		put("paladins:aeternium_kite_shield", new ItemConfiguration(0.2F, 0.4F, 0.075F, 0.0F, 0.0F, 0.06F, 0.0F, -90.0F, 15.0F));
		put("paladins:aether_kite_shield", new ItemConfiguration(0.2F, 0.4F, 0.075F, 0.0F, 0.0F, 0.06F, 0.0F, -90.0F, 15.0F));
		put("arsenal:unique_shield_1", new ItemConfiguration(0.2F, 0.4F, 0.075F, 0.0F, 0.0F, 0.06F, 0.0F, -90.0F, 15.0F));
		put("arsenal:unique_shield_sw", new ItemConfiguration(0.2F, 0.4F, 0.075F, 0.0F, 0.0F, 0.06F, 0.0F, -90.0F, 15.0F));
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
