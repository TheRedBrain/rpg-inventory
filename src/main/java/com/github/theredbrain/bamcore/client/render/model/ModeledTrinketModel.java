package com.github.theredbrain.bamcore.client.render.model;
//
//import com.github.theredbrain.bamcore.BetterAdventureModeCore;
//import com.github.theredbrain.bamcore.api.item.ModeledTrinketItem;
//import mod.azure.azurelibarmor.model.GeoModel;
//import net.minecraft.util.Identifier;
//
//public class ModeledTrinketModel extends GeoModel<ModeledTrinketItem> {
//    private final Identifier assetSubpath;
//    /**
//     * Create a new instance of this model class.<br>
//     * The asset path should be the truncated relative path from the base folder.<br>
//     * E.G.
//     * <pre>{@code
//     * 	new ResourceLocation("myMod", "armor/obsidian")
//     * }</pre>
//     *
//     * @param assetSubpath
//     */
//    public ModeledTrinketModel(Identifier assetSubpath) {
//        super();
//        this.assetSubpath = assetSubpath;
//    }
//
//    @Override
//    public Identifier getModelResource(ModeledTrinketItem animatable) {
//        return BetterAdventureModeCore.identifier("geo/item/" + assetSubpath + ".geo.json");
//    }
//
//    @Override
//    public Identifier getTextureResource(ModeledTrinketItem animatable) {
//        return BetterAdventureModeCore.identifier("textures/item/" + assetSubpath + ".png");
//    }
//
//    @Override
//    public Identifier getAnimationResource(ModeledTrinketItem animatable) {
//        return null;
//    }
//}
