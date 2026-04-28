package com.github.theredbrain.rpginventory.mixin.server.network;

import com.github.theredbrain.rpginventory.entity.DuckLivingEntityMixin;
import com.github.theredbrain.rpginventory.entity.player.DuckPlayerEntityMixin;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.mojang.authlib.GameProfile;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ClientInformation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(value = ServerPlayer.class/*, priority = 950*/) // TODO test if priority is needed
public abstract class ServerPlayerEntityMixin extends Player implements DuckLivingEntityMixin, DuckPlayerEntityMixin {

	@Shadow
	public abstract void onEnterCombat();

	public ServerPlayerEntityMixin(final MinecraftServer server, final ServerLevel level, final GameProfile gameProfile, final ClientInformation clientInformation) {
		super(level, gameProfile);
	}

	@WrapMethod(method = "drop(Z)V")
	public void rpginventory$wrap_drop(boolean all, Operation<Boolean> original) {
		if (this.rpginventory$isHandSlotOverhaulActive() && !this.rpginventory$isHandStackSheathed()) {
			ItemStack itemStack = this.getItemBySlot(EquipmentSlot.MAINHAND).copy();
			this.setItemSlot(EquipmentSlot.MAINHAND, ItemStack.EMPTY);
			this.containerMenu.setRemoteSlot(46, itemStack);
			this.drop(itemStack, false, true);
		}
		original.call(all);
	}
}
