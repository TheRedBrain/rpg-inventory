package com.github.theredbrain.betteradventuremode.network.packet;

import com.github.theredbrain.betteradventuremode.BetterAdventureModeClient;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TeleportToTeamPacketReceiver implements ServerPlayNetworking.PlayPacketHandler<TeleportToTeamPacket> {
    @Override
    public void receive(TeleportToTeamPacket packet, ServerPlayerEntity serverPlayerEntity, PacketSender responseSender) {

        Identifier targetWorldIdentifier = packet.targetWorldIdentifier;

        BlockPos targetPos = packet.targetPosition;

        double targetYaw = packet.targetYaw;
        double targetPitch = packet.targetPitch;

        MinecraftServer server = serverPlayerEntity.server;

        RegistryKey<World> dimensionregistryKey = RegistryKey.of(RegistryKeys.WORLD, targetWorldIdentifier);
        ServerWorld targetWorld = server.getWorld(dimensionregistryKey);

        if (targetWorld != null) {

            serverPlayerEntity.fallDistance = 0;
            serverPlayerEntity.teleport(targetWorld, (targetPos.getX() + 0.5), (targetPos.getY() + 0.01), (targetPos.getZ() + 0.5), (float) targetYaw, (float) targetPitch);
            if (BetterAdventureModeClient.clientConfig.show_debug_messages) {
                serverPlayerEntity.sendMessage(Text.of("Teleport to your team in world: " + targetWorld.getRegistryKey().getValue() + " at position: " + (targetPos.getX() + 0.5) + ", " + (targetPos.getY() + 0.01) + ", " + (targetPos.getZ() + 0.5) + ", with yaw: " + targetYaw + " and pitch: " + targetPitch));
            }
            ClientPlayNetworking.send(new CloseHandledScreenPacket());

        }
    }
}
