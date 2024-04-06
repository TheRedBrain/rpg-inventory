package com.github.theredbrain.betteradventuremode.block;

import com.github.theredbrain.betteradventuremode.registry.BlockRegistry;
import net.minecraft.block.*;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import net.minecraft.world.chunk.light.ChunkLightProvider;

public class MyceliumSlabBlock extends CustomSlabBlock{
    public MyceliumSlabBlock(Settings settings) {
        super(settings);
    }

    private static boolean canSurvive(BlockState state, WorldView world, BlockPos pos) {
        BlockPos blockPos = pos.up();
        BlockState blockState = world.getBlockState(blockPos);
        if (blockState.isOf(Blocks.SNOW) && (Integer)blockState.get(SnowBlock.LAYERS) == 1) {
            return true;
        } else if (blockState.getFluidState().getLevel() == 8) {
            return false;
        } else if ((state.isOf(BlockRegistry.DIRT_SLAB) || state.isOf(BlockRegistry.MYCELIUM_SLAB)) && state.get(WATERLOGGED)) {
            return false;
        }
        else {
            int i = ChunkLightProvider.getRealisticOpacity(world, state, pos, blockState, blockPos, Direction.UP, blockState.getOpacity(world, blockPos));
            return i < world.getMaxLightLevel();
        }
    }

    private static boolean canSpread(BlockState state, WorldView world, BlockPos pos) {
        BlockPos blockPos = pos.up();
        return canSurvive(state, world, pos) && !world.getFluidState(blockPos).isIn(FluidTags.WATER);
    }

    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        if (!canSurvive(state, world, pos)) {
            world.setBlockState(pos, BlockRegistry.DIRT_SLAB.getDefaultState().with(TYPE, state.get(TYPE)).with(WATERLOGGED, state.get(WATERLOGGED)));
        } else {
            if (world.getLightLevel(pos.up()) >= 9) {
                BlockState fullBlockState = Blocks.MYCELIUM.getDefaultState();
                BlockState slabBlockState = this.getDefaultState();

                for(int i = 0; i < 4; ++i) {
                    BlockPos blockPos = pos.add(random.nextInt(3) - 1, random.nextInt(5) - 3, random.nextInt(3) - 1);

                    if (world.getBlockState(blockPos).isOf(Blocks.DIRT) && canSpread(fullBlockState, world, blockPos)) {
                        world.setBlockState(blockPos, (BlockState)fullBlockState.with(SnowyBlock.SNOWY, world.getBlockState(blockPos.up()).isOf(Blocks.SNOW)));
                    } else if (world.getBlockState(blockPos).isOf(BlockRegistry.DIRT_SLAB) && !(world.getBlockState(blockPos).get(WATERLOGGED)) && canSpread(slabBlockState, world, blockPos)) {
                        world.setBlockState(blockPos, (BlockState)slabBlockState.with(TYPE, world.getBlockState(blockPos).get(TYPE)));
                    }
                }
            }

        }
    }

    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
        super.randomDisplayTick(state, world, pos, random);
        if (random.nextInt(10) == 0) {
            world.addParticle(ParticleTypes.MYCELIUM, (double)pos.getX() + random.nextDouble(), (double)pos.getY() + 1.1D, (double)pos.getZ() + random.nextDouble(), 0.0D, 0.0D, 0.0D);
        }
    }
}
