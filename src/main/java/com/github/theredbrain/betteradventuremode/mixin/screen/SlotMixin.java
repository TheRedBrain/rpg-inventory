package com.github.theredbrain.betteradventuremode.mixin.screen;

import com.github.theredbrain.betteradventuremode.screen.DuckSlotMixin;
import net.minecraft.screen.slot.Slot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Slot.class)
public class SlotMixin implements DuckSlotMixin {

    @Unique
    private boolean bamcore$disabledOverride = false;

    @Override
    public void betteradventuremode$setDisabledOverride(boolean disabled) {
        this.bamcore$disabledOverride = disabled;
    }

    @Override
    public boolean betteradventuremode$getDisabledOverride() {
        return this.bamcore$disabledOverride;
    }

    @Inject(method = "isEnabled", at = @At("TAIL"), cancellable = true)
    private void injectOverride(CallbackInfoReturnable<Boolean> cir) {
        if (!this.bamcore$disabledOverride) return;
        cir.setReturnValue(false);
    }
}
