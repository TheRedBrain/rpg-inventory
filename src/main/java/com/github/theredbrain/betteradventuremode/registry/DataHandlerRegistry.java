package com.github.theredbrain.betteradventuremode.registry;

import com.github.theredbrain.betteradventuremode.entity.decoration.MannequinEntity;
import net.minecraft.entity.data.TrackedDataHandler;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;

public class DataHandlerRegistry {
    public static final TrackedDataHandler<MannequinEntity.SheathedWeaponMode> SHEATHED_WEAPON_MODE = TrackedDataHandler.ofEnum(MannequinEntity.SheathedWeaponMode.class);
    public static void init() {
        TrackedDataHandlerRegistry.register(SHEATHED_WEAPON_MODE);
    }
}
