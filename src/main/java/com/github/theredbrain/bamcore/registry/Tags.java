package com.github.theredbrain.bamcore.registry;

import com.github.theredbrain.bamcore.BetterAdventureModCore;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;

public class Tags {
    public static final TagKey<Item> ADVENTURE_HOTBAR_ITEMS = TagKey.of(RegistryKeys.ITEM, BetterAdventureModCore.identifier("adventure_hotbar_items"));
    public static final TagKey<Item> ATTACK_ITEMS = TagKey.of(RegistryKeys.ITEM, BetterAdventureModCore.identifier("attack_items"));
    public static final TagKey<Item> TWO_HANDED_ITEMS = TagKey.of(RegistryKeys.ITEM, BetterAdventureModCore.identifier("two_handed_items"));
    public static final TagKey<Item> MAIN_HAND_ITEMS = TagKey.of(RegistryKeys.ITEM, BetterAdventureModCore.identifier("main_hand_items"));
    public static final TagKey<Item> OFF_HAND_ITEMS = TagKey.of(RegistryKeys.ITEM, BetterAdventureModCore.identifier("off_hand_items"));
    public static final TagKey<Item> EXTRA_HELMET_ITEMS = TagKey.of(RegistryKeys.ITEM, BetterAdventureModCore.identifier("extra_helmet_items"));
    public static final TagKey<Item> EXTRA_CHESTPLATE_ITEMS = TagKey.of(RegistryKeys.ITEM, BetterAdventureModCore.identifier("extra_chestplate_items"));
    public static final TagKey<Item> EXTRA_LEGGINGS_ITEMS = TagKey.of(RegistryKeys.ITEM, BetterAdventureModCore.identifier("extra_leggings_items"));
    public static final TagKey<Item> EXTRA_BOOTS_ITEMS = TagKey.of(RegistryKeys.ITEM, BetterAdventureModCore.identifier("extra_boots_items"));
    public static final TagKey<Item> EMPTY_HAND_WEAPONS = TagKey.of(RegistryKeys.ITEM, BetterAdventureModCore.identifier("empty_hand_weapons"));
}
