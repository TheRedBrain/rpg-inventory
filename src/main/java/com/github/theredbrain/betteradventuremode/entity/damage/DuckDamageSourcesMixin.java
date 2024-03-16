package com.github.theredbrain.betteradventuremode.entity.damage;

import net.minecraft.entity.damage.DamageSource;

public interface DuckDamageSourcesMixin {
    DamageSource betteradventuremode$bleeding();
    DamageSource betteradventuremode$burning();
    DamageSource betteradventuremode$poison();
    DamageSource betteradventuremode$shocked();
    DamageSource betteradventuremode$lava();
    DamageSource betteradventuremode$bloodMagicCasting();
}
