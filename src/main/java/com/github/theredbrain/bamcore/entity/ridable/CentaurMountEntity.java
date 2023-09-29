package com.github.theredbrain.bamcore.entity.ridable;
//
//import net.minecraft.entity.Entity;
//import net.minecraft.entity.EntityType;
//import net.minecraft.entity.LivingEntity;
//import net.minecraft.util.math.MathHelper;
//import net.minecraft.world.World;
//import software.bernie.geckolib.animatable.GeoEntity;
//import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
//import software.bernie.geckolib.core.animation.*;
//import software.bernie.geckolib.core.object.PlayState;
//import software.bernie.geckolib.util.GeckoLibUtil;
//
//// TODO move to mount powers mod
//public class CentaurMountEntity extends MountEntity implements GeoEntity {
//
//    private final AnimatableInstanceCache animatableInstanceCache = GeckoLibUtil.createInstanceCache(this);
//
////    public CentaurMountEntity(EntityType<? extends LivingEntity> entityType, World world, double x, double y, double z) {
////        super(entityType, world, x, y, z);
////    }
//
//    public CentaurMountEntity(EntityType<? extends LivingEntity> entityType, World world) {
//        super(entityType, world);
//    }
//
////    public CentaurMountEntity(EntityType<Entity> entityEntityType, World world) {
////        super();
////    }
//
//    private <E extends GeoEntity>PlayState predicate(AnimationState<E> event) {
////        if (event.isMoving()) {
////
////        }
//        event.getController().setAnimation(RawAnimation.begin().thenLoop("animation.centaur_mount_entity.idle"));
//        return PlayState.CONTINUE;
//    }
//
//    @Override
//    public void updatePassengerPosition(Entity passenger, PositionUpdater positionUpdater) {
//        if (this.hasPassenger(passenger)) {
////        super.updatePassengerPosition(passenger);
////        if (this.lastAngryAnimationProgress > 0.0f) {
////        RPGMod.LOGGER.info("Yaw: " + this.bodyYaw);
//            float f = MathHelper.sin(this.bodyYaw * ((float)Math.PI / 180));
//            float g = MathHelper.cos(this.bodyYaw * ((float)Math.PI / 180));
//            float h = -0.55f;
//    //            float i = 0.15f * this.lastAngryAnimationProgress;
//            positionUpdater.accept(passenger,
//                    this.getX() + (double)(h * f),
//                    this.getY() + this.getMountedHeightOffset() + passenger.getHeightOffset() - 0.25f,
//                    this.getZ() - (double)(h * g));
////            passenger.setPosition(
////                    this.getX() + (double)(h * f),
////                    this.getY() + this.getMountedHeightOffset() + passenger.getHeightOffset() - 0.25f,
////                    this.getZ() - (double)(h * g)
////            );
////            if (passenger instanceof LivingEntity) {
////                ((LivingEntity)passenger).bodyYaw = this.bodyYaw;
////            }
//        }
//    }
//
//    @Override
//    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
//        controllers.add(new AnimationController<>(this, "controller", 0, this::predicate));
//    }
//
//    @Override
//    public AnimatableInstanceCache getAnimatableInstanceCache() {
//        return animatableInstanceCache;
//    }
//
//    @Override
//    public double getTick(Object o) {
//        return 0;
//    }
//
//}
