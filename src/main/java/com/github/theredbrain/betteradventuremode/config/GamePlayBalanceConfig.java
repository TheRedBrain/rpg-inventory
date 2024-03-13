package com.github.theredbrain.betteradventuremode.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;

@Config(
        name = "gamePlayBalance"
)
public class GamePlayBalanceConfig implements ConfigData {
    @Comment("""
            Player Attribute Base Values
            """)
    public float active_spell_slot_amount_base_value = 2.0F;
    public float equipment_weight_base_value = 0.0F;
    public float max_equipment_weight_base_value = 10.0F;
    public float health_regeneration_base_value = 0.0F;
    public float mana_regeneration_base_value = 0.0F;
    public float stamina_regeneration_base_value = 1.0F;
    public float max_mana_base_value = 0.0F;
    public float max_stamina_base_value = 20.0F;
    public float additional_bashing_base_value = 0.0F;
    public float additional_piercing_base_value = 0.0F;
    public float additional_slashing_base_value = 0.0F;
    public float additional_fire_base_value = 0.0F;
    public float additional_frost_base_value = 0.0F;
    public float additional_lighting_base_value = 0.0F;
    public float additional_poison_base_value = 0.0F;
    public float increased_bashing_base_value = 1.0F;
    public float increased_piercing_base_value = 1.0F;
    public float increased_slashing_base_value = 1.0F;
    public float increased_fire_base_value = 1.0F;
    public float increased_frost_base_value = 1.0F;
    public float increased_lighting_base_value = 1.0F;
    public float increased_poison_base_value = 1.0F;
    public float fire_resistance_value = 0.0F;
    public float frost_resistance_value = 0.0F;
    public float lighting_resistance_value = 0.0F;
    public float poison_resistance_value = 0.0F;

    // TODO convert to entity attributes
    // TODO write a wiki page with an in-depth explanation
//    @Comment("""
//            When an entity has one of the damaging status effects (bleeding, burning, poison),
//            the damage is dealt every duration tick which the following formula applies to:
//            duration = duration_tick % update_modulus == update_checked_remainder
//            For a duration of 201 this means that the effect applies its damage 6 times
//            (on the following duration_ticks: 201, 161, 121, 81, 41, 1)
//            """)
    @Comment("Bleeding")
    public int bleeding_update_modulus = 20;
    public int bleeding_update_checked_remainder = 1;
    public int default_bleeding_duration = 201;
    public float default_max_bleeding_build_up = 20.0f;
    public int default_bleeding_tick_threshold = 20;
    public int default_bleeding_build_up_reduction = 1;
    @Comment("Burning")
    public int burning_update_modulus = 50;
    public int burning_update_checked_remainder = 1;
    public float burning_damage = 2.0f;
    public int default_burning_duration = 351;
    public float default_max_burning_build_up = 20.0f;
    public int default_burning_tick_threshold = 20;
    public int default_burning_build_up_reduction = 1;
    @Comment("Freeze")
    public int default_freeze_duration = 200;
    public float default_max_freeze_build_up = 20.0f;
    public int default_freeze_tick_threshold = 20;
    public int default_freeze_build_up_reduction = 1;
    @Comment("Stagger")
    public int default_stagger_duration = 200;
    public float default_max_stagger_build_up = 20.0f;
    public int default_stagger_tick_threshold = 20;
    public int default_stagger_build_up_reduction = 1;
    @Comment("Poison")
    public int poison_update_modulus = 40;
    public int poison_update_checked_remainder = 1;
    public float poison_damage = 1.0f;
    public int default_poison_duration = 201;
    public float default_max_poison_build_up = 20.0f;
    public int default_poison_tick_threshold = 20;
    public int default_poison_build_up_reduction = 1;
    @Comment("Shock")
    public float shock_damage = 10.0f;
    public float default_max_shock_build_up = 20.0f;
    public int default_shock_tick_threshold = 20;
    public int default_shock_build_up_reduction = 1;
    public GamePlayBalanceConfig() {

    }
}
