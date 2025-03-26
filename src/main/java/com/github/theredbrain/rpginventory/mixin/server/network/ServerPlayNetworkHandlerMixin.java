package com.github.theredbrain.rpginventory.mixin.server.network;

import com.github.theredbrain.rpginventory.RPGInventory;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ServerPlayNetworkHandler.class)
public class ServerPlayNetworkHandlerMixin {

	/**
	 * effectively disables the vanilla swap item mechanic, when the hand slot overhaul is enabled
	 *
	 * @reason prevent item duplication
	 */
	@WrapOperation(
			method = "onPlayerAction",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/server/network/ServerPlayerEntity;isSpectator()Z",
					ordinal = 0
			)
	)
	public boolean rpginventory$wrap_isSpectator(ServerPlayerEntity instance, Operation<Boolean> original) {
		if (RPGInventory.SERVER_CONFIG.handSlotOverhaul.enable_hand_slot_overhaul.get()) {
			instance.sendMessage(Text.translatable("hud.message.disabledVanillaItemSwapMechanic"));
			return true;
		} else {
			return original.call(instance);
		}
	}
}
