package com.github.theredbrain.rpginventory.registry;

import com.github.theredbrain.rpginventory.RPGInventory;
import com.github.theredbrain.rpginventory.screen.AbstractMannequinScreenHandler;
import com.github.theredbrain.rpginventory.screen.RPGMannequinScreenHandler;
import com.github.theredbrain.rpginventory.screen.VanillaMannequinScreenHandler;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class ScreenHandlerTypesRegistry {
	public static final ExtendedScreenHandlerType<RPGMannequinScreenHandler, AbstractMannequinScreenHandler.MannequinBlockData> RPG_MANNEQUIN_SCREEN_HANDLER = new ExtendedScreenHandlerType<>(RPGMannequinScreenHandler::new, AbstractMannequinScreenHandler.MannequinBlockData.PACKET_CODEC);
	public static final ExtendedScreenHandlerType<VanillaMannequinScreenHandler, AbstractMannequinScreenHandler.MannequinBlockData> VANILLA_MANNEQUIN_SCREEN_HANDLER = new ExtendedScreenHandlerType<>(VanillaMannequinScreenHandler::new, AbstractMannequinScreenHandler.MannequinBlockData.PACKET_CODEC);

	public static void registerAll() {
		Registry.register(Registries.SCREEN_HANDLER, RPGInventory.identifier("rpg_mannequin"), RPG_MANNEQUIN_SCREEN_HANDLER);
		Registry.register(Registries.SCREEN_HANDLER, RPGInventory.identifier("vanilla_mannequin"), VANILLA_MANNEQUIN_SCREEN_HANDLER);
	}
}
