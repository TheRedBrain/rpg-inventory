package com.github.theredbrain.rpginventory.registry;

import com.github.theredbrain.rpginventory.RPGInventory;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.network.PacketByteBuf;

public class EventsRegistry {
	private static PacketByteBuf serverConfigSerialized = PacketByteBufs.create();

	public static void initializeEvents() {
		serverConfigSerialized = ServerPacketRegistry.ServerConfigSync.write(RPGInventory.serverConfig);

		ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
			sender.sendPacket(ServerPacketRegistry.ServerConfigSync.ID, serverConfigSerialized); // TODO convert to packet
		});
	}

//    @Environment(EnvType.CLIENT)
//    public static void initializeClientEvents() {
////        ClientTickEvents.START_CLIENT_TICK.register(client -> {
////        });
//    }
}
