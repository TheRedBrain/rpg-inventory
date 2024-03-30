package com.github.theredbrain.betteradventuremode.block.entity;

import com.github.theredbrain.betteradventuremode.BetterAdventureMode;
import com.github.theredbrain.betteradventuremode.entity.IsSpawnerBound;
import com.github.theredbrain.betteradventuremode.util.BlockRotationUtils;
import com.github.theredbrain.betteradventuremode.block.Resetable;
import com.github.theredbrain.betteradventuremode.block.RotatedBlockWithEntity;
import com.github.theredbrain.betteradventuremode.block.Triggerable;
import com.github.theredbrain.betteradventuremode.entity.player.DuckPlayerEntityMixin;
import com.github.theredbrain.betteradventuremode.registry.EntityRegistry;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.attribute.*;
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
import net.minecraft.util.Identifier;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.village.VillagerData;
import net.minecraft.village.VillagerDataContainer;
import net.minecraft.world.WorldEvents;
import net.minecraft.world.event.GameEvent;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class TriggeredSpawnerBlockEntity extends RotatedBlockEntity implements Triggerable, Resetable {

    private BlockPos entitySpawnPositionOffset = new BlockPos(0, 1, 0);
    private double entitySpawnOrientationPitch = 0.0;
    private double entitySpawnOrientationYaw = 0.0;
    @Nullable
    private UUID boundEntityUuid = null;

    private SpawningMode spawningMode = SpawningMode.ONCE;
    private EntityMode entityMode = EntityMode.IDENTIFIER;

    private Multimap<EntityAttribute, EntityAttributeModifier> entityAttributeModifiers = Multimaps.newMultimap(Maps.newLinkedHashMap(), ArrayList::new);

    private BlockPos triggeredBlockPositionOffset = new BlockPos(0, 0, 0);
    private BlockPos useRelayBlockPositionOffset = new BlockPos(0, 0, 0);

    String spawnerBoundEntityName = "";
    Identifier spawnerBoundEntityModelIdentifier = BetterAdventureMode.identifier("spawner_bound_entity/default_spawner_bound_entity");;
    Identifier spawnerBoundEntityTextureIdentifier = BetterAdventureMode.identifier("spawner_bound_entity/default_spawner_bound_entity");;
    Identifier spawnerBoundEntityAnimationsIdentifier = BetterAdventureMode.identifier("spawner_bound_entity/default_spawner_bound_entity");
    float spawnerBoundEntityBoundingBoxHeight = 1.8f;
    float spawnerBoundEntityBoundingBoxWidth = 0.8f;
    Identifier spawnerBoundEntityLootTableIdentifier = BetterAdventureMode.identifier("spawner_bound_entity/default_spawner_bound_entity");;

    private String villagerProfession = "none";
    private String villagerType = "plains";
    private int villagerLevel = 1;

    private boolean triggered = false;
    private NbtCompound entityTypeCompound = new NbtCompound();

    public TriggeredSpawnerBlockEntity(BlockPos pos, BlockState state) {
        super(EntityRegistry.TRIGGERED_SPAWNER_BLOCK_ENTITY, pos, state);
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {

        super.writeNbt(nbt);

        nbt.putString("spawnerBoundEntityName", this.spawnerBoundEntityName);
        nbt.putString("spawnerBoundEntityModelIdentifier", this.spawnerBoundEntityModelIdentifier != null ? this.spawnerBoundEntityModelIdentifier.toString() : "");
        nbt.putString("spawnerBoundEntityTextureIdentifier", this.spawnerBoundEntityTextureIdentifier != null ? this.spawnerBoundEntityTextureIdentifier.toString() : "");
        nbt.putString("spawnerBoundEntityAnimationsIdentifier", this.spawnerBoundEntityAnimationsIdentifier != null ? this.spawnerBoundEntityAnimationsIdentifier.toString() : "");
        nbt.putFloat("spawnerBoundEntityBoundingBoxHeight", this.spawnerBoundEntityBoundingBoxHeight);
        nbt.putFloat("spawnerBoundEntityBoundingBoxWidth", this.spawnerBoundEntityBoundingBoxWidth);
        nbt.putString("spawnerBoundEntityLootTableIdentifier", this.spawnerBoundEntityLootTableIdentifier != null ? this.spawnerBoundEntityLootTableIdentifier.toString() : "");

        nbt.putInt("entitySpawnPositionOffsetX", this.entitySpawnPositionOffset.getX());
        nbt.putInt("entitySpawnPositionOffsetY", this.entitySpawnPositionOffset.getY());
        nbt.putInt("entitySpawnPositionOffsetZ", this.entitySpawnPositionOffset.getZ());

        nbt.putDouble("entitySpawnOrientationPitch", this.entitySpawnOrientationPitch);
        nbt.putDouble("entitySpawnOrientationYaw", this.entitySpawnOrientationYaw);

        nbt.putInt("triggeredBlockPositionOffsetX", this.triggeredBlockPositionOffset.getX());
        nbt.putInt("triggeredBlockPositionOffsetY", this.triggeredBlockPositionOffset.getY());
        nbt.putInt("triggeredBlockPositionOffsetZ", this.triggeredBlockPositionOffset.getZ());

        nbt.putInt("useRelayedBlockPositionOffsetX", this.useRelayBlockPositionOffset.getX());
        nbt.putInt("useRelayedBlockPositionOffsetY", this.useRelayBlockPositionOffset.getY());
        nbt.putInt("useRelayedBlockPositionOffsetZ", this.useRelayBlockPositionOffset.getZ());

        List<EntityAttribute> entityAttributeModifiersKeys = new ArrayList<>(this.entityAttributeModifiers.keySet());
        nbt.putInt("entityAttributeModifiersKeysSize", entityAttributeModifiersKeys.size());
        for (int i = 0; i < entityAttributeModifiersKeys.size(); i++) {
            EntityAttribute key = entityAttributeModifiersKeys.get(i);
            Collection<EntityAttributeModifier> modifierCollection = this.entityAttributeModifiers.get(key);
            nbt.putString("entityAttributeModifiers_key" + i, String.valueOf(Registries.ATTRIBUTE.getId(key)));
            List<EntityAttributeModifier> modifierList = modifierCollection.stream().toList();
            nbt.putInt("entityAttributeModifiers_modifierListSize_" + i, modifierList.size());
            for (int j = 0; j < modifierList.size(); j++) {
                nbt.put("entityAttributeModifiers_" + i + "_" + j, modifierList.get(j).toNbt());
            }
        }

        nbt.putString("spawningMode", this.spawningMode.asString());

        nbt.putString("entityMode", this.entityMode.asString());

        nbt.putBoolean("triggered", this.triggered);

        nbt.put("EntityTypeCompound", this.entityTypeCompound);

        if (this.villagerLevel > 1) {
            nbt.putInt("villagerLevel", this.villagerLevel);
        }

        if (!this.villagerProfession.equals("none")) {
            nbt.putString("villagerProfession", this.villagerProfession);
        }

        if (!this.villagerType.equals("plains")) {
            nbt.putString("villagerType", this.villagerType);
        }

        if (this.boundEntityUuid != null) {
            nbt.putUuid("boundEntityUuid", this.boundEntityUuid);
        }
    }

    @Override
    public void readNbt(NbtCompound nbt) {

        super.readNbt(nbt);

        this.spawnerBoundEntityName = nbt.getString("spawnerBoundEntityName");
        this.spawnerBoundEntityModelIdentifier = Identifier.tryParse(nbt.getString("spawnerBoundEntityModelIdentifier"));
        this.spawnerBoundEntityTextureIdentifier = Identifier.tryParse(nbt.getString("spawnerBoundEntityTextureIdentifier"));
        this.spawnerBoundEntityAnimationsIdentifier = Identifier.tryParse(nbt.getString("spawnerBoundEntityAnimationsIdentifier"));
        this.spawnerBoundEntityBoundingBoxHeight = nbt.getFloat("spawnerBoundEntityBoundingBoxHeight");
        this.spawnerBoundEntityBoundingBoxWidth = nbt.getFloat("spawnerBoundEntityBoundingBoxWidth");
        this.spawnerBoundEntityLootTableIdentifier = Identifier.tryParse(nbt.getString("spawnerBoundEntityLootTableIdentifier"));

        this.entitySpawnPositionOffset = new BlockPos(
                MathHelper.clamp(nbt.getInt("entitySpawnPositionOffsetX"), -48, 48),
                MathHelper.clamp(nbt.getInt("entitySpawnPositionOffsetY"), -48, 48),
                MathHelper.clamp(nbt.getInt("entitySpawnPositionOffsetZ"), -48, 48)
        );

        this.entitySpawnOrientationPitch = nbt.getDouble("entitySpawnOrientationPitch");
        this.entitySpawnOrientationYaw = nbt.getDouble("entitySpawnOrientationYaw");

        this.triggeredBlockPositionOffset = new BlockPos(
                MathHelper.clamp(nbt.getInt("triggeredBlockPositionOffsetX"), -48, 48),
                MathHelper.clamp(nbt.getInt("triggeredBlockPositionOffsetY"), -48, 48),
                MathHelper.clamp(nbt.getInt("triggeredBlockPositionOffsetZ"), -48, 48)
        );

        this.useRelayBlockPositionOffset = new BlockPos(
                MathHelper.clamp(nbt.getInt("useRelayedBlockPositionOffsetX"), -48, 48),
                MathHelper.clamp(nbt.getInt("useRelayedBlockPositionOffsetY"), -48, 48),
                MathHelper.clamp(nbt.getInt("useRelayedBlockPositionOffsetZ"), -48, 48)
        );

        this.entityAttributeModifiers.clear();
        int entityAttributeModifiersKeysSize = nbt.getInt("entityAttributeModifiersKeysSize");
        for (int i = 0; i < entityAttributeModifiersKeysSize; i++) {
            Optional<EntityAttribute> optional = Registries.ATTRIBUTE
                    .getOrEmpty(Identifier.tryParse(nbt.getString("entityAttributeModifiers_key" + i)));
            if (optional.isPresent()) {
                EntityAttribute key = optional.get();
                int modifierListSize = nbt.getInt("entityAttributeModifiers_modifierListSize_" + i);
                for (int j = 0; j < modifierListSize; j++) {
                    this.entityAttributeModifiers.put(key, EntityAttributeModifier.fromNbt(nbt.getCompound("entityAttributeModifiers_" + i + "_" + j)));
                }
            }
        }

        this.spawningMode = SpawningMode.byName(nbt.getString("spawningMode")).orElseGet(() -> SpawningMode.ONCE);

        this.entityMode = EntityMode.byName(nbt.getString("entityMode")).orElseGet(() -> EntityMode.IDENTIFIER);

        this.triggered = nbt.getBoolean("triggered");

        if (nbt.contains("EntityTypeCompound", NbtElement.COMPOUND_TYPE)) {
            this.entityTypeCompound = nbt.getCompound("EntityTypeCompound");
        }

        if (nbt.contains("villagerLevel", NbtElement.INT_TYPE)) {
            this.villagerLevel = nbt.getInt("villagerLevel");
        } else {
            this.villagerLevel = 1;
        }

        if (nbt.contains("villagerProfession", NbtElement.STRING_TYPE)) {
            this.villagerProfession = nbt.getString("villagerProfession");
        } else {
            this.villagerProfession = "none";
        }

        if (nbt.contains("villagerType", NbtElement.STRING_TYPE)) {
            this.villagerType = nbt.getString("villagerType");
        } else {
            this.villagerType = "plains";
        }

        if (nbt.containsUuid("boundEntityUuid")) {
            this.boundEntityUuid = nbt.getUuid("boundEntityUuid");
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
            ((DuckPlayerEntityMixin)player).betteradventuremode$openTriggeredSpawnerBlockScreen(this);
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

    public double getEntitySpawnOrientationPitch() {
        return entitySpawnOrientationPitch;
    }

    // TODO check if input is valid
    public boolean setEntitySpawnPositionPitch(double entitySpawnPositionPitch) {
        this.entitySpawnOrientationPitch = entitySpawnPositionPitch;
        return true;
    }

    public double getEntitySpawnOrientationYaw() {
        return entitySpawnOrientationYaw;
    }

    // TODO check if input is valid
    public boolean setEntitySpawnPositionYaw(double entitySpawnPositionYaw) {
        this.entitySpawnOrientationYaw = entitySpawnPositionYaw;
        return true;
    }

    public Multimap<EntityAttribute, EntityAttributeModifier> getEntityAttributeModifiers() {
        return this.entityAttributeModifiers;
    }

    public boolean setEntityAttributeModifiers(Multimap<EntityAttribute, EntityAttributeModifier> entityAttributeModifiers) {
        this.entityAttributeModifiers = entityAttributeModifiers;
        return true;
    }

    public TriggeredSpawnerBlockEntity.SpawningMode getSpawningMode() {
        return this.spawningMode;
    }

    public boolean setSpawningMode(TriggeredSpawnerBlockEntity.SpawningMode spawningMode) {
        this.spawningMode = spawningMode;
        return true;
    }

    public TriggeredSpawnerBlockEntity.EntityMode getEntityMode() {
        return this.entityMode;
    }

    public boolean setEntityMode(TriggeredSpawnerBlockEntity.EntityMode entityMode) {
        this.entityMode = entityMode;
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
        return entityTypeId.equals("");
    }

    public BlockPos getTriggeredBlockPositionOffset() {
        return this.triggeredBlockPositionOffset;
    }

    public void setTriggeredBlockPositionOffset(BlockPos triggeredBlockPositionOffset) {
        this.triggeredBlockPositionOffset = triggeredBlockPositionOffset;
    }

    public BlockPos getUseRelayBlockPositionOffset() {
        return this.useRelayBlockPositionOffset;
    }

    public void setUseRelayBlockPositionOffset(BlockPos useRelayedBlockPositionOffset) {
        this.useRelayBlockPositionOffset = useRelayedBlockPositionOffset;
    }

    public String getSpawnerBoundEntityName() {
        return this.spawnerBoundEntityName;
    }

    public void setSpawnerBoundEntityName(String spawnerBoundEntityName) {
        this.spawnerBoundEntityName = spawnerBoundEntityName;
    }

    public Identifier getSpawnerBoundEntityModelIdentifier() {
        return this.spawnerBoundEntityModelIdentifier;
    }

    public boolean setSpawnerBoundEntityModelIdentifier(String spawnerBoundEntityModelIdentifier) {
        if (Identifier.isValid(spawnerBoundEntityModelIdentifier)) {
            this.spawnerBoundEntityModelIdentifier = new Identifier(spawnerBoundEntityModelIdentifier);
            return true;
        }
        if (spawnerBoundEntityModelIdentifier.equals("")) {
            this.spawnerBoundEntityModelIdentifier = null;
            return true;
        }
        return false;
    }

    public Identifier getSpawnerBoundEntityTextureIdentifier() {
        return this.spawnerBoundEntityTextureIdentifier;
    }

    public boolean setSpawnerBoundEntityTextureIdentifier(String spawnerBoundEntityTextureIdentifier) {
        if (Identifier.isValid(spawnerBoundEntityTextureIdentifier)) {
            this.spawnerBoundEntityTextureIdentifier = new Identifier(spawnerBoundEntityTextureIdentifier);
            return true;
        }
        if (spawnerBoundEntityTextureIdentifier.equals("")) {
            this.spawnerBoundEntityTextureIdentifier = null;
            return true;
        }
        return false;
    }

    public Identifier getSpawnerBoundEntityAnimationsIdentifier() {
        return this.spawnerBoundEntityAnimationsIdentifier;
    }

    public boolean setSpawnerBoundEntityAnimationsIdentifier(String spawnerBoundEntityAnimationsIdentifier) {
        if (Identifier.isValid(spawnerBoundEntityAnimationsIdentifier)) {
            this.spawnerBoundEntityAnimationsIdentifier = new Identifier(spawnerBoundEntityAnimationsIdentifier);
            return true;
        }
        if (spawnerBoundEntityAnimationsIdentifier.equals("")) {
            this.spawnerBoundEntityAnimationsIdentifier = null;
            return true;
        }
        return false;
    }

    public double getSpawnerBoundEntityBoundingBoxHeight() {
        return this.spawnerBoundEntityBoundingBoxHeight;
    }

    public boolean setSpawnerBoundEntityBoundingBoxHeight(float spawnerBoundEntityBoundingBoxHeight) {
        this.spawnerBoundEntityBoundingBoxHeight = spawnerBoundEntityBoundingBoxHeight;
        return true;
    }

    public double getSpawnerBoundEntityBoundingBoxWidth() {
        return this.spawnerBoundEntityBoundingBoxWidth;
    }

    public boolean setSpawnerBoundEntityBoundingBoxWidth(float spawnerBoundEntityBoundingBoxWidth) {
        this.spawnerBoundEntityBoundingBoxWidth = spawnerBoundEntityBoundingBoxWidth;
        return true;
    }

    public Identifier getSpawnerBoundEntityLootTableIdentifier() {
        return this.spawnerBoundEntityLootTableIdentifier;
    }

    public boolean setSpawnerBoundEntityLootTableIdentifier(String spawnerBoundEntityLootTableIdentifier) {
        if (Identifier.isValid(spawnerBoundEntityLootTableIdentifier)) {
            this.spawnerBoundEntityLootTableIdentifier = new Identifier(spawnerBoundEntityLootTableIdentifier);
            return true;
        }
        if (spawnerBoundEntityLootTableIdentifier.equals("")) {
            this.spawnerBoundEntityLootTableIdentifier = null;
            return true;
        }
        return false;
    }

    public String getVillagerProfession() {
        return villagerProfession;
    }

    public void setVillagerProfession(String villagerProfession) {
        this.villagerProfession = villagerProfession;
    }

    public String getVillagerType() {
        return villagerType;
    }

    public void setVillagerType(String villagerType) {
        this.villagerType = villagerType;
    }

    public int getVillagerLevel() {
        return villagerLevel;
    }

    public void setVillagerLevel(int villagerLevel) {
        this.villagerLevel = villagerLevel;
    }

    //endregion getter/setter

    @Override
    protected void onRotate(BlockState state) {
        if (state.getBlock() instanceof RotatedBlockWithEntity) {
            if (state.get(RotatedBlockWithEntity.ROTATED) != this.rotated) {
                BlockRotation blockRotation = BlockRotationUtils.calculateRotationFromDifferentRotatedStates(state.get(RotatedBlockWithEntity.ROTATED), this.rotated);
                this.entitySpawnPositionOffset = BlockRotationUtils.rotateOffsetBlockPos(this.entitySpawnPositionOffset, blockRotation);
                this.entitySpawnOrientationYaw = BlockRotationUtils.rotateYaw(this.entitySpawnOrientationYaw, blockRotation);
                this.triggeredBlockPositionOffset = BlockRotationUtils.rotateOffsetBlockPos(this.triggeredBlockPositionOffset, blockRotation);
                this.useRelayBlockPositionOffset = BlockRotationUtils.rotateOffsetBlockPos(this.useRelayBlockPositionOffset, blockRotation);

                this.rotated = state.get(RotatedBlockWithEntity.ROTATED);
            }
            if (state.get(RotatedBlockWithEntity.X_MIRRORED) != this.x_mirrored) {
                this.entitySpawnPositionOffset = BlockRotationUtils.mirrorOffsetBlockPos(this.entitySpawnPositionOffset, BlockMirror.FRONT_BACK);
                this.entitySpawnOrientationYaw = BlockRotationUtils.mirrorYaw(this.entitySpawnOrientationYaw, BlockMirror.FRONT_BACK);
                this.triggeredBlockPositionOffset = BlockRotationUtils.mirrorOffsetBlockPos(this.triggeredBlockPositionOffset, BlockMirror.FRONT_BACK);
                this.useRelayBlockPositionOffset = BlockRotationUtils.mirrorOffsetBlockPos(this.useRelayBlockPositionOffset, BlockMirror.FRONT_BACK);

                this.x_mirrored = state.get(RotatedBlockWithEntity.X_MIRRORED);
            }
            if (state.get(RotatedBlockWithEntity.Z_MIRRORED) != this.z_mirrored) {
                this.entitySpawnPositionOffset = BlockRotationUtils.mirrorOffsetBlockPos(this.entitySpawnPositionOffset, BlockMirror.LEFT_RIGHT);
                this.entitySpawnOrientationYaw = BlockRotationUtils.mirrorYaw(this.entitySpawnOrientationYaw, BlockMirror.LEFT_RIGHT);
                this.triggeredBlockPositionOffset = BlockRotationUtils.mirrorOffsetBlockPos(this.triggeredBlockPositionOffset, BlockMirror.LEFT_RIGHT);
                this.useRelayBlockPositionOffset = BlockRotationUtils.mirrorOffsetBlockPos(this.useRelayBlockPositionOffset, BlockMirror.LEFT_RIGHT);

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
            this.boundEntityUuid = null;
        }
    }

    @Override
    public void trigger() {
        if ((this.spawningMode == SpawningMode.ONCE && !this.triggered) || this.spawningMode == SpawningMode.CONTINUOUS) {
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

                if (!this.entityAttributeModifiers.isEmpty()) {
                    AttributeContainer attributeContainer = ((MobEntity) entity2).getAttributes();
                    this.entityAttributeModifiers.forEach((attribute, attributeModifier) -> {
                        EntityAttributeInstance entityAttributeInstance = attributeContainer.getCustomInstance((EntityAttribute)attribute);
                        if (entityAttributeInstance != null) {
                            entityAttributeInstance.removeModifier(attributeModifier.getId());
                            entityAttributeInstance.addPersistentModifier((EntityAttributeModifier)attributeModifier);
                        }
                        if (attribute == EntityAttributes.GENERIC_MAX_HEALTH) {
                            ((MobEntity) entity2).setHealth((float) ((MobEntity) entity2).getAttributes().getValue(EntityAttributes.GENERIC_MAX_HEALTH));
                        }
                    });
                }
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
            entity2.setBodyYaw((float) this.entitySpawnOrientationYaw);
            entity2.setHeadYaw((float) this.entitySpawnOrientationYaw);
            entity2.refreshPositionAndAngles(entity2.getX(), entity2.getY(), entity2.getZ(), (float) this.entitySpawnOrientationYaw, (float) this.entitySpawnOrientationPitch);
            if (entity2 instanceof MobEntity) {
                if (this.entityTypeCompound.contains("id", NbtElement.STRING_TYPE)) {
                    NbtCompound entityNbt = new NbtCompound();
                    entityNbt.putInt("betteradventuremode$boundSpawnerBlockPosX", this.pos.getX());
                    entityNbt.putInt("betteradventuremode$boundSpawnerBlockPosY", this.pos.getY());
                    entityNbt.putInt("betteradventuremode$boundSpawnerBlockPosZ", this.pos.getZ());
                    ((MobEntity)entity2).initialize(serverWorld, serverWorld.getLocalDifficulty(entity2.getBlockPos()), SpawnReason.SPAWNER, null, entityNbt);
                }
            }
            if (entity2 instanceof IsSpawnerBound) {
                ((IsSpawnerBound)entity2).setAnimationIdentifierString(this.spawnerBoundEntityAnimationsIdentifier.toString());
                ((IsSpawnerBound)entity2).setBoundSpawnerBlockPos(this.pos);
                ((IsSpawnerBound)entity2).setBoundingBoxHeight(this.spawnerBoundEntityBoundingBoxHeight);
                ((IsSpawnerBound)entity2).setBoundingBoxWidth(this.spawnerBoundEntityBoundingBoxWidth);
                ((IsSpawnerBound)entity2).setModelIdentifierString(this.spawnerBoundEntityModelIdentifier.toString());
                ((IsSpawnerBound)entity2).setUseRelayBlockPos(this.pos.add(this.useRelayBlockPositionOffset));
                ((IsSpawnerBound)entity2).setTextureIdentifierString(this.spawnerBoundEntityTextureIdentifier.toString());
//                ((SpawnerBoundEntity)entity2).setNoGravity(true);

//                entityNbt.putString("spawnerBoundEntityName", this.spawnerBoundEntityName);

//                if (this.spawnerBoundEntityLootTableIdentifier != null) {
//                    entityNbt.putString("DeathLootTable", this.spawnerBoundEntityLootTableIdentifier.toString());
//                }
            }
            if (entity2 instanceof VillagerDataContainer) {
                ((VillagerDataContainer) entity2).setVillagerData(new VillagerData(
                        Registries.VILLAGER_TYPE.get(new Identifier(this.villagerType)),
                        Registries.VILLAGER_PROFESSION.get(new Identifier(this.villagerProfession)),
                        this.villagerLevel
                ));
            }
            if (!serverWorld.spawnNewEntityAndPassengers(entity2)) {
                return false;
            }
            serverWorld.syncWorldEvent(WorldEvents.SPAWNER_SPAWNS_MOB, this.pos, 0);
            serverWorld.emitGameEvent(entity2, GameEvent.ENTITY_PLACE, blockPos);
            if (entity2 instanceof LivingEntity) {

                this.boundEntityUuid = ((LivingEntity)entity2).getUuid();

                if (!this.entityAttributeModifiers.isEmpty()) {
                    AttributeContainer attributeContainer = ((LivingEntity)entity2).getAttributes();
                    this.entityAttributeModifiers.forEach((attribute, attributeModifier) -> {
                        EntityAttributeInstance entityAttributeInstance = attributeContainer.getCustomInstance((EntityAttribute) attribute);
                        if (entityAttributeInstance != null) {
                            entityAttributeInstance.removeModifier(attributeModifier.getId());
                            entityAttributeInstance.addPersistentModifier((EntityAttributeModifier) attributeModifier);
                        }
                        if (attribute == EntityAttributes.GENERIC_MAX_HEALTH) {
                            ((LivingEntity)entity2).setHealth((float) ((LivingEntity)entity2).getAttributes().getValue(EntityAttributes.GENERIC_MAX_HEALTH));
                        }
                    });
                }
                if (entity2 instanceof MobEntity mobEntity) {
                    mobEntity.setPersistent();
                }
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

    public static enum EntityMode implements StringIdentifiable
    {
        IDENTIFIER("identifier"),
        SPAWNER_BOUND_ENTITY("spawner_bound_entity"),
        SPAWNER_BOUND_VILLAGER_ENTITY("spawner_bound_villager_entity");

        private final String name;

        private EntityMode(String name) {
            this.name = name;
        }

        @Override
        public String asString() {
            return this.name;
        }

        public static Optional<TriggeredSpawnerBlockEntity.EntityMode> byName(String name) {
            return Arrays.stream(TriggeredSpawnerBlockEntity.EntityMode.values()).filter(entityMode -> entityMode.asString().equals(name)).findFirst();
        }

        public Text asText() {
            return Text.translatable("gui.triggered_spawner_block.entity_mode." + this.name);
        }
    }
}
