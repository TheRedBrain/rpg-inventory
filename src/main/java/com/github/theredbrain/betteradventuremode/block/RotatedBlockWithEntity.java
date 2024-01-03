package com.github.theredbrain.betteradventuremode.block;

import com.github.theredbrain.betteradventuremode.api.util.BlockRotationUtils;
import com.mojang.serialization.MapCodec;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;

public abstract class RotatedBlockWithEntity extends BlockWithEntity {
    public static final IntProperty ROTATED = IntProperty.of("rotated", 0, 3);
    public static final BooleanProperty X_MIRRORED = BooleanProperty.of("x_mirrored");
    public static final BooleanProperty Z_MIRRORED = BooleanProperty.of("z_mirrored");
    public RotatedBlockWithEntity(Settings settings) {
        super(settings);
        this.setDefaultState(this.stateManager.getDefaultState().with(ROTATED, 0).with(X_MIRRORED, false).with(Z_MIRRORED, false));
    }

    protected abstract MapCodec<? extends RotatedBlockWithEntity> getCodec();

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(ROTATED, X_MIRRORED, Z_MIRRORED);
    }

    @Override
    public BlockState rotate(BlockState state, BlockRotation rotation) {
        return state.with(RotatedBlockWithEntity.ROTATED, BlockRotationUtils.calculateNewRotatedBlockState(state.get(RotatedBlockWithEntity.ROTATED), rotation));
    }

    @Override
    public BlockState mirror(BlockState state, BlockMirror mirror) {
        if (mirror == BlockMirror.FRONT_BACK) {
            return state.with(RotatedBlockWithEntity.X_MIRRORED, !state.get(RotatedBlockWithEntity.X_MIRRORED));
        } else if (mirror == BlockMirror.LEFT_RIGHT) {
            return state.with(RotatedBlockWithEntity.Z_MIRRORED, !state.get(RotatedBlockWithEntity.Z_MIRRORED));
        }
        return state;
    }
}
