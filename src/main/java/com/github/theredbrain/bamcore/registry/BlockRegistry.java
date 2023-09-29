package com.github.theredbrain.bamcore.registry;

import com.github.theredbrain.bamcore.BetterAdventureModCore;
import com.github.theredbrain.bamcore.block.*;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.MapColor;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.sound.BlockSoundGroup;
import org.jetbrains.annotations.Nullable;

public class BlockRegistry {

    // interactive barrier blocks
    public static final Block INTERACTIVE_STONE_BLOCK = registerBlock("interactive_stone_block", new InteractiveAdventureFullToAirBlock(Tags.INTERACTIVE_STONE_BLOCK_TOOLS, true, 1/*TODO play test*/, FabricBlockSettings.create().mapColor(MapColor.STONE_GRAY).ticksRandomly().sounds(BlockSoundGroup.STONE)), ItemGroupRegistry.BAM_BLOCK);
    public static final Block TELEPORTER_BLOCK = registerBlock("teleporter_block", new TeleporterBlock(FabricBlockSettings.create().mapColor(MapColor.LIGHT_GRAY).requiresTool().strength(-1.0f, 3600000.0f).dropsNothing()), ItemGroups.OPERATOR);
    public static final Block STRUCTURE_PLACER_BLOCK = registerBlock("structure_placer_block", new StructurePlacerBlock(FabricBlockSettings.create().mapColor(MapColor.LIGHT_GRAY).requiresTool().strength(-1.0f, 3600000.0f).dropsNothing()), ItemGroups.OPERATOR);
    public static final Block AREA_FILLER_BLOCK = registerBlock("area_filler_block", new AreaFillerBlock(FabricBlockSettings.create().mapColor(MapColor.LIGHT_GRAY).requiresTool().strength(-1.0f, 3600000.0f).dropsNothing()), ItemGroups.OPERATOR);
    public static final Block REDSTONE_TRIGGER_BLOCK = registerBlock("redstone_trigger_block", new RedstoneTriggerBlock(FabricBlockSettings.create().mapColor(MapColor.LIGHT_GRAY).requiresTool().strength(-1.0f, 3600000.0f).dropsNothing()), ItemGroups.OPERATOR);
    public static final Block RELAY_TRIGGER_BLOCK = registerBlock("relay_trigger_block", new RelayTriggerBlock(FabricBlockSettings.create().mapColor(MapColor.LIGHT_GRAY).requiresTool().strength(-1.0f, 3600000.0f).dropsNothing()), ItemGroups.OPERATOR);
    public static final Block DELAY_TRIGGER_BLOCK = registerBlock("delay_trigger_block", new DelayTriggerBlock(FabricBlockSettings.create().mapColor(MapColor.LIGHT_GRAY).requiresTool().strength(-1.0f, 3600000.0f).dropsNothing()), ItemGroups.OPERATOR);
    public static final Block CHUNK_LOADER_BLOCK = registerBlock("chunk_loader_block", new ChunkLoaderBlock(FabricBlockSettings.create().mapColor(MapColor.LIGHT_GRAY).requiresTool().strength(-1.0f, 3600000.0f).dropsNothing()), ItemGroups.OPERATOR);

    // interactive log blocks
    public static final Block INTERACTIVE_OAK_LOG = registerBlock("interactive_oak_log", new InteractiveAdventureLogBlock(Tags.INTERACTIVE_OAK_LOG_TOOLS, true, 1/*TODO play test*/, FabricBlockSettings.create().mapColor(MapColor.OAK_TAN).burnable().ticksRandomly().sounds(BlockSoundGroup.WOOD)), ItemGroupRegistry.BAM_BLOCK);

