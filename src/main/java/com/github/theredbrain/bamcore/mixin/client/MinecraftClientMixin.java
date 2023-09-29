package com.github.theredbrain.bamcore.mixin.client;

import com.github.theredbrain.bamcore.client.gui.screen.ingame.AdventureInventoryScreen;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.network.ClientPlayerEntity;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Environment(EnvType.CLIENT)
@Mixin(value = MinecraftClient.class)
public abstract class MinecraftClientMixin {

    @Shadow @Nullable public ClientPlayerEntity player;

    @ModifyVariable(method = "setScreen", at = @At("HEAD"))
    private Screen bam$changeInventoryScreenToAdventureInventoryScreen(Screen screen) {
        if (screen instanceof InventoryScreen) {
            screen = new AdventureInventoryScreen(this.player);
        }
        return screen;
    }
}
