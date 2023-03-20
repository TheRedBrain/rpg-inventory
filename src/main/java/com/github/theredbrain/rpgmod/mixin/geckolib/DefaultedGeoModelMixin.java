package com.github.theredbrain.rpgmod.mixin.geckolib;

import com.github.theredbrain.rpgmod.geckolib.DuckDefaultedGeoModelMixin;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.model.DefaultedGeoModel;
import software.bernie.geckolib.model.GeoModel;

@Mixin(DefaultedGeoModel.class)
public abstract class DefaultedGeoModelMixin<T extends GeoAnimatable> extends GeoModel<T> implements DuckDefaultedGeoModelMixin<T> {

    @Shadow
    private Identifier texturePath;

    @Override
    public DefaultedGeoModel<T> withCustomTexture(Identifier customTexture) {
        this.texturePath = customTexture;

        return (DefaultedGeoModel<T>) (Object) this;
    }
}
