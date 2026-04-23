package com.github.theredbrain.rpginventory.network.packet;

import com.github.theredbrain.rpginventory.RPGInventory;
import com.github.theredbrain.rpginventory.component.type.AdvancementLockedComponent;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.resources.Identifier;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.PlayerAdvancements;
import net.minecraft.server.ServerAdvancementManager;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;

public class UpdateAdvancementLockedItemsPacketReceiver implements ServerPlayNetworking.PlayPayloadHandler<UpdateAdvancementLockedItemsPacket> {

	@Override
	public void receive(UpdateAdvancementLockedItemsPacket payload, ServerPlayNetworking.Context context) {

		ServerPlayer serverPlayerEntity = context.player();

		Inventory playerInventory = serverPlayerEntity.getInventory();

		boolean bl = false;

		for (int i = 0; i < playerInventory.getContainerSize(); i++) {
			ItemStack itemStack = playerInventory.getItem(i).copy();
			AdvancementLockedComponent advancementLockedComponent = itemStack.get(RPGInventory.ADVANCEMENT_LOCKED);

			if (advancementLockedComponent != null) {
				// status 0: not unlocked, 1: unlocked, 2: locked
				int newStatus = 0;
				if (advancementLockedComponent.unlock_advancement().isEmpty() && advancementLockedComponent.lock_advancement().isEmpty()) {
					newStatus = 1;
				}
				PlayerAdvancements playerAdvancementTracker = serverPlayerEntity.getAdvancements();
				MinecraftServer minecraftServer = serverPlayerEntity.level().getServer();

				ServerAdvancementManager serverAdvancementLoader = minecraftServer.getAdvancements();

				if (!advancementLockedComponent.unlock_advancement().isEmpty()) {
					AdvancementHolder unlockAdvancementEntry = serverAdvancementLoader.get(Identifier.parse(advancementLockedComponent.unlock_advancement()));

					if (unlockAdvancementEntry != null && playerAdvancementTracker.getOrStartProgress(unlockAdvancementEntry).isDone()) {
						newStatus = 1;
					}
				}
				if (!advancementLockedComponent.lock_advancement().isEmpty()) {
					AdvancementHolder lockAdvancementEntry = serverAdvancementLoader.get(Identifier.parse(advancementLockedComponent.lock_advancement()));

					if (lockAdvancementEntry != null && playerAdvancementTracker.getOrStartProgress(lockAdvancementEntry).isDone()) {
						newStatus = 2;
					}
				}
				if (advancementLockedComponent.status() != newStatus) {
					itemStack.set(RPGInventory.ADVANCEMENT_LOCKED, new AdvancementLockedComponent(advancementLockedComponent.unlock_advancement(), advancementLockedComponent.lock_advancement(), advancementLockedComponent.not_unlocked_tooltip_text(), advancementLockedComponent.tooltip_text(), advancementLockedComponent.locked_tooltip_text(), newStatus));
					playerInventory.setItem(i, itemStack);
					bl = true;
				}
			}
		}
		if (bl) {
			playerInventory.setChanged();
			serverPlayerEntity.containerMenu.broadcastChanges();
		}
	}
}
