package com.github.theredbrain.betteradventuremode.spell_engine;

public interface DuckSpellCostMixin {
    float betteradventuremode$getManaCost();
    void betteradventuremode$setManaCost(float manaCost);
    float betteradventuremode$getHealthCost();
    void betteradventuremode$setHealthCost(float healthCost);
    float betteradventuremode$getStaminaCost();
    void betteradventuremode$setStaminaCost(float staminaCost);
    boolean betteradventuremode$isConsumeSelf();
    void betteradventuremode$setConsumeSelf(boolean consumeSelf);
    boolean betteradventuremode$isDecrementEffect();
    void betteradventuremode$setDecrementEffect(boolean decrementEffect);
}
