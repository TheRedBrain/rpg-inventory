package com.github.theredbrain.rpginventory.network.packet;

import com.github.theredbrain.rpginventory.RPGInventory;
import com.github.theredbrain.rpginventory.component.type.AdvancementLockedComponent;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.advancement.AdvancementEntry;
import net.minecraft.advancement.PlayerAdvancementTracker;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.ServerAdvancementLoader;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public class UpdateAdvancementLockedItemsPacketReceiver implements ServerPlayNetworking.PlayPayloadHandler<UpdateAdvancementLockedItemsPacket> {

	@Override
	public void receive(UpdateAdvancementLockedItemsPacket payload, ServerPlayNetworking.Context context) {

		ServerPlayerEntity serverPlayerEntity = context.player();

		PlayerInventory playerInventory = serverPlayerEntity.getInventory();

		boolean bl = false;

		for (int i = 0; i < playerInventory.size(); i++) {
			ItemStack itemStack = playerInventory.getStack(i).copy();
			AdvancementLockedComponent advancementLockedComponent = itemStack.get(RPGInventory.ADVANCEMENT_LOCKED);

			if (advancementLockedComponent != null) {
				// status 0: not unlocked, 1: unlocked, 2: locked
				int newStatus = 0;
				if (advancementLockedComponent.unlock_advancement().isEmpty() && advancementLockedComponent.lock_advancement().isEmpty()) {
					newStatus = 1;
				}
				PlayerAdvancementTracker playerAdvancementTracker = serverPlayerEntity.getAdvancementTracker();
				ServerAdvancementLoader serverAdvancementLoader = null;
				MinecraftServer minecraftServer = serverPlayerEntity.getServer();

				if (minecraftServer != null) {
					serverAdvancementLoader = minecraftServer.getAdvancementLoader();
				}

				if (playerAdvancementTracker != null && serverAdvancementLoader != null) {

					if (!advancementLockedComponent.unlock_advancement().isEmpty()) {
						AdvancementEntry unlockAdvancementEntry = serverAdvancementLoader.get(Identifier.of(advancementLockedComponent.unlock_advancement()));

						if (unlockAdvancementEntry != null && playerAdvancementTracker.getProgress(unlockAdvancementEntry).isDone()) {
							newStatus = 1;
						}
					}
					if (!advancementLockedComponent.lock_advancement().isEmpty()) {
						AdvancementEntry lockAdvancementEntry = serverAdvancementLoader.get(Identifier.of(advancementLockedComponent.lock_advancement()));

						if (lockAdvancementEntry != null && playerAdvancementTracker.getProgress(lockAdvancementEntry).isDone()) {
							newStatus = 2;
						}
					}
				}
				if (advancementLockedComponent.status() != newStatus) {
					itemStack.set(RPGInventory.ADVANCEMENT_LOCKED, new AdvancementLockedComponent(advancementLockedComponent.unlock_advancement(), advancementLockedComponent.lock_advancement(), advancementLockedComponent.not_unlocked_tooltip_text(), advancementLockedComponent.tooltip_text(), advancementLockedComponent.locked_tooltip_text(), newStatus));
					playerInventory.setStack(i, itemStack);
					bl = true;
				}
			}
		}
		if (bl) {
			playerInventory.markDirty();
			serverPlayerEntity.currentScreenHandler.sendContentUpdates();
		}
	}
}
