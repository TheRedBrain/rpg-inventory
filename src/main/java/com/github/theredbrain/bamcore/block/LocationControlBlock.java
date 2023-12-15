package com.github.theredbrain.bamcore.block;

import com.github.theredbrain.bamcore.block.entity.LocationControlBlockEntity;
import com.github.theredbrain.bamcore.entity.player.DuckPlayerEntityMixin;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class LocationControlBlock extends RotatedBlockWithEntity {
    public LocationControlBlock(Settings settings) {
        super(settings);
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new LocationControlBlockEntity(pos, state);
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof LocationControlBlockEntity locationControlBlock && player.isCreative()) {
            if (world.isClient) {
                ((DuckPlayerEntityMixin) player).bamcore$openLocationControlBlockScreen(locationControlBlock);
                return ActionResult.SUCCESS;
            }
        }
        return ActionResult.PASS;
    }
}
