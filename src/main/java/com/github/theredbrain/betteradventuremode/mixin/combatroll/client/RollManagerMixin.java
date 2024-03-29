package com.github.theredbrain.betteradventuremode.mixin.combatroll.client;

import com.github.theredbrain.betteradventuremode.BetterAdventureMode;
import com.github.theredbrain.betteradventuremode.entity.DuckLivingEntityMixin;
import net.combatroll.client.RollManager;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(RollManager.class)
public abstract class RollManagerMixin {

    @Inject(method = "isRollAvailable(Lnet/minecraft/entity/player/PlayerEntity;)Z", at = @At("RETURN"), cancellable = true)
    public void isRollAvailable(PlayerEntity player, CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(cir.getReturnValue() && (!BetterAdventureMode.gamePlayBalanceConfig.rolling_requires_stamina || ((DuckLivingEntityMixin)player).betteradventuremode$getStamina() > 0 || player.isCreative()));
    }
}
