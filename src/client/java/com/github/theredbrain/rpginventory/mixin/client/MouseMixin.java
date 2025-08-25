package com.github.theredbrain.rpginventory.mixin.client;

import com.github.theredbrain.rpginventory.RPGInventoryClient;
import com.github.theredbrain.rpginventory.entity.player.DuckPlayerEntityMixin;
import com.github.theredbrain.rpginventory.network.packet.SheatheWeaponsPacket;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.Mouse;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Mouse.class)
public class MouseMixin {

	@Shadow
	@Final
	private MinecraftClient client;

	@Inject(method = "onMouseScroll", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerInventory;scrollInHotbar(D)V"))
	private void rpginventory$onMouseScroll(long window, double horizontal, double vertical, CallbackInfo ci) {
		if (RPGInventoryClient.CLIENT_CONFIG.hotBarOverhaul.hotbar_scrolling_tries_to_sheathe_hand_items.get() && this.client.player != null && (!((DuckPlayerEntityMixin) this.client.player).rpginventory$isHandStackSheathed() || !((DuckPlayerEntityMixin) this.client.player).rpginventory$isOffhandStackSheathed()) && !RPGInventoryClient.doesCurrentPlayerStatusPreventHandSlotAction(MinecraftClient.getInstance())) {
			ClientPlayNetworking.send(new SheatheWeaponsPacket());
		}
	}
}
