package com.github.theredbrain.rpgmod.client.render.model;

import com.github.theredbrain.rpgmod.item.CustomModeledVanillaArmorItem;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.model.DefaultedItemGeoModel;

public class CustomModeledVanillaArmorModel extends DefaultedItemGeoModel<CustomModeledVanillaArmorItem> {
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
    public CustomModeledVanillaArmorModel(Identifier assetSubpath) {
        super(assetSubpath);
    }
}
