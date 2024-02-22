package com.github.theredbrain.betteradventuremode.registry;

import com.github.theredbrain.betteradventuremode.BetterAdventureMode;
import com.github.theredbrain.betteradventuremode.block.InteractiveAdventureFullToAirBlock;
import com.github.theredbrain.betteradventuremode.block.InteractiveAdventureLogBlock;
import com.github.theredbrain.betteradventuremode.block.InteractiveAdventurePlantBlock;
import com.github.theredbrain.betteradventuremode.block.*;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.MapColor;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.sound.BlockSoundGroup;
import org.jetbrains.annotations.Nullable;

public class BlockRegistry {

    //region Content Blocks
    // content script blocks
    public static final Block USE_RELAY_OAK_DOOR = registerBlock("use_relay_oak_door", new UseRelayDoorBlock(FabricBlockSettings.create().mapColor(MapColor.OAK_TAN).requiresTool().strength(-1.0f, 3600000.0f).nonOpaque().dropsNothing()), ItemGroupRegistry.SCRIPT_BLOCKS);
    public static final Block USE_RELAY_IRON_DOOR = registerBlock("use_relay_iron_door", new UseRelayDoorBlock(FabricBlockSettings.create().mapColor(MapColor.IRON_GRAY).requiresTool().strength(-1.0f, 3600000.0f).nonOpaque().dropsNothing()), ItemGroupRegistry.SCRIPT_BLOCKS);
    public static final Block USE_RELAY_SPRUCE_DOOR = registerBlock("use_relay_spruce_door", new UseRelayDoorBlock(FabricBlockSettings.create().mapColor(MapColor.SPRUCE_BROWN).requiresTool().strength(-1.0f, 3600000.0f).nonOpaque().dropsNothing()), ItemGroupRegistry.SCRIPT_BLOCKS);
    public static final Block USE_RELAY_BIRCH_DOOR = registerBlock("use_relay_birch_door", new UseRelayDoorBlock(FabricBlockSettings.create().mapColor(MapColor.PALE_YELLOW).requiresTool().strength(-1.0f, 3600000.0f).nonOpaque().dropsNothing()), ItemGroupRegistry.SCRIPT_BLOCKS);
    public static final Block USE_RELAY_JUNGLE_DOOR = registerBlock("use_relay_jungle_door", new UseRelayDoorBlock(FabricBlockSettings.create().mapColor(MapColor.DIRT_BROWN).requiresTool().strength(-1.0f, 3600000.0f).nonOpaque().dropsNothing()), ItemGroupRegistry.SCRIPT_BLOCKS);
    public static final Block USE_RELAY_ACACIA_DOOR = registerBlock("use_relay_acacia_door", new UseRelayDoorBlock(FabricBlockSettings.create().mapColor(MapColor.ORANGE).requiresTool().strength(-1.0f, 3600000.0f).nonOpaque().dropsNothing()), ItemGroupRegistry.SCRIPT_BLOCKS);
    public static final Block USE_RELAY_CHERRY_DOOR = registerBlock("use_relay_cherry_door", new UseRelayDoorBlock(FabricBlockSettings.create().mapColor(MapColor.TERRACOTTA_WHITE).requiresTool().strength(-1.0f, 3600000.0f).nonOpaque().dropsNothing()), ItemGroupRegistry.SCRIPT_BLOCKS);
    public static final Block USE_RELAY_DARK_OAK_DOOR = registerBlock("use_relay_dark_oak_door", new UseRelayDoorBlock(FabricBlockSettings.create().mapColor(MapColor.BROWN).requiresTool().strength(-1.0f, 3600000.0f).nonOpaque().dropsNothing()), ItemGroupRegistry.SCRIPT_BLOCKS);
    public static final Block USE_RELAY_MANGROVE_DOOR = registerBlock("use_relay_mangrove_door", new UseRelayDoorBlock(FabricBlockSettings.create().mapColor(MapColor.RED).requiresTool().strength(-1.0f, 3600000.0f).nonOpaque().dropsNothing()), ItemGroupRegistry.SCRIPT_BLOCKS);
    public static final Block USE_RELAY_BAMBOO_DOOR = registerBlock("use_relay_bamboo_door", new UseRelayDoorBlock(FabricBlockSettings.create().mapColor(MapColor.YELLOW).requiresTool().strength(-1.0f, 3600000.0f).nonOpaque().dropsNothing()), ItemGroupRegistry.SCRIPT_BLOCKS);
    public static final Block USE_RELAY_CRIMSON_DOOR = registerBlock("use_relay_crimson_door", new UseRelayDoorBlock(FabricBlockSettings.create().mapColor(MapColor.DULL_PINK).requiresTool().strength(-1.0f, 3600000.0f).nonOpaque().dropsNothing()), ItemGroupRegistry.SCRIPT_BLOCKS);
    public static final Block USE_RELAY_WARPED_DOOR = registerBlock("use_relay_warped_door", new UseRelayDoorBlock(FabricBlockSettings.create().mapColor(MapColor.DARK_AQUA).requiresTool().strength(-1.0f, 3600000.0f).nonOpaque().dropsNothing()), ItemGroupRegistry.SCRIPT_BLOCKS);
    public static final Block USE_RELAY_OAK_TRAPDOOR = registerBlock("use_relay_oak_trapdoor", new UseRelayTrapdoorBlock(FabricBlockSettings.create().mapColor(MapColor.OAK_TAN).requiresTool().strength(-1.0f, 3600000.0f).nonOpaque().dropsNothing()), ItemGroupRegistry.SCRIPT_BLOCKS);
    public static final Block USE_RELAY_IRON_TRAPDOOR = registerBlock("use_relay_iron_trapdoor", new UseRelayTrapdoorBlock(FabricBlockSettings.create().mapColor(MapColor.IRON_GRAY).requiresTool().strength(-1.0f, 3600000.0f).nonOpaque().dropsNothing()), ItemGroupRegistry.SCRIPT_BLOCKS);
    public static final Block USE_RELAY_SPRUCE_TRAPDOOR = registerBlock("use_relay_spruce_trapdoor", new UseRelayTrapdoorBlock(FabricBlockSettings.create().mapColor(MapColor.SPRUCE_BROWN).requiresTool().strength(-1.0f, 3600000.0f).nonOpaque().dropsNothing()), ItemGroupRegistry.SCRIPT_BLOCKS);
    public static final Block USE_RELAY_BIRCH_TRAPDOOR = registerBlock("use_relay_birch_trapdoor", new UseRelayTrapdoorBlock(FabricBlockSettings.create().mapColor(MapColor.PALE_YELLOW).requiresTool().strength(-1.0f, 3600000.0f).nonOpaque().dropsNothing()), ItemGroupRegistry.SCRIPT_BLOCKS);
    public static final Block USE_RELAY_JUNGLE_TRAPDOOR = registerBlock("use_relay_jungle_trapdoor", new UseRelayTrapdoorBlock(FabricBlockSettings.create().mapColor(MapColor.DIRT_BROWN).requiresTool().strength(-1.0f, 3600000.0f).nonOpaque().dropsNothing()), ItemGroupRegistry.SCRIPT_BLOCKS);
    public static final Block USE_RELAY_ACACIA_TRAPDOOR = registerBlock("use_relay_acacia_trapdoor", new UseRelayTrapdoorBlock(FabricBlockSettings.create().mapColor(MapColor.ORANGE).requiresTool().strength(-1.0f, 3600000.0f).nonOpaque().dropsNothing()), ItemGroupRegistry.SCRIPT_BLOCKS);
    public static final Block USE_RELAY_CHERRY_TRAPDOOR = registerBlock("use_relay_cherry_trapdoor", new UseRelayTrapdoorBlock(FabricBlockSettings.create().mapColor(MapColor.TERRACOTTA_WHITE).requiresTool().strength(-1.0f, 3600000.0f).nonOpaque().dropsNothing()), ItemGroupRegistry.SCRIPT_BLOCKS);
    public static final Block USE_RELAY_DARK_OAK_TRAPDOOR = registerBlock("use_relay_dark_oak_trapdoor", new UseRelayTrapdoorBlock(FabricBlockSettings.create().mapColor(MapColor.BROWN).requiresTool().strength(-1.0f, 3600000.0f).nonOpaque().dropsNothing()), ItemGroupRegistry.SCRIPT_BLOCKS);
    public static final Block USE_RELAY_MANGROVE_TRAPDOOR = registerBlock("use_relay_mangrove_trapdoor", new UseRelayTrapdoorBlock(FabricBlockSettings.create().mapColor(MapColor.RED).requiresTool().strength(-1.0f, 3600000.0f).nonOpaque().dropsNothing()), ItemGroupRegistry.SCRIPT_BLOCKS);
    public static final Block USE_RELAY_BAMBOO_TRAPDOOR = registerBlock("use_relay_bamboo_trapdoor", new UseRelayTrapdoorBlock(FabricBlockSettings.create().mapColor(MapColor.YELLOW).requiresTool().strength(-1.0f, 3600000.0f).nonOpaque().dropsNothing()), ItemGroupRegistry.SCRIPT_BLOCKS);
    public static final Block USE_RELAY_CRIMSON_TRAPDOOR = registerBlock("use_relay_crimson_trapdoor", new UseRelayTrapdoorBlock(FabricBlockSettings.create().mapColor(MapColor.DULL_PINK).requiresTool().strength(-1.0f, 3600000.0f).nonOpaque().dropsNothing()), ItemGroupRegistry.SCRIPT_BLOCKS);
    public static final Block USE_RELAY_WARPED_TRAPDOOR = registerBlock("use_relay_warped_trapdoor", new UseRelayTrapdoorBlock(FabricBlockSettings.create().mapColor(MapColor.DARK_AQUA).requiresTool().strength(-1.0f, 3600000.0f).nonOpaque().dropsNothing()), ItemGroupRegistry.SCRIPT_BLOCKS);

