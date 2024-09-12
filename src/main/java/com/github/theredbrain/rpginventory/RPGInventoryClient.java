package com.github.theredbrain.rpginventory;

import com.github.theredbrain.rpginventory.config.ClientConfig;
import com.github.theredbrain.rpginventory.registry.ClientEventsRegistry;
import com.github.theredbrain.rpginventory.registry.ClientPacketRegistry;
import com.github.theredbrain.rpginventory.registry.KeyBindingsRegistry;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigHolder;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import me.shedaniel.autoconfig.serializer.PartitioningSerializer;
import net.fabricmc.api.ClientModInitializer;


public class RPGInventoryClient implements ClientModInitializer {
	public static ConfigHolder<ClientConfig> clientConfigHolder;

	public RPGInventoryClient() {
	}

	@Override
	public void onInitializeClient() {
		// Config
		AutoConfig.register(ClientConfig.class, PartitioningSerializer.wrap(JanksonConfigSerializer::new));
		clientConfigHolder = AutoConfig.getConfigHolder(ClientConfig.class);

		// Packets
		ClientPacketRegistry.init();

		// Registry
		ClientEventsRegistry.initializeClientEvents();
		KeyBindingsRegistry.registerKeyBindings();
	}
}
