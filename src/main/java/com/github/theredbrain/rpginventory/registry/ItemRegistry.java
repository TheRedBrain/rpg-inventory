package com.github.theredbrain.rpginventory.registry;

import com.github.theredbrain.rpginventory.RPGInventory;
import com.github.theredbrain.rpginventory.item.EmptyHandWeapon;
import net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;

import java.util.List;

public class ItemRegistry {

	public static final Item DEFAULT_EMPTY_HAND_WEAPON = registerItem("default_empty_hand_weapon", new EmptyHandWeapon(new Item.Properties().stacksTo(1)), List.of());

	private static Item registerItem(String name, Item item, List<ResourceKey<CreativeModeTab>> creativeModeTabList) {

		for (ResourceKey<CreativeModeTab> creativeModeTabResourceKey : creativeModeTabList) {
			CreativeModeTabEvents.modifyOutputEvent(creativeModeTabResourceKey).register(content -> content.accept(item));
		}
		return Registry.register(BuiltInRegistries.ITEM, ResourceKey.create(BuiltInRegistries.ITEM.key(), RPGInventory.identifier(name)), item);
	}

	public static void init() {
	}
}
