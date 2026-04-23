package com.github.theredbrain.rpginventory.mixin.client.gui.hud;

import com.github.theredbrain.rpginventory.RPGInventoryClient;
import com.github.theredbrain.rpginventory.gui.hud.DuckInGameHudMixin;
import com.github.theredbrain.rpginventory.gui.hud.InGameHudHelper;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Environment(EnvType.CLIENT)
@Mixin(Gui.class)
public abstract class InGameHudMixin implements DuckInGameHudMixin {

	@Shadow
	protected abstract void extractSlot(final GuiGraphicsExtractor graphics, final int x, final int y, final DeltaTracker deltaTracker, final Player player, final ItemStack itemStack, final int seed);

	@Shadow
	@Final
	private Minecraft minecraft;

	@Shadow
	@org.jspecify.annotations.Nullable
	protected abstract Player getCameraPlayer();

	@WrapOperation(
			method = "extractHotbarAndDecorations",
			at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/Gui;extractItemHotbar(Lnet/minecraft/client/gui/GuiGraphicsExtractor;Lnet/minecraft/client/DeltaTracker;)V")
	)
	private void rpginventory$wrap_renderMainHud(Gui instance, GuiGraphicsExtractor graphics, DeltaTracker deltaTracker, Operation<Void> original) {
		if (RPGInventoryClient.CLIENT_CONFIG.hotBarOverhaul.enable_hotbar_overhaul.get()) {
			InGameHudHelper.rpginventory$renderOverhauledHotbar(instance, graphics, deltaTracker);
		} else {
			original.call(instance, graphics, deltaTracker);
		}
	}

	@Nullable
	@Override
	public Player rpginventory$cameraPlayerAccessor() {
		return this.getCameraPlayer();
	}

	@Override
	public Minecraft rpginventory$clientAccessor() {
		return this.minecraft;
	}

	@Override
	public void rpginventory$extractSlot_Invoker(final GuiGraphicsExtractor graphics, final int x, final int y, final DeltaTracker deltaTracker, final Player player, final ItemStack itemStack, final int seed) {
		this.extractSlot(graphics, x, y, deltaTracker, player, itemStack, seed);
	}

	// TODO should this be removed?
	// disables rendering of the armor bar when disabled in the client config
	@WrapOperation(
			method = "extractArmor",
			at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;getArmorValue()I")
	)
	private static int rpginventory$wrap_getArmorValue(Player instance, Operation<Integer> original) {
		return RPGInventoryClient.CLIENT_CONFIG.show_armor_bar.get() ? original.call(instance) : 0;
	}
}
