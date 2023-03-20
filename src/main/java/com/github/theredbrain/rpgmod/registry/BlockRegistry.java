package com.github.theredbrain.rpgmod.registry;

import com.github.theredbrain.rpgmod.RPGMod;
import com.github.theredbrain.rpgmod.block.InteractiveAdventureFoodBlock;
import com.github.theredbrain.rpgmod.block.InteractiveAdventureFullToAirBlock;
import com.github.theredbrain.rpgmod.block.InteractiveAdventureLogBlock;
import com.github.theredbrain.rpgmod.block.InteractiveAdventurePlantBlock;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Material;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;

public class BlockRegistry {

    // interactive barrier blocks
    public static final InteractiveAdventureFullToAirBlock INTERACTIVE_STONE_BLOCK = new InteractiveAdventureFullToAirBlock(Tags.INTERACTIVE_STONE_BLOCK_TOOLS, true, 1/*TODO play test*/, FabricBlockSettings.of(Material.STONE).ticksRandomly().sounds(BlockSoundGroup.STONE));

    // interactive log blocks
    public static final InteractiveAdventureLogBlock INTERACTIVE_OAK_LOG = new InteractiveAdventureLogBlock(Tags.INTERACTIVE_OAK_LOG_TOOLS, true, 1/*TODO play test*/, FabricBlockSettings.of(Material.WOOD).ticksRandomly().sounds(BlockSoundGroup.WOOD));

    // interactive plant blocks
    public static final InteractiveAdventurePlantBlock INTERACTIVE_BERRY_BUSH_BLOCK = new InteractiveAdventurePlantBlock(1, null, false, 1/*TODO play test*/, FabricBlockSettings.of(Material.PLANT).ticksRandomly().noCollision().sounds(BlockSoundGroup.SWEET_BERRY_BUSH));
    public static final InteractiveAdventurePlantBlock INTERACTIVE_RED_MUSHROOM_BLOCK = new InteractiveAdventurePlantBlock(0, null, false, 1/*TODO play test*/, FabricBlockSettings.of(Material.PLANT).ticksRandomly().noCollision().sounds(BlockSoundGroup.CROP));
    public static final InteractiveAdventurePlantBlock INTERACTIVE_BROWN_MUSHROOM_BLOCK = new InteractiveAdventurePlantBlock(0, null, false, 1/*TODO play test*/, FabricBlockSettings.of(Material.PLANT).ticksRandomly().noCollision().sounds(BlockSoundGroup.CROP));

    // interactive food blocks
    public static final InteractiveAdventureFoodBlock INTERACTIVE_CHICKEN_MEAL_BLOCK = new InteractiveAdventureFoodBlock(new StatusEffectInstance(StatusEffectsRegistry.CHICKEN_MEAL_FOOD_EFFECT, 600, 0, false, false, true), 1,/*TODO play test*/ FabricBlockSettings.of(Material.CAKE).ticksRandomly().sounds(BlockSoundGroup.WOOL));

    public static void registerBlocks() {
        // interactive barrier blocks
        Registry.register(Registries.BLOCK, new Identifier(RPGMod.MOD_ID, "interactive_stone_block"), INTERACTIVE_STONE_BLOCK);

        // interactive log blocks
        Registry.register(Registries.BLOCK, new Identifier(RPGMod.MOD_ID, "interactive_oak_log"), INTERACTIVE_OAK_LOG);

        // interactive plant blocks
        Registry.register(Registries.BLOCK, new Identifier(RPGMod.MOD_ID, "interactive_berry_bush"), INTERACTIVE_BERRY_BUSH_BLOCK);
        Registry.register(Registries.BLOCK, new Identifier(RPGMod.MOD_ID, "interactive_brown_mushroom"), INTERACTIVE_BROWN_MUSHROOM_BLOCK);
        Registry.register(Registries.BLOCK, new Identifier(RPGMod.MOD_ID, "interactive_red_mushroom"), INTERACTIVE_RED_MUSHROOM_BLOCK);

        // interactive food blocks
        Registry.register(Registries.BLOCK, new Identifier(RPGMod.MOD_ID, "interactive_chicken_meal"), INTERACTIVE_CHICKEN_MEAL_BLOCK);
    }
}
