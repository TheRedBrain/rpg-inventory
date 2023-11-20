package com.github.theredbrain.bamcore.registry;

import com.github.theredbrain.bamcore.BetterAdventureModeCore;
import com.github.theredbrain.bamcore.BetterAdventureModeCoreClient;
import com.github.theredbrain.bamcore.api.item.BetterAdventureMode_BasicWeaponItem;
import com.github.theredbrain.bamcore.entity.player.DuckPlayerEntityMixin;
import com.github.theredbrain.bamcore.entity.player.DuckPlayerInventoryMixin;
import com.github.theredbrain.bamcore.network.event.PlayerDeathCallback;
import com.github.theredbrain.bamcore.network.event.PlayerFirstJoinCallback;
import com.github.theredbrain.bamcore.network.event.PlayerJoinCallback;
import com.github.theredbrain.bamcore.network.packet.BetterAdventureModeCoreServerPacket;
import com.github.theredbrain.bamcore.world.DimensionsManager;
import dev.emi.trinkets.api.TrinketComponent;
import dev.emi.trinkets.api.TrinketsApi;
import io.netty.buffer.Unpooled;
import net.bettercombat.api.client.BetterCombatClientEvents;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.c2s.play.CustomPayloadC2SPacket;

import java.util.Optional;

public class EventsRegistry {
    public static void initializeEvents() {
        PlayerFirstJoinCallback.EVENT.register((player, server) -> {
            if (BetterAdventureModeCoreClient.clientConfig.show_debug_log) {
                BetterAdventureModeCore.LOGGER.info("dungeon_dimension " + BetterAdventureModeCore.MOD_ID + ":" + player.getUuid().toString() + "_dungeons");
                BetterAdventureModeCore.LOGGER.info("housing_dimension " + BetterAdventureModeCore.MOD_ID + ":" + player.getUuid().toString() + "_housing");
            }
            ComponentsRegistry.PLAYER_SPECIFIC_DIMENSION_IDS.get(player).setPair("dungeon_dimension", BetterAdventureModeCore.MOD_ID + ":" + player.getUuid().toString() + "_dungeons", false);
            ComponentsRegistry.PLAYER_SPECIFIC_DIMENSION_IDS.get(player).setPair("housing_dimension", BetterAdventureModeCore.MOD_ID + ":" + player.getUuid().toString() + "_housing", false);
//            DimensionsManager.addDynamicPlayerDimension(player, server);
        });
        PlayerJoinCallback.EVENT.register((player, server) -> {
//            // if the player was longer than 5 minutes offline they get teleported to their spawn point
//            if (Math.abs(server.getOverworld().getTime() - ComponentsRegistry.LAST_LOGOUT_TIME.get(player).getValue()) >= 6000) {
//                server.getPlayerManager().respawnPlayer(player, true);
//            }
            if (server.getGameRules().getBoolean(GameRulesRegistry.TELEPORT_TO_SPAWN_ON_LOGIN)) {
                server.getPlayerManager().respawnPlayer(player, true);
            }
            ((DuckPlayerInventoryMixin)player.getInventory()).bamcore$setEmptyMainHand(ItemRegistry.DEFAULT_EMPTY_HAND_WEAPON.getDefaultStack());
            ((DuckPlayerInventoryMixin)player.getInventory()).bamcore$setEmptyOffHand(ItemRegistry.DEFAULT_EMPTY_HAND_WEAPON.getDefaultStack());
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

    @Environment(EnvType.CLIENT)
    public static void initializeClientEvents() {

        BetterCombatClientEvents.ATTACK_START.register((clientPlayerEntity, attackHand) -> {
            PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
            buf.writeItemStack(attackHand.itemStack());
            clientPlayerEntity.networkHandler.sendPacket(new CustomPayloadC2SPacket(BetterAdventureModeCoreServerPacket.ATTACK_STAMINA_COST_PACKET, buf));
        });
    }
}
