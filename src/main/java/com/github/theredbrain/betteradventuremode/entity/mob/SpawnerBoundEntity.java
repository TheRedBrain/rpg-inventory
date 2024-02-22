package com.github.theredbrain.betteradventuremode.entity.mob;

import com.github.theredbrain.betteradventuremode.block.entity.TriggeredSpawnerBlockEntity;
import mod.azure.azurelib.common.api.common.animatable.GeoEntity;
import mod.azure.azurelib.common.internal.common.core.animatable.instance.AnimatableInstanceCache;
import mod.azure.azurelib.common.internal.common.core.animation.AnimatableManager;
import mod.azure.azurelib.common.internal.common.util.AzureLibUtil;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.*;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Arm;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import java.util.List;
import java.util.Objects;

public class SpawnerBoundEntity extends LivingEntity implements GeoEntity {
    private final AnimatableInstanceCache cache = AzureLibUtil.createInstanceCache(this);

    public static final TrackedData<String> ANIMATION_IDENTIFIER_STRING;
    public static final TrackedData<BlockPos> BOUND_SPAWNER_BLOCK_POS;
    public static final TrackedData<Float> BOUNDING_BOX_HEIGHT;
    public static final TrackedData<Float> BOUNDING_BOX_WIDTH;
    public static final TrackedData<String> MODEL_IDENTIFIER_STRING;
    public static final TrackedData<BlockPos> USE_RELAY_BLOCK_POS;
    public static final TrackedData<String> TEXTURE_IDENTIFIER_STRING;

