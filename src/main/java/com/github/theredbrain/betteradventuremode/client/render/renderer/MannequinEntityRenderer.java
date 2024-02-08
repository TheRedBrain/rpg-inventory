package com.github.theredbrain.betteradventuremode.client.render.renderer;

import com.github.theredbrain.betteradventuremode.client.render.entity.MannequinModelPart;
import com.github.theredbrain.betteradventuremode.client.render.entity.feature.TrinketFeatureRenderer;
import com.github.theredbrain.betteradventuremode.client.render.model.MannequinEntityModel;
import com.github.theredbrain.betteradventuremode.entity.decoration.MannequinEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.feature.*;
import net.minecraft.client.render.entity.model.ArmorEntityModel;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Arm;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.UseAction;
import net.minecraft.util.math.Vec3d;

@Environment(EnvType.CLIENT)
public class MannequinEntityRenderer extends LivingEntityRenderer<MannequinEntity, PlayerEntityModel<MannequinEntity>> {
    public MannequinEntityRenderer(EntityRendererFactory.Context ctx) {
        super(ctx, new MannequinEntityModel(ctx.getPart(EntityModelLayers.PLAYER), false), 0.0F);
        this.addFeature(new ArmorFeatureRenderer<>(this, new ArmorEntityModel<>(ctx.getPart(EntityModelLayers.PLAYER_INNER_ARMOR)), new ArmorEntityModel<>(ctx.getPart(EntityModelLayers.PLAYER_OUTER_ARMOR)), ctx.getModelManager()));
        this.addFeature(new HeldItemFeatureRenderer<>(this, ctx.getHeldItemRenderer()));
        this.addFeature(new HeadFeatureRenderer<>(this, ctx.getModelLoader(), ctx.getHeldItemRenderer()));
        this.addFeature(new TrinketFeatureRenderer<>(this, this.model));
        this.addFeature(new SheathedMainHandItemFeatureRenderer<>(this, ctx.getHeldItemRenderer()));
        this.addFeature(new SheathedOffHandItemFeatureRenderer<>(this, ctx.getHeldItemRenderer()));
    }

    public void render(MannequinEntity mannequinEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
        this.setModelPose(mannequinEntity);
        mannequinEntity.handSwingProgress = 0.0F;
        super.render(mannequinEntity, f, g, matrixStack, vertexConsumerProvider, i);
    }

    public Vec3d getPositionOffset(MannequinEntity mannequinEntity, float f) {
        return mannequinEntity.isInSneakingPose() ? new Vec3d(0.0, -0.125, 0.0) : super.getPositionOffset(mannequinEntity, f);
    }

    private void setModelPose(MannequinEntity mannequinEntity) {
        MannequinEntityModel playerEntityModel = (MannequinEntityModel) this.getModel();
        playerEntityModel.setVisible(true);
        playerEntityModel.head.visible = mannequinEntity.isModelPartVisible(MannequinModelPart.HEAD);
        playerEntityModel.hat.visible = mannequinEntity.isModelPartVisible(MannequinModelPart.HAT);
        playerEntityModel.body.visible = mannequinEntity.isModelPartVisible(MannequinModelPart.BODY);
        playerEntityModel.jacket.visible = mannequinEntity.isModelPartVisible(MannequinModelPart.JACKET);
        playerEntityModel.leftLeg.visible = mannequinEntity.isModelPartVisible(MannequinModelPart.LEFT_LEG);
        playerEntityModel.leftPants.visible = mannequinEntity.isModelPartVisible(MannequinModelPart.LEFT_PANTS);
        playerEntityModel.rightLeg.visible = mannequinEntity.isModelPartVisible(MannequinModelPart.RIGHT_LEG);
        playerEntityModel.rightPants.visible = mannequinEntity.isModelPartVisible(MannequinModelPart.RIGHT_PANTS);
        playerEntityModel.leftArm.visible = mannequinEntity.isModelPartVisible(MannequinModelPart.LEFT_ARM);
        playerEntityModel.leftSleeve.visible = mannequinEntity.isModelPartVisible(MannequinModelPart.LEFT_SLEEVE);
        playerEntityModel.rightArm.visible = mannequinEntity.isModelPartVisible(MannequinModelPart.RIGHT_ARM);
        playerEntityModel.rightSleeve.visible = mannequinEntity.isModelPartVisible(MannequinModelPart.RIGHT_SLEEVE);
        playerEntityModel.sneaking = mannequinEntity.isInSneakingPose();
        BipedEntityModel.ArmPose armPose = getArmPose(mannequinEntity, Hand.MAIN_HAND);
        BipedEntityModel.ArmPose armPose2 = getArmPose(mannequinEntity, Hand.OFF_HAND);
        if (armPose.isTwoHanded()) {
            armPose2 = mannequinEntity.getOffHandStack().isEmpty() ? BipedEntityModel.ArmPose.EMPTY : BipedEntityModel.ArmPose.ITEM;
        }

        if (mannequinEntity.getMainArm() == Arm.RIGHT) {
            playerEntityModel.rightArmPose = armPose;
            playerEntityModel.leftArmPose = armPose2;
        } else {
            playerEntityModel.rightArmPose = armPose2;
            playerEntityModel.leftArmPose = armPose;
        }
    }

