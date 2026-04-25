package com.github.theredbrain.rpginventory.screen.slot;

import com.github.theredbrain.rpginventory.RPGInventory;
import com.github.theredbrain.rpginventory.config.ServerConfig;
import com.github.theredbrain.rpginventory.entity.ExtendedEquipmentSlot;
import com.github.theredbrain.rpginventory.util.ItemUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.Container;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.Nullable;

import java.util.List;

public class CustomHandSlot extends CustomArmorSlot {

	public CustomHandSlot(Container inventory, Player playerEntity, EquipmentSlot equipmentSlot, int index, int x, int y, @Nullable Identifier backgroundSprite, List<Component> tooltip, boolean allowsLoadoutItemRemoval) {
		super(inventory, playerEntity, equipmentSlot, index, x, y, backgroundSprite, tooltip, allowsLoadoutItemRemoval);
	}

	@Override
	public boolean mayPlace(ItemStack stack) {
		ServerConfig serverConfig = RPGInventory.SERVER_CONFIG;

		return (this.equipmentSlot == this.owner.getEquipmentSlotForItem(stack) || ExtendedEquipmentSlot.rpginventory$isOfEquipmentTag(stack, equipmentSlot) || !serverConfig.handSlotOverhaul.are_hand_items_restricted_to_item_tags.get()) && ItemUtils.isUsableByPlayer(stack, this.owner) && (stack.has(RPGInventory.IGNORES_EQUIPMENT_CHANGE_RESTRICTIONS) || this.owner.hasEffect(RPGInventory.CIVILISATION) || this.owner.isCreative() || (serverConfig.allow_equipment_changes.get() && !this.owner.hasEffect(RPGInventory.WILDERNESS)));
	}

}
