package com.github.theredbrain.betteradventuremode.block.entity;

import com.github.theredbrain.betteradventuremode.util.BlockRotationUtils;
import com.github.theredbrain.betteradventuremode.block.Resetable;
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
import java.util.HashMap;
import java.util.List;

public class TriggeredCounterBlockEntity extends RotatedBlockEntity implements Triggerable, Resetable {
    private HashMap<Integer, BlockPos> triggeredBlocks = new HashMap<>();
    private int counter = 0;
    public TriggeredCounterBlockEntity(BlockPos pos, BlockState state) {
        super(EntityRegistry.TRIGGERED_COUNTER_BLOCK_ENTITY, pos, state);
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        List<Integer> triggeredBlocksKeys = new ArrayList<>(this.triggeredBlocks.keySet());
        nbt.putInt("triggeredBlocksKeysSize", triggeredBlocksKeys.size());
        for (int i = 0; i < triggeredBlocksKeys.size(); i++) {
            int key = triggeredBlocksKeys.get(i);
            nbt.putInt("triggeredBlocks_key_" + i, key);
            nbt.putInt("triggeredBlocks_entry_X_" + i, this.triggeredBlocks.get(key).getX());
            nbt.putInt("triggeredBlocks_entry_Y_" + i, this.triggeredBlocks.get(key).getY());
            nbt.putInt("triggeredBlocks_entry_Z_" + i, this.triggeredBlocks.get(key).getZ());
        }

        nbt.putInt("counter", this.counter);

        super.writeNbt(nbt);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        this.triggeredBlocks.clear();
        int triggeredBlocksKeysSize = nbt.getInt("triggeredBlocksKeysSize");
        for (int i = 0; i < triggeredBlocksKeysSize; i++) {
            this.triggeredBlocks.put(nbt.getInt("triggeredBlocks_key_" + i), new BlockPos(
                    MathHelper.clamp(nbt.getInt("triggeredBlocks_entry_X_" + i), -48, 48),
                    MathHelper.clamp(nbt.getInt("triggeredBlocks_entry_Y_" + i), -48, 48),
                    MathHelper.clamp(nbt.getInt("triggeredBlocks_entry_Z_" + i), -48, 48)
            ));
        }

        this.counter = nbt.getInt("counter");

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
            ((DuckPlayerEntityMixin)player).betteradventuremode$openTriggeredCounterBlockScreen(this);
        }
        return true;
    }

    public HashMap<Integer, BlockPos> getTriggeredBlocks() {
        return triggeredBlocks;
    }

    public boolean setTriggeredBlocks(HashMap<Integer, BlockPos> triggeredBlocks) {
        this.triggeredBlocks = triggeredBlocks;
        return true;
    }

    public void trigger() {
        if (this.world != null) {
            this.counter++;
            if (this.triggeredBlocks.containsKey(this.counter)) {
                BlockPos blockPos = this.triggeredBlocks.get(this.counter);
                BlockEntity blockEntity = world.getBlockEntity(new BlockPos(this.pos.getX() + blockPos.getX(), this.pos.getY() + blockPos.getY(), this.pos.getZ() + blockPos.getZ()));
                if (blockEntity instanceof Triggerable triggerable && blockEntity != this) {
                    triggerable.trigger();
                }
            }
        }
    }

    @Override
    public void reset() {
        this.counter = 0;
    }

    @Override
    protected void onRotate(BlockState state) {
        if (state.getBlock() instanceof RotatedBlockWithEntity) {
            if (state.get(RotatedBlockWithEntity.ROTATED) != this.rotated) {
                BlockRotation blockRotation = BlockRotationUtils.calculateRotationFromDifferentRotatedStates(state.get(RotatedBlockWithEntity.ROTATED), this.rotated);
                List<Integer> keys = new ArrayList<>(this.triggeredBlocks.keySet());
                for (Integer key : keys) {
                    BlockPos oldBlockPos = this.triggeredBlocks.get(key);
                    this.triggeredBlocks.put(key, BlockRotationUtils.rotateOffsetBlockPos(oldBlockPos, blockRotation));
                }
                this.rotated = state.get(RotatedBlockWithEntity.ROTATED);
            }
            if (state.get(RotatedBlockWithEntity.X_MIRRORED) != this.x_mirrored) {
                List<Integer> keys = new ArrayList<>(this.triggeredBlocks.keySet());
                for (Integer key : keys) {
                    BlockPos oldBlockPos = this.triggeredBlocks.get(key);
                    this.triggeredBlocks.put(key, BlockRotationUtils.mirrorOffsetBlockPos(oldBlockPos, BlockMirror.FRONT_BACK));
                }
                this.x_mirrored = state.get(RotatedBlockWithEntity.X_MIRRORED);
            }
            if (state.get(RotatedBlockWithEntity.Z_MIRRORED) != this.z_mirrored) {
                List<Integer> keys = new ArrayList<>(this.triggeredBlocks.keySet());
                for (Integer key : keys) {
                    BlockPos oldBlockPos = this.triggeredBlocks.get(key);
                    this.triggeredBlocks.put(key, BlockRotationUtils.mirrorOffsetBlockPos(oldBlockPos, BlockMirror.LEFT_RIGHT));
                }
                this.z_mirrored = state.get(RotatedBlockWithEntity.Z_MIRRORED);
            }
        }
    }
}
