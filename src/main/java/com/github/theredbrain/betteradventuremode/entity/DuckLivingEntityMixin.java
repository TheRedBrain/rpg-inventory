package com.github.theredbrain.betteradventuremode.entity;

public interface DuckLivingEntityMixin {

    void betteradventuremode$addPoise(float amount);
    float betteradventuremode$getPoise();
    void betteradventuremode$setPoise(float poise);
    int betteradventuremode$getStaggerDuration();
    double betteradventuremode$getStaggerLimitMultiplier();
}