    private static BipedEntityModel.ArmPose getArmPose(MannequinEntity mannequinEntity, Hand hand) {
        ItemStack itemStack = mannequinEntity.getStackInHand(hand);
        if (itemStack.isEmpty()) {
            return BipedEntityModel.ArmPose.EMPTY;
        } else {
            if (mannequinEntity.getActiveHand() == hand && mannequinEntity.isUsingItem()) {
                UseAction useAction = itemStack.getUseAction();
                if (useAction == UseAction.BLOCK) {
                    return BipedEntityModel.ArmPose.BLOCK;
                }

                if (useAction == UseAction.BOW) {
                    return BipedEntityModel.ArmPose.BOW_AND_ARROW;
                }

                if (useAction == UseAction.SPEAR) {
                    return BipedEntityModel.ArmPose.THROW_SPEAR;
                }

                if (useAction == UseAction.CROSSBOW && hand == mannequinEntity.getActiveHand()) {
                    return BipedEntityModel.ArmPose.CROSSBOW_CHARGE;
                }

                if (useAction == UseAction.SPYGLASS) {
                    return BipedEntityModel.ArmPose.SPYGLASS;
                }

                if (useAction == UseAction.TOOT_HORN) {
                    return BipedEntityModel.ArmPose.TOOT_HORN;
                }

                if (useAction == UseAction.BRUSH) {
                    return BipedEntityModel.ArmPose.BRUSH;
                }
            } else if (!mannequinEntity.handSwinging && ((itemStack.isOf(Items.CROSSBOW) && CrossbowItem.isCharged(itemStack)) || mannequinEntity.getSheathedWeaponMode() == MannequinEntity.SheathedWeaponMode.OFF_HAND)) {
                return BipedEntityModel.ArmPose.CROSSBOW_HOLD;
            }

            return BipedEntityModel.ArmPose.ITEM;
        }
    }

    @Override
    public Identifier getTexture(MannequinEntity mannequinEntity) {
        return mannequinEntity.getTextureIdentifier();
    }

    protected void scale(MannequinEntity mannequinEntity, MatrixStack matrixStack, float f) {
        float g = 0.9375F;
        matrixStack.scale(0.9375F, 0.9375F, 0.9375F);
    }

