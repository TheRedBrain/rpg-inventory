package com.github.theredbrain.bamcore.spell_engine;

public interface DuckSpellCostMixin {
    float getManaCost();
    void setManaCost(float manaCost);
    float getHealthCost();
    void setHealthCost(float healthCost);
    boolean isConsumeSelf();
    void setConsumeSelf(boolean consumeSelf);
}
