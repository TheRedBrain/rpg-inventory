package com.github.theredbrain.bamcore.block.entity;

import com.github.theredbrain.bamcore.BetterAdventureModeCore;
import com.github.theredbrain.bamcore.api.util.BlockRotationUtils;
import com.github.theredbrain.bamcore.api.util.UUIDUtilities;
import com.github.theredbrain.bamcore.block.RotatedBlockWithEntity;
import com.github.theredbrain.bamcore.block.Triggerable;
import com.github.theredbrain.bamcore.registry.ComponentsRegistry;
import com.github.theredbrain.bamcore.registry.EntityRegistry;
import com.github.theredbrain.bamcore.registry.StatusEffectsRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import org.apache.commons.lang3.tuple.MutablePair;

import java.util.*;

public class HousingBlockBlockEntity extends RotatedBlockEntity {
    private String owner = "";
    private boolean isOwnerSet = false;
    private List<String> coOwnerList = new ArrayList<>(List.of());
    private List<String> trustedList = new ArrayList<>(List.of());
    private List<String> guestList = new ArrayList<>(List.of());
    private boolean showRestrictBlockBreakingArea = false;
    private Vec3i restrictBlockBreakingAreaDimensions = Vec3i.ZERO;
    private BlockPos restrictBlockBreakingAreaPositionOffset = new BlockPos(0, 1, 0);
    private int ownerMode = 0; // 0: dimension owner, 1: first interaction
    private BlockPos triggeredBlockPositionOffset = new BlockPos(0, 1, 0);
    private MutablePair<BlockPos, MutablePair<Double, Double>> entrance = new MutablePair<>(new BlockPos(0, 1, 0), new MutablePair<>(0.0, 0.0));
    public HousingBlockBlockEntity(BlockPos pos, BlockState state) {
        super(EntityRegistry.HOUSING_BLOCK_ENTITY, pos, state);
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        nbt.putString("ownerUuid", this.owner);
        nbt.putBoolean("isOwnerSet", isOwnerSet);

        int coOwnerListSize = coOwnerList.size();
        nbt.putInt("coOwnerListSize", coOwnerListSize);
        for (int i = 0; i<coOwnerListSize; i++) {
            nbt.putString("coOwnerListEntry" + i, this.coOwnerList.get(i));
        }

        int trustedListSize = trustedList.size();
        nbt.putInt("trustedListSize", trustedListSize);
        for (int i = 0; i<trustedListSize; i++) {
            nbt.putString("trustedListEntry" + i, this.trustedList.get(i));
        }

        int guestListSize = guestList.size();
        nbt.putInt("guestListSize", guestListSize);
        for (int i = 0; i<guestListSize; i++) {
            nbt.putString("guestListEntry" + i, this.guestList.get(i));
        }

        nbt.putBoolean("showRestrictBlockBreakingArea", this.showRestrictBlockBreakingArea);

        nbt.putInt("restrictBlockBreakingAreaDimensionsX", this.restrictBlockBreakingAreaDimensions.getX());
        nbt.putInt("restrictBlockBreakingAreaDimensionsY", this.restrictBlockBreakingAreaDimensions.getY());
        nbt.putInt("restrictBlockBreakingAreaDimensionsZ", this.restrictBlockBreakingAreaDimensions.getZ());

        nbt.putInt("restrictBlockBreakingAreaPositionOffsetX", this.restrictBlockBreakingAreaPositionOffset.getX());
        nbt.putInt("restrictBlockBreakingAreaPositionOffsetY", this.restrictBlockBreakingAreaPositionOffset.getY());
        nbt.putInt("restrictBlockBreakingAreaPositionOffsetZ", this.restrictBlockBreakingAreaPositionOffset.getZ());

        nbt.putInt("ownerMode", ownerMode);

        nbt.putInt("triggeredBlockPositionOffsetX", this.triggeredBlockPositionOffset.getX());
        nbt.putInt("triggeredBlockPositionOffsetY", this.triggeredBlockPositionOffset.getY());
        nbt.putInt("triggeredBlockPositionOffsetZ", this.triggeredBlockPositionOffset.getZ());

        nbt.putInt("entrance_X", this.entrance.getLeft().getX());
        nbt.putInt("entrance_Y", this.entrance.getLeft().getY());
        nbt.putInt("entrance_Z", this.entrance.getLeft().getZ());
        nbt.putDouble("entrance_Yaw", this.entrance.getRight().getLeft());
        nbt.putDouble("entrance_Pitch", this.entrance.getRight().getRight());

        super.writeNbt(nbt);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        this.owner = nbt.getString("ownerUuid");
        this.isOwnerSet = nbt.getBoolean("isOwnerSet");

        this.coOwnerList = new ArrayList<>(List.of());
        this.trustedList = new ArrayList<>(List.of());
        this.guestList = new ArrayList<>(List.of());

        int coOwnerListSize = nbt.getInt("coOwnerListSize");
        for (int i = 0; i<coOwnerListSize; i++) {
            this.coOwnerList.add(nbt.getString("coOwnerListEntry" + i));
        }

        int trustedListSize = nbt.getInt("trustedListSize");
        for (int i = 0; i<trustedListSize; i++) {
            this.trustedList.add(nbt.getString("trustedListEntry" + i));
        }

        int guestListSize = nbt.getInt("guestListSize");
        for (int i = 0; i<guestListSize; i++) {
            this.guestList.add(nbt.getString("guestListEntry" + i));
        }

        this.showRestrictBlockBreakingArea = nbt.getBoolean("showRestrictBlockBreakingArea");

        int i = MathHelper.clamp(nbt.getInt("restrictBlockBreakingAreaDimensionsX"), 0, 48);
        int j = MathHelper.clamp(nbt.getInt("restrictBlockBreakingAreaDimensionsY"), 0, 48);
        int k = MathHelper.clamp(nbt.getInt("restrictBlockBreakingAreaDimensionsZ"), 0, 48);
        this.restrictBlockBreakingAreaDimensions = new Vec3i(i, j, k);

        int l = MathHelper.clamp(nbt.getInt("restrictBlockBreakingAreaPositionOffsetX"), -48, 48);
        int m = MathHelper.clamp(nbt.getInt("restrictBlockBreakingAreaPositionOffsetY"), -48, 48);
        int n = MathHelper.clamp(nbt.getInt("restrictBlockBreakingAreaPositionOffsetZ"), -48, 48);
        this.restrictBlockBreakingAreaPositionOffset = new BlockPos(l, m, n);

        this.ownerMode = nbt.getInt("ownerMode");

        int o = MathHelper.clamp(nbt.getInt("triggeredBlockPositionOffsetX"), -48, 48);
        int p = MathHelper.clamp(nbt.getInt("triggeredBlockPositionOffsetY"), -48, 48);
        int q = MathHelper.clamp(nbt.getInt("triggeredBlockPositionOffsetZ"), -48, 48);
        this.triggeredBlockPositionOffset = new BlockPos(o, p, q);

        int entrance_X = nbt.getInt("entrance_X");
        int entrance_Y = nbt.getInt("entrance_Y");
        int entrance_Z = nbt.getInt("entrance_Z");
        this.entrance.setLeft(new BlockPos(entrance_X, entrance_Y, entrance_Z));
        double entrance_Yaw = nbt.getDouble("entrance_Yaw");
        double entrance_Pitch = nbt.getDouble("entrance_Pitch");
        this.entrance.setRight(new MutablePair<>(entrance_Yaw, entrance_Pitch));

        super.readNbt(nbt);
    }

