package com.github.theredbrain.rpginventory.config;

import com.github.theredbrain.rpginventory.RPGInventory;
import me.fzzyhmstrs.fzzy_config.annotations.Action;
import me.fzzyhmstrs.fzzy_config.annotations.ConvertFrom;
import me.fzzyhmstrs.fzzy_config.annotations.RequiresAction;
import me.fzzyhmstrs.fzzy_config.annotations.Translation;
import me.fzzyhmstrs.fzzy_config.config.Config;
import me.fzzyhmstrs.fzzy_config.config.ConfigGroup;
import me.fzzyhmstrs.fzzy_config.config.ConfigSection;
import me.fzzyhmstrs.fzzy_config.util.Walkable;
import me.fzzyhmstrs.fzzy_config.validation.collection.ValidatedStringMap;
import me.fzzyhmstrs.fzzy_config.validation.minecraft.ValidatedIdentifier;
import me.fzzyhmstrs.fzzy_config.validation.misc.ValidatedAny;
import me.fzzyhmstrs.fzzy_config.validation.misc.ValidatedBoolean;
import me.fzzyhmstrs.fzzy_config.validation.misc.ValidatedString;
import me.fzzyhmstrs.fzzy_config.validation.number.ValidatedFloat;
import me.fzzyhmstrs.fzzy_config.validation.number.ValidatedInt;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

import java.util.HashMap;

@ConvertFrom(fileName = "server.json5", folder = "rpginventory")
public class ServerConfig extends Config {

	public ServerConfig() {
		super(RPGInventory.identifier("server"));
	}

	public HandSlotOverhaul handSlotOverhaul = new HandSlotOverhaul();

	public static class HandSlotOverhaul extends ConfigSection {

		public ValidatedBoolean enable_hand_slot_overhaul = new ValidatedBoolean(true);

		public ValidatedBoolean always_allow_toggling_two_handed_stance = new ValidatedBoolean(false);

		public ValidatedBoolean are_hand_items_restricted_to_item_tags = new ValidatedBoolean(true);

		public StaminaAttributesCompat staminaAttributesCompat = new StaminaAttributesCompat();

		public static class StaminaAttributesCompat extends ConfigSection {

			public ConfigGroup swapping_hand_items = new ConfigGroup("swapping_hand_items", true);
			public ValidatedBoolean swapping_hand_items_requires_stamina = new ValidatedBoolean(true);
			@ConfigGroup.Pop
			public ValidatedFloat swapping_hand_items_stamina_cost = new ValidatedFloat(1.0f);

			public ConfigGroup sheathing_hand_items = new ConfigGroup("sheathing_hand_items", true);
			public ValidatedBoolean sheathing_hand_items_requires_stamina = new ValidatedBoolean(true);
			@ConfigGroup.Pop
			public ValidatedFloat sheathing_hand_items_stamina_cost = new ValidatedFloat(1.0f);

			public ConfigGroup toggling_two_handed_stance = new ConfigGroup("toggling_two_handed_stance", true);
			public ValidatedBoolean toggling_two_handed_stance_requires_stamina = new ValidatedBoolean(true);
			@ConfigGroup.Pop
			public ValidatedFloat toggling_two_handed_stance_stamina_cost = new ValidatedFloat(1.0f);
		}

	}

	public ValidatedBoolean enable_item_bounding_in_creative = new ValidatedBoolean(false);

	public ValidatedBoolean allow_attacking_with_non_attack_items = new ValidatedBoolean(true);

	public StatusEffects statusEffects = new StatusEffects();

	public static class StatusEffects extends ConfigSection {
		public ValidatedIdentifier keep_inventory_status_effect_identifier = ValidatedIdentifier.ofRegistry(Identifier.of("variousstatuseffects:keep_inventory"), Registries.STATUS_EFFECT);

		public ValidatedIdentifier civilisation_status_effect_identifier = ValidatedIdentifier.ofRegistry(Identifier.of("variousstatuseffects:civilisation"), Registries.STATUS_EFFECT);

