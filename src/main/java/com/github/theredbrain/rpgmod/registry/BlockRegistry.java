package com.github.theredbrain.rpgmod.registry;

import com.github.theredbrain.rpgmod.RPGMod;
import com.github.theredbrain.rpgmod.block.InteractiveAdventureFoodBlock;
import com.github.theredbrain.rpgmod.block.InteractiveAdventureFullToAirBlock;
import com.github.theredbrain.rpgmod.block.InteractiveAdventureLogBlock;
import com.github.theredbrain.rpgmod.block.InteractiveAdventurePlantBlock;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Material;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.Items;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class BlockRegistry {

    // interactive barrier blocks
    public static final InteractiveAdventureFullToAirBlock INTERACTIVE_STONE_BLOCK = new InteractiveAdventureFullToAirBlock(null, Tags.INTERACTIVE_STONE_BLOCK_TOOLS, true, 1/*TODO play test*/, FabricBlockSettings.of(Material.STONE).ticksRandomly().sounds(BlockSoundGroup.STONE));

    // interactive log blocks
    public static final InteractiveAdventureLogBlock INTERACTIVE_OAK_LOG = new InteractiveAdventureLogBlock(Items.OAK_LOG, Tags.INTERACTIVE_OAK_LOG_TOOLS, true, 1/*TODO play test*/, FabricBlockSettings.of(Material.WOOD).ticksRandomly().sounds(BlockSoundGroup.WOOD));

    // interactive plant blocks
    public static final InteractiveAdventurePlantBlock INTERACTIVE_BERRY_BUSH = new InteractiveAdventurePlantBlock(1, ItemRegistry.BERRY_FOOD, null, false, 1/*TODO play test*/, FabricBlockSettings.of(Material.PLANT).ticksRandomly().noCollision().sounds(BlockSoundGroup.SWEET_BERRY_BUSH));
    public static final InteractiveAdventurePlantBlock INTERACTIVE_RED_MUSHROOM = new InteractiveAdventurePlantBlock(0, ItemRegistry.RED_MUSHROOM_FOOD, null, false, 1/*TODO play test*/, FabricBlockSettings.of(Material.PLANT).ticksRandomly().noCollision().sounds(BlockSoundGroup.CROP));
    public static final InteractiveAdventurePlantBlock INTERACTIVE_BROWN_MUSHROOM = new InteractiveAdventurePlantBlock(0, ItemRegistry.BROWN_MUSHROOM_FOOD, null, false, 1/*TODO play test*/, FabricBlockSettings.of(Material.PLANT).ticksRandomly().noCollision().sounds(BlockSoundGroup.CROP));

    // interactive food blocks
    public static final InteractiveAdventureFoodBlock INTERACTIVE_CHICKEN_MEAL_BLOCK = new InteractiveAdventureFoodBlock(StatusEffectsRegistry.CHICKEN_MEAL_FOOD_EFFECT, 600, 1,/*TODO play test*/ FabricBlockSettings.of(Material.CAKE).ticksRandomly().sounds(BlockSoundGroup.WOOL));

    public static void registerBlocks() {
        // interactive barrier blocks
        Registry.register(Registry.BLOCK, new Identifier(RPGMod.MOD_ID, "interactive_stone_block"), INTERACTIVE_STONE_BLOCK);

        // interactive log blocks
        Registry.register(Registry.BLOCK, new Identifier(RPGMod.MOD_ID, "interactive_oak_log"), INTERACTIVE_OAK_LOG);

        // interactive plant blocks
        Registry.register(Registry.BLOCK, new Identifier(RPGMod.MOD_ID, "interactive_berry_bush"), INTERACTIVE_BERRY_BUSH);
        Registry.register(Registry.BLOCK, new Identifier(RPGMod.MOD_ID, "interactive_brown_mushroom"), INTERACTIVE_BROWN_MUSHROOM);
        Registry.register(Registry.BLOCK, new Identifier(RPGMod.MOD_ID, "interactive_red_mushroom"), INTERACTIVE_RED_MUSHROOM);

        // interactive food blocks
        Registry.register(Registry.BLOCK, new Identifier(RPGMod.MOD_ID, "interactive_chicken_meal"), INTERACTIVE_CHICKEN_MEAL_BLOCK);
    }
}
