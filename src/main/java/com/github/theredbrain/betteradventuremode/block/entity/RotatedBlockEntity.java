package com.github.theredbrain.betteradventuremode.block.entity;

import com.github.theredbrain.betteradventuremode.block.RotatedBlockWithEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;

public abstract class RotatedBlockEntity extends BlockEntity {
    protected int rotated = 0;
    protected boolean x_mirrored = false;
    protected boolean z_mirrored = false;
    public RotatedBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);

        nbt.putInt("rotated", this.rotated);
        nbt.putBoolean("x_mirrored", this.x_mirrored);
        nbt.putBoolean("z_mirrored", this.z_mirrored);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);

        this.rotated = MathHelper.clamp(nbt.getInt("rotated"), 0, 3);
        this.x_mirrored = nbt.getBoolean("x_mirrored");
        this.z_mirrored = nbt.getBoolean("z_mirrored");
        if (this.getCachedState().getBlock() instanceof RotatedBlockWithEntity) {
            this.onRotate(this.getCachedState());
        }
    }

    protected abstract void onRotate(BlockState state);
}
