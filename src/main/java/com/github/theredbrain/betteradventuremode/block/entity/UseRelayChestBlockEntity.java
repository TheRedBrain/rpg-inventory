package com.github.theredbrain.betteradventuremode.block.entity;

import com.github.theredbrain.betteradventuremode.block.Resetable;
import com.github.theredbrain.betteradventuremode.block.UseRelayChestBlock;
import com.github.theredbrain.betteradventuremode.registry.EntityRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;

public class UseRelayChestBlockEntity extends UseRelayBlockEntity implements Resetable {

    public UseRelayChestBlockEntity(BlockPos pos, BlockState state) {
        super(EntityRegistry.USE_RELAY_CHEST_BLOCK_ENTITY, pos, state);
    }

    @Override
    public void reset() {
        if (this.world != null) {
            BlockEntity blockEntity = this.world.getBlockEntity(this.pos);
            if (blockEntity != null && blockEntity.getCachedState().getBlock() instanceof UseRelayChestBlock) {
                this.world.setBlockState(this.pos, blockEntity.getCachedState().with(UseRelayChestBlock.OPEN, false));
            }
        }
    }
}
