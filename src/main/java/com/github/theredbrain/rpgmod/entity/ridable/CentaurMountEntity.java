package com.github.theredbrain.rpgmod.entity.ridable;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.world.World;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.*;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

public class CentaurMountEntity extends MountEntity implements GeoEntity {

    private final AnimatableInstanceCache animatableInstanceCache = GeckoLibUtil.createInstanceCache(this);

//    public CentaurMountEntity(EntityType<? extends LivingEntity> entityType, World world, double x, double y, double z) {
//        super(entityType, world, x, y, z);
//    }

    public CentaurMountEntity(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

//    public CentaurMountEntity(EntityType<Entity> entityEntityType, World world) {
//        super();
//    }

    private <E extends GeoEntity>PlayState predicate(AnimationState<E> event) {
//        if (event.isMoving()) {
//
//        }
        event.getController().setAnimation(RawAnimation.begin().thenLoop("animation.centaur_mount_entity.idle"));
        return PlayState.CONTINUE;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "controller", 0, this::predicate));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return animatableInstanceCache;
    }

    @Override
    public double getTick(Object o) {
        return 0;
    }

}
