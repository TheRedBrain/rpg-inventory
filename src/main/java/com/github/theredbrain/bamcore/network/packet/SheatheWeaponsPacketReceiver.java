package com.github.theredbrain.bamcore.network.packet;

import com.github.theredbrain.bamcore.entity.player.DuckPlayerEntityMixin;
import com.github.theredbrain.bamcore.api.util.BetterAdventureModeCoreStatusEffects;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

public class SheatheWeaponsPacketReceiver implements ServerPlayNetworking.PlayChannelHandler {
    @Override
    public void receive(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {

        boolean canSheathe = buf.readBoolean();

        server.execute(() -> {

            String playerName = player.getName().getString();

            if (((DuckPlayerEntityMixin) player).bamcore$getStamina() <= 0) {
                player.sendMessageToClient(Text.translatable("hud.message.staminaTooLow"), true);
            } else {
                String command = "";
                if (player.hasStatusEffect(BetterAdventureModeCoreStatusEffects.WEAPONS_SHEATHED_EFFECT)) {
                    command = "effect clear " + playerName + " bamcore:weapons_sheathed_effect";
                } else if (canSheathe) {
                    command = "effect give " + playerName + " bamcore:weapons_sheathed_effect infinite 0 false";
                }
                if (!command.equals("")) {
                    server.getCommandManager().executeWithPrefix(server.getCommandSource(), command);
                }
            }
            // TODO play sounds, maybe when getting and losing the effect
        });
    }
}
