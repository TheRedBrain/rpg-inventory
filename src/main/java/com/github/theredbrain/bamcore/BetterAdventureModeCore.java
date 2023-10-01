package com.github.theredbrain.bamcore;

import com.github.theredbrain.bamcore.api.util.BetterAdventureModeEntityAttributes;
import com.github.theredbrain.bamcore.network.packet.BetterAdventureModCoreServerPacket;
import com.github.theredbrain.bamcore.registry.*;
import com.github.theredbrain.bamcore.world.DimensionsManager;
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
		DimensionsManager.init();
		EntityRegistry.init();
		BetterAdventureModeEntityAttributes.registerAttributes();
		EventsRegistry.initializeEvents();
		ItemRegistry.init();
		BetterAdventureModCoreServerPacket.init();
		ScreenHandlerTypesRegistry.registerAll();
		StatusEffectsRegistry.registerEffects();
		GameRulesRegistry.init();
	}

	public static Identifier identifier(String path) {
		return new Identifier(MOD_ID, path);
	}

}
