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
    @Comment("Set to false to disable the 360 degree 3rd person camera for connecting clients")
    public boolean allow_360_degree_third_person = true;
    @Comment("""
            When set to true, the first person perspective is disabled.
            Exceptions are possible, when using items in itemTag 'enables_first_person_perspective_on_using'
            or when under 'first_person_status_effect'""")
    public boolean disable_first_person = false;
    @Comment("""
            When this String is a valid status effect identifier,
            every player with this status effect is put into first person perspective,
            even if 'disable_first_person' is true""")
    public String first_person_status_effect = "betteradventuremode:first_person_perspective_enabled_effect";
    @Comment("""
            When set to true, changing the players yaw while attacking is disabled.
            """)
    public boolean disable_player_yaw_changes_during_attacks = true;
    @Comment("""
            When set to true, changing the players pitch is disabled.
            Exceptions are possible, when using items in itemTag 'allows_changing_pitch_on_using'
            or when under 'allow_pitch_changes_status_effect'""")
    public boolean disable_player_pitch_changes = false;
    @Comment("""
            When this String is a valid status effect identifier,
            every player with this status effect can change their pitch,
            even if 'disable_player_pitch_changes' is true""")
    public String allow_pitch_changes_status_effect = "betteradventuremode:changing_pitch_enabled_effect";
    @Comment("""
            The default pitch for each player.
            Min value: -90.0F, Max value: 90.0F, Default/Vanilla: 0.0F""")
    public float default_player_pitch = 0.0F;
    @Comment("The player inventory screen is customized")
    public boolean use_adventure_inventory_screen = true;
    @Comment("""
            Set to false to enable the 2x2 crafting grid
            in the adventure inventory screen
            """)
    public boolean disable_inventory_crafting_slots = true;
    @Comment("Disables food, saturation and exhaustion")
    public boolean disable_vanilla_food_system = true;
    @Comment("Disables the recipe book")
    public boolean disable_recipe_book = true;
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
    public boolean shouldJigSawGenerationBeDeterministic = false;
    @Comment("Set to 'true' for the vanilla behaviour")
    public boolean shouldJigSawStructuresBeRandomlyRotated = false;
    @Comment("Additional debug log is shown in the console.")
    public boolean show_debug_log = false;
    @Comment("Additional debug messages are send in game.")
    public boolean show_debug_messages = false;
    public ServerConfig() {

    }
}