    // crafting blocks
    public static final Block CRAFTING_ROOT_BLOCK = registerBlock("crafting_root_block", new CraftingRootBlock(FabricBlockSettings.create().mapColor(MapColor.OAK_TAN)), ItemGroupRegistry.BAM_BLOCK);
    public static final Block STORAGE_AREA_0_PROVIDER_BLOCK = registerBlock("storage_area_0_provider_block", new CraftingTabProviderBlock(-1, FabricBlockSettings.create().mapColor(MapColor.OAK_TAN)), ItemGroupRegistry.BAM_BLOCK);
    public static final Block STORAGE_AREA_1_PROVIDER_BLOCK = registerBlock("storage_area_1_provider_block", new CraftingTabProviderBlock(-1, FabricBlockSettings.create().mapColor(MapColor.OAK_TAN)), ItemGroupRegistry.BAM_BLOCK);
    public static final Block STORAGE_AREA_2_PROVIDER_BLOCK = registerBlock("storage_area_2_provider_block", new CraftingTabProviderBlock(-1, FabricBlockSettings.create().mapColor(MapColor.OAK_TAN)), ItemGroupRegistry.BAM_BLOCK);
    public static final Block STORAGE_AREA_3_PROVIDER_BLOCK = registerBlock("storage_area_3_provider_block", new CraftingTabProviderBlock(-1, FabricBlockSettings.create().mapColor(MapColor.OAK_TAN)), ItemGroupRegistry.BAM_BLOCK);
    public static final Block STORAGE_AREA_4_PROVIDER_BLOCK = registerBlock("storage_area_4_provider_block", new CraftingTabProviderBlock(-1, FabricBlockSettings.create().mapColor(MapColor.OAK_TAN)), ItemGroupRegistry.BAM_BLOCK);
    public static final Block CRAFTING_TAB_1_PROVIDER_BLOCK = registerBlock("crafting_tab_1_provider_block", new CraftingTabProviderBlock(1, FabricBlockSettings.create().mapColor(MapColor.OAK_TAN)), ItemGroupRegistry.BAM_BLOCK);
    public static final Block CRAFTING_TAB_2_PROVIDER_BLOCK = registerBlock("crafting_tab_2_provider_block", new CraftingTabProviderBlock(2, FabricBlockSettings.create().mapColor(MapColor.OAK_TAN)), ItemGroupRegistry.BAM_BLOCK);
    public static final Block CRAFTING_TAB_3_PROVIDER_BLOCK = registerBlock("crafting_tab_3_provider_block", new CraftingTabProviderBlock(3, FabricBlockSettings.create().mapColor(MapColor.OAK_TAN)), ItemGroupRegistry.BAM_BLOCK);


