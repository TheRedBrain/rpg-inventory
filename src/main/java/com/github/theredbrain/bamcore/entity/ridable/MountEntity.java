package com.github.theredbrain.bamcore.entity.ridable;
//
//import com.github.theredbrain.rpgmod.RPGMod;
//import net.minecraft.entity.Entity;
//import net.minecraft.entity.EntityType;
//import net.minecraft.entity.EquipmentSlot;
//import net.minecraft.entity.LivingEntity;
//import net.minecraft.entity.attribute.DefaultAttributeContainer;
//import net.minecraft.entity.mob.MobEntity;
//import net.minecraft.entity.player.PlayerEntity;
//import net.minecraft.item.ItemStack;
//import net.minecraft.util.ActionResult;
//import net.minecraft.util.Arm;
//import net.minecraft.util.Hand;
//import net.minecraft.util.collection.DefaultedList;
//import net.minecraft.util.math.MathHelper;
//import net.minecraft.util.math.Vec3d;
//import net.minecraft.world.World;
//import org.jetbrains.annotations.Nullable;
//// TODO move to mount powers mod
//public class MountEntity extends LivingEntity {
//
//    private final DefaultedList<ItemStack> mount_armor = DefaultedList.ofSize(1, ItemStack.EMPTY);
//
//    public MountEntity(EntityType<? extends LivingEntity> entityType, World world) {
//        super(entityType, world);
//    }
//
////    public MountEntity(EntityType<? extends LivingEntity> entityType, World world, double x, double y, double z) {
////        super(entityType, world);
////    }
//
//    public static DefaultAttributeContainer.Builder createMountAttributes() {
//        return LivingEntity.createLivingAttributes();//DefaultAttributeContainer.builder().add(EntityAttributes.GENERIC_MAX_HEALTH).add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE).add(EntityAttributes.GENERIC_MOVEMENT_SPEED).add(EntityAttributes.GENERIC_ARMOR).add(EntityAttributes.GENERIC_ARMOR_TOUGHNESS);
//    }
//
////    @Override
////    public void tick() {
////        super.tick();
////        if (this.getPrimaryPassenger() == null) {
////
////        }
////    }
//
//    @Override
//    public Iterable<ItemStack> getArmorItems() {
//        return this.mount_armor;
//    }
//
//    @Override
//    public ItemStack getEquippedStack(EquipmentSlot slot) {
//        if (slot == EquipmentSlot.CHEST) {
//            return this.mount_armor.get(0);
//        }
//        return ItemStack.EMPTY;
//    }
//
//    @Override
//    public void equipStack(EquipmentSlot slot, ItemStack stack) {
//        if (slot == EquipmentSlot.CHEST) {
//            this.mount_armor.set(0, stack);
//        }
//    }
//
//    @Override
//    public ActionResult interact(PlayerEntity player, Hand hand) {
//        if (!this.hasPassengers()) {
//            player.startRiding(this);
//
//            return super.interact(player, hand);
//        }
//
//        return super.interact(player, hand);
//    }
//
////    @Override
////    public void updatePassengerPosition(Entity passenger) {
//////        super.updatePassengerPosition(passenger);
//////        if (this.lastAngryAnimationProgress > 0.0f) {
//////        RPGMod.LOGGER.info("Yaw: " + this.bodyYaw);
////        float f = MathHelper.sin(this.bodyYaw * ((float)Math.PI / 180));
////        float g = MathHelper.cos(this.bodyYaw * ((float)Math.PI / 180));
////        float h = -0.55f;
//////            float i = 0.15f * this.lastAngryAnimationProgress;
////        passenger.setPosition(
////            this.getX() + (double)(h * f),
////            this.getY() + this.getMountedHeightOffset() + passenger.getHeightOffset() - 0.25f,
////            this.getZ() - (double)(h * g)
////        );
//////            if (passenger instanceof LivingEntity) {
//////                ((LivingEntity)passenger).bodyYaw = this.bodyYaw;
//////            }
//////        }
////    }
////
////    // Apply player-controlled movement
////    @Override
////    public void travel(Vec3d movementInput) {
////        if (this.isAlive()) {
////            if (this.hasPassengers()) {
////                LivingEntity passenger = (LivingEntity)getPrimaryPassenger();
////
////                this.setRotation(passenger.getYaw(), passenger.getPitch() * 0.5f);
////                this.bodyYaw = this.headYaw = this.getYaw();
////                this.prevYaw = this.headYaw;
////                float f = passenger.sidewaysSpeed * 0.5f;
////                float g = passenger.forwardSpeed;
////                if (g <= 0.0f) {
////                    g *= 0.25f;
//////                    this.soundTicks = 0;
////                }
////                this.airStrafingSpeed = this.getMovementSpeed() * 0.1f;
////                if (this.isLogicalSideForUpdatingMovement()) {
////                    this.setMovementSpeed(passenger.getMovementSpeed());
////                    super.travel(new Vec3d(f, movementInput.y, g));
////                } else if (passenger instanceof PlayerEntity) {
////                    this.setVelocity(this.getX() - this.lastRenderX, this.getY() - this.lastRenderY, this.getZ() - this.lastRenderZ);
////                }
//////                if (this.onGround) {
//////                    this.jumpStrength = 0.0f;
//////                    this.setInAir(false);
//////                }
////                this.updateLimbs(this, false);
////                this.tryCheckBlockCollision();
//////                this.prevYaw = getYaw();
//////                this.prevPitch = getPitch();
//////
////////                compare prevYaw and passenger.getYaw()
//////                if (prevYaw < passenger.getYaw() && prevYaw - passenger.getYaw() < -30) {
//////                    setYaw(passenger.getYaw() - 30);
//////                } else if (prevYaw > passenger.getYaw() && prevYaw - passenger.getYaw() > 30) {
//////                    setYaw(passenger.getYaw() + 30);
//////                }
////////                setYaw(passenger.getYaw());
//////                setPitch(passenger.getPitch() * 0.5f);
//////                setRotation(getYaw(), getPitch());
//////
//////                this.bodyYaw = this.getYaw();
//////                this.headYaw = this.bodyYaw;
//////                float x = passenger.sidewaysSpeed * 0.25F;
//////                float z = passenger.forwardSpeed;
//////
//////                if (z <= 0)
//////                    z *= 0.35f;
//////
//////                this.setMovementSpeed(passenger.getMovementSpeed());
//////                super.travel(new Vec3d(x, pos.y, z));
////            }
////        }
////    }
//
//    @Override
//    public Arm getMainArm() {
//        return Arm.RIGHT;
//    }
//
//    // Get the controlling passenger
////    @Nullable
////    @Override
////    public Entity getPrimaryPassenger() {
////        return getFirstPassenger();
////    }
//
////    @Override
////    public boolean isLogicalSideForUpdatingMovement() {
////        return this.hasPrimaryPassenger() && super.isLogicalSideForUpdatingMovement();
////    }
//
////    @Override
////    public void setSneaking(boolean sneaking) {
////        super.setSneaking(sneaking);
////    }
//}
