package com.github.theredbrain.bamcore.block.entity;

import com.github.theredbrain.bamcore.BetterAdventureModeCore;
import com.github.theredbrain.bamcore.api.util.BlockRotationUtils;
import com.github.theredbrain.bamcore.block.Resetable;
import com.github.theredbrain.bamcore.block.RotatedBlockWithEntity;
import com.github.theredbrain.bamcore.block.Triggerable;
import com.github.theredbrain.bamcore.entity.player.DuckPlayerEntityMixin;
import com.github.theredbrain.bamcore.registry.EntityRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.registry.Registries;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.WorldEvents;
import net.minecraft.world.event.GameEvent;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

public class TriggeredSpawnerBlockEntity extends RotatedBlockEntity implements Triggerable, Resetable {

    private BlockPos entitySpawnPositionOffset = new BlockPos(0, 1, 0);
    private BlockPos triggeredBlockPositionOffset = new BlockPos(0, 1, 0);
    @Nullable
    private UUID boundEntityUuid = null;

    private SpawningMode spawningMode = SpawningMode.ONCE;

    private boolean triggered = false;
    private NbtCompound entityTypeCompound;

    public TriggeredSpawnerBlockEntity(BlockPos pos, BlockState state) {
        super(EntityRegistry.TRIGGERED_SPAWNER_BLOCK_ENTITY, pos, state);
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {

        super.writeNbt(nbt);

        nbt.putInt("entitySpawnPositionOffsetX", this.entitySpawnPositionOffset.getX());
        nbt.putInt("entitySpawnPositionOffsetY", this.entitySpawnPositionOffset.getY());
        nbt.putInt("entitySpawnPositionOffsetZ", this.entitySpawnPositionOffset.getZ());

        nbt.putInt("triggeredBlockPositionOffsetX", this.triggeredBlockPositionOffset.getX());
        nbt.putInt("triggeredBlockPositionOffsetY", this.triggeredBlockPositionOffset.getY());
        nbt.putInt("triggeredBlockPositionOffsetZ", this.triggeredBlockPositionOffset.getZ());

        nbt.putString("spawningMode", this.spawningMode.asString());

        if (this.entityTypeCompound != null) {
            nbt.put("EntityTypeCompound", this.entityTypeCompound);
        }
    }

    @Override
    public void readNbt(NbtCompound nbt) {

        super.readNbt(nbt);

        this.entitySpawnPositionOffset = new BlockPos(
                MathHelper.clamp(nbt.getInt("entitySpawnPositionOffsetX"), -48, 48),
                MathHelper.clamp(nbt.getInt("entitySpawnPositionOffsetY"), -48, 48),
                MathHelper.clamp(nbt.getInt("entitySpawnPositionOffsetZ"), -48, 48)
        );

        this.triggeredBlockPositionOffset = new BlockPos(
                MathHelper.clamp(nbt.getInt("triggeredBlockPositionOffsetX"), -48, 48),
                MathHelper.clamp(nbt.getInt("triggeredBlockPositionOffsetY"), -48, 48),
                MathHelper.clamp(nbt.getInt("triggeredBlockPositionOffsetZ"), -48, 48)
        );

        this.spawningMode = SpawningMode.byName(nbt.getString("spawningMode")).orElseGet(() -> SpawningMode.ONCE);

        if (nbt.contains("EntityTypeCompound", NbtElement.COMPOUND_TYPE)) {
            this.entityTypeCompound = nbt.getCompound("EntityTypeCompound");
        } else {
            this.entityTypeCompound = new NbtCompound();
        }
    }

    public BlockEntityUpdateS2CPacket toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    @Override
    public NbtCompound toInitialChunkDataNbt() {
        return this.createNbt();
    }

    public boolean openScreen(PlayerEntity player) {
        if (!player.isCreativeLevelTwoOp()) {
            return false;
        }
        if (player.getEntityWorld().isClient) {
            ((DuckPlayerEntityMixin)player).bamcore$openTriggeredSpawnerBlockScreen(this);
        }
        return true;
    }

    //region getter/setter
    public BlockPos getEntitySpawnPositionOffset() {
        return this.entitySpawnPositionOffset;
    }

    // TODO check if input is valid
    public boolean setEntitySpawnPositionOffset(BlockPos entitySpawnPositionOffset) {
        this.entitySpawnPositionOffset = entitySpawnPositionOffset;
        return true;
    }

    public BlockPos getTriggeredBlockPositionOffset() {
        return this.triggeredBlockPositionOffset;
    }

    // TODO check if input is valid
    public boolean setTriggeredBlockPositionOffset(BlockPos triggeredBlockPositionOffset) {
        this.triggeredBlockPositionOffset = triggeredBlockPositionOffset;
        return true;
    }

    public TriggeredSpawnerBlockEntity.SpawningMode getSpawningMode() {
        return this.spawningMode;
    }

    // TODO check if input is valid
    public boolean setSpawningMode(TriggeredSpawnerBlockEntity.SpawningMode spawningMode) {
        this.spawningMode = spawningMode;
        return true;
    }

    public String getEntityTypeId() {
        if (this.entityTypeCompound != null && this.entityTypeCompound.contains("id")) {
            return this.entityTypeCompound.getString("id");
        }
        return "";
    }

    public boolean setEntityType(String entityTypeId) {
        Optional<EntityType<?>> optional = EntityType.get(entityTypeId);
        if (optional.isPresent()) {
            EntityType<?> entityType = optional.get();
            this.entityTypeCompound.putString("id", Registries.ENTITY_TYPE.getId(entityType).toString());
            return true;
        }
        return false;
    }
    //endregion getter/setter

    @Override
    protected void onRotate(BlockState state) {
        if (state.getBlock() instanceof RotatedBlockWithEntity) {
            if (state.get(RotatedBlockWithEntity.ROTATED) != this.rotated) {
                BlockRotation blockRotation = BlockRotationUtils.calculateRotationFromDifferentRotatedStates(state.get(RotatedBlockWithEntity.ROTATED), this.rotated);
                this.entitySpawnPositionOffset = BlockRotationUtils.rotateOffsetBlockPos(this.entitySpawnPositionOffset, blockRotation);
                this.triggeredBlockPositionOffset = BlockRotationUtils.rotateOffsetBlockPos(this.triggeredBlockPositionOffset, blockRotation);

                this.rotated = state.get(RotatedBlockWithEntity.ROTATED);
            }
            if (state.get(RotatedBlockWithEntity.X_MIRRORED) != this.x_mirrored) {
                this.entitySpawnPositionOffset = BlockRotationUtils.mirrorOffsetBlockPos(this.entitySpawnPositionOffset, BlockMirror.FRONT_BACK);
                this.triggeredBlockPositionOffset = BlockRotationUtils.mirrorOffsetBlockPos(this.triggeredBlockPositionOffset, BlockMirror.FRONT_BACK);

                this.x_mirrored = state.get(RotatedBlockWithEntity.X_MIRRORED);
            }
            if (state.get(RotatedBlockWithEntity.Z_MIRRORED) != this.z_mirrored) {
                this.entitySpawnPositionOffset = BlockRotationUtils.mirrorOffsetBlockPos(this.entitySpawnPositionOffset, BlockMirror.LEFT_RIGHT);
                this.triggeredBlockPositionOffset = BlockRotationUtils.mirrorOffsetBlockPos(this.triggeredBlockPositionOffset, BlockMirror.LEFT_RIGHT);

                this.z_mirrored = state.get(RotatedBlockWithEntity.Z_MIRRORED);
            }
        }
    }

    @Override
    public void reset() {
        if (this.triggered) {
            this.triggered = false;
        }
        if (this.spawningMode == SpawningMode.BOUND && this.boundEntityUuid != null && this.world instanceof ServerWorld serverWorld) {
            Entity entity = serverWorld.getEntity(this.boundEntityUuid);
            if (entity != null) {
                entity.discard();
            }
        }
    }

    @Override
    public void trigger() {
        if (this.spawningMode == SpawningMode.ONCE && !this.triggered) {
            if (this.spawnEntity()) {
                this.triggered = true;
            }
        } else if (this.spawningMode == SpawningMode.CONTINUOUS) {
            if (this.spawnEntity()) {
                this.triggered = true;
            }
        } else if (this.spawningMode == SpawningMode.BOUND && !this.triggered) {
            if (this.spawnBoundEntity()) {
                this.triggered = true;
            }
        }
    }

    public void onBoundEntityKilled() {
        if (this.world != null) {
            BlockEntity blockEntity = world.getBlockEntity(new BlockPos(this.pos.getX() + this.triggeredBlockPositionOffset.getX(), this.pos.getY() + this.triggeredBlockPositionOffset.getY(), this.pos.getZ() + this.triggeredBlockPositionOffset.getZ()));
            if (blockEntity != null) {
                BetterAdventureModeCore.info("blockEntity: " + blockEntity.getType().toString());
            } else {
                BetterAdventureModeCore.info("blockEntity == null");
            }
            if (blockEntity instanceof Triggerable triggerable) {
                triggerable.trigger();
            }
        }
    }

    private boolean spawnEntity() {
        if (this.world instanceof ServerWorld serverWorld) {
            Optional<EntityType<?>> optional = EntityType.fromNbt(this.entityTypeCompound);
            if (optional.isEmpty()) {
                return false;
            }
            double d = (double)this.pos.getX() + this.entitySpawnPositionOffset.getX() + 0.5;
            double e = (double)this.pos.getY() + this.entitySpawnPositionOffset.getY();
            double f = (double)this.pos.getZ() + this.entitySpawnPositionOffset.getZ() + 0.5;
            if (!serverWorld.isSpaceEmpty(optional.get().createSimpleBoundingBox(d, e, f))) return false;
            BlockPos blockPos = BlockPos.ofFloored(d, e, f);
            Entity entity2 = EntityType.loadEntityWithPassengers(this.entityTypeCompound, world, entity -> {
                entity.refreshPositionAndAngles(d, e, f, entity.getYaw(), entity.getPitch());
                return entity;
            });
            if (entity2 == null) {
                return false;
            }
            entity2.refreshPositionAndAngles(entity2.getX(), entity2.getY(), entity2.getZ(), 0.0f, 0.0f);
            if (entity2 instanceof MobEntity) {
                if (this.entityTypeCompound.contains("id", NbtElement.STRING_TYPE)) {
                    ((MobEntity)entity2).initialize(serverWorld, serverWorld.getLocalDifficulty(entity2.getBlockPos()), SpawnReason.SPAWNER, null, null);
                }
            }
            if (!serverWorld.spawnNewEntityAndPassengers(entity2)) {
                return false;
            }
            serverWorld.syncWorldEvent(WorldEvents.SPAWNER_SPAWNS_MOB, this.pos, 0);
            serverWorld.emitGameEvent(entity2, GameEvent.ENTITY_PLACE, blockPos);
            if (entity2 instanceof MobEntity) {
                ((MobEntity)entity2).playSpawnEffects();
            }
            return true;
        }
        return false;
    }

    private boolean spawnBoundEntity() {
        if (this.world instanceof ServerWorld serverWorld) {
            Optional<EntityType<?>> optional = EntityType.fromNbt(this.entityTypeCompound);
            if (optional.isEmpty()) {
                return false;
            }
            double d = (double)this.pos.getX() + this.entitySpawnPositionOffset.getX() + 0.5;
            double e = (double)this.pos.getY() + this.entitySpawnPositionOffset.getY();
            double f = (double)this.pos.getZ() + this.entitySpawnPositionOffset.getZ() + 0.5;
            if (!serverWorld.isSpaceEmpty(optional.get().createSimpleBoundingBox(d, e, f))) return false;
            BlockPos blockPos = BlockPos.ofFloored(d, e, f);
            Entity entity2 = EntityType.loadEntityWithPassengers(this.entityTypeCompound, world, entity -> {
                entity.refreshPositionAndAngles(d, e, f, entity.getYaw(), entity.getPitch());
                return entity;
            });
            if (entity2 == null) {
                return false;
            }
            entity2.refreshPositionAndAngles(entity2.getX(), entity2.getY(), entity2.getZ(), 0.0f, 0.0f);
            if (entity2 instanceof MobEntity) {
                if (this.entityTypeCompound.contains("id", NbtElement.STRING_TYPE)) {
                    NbtCompound entityNbt = new NbtCompound();
                    entityNbt.putInt("boundSpawnerBlockPosX", this.pos.getX());
                    entityNbt.putInt("boundSpawnerBlockPosY", this.pos.getY());
                    entityNbt.putInt("boundSpawnerBlockPosZ", this.pos.getZ());
                    ((MobEntity)entity2).initialize(serverWorld, serverWorld.getLocalDifficulty(entity2.getBlockPos()), SpawnReason.SPAWNER, null, entityNbt);
                }
            }
            if (!serverWorld.spawnNewEntityAndPassengers(entity2)) {
                return false;
            }
            serverWorld.syncWorldEvent(WorldEvents.SPAWNER_SPAWNS_MOB, this.pos, 0);
            serverWorld.emitGameEvent(entity2, GameEvent.ENTITY_PLACE, blockPos);
            if (entity2 instanceof MobEntity mobEntity) {
                mobEntity.setPersistent();
                this.boundEntityUuid = mobEntity.getUuid();
            }
            return true;
        }
        return false;
    }

    public static enum SpawningMode implements StringIdentifiable
    {
        BOUND("bound"),
        CONTINUOUS("continuous"),
        ONCE("once");

        private final String name;

        private SpawningMode(String name) {
            this.name = name;
        }

        @Override
        public String asString() {
            return this.name;
        }

        public static Optional<TriggeredSpawnerBlockEntity.SpawningMode> byName(String name) {
            return Arrays.stream(TriggeredSpawnerBlockEntity.SpawningMode.values()).filter(spawningMode -> spawningMode.asString().equals(name)).findFirst();
        }

        public Text asText() {
            return Text.translatable("gui.triggered_spawner_block.spawning_mode." + this.name);
        }
    }
}
