package com.github.theredbrain.rpginventory.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;

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
    @Comment("Disables the recipe book")
    public boolean disable_recipe_book = false;
    @Comment("Enables velocity modifiers on many blocks, eg faster movement on dirt paths and slower movement on sand")
    public boolean enable_harder_movement = true;
    public boolean allow_jumping_during_attacks = true;
    public boolean sprinting_only_when_button_is_pressed = false;
    @Comment("""
            When set to 'false', only items in the 'attack_items' tag can be used to attack/break blocks
            Being in creative mode or having the 'adventure building' status effect always allow it
            """)
    public boolean allow_attacking_with_non_attack_items = true;
    @Comment("This status effect enables the building mode")
    public String building_mode_status_effect_identifier = "";
    @Comment("Additional debug log is shown in the console.")
    public boolean show_debug_log = false;
    @Comment("Additional debug messages are send in game.")
    public boolean show_debug_messages = false;
    public ServerConfig() {

    }
}
