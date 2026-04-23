package com.github.theredbrain.rpginventory.mixin.client.gui.screen.ingame;

import com.github.theredbrain.rpginventory.RPGInventory;
import com.github.theredbrain.rpginventory.RPGInventoryClient;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screens.inventory.EffectRenderingInventoryScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.InventoryMenu;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(value = EnvType.CLIENT)
@Mixin(InventoryScreen.class)
public abstract class InventoryScreenMixin extends EffectRenderingInventoryScreen<InventoryMenu> {

	public InventoryScreenMixin(InventoryMenu screenHandler, Inventory playerInventory, Component text) {
		super(screenHandler, playerInventory, text);
	}

	@Inject(method = "handledScreenTick", at = @At("HEAD"), cancellable = true)
	public void rpginventory$handledScreenTick(CallbackInfo ci) {
		if (this.minecraft != null && this.minecraft.player != null && RPGInventory.SERVER_CONFIG.activate_rpg_inventory_screen.get()) {
			RPGInventoryClient.openRPGInventoryScreen(this.minecraft, this.minecraft.player);
			ci.cancel();
		}
	}

	@Inject(method = "init", at = @At("HEAD"), cancellable = true)
	protected void rpginventory$init(CallbackInfo ci) {
		if (this.minecraft != null && this.minecraft.player != null && RPGInventory.SERVER_CONFIG.activate_rpg_inventory_screen.get()) {
			RPGInventoryClient.openRPGInventoryScreen(this.minecraft, this.minecraft.player);
			ci.cancel();
		}
	}
}
