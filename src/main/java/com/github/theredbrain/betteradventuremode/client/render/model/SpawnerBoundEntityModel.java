package com.github.theredbrain.betteradventuremode.client.render.model;

import com.github.theredbrain.betteradventuremode.entity.mob.SpawnerBoundEntity;
import mod.azure.azurelib.common.api.client.model.GeoModel;
import net.minecraft.util.Identifier;

public class SpawnerBoundEntityModel extends GeoModel<SpawnerBoundEntity> {
    @Override
    public Identifier getModelResource(SpawnerBoundEntity animatable) {
        Identifier spawnerBoundEntityModelIdentifier = animatable.getSpawnerBoundEntityModelIdentifier();
        return new Identifier(spawnerBoundEntityModelIdentifier.getNamespace(), "geo/entity/" + spawnerBoundEntityModelIdentifier.getPath() + ".geo.json");
    }

    @Override
    public Identifier getTextureResource(SpawnerBoundEntity animatable) {
        Identifier spawnerBoundEntityTextureIdentifier =  animatable.getSpawnerBoundEntityTextureIdentifier();
        return new Identifier(spawnerBoundEntityTextureIdentifier.getNamespace(), "textures/entity/" + spawnerBoundEntityTextureIdentifier.getPath() + ".png");
    }

    @Override
    public Identifier getAnimationResource(SpawnerBoundEntity animatable) {
        Identifier spawnerBoundEntityAnimationsIdentifier =  animatable.getSpawnerBoundEntityAnimationsIdentifier();
        return new Identifier(spawnerBoundEntityAnimationsIdentifier.getNamespace(), "animations/entity/" + spawnerBoundEntityAnimationsIdentifier.getPath() + ".animation.json");
    }
}
