package com.github.theredbrain.rpginventory.registry;

import com.github.theredbrain.rpginventory.RPGInventory;
import net.minecraft.component.ComponentType;
import net.minecraft.component.type.ProfileComponent;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Unit;

public class ItemComponentRegistry {
	static {
		RPGInventory.BOUNDS_TO_PLAYER = Registry.register(
				Registries.DATA_COMPONENT_TYPE,
				RPGInventory.identifier("bounds_to_player"),
				ComponentType.<Unit>builder().codec(Unit.CODEC).packetCodec(PacketCodec.unit(Unit.INSTANCE)).build()
		);
		RPGInventory.PLAYER_BOUND = Registry.register(
				Registries.DATA_COMPONENT_TYPE,
				RPGInventory.identifier("player_bound"),
				ComponentType.<ProfileComponent>builder().codec(ProfileComponent.CODEC).packetCodec(ProfileComponent.PACKET_CODEC).cache().build()
		);
		RPGInventory.SAVES_CRAFTING_PLAYER = Registry.register(
				Registries.DATA_COMPONENT_TYPE,
				RPGInventory.identifier("saves_crafting_player"),
				ComponentType.<Unit>builder().codec(Unit.CODEC).packetCodec(PacketCodec.unit(Unit.INSTANCE)).build()
		);
		RPGInventory.PLAYER_CRAFTED = Registry.register(
				Registries.DATA_COMPONENT_TYPE,
				RPGInventory.identifier("player_crafted"),
				ComponentType.<ProfileComponent>builder().codec(ProfileComponent.CODEC).packetCodec(ProfileComponent.PACKET_CODEC).cache().build()
		);
	}

	public static void init() {
	}
}
