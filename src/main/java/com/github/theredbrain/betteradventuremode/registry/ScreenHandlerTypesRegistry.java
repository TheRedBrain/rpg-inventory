package com.github.theredbrain.betteradventuremode.registry;

import com.github.theredbrain.betteradventuremode.BetterAdventureMode;
import com.github.theredbrain.betteradventuremode.screen.*;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.resource.featuretoggle.FeatureFlags;
import net.minecraft.screen.ScreenHandlerType;

public class ScreenHandlerTypesRegistry {
    public static final ScreenHandlerType<BackpackScreenHandler> BACKPACK_SCREEN_HANDLER = new ScreenHandlerType<>(BackpackScreenHandler::new, FeatureFlags.VANILLA_FEATURES);
    public static final ScreenHandlerType<CraftingBenchBlockScreenHandler> CRAFTING_BENCH_BLOCK_SCREEN_HANDLER = new ExtendedScreenHandlerType<>(CraftingBenchBlockScreenHandler::new);
    public static final ScreenHandlerType<MannequinScreenHandler> MANNEQUIN_SCREEN_HANDLER = new ExtendedScreenHandlerType<>(MannequinScreenHandler::new);
    public static final ScreenHandlerType<ShopBlockScreenHandler> SHOP_BLOCK_SCREEN_HANDLER = new ExtendedScreenHandlerType<>(ShopBlockScreenHandler::new);
    public static final ScreenHandlerType<TeleporterBlockScreenHandler> TELEPORTER_BLOCK_SCREEN_HANDLER = new ExtendedScreenHandlerType<>(TeleporterBlockScreenHandler::new);

    public static void registerAll() {
        Registry.register(Registries.SCREEN_HANDLER, BetterAdventureMode.identifier("backpack"), BACKPACK_SCREEN_HANDLER);
        Registry.register(Registries.SCREEN_HANDLER, BetterAdventureMode.identifier("crafting_bench"), CRAFTING_BENCH_BLOCK_SCREEN_HANDLER);
        Registry.register(Registries.SCREEN_HANDLER, BetterAdventureMode.identifier("mannequin"), MANNEQUIN_SCREEN_HANDLER);
        Registry.register(Registries.SCREEN_HANDLER, BetterAdventureMode.identifier("shop"), SHOP_BLOCK_SCREEN_HANDLER);
        Registry.register(Registries.SCREEN_HANDLER, BetterAdventureMode.identifier("teleporter"), TELEPORTER_BLOCK_SCREEN_HANDLER);
    }
}
