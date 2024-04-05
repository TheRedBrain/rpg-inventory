package com.github.theredbrain.betteradventuremode.registry;

import com.github.theredbrain.betteradventuremode.BetterAdventureMode;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;

public class Tags {
    //region EntityTags
    public static final TagKey<EntityType<?>> ATTACKS_WITH_BASHING = TagKey.of(RegistryKeys.ENTITY_TYPE, BetterAdventureMode.identifier("attacks_with_bashing"));
    public static final TagKey<EntityType<?>> ATTACKS_WITH_PIERCING = TagKey.of(RegistryKeys.ENTITY_TYPE, BetterAdventureMode.identifier("attacks_with_piercing"));
    public static final TagKey<EntityType<?>> ATTACKS_WITH_SLASHING = TagKey.of(RegistryKeys.ENTITY_TYPE, BetterAdventureMode.identifier("attacks_with_slashing"));
    //endregion EntityTags
    //region BlockTags
    public static final TagKey<Block> PROVIDES_CRAFTING_TAB_0_LEVEL = TagKey.of(RegistryKeys.BLOCK, BetterAdventureMode.identifier("provides_crafting_tab_0_level"));
    public static final TagKey<Block> PROVIDES_CRAFTING_TAB_1_LEVEL = TagKey.of(RegistryKeys.BLOCK, BetterAdventureMode.identifier("provides_crafting_tab_1_level"));
    public static final TagKey<Block> PROVIDES_CRAFTING_TAB_2_LEVEL = TagKey.of(RegistryKeys.BLOCK, BetterAdventureMode.identifier("provides_crafting_tab_2_level"));
    public static final TagKey<Block> PROVIDES_CRAFTING_TAB_3_LEVEL = TagKey.of(RegistryKeys.BLOCK, BetterAdventureMode.identifier("provides_crafting_tab_3_level"));
    public static final TagKey<Block> PROVIDES_STORAGE_AREA_0 = TagKey.of(RegistryKeys.BLOCK, BetterAdventureMode.identifier("provides_storage_area_0"));
    public static final TagKey<Block> PROVIDES_STORAGE_AREA_1 = TagKey.of(RegistryKeys.BLOCK, BetterAdventureMode.identifier("provides_storage_area_1"));
    public static final TagKey<Block> PROVIDES_STORAGE_AREA_2 = TagKey.of(RegistryKeys.BLOCK, BetterAdventureMode.identifier("provides_storage_area_2"));
    public static final TagKey<Block> PROVIDES_STORAGE_AREA_3 = TagKey.of(RegistryKeys.BLOCK, BetterAdventureMode.identifier("provides_storage_area_3"));
    public static final TagKey<Block> PROVIDES_STORAGE_AREA_4 = TagKey.of(RegistryKeys.BLOCK, BetterAdventureMode.identifier("provides_storage_area_4"));
    public static final TagKey<Block> SLOWS_DOWN_ENTITIES_INSIDE = TagKey.of(RegistryKeys.BLOCK, BetterAdventureMode.identifier("slows_down_entities_inside"));
    //endregion BlockTags
    //region DamageTypeTags
    public static final TagKey<DamageType> IS_VANILLA = TagKey.of(RegistryKeys.DAMAGE_TYPE, BetterAdventureMode.identifier("is_vanilla"));
    public static final TagKey<DamageType> IS_TRUE_DAMAGE = TagKey.of(RegistryKeys.DAMAGE_TYPE, BetterAdventureMode.identifier("is_true_damage"));

