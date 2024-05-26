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
    public boolean alternative_off_hand_slot_is_right = true;
    public boolean off_hand_slot_is_right = true;
    public int hand_slots_x_offset = -140;
    public int hand_slots_y_offset = -23;
    public int alternative_hand_slots_x_offset = 91;
    public int alternative_hand_slots_y_offset = -23;
    public boolean show_armor_bar = false;
    @Comment("Additional debug messages are shown in-game.")
    public boolean show_debug_messages = false;
    public ClientConfig() {

    }
}
