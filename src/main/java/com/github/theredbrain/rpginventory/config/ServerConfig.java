package com.github.theredbrain.rpginventory.config;

import com.github.theredbrain.rpginventory.RPGInventory;
import me.fzzyhmstrs.fzzy_config.annotations.Action;
import me.fzzyhmstrs.fzzy_config.annotations.ConvertFrom;
import me.fzzyhmstrs.fzzy_config.annotations.RequiresAction;
import me.fzzyhmstrs.fzzy_config.config.Config;
import me.fzzyhmstrs.fzzy_config.config.ConfigSection;
import me.fzzyhmstrs.fzzy_config.util.Walkable;
import me.fzzyhmstrs.fzzy_config.validation.collection.ValidatedList;
import me.fzzyhmstrs.fzzy_config.validation.collection.ValidatedStringMap;
import me.fzzyhmstrs.fzzy_config.validation.minecraft.ValidatedIdentifier;
import me.fzzyhmstrs.fzzy_config.validation.misc.ValidatedAny;
import me.fzzyhmstrs.fzzy_config.validation.misc.ValidatedBoolean;
import me.fzzyhmstrs.fzzy_config.validation.misc.ValidatedString;
import me.fzzyhmstrs.fzzy_config.validation.number.ValidatedFloat;
import me.fzzyhmstrs.fzzy_config.validation.number.ValidatedInt;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;

import java.util.HashMap;
import java.util.List;

@ConvertFrom(fileName = "server.json5", folder = "rpginventory")
public class ServerConfig extends Config {

	public ServerConfig() {
		super(RPGInventory.identifier("server"));
	}

	@Comment("""
			When true, all (off)hand slots can only hold items in the item tags 'rpginventory:hand_items' and 'rpginventory:offhand_items', respectively.
			""")
	public ValidatedBoolean are_hand_items_restricted_to_item_tags = new ValidatedBoolean(true);

	public ValidatedBoolean enable_hand_slot_overhaul = new ValidatedBoolean(true);

	@Comment("When false, toggling the two-handed stance is not possible when the main hand is sheathed.")
	public ValidatedBoolean always_allow_toggling_two_handed_stance = new ValidatedBoolean(false);

	public ValidatedBoolean allow_attacking_with_non_attack_items = new ValidatedBoolean(true);

	@Comment("""
			When the mod 'Stamina Attributes' is installed, the following 6 options take effect
			""")
	public StaminaAttributesCompat staminaAttributesCompat = new StaminaAttributesCompat();

	public static class StaminaAttributesCompat extends ConfigSection {
		@Comment("When true, stamina must be above 0 for swapping hand items.")
		public ValidatedBoolean swapping_hand_items_requires_stamina = new ValidatedBoolean(true);
		@Comment("Stamina cost for toggling two handed stance")
		public ValidatedFloat swapping_hand_items_stamina_cost = new ValidatedFloat(1.0f);

		@Comment("When true, stamina must be above 0 for sheathing hand items.")
		public ValidatedBoolean sheathing_hand_items_requires_stamina = new ValidatedBoolean(true);
		@Comment("Stamina cost for toggling two handed stance")
		public ValidatedFloat sheathing_hand_items_stamina_cost = new ValidatedFloat(1.0f);

		@Comment("When true, stamina must be above 0 for toggling two handed stance.")
		public ValidatedBoolean toggling_two_handed_stance_requires_stamina = new ValidatedBoolean(true);
		@Comment("Stamina cost for toggling two handed stance")
		public ValidatedFloat toggling_two_handed_stance_stamina_cost = new ValidatedFloat(1.0f);
	}

	public StatusEffects statusEffects = new StatusEffects();

	public static class StatusEffects extends ConfigSection {
		public ValidatedIdentifier keep_inventory_status_effect_identifier = new ValidatedIdentifier("variousstatuseffects:keep_inventory");

		public ValidatedIdentifier civilisation_status_effect_identifier = new ValidatedIdentifier("variousstatuseffects:civilisation");

		public ValidatedIdentifier wilderness_status_effect_identifier = new ValidatedIdentifier("variousstatuseffects:wilderness");

		@Comment("This status effect enables the building mode")
		public ValidatedIdentifier building_mode_status_effect_identifier = new ValidatedIdentifier("scriptblocks:building_mode");

		@Comment("This status effect is applied when an item in the 'two_handed_items' item tag is equipped and the two-handed stance is not active")
		public ValidatedIdentifier needs_two_handing_status_effect_identifier = new ValidatedIdentifier("variousstatuseffects:needs_two_handing");

