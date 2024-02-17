package com.github.theredbrain.betteradventuremode.client.render.renderer;

import com.github.theredbrain.betteradventuremode.item.ArmorTrinketItem;
import com.github.theredbrain.betteradventuremode.client.render.model.ArmorTrinketModel;
import net.minecraft.util.Identifier;

public class ArmorTrinketRenderer extends ModeledTrinketRenderer<ArmorTrinketItem>{
    public ArmorTrinketRenderer(Identifier assetSubPath, boolean slim) {
        super(new ArmorTrinketModel(assetSubPath, slim));
    }
}
