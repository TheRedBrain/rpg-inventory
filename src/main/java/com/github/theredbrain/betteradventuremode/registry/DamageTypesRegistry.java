package com.github.theredbrain.betteradventuremode.registry;

import com.github.theredbrain.betteradventuremode.BetterAdventureMode;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Identifier;
import net.minecraft.world.gen.FlatLevelGeneratorPreset;

public class DamageTypesRegistry {

    // region spell damage types
    public static final RegistryKey<DamageType> FIREBALL_DAMAGE_TYPE = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, BetterAdventureMode.identifier("fireball_damage_type"));
    public static final RegistryKey<DamageType> BLOOD_MAGIC_CASTING_DAMAGE_TYPE = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, BetterAdventureMode.identifier("blood_magic_casting_damage_type"));
    // endregion spell damage types

    // region weapon damage types
    public static final RegistryKey<DamageType> CLAW_DAMAGE_TYPE = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, BetterAdventureMode.identifier("claw_damage_type"));
    // endregion weapon damage types

    public static final RegistryKey<DamageType> LAVA_DAMAGE_TYPE = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, BetterAdventureMode.identifier("lava_damage_type"));
    public static final RegistryKey<DamageType> BASHING_DAMAGE_TYPE = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, BetterAdventureMode.identifier("bashing_damage_type"));
    public static final RegistryKey<DamageType> PIERCING_DAMAGE_TYPE = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, BetterAdventureMode.identifier("piercing_damage_type"));
    public static final RegistryKey<DamageType> SLASHING_DAMAGE_TYPE = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, BetterAdventureMode.identifier("slashing_damage_type"));
    public static final RegistryKey<DamageType> UNARMED_DAMAGE_TYPE = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, BetterAdventureMode.identifier("unarmed_damage_type"));

    // region status effect damage types
    public static final RegistryKey<DamageType> BLEEDING_DAMAGE_TYPE = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, BetterAdventureMode.identifier("bleeding_damage_type"));
    public static final RegistryKey<DamageType> BURNING_DAMAGE_TYPE = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, BetterAdventureMode.identifier("burning_damage_type"));
    public static final RegistryKey<DamageType> POISON_DAMAGE_TYPE = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, BetterAdventureMode.identifier("poison_damage_type"));
    public static final RegistryKey<DamageType> SHOCKED_DAMAGE_TYPE = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, BetterAdventureMode.identifier("shocked_damage_type"));
    // endregion status effect damage types

    // region player damage types
    public static final RegistryKey<DamageType> PLAYER_BASHING_DAMAGE_TYPE = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, BetterAdventureMode.identifier("player_bashing_damage_type"));
    public static final RegistryKey<DamageType> PLAYER_PIERCING_DAMAGE_TYPE = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, BetterAdventureMode.identifier("player_piercing_damage_type"));
    public static final RegistryKey<DamageType> PLAYER_SLASHING_DAMAGE_TYPE = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, BetterAdventureMode.identifier("player_slashing_damage_type"));
    public static final RegistryKey<DamageType> PLAYER_UNARMED_DAMAGE_TYPE = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, BetterAdventureMode.identifier("player_unarmed_damage_type"));
    // endregion player damage types

    /**
     * @return the player variant of the parameter if it exists and the parameter otherwise
     */
    public static RegistryKey<DamageType> getPlayerVariant(RegistryKey<DamageType> damageTypeRegistryKey) {
        if (damageTypeRegistryKey == BASHING_DAMAGE_TYPE) {
            return PLAYER_BASHING_DAMAGE_TYPE;
        } else if (damageTypeRegistryKey == PIERCING_DAMAGE_TYPE) {
            return PLAYER_PIERCING_DAMAGE_TYPE;
        } else if (damageTypeRegistryKey == SLASHING_DAMAGE_TYPE) {
            return PLAYER_SLASHING_DAMAGE_TYPE;
        } else if (damageTypeRegistryKey == UNARMED_DAMAGE_TYPE) {
            return PLAYER_UNARMED_DAMAGE_TYPE;
        } else {
            return damageTypeRegistryKey;
        }
    }
}
