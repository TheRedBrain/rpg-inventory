package com.github.theredbrain.rpginventory.util;

import com.github.theredbrain.rpginventory.entity.DuckLivingEntityMixin;
import com.github.theredbrain.rpginventory.entity.ExtendedEquipmentSlot;
import com.github.theredbrain.rpginventory.entity.player.DuckPlayerEntityMixin;
import com.google.common.collect.Multimap;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class SwapHandAttributesHelper {
	public static void swapHandAttributes(Player player, Runnable runnable) {

		synchronized (player) {
			if (!((DuckLivingEntityMixin) player).rpginventory$isHandStackSheathed() && !((DuckLivingEntityMixin) player).rpginventory$isOffhandStackSheathed()) {
				boolean isMainHandEmpty = player.getItemBySlot(EquipmentSlot.MAINHAND).isEmpty();
				boolean isOffhandEmpty = player.getItemBySlot(EquipmentSlot.OFFHAND).isEmpty();
				ItemStack mainHandStack = isMainHandEmpty ? player.getItemBySlot(ExtendedEquipmentSlot.EMPTY_HAND) : player.getItemBySlot(EquipmentSlot.MAINHAND);
				ItemStack offHandStack = isOffhandEmpty ? player.getItemBySlot(ExtendedEquipmentSlot.EMPTY_OFF_HAND) : player.getItemBySlot(EquipmentSlot.OFFHAND);

				setAttributesForOffHandAttack(player, true, mainHandStack, offHandStack);
				if (isMainHandEmpty) {
					player.setItemSlot(ExtendedEquipmentSlot.EMPTY_HAND, offHandStack);
				} else {
					player.setItemSlot(EquipmentSlot.MAINHAND, offHandStack);
				}
				if (isOffhandEmpty) {
					player.setItemSlot(ExtendedEquipmentSlot.EMPTY_OFF_HAND, offHandStack); // TODO should this be the main hand stack?
				} else {
					player.setItemSlot(EquipmentSlot.OFFHAND, offHandStack); // TODO should this be the main hand stack?
				}

				runnable.run();

				if (isMainHandEmpty) {
					player.setItemSlot(ExtendedEquipmentSlot.EMPTY_HAND, mainHandStack);
				} else {
					player.setItemSlot(EquipmentSlot.MAINHAND, mainHandStack);
				}
				if (isOffhandEmpty) {
					player.setItemSlot(ExtendedEquipmentSlot.EMPTY_OFF_HAND, offHandStack);
				} else {
					player.setItemSlot(EquipmentSlot.OFFHAND, offHandStack);
				}
				setAttributesForOffHandAttack(player, false, mainHandStack, offHandStack);
			}
		}
	}

	private static void setAttributesForOffHandAttack(Player player, boolean useOffHand, ItemStack mainHandStack, ItemStack offHandStack) {
//		ItemStack add;
//		ItemStack remove;
//		if (useOffHand) {
//			remove = mainHandStack;
//			add = offHandStack;
//		} else {
//			remove = offHandStack;
//			add = mainHandStack;
//		}
//
//		Multimap<Holder<Attribute>, AttributeModifier> modifiersMap;
//		if (remove != null) {
//			modifiersMap = AttributeModifierHelper.modifierMultimap(remove);
//			player.getAttributes().removeAttributeModifiers(modifiersMap);
//		}
//
//		if (add != null) {
//			modifiersMap = AttributeModifierHelper.modifierMultimap(add);
//			player.getAttributes().addTransientAttributeModifiers(modifiersMap);
//		}

	}

}
