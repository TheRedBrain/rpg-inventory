package com.github.theredbrain.betteradventuremode.client.render.model;

import com.github.theredbrain.betteradventuremode.BetterAdventureMode;
import com.github.theredbrain.betteradventuremode.entity.mob.SpawnerBoundEntity;
import mod.azure.azurelib.common.api.client.model.GeoModel;
import net.minecraft.util.Identifier;

public class SpawnerBoundEntityModel extends GeoModel<SpawnerBoundEntity> {
    Identifier defaultModelIdentifier = BetterAdventureMode.identifier("spawner_bound_entity/default_spawner_bound_entity");
    Identifier defaultTextureIdentifier = BetterAdventureMode.identifier("spawner_bound_entity/default_spawner_bound_entity");;
    Identifier defaultAnimationsIdentifier = BetterAdventureMode.identifier("spawner_bound_entity/default_spawner_bound_entity");;
    @Override
    public Identifier getModelResource(SpawnerBoundEntity animatable) {
        Identifier modelIdentifier = Identifier.tryParse(animatable.getModelIdentifierString());
        if (modelIdentifier == null) {
            modelIdentifier = defaultModelIdentifier;
        }
        return new Identifier(modelIdentifier.getNamespace(), "geo/entity/" + modelIdentifier.getPath() + ".geo.json");
    }

    @Override
    public Identifier getTextureResource(SpawnerBoundEntity animatable) {
        Identifier textureIdentifier = Identifier.tryParse(animatable.getTextureIdentifierString());
        if (textureIdentifier == null) {
            textureIdentifier = defaultTextureIdentifier;
        }
        return new Identifier(textureIdentifier.getNamespace(), "textures/entity/" + textureIdentifier.getPath() + ".png");
    }

    @Override
    public Identifier getAnimationResource(SpawnerBoundEntity animatable) {
        Identifier animationIdentifier = Identifier.tryParse(animatable.getAnimationIdentifierString());
        if (animationIdentifier == null) {
            animationIdentifier = defaultAnimationsIdentifier;
        }
        return new Identifier(animationIdentifier.getNamespace(), "animations/entity/" + animationIdentifier.getPath() + ".animation.json");
    }
}
