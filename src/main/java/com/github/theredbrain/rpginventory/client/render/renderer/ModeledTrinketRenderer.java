package com.github.theredbrain.rpginventory.client.render.renderer;

import com.github.theredbrain.rpginventory.item.ModelledTrinketItem;
import com.github.theredbrain.rpginventory.client.render.model.ModeledTrinketModel;
import net.minecraft.util.Identifier;

public class ModeledTrinketRenderer extends AbstractModeledTrinketRenderer<ModelledTrinketItem> {
    public ModeledTrinketRenderer(Identifier assetSubPath, boolean slim) {
        super(new ModeledTrinketModel(assetSubPath, slim));
    }
}
