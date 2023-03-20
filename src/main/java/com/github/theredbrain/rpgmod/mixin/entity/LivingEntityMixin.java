package com.github.theredbrain.rpgmod.mixin.entity;

import com.github.theredbrain.rpgmod.entity.ExtendedEquipmentSlot;
import com.github.theredbrain.rpgmod.item.AccessoryBeltItem;
import com.github.theredbrain.rpgmod.item.AccessoryNecklaceItem;
import net.minecraft.block.AbstractSkullBlock;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.*;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {

    @Shadow
    public DefaultedList<ItemStack> syncedHandStacks;

    @Shadow
    public DefaultedList<ItemStack> syncedArmorStacks;


    /**
     * @author TheRedBrain
     */
    @Inject(method = "<init>", at = @At("TAIL"))
    public void LivingEntity(EntityType entityType, World world, CallbackInfo ci) {
        this.syncedHandStacks = DefaultedList.ofSize(4, ItemStack.EMPTY);
        this.syncedArmorStacks = DefaultedList.ofSize(10, ItemStack.EMPTY);
    }

    /**
     * @author TheRedBrain
     */
    @Inject(method = "getPreferredEquipmentSlot", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/item/ItemStack;getItem()Lnet/minecraft/item/Item;"), cancellable = true, locals = LocalCapture.CAPTURE_FAILHARD)
    private static void bam$getPreferredEquipmentSlot(ItemStack stack, CallbackInfoReturnable<EquipmentSlot> cir, Item item) {
        if (item instanceof AccessoryBeltItem) {
            cir.setReturnValue(ExtendedEquipmentSlot.BELT);
            cir.cancel();
        }
        if (item instanceof AccessoryNecklaceItem) {
            cir.setReturnValue(ExtendedEquipmentSlot.NECKLACE);
            cir.cancel();
        }
    }

//    @Inject(method = "getPreferredEquipmentSlot", at = @At("TAIL"), cancellable = true)
//    private static void bam$getPreferredEquipmentSlot(ItemStack stack, CallbackInfoReturnable<EquipmentSlot> cir) {
//        Item item = stack.getItem();
//        if (item instanceof CustomArmorItem) {
//            cir.setReturnValue(((CustomArmorItem)item).getSlotType());
//            cir.cancel();
//        }
//    }
}
