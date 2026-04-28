package com.github.theredbrain.rpginventory.entity;

import com.github.theredbrain.rpginventory.RPGInventory;
import com.github.theredbrain.rpginventory.registry.DataAttachmentRegistry;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

public class DataAttachmentHelper {

	public static boolean isHandStackSheathed(LivingEntity livingEntity) {
		return livingEntity.getAttachedOrElse(DataAttachmentRegistry.IS_HAND_STACK_SHEATHED, !RPGInventory.isHandSlotOverhaulActive());
	}

	public static void setIsHandStackSheathed(LivingEntity livingEntity, boolean isHandStackSheathed) {
		livingEntity.setAttached(DataAttachmentRegistry.IS_HAND_STACK_SHEATHED, isHandStackSheathed);
	}

	public static boolean isOffhandStackSheathed(LivingEntity livingEntity) {
		return livingEntity.getAttachedOrElse(DataAttachmentRegistry.IS_OFFHAND_STACK_SHEATHED, !RPGInventory.isHandSlotOverhaulActive());
	}

	public static void setIsOffhandStackSheathed(LivingEntity livingEntity, boolean isOffhandStackSheathed) {
		livingEntity.setAttached(DataAttachmentRegistry.IS_OFFHAND_STACK_SHEATHED, isOffhandStackSheathed);
	}

	public static int getOldActiveSpellSlotAmount(Player player) {
		return player.getAttachedOrElse(DataAttachmentRegistry.OLD_ACTIVE_SPELL_SLOT_AMOUNT, 0);
	}

	public static void setOldActiveSpellSlotAmount(Player player, int oldActiveSpellSlotAmount) {
		player.setAttached(DataAttachmentRegistry.OLD_ACTIVE_SPELL_SLOT_AMOUNT, oldActiveSpellSlotAmount);
	}

	public static boolean shouldEjectExclusiveEquipment(Player player) {
		return player.getAttachedOrElse(DataAttachmentRegistry.SHOULD_EJECT_EXCLUSIVE_EQUIPMENT, false);
	}

	public static void setShouldEjectExclusiveEquipment(Player player, boolean shouldEjectExclusiveEquipment) {
		player.setAttached(DataAttachmentRegistry.SHOULD_EJECT_EXCLUSIVE_EQUIPMENT, shouldEjectExclusiveEquipment);
	}

	public static boolean isHandSlotOverhaulActive(Player player) {
		return player.getAttachedOrElse(DataAttachmentRegistry.IS_HAND_SLOT_OVERHAUL_ACTIVE, RPGInventory.isHandSlotOverhaulActive());
	}

	public static void setIsHandSlotOverhaulActive(Player player, boolean isHandSlotOverhaulActive) {
		player.setAttached(DataAttachmentRegistry.IS_HAND_SLOT_OVERHAUL_ACTIVE, isHandSlotOverhaulActive);
	}

	public static boolean areAlternativeHandSlotsActive(Player player) {
		return player.getAttachedOrElse(DataAttachmentRegistry.ARE_ALTERNATIVE_HAND_SLOTS_ACTIVE, RPGInventory.SERVER_CONFIG.handSlotOverhaul.enable_alternative_hand_slots.get());
	}

	public static void setAreAlternativeHandSlotsActive(Player player, boolean areAlternativeHandSlotsActive) {
		player.setAttached(DataAttachmentRegistry.ARE_ALTERNATIVE_HAND_SLOTS_ACTIVE, areAlternativeHandSlotsActive);
	}

}
