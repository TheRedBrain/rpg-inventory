package com.github.theredbrain.betteradventuremode.network.packet;

import com.github.theredbrain.betteradventuremode.BetterAdventureMode;
import com.github.theredbrain.betteradventuremode.entity.player.DuckPlayerEntityMixin;
import com.github.theredbrain.betteradventuremode.screen.BackpackScreenHandler;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.screen.SimpleNamedScreenHandlerFactory;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

public class OpenBackpackScreenPacketReceiver implements ServerPlayNetworking.PlayPacketHandler<OpenBackpackScreenPacket> {

    @Override
    public void receive(OpenBackpackScreenPacket packet, ServerPlayerEntity serverPlayerEntity, PacketSender responseSender) {

        int i = ((DuckPlayerEntityMixin)serverPlayerEntity).betteradventuremode$getBackpackCapacity();
        if (i <= 0) {
            serverPlayerEntity.sendMessageToClient(Text.translatable("gui.backpack_screen.no_active_capacity"), true);
            return;
        }
        serverPlayerEntity.openHandledScreen(new SimpleNamedScreenHandlerFactory((syncId, inventory, player) -> new BackpackScreenHandler(syncId, inventory), Text.translatable("gui.backpack_screen.title")));
    }
}
