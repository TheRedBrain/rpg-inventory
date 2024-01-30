package com.github.theredbrain.betteradventuremode.mixin.client.gui.hud;

import com.github.theredbrain.betteradventuremode.registry.StatusEffectsRegistry;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.InGameOverlayRenderer;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(InGameOverlayRenderer.class)
public class InGameOverlayRendererMixin {

    @Shadow
    private static void renderFireOverlay(MinecraftClient client, MatrixStack matrices) {
        throw new AssertionError();
    }

    @Inject(method = "renderOverlays", at = @At("TAIL"))
    private static void renderOverlays(MinecraftClient client, MatrixStack matrices, CallbackInfo ci) {
        if (client.player != null && !client.player.isSpectator()) {
            if (client.player.hasStatusEffect(StatusEffectsRegistry.BURNING)) {
                renderFireOverlay(client, matrices);
            }
        }
    }
}
