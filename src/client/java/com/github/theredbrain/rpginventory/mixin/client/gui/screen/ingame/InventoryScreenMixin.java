package com.github.theredbrain.rpginventory.mixin.client.gui.screen.ingame;

import com.github.theredbrain.rpginventory.RPGInventory;
import com.github.theredbrain.rpginventory.RPGInventoryClient;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screens.inventory.AbstractRecipeBookScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.gui.screens.recipebook.RecipeBookComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.InventoryMenu;
import org.spongepowered.asm.mixin.Mixin;

@Environment(value = EnvType.CLIENT)
@Mixin(InventoryScreen.class)
public abstract class InventoryScreenMixin extends AbstractRecipeBookScreen<InventoryMenu> {

	public InventoryScreenMixin(InventoryMenu menu, RecipeBookComponent<?> recipeBookComponent, Inventory inventory, Component title) {
		super(menu, recipeBookComponent, inventory, title);
	}

	@WrapMethod(method = "containerTick")
	public void rpginventory$containerTick(Operation<Void> original) {
		if (this.minecraft.player != null && RPGInventory.SERVER_CONFIG.activate_rpg_inventory_screen.get()) {
			RPGInventoryClient.openRPGInventoryScreen(this.minecraft, this.minecraft.player);
		}
		original.call();
	}

	@WrapMethod(method = "init")
	protected void rpginventory$init(Operation<Void> original) {
		if (this.minecraft.player != null && RPGInventory.SERVER_CONFIG.activate_rpg_inventory_screen.get()) {
			RPGInventoryClient.openRPGInventoryScreen(this.minecraft, this.minecraft.player);
		}
		original.call();
	}
}
