package com.github.theredbrain.bamcore.mixin.client.gui.screen.ingame;

import com.github.theredbrain.bamcore.BetterAdventureModeCore;
import com.github.theredbrain.bamcore.gui.screen.ingame.AdventureInventoryScreen;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.ingame.AbstractInventoryScreen;
import net.minecraft.client.gui.screen.ingame.CreativeInventoryScreen;
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
        if (BetterAdventureModeCore.serverConfig.use_adventure_inventory_screen && !this.client.interactionManager.hasCreativeInventory()) {
            this.client.setScreen(new AdventureInventoryScreen(this.client.player));
            ci.cancel();
        }
    }

    @Inject(method = "init", at = @At("HEAD"), cancellable = true)
    protected void init(CallbackInfo ci) {
        if (BetterAdventureModeCore.serverConfig.use_adventure_inventory_screen && this.client != null && this.client.player != null) {
            if (this.client.interactionManager != null && this.client.interactionManager.hasCreativeInventory()) {
                // TODO build CreativeAdventureInventoryScreen which recognizes the new positions for trinket slots
                this.client.setScreen(new CreativeInventoryScreen(this.client.player, this.client.player.networkHandler.getEnabledFeatures(), this.client.options.getOperatorItemsTab().getValue()));
            } else {
                this.client.setScreen(new AdventureInventoryScreen(this.client.player));
            }
            ci.cancel();
        }
    }
}
