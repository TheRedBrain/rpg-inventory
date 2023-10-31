package com.github.theredbrain.bamcore.client.render.renderer;

import com.github.theredbrain.bamcore.api.item.ArmorTrinketItem;
import com.github.theredbrain.bamcore.azurelib.BetterAdventureModeCoreDataTickets;
import com.github.theredbrain.bamcore.azurelib.Trinket;
import dev.emi.trinkets.api.SlotReference;
import mod.azure.azurelibarmor.animatable.GeoItem;
import mod.azure.azurelibarmor.cache.object.BakedGeoModel;
import mod.azure.azurelibarmor.cache.object.GeoBone;
import mod.azure.azurelibarmor.cache.texture.AnimatableTexture;
import mod.azure.azurelibarmor.constant.DataTickets;
import mod.azure.azurelibarmor.core.animatable.GeoAnimatable;
import mod.azure.azurelibarmor.core.animation.AnimationState;
import mod.azure.azurelibarmor.model.GeoModel;
import mod.azure.azurelibarmor.renderer.GeoRenderer;
import mod.azure.azurelibarmor.renderer.layer.GeoRenderLayer;
import mod.azure.azurelibarmor.renderer.layer.GeoRenderLayersContainer;
import mod.azure.azurelibarmor.util.RenderUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;

import java.util.List;
import java.util.Objects;

public class ModeledTrinketRenderer<T extends Item & GeoItem> extends BipedEntityModel implements GeoRenderer<T> {
    protected final GeoRenderLayersContainer<T> renderLayers = new GeoRenderLayersContainer<>(this);
    protected final GeoModel<T> model;

    protected T animatable;
    protected BipedEntityModel<?> baseModel;
    protected float scaleWidth = 1;
    protected float scaleHeight = 1;

    protected Matrix4f entityRenderTranslations = new Matrix4f();
    protected Matrix4f modelRenderTranslations = new Matrix4f();

    protected BakedGeoModel lastModel = null;
    protected GeoBone head = null;
    protected GeoBone body = null;
    protected GeoBone bodyNeck = null;
    protected GeoBone bodyWaist = null;
    protected GeoBone bodyHips = null;
    protected GeoBone rightShoulder = null;
    protected GeoBone leftShoulder = null;
    protected GeoBone rightElbow = null;
    protected GeoBone leftElbow = null;
    protected GeoBone rightHand = null;
    protected GeoBone leftHand = null;
    protected GeoBone rightLeg = null;
    protected GeoBone leftLeg = null;
    protected GeoBone rightBoot = null;
    protected GeoBone leftBoot = null;

    protected Entity currentEntity = null;
    protected ItemStack currentStack = null;
    protected String currentSlotGroup = "";
    protected String currentSlotName = "";

    public ModeledTrinketRenderer(GeoModel<T> model) {
        super(MinecraftClient.getInstance().getEntityModelLoader().getModelPart(EntityModelLayers.PLAYER_INNER_ARMOR));

        this.model = model;
        this.child = false;
    }

    /**
     * Gets the model instance for this renderer
     */
    @Override
    public GeoModel<T> getGeoModel() {
        return this.model;
    }

    /**
     * Gets the {@link GeoItem} instance currently being rendered
     */
    public T getAnimatable() {
        return this.animatable;
    }

    /**
     * Returns the entity currently being rendered with armour equipped
     */
    public Entity getCurrentEntity() {
        return this.currentEntity;
    }

    /**
     * Returns the ItemStack pertaining to the current piece of armor being rendered
     */
    public ItemStack getCurrentStack() {
        return this.currentStack;
    }

    /**
     * Returns the slot group of the trinket being rendered
     */
    public String getCurrentSlotGroup() {
        return this.currentSlotGroup;
    }

    /**
     * Returns the slot name of the trinket being rendered
     */
    public String getCurrentSlotName() {
        return this.currentSlotName;
    }

    /**
     * Gets the id that represents the current animatable's instance for animation purposes. This is mostly useful for things like items, which have a single registered instance for all objects
     */
    @Override
    public long getInstanceId(T animatable) {
        return GeoItem.getId(this.currentStack) + this.currentEntity.getId();
    }

