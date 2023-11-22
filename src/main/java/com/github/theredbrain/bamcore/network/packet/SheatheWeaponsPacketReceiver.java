package com.github.theredbrain.bamcore.network.packet;

import com.github.theredbrain.bamcore.entity.player.DuckPlayerEntityMixin;
import com.github.theredbrain.bamcore.registry.StatusEffectsRegistry;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

public class SheatheWeaponsPacketReceiver implements ServerPlayNetworking.PlayPacketHandler<SheatheWeaponsPacket> {
    @Override
    public void receive(SheatheWeaponsPacket packet, ServerPlayerEntity player, PacketSender responseSender) {

        if (((DuckPlayerEntityMixin) player).bamcore$getStamina() <= 0) {
            player.sendMessageToClient(Text.translatable("hud.message.staminaTooLow"), true);
        } else {
            if (player.hasStatusEffect(StatusEffectsRegistry.WEAPONS_SHEATHED_EFFECT)) {
                player.removeStatusEffect(StatusEffectsRegistry.WEAPONS_SHEATHED_EFFECT);
            } else {
                player.addStatusEffect(new StatusEffectInstance(StatusEffectsRegistry.WEAPONS_SHEATHED_EFFECT, -1, 0, false, false, true));
                if (player.hasStatusEffect(StatusEffectsRegistry.TWO_HANDED_EFFECT)) {
                    player.removeStatusEffect(StatusEffectsRegistry.TWO_HANDED_EFFECT);
                }
            }
        }
        // TODO play sounds, maybe when getting and losing the effect
    }
}
