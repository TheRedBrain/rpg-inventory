package com.github.theredbrain.rpginventory.entity.player;

import net.minecraft.item.ItemStack;

import java.util.List;

public interface DuckPlayerInventoryMixin {

    List<ItemStack> rpginventory$getArmor();

    ItemStack rpginventory$getOffHandStack();

    // these methods get/set the ItemStacks in the TrinketSlot inventories
    ItemStack rpginventory$getEmptyMainHand();
    ItemStack rpginventory$setEmptyMainHand(ItemStack itemStack);
    ItemStack rpginventory$getEmptyOffHand();
    ItemStack rpginventory$setEmptyOffHand(ItemStack itemStack);
    ItemStack rpginventory$getSheathedMainHand();
    ItemStack rpginventory$setSheathedMainHand(ItemStack itemStack);
    ItemStack rpginventory$getSheathedOffHand();
    ItemStack rpginventory$setSheathedOffHand(ItemStack itemStack);
    ItemStack rpginventory$getMainHand();
    ItemStack rpginventory$setMainHand(ItemStack itemStack);
//    ItemStack rpginventory$getOffHand();
//    ItemStack rpginventory$setOffHand(ItemStack itemStack);
    ItemStack rpginventory$getAlternativeMainHand();
    ItemStack rpginventory$setAlternativeMainHand(ItemStack itemStack);
    ItemStack rpginventory$getAlternativeOffHand();
    ItemStack rpginventory$setAlternativeOffHand(ItemStack itemStack);
    ItemStack rpginventory$getGlovesStack();
    ItemStack rpginventory$setGlovesStack(ItemStack itemStack);
    ItemStack rpginventory$getShouldersStack();
    ItemStack rpginventory$setShouldersStack(ItemStack itemStack);
    ItemStack rpginventory$getRing1Stack();
    ItemStack rpginventory$setRing1Stack(ItemStack itemStack);
    ItemStack rpginventory$getRing2Stack();
    ItemStack rpginventory$setRing2Stack(ItemStack itemStack);
    ItemStack rpginventory$getBeltStack();
    ItemStack rpginventory$setBeltStack(ItemStack itemStack);
    ItemStack rpginventory$getNecklaceStack();
    ItemStack rpginventory$setNecklaceStack(ItemStack itemStack);
    ItemStack rpginventory$getSpellSlotStack(int spellSlotNumber);
    ItemStack rpginventory$setSpellSlotStack(ItemStack itemStack, int spellSlotNumber);
}
