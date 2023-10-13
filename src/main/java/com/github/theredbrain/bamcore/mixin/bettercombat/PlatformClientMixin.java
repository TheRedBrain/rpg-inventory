package com.github.theredbrain.bamcore.mixin.bettercombat;
//
//import com.github.theredbrain.bamcore.api.item.BetterAdventureMode_BasicWeaponItem;
//import com.github.theredbrain.bamcore.entity.player.DuckPlayerEntityMixin;
//import net.bettercombat.PlatformClient;
//import net.minecraft.entity.player.PlayerEntity;
//import net.minecraft.item.ItemStack;
//import net.minecraft.server.network.ServerPlayerEntity;
//import org.spongepowered.asm.mixin.Mixin;
//import org.spongepowered.asm.mixin.injection.At;
//import org.spongepowered.asm.mixin.injection.Inject;
//import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
//
//@Mixin(PlatformClient.class)
//public abstract class PlatformClientMixin {
//
//    @Inject(method = "onEmptyLeftClick", at = @At("HEAD"))
//    private static void onEmptyLeftClick(PlayerEntity player, CallbackInfo ci) {
//        if (player.getServer() != null) {
//            PlayerEntity playerEntity = player.getServer().getPlayerManager().getPlayer(player.getUuid());
//            if (playerEntity != null) {
//                ItemStack mainHandStack = playerEntity.getMainHandStack();
//                if (mainHandStack.getItem() instanceof BetterAdventureMode_BasicWeaponItem) {
//                    ((DuckPlayerEntityMixin)playerEntity).bamcore$addStamina(-((BetterAdventureMode_BasicWeaponItem)mainHandStack.getItem()).getStaminaCost());
//                }
//            }
//        }
//    }
//}
