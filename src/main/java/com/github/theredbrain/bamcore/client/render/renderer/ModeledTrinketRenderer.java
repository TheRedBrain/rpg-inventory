package com.github.theredbrain.bamcore.client.render.renderer;
//
//import com.github.theredbrain.bamcore.api.item.ArmorTrinketItem;
//import com.github.theredbrain.bamcore.api.item.ModeledTrinketItem;
//import com.github.theredbrain.bamcore.client.render.model.ModeledTrinketModel;
//import mod.azure.azurelibarmor.cache.object.BakedGeoModel;
//import mod.azure.azurelibarmor.cache.object.GeoBone;
//import mod.azure.azurelibarmor.model.GeoModel;
//import mod.azure.azurelibarmor.renderer.GeoArmorRenderer;
//import mod.azure.azurelibarmor.util.RenderUtils;
//import net.minecraft.client.model.ModelPart;
//import net.minecraft.client.render.entity.model.BipedEntityModel;
//import net.minecraft.entity.EquipmentSlot;
//import net.minecraft.util.Identifier;
//import org.jetbrains.annotations.Nullable;
//
//public class ModeledTrinketRenderer extends GeoArmorRenderer<ModeledTrinketItem> {
//
//    protected GeoBone bodyLeggings = null;
//    protected GeoBone bodyWaist = null;
//    protected GeoBone bodyNeck = null;
//    protected GeoBone rightHand = null;
//    protected GeoBone rightElbow = null;
//    protected GeoBone leftHand = null;
//    protected GeoBone leftElbow = null;
//
//    public ModeledTrinketRenderer(Identifier assetSubpath) {
//        super(new ModeledTrinketModel(assetSubpath));
//    }
//
//    /**
//     * Returns the 'head' GeoBone from this model.<br>
//     * Override if your geo model has different bone names for these bones
//     * @return The bone for the head model piece, or null if not using it
//     */
//    @Nullable
//    @Override
//    public GeoBone getHeadBone() {
//        return this.model.getBone("armorHead").orElse(null);
//    }
//
//    /**
//     * Returns the 'body' GeoBone from this model.<br>
//     * Override if your geo model has different bone names for these bones
//     * @return The bone for the body model piece, or null if not using it
//     */
//    @Nullable
//    @Override
//    public GeoBone getBodyBone() {
//        return this.model.getBone("armorBody").orElse(null);
//    }
//
//    /**
//     * Returns the 'body' GeoBone from this model.<br>
//     * Override if your geo model has different bone names for these bones
//     * @return The bone for the body model piece, or null if not using it
//     */
//    @Nullable
//    public GeoBone getBodyNeckBone() {
//        return this.model.getBone("armorBodyNeck").orElse(null);
//    }
//
//    /**
//     * Returns the 'body' GeoBone from this model.<br>
//     * Override if your geo model has different bone names for these bones
//     * @return The bone for the body model piece, or null if not using it
//     */
//    @Nullable
//    public GeoBone getBodyWaistBone() {
//        return this.model.getBone("armorBodyWaist").orElse(null);
//    }
//
//    /**
//     * Returns the 'body' GeoBone from this model.<br>
//     * Override if your geo model has different bone names for these bones
//     * @return The bone for the body model piece, or null if not using it
//     */
//    @Nullable
//    public GeoBone getBodyLeggingsBone() {
//        return this.model.getBone("armorBodyLeggings").orElse(null);
//    }
//
//    /**
//     * Returns the 'right arm' GeoBone from this model.<br>
//     * Override if your geo model has different bone names for these bones
//     * @return The bone for the right arm model piece, or null if not using it
//     */
//    @Nullable
//    @Override
//    public GeoBone getRightArmBone() {
//        return this.model.getBone("armorRightShoulder").orElse(null);
//    }
//
//    /**
//     * Returns the 'left arm' GeoBone from this model.<br>
//     * Override if your geo model has different bone names for these bones
//     * @return The bone for the left arm model piece, or null if not using it
//     */
//    @Nullable
//    @Override
//    public GeoBone getLeftArmBone() {
//        return this.model.getBone("armorLeftShoulder").orElse(null);
//    }
//
//    /**
//     * Returns the 'right arm' GeoBone from this model.<br>
//     * Override if your geo model has different bone names for these bones
//     * @return The bone for the right arm model piece, or null if not using it
//     */
//    @Nullable
//    public GeoBone getRightHandBone() {
//        return this.model.getBone("armorRightHand").orElse(null);
//    }
//
//    /**
//     * Returns the 'left arm' GeoBone from this model.<br>
//     * Override if your geo model has different bone names for these bones
//     * @return The bone for the left arm model piece, or null if not using it
//     */
//    @Nullable
//    public GeoBone getLeftHandBone() {
//        return this.model.getBone("armorLeftHand").orElse(null);
//    }
//
//    /**
//     * Returns the 'right arm' GeoBone from this model.<br>
//     * Override if your geo model has different bone names for these bones
//     * @return The bone for the right arm model piece, or null if not using it
//     */
//    @Nullable
//    public GeoBone getRightElbowBone() {
//        return this.model.getBone("armorRightElbow").orElse(null);
//    }
//
//    /**
//     * Returns the 'left arm' GeoBone from this model.<br>
//     * Override if your geo model has different bone names for these bones
//     * @return The bone for the left arm model piece, or null if not using it
//     */
//    @Nullable
//    public GeoBone getLeftElbowBone() {
//        return this.model.getBone("armorLeftElbow").orElse(null);
//    }
//
//    /**
//     * Returns the 'right leg' GeoBone from this model.<br>
//     * Override if your geo model has different bone names for these bones
//     * @return The bone for the right leg model piece, or null if not using it
//     */
//    @Nullable
//    @Override
//    public GeoBone getRightLegBone() {
//        return this.model.getBone("armorRightLeg").orElse(null);
//    }
//
//    /**
//     * Returns the 'left leg' GeoBone from this model.<br>
//     * Override if your geo model has different bone names for these bones
//     * @return The bone for the left leg model piece, or null if not using it
//     */
//    @Nullable
//    @Override
//    public GeoBone getLeftLegBone() {
//        return this.model.getBone("armorLeftLeg").orElse(null);
//    }
//
//    /**
//     * Returns the 'right boot' GeoBone from this model.<br>
//     * Override if your geo model has different bone names for these bones
//     * @return The bone for the right boot model piece, or null if not using it
//     */
//    @Nullable
//    @Override
//    public GeoBone getRightBootBone() {
//        return this.model.getBone("armorRightBoot").orElse(null);
//    }
//
//    /**
//     * Returns the 'left boot' GeoBone from this model.<br>
//     * Override if your geo model has different bone names for these bones
//     * @return The bone for the left boot model piece, or null if not using it
//     */
//    @Nullable
//    @Override
//    public GeoBone getLeftBootBone() {
//        return this.model.getBone("armorLeftBoot").orElse(null);
//    }
//
//    /**
//     * Gets and caches the relevant armor model bones for this baked model if it hasn't been done already
//     */
//    @Override
//    protected void grabRelevantBones(BakedGeoModel bakedModel) {
//        if (this.lastModel == bakedModel)
//            return;
//
//        this.lastModel = bakedModel;
//        this.head = getHeadBone();
//        this.body = getBodyBone();
//        this.bodyNeck = getBodyNeckBone();
//        this.bodyWaist = getBodyWaistBone();
//        this.bodyLeggings = getBodyLeggingsBone();
//        this.rightArm = getRightArmBone();
//        this.leftArm = getLeftArmBone();
//        this.rightHand = getRightHandBone();
//        this.leftHand = getLeftHandBone();
//        this.rightElbow = getRightElbowBone();
//        this.leftElbow = getLeftElbowBone();
//        this.rightLeg = getRightLegBone();
//        this.leftLeg = getLeftLegBone();
//        this.rightBoot = getRightBootBone();
//        this.leftBoot = getLeftBootBone();
//    }
//
//    /**
//     * Resets the bone visibility for the model based on the currently rendering slot,
//     * and then sets bones relevant to the current slot as visible for rendering.<br>
//     * <br>
//     * This is only called by default for non-geo entities (I.E. players or vanilla mobs)
//     */
//    @Override
//    protected void applyBoneVisibilityBySlot(EquipmentSlot currentSlot) {
//        setVisible(false);
//
//        if (this.currentStack.getItem() instanceof ArmorTrinketItem && !((ArmorTrinketItem) this.currentStack.getItem()).isProtecting(this.currentStack)) return;
//
//        if (currentSlot == EquipmentSlot.HEAD) {
//            setBoneVisible(this.head, true);
//        } else if (currentSlot == EquipmentSlot.CHEST) {
//            setBoneVisible(this.body, true);
//        } else if (currentSlot == EquipmentSlot.LEGS) {
//            setBoneVisible(this.bodyLeggings, true);
//            setBoneVisible(this.rightLeg, true);
//            setBoneVisible(this.leftLeg, true);
//        } else if (currentSlot == EquipmentSlot.FEET) {
//            setBoneVisible(this.rightBoot, true);
//            setBoneVisible(this.leftBoot, true);
//        } else if (currentSlot == ExtendedEquipmentSlot.GLOVES) {
//            setBoneVisible(this.rightHand, true);
//            setBoneVisible(this.leftHand, true);
//        } else if (currentSlot == ExtendedEquipmentSlot.SHOULDERS) {
//            setBoneVisible(this.rightArm, true);
//            setBoneVisible(this.leftArm, true);
//        } else if (currentSlot == ExtendedEquipmentSlot.RING_1) {
//            setBoneVisible(this.rightElbow, true);
//        } else if (currentSlot == ExtendedEquipmentSlot.RING_2) {
//            setBoneVisible(this.leftElbow, true);
//        } else if (currentSlot == ExtendedEquipmentSlot.NECKLACE) {
//            setBoneVisible(this.bodyNeck, true);
//        } else if (currentSlot == ExtendedEquipmentSlot.BELT) {
//            setBoneVisible(this.bodyWaist, true);
//        }
//    }
//
//    /**
//     * Resets the bone visibility for the model based on the current {@link ModelPart} and {@link EquipmentSlot},
//     * and then sets the bones relevant to the current part as visible for rendering.<br>
//     * <br>
//     * If you are rendering a geo entity with armor, you should probably be calling this prior to rendering
//     */
//    @Override
//    public void applyBoneVisibilityByPart(EquipmentSlot currentSlot, ModelPart currentPart, BipedEntityModel<?> model) {
//        setVisible(false);
//
//        currentPart.visible = true;
//        GeoBone bone = null;
//
//        if (currentPart == model.hat || currentPart == model.head) {
//            bone = this.head;
//        }
//        else if (currentPart == model.body) {
//            bone = currentSlot == EquipmentSlot.LEGS ? this.bodyLeggings : currentSlot == ExtendedEquipmentSlot.NECKLACE ? this.bodyNeck : currentSlot == ExtendedEquipmentSlot.BELT ? this.bodyWaist : this.body;
//        }
//        else if (currentPart == model.leftArm) {
//            bone = currentSlot == ExtendedEquipmentSlot.GLOVES ? this.leftHand : currentSlot == ExtendedEquipmentSlot.RING_2 ? this.leftElbow : this.leftArm;
//        }
//        else if (currentPart == model.rightArm) {
//            bone = currentSlot == ExtendedEquipmentSlot.GLOVES ? this.rightHand : currentSlot == ExtendedEquipmentSlot.RING_1 ? this.rightElbow : this.rightArm;
//        }
//        else if (currentPart == model.leftLeg) {
//            bone = currentSlot == EquipmentSlot.FEET ? this.leftBoot : this.leftLeg;
//        }
//        else if (currentPart == model.rightLeg) {
//            bone = currentSlot == EquipmentSlot.FEET ? this.rightBoot : this.rightLeg;
//        }
//
//        if (bone != null)
//            bone.setHidden(false);
//    }
//
//    /**
//     * Transform the currently rendering {@link GeoModel} to match the positions and rotations of the base model
//     */
//    @Override
//    protected void applyBaseTransformations(BipedEntityModel<?> baseModel) {
//        if (this.head != null) {
//            ModelPart headPart = baseModel.head;
//
//            RenderUtils.matchModelPartRot(headPart, this.head);
//            this.head.updatePosition(headPart.pivotX, -headPart.pivotY, headPart.pivotZ);
//        }
//
//        if (this.body != null) {
//            ModelPart bodyPart = baseModel.body;
//
//            RenderUtils.matchModelPartRot(bodyPart, this.body);
//            this.body.updatePosition(bodyPart.pivotX, -bodyPart.pivotY, bodyPart.pivotZ);
//
//            if (this.bodyNeck != null) {
//                RenderUtils.matchModelPartRot(bodyPart, this.bodyNeck);
//                this.bodyNeck.updatePosition(bodyPart.pivotX, -bodyPart.pivotY, bodyPart.pivotZ);
//            }
//
//            if (this.bodyWaist != null) {
//                RenderUtils.matchModelPartRot(bodyPart, this.bodyWaist);
//                this.bodyWaist.updatePosition(bodyPart.pivotX, -bodyPart.pivotY, bodyPart.pivotZ);
//            }
//
//            if (this.bodyLeggings != null) {
//                RenderUtils.matchModelPartRot(bodyPart, this.bodyLeggings);
//                this.bodyLeggings.updatePosition(bodyPart.pivotX, -bodyPart.pivotY, bodyPart.pivotZ);
//            }
//        }
//
//        if (this.rightArm != null) {
//            ModelPart rightArmPart = baseModel.rightArm;
//
//            RenderUtils.matchModelPartRot(rightArmPart, this.rightArm);
//            this.rightArm.updatePosition(rightArmPart.pivotX + 5, 2 - rightArmPart.pivotY, rightArmPart.pivotZ);
//
//            if (this.rightElbow != null) {
//                RenderUtils.matchModelPartRot(rightArmPart, this.rightElbow);
//                this.rightElbow.updatePosition(rightArmPart.pivotX + 5, 2 - rightArmPart.pivotY, rightArmPart.pivotZ);
//            }
//
//            if (this.rightHand != null) {
//                RenderUtils.matchModelPartRot(rightArmPart, this.rightHand);
//                this.rightHand.updatePosition(rightArmPart.pivotX + 5, 2 - rightArmPart.pivotY, rightArmPart.pivotZ);
//            }
//        }
//
//        if (this.leftArm != null) {
//            ModelPart leftArmPart = baseModel.leftArm;
//
//            RenderUtils.matchModelPartRot(leftArmPart, this.leftArm);
//            this.leftArm.updatePosition(leftArmPart.pivotX - 5f, 2f - leftArmPart.pivotY, leftArmPart.pivotZ);
//
//            if (this.leftElbow != null) {
//                RenderUtils.matchModelPartRot(leftArmPart, this.leftElbow);
//                this.leftElbow.updatePosition(leftArmPart.pivotX - 5f, 2f - leftArmPart.pivotY, leftArmPart.pivotZ);
//            }
//
//            if (this.leftHand != null) {
//                RenderUtils.matchModelPartRot(leftArmPart, this.leftHand);
//                this.leftHand.updatePosition(leftArmPart.pivotX - 5f, 2f - leftArmPart.pivotY, leftArmPart.pivotZ);
//            }
//        }
//
//        if (this.rightLeg != null) {
//            ModelPart rightLegPart = baseModel.rightLeg;
//
//            RenderUtils.matchModelPartRot(rightLegPart, this.rightLeg);
//            this.rightLeg.updatePosition(rightLegPart.pivotX + 2, 12 - rightLegPart.pivotY, rightLegPart.pivotZ);
//
//            if (this.rightBoot != null) {
//                RenderUtils.matchModelPartRot(rightLegPart, this.rightBoot);
//                this.rightBoot.updatePosition(rightLegPart.pivotX + 2, 12 - rightLegPart.pivotY, rightLegPart.pivotZ);
//            }
//        }
//
//        if (this.leftLeg != null) {
//            ModelPart leftLegPart = baseModel.leftLeg;
//
//            RenderUtils.matchModelPartRot(leftLegPart, this.leftLeg);
//            this.leftLeg.updatePosition(leftLegPart.pivotX - 2, 12 - leftLegPart.pivotY, leftLegPart.pivotZ);
//
//            if (this.leftBoot != null) {
//                RenderUtils.matchModelPartRot(leftLegPart, this.leftBoot);
//                this.leftBoot.updatePosition(leftLegPart.pivotX - 2, 12 - leftLegPart.pivotY, leftLegPart.pivotZ);
//            }
//        }
//    }
//
//    @Override
//    public void setVisible(boolean pVisible) {
//        super.setVisible(pVisible);
//
//        setBoneVisible(this.head, pVisible);
//        setBoneVisible(this.body, pVisible);
//        setBoneVisible(this.bodyNeck, pVisible);
//        setBoneVisible(this.bodyWaist, pVisible);
//        setBoneVisible(this.bodyLeggings, pVisible);
//        setBoneVisible(this.rightArm, pVisible);
//        setBoneVisible(this.leftArm, pVisible);
//        setBoneVisible(this.rightElbow, pVisible);
//        setBoneVisible(this.leftElbow, pVisible);
//        setBoneVisible(this.rightHand, pVisible);
//        setBoneVisible(this.leftHand, pVisible);
//        setBoneVisible(this.rightLeg, pVisible);
//        setBoneVisible(this.leftLeg, pVisible);
//        setBoneVisible(this.rightBoot, pVisible);
//        setBoneVisible(this.leftBoot, pVisible);
//    }
//}