    @Override
    protected boolean hasLabel(MannequinEntity entity) {
        return entity.shouldRenderName() && entity.hasCustomName();
    }

//    @Override
//    protected void renderLabelIfPresent(MannequinEntity mannequinEntity, Text text, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
//        double d = this.dispatcher.getSquaredDistanceToCamera(mannequinEntity);
//        matrixStack.push();
//        if (d < 100.0) {
//            Scoreboard scoreboard = mannequinEntity.getScoreboard();
//            ScoreboardObjective scoreboardObjective = scoreboard.getObjectiveForSlot(ScoreboardDisplaySlot.BELOW_NAME);
//            if (scoreboardObjective != null) {
//                ReadableScoreboardScore readableScoreboardScore = scoreboard.getScore(mannequinEntity, scoreboardObjective);
//                Text text2 = ReadableScoreboardScore.getFormattedScore(readableScoreboardScore, scoreboardObjective.getNumberFormatOr(StyledNumberFormat.EMPTY));
//                super.renderLabelIfPresent(mannequinEntity, Text.empty().append((Text)text2).append(ScreenTexts.SPACE).append(scoreboardObjective.getDisplayName()), matrixStack, vertexConsumerProvider, i);
//                Objects.requireNonNull(this.getTextRenderer());
//                matrixStack.translate(0.0F, 9.0F * 1.15F * 0.025F, 0.0F);
//            }
//        }
//
//        super.renderLabelIfPresent(mannequinEntity, text, matrixStack, vertexConsumerProvider, i);
//        matrixStack.pop();
//    }

//    public void renderRightArm(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, MannequinEntity mannequinEntity) {
//        this.renderArm(matrices, vertexConsumers, light, mannequinEntity, this.model.rightArm, this.model.rightSleeve);
//    }
//
//    public void renderLeftArm(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, MannequinEntity mannequinEntity) {
//        this.renderArm(matrices, vertexConsumers, light, mannequinEntity, this.model.leftArm, this.model.leftSleeve);
//    }

//    private void renderArm(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, MannequinEntity mannequinEntity, ModelPart arm, ModelPart sleeve) {
//        PlayerEntityModel<MannequinEntity> playerEntityModel = this.getModel();
//        this.setModelPose(mannequinEntity);
//        playerEntityModel.handSwingProgress = 0.0F;
//        playerEntityModel.sneaking = false;
//        playerEntityModel.leaningPitch = 0.0F;
//        playerEntityModel.setAngles(mannequinEntity, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
//        arm.pitch = 0.0F;
//        Identifier identifier = mannequinEntity.getTextureIdentifier();
//        arm.render(matrices, vertexConsumers.getBuffer(RenderLayer.getEntitySolid(identifier)), light, OverlayTexture.DEFAULT_UV);
//        sleeve.pitch = 0.0F;
//        sleeve.render(matrices, vertexConsumers.getBuffer(RenderLayer.getEntityTranslucent(identifier)), light, OverlayTexture.DEFAULT_UV);
//    }

//    protected void setupTransforms(MannequinEntity armorStandEntity, MatrixStack matrixStack, float f, float g, float h) {
//        matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(180.0F - g));
//        float i = (float)(armorStandEntity.getWorld().getTime() - armorStandEntity.lastHitTime) + h;
//        if (i < 5.0F) {
//            matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(MathHelper.sin(i / 1.5F * 3.1415927F) * 3.0F));
//        }

//    }

//    protected void setupTransforms(MannequinEntity mannequinEntity, MatrixStack matrixStack, float f, float g, float h) {
//        float i = mannequinEntity.getLeaningPitch(h);
//        float j = mannequinEntity.getPitch(h);
//        float k;
//        float l;
//        if (mannequinEntity.isFallFlying()) {
//            super.setupTransforms(mannequinEntity, matrixStack, f, g, h);
//            k = (float)mannequinEntity.getRoll() + h;
//            l = MathHelper.clamp(k * k / 100.0F, 0.0F, 1.0F);
//            if (!mannequinEntity.isUsingRiptide()) {
//                matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(l * (-90.0F - j)));
//            }
//
//            Vec3d vec3d = mannequinEntity.getRotationVec(h);
//            Vec3d vec3d2 = mannequinEntity.lerpVelocity(h);
//            double d = vec3d2.horizontalLengthSquared();
//            double e = vec3d.horizontalLengthSquared();
//            if (d > 0.0 && e > 0.0) {
//                double m = (vec3d2.x * vec3d.x + vec3d2.z * vec3d.z) / Math.sqrt(d * e);
//                double n = vec3d2.x * vec3d.z - vec3d2.z * vec3d.x;
//                matrixStack.multiply(RotationAxis.POSITIVE_Y.rotation((float)(Math.signum(n) * Math.acos(m))));
//            }
//        } else if (i > 0.0F) {
//            super.setupTransforms(mannequinEntity, matrixStack, f, g, h);
//            k = mannequinEntity.isTouchingWater() ? -90.0F - j : -90.0F;
//            l = MathHelper.lerp(i, 0.0F, k);
//            matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(l));
//            if (mannequinEntity.isInSwimmingPose()) {
//                matrixStack.translate(0.0F, -1.0F, 0.3F);
//            }
//        } else {
//            super.setupTransforms(mannequinEntity, matrixStack, f, g, h);
//        }
//
//    }
}
