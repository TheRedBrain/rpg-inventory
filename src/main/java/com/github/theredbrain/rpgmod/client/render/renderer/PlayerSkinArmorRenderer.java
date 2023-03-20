package com.github.theredbrain.rpgmod.client.render.renderer;

import com.github.theredbrain.rpgmod.client.render.model.PlayerSkinArmorModel;
import com.github.theredbrain.rpgmod.entity.ExtendedEquipmentSlot;
import com.github.theredbrain.rpgmod.geckolib.DuckDefaultedGeoModelMixin;
import com.github.theredbrain.rpgmod.item.PlayerSkinArmorItem;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.PlayerModelPart;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoArmorRenderer;
import software.bernie.geckolib.util.RenderUtils;

import javax.annotation.Nullable;

public class PlayerSkinArmorRenderer extends GeoArmorRenderer<PlayerSkinArmorItem> {

    protected GeoBone cape = null;
    protected GeoBone jacket = null;
    protected GeoBone leftSleeve = null;
    protected GeoBone rightSleeve = null;
    protected GeoBone leftPantsLeg = null;
    protected GeoBone rightPantsLeg = null;
    protected GeoBone hat = null;

    public PlayerSkinArmorRenderer(Identifier assetSubpath, Identifier playerSkinTexture) {
        super(((DuckDefaultedGeoModelMixin<PlayerSkinArmorItem>)new PlayerSkinArmorModel(assetSubpath)).withCustomTexture(playerSkinTexture));
    }

    /**
     * Gets the {@link RenderLayer} to render the given animatable with.<br>
     * Uses the {@link RenderLayer#getArmorCutoutNoCull} {@code RenderType} by default.<br>
     * Override this to change the way a model will render (such as translucent models, etc)
     */
    @Override
    public RenderLayer getRenderType(PlayerSkinArmorItem animatable, Identifier texture, @org.jetbrains.annotations.Nullable VertexConsumerProvider bufferSource, float partialTick) {
        return RenderLayer.getEntityTranslucent(texture);
    }

    @Nullable
    public GeoBone getCapeBone() {
        return this.model.getBone("cape").orElse(null);
    }

    /**
     * Returns the 'head' GeoBone from this model.<br>
     * Override if your geo model has different bone names for these bones
     * @return The bone for the head model piece, or null if not using it
     */
    @Nullable
    @Override
    public GeoBone getHeadBone() {
        return this.model.getBone("inner_head").orElse(null);
    }

    @Nullable
    public GeoBone getHatBone() {
        return this.model.getBone("outer_head").orElse(null);
    }

    /**
     * Returns the 'body' GeoBone from this model.<br>
     * Override if your geo model has different bone names for these bones
     * @return The bone for the body model piece, or null if not using it
     */
    @Nullable
    @Override
    public GeoBone getBodyBone() {
        return this.model.getBone("inner_body").orElse(null);
    }

    /**
     * Returns the 'body' GeoBone from this model.<br>
     * Override if your geo model has different bone names for these bones
     * @return The bone for the body model piece, or null if not using it
     */
    @Nullable
    public GeoBone getJacketBone() {
        return this.model.getBone("outer_body").orElse(null);
    }

    /**
     * Returns the 'right arm' GeoBone from this model.<br>
     * Override if your geo model has different bone names for these bones
     * @return The bone for the right arm model piece, or null if not using it
     */
    @Nullable
    @Override
    public GeoBone getRightArmBone() {
        return this.model.getBone("inner_right_arm").orElse(null);
    }

    /**
     * Returns the 'left arm' GeoBone from this model.<br>
     * Override if your geo model has different bone names for these bones
     * @return The bone for the left arm model piece, or null if not using it
     */
    @Nullable
    @Override
    public GeoBone getLeftArmBone() {
        return this.model.getBone("inner_left_arm").orElse(null);
    }

    /**
     * Returns the 'right arm' GeoBone from this model.<br>
     * Override if your geo model has different bone names for these bones
     * @return The bone for the right arm model piece, or null if not using it
     */
    @Nullable
    public GeoBone getRightSleeveBone() {
        return this.model.getBone("outer_right_arm").orElse(null);
    }

    /**
     * Returns the 'left arm' GeoBone from this model.<br>
     * Override if your geo model has different bone names for these bones
     * @return The bone for the left arm model piece, or null if not using it
     */
    @Nullable
    public GeoBone getLeftSleeveBone() {
        return this.model.getBone("outer_left_arm").orElse(null);
    }

    /**
     * Returns the 'right leg' GeoBone from this model.<br>
     * Override if your geo model has different bone names for these bones
     * @return The bone for the right leg model piece, or null if not using it
     */
    @Nullable
    @Override
    public GeoBone getRightLegBone() {
        return this.model.getBone("inner_right_leg").orElse(null);
    }

