package com.github.theredbrain.rpginventory.registry;

import com.github.theredbrain.rpginventory.RPGInventory;
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
	public static final TagKey<Item> RINGS = TagKey.of(RegistryKeys.ITEM, RPGInventory.identifier("rings"));
	public static final TagKey<Item> GLOVES = TagKey.of(RegistryKeys.ITEM, RPGInventory.identifier("gloves"));
	public static final TagKey<Item> BOOTS = TagKey.of(RegistryKeys.ITEM, RPGInventory.identifier("boots"));
	public static final TagKey<Item> SPELLS = TagKey.of(RegistryKeys.ITEM, RPGInventory.identifier("spells"));
	public static final TagKey<Item> KEEPS_INVENTORY_ON_DEATH = TagKey.of(RegistryKeys.ITEM, RPGInventory.identifier("keeps_inventory_on_death"));
	public static final TagKey<Item> UNUSABLE_WHEN_LOW_DURABILITY = TagKey.of(RegistryKeys.ITEM, RPGInventory.identifier("unusable_when_low_durability"));
	public static final TagKey<Item> ARMOR_TRINKETS = TagKey.of(RegistryKeys.ITEM, RPGInventory.identifier("armor_trinkets"));
	public static final TagKey<Item> UNIQUE_RINGS = TagKey.of(RegistryKeys.ITEM, RPGInventory.identifier("unique_rings"));

}
