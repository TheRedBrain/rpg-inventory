package com.github.theredbrain.rpginventory.mixin.screen;

import com.github.theredbrain.rpginventory.RPGInventory;
import com.github.theredbrain.rpginventory.registry.Tags;
import com.github.theredbrain.rpginventory.util.ItemUtils;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.world.Container;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(targets = {"net/minecraft/world/inventory/ArmorSlot"})
public abstract class ArmorSlotMixin extends Slot {

	@Shadow
	@Final
	private LivingEntity owner;

	@Shadow
	@Final
	private EquipmentSlot slot;

	public ArmorSlotMixin(Container inventory, int index, int x, int y) {
		super(inventory, index, x, y);
	}

	@WrapMethod(method = "mayPlace")
	public boolean rpginventory$mayPlace(ItemStack itemStack, Operation<Boolean> original) {

		boolean isOwned = true;
		boolean isCreative = false;
		if (this.owner instanceof Player playerEntity) {
			isOwned = ItemUtils.isUsableByPlayer(itemStack, playerEntity);
			isCreative = playerEntity.isCreative();
		}

		return (original.call(itemStack) || rpginventory$isOfEquipmentTag(itemStack, this.slot)) && isOwned && (itemStack.has(RPGInventory.IGNORES_EQUIPMENT_CHANGE_RESTRICTIONS) || owner.hasEffect(RPGInventory.CIVILISATION) || isCreative || (RPGInventory.SERVER_CONFIG.allow_equipment_changes.get() && !owner.hasEffect(RPGInventory.WILDERNESS)));
	}

	@WrapMethod(method = "mayPickup")
	public boolean rpginventory$mayPickup(Player player, Operation<Boolean> original) {
		return original.call(player) && !this.getItem().has(RPGInventory.LOAD_OUT_ITEM) && (this.getItem().has(RPGInventory.IGNORES_EQUIPMENT_CHANGE_RESTRICTIONS) || owner.hasEffect(RPGInventory.CIVILISATION) || player.isCreative() || (RPGInventory.SERVER_CONFIG.allow_equipment_changes.get() && !owner.hasEffect(RPGInventory.WILDERNESS)));
	}

	@Unique
	private boolean rpginventory$isOfEquipmentTag(ItemStack itemStack, EquipmentSlot slot) {
		return switch (slot) {
			case FEET -> itemStack.is(Tags.BOOTS);
			case LEGS -> itemStack.is(Tags.LEGGINGS);
			case CHEST -> itemStack.is(Tags.CHEST_PLATES);
			case HEAD -> itemStack.is(Tags.HELMETS);
			default -> false;
		};
	}
}
