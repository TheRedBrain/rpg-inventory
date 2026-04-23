package com.github.theredbrain.rpginventory.mixin.client.gui.screen.ingame;

import com.github.theredbrain.rpginventory.RPGInventory;
import com.github.theredbrain.rpginventory.network.packet.UpdateAdvancementLockedItemsPacket;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.inventory.AbstractContainerMenu;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractContainerScreen.class)
public abstract class HandledScreenMixin<T extends AbstractContainerMenu> extends Screen {

	@Unique
	private int cachedInvChangeCount = 0;

	protected HandledScreenMixin(Component title) {
		super(title);
	}

	@Inject(method = "handledScreenTick", at = @At("TAIL"))
	protected void rpginventory$handledScreenTick(CallbackInfo ci) {
		if (this.minecraft != null && this.minecraft.player != null && this.cachedInvChangeCount != this.minecraft.player.getInventory().getTimesChanged()) {
			ClientPlayNetworking.send(new UpdateAdvancementLockedItemsPacket());
			this.cachedInvChangeCount = this.minecraft.player.getInventory().getTimesChanged();
		}
	}

	@Inject(method = "init", at = @At("TAIL"))
	protected void rpginventory$init(CallbackInfo ci) {
		this.cachedInvChangeCount = 0;
		ClientPlayNetworking.send(new UpdateAdvancementLockedItemsPacket());
	}

	/**
	 * effectively disables the vanilla swap item mechanic, when the hand slot overhaul is enabled
	 *
	 * @reason prevent item duplication
	 */
	@WrapOperation(
			method = "onMouseClick(I)V",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/client/option/KeyBinding;matchesMouse(I)Z",
					ordinal = 0
			)
	)
	public boolean rpginventory$wrap_matchesMouse(KeyMapping instance, int code, Operation<Boolean> original) {
		if (RPGInventory.isHandSlotOverhaulActive()) {
			return false;
		} else {
			return original.call(instance, code);
		}
	}

	/**
	 * effectively disables the vanilla swap item mechanic, when the hand slot overhaul is enabled
	 *
	 * @reason prevent item duplication
	 */
	@WrapOperation(
			method = "handleHotbarKeyPressed",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/client/option/KeyBinding;matchesKey(II)Z",
					ordinal = 0
			)
	)
	public boolean rpginventory$wrap_matchesKey(KeyMapping instance, int keyCode, int scanCode, Operation<Boolean> original) {
		if (RPGInventory.isHandSlotOverhaulActive()) {
			return false;
		} else {
			return original.call(instance, keyCode, scanCode);
		}
	}
}