    /**
     * Gets the {@link RenderLayer} to render the given animatable with.<br>
     * Uses the {@link RenderLayer#getArmorCutoutNoCull} {@code RenderType} by default.<br>
     * Override this to change the way a model will render (such as translucent models, etc)
     */
    @Override
    public RenderLayer getRenderType(T animatable, Identifier texture, @org.jetbrains.annotations.Nullable VertexConsumerProvider bufferSource, float partialTick) {
        return RenderLayer.getArmorCutoutNoCull(texture);
    }

    /**
     * Returns the list of registered {@link GeoRenderLayer GeoRenderLayers} for this renderer
     */
    @Override
    public List<GeoRenderLayer<T>> getRenderLayers() {
        return this.renderLayers.getRenderLayers();
    }

    /**
     * Adds a {@link GeoRenderLayer} to this renderer, to be called after the main model is rendered each frame
     */
    public ModeledTrinketRenderer<T> addRenderLayer(GeoRenderLayer<T> renderLayer) {
        this.renderLayers.addLayer(renderLayer);

        return this;
    }

    /**
     * Sets a scale override for this renderer, telling AzureLib to pre-scale the model
     */
    public ModeledTrinketRenderer<T> withScale(float scale) {
        return withScale(scale, scale);
    }

    /**
     * Sets a scale override for this renderer, telling AzureLib to pre-scale the model
     */
    public ModeledTrinketRenderer<T> withScale(float scaleWidth, float scaleHeight) {
        this.scaleWidth = scaleWidth;
        this.scaleHeight = scaleHeight;

        return this;
    }

    /**
     * Returns the 'head' GeoBone from this model.<br>
     * Override if your geo model has different bone names for these bones
     * @return The bone for the head model piece, or null if not using it
     */
    @Nullable
    public GeoBone getHeadBone() {
        return this.model.getBone("armorHead").orElse(null);
    }

    /**
     * Returns the 'body' GeoBone from this model.<br>
     * Override if your geo model has different bone names for these bones
     * @return The bone for the body model piece, or null if not using it
     */
    @Nullable
    public GeoBone getBodyBone() {
        return this.model.getBone("armorBody").orElse(null);
    }

    /**
     * Returns the 'body neck' GeoBone from this model.<br>
     * Override if your geo model has different bone names for these bones
     * @return The bone for the body model piece, or null if not using it
     */
    @Nullable
    public GeoBone getBodyNeckBone() {
        return this.model.getBone("armorBodyNeck").orElse(null);
    }

    /**
     * Returns the 'body waist' GeoBone from this model.<br>
     * Override if your geo model has different bone names for these bones
     * @return The bone for the body model piece, or null if not using it
     */
    @Nullable
    public GeoBone getBodyWaistBone() {
        return this.model.getBone("armorBodyWaist").orElse(null);
    }

    /**
     * Returns the 'body hips' GeoBone from this model.<br>
     * Override if your geo model has different bone names for these bones
     * @return The bone for the body model piece, or null if not using it
     */
    @Nullable
    public GeoBone getBodyHipsBone() {
        return this.model.getBone("armorBodyHips").orElse(null);
    }

    /**
     * Returns the 'right shoulder' GeoBone from this model.<br>
     * Override if your geo model has different bone names for these bones
     * @return The bone for the right arm model piece, or null if not using it
     */
    @Nullable
    public GeoBone getRightShoulderBone() {
        return this.model.getBone("armorRightShoulder").orElse(null);
    }

    /**
     * Returns the 'left shoulder' GeoBone from this model.<br>
     * Override if your geo model has different bone names for these bones
     * @return The bone for the left arm model piece, or null if not using it
     */
    @Nullable
    public GeoBone getLeftShoulderBone() {
        return this.model.getBone("armorLeftShoulder").orElse(null);
    }

    /**
     * Returns the 'right elbow' GeoBone from this model.<br>
     * Override if your geo model has different bone names for these bones
     * @return The bone for the right arm model piece, or null if not using it
     */
    @Nullable
    public GeoBone getRightElbowBone() {
        return this.model.getBone("armorRightElbow").orElse(null);
    }

    /**
     * Returns the 'left elbow' GeoBone from this model.<br>
     * Override if your geo model has different bone names for these bones
     * @return The bone for the left arm model piece, or null if not using it
     */
    @Nullable
    public GeoBone getLeftElbowBone() {
        return this.model.getBone("armorLeftElbow").orElse(null);
    }

