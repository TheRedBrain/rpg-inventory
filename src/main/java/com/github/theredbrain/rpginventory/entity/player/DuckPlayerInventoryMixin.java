package com.github.theredbrain.rpginventory.entity.player;

import net.minecraft.item.ItemStack;

import java.util.List;

public interface DuckPlayerInventoryMixin {

	List<ItemStack> rpginventory$getAdditionalNonArmorEquipmentItems();

	List<ItemStack> rpginventory$getSpellProvidingEquipmentItems();

	ItemStack rpginventory$getOffHandStack();

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

	ItemStack rpginventory$getAdditionalEquipmentStack(int index);

	ItemStack rpginventory$setAdditionalEquipmentStack(int index, ItemStack itemStack);
}
