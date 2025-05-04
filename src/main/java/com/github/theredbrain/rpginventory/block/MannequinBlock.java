package com.github.theredbrain.rpginventory.block;

import com.github.theredbrain.rpginventory.block.entity.MannequinBlockEntity;
import com.mojang.serialization.MapCodec;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class MannequinBlock extends BlockWithEntity {
	public static final MapCodec<MannequinBlock> CODEC = createCodec(MannequinBlock::new);

	public MannequinBlock(Settings settings) {
		super(settings);
	}

	@Override
	public MapCodec<MannequinBlock> getCodec() {
		return CODEC;
	}

	@Nullable
	@Override
	public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return new MannequinBlockEntity(pos, state);
	}

	@Override
	public BlockRenderType getRenderType(BlockState state) {
		return BlockRenderType.MODEL;
	}

//	@Override
//	@Nullable
//	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
//		return validateTicker(type, EntityRegistry.AREA_BLOCK_ENTITY, AreaBlockEntity::tick);
//	}

	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
		BlockEntity blockEntity = world.getBlockEntity(pos);
		if (blockEntity instanceof MannequinBlockEntity mannequinBlockEntity) {
			player.openHandledScreen(mannequinBlockEntity);
			return ActionResult.success(world.isClient);
		}
		return ActionResult.PASS;
	}

}
