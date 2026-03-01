package com.github.theredbrain.rpginventory.screen.slot;

import com.github.theredbrain.rpginventory.RPGInventory;
import com.github.theredbrain.rpginventory.config.ServerConfig;
import com.github.theredbrain.rpginventory.entity.ExtendedEquipmentSlot;
import com.github.theredbrain.rpginventory.util.ItemUtils;
import com.github.theredbrain.slotcustomizationapi.api.SlotCustomization;
import com.mojang.datafixers.util.Pair;
import net.minecraft.component.EnchantmentEffectComponentTypes;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class AlternativeHandSlot extends Slot {
	private final PlayerEntity owner;
	private final EquipmentSlot equipmentSlot;
	@Nullable
	private final Identifier backgroundSprite;
	private final boolean allowsLoadoutItemRemoval;

	public AlternativeHandSlot(Inventory inventory, PlayerEntity playerEntity, EquipmentSlot equipmentSlot, int index, int x, int y, @Nullable Identifier backgroundSprite, List<Text> tooltip) {
		this(inventory, playerEntity, equipmentSlot, index, x, y, backgroundSprite, tooltip, false);
	}

	public AlternativeHandSlot(Inventory inventory, PlayerEntity playerEntity, EquipmentSlot equipmentSlot, int index, int x, int y, @Nullable Identifier backgroundSprite, List<Text> tooltip, boolean allowsLoadoutItemRemoval) {
		super(inventory, index, x, y);
		this.owner = playerEntity;
		this.equipmentSlot = equipmentSlot;
		this.backgroundSprite = backgroundSprite;
		this.allowsLoadoutItemRemoval = allowsLoadoutItemRemoval;
		((SlotCustomization) this).slotcustomizationapi$setSlotTooltipText(tooltip);
	}

	@Override
	public void onTakeItem(PlayerEntity player, ItemStack stack) {
		if (stack.contains(RPGInventory.LOAD_OUT_ITEM) && this.allowsLoadoutItemRemoval) {
			stack.setCount(0);
		}
		super.onTakeItem(player, stack);
	}

	@Override
	public int getMaxItemCount() {
		return 1;
	}

	@Override
	public boolean canInsert(ItemStack stack) {
		ServerConfig serverConfig = RPGInventory.SERVER_CONFIG;

		return (!serverConfig.handSlotOverhaul.are_hand_items_restricted_to_item_tags.get() || equipmentSlot == this.owner.getPreferredEquipmentSlot(stack) || ExtendedEquipmentSlot.rpginventory$isOfEquipmentTag(stack, equipmentSlot)) && ItemUtils.isUsableByPlayer(stack, this.owner) && (stack.contains(RPGInventory.IGNORES_EQUIPMENT_CHANGE_RESTRICTIONS) || owner.hasStatusEffect(RPGInventory.CIVILISATION) || this.owner.isCreative() || (serverConfig.allow_equipment_changes.get() && !owner.hasStatusEffect(RPGInventory.WILDERNESS)));
	}

	@Override
	public boolean canTakeItems(PlayerEntity playerEntity) {

		boolean isCreative = playerEntity.isCreative();

		ItemStack itemStack = this.getStack();
		return (
				!itemStack.isEmpty()
						&& !isCreative
						&& EnchantmentHelper.hasAnyEnchantmentsWith(itemStack, EnchantmentEffectComponentTypes.PREVENT_ARMOR_CHANGE)
						? false
						: super.canTakeItems(playerEntity)
		) && (!itemStack.contains(RPGInventory.LOAD_OUT_ITEM) || this.allowsLoadoutItemRemoval) && (itemStack.contains(RPGInventory.IGNORES_EQUIPMENT_CHANGE_RESTRICTIONS) || owner.hasStatusEffect(RPGInventory.CIVILISATION) || isCreative || (RPGInventory.SERVER_CONFIG.allow_equipment_changes.get() && !owner.hasStatusEffect(RPGInventory.WILDERNESS)));
	}

	@Override
	public Pair<Identifier, Identifier> getBackgroundSprite() {
		return this.backgroundSprite != null ? Pair.of(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, this.backgroundSprite) : super.getBackgroundSprite();
	}
}