package com.github.theredbrain.betteradventuremode.network.packet;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.registry.Registries;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public class AddStatusEffectPacketReceiver implements ServerPlayNetworking.PlayPacketHandler<AddStatusEffectPacket> {

    @Override
    public void receive(AddStatusEffectPacket packet, ServerPlayerEntity player, PacketSender responseSender) {

        Identifier effectId = packet.effectId;
        int duration = packet.duration;
        int amplifier = packet.amplifier;
        boolean ambient = packet.ambient;
        boolean showParticles = packet.showParticles;
        boolean showIcon = packet.showIcon;
        boolean toggle = packet.toggle;

        StatusEffect statusEffect = Registries.STATUS_EFFECT.get(effectId);

        if (statusEffect != null) {
            if (toggle && player.hasStatusEffect(statusEffect)) {
                player.removeStatusEffect(statusEffect);
            } else {
                player.addStatusEffect(new StatusEffectInstance(statusEffect, duration, amplifier, ambient, showParticles, showIcon));
            }
        }
    }
}
