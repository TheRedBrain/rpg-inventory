package com.github.theredbrain.bamcore.client.render.model;

import com.github.theredbrain.bamcore.BetterAdventureMode;
import com.github.theredbrain.bamcore.api.item.AccessoryTrinketItem;
import mod.azure.azurelib.common.api.client.model.GeoModel;
import net.minecraft.util.Identifier;

public class AccessoryTrinketModel extends GeoModel<AccessoryTrinketItem> {
    private final Identifier assetSubpath;

    public AccessoryTrinketModel(Identifier assetSubpath) {
        super();
        this.assetSubpath = assetSubpath;
    }

    @Override
    public Identifier getModelResource(AccessoryTrinketItem animatable) {
        return BetterAdventureMode.identifier("geo/item/" + assetSubpath.getPath() + ".geo.json");
    }

    @Override
    public Identifier getTextureResource(AccessoryTrinketItem animatable) {
        return BetterAdventureMode.identifier("textures/item/" + assetSubpath.getPath() + ".png");
    }

    @Override
    public Identifier getAnimationResource(AccessoryTrinketItem animatable) {
        return null;
    }
}
