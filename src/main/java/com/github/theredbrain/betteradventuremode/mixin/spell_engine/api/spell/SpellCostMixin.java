package com.github.theredbrain.betteradventuremode.mixin.spell_engine.api.spell;

import com.github.theredbrain.betteradventuremode.spell_engine.DuckSpellCostMixin;
import net.spell_engine.api.spell.Spell;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(Spell.Cost.class)
public class SpellCostMixin implements DuckSpellCostMixin {
    @Unique
    private boolean checkHealthCost = false;
    @Unique
    private boolean checkManaCost = true;
    @Unique
    private boolean checkStaminaCost = false;
    @Unique
    private boolean checkSpellEffectCost = false;
    @Unique
    private boolean consumeSelf = false;
    @Unique
    private int decrementEffectAmount = -1;
    @Unique
    private float manaCost = 0.0F;
    @Unique
    private float healthCost = 0.0F;
    @Unique
    private float staminaCost = 0.0F;

    public boolean betteradventuremode$checkHealthCost() {
        return this.checkHealthCost;
    }

    public void betteradventuremode$setCheckHealthCost(boolean checkHealthCost) {
        this.checkHealthCost = checkHealthCost;
    }

    public boolean betteradventuremode$checkManaCost() {
        return this.checkManaCost;
    }

    public void betteradventuremode$setCheckManaCost(boolean checkManaCost) {
        this.checkManaCost = checkManaCost;
    }

    public boolean betteradventuremode$checkStaminaCost() {
        return this.checkStaminaCost;
    }

    public void betteradventuremode$setCheckStaminaCost(boolean checkStaminaCost) {
        this.checkStaminaCost = checkStaminaCost;
    }

    public boolean betteradventuremode$checkSpellEffectCost() {
        return this.checkSpellEffectCost;
    }

    public void betteradventuremode$setCheckSpellEffectCost(boolean checkSpellEffectCost) {
        this.checkSpellEffectCost = checkSpellEffectCost;
    }

    public float betteradventuremode$getManaCost() {
        return this.manaCost;
    }

    public void betteradventuremode$setManaCost(float manaCost) {
        this.manaCost = manaCost;
    }

    public float betteradventuremode$getHealthCost() {
        return this.healthCost;
    }

    public void betteradventuremode$setHealthCost(float healthCost) {
        this.healthCost = healthCost;
    }

    public float betteradventuremode$getStaminaCost() {
        return this.staminaCost;
    }

    public void betteradventuremode$setStaminaCost(float staminaCost) {
        this.staminaCost = staminaCost;
    }

    public boolean betteradventuremode$consumeSelf() {
        return this.consumeSelf;
    }

    public void betteradventuremode$setConsumeSelf(boolean consumeSelf) {
        this.consumeSelf = consumeSelf;
    }

    public int betteradventuremode$getDecrementEffectAmount() {
        return this.decrementEffectAmount;
    }

    public void betteradventuremode$setDecrementEffectAmount(int decrementEffect) {
        this.decrementEffectAmount = decrementEffect;
    }
}