    public static final TagKey<DamageType> HAS_BASHING_DIVISION_OF_0_1 = TagKey.of(RegistryKeys.DAMAGE_TYPE, BetterAdventureMode.identifier("has_bashing_division_of_0_1"));
    public static final TagKey<DamageType> HAS_BASHING_DIVISION_OF_0_2 = TagKey.of(RegistryKeys.DAMAGE_TYPE, BetterAdventureMode.identifier("has_bashing_division_of_0_2"));
    public static final TagKey<DamageType> HAS_BASHING_DIVISION_OF_0_3 = TagKey.of(RegistryKeys.DAMAGE_TYPE, BetterAdventureMode.identifier("has_bashing_division_of_0_3"));
    public static final TagKey<DamageType> HAS_BASHING_DIVISION_OF_0_4 = TagKey.of(RegistryKeys.DAMAGE_TYPE, BetterAdventureMode.identifier("has_bashing_division_of_0_4"));
    public static final TagKey<DamageType> HAS_BASHING_DIVISION_OF_0_5 = TagKey.of(RegistryKeys.DAMAGE_TYPE, BetterAdventureMode.identifier("has_bashing_division_of_0_5"));
    public static final TagKey<DamageType> HAS_BASHING_DIVISION_OF_0_6 = TagKey.of(RegistryKeys.DAMAGE_TYPE, BetterAdventureMode.identifier("has_bashing_division_of_0_6"));
    public static final TagKey<DamageType> HAS_BASHING_DIVISION_OF_0_7 = TagKey.of(RegistryKeys.DAMAGE_TYPE, BetterAdventureMode.identifier("has_bashing_division_of_0_7"));
    public static final TagKey<DamageType> HAS_BASHING_DIVISION_OF_0_8 = TagKey.of(RegistryKeys.DAMAGE_TYPE, BetterAdventureMode.identifier("has_bashing_division_of_0_8"));
    public static final TagKey<DamageType> HAS_BASHING_DIVISION_OF_0_9 = TagKey.of(RegistryKeys.DAMAGE_TYPE, BetterAdventureMode.identifier("has_bashing_division_of_0_9"));
    public static final TagKey<DamageType> HAS_BASHING_DIVISION_OF_1 = TagKey.of(RegistryKeys.DAMAGE_TYPE, BetterAdventureMode.identifier("has_bashing_division_of_1"));
    
    public static final TagKey<DamageType> HAS_PIERCING_DIVISION_OF_0_1 = TagKey.of(RegistryKeys.DAMAGE_TYPE, BetterAdventureMode.identifier("has_piercing_division_of_0_1"));
    public static final TagKey<DamageType> HAS_PIERCING_DIVISION_OF_0_2 = TagKey.of(RegistryKeys.DAMAGE_TYPE, BetterAdventureMode.identifier("has_piercing_division_of_0_2"));
    public static final TagKey<DamageType> HAS_PIERCING_DIVISION_OF_0_3 = TagKey.of(RegistryKeys.DAMAGE_TYPE, BetterAdventureMode.identifier("has_piercing_division_of_0_3"));
    public static final TagKey<DamageType> HAS_PIERCING_DIVISION_OF_0_4 = TagKey.of(RegistryKeys.DAMAGE_TYPE, BetterAdventureMode.identifier("has_piercing_division_of_0_4"));
    public static final TagKey<DamageType> HAS_PIERCING_DIVISION_OF_0_5 = TagKey.of(RegistryKeys.DAMAGE_TYPE, BetterAdventureMode.identifier("has_piercing_division_of_0_5"));
    public static final TagKey<DamageType> HAS_PIERCING_DIVISION_OF_0_6 = TagKey.of(RegistryKeys.DAMAGE_TYPE, BetterAdventureMode.identifier("has_piercing_division_of_0_6"));
    public static final TagKey<DamageType> HAS_PIERCING_DIVISION_OF_0_7 = TagKey.of(RegistryKeys.DAMAGE_TYPE, BetterAdventureMode.identifier("has_piercing_division_of_0_7"));
    public static final TagKey<DamageType> HAS_PIERCING_DIVISION_OF_0_8 = TagKey.of(RegistryKeys.DAMAGE_TYPE, BetterAdventureMode.identifier("has_piercing_division_of_0_8"));
    public static final TagKey<DamageType> HAS_PIERCING_DIVISION_OF_0_9 = TagKey.of(RegistryKeys.DAMAGE_TYPE, BetterAdventureMode.identifier("has_piercing_division_of_0_9"));
    public static final TagKey<DamageType> HAS_PIERCING_DIVISION_OF_1 = TagKey.of(RegistryKeys.DAMAGE_TYPE, BetterAdventureMode.identifier("has_piercing_division_of_1"));
    
