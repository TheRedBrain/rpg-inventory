package com.github.theredbrain.betteradventuremode.mixin.entity.mob;

import com.github.theredbrain.betteradventuremode.block.entity.TriggeredSpawnerBlockEntity;
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
    private BlockPos betteradventuremode$boundSpawnerBlockPos;

    protected MobEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "writeCustomDataToNbt", at = @At("TAIL"))
    public void betteradventuremode$writeCustomDataToNbt(NbtCompound nbt, CallbackInfo ci) {
        if (this.betteradventuremode$boundSpawnerBlockPos != null) {
            nbt.putInt("betteradventuremode$boundSpawnerBlockPosX", this.betteradventuremode$boundSpawnerBlockPos.getX());
            nbt.putInt("betteradventuremode$boundSpawnerBlockPosY", this.betteradventuremode$boundSpawnerBlockPos.getY());
            nbt.putInt("betteradventuremode$boundSpawnerBlockPosZ", this.betteradventuremode$boundSpawnerBlockPos.getZ());
        }
    }

    @Inject(method = "readCustomDataFromNbt", at = @At("TAIL"))
    public void betteradventuremode$readCustomDataFromNbt(NbtCompound nbt, CallbackInfo ci) {
        if (nbt.contains("betteradventuremode$boundSpawnerBlockPosX") && nbt.contains("betteradventuremode$boundSpawnerBlockPosY") && nbt.contains("betteradventuremode$boundSpawnerBlockPosZ")) {
            this.betteradventuremode$boundSpawnerBlockPos = new BlockPos(
                    nbt.getInt("betteradventuremode$boundSpawnerBlockPosX"),
                    nbt.getInt("betteradventuremode$boundSpawnerBlockPosY"),
                    nbt.getInt("betteradventuremode$boundSpawnerBlockPosZ")
            );
        }
    }

    @Inject(method = "initialize", at = @At("RETURN"))
    public void betteradventuremode$initialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, EntityData entityData, NbtCompound entityNbt, CallbackInfoReturnable<EntityData> cir) {
        if (entityNbt != null && entityNbt.contains("betteradventuremode$boundSpawnerBlockPosX") && entityNbt.contains("betteradventuremode$boundSpawnerBlockPosY") && entityNbt.contains("betteradventuremode$boundSpawnerBlockPosZ")) {
            this.betteradventuremode$boundSpawnerBlockPos = new BlockPos(
                    entityNbt.getInt("betteradventuremode$boundSpawnerBlockPosX"),
                    entityNbt.getInt("betteradventuremode$boundSpawnerBlockPosY"),
                    entityNbt.getInt("betteradventuremode$boundSpawnerBlockPosZ")
            );
        }
    }

    @Override
    public void onDeath(DamageSource damageSource) {
        super.onDeath(damageSource);
        if (this.getWorld() instanceof ServerWorld serverWorld && this.betteradventuremode$boundSpawnerBlockPos != null) {
            BlockEntity blockEntity = serverWorld.getBlockEntity(this.betteradventuremode$boundSpawnerBlockPos);
            if (blockEntity instanceof TriggeredSpawnerBlockEntity triggeredSpawnerBlockEntity) {
                triggeredSpawnerBlockEntity.onBoundEntityKilled();
            }
        }
    }
}
