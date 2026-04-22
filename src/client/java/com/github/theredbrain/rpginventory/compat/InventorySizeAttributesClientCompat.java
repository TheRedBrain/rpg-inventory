package com.github.theredbrain.rpginventory.compat;

import com.github.theredbrain.inventorysizeattributes.InventorySizeAttributesClient;
import com.github.theredbrain.inventorysizeattributes.entity.player.DuckPlayerEntityMixin;
import com.github.theredbrain.rpginventory.RPGInventory;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;

public class InventorySizeAttributesClientCompat {

	public static boolean showInactiveInventorySlots() {
		return InventorySizeAttributesClient.CLIENT_CONFIG.show_inactive_inventory_slots.get();
	}

	public static int drawAlternativeHotbar(DrawContext context, PlayerEntity player, Identifier hotbarTexture) {

		int activeHotbarSize = ((DuckPlayerEntityMixin) player).inventorysizeattributes$getActiveHotbarSlotAmount();
		int hotbarPositionX = context.getScaledWindowWidth() / 2 - 91;

		if (activeHotbarSize < 9) {
			if (InventorySizeAttributesClient.CLIENT_CONFIG.is_hotbar_centered.get()) {
				hotbarPositionX = hotbarPositionX + ((9 - activeHotbarSize) * 20) / 2;
			}
			context.drawGuiTexture(RPGInventory.identifier("hud/hotbar_" + activeHotbarSize), hotbarPositionX, context.getScaledWindowHeight() - 22, 182 - (9 - activeHotbarSize) * 20, 22);
		} else {
			context.drawGuiTexture(hotbarTexture, context.getScaledWindowWidth() / 2 - 91, context.getScaledWindowHeight() - 22, 182, 22);
		}
		return hotbarPositionX;
	}
}
