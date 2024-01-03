package com.github.theredbrain.betteradventuremode.block.entity;

import com.github.theredbrain.betteradventuremode.api.util.BlockRotationUtils;
import com.github.theredbrain.betteradventuremode.block.RotatedBlockWithEntity;
import com.github.theredbrain.betteradventuremode.block.Triggerable;
import com.github.theredbrain.betteradventuremode.entity.player.DuckPlayerEntityMixin;
import com.github.theredbrain.betteradventuremode.registry.EntityRegistry;
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

public class RelayTriggerBlockBlockEntity extends RotatedBlockEntity implements Triggerable {
    private List<BlockPos> triggeredBlocks = new ArrayList<>(List.of());
    public RelayTriggerBlockBlockEntity(BlockPos pos, BlockState state) {
        super(EntityRegistry.RELAY_TRIGGER_BLOCK_ENTITY, pos, state);
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        nbt.putInt("triggeredBlocksSize", triggeredBlocks.size());
        for (int i = 0; i < this.triggeredBlocks.size(); i++) {
            BlockPos triggeredBlock = this.triggeredBlocks.get(i);
            nbt.putInt("triggeredBlockPositionOffsetX_" + i, triggeredBlock.getX());
            nbt.putInt("triggeredBlockPositionOffsetY_" + i, triggeredBlock.getY());
            nbt.putInt("triggeredBlockPositionOffsetZ_" + i, triggeredBlock.getZ());
        }

        super.writeNbt(nbt);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        int triggeredBlocksSize = nbt.getInt("triggeredBlocksSize");
        this.triggeredBlocks = new ArrayList<>(List.of());
        for (int i = 0; i < triggeredBlocksSize; i++) {
            int l = MathHelper.clamp(nbt.getInt("triggeredBlockPositionOffsetX_" + i), -48, 48);
            int m = MathHelper.clamp(nbt.getInt("triggeredBlockPositionOffsetY_" + i), -48, 48);
            int n = MathHelper.clamp(nbt.getInt("triggeredBlockPositionOffsetZ_" + i), -48, 48);
            this.triggeredBlocks.add(new BlockPos(l, m, n));
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
            ((DuckPlayerEntityMixin)player).bamcore$openRelayTriggerBlockScreen(this);
        }
        return true;
    }

    public List<BlockPos> getTriggeredBlocks() {
        return triggeredBlocks;
    }

    // TODO check if input is valid
    public boolean setTriggeredBlocks(List<BlockPos> triggeredBlocks) {
        this.triggeredBlocks = triggeredBlocks;
        return true;
    }

    public void trigger() {
        if (this.world != null) {
            BlockEntity blockEntity;
            for (BlockPos triggeredBlock : this.triggeredBlocks) {
                blockEntity = world.getBlockEntity(new BlockPos(this.pos.getX() + triggeredBlock.getX(), this.pos.getY() + triggeredBlock.getY(), this.pos.getZ() + triggeredBlock.getZ()));
                if (blockEntity instanceof Triggerable triggerable && blockEntity != this) {
                    triggerable.trigger();
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
                for (BlockPos triggeredBlock : this.triggeredBlocks) {
                    newTriggeredBlocks.add(BlockRotationUtils.rotateOffsetBlockPos(triggeredBlock, blockRotation));
                }
                this.triggeredBlocks = newTriggeredBlocks;
                this.rotated = state.get(RotatedBlockWithEntity.ROTATED);
            }
            if (state.get(RotatedBlockWithEntity.X_MIRRORED) != this.x_mirrored) {
                List<BlockPos> newTriggeredBlocks = new ArrayList<>(List.of());
                for (BlockPos triggeredBlock : this.triggeredBlocks) {
                    newTriggeredBlocks.add(BlockRotationUtils.mirrorOffsetBlockPos(triggeredBlock, BlockMirror.FRONT_BACK));
                }
                this.triggeredBlocks = newTriggeredBlocks;
                this.x_mirrored = state.get(RotatedBlockWithEntity.X_MIRRORED);
            }
            if (state.get(RotatedBlockWithEntity.Z_MIRRORED) != this.z_mirrored) {
                List<BlockPos> newTriggeredBlocks = new ArrayList<>(List.of());
                for (BlockPos triggeredBlock : this.triggeredBlocks) {
                    newTriggeredBlocks.add(BlockRotationUtils.mirrorOffsetBlockPos(triggeredBlock, BlockMirror.LEFT_RIGHT));
                }
                this.triggeredBlocks = newTriggeredBlocks;
                this.z_mirrored = state.get(RotatedBlockWithEntity.Z_MIRRORED);
            }
        }
    }
}
