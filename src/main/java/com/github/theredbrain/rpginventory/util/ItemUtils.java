package com.github.theredbrain.rpginventory.util;

import com.github.theredbrain.rpginventory.RPGInventory;
import com.github.theredbrain.rpginventory.compat.PufferfishsSkillsCompat;
import com.github.theredbrain.rpginventory.component.type.SkillLockedComponent;
import com.github.theredbrain.rpginventory.registry.Tags;
import com.mojang.authlib.GameProfile;
import net.minecraft.component.type.ProfileComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;

public class ItemUtils {

	/**
	 * @return whether this item should provide its attribute modifiers and if it should be rendered
	 */
	public static boolean isUsable(ItemStack stack) {
		return stack.getMaxDamage() <= 0 || stack.getDamage() < stack.getMaxDamage() - 1 || stack.isIn(Tags.EMPTY_HAND_WEAPONS) || !stack.isIn(Tags.UNUSABLE_WHEN_LOW_DURABILITY);
	}

	/**
	 * @return whether the player can equip and use this item
	 */
	public static boolean isUsableByPlayer(ItemStack stack, PlayerEntity playerEntity) {
		return isOwnedByPlayer(stack, playerEntity.getGameProfile()) && isPlayerSkilledToUse(stack, playerEntity);
	}

	/**
	 * @return whether this item is owned by the player and should be usable
	 */
	private static boolean isOwnedByPlayer(ItemStack stack, GameProfile gameProfile) {
		ProfileComponent profileComponent = stack.get(RPGInventory.PLAYER_BOUND);
		if (profileComponent != null) {
			return profileComponent.gameProfile().equals(gameProfile);
		}
		return true;
	}

	/**
	 * @return whether the player has the correct skill unlocked to use this item
	 */
	private static boolean isPlayerSkilledToUse(ItemStack stack, PlayerEntity playerEntity) {
		SkillLockedComponent skillLockedComponent = stack.get(RPGInventory.SKILL_LOCKED);
		if (skillLockedComponent != null && playerEntity instanceof ServerPlayerEntity serverPlayerEntity) {
			if (!skillLockedComponent.category().isEmpty() && !skillLockedComponent.skill().isEmpty()) {
				if (RPGInventory.isPufferfishsSkillsLoaded) {
					return PufferfishsSkillsCompat.isSkillUnlocked(serverPlayerEntity, skillLockedComponent.category(), skillLockedComponent.skill());
				}
			}
		}
		return true;
	}
}
