package com.github.theredbrain.bamcore.block.entity;

import com.github.theredbrain.bamcore.api.util.BlockRotationUtils;
import com.github.theredbrain.bamcore.block.RotatedBlockWithEntity;
import com.github.theredbrain.bamcore.registry.EntityRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.BlockPos;
import org.apache.commons.lang3.tuple.MutablePair;

import java.util.HashMap;
import java.util.Map;

public class DungeonControlBlockEntity extends RotatedBlockEntity {
    private MutablePair<BlockPos, MutablePair<Double, Double>> mainEntrance = new MutablePair<>(new BlockPos(0, 1, 0), new MutablePair<>(0.0, 0.0));
    private HashMap<String, MutablePair<BlockPos, MutablePair<Double, Double>>> sideEntrances = new HashMap<>(Map.of());
//    private BlockPos triggeredBlockPositionOffset = new BlockPos(0, 1, 0);
    public DungeonControlBlockEntity(BlockPos pos, BlockState state) {
        super(EntityRegistry.DUNGEON_CONTROL_BLOCK_ENTITY, pos, state);
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {

        nbt.putInt("mainEntrance_X", this.mainEntrance.getLeft().getX());
        nbt.putInt("mainEntrance_Y", this.mainEntrance.getLeft().getY());
        nbt.putInt("mainEntrance_Z", this.mainEntrance.getLeft().getZ());
        nbt.putDouble("mainEntrance_Yaw", this.mainEntrance.getRight().getLeft());
        nbt.putDouble("mainEntrance_Pitch", this.mainEntrance.getRight().getRight());

//        int sideEntrancesSize = this.sideEntrances.keySet().size();
//        nbt.putInt("sideEntrancesSize", sideEntrancesSize);
//        for (int i = 0; i < sideEntrancesSize; i++) {
//
////            Set test;
//            String key = this.sideEntrances.keySet().get(i).getLeft();
//            nbt.putInt("sideEntrance_" + i + "_X", this.sideEntrances.get(i).getLeft().getX());
//            nbt.putInt("sideEntrance_" + i + "_Y", this.sideEntrances.get(i).getLeft().getY());
//            nbt.putInt("sideEntrance_" + i + "_Z", this.sideEntrances.get(i).getLeft().getZ());
//            nbt.putDouble("sideEntrance_" + i + "_Yaw", this.sideEntrances.get(i).getRight().getLeft());
//            nbt.putDouble("sideEntrance_" + i + "_Pitch", this.sideEntrances.get(i).getRight().getRight());
//        }

//        nbt.putInt("triggeredBlockPositionOffsetX", this.triggeredBlockPositionOffset.getX());
//        nbt.putInt("triggeredBlockPositionOffsetY", this.triggeredBlockPositionOffset.getY());
//        nbt.putInt("triggeredBlockPositionOffsetZ", this.triggeredBlockPositionOffset.getZ());

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

//        int sideEntrancesSize = nbt.getInt("sideEntrancesSize");
//        this.sideEntrances = new HashMap<>(Map.of());
//        for (int i = 0; i < sideEntrancesSize; i++) {
//
//            int sideEntrance_X = nbt.getInt("sideEntrance_" + i + "_X");
//            int sideEntrance_Y = nbt.getInt("sideEntrance_" + i + "_Y");
//            int sideEntrance_Z = nbt.getInt("sideEntrance_" + i + "_Z");
//            this.mainEntrance.setLeft(new BlockPos(mainEntrance_X, mainEntrance_Y, mainEntrance_Z));
//            double sideEntrance_Yaw = nbt.getDouble("sideEntrance_" + i + "_Yaw");
//            double sideEntrance_Pitch = nbt.getDouble("sideEntrance_" + i + "_Pitch");
//            this.mainEntrance.setRight(new MutablePair<>(mainEntrance_Yaw, mainEntrance_Pitch));
//            this.sideEntrances.put()
//            nbt.putInt("sideEntrance_" + i + "_X", this.sideEntrances.get(i).getLeft().getX());
//            nbt.putInt("sideEntrance_" + i + "_Y", this.sideEntrances.get(i).getLeft().getY());
//            nbt.putInt("sideEntrance_" + i + "_Z", this.sideEntrances.get(i).getLeft().getZ());
//            nbt.putDouble("sideEntrance_" + i + "Yaw", this.sideEntrances.get(i).getRight().getLeft());
//            nbt.putDouble("sideEntrance_" + i + "Pitch", this.sideEntrances.get(i).getRight().getRight());
//        }

//        int o = MathHelper.clamp(nbt.getInt("triggeredBlockPositionOffsetX"), -48, 48);
//        int p = MathHelper.clamp(nbt.getInt("triggeredBlockPositionOffsetY"), -48, 48);
//        int q = MathHelper.clamp(nbt.getInt("triggeredBlockPositionOffsetZ"), -48, 48);
//        this.triggeredBlockPositionOffset = new BlockPos(o, p, q);

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
        this.sideEntrances = sideEntrances;
        return true;
    }

//    public void trigger() {
//        if (this.world != null) {
//            BlockEntity blockEntity = world.getBlockEntity(new BlockPos(this.pos.getX() + this.triggeredBlockPositionOffset.getX(), this.pos.getY() + this.triggeredBlockPositionOffset.getY(), this.pos.getZ() + this.triggeredBlockPositionOffset.getZ()));
//            if (blockEntity instanceof Triggerable triggerable) {
//                triggerable.trigger();
//            }
//        }
//    }

    @Override
    protected void onRotate(BlockState state) {
        if (state.getBlock() instanceof RotatedBlockWithEntity) {
            if (state.get(RotatedBlockWithEntity.ROTATED) != this.rotated) {
                BlockRotation blockRotation = BlockRotationUtils.calculateRotationFromDifferentRotatedStates(state.get(RotatedBlockWithEntity.ROTATED), this.rotated);
//                this.triggeredBlockPositionOffset = BlockRotationUtils.rotateOffsetBlockPos(this.triggeredBlockPositionOffset, blockRotation);
                this.mainEntrance = BlockRotationUtils.rotateEntrance(this.mainEntrance, blockRotation);
                this.rotated = state.get(RotatedBlockWithEntity.ROTATED);
            }
            if (state.get(RotatedBlockWithEntity.X_MIRRORED) != this.x_mirrored) {
//                this.triggeredBlockPositionOffset = BlockRotationUtils.mirrorOffsetBlockPos(this.triggeredBlockPositionOffset, BlockMirror.FRONT_BACK);

                this.mainEntrance = BlockRotationUtils.mirrorEntrance(this.mainEntrance, BlockMirror.FRONT_BACK);
                this.x_mirrored = state.get(RotatedBlockWithEntity.X_MIRRORED);
            }
            if (state.get(RotatedBlockWithEntity.Z_MIRRORED) != this.z_mirrored) {
//                this.triggeredBlockPositionOffset = BlockRotationUtils.mirrorOffsetBlockPos(this.triggeredBlockPositionOffset, BlockMirror.LEFT_RIGHT);

                this.mainEntrance = BlockRotationUtils.mirrorEntrance(this.mainEntrance, BlockMirror.LEFT_RIGHT);
                this.z_mirrored = state.get(RotatedBlockWithEntity.Z_MIRRORED);
            }
        }
    }
}
