package com.github.theredbrain.rpgmod.block;

import com.github.theredbrain.rpgmod.entity.player.DuckPlayerEntityMixin;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class InteractiveAdventureFoodBlock extends AbstractInteractiveAdventureBlock {

    private final StatusEffectInstance statusEffectInstance;
    private static final VoxelShape SHAPE;

    public InteractiveAdventureFoodBlock(StatusEffectInstance statusEffectInstance, int respawnModifier, Settings settings) {
        super(null, false, respawnModifier, settings);
        this.statusEffectInstance = statusEffectInstance;
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return VoxelShapes.fullCube();
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (!world.isClient() && state.isOf(this) && state.get(AbstractInteractiveAdventureBlock.INTACT) && ((DuckPlayerEntityMixin)player).tryEatAdventureFood(this.statusEffectInstance)) {
            player.addStatusEffect(new StatusEffectInstance(this.statusEffectInstance));
            world.setBlockState(pos, state.with(INTACT, false));
            return ActionResult.CONSUME;
        } else {
            return ActionResult.PASS;
        }
    }

    static {
        SHAPE = Block.createCuboidShape(5.0D, 0.0D, 5.0D, 11.0D, 6.0D, 11.0D);
    }
}
