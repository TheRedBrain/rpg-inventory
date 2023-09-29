package com.github.theredbrain.bamcore.registry;

import com.github.theredbrain.bamcore.BetterAdventureModCore;
import com.github.theredbrain.bamcore.item.*;
import com.github.theredbrain.bamcore.spell_engine.DuckSpellContainerMixin;
import dev.emi.trinkets.api.TrinketItem;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
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

    // armor
    // TODO
//    public static final Item LEATHER_HELMET = registerItem("leather_helmet", new CustomModeledDyeableArmorItem(Tags.LEATHER_ARMOR_SET, RPGMod.identifier("armor/custom_chainmail_armor"), 2, 2, CustomArmorMaterials.LEATHER, ArmorItem.Type.HELMET, new FabricItemSettings().maxCount(1)), ItemGroupRegistry.RPG_EQUIPMENT);
//    public static final Item LEATHER_SHOULDERS = registerItem("leather_shoulders", new CustomModeledDyeableArmorItem(Tags.LEATHER_ARMOR_SET, RPGMod.identifier("armor/custom_chainmail_armor"), 2, 2, CustomArmorMaterials.LEATHER, ExtendedArmorItemType.SHOULDERS, new FabricItemSettings().maxCount(1)), ItemGroupRegistry.RPG_EQUIPMENT);
//    public static final Item LEATHER_CHESTPLATE = registerItem("leather_chestplate", new CustomModeledDyeableArmorItem(Tags.LEATHER_ARMOR_SET, RPGMod.identifier("armor/custom_chainmail_armor"), 2, 2, CustomArmorMaterials.LEATHER, ArmorItem.Type.CHESTPLATE, new FabricItemSettings().maxCount(1)), ItemGroupRegistry.RPG_EQUIPMENT);
//    public static final Item LEATHER_GLOVES = registerItem("leather_gloves", new CustomModeledDyeableArmorItem(Tags.LEATHER_ARMOR_SET, RPGMod.identifier("armor/custom_chainmail_armor"), 2, 2, CustomArmorMaterials.LEATHER, ExtendedArmorItemType.GLOVES, new FabricItemSettings().maxCount(1)), ItemGroupRegistry.RPG_EQUIPMENT);
//    public static final Item LEATHER_LEGGINGS = registerItem("leather_leggings", new CustomModeledDyeableArmorItem(Tags.LEATHER_ARMOR_SET, RPGMod.identifier("armor/custom_chainmail_armor"), 2, 2, CustomArmorMaterials.LEATHER, ArmorItem.Type.LEGGINGS, new FabricItemSettings().maxCount(1)), ItemGroupRegistry.RPG_EQUIPMENT);
//    public static final Item LEATHER_BOOTS = registerItem("leather_boots", new CustomModeledDyeableArmorItem(Tags.LEATHER_ARMOR_SET, RPGMod.identifier("armor/custom_chainmail_armor"), 2, 2, CustomArmorMaterials.LEATHER, ArmorItem.Type.BOOTS, new FabricItemSettings().maxCount(1)), ItemGroupRegistry.RPG_EQUIPMENT);
    public static final Item LEATHER_GLOVES_TRINKET = registerItem("leather_gloves_trinket", new ArmorTrinketItem(new FabricItemSettings().maxCount(1)), ItemGroupRegistry.BAM_EQUIPMENT);
    public static final Item LEATHER_SHOULDERS_TRINKET = registerItem("leather_shoulders_trinket", new ArmorTrinketItem(new FabricItemSettings().maxCount(1)), ItemGroupRegistry.BAM_EQUIPMENT);

