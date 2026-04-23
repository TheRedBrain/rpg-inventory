package com.github.theredbrain.rpginventory.compat;

import com.github.theredbrain.inventorysizeattributes.InventorySizeAttributes;
import com.github.theredbrain.inventorysizeattributes.InventorySizeAttributesClient;
import com.github.theredbrain.rpginventory.RPGInventory;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Player;

public class InventorySizeAttributesClientCompat {

	public static boolean showInactiveInventorySlots() {
		return InventorySizeAttributesClient.CLIENT_CONFIG.show_inactive_inventory_slots.get();
	}

	public static int drawAlternativeHotbar(GuiGraphicsExtractor guiGraphicsExtractor, Player player, Identifier hotbarTexture) {

		int activeHotbarSize = InventorySizeAttributes.getActiveHotbarSlotAmount(player);
		int hotbarPositionX = guiGraphicsExtractor.guiWidth() / 2 - 91;

		if (activeHotbarSize < 9) {
			if (InventorySizeAttributesClient.CLIENT_CONFIG.is_hotbar_centered.get()) {
				hotbarPositionX = hotbarPositionX + ((9 - activeHotbarSize) * 20) / 2;
			}
			guiGraphicsExtractor.blitSprite(RenderPipelines.GUI_TEXTURED, RPGInventory.identifier("hud/hotbar_" + activeHotbarSize), hotbarPositionX, guiGraphicsExtractor.guiHeight() - 22, 182 - (9 - activeHotbarSize) * 20, 22);
		} else {
			guiGraphicsExtractor.blitSprite(RenderPipelines.GUI_TEXTURED, hotbarTexture, guiGraphicsExtractor.guiWidth() / 2 - 91, guiGraphicsExtractor.guiHeight() - 22, 182, 22);
		}
		return hotbarPositionX;
	}
}
