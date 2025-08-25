package com.github.theredbrain.rpginventory.block;

import com.github.theredbrain.rpginventory.block.entity.MannequinBlockEntity;
import com.mojang.serialization.MapCodec;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class MannequinBlock extends BlockWithEntity {
	public static final MapCodec<MannequinBlock> CODEC = createCodec(MannequinBlock::new);
	private static final DirectionProperty FACING = HorizontalFacingBlock.FACING;
	private static final BooleanProperty WATERLOGGED = Properties.WATERLOGGED;
	public static final VoxelShape BOTTOM_SHAPE = Block.createCuboidShape(0.0, 0.0, 0.0, 16.0, 2.0, 16.0);
	public static final VoxelShape MIDDLE_SHAPE = Block.createCuboidShape(4.0, 2.0, 4.0, 12.0, 14.0, 12.0);
	public static final VoxelShape BASE_SHAPE = VoxelShapes.union(BOTTOM_SHAPE, MIDDLE_SHAPE);
	public static final VoxelShape COLLISION_SHAPE_TOP = Block.createCuboidShape(0.0, 15.0, 0.0, 16.0, 15.0, 16.0);
	public static final VoxelShape COLLISION_SHAPE = VoxelShapes.union(BASE_SHAPE, COLLISION_SHAPE_TOP);
	public static final VoxelShape WEST_SHAPE = VoxelShapes.union(
			Block.createCuboidShape(1.0, 10.0, 0.0, 5.333333, 14.0, 16.0),
			Block.createCuboidShape(5.333333, 12.0, 0.0, 9.666667, 16.0, 16.0),
			Block.createCuboidShape(9.666667, 14.0, 0.0, 14.0, 18.0, 16.0),
			BASE_SHAPE
	);
	public static final VoxelShape NORTH_SHAPE = VoxelShapes.union(
			Block.createCuboidShape(0.0, 10.0, 1.0, 16.0, 14.0, 5.333333),
			Block.createCuboidShape(0.0, 12.0, 5.333333, 16.0, 16.0, 9.666667),
			Block.createCuboidShape(0.0, 14.0, 9.666667, 16.0, 18.0, 14.0),
			BASE_SHAPE
	);
	public static final VoxelShape EAST_SHAPE = VoxelShapes.union(
			Block.createCuboidShape(10.666667, 10.0, 0.0, 15.0, 14.0, 16.0),
			Block.createCuboidShape(6.333333, 12.0, 0.0, 10.666667, 16.0, 16.0),
			Block.createCuboidShape(2.0, 14.0, 0.0, 6.333333, 18.0, 16.0),
			BASE_SHAPE
	);
	public static final VoxelShape SOUTH_SHAPE = VoxelShapes.union(
			Block.createCuboidShape(0.0, 10.0, 10.666667, 16.0, 14.0, 15.0),
			Block.createCuboidShape(0.0, 12.0, 6.333333, 16.0, 16.0, 10.666667),
			Block.createCuboidShape(0.0, 14.0, 2.0, 16.0, 18.0, 6.333333),
			BASE_SHAPE
	);

	public MannequinBlock(Settings settings) {
		super(settings);
		this.setDefaultState(this.stateManager.getDefaultState().with(FACING, Direction.NORTH).with(WATERLOGGED, false));
	}

	@Override
	public MapCodec<MannequinBlock> getCodec() {
		return CODEC;
	}

	@Nullable
	@Override
	public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return new MannequinBlockEntity(pos, state);
	}

	@Override
	public BlockRenderType getRenderType(BlockState state) {
		return BlockRenderType.MODEL;
	}

	@Override
	public BlockState getPlacementState(ItemPlacementContext ctx) {
		FluidState fluidState = ctx.getWorld().getFluidState(ctx.getBlockPos());
		return this.getDefaultState().with(FACING, ctx.getHorizontalPlayerFacing().getOpposite()).with(WATERLOGGED, fluidState.getFluid() == Fluids.WATER);
	}

	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
		BlockEntity blockEntity = world.getBlockEntity(pos);
		if (blockEntity instanceof MannequinBlockEntity mannequinBlockEntity) {
			player.openHandledScreen(mannequinBlockEntity);
			return ActionResult.success(world.isClient);
		}
		return ActionResult.PASS;
	}

	@Override
	public boolean canPathfindThrough(BlockState state, NavigationType type) {
		return false;
	}

	@Override
	protected VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return COLLISION_SHAPE;
	}

	@Override
	protected VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		switch ((Direction) state.get(FACING)) {
			case NORTH:
				return NORTH_SHAPE;
			case SOUTH:
				return SOUTH_SHAPE;
			case EAST:
				return EAST_SHAPE;
			case WEST:
				return WEST_SHAPE;
			default:
				return BASE_SHAPE;
		}
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(FACING, WATERLOGGED);
	}

	protected BlockState rotate(BlockState state, BlockRotation rotation) {
		return (BlockState) state.with(FACING, rotation.rotate((Direction) state.get(FACING)));
	}

	@Override
	protected BlockState mirror(BlockState state, BlockMirror mirror) {
		return state.rotate(mirror.getRotation(state.get(FACING)));
	}
}
