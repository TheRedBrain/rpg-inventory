package com.github.theredbrain.rpginventory.screen.slot;

import com.github.theredbrain.rpginventory.RPGInventory;
import com.github.theredbrain.rpginventory.config.ServerConfig;
import com.github.theredbrain.rpginventory.entity.ExtendedEquipmentSlot;
import com.github.theredbrain.rpginventory.util.ItemUtils;
import com.github.theredbrain.slotcustomizationapi.api.SlotCustomization;
import com.mojang.datafixers.util.Pair;

import java.util.List;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.Container;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentEffectComponents;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import org.jspecify.annotations.Nullable;

public class CustomArmorSlot extends Slot {
	private final Player owner;
	private final EquipmentSlot equipmentSlot;
	@Nullable
	private final Identifier backgroundSprite;
	private final boolean allowsLoadoutItemRemoval;

	public CustomArmorSlot(Container inventory, Player playerEntity, EquipmentSlot equipmentSlot, int index, int x, int y, @Nullable Identifier backgroundSprite, List<Component> tooltip) {
		this(inventory, playerEntity, equipmentSlot, index, x, y, backgroundSprite, tooltip, false);
	}

	public CustomArmorSlot(Container inventory, Player playerEntity, EquipmentSlot equipmentSlot, int index, int x, int y, @Nullable Identifier backgroundSprite, List<Component> tooltip, boolean allowsLoadoutItemRemoval) {
		super(inventory, index, x, y);
		this.owner = playerEntity;
		this.equipmentSlot = equipmentSlot;
		this.backgroundSprite = backgroundSprite;
		this.allowsLoadoutItemRemoval = allowsLoadoutItemRemoval;
		((SlotCustomization) this).slotcustomizationapi$setSlotTooltipText(tooltip);
	}

	@Override
	public void onTake(Player player, ItemStack stack) {
		if (stack.has(RPGInventory.LOAD_OUT_ITEM) && this.allowsLoadoutItemRemoval) {
			stack.setCount(0);
		}
		super.onTake(player, stack);
	}

	@Override
	public void setByPlayer(ItemStack stack, ItemStack previousStack) {
		this.owner.onEquipItem(this.equipmentSlot, previousStack, stack);
		super.setByPlayer(stack, previousStack);
	}

	@Override
	public int getMaxStackSize() {
		return 1;
	}

	@Override
	public boolean mayPlace(ItemStack stack) {
		ServerConfig serverConfig = RPGInventory.SERVER_CONFIG;

		return (equipmentSlot == this.owner.getEquipmentSlotForItem(stack) || ExtendedEquipmentSlot.rpginventory$isOfEquipmentTag(stack, equipmentSlot) || !serverConfig.handSlotOverhaul.are_hand_items_restricted_to_item_tags.get()) && ItemUtils.isUsableByPlayer(stack, this.owner) && (stack.has(RPGInventory.IGNORES_EQUIPMENT_CHANGE_RESTRICTIONS) || this.owner.hasEffect(RPGInventory.CIVILISATION) || this.owner.isCreative() || (serverConfig.allow_equipment_changes.get() && !this.owner.hasEffect(RPGInventory.WILDERNESS)));
	}

	@Override
	public boolean mayPickup(Player playerEntity) {

		boolean isCreative = playerEntity.isCreative();

		ItemStack itemStack = this.getItem();
		return (
				!itemStack.isEmpty()
						&& !isCreative
						&& EnchantmentHelper.has(itemStack, EnchantmentEffectComponents.PREVENT_ARMOR_CHANGE)
						? false
						: super.mayPickup(playerEntity)
		) && (!this.getItem().has(RPGInventory.LOAD_OUT_ITEM) || this.allowsLoadoutItemRemoval) && (itemStack.has(RPGInventory.IGNORES_EQUIPMENT_CHANGE_RESTRICTIONS) || this.owner.hasEffect(RPGInventory.CIVILISATION) || isCreative || (RPGInventory.SERVER_CONFIG.allow_equipment_changes.get() && !this.owner.hasEffect(RPGInventory.WILDERNESS)));
	}

	@Override
	@Nullable
	public Identifier getNoItemIcon() {
		return this.backgroundSprite;
	}
}
