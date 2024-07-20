package com.github.theredbrain.rpginventory.registry;

import com.github.theredbrain.rpginventory.RPGInventory;
import com.github.theredbrain.rpginventory.config.ServerConfig;
import com.github.theredbrain.rpginventory.network.packet.SheatheWeaponsPacket;
import com.github.theredbrain.rpginventory.network.packet.SheatheWeaponsPacketReceiver;
import com.github.theredbrain.rpginventory.network.packet.SwapHandItemsPacket;
import com.github.theredbrain.rpginventory.network.packet.SwapHandItemsPacketReceiver;
import com.github.theredbrain.rpginventory.network.packet.ToggleTwoHandedStancePacket;
import com.github.theredbrain.rpginventory.network.packet.ToggleTwoHandedStancePacketReceiver;
import com.google.gson.Gson;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

public class ServerPacketRegistry {

	public static final Identifier SWAPPED_HAND_ITEMS_PACKET = RPGInventory.identifier("swapped_hand_items");
	public static final Identifier SHEATHED_WEAPONS_PACKET = RPGInventory.identifier("sheathed_weapons"); // TODO if weapon sheathing is not visible in multiplayer

	public static void init() {
		ServerPlayNetworking.registerGlobalReceiver(SwapHandItemsPacket.TYPE, new SwapHandItemsPacketReceiver());

		ServerPlayNetworking.registerGlobalReceiver(SheatheWeaponsPacket.TYPE, new SheatheWeaponsPacketReceiver());

		ServerPlayNetworking.registerGlobalReceiver(ToggleTwoHandedStancePacket.TYPE, new ToggleTwoHandedStancePacketReceiver());

	}

	public static class ServerConfigSync {
		public static Identifier ID = RPGInventory.identifier("server_config_sync");

		public static PacketByteBuf write(ServerConfig serverConfig) {
			var gson = new Gson();
			var json = gson.toJson(serverConfig);
			var buffer = PacketByteBufs.create();
			buffer.writeString(json);
			return buffer;
		}

		public static ServerConfig read(PacketByteBuf buffer) {
			var gson = new Gson();
			var json = buffer.readString();
			return gson.fromJson(json, ServerConfig.class);
		}
	}
}
