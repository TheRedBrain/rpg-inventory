package com.github.theredbrain.rpginventory.block;

import com.github.theredbrain.rpginventory.RPGInventory;
import com.github.theredbrain.rpginventory.block.entity.MannequinBlockEntity;
import com.github.theredbrain.rpginventory.screen.AbstractMannequinScreenHandler;
import com.github.theredbrain.rpginventory.screen.RPGMannequinScreenHandler;
import com.github.theredbrain.rpginventory.screen.VanillaMannequinScreenHandler;
import com.mojang.serialization.MapCodec;
import net.fabricmc.fabric.api.menu.v1.ExtendedMenuProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class MannequinBlock extends BaseEntityBlock {
	public static final MapCodec<MannequinBlock> CODEC = simpleCodec(MannequinBlock::new);
	private static final EnumProperty<Direction> FACING = HorizontalDirectionalBlock.FACING;
	private static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
	public static final VoxelShape BOTTOM_SHAPE = Block.box(0.0, 0.0, 0.0, 16.0, 2.0, 16.0);
	public static final VoxelShape MIDDLE_SHAPE = Block.box(4.0, 2.0, 4.0, 12.0, 14.0, 12.0);
	public static final VoxelShape BASE_SHAPE = Shapes.or(BOTTOM_SHAPE, MIDDLE_SHAPE);
	public static final VoxelShape COLLISION_SHAPE_TOP = Block.box(0.0, 15.0, 0.0, 16.0, 15.0, 16.0);
	public static final VoxelShape COLLISION_SHAPE = Shapes.or(BASE_SHAPE, COLLISION_SHAPE_TOP);
	public static final VoxelShape WEST_SHAPE = Shapes.or(
			Block.box(1.0, 10.0, 0.0, 5.333333, 14.0, 16.0),
			Block.box(5.333333, 12.0, 0.0, 9.666667, 16.0, 16.0),
			Block.box(9.666667, 14.0, 0.0, 14.0, 18.0, 16.0),
			BASE_SHAPE
	);
	public static final VoxelShape NORTH_SHAPE = Shapes.or(
			Block.box(0.0, 10.0, 1.0, 16.0, 14.0, 5.333333),
			Block.box(0.0, 12.0, 5.333333, 16.0, 16.0, 9.666667),
			Block.box(0.0, 14.0, 9.666667, 16.0, 18.0, 14.0),
			BASE_SHAPE
	);
	public static final VoxelShape EAST_SHAPE = Shapes.or(
			Block.box(10.666667, 10.0, 0.0, 15.0, 14.0, 16.0),
			Block.box(6.333333, 12.0, 0.0, 10.666667, 16.0, 16.0),
			Block.box(2.0, 14.0, 0.0, 6.333333, 18.0, 16.0),
			BASE_SHAPE
	);
	public static final VoxelShape SOUTH_SHAPE = Shapes.or(
			Block.box(0.0, 10.0, 10.666667, 16.0, 14.0, 15.0),
			Block.box(0.0, 12.0, 6.333333, 16.0, 16.0, 10.666667),
			Block.box(0.0, 14.0, 2.0, 16.0, 18.0, 6.333333),
			BASE_SHAPE
	);

	public MannequinBlock(Properties settings) {
		super(settings);
		this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(WATERLOGGED, false));
	}

	@Override
	public MapCodec<MannequinBlock> codec() {
		return CODEC;
	}

	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new MannequinBlockEntity(pos, state);
	}

	@Override
	public RenderShape getRenderShape(BlockState state) {
		return RenderShape.MODEL;
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext ctx) {
		FluidState fluidState = ctx.getLevel().getFluidState(ctx.getClickedPos());
		return this.defaultBlockState().setValue(FACING, ctx.getHorizontalDirection().getOpposite()).setValue(WATERLOGGED, fluidState.getType() == Fluids.WATER);
	}

	@Override
	protected InteractionResult useWithoutItem(final BlockState state, final Level level, final BlockPos pos, final Player player, final BlockHitResult hitResult) {
		BlockEntity blockEntity = level.getBlockEntity(pos);
		if (blockEntity instanceof MannequinBlockEntity mannequinBlockEntity && !mannequinBlockEntity.isLockedForPlayer(player)) {
			if (!level.isClientSide()) {
				player.openMenu(createMannequinBlockScreenHandlerFactory(pos, player, mannequinBlockEntity));
			}
			return InteractionResult.SUCCESS;
		}
		return InteractionResult.PASS;
	}

	public static MenuProvider createMannequinBlockScreenHandlerFactory(BlockPos pos, Player player, MannequinBlockEntity mannequinBlockEntity) {
		return new ExtendedMenuProvider<>() {

			@Override
			public AbstractMannequinScreenHandler.MannequinBlockData getScreenOpeningData(ServerPlayer player) {
				return new AbstractMannequinScreenHandler.MannequinBlockData(pos, mannequinBlockEntity.canChangeInventory() || player.isCreative(), mannequinBlockEntity.canEquip() || player.isCreative());
			}

			@Override
			public Component getDisplayName() {
				return mannequinBlockEntity.getDisplayName();
			}

			@Nullable
			@Override
			public AbstractContainerMenu createMenu(int syncId, Inventory playerInventory, Player player) {
				if (RPGInventory.SERVER_CONFIG.activate_rpg_inventory_screen.get()) {
					return new RPGMannequinScreenHandler(syncId, playerInventory, mannequinBlockEntity, pos, mannequinBlockEntity.canChangeInventory() || player.isCreative(), mannequinBlockEntity.canEquip() || player.isCreative());
				} else {
					return new VanillaMannequinScreenHandler(syncId, playerInventory, mannequinBlockEntity, pos, mannequinBlockEntity.canChangeInventory() || player.isCreative(), mannequinBlockEntity.canEquip() || player.isCreative());
				}
			}
		};
	}

	@Override
	public boolean isPathfindable(BlockState state, PathComputationType type) {
		return false;
	}

	@Override
	protected VoxelShape getCollisionShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
		return COLLISION_SHAPE;
	}

	@Override
	protected VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
		switch ((Direction) state.getValue(FACING)) {
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
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(FACING, WATERLOGGED);
	}

	protected BlockState rotate(BlockState state, Rotation rotation) {
		return (BlockState) state.setValue(FACING, rotation.rotate((Direction) state.getValue(FACING)));
	}

	@Override
	protected BlockState mirror(BlockState state, Mirror mirror) {
		return state.rotate(mirror.getRotation(state.getValue(FACING)));
	}
}
