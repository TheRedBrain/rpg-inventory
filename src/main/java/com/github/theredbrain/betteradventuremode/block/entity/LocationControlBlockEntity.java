package com.github.theredbrain.betteradventuremode.block.entity;

import com.github.theredbrain.betteradventuremode.api.util.BlockRotationUtils;
import com.github.theredbrain.betteradventuremode.block.RotatedBlockWithEntity;
import com.github.theredbrain.betteradventuremode.block.Triggerable;
import com.github.theredbrain.betteradventuremode.registry.EntityRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import org.apache.commons.lang3.tuple.MutablePair;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LocationControlBlockEntity extends RotatedBlockEntity {
    private MutablePair<BlockPos, MutablePair<Double, Double>> mainEntrance = new MutablePair<>(new BlockPos(0, 1, 0), new MutablePair<>(0.0, 0.0));
    private HashMap<String, MutablePair<BlockPos, MutablePair<Double, Double>>> sideEntrances = new HashMap<>(Map.of());
    private BlockPos triggeredBlockPositionOffset = new BlockPos(0, 1, 0);
    public LocationControlBlockEntity(BlockPos pos, BlockState state) {
        super(EntityRegistry.LOCATION_CONTROL_BLOCK_ENTITY, pos, state);
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {

        nbt.putInt("mainEntrance_X", this.mainEntrance.getLeft().getX());
        nbt.putInt("mainEntrance_Y", this.mainEntrance.getLeft().getY());
        nbt.putInt("mainEntrance_Z", this.mainEntrance.getLeft().getZ());
        nbt.putDouble("mainEntrance_Yaw", this.mainEntrance.getRight().getLeft());
        nbt.putDouble("mainEntrance_Pitch", this.mainEntrance.getRight().getRight());

        List<String> keyList = this.sideEntrances.keySet().stream().toList();
        int sideEntrancesSize = this.sideEntrances.keySet().size();
        nbt.putInt("sideEntrancesSize", sideEntrancesSize);
        for (int i = 0; i < sideEntrancesSize; i++) {
            String key = keyList.get(i);
            nbt.putString("key_" + i, key);
            nbt.putInt("sideEntrance_" + i + "_X", this.sideEntrances.get(key).getLeft().getX());
            nbt.putInt("sideEntrance_" + i + "_Y", this.sideEntrances.get(key).getLeft().getY());
            nbt.putInt("sideEntrance_" + i + "_Z", this.sideEntrances.get(key).getLeft().getZ());
            nbt.putDouble("sideEntrance_" + i + "_Yaw", this.sideEntrances.get(key).getRight().getLeft());
            nbt.putDouble("sideEntrance_" + i + "_Pitch", this.sideEntrances.get(key).getRight().getRight());
        }

        nbt.putInt("triggeredBlockPositionOffsetX", this.triggeredBlockPositionOffset.getX());
        nbt.putInt("triggeredBlockPositionOffsetY", this.triggeredBlockPositionOffset.getY());
        nbt.putInt("triggeredBlockPositionOffsetZ", this.triggeredBlockPositionOffset.getZ());

        super.writeNbt(nbt);
    }

    @Override
    public void readNbt(NbtCompound nbt) {

        int mainEntrance_X = nbt.getInt("mainEntrance_X");
        int mainEntrance_Y = nbt.getInt("mainEntrance_Y");
        int mainEntrance_Z = nbt.getInt("mainEntrance_Z");
        this.mainEntrance.setLeft(new BlockPos(mainEntrance_X, mainEntrance_Y, mainEntrance_Z));
        double mainEntrance_Yaw = nbt.getDouble("mainEntrance_Yaw");
        double mainEntrance_Pitch = nbt.getDouble("mainEntrance_Pitch");
        this.mainEntrance.setRight(new MutablePair<>(mainEntrance_Yaw, mainEntrance_Pitch));

        int sideEntrancesSize = nbt.getInt("sideEntrancesSize");
        this.sideEntrances = new HashMap<>(Map.of());
        for (int i = 0; i < sideEntrancesSize; i++) {
            String key = nbt.getString("key_" + i);
            int sideEntranceX = nbt.getInt("sideEntrance_" + i + "_X");
            int sideEntranceY = nbt.getInt("sideEntrance_" + i + "_Y");
            int sideEntranceZ = nbt.getInt("sideEntrance_" + i + "_Z");
            double sideEntranceYaw = nbt.getDouble("sideEntrance_" + i + "_Yaw");
            double sideEntrancePitch = nbt.getDouble("sideEntrance_" + i + "_Pitch");
            this.sideEntrances.put(key, new MutablePair<>(new BlockPos(sideEntranceX, sideEntranceY, sideEntranceZ), new MutablePair<>(sideEntranceYaw, sideEntrancePitch)));
        }

        int o = MathHelper.clamp(nbt.getInt("triggeredBlockPositionOffsetX"), -48, 48);
        int p = MathHelper.clamp(nbt.getInt("triggeredBlockPositionOffsetY"), -48, 48);
        int q = MathHelper.clamp(nbt.getInt("triggeredBlockPositionOffsetZ"), -48, 48);
        this.triggeredBlockPositionOffset = new BlockPos(o, p, q);

        super.readNbt(nbt);
    }

    public BlockEntityUpdateS2CPacket toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    @Override
    public NbtCompound toInitialChunkDataNbt() {
        return this.createNbt();
    }

    public MutablePair<BlockPos, MutablePair<Double, Double>> getMainEntrance() {
        return mainEntrance;
    }

    public boolean setMainEntrance(MutablePair<BlockPos, MutablePair<Double, Double>> mainEntrance) {
        this.mainEntrance = mainEntrance;
        return true;
    }

    public MutablePair<BlockPos, MutablePair<Double, Double>> getTargetEntrance(String entrance) {
        MutablePair<BlockPos, MutablePair<Double, Double>> targetEntrance;
        if (!entrance.equals("") && sideEntrances.containsKey(entrance)) {
            MutablePair<BlockPos, MutablePair<Double, Double>> targetEntranceOffset = this.sideEntrances.get(entrance);
            targetEntrance = new MutablePair<>(new BlockPos(targetEntranceOffset.getLeft().getX() + this.getPos().getX(), targetEntranceOffset.getLeft().getY() + this.getPos().getY(), targetEntranceOffset.getLeft().getZ() + this.getPos().getZ()), targetEntranceOffset.getRight());
        } else {
            targetEntrance = new MutablePair<>(new BlockPos(this.mainEntrance.getLeft().getX() + this.getPos().getX(), this.mainEntrance.getLeft().getY() + this.getPos().getY(), this.mainEntrance.getLeft().getZ() + this.getPos().getZ()), this.mainEntrance.getRight());
        }
        return targetEntrance;
    }

    public HashMap<String, MutablePair<BlockPos, MutablePair<Double, Double>>> getSideEntrances() {
        return sideEntrances;
    }

    public boolean setSideEntrances(HashMap<String, MutablePair<BlockPos, MutablePair<Double, Double>>> sideEntrances) {
        this.sideEntrances.clear();
        this.sideEntrances.putAll(sideEntrances);
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
                this.mainEntrance = BlockRotationUtils.rotateEntrance(this.mainEntrance, blockRotation);

                List<String> keyList = this.sideEntrances.keySet().stream().toList();
                int sideEntrancesSize = this.sideEntrances.keySet().size();
                for (int i = 0; i < sideEntrancesSize; i++) {
                    String key = keyList.get(i);
                    MutablePair<BlockPos, MutablePair<Double, Double>> rotatedEntrance = BlockRotationUtils.rotateEntrance(this.sideEntrances.get(key), blockRotation);
                    this.sideEntrances.put(key, rotatedEntrance);
                }

                this.rotated = state.get(RotatedBlockWithEntity.ROTATED);
            }
            if (state.get(RotatedBlockWithEntity.X_MIRRORED) != this.x_mirrored) {
                this.mainEntrance = BlockRotationUtils.mirrorEntrance(this.mainEntrance, BlockMirror.FRONT_BACK);

                List<String> keyList = this.sideEntrances.keySet().stream().toList();
                int sideEntrancesSize = this.sideEntrances.keySet().size();
                for (int i = 0; i < sideEntrancesSize; i++) {
                    String key = keyList.get(i);
                    MutablePair<BlockPos, MutablePair<Double, Double>> mirroredEntrance = BlockRotationUtils.mirrorEntrance(this.sideEntrances.get(key), BlockMirror.FRONT_BACK);
                    this.sideEntrances.put(key, mirroredEntrance);
                }

                this.x_mirrored = state.get(RotatedBlockWithEntity.X_MIRRORED);
            }
            if (state.get(RotatedBlockWithEntity.Z_MIRRORED) != this.z_mirrored) {
                this.mainEntrance = BlockRotationUtils.mirrorEntrance(this.mainEntrance, BlockMirror.LEFT_RIGHT);

                List<String> keyList = this.sideEntrances.keySet().stream().toList();
                int sideEntrancesSize = this.sideEntrances.keySet().size();
                for (int i = 0; i < sideEntrancesSize; i++) {
                    String key = keyList.get(i);
                    MutablePair<BlockPos, MutablePair<Double, Double>> mirroredEntrance = BlockRotationUtils.mirrorEntrance(this.sideEntrances.get(key), BlockMirror.LEFT_RIGHT);
                    this.sideEntrances.put(key, mirroredEntrance);
                }

                this.z_mirrored = state.get(RotatedBlockWithEntity.Z_MIRRORED);
            }
        }
    }
}
