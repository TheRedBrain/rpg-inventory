package com.github.theredbrain.bamcore.client.render.model;

import com.github.theredbrain.bamcore.BetterAdventureModeCore;
import com.github.theredbrain.bamcore.api.item.AccessoryTrinketItem;
import mod.azure.azurelib.model.GeoModel;
import net.minecraft.util.Identifier;

public class AccessoryTrinketModel extends GeoModel<AccessoryTrinketItem> {
    private final Identifier assetSubpath;

    public AccessoryTrinketModel(Identifier assetSubpath) {
        super();
        this.assetSubpath = assetSubpath;
    }

    @Override
    public Identifier getModelResource(AccessoryTrinketItem animatable) {
        return BetterAdventureModeCore.identifier("geo/item/" + assetSubpath.getPath() + ".geo.json");
    }

    @Override
    public Identifier getTextureResource(AccessoryTrinketItem animatable) {
        return BetterAdventureModeCore.identifier("textures/item/" + assetSubpath.getPath() + ".png");
    }

    @Override
    public Identifier getAnimationResource(AccessoryTrinketItem animatable) {
        return null;
    }
}
