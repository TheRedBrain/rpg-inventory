package com.github.theredbrain.betteradventuremode.client.render.renderer;

import com.github.theredbrain.betteradventuremode.client.render.model.SpawnerBoundEntityModel;
import com.github.theredbrain.betteradventuremode.entity.mob.SpawnerBoundEntity;
import mod.azure.azurelib.common.api.client.renderer.GeoEntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;

public class SpawnerBoundEntityRenderer extends GeoEntityRenderer<SpawnerBoundEntity> {
    public SpawnerBoundEntityRenderer(EntityRendererFactory.Context renderManager) {
        super(renderManager, new SpawnerBoundEntityModel());
    }
}
