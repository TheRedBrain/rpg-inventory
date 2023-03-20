package com.github.theredbrain.rpgmod.client.render.renderer;

import com.github.theredbrain.rpgmod.RPGMod;
import com.github.theredbrain.rpgmod.client.render.model.CentaurMountEntityModel;
import com.github.theredbrain.rpgmod.entity.ridable.CentaurMountEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class CentaurMountEntityRenderer extends GeoEntityRenderer<CentaurMountEntity> {
    public CentaurMountEntityRenderer(EntityRendererFactory.Context renderManager) {
        super(renderManager, new CentaurMountEntityModel(new Identifier(RPGMod.MOD_ID, "mounts/centaur_mount_entity")));
    }

//    @Override
//    public Identifier getTextureLocation(CentaurMountEntity animatable) {
//        return new Identifier(RPGMod.MOD_ID, "mounts/centaur_mount_entity");
//    }
}
