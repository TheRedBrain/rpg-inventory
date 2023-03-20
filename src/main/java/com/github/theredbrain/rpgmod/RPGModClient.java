package com.github.theredbrain.rpgmod;

import com.github.theredbrain.rpgmod.client.render.renderer.CentaurMountEntityRenderer;
import com.github.theredbrain.rpgmod.entity.ridable.CentaurMountEntity;
import com.github.theredbrain.rpgmod.registry.BlockRegistry;
import com.github.theredbrain.rpgmod.registry.EntityRegistry;
import com.github.theredbrain.rpgmod.util.KeyBindings;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.render.RenderLayer;
import software.bernie.geckolib.renderer.GeoArmorRenderer;

public class RPGModClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        KeyBindings.registerKeyBindings();
        registerTransparency();
        registerEntityRenderer();
    }

    private void registerTransparency() {
        BlockRenderLayerMap.INSTANCE.putBlock(BlockRegistry.INTERACTIVE_BERRY_BUSH_BLOCK, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(BlockRegistry.INTERACTIVE_RED_MUSHROOM_BLOCK, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(BlockRegistry.INTERACTIVE_BROWN_MUSHROOM_BLOCK, RenderLayer.getCutout());
    }

    private void registerEntityRenderer() {
        EntityRendererRegistry.register(EntityRegistry.CENTAUR_MOUNT, CentaurMountEntityRenderer::new);
    }
}
