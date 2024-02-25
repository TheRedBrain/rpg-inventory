package com.github.theredbrain.betteradventuremode.block;

import com.github.theredbrain.betteradventuremode.entity.damage.DuckDamageSourcesMixin;
import com.github.theredbrain.betteradventuremode.registry.StatusEffectsRegistry;
import com.mojang.serialization.MapCodec;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class LavaBlock extends Block {
//    public static final MapCodec<LavaBlock> CODEC = createCodec(LavaBlock::new);
    protected static final VoxelShape COLLISION_SHAPE = Block.createCuboidShape(0.0, 0.0, 0.0, 16.0, 10.0, 16.0);
    protected static final VoxelShape SHAPE = Block.createCuboidShape(0.0, 0.0, 0.0, 16.0, 14.0, 16.0);

    public LavaBlock(Settings settings) {
        super(settings);
    }

//    @Override
//    public MapCodec<LavaBlock> getCodec() {
//        return CODEC;
//    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return COLLISION_SHAPE;
    }


    @Override
    public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
        if (entity instanceof LivingEntity livingEntity && livingEntity.hasStatusEffect(StatusEffectsRegistry.LAVA_IMMUNE)) {
            entity.slowMovement(state, new Vec3d(0.95F, 1.0F, 0.95F));
            if (world.getTime() % 80L == 0L) {
                entity.damage(((DuckDamageSourcesMixin) entity.getDamageSources()).betteradventuremode$lava(), 1.0F);
            }
        } else {
            entity.slowMovement(state, new Vec3d(0.55F, 1.0F, 0.55F));
            if (world.getTime() % 30L == 0L) {
                entity.damage(((DuckDamageSourcesMixin) entity.getDamageSources()).betteradventuremode$lava(), 4.0f);
            }
        }
    }

    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPE;
    }

    @Override
    public VoxelShape getCameraCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPE;
    }

    @Override
    public boolean canPathfindThrough(BlockState state, BlockView world, BlockPos pos, NavigationType type) {
        return false;
    }

}
