package com.github.theredbrain.betteradventuremode.item;

import net.minecraft.sound.SoundEvent;
import org.jetbrains.annotations.Nullable;

public interface IMakesEquipSound {

    @Nullable
    SoundEvent getEquipSound();

}
