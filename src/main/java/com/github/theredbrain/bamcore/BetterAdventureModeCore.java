package com.github.theredbrain.bamcore;

import com.github.theredbrain.bamcore.api.util.BetterAdventureModeCoreEntityAttributes;
import com.github.theredbrain.bamcore.api.util.BetterAdventureModeCoreStatusEffects;
import com.github.theredbrain.bamcore.network.packet.BetterAdventureModCoreServerPacket;
import com.github.theredbrain.bamcore.registry.*;
import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BetterAdventureModeCore implements ModInitializer {
	public static final String MOD_ID = "bamcore";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		LOGGER.info("We are going on an adventure!");
		BlockRegistry.init();
		// TODO move to bamdimensions
//		DimensionsManager.init();
		EntityRegistry.init();
		BetterAdventureModeCoreEntityAttributes.registerAttributes();
		EventsRegistry.initializeEvents();
		ItemRegistry.init();
		BetterAdventureModCoreServerPacket.init();
		ScreenHandlerTypesRegistry.registerAll();
		BetterAdventureModeCoreStatusEffects.registerEffects();
		GameRulesRegistry.init();
		PredicateRegistry.init();
	}

	public static Identifier identifier(String path) {
		return new Identifier(MOD_ID, path);
	}

}
