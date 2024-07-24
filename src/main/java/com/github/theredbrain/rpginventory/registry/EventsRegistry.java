package com.github.theredbrain.rpginventory.registry;

import com.github.theredbrain.rpginventory.RPGInventory;
import com.github.theredbrain.rpginventory.network.packet.ServerConfigSyncPacket;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;

public class EventsRegistry {
	public static void initializeEvents() {
		ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
			sender.sendPacket(new ServerConfigSyncPacket(RPGInventory.serverConfig));
		});
	}
}
