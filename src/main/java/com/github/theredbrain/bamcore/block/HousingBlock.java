package com.github.theredbrain.bamcore.block;

import com.github.theredbrain.bamcore.block.entity.HousingBlockBlockEntity;
import com.github.theredbrain.bamcore.entity.player.DuckPlayerEntityMixin;
import com.github.theredbrain.bamcore.registry.ComponentsRegistry;
import com.github.theredbrain.bamcore.registry.EntityRegistry;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
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

public class HousingBlock extends RotatedBlockWithEntity {
    public HousingBlock(Settings settings) {
        super(settings);
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new HousingBlockBlockEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return validateTicker(type, EntityRegistry.HOUSING_BLOCK_ENTITY, HousingBlockBlockEntity::tick);
    }
    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof HousingBlockBlockEntity housingBlockBlockEntity && player.isCreative()) {
            ComponentsRegistry.CURRENT_HOUSING_BLOCK_POS.get(player).setValue(housingBlockBlockEntity.getPos());
            if (world.isClient) {
                ((DuckPlayerEntityMixin) player).bamcore$openHousingScreen();
                return ActionResult.SUCCESS;
            }
        }
        return ActionResult.PASS;
    }
}
