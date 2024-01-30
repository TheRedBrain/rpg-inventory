package com.github.theredbrain.betteradventuremode.network.packet;

import com.github.theredbrain.betteradventuremode.entity.DuckLivingEntityMixin;
import com.github.theredbrain.betteradventuremode.registry.StatusEffectsRegistry;
import com.github.theredbrain.betteradventuremode.registry.Tags;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

public class TwoHandMainWeaponPacketReceiver implements ServerPlayNetworking.PlayPacketHandler<TwoHandMainWeaponPacket> {
    @Override
    public void receive(TwoHandMainWeaponPacket packet, ServerPlayerEntity player, PacketSender responseSender) {

        if (((DuckLivingEntityMixin) player).betteradventuremode$getStamina() <= 0) {
            player.sendMessageToClient(Text.translatable("hud.message.staminaTooLow"), true);
        } else if (player.hasStatusEffect(StatusEffectsRegistry.WEAPONS_SHEATHED_EFFECT)) {
            player.sendMessageToClient(Text.translatable("hud.message.weaponsAreSheathed"), true);
        } else if (player.getMainHandStack().isIn(Tags.NON_TWO_HANDED_ITEMS)) {
            player.sendMessageToClient(Text.translatable("hud.message.nonTwoHandedWeaponEquipped"), true);
        } else {
            if (player.hasStatusEffect(StatusEffectsRegistry.TWO_HANDED_EFFECT)) {
                player.removeStatusEffect(StatusEffectsRegistry.TWO_HANDED_EFFECT);
            } else {
                player.addStatusEffect(new StatusEffectInstance(StatusEffectsRegistry.TWO_HANDED_EFFECT, -1, 0, false, false, true));
            }
        }
        // TODO play sounds, maybe when getting and losing the effect
    }
}