    public BlockEntityUpdateS2CPacket toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    @Override
    public NbtCompound toInitialChunkDataNbt() {
        return this.createNbt();
    }

    public static void tick(World world, BlockPos pos, BlockState state, HousingBlockBlockEntity blockEntity) {
        if (world.getTime() % 20L == 0L) {
            if (blockEntity.hasWorld() && !blockEntity.isOwnerSet) {
                if (Objects.equals(blockEntity.owner, "") && blockEntity.ownerMode == 0) {
                    blockEntity.owner = initOwner(blockEntity.world);
                    BetterAdventureModeCore.LOGGER.info(blockEntity.owner);
                    blockEntity.isOwnerSet = true;
                }
            }

            Box box = new Box(
                    blockEntity.pos.getX() + blockEntity.restrictBlockBreakingAreaPositionOffset.getX(),
                    blockEntity.pos.getY() + blockEntity.restrictBlockBreakingAreaPositionOffset.getY(),
                    blockEntity.pos.getZ() + blockEntity.restrictBlockBreakingAreaPositionOffset.getZ(),
                    blockEntity.pos.getX() + blockEntity.restrictBlockBreakingAreaPositionOffset.getX() + blockEntity.restrictBlockBreakingAreaDimensions.getX(),
                    blockEntity.pos.getY() + blockEntity.restrictBlockBreakingAreaPositionOffset.getY() + blockEntity.restrictBlockBreakingAreaDimensions.getY(),
                    blockEntity.pos.getZ() + blockEntity.restrictBlockBreakingAreaPositionOffset.getZ() + blockEntity.restrictBlockBreakingAreaDimensions.getZ()
            );
            List<PlayerEntity> list = world.getNonSpectatingEntities(PlayerEntity.class, box);
            Iterator var11 = list.iterator();

            PlayerEntity playerEntity;
            while (var11.hasNext()) {
                playerEntity = (PlayerEntity) var11.next();

                String playerName = playerEntity.getName().getString();
                String playerUuid = playerEntity.getUuidAsString();
                if (Objects.equals(playerUuid, blockEntity.getOwner())/* || playerEntity.isCreative()*/) {
                    playerEntity.addStatusEffect(new StatusEffectInstance(StatusEffectsRegistry.HOUSING_OWNER_EFFECT, 100, 0, true, false, true));
                } else if (blockEntity.getCoOwnerList().contains(playerName)) {
                    playerEntity.addStatusEffect(new StatusEffectInstance(StatusEffectsRegistry.HOUSING_CO_OWNER_EFFECT, 100, 0, true, false, true));
                } else if (blockEntity.getTrustedList().contains(playerName)) {
                    playerEntity.addStatusEffect(new StatusEffectInstance(StatusEffectsRegistry.HOUSING_TRUSTED_EFFECT, 100, 0, true, false, true));
                } else if (blockEntity.getGuestList().contains(playerName)) {
                    playerEntity.addStatusEffect(new StatusEffectInstance(StatusEffectsRegistry.HOUSING_GUEST_EFFECT, 100, 0, true, false, true));
                } else {
                    playerEntity.addStatusEffect(new StatusEffectInstance(StatusEffectsRegistry.HOUSING_STRANGER_EFFECT, 100, 0, true, false, true));
                }
                ComponentsRegistry.CURRENT_HOUSING_BLOCK_POS.get(playerEntity).setValue(blockEntity.pos);
            }
        }
    }

