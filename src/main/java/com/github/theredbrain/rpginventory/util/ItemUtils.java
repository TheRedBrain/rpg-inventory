package com.github.theredbrain.rpginventory.util;

import com.github.theredbrain.rpginventory.RPGInventory;
import com.github.theredbrain.rpginventory.component.type.AdvancementLockedComponent;
import com.github.theredbrain.rpginventory.registry.Tags;
import com.mojang.authlib.GameProfile;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ResolvableProfile;

public class ItemUtils {

	/**
	 * @return whether this item should provide its attribute modifiers and if it should be rendered
	 */
	public static boolean isUsable(ItemStack stack) {
		return stack.getMaxDamage() <= 0 || stack.getDamageValue() < stack.getMaxDamage() - 1 || stack.is(Tags.EMPTY_HAND_WEAPONS) || !stack.is(Tags.UNUSABLE_WHEN_LOW_DURABILITY) || !stack.has(RPGInventory.UNUSABLE_WHEN_LOW_DURABILITY);
	}

	/**
	 * @return whether the player can equip and use this item
	 */
	public static boolean isUsableByPlayer(ItemStack stack, Player playerEntity) {
		return isOwnedByPlayer(stack, playerEntity.getGameProfile()) && isAdvancementUnlockedByPlayer(stack, playerEntity);
	}

	/**
	 * @return whether this item is owned by the player and should be usable
	 */
	public static boolean isOwnedByPlayer(ItemStack stack, GameProfile gameProfile) {
		ResolvableProfile profileComponent = stack.get(RPGInventory.PLAYER_BOUND);
		if (profileComponent != null) {
			return profileComponent.partialProfile().equals(gameProfile);
		}
		return true;
	}

	/**
	 * @return whether the player has the correct advancement unlock status to use this item
	 */
	public static boolean isAdvancementUnlockedByPlayer(ItemStack stack, Player playerEntity) {

		AdvancementLockedComponent advancementLockedComponent = stack.get(RPGInventory.ADVANCEMENT_LOCKED);
		if (advancementLockedComponent != null) {
			return advancementLockedComponent.status() == 1;
		}
		return true;
	}

}
