package com.github.theredbrain.betteradventuremode.registry;

import com.github.theredbrain.betteradventuremode.entity.player.DuckPlayerInventoryMixin;
import com.github.theredbrain.betteradventuremode.network.event.PlayerDeathCallback;
import com.github.theredbrain.betteradventuremode.network.event.PlayerJoinCallback;
//import net.bettercombat.api.client.BetterCombatClientEvents;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;

public class EventsRegistry {
    public static void initializeEvents() {
        PlayerJoinCallback.EVENT.register((player, server) -> {
//            // if the player was longer than 5 minutes offline they get teleported to their spawn point
//            if (Math.abs(server.getOverworld().getTime() - ComponentsRegistry.LAST_LOGOUT_TIME.get(player).getValue()) >= 6000) {
//                server.getPlayerManager().respawnPlayer(player, true);
//            }
            if (server.getGameRules().getBoolean(GameRulesRegistry.TELEPORT_TO_SPAWN_ON_LOGIN)) {
                server.getPlayerManager().respawnPlayer(player, true);
            }
            ((DuckPlayerInventoryMixin)player.getInventory()).betteradventuremode$setEmptyMainHand(ItemRegistry.DEFAULT_EMPTY_HAND_WEAPON.getDefaultStack());
            ((DuckPlayerInventoryMixin)player.getInventory()).betteradventuremode$setEmptyOffHand(ItemRegistry.DEFAULT_EMPTY_HAND_WEAPON.getDefaultStack());
        });
        PlayerDeathCallback.EVENT.register((player, server, source) -> {
            if (server.getGameRules().getBoolean(GameRulesRegistry.RESET_ADVANCEMENTS_ON_DEATH)) {
                player.getAdvancementTracker().reload(server.getAdvancementLoader());
            }
            if (server.getGameRules().getBoolean(GameRulesRegistry.RESET_RECIPES_ON_DEATH)) {
                player.getRecipeBook().lockRecipes(server.getRecipeManager().values(), player);
            }
        });
        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
            sender.sendPacket(ServerPacketRegistry.SYNC_PLAYER_LOCATIONS, PlayerLocationsRegistry.getEncodedRegistry());
        });
    }

    @Environment(EnvType.CLIENT)
    public static void initializeClientEvents() {

//        // TODO BetterCombat 1.20.4
//        BetterCombatClientEvents.ATTACK_START.register((clientPlayerEntity, attackHand) -> {
//            ClientPlayNetworking.send(new AttackStaminaCostPacket(
//                    attackHand.itemStack()
//            ));
//        });
    }
}
