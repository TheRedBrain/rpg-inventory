package com.github.theredbrain.bamcore.client.render.renderer;

import com.github.theredbrain.bamcore.api.item.ArmorTrinketItem;
import com.github.theredbrain.bamcore.client.render.model.ArmorTrinketModel;
import net.minecraft.util.Identifier;

public class ArmorTrinketRenderer extends ModeledTrinketRenderer<ArmorTrinketItem>{
    public ArmorTrinketRenderer(Identifier assetSubPath) {
        super(new ArmorTrinketModel(assetSubPath));
    }
}
