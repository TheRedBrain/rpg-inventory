package com.github.theredbrain.bamcore.mixin.trinkets.api;
//
//import dev.emi.trinkets.api.TrinketInventory;
//import net.minecraft.inventory.Inventory;
//import org.spongepowered.asm.mixin.Mixin;
//import org.spongepowered.asm.mixin.injection.At;
//import org.spongepowered.asm.mixin.injection.Inject;
//import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
//
//@Mixin(TrinketInventory.class)
//public class TrinketInventoryMixin {
//
//    @Inject(method = "markDirty", at = @At("TAIL"))
//    public void markDirty(CallbackInfo ci) {
//        ((TrinketInventory) (Object) this)..onContentChanged(this)
//    }
//}
