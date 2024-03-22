package com.github.theredbrain.betteradventuremode.mixin.client.input;

import com.github.theredbrain.betteradventuremode.client.input.DuckKeyboardInputMixin;
import net.minecraft.client.input.Input;
import net.minecraft.client.input.KeyboardInput;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(KeyboardInput.class)
public abstract class KeyboardInputMixin extends Input implements DuckKeyboardInputMixin {
    @Shadow private static float getMovementMultiplier(boolean positive, boolean negative) {
        throw new AssertionError();
    }

    @Override
    public void betterAdventureMode$updateMovement(boolean slowDown, float slowDownFactor) {
        this.movementForward = getMovementMultiplier(this.pressingForward, this.pressingBack);
        this.movementSideways = getMovementMultiplier(this.pressingLeft, this.pressingRight);
        if (slowDown) {
            this.movementSideways *= slowDownFactor;
            this.movementForward *= slowDownFactor;
        }
    }
}
