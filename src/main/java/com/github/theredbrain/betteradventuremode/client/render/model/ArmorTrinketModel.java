package com.github.theredbrain.betteradventuremode.client.render.model;

import com.github.theredbrain.betteradventuremode.BetterAdventureMode;
import com.github.theredbrain.betteradventuremode.item.ArmorTrinketItem;
import mod.azure.azurelib.common.api.client.model.GeoModel;
import net.minecraft.util.Identifier;

public class ArmorTrinketModel extends GeoModel<ArmorTrinketItem> {
    private final Identifier assetSubpath;

    public ArmorTrinketModel(Identifier assetSubpath) {
        super();
        this.assetSubpath = assetSubpath;
    }

    @Override
    public Identifier getModelResource(ArmorTrinketItem animatable) {
        return BetterAdventureMode.identifier("geo/item/" + assetSubpath.getPath() + ".geo.json");
    }

    @Override
    public Identifier getTextureResource(ArmorTrinketItem animatable) {
        return BetterAdventureMode.identifier("textures/item/" + assetSubpath.getPath() + ".png");
    }

    @Override
    public Identifier getAnimationResource(ArmorTrinketItem animatable) {
        return null;
    }
}
