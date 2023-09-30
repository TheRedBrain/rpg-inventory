package com.github.theredbrain.bamcore.registry;

import com.github.theredbrain.bamcore.BetterAdventureModeCore;
import com.github.theredbrain.bamcore.block.*;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.MapColor;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import org.jetbrains.annotations.Nullable;

public class BlockRegistry {

    public static final Block TELEPORTER_BLOCK = registerBlock("teleporter_block", new TeleporterBlock(FabricBlockSettings.create().mapColor(MapColor.LIGHT_GRAY).requiresTool().strength(-1.0f, 3600000.0f).dropsNothing()), ItemGroups.OPERATOR);
    public static final Block STRUCTURE_PLACER_BLOCK = registerBlock("structure_placer_block", new StructurePlacerBlock(FabricBlockSettings.create().mapColor(MapColor.LIGHT_GRAY).requiresTool().strength(-1.0f, 3600000.0f).dropsNothing()), ItemGroups.OPERATOR);
    public static final Block AREA_FILLER_BLOCK = registerBlock("area_filler_block", new AreaFillerBlock(FabricBlockSettings.create().mapColor(MapColor.LIGHT_GRAY).requiresTool().strength(-1.0f, 3600000.0f).dropsNothing()), ItemGroups.OPERATOR);
    public static final Block REDSTONE_TRIGGER_BLOCK = registerBlock("redstone_trigger_block", new RedstoneTriggerBlock(FabricBlockSettings.create().mapColor(MapColor.LIGHT_GRAY).requiresTool().strength(-1.0f, 3600000.0f).dropsNothing()), ItemGroups.OPERATOR);
    public static final Block RELAY_TRIGGER_BLOCK = registerBlock("relay_trigger_block", new RelayTriggerBlock(FabricBlockSettings.create().mapColor(MapColor.LIGHT_GRAY).requiresTool().strength(-1.0f, 3600000.0f).dropsNothing()), ItemGroups.OPERATOR);
    public static final Block DELAY_TRIGGER_BLOCK = registerBlock("delay_trigger_block", new DelayTriggerBlock(FabricBlockSettings.create().mapColor(MapColor.LIGHT_GRAY).requiresTool().strength(-1.0f, 3600000.0f).dropsNothing()), ItemGroups.OPERATOR);
    public static final Block CHUNK_LOADER_BLOCK = registerBlock("chunk_loader_block", new ChunkLoaderBlock(FabricBlockSettings.create().mapColor(MapColor.LIGHT_GRAY).requiresTool().strength(-1.0f, 3600000.0f).dropsNothing()), ItemGroups.OPERATOR);

    private static Block registerBlock(String name, Block block, @Nullable RegistryKey<ItemGroup> itemGroup) {

        Registry.register(Registries.ITEM, BetterAdventureModeCore.identifier(name), new BlockItem(block, new FabricItemSettings()));
        ItemGroupEvents.modifyEntriesEvent(itemGroup).register(content -> content.add(block));
        return Registry.register(Registries.BLOCK, BetterAdventureModeCore.identifier(name), block);
    }

    public static void init() {}
}
