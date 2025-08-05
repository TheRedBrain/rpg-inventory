package com.github.theredbrain.rpginventory.compat;

import com.github.theredbrain.inventorysizeattributes.entity.player.DuckPlayerEntityMixin;
import net.minecraft.entity.player.PlayerEntity;

public class InventorySizeAttributesCompat {

	public static int getActiveInventorySize(PlayerEntity player) {
		return ((DuckPlayerEntityMixin) player).inventorysizeattributes$getActiveInventorySlotAmount();
	}

	public static int getActiveHotbarSize(PlayerEntity player) {
		return ((DuckPlayerEntityMixin) player).inventorysizeattributes$getActiveHotbarSlotAmount();
	}

}