    public static final TagKey<DamageType> HAS_SLASHING_DIVISION_OF_0_1 = TagKey.of(RegistryKeys.DAMAGE_TYPE, BetterAdventureMode.identifier("has_slashing_division_of_0_1"));
    public static final TagKey<DamageType> HAS_SLASHING_DIVISION_OF_0_2 = TagKey.of(RegistryKeys.DAMAGE_TYPE, BetterAdventureMode.identifier("has_slashing_division_of_0_2"));
    public static final TagKey<DamageType> HAS_SLASHING_DIVISION_OF_0_3 = TagKey.of(RegistryKeys.DAMAGE_TYPE, BetterAdventureMode.identifier("has_slashing_division_of_0_3"));
    public static final TagKey<DamageType> HAS_SLASHING_DIVISION_OF_0_4 = TagKey.of(RegistryKeys.DAMAGE_TYPE, BetterAdventureMode.identifier("has_slashing_division_of_0_4"));
    public static final TagKey<DamageType> HAS_SLASHING_DIVISION_OF_0_5 = TagKey.of(RegistryKeys.DAMAGE_TYPE, BetterAdventureMode.identifier("has_slashing_division_of_0_5"));
    public static final TagKey<DamageType> HAS_SLASHING_DIVISION_OF_0_6 = TagKey.of(RegistryKeys.DAMAGE_TYPE, BetterAdventureMode.identifier("has_slashing_division_of_0_6"));
    public static final TagKey<DamageType> HAS_SLASHING_DIVISION_OF_0_7 = TagKey.of(RegistryKeys.DAMAGE_TYPE, BetterAdventureMode.identifier("has_slashing_division_of_0_7"));
    public static final TagKey<DamageType> HAS_SLASHING_DIVISION_OF_0_8 = TagKey.of(RegistryKeys.DAMAGE_TYPE, BetterAdventureMode.identifier("has_slashing_division_of_0_8"));
    public static final TagKey<DamageType> HAS_SLASHING_DIVISION_OF_0_9 = TagKey.of(RegistryKeys.DAMAGE_TYPE, BetterAdventureMode.identifier("has_slashing_division_of_0_9"));
    public static final TagKey<DamageType> HAS_SLASHING_DIVISION_OF_1 = TagKey.of(RegistryKeys.DAMAGE_TYPE, BetterAdventureMode.identifier("has_slashing_division_of_1"));
    
    public static final TagKey<DamageType> APPLIES_BLEEDING = TagKey.of(RegistryKeys.DAMAGE_TYPE, BetterAdventureMode.identifier("applies_bleeding"));

    public static final TagKey<DamageType> HAS_POISON_DIVISION_OF_0_1 = TagKey.of(RegistryKeys.DAMAGE_TYPE, BetterAdventureMode.identifier("has_poison_division_of_0_1"));
    public static final TagKey<DamageType> HAS_POISON_DIVISION_OF_0_2 = TagKey.of(RegistryKeys.DAMAGE_TYPE, BetterAdventureMode.identifier("has_poison_division_of_0_2"));
    public static final TagKey<DamageType> HAS_POISON_DIVISION_OF_0_3 = TagKey.of(RegistryKeys.DAMAGE_TYPE, BetterAdventureMode.identifier("has_poison_division_of_0_3"));
    public static final TagKey<DamageType> HAS_POISON_DIVISION_OF_0_4 = TagKey.of(RegistryKeys.DAMAGE_TYPE, BetterAdventureMode.identifier("has_poison_division_of_0_4"));
    public static final TagKey<DamageType> HAS_POISON_DIVISION_OF_0_5 = TagKey.of(RegistryKeys.DAMAGE_TYPE, BetterAdventureMode.identifier("has_poison_division_of_0_5"));
    public static final TagKey<DamageType> HAS_POISON_DIVISION_OF_0_6 = TagKey.of(RegistryKeys.DAMAGE_TYPE, BetterAdventureMode.identifier("has_poison_division_of_0_6"));
    public static final TagKey<DamageType> HAS_POISON_DIVISION_OF_0_7 = TagKey.of(RegistryKeys.DAMAGE_TYPE, BetterAdventureMode.identifier("has_poison_division_of_0_7"));
    public static final TagKey<DamageType> HAS_POISON_DIVISION_OF_0_8 = TagKey.of(RegistryKeys.DAMAGE_TYPE, BetterAdventureMode.identifier("has_poison_division_of_0_8"));
    public static final TagKey<DamageType> HAS_POISON_DIVISION_OF_0_9 = TagKey.of(RegistryKeys.DAMAGE_TYPE, BetterAdventureMode.identifier("has_poison_division_of_0_9"));
    public static final TagKey<DamageType> HAS_POISON_DIVISION_OF_1 = TagKey.of(RegistryKeys.DAMAGE_TYPE, BetterAdventureMode.identifier("has_poison_division_of_1"));

