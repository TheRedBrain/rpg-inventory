package com.github.theredbrain.rpginventory.mixin.entity.player;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.world.entity.EntityEquipment;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.PlayerEquipment;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(PlayerEquipment.class)
public class PlayerEquipmentMixin extends EntityEquipment {

	@WrapMethod(method = "set")
	public ItemStack set(EquipmentSlot slot, ItemStack itemStack, Operation<ItemStack> original) {
		return super.set(slot, itemStack);
	}

	@WrapMethod(method = "get")
	public ItemStack get(EquipmentSlot slot, Operation<ItemStack> original) {
		return super.get(slot);
	}
}
