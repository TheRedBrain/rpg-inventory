package com.github.theredbrain.bamcore.api.block;

import com.github.theredbrain.bamcore.entity.player.DuckPlayerEntityMixin;
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

public abstract class AbstractInteractiveAdventureFoodBlock extends AbstractInteractiveAdventureBlock {

    private final StatusEffectInstance statusEffectInstance;

    public AbstractInteractiveAdventureFoodBlock(StatusEffectInstance statusEffectInstance, int respawnModifier, Settings settings) {
        super(null, false, respawnModifier, settings);
        this.statusEffectInstance = statusEffectInstance;
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (!world.isClient() && state.isOf(this) && state.get(AbstractInteractiveAdventureBlock.INTACT) && ((DuckPlayerEntityMixin)player).bamcore$tryEatAdventureFood(this.statusEffectInstance)) {
            player.addStatusEffect(new StatusEffectInstance(this.statusEffectInstance));
            world.setBlockState(pos, state.with(INTACT, false));
            return ActionResult.CONSUME;
        } else {
            return ActionResult.PASS;
        }
    }
}
