package com.github.theredbrain.rpginventory.registry;

import com.github.theredbrain.rpginventory.RPGInventory;
import com.github.theredbrain.rpginventory.component.type.AdvancementLockedComponent;
import com.github.theredbrain.rpginventory.component.type.ExclusiveEquipmentComponent;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.Unit;
import net.minecraft.world.item.component.ResolvableProfile;

public class ItemComponentRegistry {
	static {
		RPGInventory.BOUNDS_TO_PLAYER = Registry.register(
				BuiltInRegistries.DATA_COMPONENT_TYPE,
				RPGInventory.identifier("bounds_to_player"),
				DataComponentType.<Unit>builder().persistent(Unit.CODEC).networkSynchronized(StreamCodec.unit(Unit.INSTANCE)).build()
		);
		RPGInventory.PLAYER_BOUND = Registry.register(
				BuiltInRegistries.DATA_COMPONENT_TYPE,
				RPGInventory.identifier("player_bound"),
				DataComponentType.<ResolvableProfile>builder().persistent(ResolvableProfile.CODEC).networkSynchronized(ResolvableProfile.STREAM_CODEC).cacheEncoding().build()
		);
		RPGInventory.SAVES_CRAFTING_PLAYER = Registry.register(
				BuiltInRegistries.DATA_COMPONENT_TYPE,
				RPGInventory.identifier("saves_crafting_player"),
				DataComponentType.<Unit>builder().persistent(Unit.CODEC).networkSynchronized(StreamCodec.unit(Unit.INSTANCE)).build()
		);
		RPGInventory.PLAYER_CRAFTED = Registry.register(
				BuiltInRegistries.DATA_COMPONENT_TYPE,
				RPGInventory.identifier("player_crafted"),
				DataComponentType.<ResolvableProfile>builder().persistent(ResolvableProfile.CODEC).networkSynchronized(ResolvableProfile.STREAM_CODEC).cacheEncoding().build()
		);
		RPGInventory.LOAD_OUT_ITEM = Registry.register(
				BuiltInRegistries.DATA_COMPONENT_TYPE,
				RPGInventory.identifier("load_out_item"),
				DataComponentType.<Unit>builder().persistent(Unit.CODEC).networkSynchronized(StreamCodec.unit(Unit.INSTANCE)).build()
		);
		RPGInventory.ADVANCEMENT_LOCKED = Registry.register(
				BuiltInRegistries.DATA_COMPONENT_TYPE,
				RPGInventory.identifier("advancement_locked"),
				DataComponentType.<AdvancementLockedComponent>builder().persistent(AdvancementLockedComponent.CODEC).networkSynchronized(AdvancementLockedComponent.PACKET_CODEC).build()
		);
		RPGInventory.IGNORES_EQUIPMENT_CHANGE_RESTRICTIONS = Registry.register(
				BuiltInRegistries.DATA_COMPONENT_TYPE,
				RPGInventory.identifier("ignores_equipment_change_restrictions"),
				DataComponentType.<Unit>builder().persistent(Unit.CODEC).networkSynchronized(StreamCodec.unit(Unit.INSTANCE)).build()
		);
		RPGInventory.IS_DESTROYED_ON_DEATH = Registry.register(
				BuiltInRegistries.DATA_COMPONENT_TYPE,
				RPGInventory.identifier("is_destroyed_on_death"),
				DataComponentType.<Unit>builder().persistent(Unit.CODEC).networkSynchronized(StreamCodec.unit(Unit.INSTANCE)).build()
		);
		RPGInventory.IS_KEPT_ON_DEATH = Registry.register(
				BuiltInRegistries.DATA_COMPONENT_TYPE,
				RPGInventory.identifier("is_kept_on_death"),
				DataComponentType.<Unit>builder().persistent(Unit.CODEC).networkSynchronized(StreamCodec.unit(Unit.INSTANCE)).build()
		);
		RPGInventory.UNUSABLE_WHEN_LOW_DURABILITY = Registry.register(
				BuiltInRegistries.DATA_COMPONENT_TYPE,
				RPGInventory.identifier("unusable_when_low_durability"),
				DataComponentType.<Unit>builder().persistent(Unit.CODEC).networkSynchronized(StreamCodec.unit(Unit.INSTANCE)).build()
		);
		RPGInventory.EXCLUSIVE_EQUIPMENT = Registry.register(
				BuiltInRegistries.DATA_COMPONENT_TYPE,
				RPGInventory.identifier("exclusive_equipment"),
				DataComponentType.<ExclusiveEquipmentComponent>builder().persistent(ExclusiveEquipmentComponent.CODEC).networkSynchronized(ExclusiveEquipmentComponent.PACKET_CODEC).build()
		);
	}

	public static void init() {
	}
}
