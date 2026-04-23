package com.github.theredbrain.rpginventory.registry;

import com.github.theredbrain.rpginventory.RPGInventory;
import com.github.theredbrain.rpginventory.screen.AbstractMannequinScreenHandler;
import com.github.theredbrain.rpginventory.screen.RPGMannequinScreenHandler;
import com.github.theredbrain.rpginventory.screen.VanillaMannequinScreenHandler;
import net.fabricmc.fabric.api.menu.v1.ExtendedMenuType;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;

public class ScreenHandlerTypesRegistry {
	public static final ExtendedMenuType<RPGMannequinScreenHandler, AbstractMannequinScreenHandler.MannequinBlockData> RPG_MANNEQUIN_SCREEN_HANDLER = new ExtendedMenuType<>(RPGMannequinScreenHandler::new, AbstractMannequinScreenHandler.MannequinBlockData.PACKET_CODEC);
	public static final ExtendedMenuType<VanillaMannequinScreenHandler, AbstractMannequinScreenHandler.MannequinBlockData> VANILLA_MANNEQUIN_SCREEN_HANDLER = new ExtendedMenuType<>(VanillaMannequinScreenHandler::new, AbstractMannequinScreenHandler.MannequinBlockData.PACKET_CODEC);

	public static void registerAll() {
		Registry.register(BuiltInRegistries.MENU, RPGInventory.identifier("rpg_mannequin"), RPG_MANNEQUIN_SCREEN_HANDLER);
		Registry.register(BuiltInRegistries.MENU, RPGInventory.identifier("vanilla_mannequin"), VANILLA_MANNEQUIN_SCREEN_HANDLER);
	}
}
