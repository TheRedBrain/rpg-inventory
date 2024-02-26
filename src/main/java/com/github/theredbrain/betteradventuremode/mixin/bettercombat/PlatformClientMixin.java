package com.github.theredbrain.betteradventuremode.mixin.bettercombat;

import com.github.theredbrain.betteradventuremode.entity.DuckLivingEntityMixin;
import com.github.theredbrain.betteradventuremode.entity.player.DuckPlayerEntityMixin;
import com.github.theredbrain.betteradventuremode.item.BasicWeaponItem;
import net.bettercombat.PlatformClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlatformClient.class)
public abstract class PlatformClientMixin {

    @Inject(method = "onEmptyLeftClick", at = @At("HEAD"))
    private static void betteradventuremode$onEmptyLeftClick(PlayerEntity player, CallbackInfo ci) {
        if (player.getServer() != null) {
            PlayerEntity playerEntity = player.getServer().getPlayerManager().getPlayer(player.getUuid());
            if (playerEntity != null) {
                ItemStack mainHandStack = playerEntity.getMainHandStack();
                if (mainHandStack.getItem() instanceof BasicWeaponItem) {
                    boolean twoHanded = !((DuckPlayerEntityMixin) playerEntity).betteradventuremode$isMainHandStackSheathed() && ((DuckPlayerEntityMixin) playerEntity).betteradventuremode$isOffHandStackSheathed();
                    ((DuckLivingEntityMixin)playerEntity).betteradventuremode$addStamina(-((BasicWeaponItem)mainHandStack.getItem()).getStaminaCost(twoHanded));
                }
            }
        }
    }
}
