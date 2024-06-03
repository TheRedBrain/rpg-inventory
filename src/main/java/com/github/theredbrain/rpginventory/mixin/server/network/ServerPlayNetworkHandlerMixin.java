package com.github.theredbrain.rpginventory.mixin.server.network;

import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ServerPlayNetworkHandler.class)
public class ServerPlayNetworkHandlerMixin {

    /**
     * effectively disables the vanilla swap item mechanic
     * @reason prevent item duplication
    */
    @Redirect(
            method = "onPlayerAction",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/network/ServerPlayerEntity;isSpectator()Z",
                    ordinal = 0
            )
    )
    public boolean rpginventory$redirect_isSpectator(ServerPlayerEntity instance) {
        instance.sendMessage(Text.translatable("hud.message.disabledVanillaItemSwapMechanic"));
        return true;
    }
}
