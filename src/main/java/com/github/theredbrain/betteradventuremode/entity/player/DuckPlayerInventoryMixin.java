package com.github.theredbrain.betteradventuremode.entity.player;

import net.minecraft.item.ItemStack;

import java.util.List;

public interface DuckPlayerInventoryMixin {

    List<ItemStack> betteradventuremode$getArmor();

    ItemStack betteradventuremode$getOffHandStack();

    // these methods get/set the ItemStacks in the TrinketSlot inventories
    ItemStack betteradventuremode$getEmptyMainHand();
    ItemStack betteradventuremode$setEmptyMainHand(ItemStack itemStack);
    ItemStack betteradventuremode$getEmptyOffHand();
    ItemStack betteradventuremode$setEmptyOffHand(ItemStack itemStack);
    ItemStack betteradventuremode$getSheathedMainHand();
    ItemStack betteradventuremode$setSheathedMainHand(ItemStack itemStack);
    ItemStack betteradventuremode$getSheathedOffHand();
    ItemStack betteradventuremode$setSheathedOffHand(ItemStack itemStack);
    ItemStack betteradventuremode$getMainHand();
    ItemStack betteradventuremode$setMainHand(ItemStack itemStack);
//    ItemStack betteradventuremode$getOffHand();
//    ItemStack betteradventuremode$setOffHand(ItemStack itemStack);
    ItemStack betteradventuremode$getAlternativeMainHand();
    ItemStack betteradventuremode$setAlternativeMainHand(ItemStack itemStack);
    ItemStack betteradventuremode$getAlternativeOffHand();
    ItemStack betteradventuremode$setAlternativeOffHand(ItemStack itemStack);
    ItemStack betteradventuremode$getGlovesStack();
    ItemStack betteradventuremode$setGlovesStack(ItemStack itemStack);
    ItemStack betteradventuremode$getShouldersStack();
    ItemStack betteradventuremode$setShouldersStack(ItemStack itemStack);
    ItemStack betteradventuremode$getRing1Stack();
    ItemStack betteradventuremode$setRing1Stack(ItemStack itemStack);
    ItemStack betteradventuremode$getRing2Stack();
    ItemStack betteradventuremode$setRing2Stack(ItemStack itemStack);
    ItemStack betteradventuremode$getBeltStack();
    ItemStack betteradventuremode$setBeltStack(ItemStack itemStack);
    ItemStack betteradventuremode$getNecklaceStack();
    ItemStack betteradventuremode$setNecklaceStack(ItemStack itemStack);
    ItemStack betteradventuremode$getSpellSlotStack(int spellSlotNumber);
    ItemStack betteradventuremode$setSpellSlotStack(ItemStack itemStack, int spellSlotNumber);
}
