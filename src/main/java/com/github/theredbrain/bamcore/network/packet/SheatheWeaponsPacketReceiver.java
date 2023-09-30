package com.github.theredbrain.bamcore.network.packet;

import com.github.theredbrain.bamcore.registry.StatusEffectsRegistry;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;

public class SheatheWeaponsPacketReceiver implements ServerPlayNetworking.PlayChannelHandler {
    @Override
    public void receive(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {

        boolean canSheathe = buf.readBoolean();

        server.execute(() -> {

        String playerName = player.getName().getString();
            // TODO check if player has enough stamina to swap weapons
            if (true) {
                String command = "";
                if (player.hasStatusEffect(StatusEffectsRegistry.WEAPONS_SHEATHED_EFFECT)) {
                    command = "effect clear " + playerName + " bamcore:weapons_sheathed_effect";
                } else if (canSheathe) {
                    command = "effect give " + playerName + " bamcore:weapons_sheathed_effect infinite 0 true";
                }
                if (!command.equals("")) {
                    server.getCommandManager().executeWithPrefix(server.getCommandSource(), command);
                }
                // TODO player loses stamina
            } else {
                // TODO send message about not enough stamina
            }
        // TODO play sounds, maybe when getting and losing the effect
        });
    }
}
