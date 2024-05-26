package com.github.theredbrain.rpginventory.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;

@Config(
        name = "server"
)
public class ServerConfig implements ConfigData {
    @Comment("""
            Set to false to enable the 2x2 crafting grid
            in the adventure inventory screen
            """)
    public boolean disable_inventory_crafting_slots = false;

    @Comment("When true, stamina must be above 0 for swapping hand items.")
    public boolean swapping_hand_items_requires_stamina = false;
    @Comment("Stamina cost for toggling two handed stance")
    public float swapping_hand_items_stamina_cost = 0.0f;

    @Comment("When true, stamina must be above 0 for sheathing hand items.")
    public boolean sheathing_hand_items_requires_stamina = false;
    @Comment("Stamina cost for toggling two handed stance")
    public float sheathing_hand_items_stamina_cost = 0.0f;

    @Comment("When true, stamina must be above 0 for toggling two handed stance.")
    public boolean toggling_two_handed_stance_requires_stamina = false;
    @Comment("Stamina cost for toggling two handed stance")
    public float toggling_two_handed_stance_stamina_cost = 0.0f;

    @Comment("""
            When set to 'false', only items in the 'attack_items' tag can be used to attack/break blocks
            Being in creative mode or having the 'building mode' status effect always allows it
            """)
    public boolean allow_attacking_with_non_attack_items = true;
    @Comment("This status effect enables the building mode")
    public String building_mode_status_effect_identifier = "";

    @Comment("Additional debug log is shown in the console.")
    public boolean show_debug_log = false;
    public ServerConfig() {

    }
}
