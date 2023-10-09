package com.github.theredbrain.bamcore.mixin.client.gui.screen.ingame;

import com.github.theredbrain.bamcore.client.gui.screen.ingame.AdventureInventoryScreen;
import com.github.theredbrain.bamcore.registry.GameRulesRegistry;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.ingame.AbstractInventoryScreen;
import net.minecraft.client.gui.screen.ingame.CreativeInventoryScreen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.gui.widget.TexturedButtonWidget;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.AbstractRecipeScreenHandler;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
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
        if (this.client.getServer().getGameRules().getBoolean(GameRulesRegistry.USE_CUSTOM_INVENTORY_SCREEN) && !this.client.interactionManager.hasCreativeInventory()) {
            this.client.setScreen(new AdventureInventoryScreen(this.client.player));
            ci.cancel();
        }
    }

    @Inject(method = "init", at = @At("HEAD"), cancellable = true)
    protected void init(CallbackInfo ci) {
        if (this.client.getServer().getGameRules().getBoolean(GameRulesRegistry.USE_CUSTOM_INVENTORY_SCREEN) && !this.client.interactionManager.hasCreativeInventory()) {
            this.client.setScreen(new AdventureInventoryScreen(this.client.player));
            ci.cancel();
        }
    }
}
