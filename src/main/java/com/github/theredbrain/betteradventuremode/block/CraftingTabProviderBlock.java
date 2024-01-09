package com.github.theredbrain.betteradventuremode.block;

import com.github.theredbrain.betteradventuremode.BetterAdventureMode;
import com.github.theredbrain.betteradventuremode.registry.BlockRegistry;
import com.mojang.serialization.MapCodec;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class CraftingTabProviderBlock extends Block {
    private final CraftingRootBlock.Tab openedTab;
    public CraftingTabProviderBlock(CraftingRootBlock.Tab openedTab, Settings settings) {
        super(settings);
        this.openedTab = openedTab;
    }

    // TODO Block Codecs
    public MapCodec<CraftingTabProviderBlock> getCodec() {
        return null;
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (world.isClient) {
            return ActionResult.SUCCESS;
        }
        int posX = pos.getX();
        int posY = pos.getY();
        int posZ = pos.getZ();
        int crafting_root_block_reach_radius = BetterAdventureMode.serverConfig.crafting_root_block_reach_radius;
        for (int i = -crafting_root_block_reach_radius; i <= crafting_root_block_reach_radius; i++) {
            for (int j = -crafting_root_block_reach_radius; j <= crafting_root_block_reach_radius; j++) {
                for (int k = -crafting_root_block_reach_radius; k <= crafting_root_block_reach_radius; k++) {
                    BlockPos blockPos = new BlockPos(posX + i, posY + j, posZ + k);
                    BlockState blockState = world.getBlockState(blockPos);
                    if (blockState.isOf(BlockRegistry.CRAFTING_BENCH_BLOCK)) {
                        player.openHandledScreen(CraftingRootBlock.createCraftingRootBlockScreenHandlerFactory(blockState, world, blockPos, this.openedTab));
                        return ActionResult.CONSUME;
                    }
                }
            }
        }
        return ActionResult.FAIL;
//        player.incrementStat(Stats.INTERACT_WITH_CRAFTING_TABLE); // TODO stats
    }
}
