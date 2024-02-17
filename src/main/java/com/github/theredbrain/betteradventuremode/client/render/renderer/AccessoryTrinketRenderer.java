package com.github.theredbrain.betteradventuremode.client.render.renderer;

import com.github.theredbrain.betteradventuremode.item.AccessoryTrinketItem;
import com.github.theredbrain.betteradventuremode.client.render.model.AccessoryTrinketModel;
import net.minecraft.util.Identifier;

public class AccessoryTrinketRenderer extends ModeledTrinketRenderer<AccessoryTrinketItem>{
    public AccessoryTrinketRenderer(Identifier assetSubPath, boolean slim) {
        super(new AccessoryTrinketModel(assetSubPath, slim));
    }
}
