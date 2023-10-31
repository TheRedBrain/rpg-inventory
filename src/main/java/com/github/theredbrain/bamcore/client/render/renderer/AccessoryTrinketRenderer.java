package com.github.theredbrain.bamcore.client.render.renderer;

import com.github.theredbrain.bamcore.api.item.AccessoryTrinketItem;
import com.github.theredbrain.bamcore.client.render.model.AccessoryTrinketModel;
import net.minecraft.util.Identifier;

public class AccessoryTrinketRenderer extends ModeledTrinketRenderer<AccessoryTrinketItem>{
    public AccessoryTrinketRenderer(Identifier assetSubPath) {
        super(new AccessoryTrinketModel(assetSubPath));
    }
}
