package com.github.theredbrain.rpginventory.mixin.entity;

import net.minecraft.world.entity.EquipmentSlot;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(EquipmentSlot.Type.class)
enum EquipmentSlotTypeMixin {
	RPG_INVENTORY_SLOT_TYPE;
}
