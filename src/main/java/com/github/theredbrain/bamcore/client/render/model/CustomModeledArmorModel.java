package com.github.theredbrain.bamcore.client.render.model;

import com.github.theredbrain.bamcore.item.CustomModeledArmorItem;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.model.DefaultedItemGeoModel;

public class CustomModeledArmorModel extends DefaultedItemGeoModel<CustomModeledArmorItem> {
    /**
     * Create a new instance of this model class.<br>
     * The asset path should be the truncated relative path from the base folder.<br>
     * E.G.
     * <pre>{@code
     * 	new ResourceLocation("myMod", "armor/obsidian")
     * }</pre>
     *
     * @param assetSubpath
     */
    public CustomModeledArmorModel(Identifier assetSubpath) {
        super(assetSubpath);
    }
}