//    public static final Item CHAINMAIL_HELMET = registerItem("chainmail_helmet", new CustomModeledArmorItem(Tags.CHAINMAIL_ARMOR_SET, RPGMod.identifier("armor/custom_chainmail_armor"), 2, 2, CustomArmorMaterials.CHAINMAIL, ArmorItem.Type.HELMET, new FabricItemSettings().maxCount(1)), ItemGroupRegistry.RPG_EQUIPMENT);
//    public static final Item CHAINMAIL_SHOULDERS = registerItem("chainmail_shoulders", new CustomModeledArmorItem(Tags.CHAINMAIL_ARMOR_SET, RPGMod.identifier("armor/custom_chainmail_armor"), 2, 2, CustomArmorMaterials.CHAINMAIL, ExtendedArmorItemType.SHOULDERS, new FabricItemSettings().maxCount(1)), ItemGroupRegistry.RPG_EQUIPMENT);
//    public static final Item CHAINMAIL_CHESTPLATE = registerItem("chainmail_chestplate", new CustomModeledArmorItem(Tags.CHAINMAIL_ARMOR_SET, RPGMod.identifier("armor/custom_chainmail_armor"), 2, 2, CustomArmorMaterials.CHAINMAIL, ArmorItem.Type.CHESTPLATE, new FabricItemSettings().maxCount(1)), ItemGroupRegistry.RPG_EQUIPMENT);
//    public static final Item CHAINMAIL_GLOVES = registerItem("chainmail_gloves", new CustomModeledArmorItem(Tags.CHAINMAIL_ARMOR_SET, RPGMod.identifier("armor/custom_chainmail_armor"), 2, 2, CustomArmorMaterials.CHAINMAIL, ExtendedArmorItemType.GLOVES, new FabricItemSettings().maxCount(1)), ItemGroupRegistry.RPG_EQUIPMENT);
//    public static final Item CHAINMAIL_LEGGINGS = registerItem("chainmail_leggings", new CustomModeledArmorItem(Tags.CHAINMAIL_ARMOR_SET, RPGMod.identifier("armor/custom_chainmail_armor"), 2, 2, CustomArmorMaterials.CHAINMAIL, ArmorItem.Type.LEGGINGS, new FabricItemSettings().maxCount(1)), ItemGroupRegistry.RPG_EQUIPMENT);
//    public static final Item CHAINMAIL_BOOTS = registerItem("chainmail_boots", new CustomModeledArmorItem(Tags.CHAINMAIL_ARMOR_SET, RPGMod.identifier("armor/custom_chainmail_armor"), 2, 2, CustomArmorMaterials.CHAINMAIL, ArmorItem.Type.BOOTS, new FabricItemSettings().maxCount(1)), ItemGroupRegistry.RPG_EQUIPMENT);
    public static final Item CHAINMAIL_GLOVES_TRINKET = registerItem("chainmail_gloves_trinket", new ArmorTrinketItem(new FabricItemSettings().maxCount(1)), ItemGroupRegistry.BAM_EQUIPMENT);
    public static final Item CHAINMAIL_SHOULDERS_TRINKET = registerItem("chainmail_shoulders_trinket", new ArmorTrinketItem(new FabricItemSettings().maxCount(1)), ItemGroupRegistry.BAM_EQUIPMENT);

//    public static final Item IRON_HELMET = registerItem("iron_helmet", new CustomModeledArmorItem(Tags.IRON_ARMOR_SET, RPGMod.identifier("armor/custom_iron_armor"), 2, 2, CustomArmorMaterials.IRON, ArmorItem.Type.HELMET, new FabricItemSettings().maxCount(1)), ItemGroupRegistry.RPG_EQUIPMENT);
//    public static final Item IRON_SHOULDERS = registerItem("iron_shoulders", new CustomModeledArmorItem(Tags.IRON_ARMOR_SET, RPGMod.identifier("armor/custom_iron_armor"), 2, 2, CustomArmorMaterials.IRON, ExtendedArmorItemType.SHOULDERS, new FabricItemSettings().maxCount(1)), ItemGroupRegistry.RPG_EQUIPMENT);
//    public static final Item IRON_CHESTPLATE = registerItem("iron_chestplate", new CustomModeledArmorItem(Tags.IRON_ARMOR_SET, RPGMod.identifier("armor/custom_iron_armor"), 2, 2, CustomArmorMaterials.IRON, ArmorItem.Type.CHESTPLATE, new FabricItemSettings().maxCount(1)), ItemGroupRegistry.RPG_EQUIPMENT);
//    public static final Item IRON_GLOVES = registerItem("iron_gloves", new CustomModeledArmorItem(Tags.IRON_ARMOR_SET, RPGMod.identifier("armor/custom_iron_armor"), 2, 2, CustomArmorMaterials.IRON, ExtendedArmorItemType.GLOVES, new FabricItemSettings().maxCount(1)), ItemGroupRegistry.RPG_EQUIPMENT);
//    public static final Item IRON_LEGGINGS = registerItem("iron_leggings", new CustomModeledArmorItem(Tags.IRON_ARMOR_SET, RPGMod.identifier("armor/custom_iron_armor"), 2, 2, CustomArmorMaterials.IRON, ArmorItem.Type.LEGGINGS, new FabricItemSettings().maxCount(1)), ItemGroupRegistry.RPG_EQUIPMENT);
//    public static final Item IRON_BOOTS = registerItem("iron_boots", new CustomModeledArmorItem(Tags.IRON_ARMOR_SET, RPGMod.identifier("armor/custom_iron_armor"), 2, 2, CustomArmorMaterials.IRON, ArmorItem.Type.BOOTS, new FabricItemSettings().maxCount(1)), ItemGroupRegistry.RPG_EQUIPMENT);
    public static final Item IRON_GLOVES_TRINKET = registerItem("iron_gloves_trinket", new ArmorTrinketItem(new FabricItemSettings().maxCount(1)), ItemGroupRegistry.BAM_EQUIPMENT);
    public static final Item IRON_SHOULDERS_TRINKET = registerItem("iron_shoulders_trinket", new ArmorTrinketItem(new FabricItemSettings().maxCount(1)), ItemGroupRegistry.BAM_EQUIPMENT);

