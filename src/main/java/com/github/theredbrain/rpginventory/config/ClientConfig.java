package com.github.theredbrain.rpginventory.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;

@Config(
        name = "client"
)
public class ClientConfig implements ConfigData {
    public boolean off_hand_item_is_right = true;
    public boolean alternative_off_hand_item_is_right = true;
    public int hand_slots_x_offset = -140;
    public int hand_slots_y_offset = -23;
    public int alternative_hand_slots_x_offset = 91;
    public int alternative_hand_slots_y_offset = -23;
    public boolean show_armor_bar = false;
    public ClientConfig() {

    }
}
