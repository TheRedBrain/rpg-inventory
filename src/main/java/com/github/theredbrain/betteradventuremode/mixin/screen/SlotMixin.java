package com.github.theredbrain.betteradventuremode.mixin.screen;

import com.github.theredbrain.betteradventuremode.screen.DuckSlotMixin;
import net.minecraft.screen.slot.Slot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Slot.class)
public class SlotMixin implements DuckSlotMixin {

    @Shadow @Mutable public int x;

    @Shadow @Mutable public int y;

    public void betteradventuremode$setX(int x) {
        this.x = x;
    }
    public void betteradventuremode$setY(int y) {
        this.y = y;
    }
    @Unique
    private boolean betteradventuremode$disabledOverride = false;

    @Override
    public void betteradventuremode$setDisabledOverride(boolean disabled) {
        this.betteradventuremode$disabledOverride = disabled;
    }

    @Override
    public boolean betteradventuremode$getDisabledOverride() {
        return this.betteradventuremode$disabledOverride;
    }

    @Inject(method = "isEnabled", at = @At("TAIL"), cancellable = true)
    private void betteradventuremode$isEnabled(CallbackInfoReturnable<Boolean> cir) {
        if (!this.betteradventuremode$disabledOverride) return;
        cir.setReturnValue(false);
    }
}
