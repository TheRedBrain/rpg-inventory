package com.github.theredbrain.rpginventory.mixin.screen;

import com.github.theredbrain.rpginventory.RPGInventory;
import com.github.theredbrain.rpginventory.registry.Tags;
import com.github.theredbrain.rpginventory.util.ItemUtils;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(targets = {"net/minecraft/screen/slot/ArmorSlot"})
public abstract class ArmorSlotMixin extends Slot {

	@Shadow
	@Final
	private LivingEntity entity;

	@Shadow
	@Final
	private EquipmentSlot equipmentSlot;

	public ArmorSlotMixin(Inventory inventory, int index, int x, int y) {
		super(inventory, index, x, y);
	}

	@WrapMethod(method = "canInsert")
	public boolean rpginventory$canInsert(ItemStack stack, Operation<Boolean> original) {

		boolean isOwned = true;
		boolean isCreative = false;
		if (entity instanceof PlayerEntity playerEntity) {
			isOwned = ItemUtils.isUsableByPlayer(stack, playerEntity);
			isCreative = playerEntity.isCreative();
		}

		return (original.call(stack) || rpginventory$isOfEquipmentTag(stack, this.equipmentSlot)) && isOwned && (stack.contains(RPGInventory.IGNORES_EQUIPMENT_CHANGE_RESTRICTIONS) || entity.hasStatusEffect(RPGInventory.CIVILISATION) || isCreative || (RPGInventory.SERVER_CONFIG.allow_equipment_changes.get() && !entity.hasStatusEffect(RPGInventory.WILDERNESS)));
	}

	@WrapMethod(method = "canTakeItems")
	public boolean rpginventory$canTakeItems(PlayerEntity playerEntity, Operation<Boolean> original) {
		return original.call(playerEntity) && !this.getStack().contains(RPGInventory.LOAD_OUT_ITEM) && (this.getStack().contains(RPGInventory.IGNORES_EQUIPMENT_CHANGE_RESTRICTIONS) || entity.hasStatusEffect(RPGInventory.CIVILISATION) || playerEntity.isCreative() || (RPGInventory.SERVER_CONFIG.allow_equipment_changes.get() && !entity.hasStatusEffect(RPGInventory.WILDERNESS)));
	}

	@Unique
	private boolean rpginventory$isOfEquipmentTag(ItemStack itemStack, EquipmentSlot slot) {
		return switch (slot) {
			case FEET -> itemStack.isIn(Tags.BOOTS);
			case LEGS -> itemStack.isIn(Tags.LEGGINGS);
			case CHEST -> itemStack.isIn(Tags.CHEST_PLATES);
			case HEAD -> itemStack.isIn(Tags.HELMETS);
			default -> false;
		};
	}
}
