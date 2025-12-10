package com.github.theredbrain.rpginventory.entity;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;

import java.util.function.Predicate;

public class LivingEntityHelper {

	public static boolean rpginventory$hasEquipped(LivingEntity livingEntity, Predicate<ItemStack> predicate) {
		if (predicate.test(livingEntity.getEquippedStack(EquipmentSlot.MAINHAND))) {
			return true;
		}
		if (predicate.test(livingEntity.getEquippedStack(EquipmentSlot.OFFHAND))) {
			return true;
		}
		if (predicate.test(livingEntity.getEquippedStack(EquipmentSlot.FEET))) {
			return true;
		}
		if (predicate.test(livingEntity.getEquippedStack(EquipmentSlot.LEGS))) {
			return true;
		}
		if (predicate.test(livingEntity.getEquippedStack(EquipmentSlot.CHEST))) {
			return true;
		}
		if (predicate.test(livingEntity.getEquippedStack(EquipmentSlot.HEAD))) {
			return true;
		}
		if (predicate.test(livingEntity.getEquippedStack(ExtendedEquipmentSlot.SHOULDERS))) {
			return true;
		}
		if (predicate.test(livingEntity.getEquippedStack(ExtendedEquipmentSlot.GLOVES))) {
			return true;
		}
		if (predicate.test(livingEntity.getEquippedStack(ExtendedEquipmentSlot.RING_1))) {
			return true;
		}
		if (predicate.test(livingEntity.getEquippedStack(ExtendedEquipmentSlot.RING_2))) {
			return true;
		}
		if (predicate.test(livingEntity.getEquippedStack(ExtendedEquipmentSlot.NECKLACE))) {
			return true;
		}
		if (predicate.test(livingEntity.getEquippedStack(ExtendedEquipmentSlot.BELT))) {
			return true;
		}
		if (predicate.test(livingEntity.getEquippedStack(ExtendedEquipmentSlot.RELIC))) {
			return true;
		}
		if (predicate.test(livingEntity.getEquippedStack(ExtendedEquipmentSlot.CLASS_ITEM))) {
			return true;
		}
		if (predicate.test(livingEntity.getEquippedStack(ExtendedEquipmentSlot.SPELL_1))) {
			return true;
		}
		if (predicate.test(livingEntity.getEquippedStack(ExtendedEquipmentSlot.SPELL_2))) {
			return true;
		}
		if (predicate.test(livingEntity.getEquippedStack(ExtendedEquipmentSlot.SPELL_3))) {
			return true;
		}
		if (predicate.test(livingEntity.getEquippedStack(ExtendedEquipmentSlot.SPELL_4))) {
			return true;
		}
		if (predicate.test(livingEntity.getEquippedStack(ExtendedEquipmentSlot.SPELL_5))) {
			return true;
		}
		if (predicate.test(livingEntity.getEquippedStack(ExtendedEquipmentSlot.SPELL_6))) {
			return true;
		}
		if (predicate.test(livingEntity.getEquippedStack(ExtendedEquipmentSlot.SPELL_7))) {
			return true;
		}
		if (predicate.test(livingEntity.getEquippedStack(ExtendedEquipmentSlot.SPELL_8))) {
			return true;
		}
		return false;
	}

	public static int rpginventory$getAmountEquipped(LivingEntity livingEntity, Predicate<ItemStack> predicate) {
		int i = 0;
		if (predicate.test(livingEntity.getEquippedStack(EquipmentSlot.MAINHAND))) {
			i += 1;
		}
		if (predicate.test(livingEntity.getEquippedStack(EquipmentSlot.OFFHAND))) {
			i += 1;
		}
		if (predicate.test(livingEntity.getEquippedStack(EquipmentSlot.FEET))) {
			i += 1;
		}
		if (predicate.test(livingEntity.getEquippedStack(EquipmentSlot.LEGS))) {
			i += 1;
		}
		if (predicate.test(livingEntity.getEquippedStack(EquipmentSlot.CHEST))) {
			i += 1;
		}
		if (predicate.test(livingEntity.getEquippedStack(EquipmentSlot.HEAD))) {
			i += 1;
		}
		if (predicate.test(livingEntity.getEquippedStack(ExtendedEquipmentSlot.SHOULDERS))) {
			i += 1;
		}
		if (predicate.test(livingEntity.getEquippedStack(ExtendedEquipmentSlot.GLOVES))) {
			i += 1;
		}
		if (predicate.test(livingEntity.getEquippedStack(ExtendedEquipmentSlot.RING_1))) {
			i += 1;
		}
		if (predicate.test(livingEntity.getEquippedStack(ExtendedEquipmentSlot.RING_2))) {
			i += 1;
		}
		if (predicate.test(livingEntity.getEquippedStack(ExtendedEquipmentSlot.NECKLACE))) {
			i += 1;
		}
		if (predicate.test(livingEntity.getEquippedStack(ExtendedEquipmentSlot.BELT))) {
			i += 1;
		}
		if (predicate.test(livingEntity.getEquippedStack(ExtendedEquipmentSlot.RELIC))) {
			i += 1;
		}
		if (predicate.test(livingEntity.getEquippedStack(ExtendedEquipmentSlot.CLASS_ITEM))) {
			i += 1;
		}
		if (predicate.test(livingEntity.getEquippedStack(ExtendedEquipmentSlot.SPELL_1))) {
			i += 1;
		}
		if (predicate.test(livingEntity.getEquippedStack(ExtendedEquipmentSlot.SPELL_2))) {
			i += 1;
		}
		if (predicate.test(livingEntity.getEquippedStack(ExtendedEquipmentSlot.SPELL_3))) {
			i += 1;
		}
		if (predicate.test(livingEntity.getEquippedStack(ExtendedEquipmentSlot.SPELL_4))) {
			i += 1;
		}
		if (predicate.test(livingEntity.getEquippedStack(ExtendedEquipmentSlot.SPELL_5))) {
			i += 1;
		}
		if (predicate.test(livingEntity.getEquippedStack(ExtendedEquipmentSlot.SPELL_6))) {
			i += 1;
		}
		if (predicate.test(livingEntity.getEquippedStack(ExtendedEquipmentSlot.SPELL_7))) {
			i += 1;
		}
		if (predicate.test(livingEntity.getEquippedStack(ExtendedEquipmentSlot.SPELL_8))) {
			i += 1;
		}
		return i;
	}
}
