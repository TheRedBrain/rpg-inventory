package com.github.theredbrain.rpginventory.mixin.client;

import com.github.theredbrain.rpginventory.RPGInventoryClient;
import com.github.theredbrain.rpginventory.entity.DuckLivingEntityMixin;
import com.github.theredbrain.rpginventory.network.packet.SheatheWeaponsPacket;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.Minecraft;
import net.minecraft.client.MouseHandler;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MouseHandler.class)
public class MouseHandlerMixin {

	@Shadow
	@Final
	private Minecraft minecraft;

	@Inject(method = "onScroll", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Inventory;setSelectedSlot(I)V"))
	private void rpginventory$onMouseScroll(long handle, double xoffset, double yoffset, CallbackInfo ci) {
		if (RPGInventoryClient.CLIENT_CONFIG.hotBarOverhaul.hotbar_scrolling_tries_to_sheathe_hand_items.get() && this.minecraft.player != null && (!((DuckLivingEntityMixin) this.minecraft.player).rpginventory$isHandStackSheathed() || !((DuckLivingEntityMixin) this.minecraft.player).rpginventory$isOffhandStackSheathed()) && !RPGInventoryClient.doesCurrentPlayerStatusPreventHandSlotAction(Minecraft.getInstance())) {
			ClientPlayNetworking.send(new SheatheWeaponsPacket());
		}
	}
}
