package com.github.theredbrain.rpginventory.registry;

import com.github.theredbrain.rpginventory.RPGInventory;
import com.github.theredbrain.rpginventory.screen.MannequinScreenHandler;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.resource.featuretoggle.FeatureFlags;
import net.minecraft.resource.featuretoggle.FeatureSet;
import net.minecraft.screen.ScreenHandlerType;

public class ScreenHandlerTypesRegistry {
	public static final ScreenHandlerType<MannequinScreenHandler> MANNEQUIN_SCREEN_HANDLER = new ScreenHandlerType<>(MannequinScreenHandler::new, FeatureSet.of(FeatureFlags.VANILLA));

	public static void registerAll() {
		Registry.register(Registries.SCREEN_HANDLER, RPGInventory.identifier("mannequin"), MANNEQUIN_SCREEN_HANDLER);
	}
}
