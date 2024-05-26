package com.github.theredbrain.betteradventuremode;

import com.github.theredbrain.betteradventuremode.registry.ClientPacketRegistry;
import com.github.theredbrain.betteradventuremode.config.ClientConfig;
import com.github.theredbrain.betteradventuremode.config.ClientConfigWrapper;
import com.github.theredbrain.betteradventuremode.registry.*;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import me.shedaniel.autoconfig.serializer.PartitioningSerializer;
import net.fabricmc.api.ClientModInitializer;


public class BetterAdventureModeClient implements ClientModInitializer {

    public static ClientConfig clientConfig;

    public BetterAdventureModeClient(){
    }

    @Override
    public void onInitializeClient() {
        // Config
        AutoConfig.register(ClientConfigWrapper.class, PartitioningSerializer.wrap(JanksonConfigSerializer::new));
        clientConfig = ((ClientConfigWrapper)AutoConfig.getConfigHolder(ClientConfigWrapper.class).getConfig()).client;

        // Packets
        ClientPacketRegistry.init();

        // Registry
        KeyBindingsRegistry.registerKeyBindings();
    }
}
