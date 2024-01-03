package com.github.theredbrain.betteradventuremode.entity.player;

public interface DuckHungerManagerMixin {

    int betteradventuremode$getHealthTickTimer();

    void betteradventuremode$setHealthTickTimer(int healthTickTimer);

    int betteradventuremode$getManaTickTimer();

    void betteradventuremode$setManaTickTimer(int manaTickTimer);

    int betteradventuremode$getStaminaTickTimer();

    void betteradventuremode$setStaminaTickTimer(int staminaTickTimer);

    int betteradventuremode$getStaminaRegenerationDelayTimer();

    void betteradventuremode$setStaminaRegenerationDelayTimer(int staminaRegenerationDelayTimer);

    boolean betteradventuremode$isOverBurdened();

    double betteradventuremode$getEncumbrance();
}
