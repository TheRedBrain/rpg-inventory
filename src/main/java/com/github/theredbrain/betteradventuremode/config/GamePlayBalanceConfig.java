package com.github.theredbrain.betteradventuremode.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;

@Config(
        name = "gamePlayBalance"
)
public class GamePlayBalanceConfig implements ConfigData {

    // TODO write a wiki page with an in-depth explanation
    @Comment("""
            When an entity has one of the damaging status effects (bleeding, burning, poison),
            the damage is dealt every duration tick which the following formula applies to:
            duration = duration_tick % update_modulus == update_checked_remainder
            For a duration of 201, an update_modulus of 40 and an update_checked_remainder of 1
            this means that the effect applies its damage 6 times
            (on the following duration_ticks: 201, 161, 121, 81, 41, 1)
            
            Bleeding
            """)
    public int bleeding_update_modulus = 20;
    public int bleeding_update_checked_remainder = 1;
    @Comment("Burning")
    public int burning_update_modulus = 50;
    public int burning_update_checked_remainder = 1;
    public float burning_damage = 2.0f;
    @Comment("Poison")
    public int poison_update_modulus = 40;
    public int poison_update_checked_remainder = 1;
    public float poison_damage = 1.0f;
    @Comment("Shock")
    public float shock_damage = 10.0f;
    @Comment("""
            When food effects have less than this many ticks of duration left,
            their corresponding food type can be eaten again
            """)
    public int food_effect_duration_threshold_to_allow_eating = 200;
    @Comment("Combat Roll Compat")
    public boolean rolling_requires_stamina = true;
    public float rolling_stamina_cost = 3.0f;
    public GamePlayBalanceConfig() {

    }
}