    // interactive plant blocks
    public static final Block INTERACTIVE_BERRY_BUSH_BLOCK = registerBlock("interactive_berry_bush", new InteractiveAdventurePlantBlock(1, null, false, 1/*TODO play test*/, FabricBlockSettings.create().mapColor(MapColor.DARK_GREEN).notSolid().pistonBehavior(PistonBehavior.DESTROY).ticksRandomly().noCollision().sounds(BlockSoundGroup.SWEET_BERRY_BUSH)), ItemGroupRegistry.BAM_BLOCK);
    public static final Block INTERACTIVE_RED_MUSHROOM_BLOCK = registerBlock("interactive_red_mushroom", new InteractiveAdventurePlantBlock(0, null, false, 1/*TODO play test*/, FabricBlockSettings.create().mapColor(MapColor.DARK_GREEN).notSolid().pistonBehavior(PistonBehavior.DESTROY).ticksRandomly().noCollision().sounds(BlockSoundGroup.CROP)), ItemGroupRegistry.BAM_BLOCK);
    public static final Block INTERACTIVE_BROWN_MUSHROOM_BLOCK = registerBlock("interactive_brown_mushroom", new InteractiveAdventurePlantBlock(0, null, false, 1/*TODO play test*/, FabricBlockSettings.create().mapColor(MapColor.DARK_GREEN).notSolid().pistonBehavior(PistonBehavior.DESTROY).ticksRandomly().noCollision().sounds(BlockSoundGroup.CROP)), ItemGroupRegistry.BAM_BLOCK);

    // interactive food blocks
    public static final Block INTERACTIVE_CHICKEN_MEAL_BLOCK = registerBlock("interactive_chicken_meal", new InteractiveAdventureFoodBlock(new StatusEffectInstance(StatusEffectsRegistry.CHICKEN_MEAL_FOOD_EFFECT, 600, 0, false, false, true), 1,/*TODO play test*/ FabricBlockSettings.create().mapColor(MapColor.CLEAR).pistonBehavior(PistonBehavior.DESTROY).ticksRandomly().sounds(BlockSoundGroup.WOOL)), ItemGroupRegistry.BAM_BLOCK);

//    public static void registerBlocks() {
//        // interactive barrier blocks
//        Registry.register(Registries.BLOCK, RPGMod.identifier("interactive_stone_block"), INTERACTIVE_STONE_BLOCK);
//
//        Registry.register(Registries.BLOCK, RPGMod.identifier("teleporter_block"), TELEPORTER_BLOCK);
//        Registry.register(Registries.BLOCK, RPGMod.identifier("area_filler_block"), AREA_FILLER_BLOCK);
//        Registry.register(Registries.BLOCK, RPGMod.identifier("structure_placer_block"), STRUCTURE_PLACER_BLOCK);
//        Registry.register(Registries.BLOCK, RPGMod.identifier("redstone_trigger_block"), REDSTONE_TRIGGER_BLOCK);
//        Registry.register(Registries.BLOCK, RPGMod.identifier("relay_trigger_block"), RELAY_TRIGGER_BLOCK);
//        Registry.register(Registries.BLOCK, RPGMod.identifier("delay_trigger_block"), DELAY_TRIGGER_BLOCK);
//        Registry.register(Registries.BLOCK, RPGMod.identifier("chunk_loader_block"), CHUNK_LOADER_BLOCK);
//
//        // interactive log blocks
//        Registry.register(Registries.BLOCK, RPGMod.identifier("interactive_oak_log"), INTERACTIVE_OAK_LOG);
//
//        // interactive plant blocks
//        Registry.register(Registries.BLOCK, RPGMod.identifier("interactive_berry_bush"), INTERACTIVE_BERRY_BUSH_BLOCK);
//        Registry.register(Registries.BLOCK, RPGMod.identifier("interactive_brown_mushroom"), INTERACTIVE_BROWN_MUSHROOM_BLOCK);
//        Registry.register(Registries.BLOCK, RPGMod.identifier("interactive_red_mushroom"), INTERACTIVE_RED_MUSHROOM_BLOCK);
//
//        // interactive food blocks
////        Registry.register(Registries.BLOCK, RPGMod.identifier("interactive_chicken_meal"), INTERACTIVE_CHICKEN_MEAL_BLOCK);
//    }

    private static Block registerBlock(String name, Block block, @Nullable RegistryKey<ItemGroup> itemGroup) {

        Registry.register(Registries.ITEM, BetterAdventureModCore.identifier(name), new BlockItem(block, new FabricItemSettings()));
        ItemGroupEvents.modifyEntriesEvent(itemGroup).register(content -> content.add(block));
        return Registry.register(Registries.BLOCK, BetterAdventureModCore.identifier(name), block);
    }

    public static void init() {}
}
