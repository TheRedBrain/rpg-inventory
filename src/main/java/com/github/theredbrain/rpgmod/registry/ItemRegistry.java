package com.github.theredbrain.rpgmod.registry;

import com.github.theredbrain.rpgmod.RPGMod;
import com.github.theredbrain.rpgmod.entity.ExtendedEquipmentSlot;
import com.github.theredbrain.rpgmod.item.*;
import com.github.theredbrain.rpgmod.item.CustomModeledVanillaArmorItem;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;

public class ItemRegistry {

    // armour
    // custom iron


//    public static final CustomModeledVanillaArmorItem LEATHER_SHOULDERS = new CustomModeledVanillaArmorItem(CustomArmorMaterials.LEATHER, ExtendedEquipmentSlot.SHOULDERS, new FabricItemSettings().maxCount(1));
    public static final CustomModeledVanillaArmorItem CHAINMAIL_SHOULDERS = new CustomModeledVanillaArmorItem(CustomArmorMaterials.CHAINMAIL, ExtendedEquipmentSlot.SHOULDERS, Tags.CHAINMAIL_ARMOR_SET, new Identifier(RPGMod.MOD_ID, "armor/custom_chainmail_armor"), new FabricItemSettings().maxCount(1));
public static final CustomModeledVanillaArmorItem DIAMOND_SHOULDERS = new CustomModeledVanillaArmorItem(CustomArmorMaterials.DIAMOND, ExtendedEquipmentSlot.SHOULDERS, Tags.DIAMOND_ARMOR_SET, new Identifier(RPGMod.MOD_ID, "armor/custom_diamond_armor"), new FabricItemSettings().maxCount(1));
    public static final CustomModeledVanillaArmorItem GOLD_SHOULDERS = new CustomModeledVanillaArmorItem(CustomArmorMaterials.GOLD, ExtendedEquipmentSlot.SHOULDERS, Tags.GOLDEN_ARMOR_SET, new Identifier(RPGMod.MOD_ID, "armor/custom_gold_armor"), new FabricItemSettings().maxCount(1));
public static final CustomModeledVanillaArmorItem IRON_SHOULDERS = new CustomModeledVanillaArmorItem(CustomArmorMaterials.IRON, ExtendedEquipmentSlot.SHOULDERS, Tags.IRON_ARMOR_SET, new Identifier(RPGMod.MOD_ID, "armor/custom_iron_armor"), new FabricItemSettings().maxCount(1));
    public static final CustomModeledVanillaArmorItem NETHERITE_SHOULDERS = new CustomModeledVanillaArmorItem(CustomArmorMaterials.NETHERITE, ExtendedEquipmentSlot.SHOULDERS, Tags.NETHERITE_ARMOR_SET, new Identifier(RPGMod.MOD_ID, "armor/custom_netherite_armor"), new FabricItemSettings().maxCount(1));

//    public static final CustomModeledVanillaArmorItem LEATHER_GLOVES = new CustomModeledVanillaArmorItem(CustomArmorMaterials.LEATHER, ExtendedEquipmentSlot.GLOVES, new FabricItemSettings().maxCount(1));
    public static final CustomModeledVanillaArmorItem CHAINMAIL_GLOVES = new CustomModeledVanillaArmorItem(CustomArmorMaterials.CHAINMAIL, ExtendedEquipmentSlot.GLOVES, Tags.CHAINMAIL_ARMOR_SET, new Identifier(RPGMod.MOD_ID, "armor/custom_chainmail_armor"), new FabricItemSettings().maxCount(1));
public static final CustomModeledVanillaArmorItem DIAMOND_GLOVES = new CustomModeledVanillaArmorItem(CustomArmorMaterials.DIAMOND, ExtendedEquipmentSlot.GLOVES, Tags.DIAMOND_ARMOR_SET, new Identifier(RPGMod.MOD_ID, "armor/custom_diamond_armor"), new FabricItemSettings().maxCount(1));
    public static final CustomModeledVanillaArmorItem GOLD_GLOVES = new CustomModeledVanillaArmorItem(CustomArmorMaterials.GOLD, ExtendedEquipmentSlot.GLOVES, Tags.GOLDEN_ARMOR_SET, new Identifier(RPGMod.MOD_ID, "armor/custom_gold_armor"), new FabricItemSettings().maxCount(1));
public static final CustomModeledVanillaArmorItem IRON_GLOVES = new CustomModeledVanillaArmorItem(CustomArmorMaterials.IRON, ExtendedEquipmentSlot.GLOVES, Tags.IRON_ARMOR_SET, new Identifier(RPGMod.MOD_ID, "armor/custom_iron_armor"), new FabricItemSettings().maxCount(1));
    public static final CustomModeledVanillaArmorItem NETHERITE_GLOVES = new CustomModeledVanillaArmorItem(CustomArmorMaterials.NETHERITE, ExtendedEquipmentSlot.GLOVES, Tags.NETHERITE_ARMOR_SET, new Identifier(RPGMod.MOD_ID, "armor/custom_netherite_armor"), new FabricItemSettings().maxCount(1));

