package com.github.theredbrain.bamcore.registry;

import net.minecraft.entity.attribute.ClampedEntityAttribute;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class EntityAttributesRegistry {

    public static final EntityAttribute MAX_EQUIPMENT_WEIGHT = new ClampedEntityAttribute("attribute.name.generic.max_equipment_weight", 1.0, 1.0, 1024.0).setTracked(true);
    public static final EntityAttribute EQUIPMENT_WEIGHT = new ClampedEntityAttribute("attribute.name.generic.equipment_weight", 0.0, 0.0, 1024.0).setTracked(true);
    public static final EntityAttribute HEALTH_REGENERATION = new ClampedEntityAttribute("attribute.name.generic.health_regeneration", 0.0, -1024.0, 1024.0).setTracked(true);
    public static final EntityAttribute MANA_REGENERATION = new ClampedEntityAttribute("attribute.name.generic.mana_regeneration", 0.0, -1024.0, 1024.0).setTracked(true);
    public static final EntityAttribute STAMINA_REGENERATION = new ClampedEntityAttribute("attribute.name.generic.stamina_regeneration", 0.0, -1024.0, 1024.0).setTracked(true);
    public static final EntityAttribute MAX_MANA = new ClampedEntityAttribute("attribute.name.generic.max_mana", 0.0, -0.0, 1024.0).setTracked(true);
    public static final EntityAttribute MAX_STAMINA = new ClampedEntityAttribute("attribute.name.generic.max_stamina", 0.0, -0.0, 1024.0).setTracked(true);
    public static final EntityAttribute MAX_MAGIC_SHIELD = new ClampedEntityAttribute("attribute.name.generic.max_magic_shield", 0.0, 0.0, 1024.0).setTracked(true);
    public static final EntityAttribute MAX_POISE = new ClampedEntityAttribute("attribute.name.generic.max_poise", 0.0, 0.0, 1024.0).setTracked(true);
    public static final EntityAttribute ACTIVE_SPELL_SLOT_AMOUNT = new ClampedEntityAttribute("attribute.name.generic.active_spell_slot_amount", 0.0, 0.0, 8.0).setTracked(true);

    public static void registerAttributes() {

        Registry.register(Registries.ATTRIBUTE, "generic.max_equipment_weight", MAX_EQUIPMENT_WEIGHT);
        Registry.register(Registries.ATTRIBUTE, "generic.equipment_weight", EQUIPMENT_WEIGHT);
        Registry.register(Registries.ATTRIBUTE, "generic.health_regeneration", HEALTH_REGENERATION);
        Registry.register(Registries.ATTRIBUTE, "generic.mana_regeneration", MANA_REGENERATION);
        Registry.register(Registries.ATTRIBUTE, "generic.stamina_regeneration", STAMINA_REGENERATION);
        Registry.register(Registries.ATTRIBUTE, "generic.max_mana", MAX_MANA);
        Registry.register(Registries.ATTRIBUTE, "generic.max_stamina", MAX_STAMINA);
        Registry.register(Registries.ATTRIBUTE, "generic.max_magic_shield", MAX_MAGIC_SHIELD);
        Registry.register(Registries.ATTRIBUTE, "generic.max_poise", MAX_POISE);
        Registry.register(Registries.ATTRIBUTE, "generic.active_spell_slot_amount", ACTIVE_SPELL_SLOT_AMOUNT);
    }
}
