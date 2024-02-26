package com.github.theredbrain.betteradventuremode.network.packet;

import com.github.theredbrain.betteradventuremode.entity.DuckLivingEntityMixin;
import com.github.theredbrain.betteradventuremode.entity.player.DuckPlayerEntityMixin;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

public class SheatheWeaponsPacketReceiver implements ServerPlayNetworking.PlayPacketHandler<SheatheWeaponsPacket> {
    @Override
    public void receive(SheatheWeaponsPacket packet, ServerPlayerEntity player, PacketSender responseSender) {

        if (((DuckLivingEntityMixin) player).betteradventuremode$getStamina() <= 0) {
            player.sendMessageToClient(Text.translatable("hud.message.staminaTooLow"), true);
        } else {
            if (((DuckPlayerEntityMixin)player).betteradventuremode$isMainHandStackSheathed() && ((DuckPlayerEntityMixin)player).betteradventuremode$isOffHandStackSheathed()) {
                ((DuckPlayerEntityMixin)player).betteradventuremode$setIsMainHandStackSheathed(false);
                ((DuckPlayerEntityMixin)player).betteradventuremode$setIsOffHandStackSheathed(false);
            } else {
                ((DuckPlayerEntityMixin)player).betteradventuremode$setIsMainHandStackSheathed(true);
                ((DuckPlayerEntityMixin)player).betteradventuremode$setIsOffHandStackSheathed(true);
            }
        }
    }
}
