package com.github.theredbrain.betteradventuremode.registry;

import com.github.theredbrain.betteradventuremode.BetterAdventureMode;
import com.github.theredbrain.betteradventuremode.components.BlockPosComponent;
import com.github.theredbrain.betteradventuremode.components.LongComponent;
import com.github.theredbrain.betteradventuremode.components.PlayerLocationAccessPosComponent;
import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentInitializer;
import dev.onyxstudios.cca.api.v3.entity.RespawnCopyStrategy;

public final class ComponentsRegistry implements EntityComponentInitializer {
    public static final ComponentKey<LongComponent> LAST_LOGOUT_TIME =
            ComponentRegistry.getOrCreate(BetterAdventureMode.identifier("last_logout_time"), LongComponent.class);
    public static final ComponentKey<BlockPosComponent> CURRENT_HOUSING_BLOCK_POS =
            ComponentRegistry.getOrCreate(BetterAdventureMode.identifier("current_housing_block_pos"), BlockPosComponent.class);
    public static final ComponentKey<PlayerLocationAccessPosComponent> PLAYER_LOCATION_ACCESS_POS =
            ComponentRegistry.getOrCreate(BetterAdventureMode.identifier("player_location_access_pos"), PlayerLocationAccessPosComponent.class);

    @Override
    public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
        registry.registerForPlayers(LAST_LOGOUT_TIME, e -> new LongComponent(), RespawnCopyStrategy.ALWAYS_COPY);
        registry.registerForPlayers(CURRENT_HOUSING_BLOCK_POS, e -> new BlockPosComponent(), RespawnCopyStrategy.INVENTORY);
        registry.registerForPlayers(PLAYER_LOCATION_ACCESS_POS, e -> new PlayerLocationAccessPosComponent(), RespawnCopyStrategy.INVENTORY);
    }
}
