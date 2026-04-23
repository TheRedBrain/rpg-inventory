package com.github.theredbrain.rpginventory.registry;

import com.github.theredbrain.rpginventory.RPGInventory;
import com.github.theredbrain.rpginventory.block.MannequinBlock;
import net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;

import java.util.List;

public class BlockRegistry {

	public static final Block MANNEQUIN = registerBlock("mannequin", new MannequinBlock(BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).strength(10.0F, 3600000.0f).noOcclusion()), List.of(CreativeModeTabs.FUNCTIONAL_BLOCKS));

	private static Block registerBlock(String name, Block block, List<ResourceKey<CreativeModeTab>> creativeModeTabList) {
		Registry.register(BuiltInRegistries.ITEM, RPGInventory.identifier(name), new BlockItem(block, new Item.Properties()));
		for (ResourceKey<CreativeModeTab> creativeModeTabResourceKey : creativeModeTabList) {
			CreativeModeTabEvents.modifyOutputEvent(creativeModeTabResourceKey).register(content -> content.accept(block));
		}
		return Registry.register(BuiltInRegistries.BLOCK, RPGInventory.identifier(name), block);
	}

	public static void init() {
	}
}
