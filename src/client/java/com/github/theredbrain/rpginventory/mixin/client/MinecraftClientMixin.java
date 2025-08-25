package com.github.theredbrain.rpginventory.mixin.client;

import com.github.theredbrain.rpginventory.entity.player.DuckPlayerEntityMixin;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {

	@Shadow
	@Nullable
	public ClientPlayerEntity player;

	@Inject(method = "doItemPick", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerEntity;getInventory()Lnet/minecraft/entity/player/PlayerInventory;"), cancellable = true)
	private void rpginventory$doItemPick(CallbackInfo ci) {
		if (this.player != null) {
			if (!((DuckPlayerEntityMixin) this.player).rpginventory$isHandStackSheathed() || !((DuckPlayerEntityMixin) this.player).rpginventory$isOffhandStackSheathed()) {
				this.player.sendMessage(Text.translatable("hud.message.pickBlockDisabledByHandItems"), true);
				ci.cancel();
			}
		}
	}
}
