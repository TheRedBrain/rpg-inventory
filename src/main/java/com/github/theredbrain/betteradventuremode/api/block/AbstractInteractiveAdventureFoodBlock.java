package com.github.theredbrain.betteradventuremode.api.block;

import com.github.theredbrain.betteradventuremode.entity.player.DuckPlayerEntityMixin;
import com.mojang.serialization.MapCodec;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public abstract class AbstractInteractiveAdventureFoodBlock extends AbstractInteractiveAdventureBlock {

    private final StatusEffectInstance statusEffectInstance;

    public AbstractInteractiveAdventureFoodBlock(@Nullable Block cloakedBlock, @Nullable Identifier cloakAdvancementIdentifier, StatusEffectInstance statusEffectInstance, int respawnModifier, Settings settings) {
        super(cloakedBlock, cloakAdvancementIdentifier, null, false, respawnModifier, settings);
        this.statusEffectInstance = statusEffectInstance;
    }

    protected abstract MapCodec<? extends AbstractInteractiveAdventureFoodBlock> getCodec();

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
