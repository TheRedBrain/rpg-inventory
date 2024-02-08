package com.github.theredbrain.betteradventuremode;

import com.github.theredbrain.betteradventuremode.registry.ServerPacketRegistry;
import com.github.theredbrain.betteradventuremode.registry.EntityAttributesRegistry;
import com.github.theredbrain.betteradventuremode.registry.StatusEffectsRegistry;
import com.github.theredbrain.betteradventuremode.config.ServerConfig;
import com.github.theredbrain.betteradventuremode.config.ServerConfigWrapper;
import com.github.theredbrain.betteradventuremode.registry.*;
import com.github.theredbrain.betteradventuremode.world.DimensionsManager;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import me.shedaniel.autoconfig.serializer.PartitioningSerializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.resource.ResourcePackActivationType;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static net.fabricmc.fabric.api.resource.ResourceManagerHelper.registerBuiltinResourcePack;

public class BetterAdventureMode implements ModInitializer {
	public static final String MOD_ID = "betteradventuremode";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	public static ServerConfig serverConfig;

	@Override
	public void onInitialize() {
		LOGGER.info("We are going on an adventure!");

		// Config
		AutoConfig.register(ServerConfigWrapper.class, PartitioningSerializer.wrap(JanksonConfigSerializer::new));
		serverConfig = ((ServerConfigWrapper)AutoConfig.getConfigHolder(ServerConfigWrapper.class).getConfig()).server;

		// Packets
		ServerPacketRegistry.init();

		// Registry
		BlockRegistry.init();
		EntityRegistry.init();
		EntityAttributesRegistry.registerAttributes();
		EntityAttributesRegistry.registerEntityAttributes();
		DataHandlerRegistry.init();
		DimensionsManager.init();
		CraftingRecipeRegistry.init();
		EventsRegistry.initializeEvents();
		DialoguesRegistry.init();
		ShopsRegistry.init();
		LocationsRegistry.init();
		ItemRegistry.init();
		ItemGroupRegistry.init();
		ScreenHandlerTypesRegistry.registerAll();
		StatusEffectsRegistry.registerEffects();
		GameRulesRegistry.init();
		PredicateRegistry.init();
		StructurePlacementTypesRegistry.register();
	}

	static {
		ModContainer modContainer = FabricLoader.getInstance().getModContainer(MOD_ID).get();
		registerBuiltinResourcePack(new Identifier(MOD_ID, "betteradventuremode_testing"), modContainer, Text.translatable("betteradventuremode.builtin_resource_packs.betteradventuremode_testing"), ResourcePackActivationType.DEFAULT_ENABLED);
	}

	public static Identifier identifier(String path) {
		return new Identifier(MOD_ID, path);
	}

	public static void info(String message) {
		LOGGER.info("[" + MOD_ID + "] [info]: " + message);
	}

	public static void warn(String message) {
		LOGGER.warn("[" + MOD_ID + "] [warn]: " + message);
	}

	public static void debug(String message) {
		LOGGER.debug("[" + MOD_ID + "] [debug]: " + message);
	}

	public static void error(String message) {
		LOGGER.error("[" + MOD_ID + "] [error]: " + message);
	}
}
