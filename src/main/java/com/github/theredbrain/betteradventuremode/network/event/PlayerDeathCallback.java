package com.github.theredbrain.betteradventuremode.network.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;

public interface PlayerDeathCallback {
    Event<PlayerDeathCallback> EVENT = EventFactory.createArrayBacked(PlayerDeathCallback.class, (listeners) -> (player, server, source) -> {
        for (PlayerDeathCallback listener : listeners) {
            listener.kill(player, server, source);
        }
    });

    void kill(ServerPlayerEntity player, MinecraftServer server, DamageSource source);
}