    public static final Block BONFIRE_BLOCK = registerBlock("bonfire", new BonfireBlock(FabricBlockSettings.create().mapColor(MapColor.SPRUCE_BROWN).ticksRandomly().sounds(BlockSoundGroup.STONE).luminance((state) -> state.get(BonfireBlock.ACTIVE) ? 15 : 0).nonOpaque()), ItemGroupRegistry.BAM_BLOCK);

    // interactive barrier blocks
    public static final Block INTERACTIVE_STONE_BLOCK = registerBlock("interactive_stone_block", new InteractiveAdventureFullToAirBlock(null, null, Tags.INTERACTIVE_STONE_BLOCK_TOOLS, true, 1/*TODO play test*/, FabricBlockSettings.create().mapColor(MapColor.STONE_GRAY).ticksRandomly().sounds(BlockSoundGroup.STONE)), ItemGroupRegistry.BAM_BLOCK);

    // interactive log blocks
    public static final Block INTERACTIVE_OAK_LOG = registerBlock("interactive_oak_log", new InteractiveAdventureLogBlock(null, null, Tags.INTERACTIVE_OAK_LOG_TOOLS, true, 1/*TODO play test*/, FabricBlockSettings.create().mapColor(MapColor.OAK_TAN).burnable().ticksRandomly().sounds(BlockSoundGroup.WOOD)), ItemGroupRegistry.BAM_BLOCK);

