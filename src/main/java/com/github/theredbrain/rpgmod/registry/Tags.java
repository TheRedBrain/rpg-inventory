package com.github.theredbrain.rpgmod.registry;

import com.github.theredbrain.rpgmod.RPGMod;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

public class Tags {
    public static final TagKey<Item> INTERACTIVE_STONE_BLOCK_TOOLS = TagKey.of(RegistryKeys.ITEM, new Identifier(RPGMod.MOD_ID, "tools/interactive_stone_block_tools"));
    public static final TagKey<Item> INTERACTIVE_OAK_LOG_TOOLS = TagKey.of(RegistryKeys.ITEM, new Identifier(RPGMod.MOD_ID, "tools/interactive_oak_log_tools"));

    public static final TagKey<Item> ADVENTURE_HOTBAR_ITEMS = TagKey.of(RegistryKeys.ITEM, new Identifier(RPGMod.MOD_ID, "adventure_hotbar_items"));
    public static final TagKey<Item> BELT_ITEMS = TagKey.of(RegistryKeys.ITEM, new Identifier(RPGMod.MOD_ID, "belt_items"));
    public static final TagKey<Item> GLOVES_ITEMS = TagKey.of(RegistryKeys.ITEM, new Identifier(RPGMod.MOD_ID, "gloves_items"));
    public static final TagKey<Item> NECKLACE_ITEMS = TagKey.of(RegistryKeys.ITEM, new Identifier(RPGMod.MOD_ID, "necklace_items"));
    public static final TagKey<Item> RING_ITEMS = TagKey.of(RegistryKeys.ITEM, new Identifier(RPGMod.MOD_ID, "ring_items"));
    public static final TagKey<Item> SHOULDERS_ITEMS = TagKey.of(RegistryKeys.ITEM, new Identifier(RPGMod.MOD_ID, "shoulders_items"));
    public static final TagKey<Item> EXTRA_HELMET_ITEMS = TagKey.of(RegistryKeys.ITEM, new Identifier(RPGMod.MOD_ID, "shoulders_items"));
    public static final TagKey<Item> EXTRA_CHESTPLATE_ITEMS = TagKey.of(RegistryKeys.ITEM, new Identifier(RPGMod.MOD_ID, "shoulders_items"));
    public static final TagKey<Item> EXTRA_LEGGINGS_ITEMS = TagKey.of(RegistryKeys.ITEM, new Identifier(RPGMod.MOD_ID, "shoulders_items"));
    public static final TagKey<Item> EXTRA_BOOTS_ITEMS = TagKey.of(RegistryKeys.ITEM, new Identifier(RPGMod.MOD_ID, "shoulders_items"));

    public static final TagKey<Item> CHAINMAIL_ARMOR_SET = TagKey.of(RegistryKeys.ITEM, new Identifier(RPGMod.MOD_ID, "custom_chainmail_armor_set"));
    public static final TagKey<Item> DIAMOND_ARMOR_SET = TagKey.of(RegistryKeys.ITEM, new Identifier(RPGMod.MOD_ID, "custom_diamond_armor_set"));
    public static final TagKey<Item> GOLDEN_ARMOR_SET = TagKey.of(RegistryKeys.ITEM, new Identifier(RPGMod.MOD_ID, "custom_golden_armor_set"));
    public static final TagKey<Item> IRON_ARMOR_SET = TagKey.of(RegistryKeys.ITEM, new Identifier(RPGMod.MOD_ID, "custom_iron_armor_set"));
    public static final TagKey<Item> NETHERITE_ARMOR_SET = TagKey.of(RegistryKeys.ITEM, new Identifier(RPGMod.MOD_ID, "custom_netherite_armor_set"));
    public static final TagKey<Item> TURTLE_ARMOR_SET = TagKey.of(RegistryKeys.ITEM, new Identifier(RPGMod.MOD_ID, "custom_turtle_armor_set"));

    public static final TagKey<Item> EMPTY_HAND_WEAPONS = TagKey.of(RegistryKeys.ITEM, new Identifier(RPGMod.MOD_ID, "empty_hand_weapons"));
}
