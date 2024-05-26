package com.github.theredbrain.betteradventuremode;

import com.github.theredbrain.betteradventuremode.registry.ServerPacketRegistry;
import com.github.theredbrain.betteradventuremode.registry.StatusEffectsRegistry;
import com.github.theredbrain.betteradventuremode.config.ServerConfig;
import com.github.theredbrain.betteradventuremode.config.ServerConfigWrapper;
import com.github.theredbrain.betteradventuremode.registry.*;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import me.shedaniel.autoconfig.serializer.PartitioningSerializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.entity.attribute.ClampedEntityAttribute;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static net.fabricmc.fabric.api.resource.ResourceManagerHelper.registerBuiltinResourcePack;

public class BetterAdventureMode implements ModInitializer {
	public static final String MOD_ID = "betteradventuremode";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	public static ServerConfig serverConfig;

	public static EntityAttribute ACTIVE_SPELL_SLOT_AMOUNT;
	public static EntityAttribute EQUIPMENT_WEIGHT;
	public static EntityAttribute MAX_EQUIPMENT_WEIGHT;

	@Override
	public void onInitialize() {
		LOGGER.info("We are going on an adventure!");

		// Config
		AutoConfig.register(ServerConfigWrapper.class, PartitioningSerializer.wrap(JanksonConfigSerializer::new));
		serverConfig = ((ServerConfigWrapper)AutoConfig.getConfigHolder(ServerConfigWrapper.class).getConfig()).server;

		// Packets
		ServerPacketRegistry.init();

		// Registry
		EventsRegistry.initializeEvents();
		ItemRegistry.init();
		StatusEffectsRegistry.registerEffects();
		GameRulesRegistry.init();
		PredicateRegistry.init();
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
