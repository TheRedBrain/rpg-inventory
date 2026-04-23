package com.github.theredbrain.rpginventory.compat;

import com.github.theredbrain.inventorysizeattributes.entity.player.DuckPlayerEntityMixin;
import net.minecraft.world.entity.player.Player;

public class InventorySizeAttributesCompat {

	public static int getActiveInventorySize(Player player) {
		return ((DuckPlayerEntityMixin) player).inventorysizeattributes$getActiveInventorySlotAmount();
	}

	public static int getActiveHotbarSize(Player player) {
		return ((DuckPlayerEntityMixin) player).inventorysizeattributes$getActiveHotbarSlotAmount();
	}

}
