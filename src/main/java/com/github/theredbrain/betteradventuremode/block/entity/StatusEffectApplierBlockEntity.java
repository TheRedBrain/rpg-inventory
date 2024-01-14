package com.github.theredbrain.betteradventuremode.block.entity;

import com.github.theredbrain.betteradventuremode.BetterAdventureMode;
import com.github.theredbrain.betteradventuremode.api.util.BlockRotationUtils;
import com.github.theredbrain.betteradventuremode.block.Resetable;
import com.github.theredbrain.betteradventuremode.block.RotatedBlockWithEntity;
import com.github.theredbrain.betteradventuremode.block.Triggerable;
import com.github.theredbrain.betteradventuremode.entity.player.DuckPlayerEntityMixin;
import com.github.theredbrain.betteradventuremode.registry.EntityRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.registry.Registries;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.*;
import net.minecraft.world.World;
import org.apache.commons.lang3.tuple.MutablePair;

import java.util.Iterator;
import java.util.List;

public class StatusEffectApplierBlockEntity extends RotatedBlockEntity implements Triggerable, Resetable {

    private boolean calculateApplicationBox = true;
    private Box applicationArea = null;
    private boolean showApplicationArea = false;
    private Vec3i applicationAreaDimensions = Vec3i.ZERO;
    private BlockPos applicationAreaPositionOffset = new BlockPos(0, 1, 0);
    private String appliedStatusEffectIdentifier = "";
    private int appliedStatusEffectAmplifier = 0;
    private boolean appliedStatusEffectAmbient = false;
    private boolean appliedStatusEffectShowParticles = false;
    private boolean appliedStatusEffectShowIcon = false;
    private boolean triggered = false;

    public StatusEffectApplierBlockEntity(BlockPos pos, BlockState state) {
        super(EntityRegistry.STATUS_EFFECT_APPLIER_BLOCK_ENTITY, pos, state);
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {

        nbt.putBoolean("showApplicationArea", this.showApplicationArea);

        nbt.putInt("applicationAreaDimensionsX", this.applicationAreaDimensions.getX());
        nbt.putInt("applicationAreaDimensionsY", this.applicationAreaDimensions.getY());
        nbt.putInt("applicationAreaDimensionsZ", this.applicationAreaDimensions.getZ());

        nbt.putInt("applicationAreaPositionOffsetX", this.applicationAreaPositionOffset.getX());
        nbt.putInt("applicationAreaPositionOffsetY", this.applicationAreaPositionOffset.getY());
        nbt.putInt("applicationAreaPositionOffsetZ", this.applicationAreaPositionOffset.getZ());

        nbt.putString("appliedStatusEffectIdentifier", this.appliedStatusEffectIdentifier);

        nbt.putInt("appliedStatusEffectAmplifier", this.appliedStatusEffectAmplifier);

        nbt.putBoolean("appliedStatusEffectAmbient", this.appliedStatusEffectAmbient);

        nbt.putBoolean("appliedStatusEffectShowParticles", this.appliedStatusEffectShowParticles);

        nbt.putBoolean("appliedStatusEffectShowIcon", this.appliedStatusEffectShowIcon);

        nbt.putBoolean("triggered", this.triggered);

        if (this.applicationArea != null) {
            nbt.putDouble("applicationAreaMinX", this.applicationArea.minX);
            nbt.putDouble("applicationAreaMaxX", this.applicationArea.maxX);
            nbt.putDouble("applicationAreaMinY", this.applicationArea.minY);
            nbt.putDouble("applicationAreaMaxY", this.applicationArea.maxY);
            nbt.putDouble("applicationAreaMinZ", this.applicationArea.minZ);
            nbt.putDouble("applicationAreaMaxZ", this.applicationArea.maxZ);
        }

        super.writeNbt(nbt);

    }

    @Override
    public void readNbt(NbtCompound nbt) {

        this.showApplicationArea = nbt.getBoolean("showApplicationArea");

        int i = MathHelper.clamp(nbt.getInt("applicationAreaDimensionsX"), 0, 48);
        int j = MathHelper.clamp(nbt.getInt("applicationAreaDimensionsY"), 0, 48);
        int k = MathHelper.clamp(nbt.getInt("applicationAreaDimensionsZ"), 0, 48);
        this.applicationAreaDimensions = new Vec3i(i, j, k);

        int l = MathHelper.clamp(nbt.getInt("applicationAreaPositionOffsetX"), -48, 48);
        int m = MathHelper.clamp(nbt.getInt("applicationAreaPositionOffsetY"), -48, 48);
        int n = MathHelper.clamp(nbt.getInt("applicationAreaPositionOffsetZ"), -48, 48);
        this.applicationAreaPositionOffset = new BlockPos(l, m, n);

        this.appliedStatusEffectIdentifier = nbt.getString("appliedStatusEffectIdentifier");

        this.appliedStatusEffectAmplifier = nbt.getInt("appliedStatusEffectAmplifier");

        this.appliedStatusEffectAmbient = nbt.getBoolean("appliedStatusEffectAmbient");

        this.appliedStatusEffectShowParticles = nbt.getBoolean("appliedStatusEffectShowParticles");

        this.appliedStatusEffectShowIcon = nbt.getBoolean("appliedStatusEffectShowIcon");

        this.triggered = nbt.getBoolean("triggered");

        if (nbt.contains("applicationAreaMinX") && nbt.contains("applicationAreaMinY") && nbt.contains("applicationAreaMinZ") && nbt.contains("applicationAreaMaxX") && nbt.contains("applicationAreaMaxY") && nbt.contains("applicationAreaMaxZ")) {
            this.applicationArea = new Box(nbt.getDouble("applicationAreaMinX"), nbt.getDouble("applicationAreaMinY"), nbt.getDouble("applicationAreaMinZ"), nbt.getDouble("applicationAreaMaxX"), nbt.getDouble("applicationAreaMaxY"), nbt.getDouble("applicationAreaMaxZ"));
            this.calculateApplicationBox = true;
        }

        super.readNbt(nbt);
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
            ((DuckPlayerEntityMixin)player).betteradventuremode$openStatusEffectApplierBlockScreen(this);
        }
        return true;
    }