    public static final TagKey<DamageType> HAS_FIRE_DIVISION_OF_0_1 = TagKey.of(RegistryKeys.DAMAGE_TYPE, BetterAdventureMode.identifier("has_fire_division_of_0_1"));
    public static final TagKey<DamageType> HAS_FIRE_DIVISION_OF_0_2 = TagKey.of(RegistryKeys.DAMAGE_TYPE, BetterAdventureMode.identifier("has_fire_division_of_0_2"));
    public static final TagKey<DamageType> HAS_FIRE_DIVISION_OF_0_3 = TagKey.of(RegistryKeys.DAMAGE_TYPE, BetterAdventureMode.identifier("has_fire_division_of_0_3"));
    public static final TagKey<DamageType> HAS_FIRE_DIVISION_OF_0_4 = TagKey.of(RegistryKeys.DAMAGE_TYPE, BetterAdventureMode.identifier("has_fire_division_of_0_4"));
    public static final TagKey<DamageType> HAS_FIRE_DIVISION_OF_0_5 = TagKey.of(RegistryKeys.DAMAGE_TYPE, BetterAdventureMode.identifier("has_fire_division_of_0_5"));
    public static final TagKey<DamageType> HAS_FIRE_DIVISION_OF_0_6 = TagKey.of(RegistryKeys.DAMAGE_TYPE, BetterAdventureMode.identifier("has_fire_division_of_0_6"));
    public static final TagKey<DamageType> HAS_FIRE_DIVISION_OF_0_7 = TagKey.of(RegistryKeys.DAMAGE_TYPE, BetterAdventureMode.identifier("has_fire_division_of_0_7"));
    public static final TagKey<DamageType> HAS_FIRE_DIVISION_OF_0_8 = TagKey.of(RegistryKeys.DAMAGE_TYPE, BetterAdventureMode.identifier("has_fire_division_of_0_8"));
    public static final TagKey<DamageType> HAS_FIRE_DIVISION_OF_0_9 = TagKey.of(RegistryKeys.DAMAGE_TYPE, BetterAdventureMode.identifier("has_fire_division_of_0_9"));
    public static final TagKey<DamageType> HAS_FIRE_DIVISION_OF_1 = TagKey.of(RegistryKeys.DAMAGE_TYPE, BetterAdventureMode.identifier("has_fire_division_of_1"));

