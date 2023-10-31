package com.github.theredbrain.bamcore.client.render.model;

import com.github.theredbrain.bamcore.BetterAdventureModeCore;
import com.github.theredbrain.bamcore.api.item.ArmorTrinketItem;
import mod.azure.azurelibarmor.model.GeoModel;
import net.minecraft.util.Identifier;

public class ArmorTrinketModel extends GeoModel<ArmorTrinketItem> {
    private final Identifier assetSubpath;

    public ArmorTrinketModel(Identifier assetSubpath) {
        super();
        this.assetSubpath = assetSubpath;
    }

    @Override
    public Identifier getModelResource(ArmorTrinketItem animatable) {
        return BetterAdventureModeCore.identifier("geo/item/" + assetSubpath.getPath() + ".geo.json");
    }

    @Override
    public Identifier getTextureResource(ArmorTrinketItem animatable) {
        return BetterAdventureModeCore.identifier("textures/item/" + assetSubpath.getPath() + ".png");
    }

    @Override
    public Identifier getAnimationResource(ArmorTrinketItem animatable) {
        return null;
    }
}
