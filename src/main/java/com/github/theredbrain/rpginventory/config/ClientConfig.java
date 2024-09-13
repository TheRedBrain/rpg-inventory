package com.github.theredbrain.rpginventory.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.autoconfig.serializer.PartitioningSerializer;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;

import java.util.LinkedHashMap;

@Config(
		name = "rpginventory"
)
public class ClientConfig extends PartitioningSerializer.GlobalData {
	@ConfigEntry.Category("generalClientConfig")
	@ConfigEntry.Gui.TransitiveObject
	public GeneralClientConfig generalClientConfig = new GeneralClientConfig();
	@ConfigEntry.Category("attributeScreenClientConfig")
	@ConfigEntry.Gui.TransitiveObject
	public AttributeScreenClientConfig attributeScreenClientConfig = new AttributeScreenClientConfig();
	@ConfigEntry.Category("sheathedItemPositionsClientConfig")
	@ConfigEntry.Gui.TransitiveObject
	public SheathedItemPositionsClientConfig sheathedItemPositionsClientConfig = new SheathedItemPositionsClientConfig();

	public ClientConfig() {
	}

	@Config(
			name = "generalClientConfig"
	)
	public static class GeneralClientConfig implements ConfigData {
		@ConfigEntry.Gui.PrefixText
		public boolean always_show_selected_hotbar_slot = false;

		public boolean show_empty_hand_slots = true;
		public int hand_slots_offset_x = -140;
		public int hand_slots_offset_y = -23;
		public boolean offhand_item_is_right = true;

		public boolean show_empty_alternative_hand_slots = true;
		public int alternative_hand_slots_offset_x = 91;
		public int alternative_hand_slots_offset_y = -23;
		public boolean alternative_offhand_item_is_right = true;

		public boolean show_armor_bar = false;

		@ConfigEntry.Gui.PrefixText
		public boolean show_attribute_screen_when_opening_inventory_screen = false;
		public boolean can_hide_status_effect_screen = false;
		public boolean show_effect_screen_when_opening_inventory_screen = true;

		@ConfigEntry.Gui.PrefixText
		public boolean show_inactive_inventory_slots = true;

		@ConfigEntry.Gui.PrefixText
		public boolean enable_open_backpack_button = false;
		public int open_backpack_button_offset_x = 99;
		public int open_backpack_button_offset_y = 35;

		@ConfigEntry.Gui.PrefixText
		public boolean enable_open_hand_crafting_button = false;
		public int open_hand_crafting_button_offset_x = 99;
		public int open_hand_crafting_button_offset_y = 57;

		@ConfigEntry.Gui.PrefixText
		public boolean slots_with_unusable_items_have_overlay = true;
		public int first_overlay_colour_for_slots_with_unusable_items = -1602211792;
		public int second_overlay_colour_for_slots_with_unusable_items = -1602211792;
		public boolean show_slot_tooltips = true;
		public boolean show_item_tooltip_two_handed_items = true;
		public boolean show_item_tooltip_equipment_slots = true;

		public GeneralClientConfig() {
		}

	}

	@Config(
			name = "attributeScreenClientConfig"
	)
	public static class AttributeScreenClientConfig implements ConfigData {
		// TODO
		//  format where only difference to base value is shown
		@ConfigEntry.Gui.PrefixText
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

		public AttributeScreenClientConfig() {
		}

	}

	@Config(
			name = "sheathedItemPositionsClientConfig"
	)
	public static class SheathedItemPositionsClientConfig implements ConfigData {
		// TODO register GUI provider for java.util.LinkedHashMap
		@ConfigEntry.Gui.PrefixText
		@Comment("""
				These values describe how the matrixStack is manipulated.
				
				They are defined like so:
				
				initial_translation_x
				initial_translation_y
				initial_translation_z
				equipped_chest_offset_x
				equipped_chest_offset_y
				equipped_chest_offset_z
				rotation_positive_z
				rotation_positive_y
				rotation_positive_x
				
				Manipulations:
				matrixStack.translate(initial_translation_x, initial_translation_y, initial_translation_z);
				if (hasStackEquippedInChestSlot) {
					matrixStack.translate(equipped_chest_offset_x, equipped_chest_offset_y, equipped_chest_offset_z);
				}
				matrixStack.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(rotation_positive_z));
				matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(rotation_positive_y));
				matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(rotation_positive_x));
				""")
		@ConfigEntry.Gui.Excluded
		public LinkedHashMap<String, Float[]> sheathed_hand_item_positions = new LinkedHashMap<>() {{
			put("minecraft:crossbow", new Float[]{-0.3F, 0.1F, 0.16F, 0.0F, 0.0F, 0.06F, 0.0F, 90.0F, -10.0F});
		}};
		@ConfigEntry.Gui.Excluded
		public LinkedHashMap<String, Float[]> sheathed_offhand_item_positions = new LinkedHashMap<>() {{
			put("minecraft:shield", new Float[]{0.2F, 0.4F, 0.0F, 0.0F, 0.0F, 0.06F, 0.0F, -90.0F, 15.0F});
		}};

		public SheathedItemPositionsClientConfig() {
		}

	}

}
