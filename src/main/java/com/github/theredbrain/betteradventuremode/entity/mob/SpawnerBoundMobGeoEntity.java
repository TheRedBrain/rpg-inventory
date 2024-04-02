package com.github.theredbrain.betteradventuremode.entity.mob;

import mod.azure.azurelib.animatable.GeoEntity;
import mod.azure.azurelib.core.animatable.instance.AnimatableInstanceCache;
import mod.azure.azurelib.core.animation.AnimatableManager;
import mod.azure.azurelib.util.AzureLibUtil;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.World;

public class SpawnerBoundMobGeoEntity extends SpawnerBoundMobEntity implements GeoEntity {
    private final AnimatableInstanceCache cache = AzureLibUtil.createInstanceCache(this);

    public static final TrackedData<String> ANIMATION_IDENTIFIER_STRING;
    public static final TrackedData<Float> BOUNDING_BOX_HEIGHT;
    public static final TrackedData<Float> BOUNDING_BOX_WIDTH;
    public static final TrackedData<String> MODEL_IDENTIFIER_STRING;
    public static final TrackedData<String> TEXTURE_IDENTIFIER_STRING;

    public SpawnerBoundMobGeoEntity(EntityType<? extends MobEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(ANIMATION_IDENTIFIER_STRING, "");
        this.dataTracker.startTracking(BOUNDING_BOX_HEIGHT, 1.8f);
        this.dataTracker.startTracking(BOUNDING_BOX_WIDTH, 0.8f);
        this.dataTracker.startTracking(MODEL_IDENTIFIER_STRING, "");
        this.dataTracker.startTracking(TEXTURE_IDENTIFIER_STRING, "");
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);

        String animationIdentifierString = this.getAnimationIdentifierString();
        if (!animationIdentifierString.equals("")) {
            nbt.putString("animationIdentifierString", animationIdentifierString);
        } else {
            nbt.remove("animationIdentifierString");
        }

        float boundingBoxHeight = this.getBoundingBoxHeight();
        if (boundingBoxHeight != 1.8f) {
            nbt.putFloat("boundingBoxHeight", boundingBoxHeight);
        } else {
            nbt.remove("boundingBoxHeight");
        }

        float boundingBoxWidth = this.getBoundingBoxWidth();
        if (boundingBoxWidth != 0.8f) {
            nbt.putFloat("boundingBoxWidth", boundingBoxWidth);
        } else {
            nbt.remove("boundingBoxWidth");
        }

        String modelIdentifierString = this.getModelIdentifierString();
        if (!modelIdentifierString.equals("")) {
            nbt.putString("modelIdentifierString", modelIdentifierString);
        } else {
            nbt.remove("modelIdentifierString");
        }

        String textureIdentifierString = this.getTextureIdentifierString();
        if (!textureIdentifierString.equals("")) {
            nbt.putString("textureIdentifierString", textureIdentifierString);
        } else {
            nbt.remove("textureIdentifierString");
        }
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);

        if (nbt.contains("animationIdentifierString")) {
            this.setAnimationIdentifierString(nbt.getString("animationIdentifierString"));
        }

        if (nbt.contains("boundingBoxHeight")) {
            this.setBoundingBoxHeight(nbt.getFloat("boundingBoxHeight"));
        }

        if (nbt.contains("boundingBoxWidth")) {
            this.setBoundingBoxWidth(nbt.getFloat("boundingBoxWidth"));
        }

        if (nbt.contains("modelIdentifierString")) {
            this.setModelIdentifierString(nbt.getString("modelIdentifierString"));
        }

        if (nbt.contains("textureIdentifierString")) {
            this.setTextureIdentifierString(nbt.getString("textureIdentifierString"));
        }
    }

    public String getAnimationIdentifierString() {
        return this.dataTracker.get(ANIMATION_IDENTIFIER_STRING);
    }
    public void setAnimationIdentifierString(String animationIdentifierString) {
        this.dataTracker.set(ANIMATION_IDENTIFIER_STRING, animationIdentifierString);
    }

    public float getBoundingBoxHeight() {
        return this.dataTracker.get(BOUNDING_BOX_HEIGHT);
    }
    public void setBoundingBoxHeight(float boundingBoxHeight) {
        this.dataTracker.set(BOUNDING_BOX_HEIGHT, boundingBoxHeight);
    }

    public float getBoundingBoxWidth() {
        return this.dataTracker.get(BOUNDING_BOX_WIDTH);
    }
    public void setBoundingBoxWidth(float boundingBoxWidth) {
        this.dataTracker.set(BOUNDING_BOX_WIDTH, boundingBoxWidth);
    }

    public String getModelIdentifierString() {
        return this.dataTracker.get(MODEL_IDENTIFIER_STRING);
    }
    public void setModelIdentifierString(String modelIdentifierString) {
        this.dataTracker.set(MODEL_IDENTIFIER_STRING, modelIdentifierString);
    }

    public String getTextureIdentifierString() {
        return this.dataTracker.get(TEXTURE_IDENTIFIER_STRING);
    }
    public void setTextureIdentifierString(String textureIdentifierString) {
        this.dataTracker.set(TEXTURE_IDENTIFIER_STRING, textureIdentifierString);
    }

    @Override
    public EntityDimensions getDimensions(EntityPose pose) {
        return EntityDimensions.changing(this.getBoundingBoxHeight(), this.getBoundingBoxWidth());
    }

//    @Override
//    protected float getActiveEyeHeight(EntityPose pose, EntityDimensions dimensions) {
//        return (float) this.spawnerBoundEntityEyeHeight;
//    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {

    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    static {

        ANIMATION_IDENTIFIER_STRING = DataTracker.registerData(SpawnerBoundMobGeoEntity.class, TrackedDataHandlerRegistry.STRING);
        BOUNDING_BOX_HEIGHT = DataTracker.registerData(SpawnerBoundMobGeoEntity.class, TrackedDataHandlerRegistry.FLOAT);
        BOUNDING_BOX_WIDTH = DataTracker.registerData(SpawnerBoundMobGeoEntity.class, TrackedDataHandlerRegistry.FLOAT);
        MODEL_IDENTIFIER_STRING = DataTracker.registerData(SpawnerBoundMobGeoEntity.class, TrackedDataHandlerRegistry.STRING);
        TEXTURE_IDENTIFIER_STRING = DataTracker.registerData(SpawnerBoundMobGeoEntity.class, TrackedDataHandlerRegistry.STRING);
    }
}
