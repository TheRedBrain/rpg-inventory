package com.github.theredbrain.betteradventuremode.block.entity;

import com.github.theredbrain.betteradventuremode.util.BlockRotationUtils;
import com.github.theredbrain.betteradventuremode.block.RotatedBlockWithEntity;
import com.github.theredbrain.betteradventuremode.entity.player.DuckPlayerEntityMixin;
import com.github.theredbrain.betteradventuremode.registry.EntityRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.BlockPos;
import org.apache.commons.lang3.tuple.MutablePair;

public class EntranceDelegationBlockEntity extends RotatedBlockEntity {
    private MutablePair<BlockPos, MutablePair<Double, Double>> delegatedEntrance = new MutablePair<>(new BlockPos(0, 1, 0), new MutablePair<>(0.0, 0.0));

    public EntranceDelegationBlockEntity(BlockPos pos, BlockState state) {
        super(EntityRegistry.ENTRANCE_DELEGATION_BLOCK_ENTITY, pos, state);
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {

        nbt.putInt("delegatedEntrance_X", this.delegatedEntrance.getLeft().getX());
        nbt.putInt("delegatedEntrance_Y", this.delegatedEntrance.getLeft().getY());
        nbt.putInt("delegatedEntrance_Z", this.delegatedEntrance.getLeft().getZ());
        nbt.putDouble("delegatedEntrance_Yaw", this.delegatedEntrance.getRight().getLeft());
        nbt.putDouble("delegatedEntrance_Pitch", this.delegatedEntrance.getRight().getRight());

        super.writeNbt(nbt);

    }

    @Override
    public void readNbt(NbtCompound nbt) {

        int delegatedEntrance_X = nbt.getInt("delegatedEntrance_X");
        int delegatedEntrance_Y = nbt.getInt("delegatedEntrance_Y");
        int delegatedEntrance_Z = nbt.getInt("delegatedEntrance_Z");
        this.delegatedEntrance.setLeft(new BlockPos(delegatedEntrance_X, delegatedEntrance_Y, delegatedEntrance_Z));
        double delegatedEntrance_Yaw = nbt.getDouble("delegatedEntrance_Yaw");
        double delegatedEntrance_Pitch = nbt.getDouble("delegatedEntrance_Pitch");
        this.delegatedEntrance.setRight(new MutablePair<>(delegatedEntrance_Yaw, delegatedEntrance_Pitch));

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
            ((DuckPlayerEntityMixin)player).betteradventuremode$openEntranceDelegationBlockScreen(this);
        }
        return true;
    }

    public MutablePair<BlockPos, MutablePair<Double, Double>> getDelegatedEntrance() {
        return this.delegatedEntrance;
    }

    public boolean setDelegatedEntrance(MutablePair<BlockPos, MutablePair<Double, Double>> delegatedEntrance) {
        this.delegatedEntrance = delegatedEntrance;
        return true;
    }

    @Override
    protected void onRotate(BlockState state) {
        if (state.getBlock() instanceof RotatedBlockWithEntity) {
            if (state.get(RotatedBlockWithEntity.ROTATED) != this.rotated) {
                BlockRotation blockRotation = BlockRotationUtils.calculateRotationFromDifferentRotatedStates(state.get(RotatedBlockWithEntity.ROTATED), this.rotated);
                this.delegatedEntrance = BlockRotationUtils.rotateEntrance(this.delegatedEntrance, blockRotation);

                this.rotated = state.get(RotatedBlockWithEntity.ROTATED);
            }
            if (state.get(RotatedBlockWithEntity.X_MIRRORED) != this.x_mirrored) {
                this.delegatedEntrance = BlockRotationUtils.mirrorEntrance(this.delegatedEntrance, BlockMirror.FRONT_BACK);

                this.x_mirrored = state.get(RotatedBlockWithEntity.X_MIRRORED);
            }
            if (state.get(RotatedBlockWithEntity.Z_MIRRORED) != this.z_mirrored) {
                this.delegatedEntrance = BlockRotationUtils.mirrorEntrance(this.delegatedEntrance, BlockMirror.LEFT_RIGHT);

                this.z_mirrored = state.get(RotatedBlockWithEntity.Z_MIRRORED);
            }
        }
    }
}
