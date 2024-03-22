package com.github.theredbrain.betteradventuremode.registry;

import com.github.theredbrain.betteradventuremode.BetterAdventureMode;
import com.github.theredbrain.betteradventuremode.network.event.PlayerDeathCallback;
import com.github.theredbrain.betteradventuremode.network.event.PlayerJoinCallback;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.network.PacketByteBuf;

public class EventsRegistry {
    private static PacketByteBuf serverConfigSerialized = PacketByteBufs.create();
    private static PacketByteBuf gamePlayBalanceConfigSerialized = PacketByteBufs.create();
    public static void initializeEvents() {
        serverConfigSerialized = ServerPacketRegistry.ServerConfigSync.write(BetterAdventureMode.serverConfig);
        gamePlayBalanceConfigSerialized = ServerPacketRegistry.GamePlayBalanceConfigSync.write(BetterAdventureMode.gamePlayBalanceConfig);

        PlayerJoinCallback.EVENT.register((player, server) -> {
//            // if the player was longer than 5 minutes offline they get teleported to their spawn point
//            if (Math.abs(server.getOverworld().getTime() - ComponentsRegistry.LAST_LOGOUT_TIME.get(player).getValue()) >= 6000) {
//                server.getPlayerManager().respawnPlayer(player, true);
//            }
            if (server.getGameRules().getBoolean(GameRulesRegistry.TELEPORT_TO_SPAWN_ON_LOGIN)) {
                server.getPlayerManager().respawnPlayer(player, true);
            }
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
            sender.sendPacket(ServerPacketRegistry.ServerConfigSync.ID, serverConfigSerialized); // TODO convert to packet
            sender.sendPacket(ServerPacketRegistry.GamePlayBalanceConfigSync.ID, gamePlayBalanceConfigSerialized); // TODO convert to packet
            sender.sendPacket(ServerPacketRegistry.SYNC_CRAFTING_RECIPES, CraftingRecipeRegistry.getEncodedRegistry()); // TODO convert to packet
            sender.sendPacket(ServerPacketRegistry.SYNC_DIALOGUES, DialoguesRegistry.getEncodedRegistry()); // TODO convert to packet
            sender.sendPacket(ServerPacketRegistry.SYNC_DIALOGUE_ANSWERS, DialogueAnswersRegistry.getEncodedRegistry()); // TODO convert to packet
            sender.sendPacket(ServerPacketRegistry.SYNC_LOCATIONS, LocationsRegistry.getEncodedRegistry()); // TODO convert to packet
            sender.sendPacket(ServerPacketRegistry.SYNC_SHOPS, ShopsRegistry.getEncodedRegistry()); // TODO convert to packet
            sender.sendPacket(ServerPacketRegistry.SYNC_WEAPON_POSES, WeaponPosesRegistry.getEncodedRegistry()); // TODO convert to packet
        });
    }
}
