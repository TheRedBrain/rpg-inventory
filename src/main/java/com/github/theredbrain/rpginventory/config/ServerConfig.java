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
    public boolean swapping_hand_items_requires_stamina = true;
    @Comment("Stamina cost for toggling two handed stance")
    public float swapping_hand_items_stamina_cost = 1.0f;

    @Comment("When true, stamina must be above 0 for sheathing hand items.")
    public boolean sheathing_hand_items_requires_stamina = true;
    @Comment("Stamina cost for toggling two handed stance")
    public float sheathing_hand_items_stamina_cost = 1.0f;

    @Comment("When true, stamina must be above 0 for toggling two handed stance.")
    public boolean toggling_two_handed_stance_requires_stamina = true;
    @Comment("Stamina cost for toggling two handed stance")
    public float toggling_two_handed_stance_stamina_cost = 1.0f;

    public String keep_inventory_status_effect_identifier = "variousstatuseffects:keep_inventory";

    public String civilisation_status_effect_identifier = "variousstatuseffects:civilisation";

    public String wilderness_status_effect_identifier = "variousstatuseffects:wilderness";

    @Comment("This status effect enables the building mode")
    public String building_mode_status_effect_identifier = "scriptblocks:building_mode";

    @Comment("This status effect is applied when an item in the 'non_two_handed_items' item tag is equipped")
    public String needs_two_handing_status_effect_identifier = "variousstatuseffects:needs_two_handing";

    @Comment("This status effect is applied when an item which is not in the 'attack_items' item tag is equipped and the 'allow_attacking_with_non_attack_items' option is set to false")
    public String no_attack_item_status_effect_identifier = "variousstatuseffects:no_attack_item";
    public boolean allow_attacking_with_non_attack_items = true;

    @Comment("Additional debug log is shown in the console.")
    public boolean show_debug_log = false;
    public ServerConfig() {

    }
}
