package com.github.theredbrain.bamcore.registry;

import com.github.theredbrain.bamcore.BetterAdventureModCore;
import com.github.theredbrain.bamcore.screen.TeleporterBlockScreenHandler;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.screen.ScreenHandlerType;

public class ScreenHandlerTypesRegistry {

    public static final ScreenHandlerType<TeleporterBlockScreenHandler> TELEPORTER_BLOCK_SCREEN_HANDLER = new ExtendedScreenHandlerType<>(TeleporterBlockScreenHandler::new);

    public static void registerAll() {
        Registry.register(Registries.SCREEN_HANDLER, BetterAdventureModCore.identifier("teleporter"), TELEPORTER_BLOCK_SCREEN_HANDLER);
    }
}
