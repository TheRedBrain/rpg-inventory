package com.github.theredbrain.bamcore;

import com.github.theredbrain.bamcore.network.packet.BetterAdventureModCoreClientPacket;
import com.github.theredbrain.bamcore.registry.ItemRegistry;
import com.github.theredbrain.bamcore.registry.KeyBindingsRegistry;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;

public class BetterAdventureModeCoreClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        KeyBindingsRegistry.registerKeyBindings();
        registerBlockEntityRenderer();
        BetterAdventureModCoreClientPacket.init();
        registerScreens();
        registerModelPredicateProviders();
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

    private void registerModelPredicateProviders() {
//        HandledScreens.<TriggeredBlockScreenHandler, TriggeredBlockScreen>register(ExtendedScreenTypesRegistry.TRIGGERED_SCREEN_HANDLER, (gui, inventory, title) -> new TriggeredBlockScreen(gui, inventory.player, title));
        // TODO move to bamdimensions
//        HandledScreens.register(ScreenHandlerTypesRegistry.TELEPORTER_BLOCK_SCREEN_HANDLER, TeleporterBlockScreen::new);
        ModelPredicateProviderRegistry.register(ItemRegistry.TEST_BUCKLER, new Identifier("blocking"), (stack, world, entity, seed) -> entity != null && entity.isUsingItem() && entity.getActiveItem() == stack ? 1.0f : 0.0f);
        ModelPredicateProviderRegistry.register(ItemRegistry.TEST_NORMAL_SHIELD, new Identifier("blocking"), (stack, world, entity, seed) -> entity != null && entity.isUsingItem() && entity.getActiveItem() == stack ? 1.0f : 0.0f);
        ModelPredicateProviderRegistry.register(ItemRegistry.TEST_TOWER_SHIELD, new Identifier("blocking"), (stack, world, entity, seed) -> entity != null && entity.isUsingItem() && entity.getActiveItem() == stack ? 1.0f : 0.0f);
    }
}
