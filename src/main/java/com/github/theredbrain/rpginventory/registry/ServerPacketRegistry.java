package com.github.theredbrain.rpginventory.registry;

import com.github.theredbrain.rpginventory.network.packet.SheatheWeaponsPacket;
import com.github.theredbrain.rpginventory.network.packet.SheatheWeaponsPacketReceiver;
import com.github.theredbrain.rpginventory.network.packet.SwapHandItemsPacket;
import com.github.theredbrain.rpginventory.network.packet.SwapHandItemsPacketReceiver;
import com.github.theredbrain.rpginventory.network.packet.ToggleTwoHandedStancePacket;
import com.github.theredbrain.rpginventory.network.packet.ToggleTwoHandedStancePacketReceiver;
import com.github.theredbrain.rpginventory.network.packet.UpdateAdvancementLockedItemsPacket;
import com.github.theredbrain.rpginventory.network.packet.UpdateAdvancementLockedItemsPacketReceiver;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

public class ServerPacketRegistry {

	public static void init() {

//		PayloadTypeRegistry.clientboundPlay().register(SheathedWeaponsPacket.PACKET_ID, SheathedWeaponsPacket.PACKET_CODEC);
//
//		PayloadTypeRegistry.clientboundPlay().register(SwappedHandItemsPacket.PACKET_ID, SwappedHandItemsPacket.PACKET_CODEC);

		PayloadTypeRegistry.serverboundPlay().register(SwapHandItemsPacket.PACKET_ID, SwapHandItemsPacket.PACKET_CODEC);
		ServerPlayNetworking.registerGlobalReceiver(SwapHandItemsPacket.PACKET_ID, new SwapHandItemsPacketReceiver());

		PayloadTypeRegistry.serverboundPlay().register(SheatheWeaponsPacket.PACKET_ID, SheatheWeaponsPacket.PACKET_CODEC);
		ServerPlayNetworking.registerGlobalReceiver(SheatheWeaponsPacket.PACKET_ID, new SheatheWeaponsPacketReceiver());

		PayloadTypeRegistry.serverboundPlay().register(ToggleTwoHandedStancePacket.PACKET_ID, ToggleTwoHandedStancePacket.PACKET_CODEC);
		ServerPlayNetworking.registerGlobalReceiver(ToggleTwoHandedStancePacket.PACKET_ID, new ToggleTwoHandedStancePacketReceiver());

		PayloadTypeRegistry.serverboundPlay().register(UpdateAdvancementLockedItemsPacket.PACKET_ID, UpdateAdvancementLockedItemsPacket.PACKET_CODEC);
		ServerPlayNetworking.registerGlobalReceiver(UpdateAdvancementLockedItemsPacket.PACKET_ID, new UpdateAdvancementLockedItemsPacketReceiver());
	}
}
