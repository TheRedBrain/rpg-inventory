package com.github.theredbrain.betteradventuremode.registry;

import com.github.theredbrain.betteradventuremode.BetterAdventureMode;
import net.minecraft.block.Block;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;

public class Tags {
    //region BlockTags
    public static final TagKey<Block> PROVIDES_CRAFTING_BENCH_LEVEL = TagKey.of(RegistryKeys.BLOCK, BetterAdventureMode.identifier("provides_crafting_bench_level"));
    public static final TagKey<Block> PROVIDES_SMITHY_LEVEL = TagKey.of(RegistryKeys.BLOCK, BetterAdventureMode.identifier("provides_smithy_level"));
    //endregion BlockTags
    //region DamageTypeTags
    public static final TagKey<DamageType> STAGGERS = TagKey.of(RegistryKeys.DAMAGE_TYPE, BetterAdventureMode.identifier("staggers"));
    public static final TagKey<DamageType> HAS_STAGGER_MULTIPLIER_OF_0_5 = TagKey.of(RegistryKeys.DAMAGE_TYPE, BetterAdventureMode.identifier("has_stagger_multiplier_of_0_5"));
    //endregion DamageTypeTags
    //region ItemTags
    public static final TagKey<Item> INTERACTIVE_STONE_BLOCK_TOOLS = TagKey.of(RegistryKeys.ITEM, BetterAdventureMode.identifier("tools/interactive_stone_block_tools"));
    public static final TagKey<Item> INTERACTIVE_OAK_LOG_TOOLS = TagKey.of(RegistryKeys.ITEM, BetterAdventureMode.identifier("tools/interactive_oak_log_tools"));
    public static final TagKey<Item> ADVENTURE_HOTBAR_ITEMS = TagKey.of(RegistryKeys.ITEM, BetterAdventureMode.identifier("adventure_hotbar_items"));
    public static final TagKey<Item> ATTACK_ITEMS = TagKey.of(RegistryKeys.ITEM, BetterAdventureMode.identifier("attack_items"));
    public static final TagKey<Item> TWO_HANDED_ITEMS = TagKey.of(RegistryKeys.ITEM, BetterAdventureMode.identifier("two_handed_items"));
    public static final TagKey<Item> NON_TWO_HANDED_ITEMS = TagKey.of(RegistryKeys.ITEM, BetterAdventureMode.identifier("non_two_handed_items"));
    public static final TagKey<Item> MAIN_HAND_ITEMS = TagKey.of(RegistryKeys.ITEM, BetterAdventureMode.identifier("main_hand_items"));
    public static final TagKey<Item> OFF_HAND_ITEMS = TagKey.of(RegistryKeys.ITEM, BetterAdventureMode.identifier("off_hand_items"));
    public static final TagKey<Item> EMPTY_HAND_WEAPONS = TagKey.of(RegistryKeys.ITEM, BetterAdventureMode.identifier("empty_hand_weapons"));
    public static final TagKey<Item> MANA_REGENERATING_TRINKETS = TagKey.of(RegistryKeys.ITEM, BetterAdventureMode.identifier("mana_regenerating_trinkets"));
    public static final TagKey<Item> DEPTH_STRIDER_TRINKETS = TagKey.of(RegistryKeys.ITEM, BetterAdventureMode.identifier("depth_strider_trinkets"));
    public static final TagKey<Item> LOOTING_TRINKETS = TagKey.of(RegistryKeys.ITEM, BetterAdventureMode.identifier("looting_trinkets"));
    public static final TagKey<Item> UNBREAKING_TRINKETS = TagKey.of(RegistryKeys.ITEM, BetterAdventureMode.identifier("unbreaking_trinkets"));
    public static final TagKey<Item> FEATHER_FALLING_TRINKETS = TagKey.of(RegistryKeys.ITEM, BetterAdventureMode.identifier("feather_falling_trinkets"));
    public static final TagKey<Item> TELEPORT_HOME_NECKLACES = TagKey.of(RegistryKeys.ITEM, BetterAdventureMode.identifier("teleport_home_necklaces"));
    //endregion ItemTags
}