    // interactive plant blocks
    public static final Block INTERACTIVE_BERRY_BUSH_BLOCK = registerBlock("interactive_berry_bush", new InteractiveAdventurePlantBlock(1, null, null, null, false, 1/*TODO play test*/, FabricBlockSettings.create().mapColor(MapColor.DARK_GREEN).notSolid().pistonBehavior(PistonBehavior.DESTROY).ticksRandomly().noCollision().sounds(BlockSoundGroup.SWEET_BERRY_BUSH)), ItemGroupRegistry.BAM_BLOCK);
    public static final Block INTERACTIVE_RED_MUSHROOM_BLOCK = registerBlock("interactive_red_mushroom", new InteractiveAdventurePlantBlock(0, null, null, null, false, 1/*TODO play test*/, FabricBlockSettings.create().mapColor(MapColor.DARK_GREEN).notSolid().pistonBehavior(PistonBehavior.DESTROY).ticksRandomly().noCollision().sounds(BlockSoundGroup.CROP)), ItemGroupRegistry.BAM_BLOCK);
    public static final Block INTERACTIVE_BROWN_MUSHROOM_BLOCK = registerBlock("interactive_brown_mushroom", new InteractiveAdventurePlantBlock(0, null, null, null, false, 1/*TODO play test*/, FabricBlockSettings.create().mapColor(MapColor.DARK_GREEN).notSolid().pistonBehavior(PistonBehavior.DESTROY).ticksRandomly().noCollision().sounds(BlockSoundGroup.CROP)), ItemGroupRegistry.BAM_BLOCK);

