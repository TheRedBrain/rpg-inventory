package com.github.theredbrain.rpgmod.client.render.model;

import com.github.theredbrain.rpgmod.geckolib.DuckDefaultedGeoModelMixin;
import com.github.theredbrain.rpgmod.item.CustomModeledVanillaArmorItem;
import com.github.theredbrain.rpgmod.item.PlayerSkinArmorItem;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.model.DefaultedGeoModel;
import software.bernie.geckolib.model.DefaultedItemGeoModel;

public class PlayerSkinArmorModel extends DefaultedItemGeoModel<PlayerSkinArmorItem> {
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
    public PlayerSkinArmorModel(Identifier assetSubpath) {
        super(assetSubpath);
    }

//    public DefaultedGeoModel<PlayerSkinArmorItem> withCustomTexture(Identifier customTexture) {
//        return ((DuckDefaultedGeoModelMixin) super).withCustomTexture(customTexture);
//    }
}
