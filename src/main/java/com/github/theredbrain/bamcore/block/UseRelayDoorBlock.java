package com.github.theredbrain.bamcore.block;

import com.github.theredbrain.bamcore.BetterAdventureModeCore;
import com.github.theredbrain.bamcore.api.util.BlockRotationUtils;
import com.github.theredbrain.bamcore.block.entity.UseRelayBlockEntity;
import com.mojang.serialization.MapCodec;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.enums.DoorHinge;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.*;
import org.jetbrains.annotations.Nullable;

public class UseRelayDoorBlock extends RotatedBlockWithEntity {
    public static final DirectionProperty FACING = HorizontalFacingBlock.FACING;
    public static final BooleanProperty OPEN = Properties.OPEN;
    public static final EnumProperty<DoorHinge> HINGE = Properties.DOOR_HINGE;
    public static final EnumProperty<DoubleBlockHalf> HALF = Properties.DOUBLE_BLOCK_HALF;
    protected static final VoxelShape NORTH_SHAPE = Block.createCuboidShape(0.0, 0.0, 0.0, 16.0, 16.0, 3.0);
    protected static final VoxelShape SOUTH_SHAPE = Block.createCuboidShape(0.0, 0.0, 13.0, 16.0, 16.0, 16.0);
    protected static final VoxelShape EAST_SHAPE = Block.createCuboidShape(13.0, 0.0, 0.0, 16.0, 16.0, 16.0);
    protected static final VoxelShape WEST_SHAPE = Block.createCuboidShape(0.0, 0.0, 0.0, 3.0, 16.0, 16.0);

    public UseRelayDoorBlock(Settings settings) {
        super(settings);
        this.setDefaultState((BlockState)((BlockState)((BlockState)((BlockState)((BlockState)((BlockState)this.stateManager.getDefaultState()).with(FACING, Direction.NORTH)).with(OPEN, false)).with(HINGE, DoorHinge.LEFT))).with(HALF, DoubleBlockHalf.LOWER));
    }

    // TODO Block Codecs
    public MapCodec<UseRelayDoorBlock> getCodec() {
        return null;
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new UseRelayBlockEntity(pos, state);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(HALF, FACING, OPEN, HINGE);
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        Direction direction = state.get(FACING);
        boolean bl = !state.get(OPEN);
        boolean bl2 = state.get(HINGE) == DoorHinge.RIGHT;
        switch (direction) {
            default: {
                return bl ? WEST_SHAPE : (bl2 ? SOUTH_SHAPE : NORTH_SHAPE);
            }
            case SOUTH: {
                return bl ? NORTH_SHAPE : (bl2 ? WEST_SHAPE : EAST_SHAPE);
            }
            case WEST: {
                return bl ? EAST_SHAPE : (bl2 ? NORTH_SHAPE : SOUTH_SHAPE);
            }
            case NORTH:
        }
        return bl ? SOUTH_SHAPE : (bl2 ? EAST_SHAPE : WEST_SHAPE);
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        DoubleBlockHalf doubleBlockHalf = state.get(HALF);
        if (direction.getAxis() == Direction.Axis.Y && doubleBlockHalf == DoubleBlockHalf.LOWER == (direction == Direction.UP)) {
            if (neighborState.isOf(this) && neighborState.get(HALF) != doubleBlockHalf) {
                return (BlockState)((BlockState)((BlockState)((BlockState)state.with(FACING, neighborState.get(FACING))).with(OPEN, neighborState.get(OPEN))).with(HINGE, neighborState.get(HINGE)));
            }
            return Blocks.AIR.getDefaultState();
        }
        if (doubleBlockHalf == DoubleBlockHalf.LOWER && direction == Direction.DOWN && !state.canPlaceAt(world, pos)) {
            return Blocks.AIR.getDefaultState();
        }
        return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
    }

