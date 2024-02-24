package com.github.theredbrain.betteradventuremode.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;

@Config(
        name = "client"
)
public class ClientConfig implements ConfigData {
    public boolean show_adventure_hud = true;
    @Comment("Current health, stamina and mana are shown above their respective bars in the HUD.")
    public boolean show_resource_bar_numbers = true;
    @Comment("Color of the health bar number.")
    public int health_bar_number_color = -6250336;
    @Comment("Color of the stamina bar number.")
    public int stamina_bar_number_color = -6250336;
    @Comment("Color of the mana bar number.")
    public int mana_bar_number_color = -6250336;
    @Comment("Additional debug messages are shown in-game.")
    public boolean show_debug_messages = true;
    public ClientConfig() {

    }
}
