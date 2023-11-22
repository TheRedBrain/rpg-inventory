package com.github.theredbrain.bamcore.block;

import com.github.theredbrain.bamcore.block.entity.DelayTriggerBlockBlockEntity;
import com.github.theredbrain.bamcore.registry.EntityRegistry;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class DelayTriggerBlock extends RotatedBlockWithEntity {

    public DelayTriggerBlock(Settings settings) {
        super(settings);
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new DelayTriggerBlockBlockEntity(pos, state);
    }

    @Override
    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return validateTicker(type, EntityRegistry.DELAY_TRIGGER_BLOCK_ENTITY, DelayTriggerBlockBlockEntity::tick);
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof DelayTriggerBlockBlockEntity) {
            return ((DelayTriggerBlockBlockEntity)blockEntity).openScreen(player) ? ActionResult.success(world.isClient) : ActionResult.PASS;
        }
        return ActionResult.PASS;
    }
}
