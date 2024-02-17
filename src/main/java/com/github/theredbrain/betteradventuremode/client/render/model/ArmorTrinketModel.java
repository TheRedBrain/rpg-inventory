package com.github.theredbrain.betteradventuremode.client.render.model;

import com.github.theredbrain.betteradventuremode.BetterAdventureMode;
import com.github.theredbrain.betteradventuremode.item.ArmorTrinketItem;
import mod.azure.azurelib.common.api.client.model.GeoModel;
import net.minecraft.util.Identifier;

public class ArmorTrinketModel extends GeoModel<ArmorTrinketItem> {
    private final Identifier assetSubpath;
    private final boolean slim;

    public ArmorTrinketModel(Identifier assetSubpath, boolean slim) {
        super();
        this.assetSubpath = assetSubpath;
        this.slim = slim;
    }

    @Override
    public Identifier getModelResource(ArmorTrinketItem animatable) {
        return BetterAdventureMode.identifier("geo/item/" + assetSubpath.getPath() + (slim ? "_slim" : "") + ".geo.json");
    }

    @Override
    public Identifier getTextureResource(ArmorTrinketItem animatable) {
        return BetterAdventureMode.identifier("textures/item/" + assetSubpath.getPath() + (slim ? "_slim" : "") + ".png");
    }

    @Override
    public Identifier getAnimationResource(ArmorTrinketItem animatable) {
        return null;
    }
}
