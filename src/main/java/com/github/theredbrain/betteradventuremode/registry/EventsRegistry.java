package com.github.theredbrain.betteradventuremode.registry;

import com.github.theredbrain.betteradventuremode.BetterAdventureMode;
import com.github.theredbrain.betteradventuremode.BetterAdventureModeClient;
import com.github.theredbrain.betteradventuremode.entity.DuckLivingEntityMixin;
import com.github.theredbrain.betteradventuremode.network.event.PlayerDeathCallback;
import com.github.theredbrain.betteradventuremode.network.event.PlayerJoinCallback;
import net.bettercombat.api.MinecraftClient_BetterCombat;
import net.combatroll.api.event.ServerSideRollEvents;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.client.option.Perspective;
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
        ServerSideRollEvents.PLAYER_START_ROLLING.register((serverPlayerEntity, vec3d) -> {
            if (!serverPlayerEntity.isCreative()) {
                ((DuckLivingEntityMixin) serverPlayerEntity).betteradventuremode$addStamina(-BetterAdventureMode.gamePlayBalanceConfig.rolling_stamina_cost);
            }
        });
    }

    @Environment(EnvType.CLIENT)
    public static void initializeClientEvents() {
        ClientTickEvents.START_CLIENT_TICK.register(client -> {
            boolean bl = client.options.leftKey.isPressed() || client.options.backKey.isPressed() || client.options.rightKey.isPressed();
            boolean arePlayerYawChangesDisabledByAttacking = BetterAdventureMode.serverConfig.disable_player_yaw_changes_during_attacks && ((MinecraftClient_BetterCombat) client).isWeaponSwingInProgress();
            if (client.options.attackKey.isPressed() && !bl &&
                    client.player != null && BetterAdventureModeClient.clientConfig.enable_360_degree_third_person &&
                    BetterAdventureMode.serverConfig.allow_360_degree_third_person &&
                    client.options.getPerspective() != Perspective.FIRST_PERSON &&
                    BetterAdventureModeClient.clientConfig.attacking_towards_camera_direction) {
                if (!BetterAdventureMode.serverConfig.disable_player_pitch_changes) {
                    client.player.setPitch(BetterAdventureModeClient.INSTANCE.cameraPitch);
                }
                if (!arePlayerYawChangesDisabledByAttacking) {
                    client.player.setYaw(BetterAdventureModeClient.INSTANCE.cameraYaw);
                }
            }
            if (client.options.pickItemKey.isPressed() && !bl &&
                    client.player != null && BetterAdventureModeClient.clientConfig.enable_360_degree_third_person &&
                    BetterAdventureMode.serverConfig.allow_360_degree_third_person &&
                    client.options.getPerspective() != Perspective.FIRST_PERSON &&
                    BetterAdventureModeClient.clientConfig.pick_block_towards_camera_direction) {
                if (!BetterAdventureMode.serverConfig.disable_player_pitch_changes) {
                    client.player.setPitch(BetterAdventureModeClient.INSTANCE.cameraPitch);
                }
                if (!arePlayerYawChangesDisabledByAttacking) {
                    client.player.setYaw(BetterAdventureModeClient.INSTANCE.cameraYaw);
                }
            }
            if (client.options.useKey.isPressed() && !bl &&
                    client.player != null && BetterAdventureModeClient.clientConfig.enable_360_degree_third_person &&
                    BetterAdventureMode.serverConfig.allow_360_degree_third_person &&
                    client.options.getPerspective() != Perspective.FIRST_PERSON &&
                    BetterAdventureModeClient.clientConfig.using_items_towards_camera_direction) {
                if (!BetterAdventureMode.serverConfig.disable_player_pitch_changes) {
                    client.player.setPitch(BetterAdventureModeClient.INSTANCE.cameraPitch);
                }
                if (!arePlayerYawChangesDisabledByAttacking) {
                    client.player.setYaw(BetterAdventureModeClient.INSTANCE.cameraYaw);
                }
            }
        });
    }
}
