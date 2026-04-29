package com.github.theredbrain.rpginventory.registry;

import com.github.theredbrain.rpginventory.RPGInventory;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.item.Item;

public class Tags {
	public static final TagKey<Item> ADVENTURE_HOTBAR_ITEMS = TagKey.create(Registries.ITEM, RPGInventory.identifier("adventure_hotbar_items"));
	public static final TagKey<Item> ATTACK_ITEMS = TagKey.create(Registries.ITEM, RPGInventory.identifier("attack_items"));
	public static final TagKey<Item> TWO_HANDED_ITEMS = TagKey.create(Registries.ITEM, RPGInventory.identifier("two_handed_items"));
	public static final TagKey<Item> NON_TWO_HANDED_ITEMS = TagKey.create(Registries.ITEM, RPGInventory.identifier("non_two_handed_items"));
	public static final TagKey<Item> HAND_ITEMS = TagKey.create(Registries.ITEM, RPGInventory.identifier("hand_items"));
	public static final TagKey<Item> OFFHAND_ITEMS = TagKey.create(Registries.ITEM, RPGInventory.identifier("offhand_items"));
	public static final TagKey<Item> EMPTY_HAND_WEAPONS = TagKey.create(Registries.ITEM, RPGInventory.identifier("empty_hand_weapons"));
	public static final TagKey<Item> HELMETS = TagKey.create(Registries.ITEM, RPGInventory.identifier("helmets"));
	public static final TagKey<Item> SHOULDERS = TagKey.create(Registries.ITEM, RPGInventory.identifier("shoulders"));
	public static final TagKey<Item> CHEST_PLATES = TagKey.create(Registries.ITEM, RPGInventory.identifier("chest_plates"));
	public static final TagKey<Item> BELTS = TagKey.create(Registries.ITEM, RPGInventory.identifier("belts"));
	public static final TagKey<Item> LEGGINGS = TagKey.create(Registries.ITEM, RPGInventory.identifier("leggings"));
	public static final TagKey<Item> NECKLACES = TagKey.create(Registries.ITEM, RPGInventory.identifier("necklaces"));
	public static final TagKey<Item> RINGS_1 = TagKey.create(Registries.ITEM, RPGInventory.identifier("rings_1"));
	public static final TagKey<Item> RINGS_2 = TagKey.create(Registries.ITEM, RPGInventory.identifier("rings_2"));
	public static final TagKey<Item> GLOVES = TagKey.create(Registries.ITEM, RPGInventory.identifier("gloves"));
	public static final TagKey<Item> BOOTS = TagKey.create(Registries.ITEM, RPGInventory.identifier("boots"));
	public static final TagKey<Item> SPELLS_1 = TagKey.create(Registries.ITEM, RPGInventory.identifier("spells_1"));
	public static final TagKey<Item> SPELLS_2 = TagKey.create(Registries.ITEM, RPGInventory.identifier("spells_2"));
	public static final TagKey<Item> SPELLS_3 = TagKey.create(Registries.ITEM, RPGInventory.identifier("spells_3"));
	public static final TagKey<Item> SPELLS_4 = TagKey.create(Registries.ITEM, RPGInventory.identifier("spells_4"));
	public static final TagKey<Item> SPELLS_5 = TagKey.create(Registries.ITEM, RPGInventory.identifier("spells_5"));
	public static final TagKey<Item> SPELLS_6 = TagKey.create(Registries.ITEM, RPGInventory.identifier("spells_6"));
	public static final TagKey<Item> SPELLS_7 = TagKey.create(Registries.ITEM, RPGInventory.identifier("spells_7"));
	public static final TagKey<Item> SPELLS_8 = TagKey.create(Registries.ITEM, RPGInventory.identifier("spells_8"));
	public static final TagKey<Item> RELICS = TagKey.create(Registries.ITEM, RPGInventory.identifier("relics"));
	public static final TagKey<Item> SACRIFICED_TO_KEEP_INVENTORY_ON_DEATH = TagKey.create(Registries.ITEM, RPGInventory.identifier("sacrificed_to_keep_inventory_on_death"));
	public static final TagKey<Item> UNUSABLE_WHEN_LOW_DURABILITY = TagKey.create(Registries.ITEM, RPGInventory.identifier("unusable_when_low_durability"));
	public static final TagKey<Item> NOT_SHOWN_WHEN_IN_SHEATHED_LEFT_HAND = TagKey.create(Registries.ITEM, RPGInventory.identifier("not_shown_when_in_sheathed_left_hand"));
	public static final TagKey<Item> NOT_SHOWN_WHEN_IN_SHEATHED_RIGHT_HAND = TagKey.create(Registries.ITEM, RPGInventory.identifier("not_shown_when_in_sheathed_right_hand"));

	public static final TagKey<MobEffect> PREVENTS_MANNEQUIN_INTERACTION = TagKey.create(Registries.MOB_EFFECT, RPGInventory.identifier("prevents_mannequin_interaction"));
	public static final TagKey<MobEffect> PREVENTS_MANNEQUIN_SLOT_INTERACTION = TagKey.create(Registries.MOB_EFFECT, RPGInventory.identifier("prevents_mannequin_slot_interaction"));
	public static final TagKey<MobEffect> FOOD_EFFECTS = TagKey.create(Registries.MOB_EFFECT, RPGInventory.identifier("food_effects"));
	public static final TagKey<MobEffect> KEPT_ON_PVP_DEATH = TagKey.create(Registries.MOB_EFFECT, RPGInventory.identifier("kept_on_pvp_death"));

	public static final TagKey<DamageType> REMOVES_PLAYER_FROM_PVP = TagKey.create(Registries.DAMAGE_TYPE, RPGInventory.identifier("removes_player_from_pvp"));
	public static final TagKey<DamageType> PREVENTS_PVP_DEATH_MESSAGE = TagKey.create(Registries.DAMAGE_TYPE, RPGInventory.identifier("prevents_pvp_death_message"));

}
