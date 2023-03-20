package com.github.theredbrain.rpgmod.registry;

import net.minecraft.entity.attribute.ClampedEntityAttribute;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class EntityAttributesRegistry {

    public static final EntityAttribute BAM_HEALTH_REGENERATION = new ClampedEntityAttribute("bam_attribute.name.generic.bam_health_regeneration", 0.0, -1024.0, 1024.0).setTracked(true);
    public static final EntityAttribute BAM_MANA_REGENERATION = new ClampedEntityAttribute("bam_attribute.name.generic.bam_mana_regeneration", 0.0, -1024.0, 1024.0).setTracked(true);
    public static final EntityAttribute BAM_STAMINA_REGENERATION = new ClampedEntityAttribute("bam_attribute.name.generic.bam_stamina_regeneration", 0.0, -1024.0, 1024.0).setTracked(true);
    public static final EntityAttribute BAM_MAX_MANA = new ClampedEntityAttribute("bam_attribute.name.generic.bam_max_mana", 0.0, -0.0, 1024.0).setTracked(true);
    public static final EntityAttribute BAM_MAX_STAMINA = new ClampedEntityAttribute("bam_attribute.name.generic.bam_max_stamina", 0.0, -0.0, 1024.0).setTracked(true);

    public static void registerAttributes() {

        Registry.register(Registries.ATTRIBUTE, "generic.bam_health_regeneration", BAM_HEALTH_REGENERATION);
        Registry.register(Registries.ATTRIBUTE, "generic.bam_mana_regeneration", BAM_MANA_REGENERATION);
        Registry.register(Registries.ATTRIBUTE, "generic.bam_stamina_regeneration", BAM_STAMINA_REGENERATION);
        Registry.register(Registries.ATTRIBUTE, "generic.bam_max_mana", BAM_MAX_MANA);
        Registry.register(Registries.ATTRIBUTE, "generic.bam_max_stamina", BAM_MAX_STAMINA);
    }
}
