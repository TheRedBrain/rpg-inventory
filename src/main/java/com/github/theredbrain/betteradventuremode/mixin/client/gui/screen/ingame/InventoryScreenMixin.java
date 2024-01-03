package com.github.theredbrain.betteradventuremode.mixin.client.gui.screen.ingame;

import com.github.theredbrain.betteradventuremode.BetterAdventureMode;
import com.github.theredbrain.betteradventuremode.client.gui.screen.ingame.AdventureInventoryScreen;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.ingame.AbstractInventoryScreen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(value= EnvType.CLIENT)
@Mixin(InventoryScreen.class)
public abstract class InventoryScreenMixin extends AbstractInventoryScreen<PlayerScreenHandler> {

    public InventoryScreenMixin(PlayerScreenHandler screenHandler, PlayerInventory playerInventory, Text text) {
        super(screenHandler, playerInventory, text);
    }

    @Inject(method = "handledScreenTick", at = @At("HEAD"), cancellable = true)
    public void bamcore$handledScreenTick(CallbackInfo ci) {
        if (BetterAdventureMode.serverConfig.use_adventure_inventory_screen && this.client != null && this.client.player != null) {
            this.client.setScreen(new AdventureInventoryScreen(this.client.player));
            ci.cancel();
        }
    }

    @Inject(method = "init", at = @At("HEAD"), cancellable = true)
    protected void init(CallbackInfo ci) {
        if (BetterAdventureMode.serverConfig.use_adventure_inventory_screen && this.client != null && this.client.player != null) {
            this.client.setScreen(new AdventureInventoryScreen(this.client.player));
            ci.cancel();
        }
    }
}