    public static void tick(World world, BlockPos pos, BlockState state, StatusEffectApplierBlockEntity blockEntity) {
        if (!world.isClient && world.getTime() % 80L == 0L && blockEntity.triggered) {
            if (blockEntity.calculateApplicationBox || blockEntity.applicationArea == null) {
                BlockPos applicationAreaPositionOffset = blockEntity.applicationAreaPositionOffset;
                Vec3i applicationAreaDimensions = blockEntity.applicationAreaDimensions;
                Vec3d applicationAreaStart = new Vec3d(pos.getX() + applicationAreaPositionOffset.getX(), pos.getY() + applicationAreaPositionOffset.getY(), pos.getZ() + applicationAreaPositionOffset.getZ());
                Vec3d applicationAreaEnd = new Vec3d(applicationAreaStart.getX() + applicationAreaDimensions.getX(), applicationAreaStart.getY() + applicationAreaDimensions.getY(), applicationAreaStart.getZ() + applicationAreaDimensions.getZ());
                blockEntity.applicationArea = new Box(applicationAreaStart, applicationAreaEnd);
                blockEntity.calculateApplicationBox = false;
            }
            StatusEffect statusEffect = Registries.STATUS_EFFECT.get(Identifier.tryParse(blockEntity.appliedStatusEffectIdentifier));
            if (statusEffect != null) {
                List<PlayerEntity> list = world.getNonSpectatingEntities(PlayerEntity.class, blockEntity.applicationArea);
                Iterator var11 = list.iterator();

                PlayerEntity playerEntity;
                while(var11.hasNext()) {
                    playerEntity = (PlayerEntity)var11.next();
                    playerEntity.addStatusEffect(
                            new StatusEffectInstance(
                                    statusEffect,
                                    100,
                                    blockEntity.appliedStatusEffectAmplifier,
                                    blockEntity.appliedStatusEffectAmbient,
                                    blockEntity.appliedStatusEffectShowParticles,
                                    blockEntity.appliedStatusEffectShowIcon
                            )
                    );
                }
            }
        }
    }

    //region --- getter & setter ---
    public boolean getShowApplicationArea() {
        return showApplicationArea;
    }

    public boolean setShowApplicationArea(boolean showApplicationArea) {
        this.showApplicationArea = showApplicationArea;
        return true;
    }

    public Vec3i getApplicationAreaDimensions() {
        return applicationAreaDimensions;
    }

    // TODO check if input is valid
    public boolean setApplicationAreaDimensions(Vec3i applicationAreaDimensions) {
        this.applicationAreaDimensions = applicationAreaDimensions;
        this.calculateApplicationBox = true;
        return true;
    }

