package com.github.theredbrain.rpginventory.registry;

import com.github.theredbrain.rpginventory.RPGInventory;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;

public class Tags {
	public static final TagKey<Item> ADVENTURE_HOTBAR_ITEMS = TagKey.of(RegistryKeys.ITEM, RPGInventory.identifier("adventure_hotbar_items"));
	public static final TagKey<Item> ATTACK_ITEMS = TagKey.of(RegistryKeys.ITEM, RPGInventory.identifier("attack_items"));
	public static final TagKey<Item> TWO_HANDED_ITEMS = TagKey.of(RegistryKeys.ITEM, RPGInventory.identifier("two_handed_items"));
	public static final TagKey<Item> NON_TWO_HANDED_ITEMS = TagKey.of(RegistryKeys.ITEM, RPGInventory.identifier("non_two_handed_items"));
	public static final TagKey<Item> HAND_ITEMS = TagKey.of(RegistryKeys.ITEM, RPGInventory.identifier("hand_items"));
	public static final TagKey<Item> OFFHAND_ITEMS = TagKey.of(RegistryKeys.ITEM, RPGInventory.identifier("offhand_items"));
	public static final TagKey<Item> EMPTY_HAND_WEAPONS = TagKey.of(RegistryKeys.ITEM, RPGInventory.identifier("empty_hand_weapons"));
	public static final TagKey<Item> HELMETS = TagKey.of(RegistryKeys.ITEM, RPGInventory.identifier("helmets"));
	public static final TagKey<Item> SHOULDERS = TagKey.of(RegistryKeys.ITEM, RPGInventory.identifier("shoulders"));
	public static final TagKey<Item> CHEST_PLATES = TagKey.of(RegistryKeys.ITEM, RPGInventory.identifier("chest_plates"));
	public static final TagKey<Item> BELTS = TagKey.of(RegistryKeys.ITEM, RPGInventory.identifier("belts"));
	public static final TagKey<Item> LEGGINGS = TagKey.of(RegistryKeys.ITEM, RPGInventory.identifier("leggings"));
	public static final TagKey<Item> NECKLACES = TagKey.of(RegistryKeys.ITEM, RPGInventory.identifier("necklaces"));
	public static final TagKey<Item> RINGS_1 = TagKey.of(RegistryKeys.ITEM, RPGInventory.identifier("rings_1"));
	public static final TagKey<Item> RINGS_2 = TagKey.of(RegistryKeys.ITEM, RPGInventory.identifier("rings_2"));
	public static final TagKey<Item> GLOVES = TagKey.of(RegistryKeys.ITEM, RPGInventory.identifier("gloves"));
	public static final TagKey<Item> BOOTS = TagKey.of(RegistryKeys.ITEM, RPGInventory.identifier("boots"));
	public static final TagKey<Item> SPELLS_1 = TagKey.of(RegistryKeys.ITEM, RPGInventory.identifier("spells_1"));
	public static final TagKey<Item> SPELLS_2 = TagKey.of(RegistryKeys.ITEM, RPGInventory.identifier("spells_2"));
	public static final TagKey<Item> SPELLS_3 = TagKey.of(RegistryKeys.ITEM, RPGInventory.identifier("spells_3"));
	public static final TagKey<Item> SPELLS_4 = TagKey.of(RegistryKeys.ITEM, RPGInventory.identifier("spells_4"));
	public static final TagKey<Item> SPELLS_5 = TagKey.of(RegistryKeys.ITEM, RPGInventory.identifier("spells_5"));
	public static final TagKey<Item> SPELLS_6 = TagKey.of(RegistryKeys.ITEM, RPGInventory.identifier("spells_6"));
	public static final TagKey<Item> SPELLS_7 = TagKey.of(RegistryKeys.ITEM, RPGInventory.identifier("spells_7"));
	public static final TagKey<Item> SPELLS_8 = TagKey.of(RegistryKeys.ITEM, RPGInventory.identifier("spells_8"));
	public static final TagKey<Item> RELICS = TagKey.of(RegistryKeys.ITEM, RPGInventory.identifier("relics"));
	public static final TagKey<Item> SACRIFICED_TO_KEEP_INVENTORY_ON_DEATH = TagKey.of(RegistryKeys.ITEM, RPGInventory.identifier("sacrificed_to_keep_inventory_on_death"));
	public static final TagKey<Item> UNUSABLE_WHEN_LOW_DURABILITY = TagKey.of(RegistryKeys.ITEM, RPGInventory.identifier("unusable_when_low_durability"));
	public static final TagKey<Item> UNIQUE_RINGS = TagKey.of(RegistryKeys.ITEM, RPGInventory.identifier("unique_rings"));
	public static final TagKey<Item> NOT_SHOWN_WHEN_IN_SHEATHED_OFFHAND = TagKey.of(RegistryKeys.ITEM, RPGInventory.identifier("not_shown_when_in_sheathed_offhand"));
	public static final TagKey<Item> NOT_SHOWN_WHEN_IN_SHEATHED_HAND = TagKey.of(RegistryKeys.ITEM, RPGInventory.identifier("not_shown_when_in_sheathed_hand"));

	public static final TagKey<StatusEffect> PREVENTS_MANNEQUIN_INTERACTION = TagKey.of(RegistryKeys.STATUS_EFFECT, RPGInventory.identifier("prevents_mannequin_interaction"));
	public static final TagKey<StatusEffect> PREVENTS_MANNEQUIN_SLOT_INTERACTION = TagKey.of(RegistryKeys.STATUS_EFFECT, RPGInventory.identifier("prevents_mannequin_slot_interaction"));
	public static final TagKey<StatusEffect> FOOD_EFFECTS = TagKey.of(RegistryKeys.STATUS_EFFECT, RPGInventory.identifier("food_effects"));
	public static final TagKey<StatusEffect> KEPT_ON_PVP_DEATH = TagKey.of(RegistryKeys.STATUS_EFFECT, RPGInventory.identifier("kept_on_pvp_death"));

	public static final TagKey<DamageType> REMOVES_PLAYER_FROM_PVP = TagKey.of(RegistryKeys.DAMAGE_TYPE, RPGInventory.identifier("removes_player_from_pvp"));

}
