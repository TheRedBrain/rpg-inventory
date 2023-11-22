package com.github.theredbrain.bamcore.block;

import com.github.theredbrain.bamcore.block.entity.RelayTriggerBlockBlockEntity;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class RelayTriggerBlock extends RotatedBlockWithEntity {

    public RelayTriggerBlock(Settings settings) {
        super(settings);
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new RelayTriggerBlockBlockEntity(pos, state);
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof RelayTriggerBlockBlockEntity) {
            return ((RelayTriggerBlockBlockEntity)blockEntity).openScreen(player) ? ActionResult.success(world.isClient) : ActionResult.PASS;
        }
        return ActionResult.PASS;
    }
}
