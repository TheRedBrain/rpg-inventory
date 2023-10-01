package com.github.theredbrain.bamcore.registry;

import com.github.theredbrain.bamcore.BetterAdventureModeCore;
import com.github.theredbrain.bamcore.network.event.PlayerDeathCallback;
import com.github.theredbrain.bamcore.network.event.PlayerFirstJoinCallback;
import com.github.theredbrain.bamcore.network.event.PlayerJoinCallback;
import com.github.theredbrain.bamcore.network.event.PlayerLeaveCallback;

public class EventsRegistry {
    public static void initializeEvents() {
        PlayerFirstJoinCallback.EVENT.register((player, server) -> {
//            RPGMod.LOGGER.info("dungeon_dimension " + RPGMod.MOD_ID + ":" + player.getUuid().toString() + "_dungeons");
//            RPGMod.LOGGER.info("housing_dimension " + RPGMod.MOD_ID + ":" + player.getUuid().toString() + "_housing");
            ComponentsRegistry.PLAYER_SPECIFIC_DIMENSION_IDS.get(player).setPair("dungeon_dimension", BetterAdventureModeCore.MOD_ID + ":" + player.getUuid().toString() + "_dungeons", false);
            ComponentsRegistry.PLAYER_SPECIFIC_DIMENSION_IDS.get(player).setPair("housing_dimension", BetterAdventureModeCore.MOD_ID + ":" + player.getUuid().toString() + "_housing", false);
//            DimensionsManager.addDynamicPlayerDimension(player, server);
        });
        PlayerJoinCallback.EVENT.register((player, server) -> {
            // if the player was longer than 5 minutes offline they get teleported to their spawn point
            if (Math.abs(server.getOverworld().getTime() - ComponentsRegistry.LAST_LOGOUT_TIME.get(player).getValue()) >= 6000) {
                server.getPlayerManager().respawnPlayer(player, true);
            }
        });
        PlayerLeaveCallback.EVENT.register((player, server) -> {
            ComponentsRegistry.LAST_LOGOUT_TIME.get(player).setValue(server.getOverworld().getTime());
        });
        PlayerDeathCallback.EVENT.register((player, server, source) -> {
            if (server.getGameRules().getBoolean(GameRulesRegistry.RESET_ADVANCEMENTS_ON_DEATH)) {
                player.getAdvancementTracker().reload(server.getAdvancementLoader());
            }
            if (server.getGameRules().getBoolean(GameRulesRegistry.RESET_RECIPES_ON_DEATH)) {
                player.getRecipeBook().lockRecipes(server.getRecipeManager().values(), player);
            }
        });
    }
}
