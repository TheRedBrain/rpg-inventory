package com.github.theredbrain.rpginventory.gui;

import com.github.theredbrain.rpginventory.RPGInventory;
import com.github.theredbrain.rpginventory.RPGInventoryClient;
import com.github.theredbrain.rpginventory.component.type.AdvancementLockedComponent;
import com.github.theredbrain.rpginventory.config.ClientConfig;
import com.github.theredbrain.rpginventory.network.DuckClientAdvancementManagerMixin;
import com.github.theredbrain.rpginventory.util.ItemUtils;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.advancement.AdvancementEntry;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.network.ClientAdvancementManager;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

public class SlotOverlayHelper {

	public static void drawCustomSlotOverlays(DrawContext context, int x, int y, ItemStack itemStack, ClientPlayerEntity clientPlayerEntity) {

		ClientConfig clientConfig = RPGInventoryClient.CLIENT_CONFIG;
		if (!ItemUtils.isUsable(itemStack) && RPGInventoryClient.CLIENT_CONFIG.slots_with_unusable_items_have_overlay.get()) {
			drawSlotHighlight(context, x, y, 0, clientConfig.first_overlay_colour_for_slots_with_unusable_items.toInt(), clientConfig.second_overlay_colour_for_slots_with_unusable_items.toInt());
		} else if (itemStack.contains(RPGInventory.LOAD_OUT_ITEM) && RPGInventoryClient.CLIENT_CONFIG.slots_with_loadout_items_have_overlay.get()) {
			drawSlotHighlight(context, x, y, 0, clientConfig.first_overlay_colour_for_slots_with_loadout_items.toInt(), clientConfig.second_overlay_colour_for_slots_with_loadout_items.toInt());
		} else if (!ItemUtils.isOwnedByPlayer(itemStack, clientPlayerEntity.getGameProfile()) && RPGInventoryClient.CLIENT_CONFIG.slots_with_not_owned_items_have_overlay.get()) {
			drawSlotHighlight(context, x, y, 0, clientConfig.first_overlay_colour_for_slots_with_not_owned_items.toInt(), clientConfig.second_overlay_colour_for_slots_with_not_owned_items.toInt());
		} else if (RPGInventoryClient.CLIENT_CONFIG.slots_with_advancement_locked_items_have_overlay.get()) {
			int status = getClientSideAdvancementLockedStatus(itemStack, clientPlayerEntity);
			if (status == 0) {
				drawSlotHighlight(context, x, y, 0, clientConfig.first_overlay_colour_for_slots_with_advancement_not_unlocked_items.toInt(), clientConfig.second_overlay_colour_for_slots_with_advancement_not_unlocked_items.toInt());
			} else if (status == 2) {
				drawSlotHighlight(context, x, y, 0, clientConfig.first_overlay_colour_for_slots_with_advancement_locked_items.toInt(), clientConfig.second_overlay_colour_for_slots_with_advancement_locked_items.toInt());
			}
		}
	}

	public static int getClientSideAdvancementLockedStatus(ItemStack itemStack, ClientPlayerEntity clientPlayerEntity) {
		// status 0: not unlocked, 1: unlocked, 2: locked
		int status = 1;

		AdvancementLockedComponent advancementLockedComponent = itemStack.get(RPGInventory.ADVANCEMENT_LOCKED);
		if (advancementLockedComponent != null) {

			if (!advancementLockedComponent.unlock_advancement().isEmpty() || !advancementLockedComponent.lock_advancement().isEmpty()) {
				status = 0;
			}

			ClientAdvancementManager clientAdvancementManager = clientPlayerEntity.networkHandler.getAdvancementHandler();

			AdvancementEntry unlockAdvancementEntry = null;
			AdvancementEntry lockAdvancementEntry = null;
			if (clientAdvancementManager != null) {
				if (!advancementLockedComponent.unlock_advancement().isEmpty()) {
					unlockAdvancementEntry = clientAdvancementManager.get(Identifier.of(advancementLockedComponent.unlock_advancement()));
				}
				if (!advancementLockedComponent.lock_advancement().isEmpty()) {
					lockAdvancementEntry = clientAdvancementManager.get(Identifier.of(advancementLockedComponent.lock_advancement()));
				}
			}

			boolean is_unlocked = false;
			boolean is_locked = false;
			if (unlockAdvancementEntry != null) {
				is_unlocked = ((DuckClientAdvancementManagerMixin) clientAdvancementManager).rpginventory$getAdvancementProgressDone(unlockAdvancementEntry);
			}
			if (lockAdvancementEntry != null) {
				is_locked = ((DuckClientAdvancementManagerMixin) clientAdvancementManager).rpginventory$getAdvancementProgressDone(lockAdvancementEntry);
			}

			if (is_unlocked) {
				status = 1;
			}
			if (is_locked) {
				status = 2;
			}
		}
		return status;
	}

	private static void drawSlotHighlight(DrawContext context, int x, int y, int z, int colorStart, int colorEnd) {
		RenderSystem.disableDepthTest();
		RenderSystem.colorMask(true, true, true, false);
		context.fillGradient(RenderLayer.getGuiOverlay(), x, y, x + 16, y + 16, colorStart, colorEnd, z);
		RenderSystem.colorMask(true, true, true, true);
		RenderSystem.enableDepthTest();
	}
}
