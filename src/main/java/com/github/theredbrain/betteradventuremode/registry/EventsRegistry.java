package com.github.theredbrain.betteradventuremode.registry;

import com.github.theredbrain.betteradventuremode.BetterAdventureMode;
import com.github.theredbrain.betteradventuremode.BetterAdventureModeClient;
import com.github.theredbrain.betteradventuremode.entity.DuckLivingEntityMixin;
import com.github.theredbrain.betteradventuremode.network.event.PlayerDeathCallback;
import com.github.theredbrain.betteradventuremode.network.event.PlayerJoinCallback;
import com.github.theredbrain.betteradventuremode.network.event.PlayerLeaveCallback;
import net.bettercombat.api.MinecraftClient_BetterCombat;
import net.combatroll.api.event.ServerSideRollEvents;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.client.option.Perspective;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

public class EventsRegistry {
    private static PacketByteBuf serverConfigSerialized = PacketByteBufs.create();
    public static void initializeEvents() {
        serverConfigSerialized = ServerPacketRegistry.ServerConfigSync.write(BetterAdventureMode.serverConfig);

        PlayerJoinCallback.EVENT.register((player, server) -> {
            int minOfflineTimeToTeleportToSpawn = server.getGameRules().getInt(GameRulesRegistry.MIN_OFFLINE_TICKS_TO_TELEPORT_TO_SPAWN);
            if (minOfflineTimeToTeleportToSpawn > -1 && Math.abs(server.getOverworld().getTime() - ComponentsRegistry.LAST_LOGOUT_TIME.get(player).getValue()) >= minOfflineTimeToTeleportToSpawn) {
                server.getPlayerManager().respawnPlayer(player, true);
                // using the teleport after the update to 1.20.2
                // until then use the respawn to avoid the shuffled status effect ids
                // because respawn removes status effects
//                ServerWorld serverWorld = server.getOverworld();
//                BlockPos spawnPoint = serverWorld.getSpawnPos();
//                player.teleport(serverWorld, spawnPoint.getX() + 0.5, spawnPoint.getY(), spawnPoint.getZ() + 0.5, serverWorld.getSpawnAngle(), 0.0F);
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
        PlayerLeaveCallback.EVENT.register((player, server) -> ComponentsRegistry.LAST_LOGOUT_TIME.get(player).setValue(server.getOverworld().getTime()));
        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
            sender.sendPacket(ServerPacketRegistry.ServerConfigSync.ID, serverConfigSerialized); // TODO convert to packet
            sender.sendPacket(ServerPacketRegistry.SYNC_CRAFTING_RECIPES, CraftingRecipeRegistry.getEncodedRegistry()); // TODO convert to packet
            sender.sendPacket(ServerPacketRegistry.SYNC_DIALOGUES, DialoguesRegistry.getEncodedRegistry()); // TODO convert to packet
            sender.sendPacket(ServerPacketRegistry.SYNC_DIALOGUE_ANSWERS, DialogueAnswersRegistry.getEncodedRegistry()); // TODO convert to packet
            sender.sendPacket(ServerPacketRegistry.SYNC_LOCATIONS, LocationsRegistry.getEncodedRegistry()); // TODO convert to packet
            sender.sendPacket(ServerPacketRegistry.SYNC_SHOPS, ShopsRegistry.getEncodedRegistry()); // TODO convert to packet
            sender.sendPacket(ServerPacketRegistry.SYNC_WEAPON_POSES, WeaponPosesRegistry.getEncodedRegistry()); // TODO convert to packet
        });
        ServerSideRollEvents.PLAYER_START_ROLLING.register((serverPlayerEntity, vec3d) -> {
            if (!serverPlayerEntity.isCreative()) {
                ((DuckLivingEntityMixin) serverPlayerEntity).betteradventuremode$addStamina(-BetterAdventureMode.serverConfig.rolling_stamina_cost);
            }
        });
    }

    @Environment(EnvType.CLIENT)
    public static void initializeClientEvents() {
//        ClientTickEvents.START_CLIENT_TICK.register(client -> {
//            boolean bl = client.options.leftKey.isPressed() || client.options.backKey.isPressed() || client.options.rightKey.isPressed();
//            boolean arePlayerYawChangesDisabledByAttacking = BetterAdventureMode.serverConfig.disable_player_yaw_changes_during_attacks && ((MinecraftClient_BetterCombat) client).isWeaponSwingInProgress();
//            if (client.options.attackKey.isPressed() && !bl &&
//                    client.player != null && client.options.getPerspective() != Perspective.FIRST_PERSON) {
//                if (!arePlayerYawChangesDisabledByAttacking) {
//                    client.player.setYaw(BetterAdventureModeClient.INSTANCE.cameraYaw);
//                }
//            }
//            if (client.options.pickItemKey.isPressed() && !bl &&
//                    client.player != null && client.options.getPerspective() != Perspective.FIRST_PERSON) {
//                if (!arePlayerYawChangesDisabledByAttacking) {
//                    client.player.setYaw(BetterAdventureModeClient.INSTANCE.cameraYaw);
//                }
//            }
//            if (client.options.useKey.isPressed() && !bl &&
//                    client.player != null && client.options.getPerspective() != Perspective.FIRST_PERSON) {
//                if (!arePlayerYawChangesDisabledByAttacking) {
//                    client.player.setYaw(BetterAdventureModeClient.INSTANCE.cameraYaw);
//                }
//            }
//        });
    }
}
