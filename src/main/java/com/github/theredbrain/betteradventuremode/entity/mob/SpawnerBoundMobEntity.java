package com.github.theredbrain.betteradventuremode.entity.mob;

import com.github.theredbrain.betteradventuremode.block.entity.TriggeredSpawnerBlockEntity;
import com.github.theredbrain.betteradventuremode.entity.IsSpawnerBound;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.*;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import java.util.Objects;

public abstract class SpawnerBoundMobEntity extends MobEntity implements IsSpawnerBound {
    public static final TrackedData<BlockPos> BOUND_SPAWNER_BLOCK_POS;
    public static final TrackedData<BlockPos> USE_RELAY_BLOCK_POS;

    public SpawnerBoundMobEntity(EntityType<? extends MobEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(BOUND_SPAWNER_BLOCK_POS, new BlockPos(0, -100, 0));
        this.dataTracker.startTracking(USE_RELAY_BLOCK_POS, new BlockPos(0, -100, 0));
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);

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
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);

        if (nbt.contains("bound_spawner_block_pos_x") || nbt.contains("bound_spawner_block_pos_y") || nbt.contains("bound_spawner_block_pos_z")) {
            this.setBoundSpawnerBlockPos(new BlockPos(
                    nbt.getInt("bound_spawner_block_pos_x"),
                    nbt.getInt("bound_spawner_block_pos_y"),
                    nbt.getInt("bound_spawner_block_pos_z")
            ));
        }

        if (nbt.contains("use_relay_block_pos_x") || nbt.contains("use_relay_block_pos_y") || nbt.contains("use_relay_block_pos_z")) {
            this.setUseRelayBlockPos(new BlockPos(
                    nbt.getInt("use_relay_block_pos_x"),
                    nbt.getInt("use_relay_block_pos_y"),
                    nbt.getInt("use_relay_block_pos_z")
            ));
        }
    }

    @Override
    protected ActionResult interactMob(PlayerEntity player, Hand hand) {

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

    public void setAnimationIdentifierString(String animationIdentifierString) {}
    public BlockPos getBoundSpawnerBlockPos() {
        return this.dataTracker.get(BOUND_SPAWNER_BLOCK_POS);
    }
    @Override
    public void setBoundSpawnerBlockPos(BlockPos boundSpawnerBlockPos) {
        this.dataTracker.set(BOUND_SPAWNER_BLOCK_POS, boundSpawnerBlockPos);
    }
    @Override
    public void setBoundingBoxHeight(float boundingBoxHeight) {}
    @Override
    public void setBoundingBoxWidth(float boundingBoxWidth) {}
    @Override
    public void setModelIdentifierString(String modelIdentifierString) {}
    public BlockPos getUseRelayBlockPos() {
        return this.dataTracker.get(USE_RELAY_BLOCK_POS);
    }
    @Override
    public void setUseRelayBlockPos(BlockPos relayBlockPos) {
        this.dataTracker.set(USE_RELAY_BLOCK_POS, relayBlockPos);
    }
    @Override
    public void setTextureIdentifierString(String textureIdentifierString) {}
    static {
        BOUND_SPAWNER_BLOCK_POS = DataTracker.registerData(SpawnerBoundMobEntity.class, TrackedDataHandlerRegistry.BLOCK_POS);
        USE_RELAY_BLOCK_POS = DataTracker.registerData(SpawnerBoundMobEntity.class, TrackedDataHandlerRegistry.BLOCK_POS);
    }
}
