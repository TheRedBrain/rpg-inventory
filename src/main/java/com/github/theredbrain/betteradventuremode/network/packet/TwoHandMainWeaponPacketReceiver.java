package com.github.theredbrain.betteradventuremode.network.packet;

import com.github.theredbrain.betteradventuremode.entity.DuckLivingEntityMixin;
import com.github.theredbrain.betteradventuremode.entity.player.DuckPlayerEntityMixin;
import com.github.theredbrain.betteradventuremode.registry.Tags;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

public class TwoHandMainWeaponPacketReceiver implements ServerPlayNetworking.PlayPacketHandler<TwoHandMainWeaponPacket> {
    @Override
    public void receive(TwoHandMainWeaponPacket packet, ServerPlayerEntity player, PacketSender responseSender) {

        if (((DuckLivingEntityMixin) player).betteradventuremode$getStamina() <= 0) {
            player.sendMessageToClient(Text.translatable("hud.message.staminaTooLow"), true);
        } else if (((DuckPlayerEntityMixin) player).betteradventuremode$isMainHandStackSheathed() && ((DuckPlayerEntityMixin) player).betteradventuremode$isOffHandStackSheathed()) {
            player.sendMessageToClient(Text.translatable("hud.message.weaponsAreSheathed"), true);
        } else if (((DuckPlayerEntityMixin) player).betteradventuremode$isOffHandStackSheathed()) {
            ((DuckPlayerEntityMixin) player).betteradventuremode$setIsOffHandStackSheathed(false);
        } else if (player.getMainHandStack().isIn(Tags.NON_TWO_HANDED_ITEMS)) {
            player.sendMessageToClient(Text.translatable("hud.message.nonTwoHandedWeaponEquipped"), true);
        } else {
            ((DuckPlayerEntityMixin) player).betteradventuremode$setIsOffHandStackSheathed(true);
        }
    }
}