		public ValidatedIdentifier wilderness_status_effect_identifier = ValidatedIdentifier.ofRegistry(Identifier.of("variousstatuseffects:wilderness"), Registries.STATUS_EFFECT);

		public ValidatedIdentifier building_mode_status_effect_identifier = ValidatedIdentifier.ofRegistry(Identifier.of("scriptblocks:building_mode"), Registries.STATUS_EFFECT);

		public ValidatedIdentifier needs_two_handing_status_effect_identifier = ValidatedIdentifier.ofRegistry(Identifier.of("variousstatuseffects:needs_two_handing"), Registries.STATUS_EFFECT);

		public ValidatedIdentifier no_attack_item_status_effect_identifier = ValidatedIdentifier.ofRegistry(Identifier.of("variousstatuseffects:no_attack_item"), Registries.STATUS_EFFECT);
	}

	public InventorySlots inventorySlots = new InventorySlots();

	@RequiresAction(action = Action.RELOG) // TODO test if a dedicated server RESTART is required
	public static class InventorySlots extends ConfigSection {

		public ConfigGroup crafting_slots = new ConfigGroup("crafting_slots", true);

		public ValidatedBoolean disable_inventory_crafting_slots = new ValidatedBoolean(false);
		public ValidatedInt inventory_crafting_slots_x_offset = new ValidatedInt(97);
		@ConfigGroup.Pop
		public ValidatedInt inventory_crafting_slots_y_offset = new ValidatedInt(42);

		public ConfigGroup head_slot = new ConfigGroup("head_slot", true);
		public ValidatedInt head_slot_x_offset = new ValidatedInt(33);
		@ConfigGroup.Pop
		public ValidatedInt head_slot_y_offset = new ValidatedInt(18);

		public ConfigGroup chest_slot = new ConfigGroup("chest_slot", true);
		public ValidatedInt chest_slot_x_offset = new ValidatedInt(8);
		@ConfigGroup.Pop
		public ValidatedInt chest_slot_y_offset = new ValidatedInt(54);

		public ConfigGroup legs_slot = new ConfigGroup("legs_slot", true);
		public ValidatedInt legs_slot_x_offset = new ValidatedInt(8);
		@ConfigGroup.Pop
		public ValidatedInt legs_slot_y_offset = new ValidatedInt(90);

		public ConfigGroup feet_slot = new ConfigGroup("feet_slot", true);
		public ValidatedInt feet_slot_x_offset = new ValidatedInt(77);
		@ConfigGroup.Pop
		public ValidatedInt feet_slot_y_offset = new ValidatedInt(90);

		public ConfigGroup offhand_slot = new ConfigGroup("offhand_slot", true);
		public ValidatedInt offhand_slot_x_offset = new ValidatedInt(26);
		@ConfigGroup.Pop
//		@ConfigGroup.Pop
		public ValidatedInt offhand_slot_y_offset = new ValidatedInt(108);

		public ConfigGroup hand_slots = new ConfigGroup("hand_slots", true);
		public ValidatedInt hand_slot_x_offset = new ValidatedInt(8);
		@ConfigGroup.Pop
		public ValidatedInt hand_slot_y_offset = new ValidatedInt(108);

		public ConfigGroup alternative_hand_slots = new ConfigGroup("alternative_hand_slots", true);
		public ValidatedInt alternative_hand_slot_x_offset = new ValidatedInt(59);
		@ConfigGroup.Pop
		public ValidatedInt alternative_hand_slot_y_offset = new ValidatedInt(108);

		public ConfigGroup alternative_offhand_slots = new ConfigGroup("alternative_offhand_slots", true);
		public ValidatedInt alternative_offhand_slot_x_offset = new ValidatedInt(77);
		@ConfigGroup.Pop
//		@ConfigGroup.Pop
		public ValidatedInt alternative_offhand_slot_y_offset = new ValidatedInt(108);

		public ConfigGroup trinket_slots = new ConfigGroup("trinket_slots", true);
		public ValidatedInt default_spell_slot_amount = new ValidatedInt(0, 8, 0);

		@ConfigGroup.Pop
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

		@Translation(prefix = "rpginventory.server.slot_group_position")
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
