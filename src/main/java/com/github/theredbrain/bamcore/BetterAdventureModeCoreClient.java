package com.github.theredbrain.bamcore;

import com.github.theredbrain.bamcore.network.packet.BetterAdventureModCoreClientPacket;
import com.github.theredbrain.bamcore.registry.ScreenHandlerTypesRegistry;
import com.github.theredbrain.bamcore.util.KeyBindings;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;

public class BetterAdventureModeCoreClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        KeyBindings.registerKeyBindings();
        registerBlockEntityRenderer();
        BetterAdventureModCoreClientPacket.init();
        registerScreens();
    }

    private void registerBlockEntityRenderer() {
        // TODO move to bamdimensions
//        BlockEntityRendererFactories.register(TELEPORTER_BLOCK_ENTITY, TeleporterBlockBlockEntityRenderer::new);
    }

    private void registerScreens() {
//        HandledScreens.<TriggeredBlockScreenHandler, TriggeredBlockScreen>register(ExtendedScreenTypesRegistry.TRIGGERED_SCREEN_HANDLER, (gui, inventory, title) -> new TriggeredBlockScreen(gui, inventory.player, title));
        // TODO move to bamdimensions
//        HandledScreens.register(ScreenHandlerTypesRegistry.TELEPORTER_BLOCK_SCREEN_HANDLER, TeleporterBlockScreen::new);
    }
}