    public String getOwner() {
        return this.owner;
    }

    public boolean setOwner(String owner) {
        this.owner = owner;
        return true;
    }

    public List<String> getCoOwnerList() {
        return this.coOwnerList;
    }

    public boolean setCoOwnerList(List<String> coOwnerList) {
        this.coOwnerList = coOwnerList;
        return true;
    }

    public List<String> getTrustedList() {
        return this.trustedList;
    }

    public boolean setTrustedList(List<String> trustedList) {
        this.trustedList = trustedList;
        return true;
    }

    public List<String> getGuestList() {
        return this.guestList;
    }

    public boolean setGuestList(List<String> guestList) {
        this.guestList = guestList;
        return true;
    }

    public boolean getShowRestrictBlockBreakingArea() {
        return this.showRestrictBlockBreakingArea;
    }

    // TODO check if input is valid
    public boolean setShowRestrictBlockBreakingArea(boolean showRestrictBlockBreakingArea) {
        this.showRestrictBlockBreakingArea = showRestrictBlockBreakingArea;
        return true;
    }

    public Vec3i getRestrictBlockBreakingAreaDimensions() {
        return this.restrictBlockBreakingAreaDimensions;
    }

    // TODO check if input is valid
    public boolean setRestrictBlockBreakingAreaDimensions(Vec3i restrictBlockBreakingAreaDimensions) {
        this.restrictBlockBreakingAreaDimensions = restrictBlockBreakingAreaDimensions;
        return true;
    }

    public BlockPos getRestrictBlockBreakingAreaPositionOffset() {
        return this.restrictBlockBreakingAreaPositionOffset;
    }

