package com.github.theredbrain.betteradventuremode.entity.mob;

import com.github.theredbrain.betteradventuremode.BetterAdventureMode;
import mod.azure.azurelib.common.api.common.animatable.GeoEntity;
import mod.azure.azurelib.common.internal.common.core.animatable.instance.AnimatableInstanceCache;
import mod.azure.azurelib.common.internal.common.core.animation.AnimatableManager;
import mod.azure.azurelib.common.internal.common.util.AzureLibUtil;
import net.minecraft.entity.*;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class SpawnerBoundEntity extends MobEntity implements GeoEntity {
    private final AnimatableInstanceCache cache = AzureLibUtil.createInstanceCache(this);

    Identifier spawnerBoundEntityModelIdentifier = BetterAdventureMode.identifier("spawner_bound_entity/default_spawner_bound_entity");
    Identifier spawnerBoundEntityTextureIdentifier = BetterAdventureMode.identifier("spawner_bound_entity/default_spawner_bound_entity");;
    Identifier spawnerBoundEntityAnimationsIdentifier = BetterAdventureMode.identifier("spawner_bound_entity/default_spawner_bound_entity");;
    double spawnerBoundEntityBoundingBoxHeight = 1.8;
    double spawnerBoundEntityBoundingBoxWidth = 0.8;
    double spawnerBoundEntityEyeHeight = 1.65;

    public SpawnerBoundEntity(EntityType<? extends MobEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        if (this.spawnerBoundEntityModelIdentifier != null) {
            nbt.putString("spawnerBoundEntityModelIdentifier", this.spawnerBoundEntityModelIdentifier.toString());
        }
        if (this.spawnerBoundEntityTextureIdentifier != null) {
            nbt.putString("spawnerBoundEntityTextureIdentifier", this.spawnerBoundEntityTextureIdentifier.toString());
        }
        if (this.spawnerBoundEntityAnimationsIdentifier != null) {
            nbt.putString("spawnerBoundEntityAnimationsIdentifier", this.spawnerBoundEntityAnimationsIdentifier.toString());
        }
        nbt.putDouble("spawnerBoundEntityBoundingBoxHeight", this.spawnerBoundEntityBoundingBoxHeight > 0.0 ? this.spawnerBoundEntityBoundingBoxHeight : 1.8);
        nbt.putDouble("spawnerBoundEntityBoundingBoxWidth", this.spawnerBoundEntityBoundingBoxWidth > 0.0 ? this.spawnerBoundEntityBoundingBoxWidth : 0.8);
//        nbt.putDouble("spawnerBoundEntityBoundingBoxWidth", this.spawnerBoundEntityBoundingBoxWidth > 0.0 ? this.spawnerBoundEntityBoundingBoxWidth : 0.8);

//        nbt.putString("variant", Registries.CAT_VARIANT.getId(this.getVariant()).toString());
//        nbt.putByte("CollarColor", (byte)this.getCollarColor().getId());
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);

        if (nbt.contains("spawnerBoundEntityModelIdentifier")) {
            Identifier spawnerBoundEntityModelIdentifier = Identifier.tryParse(nbt.getString("spawnerBoundEntityModelIdentifier"));
            if (spawnerBoundEntityModelIdentifier != null) {
                this.spawnerBoundEntityModelIdentifier = spawnerBoundEntityModelIdentifier;
            }
        }

        if (nbt.contains("spawnerBoundEntityTextureIdentifier")) {
            Identifier spawnerBoundEntityTextureIdentifier = Identifier.tryParse(nbt.getString("spawnerBoundEntityTextureIdentifier"));
            if (spawnerBoundEntityTextureIdentifier != null) {
                this.spawnerBoundEntityTextureIdentifier = spawnerBoundEntityTextureIdentifier;
            }
        }

        if (nbt.contains("spawnerBoundEntityAnimationsIdentifier")) {
            Identifier spawnerBoundEntityAnimationsIdentifier = Identifier.tryParse(nbt.getString("spawnerBoundEntityAnimationsIdentifier"));
            if (spawnerBoundEntityAnimationsIdentifier != null) {
                this.spawnerBoundEntityAnimationsIdentifier = spawnerBoundEntityAnimationsIdentifier;
            }
        }

        if (nbt.contains("spawnerBoundEntityBoundingBoxHeight")) {
            this.spawnerBoundEntityBoundingBoxHeight = nbt.getDouble("spawnerBoundEntityBoundingBoxHeight");
        }

        if (nbt.contains("spawnerBoundEntityBoundingBoxWidth")) {
            this.spawnerBoundEntityBoundingBoxWidth = nbt.getDouble("spawnerBoundEntityBoundingBoxWidth");
        }
    }

    @Override
    @Nullable
    public EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, @Nullable EntityData entityData, @Nullable NbtCompound entityNbt) {
        if (entityNbt != null) {
            if (entityNbt.contains("spawnerBoundEntityName")) {
                this.setCustomName(Text.translatable(entityNbt.getString("spawnerBoundEntityName")));
            }

            if (entityNbt.contains("spawnerBoundEntityModelIdentifier")) {
                Identifier spawnerBoundEntityModelIdentifier = Identifier.tryParse(entityNbt.getString("spawnerBoundEntityModelIdentifier"));
                if (spawnerBoundEntityModelIdentifier != null) {
                    this.spawnerBoundEntityModelIdentifier = spawnerBoundEntityModelIdentifier;
                }
            }

            if (entityNbt.contains("spawnerBoundEntityTextureIdentifier")) {
                Identifier spawnerBoundEntityTextureIdentifier = Identifier.tryParse(entityNbt.getString("spawnerBoundEntityTextureIdentifier"));
                if (spawnerBoundEntityTextureIdentifier != null) {
                    this.spawnerBoundEntityTextureIdentifier = spawnerBoundEntityTextureIdentifier;
                }
            }

            if (entityNbt.contains("spawnerBoundEntityAnimationsIdentifier")) {
                Identifier spawnerBoundEntityAnimationsIdentifier = Identifier.tryParse(entityNbt.getString("spawnerBoundEntityAnimationsIdentifier"));
                if (spawnerBoundEntityAnimationsIdentifier != null) {
                    this.spawnerBoundEntityAnimationsIdentifier = spawnerBoundEntityAnimationsIdentifier;
                }
            }

            if (entityNbt.contains("spawnerBoundEntityBoundingBoxHeight")) {
                this.spawnerBoundEntityBoundingBoxHeight = entityNbt.getDouble("spawnerBoundEntityBoundingBoxHeight");
            }

            if (entityNbt.contains("spawnerBoundEntityBoundingBoxWidth")) {
                this.spawnerBoundEntityBoundingBoxWidth = entityNbt.getDouble("spawnerBoundEntityBoundingBoxWidth");
            }
        }
        return super.initialize(world, difficulty, spawnReason, entityData, entityNbt);
    }

    public Identifier getSpawnerBoundEntityModelIdentifier() {
        return spawnerBoundEntityModelIdentifier;
    }

    public Identifier getSpawnerBoundEntityTextureIdentifier() {
        return spawnerBoundEntityTextureIdentifier;
    }

    public Identifier getSpawnerBoundEntityAnimationsIdentifier() {
        return spawnerBoundEntityAnimationsIdentifier;
    }

    @Override
    public EntityDimensions getDimensions(EntityPose pose) {
        return EntityDimensions.fixed((float) this.spawnerBoundEntityBoundingBoxWidth, (float) this.spawnerBoundEntityBoundingBoxHeight);
    }

    @Override
    protected float getActiveEyeHeight(EntityPose pose, EntityDimensions dimensions) {
        return (float) this.spawnerBoundEntityEyeHeight;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {

    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }
}
