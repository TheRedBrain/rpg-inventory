package com.github.theredbrain.bamcore.entity;

public interface DuckLivingEntityMixin {

    void bamcore$addPoise(float amount);
    float bamcore$getPoise();
    void bamcore$setPoise(float poise);
    int getStaggerDuration();
    double getStaggerLimitMultiplier();
}
