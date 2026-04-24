package com.github.theredbrain.rpginventory.registry;

import com.github.theredbrain.rpginventory.RPGInventory;
import com.github.theredbrain.rpginventory.block.MannequinBlock;
import net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
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
	public static ResourceKey<Block> MANNEQUIN_BLOCK_KEY = ResourceKey.create(Registries.BLOCK, RPGInventory.identifier("mannequin"));
	public static ResourceKey<Item> MANNEQUIN_ITEM_KEY = ResourceKey.create(Registries.ITEM, RPGInventory.identifier("mannequin"));

	public static final Block MANNEQUIN = registerBlock(MANNEQUIN_BLOCK_KEY, MANNEQUIN_ITEM_KEY, new MannequinBlock(BlockBehaviour.Properties.of().setId(MANNEQUIN_BLOCK_KEY).mapColor(MapColor.WOOD).strength(10.0F, 3600000.0f).noOcclusion()), List.of(CreativeModeTabs.FUNCTIONAL_BLOCKS));

	private static Block registerBlock(ResourceKey<Block> block_key, ResourceKey<Item> item_key, Block block, List<ResourceKey<CreativeModeTab>> creativeModeTabList, Block... alternatives) {
		Item blockItem = Registry.register(BuiltInRegistries.ITEM, item_key, new BlockItem(block, new Item.Properties().setId(item_key).fireResistant()));
		for (ResourceKey<CreativeModeTab> creativeModeTab : creativeModeTabList) {
			CreativeModeTabEvents.modifyOutputEvent(creativeModeTab).register(content -> content.accept(block));
		}
		for (Block alternative : alternatives) {
			Item.BY_BLOCK.put(alternative, blockItem);
		}
		return Registry.register(BuiltInRegistries.BLOCK, block_key, block);
	}

	public static void init() {
	}
}
