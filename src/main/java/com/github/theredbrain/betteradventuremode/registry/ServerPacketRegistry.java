package com.github.theredbrain.betteradventuremode.registry;

import com.github.theredbrain.betteradventuremode.BetterAdventureMode;
import com.github.theredbrain.betteradventuremode.config.ServerConfig;
import com.github.theredbrain.betteradventuremode.network.packet.*;
import com.google.gson.Gson;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

public class ServerPacketRegistry {

    public static final Identifier SWAPPED_HAND_ITEMS_PACKET = BetterAdventureMode.identifier("swapped_hand_items");
    public static final Identifier SHEATHED_WEAPONS_PACKET = BetterAdventureMode.identifier("sheathed_weapons"); // TODO if weapon sheathing is not visible in multiplayer

    public static void init() {
        ServerPlayNetworking.registerGlobalReceiver(SwapHandItemsPacket.TYPE, new SwapHandItemsPacketReceiver());

        ServerPlayNetworking.registerGlobalReceiver(SheatheWeaponsPacket.TYPE, new SheatheWeaponsPacketReceiver());

        ServerPlayNetworking.registerGlobalReceiver(TwoHandMainWeaponPacket.TYPE, new TwoHandMainWeaponPacketReceiver());

    }

    public static class ServerConfigSync {
        public static Identifier ID = BetterAdventureMode.identifier("server_config_sync");

        public static PacketByteBuf write(ServerConfig serverConfig) {
            var gson = new Gson();
            var json = gson.toJson(serverConfig);
            var buffer = PacketByteBufs.create();
            buffer.writeString(json);
            return buffer;
        }

        public static ServerConfig read(PacketByteBuf buffer) {
            var gson = new Gson();
            var json = buffer.readString();
            return gson.fromJson(json, ServerConfig.class);
        }
    }
}
