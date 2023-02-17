package com.github.theredbrain.rpgmod.registry;

import com.github.theredbrain.rpgmod.RPGMod;
import com.github.theredbrain.rpgmod.effect.FoodStatusEffect;
import com.github.theredbrain.rpgmod.item.AdventureFoodConsumable;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.*;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ItemRegistry {
    
    // food
    public static final AdventureFoodConsumable BERRY_FOOD = new AdventureFoodConsumable((FoodStatusEffect) StatusEffectsRegistry.BERRY_FOOD_EFFECT, 600, new FabricItemSettings().maxCount(16).group(RPGMod.RPG_FOOD));
    public static final AdventureFoodConsumable BROWN_MUSHROOM_FOOD = new AdventureFoodConsumable((FoodStatusEffect) StatusEffectsRegistry.BROWN_MUSHROOM_FOOD_EFFECT, 600, new FabricItemSettings().maxCount(16).group(RPGMod.RPG_FOOD));
    public static final AdventureFoodConsumable RED_MUSHROOM_FOOD = new AdventureFoodConsumable((FoodStatusEffect) StatusEffectsRegistry.RED_MUSHROOM_FOOD_EFFECT, 600, new FabricItemSettings().maxCount(16).group(RPGMod.RPG_FOOD));

    // weapons
    public static final SwordItem ZWEIHANDER = new SwordItem(ToolMaterials.IRON, 7, -3.5F, (new Item.Settings()).group(RPGMod.RPG_ITEM));

    public static void registerBlockItems() {
        // interactive barrier blocks
        Registry.register(Registry.ITEM, new Identifier(RPGMod.MOD_ID, "interactive_stone_block"), new BlockItem(BlockRegistry.INTERACTIVE_STONE_BLOCK, new FabricItemSettings().group(RPGMod.RPG_BLOCK)));

        // interactive log blocks
        Registry.register(Registry.ITEM, new Identifier(RPGMod.MOD_ID, "interactive_oak_log"), new BlockItem(BlockRegistry.INTERACTIVE_OAK_LOG, new FabricItemSettings().group(RPGMod.RPG_BLOCK)));

        // interactive plant blocks
        Registry.register(Registry.ITEM, new Identifier(RPGMod.MOD_ID, "interactive_berry_bush"), new BlockItem(BlockRegistry.INTERACTIVE_BERRY_BUSH, new FabricItemSettings().group(RPGMod.RPG_BLOCK)));
        Registry.register(Registry.ITEM, new Identifier(RPGMod.MOD_ID, "interactive_brown_mushroom"), new BlockItem(BlockRegistry.INTERACTIVE_BROWN_MUSHROOM, new FabricItemSettings().group(RPGMod.RPG_BLOCK)));
        Registry.register(Registry.ITEM, new Identifier(RPGMod.MOD_ID, "interactive_red_mushroom"), new BlockItem(BlockRegistry.INTERACTIVE_RED_MUSHROOM, new FabricItemSettings().group(RPGMod.RPG_BLOCK)));

        // interactive food blocks
        Registry.register(Registry.ITEM, new Identifier(RPGMod.MOD_ID, "interactive_chicken_meal"), new BlockItem(BlockRegistry.INTERACTIVE_CHICKEN_MEAL_BLOCK, new FabricItemSettings().group(RPGMod.RPG_BLOCK)));
    }

    public static void registerItems() {
        // food
        Registry.register(Registry.ITEM, new Identifier(RPGMod.MOD_ID, "berry_food"), BERRY_FOOD);
        Registry.register(Registry.ITEM, new Identifier(RPGMod.MOD_ID, "brown_mushroom_food"), BROWN_MUSHROOM_FOOD);
        Registry.register(Registry.ITEM, new Identifier(RPGMod.MOD_ID, "red_mushroom_food"), RED_MUSHROOM_FOOD);
        // weapons
        Registry.register(Registry.ITEM, new Identifier(RPGMod.MOD_ID, "zweihander"), ZWEIHANDER);
    }
}
