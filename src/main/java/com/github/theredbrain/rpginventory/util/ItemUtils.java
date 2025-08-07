package com.github.theredbrain.rpginventory.util;

import com.github.theredbrain.rpginventory.RPGInventory;
import com.github.theredbrain.rpginventory.client.network.DuckClientAdvancementManagerMixin;
import com.github.theredbrain.rpginventory.component.type.AdvancementLockedComponent;
import com.github.theredbrain.rpginventory.registry.Tags;
import com.mojang.authlib.GameProfile;
import net.minecraft.advancement.AdvancementEntry;
import net.minecraft.advancement.PlayerAdvancementTracker;
import net.minecraft.client.network.ClientAdvancementManager;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.component.type.ProfileComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.ServerAdvancementLoader;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

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
		return isOwnedByPlayer(stack, playerEntity.getGameProfile()) && isAdvancementUnlockedByPlayer(stack, playerEntity);
	}

	/**
	 * @return whether this item is owned by the player and should be usable
	 */
	public static boolean isOwnedByPlayer(ItemStack stack, GameProfile gameProfile) {
		ProfileComponent profileComponent = stack.get(RPGInventory.PLAYER_BOUND);
		if (profileComponent != null) {
			return profileComponent.gameProfile().equals(gameProfile);
		}
		return true;
	}

	/**
	 * @return whether the player has the correct advancement unlock status to use this item
	 */
	public static boolean isAdvancementUnlockedByPlayer(ItemStack stack, PlayerEntity playerEntity) {

		AdvancementLockedComponent advancementLockedComponent = stack.get(RPGInventory.ADVANCEMENT_LOCKED);
		if (advancementLockedComponent != null) {
			if (!advancementLockedComponent.unlock_advancement().isEmpty() || !advancementLockedComponent.lock_advancement().isEmpty()) {
				if (playerEntity instanceof ClientPlayerEntity clientPlayerEntity) {

					ClientAdvancementManager advancementHandler = clientPlayerEntity.networkHandler.getAdvancementHandler();
					Identifier lockAdvancementIdentifier = null;
					Identifier unlockAdvancementIdentifier = null;
					if (!advancementLockedComponent.unlock_advancement().isEmpty()) {
						unlockAdvancementIdentifier = Identifier.of(advancementLockedComponent.unlock_advancement());
					}
					if (!advancementLockedComponent.lock_advancement().isEmpty()) {
						lockAdvancementIdentifier = Identifier.of(advancementLockedComponent.lock_advancement());
					}

					if (advancementHandler != null) {
						AdvancementEntry lockAdvancementEntry = null;
						if (lockAdvancementIdentifier != null) {
							lockAdvancementEntry = advancementHandler.get(lockAdvancementIdentifier);
						}
						AdvancementEntry unlockAdvancementEntry = null;
						if (unlockAdvancementIdentifier != null) {
							unlockAdvancementEntry = advancementHandler.get(unlockAdvancementIdentifier);
						}
						return (lockAdvancementIdentifier == null || (lockAdvancementEntry != null && !((DuckClientAdvancementManagerMixin) advancementHandler).scriptblocks$getAdvancementProgressDone(lockAdvancementEntry))) && (unlockAdvancementIdentifier == null || (unlockAdvancementEntry != null && ((DuckClientAdvancementManagerMixin) advancementHandler).scriptblocks$getAdvancementProgressDone(unlockAdvancementEntry)));
					}
				} else if (playerEntity instanceof ServerPlayerEntity serverPlayerEntity) {

					PlayerAdvancementTracker playerAdvancementTracker = serverPlayerEntity.getAdvancementTracker();
					ServerAdvancementLoader serverAdvancementLoader = null;

					MinecraftServer minecraftServer = serverPlayerEntity.getServer();
					if (minecraftServer != null) {
						serverAdvancementLoader = minecraftServer.getAdvancementLoader();
					}

					if (playerAdvancementTracker != null && serverAdvancementLoader != null) {
						AdvancementEntry lockAdvancementEntry = serverAdvancementLoader.get(Identifier.of(advancementLockedComponent.lock_advancement()));

						AdvancementEntry unlockAdvancementEntry = serverAdvancementLoader.get(Identifier.of(advancementLockedComponent.unlock_advancement()));

						return (lockAdvancementEntry != null && !playerAdvancementTracker.getProgress(lockAdvancementEntry).isDone()) && (unlockAdvancementEntry != null && playerAdvancementTracker.getProgress(unlockAdvancementEntry).isDone());
					}
				}
			}
		}
		return true;
	}
}