//    public static final Item DIAMOND_HELMET = registerItem("diamond_helmet", new CustomModeledArmorItem(Tags.DIAMOND_ARMOR_SET, RPGMod.identifier("armor/custom_diamond_armor"), 2, 2, CustomArmorMaterials.DIAMOND, ArmorItem.Type.HELMET, new FabricItemSettings().maxCount(1)), ItemGroupRegistry.RPG_EQUIPMENT);
//    public static final Item DIAMOND_SHOULDERS = registerItem("diamond_shoulders", new CustomModeledArmorItem(Tags.DIAMOND_ARMOR_SET, RPGMod.identifier("armor/custom_diamond_armor"), 2, 2, CustomArmorMaterials.DIAMOND, ExtendedArmorItemType.SHOULDERS, new FabricItemSettings().maxCount(1)), ItemGroupRegistry.RPG_EQUIPMENT);
//    public static final Item DIAMOND_CHESTPLATE = registerItem("diamond_chestplate", new CustomModeledArmorItem(Tags.DIAMOND_ARMOR_SET, RPGMod.identifier("armor/custom_diamond_armor"), 2, 2, CustomArmorMaterials.DIAMOND, ArmorItem.Type.CHESTPLATE, new FabricItemSettings().maxCount(1)), ItemGroupRegistry.RPG_EQUIPMENT);
//    public static final Item DIAMOND_GLOVES = registerItem("diamond_gloves", new CustomModeledArmorItem(Tags.DIAMOND_ARMOR_SET, RPGMod.identifier("armor/custom_diamond_armor"), 2, 2, CustomArmorMaterials.DIAMOND, ExtendedArmorItemType.GLOVES, new FabricItemSettings().maxCount(1)), ItemGroupRegistry.RPG_EQUIPMENT);
//    public static final Item DIAMOND_LEGGINGS = registerItem("diamond_leggings", new CustomModeledArmorItem(Tags.DIAMOND_ARMOR_SET, RPGMod.identifier("armor/custom_diamond_armor"), 2, 2, CustomArmorMaterials.DIAMOND, ArmorItem.Type.LEGGINGS, new FabricItemSettings().maxCount(1)), ItemGroupRegistry.RPG_EQUIPMENT);
//    public static final Item DIAMOND_BOOTS = registerItem("diamond_boots", new CustomModeledArmorItem(Tags.DIAMOND_ARMOR_SET, RPGMod.identifier("armor/custom_diamond_armor"), 2, 2, CustomArmorMaterials.DIAMOND, ArmorItem.Type.BOOTS, new FabricItemSettings().maxCount(1)), ItemGroupRegistry.RPG_EQUIPMENT);
    public static final Item DIAMOND_GLOVES_TRINKET = registerItem("diamond_gloves_trinket", new ArmorTrinketItem(new FabricItemSettings().maxCount(1)), ItemGroupRegistry.BAM_EQUIPMENT);
    public static final Item DIAMOND_SHOULDERS_TRINKET = registerItem("diamond_shoulders_trinket", new ArmorTrinketItem(new FabricItemSettings().maxCount(1)), ItemGroupRegistry.BAM_EQUIPMENT);

