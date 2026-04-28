package com.github.theredbrain.rpginventory.mixin.entity.player;

import com.github.theredbrain.rpginventory.entity.ExtendedEquipmentSlot;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.world.entity.EntityEquipment;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.PlayerEquipment;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(PlayerEquipment.class)
public class PlayerEquipmentMixin extends EntityEquipment {

	@Shadow
	@Final
	private Player player;

	@WrapMethod(method = "set")
	public ItemStack set(EquipmentSlot slot, ItemStack itemStack, Operation<ItemStack> original) {
		return slot == ExtendedEquipmentSlot.SELECTED_HOTBAR_SLOT ? this.player.getInventory().setSelectedItem(itemStack) : super.set(slot, itemStack);
	}

	@WrapMethod(method = "get")
	public ItemStack get(EquipmentSlot slot, Operation<ItemStack> original) {
		return slot == ExtendedEquipmentSlot.SELECTED_HOTBAR_SLOT ? this.player.getInventory().getSelectedItem() : super.get(slot);
	}
}
