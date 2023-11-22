package com.github.theredbrain.bamcore.item;

import dev.emi.trinkets.api.TrinketItem;
import mod.azure.azurelibarmor.animatable.GeoItem;
import mod.azure.azurelibarmor.core.animatable.instance.AnimatableInstanceCache;
import mod.azure.azurelibarmor.core.animation.AnimatableManager;
import mod.azure.azurelibarmor.util.AzureLibUtil;
import net.minecraft.util.Identifier;

import java.util.function.Supplier;

public abstract class ModeledTrinketItem extends TrinketItem implements GeoItem {
    public final Identifier assetSubpath;
    private final AnimatableInstanceCache cache = AzureLibUtil.createInstanceCache(this);
    private final Supplier<Object> renderProvider = GeoItem.makeRenderer(this);

    public ModeledTrinketItem(Identifier assetSubpath, Settings settings) {
        super(settings);
        this.assetSubpath = assetSubpath;
    }

    @Override
    public Supplier<Object> getRenderProvider() {
        return renderProvider;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {}

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }
}
