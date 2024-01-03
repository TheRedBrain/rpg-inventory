package com.github.theredbrain.betteradventuremode.entity;

public interface DuckLivingEntityMixin {

    void bamcore$addPoise(float amount);
    float bamcore$getPoise();
    void bamcore$setPoise(float poise);
    int getStaggerDuration();
    double getStaggerLimitMultiplier();
}
