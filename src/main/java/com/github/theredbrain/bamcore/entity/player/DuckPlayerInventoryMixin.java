package com.github.theredbrain.bamcore.entity.player;

import net.minecraft.item.ItemStack;

public interface DuckPlayerInventoryMixin {

    ItemStack bamcore$getOffHandStack();
    /**
     * call this to set this player's internal unarmed weapon
     * @param itemStack is the default itemStack used when the main hand is empty
     */
    void bamcore$setEmptyMainHandStack(ItemStack itemStack);

    /**
     * call this to set this player's internal unarmed weapon
     * @param itemStack is the default itemStack used when the offhand is empty
     */
    void bamcore$setEmptyOffHandStack(ItemStack itemStack);

    ItemStack bamcore$getMainHand();
    ItemStack bamcore$setMainHand(ItemStack itemStack);
    ItemStack bamcore$getAlternativeMainHand();
    ItemStack bamcore$setAlternativeMainHand(ItemStack itemStack);
    ItemStack bamcore$getAlternativeOffHand();
    ItemStack bamcore$setAlternativeOffHand(ItemStack itemStack);
}
