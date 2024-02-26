package com.github.theredbrain.betteradventuremode.registry;

import com.github.theredbrain.betteradventuremode.BetterAdventureMode;
import com.github.theredbrain.betteradventuremode.effect.AuraStatusEffect;
import com.github.theredbrain.betteradventuremode.item.*;
import com.github.theredbrain.betteradventuremode.util.ItemUtils;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.item.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.sound.SoundEvents;
import net.spell_engine.api.item.trinket.SpellBookItem;
import net.spell_engine.api.item.weapon.StaffItem;
import net.spell_engine.api.spell.SpellContainer;
import net.spell_engine.internals.SpellRegistry;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ItemRegistry {

    // region Armor
    public static final Item LEATHER_HELMET = registerItem("leather_helmet", new DyeableArmorTrinketItem(1, 0, 1, BetterAdventureMode.identifier("model/armor/leather_armor"), new FabricItemSettings().maxDamage(60)), ItemGroupRegistry.BAM_EQUIPMENT);
    public static final Item LEATHER_CHESTPLATE = registerItem("leather_chestplate", new DyeableArmorTrinketItem(3, 0, 3, BetterAdventureMode.identifier("model/armor/leather_armor"), new FabricItemSettings().maxDamage(60)), ItemGroupRegistry.BAM_EQUIPMENT);
    public static final Item LEATHER_LEGGINGS = registerItem("leather_leggings", new DyeableArmorTrinketItem(2, 0, 2, BetterAdventureMode.identifier("model/armor/leather_armor"), new FabricItemSettings().maxDamage(60)), ItemGroupRegistry.BAM_EQUIPMENT);
    public static final Item LEATHER_BOOTS = registerItem("leather_boots", new DyeableArmorTrinketItem(1, 0, 1, BetterAdventureMode.identifier("model/armor/leather_armor"), new FabricItemSettings().maxDamage(60)), ItemGroupRegistry.BAM_EQUIPMENT);
    public static final Item LEATHER_GLOVES = registerItem("leather_gloves", new DyeableArmorTrinketItem(1, 0, 1, BetterAdventureMode.identifier("model/armor/leather_armor"), new FabricItemSettings().maxDamage(60)), ItemGroupRegistry.BAM_EQUIPMENT);
    public static final Item LEATHER_SHOULDERS = registerItem("leather_shoulders", new DyeableArmorTrinketItem(1, 0, 1, BetterAdventureMode.identifier("model/armor/leather_armor"), new FabricItemSettings().maxDamage(60)), ItemGroupRegistry.BAM_EQUIPMENT);

    public static final Item CHAINMAIL_HELMET = registerItem("chainmail_helmet", new ArmorTrinketItem(2, 0, 2, BetterAdventureMode.identifier("model/armor/chainmail_armor"), new FabricItemSettings().maxDamage(70)), ItemGroupRegistry.BAM_EQUIPMENT);
    public static final Item CHAINMAIL_CHESTPLATE = registerItem("chainmail_chestplate", new ArmorTrinketItem(5, 0, 5, BetterAdventureMode.identifier("model/armor/chainmail_armor"), new FabricItemSettings().maxDamage(70)), ItemGroupRegistry.BAM_EQUIPMENT);
    public static final Item CHAINMAIL_LEGGINGS = registerItem("chainmail_leggings", new ArmorTrinketItem(4, 0, 4, BetterAdventureMode.identifier("model/armor/chainmail_armor"), new FabricItemSettings().maxDamage(70)), ItemGroupRegistry.BAM_EQUIPMENT);
    public static final Item CHAINMAIL_BOOTS = registerItem("chainmail_boots", new ArmorTrinketItem(1, 0, 1, BetterAdventureMode.identifier("model/armor/chainmail_armor"), new FabricItemSettings().maxDamage(70)), ItemGroupRegistry.BAM_EQUIPMENT);
    public static final Item CHAINMAIL_GLOVES = registerItem("chainmail_gloves", new ArmorTrinketItem(1, 0, 1, BetterAdventureMode.identifier("model/armor/chainmail_armor"), new FabricItemSettings().maxDamage(70)), ItemGroupRegistry.BAM_EQUIPMENT);
    public static final Item CHAINMAIL_SHOULDERS = registerItem("chainmail_shoulders", new ArmorTrinketItem(2, 0, 2, BetterAdventureMode.identifier("model/armor/chainmail_armor"), new FabricItemSettings().maxDamage(70)), ItemGroupRegistry.BAM_EQUIPMENT);

    public static final Item IRON_HELMET = registerItem("iron_helmet", new ArmorTrinketItem(2, 0, 2, BetterAdventureMode.identifier("model/armor/iron_armor"), new FabricItemSettings().maxDamage(90)), ItemGroupRegistry.BAM_EQUIPMENT);
    public static final Item IRON_CHESTPLATE = registerItem("iron_chestplate", new ArmorTrinketItem(6, 0, 6, BetterAdventureMode.identifier("model/armor/iron_armor"), new FabricItemSettings().maxDamage(90)), ItemGroupRegistry.BAM_EQUIPMENT);
    public static final Item IRON_LEGGINGS = registerItem("iron_leggings", new ArmorTrinketItem(5, 0, 5, BetterAdventureMode.identifier("model/armor/iron_armor"), new FabricItemSettings().maxDamage(90)), ItemGroupRegistry.BAM_EQUIPMENT);
    public static final Item IRON_BOOTS = registerItem("iron_boots", new ArmorTrinketItem(2, 0, 2, BetterAdventureMode.identifier("model/armor/iron_armor"), new FabricItemSettings().maxDamage(90)), ItemGroupRegistry.BAM_EQUIPMENT);
    public static final Item IRON_GLOVES = registerItem("iron_gloves", new ArmorTrinketItem(2, 0, 2, BetterAdventureMode.identifier("model/armor/iron_armor"), new FabricItemSettings().maxDamage(90)), ItemGroupRegistry.BAM_EQUIPMENT);
    public static final Item IRON_SHOULDERS = registerItem("iron_shoulders", new ArmorTrinketItem(2, 0, 2, BetterAdventureMode.identifier("model/armor/iron_armor"), new FabricItemSettings().maxDamage(90)), ItemGroupRegistry.BAM_EQUIPMENT);

    public static final Item DIAMOND_HELMET = registerItem("diamond_helmet", new ArmorTrinketItem(3, 0.025, 3, BetterAdventureMode.identifier("model/armor/diamond_armor"), new FabricItemSettings().maxDamage(40)), ItemGroupRegistry.BAM_EQUIPMENT);
    public static final Item DIAMOND_CHESTPLATE = registerItem("diamond_chestplate", new ArmorTrinketItem(8, 0.025, 8, BetterAdventureMode.identifier("model/armor/diamond_armor"), new FabricItemSettings().maxDamage(40)), ItemGroupRegistry.BAM_EQUIPMENT);
    public static final Item DIAMOND_LEGGINGS = registerItem("diamond_leggings", new ArmorTrinketItem(6, 0.025, 6, BetterAdventureMode.identifier("model/armor/diamond_armor"), new FabricItemSettings().maxDamage(40)), ItemGroupRegistry.BAM_EQUIPMENT);
    public static final Item DIAMOND_BOOTS = registerItem("diamond_boots", new ArmorTrinketItem(3, 0.025, 3, BetterAdventureMode.identifier("model/armor/diamond_armor"), new FabricItemSettings().maxDamage(40)), ItemGroupRegistry.BAM_EQUIPMENT);
    public static final Item DIAMOND_GLOVES = registerItem("diamond_gloves", new ArmorTrinketItem(3, 0.025, 3, BetterAdventureMode.identifier("model/armor/diamond_armor"), new FabricItemSettings().maxDamage(40)), ItemGroupRegistry.BAM_EQUIPMENT);
    public static final Item DIAMOND_SHOULDERS = registerItem("diamond_shoulders", new ArmorTrinketItem(3, 0.025, 3, BetterAdventureMode.identifier("model/armor/diamond_armor"), new FabricItemSettings().maxDamage(40)), ItemGroupRegistry.BAM_EQUIPMENT);

    public static final Item GOLDEN_HELMET = registerItem("golden_helmet", new ArmorTrinketItem(2, 0, 2, BetterAdventureMode.identifier("model/armor/golden_armor"), new FabricItemSettings().maxDamage(50)), ItemGroupRegistry.BAM_EQUIPMENT);
    public static final Item GOLDEN_CHESTPLATE = registerItem("golden_chestplate", new ArmorTrinketItem(5, 0, 5, BetterAdventureMode.identifier("model/armor/golden_armor"), new FabricItemSettings().maxDamage(50)), ItemGroupRegistry.BAM_EQUIPMENT);
    public static final Item GOLDEN_LEGGINGS = registerItem("golden_leggings", new ArmorTrinketItem(3, 0, 3, BetterAdventureMode.identifier("model/armor/golden_armor"), new FabricItemSettings().maxDamage(50)), ItemGroupRegistry.BAM_EQUIPMENT);
    public static final Item GOLDEN_BOOTS = registerItem("golden_boots", new ArmorTrinketItem(1, 0, 1, BetterAdventureMode.identifier("model/armor/golden_armor"), new FabricItemSettings().maxDamage(50)), ItemGroupRegistry.BAM_EQUIPMENT);
    public static final Item GOLDEN_GLOVES = registerItem("golden_gloves", new ArmorTrinketItem(1, 0, 1, BetterAdventureMode.identifier("model/armor/golden_armor"), new FabricItemSettings().maxDamage(50)), ItemGroupRegistry.BAM_EQUIPMENT);
    public static final Item GOLDEN_SHOULDERS = registerItem("golden_shoulders", new ArmorTrinketItem(2, 0, 2, BetterAdventureMode.identifier("model/armor/golden_armor"), new FabricItemSettings().maxDamage(50)), ItemGroupRegistry.BAM_EQUIPMENT);

    public static final Item NETHERITE_HELMET = registerItem("netherite_helmet", new ArmorTrinketItem(3, 0.05, 3, BetterAdventureMode.identifier("model/armor/netherite_armor"), new FabricItemSettings().maxDamage(120)), ItemGroupRegistry.BAM_EQUIPMENT);
    public static final Item NETHERITE_CHESTPLATE = registerItem("netherite_chestplate", new ArmorTrinketItem(8, 0.05, 8, BetterAdventureMode.identifier("model/armor/netherite_armor"), new FabricItemSettings().maxDamage(120)), ItemGroupRegistry.BAM_EQUIPMENT);
    public static final Item NETHERITE_LEGGINGS = registerItem("netherite_leggings", new ArmorTrinketItem(6, 0.05, 6, BetterAdventureMode.identifier("model/armor/netherite_armor"), new FabricItemSettings().maxDamage(120)), ItemGroupRegistry.BAM_EQUIPMENT);
    public static final Item NETHERITE_BOOTS = registerItem("netherite_boots", new ArmorTrinketItem(3, 0.05, 3, BetterAdventureMode.identifier("model/armor/netherite_armor"), new FabricItemSettings().maxDamage(120)), ItemGroupRegistry.BAM_EQUIPMENT);
    public static final Item NETHERITE_GLOVES = registerItem("netherite_gloves", new ArmorTrinketItem(3, 0.05, 3, BetterAdventureMode.identifier("model/armor/netherite_armor"), new FabricItemSettings().maxDamage(120)), ItemGroupRegistry.BAM_EQUIPMENT);
    public static final Item NETHERITE_SHOULDERS = registerItem("netherite_shoulders", new ArmorTrinketItem(3, 0.05, 3, BetterAdventureMode.identifier("model/armor/netherite_armor"), new FabricItemSettings().maxDamage(120)), ItemGroupRegistry.BAM_EQUIPMENT);
    //endregion Armor

    //region Accessories
    // belts
    public static final Item MANA_REGENERATION_BELT = registerItem("mana_regeneration_belt", new AccessoryTrinketItem(BetterAdventureMode.identifier("model/accessory/belt_basic"), new FabricItemSettings().maxCount(1)), ItemGroupRegistry.BAM_EQUIPMENT);
    public static final Item DEPTH_STRIDER_BELT = registerItem("depth_strider_belt", new AccessoryTrinketItem(BetterAdventureMode.identifier("model/accessory/belt_basic"), new FabricItemSettings().maxCount(1)), ItemGroupRegistry.BAM_EQUIPMENT);
    public static final Item LOOTING_BELT = registerItem("looting_belt", new AccessoryTrinketItem(BetterAdventureMode.identifier("model/accessory/belt_basic"), new FabricItemSettings().maxCount(1)), ItemGroupRegistry.BAM_EQUIPMENT);
    public static final Item UNBREAKING_BELT = registerItem("unbreaking_belt", new AccessoryTrinketItem(BetterAdventureMode.identifier("model/accessory/belt_basic"), new FabricItemSettings().maxCount(1)), ItemGroupRegistry.BAM_EQUIPMENT);
    public static final Item FEATHER_FALLING_BELT = registerItem("feather_falling_belt", new AccessoryTrinketItem(BetterAdventureMode.identifier("model/accessory/belt_basic"), new FabricItemSettings().maxCount(1)), ItemGroupRegistry.BAM_EQUIPMENT);

    // necklaces
    public static final Item TELEPORT_HOME_NECKLACE = registerItem("teleport_home_necklace", new AccessoryTrinketItem(BetterAdventureMode.identifier("model/accessory/necklace_basic"), new FabricItemSettings().maxCount(1)), ItemGroupRegistry.BAM_EQUIPMENT);
    public static final Item HEALTH_REGENERATION_AURA_NECKLACE = registerItem("health_regeneration_aura_necklace", new AuraGrantingNecklaceTrinketItem((AuraStatusEffect) StatusEffectsRegistry.HEALTH_REGENERATION_AURA_EFFECT, BetterAdventureMode.identifier("model/accessory/necklace_basic"), new FabricItemSettings().maxCount(1)), ItemGroupRegistry.BAM_EQUIPMENT);

    // rings
    public static final Item TWO_HEALTH_REGENERATION_RING = registerItem("two_health_regeneration_ring", new ModifyEntityAttributeRingItem(EntityAttributesRegistry.HEALTH_REGENERATION, "health_regeneration", 2, EntityAttributeModifier.Operation.ADDITION, BetterAdventureMode.identifier("model/accessory/ring_basic"), new FabricItemSettings().maxCount(1)), ItemGroupRegistry.BAM_EQUIPMENT);
    public static final Item THREE_SPELL_SLOT_RING = registerItem("three_spell_slot_ring", new ModifyEntityAttributeRingItem(EntityAttributesRegistry.ACTIVE_SPELL_SLOT_AMOUNT, "active_spell_slot_amount", 3, EntityAttributeModifier.Operation.ADDITION, BetterAdventureMode.identifier("model/accessory/ring_basic"), new FabricItemSettings().maxCount(1)), ItemGroupRegistry.BAM_EQUIPMENT);
    public static final Item ARMOR_TOUGHNESS_RING = registerItem("armor_toughness_ring", new ModifyEntityAttributeRingItem(EntityAttributes.GENERIC_ARMOR_TOUGHNESS, "armor_toughness", 0.25, EntityAttributeModifier.Operation.ADDITION, BetterAdventureMode.identifier("model/accessory/ring_basic"), new FabricItemSettings().maxCount(1)), ItemGroupRegistry.BAM_EQUIPMENT);

    // Wizards spell books
    public static final Item ARCANE_BEAM_SPELL_BOOK = registerSpellContainerItem("arcane_beam_spell_book", new MultiSlotSpellBookItem(-1, BetterAdventureMode.identifier("arcane_beam"), new FabricItemSettings().maxCount(1)), ItemGroupRegistry.BAM_EQUIPMENT, List.of("wizards:arcane_beam"));
    public static final Item ARCANE_BLAST_SPELL_BOOK = registerSpellContainerItem("arcane_blast_spell_book", new SpellBookItem(BetterAdventureMode.identifier("arcane_blast"), new FabricItemSettings().maxCount(1)), ItemGroupRegistry.BAM_EQUIPMENT, List.of("wizards:arcane_blast"));
    public static final Item ARCANE_BOLT_SPELL_BOOK = registerSpellContainerItem("arcane_bolt_spell_book", new SpellBookItem(BetterAdventureMode.identifier("arcane_bolt"), new FabricItemSettings().maxCount(1)), ItemGroupRegistry.BAM_EQUIPMENT, List.of("wizards:arcane_bolt"));
    public static final Item ARCANE_MISSILE_SPELL_BOOK = registerSpellContainerItem("arcane_missile_spell_book", new SpellBookItem(BetterAdventureMode.identifier("arcane_missile"), new FabricItemSettings().maxCount(1)), ItemGroupRegistry.BAM_EQUIPMENT, List.of("wizards:arcane_missile"));
    public static final Item FIRE_BREATH_SPELL_BOOK = registerSpellContainerItem("fire_breath_spell_book", new SpellBookItem(BetterAdventureMode.identifier("fire_breath"), new FabricItemSettings().maxCount(1)), ItemGroupRegistry.BAM_EQUIPMENT, List.of("wizards:fire_breath"));
    public static final Item FIRE_METEOR_SPELL_BOOK = registerSpellContainerItem("fire_meteor_spell_book", new MultiSlotSpellBookItem(-1, BetterAdventureMode.identifier("fire_meteor"), new FabricItemSettings().maxCount(1)), ItemGroupRegistry.BAM_EQUIPMENT, List.of("wizards:fire_meteor"));
    public static final Item FIRE_SCORCH_SPELL_BOOK = registerSpellContainerItem("fire_scorch_spell_book", new SpellBookItem(BetterAdventureMode.identifier("fire_scorch"), new FabricItemSettings().maxCount(1)), ItemGroupRegistry.BAM_EQUIPMENT, List.of("wizards:fire_scorch"));
    public static final Item FIREBALL_SPELL_BOOK = registerSpellContainerItem("fireball_spell_book", new SpellBookItem(BetterAdventureMode.identifier("fireball"), new FabricItemSettings().maxCount(1)), ItemGroupRegistry.BAM_EQUIPMENT, List.of("wizards:fireball"));
    public static final Item FROST_BOLT_SPELL_BOOK = registerSpellContainerItem("frost_bolt_spell_book", new SpellBookItem(BetterAdventureMode.identifier("frost_bolt"), new FabricItemSettings().maxCount(1)), ItemGroupRegistry.BAM_EQUIPMENT, List.of("wizards:frost_bolt"));
    public static final Item FROST_NOVA_SPELL_BOOK = registerSpellContainerItem("frost_nova_spell_book", new SpellBookItem(BetterAdventureMode.identifier("frost_nova"), new FabricItemSettings().maxCount(1)), ItemGroupRegistry.BAM_EQUIPMENT, List.of("wizards:frost_nova"));
    public static final Item FROST_SHIELD_SPELL_BOOK = registerSpellContainerItem("frost_shield_spell_book", new MultiSlotSpellBookItem(-1, BetterAdventureMode.identifier("frost_shield"), new FabricItemSettings().maxCount(1)), ItemGroupRegistry.BAM_EQUIPMENT, List.of("wizards:frost_shield"));
    public static final Item ICE_LANCE_SPELL_BOOK = registerSpellContainerItem("ice_lance_spell_book", new SpellBookItem(BetterAdventureMode.identifier("ice_lance"), new FabricItemSettings().maxCount(1)), ItemGroupRegistry.BAM_EQUIPMENT, List.of("wizards:ice_lance"));

    // Paladins & Priests spell books
    public static final Item CIRCLE_OF_HEALING_SPELL_BOOK = registerSpellContainerItem("circle_of_healing_spell_book", new MultiSlotSpellBookItem(-1, BetterAdventureMode.identifier("circle_of_healing"), new FabricItemSettings().maxCount(1)), ItemGroupRegistry.BAM_EQUIPMENT, List.of("paladins:circle_of_healing"));
    public static final Item DIVINE_PROTECTION_SPELL_BOOK = registerSpellContainerItem("divine_protection_spell_book", new MultiSlotSpellBookItem(-1, BetterAdventureMode.identifier("divine_protection"), new FabricItemSettings().maxCount(1)), ItemGroupRegistry.BAM_EQUIPMENT, List.of("paladins:divine_protection"));
    public static final Item FLASH_HEAL_SPELL_BOOK = registerSpellContainerItem("flash_heal_spell_book", new SpellBookItem(BetterAdventureMode.identifier("flash_heal"), new FabricItemSettings().maxCount(1)), ItemGroupRegistry.BAM_EQUIPMENT, List.of("paladins:flash_heal"));
    public static final Item HEAL_SPELL_BOOK = registerSpellContainerItem("heal_spell_book", new SpellBookItem(BetterAdventureMode.identifier("heal"), new FabricItemSettings().maxCount(1)), ItemGroupRegistry.BAM_EQUIPMENT, List.of("paladins:heal"));
    public static final Item HOLY_BEAM_SPELL_BOOK = registerSpellContainerItem("holy_beam_spell_book", new SpellBookItem(BetterAdventureMode.identifier("holy_beam"), new FabricItemSettings().maxCount(1)), ItemGroupRegistry.BAM_EQUIPMENT, List.of("paladins:holy_beam"));
    public static final Item HOLY_SHOCK_SPELL_BOOK = registerSpellContainerItem("holy_shock_spell_book", new SpellBookItem(BetterAdventureMode.identifier("holy_shock"), new FabricItemSettings().maxCount(1)), ItemGroupRegistry.BAM_EQUIPMENT, List.of("paladins:holy_shock"));
    public static final Item JUDGEMENT_SPELL_BOOK = registerSpellContainerItem("judgement_spell_book", new SpellBookItem(BetterAdventureMode.identifier("judgement"), new FabricItemSettings().maxCount(1)), ItemGroupRegistry.BAM_EQUIPMENT, List.of("paladins:judgement"));
    //endregion Accessories

    //region Hotbar Items
    // spell scrolls
//    public static final Item FIREBALL_SPELL_SCROLL = registerItem("fireball_spell_scroll", new Item(new FabricItemSettings().maxCount(16)), ItemGroupRegistry.BAM_EQUIPMENT);

    // food
    public static final Item SWEET_BERRIES = registerItem("sweet_berries", new AdventureFoodConsumable(new FabricItemSettings().maxCount(16).food(AdventureFoodComponents.SWEET_BERRIES)), ItemGroupRegistry.BAM_FOOD);
    public static final Item BROWN_MUSHROOM = registerItem("brown_mushroom", new AdventureFoodConsumable(new FabricItemSettings().maxCount(16).food(AdventureFoodComponents.BROWN_MUSHROOM)), ItemGroupRegistry.BAM_FOOD);
    public static final Item RED_MUSHROOM = registerItem("red_mushroom", new AdventureFoodConsumable(new FabricItemSettings().maxCount(16).food(AdventureFoodComponents.RED_MUSHROOM)), ItemGroupRegistry.BAM_FOOD);
    public static final Item GLOW_BERRIES = registerItem("glow_berries", new AdventureFoodConsumable(new FabricItemSettings().maxCount(16).food(AdventureFoodComponents.GLOW_BERRIES)), ItemGroupRegistry.BAM_FOOD);
    //endregion Hotbar Items
    
    // weapons
    // TODO SpellEngine
    public static final Item ELEMENTAL_FLAME = registerFilteredSpellProxyItem("elemental_flame", new StaffItem(ToolMaterials.WOOD, new FabricItemSettings().maxDamage(50)), ItemGroupRegistry.BAM_EQUIPMENT, List.of(), "betteradventuremode:fire_spell_pool");
    public static final Item ARCANE_ENCYCLOPEDIA = registerFilteredSpellProxyItem("arcane_encyclopedia", new StaffItem(ToolMaterials.WOOD, new FabricItemSettings().maxDamage(50)), ItemGroupRegistry.BAM_EQUIPMENT, List.of(), "betteradventuremode:arcane_spell_pool");
    public static final Item ICE_STAFF = registerFilteredSpellProxyItem("ice_staff", new StaffItem(ToolMaterials.WOOD, new FabricItemSettings().maxDamage(50)), ItemGroupRegistry.BAM_EQUIPMENT, List.of(), "betteradventuremode:frost_spell_pool");
    public static final Item BLESSED_TALISMAN = registerFilteredSpellProxyItem("blessed_talisman", new StaffItem(ToolMaterials.WOOD, new FabricItemSettings().maxDamage(50)), ItemGroupRegistry.BAM_EQUIPMENT, List.of(), "betteradventuremode:holy_spell_pool");

//    public static final Item ZWEIHANDER = registerItem("zweihander", new BetterAdventureMode_BasicWeaponItem(DamageTypes.PLAYER_ATTACK, DamageTypes.PLAYER_ATTACK,7, -3.5F, 5, 5,  new Item.Settings().maxDamage(336)), ItemGroupRegistry.BAM_EQUIPMENT);
    public static final Item WOODEN_SWORD = registerItem("wooden_sword", new BasicWeaponItem(DamageTypesRegistry.SLASHING_DAMAGE_TYPE, DamageTypesRegistry.PIERCING_DAMAGE_TYPE,3, -2.4F, 1, 1, 1, SoundEvents.ITEM_ARMOR_EQUIP_GENERIC,  new Item.Settings().maxDamage(50)), ItemGroupRegistry.BAM_EQUIPMENT);
    public static final Item STONE_SWORD = registerItem("stone_sword", new BasicWeaponItem(DamageTypesRegistry.SLASHING_DAMAGE_TYPE, DamageTypesRegistry.PIERCING_DAMAGE_TYPE,4, -2.4F, 1, 1, 3, SoundEvents.ITEM_ARMOR_EQUIP_GENERIC,  new Item.Settings().maxDamage(100)), ItemGroupRegistry.BAM_EQUIPMENT);
    public static final Item GOLDEN_SWORD = registerItem("golden_sword", new BasicWeaponItem(DamageTypesRegistry.SLASHING_DAMAGE_TYPE, DamageTypesRegistry.PIERCING_DAMAGE_TYPE,3, -2.4F, 1, 1, 4, SoundEvents.ITEM_ARMOR_EQUIP_GOLD,  new Item.Settings().maxDamage(50)), ItemGroupRegistry.BAM_EQUIPMENT);
    public static final Item IRON_SWORD = registerItem("iron_sword", new BasicWeaponItem(DamageTypesRegistry.SLASHING_DAMAGE_TYPE, DamageTypesRegistry.PIERCING_DAMAGE_TYPE,5, -2.4F, 1, 1, 3, SoundEvents.ITEM_ARMOR_EQUIP_IRON,  new Item.Settings().maxDamage(200)), ItemGroupRegistry.BAM_EQUIPMENT);
    public static final Item DIAMOND_SWORD = registerItem("diamond_sword", new BasicWeaponItem(DamageTypesRegistry.SLASHING_DAMAGE_TYPE, DamageTypesRegistry.PIERCING_DAMAGE_TYPE,6, -2.4F, 2, 1, 3, SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND,  new Item.Settings().maxDamage(250)), ItemGroupRegistry.BAM_EQUIPMENT);
    public static final Item NETHERITE_SWORD = registerItem("netherite_sword", new BasicWeaponItem(DamageTypesRegistry.SLASHING_DAMAGE_TYPE, DamageTypesRegistry.PIERCING_DAMAGE_TYPE,7, -2.4F, 2, 1, 3, SoundEvents.ITEM_ARMOR_EQUIP_NETHERITE,  new Item.Settings().maxDamage(300)), ItemGroupRegistry.BAM_EQUIPMENT);

    public static final Item WOODEN_PICKAXE = registerItem("wooden_pickaxe", new BasicWeaponItem(DamageTypesRegistry.PIERCING_DAMAGE_TYPE,3, -2.8F, 1, 1, SoundEvents.ITEM_ARMOR_EQUIP_GENERIC,  new Item.Settings().maxDamage(50)), ItemGroupRegistry.BAM_EQUIPMENT);
    public static final Item STONE_PICKAXE = registerItem("stone_pickaxe", new BasicWeaponItem(DamageTypesRegistry.PIERCING_DAMAGE_TYPE,4, -2.8F, 1, 3, SoundEvents.ITEM_ARMOR_EQUIP_GENERIC,  new Item.Settings().maxDamage(100)), ItemGroupRegistry.BAM_EQUIPMENT);
    public static final Item GOLDEN_PICKAXE = registerItem("golden_pickaxe", new BasicWeaponItem(DamageTypesRegistry.PIERCING_DAMAGE_TYPE,3, -2.8F, 1, 4, SoundEvents.ITEM_ARMOR_EQUIP_GOLD,  new Item.Settings().maxDamage(50)), ItemGroupRegistry.BAM_EQUIPMENT);
    public static final Item IRON_PICKAXE = registerItem("iron_pickaxe", new BasicWeaponItem(DamageTypesRegistry.PIERCING_DAMAGE_TYPE,5, -2.8F, 1, 3, SoundEvents.ITEM_ARMOR_EQUIP_IRON,  new Item.Settings().maxDamage(200)), ItemGroupRegistry.BAM_EQUIPMENT);
    public static final Item DIAMOND_PICKAXE = registerItem("diamond_pickaxe", new BasicWeaponItem(DamageTypesRegistry.PIERCING_DAMAGE_TYPE,6, -2.8F, 2, 3, SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND,  new Item.Settings().maxDamage(250)), ItemGroupRegistry.BAM_EQUIPMENT);
    public static final Item NETHERITE_PICKAXE = registerItem("netherite_pickaxe", new BasicWeaponItem(DamageTypesRegistry.PIERCING_DAMAGE_TYPE,7, -2.8F, 2, 3, SoundEvents.ITEM_ARMOR_EQUIP_NETHERITE,  new Item.Settings().maxDamage(300)), ItemGroupRegistry.BAM_EQUIPMENT);

    public static final Item WOODEN_AXE = registerItem("wooden_axe", new BasicWeaponItem(DamageTypesRegistry.SLASHING_DAMAGE_TYPE,3, -3.0F, 1, 1, SoundEvents.ITEM_ARMOR_EQUIP_GENERIC,  new Item.Settings().maxDamage(50)), ItemGroupRegistry.BAM_EQUIPMENT);
    public static final Item STONE_AXE = registerItem("stone_axe", new BasicWeaponItem(DamageTypesRegistry.SLASHING_DAMAGE_TYPE,4, -3.0F, 1, 3, SoundEvents.ITEM_ARMOR_EQUIP_GENERIC,  new Item.Settings().maxDamage(100)), ItemGroupRegistry.BAM_EQUIPMENT);
    public static final Item GOLDEN_AXE = registerItem("golden_axe", new BasicWeaponItem(DamageTypesRegistry.SLASHING_DAMAGE_TYPE,3, -3.0F, 1, 4, SoundEvents.ITEM_ARMOR_EQUIP_GOLD,  new Item.Settings().maxDamage(50)), ItemGroupRegistry.BAM_EQUIPMENT);
    public static final Item IRON_AXE = registerItem("iron_axe", new BasicWeaponItem(DamageTypesRegistry.SLASHING_DAMAGE_TYPE,5, -3.0F, 1, 3, SoundEvents.ITEM_ARMOR_EQUIP_IRON,  new Item.Settings().maxDamage(200)), ItemGroupRegistry.BAM_EQUIPMENT);
    public static final Item DIAMOND_AXE = registerItem("diamond_axe", new BasicWeaponItem(DamageTypesRegistry.SLASHING_DAMAGE_TYPE,6, -3.0F, 2, 3, SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND,  new Item.Settings().maxDamage(250)), ItemGroupRegistry.BAM_EQUIPMENT);
    public static final Item NETHERITE_AXE = registerItem("netherite_axe", new BasicWeaponItem(DamageTypesRegistry.SLASHING_DAMAGE_TYPE,7, -3.0F, 2, 3, SoundEvents.ITEM_ARMOR_EQUIP_NETHERITE,  new Item.Settings().maxDamage(300)), ItemGroupRegistry.BAM_EQUIPMENT);

    public static final Item DEFAULT_EMPTY_HAND_WEAPON = registerItem("default_empty_hand_weapon", new EmptyHandWeapon(1, -3.0F, 1, new Item.Settings()), null);

    // test items
    public static final Item TEST_NECKLACE = registerItem("test_necklace", new AccessoryTrinketItem(BetterAdventureMode.identifier("model/accessory/necklace_basic"), new FabricItemSettings().maxCount(1)), ItemGroups.OPERATOR);
    public static final Item BLOCK_POSITION_DISTANCE_METER = registerItem("block_position_distance_meter", new BlockPositionDistanceMeterItem(new FabricItemSettings().maxCount(1)), ItemGroupRegistry.SCRIPT_BLOCKS);

    // TODO SpellEngine
//    public static final Item TEST_WAND = registerFilteredSpellProxyItem("test_wand", new StaffItem(ToolMaterials.WOOD, new FabricItemSettings().maxDamage(50)), ItemGroups.OPERATOR, List.of(), "betteradventuremode:test_wand_pool");
    public static final Item TEST_BELT = registerItem("test_belt", new AccessoryTrinketItem(BetterAdventureMode.identifier("model/accessory/belt_basic"), new FabricItemSettings().maxCount(1)), ItemGroups.OPERATOR);
    public static final Item TEST_RING = registerItem("test_ring", new AccessoryTrinketItem(BetterAdventureMode.identifier("model/accessory/ring_basic"), new FabricItemSettings().maxCount(1)), ItemGroups.OPERATOR);
    public static final Item MANA_TEST_BELT = registerItem("mana_test_belt", new ManaTestBeltItem(new FabricItemSettings().maxCount(1)), ItemGroups.OPERATOR);
    public static final Item TEST_BUCKLER = registerItem("test_buckler", new BasicShieldItem(2, 0, 0, 0, 0.2, true, 3.0, 2, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, new FabricItemSettings().maxDamage(336)), ItemGroups.OPERATOR);
    public static final Item TEST_NORMAL_SHIELD = registerItem("test_normal_shield", new BasicShieldItem(3, 0, 0, 0, 0.5, true, 1.5, 3, SoundEvents.ITEM_ARMOR_EQUIP_IRON, new FabricItemSettings().maxDamage(336)), ItemGroups.OPERATOR);
    public static final Item TEST_TOWER_SHIELD = registerItem("test_tower_shield", new BasicShieldItem(5, 0, 0, 0, 1.0, false, 1.0, 5, SoundEvents.ITEM_ARMOR_EQUIP_NETHERITE, new FabricItemSettings().maxDamage(336)), ItemGroups.OPERATOR);
    public static final Item TEST_SWORD = registerItem("test_sword", new BasicWeaponItem(DamageTypesRegistry.SLASHING_DAMAGE_TYPE, DamageTypesRegistry.PIERCING_DAMAGE_TYPE, 4, -3.0F, 3, 2, 2, SoundEvents.ITEM_ARMOR_EQUIP_GENERIC, new FabricItemSettings().maxDamage(336)), ItemGroups.OPERATOR);
    public static final Item TEST_AURA_NECKLACE = registerItem("test_aura_necklace", new AuraGrantingNecklaceTrinketItem((AuraStatusEffect) StatusEffectsRegistry.TEST_AURA_EFFECT, BetterAdventureMode.identifier("model/accessory/invisible"), new FabricItemSettings().maxCount(1)), ItemGroups.OPERATOR);
    public static final Item BLOODY_MOSS = registerItem("bloody_moss", new AddEffectBuildUpItem(new FabricItemSettings().maxCount(16), 0, false), ItemGroups.OPERATOR);
    public static final Item BURNING_MOSS = registerItem("burning_moss", new AddEffectBuildUpItem(new FabricItemSettings().maxCount(16), 1, false), ItemGroups.OPERATOR);
    public static final Item FREEZING_MOSS = registerItem("freezing_moss", new AddEffectBuildUpItem(new FabricItemSettings().maxCount(16), 2, false), ItemGroups.OPERATOR);
    public static final Item POISON_MOSS = registerItem("poison_moss", new AddEffectBuildUpItem(new FabricItemSettings().maxCount(16), 3, false), ItemGroups.OPERATOR);
    public static final Item SHOCKING_MOSS = registerItem("shocking_moss", new AddEffectBuildUpItem(new FabricItemSettings().maxCount(16), 4, false), ItemGroups.OPERATOR);
    public static final Item STAGGERING_MOSS = registerItem("staggering_moss", new AddEffectBuildUpItem(new FabricItemSettings().maxCount(16), 5, false), ItemGroups.OPERATOR);

    public static final Item MANNEQUIN = registerItem("mannequin", new MannequinItem(new FabricItemSettings()), ItemGroupRegistry.BAM_BLOCK);
//    public static final Item MANNEQUIN_SLIM = registerItem("mannequin_slim", new MannequinItem(new FabricItemSettings(), true), ItemGroupRegistry.BAM_BLOCK);

    private static Item registerItem(String name, Item item, @Nullable RegistryKey<ItemGroup> itemGroup) {

        if (itemGroup != null) {
            ItemGroupEvents.modifyEntriesEvent(itemGroup).register(content -> {
                content.add(item);
            });
        }
        return Registry.register(Registries.ITEM, BetterAdventureMode.identifier(name), item);
    }

    private static Item registerSpellContainerItem(String name, Item item, @Nullable RegistryKey<ItemGroup> itemGroup, List<String> spells) {

        SpellContainer container = new SpellContainer(false, null, 0, spells);
        SpellRegistry.book_containers.put(BetterAdventureMode.identifier(name), container);

        if (itemGroup != null) {
            ItemGroupEvents.modifyEntriesEvent(itemGroup).register(content -> {
                content.add(item);
            });
        }
        return Registry.register(Registries.ITEM, BetterAdventureMode.identifier(name), item);
    }

    private static Item registerFilteredSpellProxyItem(String name, Item item, @Nullable RegistryKey<ItemGroup> itemGroup, List<String> spells, String proxyPool) {

        SpellContainer container = ItemUtils.setProxyPool(new SpellContainer(true, null, 0, spells), proxyPool);
        SpellRegistry.book_containers.put(BetterAdventureMode.identifier(name), container);

        if (itemGroup != null) {
            ItemGroupEvents.modifyEntriesEvent(itemGroup).register(content -> {
                content.add(item);
            });
        }
        return Registry.register(Registries.ITEM, BetterAdventureMode.identifier(name), item);
    }

    public static void init() {
    }
}
