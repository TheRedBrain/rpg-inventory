package com.github.theredbrain.betteradventuremode.entity;

public interface DuckLivingEntityMixin {

    int betteradventuremode$getStaminaRegenerationDelayThreshold();
    int betteradventuremode$getHealthTickThreshold();
    int betteradventuremode$getStaminaTickThreshold();
    int betteradventuremode$getManaTickThreshold();

    float betteradventuremode$getRegeneratedHealth();
    float betteradventuremode$getRegeneratedStamina();
    float betteradventuremode$getRegeneratedMana();

    void betteradventuremode$addBleedingBuildUp(float amount);
    float betteradventuremode$getBleedingBuildUp();
    void betteradventuremode$setBleedingBuildUp(float bleedingBuildUp);
    float betteradventuremode$getMaxBleedingBuildUp();
    int betteradventuremode$getBleedingDuration();

    void betteradventuremode$addBurnBuildUp(float amount);
    float betteradventuremode$getBurnBuildUp();
    void betteradventuremode$setBurnBuildUp(float burnBuildUp);
    float betteradventuremode$getMaxBurnBuildUp();
    int betteradventuremode$getBurnDuration();

    void betteradventuremode$addFreezeBuildUp(float amount);
    float betteradventuremode$getFreezeBuildUp();
    void betteradventuremode$setFreezeBuildUp(float freezeBuildUp);
    float betteradventuremode$getMaxFreezeBuildUp();
    int betteradventuremode$getFreezeDuration();

    void betteradventuremode$addStaggerBuildUp(float amount);
    float betteradventuremode$getStaggerBuildUp();
    void betteradventuremode$setStaggerBuildUp(float poise);
    float betteradventuremode$getMaxStaggerBuildUp();
    int betteradventuremode$getStaggerDuration();

    void betteradventuremode$addPoisonBuildUp(float amount);
    float betteradventuremode$getPoisonBuildUp();
    void betteradventuremode$setPoisonBuildUp(float poisonBuildUp);
    float betteradventuremode$getMaxPoisonBuildUp();
    int betteradventuremode$getPoisonDuration();

    void betteradventuremode$addShockBuildUp(float amount);
    float betteradventuremode$getShockBuildUp();
    void betteradventuremode$setShockBuildUp(float shockBuildUp);
    float betteradventuremode$getMaxShockBuildUp();
    int betteradventuremode$getShockDuration();

    float betteradventuremode$getHealthRegeneration();

    float betteradventuremode$getManaRegeneration();
    float betteradventuremode$getMaxMana();
    void betteradventuremode$addMana(float amount);
    float betteradventuremode$getMana();
    void betteradventuremode$setMana(float mana);

    float betteradventuremode$getStaminaRegeneration();
    float betteradventuremode$getMaxStamina();
    void betteradventuremode$addStamina(float amount);
    float betteradventuremode$getStamina();
    void betteradventuremode$setStamina(float mana);

    boolean betteradventuremode$canParry();

}