    /**
     * Returns the 'left leg' GeoBone from this model.<br>
     * Override if your geo model has different bone names for these bones
     * @return The bone for the left leg model piece, or null if not using it
     */
    @Nullable
    @Override
    public GeoBone getLeftLegBone() {
        return this.model.getBone("inner_left_leg").orElse(null);
    }

    /**
     * Returns the 'right boot' GeoBone from this model.<br>
     * Override if your geo model has different bone names for these bones
     * @return The bone for the right boot model piece, or null if not using it
     */
    @Nullable
    public GeoBone getRightPantsLegBone() {
        return this.model.getBone("outer_right_leg").orElse(null);
    }

    /**
     * Returns the 'left boot' GeoBone from this model.<br>
     * Override if your geo model has different bone names for these bones
     * @return The bone for the left boot model piece, or null if not using it
     */
    @Nullable
    public GeoBone getLeftPantsLegBone() {
        return this.model.getBone("outer_left_leg").orElse(null);
    }

    /**
     * Gets and caches the relevant armor model bones for this baked model if it hasn't been done already
     */
    @Override
    protected void grabRelevantBones(BakedGeoModel bakedModel) {
        if (this.lastModel == bakedModel)
            return;

        this.lastModel = bakedModel;
        this.head = getHeadBone();
        this.hat = getHatBone();
        this.body = getBodyBone();
        this.jacket = getJacketBone();
        this.rightArm = getRightArmBone();
        this.rightSleeve = getRightSleeveBone();
        this.leftArm = getLeftArmBone();
        this.leftSleeve = getLeftSleeveBone();
        this.rightLeg = getRightLegBone();
        this.rightPantsLeg = getRightPantsLegBone();
        this.leftLeg = getLeftLegBone();
        this.leftPantsLeg = getLeftPantsLegBone();
        this.cape = getCapeBone();
    }

    /**
     * Resets the bone visibility for the model based on the currently rendering slot,
     * and then sets bones relevant to the current slot as visible for rendering.<br>
     * <br>
     * This is only called by default for non-geo entities (I.E. players or vanilla mobs)
     */
    @Override
    protected void applyBoneVisibilityBySlot(EquipmentSlot currentSlot) {
        setVisible(false);

        if (!(this.currentEntity instanceof PlayerEntity)) return;

        if (currentSlot == ExtendedEquipmentSlot.PLAYER_SKIN_ARMOR || currentSlot == EquipmentSlot.CHEST) {
            setBoneVisible(this.head, true);
            setBoneVisible(this.hat, ((PlayerEntity)this.currentEntity).isPartVisible(PlayerModelPart.HAT));
            setBoneVisible(this.body, true);
            setBoneVisible(this.jacket, ((PlayerEntity)this.currentEntity).isPartVisible(PlayerModelPart.JACKET));
            setBoneVisible(this.rightArm, true);
            setBoneVisible(this.rightSleeve, ((PlayerEntity)this.currentEntity).isPartVisible(PlayerModelPart.RIGHT_SLEEVE));
            setBoneVisible(this.leftArm, true);
            setBoneVisible(this.leftSleeve, ((PlayerEntity)this.currentEntity).isPartVisible(PlayerModelPart.LEFT_SLEEVE));
            setBoneVisible(this.rightLeg, true);
            setBoneVisible(this.rightPantsLeg, ((PlayerEntity)this.currentEntity).isPartVisible(PlayerModelPart.LEFT_PANTS_LEG));
            setBoneVisible(this.leftLeg, true);
            setBoneVisible(this.leftPantsLeg, ((PlayerEntity)this.currentEntity).isPartVisible(PlayerModelPart.LEFT_PANTS_LEG));
            setBoneVisible(this.cape, ((PlayerEntity)this.currentEntity).isPartVisible(PlayerModelPart.CAPE));
        }
    }

    /**
     * Resets the bone visibility for the model based on the current {@link ModelPart} and {@link EquipmentSlot},
     * and then sets the bones relevant to the current part as visible for rendering.<br>
     * <br>
     * If you are rendering a geo entity with armor, you should probably be calling this prior to rendering
     */
    @Override
    public void applyBoneVisibilityByPart(EquipmentSlot currentSlot, ModelPart currentPart, BipedEntityModel<?> model) {
        setVisible(false);

        currentPart.visible = true;
        GeoBone bone = null;

        if (currentPart == model.hat) {
            bone = this.head;
        }
        else if (currentPart == model.body) {
            bone = this.body;
        }
        else if (currentPart == model.leftArm) {
            bone = this.leftArm;
        }
        else if (currentPart == model.rightArm) {
            bone = this.rightArm;
        }
        else if (currentPart == model.leftLeg) {
            bone = this.leftLeg;
        }
        else if (currentPart == model.rightLeg) {
            bone = this.rightLeg;
        }

        if (bone != null)
            bone.setHidden(false);
    }