    public static final PlayerSkinArmorItem PLAYER_SKIN_ARMOR_ITEM = new PlayerSkinArmorItem(new FabricItemSettings().maxCount(1));
    public static final NoLegsPlayerSkinArmorItem NO_LEGS_PLAYER_SKIN_ARMOR_ITEM = new NoLegsPlayerSkinArmorItem(new FabricItemSettings().maxCount(1));

    // accessories
    public static final AccessoryRingItem IRON_RING = new AccessoryRingItem(SoundEvents.ITEM_ARMOR_EQUIP_IRON, new FabricItemSettings().maxCount(1));

    // food
    public static final AdventureFoodConsumable BERRY_FOOD = new AdventureFoodConsumable(new FabricItemSettings().maxCount(16).food(AdventureFoodComponents.BERRY_FOOD));
    public static final AdventureFoodConsumable BROWN_MUSHROOM_FOOD = new AdventureFoodConsumable(new FabricItemSettings().maxCount(16).food(AdventureFoodComponents.BROWN_MUSHROOM_FOOD));
    public static final AdventureFoodConsumable RED_MUSHROOM_FOOD = new AdventureFoodConsumable(new FabricItemSettings().maxCount(16).food(AdventureFoodComponents.RED_MUSHROOM_FOOD));

    // weapons
    public static final SwordItem ZWEIHANDER = new SwordItem(CustomToolMaterials.IRON, 7, -3.5F, new Item.Settings());
    public static final EmptyHandWeapon DEFAULT_EMPTY_HAND_WEAPON = new EmptyHandWeapon(0, -3.5F, new Item.Settings().maxCount(1));

    // block items
    // interactive barrier blocks
    public static final BlockItem INTERACTIVE_STONE_BLOCK_ITEM = new BlockItem(BlockRegistry.INTERACTIVE_STONE_BLOCK, new FabricItemSettings());

    // interactive log blocks
    public static final BlockItem INTERACTIVE_OAK_LOG_ITEM = new BlockItem(BlockRegistry.INTERACTIVE_OAK_LOG, new FabricItemSettings());

    // interactive plant blocks
    public static final BlockItem INTERACTIVE_BERRY_BUSH_ITEM = new BlockItem(BlockRegistry.INTERACTIVE_BERRY_BUSH_BLOCK, new FabricItemSettings());
    public static final BlockItem INTERACTIVE_BROWN_MUSHROOM_ITEM = new BlockItem(BlockRegistry.INTERACTIVE_BROWN_MUSHROOM_BLOCK, new FabricItemSettings());
    public static final BlockItem INTERACTIVE_RED_MUSHROOM_ITEM = new BlockItem(BlockRegistry.INTERACTIVE_RED_MUSHROOM_BLOCK, new FabricItemSettings());

    // interactive food blocks
    public static final BlockItem INTERACTIVE_CHICKEN_MEAL_BLOCK_ITEM = new BlockItem(BlockRegistry.INTERACTIVE_CHICKEN_MEAL_BLOCK, new FabricItemSettings());

    public static void registerBlockItems() {
        // interactive barrier blocks
        Registry.register(Registries.ITEM, new Identifier(RPGMod.MOD_ID, "interactive_stone_block"), INTERACTIVE_STONE_BLOCK_ITEM);

        // interactive log blocks
        Registry.register(Registries.ITEM, new Identifier(RPGMod.MOD_ID, "interactive_oak_log"), INTERACTIVE_OAK_LOG_ITEM);

        // interactive plant blocks
        Registry.register(Registries.ITEM, new Identifier(RPGMod.MOD_ID, "interactive_berry_bush"), INTERACTIVE_BERRY_BUSH_ITEM);
        Registry.register(Registries.ITEM, new Identifier(RPGMod.MOD_ID, "interactive_brown_mushroom"), INTERACTIVE_BROWN_MUSHROOM_ITEM);
        Registry.register(Registries.ITEM, new Identifier(RPGMod.MOD_ID, "interactive_red_mushroom"), INTERACTIVE_RED_MUSHROOM_ITEM);

        // interactive food blocks
        Registry.register(Registries.ITEM, new Identifier(RPGMod.MOD_ID, "interactive_chicken_meal"), INTERACTIVE_CHICKEN_MEAL_BLOCK_ITEM);
    }