    public static final TagKey<DamageType> HAS_FROST_DIVISION_OF_0_1 = TagKey.of(RegistryKeys.DAMAGE_TYPE, BetterAdventureMode.identifier("has_frost_division_of_0_1"));
    public static final TagKey<DamageType> HAS_FROST_DIVISION_OF_0_2 = TagKey.of(RegistryKeys.DAMAGE_TYPE, BetterAdventureMode.identifier("has_frost_division_of_0_2"));
    public static final TagKey<DamageType> HAS_FROST_DIVISION_OF_0_3 = TagKey.of(RegistryKeys.DAMAGE_TYPE, BetterAdventureMode.identifier("has_frost_division_of_0_3"));
    public static final TagKey<DamageType> HAS_FROST_DIVISION_OF_0_4 = TagKey.of(RegistryKeys.DAMAGE_TYPE, BetterAdventureMode.identifier("has_frost_division_of_0_4"));
    public static final TagKey<DamageType> HAS_FROST_DIVISION_OF_0_5 = TagKey.of(RegistryKeys.DAMAGE_TYPE, BetterAdventureMode.identifier("has_frost_division_of_0_5"));
    public static final TagKey<DamageType> HAS_FROST_DIVISION_OF_0_6 = TagKey.of(RegistryKeys.DAMAGE_TYPE, BetterAdventureMode.identifier("has_frost_division_of_0_6"));
    public static final TagKey<DamageType> HAS_FROST_DIVISION_OF_0_7 = TagKey.of(RegistryKeys.DAMAGE_TYPE, BetterAdventureMode.identifier("has_frost_division_of_0_7"));
    public static final TagKey<DamageType> HAS_FROST_DIVISION_OF_0_8 = TagKey.of(RegistryKeys.DAMAGE_TYPE, BetterAdventureMode.identifier("has_frost_division_of_0_8"));
    public static final TagKey<DamageType> HAS_FROST_DIVISION_OF_0_9 = TagKey.of(RegistryKeys.DAMAGE_TYPE, BetterAdventureMode.identifier("has_frost_division_of_0_9"));
    public static final TagKey<DamageType> HAS_FROST_DIVISION_OF_1 = TagKey.of(RegistryKeys.DAMAGE_TYPE, BetterAdventureMode.identifier("has_frost_division_of_1"));

    public static final TagKey<DamageType> HAS_LIGHTNING_DIVISION_OF_0_1 = TagKey.of(RegistryKeys.DAMAGE_TYPE, BetterAdventureMode.identifier("has_lightning_division_of_0_1"));
    public static final TagKey<DamageType> HAS_LIGHTNING_DIVISION_OF_0_2 = TagKey.of(RegistryKeys.DAMAGE_TYPE, BetterAdventureMode.identifier("has_lightning_division_of_0_2"));
    public static final TagKey<DamageType> HAS_LIGHTNING_DIVISION_OF_0_3 = TagKey.of(RegistryKeys.DAMAGE_TYPE, BetterAdventureMode.identifier("has_lightning_division_of_0_3"));
    public static final TagKey<DamageType> HAS_LIGHTNING_DIVISION_OF_0_4 = TagKey.of(RegistryKeys.DAMAGE_TYPE, BetterAdventureMode.identifier("has_lightning_division_of_0_4"));
    public static final TagKey<DamageType> HAS_LIGHTNING_DIVISION_OF_0_5 = TagKey.of(RegistryKeys.DAMAGE_TYPE, BetterAdventureMode.identifier("has_lightning_division_of_0_5"));
    public static final TagKey<DamageType> HAS_LIGHTNING_DIVISION_OF_0_6 = TagKey.of(RegistryKeys.DAMAGE_TYPE, BetterAdventureMode.identifier("has_lightning_division_of_0_6"));
    public static final TagKey<DamageType> HAS_LIGHTNING_DIVISION_OF_0_7 = TagKey.of(RegistryKeys.DAMAGE_TYPE, BetterAdventureMode.identifier("has_lightning_division_of_0_7"));
    public static final TagKey<DamageType> HAS_LIGHTNING_DIVISION_OF_0_8 = TagKey.of(RegistryKeys.DAMAGE_TYPE, BetterAdventureMode.identifier("has_lightning_division_of_0_8"));
    public static final TagKey<DamageType> HAS_LIGHTNING_DIVISION_OF_0_9 = TagKey.of(RegistryKeys.DAMAGE_TYPE, BetterAdventureMode.identifier("has_lightning_division_of_0_9"));
    public static final TagKey<DamageType> HAS_LIGHTNING_DIVISION_OF_1 = TagKey.of(RegistryKeys.DAMAGE_TYPE, BetterAdventureMode.identifier("has_lightning_division_of_1"));

