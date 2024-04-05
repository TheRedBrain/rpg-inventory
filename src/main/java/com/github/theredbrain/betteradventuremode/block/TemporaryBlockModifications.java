package com.github.theredbrain.betteradventuremode.block;

import com.github.theredbrain.betteradventuremode.mixin.block.TemporaryAbstractBlockAccessor;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;

// TODO remove when data-driven blocks are in vanilla
public class TemporaryBlockModifications implements ITemporaryBlockModifications {

    @Override
    public void betteradventuremode$setVelocityMultiplier(Block block, float velocityMultiplier) {
        ((TemporaryAbstractBlockAccessor) block).betteradventuremode$setVelocityMultiplier(velocityMultiplier);
    }

    @Override
    public void betteradventuremode$setJumpVelocityMultiplier(Block block, float jumpVelocityMultiplier) {
        ((TemporaryAbstractBlockAccessor) block).betteradventuremode$setJumpVelocityMultiplier(jumpVelocityMultiplier);
    }

    public static void betteradventuremode$applyVelocityModifications() {

        ITemporaryBlockModifications.INSTANCE.betteradventuremode$setVelocityMultiplier(Blocks.SAND, 0.75F);
        ITemporaryBlockModifications.INSTANCE.betteradventuremode$setVelocityMultiplier(Blocks.RED_SAND, 0.75F);
        ITemporaryBlockModifications.INSTANCE.betteradventuremode$setVelocityMultiplier(Blocks.SUSPICIOUS_SAND, 0.75F);
        ITemporaryBlockModifications.INSTANCE.betteradventuremode$setVelocityMultiplier(Blocks.SNOW, 0.75F);
        ITemporaryBlockModifications.INSTANCE.betteradventuremode$setVelocityMultiplier(Blocks.SNOW_BLOCK, 0.75F);
        ITemporaryBlockModifications.INSTANCE.betteradventuremode$setVelocityMultiplier(Blocks.FARMLAND, 0.75F);
        ITemporaryBlockModifications.INSTANCE.betteradventuremode$setVelocityMultiplier(Blocks.WHITE_WOOL, 0.75F);
        ITemporaryBlockModifications.INSTANCE.betteradventuremode$setVelocityMultiplier(Blocks.ORANGE_WOOL, 0.75F);
        ITemporaryBlockModifications.INSTANCE.betteradventuremode$setVelocityMultiplier(Blocks.MAGENTA_WOOL, 0.75F);
        ITemporaryBlockModifications.INSTANCE.betteradventuremode$setVelocityMultiplier(Blocks.LIGHT_BLUE_WOOL, 0.75F);
        ITemporaryBlockModifications.INSTANCE.betteradventuremode$setVelocityMultiplier(Blocks.YELLOW_WOOL, 0.75F);
        ITemporaryBlockModifications.INSTANCE.betteradventuremode$setVelocityMultiplier(Blocks.LIME_WOOL, 0.75F);
        ITemporaryBlockModifications.INSTANCE.betteradventuremode$setVelocityMultiplier(Blocks.PINK_WOOL, 0.75F);
        ITemporaryBlockModifications.INSTANCE.betteradventuremode$setVelocityMultiplier(Blocks.GRAY_WOOL, 0.75F);
        ITemporaryBlockModifications.INSTANCE.betteradventuremode$setVelocityMultiplier(Blocks.LIGHT_GRAY_WOOL, 0.75F);
        ITemporaryBlockModifications.INSTANCE.betteradventuremode$setVelocityMultiplier(Blocks.CYAN_WOOL, 0.75F);
        ITemporaryBlockModifications.INSTANCE.betteradventuremode$setVelocityMultiplier(Blocks.PURPLE_WOOL, 0.75F);
        ITemporaryBlockModifications.INSTANCE.betteradventuremode$setVelocityMultiplier(Blocks.BLUE_WOOL, 0.75F);
        ITemporaryBlockModifications.INSTANCE.betteradventuremode$setVelocityMultiplier(Blocks.BROWN_WOOL, 0.75F);
        ITemporaryBlockModifications.INSTANCE.betteradventuremode$setVelocityMultiplier(Blocks.GREEN_WOOL, 0.75F);
        ITemporaryBlockModifications.INSTANCE.betteradventuremode$setVelocityMultiplier(Blocks.RED_WOOL, 0.75F);
        ITemporaryBlockModifications.INSTANCE.betteradventuremode$setVelocityMultiplier(Blocks.BLACK_WOOL, 0.75F);
        ITemporaryBlockModifications.INSTANCE.betteradventuremode$setVelocityMultiplier(Blocks.OAK_LEAVES, 0.75F);
        ITemporaryBlockModifications.INSTANCE.betteradventuremode$setVelocityMultiplier(Blocks.SPRUCE_LEAVES, 0.75F);
        ITemporaryBlockModifications.INSTANCE.betteradventuremode$setVelocityMultiplier(Blocks.BIRCH_LEAVES, 0.75F);
        ITemporaryBlockModifications.INSTANCE.betteradventuremode$setVelocityMultiplier(Blocks.JUNGLE_LEAVES, 0.75F);
        ITemporaryBlockModifications.INSTANCE.betteradventuremode$setVelocityMultiplier(Blocks.ACACIA_LEAVES, 0.75F);
        ITemporaryBlockModifications.INSTANCE.betteradventuremode$setVelocityMultiplier(Blocks.CHERRY_LEAVES, 0.75F);
        ITemporaryBlockModifications.INSTANCE.betteradventuremode$setVelocityMultiplier(Blocks.DARK_OAK_LEAVES, 0.75F);
        ITemporaryBlockModifications.INSTANCE.betteradventuremode$setVelocityMultiplier(Blocks.MANGROVE_LEAVES, 0.75F);
        ITemporaryBlockModifications.INSTANCE.betteradventuremode$setVelocityMultiplier(Blocks.MANGROVE_LEAVES, 0.75F);
        ITemporaryBlockModifications.INSTANCE.betteradventuremode$setVelocityMultiplier(Blocks.FLOWERING_AZALEA_LEAVES, 0.75F);

        ITemporaryBlockModifications.INSTANCE.betteradventuremode$setVelocityMultiplier(Blocks.DIRT, 0.85F);
        ITemporaryBlockModifications.INSTANCE.betteradventuremode$setVelocityMultiplier(Blocks.GRAVEL, 0.85F);
        ITemporaryBlockModifications.INSTANCE.betteradventuremode$setVelocityMultiplier(Blocks.SUSPICIOUS_GRAVEL, 0.85F);
        ITemporaryBlockModifications.INSTANCE.betteradventuremode$setVelocityMultiplier(Blocks.COARSE_DIRT, 0.85F);
        ITemporaryBlockModifications.INSTANCE.betteradventuremode$setVelocityMultiplier(Blocks.MOSS_BLOCK, 0.85F);

        ITemporaryBlockModifications.INSTANCE.betteradventuremode$setVelocityMultiplier(Blocks.GRASS_BLOCK, 0.9F);
        ITemporaryBlockModifications.INSTANCE.betteradventuremode$setVelocityMultiplier(Blocks.PODZOL, 0.9F);
        ITemporaryBlockModifications.INSTANCE.betteradventuremode$setVelocityMultiplier(Blocks.MYCELIUM, 0.9F);
        ITemporaryBlockModifications.INSTANCE.betteradventuremode$setVelocityMultiplier(Blocks.ROOTED_DIRT, 0.9F);

        ITemporaryBlockModifications.INSTANCE.betteradventuremode$setVelocityMultiplier(Blocks.DIRT_PATH, 1.1F);
    }
}
