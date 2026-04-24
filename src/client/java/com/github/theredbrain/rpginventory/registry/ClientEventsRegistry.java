package com.github.theredbrain.rpginventory.registry;

import com.github.theredbrain.rpginventory.RPGInventory;
import com.github.theredbrain.rpginventory.RPGInventoryClient;
import com.github.theredbrain.rpginventory.config.ClientConfig;
import com.github.theredbrain.rpginventory.item.ItemTooltipHelper;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;

public class ClientEventsRegistry {

	public static void initializeClientEvents() {
		ItemTooltipCallback.EVENT.register((stack, context, type, lines) -> {
			ClientConfig clientConfig = RPGInventoryClient.CLIENT_CONFIG;
			boolean isLoadOutItem = stack.has(RPGInventory.LOAD_OUT_ITEM);

			if (isLoadOutItem && clientConfig.itemTooltipSection.show_load_out_item_tooltip.get()) {
				ItemTooltipHelper.addLoadOutItemsTooltipLines(lines);
			}

			if (!isLoadOutItem && clientConfig.itemTooltipSection.show_item_tooltip_bound_to_player_name.get()) {
				ItemTooltipHelper.addPlayerBoundItemTooltipLines(lines, stack);
			}

			if (!isLoadOutItem && clientConfig.itemTooltipSection.show_item_tooltip_crafted_by_player_name.get()) {
				ItemTooltipHelper.addPlayerCraftedItemTooltipLines(lines, stack);
			}

			if (!isLoadOutItem && clientConfig.itemTooltipSection.show_item_tooltip_advancement_locked.get()) {
				ItemTooltipHelper.addAdvancementLockedItemTooltipLines(lines, stack);
			}

			if (stack.is(Tags.TWO_HANDED_ITEMS) && clientConfig.itemTooltipSection.show_item_tooltip_two_handed_items.get() && RPGInventory.SERVER_CONFIG.enable_two_handed_items_restriction.get()) {
				ItemTooltipHelper.addTwoHandedItemTooltipLines(lines);
			}

			if (!isLoadOutItem && clientConfig.itemTooltipSection.show_item_tooltip_equipment_slots.get()) {
				ItemTooltipHelper.addEquipmentSlotTooltipLines(lines, stack);
			}
		});
	}
}