    public static void registerItems() {
        // armour
        // gloves
//        Registry.register(Registries.ITEM, new Identifier(RPGMod.MOD_ID, "leather_gloves"), LEATHER_GLOVES);
        Registry.register(Registries.ITEM, new Identifier(RPGMod.MOD_ID, "chainmail_gloves"), CHAINMAIL_GLOVES);
        Registry.register(Registries.ITEM, new Identifier(RPGMod.MOD_ID, "diamond_gloves"), DIAMOND_GLOVES);
        Registry.register(Registries.ITEM, new Identifier(RPGMod.MOD_ID, "gold_gloves"), GOLD_GLOVES);
        Registry.register(Registries.ITEM, new Identifier(RPGMod.MOD_ID, "iron_gloves"), IRON_GLOVES);
        Registry.register(Registries.ITEM, new Identifier(RPGMod.MOD_ID, "netherite_gloves"), NETHERITE_GLOVES);

        // shoulders
//        Registry.register(Registries.ITEM, new Identifier(RPGMod.MOD_ID, "leather_shoulders"), LEATHER_SHOULDERS);
        Registry.register(Registries.ITEM, new Identifier(RPGMod.MOD_ID, "chainmail_shoulders"), CHAINMAIL_SHOULDERS);
        Registry.register(Registries.ITEM, new Identifier(RPGMod.MOD_ID, "diamond_shoulders"), DIAMOND_SHOULDERS);
        Registry.register(Registries.ITEM, new Identifier(RPGMod.MOD_ID, "gold_shoulders"), GOLD_SHOULDERS);
        Registry.register(Registries.ITEM, new Identifier(RPGMod.MOD_ID, "iron_shoulders"), IRON_SHOULDERS);
        Registry.register(Registries.ITEM, new Identifier(RPGMod.MOD_ID, "netherite_shoulders"), NETHERITE_SHOULDERS);

        Registry.register(Registries.ITEM, new Identifier(RPGMod.MOD_ID, "player_skin_armor_item"), PLAYER_SKIN_ARMOR_ITEM);
        Registry.register(Registries.ITEM, new Identifier(RPGMod.MOD_ID, "no_legs_player_skin_armor_item"), NO_LEGS_PLAYER_SKIN_ARMOR_ITEM);

        // accessories
        Registry.register(Registries.ITEM, new Identifier(RPGMod.MOD_ID, "iron_ring"), IRON_RING);
        // food
        Registry.register(Registries.ITEM, new Identifier(RPGMod.MOD_ID, "berry_food"), BERRY_FOOD);
        Registry.register(Registries.ITEM, new Identifier(RPGMod.MOD_ID, "brown_mushroom_food"), BROWN_MUSHROOM_FOOD);
        Registry.register(Registries.ITEM, new Identifier(RPGMod.MOD_ID, "red_mushroom_food"), RED_MUSHROOM_FOOD);
        // weapons
        Registry.register(Registries.ITEM, new Identifier(RPGMod.MOD_ID, "zweihander"), ZWEIHANDER);
        Registry.register(Registries.ITEM, new Identifier(RPGMod.MOD_ID, "default_empty_hand_weapon"), DEFAULT_EMPTY_HAND_WEAPON);
    }

    public static void registerItemsToItemGroups() {
        ItemGroupEvents.modifyEntriesEvent(RPGMod.RPG_BLOCK).register(content -> {
            content.add(INTERACTIVE_STONE_BLOCK_ITEM);
            content.add(INTERACTIVE_OAK_LOG_ITEM);
            content.add(INTERACTIVE_BERRY_BUSH_ITEM);
            content.add(INTERACTIVE_BROWN_MUSHROOM_ITEM);
            content.add(INTERACTIVE_RED_MUSHROOM_ITEM);
            content.add(INTERACTIVE_CHICKEN_MEAL_BLOCK_ITEM);
        });
        ItemGroupEvents.modifyEntriesEvent(RPGMod.RPG_ITEM).register(content -> {
//            content.add(LEATHER_GLOVES);
//            content.add(LEATHER_SHOULDERS);
            content.add(CHAINMAIL_GLOVES);
            content.add(CHAINMAIL_SHOULDERS);
            content.add(DIAMOND_GLOVES);
            content.add(DIAMOND_SHOULDERS);
            content.add(GOLD_GLOVES);
            content.add(GOLD_SHOULDERS);
            content.add(IRON_GLOVES);
            content.add(IRON_SHOULDERS);
            content.add(NETHERITE_GLOVES);
            content.add(NETHERITE_SHOULDERS);
            content.add(ZWEIHANDER);
            content.add(IRON_RING);
        });
        ItemGroupEvents.modifyEntriesEvent(RPGMod.RPG_FOOD).register(content -> {
            content.add(BERRY_FOOD);
            content.add(BROWN_MUSHROOM_FOOD);
            content.add(RED_MUSHROOM_FOOD);
        });
    }
}
