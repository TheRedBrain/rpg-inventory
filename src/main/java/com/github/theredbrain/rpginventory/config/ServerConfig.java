package com.github.theredbrain.rpginventory.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;

@Config(
        name = "server"
)
public class ServerConfig implements ConfigData {
    @Comment("""
            When the mod 'Stamina Attributes' is installed, the following 6 options take effect
            
            When true, stamina must be above 0 for swapping hand items.
            """)
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

    @Comment("""
            Set to false to enable the 2x2 crafting grid
            in the adventure inventory screen
            """)
    public boolean disable_inventory_crafting_slots = false;
    public int inventory_crafting_slots_x_offset = 97;
    public int inventory_crafting_slots_y_offset = 42;

    public int spell_slots_x_offset = 98;
    public int spell_slots_y_offset = 90;

    @Comment("""
            Belts group by default
            
            default: 8
            """)
    public int order_1_slot_x_offset = 8;
    @Comment("default: 72")
    public int order_1_slot_y_offset = 72;

    @Comment("""
            Shoulders group by default
            
            default: 8
            """)
    public int order_2_slot_x_offset = 8;
    @Comment("default: 36")
    public int order_2_slot_y_offset = 36;

    @Comment("""
            Necklaces group by default
            
            default: 52
            """)
    public int order_3_slot_x_offset = 52;
    @Comment("default: 18")
    public int order_3_slot_y_offset = 18;

    @Comment("""
            Rings 1 group by default
            
            default: 77
            """)
    public int order_4_slot_x_offset = 77;
    @Comment("default: 36")
    public int order_4_slot_y_offset = 36;

    @Comment("""
            Rings 2 group by default
            
            default: 77
            """)
    public int order_5_slot_x_offset = 77;
    @Comment("default: 54")
    public int order_5_slot_y_offset = 54;

    @Comment("""
            Gloves group by default
            
            default: 77
            """)
    public int order_6_slot_x_offset = 77;
    @Comment("default: 72")
    public int order_6_slot_y_offset = 72;

    @Comment("""
            Main hand group by default
            
            default: 8
            """)
    public int order_7_slot_x_offset = 8;
    @Comment("default: 108")
    public int order_7_slot_y_offset = 108;

    @Comment("""
            Alternative main hand group by default
            
            default: 59
            """)
    public int order_8_slot_x_offset = 59;
    @Comment("default: 108")
    public int order_8_slot_y_offset = 108;

    @Comment("""
            Alternative offhand group by default
            
            default: 77
            """)
    public int order_9_slot_x_offset = 77;
    @Comment("default: 108")
    public int order_9_slot_y_offset = 108;

    public ServerConfig() {

    }
}
