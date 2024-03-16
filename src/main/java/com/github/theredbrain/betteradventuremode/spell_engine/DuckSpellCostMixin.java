package com.github.theredbrain.betteradventuremode.spell_engine;

public interface DuckSpellCostMixin {
    boolean betteradventuremode$checkHealthCost();
    void betteradventuremode$setCheckHealthCost(boolean checkHealthCost);
    boolean betteradventuremode$checkManaCost();
    void betteradventuremode$setCheckManaCost(boolean checkManaCost);
    boolean betteradventuremode$checkStaminaCost();
    void betteradventuremode$setCheckStaminaCost(boolean checkStaminaCost);
    boolean betteradventuremode$checkSpellEffectCost();
    void betteradventuremode$setCheckSpellEffectCost(boolean checkSpellEffectCost);
    float betteradventuremode$getManaCost();
    void betteradventuremode$setManaCost(float manaCost);
    float betteradventuremode$getHealthCost();
    void betteradventuremode$setHealthCost(float healthCost);
    float betteradventuremode$getStaminaCost();
    void betteradventuremode$setStaminaCost(float staminaCost);
    boolean betteradventuremode$consumeSelf();
    void betteradventuremode$setConsumeSelf(boolean consumeSelf);
    int betteradventuremode$getDecrementEffectAmount();
    void betteradventuremode$setDecrementEffectAmount(int decrementEffectAmount);
}
