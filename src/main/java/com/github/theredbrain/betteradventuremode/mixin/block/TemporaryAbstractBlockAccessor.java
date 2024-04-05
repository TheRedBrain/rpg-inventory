package com.github.theredbrain.betteradventuremode.mixin.block;

import net.minecraft.block.AbstractBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(AbstractBlock.class)
public interface TemporaryAbstractBlockAccessor {

    @Accessor("velocityMultiplier")
    void betteradventuremode$setVelocityMultiplier(float velocityMultiplier);

    @Accessor("jumpVelocityMultiplier")
    void betteradventuremode$setJumpVelocityMultiplier(float jumpVelocityMultiplier);
}
