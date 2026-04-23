package com.github.theredbrain.rpginventory.entity;

import java.util.function.Predicate;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public class LivingEntityHelper {

	public static boolean rpginventory$hasEquipped(LivingEntity livingEntity, Predicate<ItemStack> predicate) {
		if (predicate.test(livingEntity.getItemBySlot(EquipmentSlot.MAINHAND))) {
			return true;
		}
		if (predicate.test(livingEntity.getItemBySlot(EquipmentSlot.OFFHAND))) {
			return true;
		}
		if (predicate.test(livingEntity.getItemBySlot(EquipmentSlot.FEET))) {
			return true;
		}
		if (predicate.test(livingEntity.getItemBySlot(EquipmentSlot.LEGS))) {
			return true;
		}
		if (predicate.test(livingEntity.getItemBySlot(EquipmentSlot.CHEST))) {
			return true;
		}
		if (predicate.test(livingEntity.getItemBySlot(EquipmentSlot.HEAD))) {
			return true;
		}
		if (predicate.test(livingEntity.getItemBySlot(ExtendedEquipmentSlot.SHOULDERS))) {
			return true;
		}
		if (predicate.test(livingEntity.getItemBySlot(ExtendedEquipmentSlot.GLOVES))) {
			return true;
		}
		if (predicate.test(livingEntity.getItemBySlot(ExtendedEquipmentSlot.RING_1))) {
			return true;
		}
		if (predicate.test(livingEntity.getItemBySlot(ExtendedEquipmentSlot.RING_2))) {
			return true;
		}
		if (predicate.test(livingEntity.getItemBySlot(ExtendedEquipmentSlot.NECKLACE))) {
			return true;
		}
		if (predicate.test(livingEntity.getItemBySlot(ExtendedEquipmentSlot.BELT))) {
			return true;
		}
		if (predicate.test(livingEntity.getItemBySlot(ExtendedEquipmentSlot.RELIC))) {
			return true;
		}
		if (predicate.test(livingEntity.getItemBySlot(ExtendedEquipmentSlot.CLASS_ITEM))) {
			return true;
		}
		if (predicate.test(livingEntity.getItemBySlot(ExtendedEquipmentSlot.SPELL_1))) {
			return true;
		}
		if (predicate.test(livingEntity.getItemBySlot(ExtendedEquipmentSlot.SPELL_2))) {
			return true;
		}
		if (predicate.test(livingEntity.getItemBySlot(ExtendedEquipmentSlot.SPELL_3))) {
			return true;
		}
		if (predicate.test(livingEntity.getItemBySlot(ExtendedEquipmentSlot.SPELL_4))) {
			return true;
		}
		if (predicate.test(livingEntity.getItemBySlot(ExtendedEquipmentSlot.SPELL_5))) {
			return true;
		}
		if (predicate.test(livingEntity.getItemBySlot(ExtendedEquipmentSlot.SPELL_6))) {
			return true;
		}
		if (predicate.test(livingEntity.getItemBySlot(ExtendedEquipmentSlot.SPELL_7))) {
			return true;
		}
		if (predicate.test(livingEntity.getItemBySlot(ExtendedEquipmentSlot.SPELL_8))) {
			return true;
		}
		return false;
	}

	public static int rpginventory$getAmountEquipped(LivingEntity livingEntity, Predicate<ItemStack> predicate) {
		int i = 0;
		if (predicate.test(livingEntity.getItemBySlot(EquipmentSlot.MAINHAND))) {
			i += 1;
		}
		if (predicate.test(livingEntity.getItemBySlot(EquipmentSlot.OFFHAND))) {
			i += 1;
		}
		if (predicate.test(livingEntity.getItemBySlot(EquipmentSlot.FEET))) {
			i += 1;
		}
		if (predicate.test(livingEntity.getItemBySlot(EquipmentSlot.LEGS))) {
			i += 1;
		}
		if (predicate.test(livingEntity.getItemBySlot(EquipmentSlot.CHEST))) {
			i += 1;
		}
		if (predicate.test(livingEntity.getItemBySlot(EquipmentSlot.HEAD))) {
			i += 1;
		}
		if (predicate.test(livingEntity.getItemBySlot(ExtendedEquipmentSlot.SHOULDERS))) {
			i += 1;
		}
		if (predicate.test(livingEntity.getItemBySlot(ExtendedEquipmentSlot.GLOVES))) {
			i += 1;
		}
		if (predicate.test(livingEntity.getItemBySlot(ExtendedEquipmentSlot.RING_1))) {
			i += 1;
		}
		if (predicate.test(livingEntity.getItemBySlot(ExtendedEquipmentSlot.RING_2))) {
			i += 1;
		}
		if (predicate.test(livingEntity.getItemBySlot(ExtendedEquipmentSlot.NECKLACE))) {
			i += 1;
		}
		if (predicate.test(livingEntity.getItemBySlot(ExtendedEquipmentSlot.BELT))) {
			i += 1;
		}
		if (predicate.test(livingEntity.getItemBySlot(ExtendedEquipmentSlot.RELIC))) {
			i += 1;
		}
		if (predicate.test(livingEntity.getItemBySlot(ExtendedEquipmentSlot.CLASS_ITEM))) {
			i += 1;
		}
		if (predicate.test(livingEntity.getItemBySlot(ExtendedEquipmentSlot.SPELL_1))) {
			i += 1;
		}
		if (predicate.test(livingEntity.getItemBySlot(ExtendedEquipmentSlot.SPELL_2))) {
			i += 1;
		}
		if (predicate.test(livingEntity.getItemBySlot(ExtendedEquipmentSlot.SPELL_3))) {
			i += 1;
		}
		if (predicate.test(livingEntity.getItemBySlot(ExtendedEquipmentSlot.SPELL_4))) {
			i += 1;
		}
		if (predicate.test(livingEntity.getItemBySlot(ExtendedEquipmentSlot.SPELL_5))) {
			i += 1;
		}
		if (predicate.test(livingEntity.getItemBySlot(ExtendedEquipmentSlot.SPELL_6))) {
			i += 1;
		}
		if (predicate.test(livingEntity.getItemBySlot(ExtendedEquipmentSlot.SPELL_7))) {
			i += 1;
		}
		if (predicate.test(livingEntity.getItemBySlot(ExtendedEquipmentSlot.SPELL_8))) {
			i += 1;
		}
		return i;
	}
}
