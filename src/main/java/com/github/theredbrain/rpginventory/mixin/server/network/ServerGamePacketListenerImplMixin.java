package com.github.theredbrain.rpginventory.mixin.server.network;

import com.github.theredbrain.rpginventory.RPGInventory;
import com.github.theredbrain.rpginventory.entity.player.DuckPlayerEntityMixin;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ServerGamePacketListenerImpl.class)
public class ServerGamePacketListenerImplMixin {

	/**
	 * effectively disables the vanilla swap item mechanic, when the hand slot overhaul is enabled
	 *
	 * @reason prevent item duplication
	 */
	@WrapOperation(
			method = "handlePlayerAction(Lnet/minecraft/network/protocol/game/ServerboundPlayerActionPacket;)V",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/server/level/ServerPlayer;isSpectator()Z",
					ordinal = 1
			)
	)
	public boolean rpginventory$wrap_isSpectator(ServerPlayer instance, Operation<Boolean> original) {
		if (((DuckPlayerEntityMixin) instance).rpginventory$isHandSlotOverhaulActive()) {
			instance.sendSystemMessage(Component.translatable("hud.message.disabledVanillaItemSwapMechanic"));
			return true;
		} else {
			return original.call(instance);
		}
	}
}
