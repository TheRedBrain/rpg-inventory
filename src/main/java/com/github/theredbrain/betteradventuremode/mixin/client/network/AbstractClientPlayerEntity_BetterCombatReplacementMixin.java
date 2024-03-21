package com.github.theredbrain.betteradventuremode.mixin.client.network;

import com.github.theredbrain.betteradventuremode.data.WeaponPoses;
import com.github.theredbrain.betteradventuremode.entity.player.DuckPlayerEntityMixin;
import com.github.theredbrain.betteradventuremode.registry.WeaponPosesRegistry;
import com.mojang.authlib.GameProfile;
import dev.kosmx.playerAnim.api.firstPerson.FirstPersonConfiguration;
import dev.kosmx.playerAnim.api.firstPerson.FirstPersonMode;
import dev.kosmx.playerAnim.api.layered.AnimationStack;
import dev.kosmx.playerAnim.api.layered.IAnimation;
import dev.kosmx.playerAnim.api.layered.KeyframeAnimationPlayer;
import dev.kosmx.playerAnim.api.layered.modifier.AbstractFadeModifier;
import dev.kosmx.playerAnim.api.layered.modifier.AbstractModifier;
import dev.kosmx.playerAnim.api.layered.modifier.AdjustmentModifier;
import dev.kosmx.playerAnim.core.data.KeyframeAnimation;
import dev.kosmx.playerAnim.core.util.Ease;
import dev.kosmx.playerAnim.core.util.Vec3f;
import dev.kosmx.playerAnim.impl.IAnimatedPlayer;
import net.bettercombat.BetterCombat;
import net.bettercombat.Platform;
import net.bettercombat.client.BetterCombatClient;
import net.bettercombat.client.animation.*;
import net.bettercombat.client.animation.modifier.HarshAdjustmentModifier;
import net.bettercombat.client.animation.modifier.TransmissionSpeedModifier;
import net.bettercombat.compatibility.CompatibilityFlags;
import net.bettercombat.logic.AnimatedHand;
import net.bettercombat.mixin.LivingEntityAccessor;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.util.Arm;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.Optional;

@Mixin({AbstractClientPlayerEntity.class})
public abstract class AbstractClientPlayerEntity_BetterCombatReplacementMixin extends PlayerEntity implements PlayerAttackAnimatable {
    @Unique
    private final AttackAnimationSubStack attackAnimation = new AttackAnimationSubStack(this.createAttackAdjustment());
    @Unique
    private final PoseSubStack mainHandBodyPose = new PoseSubStack(this.createPoseAdjustment(), true, true);
    @Unique
    private final PoseSubStack mainHandItemPose = new PoseSubStack((AbstractModifier)null, false, true);
    @Unique
    private final PoseSubStack offHandBodyPose = new PoseSubStack((AbstractModifier)null, true, false);
    @Unique
    private final PoseSubStack offHandItemPose = new PoseSubStack((AbstractModifier)null, false, true);

    public AbstractClientPlayerEntity_BetterCombatReplacementMixin(World world, BlockPos pos, float yaw, GameProfile gameProfile) {
        super(world, pos, yaw, gameProfile);
    }

    @Inject(
            method = {"<init>"},
            at = {@At("TAIL")}
    )
    private void postInit(ClientWorld world, GameProfile profile, CallbackInfo ci) {
        AnimationStack stack = ((IAnimatedPlayer)this).getAnimationStack();
        stack.addAnimLayer(1, this.offHandItemPose.base);
        stack.addAnimLayer(2, this.offHandBodyPose.base);
        stack.addAnimLayer(3, this.mainHandItemPose.base);
        stack.addAnimLayer(4, this.mainHandBodyPose.base);
        stack.addAnimLayer(2000, this.attackAnimation.base);
        this.mainHandBodyPose.configure = this::updateAnimationByCurrentActivity;
        this.offHandBodyPose.configure = this::updateAnimationByCurrentActivity;
    }

