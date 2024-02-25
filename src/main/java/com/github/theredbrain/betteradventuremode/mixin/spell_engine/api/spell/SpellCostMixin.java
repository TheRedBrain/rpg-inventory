package com.github.theredbrain.betteradventuremode.mixin.spell_engine.api.spell;

import com.github.theredbrain.betteradventuremode.spell_engine.DuckSpellCostMixin;
import net.spell_engine.api.spell.Spell;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(Spell.Cost.class)
public class SpellCostMixin implements DuckSpellCostMixin {
    @Unique
    private boolean consumeSelf = false;
    @Unique
    private boolean decrementEffect = false;
    @Unique
    private float manaCost = 0.0F;
    @Unique
    private float healthCost = 0.0F;
    @Unique
    private float staminaCost = 0.0F;

    public float betteradventuremode$getManaCost() {
        return manaCost;
    }

    public void betteradventuremode$setManaCost(float manaCost) {
        this.manaCost = manaCost;
    }

    public float betteradventuremode$getHealthCost() {
        return healthCost;
    }

    public void betteradventuremode$setHealthCost(float healthCost) {
        this.healthCost = healthCost;
    }

    public float betteradventuremode$getStaminaCost() {
        return staminaCost;
    }

    public void betteradventuremode$setStaminaCost(float staminaCost) {
        this.staminaCost = staminaCost;
    }

    public boolean betteradventuremode$isConsumeSelf() {
        return consumeSelf;
    }

    public void betteradventuremode$setConsumeSelf(boolean consumeSelf) {
        this.consumeSelf = consumeSelf;
    }

    public boolean betteradventuremode$isDecrementEffect() {
        return decrementEffect;
    }

    public void betteradventuremode$setDecrementEffect(boolean decrementEffect) {
        this.decrementEffect = decrementEffect;
    }
}
