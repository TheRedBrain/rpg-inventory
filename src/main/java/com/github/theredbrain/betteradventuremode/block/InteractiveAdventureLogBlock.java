package com.github.theredbrain.betteradventuremode.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.state.property.Property;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import org.jetbrains.annotations.Nullable;

public class InteractiveAdventureLogBlock extends AbstractInteractiveAdventureBlock {

    private static final VoxelShape EAST_WEST_SHAPE;
    private static final VoxelShape NORTH_SOUTH_SHAPE;
    private static final EnumProperty<Direction> FACING;
    public InteractiveAdventureLogBlock(@Nullable Block cloakedBlock, @Nullable Identifier cloakAdvancementIdentifier, @Nullable TagKey<Item> requiredTools, boolean requiresTools, int respawnModifier, Settings settings) {
        super(cloakedBlock, cloakAdvancementIdentifier, requiredTools, requiresTools, respawnModifier, settings);
        this.setDefaultState(this.getStateManager().getDefaultState().with(FACING, Direction.NORTH));
    }

    // TODO Block Codecs
    public MapCodec<InteractiveAdventureLogBlock> getCodec() {
        return null;
    }

    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(new Property[]{FACING});
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        if (state.isOf(this) && state.get(AbstractInteractiveAdventureBlock.INTACT)) {
            if (state.isOf(this) && (state.get(FACING) == Direction.EAST || state.get(FACING) == Direction.WEST)) {
                return EAST_WEST_SHAPE;
            }
            if (state.isOf(this) && (state.get(FACING) == Direction.NORTH || state.get(FACING) == Direction.SOUTH)) {
                return NORTH_SOUTH_SHAPE;
            }
        }

        return VoxelShapes.empty();
    }

    public BlockState getPlacementState(ItemPlacementContext ctx) {
        Direction direction = ctx.getSide();
        BlockPos blockPos = ctx.getBlockPos();
        Direction facing;
        switch (direction) {
            case NORTH -> facing = Direction.SOUTH;
            case SOUTH -> facing = Direction.NORTH;
            case WEST -> facing = Direction.EAST;
            default -> facing = Direction.WEST;
        }
        return this.getDefaultState().with(FACING, facing);
    }

    /*
    TODO interactable ressource blocks
        interacting "harvests" them and changes blockstate
            maybe multiple stages before harvest is complete
            different tools progress different amounts of stages
        might need interaction with a specific items/tool
        after being harvested some leaves/rubble/etc remains
        refills after some time
        tree logs
        stone/ore veins
        plants/mushrooms
     */

    static {
        FACING = Properties.HORIZONTAL_FACING;
        EAST_WEST_SHAPE = Block.createCuboidShape(1.0D, 0.0D, 3.0D, 15.0D, 10.0D, 13.0D);
        NORTH_SOUTH_SHAPE = Block.createCuboidShape(3.0D, 0.0D, 1.0D, 13.0D, 10.0D, 15.0D);
    }
}
