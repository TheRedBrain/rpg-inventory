package com.github.theredbrain.rpginventory.mixin.client.gui;

import com.github.theredbrain.rpginventory.gui.SlotOverlayHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(DrawContext.class)
public class DrawContextMixin {

	@Shadow
	@Final
	private MinecraftClient client;

	@Inject(method = "drawItemInSlot(Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/item/ItemStack;IILjava/lang/String;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/util/math/MatrixStack;pop()V"))
	public void rpginventory$drawItemInSlot(TextRenderer textRenderer, ItemStack stack, int x, int y, String countOverride, CallbackInfo ci) {
		ClientPlayerEntity clientPlayerEntity = this.client.player;
		if (clientPlayerEntity != null && clientPlayerEntity.getItemCooldownManager().getCooldownProgress(stack.getItem(), this.client.getRenderTickCounter().getTickDelta(true)) <= 0) {
			SlotOverlayHelper.drawCustomSlotOverlays(((DrawContext) (Object) this), x, y, stack, clientPlayerEntity);
		}
	}
}
