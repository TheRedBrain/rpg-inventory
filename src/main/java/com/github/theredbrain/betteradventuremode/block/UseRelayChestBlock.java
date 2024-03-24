package com.github.theredbrain.betteradventuremode.block;

import com.github.theredbrain.betteradventuremode.block.entity.UseRelayChestBlockEntity;
import com.mojang.serialization.MapCodec;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.Nullable;

public class UseRelayChestBlock extends RotatedBlockWithEntity {
    public static final DirectionProperty FACING = HorizontalFacingBlock.FACING;
    public static final BooleanProperty OPEN = Properties.OPEN;
    public static final BooleanProperty WATERLOGGED = Properties.WATERLOGGED;
    @Nullable
    private final TagKey<Item> requiredKeys;
    protected static final VoxelShape CLOSED_SHAPE = Block.createCuboidShape(1.0, 0.0, 1.0, 15.0, 14.0, 15.0);
    protected static final VoxelShape OPENED_SHAPE = Block.createCuboidShape(1.0, 0.0, 1.0, 15.0, 10.0, 15.0);

    public UseRelayChestBlock(@Nullable TagKey<Item> requiredKeys, Settings settings) {
        super(settings);
        this.requiredKeys = requiredKeys;
        this.setDefaultState(this.stateManager.getDefaultState().with(ROTATED, 0).with(X_MIRRORED, false).with(Z_MIRRORED, false).with(FACING, Direction.NORTH).with(OPEN, false).with(WATERLOGGED, false));
    }

    // TODO Block Codecs
    public MapCodec<UseRelayChestBlock> getCodec() {
        return null;
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new UseRelayChestBlockEntity(pos, state);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(FACING, OPEN, WATERLOGGED);
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        if (state.get(OPEN)) {
            return OPENED_SHAPE;
        } else {
            return CLOSED_SHAPE;
        }
    }

    @Override
    public boolean canPathfindThrough(BlockState state, BlockView world, BlockPos pos, NavigationType type) {
        return false;
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        FluidState fluidState = ctx.getWorld().getFluidState(ctx.getBlockPos());
        Direction direction = ctx.getHorizontalPlayerFacing().getOpposite();
        return this.getDefaultState().with(FACING, direction).with(WATERLOGGED, fluidState.getFluid() == Fluids.WATER);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof UseRelayChestBlockEntity useRelayChestBlockEntity) {
            if (player.isCreativeLevelTwoOp()) {
                return ((UseRelayChestBlockEntity) blockEntity).openScreen(player) ? ActionResult.success(world.isClient) : ActionResult.PASS;
            } else {
                if (!state.get(OPEN)) {
                    ItemStack mainHandStack = player.getMainHandStack();
                    if (this.requiredKeys == null || mainHandStack.isIn(this.requiredKeys)) {
                        world.setBlockState(pos, state.with(OPEN, true), Block.NOTIFY_LISTENERS);
                        world.playSound((PlayerEntity)null, (double)pos.getX() + 0.5, (double)pos.getY() + 0.5, (double)pos.getZ() + 0.5, SoundEvents.BLOCK_CHEST_OPEN, SoundCategory.BLOCKS, 0.5F, world.random.nextFloat() * 0.1F + 0.9F);
                        return ActionResult.success(world.isClient);
                    } else {
                        player.sendMessage(Text.translatable("hud.message.keyIsNeeded"), true);
                    }
                } else {
                    BlockPos relayBlockPosOffset = useRelayChestBlockEntity.getRelayBlockPositionOffset();
                    BlockPos relayBlockPos = pos.add(relayBlockPosOffset.getX(), relayBlockPosOffset.getY(), relayBlockPosOffset.getZ());
                    BlockState relayBlockState = world.getBlockState(relayBlockPos);
                    if (relayBlockState.isOf(this)) {
                        return ActionResult.PASS;
                    }
                    return relayBlockState.getBlock().onUse(relayBlockState, world, relayBlockPos, player, hand, hit);
                }
            }
        }
        return ActionResult.PASS;
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        if (state.get(WATERLOGGED).booleanValue()) {
            return Fluids.WATER.getStill(false);
        }
        return super.getFluidState(state);
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        if (state.get(WATERLOGGED).booleanValue()) {
            world.scheduleFluidTick(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
        }
        return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
    }

    @Override
    public BlockState rotate(BlockState state, BlockRotation rotation) {
        return (BlockState) state.with(FACING, rotation.rotate((Direction) state.get(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, BlockMirror mirror) {
        return state.rotate(mirror.getRotation((Direction) state.get(FACING)));
    }
}
