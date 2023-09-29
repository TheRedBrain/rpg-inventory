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
    private DefaultedList<ItemStack> handItems;// = DefaultedList.ofSize(4, ItemStack.EMPTY);//newHandItems();

    @Shadow
    @Final
    @Mutable
    protected float[] handDropChances;// = new float[4];//newHandDropChances();

//    @Shadow
//    @Final
//    @Mutable
//    private DefaultedList<ItemStack> armorItems;// = DefaultedList.ofSize(10, ItemStack.EMPTY);//newArmorItems();
//
//    @Shadow
//    @Final
//    @Mutable
//    protected float[] armorDropChances;// = new float[10];//newArmorDropChances();


    /**
     * @author TheRedBrain
     */
    @Inject(method = "<init>", at = @At("TAIL"))
    public void MobEntity(EntityType entityType, World world, CallbackInfo ci) {
        this.handItems = DefaultedList.ofSize(4, ItemStack.EMPTY);
        this.handDropChances = new float[4];
//        this.armorItems = DefaultedList.ofSize(6, ItemStack.EMPTY);
//        this.armorDropChances = new float[10];
//        Arrays.fill(this.armorDropChances, 0.085f);
        Arrays.fill(this.handDropChances, 0.085f);
//        this.syncedHandStacks = DefaultedList.ofSize(4, ItemStack.EMPTY);
//        this.syncedArmorStacks = DefaultedList.ofSize(10, ItemStack.EMPTY);
        // inject into a constructor
    }
//    private DefaultedList<ItemStack> newHandItems() {
//        return DefaultedList.ofSize(4, ItemStack.EMPTY);
//    }
//
//    private float[] newHandDropChances() {
//        return new float[4];
//    }
//
//    private DefaultedList<ItemStack> newArmorItems() {
//        return DefaultedList.ofSize(10, ItemStack.EMPTY);
//    }
//
//    private float[] newArmorDropChances() {
//        return new float[10];
//    }
}