    /**
     * Transform the currently rendering {@link GeoModel} to match the positions and rotations of the base model
     */
    @Override
    protected void applyBaseTransformations(BipedEntityModel<?> baseModel) {
        if (this.head != null) {
            ModelPart headPart = baseModel.head;

            RenderUtils.matchModelPartRot(headPart, this.head);
            this.head.updatePosition(headPart.pivotX, -headPart.pivotY, headPart.pivotZ);

            if (this.hat != null) {
                RenderUtils.matchModelPartRot(headPart, this.hat);
                this.hat.updatePosition(headPart.pivotX, -headPart.pivotY, headPart.pivotZ);
            }
        }

        if (this.body != null) {
            ModelPart bodyPart = baseModel.body;

            RenderUtils.matchModelPartRot(bodyPart, this.body);
            this.body.updatePosition(bodyPart.pivotX, -bodyPart.pivotY, bodyPart.pivotZ);

            if (this.jacket != null) {
                RenderUtils.matchModelPartRot(bodyPart, this.jacket);
                this.jacket.updatePosition(bodyPart.pivotX, -bodyPart.pivotY, bodyPart.pivotZ);
            }

            if (this.cape != null) {
                RenderUtils.matchModelPartRot(bodyPart, this.cape);
                this.cape.updatePosition(bodyPart.pivotX, -bodyPart.pivotY, bodyPart.pivotZ);
            }
        }

        if (this.rightArm != null) {
            ModelPart rightArmPart = baseModel.rightArm;

            RenderUtils.matchModelPartRot(rightArmPart, this.rightArm);
            this.rightArm.updatePosition(rightArmPart.pivotX + 5, 2 - rightArmPart.pivotY, rightArmPart.pivotZ);

            if (this.rightSleeve != null) {
                RenderUtils.matchModelPartRot(rightArmPart, this.rightSleeve);
                this.rightSleeve.updatePosition(rightArmPart.pivotX + 5, 2 - rightArmPart.pivotY, rightArmPart.pivotZ);
            }
        }

        if (this.leftArm != null) {
            ModelPart leftArmPart = baseModel.leftArm;

            RenderUtils.matchModelPartRot(leftArmPart, this.leftArm);
            this.leftArm.updatePosition(leftArmPart.pivotX - 5f, 2f - leftArmPart.pivotY, leftArmPart.pivotZ);

            if (this.leftSleeve != null) {
                RenderUtils.matchModelPartRot(leftArmPart, this.leftSleeve);
                this.leftSleeve.updatePosition(leftArmPart.pivotX - 5f, 2f - leftArmPart.pivotY, leftArmPart.pivotZ);
            }
        }

        if (this.rightLeg != null) {
            ModelPart rightLegPart = baseModel.rightLeg;

            RenderUtils.matchModelPartRot(rightLegPart, this.rightLeg);
            this.rightLeg.updatePosition(rightLegPart.pivotX + 2, 12 - rightLegPart.pivotY, rightLegPart.pivotZ);

            if (this.rightPantsLeg != null) {
                RenderUtils.matchModelPartRot(rightLegPart, this.rightPantsLeg);
                this.rightPantsLeg.updatePosition(rightLegPart.pivotX + 2, 12 - rightLegPart.pivotY, rightLegPart.pivotZ);
            }
        }

        if (this.leftLeg != null) {
            ModelPart leftLegPart = baseModel.leftLeg;

            RenderUtils.matchModelPartRot(leftLegPart, this.leftLeg);
            this.leftLeg.updatePosition(leftLegPart.pivotX - 2, 12 - leftLegPart.pivotY, leftLegPart.pivotZ);

            if (this.leftPantsLeg != null) {
                RenderUtils.matchModelPartRot(leftLegPart, this.leftPantsLeg);
                this.leftPantsLeg.updatePosition(leftLegPart.pivotX - 2, 12 - leftLegPart.pivotY, leftLegPart.pivotZ);
            }
        }
    }

    @Override
    public void setVisible(boolean pVisible) {
        super.setVisible(pVisible);

        setBoneVisible(this.head, pVisible);
        setBoneVisible(this.hat, pVisible);
        setBoneVisible(this.body, pVisible);
        setBoneVisible(this.jacket, pVisible);
        setBoneVisible(this.rightArm, pVisible);
        setBoneVisible(this.leftArm, pVisible);
        setBoneVisible(this.rightSleeve, pVisible);
        setBoneVisible(this.leftSleeve, pVisible);
        setBoneVisible(this.rightLeg, pVisible);
        setBoneVisible(this.leftLeg, pVisible);
        setBoneVisible(this.rightPantsLeg, pVisible);
        setBoneVisible(this.leftPantsLeg, pVisible);
        setBoneVisible(this.cape, pVisible);
    }
}
