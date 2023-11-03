package com.github.theredbrain.bamcore.entity.player;

public interface DuckHungerManagerMixin {

    int getHealthTickTimer();

    void setHealthTickTimer(int healthTickTimer);

    int getManaTickTimer();

    void setManaTickTimer(int manaTickTimer);

    int getStaminaTickTimer();

    void setStaminaTickTimer(int staminaTickTimer);

    int getStaminaRegenerationDelayTimer();

    void setStaminaRegenerationDelayTimer(int staminaRegenerationDelayTimer);

    boolean isOverBurdened();

    double getEncumbrance();
}
