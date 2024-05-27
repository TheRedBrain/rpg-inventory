package com.github.theredbrain.rpginventory.client.render.model;

import com.github.theredbrain.rpginventory.RPGInventory;
import com.github.theredbrain.rpginventory.item.ModelledTrinketItem;
//import mod.azure.azurelib.common.api.client.model.GeoModel;
import mod.azure.azurelib.model.GeoModel;
import net.minecraft.util.Identifier;

public class ModeledTrinketModel extends GeoModel<ModelledTrinketItem> {
    private final Identifier assetSubpath;
    private final boolean slim;

    public ModeledTrinketModel(Identifier assetSubpath, boolean slim) {
        super();
        this.assetSubpath = assetSubpath;
        this.slim = slim;
    }

    @Override
    public Identifier getModelResource(ModelledTrinketItem animatable) {
        return RPGInventory.identifier("geo/item/" + assetSubpath.getPath() + (slim ? "_slim" : "") + ".geo.json");
    }

    @Override
    public Identifier getTextureResource(ModelledTrinketItem animatable) {
        return RPGInventory.identifier("textures/item/" + assetSubpath.getPath() + (slim ? "_slim" : "") + ".png");
    }

    @Override
    public Identifier getAnimationResource(ModelledTrinketItem animatable) {
        return null;
    }
}
