package com.github.theredbrain.bamcore.entity.player;

import net.minecraft.item.ItemStack;

import java.util.List;

public interface DuckPlayerInventoryMixin {

    List<ItemStack> getArmorTrinkets();

    ItemStack bamcore$getOffHandStack();

    // these methods get/set the ItemStacks in the TrinketSlot inventories
    ItemStack bamcore$getEmptyMainHand();
    ItemStack bamcore$setEmptyMainHand(ItemStack itemStack);
    ItemStack bamcore$getEmptyOffHand();
    ItemStack bamcore$setEmptyOffHand(ItemStack itemStack);
    ItemStack bamcore$getMainHand();
    ItemStack bamcore$setMainHand(ItemStack itemStack);
    ItemStack bamcore$getOffHand();
    ItemStack bamcore$setOffHand(ItemStack itemStack);
    ItemStack bamcore$getAlternativeMainHand();
    ItemStack bamcore$setAlternativeMainHand(ItemStack itemStack);
    ItemStack bamcore$getAlternativeOffHand();
    ItemStack bamcore$setAlternativeOffHand(ItemStack itemStack);
    ItemStack bamcore$getHeadStack();
    ItemStack bamcore$setHeadStack(ItemStack itemStack);
    ItemStack bamcore$getChestStack();
    ItemStack bamcore$setChestStack(ItemStack itemStack);
    ItemStack bamcore$getLegsStack();
    ItemStack bamcore$setLegsStack(ItemStack itemStack);
    ItemStack bamcore$getFeetStack();
    ItemStack bamcore$setFeetStack(ItemStack itemStack);
    ItemStack bamcore$getGlovesStack();
    ItemStack bamcore$setGlovesStack(ItemStack itemStack);
    ItemStack bamcore$getShouldersStack();
    ItemStack bamcore$setShouldersStack(ItemStack itemStack);
    ItemStack bamcore$getRing1Stack();
    ItemStack bamcore$setRing1Stack(ItemStack itemStack);
    ItemStack bamcore$getRing2Stack();
    ItemStack bamcore$setRing2Stack(ItemStack itemStack);
    ItemStack bamcore$getBeltStack();
    ItemStack bamcore$setBeltStack(ItemStack itemStack);
    ItemStack bamcore$getNecklaceStack();
    ItemStack bamcore$setNecklaceStack(ItemStack itemStack);
    ItemStack bamcore$getSpellSlotStack(int spellSlotNumber);
    ItemStack bamcore$setSpellSlotStack(ItemStack itemStack, int spellSlotNumber);
}
