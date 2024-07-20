package com.github.theredbrain.rpginventory.entity.player;

import net.minecraft.item.ItemStack;

import java.util.List;

public interface DuckPlayerInventoryMixin {

	List<ItemStack> rpginventory$getArmor();

	ItemStack rpginventory$getOffHandStack();

	// these methods get/set the ItemStacks in the TrinketSlot inventories
	ItemStack rpginventory$getEmptyHand();

	ItemStack rpginventory$setEmptyHand(ItemStack itemStack);

	ItemStack rpginventory$getEmptyOffhand();

	ItemStack rpginventory$setEmptyOffhand(ItemStack itemStack);

	ItemStack rpginventory$getSheathedHand();

	ItemStack rpginventory$setSheathedHand(ItemStack itemStack);

	ItemStack rpginventory$getSheathedOffhand();

	ItemStack rpginventory$setSheathedOffhand(ItemStack itemStack);

	ItemStack rpginventory$getHand();

	ItemStack rpginventory$setHand(ItemStack itemStack);

	ItemStack rpginventory$getAlternativeHand();

	ItemStack rpginventory$setAlternativeHand(ItemStack itemStack);

	ItemStack rpginventory$getAlternativeOffhand();

	ItemStack rpginventory$setAlternativeOffhand(ItemStack itemStack);

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
