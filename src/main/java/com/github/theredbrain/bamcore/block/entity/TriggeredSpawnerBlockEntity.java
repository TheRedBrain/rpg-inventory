package com.github.theredbrain.bamcore.block.entity;

import com.github.theredbrain.bamcore.BetterAdventureModeCore;
import com.github.theredbrain.bamcore.api.util.BlockRotationUtils;
import com.github.theredbrain.bamcore.block.Resetable;
import com.github.theredbrain.bamcore.block.RotatedBlockWithEntity;
import com.github.theredbrain.bamcore.block.Triggerable;
import com.github.theredbrain.bamcore.entity.player.DuckPlayerEntityMixin;
import com.github.theredbrain.bamcore.registry.EntityRegistry;
import net.minecraft.block.BlockState;
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

import java.util.Arrays;
import java.util.Optional;

public class TriggeredSpawnerBlockEntity extends RotatedBlockEntity implements Triggerable, Resetable {

    private BlockPos entitySpawnPositionOffset = new BlockPos(0, 1, 0);
    private double entitySpawnOrientationYaw = 0.0;
    private double entitySpawnOrientationPitch = 0.0;

    private SpawningMode spawningMode = SpawningMode.TRIGGERED;

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

        nbt.putDouble("entitySpawnOrientationYaw", this.entitySpawnOrientationYaw);
        nbt.putDouble("entitySpawnOrientationPitch", this.entitySpawnOrientationPitch);

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
        this.entitySpawnOrientationYaw = nbt.getDouble("entitySpawnOrientationYaw");
        this.entitySpawnOrientationPitch = nbt.getDouble("entitySpawnOrientationPitch");

        this.spawningMode = SpawningMode.byName(nbt.getString("spawningMode")).orElseGet(() -> SpawningMode.TRIGGERED);

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

    public double getEntitySpawnOrientationYaw() {
        return entitySpawnOrientationYaw;
    }

    // TODO check if input is valid
    public boolean setEntitySpawnOrientationYaw(double entitySpawnOrientationYaw) {
        this.entitySpawnOrientationYaw = MathHelper.clamp(entitySpawnOrientationYaw, -180.0f, 180.0f);
        return true;
    }

    public double getEntitySpawnOrientationPitch() {
        return entitySpawnOrientationPitch;
    }

    // TODO check if input is valid
    public boolean setEntitySpawnOrientationPitch(double entitySpawnOrientationPitch) {
        this.entitySpawnOrientationPitch = MathHelper.clamp(entitySpawnOrientationPitch, -90.0f, 90.0f);
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

                this.entitySpawnOrientationYaw = BlockRotationUtils.rotateYaw(this.entitySpawnOrientationYaw, blockRotation);

                this.rotated = state.get(RotatedBlockWithEntity.ROTATED);
            }
            if (state.get(RotatedBlockWithEntity.X_MIRRORED) != this.x_mirrored) {
                this.entitySpawnPositionOffset = BlockRotationUtils.mirrorOffsetBlockPos(this.entitySpawnPositionOffset, BlockMirror.FRONT_BACK);

                this.entitySpawnOrientationYaw = BlockRotationUtils.mirrorYaw(this.entitySpawnOrientationYaw, BlockMirror.FRONT_BACK);

                this.x_mirrored = state.get(RotatedBlockWithEntity.X_MIRRORED);
            }
            if (state.get(RotatedBlockWithEntity.Z_MIRRORED) != this.z_mirrored) {
                this.entitySpawnPositionOffset = BlockRotationUtils.mirrorOffsetBlockPos(this.entitySpawnPositionOffset, BlockMirror.LEFT_RIGHT);

                this.entitySpawnOrientationYaw = BlockRotationUtils.mirrorYaw(this.entitySpawnOrientationYaw, BlockMirror.LEFT_RIGHT);

                this.z_mirrored = state.get(RotatedBlockWithEntity.Z_MIRRORED);
            }
        }
    }

    @Override
    public void reset() {
        if (this.triggered) {
            this.triggered = false;
        }
    }

    @Override
    public void trigger() {
        if (this.spawningMode == SpawningMode.TRIGGERED && !this.triggered) {
            if (this.spawnEntity()) {
                this.triggered = true;
            }
        } else if (this.spawningMode == SpawningMode.CONTINUOUS) {
            if (this.spawnEntity()) {
                this.triggered = true;
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

    public static enum SpawningMode implements StringIdentifiable
    {
        CONTINUOUS("continuous"),
        TRIGGERED("triggered");

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
