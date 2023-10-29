package com.github.theredbrain.bamcore.registry;

import com.github.theredbrain.bamcore.BetterAdventureModeCore;
import com.github.theredbrain.bamcore.api.effect.AuraStatusEffect;
import com.github.theredbrain.bamcore.api.item.*;
import com.github.theredbrain.bamcore.api.util.BetterAdventureModCoreItemUtils;
import com.github.theredbrain.bamcore.item.*;
import dev.emi.trinkets.api.TrinketItem;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.item.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.spell_engine.api.item.trinket.SpellBookItem;
import net.spell_engine.api.item.weapon.StaffItem;
import net.spell_engine.api.spell.SpellContainer;
import net.spell_engine.internals.SpellRegistry;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ItemRegistry {

    // region Armor
    public static final Item LEATHER_HELMET = registerItem("leather_helmet", new ArmorTrinketItem(1, 0, 1, new FabricItemSettings().maxDamage(60)), ItemGroupRegistry.BAM_EQUIPMENT);
    public static final Item LEATHER_CHESTPLATE = registerItem("leather_chestplate", new ArmorTrinketItem(3, 0, 3, new FabricItemSettings().maxDamage(60)), ItemGroupRegistry.BAM_EQUIPMENT);
    public static final Item LEATHER_LEGGINGS = registerItem("leather_leggings", new ArmorTrinketItem(2, 0, 2, new FabricItemSettings().maxDamage(60)), ItemGroupRegistry.BAM_EQUIPMENT);
    public static final Item LEATHER_BOOTS = registerItem("leather_boots", new ArmorTrinketItem(1, 0, 1, new FabricItemSettings().maxDamage(60)), ItemGroupRegistry.BAM_EQUIPMENT);
    public static final Item LEATHER_GLOVES = registerItem("leather_gloves", new ArmorTrinketItem(1, 0, 1, new FabricItemSettings().maxDamage(60)), ItemGroupRegistry.BAM_EQUIPMENT);
    public static final Item LEATHER_SHOULDERS = registerItem("leather_shoulders", new ArmorTrinketItem(1, 0, 1, new FabricItemSettings().maxDamage(60)), ItemGroupRegistry.BAM_EQUIPMENT);

    public static final Item CHAINMAIL_HELMET = registerItem("chainmail_helmet", new ArmorTrinketItem(2, 0, 2, new FabricItemSettings().maxDamage(70)), ItemGroupRegistry.BAM_EQUIPMENT);
    public static final Item CHAINMAIL_CHESTPLATE = registerItem("chainmail_chestplate", new ArmorTrinketItem(5, 0, 5, new FabricItemSettings().maxDamage(70)), ItemGroupRegistry.BAM_EQUIPMENT);
    public static final Item CHAINMAIL_LEGGINGS = registerItem("chainmail_leggings", new ArmorTrinketItem(4, 0, 4, new FabricItemSettings().maxDamage(70)), ItemGroupRegistry.BAM_EQUIPMENT);
    public static final Item CHAINMAIL_BOOTS = registerItem("chainmail_boots", new ArmorTrinketItem(1, 0, 1, new FabricItemSettings().maxDamage(70)), ItemGroupRegistry.BAM_EQUIPMENT);
    public static final Item CHAINMAIL_GLOVES = registerItem("chainmail_gloves", new ArmorTrinketItem(1, 0, 1, new FabricItemSettings().maxDamage(70)), ItemGroupRegistry.BAM_EQUIPMENT);
    public static final Item CHAINMAIL_SHOULDERS = registerItem("chainmail_shoulders", new ArmorTrinketItem(2, 0, 2, new FabricItemSettings().maxDamage(70)), ItemGroupRegistry.BAM_EQUIPMENT);

    public static final Item IRON_HELMET = registerItem("iron_helmet", new ArmorTrinketItem(2, 0, 2, new FabricItemSettings().maxDamage(90)), ItemGroupRegistry.BAM_EQUIPMENT);
    public static final Item IRON_CHESTPLATE = registerItem("iron_chestplate", new ArmorTrinketItem(6, 0, 6, new FabricItemSettings().maxDamage(90)), ItemGroupRegistry.BAM_EQUIPMENT);
    public static final Item IRON_LEGGINGS = registerItem("iron_leggings", new ArmorTrinketItem(5, 0, 5, new FabricItemSettings().maxDamage(90)), ItemGroupRegistry.BAM_EQUIPMENT);
    public static final Item IRON_BOOTS = registerItem("iron_boots", new ArmorTrinketItem(2, 0, 2, new FabricItemSettings().maxDamage(90)), ItemGroupRegistry.BAM_EQUIPMENT);
    public static final Item IRON_GLOVES = registerItem("iron_gloves", new ArmorTrinketItem(2, 0, 2, new FabricItemSettings().maxDamage(90)), ItemGroupRegistry.BAM_EQUIPMENT);
    public static final Item IRON_SHOULDERS = registerItem("iron_shoulders", new ArmorTrinketItem(2, 0, 2, new FabricItemSettings().maxDamage(90)), ItemGroupRegistry.BAM_EQUIPMENT);

    public static final Item DIAMOND_HELMET = registerItem("diamond_helmet", new ArmorTrinketItem(3, 0.025, 3, new FabricItemSettings().maxDamage(40)), ItemGroupRegistry.BAM_EQUIPMENT);
    public static final Item DIAMOND_CHESTPLATE = registerItem("diamond_chestplate", new ArmorTrinketItem(8, 0.025, 8, new FabricItemSettings().maxDamage(40)), ItemGroupRegistry.BAM_EQUIPMENT);
    public static final Item DIAMOND_LEGGINGS = registerItem("diamond_leggings", new ArmorTrinketItem(6, 0.025, 6, new FabricItemSettings().maxDamage(40)), ItemGroupRegistry.BAM_EQUIPMENT);
    public static final Item DIAMOND_BOOTS = registerItem("diamond_boots", new ArmorTrinketItem(3, 0.025, 3, new FabricItemSettings().maxDamage(40)), ItemGroupRegistry.BAM_EQUIPMENT);
    public static final Item DIAMOND_GLOVES = registerItem("diamond_gloves", new ArmorTrinketItem(3, 0.025, 3, new FabricItemSettings().maxDamage(40)), ItemGroupRegistry.BAM_EQUIPMENT);
    public static final Item DIAMOND_SHOULDERS = registerItem("diamond_shoulders", new ArmorTrinketItem(3, 0.025, 3, new FabricItemSettings().maxDamage(40)), ItemGroupRegistry.BAM_EQUIPMENT);

    public static final Item GOLDEN_HELMET = registerItem("golden_helmet", new ArmorTrinketItem(2, 0, 2, new FabricItemSettings().maxDamage(50)), ItemGroupRegistry.BAM_EQUIPMENT);
    public static final Item GOLDEN_CHESTPLATE = registerItem("golden_chestplate", new ArmorTrinketItem(5, 0, 5, new FabricItemSettings().maxDamage(50)), ItemGroupRegistry.BAM_EQUIPMENT);
    public static final Item GOLDEN_LEGGINGS = registerItem("golden_leggings", new ArmorTrinketItem(3, 0, 3, new FabricItemSettings().maxDamage(50)), ItemGroupRegistry.BAM_EQUIPMENT);
    public static final Item GOLDEN_BOOTS = registerItem("golden_boots", new ArmorTrinketItem(1, 0, 1, new FabricItemSettings().maxDamage(50)), ItemGroupRegistry.BAM_EQUIPMENT);
    public static final Item GOLDEN_GLOVES = registerItem("golden_gloves", new ArmorTrinketItem(1, 0, 1, new FabricItemSettings().maxDamage(50)), ItemGroupRegistry.BAM_EQUIPMENT);
    public static final Item GOLDEN_SHOULDERS = registerItem("golden_shoulders", new ArmorTrinketItem(2, 0, 2, new FabricItemSettings().maxDamage(50)), ItemGroupRegistry.BAM_EQUIPMENT);

    public static final Item NETHERITE_HELMET = registerItem("netherite_helmet", new ArmorTrinketItem(3, 0.05, 3, new FabricItemSettings().maxDamage(120)), ItemGroupRegistry.BAM_EQUIPMENT);
    public static final Item NETHERITE_CHESTPLATE = registerItem("netherite_chestplate", new ArmorTrinketItem(8, 0.05, 8, new FabricItemSettings().maxDamage(120)), ItemGroupRegistry.BAM_EQUIPMENT);
    public static final Item NETHERITE_LEGGINGS = registerItem("netherite_leggings", new ArmorTrinketItem(6, 0.05, 6, new FabricItemSettings().maxDamage(120)), ItemGroupRegistry.BAM_EQUIPMENT);
    public static final Item NETHERITE_BOOTS = registerItem("netherite_boots", new ArmorTrinketItem(3, 0.05, 3, new FabricItemSettings().maxDamage(120)), ItemGroupRegistry.BAM_EQUIPMENT);
    public static final Item NETHERITE_GLOVES = registerItem("netherite_gloves", new ArmorTrinketItem(3, 0.05, 3, new FabricItemSettings().maxDamage(120)), ItemGroupRegistry.BAM_EQUIPMENT);
    public static final Item NETHERITE_SHOULDERS = registerItem("netherite_shoulders", new ArmorTrinketItem(3, 0.05, 3, new FabricItemSettings().maxDamage(120)), ItemGroupRegistry.BAM_EQUIPMENT);
    //endregion Armor

    //region Accessories
    // belts
    public static final Item MANA_REGENERATION_BELT = registerItem("mana_regeneration_belt", new TrinketItem(new FabricItemSettings().maxCount(1)), ItemGroupRegistry.BAM_EQUIPMENT);
    public static final Item DEPTH_STRIDER_BELT = registerItem("depth_strider_belt", new TrinketItem(new FabricItemSettings().maxCount(1)), ItemGroupRegistry.BAM_EQUIPMENT);
    public static final Item LOOTING_BELT = registerItem("looting_belt", new TrinketItem(new FabricItemSettings().maxCount(1)), ItemGroupRegistry.BAM_EQUIPMENT);
    public static final Item UNBREAKING_BELT = registerItem("unbreaking_belt", new TrinketItem(new FabricItemSettings().maxCount(1)), ItemGroupRegistry.BAM_EQUIPMENT);
    public static final Item FEATHER_FALLING_BELT = registerItem("feather_falling_belt", new TrinketItem(new FabricItemSettings().maxCount(1)), ItemGroupRegistry.BAM_EQUIPMENT);

    // necklaces
    public static final Item TELEPORT_HOME_NECKLACE = registerItem("teleport_home_necklace", new TrinketItem(new FabricItemSettings().maxCount(1)), ItemGroupRegistry.BAM_EQUIPMENT);
    public static final Item HEALTH_REGENERATION_AURA_NECKLACE = registerItem("health_regeneration_aura_necklace", new AuraGrantingNecklaceTrinketItem((AuraStatusEffect) StatusEffectsRegistry.HEALTH_REGENERATION_AURA_EFFECT, new FabricItemSettings().maxCount(1)), ItemGroupRegistry.BAM_EQUIPMENT);

    // rings
    public static final Item TWO_HEALTH_REGENERATION_RING = registerItem("two_health_regeneration_ring", new ModifyEntityAttributeRingItem(EntityAttributesRegistry.HEALTH_REGENERATION, "health_regeneration", 2, EntityAttributeModifier.Operation.ADDITION, new FabricItemSettings().maxCount(1)), ItemGroupRegistry.BAM_EQUIPMENT);
    public static final Item THREE_SPELL_SLOT_RING = registerItem("three_spell_slot_ring", new ModifyEntityAttributeRingItem(EntityAttributesRegistry.ACTIVE_SPELL_SLOT_AMOUNT, "active_spell_slot_amount", 3, EntityAttributeModifier.Operation.ADDITION, new FabricItemSettings().maxCount(1)), ItemGroupRegistry.BAM_EQUIPMENT);

    // elemental spell books
    public static final Item FIREBALL_SPELL_BOOK = registerSpellContainerItem("fireball_spell_book", new MultiSlotSpellBookItem(-1, BetterAdventureModeCore.identifier("fireball"), new FabricItemSettings().maxCount(1)), ItemGroupRegistry.BAM_EQUIPMENT, List.of("bamcontent:fireball"));
    public static final Item DRAGONS_BREATH_SPELL_BOOK = registerSpellContainerItem("dragons_breath_spell_book", new SpellBookItem(BetterAdventureModeCore.identifier("dragons_breath"), new FabricItemSettings().maxCount(1)), ItemGroupRegistry.BAM_EQUIPMENT, List.of("bamcontent:dragons_breath"));
    public static final Item RING_OF_FIRE_SPELL_BOOK = registerSpellContainerItem("ring_of_fire_spell_book", new MultiSlotSpellBookItem(-1, BetterAdventureModeCore.identifier("ring_of_fire"), new FabricItemSettings().maxCount(1)), ItemGroupRegistry.BAM_EQUIPMENT, List.of("bamcontent:ring_of_fire"));
    public static final Item FROST_SHARDS_SPELL_BOOK = registerSpellContainerItem("frost_shards_spell_book", new SpellBookItem(BetterAdventureModeCore.identifier("frost_shards"), new FabricItemSettings().maxCount(1)), ItemGroupRegistry.BAM_EQUIPMENT, List.of("bamcontent:frost_shards"));
    public static final Item GLACIER_SPELL_BOOK = registerSpellContainerItem("glacier_spell_book", new MultiSlotSpellBookItem(-1, BetterAdventureModeCore.identifier("glacier"), new FabricItemSettings().maxCount(1)), ItemGroupRegistry.BAM_EQUIPMENT, List.of("bamcontent:glacier"));
    public static final Item COLD_SNAP_SPELL_BOOK = registerSpellContainerItem("cold_snap_spell_book", new SpellBookItem(BetterAdventureModeCore.identifier("cold_snap"), new FabricItemSettings().maxCount(1)), ItemGroupRegistry.BAM_EQUIPMENT, List.of("bamcontent:cold_snap"));
    public static final Item LIGHTNING_STRIKE_SPELL_BOOK = registerSpellContainerItem("lightning_strike_spell_book", new MultiSlotSpellBookItem(-1, BetterAdventureModeCore.identifier("lightning_strike"), new FabricItemSettings().maxCount(1)), ItemGroupRegistry.BAM_EQUIPMENT, List.of("bamcontent:lightning_strike"));
    public static final Item WATER_SPRAY_SPELL_BOOK = registerSpellContainerItem("water_spray_spell_book", new SpellBookItem(BetterAdventureModeCore.identifier("water_spray"), new FabricItemSettings().maxCount(1)), ItemGroupRegistry.BAM_EQUIPMENT, List.of("bamcontent:water_spray"));

    // divine spell books
//    public static final Item SINGLE_TARGET_HEAL_SPELL_BOOK = registerSpellContainerItem("single_target_heal_spell_book", new SpellBookItem(BetterAdventureModeCore.identifier("single_target_heal"), new FabricItemSettings().maxCount(1)), ItemGroupRegistry.BAM_EQUIPMENT, List.of("bamcontent:frost_nova"));
    //endregion Accessories

    //region Hotbar Items
    // spell scrolls
    public static final Item FIREBALL_SPELL_SCROLL = registerItem("fireball_spell_scroll", new Item(new FabricItemSettings().maxCount(16)), ItemGroupRegistry.BAM_EQUIPMENT);

    // food
    public static final Item SWEET_BERRY_FOOD = registerItem("sweet_berry_food", new AdventureFoodConsumable(new FabricItemSettings().maxCount(16).food(AdventureFoodComponents.SWEET_BERRY_FOOD)), ItemGroupRegistry.BAM_FOOD);
    public static final Item BROWN_MUSHROOM_FOOD = registerItem("brown_mushroom_food", new AdventureFoodConsumable(new FabricItemSettings().maxCount(16).food(AdventureFoodComponents.BROWN_MUSHROOM_FOOD)), ItemGroupRegistry.BAM_FOOD);
    public static final Item RED_MUSHROOM_FOOD = registerItem("red_mushroom_food", new AdventureFoodConsumable(new FabricItemSettings().maxCount(16).food(AdventureFoodComponents.RED_MUSHROOM_FOOD)), ItemGroupRegistry.BAM_FOOD);
    public static final Item GLOW_BERRY_FOOD = registerItem("glow_berry_food", new AdventureFoodConsumable(new FabricItemSettings().maxCount(16).food(AdventureFoodComponents.GLOW_BERRY_FOOD)), ItemGroupRegistry.BAM_FOOD);
    //endregion Hotbar Items
    
    // weapons
    public static final Item ELEMENTAL_FIRE_WAND = registerFilteredSpellProxyItem("elemental_fire_wand", new StaffItem(ToolMaterials.WOOD, new FabricItemSettings().maxDamage(50)), ItemGroupRegistry.BAM_EQUIPMENT, List.of("bamcontent:burning_touch"), "bamcontent:elemental_fire_wand_pool");
    public static final Item ELEMENTAL_ICE_WAND = registerFilteredSpellProxyItem("elemental_ice_wand", new StaffItem(ToolMaterials.WOOD, new FabricItemSettings().maxDamage(50)), ItemGroupRegistry.BAM_EQUIPMENT, List.of("bamcontent:chilling_touch"), "bamcontent:elemental_ice_wand_pool");

    public static final Item ZWEIHANDER = registerItem("zweihander", new BetterAdventureMode_BasicWeaponItem(DamageTypes.PLAYER_ATTACK, DamageTypes.PLAYER_ATTACK,7, -3.5F, 5, 5,  new Item.Settings().maxDamage(336)), ItemGroupRegistry.BAM_EQUIPMENT);

    public static final Item DEFAULT_EMPTY_HAND_WEAPON = registerItem("default_empty_hand_weapon", new EmptyHandWeapon(1, -3.0F, 1, new Item.Settings()), null);

    // test items
    public static final Item TEST_NECKLACE = registerItem("test_necklace", new TrinketItem(new FabricItemSettings().maxCount(1)), ItemGroupRegistry.BAM_EQUIPMENT);
    public static final Item TEST_WAND = registerFilteredSpellProxyItem("test_wand", new StaffItem(ToolMaterials.WOOD, new FabricItemSettings().maxDamage(50)), ItemGroupRegistry.BAM_EQUIPMENT, List.of(), "bamcontent:test_wand_pool");
    public static final Item TEST_BELT = registerItem("test_belt", new TrinketItem(new FabricItemSettings().maxCount(1)), ItemGroupRegistry.BAM_EQUIPMENT);
    public static final Item TEST_RING = registerItem("test_ring", new TrinketItem(new FabricItemSettings().maxCount(1)), ItemGroupRegistry.BAM_EQUIPMENT);
    public static final Item MANA_TEST_BELT = registerItem("mana_test_belt", new ManaTestBeltItem(new FabricItemSettings().maxCount(1)), ItemGroups.COMBAT);
    public static final Item TEST_BUCKLER = registerItem("test_buckler", new BetterAdventureMode_BasicShieldItem(2, 0.2, true, 3.0, 2, new FabricItemSettings().maxDamage(336)), ItemGroups.COMBAT);
    public static final Item TEST_NORMAL_SHIELD = registerItem("test_normal_shield", new BetterAdventureMode_BasicShieldItem(3, 0.5, true, 1.5, 3, new FabricItemSettings().maxDamage(336)), ItemGroups.COMBAT);
    public static final Item TEST_TOWER_SHIELD = registerItem("test_tower_shield", new BetterAdventureMode_BasicShieldItem(5, 1.0, false, 1.0, 5, new FabricItemSettings().maxDamage(336)), ItemGroups.COMBAT);
    public static final Item TEST_SWORD = registerItem("test_sword", new BetterAdventureMode_BasicWeaponItem(DamageTypesRegistry.PLAYER_SLASHING_DAMAGE_TYPE, DamageTypesRegistry.PLAYER_PIERCING_DAMAGE_TYPE, 4, -3.0F, 3, 2, new FabricItemSettings().maxDamage(336)), ItemGroups.COMBAT);
    public static final Item TEST_AURA_NECKLACE = registerItem("test_aura_necklace", new AuraGrantingNecklaceTrinketItem((AuraStatusEffect) StatusEffectsRegistry.TEST_AURA_EFFECT, new FabricItemSettings().maxCount(1)), ItemGroups.COMBAT);


    private static Item registerItem(String name, Item item, @Nullable RegistryKey<ItemGroup> itemGroup) {

        if (itemGroup != null) {
            ItemGroupEvents.modifyEntriesEvent(itemGroup).register(content -> {
                content.add(item);
            });
        }
        return Registry.register(Registries.ITEM, BetterAdventureModeCore.identifier(name), item);
    }

    private static Item registerSpellContainerItem(String name, Item item, @Nullable RegistryKey<ItemGroup> itemGroup, List<String> spells) {

        SpellContainer container = new SpellContainer(false, null, 0, spells);
        SpellRegistry.book_containers.put(BetterAdventureModeCore.identifier(name), container);

        if (itemGroup != null) {
            ItemGroupEvents.modifyEntriesEvent(itemGroup).register(content -> {
                content.add(item);
            });
        }
        return Registry.register(Registries.ITEM, BetterAdventureModeCore.identifier(name), item);
    }

    private static Item registerFilteredSpellProxyItem(String name, Item item, @Nullable RegistryKey<ItemGroup> itemGroup, List<String> spells, String proxyPool) {

        SpellContainer container = BetterAdventureModCoreItemUtils.setProxyPool(new SpellContainer(true, null, 0, spells), proxyPool);
        SpellRegistry.book_containers.put(BetterAdventureModeCore.identifier(name), container);

        if (itemGroup != null) {
            ItemGroupEvents.modifyEntriesEvent(itemGroup).register(content -> {
                content.add(item);
            });
        }
        return Registry.register(Registries.ITEM, BetterAdventureModeCore.identifier(name), item);
    }

    public static void init() {
    }
}
