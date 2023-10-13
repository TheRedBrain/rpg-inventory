package com.github.theredbrain.bamcore.network.packet;

import com.github.theredbrain.bamcore.api.util.BetterAdventureModeCoreStatusEffects;
import com.github.theredbrain.bamcore.entity.player.DuckPlayerEntityMixin;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

public class TwoHandMainWeaponPacketReceiver implements ServerPlayNetworking.PlayChannelHandler {
    @Override
    public void receive(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {

        server.execute(() -> {

            if (((DuckPlayerEntityMixin) player).bamcore$getStamina() <= 0) {
                player.sendMessageToClient(Text.translatable("hud.message.staminaTooLow"), true);
            } else if (player.hasStatusEffect(BetterAdventureModeCoreStatusEffects.WEAPONS_SHEATHED_EFFECT)) {
                player.sendMessageToClient(Text.translatable("hud.message.weaponsAreSheathed"), true);
            } else {
                if (player.hasStatusEffect(BetterAdventureModeCoreStatusEffects.TWO_HANDED_EFFECT)) {
                    player.removeStatusEffect(BetterAdventureModeCoreStatusEffects.TWO_HANDED_EFFECT);
                } else {
                    player.addStatusEffect(new StatusEffectInstance(BetterAdventureModeCoreStatusEffects.TWO_HANDED_EFFECT, -1, 0, false, false, true));
                }
            }
            // TODO play sounds, maybe when getting and losing the effect
        });
    }
}
