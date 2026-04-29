package com.github.theredbrain.rpginventory.registry;

import com.github.theredbrain.rpginventory.RPGInventory;
import net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.Unit;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;

import java.util.List;

public class ItemRegistry {

	public static ResourceKey<Item> DEFAULT_EMPTY_HAND_WEAPON_KEY = ResourceKey.create(Registries.ITEM, RPGInventory.identifier("default_empty_hand_weapon"));
	public static final Item DEFAULT_EMPTY_HAND_WEAPON = registerItem(DEFAULT_EMPTY_HAND_WEAPON_KEY, new Item(new Item.Properties().setId(DEFAULT_EMPTY_HAND_WEAPON_KEY).component(DataComponents.UNBREAKABLE, Unit.INSTANCE).stacksTo(1)), List.of());

	private static Item registerItem(ResourceKey<Item> key, Item item, List<ResourceKey<CreativeModeTab>> creativeModeTabList) {

		for (ResourceKey<CreativeModeTab> creativeModeTab : creativeModeTabList) {
			CreativeModeTabEvents.modifyOutputEvent(creativeModeTab).register(content -> {
				content.accept(item);
			});
		}

		return Registry.register(BuiltInRegistries.ITEM, key, item);
	}

	public static void init() {
	}
}
