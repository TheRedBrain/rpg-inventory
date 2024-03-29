package com.github.theredbrain.betteradventuremode.registry;

import com.github.theredbrain.betteradventuremode.BetterAdventureMode;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;

public class DamageTypesRegistry {

    // region spell damage types
    public static final RegistryKey<DamageType> FIREBALL_DAMAGE_TYPE = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, BetterAdventureMode.identifier("fireball_damage_type"));
    public static final RegistryKey<DamageType> BLOOD_MAGIC_CASTING_DAMAGE_TYPE = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, BetterAdventureMode.identifier("blood_magic_casting_damage_type"));
    // endregion spell damage types

    // region weapon damage types
    public static final RegistryKey<DamageType> BATTLE_AXE_ONE_HANDED_DAMAGE_TYPE = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, BetterAdventureMode.identifier("battle_axe_one_handed_damage_type"));
    public static final RegistryKey<DamageType> BATTLE_AXE_TWO_HANDED_DAMAGE_TYPE = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, BetterAdventureMode.identifier("battle_axe_two_handed_damage_type"));
    public static final RegistryKey<DamageType> CLAW_DAMAGE_TYPE = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, BetterAdventureMode.identifier("claw_damage_type"));
    public static final RegistryKey<DamageType> DAGGER_DAMAGE_TYPE = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, BetterAdventureMode.identifier("dagger_damage_type"));
    public static final RegistryKey<DamageType> HALBERD_ONE_HANDED_DAMAGE_TYPE = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, BetterAdventureMode.identifier("halberd_one_handed_damage_type"));
    public static final RegistryKey<DamageType> HALBERD_TWO_HANDED_DAMAGE_TYPE = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, BetterAdventureMode.identifier("halberd_two_handed_damage_type"));
    public static final RegistryKey<DamageType> LONG_SWORD_ONE_HANDED_DAMAGE_TYPE = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, BetterAdventureMode.identifier("long_sword_one_handed_damage_type"));
    public static final RegistryKey<DamageType> LONG_SWORD_TWO_HANDED_DAMAGE_TYPE = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, BetterAdventureMode.identifier("long_sword_two_handed_damage_type"));
    public static final RegistryKey<DamageType> MACE_ONE_HANDED_DAMAGE_TYPE = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, BetterAdventureMode.identifier("mace_one_handed_damage_type"));
    public static final RegistryKey<DamageType> MACE_TWO_HANDED_DAMAGE_TYPE = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, BetterAdventureMode.identifier("mace_two_handed_damage_type"));
    public static final RegistryKey<DamageType> SHORT_SWORD_ONE_HANDED_DAMAGE_TYPE = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, BetterAdventureMode.identifier("short_sword_one_handed_damage_type"));
    public static final RegistryKey<DamageType> SHORT_SWORD_TWO_HANDED_DAMAGE_TYPE = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, BetterAdventureMode.identifier("short_sword_two_handed_damage_type"));
    public static final RegistryKey<DamageType> SHORT_SWORD_FIRE_ONE_HANDED_DAMAGE_TYPE = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, BetterAdventureMode.identifier("short_sword_fire_one_handed_damage_type"));
    public static final RegistryKey<DamageType> SHORT_SWORD_FIRE_TWO_HANDED_DAMAGE_TYPE = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, BetterAdventureMode.identifier("short_sword_fire_two_handed_damage_type"));
    public static final RegistryKey<DamageType> SPEAR_ONE_HANDED_DAMAGE_TYPE = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, BetterAdventureMode.identifier("spear_one_handed_damage_type"));
    public static final RegistryKey<DamageType> SPEAR_TWO_HANDED_DAMAGE_TYPE = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, BetterAdventureMode.identifier("spear_two_handed_damage_type"));
    public static final RegistryKey<DamageType> WAR_HAMMER_ONE_HANDED_DAMAGE_TYPE = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, BetterAdventureMode.identifier("war_hammer_one_handed_damage_type"));
    public static final RegistryKey<DamageType> WAR_HAMMER_TWO_HANDED_DAMAGE_TYPE = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, BetterAdventureMode.identifier("war_hammer_two_handed_damage_type"));
    public static final RegistryKey<DamageType> ZWEIHANDER_ONE_HANDED_DAMAGE_TYPE = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, BetterAdventureMode.identifier("zweihander_one_handed_damage_type"));
    public static final RegistryKey<DamageType> ZWEIHANDER_TWO_HANDED_DAMAGE_TYPE = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, BetterAdventureMode.identifier("zweihander_two_handed_damage_type"));
    // endregion weapon damage types

    public static final RegistryKey<DamageType> LAVA_DAMAGE_TYPE = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, BetterAdventureMode.identifier("lava_damage_type"));
    public static final RegistryKey<DamageType> LETHAL_FALL_DAMAGE_TYPE = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, BetterAdventureMode.identifier("lethal_fall_damage_type"));
    public static final RegistryKey<DamageType> BASHING_DAMAGE_TYPE = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, BetterAdventureMode.identifier("bashing_damage_type"));
    public static final RegistryKey<DamageType> PIERCING_DAMAGE_TYPE = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, BetterAdventureMode.identifier("piercing_damage_type"));
    public static final RegistryKey<DamageType> SLASHING_DAMAGE_TYPE = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, BetterAdventureMode.identifier("slashing_damage_type"));

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
    public static final RegistryKey<DamageType> DEFAULT_UNARMED_DAMAGE_TYPE = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, BetterAdventureMode.identifier("default_unarmed_damage_type"));
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
        } else {
            return damageTypeRegistryKey;
        }
    }
}