    public void updateAnimationsOnTick() {
        PlayerEntity player = (PlayerEntity)this;
        boolean isLeftHanded = this.isLeftHanded();
        boolean hasActiveAttackAnimation = this.attackAnimation.base.getAnimation() != null && this.attackAnimation.base.getAnimation().isActive();
        ItemStack mainHandStack = player.getMainHandStack();
        if (!player.handSwinging && !player.isSwimming()/* && !player.isUsingItem()*/ && !Platform.isCastingSpell(player) && !CrossbowItem.isCharged(mainHandStack)) {

            // TODO implement remaining pose types
            boolean isSneaking = this.isSneaking();
            boolean isWalking = this.isWalking();
            boolean isBlocking = this.isBlocking();
            boolean isSprinting = this.isSprinting();
            boolean isTwoHandedWielding = ((DuckPlayerEntityMixin) this).betteradventuremode$isOffHandStackSheathed() && !(((DuckPlayerEntityMixin) this).betteradventuremode$isMainHandStackSheathed());
            if (hasActiveAttackAnimation) {
                ((LivingEntityAccessor)player).invokeTurnHead(player.getHeadYaw(), 0.0F);
            }

            KeyframeAnimation newMainHandPose = null;
            KeyframeAnimation newOffHandPose = null;
            WeaponPoses mainHandWeaponPoses = WeaponPosesRegistry.getWeaponPoses(Registries.ITEM.getId(mainHandStack.getItem()));
            WeaponPoses offHandWeaponPoses = WeaponPosesRegistry.getWeaponPoses(Registries.ITEM.getId(player.getOffHandStack().getItem()));

            if (isTwoHandedWielding) {

                if (mainHandWeaponPoses != null && mainHandWeaponPoses.twoHandedPose() != null) {
                    newMainHandPose = (KeyframeAnimation) AnimationRegistry.animations.get(mainHandWeaponPoses.twoHandedPose());
                }
            } else {
                if (mainHandWeaponPoses != null) {
                    newMainHandPose = isBlocking && !isWalking && mainHandWeaponPoses.poseBlocking() != null ? (KeyframeAnimation) AnimationRegistry.animations.get(mainHandWeaponPoses.poseBlocking()) : mainHandWeaponPoses.pose() != null ? (KeyframeAnimation) AnimationRegistry.animations.get(mainHandWeaponPoses.pose()) : null;
                }
                if (offHandWeaponPoses != null) {
                    newOffHandPose = isBlocking && !isWalking && offHandWeaponPoses.offHandPoseBlocking() != null ? (KeyframeAnimation) AnimationRegistry.animations.get(offHandWeaponPoses.offHandPoseBlocking()) : offHandWeaponPoses.offHandPose() != null ? (KeyframeAnimation) AnimationRegistry.animations.get(offHandWeaponPoses.offHandPose()) : null;
                }
            }

            this.mainHandItemPose.setPose(newMainHandPose, isLeftHanded);
            this.offHandItemPose.setPose(newOffHandPose, isLeftHanded);
            this.mainHandBodyPose.setPose(newMainHandPose, isLeftHanded);
            this.offHandBodyPose.setPose(newOffHandPose, isLeftHanded);
        } else {
            this.mainHandBodyPose.setPose((KeyframeAnimation)null, isLeftHanded);
            this.mainHandItemPose.setPose((KeyframeAnimation)null, isLeftHanded);
            this.offHandBodyPose.setPose((KeyframeAnimation)null, isLeftHanded);
            this.offHandItemPose.setPose((KeyframeAnimation)null, isLeftHanded);
        }
    }

    public void playAttackAnimation(String name, AnimatedHand animatedHand, float length, float upswing) {
        try {
            KeyframeAnimation animation = (KeyframeAnimation)AnimationRegistry.animations.get(name);
            KeyframeAnimation.AnimationBuilder copy = animation.mutableCopy();
            this.updateAnimationByCurrentActivity(copy);
            copy.torso.fullyEnablePart(true);
            copy.head.pitch.setEnabled(false);
            float speed = (float)animation.endTick / length;
            boolean mirror = animatedHand.isOffHand();
            if (this.isLeftHanded()) {
                mirror = !mirror;
            }

            int fadeIn = copy.beginTick;
            float upswingSpeed = speed / BetterCombat.config.getUpswingMultiplier();
            float downwindSpeed = (float)((double)speed * MathHelper.lerp(Math.max((double)BetterCombat.config.getUpswingMultiplier() - 0.5, 0.0) / 0.5, (double)(1.0F - upswing), (double)(upswing / (1.0F - upswing))));
            this.attackAnimation.speed.set(upswingSpeed, List.of(new TransmissionSpeedModifier.Gear(length * upswing, downwindSpeed), new TransmissionSpeedModifier.Gear(length, speed)));
            this.attackAnimation.mirror.setEnabled(mirror);
            CustomAnimationPlayer player = new CustomAnimationPlayer(copy.build(), 0);
            player.setFirstPersonMode(CompatibilityFlags.firstPersonRender() ? FirstPersonMode.THIRD_PERSON_MODEL : FirstPersonMode.NONE);
            player.setFirstPersonConfiguration(this.firstPersonConfig(animatedHand));
            this.attackAnimation.base.replaceAnimationWithFade(AbstractFadeModifier.standardFadeIn(fadeIn, Ease.INOUTSINE), player);
        } catch (Exception var13) {
            var13.printStackTrace();
        }

    }

