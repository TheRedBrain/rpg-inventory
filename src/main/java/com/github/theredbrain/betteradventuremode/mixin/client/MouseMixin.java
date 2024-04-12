package com.github.theredbrain.betteradventuremode.mixin.client;

import com.github.theredbrain.betteradventuremode.BetterAdventureMode;
import net.bettercombat.api.MinecraftClient_BetterCombat;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.Mouse;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(Mouse.class)
public abstract class MouseMixin {

	@Shadow @Final private MinecraftClient client;

    @Inject(method = "updateMouse", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerEntity;changeLookDirection(DD)V", ordinal = 0), cancellable = true)
    private void betterAdventureMode$updateMouse(CallbackInfo ci) {
        if (BetterAdventureMode.serverConfig.disable_player_yaw_changes_during_attacks && ((MinecraftClient_BetterCombat) this.client).isWeaponSwingInProgress()) {
            ci.cancel();
        }
    }
}