//    public static final Item GOLDEN_HELMET = registerItem("golden_helmet", new CustomModeledArmorItem(Tags.GOLDEN_ARMOR_SET, RPGMod.identifier("armor/custom_gold_armor"), 2, 2, CustomArmorMaterials.GOLD, ArmorItem.Type.HELMET, new FabricItemSettings().maxCount(1)), ItemGroupRegistry.RPG_EQUIPMENT);
//    public static final Item GOLDEN_SHOULDERS = registerItem("golden_shoulders", new CustomModeledArmorItem(Tags.GOLDEN_ARMOR_SET, RPGMod.identifier("armor/custom_gold_armor"), 2, 2, CustomArmorMaterials.GOLD, ExtendedArmorItemType.SHOULDERS, new FabricItemSettings().maxCount(1)), ItemGroupRegistry.RPG_EQUIPMENT);
//    public static final Item GOLDEN_CHESTPLATE = registerItem("golden_chestplate", new CustomModeledArmorItem(Tags.GOLDEN_ARMOR_SET, RPGMod.identifier("armor/custom_gold_armor"), 2, 2, CustomArmorMaterials.GOLD, ArmorItem.Type.CHESTPLATE, new FabricItemSettings().maxCount(1)), ItemGroupRegistry.RPG_EQUIPMENT);
//    public static final Item GOLDEN_GLOVES = registerItem("golden_gloves", new CustomModeledArmorItem(Tags.GOLDEN_ARMOR_SET, RPGMod.identifier("armor/custom_gold_armor"), 2, 2, CustomArmorMaterials.GOLD, ExtendedArmorItemType.GLOVES, new FabricItemSettings().maxCount(1)), ItemGroupRegistry.RPG_EQUIPMENT);
//    public static final Item GOLDEN_LEGGINGS = registerItem("golden_leggings", new CustomModeledArmorItem(Tags.GOLDEN_ARMOR_SET, RPGMod.identifier("armor/custom_gold_armor"), 2, 2, CustomArmorMaterials.GOLD, ArmorItem.Type.LEGGINGS, new FabricItemSettings().maxCount(1)), ItemGroupRegistry.RPG_EQUIPMENT);
//    public static final Item GOLDEN_BOOTS = registerItem("golden_boots", new CustomModeledArmorItem(Tags.GOLDEN_ARMOR_SET, RPGMod.identifier("armor/custom_gold_armor"), 2, 2, CustomArmorMaterials.GOLD, ArmorItem.Type.BOOTS, new FabricItemSettings().maxCount(1)), ItemGroupRegistry.RPG_EQUIPMENT);
    public static final Item GOLDEN_GLOVES_TRINKET = registerItem("golden_gloves_trinket", new ArmorTrinketItem(new FabricItemSettings().maxCount(1)), ItemGroupRegistry.BAM_EQUIPMENT);
    public static final Item GOLDEN_SHOULDERS_TRINKET = registerItem("golden_shoulders_trinket", new ArmorTrinketItem(new FabricItemSettings().maxCount(1)), ItemGroupRegistry.BAM_EQUIPMENT);

