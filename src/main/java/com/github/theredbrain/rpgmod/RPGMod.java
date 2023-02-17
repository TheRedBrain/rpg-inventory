package com.github.theredbrain.rpgmod;

import com.github.theredbrain.rpgmod.registry.BlockRegistry;
import com.github.theredbrain.rpgmod.registry.ItemRegistry;
import com.github.theredbrain.rpgmod.registry.StatusEffectsRegistry;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RPGMod implements ModInitializer {
	public static final String MOD_ID = "rpgmod";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	// item groups
	public static ItemGroup RPG_BLOCK = FabricItemGroupBuilder.build(
			new Identifier("rpgmod", "rpg_block"),
			() -> new ItemStack(ItemRegistry.ZWEIHANDER));
	public static ItemGroup RPG_ITEM = FabricItemGroupBuilder.build(
			new Identifier("rpgmod", "rpg_item"),
			() -> new ItemStack(ItemRegistry.ZWEIHANDER));
	public static ItemGroup RPG_FOOD = FabricItemGroupBuilder.build(
			new Identifier("rpgmod", "rpg_food"),
			() -> new ItemStack(ItemRegistry.ZWEIHANDER));

	@Override
	public void onInitialize() {
		LOGGER.info("We are going on an adventure!");
		BlockRegistry.registerBlocks();
		ItemRegistry.registerBlockItems();
		ItemRegistry.registerItems();
		StatusEffectsRegistry.registerEffects();
	}
}