    public SpawnerBoundEntity(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(ANIMATION_IDENTIFIER_STRING, "");
        this.dataTracker.startTracking(BOUND_SPAWNER_BLOCK_POS, new BlockPos(0, -100, 0));
        this.dataTracker.startTracking(BOUNDING_BOX_HEIGHT, 1.8f);
        this.dataTracker.startTracking(BOUNDING_BOX_WIDTH, 0.8f);
        this.dataTracker.startTracking(MODEL_IDENTIFIER_STRING, "");
        this.dataTracker.startTracking(USE_RELAY_BLOCK_POS, new BlockPos(0, -100, 0));
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

        BlockPos boundSpawnerBlockPos = this.getBoundSpawnerBlockPos();
        if (!boundSpawnerBlockPos.equals(new BlockPos(0, -100, 0))) {
            nbt.putInt("bound_spawner_block_pos_x", boundSpawnerBlockPos.getX());
            nbt.putInt("bound_spawner_block_pos_y", boundSpawnerBlockPos.getY());
            nbt.putInt("bound_spawner_block_pos_z", boundSpawnerBlockPos.getZ());
        } else {
            nbt.remove("bound_spawner_block_pos_x");
            nbt.remove("bound_spawner_block_pos_y");
            nbt.remove("bound_spawner_block_pos_z");
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

        BlockPos useRelayBlockPos = this.getUseRelayBlockPos();
        if (!useRelayBlockPos.equals(new BlockPos(0, -100, 0))) {
            nbt.putInt("use_relay_block_pos_x", useRelayBlockPos.getX());
            nbt.putInt("use_relay_block_pos_y", useRelayBlockPos.getY());
            nbt.putInt("use_relay_block_pos_z", useRelayBlockPos.getZ());
        } else {
            nbt.remove("use_relay_block_pos_x");
            nbt.remove("use_relay_block_pos_y");
            nbt.remove("use_relay_block_pos_z");
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

        if (nbt.contains("bound_spawner_block_pos_x") || nbt.contains("bound_spawner_block_pos_y") || nbt.contains("bound_spawner_block_pos_z")) {
            this.setBoundSpawnerBlockPos(new BlockPos(
                    nbt.getInt("bound_spawner_block_pos_x"),
                    nbt.getInt("bound_spawner_block_pos_y"),
                    nbt.getInt("bound_spawner_block_pos_z")
            ));
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

        if (nbt.contains("use_relay_block_pos_x") || nbt.contains("use_relay_block_pos_y") || nbt.contains("use_relay_block_pos_z")) {
            this.setUseRelayBlockPos(new BlockPos(
                    nbt.getInt("use_relay_block_pos_x"),
                    nbt.getInt("use_relay_block_pos_y"),
                    nbt.getInt("use_relay_block_pos_z")
            ));
        }

        if (nbt.contains("textureIdentifierString")) {
            this.setTextureIdentifierString(nbt.getString("textureIdentifierString"));
        }
    }

    @Override
    public ActionResult interact(PlayerEntity player, Hand hand) {

        World world = player.getWorld();
        BlockHitResult blockHitResult = new BlockHitResult(player.getPos(), Direction.UP, this.getUseRelayBlockPos(), false);
        ItemStack itemStack = player.getStackInHand(hand);

        if (!Objects.equals(this.getUseRelayBlockPos(), new BlockPos(0, -100, 0)) && player instanceof ServerPlayerEntity serverPlayerEntity) {
            serverPlayerEntity.interactionManager.interactBlock(serverPlayerEntity, world, itemStack, hand, blockHitResult);
            return ActionResult.SUCCESS;
        }
        return ActionResult.PASS;
    }

    @Override
    public void onDeath(DamageSource damageSource) {
        super.onDeath(damageSource);
        if (this.getWorld() instanceof ServerWorld serverWorld && this.getBoundSpawnerBlockPos() != null) {
            BlockEntity blockEntity = serverWorld.getBlockEntity(this.getBoundSpawnerBlockPos());
            if (blockEntity instanceof TriggeredSpawnerBlockEntity triggeredSpawnerBlockEntity) {
                triggeredSpawnerBlockEntity.onBoundEntityKilled();
            }
        }
    }

    public String getAnimationIdentifierString() {
        return this.dataTracker.get(ANIMATION_IDENTIFIER_STRING);
    }
    public void setAnimationIdentifierString(String animationIdentifierString) {
        this.dataTracker.set(ANIMATION_IDENTIFIER_STRING, animationIdentifierString);
    }

    public BlockPos getBoundSpawnerBlockPos() {
        return this.dataTracker.get(BOUND_SPAWNER_BLOCK_POS);
    }
    public void setBoundSpawnerBlockPos(BlockPos boundSpawnerBlockPos) {
        this.dataTracker.set(BOUND_SPAWNER_BLOCK_POS, boundSpawnerBlockPos);
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

    public BlockPos getUseRelayBlockPos() {
        return this.dataTracker.get(USE_RELAY_BLOCK_POS);
    }
    public void setUseRelayBlockPos(BlockPos relayBlockPos) {
        this.dataTracker.set(USE_RELAY_BLOCK_POS, relayBlockPos);
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
    public Iterable<ItemStack> getArmorItems() {
        return List.of();
    }

    @Override
    public ItemStack getEquippedStack(EquipmentSlot slot) {
        return ItemStack.EMPTY;
    }

    @Override
    public void equipStack(EquipmentSlot slot, ItemStack stack) {

    }

    @Override
    public Arm getMainArm() {
        return Arm.RIGHT;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {

    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    static {

        ANIMATION_IDENTIFIER_STRING = DataTracker.registerData(SpawnerBoundEntity.class, TrackedDataHandlerRegistry.STRING);
        BOUND_SPAWNER_BLOCK_POS = DataTracker.registerData(SpawnerBoundEntity.class, TrackedDataHandlerRegistry.BLOCK_POS);
        BOUNDING_BOX_HEIGHT = DataTracker.registerData(SpawnerBoundEntity.class, TrackedDataHandlerRegistry.FLOAT);
        BOUNDING_BOX_WIDTH = DataTracker.registerData(SpawnerBoundEntity.class, TrackedDataHandlerRegistry.FLOAT);
        MODEL_IDENTIFIER_STRING = DataTracker.registerData(SpawnerBoundEntity.class, TrackedDataHandlerRegistry.STRING);
        USE_RELAY_BLOCK_POS = DataTracker.registerData(SpawnerBoundEntity.class, TrackedDataHandlerRegistry.BLOCK_POS);
        TEXTURE_IDENTIFIER_STRING = DataTracker.registerData(SpawnerBoundEntity.class, TrackedDataHandlerRegistry.STRING);
    }
}
