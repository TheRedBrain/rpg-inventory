package com.github.theredbrain.rpginventory.mixin.screen;

import com.github.theredbrain.rpginventory.RPGInventory;
import com.github.theredbrain.rpginventory.config.ServerConfig;
import com.github.theredbrain.rpginventory.entity.DuckLivingEntityMixin;
import com.github.theredbrain.rpginventory.registry.Tags;
import com.github.theredbrain.rpginventory.util.ItemUtils;
import net.minecraft.world.Container;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(targets = {"net/minecraft/world/inventory/InventoryMenu$1"})
public abstract class PlayerScreenHandlerOffHandSlotMixin extends Slot {

	@Shadow
	@Final
	Player val$owner;

	public PlayerScreenHandlerOffHandSlotMixin(Container inventory, int index, int x, int y) {
		super(inventory, index, x, y);
	}

	@Override
	public boolean isActive() {
		return !RPGInventory.isHandSlotOverhaulActive() || !((DuckLivingEntityMixin) this.val$owner).rpginventory$isOffhandStackSheathed();
	}

	@Override
	public boolean mayPlace(ItemStack stack) {
		ServerConfig serverConfig = RPGInventory.SERVER_CONFIG;

		boolean handSlotOverhaulIsInactive = !RPGInventory.isHandSlotOverhaulActive();

		return (EquipmentSlot.OFFHAND == this.val$owner.getEquipmentSlotForItem(stack) || stack.is(Tags.OFFHAND_ITEMS) || !serverConfig.handSlotOverhaul.are_hand_items_restricted_to_item_tags.get() || handSlotOverhaulIsInactive) && (handSlotOverhaulIsInactive || !((DuckLivingEntityMixin) this.val$owner).rpginventory$isOffhandStackSheathed()) && ItemUtils.isUsableByPlayer(stack, this.val$owner) && (stack.has(RPGInventory.IGNORES_EQUIPMENT_CHANGE_RESTRICTIONS) || this.val$owner.hasEffect(RPGInventory.CIVILISATION) || this.val$owner.isCreative() || (serverConfig.allow_equipment_changes.get() && !this.val$owner.hasEffect(RPGInventory.WILDERNESS)));
	}

	@Override
	public boolean mayPickup(Player playerEntity) {
		return !this.getItem().has(RPGInventory.LOAD_OUT_ITEM) && (this.getItem().has(RPGInventory.IGNORES_EQUIPMENT_CHANGE_RESTRICTIONS) || playerEntity.hasEffect(RPGInventory.CIVILISATION) || playerEntity.isCreative() || (RPGInventory.SERVER_CONFIG.allow_equipment_changes.get() && !playerEntity.hasEffect(RPGInventory.WILDERNESS)));
	}
}
