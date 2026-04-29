package com.github.theredbrain.rpginventory.mixin.world.entity;

import com.github.theredbrain.rpginventory.RPGInventory;
import com.github.theredbrain.rpginventory.entity.player.DuckEntityEquipmentMixin;
import com.github.theredbrain.rpginventory.registry.Tags;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.world.entity.EntityEquipment;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@Mixin(EntityEquipment.class)
public abstract class EntityEquipmentMixin implements DuckEntityEquipmentMixin {

	@Shadow
	@Final
	private EnumMap<EquipmentSlot, ItemStack> items;

	@Shadow
	public abstract void clear();

	@WrapMethod(method = "dropAll")
	public void rpginventory$wrap_dropAll(LivingEntity dropper, Operation<Void> original) {
		EnumMap<EquipmentSlot, ItemStack> newItems = new EnumMap<>(this.items);
		newItems.clear();

		for (Map.Entry<EquipmentSlot, ItemStack> entry : this.items.entrySet()) {
			ItemStack itemStack = entry.getValue();
			if (itemStack.has(RPGInventory.IS_KEPT_ON_DEATH) || itemStack.is(Tags.EMPTY_HAND_WEAPONS)) {
				newItems.put(entry.getKey(), entry.getValue().copy());
				continue;
			}
			if (itemStack.has(RPGInventory.IS_DESTROYED_ON_DEATH) || RPGInventory.SERVER_CONFIG.destroy_dropped_items_on_death.get()) {
				continue;
			}
			if (!itemStack.isEmpty()) {
				dropper.drop(itemStack, true, false);
			}
		}
		this.clear();
		this.items.putAll(newItems);
	}

	@Override
	public void rpginventory$breakKeepInventoryItems() {
		List<EquipmentSlot> list = new ArrayList<>();
		for (Map.Entry<EquipmentSlot, ItemStack> entry : this.items.entrySet()) {
			if (entry.getValue().is(Tags.SACRIFICED_TO_KEEP_INVENTORY_ON_DEATH)) {
				list.add(entry.getKey());
			}
		}
		for (EquipmentSlot slot : list) {
			this.items.put(slot, ItemStack.EMPTY);
		}
	}
}