    // TODO check if input is valid
    public boolean setRestrictBlockBreakingAreaPositionOffset(BlockPos restrictBlockBreakingAreaPositionOffset) {
        this.restrictBlockBreakingAreaPositionOffset = restrictBlockBreakingAreaPositionOffset;
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

    public int getOwnerMode() {
        return this.ownerMode;
    }

    // TODO check if input is valid
    public boolean setOwnerMode(int ownerMode) {
        this.ownerMode = ownerMode;
        return true;
    }

    public MutablePair<BlockPos, MutablePair<Double, Double>> getEntrance() {
        return entrance;
    }

    public boolean setEntrance(MutablePair<BlockPos, MutablePair<Double, Double>> entrance) {
        this.entrance = entrance;
        return true;
    }

    public void setIsOwnerSet(boolean isOwnerSet) {
        this.isOwnerSet = isOwnerSet;
    }

    private static String initOwner(World world) {
        if (world != null) {
            String worldRegistryKey = world.getRegistryKey().getValue().getPath();
            String[] parts = worldRegistryKey.split("_");
            if (world.getServer() != null) {
                String uuidString = parts[0];
                if (UUIDUtilities.isStringValidUUID(uuidString)) {
                    PlayerEntity playerEntity = world.getServer().getPlayerManager().getPlayer(UUID.fromString(uuidString));
                    if (playerEntity != null) {
                        return playerEntity.getName().getString();
                    }
                }
            }
        }
        return "";
    }

    public boolean restrictBlockBreakingAreaContains(BlockPos pos) {
        return (double)(pos.getX() + 1) > (this.pos.getX() + this.restrictBlockBreakingAreaPositionOffset.getX())
                && (double)pos.getX() < (this.pos.getX() + this.restrictBlockBreakingAreaPositionOffset.getX() + this.restrictBlockBreakingAreaDimensions.getX())
                && (double)(pos.getY() + 1) > (this.pos.getY() + this.restrictBlockBreakingAreaPositionOffset.getY())
                && (double)pos.getY() < (this.pos.getY() + this.restrictBlockBreakingAreaPositionOffset.getY() + this.restrictBlockBreakingAreaDimensions.getY())
                && (double)(pos.getZ() + 1) > (this.pos.getZ() + this.restrictBlockBreakingAreaPositionOffset.getZ())
                && (double)pos.getZ() < (this.pos.getZ() + this.restrictBlockBreakingAreaPositionOffset.getZ() + this.restrictBlockBreakingAreaDimensions.getZ());
    }

    public void trigger() {
        if (this.world != null) {
            BlockEntity blockEntity = world.getBlockEntity(new BlockPos(this.pos.getX() + this.triggeredBlockPositionOffset.getX(), this.pos.getY() + this.triggeredBlockPositionOffset.getY(), this.pos.getZ() + this.triggeredBlockPositionOffset.getZ()));
            if (blockEntity instanceof Triggerable triggerable) {
                triggerable.trigger();
            }
        }
    }

    @Override
    protected void onRotate(BlockState state) {
        if (state.getBlock() instanceof RotatedBlockWithEntity) {
            if (state.get(RotatedBlockWithEntity.ROTATED) != this.rotated) {
                BlockRotation blockRotation = BlockRotationUtils.calculateRotationFromDifferentRotatedStates(state.get(RotatedBlockWithEntity.ROTATED), this.rotated);
                this.triggeredBlockPositionOffset = BlockRotationUtils.rotateOffsetBlockPos(this.triggeredBlockPositionOffset, blockRotation);
                this.restrictBlockBreakingAreaPositionOffset = BlockRotationUtils.rotateOffsetBlockPos(this.restrictBlockBreakingAreaPositionOffset, blockRotation);
                this.restrictBlockBreakingAreaDimensions = BlockRotationUtils.rotateOffsetVec3i(this.restrictBlockBreakingAreaDimensions, blockRotation);
                this.rotated = state.get(RotatedBlockWithEntity.ROTATED);
            }
            if (state.get(RotatedBlockWithEntity.X_MIRRORED) != this.x_mirrored) {
                this.triggeredBlockPositionOffset = BlockRotationUtils.mirrorOffsetBlockPos(this.triggeredBlockPositionOffset, BlockMirror.FRONT_BACK);
                this.restrictBlockBreakingAreaPositionOffset = BlockRotationUtils.mirrorOffsetBlockPos(this.restrictBlockBreakingAreaPositionOffset, BlockMirror.FRONT_BACK);
                this.restrictBlockBreakingAreaDimensions = BlockRotationUtils.mirrorOffsetVec3i(this.restrictBlockBreakingAreaDimensions, BlockMirror.FRONT_BACK);
                this.x_mirrored = state.get(RotatedBlockWithEntity.X_MIRRORED);
            }
            if (state.get(RotatedBlockWithEntity.Z_MIRRORED) != this.z_mirrored) {
                this.triggeredBlockPositionOffset = BlockRotationUtils.mirrorOffsetBlockPos(this.triggeredBlockPositionOffset, BlockMirror.LEFT_RIGHT);
                this.restrictBlockBreakingAreaPositionOffset = BlockRotationUtils.mirrorOffsetBlockPos(this.restrictBlockBreakingAreaPositionOffset, BlockMirror.LEFT_RIGHT);
                this.restrictBlockBreakingAreaDimensions = BlockRotationUtils.mirrorOffsetVec3i(this.restrictBlockBreakingAreaDimensions, BlockMirror.LEFT_RIGHT);
                this.z_mirrored = state.get(RotatedBlockWithEntity.Z_MIRRORED);
            }
        }
    }
}
