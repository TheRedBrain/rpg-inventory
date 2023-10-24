package com.github.theredbrain.bamcore.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;

@Config(
        name = "server"
)
public class ServerConfig implements ConfigData {
    @Comment("The player inventory screen is customized (WIP)")
    public boolean use_adventure_inventory_screen = true;
    public ServerConfig() {

    }
}
