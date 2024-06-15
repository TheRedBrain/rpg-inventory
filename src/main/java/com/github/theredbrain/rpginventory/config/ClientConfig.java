package com.github.theredbrain.rpginventory.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;

@Config(
        name = "client"
)
public class ClientConfig implements ConfigData {
    public boolean always_show_selected_hotbar_slot = false;
    public boolean offhand_item_is_right = true;
    public boolean alternative_offhand_item_is_right = true;
    public int hand_slots_x_offset = -140;
    public int hand_slots_y_offset = -23;
    public int alternative_hand_slots_x_offset = 91;
    public int alternative_hand_slots_y_offset = -23;
    public boolean show_armor_bar = false;
    public boolean show_attribute_screen_when_opening_inventory_screen = false;
    // TODO
    //  format where only difference to base value is shown
    @Comment("""
            Defines what attributes are displayed on the attribute screen
            Each string must follow one of these formats:
            ATTRIBUTE_VALUE:<translation_key>:attribute_id_namespace:attribute_id_path
            ATTRIBUTE_RELATION:<translation_key>:attribute_id_namespace:attribute_id_path:attribute_id_namespace:attribute_id_path
            EMPTY_LINE
            STRING:string
            TRANSLATABLE_STRING:<translation_key>
            Strings with other formats are ignored
            """)
    public String[] attribute_screen_configuration = {
            "TRANSLATABLE_STRING:gui.adventureInventory.attributes",
            "EMPTY_LINE",
            "ATTRIBUTE_VALUE:minecraft:generic.max_health",
            "ATTRIBUTE_VALUE:healthregenerationoverhaul:generic.health_regeneration",
            "ATTRIBUTE_VALUE:manaattributes:generic.max_mana",
            "ATTRIBUTE_VALUE:manaattributes:generic.mana_regeneration",
            "ATTRIBUTE_VALUE:staminaattributes:generic.max_stamina",
            "ATTRIBUTE_VALUE:staminaattributes:generic.stamina_regeneration",
            "ATTRIBUTE_VALUE:minecraft:generic.armor",
            "ATTRIBUTE_VALUE:minecraft:generic.armor_toughness",
            "ATTRIBUTE_VALUE:minecraft:generic.luck"
    };
    public ClientConfig() {

    }
}
