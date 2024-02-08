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
    public static final TagKey<DamageType> IS_VANILLA = TagKey.of(RegistryKeys.DAMAGE_TYPE, BetterAdventureMode.identifier("is_vanilla"));
    public static final TagKey<DamageType> APPLIES_POISONED = TagKey.of(RegistryKeys.DAMAGE_TYPE, BetterAdventureMode.identifier("applies_poisoned"));
    public static final TagKey<DamageType> HAS_APPLIES_POISONED_DIVISION_OF_0_5 = TagKey.of(RegistryKeys.DAMAGE_TYPE, BetterAdventureMode.identifier("has_applies_poisoned_division_of_0_5"));
    public static final TagKey<DamageType> APPLIES_BURNING = TagKey.of(RegistryKeys.DAMAGE_TYPE, BetterAdventureMode.identifier("applies_burning"));
    public static final TagKey<DamageType> HAS_APPLIES_BURNING_DIVISION_OF_0_5 = TagKey.of(RegistryKeys.DAMAGE_TYPE, BetterAdventureMode.identifier("has_applies_burning_division_of_0_5"));
    public static final TagKey<DamageType> IS_BURNING = TagKey.of(RegistryKeys.DAMAGE_TYPE, BetterAdventureMode.identifier("is_burning"));
    public static final TagKey<DamageType> HAS_BURNING_DIVISION_OF_0_5 = TagKey.of(RegistryKeys.DAMAGE_TYPE, BetterAdventureMode.identifier("has_burning_division_of_0_5"));
    public static final TagKey<DamageType> APPLIES_CHILLED_AND_FREEZING = TagKey.of(RegistryKeys.DAMAGE_TYPE, BetterAdventureMode.identifier("applies_chilled_and_freezing"));
    public static final TagKey<DamageType> HAS_APPLIES_CHILLED_AND_FREEZING_DIVISION_OF_0_5 = TagKey.of(RegistryKeys.DAMAGE_TYPE, BetterAdventureMode.identifier("has_applies_chilled_and_freezing_division_of_0_5"));
    public static final TagKey<DamageType> IS_LIGHTNING = TagKey.of(RegistryKeys.DAMAGE_TYPE, BetterAdventureMode.identifier("is_lightning"));
    public static final TagKey<DamageType> HAS_LIGHTNING_DIVISION_OF_0_5 = TagKey.of(RegistryKeys.DAMAGE_TYPE, BetterAdventureMode.identifier("has_lightning_division_of_0_5"));
    public static final TagKey<DamageType> IS_BASHING = TagKey.of(RegistryKeys.DAMAGE_TYPE, BetterAdventureMode.identifier("is_bashing"));
    public static final TagKey<DamageType> HAS_BASHING_DIVISION_OF_0_5 = TagKey.of(RegistryKeys.DAMAGE_TYPE, BetterAdventureMode.identifier("has_bashing_division_of_0_5"));
    public static final TagKey<DamageType> IS_PIERCING = TagKey.of(RegistryKeys.DAMAGE_TYPE, BetterAdventureMode.identifier("is_piercing"));
    public static final TagKey<DamageType> HAS_PIERCING_DIVISION_OF_0_5 = TagKey.of(RegistryKeys.DAMAGE_TYPE, BetterAdventureMode.identifier("has_piercing_division_of_0_5"));
    public static final TagKey<DamageType> IS_SLASHING = TagKey.of(RegistryKeys.DAMAGE_TYPE, BetterAdventureMode.identifier("is_slashing"));
    public static final TagKey<DamageType> HAS_SLASHING_DIVISION_OF_0_5 = TagKey.of(RegistryKeys.DAMAGE_TYPE, BetterAdventureMode.identifier("has_slashing_division_of_0_5"));
    public static final TagKey<DamageType> APPLIES_BLEEDING = TagKey.of(RegistryKeys.DAMAGE_TYPE, BetterAdventureMode.identifier("applies_bleeding"));
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
    public static final TagKey<Item> HELMETS = TagKey.of(RegistryKeys.ITEM, BetterAdventureMode.identifier("helmets"));
    public static final TagKey<Item> SHOULDERS = TagKey.of(RegistryKeys.ITEM, BetterAdventureMode.identifier("shoulders"));
    public static final TagKey<Item> CHEST_PLATES = TagKey.of(RegistryKeys.ITEM, BetterAdventureMode.identifier("chest_plates"));
    public static final TagKey<Item> BELTS = TagKey.of(RegistryKeys.ITEM, BetterAdventureMode.identifier("belts"));
    public static final TagKey<Item> LEGGINGS = TagKey.of(RegistryKeys.ITEM, BetterAdventureMode.identifier("leggings"));
    public static final TagKey<Item> NECKLACES = TagKey.of(RegistryKeys.ITEM, BetterAdventureMode.identifier("necklaces"));
    public static final TagKey<Item> RINGS = TagKey.of(RegistryKeys.ITEM, BetterAdventureMode.identifier("rings"));
    public static final TagKey<Item> GLOVES = TagKey.of(RegistryKeys.ITEM, BetterAdventureMode.identifier("gloves"));
    public static final TagKey<Item> BOOTS = TagKey.of(RegistryKeys.ITEM, BetterAdventureMode.identifier("boots"));
    public static final TagKey<Item> SPELLS = TagKey.of(RegistryKeys.ITEM, BetterAdventureMode.identifier("spells"));
    public static final TagKey<Item> MANA_REGENERATING_TRINKETS = TagKey.of(RegistryKeys.ITEM, BetterAdventureMode.identifier("mana_regenerating_trinkets"));
    public static final TagKey<Item> DEPTH_STRIDER_TRINKETS = TagKey.of(RegistryKeys.ITEM, BetterAdventureMode.identifier("depth_strider_trinkets"));
    public static final TagKey<Item> LOOTING_TRINKETS = TagKey.of(RegistryKeys.ITEM, BetterAdventureMode.identifier("looting_trinkets"));
    public static final TagKey<Item> UNBREAKING_TRINKETS = TagKey.of(RegistryKeys.ITEM, BetterAdventureMode.identifier("unbreaking_trinkets"));
    public static final TagKey<Item> FEATHER_FALLING_TRINKETS = TagKey.of(RegistryKeys.ITEM, BetterAdventureMode.identifier("feather_falling_trinkets"));
    public static final TagKey<Item> TELEPORT_HOME_NECKLACES = TagKey.of(RegistryKeys.ITEM, BetterAdventureMode.identifier("teleport_home_necklaces"));
    //endregion ItemTags
}
