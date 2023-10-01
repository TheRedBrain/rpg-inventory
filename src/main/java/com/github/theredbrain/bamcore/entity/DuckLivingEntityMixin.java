package com.github.theredbrain.bamcore.entity;

public interface DuckLivingEntityMixin {

    float bamcore$getCustomArmor();
    float bamcore$getCustomArmorToughness();
//    float bamcore$getMaxPoise(); // TODO poise
    void bamcore$addPoise(float amount);
//    float bamcore$getPoise(); // TODO poise
    void bamcore$setPoise(float poise);
}