    @Unique
    private AdjustmentModifier createAttackAdjustment() {
        return new AdjustmentModifier((partName) -> {
            float rotationX = 0.0F;
            float rotationY = 0.0F;
            float rotationZ = 0.0F;
            float offsetX = 0.0F;
            float offsetY = 0.0F;
            float offsetZ = 0.0F;
            float pitch;
            if (FirstPersonMode.isFirstPersonPass()) {
                pitch = this.getPitch();
                pitch = (float)Math.toRadians((double)pitch);
                switch (partName) {
                    case "body":
                        rotationX -= pitch;
                        if (pitch < 0.0F) {
                            double offset = Math.abs(Math.sin((double)pitch));
                            offsetY = (float)((double)offsetY + offset * 0.5);
                            offsetZ = (float)((double)offsetZ - offset);
                        }
                        break;
                    default:
                        return Optional.empty();
                }
            } else {
                pitch = this.getPitch();
                pitch = (float)Math.toRadians((double)pitch);
                switch (partName) {
                    case "body":
                        rotationX -= pitch * 0.75F;
                        break;
                    case "rightArm":
                    case "leftArm":
                        rotationX += pitch * 0.25F;
                        break;
                    case "rightLeg":
                    case "leftLeg":
                        rotationX = (float)((double)rotationX - (double)pitch * 0.75);
                        break;
                    default:
                        return Optional.empty();
                }
            }

            return Optional.of(new AdjustmentModifier.PartModifier(new Vec3f(rotationX, rotationY, rotationZ), new Vec3f(offsetX, offsetY, offsetZ)));
        });
    }

    @Unique
    private AdjustmentModifier createPoseAdjustment() {
        return new HarshAdjustmentModifier((partName) -> {
            float rotationX = 0.0F;
            float rotationY = 0.0F;
            float rotationZ = 0.0F;
            float offsetX = 0.0F;
            float offsetY = 0.0F;
            float offsetZ = 0.0F;
            if (!FirstPersonMode.isFirstPersonPass()) {
                switch (partName) {
                    case "rightArm":
                    case "leftArm":
                        if (!this.mainHandItemPose.lastAnimationUsesBodyChannel && this.isSneaking()) {
                            offsetY += 3.0F;
                        }
                        break;
                    default:
                        return Optional.empty();
                }
            }

            return Optional.of(new AdjustmentModifier.PartModifier(new Vec3f(rotationX, rotationY, rotationZ), new Vec3f(offsetX, offsetY, offsetZ)));
        });
    }

    @Unique
    private void updateAnimationByCurrentActivity(KeyframeAnimation.AnimationBuilder animation) {
        EntityPose pose = this.getPose();
        switch (pose) {
            case SWIMMING:
                StateCollectionHelper.configure(animation.rightLeg, false, false);
                StateCollectionHelper.configure(animation.leftLeg, false, false);
            case STANDING:
            case FALL_FLYING:
            case SLEEPING:
            case SPIN_ATTACK:
            case CROUCHING:
            case LONG_JUMPING:
            case DYING:
            default:
                if (this.isMounting()) {
                    StateCollectionHelper.configure(animation.rightLeg, false, false);
                    StateCollectionHelper.configure(animation.leftLeg, false, false);
                }

        }
    }

    @Unique
    private boolean isWalking() {
        return !this.isDead() && (this.isSwimming() || this.getVelocity().horizontalLength() > 0.03);
    }

    @Unique
    private boolean isMounting() {
        return this.getVehicle() != null;
    }

    @Unique
    public boolean isLeftHanded() {
        return this.getMainArm() == Arm.LEFT;
    }

    public void stopAttackAnimation(float length) {
        IAnimation currentAnimation = this.attackAnimation.base.getAnimation();
        if (currentAnimation != null && currentAnimation instanceof KeyframeAnimationPlayer) {
            int fadeOut = Math.round(length);
            this.attackAnimation.adjustmentModifier.fadeOut(fadeOut);
            this.attackAnimation.base.replaceAnimationWithFade(AbstractFadeModifier.standardFadeIn(fadeOut, Ease.INOUTSINE), (IAnimation)null);
        }

    }

    @Unique
    private FirstPersonConfiguration firstPersonConfig(AnimatedHand animatedHand) {
        boolean showRightItem = true;
        boolean showLeftItem = BetterCombatClient.config.isShowingOtherHandFirstPerson || animatedHand == AnimatedHand.TWO_HANDED;
        boolean showRightArm = showRightItem && BetterCombatClient.config.isShowingArmsInFirstPerson;
        boolean showLeftArm = showLeftItem && BetterCombatClient.config.isShowingArmsInFirstPerson;
        FirstPersonConfiguration config = new FirstPersonConfiguration(showRightArm, showLeftArm, showRightItem, showLeftItem);
        return config;
    }
}
