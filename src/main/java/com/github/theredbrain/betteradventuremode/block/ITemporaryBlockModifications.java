package com.github.theredbrain.betteradventuremode.block;

import net.minecraft.block.Block;

// TODO remove when data-driven blocks are in vanilla
public interface ITemporaryBlockModifications {
    ITemporaryBlockModifications INSTANCE = new TemporaryBlockModifications();
    void betteradventuremode$setVelocityMultiplier(Block block, float velocityMultiplier);

    void betteradventuremode$setJumpVelocityMultiplier(Block block, float jumpVelocityMultiplier);
}