//    public static final Item NETHERITE_HELMET = registerItem("netherite_helmet", new CustomModeledArmorItem(Tags.NETHERITE_ARMOR_SET, RPGMod.identifier("armor/custom_netherite_armor"), 2, 2, CustomArmorMaterials.NETHERITE, ArmorItem.Type.HELMET, new FabricItemSettings().maxCount(1)), ItemGroupRegistry.RPG_EQUIPMENT);
//    public static final Item NETHERITE_SHOULDERS = registerItem("netherite_shoulders", new CustomModeledArmorItem(Tags.NETHERITE_ARMOR_SET, RPGMod.identifier("armor/custom_netherite_armor"), 2, 2, CustomArmorMaterials.NETHERITE, ExtendedArmorItemType.SHOULDERS, new FabricItemSettings().maxCount(1)), ItemGroupRegistry.RPG_EQUIPMENT);
//    public static final Item NETHERITE_CHESTPLATE = registerItem("netherite_chestplate", new CustomModeledArmorItem(Tags.NETHERITE_ARMOR_SET, RPGMod.identifier("armor/custom_netherite_armor"), 2, 2, CustomArmorMaterials.NETHERITE, ArmorItem.Type.CHESTPLATE, new FabricItemSettings().maxCount(1)), ItemGroupRegistry.RPG_EQUIPMENT);
//    public static final Item NETHERITE_GLOVES = registerItem("netherite_gloves", new CustomModeledArmorItem(Tags.NETHERITE_ARMOR_SET, RPGMod.identifier("armor/custom_netherite_armor"), 2, 2, CustomArmorMaterials.NETHERITE, ExtendedArmorItemType.GLOVES, new FabricItemSettings().maxCount(1)), ItemGroupRegistry.RPG_EQUIPMENT);
//    public static final Item NETHERITE_LEGGINGS = registerItem("netherite_leggings", new CustomModeledArmorItem(Tags.NETHERITE_ARMOR_SET, RPGMod.identifier("armor/custom_netherite_armor"), 2, 2, CustomArmorMaterials.NETHERITE, ArmorItem.Type.LEGGINGS, new FabricItemSettings().maxCount(1)), ItemGroupRegistry.RPG_EQUIPMENT);
//    public static final Item NETHERITE_BOOTS = registerItem("netherite_boots", new CustomModeledArmorItem(Tags.NETHERITE_ARMOR_SET, RPGMod.identifier("armor/custom_netherite_armor"), 2, 2, CustomArmorMaterials.NETHERITE, ArmorItem.Type.BOOTS, new FabricItemSettings().maxCount(1)), ItemGroupRegistry.RPG_EQUIPMENT);
    public static final Item NETHERITE_GLOVES_TRINKET = registerItem("netherite_gloves_trinket", new ArmorTrinketItem(new FabricItemSettings().maxCount(1)), ItemGroupRegistry.BAM_EQUIPMENT);
    public static final Item NETHERITE_SHOULDERS_TRINKET = registerItem("netherite_shoulders_trinket", new ArmorTrinketItem(new FabricItemSettings().maxCount(1)), ItemGroupRegistry.BAM_EQUIPMENT);

    // --- accessories ---
    // belts
    public static final Item TEST_BELT = registerItem("test_belt", new TrinketItem(new FabricItemSettings().maxCount(1)), ItemGroupRegistry.BAM_EQUIPMENT);
    // necklaces
    public static final Item TEST_NECKLACE = registerItem("test_necklace", new TrinketItem(new FabricItemSettings().maxCount(1)), ItemGroupRegistry.BAM_EQUIPMENT);
    // rings
    public static final Item TEST_RING = registerItem("test_ring", new TrinketItem(new FabricItemSettings().maxCount(1)), ItemGroupRegistry.BAM_EQUIPMENT);
//    public static final Item TWO_SLOT_SPELL_ITEM = registerTwoSlotSpellItem("two_slot_spell", ((DuckSpellBooksMixin)SpellBooks).createTwoSlotSpellItem(RPGMod.identifier("")), ItemGroupRegistry.RPG_EQUIPMENT);
//    public static final Item FIREBALL_SPELL_BOOK = registerTwoSlotSpellItem("fireball_spell_book", SpellBooks.create(RPGMod.identifier("fireball")), ItemGroupRegistry.RPG_EQUIPMENT);
    public static final Item FIREBALL_SPELL_BOOK = registerSpellContainerItem("fireball_spell_book", new TwoSlotSpellItem(BetterAdventureModCore.identifier("fireball"), new FabricItemSettings().maxCount(1)), ItemGroupRegistry.BAM_EQUIPMENT, List.of("rpgmod:fireball"));
    public static final Item FIREBALL_SPELL_SCROLL = registerItem("fireball_spell_scroll", new Item(new FabricItemSettings().maxCount(16)), ItemGroupRegistry.BAM_EQUIPMENT);
    public static final Item FROST_NOVA_SPELL_BOOK = registerSpellContainerItem("frost_nova_spell_book", new SpellBookItem(BetterAdventureModCore.identifier("frost_nova"), new FabricItemSettings().maxCount(1)), ItemGroupRegistry.BAM_EQUIPMENT, List.of("rpgmod:frost_nova"));

    public static final Item TEST_WAND = registerFilteredSpellProxyItem("test_wand", new StaffItem(ToolMaterials.WOOD, new FabricItemSettings().maxDamage(50)), ItemGroupRegistry.BAM_EQUIPMENT, List.of(), "rpgmod:test_wand_pool");

    public static final Item TWO_SPELL_SLOT_RING_ITEM = registerItem("two_spell_slot_ring", new TwoSpellSlotRingItem(new FabricItemSettings().maxCount(1)), ItemGroupRegistry.BAM_EQUIPMENT);
    public static final Item TWO_HEALTH_REGENERATION_RING_ITEM = registerItem("two_health_regeneration_ring", new TwoHealthRegenerationRingItem(new FabricItemSettings().maxCount(1)), ItemGroupRegistry.BAM_EQUIPMENT);

    // mounts
