package com.github.theredbrain.betteradventuremode.network.packet;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.advancement.AdvancementEntry;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

import java.util.UUID;

public class DialogueGrantAdvancementPacketReceiver implements ServerPlayNetworking.PlayPacketHandler<DialogueGrantAdvancementPacket> {

    @Override
    public void receive(DialogueGrantAdvancementPacket packet, ServerPlayerEntity player, PacketSender responseSender) {

        UUID playerUuid = packet.playerUuid;

        Identifier advancementIdentifier = packet.advancementIdentifier;

        String criterionName = packet.criterionName;

        MinecraftServer server = player.getServer();

        if (server != null) {
            AdvancementEntry advancementEntry = server.getAdvancementLoader().get(advancementIdentifier);
            ServerPlayerEntity playerEntity = server.getPlayerManager().getPlayer(playerUuid);
            if (advancementEntry != null && playerEntity != null) {
                playerEntity.getAdvancementTracker().grantCriterion(advancementEntry, criterionName);
            }
        }
    }
}
