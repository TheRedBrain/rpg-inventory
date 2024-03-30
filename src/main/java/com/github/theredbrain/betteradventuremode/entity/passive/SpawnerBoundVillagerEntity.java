package com.github.theredbrain.betteradventuremode.entity.passive;

import com.github.theredbrain.betteradventuremode.entity.mob.SpawnerBoundMobEntity;
import com.mojang.logging.LogUtils;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.Dynamic;
import net.minecraft.entity.*;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtOps;
import net.minecraft.village.VillagerData;
import net.minecraft.village.VillagerDataContainer;
import net.minecraft.village.VillagerProfession;
import net.minecraft.village.VillagerType;
import net.minecraft.world.World;
import org.slf4j.Logger;

import java.util.Objects;

public class SpawnerBoundVillagerEntity extends SpawnerBoundMobEntity implements VillagerDataContainer {
    private static final Logger LOGGER = LogUtils.getLogger();

    private static final TrackedData<VillagerData> VILLAGER_DATA;

    public SpawnerBoundVillagerEntity(EntityType<? extends MobEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(VILLAGER_DATA, new VillagerData(VillagerType.PLAINS, VillagerProfession.NONE, 1));
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);

        DataResult<NbtElement> var10000 = VillagerData.CODEC.encodeStart(NbtOps.INSTANCE, this.getVillagerData());
        Logger var10001 = LOGGER;
        Objects.requireNonNull(var10001);
        var10000.resultOrPartial(var10001::error).ifPresent((nbtElement) -> {
            nbt.put("VillagerData", nbtElement);
        });

    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        if (nbt.contains("VillagerData", NbtElement.COMPOUND_TYPE)) {
            DataResult<VillagerData> dataResult = VillagerData.CODEC.parse(new Dynamic(NbtOps.INSTANCE, nbt.get("VillagerData")));
            Logger var10001 = LOGGER;
            Objects.requireNonNull(var10001);
            dataResult.resultOrPartial(var10001::error).ifPresent(this::setVillagerData);
        }
    }

    public void setAnimationIdentifierString(String animationIdentifierString) {}

    public void setBoundingBoxHeight(float boundingBoxHeight) {}

    public void setBoundingBoxWidth(float boundingBoxWidth) {}

    public void setModelIdentifierString(String modelIdentifierString) {}

    public void setVillagerData(VillagerData villagerData) {
        this.dataTracker.set(VILLAGER_DATA, villagerData);
    }

    public VillagerData getVillagerData() {
        return (VillagerData)this.dataTracker.get(VILLAGER_DATA);
    }

    public void setTextureIdentifierString(String textureIdentifierString) {}

    static {
        VILLAGER_DATA = DataTracker.registerData(SpawnerBoundVillagerEntity.class, TrackedDataHandlerRegistry.VILLAGER_DATA);
    }
}