    /**
     * Returns the 'right hand' GeoBone from this model.<br>
     * Override if your geo model has different bone names for these bones
     * @return The bone for the right arm model piece, or null if not using it
     */
    @Nullable
    public GeoBone getRightHandBone() {
        return this.model.getBone("armorRightHand").orElse(null);
    }

    /**
     * Returns the 'left hand' GeoBone from this model.<br>
     * Override if your geo model has different bone names for these bones
     * @return The bone for the left arm model piece, or null if not using it
     */
    @Nullable
    public GeoBone getLeftHandBone() {
        return this.model.getBone("armorLeftHand").orElse(null);
    }

    /**
     * Returns the 'right leg' GeoBone from this model.<br>
     * Override if your geo model has different bone names for these bones
     * @return The bone for the right leg model piece, or null if not using it
     */
    @Nullable
    public GeoBone getRightLegBone() {
        return this.model.getBone("armorRightLeg").orElse(null);
    }

    /**
     * Returns the 'left leg' GeoBone from this model.<br>
     * Override if your geo model has different bone names for these bones
     * @return The bone for the left leg model piece, or null if not using it
     */
    @Nullable
    public GeoBone getLeftLegBone() {
        return this.model.getBone("armorLeftLeg").orElse(null);
    }

    /**
     * Returns the 'right boot' GeoBone from this model.<br>
     * Override if your geo model has different bone names for these bones
     * @return The bone for the right boot model piece, or null if not using it
     */
    @Nullable
    public GeoBone getRightBootBone() {
        return this.model.getBone("armorRightBoot").orElse(null);
    }

    /**
     * Returns the 'left boot' GeoBone from this model.<br>
     * Override if your geo model has different bone names for these bones
     * @return The bone for the left boot model piece, or null if not using it
     */
    @Nullable
    public GeoBone getLeftBootBone() {
        return this.model.getBone("armorLeftBoot").orElse(null);
    }

    /**
     * Called before rendering the model to buffer. Allows for render modifications and preparatory work such as scaling and translating.<br>
     * {@link MatrixStack} translations made here are kept until the end of the render process
     */
    @Override
    public void preRender(MatrixStack poseStack, T animatable, BakedGeoModel model, @Nullable VertexConsumerProvider bufferSource, @Nullable VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        this.entityRenderTranslations = new Matrix4f(poseStack.peek().getPositionMatrix());

        applyBaseModel(this.baseModel);
        grabRelevantBones(getGeoModel().getBakedModel(getGeoModel().getModelResource(this.animatable)));
        applyBaseTransformations(this.baseModel);
        scaleModelForBaby(poseStack, animatable, partialTick, isReRender);
        scaleModelForRender(this.scaleWidth, this.scaleHeight, poseStack, animatable, model, isReRender, partialTick, packedLight, packedOverlay);

        if (!(this.currentEntity instanceof GeoAnimatable))
            applyBoneVisibilityByTrinketSlot(this.currentSlotGroup, this.currentSlotName);
    }

    @Override
    public void render(MatrixStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        MinecraftClient mc = MinecraftClient.getInstance();
        VertexConsumerProvider bufferSource = mc.worldRenderer.bufferBuilders.getEntityVertexConsumers(); // TODO correct method call?

        if (mc.worldRenderer.canDrawEntityOutlines() && mc.hasOutline(this.currentEntity))
            bufferSource = mc.worldRenderer.bufferBuilders.getOutlineVertexConsumers(); // TODO correct method call?

        float partialTick = mc.getTickDelta();
        RenderLayer renderType = getRenderType(this.animatable, getTextureLocation(this.animatable), bufferSource, partialTick);
        buffer = ItemRenderer.getArmorGlintConsumer(bufferSource, renderType, false, this.currentStack.hasGlint());

        defaultRender(poseStack, this.animatable, bufferSource, null, buffer, 0, partialTick, packedLight);
    }

