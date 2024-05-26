package com.github.theredbrain.rpginventory.client.render.renderer;

import com.github.theredbrain.rpginventory.item.AccessoryTrinketItem;
import com.github.theredbrain.rpginventory.client.render.model.AccessoryTrinketModel;
import net.minecraft.util.Identifier;

public class AccessoryTrinketRenderer extends ModeledTrinketRenderer<AccessoryTrinketItem>{
    public AccessoryTrinketRenderer(Identifier assetSubPath, boolean slim) {
        super(new AccessoryTrinketModel(assetSubPath, slim));
    }
}
