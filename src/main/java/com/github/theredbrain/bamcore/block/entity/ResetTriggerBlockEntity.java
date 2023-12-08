package com.github.theredbrain.bamcore.block.entity;

import com.github.theredbrain.bamcore.api.util.BlockRotationUtils;
import com.github.theredbrain.bamcore.block.Resetable;
import com.github.theredbrain.bamcore.block.RotatedBlockWithEntity;
import com.github.theredbrain.bamcore.block.Triggerable;
import com.github.theredbrain.bamcore.entity.player.DuckPlayerEntityMixin;
import com.github.theredbrain.bamcore.registry.EntityRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;

import java.util.ArrayList;
import java.util.List;

public class ResetTriggerBlockEntity extends RotatedBlockEntity implements Triggerable {
    private List<BlockPos> resetBlocks = new ArrayList<>(List.of());
    public ResetTriggerBlockEntity(BlockPos pos, BlockState state) {
        super(EntityRegistry.RESET_TRIGGER_BLOCK_ENTITY, pos, state);
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        nbt.putInt("resetBlocksSize", resetBlocks.size());
        for (int i = 0; i < this.resetBlocks.size(); i++) {
            BlockPos resetBlock = this.resetBlocks.get(i);
            nbt.putInt("resetBlockPositionOffsetX_" + i, resetBlock.getX());
            nbt.putInt("resetBlockPositionOffsetY_" + i, resetBlock.getY());
            nbt.putInt("resetBlockPositionOffsetZ_" + i, resetBlock.getZ());
        }

        super.writeNbt(nbt);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        int resetBlocksSize = nbt.getInt("resetBlocksSize");
        this.resetBlocks = new ArrayList<>(List.of());
        for (int i = 0; i < resetBlocksSize; i++) {
            int l = MathHelper.clamp(nbt.getInt("resetBlockPositionOffsetX_" + i), -48, 48);
            int m = MathHelper.clamp(nbt.getInt("resetBlockPositionOffsetY_" + i), -48, 48);
            int n = MathHelper.clamp(nbt.getInt("resetBlockPositionOffsetZ_" + i), -48, 48);
            this.resetBlocks.add(new BlockPos(l, m, n));
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
            ((DuckPlayerEntityMixin)player).bamcore$openResetTriggerBlockScreen(this);
        }
        return true;
    }

    public List<BlockPos> getResetBlocks() {
        return resetBlocks;
    }

    // TODO check if input is valid
    public boolean setResetBlocks(List<BlockPos> resetBlocks) {
        this.resetBlocks = resetBlocks;
        return true;
    }

    public void trigger() {
        if (this.world != null) {
            BlockEntity blockEntity;
            for (BlockPos resetBlock : this.resetBlocks) {
                blockEntity = world.getBlockEntity(new BlockPos(this.pos.getX() + resetBlock.getX(), this.pos.getY() + resetBlock.getY(), this.pos.getZ() + resetBlock.getZ()));
                if (blockEntity instanceof Resetable resetable && blockEntity != this) {
                    resetable.reset();
                }
            }
        }
    }

    @Override
    protected void onRotate(BlockState state) {
        if (state.getBlock() instanceof RotatedBlockWithEntity) {
            if (state.get(RotatedBlockWithEntity.ROTATED) != this.rotated) {
                BlockRotation blockRotation = BlockRotationUtils.calculateRotationFromDifferentRotatedStates(state.get(RotatedBlockWithEntity.ROTATED), this.rotated);
                List<BlockPos> newTriggeredBlocks = new ArrayList<>(List.of());
                for (BlockPos resetBlock : this.resetBlocks) {
                    newTriggeredBlocks.add(BlockRotationUtils.rotateOffsetBlockPos(resetBlock, blockRotation));
                }
                this.resetBlocks = newTriggeredBlocks;
                this.rotated = state.get(RotatedBlockWithEntity.ROTATED);
            }
            if (state.get(RotatedBlockWithEntity.X_MIRRORED) != this.x_mirrored) {
                List<BlockPos> newTriggeredBlocks = new ArrayList<>(List.of());
                for (BlockPos resetBlock : this.resetBlocks) {
                    newTriggeredBlocks.add(BlockRotationUtils.mirrorOffsetBlockPos(resetBlock, BlockMirror.FRONT_BACK));
                }
                this.resetBlocks = newTriggeredBlocks;
                this.x_mirrored = state.get(RotatedBlockWithEntity.X_MIRRORED);
            }
            if (state.get(RotatedBlockWithEntity.Z_MIRRORED) != this.z_mirrored) {
                List<BlockPos> newTriggeredBlocks = new ArrayList<>(List.of());
                for (BlockPos resetBlock : this.resetBlocks) {
                    newTriggeredBlocks.add(BlockRotationUtils.mirrorOffsetBlockPos(resetBlock, BlockMirror.LEFT_RIGHT));
                }
                this.resetBlocks = newTriggeredBlocks;
                this.z_mirrored = state.get(RotatedBlockWithEntity.Z_MIRRORED);
            }
        }
    }
}
