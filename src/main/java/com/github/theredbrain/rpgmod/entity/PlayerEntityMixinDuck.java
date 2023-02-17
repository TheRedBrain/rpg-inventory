package com.github.theredbrain.rpgmod.entity;

import net.minecraft.entity.effect.StatusEffect;

public interface PlayerEntityMixinDuck {

    boolean tryEatAdventureFood(StatusEffect statusEffect, int duration);
}
