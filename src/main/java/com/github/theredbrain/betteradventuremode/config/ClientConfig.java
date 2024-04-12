package com.github.theredbrain.betteradventuremode.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;

@Config(
        name = "client"
)
public class ClientConfig implements ConfigData {
    @Comment("""
            Changes HUD elements like the health bar, disables some like the armor bar
            and adds new ones like a stamina bar.
            """)
    public boolean show_adventure_hud = false;
    @Comment("Current health, stamina and mana are shown above their respective bars in the HUD.")
    public boolean show_resource_bar_numbers = true;
    public int health_bar_number_color = -6250336;
    public int stamina_bar_number_color = -6250336;
    public int mana_bar_number_color = -6250336;
    @Comment("Additional debug messages are shown in-game.")
    public boolean show_debug_messages = false;
    public ClientConfig() {

    }
}
