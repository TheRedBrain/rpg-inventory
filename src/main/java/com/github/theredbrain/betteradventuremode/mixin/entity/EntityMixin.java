package com.github.theredbrain.betteradventuremode.mixin.entity;

import com.github.theredbrain.betteradventuremode.registry.Tags;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public abstract class EntityMixin {

    @Shadow public abstract World getWorld();

    @Shadow public abstract BlockPos getBlockPos();

    @Inject(method = "getVelocityMultiplier", at = @At("RETURN"), cancellable = true)
    protected void betteradventuremode$getVelocityMultiplier(CallbackInfoReturnable<Float> cir) {
        cir.setReturnValue(cir.getReturnValue() - (this.getWorld().getBlockState(this.getBlockPos()).isIn(Tags.SLOWS_DOWN_ENTITIES_INSIDE) ? 0.15F : 0.0F));
    }

}
