package com.github.theredbrain.betteradventuremode.mixin.server;

import com.github.theredbrain.betteradventuremode.network.event.PlayerFirstJoinCallback;
import com.github.theredbrain.betteradventuremode.network.event.PlayerJoinCallback;
import net.minecraft.network.ClientConnection;
import net.minecraft.server.PlayerManager;
//import net.minecraft.server.network.ConnectedClientData;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.stat.Stats;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = PlayerManager.class)
public class PlayerManagerMixin {
    @Inject(at = @At(value = "TAIL"), method = "onPlayerConnect")
    private void betteradventuremode$onPlayerConnect(ClientConnection connection, ServerPlayerEntity player/*, ConnectedClientData clientData*/, CallbackInfo ci) {
        PlayerJoinCallback.EVENT.invoker().joinServer(player, player.getServer());
        if (player.getStatHandler().getStat(Stats.CUSTOM.getOrCreateStat(Stats.LEAVE_GAME)) < 1) {
            PlayerFirstJoinCallback.EVENT.invoker().joinServerForFirstTime(player, player.getServer());
        }
    }
}
