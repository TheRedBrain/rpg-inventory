package com.github.theredbrain.bamcore;

import com.github.theredbrain.bamcore.client.gui.screen.ingame.TeleporterBlockScreen;
import com.github.theredbrain.bamcore.client.render.block.entity.TeleporterBlockBlockEntityRenderer;
import com.github.theredbrain.bamcore.network.packet.BetterAdventureModCoreClientPacket;
import com.github.theredbrain.bamcore.registry.BlockRegistry;
import com.github.theredbrain.bamcore.registry.ScreenHandlerTypesRegistry;
import com.github.theredbrain.bamcore.util.KeyBindings;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;
import net.spell_engine.api.render.CustomModels;

import java.util.*;

import static com.github.theredbrain.bamcore.registry.EntityRegistry.TELEPORTER_BLOCK_ENTITY;

public class BetterAdventureModCoreClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        KeyBindings.registerKeyBindings();
        registerBlockEntityRenderer();
        BetterAdventureModCoreClientPacket.init();
        registerScreens();
    }

    private void registerBlockEntityRenderer() {
        BlockEntityRendererFactories.register(TELEPORTER_BLOCK_ENTITY, TeleporterBlockBlockEntityRenderer::new);
    }

    private void registerScreens() {
//        HandledScreens.<TriggeredBlockScreenHandler, TriggeredBlockScreen>register(ExtendedScreenTypesRegistry.TRIGGERED_SCREEN_HANDLER, (gui, inventory, title) -> new TriggeredBlockScreen(gui, inventory.player, title));
        HandledScreens.register(ScreenHandlerTypesRegistry.TELEPORTER_BLOCK_SCREEN_HANDLER, TeleporterBlockScreen::new);
    }
}
