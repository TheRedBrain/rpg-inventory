package com.github.theredbrain.rpginventory.registry;

import com.github.theredbrain.rpginventory.RPGInventory;
import com.github.theredbrain.rpginventory.block.MannequinBlock;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.block.Block;
import net.minecraft.block.MapColor;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;

public class BlockRegistry {

	public static final Block MANNEQUIN = registerBlock("mannequin", new MannequinBlock(Block.Settings.create().mapColor(MapColor.OAK_TAN).strength(10.0F, 3600000.0f).nonOpaque()), ItemGroups.FUNCTIONAL);

	private static Block registerBlock(String name, Block block, RegistryKey<ItemGroup> itemGroup) {
		Registry.register(Registries.ITEM, RPGInventory.identifier(name), new BlockItem(block, new Item.Settings()));
		ItemGroupEvents.modifyEntriesEvent(itemGroup).register(content -> content.add(block));
		return Registry.register(Registries.BLOCK, RPGInventory.identifier(name), block);
	}

	public static void init() {
	}
}
