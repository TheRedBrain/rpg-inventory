package com.github.theredbrain.rpginventory.entity.player;

import java.util.List;
import net.minecraft.world.item.ItemStack;

public interface DuckPlayerInventoryMixin {

	List<ItemStack> rpginventory$getAdditionalNonArmorEquipmentItems();

	List<ItemStack> rpginventory$getSpellProvidingEquipmentItems();

	void rpginventory$breakKeepInventoryItems();

	ItemStack rpginventory$getAdditionalEquipmentStack(int index);

	ItemStack rpginventory$setAdditionalEquipmentStack(int index, ItemStack itemStack);
}
