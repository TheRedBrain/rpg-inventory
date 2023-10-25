package com.github.theredbrain.bamcore.registry;

import com.github.theredbrain.bamcore.BetterAdventureModeCore;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.screen.ScreenHandlerType;

public class ScreenHandlerTypesRegistry {

    // TODO move to bamdimensions
//    public static final ScreenHandlerType<TeleporterBlockScreenHandler> TELEPORTER_BLOCK_SCREEN_HANDLER = new ExtendedScreenHandlerType<>(TeleporterBlockScreenHandler::new);

    public static void registerAll() {
//        Registry.register(Registries.SCREEN_HANDLER, BetterAdventureModeCore.identifier("teleporter"), TELEPORTER_BLOCK_SCREEN_HANDLER);
    }
}
