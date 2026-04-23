package com.github.theredbrain.rpginventory.mixin.client.network;

import com.github.theredbrain.rpginventory.network.DuckClientAdvancementManagerMixin;
import com.github.theredbrain.rpginventory.network.packet.UpdateAdvancementLockedItemsPacket;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.client.multiplayer.ClientAdvancements;
import net.minecraft.network.protocol.game.ClientboundUpdateAdvancementsPacket;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

@Mixin(ClientAdvancements.class)
public class ClientAdvancementManagerMixin implements DuckClientAdvancementManagerMixin {
	@Shadow
	@Final
	private Map<AdvancementHolder, AdvancementProgress> advancementProgresses;

	@Override
	public boolean rpginventory$getAdvancementProgressDone(AdvancementHolder advancementEntry) {
		AdvancementProgress advancementProgress = this.advancementProgresses.get(advancementEntry);
		return advancementProgress == null || advancementProgress.isDone();
	}

	@Inject(method = "onAdvancements", at = @At("TAIL"))
	public void rpginventory$onAdvancements(ClientboundUpdateAdvancementsPacket packet, CallbackInfo ci) {
		ClientPlayNetworking.send(new UpdateAdvancementLockedItemsPacket());
	}
}