		@Comment("This status effect is applied when an item which is not in the 'attack_items' item tag is equipped and the 'allow_attacking_with_non_attack_items' option is set to false")
		public ValidatedIdentifier no_attack_item_status_effect_identifier = new ValidatedIdentifier("variousstatuseffects:no_attack_item");
	}

	@Comment("Additional debug log is shown in the console.")
	public ValidatedBoolean show_debug_log = new ValidatedBoolean(false);

	@Comment("""
			The default amount of spell slots.
			Must be between 0 and 8 (both inclusive)
			""")
	public ValidatedInt default_spell_slot_amount = new ValidatedInt(0);

	public InventorySlots inventorySlots = new InventorySlots();

	@RequiresAction(action = Action.RELOG) // TODO test if a dedicated server RESTART is required
	public static class InventorySlots extends ConfigSection {
		@Comment("""
				Set to false to enable the 2x2 crafting grid
				in the adventure inventory screen
				""")
		public ValidatedBoolean disable_inventory_crafting_slots = new ValidatedBoolean(false);

		public ValidatedInt inventory_crafting_slots_x_offset = new ValidatedInt(97);
		public ValidatedInt inventory_crafting_slots_y_offset = new ValidatedInt(42);

		public ValidatedInt spell_slots_label_x_offset = new ValidatedInt(98);
		public ValidatedInt spell_slots_label_y_offset = new ValidatedInt(79);

		public ValidatedInt head_slot_x_offset = new ValidatedInt(33);
		public ValidatedInt head_slot_y_offset = new ValidatedInt(18);

		public ValidatedInt chest_slot_x_offset = new ValidatedInt(8);
		public ValidatedInt chest_slot_y_offset = new ValidatedInt(54);

		public ValidatedInt legs_slot_x_offset = new ValidatedInt(8);
		public ValidatedInt legs_slot_y_offset = new ValidatedInt(90);

		public ValidatedInt feet_slot_x_offset = new ValidatedInt(77);
		public ValidatedInt feet_slot_y_offset = new ValidatedInt(90);

		public ValidatedInt hand_slot_x_offset = new ValidatedInt(8);
		public ValidatedInt hand_slot_y_offset = new ValidatedInt(108);

		public ValidatedInt offhand_slot_x_offset = new ValidatedInt(26);
		public ValidatedInt offhand_slot_y_offset = new ValidatedInt(108);

		public ValidatedInt alternative_hand_slot_x_offset = new ValidatedInt(59);
		public ValidatedInt alternative_hand_slot_y_offset = new ValidatedInt(108);

		public ValidatedInt alternative_offhand_slot_x_offset = new ValidatedInt(77);
		public ValidatedInt alternative_offhand_slot_y_offset = new ValidatedInt(108);

		public ValidatedStringMap<SlotGroupPosition> slot_group_positions = new ValidatedStringMap<>(new HashMap<>(){{
			put("belts", new SlotGroupPosition(8, 72, 152, 5));
			put("shoulders", new SlotGroupPosition(8, 36, 26, 5));
			put("necklaces", new SlotGroupPosition(52, 18, 98, 5));
			put("rings_1", new SlotGroupPosition(77, 36, 116, 5));
			put("rings_2", new SlotGroupPosition(77, 54, 134, 5));
			put("gloves", new SlotGroupPosition(77, 72, 8, 32));
			put("spell_slot_1", new SlotGroupPosition(98, 90, 192, 7));
			put("spell_slot_2", new SlotGroupPosition(116, 90, 192, 25));
			put("spell_slot_3", new SlotGroupPosition(134, 90, 192, 43));
			put("spell_slot_4", new SlotGroupPosition(152, 90, 192, 61));
			put("spell_slot_5", new SlotGroupPosition(98, 108, 210, 7));
			put("spell_slot_6", new SlotGroupPosition(116, 108, 210, 25));
			put("spell_slot_7", new SlotGroupPosition(134, 108, 210, 43));
			put("spell_slot_8", new SlotGroupPosition(152, 108, 210, 61));
		}}, new ValidatedString(), new ValidatedAny<>(new SlotGroupPosition()));

		public static class SlotGroupPosition implements Walkable {

			public SlotGroupPosition() {
				new SlotGroupPosition(0, 0, 0, 0);
			}

			public SlotGroupPosition(int survival_x, int survival_y, int creative_x, int creative_y) {
				this.survival_x = survival_x;
				this.survival_y = survival_y;
				this.creative_x = creative_x;
				this.creative_y = creative_y;
			}

			public int survival_x;
			public int survival_y;
			public int creative_x;
			public int creative_y;

			public String toString() {
				return "survival_x: " + this.survival_x + ", survival_y: " + this.survival_y + ", creative_x: " + this.creative_x + ", creative_y: " + this.creative_y;
			}
		}
	}
}
