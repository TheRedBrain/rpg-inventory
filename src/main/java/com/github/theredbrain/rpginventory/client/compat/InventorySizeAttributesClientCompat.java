package com.github.theredbrain.rpginventory.client.compat;

import com.github.theredbrain.inventorysizeattributes.InventorySizeAttributesClient;

public class InventorySizeAttributesClientCompat {

	public static boolean showInactiveInventorySlots() {
		return InventorySizeAttributesClient.CLIENT_CONFIG.show_inactive_inventory_slots.get();
	}

}