    @Override
    public BlockState onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        if (!world.isClient && player.isCreative()) {
            onBreakInCreative(world, pos, state, player);
        }
        return super.onBreak(world, pos, state, player);
    }

    @Override
    public boolean canPathfindThrough(BlockState state, BlockView world, BlockPos pos, NavigationType type) {
        switch (type) {
            case LAND, AIR -> {
                return state.get(OPEN);
            }
            case WATER -> {
                return false;
            }
        }
        return false;
    }

    @Override
    @Nullable
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        BlockPos blockPos = ctx.getBlockPos();
        World world = ctx.getWorld();
        if (blockPos.getY() < world.getTopY() - 1 && world.getBlockState(blockPos.up()).canReplace(ctx)) {
            boolean bl = world.isReceivingRedstonePower(blockPos) || world.isReceivingRedstonePower(blockPos.up());
            return (BlockState)((BlockState)((BlockState)((BlockState)((BlockState)this.getDefaultState().with(FACING, ctx.getHorizontalPlayerFacing())).with(HINGE, this.getHinge(ctx)))).with(OPEN, bl)).with(HALF, DoubleBlockHalf.LOWER);
        }
        return null;
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack) {
        world.setBlockState(pos.up(), (BlockState)state.with(HALF, DoubleBlockHalf.UPPER), Block.NOTIFY_ALL);
    }

    private DoorHinge getHinge(ItemPlacementContext ctx) {
        boolean bl2;
        World blockView = ctx.getWorld();
        BlockPos blockPos = ctx.getBlockPos();
        Direction direction = ctx.getHorizontalPlayerFacing();
        BlockPos blockPos2 = blockPos.up();
        Direction direction2 = direction.rotateYCounterclockwise();
        BlockPos blockPos3 = blockPos.offset(direction2);
        BlockState blockState = blockView.getBlockState(blockPos3);
        BlockPos blockPos4 = blockPos2.offset(direction2);
        BlockState blockState2 = blockView.getBlockState(blockPos4);
        Direction direction3 = direction.rotateYClockwise();
        BlockPos blockPos5 = blockPos.offset(direction3);
        BlockState blockState3 = blockView.getBlockState(blockPos5);
        BlockPos blockPos6 = blockPos2.offset(direction3);
        BlockState blockState4 = blockView.getBlockState(blockPos6);
        int i = (blockState.isFullCube(blockView, blockPos3) ? -1 : 0) + (blockState2.isFullCube(blockView, blockPos4) ? -1 : 0) + (blockState3.isFullCube(blockView, blockPos5) ? 1 : 0) + (blockState4.isFullCube(blockView, blockPos6) ? 1 : 0);
        boolean bl = blockState.isOf(this) && blockState.get(HALF) == DoubleBlockHalf.LOWER;
        boolean bl3 = bl2 = blockState3.isOf(this) && blockState3.get(HALF) == DoubleBlockHalf.LOWER;
        if (bl && !bl2 || i > 0) {
            return DoorHinge.RIGHT;
        }
        if (bl2 && !bl || i < 0) {
            return DoorHinge.LEFT;
        }
        int j = direction.getOffsetX();
        int k = direction.getOffsetZ();
        Vec3d vec3d = ctx.getHitPos();
        double d = vec3d.x - (double)blockPos.getX();
        double e = vec3d.z - (double)blockPos.getZ();
        return j < 0 && e < 0.5 || j > 0 && e > 0.5 || k < 0 && d > 0.5 || k > 0 && d < 0.5 ? DoorHinge.RIGHT : DoorHinge.LEFT;
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof UseRelayBlockEntity useRelayBlockEntity) {
            boolean bl = player.isCreativeLevelTwoOp();
            if (player.isSneaking() && bl) {
                state = (BlockState)state.cycle(OPEN);
                world.setBlockState(pos, state, Block.NOTIFY_LISTENERS | Block.REDRAW_ON_MAIN_THREAD);
                return ActionResult.success(world.isClient);
            }
            if (state.get(HALF) == DoubleBlockHalf.UPPER) {
                BlockPos posDown = pos.down();
                BlockState stateDown = world.getBlockState(pos.down());
                if (stateDown.get(HALF) == DoubleBlockHalf.UPPER) {
                    return ActionResult.PASS;
                }
                return stateDown.getBlock().onUse(stateDown, world, posDown, player, hand, hit);
            }
            if (bl) {
                return ((UseRelayBlockEntity) blockEntity).openScreen(player) ? ActionResult.success(world.isClient) : ActionResult.PASS;
            } else {
                BlockPos relayBlockPosOffset = useRelayBlockEntity.getRelayBlockPositionOffset();
                BlockPos relayBlockPos = pos.add(relayBlockPosOffset.getX(), relayBlockPosOffset.getY(), relayBlockPosOffset.getZ());
                BlockState relayBlockState = world.getBlockState(relayBlockPos);
                if (relayBlockState.isOf(this)) {
                    return ActionResult.PASS;
                }
                return relayBlockState.getBlock().onUse(relayBlockState, world, relayBlockPos, player, hand, hit);
            }
        }
        return ActionResult.PASS;
    }

    @Override
    public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
        BlockPos blockPos = pos.down();
        BlockState blockState = world.getBlockState(blockPos);
        if (state.get(HALF) == DoubleBlockHalf.LOWER) {
            return true;
        }
        return blockState.isOf(this);
    }

    @Override
    public BlockState rotate(BlockState state, BlockRotation rotation) {
        return super.rotate(state, rotation).with(FACING, BlockRotationUtils.calculateNewHorizontalFacingBlockState(state.get(FACING), rotation));
    }

    @Override
    public BlockState mirror(BlockState state, BlockMirror mirror) {
        if (mirror == BlockMirror.FRONT_BACK) {
            return super.mirror(state, mirror).with(FACING, BlockRotationUtils.calculateNewHorizontalFacingBlockState(state.get(FACING), mirror.getRotation(state.get(FACING)))).cycle(HINGE);
        } else if (mirror == BlockMirror.LEFT_RIGHT) {
            return super.mirror(state, mirror).with(FACING, BlockRotationUtils.calculateNewHorizontalFacingBlockState(state.get(FACING), mirror.getRotation(state.get(FACING)))).cycle(HINGE);
        }
        return state;
    }

