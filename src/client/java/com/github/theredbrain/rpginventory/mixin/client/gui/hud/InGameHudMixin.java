package com.github.theredbrain.rpginventory.mixin.client.gui.hud;

import com.github.theredbrain.rpginventory.RPGInventoryClient;
import com.github.theredbrain.rpginventory.gui.hud.DuckInGameHudMixin;
import com.github.theredbrain.rpginventory.gui.hud.InGameHudHelper;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Environment(EnvType.CLIENT)
@Mixin(InGameHud.class)
public abstract class InGameHudMixin implements DuckInGameHudMixin {

	@Shadow
	protected abstract PlayerEntity getCameraPlayer();

	@Shadow
	protected abstract void renderHotbarItem(DrawContext context, int x, int y, RenderTickCounter tickCounter, PlayerEntity player, ItemStack stack, int seed);

	@Shadow
	@Final
	private MinecraftClient client;

	@WrapOperation(
			method = "renderMainHud",
			at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/InGameHud;renderHotbar(Lnet/minecraft/client/gui/DrawContext;Lnet/minecraft/client/render/RenderTickCounter;)V")
	)
	private void rpginventory$wrap_renderMainHud(InGameHud instance, DrawContext context, RenderTickCounter tickCounter, Operation<Void> original) {
		if (RPGInventoryClient.CLIENT_CONFIG.hotBarOverhaul.enable_hotbar_overhaul.get()) {
			InGameHudHelper.rpginventory$renderOverhauledHotbar(instance, context, tickCounter);
		} else {
			original.call(instance, context, tickCounter);
		}
	}

	@Nullable
	@Override
	public PlayerEntity rpginventory$cameraPlayerAccessor() {
		return this.getCameraPlayer();
	}

	@Override
	public MinecraftClient rpginventory$clientAccessor() {
		return this.client;
	}

	@Override
	public void rpginventory$renderHotbarItem_Invoker(DrawContext context, int x, int y, RenderTickCounter tickCounter, PlayerEntity player, ItemStack stack, int seed) {
		this.renderHotbarItem(context, x, y, tickCounter, player, stack, seed);
	}

	// disables rendering of the armor bar when disabled in the client config
	@WrapOperation(
			method = "renderArmor",
			at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;getArmor()I")
	)
	private static int rpginventory$wrap_getArmor(PlayerEntity instance, Operation<Integer> original) {
		return RPGInventoryClient.CLIENT_CONFIG.show_armor_bar.get() ? original.call(instance) : 0;
	}
}
