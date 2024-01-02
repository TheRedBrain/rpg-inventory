package com.github.theredbrain.bamcore.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;

import java.util.ArrayList;
import java.util.List;

@Config(
        name = "server"
)
public class ServerConfig implements ConfigData {
    @Comment("The player inventory screen is customized (WIP)")
    public boolean use_adventure_inventory_screen = true;
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
//    public List<Double> worldSpawnAngleList = new ArrayList<>();
    public ServerConfig() {

    }
}
