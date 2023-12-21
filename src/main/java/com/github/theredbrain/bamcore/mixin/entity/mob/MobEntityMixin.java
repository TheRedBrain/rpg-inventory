package com.github.theredbrain.bamcore.mixin.entity.mob;

import com.github.theredbrain.bamcore.block.entity.TriggeredSpawnerBlockEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.EntityData;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MobEntity.class)
public abstract class MobEntityMixin extends LivingEntity {

    @Unique
    @Nullable
    private BlockPos bamcore$boundSpawnerBlockPos;

    protected MobEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "writeCustomDataToNbt", at = @At("TAIL"))
    public void bamcore$writeCustomDataToNbt(NbtCompound nbt, CallbackInfo ci) {
        if (this.bamcore$boundSpawnerBlockPos != null) {
            nbt.putInt("boundSpawnerBlockPosX", this.bamcore$boundSpawnerBlockPos.getX());
            nbt.putInt("boundSpawnerBlockPosY", this.bamcore$boundSpawnerBlockPos.getY());
            nbt.putInt("boundSpawnerBlockPosZ", this.bamcore$boundSpawnerBlockPos.getZ());
        }
    }

    @Inject(method = "readCustomDataFromNbt", at = @At("TAIL"))
    public void bamcore$readCustomDataFromNbt(NbtCompound nbt, CallbackInfo ci) {
        if (nbt.contains("boundSpawnerBlockPosX") && nbt.contains("boundSpawnerBlockPosY") && nbt.contains("boundSpawnerBlockPosZ")) {
            this.bamcore$boundSpawnerBlockPos = new BlockPos(
                    nbt.getInt("boundSpawnerBlockPosX"),
                    nbt.getInt("boundSpawnerBlockPosY"),
                    nbt.getInt("boundSpawnerBlockPosZ")
            );
        }
    }

    @Inject(method = "initialize", at = @At("RETURN"))
    public void bamcore$initialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, EntityData entityData, NbtCompound entityNbt, CallbackInfoReturnable<EntityData> cir) {
        if (entityNbt != null && entityNbt.contains("boundSpawnerBlockPosX") && entityNbt.contains("boundSpawnerBlockPosY") && entityNbt.contains("boundSpawnerBlockPosZ")) {
            this.bamcore$boundSpawnerBlockPos = new BlockPos(
                    entityNbt.getInt("boundSpawnerBlockPosX"),
                    entityNbt.getInt("boundSpawnerBlockPosY"),
                    entityNbt.getInt("boundSpawnerBlockPosZ")
            );
        }
    }

    @Override
    public void onDeath(DamageSource damageSource) {
        super.onDeath(damageSource);
        if (this.getWorld() instanceof ServerWorld serverWorld && this.bamcore$boundSpawnerBlockPos != null) {
            BlockEntity blockEntity = serverWorld.getBlockEntity(this.bamcore$boundSpawnerBlockPos);
            if (blockEntity instanceof TriggeredSpawnerBlockEntity triggeredSpawnerBlockEntity) {
                triggeredSpawnerBlockEntity.onBoundEntityKilled();
            }
        }
    }
}
