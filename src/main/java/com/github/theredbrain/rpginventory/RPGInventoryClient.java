package com.github.theredbrain.rpginventory;

import com.github.theredbrain.playerattributescreen.PlayerAttributeScreenClient;
import com.github.theredbrain.rpginventory.config.ClientConfig;
import com.github.theredbrain.rpginventory.registry.ClientEventsRegistry;
import com.github.theredbrain.rpginventory.registry.ClientPacketRegistry;
import com.github.theredbrain.rpginventory.registry.KeyBindingsRegistry;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigHolder;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import me.shedaniel.autoconfig.serializer.PartitioningSerializer;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import org.apache.commons.lang3.tuple.MutablePair;

import java.util.ArrayList;
import java.util.List;


public class RPGInventoryClient implements ClientModInitializer {
	public static ConfigHolder<ClientConfig> clientConfigHolder;

	public RPGInventoryClient() {
	}

	public static List<MutablePair<Text, List<Text>>> getPlayerAttributeScreenData(MinecraftClient client) {
		List<MutablePair<Text, List<Text>>> newData = new ArrayList<>(List.of());
		if (RPGInventory.isPlayerAttributeScreenLoaded) {
			newData = PlayerAttributeScreenClient.getPlayerAttributeScreenData(client);
		}
		return newData;
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
