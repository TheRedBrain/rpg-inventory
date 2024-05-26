package com.github.theredbrain.rpginventory.client.render.renderer;

import com.github.theredbrain.rpginventory.item.ArmorTrinketItem;
import com.github.theredbrain.rpginventory.client.render.model.ArmorTrinketModel;
import net.minecraft.util.Identifier;

public class ArmorTrinketRenderer extends ModeledTrinketRenderer<ArmorTrinketItem>{
    public ArmorTrinketRenderer(Identifier assetSubPath, boolean slim) {
        super(new ArmorTrinketModel(assetSubPath, slim));
    }
}
