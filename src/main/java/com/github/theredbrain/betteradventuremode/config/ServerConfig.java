package com.github.theredbrain.betteradventuremode.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;

import java.util.ArrayList;
import java.util.List;

@Config(
        name = "server"
)
public class ServerConfig implements ConfigData {
    @Comment("The player inventory screen is customized")
    public boolean use_adventure_inventory_screen = false;
    @Comment("""
            Set to false to enable the 2x2 crafting grid
            in the adventure inventory screen
            """)
    public boolean disable_inventory_crafting_slots = false;
    @Comment("Disables food, saturation and exhaustion")
    public boolean disable_vanilla_food_system = false;
    @Comment("Disables the recipe book")
    public boolean disable_recipe_book = false;
    @Comment("Enables velocity modifiers on many blocks, eg faster movement on dirt paths and slower movement on sand")
    public boolean enable_harder_movement = true;
    @Comment("""
            When set to true, changing the players orientation while attacking is disabled.
            """)
    public boolean disable_player_yaw_changes_during_attacks = false;
    @Comment("""
            When set to true, the pitch of the players attack is restricted.
            """)
    public boolean restrict_attack_pitch = false;
    public boolean allow_jumping_during_attacks = true;
    public boolean sprinting_only_when_button_is_pressed = false;
    @Comment("""
            Disables Better Combat's formerly client feature of
            continuously attacking while holding down the attack key.
            """)
    public boolean disable_better_combat_hold_to_attack = false;
    @Comment("""
            World Spawn is chosen randomly from the following lists.
            It is recommended to set the gamerule 'spawnRadius' to 0.
            """)
    public boolean use_predefined_position_for_world_spawn = false;
    @Comment("""
            A random value from worldSpawnXList is chosen.
            If worldSpawnYList and worldSpawnZList have an entry
            at the same index, that is the new Spawn Point.
            If not, the normal Spawn Point is used.
            """)
    public List<Integer> worldSpawnXList = new ArrayList<>();
    public List<Integer> worldSpawnYList = new ArrayList<>();
    public List<Integer> worldSpawnZList = new ArrayList<>();
    @Comment("""
            The radius around crafting root blocks where tab provider
            blocks can open the crafting screen and enable their crafting tab/level.
            """)
    public int crafting_root_block_reach_radius = 10;
    @Comment("""
            When set to true, interacting with the Crafting Root block
            opens the crafting screen on tab 0.
            Optionally add 'betteradventuremode:crafting_root_block' to
            the 'provides_crafting_tab_0_level' block tag.
            """)
    public boolean crafting_root_block_provides_crafting_tab = false;
    @Comment("Set to 'true' for the vanilla behaviour")
    public boolean shouldJigSawGenerationBeDeterministic = true;
    @Comment("Set to 'true' for the vanilla behaviour")
    public boolean shouldJigSawStructuresBeRandomlyRotated = true;
    @Comment("Set to 'true' for the vanilla behaviour")
    public boolean useVanillaDamageCalculation = true;
    @Comment("""
            When set to 'false', only items in the 'attack_items' tag can be used to attack/break blocks
            Being in creative mode or having the 'adventure building' status effect always allow it
            """)
    public boolean allow_attacking_with_non_attack_items = true;
    @Comment("""
            When food effects have less than this many ticks of duration left,
            their corresponding food type can be eaten again
            """)
    public int food_effect_duration_threshold_to_allow_eating = 200;
    @Comment("Combat Roll Compat")
    public boolean rolling_requires_stamina = false;
    public float rolling_stamina_cost = 0.0f;
    @Comment("Set to 'true' for the vanilla behaviour")
    public boolean disable_jump_crit_mechanic = false;
    @Comment("Additional debug log is shown in the console.")
    public boolean show_debug_log = false;
    @Comment("Additional debug messages are send in game.")
    public boolean show_debug_messages = false;
    public ServerConfig() {

    }
}
