package com.github.theredbrain.betteradventuremode.client.render.model;

import com.github.theredbrain.betteradventuremode.BetterAdventureMode;
import com.github.theredbrain.betteradventuremode.item.AccessoryTrinketItem;
import mod.azure.azurelib.common.api.client.model.GeoModel;
import net.minecraft.util.Identifier;

public class AccessoryTrinketModel extends GeoModel<AccessoryTrinketItem> {
    private final Identifier assetSubpath;
    private final boolean slim;

    public AccessoryTrinketModel(Identifier assetSubpath, boolean slim) {
        super();
        this.assetSubpath = assetSubpath;
        this.slim = slim;
    }

    @Override
    public Identifier getModelResource(AccessoryTrinketItem animatable) {
        return BetterAdventureMode.identifier("geo/item/" + assetSubpath.getPath() + (slim ? "_slim" : "") + ".geo.json");
    }

    @Override
    public Identifier getTextureResource(AccessoryTrinketItem animatable) {
        return BetterAdventureMode.identifier("textures/item/" + assetSubpath.getPath() + (slim ? "_slim" : "") + ".png");
    }

    @Override
    public Identifier getAnimationResource(AccessoryTrinketItem animatable) {
        return null;
    }
}
