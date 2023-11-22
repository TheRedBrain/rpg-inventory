package com.github.theredbrain.bamcore.network.packet;

import com.github.theredbrain.bamcore.BetterAdventureModeCore;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;

public class AddStatusEffectPacketReceiver implements ServerPlayNetworking.PlayChannelHandler {

    @Override
    public void receive(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {

        int effectId = buf.readInt();
        int duration = buf.readInt();
        int amplifier = buf.readInt();
        boolean ambient = buf.readBoolean();
        boolean showParticles = buf.readBoolean();
        boolean showIcon = buf.readBoolean();
        boolean toggle = buf.readBoolean();

        server.execute(() -> {

            StatusEffect statusEffect = StatusEffect.byRawId(effectId);
            if (statusEffect != null) {
                if (toggle && player.hasStatusEffect(statusEffect)) {
                    player.removeStatusEffect(statusEffect);
                } else {
                    BetterAdventureModeCore.LOGGER.info("add status effect " + statusEffect.getName());
                    player.addStatusEffect(new StatusEffectInstance(statusEffect, duration, amplifier, ambient, showParticles, showIcon));
                }
            }
        });
    }
}
