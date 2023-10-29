package com.github.theredbrain.bamcore.registry;

import com.github.theredbrain.bamcore.BetterAdventureModeCore;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;

public class Tags {
    //region ItemTags
    public static final TagKey<Item> INTERACTIVE_STONE_BLOCK_TOOLS = TagKey.of(RegistryKeys.ITEM, BetterAdventureModeCore.identifier("tools/interactive_stone_block_tools"));
    public static final TagKey<Item> INTERACTIVE_OAK_LOG_TOOLS = TagKey.of(RegistryKeys.ITEM, BetterAdventureModeCore.identifier("tools/interactive_oak_log_tools"));
    public static final TagKey<Item> ADVENTURE_HOTBAR_ITEMS = TagKey.of(RegistryKeys.ITEM, BetterAdventureModeCore.identifier("adventure_hotbar_items"));
    public static final TagKey<Item> ATTACK_ITEMS = TagKey.of(RegistryKeys.ITEM, BetterAdventureModeCore.identifier("attack_items"));
    public static final TagKey<Item> TWO_HANDED_ITEMS = TagKey.of(RegistryKeys.ITEM, BetterAdventureModeCore.identifier("two_handed_items"));
    public static final TagKey<Item> NON_TWO_HANDED_ITEMS = TagKey.of(RegistryKeys.ITEM, BetterAdventureModeCore.identifier("non_two_handed_items"));
    public static final TagKey<Item> MAIN_HAND_ITEMS = TagKey.of(RegistryKeys.ITEM, BetterAdventureModeCore.identifier("main_hand_items"));
    public static final TagKey<Item> OFF_HAND_ITEMS = TagKey.of(RegistryKeys.ITEM, BetterAdventureModeCore.identifier("off_hand_items"));
    public static final TagKey<Item> EMPTY_HAND_WEAPONS = TagKey.of(RegistryKeys.ITEM, BetterAdventureModeCore.identifier("empty_hand_weapons"));
    public static final TagKey<Item> MANA_REGENERATING_TRINKETS = TagKey.of(RegistryKeys.ITEM, BetterAdventureModeCore.identifier("mana_regenerating_trinkets"));
    public static final TagKey<Item> DEPTH_STRIDER_TRINKETS = TagKey.of(RegistryKeys.ITEM, BetterAdventureModeCore.identifier("depth_strider_trinkets"));
    public static final TagKey<Item> LOOTING_TRINKETS = TagKey.of(RegistryKeys.ITEM, BetterAdventureModeCore.identifier("looting_trinkets"));
    public static final TagKey<Item> UNBREAKING_TRINKETS = TagKey.of(RegistryKeys.ITEM, BetterAdventureModeCore.identifier("unbreaking_trinkets"));
    public static final TagKey<Item> FEATHER_FALLING_TRINKETS = TagKey.of(RegistryKeys.ITEM, BetterAdventureModeCore.identifier("feather_falling_trinkets"));
    public static final TagKey<Item> TELEPORT_HOME_NECKLACES = TagKey.of(RegistryKeys.ITEM, BetterAdventureModeCore.identifier("teleport_home_necklaces"));
    //endregion ItemTags
    //region DamageTypeTags
    public static final TagKey<DamageType> STAGGERS = TagKey.of(RegistryKeys.DAMAGE_TYPE, BetterAdventureModeCore.identifier("staggers"));
    public static final TagKey<DamageType> HAS_STAGGER_MULTIPLIER_OF_0_5 = TagKey.of(RegistryKeys.DAMAGE_TYPE, BetterAdventureModeCore.identifier("has_stagger_multiplier_of_0_5"));
    //endregion DamageTypeTags
}
