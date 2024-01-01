package com.github.theredbrain.bamcore.client.render.renderer;

import com.github.theredbrain.bamcore.client.render.model.SpawnerBoundEntityModel;
import com.github.theredbrain.bamcore.entity.mob.SpawnerBoundEntity;
import mod.azure.azurelib.renderer.GeoEntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;

public class SpawnerBoundEntityRenderer extends GeoEntityRenderer<SpawnerBoundEntity> {
    public SpawnerBoundEntityRenderer(EntityRendererFactory.Context renderManager) {
        super(renderManager, new SpawnerBoundEntityModel());
    }
}