    // interactive food blocks
//    public static final Block INTERACTIVE_CHICKEN_MEAL_BLOCK = registerBlock("interactive_chicken_meal", new InteractiveAdventureFoodBlock(new StatusEffectInstance(StatusEffectsRegistry.CHICKEN_MEAL_FOOD_EFFECT, 600, 0, false, false, true), 1,/*TODO play test*/ FabricBlockSettings.create().mapColor(MapColor.CLEAR).pistonBehavior(PistonBehavior.DESTROY).ticksRandomly().sounds(BlockSoundGroup.WOOL)), ItemGroupRegistry.BAM_BLOCK);
    public static final Block LAVA_BLOCK = registerBlock("lava_block", new LavaBlock(FabricBlockSettings.create().mapColor(MapColor.DARK_RED).jumpVelocityMultiplier(0.0f).luminance(state -> 15).emissiveLighting(Blocks::always)), ItemGroupRegistry.SCRIPT_BLOCKS);
    //endregion Content Blocks

    //region Script Blocks
    public static final Block DIALOGUE_BLOCK = registerBlock("dialogue_block", new DialogueBlock(FabricBlockSettings.create().mapColor(MapColor.LIGHT_GRAY).requiresTool().strength(-1.0f, 3600000.0f).dropsNothing()), ItemGroupRegistry.SCRIPT_BLOCKS);
    public static final Block SHOP_BLOCK = registerBlock("shop_block", new ShopBlock(FabricBlockSettings.create().mapColor(MapColor.LIGHT_GRAY).requiresTool().strength(-1.0f, 3600000.0f).dropsNothing()), ItemGroupRegistry.SCRIPT_BLOCKS);
    public static final Block MIMIC_BLOCK = registerBlock("mimic_block", new MimicBlock(FabricBlockSettings.create().mapColor(MapColor.LIGHT_GRAY).requiresTool().strength(-1.0f, 3600000.0f).dropsNothing()), ItemGroupRegistry.SCRIPT_BLOCKS);
    public static final Block MIMIC_FALLBACK_BLOCK = Registry.register(Registries.BLOCK, BetterAdventureMode.identifier("mimic_fallback_block"), new Block(FabricBlockSettings.create().mapColor(MapColor.LIGHT_GRAY).requiresTool().strength(-1.0f, 3600000.0f).dropsNothing()));
    public static final Block TRIGGERED_MESSAGE_BLOCK = registerBlock("triggered_message_block", new TriggeredMessageBlock(FabricBlockSettings.create().mapColor(MapColor.LIGHT_GRAY).requiresTool().strength(-1.0f, 3600000.0f).dropsNothing()), ItemGroupRegistry.SCRIPT_BLOCKS);
    public static final Block TRIGGERED_SPAWNER_BLOCK = registerBlock("triggered_spawner_block", new TriggeredSpawnerBlock(FabricBlockSettings.create().mapColor(MapColor.LIGHT_GRAY).requiresTool().strength(-1.0f, 3600000.0f).dropsNothing()), ItemGroupRegistry.SCRIPT_BLOCKS);
    public static final Block LOCATION_CONTROL_BLOCK = registerBlock("location_control_block", new LocationControlBlock(FabricBlockSettings.create().mapColor(MapColor.LIGHT_GRAY).requiresTool().strength(-1.0f, 3600000.0f).dropsNothing()), ItemGroupRegistry.SCRIPT_BLOCKS);
    public static final Block HOUSING_BLOCK = registerBlock("housing_block", new HousingBlock(FabricBlockSettings.create().mapColor(MapColor.LIGHT_GRAY).requiresTool().strength(-1.0f, 3600000.0f).dropsNothing()), ItemGroupRegistry.SCRIPT_BLOCKS);
    public static final Block TELEPORTER_BLOCK = registerBlock("teleporter_block", new TeleporterBlock(FabricBlockSettings.create().mapColor(MapColor.LIGHT_GRAY).requiresTool().strength(-1.0f, 3600000.0f).dropsNothing()), ItemGroupRegistry.SCRIPT_BLOCKS);
    public static final Block JIGSAW_PLACER_BLOCK = registerBlock("jigsaw_placer_block", new JigsawPlacerBlock(FabricBlockSettings.create().mapColor(MapColor.LIGHT_GRAY).requiresTool().strength(-1.0f, 3600000.0f).dropsNothing()), ItemGroupRegistry.SCRIPT_BLOCKS);
    public static final Block REDSTONE_TRIGGER_BLOCK = registerBlock("redstone_trigger_block", new RedstoneTriggerBlock(FabricBlockSettings.create().mapColor(MapColor.LIGHT_GRAY).requiresTool().strength(-1.0f, 3600000.0f).dropsNothing()), ItemGroupRegistry.SCRIPT_BLOCKS);
    public static final Block RELAY_TRIGGER_BLOCK = registerBlock("relay_trigger_block", new RelayTriggerBlock(FabricBlockSettings.create().mapColor(MapColor.LIGHT_GRAY).requiresTool().strength(-1.0f, 3600000.0f).dropsNothing()), ItemGroupRegistry.SCRIPT_BLOCKS);
    public static final Block RESET_TRIGGER_BLOCK = registerBlock("reset_trigger_block", new ResetTriggerBlock(FabricBlockSettings.create().mapColor(MapColor.LIGHT_GRAY).requiresTool().strength(-1.0f, 3600000.0f).dropsNothing()), ItemGroupRegistry.SCRIPT_BLOCKS);
    public static final Block DELAY_TRIGGER_BLOCK = registerBlock("delay_trigger_block", new DelayTriggerBlock(FabricBlockSettings.create().mapColor(MapColor.LIGHT_GRAY).requiresTool().strength(-1.0f, 3600000.0f).dropsNothing()), ItemGroupRegistry.SCRIPT_BLOCKS);
    public static final Block USE_RELAY_BLOCK = registerBlock("use_relay_block", new UseRelayBlock(FabricBlockSettings.create().mapColor(MapColor.LIGHT_GRAY).requiresTool().strength(-1.0f, 3600000.0f).dropsNothing()), ItemGroupRegistry.SCRIPT_BLOCKS);
    public static final Block TRIGGERED_COUNTER_BLOCK = registerBlock("triggered_counter_block", new TriggeredCounterBlock(FabricBlockSettings.create().mapColor(MapColor.LIGHT_GRAY).requiresTool().strength(-1.0f, 3600000.0f).dropsNothing()), ItemGroupRegistry.SCRIPT_BLOCKS);
    public static final Block ENTRANCE_DELEGATION_BLOCK = registerBlock("entrance_delegation_block", new EntranceDelegationBlock(FabricBlockSettings.create().mapColor(MapColor.LIGHT_GRAY).requiresTool().strength(-1.0f, 3600000.0f).dropsNothing()), ItemGroupRegistry.SCRIPT_BLOCKS);
    public static final Block AREA_BLOCK = registerBlock("area_block", new AreaBlock(FabricBlockSettings.create().mapColor(MapColor.LIGHT_GRAY).requiresTool().strength(-1.0f, 3600000.0f).dropsNothing()), ItemGroupRegistry.SCRIPT_BLOCKS);
    public static final Block TRIGGERED_ADVANCEMENT_CHECKER_BLOCK = registerBlock("triggered_advancement_checker_block", new TriggeredAdvancementCheckerBlock(FabricBlockSettings.create().mapColor(MapColor.LIGHT_GRAY).requiresTool().strength(-1.0f, 3600000.0f).dropsNothing()), ItemGroupRegistry.SCRIPT_BLOCKS);
    //endregion Script Blocks

    private static Block registerBlock(String name, Block block, @Nullable RegistryKey<ItemGroup> itemGroup) {

        Registry.register(Registries.ITEM, BetterAdventureMode.identifier(name), new BlockItem(block, new FabricItemSettings()));
        ItemGroupEvents.modifyEntriesEvent(itemGroup).register(content -> content.add(block));
        return Registry.register(Registries.BLOCK, BetterAdventureMode.identifier(name), block);
    }

    public static void init() {}
}
