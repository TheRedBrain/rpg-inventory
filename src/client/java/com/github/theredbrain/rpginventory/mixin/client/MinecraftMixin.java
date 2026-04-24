package com.github.theredbrain.rpginventory.mixin.client;

import com.github.theredbrain.rpginventory.entity.DuckLivingEntityMixin;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Minecraft.class)
public class MinecraftMixin {

	@Shadow
	@Nullable
	public LocalPlayer player;

	@WrapMethod(method = "pickBlockOrEntity")
	private void rpginventory$wrap_pickBlockOrEntity(Operation<Void> original) {
		if (this.player != null) {
			if (!((DuckLivingEntityMixin) this.player).rpginventory$isHandStackSheathed() || !((DuckLivingEntityMixin) this.player).rpginventory$isOffhandStackSheathed()) {
				this.player.sendOverlayMessage(Component.translatable("hud.message.pickBlockDisabledByHandItems"));
				return;
			}
		}
		original.call();
	}
}
