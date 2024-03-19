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
    public boolean use_adventure_inventory_screen = true;
    @Comment("Set to false to enable the 2x2 crafting grid in the adventure inventory screen")
    public boolean disable_inventory_crafting_slots = true;
    @Comment("Disables food, saturation and exhaustion")
    public boolean disable_vanilla_food_system = true;
    @Comment("Enables Better3rdPerson compatibility")
    public boolean enableBetter3rdPersonCompat = true;
    @Comment("Disables the recipe book")
    public boolean disable_recipe_book = true;
    @Comment("""
            World Spawn is chosen randomly from the following lists.
            It is recommended to set the gamerule 'spawnRadius' to 0.
            """)
    public boolean use_predefined_position_for_world_spawn = true;
    @Comment("""
            A random value from worldSpawnXList is chosen.
            If worldSpawnYList and worldSpawnZList have an entry at the same index, that is the new Spawn Point.
            If not, the normal Spawn Point is used.
            """)
    public List<Integer> worldSpawnXList = new ArrayList<>();
    public List<Integer> worldSpawnYList = new ArrayList<>();
    public List<Integer> worldSpawnZList = new ArrayList<>();
    @Comment("The radius crafting root blocks search for tab provider blocks")
    public int crafting_root_block_reach_radius = 10;
    @Comment("Set to 'true' for the vanilla behaviour")
    public boolean shouldJigSawGenerationBeDeterministic = false;
    @Comment("Set to 'true' for the vanilla behaviour")
    public boolean shouldJigSawStructuresBeRandomlyRotated = false;
    @Comment("Additional debug log is shown in the console.")
    public boolean show_debug_log = true;
    @Comment("Additional debug messages are send in game.")
    public boolean show_debug_messages = true;
    public ServerConfig() {

    }
}
