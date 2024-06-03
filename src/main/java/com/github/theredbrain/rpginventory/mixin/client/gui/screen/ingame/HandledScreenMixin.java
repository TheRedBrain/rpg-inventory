package com.github.theredbrain.rpginventory.mixin.client.gui.screen.ingame;

import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.option.KeyBinding;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(HandledScreen.class)
public class HandledScreenMixin {

    /**
     * effectively disables the vanilla swap item mechanic
     * @reason prevent item duplication
     */
    @Redirect(
            method = "onMouseClick(I)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/option/KeyBinding;matchesMouse(I)Z",
                    ordinal = 0
            )
    )
    public boolean rpginventory$redirect_matchesMouse(KeyBinding instance, int code) {
        return false;
    }

    /**
     * effectively disables the vanilla swap item mechanic
     * @reason prevent item duplication
     */
    @Redirect(
            method = "handleHotbarKeyPressed",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/option/KeyBinding;matchesKey(II)Z",
                    ordinal = 0
            )
    )
    public boolean rpginventory$redirect_matchesKey(KeyBinding instance, int keyCode, int scanCode) {
        return false;
    }
}
