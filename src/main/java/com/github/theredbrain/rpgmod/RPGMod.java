package com.github.theredbrain.rpgmod;

import com.github.theredbrain.rpgmod.registry.*;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RPGMod implements ModInitializer {
	public static final String MOD_ID = "rpgmod";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	// items groups
	public static ItemGroup RPG_BLOCK =FabricItemGroup.builder(
			new Identifier("rpgmod", "rpg_block"))
			.icon(() -> new ItemStack(ItemRegistry.ZWEIHANDER))
			.build();
	public static ItemGroup RPG_ITEM =FabricItemGroup.builder(
			new Identifier("rpgmod", "rpg_item"))
			.icon(() -> new ItemStack(ItemRegistry.ZWEIHANDER))
			.build();
	public static ItemGroup RPG_FOOD =FabricItemGroup.builder(
			new Identifier("rpgmod", "rpg_food"))
			.icon(() -> new ItemStack(ItemRegistry.ZWEIHANDER))
			.build();

	@Override
	public void onInitialize() {
		LOGGER.info("We are going on an adventure!");
		BlockRegistry.registerBlocks();
		new EntityRegistry();
		EntityAttributesRegistry.registerAttributes();
		ItemRegistry.registerItems();
		ItemRegistry.registerBlockItems();
		ItemRegistry.registerItemsToItemGroups();
		StatusEffectsRegistry.registerEffects();
	}
}