//    public static final Item MOUNT_TOKEN = registerItem("mount_token", new MountProvidingItem(new FabricItemSettings().maxCount(1)), ItemGroupRegistry.RPG_EQUIPMENT);

    // food
    public static final Item BERRY_FOOD = registerItem("berry_food", new AdventureFoodConsumable(new FabricItemSettings().maxCount(16).food(AdventureFoodComponents.BERRY_FOOD)), ItemGroupRegistry.BAM_FOOD);
    public static final Item BROWN_MUSHROOM_FOOD = registerItem("brown_mushroom_food", new AdventureFoodConsumable(new FabricItemSettings().maxCount(16).food(AdventureFoodComponents.BROWN_MUSHROOM_FOOD)), ItemGroupRegistry.BAM_FOOD);
    public static final Item RED_MUSHROOM_FOOD = registerItem("red_mushroom_food", new AdventureFoodConsumable(new FabricItemSettings().maxCount(16).food(AdventureFoodComponents.RED_MUSHROOM_FOOD)), ItemGroupRegistry.BAM_FOOD);

    // weapons
    public static final Item ZWEIHANDER = registerItem("zweihander", new SwordItem(CustomToolMaterials.IRON, 7, -3.5F, new Item.Settings()), ItemGroupRegistry.BAM_EQUIPMENT);
    public static final Item DEFAULT_EMPTY_HAND_WEAPON = registerItem("default_empty_hand_weapon", new EmptyHandWeapon(0, -3.5F, new Item.Settings().maxCount(1)), null);

    private static Item registerItem(String name, Item item, @Nullable RegistryKey<ItemGroup> itemGroup) {

        if (itemGroup != null) {
            ItemGroupEvents.modifyEntriesEvent(itemGroup).register(content -> {
                content.add(item);
            });
        }
        return Registry.register(Registries.ITEM, BetterAdventureModCore.identifier(name), item);
    }

    private static Item registerSpellContainerItem(String name, Item item, @Nullable RegistryKey<ItemGroup> itemGroup, List<String> spells) {

        SpellContainer container = new SpellContainer(false, null, 0, spells);
        SpellRegistry.book_containers.put(BetterAdventureModCore.identifier(name), container);

        if (itemGroup != null) {
            ItemGroupEvents.modifyEntriesEvent(itemGroup).register(content -> {
                content.add(item);
            });
        }
        return Registry.register(Registries.ITEM, BetterAdventureModCore.identifier(name), item);
    }

    private static Item registerFilteredSpellProxyItem(String name, Item item, @Nullable RegistryKey<ItemGroup> itemGroup, List<String> spells, String proxyPool) {

        SpellContainer container = new SpellContainer(true, null, 0, spells);
        ((DuckSpellContainerMixin) container).setProxyPool(proxyPool);
        SpellRegistry.book_containers.put(BetterAdventureModCore.identifier(name), container);

        if (itemGroup != null) {
            ItemGroupEvents.modifyEntriesEvent(itemGroup).register(content -> {
                content.add(item);
            });
        }
        return Registry.register(Registries.ITEM, BetterAdventureModCore.identifier(name), item);
    }

    public static void init() {
    }
}
