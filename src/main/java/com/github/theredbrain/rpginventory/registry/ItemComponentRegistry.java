package com.github.theredbrain.rpginventory.registry;

import com.github.theredbrain.rpginventory.RPGInventory;
import com.github.theredbrain.rpginventory.component.type.AdvancementLockedComponent;
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
		RPGInventory.LOAD_OUT_ITEM = Registry.register(
				Registries.DATA_COMPONENT_TYPE,
				RPGInventory.identifier("load_out_item"),
				ComponentType.<Unit>builder().codec(Unit.CODEC).packetCodec(PacketCodec.unit(Unit.INSTANCE)).build()
		);
		RPGInventory.ADVANCEMENT_LOCKED = Registry.register(
				Registries.DATA_COMPONENT_TYPE,
				RPGInventory.identifier("advancement_locked"),
				ComponentType.<AdvancementLockedComponent>builder().codec(AdvancementLockedComponent.CODEC).packetCodec(AdvancementLockedComponent.PACKET_CODEC).build()
		);
		RPGInventory.IGNORES_EQUIPMENT_CHANGE_RESTRICTIONS = Registry.register(
				Registries.DATA_COMPONENT_TYPE,
				RPGInventory.identifier("ignores_equipment_change_restrictions"),
				ComponentType.<Unit>builder().codec(Unit.CODEC).packetCodec(PacketCodec.unit(Unit.INSTANCE)).build()
		);
		RPGInventory.IS_DESTROYED_ON_DEATH = Registry.register(
				Registries.DATA_COMPONENT_TYPE,
				RPGInventory.identifier("is_destroyed_on_death"),
				ComponentType.<Unit>builder().codec(Unit.CODEC).packetCodec(PacketCodec.unit(Unit.INSTANCE)).build()
		);
		RPGInventory.IS_KEPT_ON_DEATH = Registry.register(
				Registries.DATA_COMPONENT_TYPE,
				RPGInventory.identifier("is_kept_on_death"),
				ComponentType.<Unit>builder().codec(Unit.CODEC).packetCodec(PacketCodec.unit(Unit.INSTANCE)).build()
		);
	}

	public static void init() {
	}
}
