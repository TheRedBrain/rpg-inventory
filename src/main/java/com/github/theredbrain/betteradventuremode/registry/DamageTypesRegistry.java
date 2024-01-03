package com.github.theredbrain.betteradventuremode.registry;

import com.github.theredbrain.betteradventuremode.BetterAdventureMode;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

public class DamageTypesRegistry {

    public static final RegistryKey<DamageType> PLAYER_BASHING_DAMAGE_TYPE = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, BetterAdventureMode.identifier("player_bashing_damage_type"));
    public static final RegistryKey<DamageType> PLAYER_PIERCING_DAMAGE_TYPE = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, BetterAdventureMode.identifier("player_piercing_damage_type"));
    public static final RegistryKey<DamageType> PLAYER_SLASHING_DAMAGE_TYPE = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, BetterAdventureMode.identifier("player_slashing_damage_type"));
    public static final RegistryKey<DamageType> PLAYER_UNARMED_DAMAGE_TYPE = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, BetterAdventureMode.identifier("player_unarmed_damage_type"));

}
