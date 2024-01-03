package com.github.theredbrain.betteradventuremode.mixin.block;

import com.github.theredbrain.betteradventuremode.registry.GameRulesRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.block.RespawnAnchorBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(RespawnAnchorBlock.class)
public abstract class RespawnAnchorBlockMixin {

    @Shadow
    private static boolean isChargeItem(ItemStack stack) {
        throw new AssertionError();
    }

    @Shadow
    private static boolean canCharge(BlockState state) {
        throw new AssertionError();
    }

    @Shadow
    public static void charge(@Nullable Entity charger, World world, BlockPos pos, BlockState state) {
        throw new AssertionError();
    }

    @Shadow
    public static boolean isNether(World world) {
        throw new AssertionError();
    }

    @Shadow
    private void explode(BlockState state, World world, BlockPos explodedPos) {
        throw new AssertionError();
    }

    /**
     * @author TheRedBrain
     * @reason
     */
    @Overwrite
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        ItemStack itemStack = player.getStackInHand(hand);
        if (hand == Hand.MAIN_HAND && !isChargeItem(itemStack) && isChargeItem(player.getStackInHand(Hand.OFF_HAND))) {
            return ActionResult.PASS;
        } else if (isChargeItem(itemStack) && canCharge(state)) {
            charge(player, world, pos, state);
            if (!player.getAbilities().creativeMode) {
                itemStack.decrement(1);
            }

            return ActionResult.success(world.isClient);
        } else if ((Integer)state.get(RespawnAnchorBlock.CHARGES) == 0) {
            return ActionResult.PASS;
        } else if (!isNether(world)) {
            if (!world.isClient) {
                this.explode(state, world, pos);
            }

            return ActionResult.success(world.isClient);
        } else {
            if (!world.isClient && world.getGameRules().getBoolean(GameRulesRegistry.CAN_SET_SPAWN_ON_RESPAWN_ANCHOR)) {
                ServerPlayerEntity serverPlayerEntity = (ServerPlayerEntity)player;
                if (serverPlayerEntity.getSpawnPointDimension() != world.getRegistryKey() || !pos.equals(serverPlayerEntity.getSpawnPointPosition())) {
                    serverPlayerEntity.setSpawnPoint(world.getRegistryKey(), pos, 0.0F, false, true);
                    world.playSound((PlayerEntity)null, (double)pos.getX() + 0.5, (double)pos.getY() + 0.5, (double)pos.getZ() + 0.5, SoundEvents.BLOCK_RESPAWN_ANCHOR_SET_SPAWN, SoundCategory.BLOCKS, 1.0F, 1.0F);
                    return ActionResult.SUCCESS;
                }
            }

            return ActionResult.CONSUME;
        }
    }
}