//    /**
//     * Destroys a bottom half of a tall double block (such as a plant or a door)
//     * without dropping an item when broken in creative.
//     *
//     * @see Block#onBreak(World, BlockPos, BlockState, PlayerEntity)
//     */
//    protected static void onBreakInCreative(World world, BlockPos pos, BlockState state, PlayerEntity player) {
//        BlockPos blockPos;
//        BlockState blockState;
//        DoubleBlockHalf doubleBlockHalf = state.get(HALF);
//        if (doubleBlockHalf == DoubleBlockHalf.UPPER && (blockState = world.getBlockState(blockPos = pos.down())).isOf(state.getBlock()) && blockState.get(HALF) == DoubleBlockHalf.LOWER) {
//            BlockState blockState2 = blockState.getFluidState().isOf(Fluids.WATER) ? Blocks.WATER.getDefaultState() : Blocks.AIR.getDefaultState();
//            world.setBlockState(blockPos, blockState2, Block.NOTIFY_ALL | Block.SKIP_DROPS);
//            world.syncWorldEvent(player, WorldEvents.BLOCK_BROKEN, blockPos, Block.getRawIdFromState(blockState));
//        }
//    }

    protected static void onBreakInCreative(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        DoubleBlockHalf doubleBlockHalf = (DoubleBlockHalf)state.get(HALF);
        if (doubleBlockHalf == DoubleBlockHalf.UPPER) {
            BlockPos blockPos = pos.down();
            BlockState blockState = world.getBlockState(blockPos);
            if (blockState.isOf(state.getBlock()) && blockState.get(HALF) == DoubleBlockHalf.LOWER) {
                BlockState blockState2 = blockState.getFluidState().isOf(Fluids.WATER) ? Blocks.WATER.getDefaultState() : Blocks.AIR.getDefaultState();
                world.setBlockState(blockPos, blockState2, 35);
                world.syncWorldEvent(player, 2001, blockPos, Block.getRawIdFromState(blockState));
            }
        }

    }

}
