package com.github.theredbrain.rpgmod.entity.player;

import net.minecraft.item.ItemStack;
import net.minecraft.util.collection.DefaultedList;

public interface DuckPlayerInventoryMixin {

//    DefaultedList<ItemStack> getActiveGear();
    ItemStack bam$getOffHandStack();


    DefaultedList<ItemStack> bam$getAccessories(int slot);
    ItemStack bam$getAccessorySlot(int slot);
    ItemStack bam$setAccessorySlot(int slot, ItemStack itemStack);
    ItemStack bam$getMainHand();
    ItemStack bam$setMainHand(ItemStack itemStack);
    ItemStack bam$getAlternativeMainHand();
    ItemStack bam$setAlternativeMainHand(ItemStack itemStack);
    ItemStack bam$getAlternativeOffHand();
    ItemStack bam$setAlternativeOffHand(ItemStack itemStack);
    ItemStack bam$getPlayerSkinItem();
    ItemStack bam$setPlayerSkinItem(ItemStack itemStack);
}
