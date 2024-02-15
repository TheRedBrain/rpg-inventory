package com.github.theredbrain.betteradventuremode.block;

import com.github.theredbrain.betteradventuremode.block.entity.JigsawPlacerBlockBlockEntity;
import com.mojang.serialization.MapCodec;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.enums.Orientation;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class JigsawPlacerBlock extends RotatedBlockWithEntity implements OperatorBlock {
    public static final EnumProperty<Orientation> ORIENTATION = Properties.ORIENTATION;
    public JigsawPlacerBlock(Settings settings) {
        super(settings);
        this.setDefaultState((BlockState)((BlockState)this.stateManager.getDefaultState()).with(ROTATED, 0).with(X_MIRRORED, false).with(Z_MIRRORED, false).with(ORIENTATION, Orientation.NORTH_UP));
    }

    // TODO Block Codecs
    public MapCodec<JigsawPlacerBlock> getCodec() {
        return null;
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(ORIENTATION);
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        Direction direction = ctx.getSide();
        Direction direction2 = direction.getAxis() == Direction.Axis.Y ? ctx.getHorizontalPlayerFacing().getOpposite() : Direction.UP;
        return (BlockState)this.getDefaultState().with(ORIENTATION, Orientation.byDirections(direction, direction2));
    }

    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new JigsawPlacerBlockBlockEntity(pos, state);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof JigsawPlacerBlockBlockEntity) {
            return ((JigsawPlacerBlockBlockEntity)blockEntity).openScreen(player) ? ActionResult.success(world.isClient) : ActionResult.PASS;
        }
        return ActionResult.PASS;
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    public BlockState rotate(BlockState state, BlockRotation rotation) {
        return (BlockState)super.rotate(state, rotation).with(ORIENTATION, rotation.getDirectionTransformation().mapJigsawOrientation(state.get(ORIENTATION)));
    }

    @Override
    public BlockState mirror(BlockState state, BlockMirror mirror) {
        return (BlockState)super.mirror(state, mirror).with(ORIENTATION, mirror.getDirectionTransformation().mapJigsawOrientation(state.get(ORIENTATION)));
    }

}