    public BlockPos getApplicationAreaPositionOffset() {
        return applicationAreaPositionOffset;
    }

    // TODO check if input is valid
    public boolean setApplicationAreaPositionOffset(BlockPos applicationAreaPositionOffset) {
        this.applicationAreaPositionOffset = applicationAreaPositionOffset;
        this.calculateApplicationBox = true;
        return true;
    }

    public String getAppliedStatusEffectIdentifier() {
        return this.appliedStatusEffectIdentifier;
    }

    public boolean setAppliedStatusEffectIdentifier(String appliedStatusEffectIdentifier) {
        if (Registries.STATUS_EFFECT.get(Identifier.tryParse(appliedStatusEffectIdentifier)) != null) {
            this.appliedStatusEffectIdentifier = appliedStatusEffectIdentifier;
            return true;
        }
        return false;
    }

    public int getAppliedStatusEffectAmplifier() {
        return appliedStatusEffectAmplifier;
    }

    public boolean setAppliedStatusEffectAmplifier(int appliedStatusEffectAmplifier) {
        this.appliedStatusEffectAmplifier = appliedStatusEffectAmplifier;
        return true;
    }

    public boolean getAppliedStatusEffectAmbient() {
        return appliedStatusEffectAmbient;
    }

    public boolean setAppliedStatusEffectAmbient(boolean appliedStatusEffectAmbient) {
        this.appliedStatusEffectAmbient = appliedStatusEffectAmbient;
        return true;
    }

    public boolean getAppliedStatusEffectShowParticles() {
        return appliedStatusEffectShowParticles;
    }

    public boolean setAppliedStatusEffectShowParticles(boolean appliedStatusEffectShowParticles) {
        this.appliedStatusEffectShowParticles = appliedStatusEffectShowParticles;
        return true;
    }

    public boolean getAppliedStatusEffectShowIcon() {
        return appliedStatusEffectShowIcon;
    }

    public boolean setAppliedStatusEffectShowIcon(boolean appliedStatusEffectShowIcon) {
        this.appliedStatusEffectShowIcon = appliedStatusEffectShowIcon;
        return true;
    }

    public boolean getTriggered() {
        return this.triggered;
    }

    public boolean setTriggered(boolean triggered) {
        this.triggered = triggered;
        return true;
    }
    //endregion --- getter & setter ---

    @Override
    public void reset() {
        if (this.triggered) {
            this.triggered = false;
        }
    }

    @Override
    public void trigger() {
        if (!this.triggered) {
            this.triggered = true;
        }
    }

    @Override
    protected void onRotate(BlockState state) {
        if (state.getBlock() instanceof RotatedBlockWithEntity) {
            if (state.get(RotatedBlockWithEntity.ROTATED) != this.rotated) {
                BlockRotation blockRotation = BlockRotationUtils.calculateRotationFromDifferentRotatedStates(state.get(RotatedBlockWithEntity.ROTATED), this.rotated);

                MutablePair<BlockPos, Vec3i> offsetArea = BlockRotationUtils.rotateOffsetArea(this.applicationAreaPositionOffset, this.applicationAreaDimensions, blockRotation);
                this.applicationAreaPositionOffset = offsetArea.getLeft();
                this.applicationAreaDimensions = offsetArea.getRight();

                this.rotated = state.get(RotatedBlockWithEntity.ROTATED);
            }
            if (state.get(RotatedBlockWithEntity.X_MIRRORED) != this.x_mirrored) {

                MutablePair<BlockPos, Vec3i> offsetArea = BlockRotationUtils.mirrorOffsetArea(this.applicationAreaPositionOffset, this.applicationAreaDimensions, BlockMirror.FRONT_BACK);
                this.applicationAreaPositionOffset = offsetArea.getLeft();
                this.applicationAreaDimensions = offsetArea.getRight();

                this.x_mirrored = state.get(RotatedBlockWithEntity.X_MIRRORED);
            }
            if (state.get(RotatedBlockWithEntity.Z_MIRRORED) != this.z_mirrored) {

                MutablePair<BlockPos, Vec3i> offsetArea = BlockRotationUtils.mirrorOffsetArea(this.applicationAreaPositionOffset, this.applicationAreaDimensions, BlockMirror.LEFT_RIGHT);
                this.applicationAreaPositionOffset = offsetArea.getLeft();
                this.applicationAreaDimensions = offsetArea.getRight();

                this.z_mirrored = state.get(RotatedBlockWithEntity.Z_MIRRORED);
            }
        }
    }
}
