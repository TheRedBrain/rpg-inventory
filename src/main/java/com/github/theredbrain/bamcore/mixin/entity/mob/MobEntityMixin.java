package com.github.theredbrain.bamcore.mixin.entity.mob;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Arrays;

@Mixin(MobEntity.class)
public class MobEntityMixin {

    @Shadow
    @Final
    @Mutable
    private DefaultedList<ItemStack> handItems;

    @Shadow
    @Final
    @Mutable
    protected float[] handDropChances;


    /**
     * @author TheRedBrain
     */
    @Inject(method = "<init>", at = @At("TAIL"))
    public void MobEntity(EntityType entityType, World world, CallbackInfo ci) {
        this.handItems = DefaultedList.ofSize(4, ItemStack.EMPTY);
        this.handDropChances = new float[4];
        Arrays.fill(this.handDropChances, 0.085f);
        // inject into a constructor
    }
}
