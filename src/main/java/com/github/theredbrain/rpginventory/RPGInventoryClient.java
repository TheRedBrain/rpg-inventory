package com.github.theredbrain.rpginventory;

import com.github.theredbrain.rpginventory.config.ClientConfig;
import com.github.theredbrain.rpginventory.config.ClientConfigWrapper;
import com.github.theredbrain.rpginventory.registry.ClientEventsRegistry;
import com.github.theredbrain.rpginventory.registry.ClientPacketRegistry;
import com.github.theredbrain.rpginventory.registry.KeyBindingsRegistry;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import me.shedaniel.autoconfig.serializer.PartitioningSerializer;
import net.fabricmc.api.ClientModInitializer;


public class RPGInventoryClient implements ClientModInitializer {

	public static ClientConfig clientConfig;

	public RPGInventoryClient() {
	}

	@Override
	public void onInitializeClient() {
		// Config
		AutoConfig.register(ClientConfigWrapper.class, PartitioningSerializer.wrap(JanksonConfigSerializer::new));
		clientConfig = ((ClientConfigWrapper) AutoConfig.getConfigHolder(ClientConfigWrapper.class).getConfig()).client;

		// Packets
		ClientPacketRegistry.init();

		// Registry
		ClientEventsRegistry.initializeClientEvents();
		KeyBindingsRegistry.registerKeyBindings();
	}
}