    /**
     * The actual render method that subtype renderers should override to handle their specific rendering tasks.<br>
     * {@link GeoRenderer#preRender} has already been called by this stage, and {@link GeoRenderer#postRender} will be called directly after
     */
    @Override
    public void actuallyRender(MatrixStack poseStack, T animatable, BakedGeoModel model, RenderLayer renderType, VertexConsumerProvider bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        poseStack.push();
        poseStack.translate(0, 24 / 16f, 0);
        poseStack.scale(-1, -1, 1);

        if (!isReRender) {
            AnimationState<T> animationState = new AnimationState<>(animatable, 0, 0, partialTick, false);
            long instanceId = getInstanceId(animatable);

            animationState.setData(DataTickets.TICK, animatable.getTick(this.currentEntity));
            animationState.setData(DataTickets.ITEMSTACK, this.currentStack);
            animationState.setData(DataTickets.ENTITY, this.currentEntity);
            animationState.setData(BetterAdventureModeCoreDataTickets.STRING, this.currentSlotGroup);
            animationState.setData(BetterAdventureModeCoreDataTickets.STRING, this.currentSlotName);
            this.model.addAdditionalStateData(animatable, instanceId, animationState::setData);
            this.model.handleAnimations(animatable, instanceId, animationState);
        }

        this.modelRenderTranslations = new Matrix4f(poseStack.peek().getPositionMatrix());

        GeoRenderer.super.actuallyRender(poseStack, animatable, model, renderType, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
        poseStack.pop();
    }

    /**
     * Renders the provided {@link GeoBone} and its associated child bones
     */
    @Override
    public void renderRecursively(MatrixStack poseStack, T animatable, GeoBone bone, RenderLayer renderType, VertexConsumerProvider bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        if (bone.isTrackingMatrices()) {
            Matrix4f poseState = new Matrix4f(poseStack.peek().getPositionMatrix());
            Matrix4f localMatrix = RenderUtils.invertAndMultiplyMatrices(poseState, this.entityRenderTranslations);

            bone.setModelSpaceMatrix(RenderUtils.invertAndMultiplyMatrices(poseState, this.modelRenderTranslations));
            bone.setLocalSpaceMatrix(RenderUtils.translateMatrix(localMatrix, getRenderOffset(this.currentEntity, 1).toVector3f()));
            bone.setWorldSpaceMatrix(RenderUtils.translateMatrix(new Matrix4f(localMatrix), this.currentEntity.getPos().toVector3f()));
        }

        GeoRenderer.super.renderRecursively(poseStack, animatable, bone, renderType, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
    }

    public Vec3d getRenderOffset(Entity entity, float f) {
        return Vec3d.ZERO;
    }

    /**
     * Gets and caches the relevant armor model bones for this baked model if it hasn't been done already
     */
    protected void grabRelevantBones(BakedGeoModel bakedModel) {
        if (this.lastModel == bakedModel)
            return;

        this.lastModel = bakedModel;
        this.head = getHeadBone();
        this.body = getBodyBone();
        this.bodyNeck = getBodyNeckBone();
        this.bodyWaist = getBodyWaistBone();
        this.bodyHips = getBodyHipsBone();
        this.rightShoulder = getRightShoulderBone();
        this.leftShoulder = getLeftShoulderBone();
        this.rightElbow = getRightElbowBone();
        this.leftElbow = getLeftElbowBone();
        this.rightHand = getRightHandBone();
        this.leftHand = getLeftHandBone();
        this.rightLeg = getRightLegBone();
        this.leftLeg = getLeftLegBone();
        this.rightBoot = getRightBootBone();
        this.leftBoot = getLeftBootBone();
    }

    /**
     * Prepare the renderer for the current render cycle.<br>
     * Must be called prior to render as the default HumanoidModel doesn't give render context.<br>
     * Params have been left nullable so that the renderer can be called for model/texture purposes safely. If you do grab the renderer using null parameters, you should not use it for actual rendering.
     *
     * @param entity The entity being rendered with the armor on
     * @param stack The ItemStack being rendered
     * @param slotGroup The slotGroup of the slot being rendered
     * @param slotName The slotName of the slot being rendered
     * @param baseModel The default (vanilla) model that would have been rendered if this model hadn't replaced it
     */
    public void prepForRender(@Nullable Entity entity, ItemStack stack, @Nullable String slotGroup, @Nullable String slotName, @Nullable BipedEntityModel<?> baseModel) {
        if (entity == null || Objects.equals(slotGroup, "") || Objects.equals(slotName, "") || baseModel == null)
            return;

        this.baseModel = baseModel;
        this.currentEntity = entity;
        this.currentStack = stack;
        this.animatable = (T) stack.getItem();
        this.currentSlotGroup = slotGroup;
        this.currentSlotName = slotName;
    }

    /**
     * Applies settings and transformations pre-render based on the default model
     */
    protected void applyBaseModel(BipedEntityModel<?> baseModel) {
        this.child = baseModel.child;
        this.sneaking = baseModel.sneaking;
        this.riding = baseModel.riding;
        this.rightArmPose = baseModel.rightArmPose;
        this.leftArmPose = baseModel.leftArmPose;
    }

    /**
     * Resets the bone visibility for the model based on the currently rendering slot,
     * and then sets bones relevant to the current slot as visible for rendering.<br>
     * <br>
     * This is only called by default for non-geo entities (I.E. players or vanilla mobs)
     */
    protected void applyBoneVisibilityByTrinketSlot(String currentSlotGroup, String currentSlotName) { // TODO trinket slot
        setVisible(false);

        if (this.currentStack.getItem() instanceof ArmorTrinketItem && !((ArmorTrinketItem) this.currentStack.getItem()).isProtecting(this.currentStack)) return;

//        SlotType slotType = currentSlotGroup.inventory().getSlotType();
        if (currentSlotGroup.equals("helmets")/* && slotType.getName().equals("helmet")*/) {
            setBoneVisible(this.head, true);
        } else if (currentSlotGroup.equals("chest_plates")/* && slotType.getName().equals("helmet")*/) {
            setBoneVisible(this.body, true);
        } else if (currentSlotGroup.equals("leggings")/* && slotType.getName().equals("helmet")*/) {
            setBoneVisible(this.bodyHips, true);
            setBoneVisible(this.rightLeg, true);
            setBoneVisible(this.leftLeg, true);
        } else if (currentSlotGroup.equals("boots")/* && slotType.getName().equals("helmet")*/) {
            setBoneVisible(this.rightBoot, true);
            setBoneVisible(this.leftBoot, true);
        } else if (currentSlotGroup.equals("gloves")/* && slotType.getName().equals("helmet")*/) {
            setBoneVisible(this.rightHand, true);
            setBoneVisible(this.leftHand, true);
        } else if (currentSlotGroup.equals("shoulders")/* && slotType.getName().equals("helmet")*/) {
            setBoneVisible(this.rightShoulder, true);
            setBoneVisible(this.leftShoulder, true);
        } else if (currentSlotGroup.equals("rings_1")/* && slotType.getName().equals("helmet")*/) {
            setBoneVisible(this.rightElbow, true);
        } else if (currentSlotGroup.equals("rings_2")/* && slotType.getName().equals("helmet")*/) {
            setBoneVisible(this.leftElbow, true);
        } else if (currentSlotGroup.equals("necklaces")/* && slotType.getName().equals("helmet")*/) {
            setBoneVisible(this.bodyNeck, true);
        } else if (currentSlotGroup.equals("belts")/* && slotType.getName().equals("helmet")*/) {
            setBoneVisible(this.bodyWaist, true);
        }
    }

    /**
     * Resets the bone visibility for the model based on the current {@link ModelPart} and {@link EquipmentSlot},
     * and then sets the bones relevant to the current part as visible for rendering.<br>
     * <br>
     * If you are rendering a geo entity with armor, you should probably be calling this prior to rendering
     */
    public void applyBoneVisibilityByPart(SlotReference currentSlotReference, ModelPart currentPart, BipedEntityModel<?> model) {
//        setVisible(false);
//
//        currentPart.visible = true;
//        GeoBone bone = null;
//
//        if (currentPart == model.hat || currentPart == model.head) {
//            bone = this.head;
//        }
//        else if (currentPart == model.body) {
//            bone = currentSlotReference == EquipmentSlot.LEGS ? this.bodyHips : currentSlotReference == ExtendedEquipmentSlot.NECKLACE ? this.bodyNeck : currentSlotReference == ExtendedEquipmentSlot.BELT ? this.bodyWaist : this.body;
//        }
//        else if (currentPart == model.leftArm) {
//            bone = currentSlotReference == ExtendedEquipmentSlot.GLOVES ? this.leftHand : currentSlotReference == ExtendedEquipmentSlot.RING_2 ? this.leftElbow : this.leftShoulder;
//        }
//        else if (currentPart == model.rightArm) {
//            bone = currentSlotReference == ExtendedEquipmentSlot.GLOVES ? this.rightHand : currentSlotReference == ExtendedEquipmentSlot.RING_1 ? this.rightElbow : this.rightShoulder;
//        }
//        else if (currentPart == model.leftLeg) {
//            bone = currentSlotReference == EquipmentSlot.FEET ? this.leftBoot : this.leftLeg;
//        }
//        else if (currentPart == model.rightLeg) {
//            bone = currentSlotReference == EquipmentSlot.FEET ? this.rightBoot : this.rightLeg;
//        }
//
//        if (bone != null)
//            bone.setHidden(false);
    }

    /**
     * Transform the currently rendering {@link GeoModel} to match the positions and rotations of the base model
     */
    protected void applyBaseTransformations(BipedEntityModel<?> baseModel) {
        if (this.head != null) {
            ModelPart headPart = baseModel.head;

            RenderUtils.matchModelPartRot(headPart, this.head);
            this.head.updatePosition(headPart.pivotX, -headPart.pivotY, headPart.pivotZ);
        }

        if (this.body != null) {
            ModelPart bodyPart = baseModel.body;

            RenderUtils.matchModelPartRot(bodyPart, this.body);
            this.body.updatePosition(bodyPart.pivotX, -bodyPart.pivotY, bodyPart.pivotZ);

            if (this.bodyNeck != null) {
                RenderUtils.matchModelPartRot(bodyPart, this.bodyNeck);
                this.bodyNeck.updatePosition(bodyPart.pivotX, -bodyPart.pivotY, bodyPart.pivotZ);
            }

            if (this.bodyWaist != null) {
                RenderUtils.matchModelPartRot(bodyPart, this.bodyWaist);
                this.bodyWaist.updatePosition(bodyPart.pivotX, -bodyPart.pivotY, bodyPart.pivotZ);
            }

            if (this.bodyHips != null) {
                RenderUtils.matchModelPartRot(bodyPart, this.bodyHips);
                this.bodyHips.updatePosition(bodyPart.pivotX, -bodyPart.pivotY, bodyPart.pivotZ);
            }
        }

        // TODO decouple elbows and hands from shoulders
        if (this.rightShoulder != null) {
            ModelPart rightArmPart = baseModel.rightArm;

            RenderUtils.matchModelPartRot(rightArmPart, this.rightShoulder);
            this.rightShoulder.updatePosition(rightArmPart.pivotX + 5, 2 - rightArmPart.pivotY, rightArmPart.pivotZ);

            if (this.rightElbow != null) {
                RenderUtils.matchModelPartRot(rightArmPart, this.rightElbow);
                this.rightElbow.updatePosition(rightArmPart.pivotX + 5, 2 - rightArmPart.pivotY, rightArmPart.pivotZ);
            }

            if (this.rightHand != null) {
                RenderUtils.matchModelPartRot(rightArmPart, this.rightHand);
                this.rightHand.updatePosition(rightArmPart.pivotX + 5, 2 - rightArmPart.pivotY, rightArmPart.pivotZ);
            }
        }

        if (this.leftShoulder != null) {
            ModelPart leftArmPart = baseModel.leftArm;

            RenderUtils.matchModelPartRot(leftArmPart, this.leftShoulder);
            this.leftShoulder.updatePosition(leftArmPart.pivotX - 5f, 2f - leftArmPart.pivotY, leftArmPart.pivotZ);

            if (this.leftElbow != null) {
                RenderUtils.matchModelPartRot(leftArmPart, this.leftElbow);
                this.leftElbow.updatePosition(leftArmPart.pivotX - 5f, 2f - leftArmPart.pivotY, leftArmPart.pivotZ);
            }

            if (this.leftHand != null) {
                RenderUtils.matchModelPartRot(leftArmPart, this.leftHand);
                this.leftHand.updatePosition(leftArmPart.pivotX - 5f, 2f - leftArmPart.pivotY, leftArmPart.pivotZ);
            }
        }

        // TODO decouple boots from legs
        if (this.rightLeg != null) {
            ModelPart rightLegPart = baseModel.rightLeg;

            RenderUtils.matchModelPartRot(rightLegPart, this.rightLeg);
            this.rightLeg.updatePosition(rightLegPart.pivotX + 2, 12 - rightLegPart.pivotY, rightLegPart.pivotZ);

            if (this.rightBoot != null) {
                RenderUtils.matchModelPartRot(rightLegPart, this.rightBoot);
                this.rightBoot.updatePosition(rightLegPart.pivotX + 2, 12 - rightLegPart.pivotY, rightLegPart.pivotZ);
            }
        }

        if (this.leftLeg != null) {
            ModelPart leftLegPart = baseModel.leftLeg;

            RenderUtils.matchModelPartRot(leftLegPart, this.leftLeg);
            this.leftLeg.updatePosition(leftLegPart.pivotX - 2, 12 - leftLegPart.pivotY, leftLegPart.pivotZ);

            if (this.leftBoot != null) {
                RenderUtils.matchModelPartRot(leftLegPart, this.leftBoot);
                this.leftBoot.updatePosition(leftLegPart.pivotX - 2, 12 - leftLegPart.pivotY, leftLegPart.pivotZ);
            }
        }
    }

    @Override
    public void setVisible(boolean pVisible) {
        super.setVisible(pVisible);

        setBoneVisible(this.head, pVisible);
        setBoneVisible(this.body, pVisible);
        setBoneVisible(this.bodyNeck, pVisible);
        setBoneVisible(this.bodyWaist, pVisible);
        setBoneVisible(this.bodyHips, pVisible);
        setBoneVisible(this.rightShoulder, pVisible);
        setBoneVisible(this.leftShoulder, pVisible);
        setBoneVisible(this.rightElbow, pVisible);
        setBoneVisible(this.leftElbow, pVisible);
        setBoneVisible(this.rightHand, pVisible);
        setBoneVisible(this.leftHand, pVisible);
        setBoneVisible(this.rightLeg, pVisible);
        setBoneVisible(this.leftLeg, pVisible);
        setBoneVisible(this.rightBoot, pVisible);
        setBoneVisible(this.leftBoot, pVisible);
    }

    /**
     * Apply custom scaling to account for {@link net.minecraft.client.render.entity.model.AnimalModel AgeableListModel} baby models
     */
    public void scaleModelForBaby(MatrixStack poseStack, T animatable, float partialTick, boolean isReRender) {
//        if (!this.child || isReRender)
//            return;
//
//        if (this.currentSlot == EquipmentSlot.HEAD) {
//            if (this.baseModel.headScaled) {
//                float headScale = 1.5f / this.baseModel.invertedChildHeadScale;
//
//                poseStack.scale(headScale, headScale, headScale);
//            }
//
//            poseStack.translate(0, this.baseModel.childHeadYOffset / 16f, this.baseModel.childHeadZOffset / 16f);
//        } else {
//            float bodyScale = 1 / this.baseModel.invertedChildBodyScale;
//
//            poseStack.scale(bodyScale, bodyScale, bodyScale);
//            poseStack.translate(0, this.baseModel.childBodyYOffset / 16f, 0);
//        }
    }

    /**
     * Sets a bone as visible or hidden, with nullability
     */
    protected void setBoneVisible(@Nullable GeoBone bone, boolean visible) {
        if (bone == null)
            return;

        bone.setHidden(!visible);
    }

    /**
     * Update the current frame of a {@link AnimatableTexture potentially animated} texture used by this GeoRenderer.<br>
     * This should only be called immediately prior to rendering, and only
     *
     * @see AnimatableTexture#setAndUpdate(Identifier, int)
     */
    @Override
    public void updateAnimatedTextureFrame(T animatable) {
        if (this.currentEntity != null)
            AnimatableTexture.setAndUpdate(getTextureLocation(animatable), this.currentEntity.getId() + this.currentEntity.age);
    }

    /**
     * Create and fire the relevant {@code CompileLayers} event hook for this renderer
     */
    @Override
    public void fireCompileRenderLayersEvent() {
        Trinket.CompileRenderLayers.EVENT.invoker().handle(new Trinket.CompileRenderLayers(this));
    }

    /**
     * Create and fire the relevant {@code Pre-Render} event hook for this renderer.<br>
     *
     * @return Whether the renderer should proceed based on the cancellation state of the event
     */
    @Override
    public boolean firePreRenderEvent(MatrixStack poseStack, BakedGeoModel model, VertexConsumerProvider bufferSource, float partialTick, int packedLight) {
        return Trinket.Pre.EVENT.invoker().handle(new Trinket.Pre(this, poseStack, model, bufferSource, partialTick, packedLight));
    }

    /**
     * Create and fire the relevant {@code Post-Render} event hook for this renderer
     */
    @Override
    public void firePostRenderEvent(MatrixStack poseStack, BakedGeoModel model, VertexConsumerProvider bufferSource, float partialTick, int packedLight) {
        Trinket.Post.EVENT.invoker().handle(new Trinket.Post(this, poseStack, model, bufferSource, partialTick, packedLight));
    }
}