    //endregion DamageTypeTags
    //region ItemTags
    public static final TagKey<Item> INTERACTIVE_STONE_BLOCK_TOOLS = TagKey.of(RegistryKeys.ITEM, BetterAdventureMode.identifier("tools/interactive_stone_block_tools"));
    public static final TagKey<Item> INTERACTIVE_OAK_LOG_TOOLS = TagKey.of(RegistryKeys.ITEM, BetterAdventureMode.identifier("tools/interactive_oak_log_tools"));
    public static final TagKey<Item> KEYS_FOR_LOCKED_USE_RELAY_CHEST = TagKey.of(RegistryKeys.ITEM, BetterAdventureMode.identifier("keys_for_locked_use_relay_chest"));
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

    // enables functionality
    public static final TagKey<Item> ENABLES_MANA_REGENERATION = TagKey.of(RegistryKeys.ITEM, BetterAdventureMode.identifier("enables_mana_regeneration"));
    public static final TagKey<Item> ENABLES_CHANGING_PITCH = TagKey.of(RegistryKeys.ITEM, BetterAdventureMode.identifier("enables_changing_pitch"));
    public static final TagKey<Item> ENABLES_CHANGING_PITCH_ON_USING = TagKey.of(RegistryKeys.ITEM, BetterAdventureMode.identifier("enables_changing_pitch_on_using"));
    public static final TagKey<Item> ENABLES_FIRST_PERSON_PERSPECTIVE = TagKey.of(RegistryKeys.ITEM, BetterAdventureMode.identifier("enables_first_person_perspective"));
    public static final TagKey<Item> ENABLES_FIRST_PERSON_PERSPECTIVE_ON_USING = TagKey.of(RegistryKeys.ITEM, BetterAdventureMode.identifier("enables_first_person_perspective_on_using"));
    public static final TagKey<Item> ROTATE_PLAYER_ON_USING = TagKey.of(RegistryKeys.ITEM, BetterAdventureMode.identifier("rotate_player_on_using"));
    public static final TagKey<Item> IGNORES_ATTACK_MOVEMENT_PENALTY = TagKey.of(RegistryKeys.ITEM, BetterAdventureMode.identifier("ignores_attack_movement_penalty"));
    public static final TagKey<Item> KEEPS_INVENTORY_ON_DEATH = TagKey.of(RegistryKeys.ITEM, BetterAdventureMode.identifier("keeps_inventory_on_death"));
    public static final TagKey<Item> DOUBLES_INCOMING_DAMAGE = TagKey.of(RegistryKeys.ITEM, BetterAdventureMode.identifier("doubles_incoming_damage"));

    // granting enchantments
    public static final TagKey<Item> GRANTS_DEPTH_STRIDER_LEVEL_3 = TagKey.of(RegistryKeys.ITEM, BetterAdventureMode.identifier("grants_depth_strider_level_3"));
    public static final TagKey<Item> GRANTS_LOOTING_LEVEL_3 = TagKey.of(RegistryKeys.ITEM, BetterAdventureMode.identifier("grants_looting_level_3"));
    public static final TagKey<Item> GRANTS_UNBREAKING_LEVEL_3 = TagKey.of(RegistryKeys.ITEM, BetterAdventureMode.identifier("grants_unbreaking_level_3"));
    public static final TagKey<Item> GRANTS_FEATHER_FALLING_LEVEL_4 = TagKey.of(RegistryKeys.ITEM, BetterAdventureMode.identifier("grants_feather_falling_level_4"));
    public static final TagKey<Item> PREVENTS_NON_LETHAL_FALL_DAMAGE = TagKey.of(RegistryKeys.ITEM, BetterAdventureMode.identifier("prevents_non_lethal_fall_damage"));
    public static final TagKey<Item> GRANTS_SWIFT_SNEAK_LEVEL_3 = TagKey.of(RegistryKeys.ITEM, BetterAdventureMode.identifier("grants_swift_sneak_level_3"));
    public static final TagKey<Item> GRANTS_RESPIRATION_LEVEL_3 = TagKey.of(RegistryKeys.ITEM, BetterAdventureMode.identifier("grants_respiration_level_3"));

    // special necklaces
    public static final TagKey<Item> TELEPORT_HOME_NECKLACES = TagKey.of(RegistryKeys.ITEM, BetterAdventureMode.identifier("teleport_home_necklaces"));
    //endregion ItemTags
}
