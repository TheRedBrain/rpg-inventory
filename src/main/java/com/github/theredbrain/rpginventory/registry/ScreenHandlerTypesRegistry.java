package com.github.theredbrain.rpginventory.registry;

import com.github.theredbrain.rpginventory.RPGInventory;
import com.github.theredbrain.rpginventory.screen.MannequinScreenHandler;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class ScreenHandlerTypesRegistry {
	public static final ExtendedScreenHandlerType<MannequinScreenHandler, MannequinScreenHandler.MannequinBlockData> MANNEQUIN_SCREEN_HANDLER = new ExtendedScreenHandlerType<>(MannequinScreenHandler::new, MannequinScreenHandler.MannequinBlockData.PACKET_CODEC);

	public static void registerAll() {
		Registry.register(Registries.SCREEN_HANDLER, RPGInventory.identifier("mannequin"), MANNEQUIN_SCREEN_HANDLER);
	}
}
