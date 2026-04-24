package com.github.theredbrain.rpginventory.mixin.client.gui;

import com.github.theredbrain.rpginventory.gui.SlotOverlayHelper;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(GuiGraphicsExtractor.class)
public class DrawContextMixin {

	@Shadow
	@Final
	private Minecraft minecraft;

	@WrapMethod(method = "itemDecorations(Lnet/minecraft/client/gui/Font;Lnet/minecraft/world/item/ItemStack;IILjava/lang/String;)V")
	public void rpginventory$wrap_itemDecorations(Font font, ItemStack itemStack, int x, int y, String countText, Operation<Void> original) {
		LocalPlayer clientPlayerEntity = this.minecraft.player;
		if (clientPlayerEntity != null && clientPlayerEntity.getCooldowns().getCooldownPercent(itemStack, this.minecraft.getDeltaTracker().getGameTimeDeltaPartialTick(true)) <= 0) {
			SlotOverlayHelper.drawCustomSlotOverlays(((GuiGraphicsExtractor) (Object) this), x, y, itemStack, clientPlayerEntity);
		}
	}
}
