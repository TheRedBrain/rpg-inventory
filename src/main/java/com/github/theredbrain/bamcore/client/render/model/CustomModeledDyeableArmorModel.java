package com.github.theredbrain.bamcore.client.render.model;

import com.github.theredbrain.bamcore.item.CustomModeledDyeableArmorItem;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.model.DefaultedItemGeoModel;

public class CustomModeledDyeableArmorModel extends DefaultedItemGeoModel<CustomModeledDyeableArmorItem> {
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
    public CustomModeledDyeableArmorModel(Identifier assetSubpath) {
        super(assetSubpath);
    }
}
