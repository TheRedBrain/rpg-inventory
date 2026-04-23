package com.github.theredbrain.rpginventory.mixin.client.gui;

import com.github.theredbrain.rpginventory.gui.SlotOverlayHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiGraphics.class)
public class DrawContextMixin {

	@Shadow
	@Final
	private Minecraft client;

	@Inject(method = "drawItemInSlot(Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/item/ItemStack;IILjava/lang/String;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/util/math/MatrixStack;pop()V"))
	public void rpginventory$drawItemInSlot(Font textRenderer, ItemStack stack, int x, int y, String countOverride, CallbackInfo ci) {
		LocalPlayer clientPlayerEntity = this.client.player;
		if (clientPlayerEntity != null && clientPlayerEntity.getCooldowns().getCooldownPercent(stack.getItem(), this.client.getTimer().getGameTimeDeltaPartialTick(true)) <= 0) {
			SlotOverlayHelper.drawCustomSlotOverlays(((GuiGraphics) (Object) this), x, y, stack, clientPlayerEntity);
		}
	}
}
