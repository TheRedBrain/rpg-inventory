package com.github.theredbrain.betteradventuremode.registry;

import com.github.theredbrain.betteradventuremode.BetterAdventureMode;
import com.github.theredbrain.betteradventuremode.entity.decoration.MannequinEntity;
import com.github.theredbrain.betteradventuremode.entity.mob.SpawnerBoundEntity;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.entity.attribute.ClampedEntityAttribute;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class EntityAttributesRegistry {

    public static final EntityAttribute ACTIVE_SPELL_SLOT_AMOUNT = new ClampedEntityAttribute("attribute.name.player.active_spell_slot_amount", 0.0, 0.0, 8.0).setTracked(true);
    public static final EntityAttribute EQUIPMENT_WEIGHT = new ClampedEntityAttribute("attribute.name.player.equipment_weight", 0.0, 0.0, 1024.0).setTracked(true);
    public static final EntityAttribute MAX_EQUIPMENT_WEIGHT = new ClampedEntityAttribute("attribute.name.player.max_equipment_weight", 1.0, 1.0, 1024.0).setTracked(true);
    public static final EntityAttribute HEALTH_REGENERATION = new ClampedEntityAttribute("attribute.name.generic.health_regeneration", 0.0, -1024.0, 1024.0).setTracked(true);
    public static final EntityAttribute MANA_REGENERATION = new ClampedEntityAttribute("attribute.name.generic.mana_regeneration", 0.0, -1024.0, 1024.0).setTracked(true);
    public static final EntityAttribute STAMINA_REGENERATION = new ClampedEntityAttribute("attribute.name.generic.stamina_regeneration", 0.0, -1024.0, 1024.0).setTracked(true);
    public static final EntityAttribute MAX_MANA = new ClampedEntityAttribute("attribute.name.generic.max_mana", 0.0, 0.0, 1024.0).setTracked(true);
    public static final EntityAttribute MAX_STAMINA = new ClampedEntityAttribute("attribute.name.generic.max_stamina", 0.0, 0.0, 1024.0).setTracked(true);

    public static final EntityAttribute ADDITIONAL_BASHING_DAMAGE = new ClampedEntityAttribute("attribute.name.generic.additional_bashing_damage", 0.0, -1024.0, 1024.0).setTracked(true);
    public static final EntityAttribute INCREASED_BASHING_DAMAGE = new ClampedEntityAttribute("attribute.name.generic.increased_bashing_damage", 1.0, -1024.0, 1024.0).setTracked(true);
    public static final EntityAttribute ADDITIONAL_PIERCING_DAMAGE = new ClampedEntityAttribute("attribute.name.generic.additional_piercing_damage", 0.0, -1024.0, 1024.0).setTracked(true);
    public static final EntityAttribute INCREASED_PIERCING_DAMAGE = new ClampedEntityAttribute("attribute.name.generic.increased_piercing_damage", 1.0, -1024.0, 1024.0).setTracked(true);
    public static final EntityAttribute ADDITIONAL_SLASHING_DAMAGE = new ClampedEntityAttribute("attribute.name.generic.additional_slashing_damage", 0.0, -1024.0, 1024.0).setTracked(true);
    public static final EntityAttribute INCREASED_SLASHING_DAMAGE = new ClampedEntityAttribute("attribute.name.generic.increased_slashing_damage", 1.0, -1024.0, 1024.0).setTracked(true);
    public static final EntityAttribute ADDITIONAL_FROST_DAMAGE = new ClampedEntityAttribute("attribute.name.generic.additional_frost_damage", 0.0, -1024.0, 1024.0).setTracked(true);
    public static final EntityAttribute INCREASED_FROST_DAMAGE = new ClampedEntityAttribute("attribute.name.generic.increased_frost_damage", 1.0, -1024.0, 1024.0).setTracked(true);
    public static final EntityAttribute FROST_RESISTANCE = new ClampedEntityAttribute("attribute.name.generic.frost_resistance", 0.0, -1024.0, 1024.0).setTracked(true);
    public static final EntityAttribute ADDITIONAL_FIRE_DAMAGE = new ClampedEntityAttribute("attribute.name.generic.additional_fire_damage", 0.0, -1024.0, 1024.0).setTracked(true);
    public static final EntityAttribute INCREASED_FIRE_DAMAGE = new ClampedEntityAttribute("attribute.name.generic.increased_fire_damage", 1.0, -1024.0, 1024.0).setTracked(true);
    public static final EntityAttribute FIRE_RESISTANCE = new ClampedEntityAttribute("attribute.name.generic.fire_resistance", 0.0, -1024.0, 1024.0).setTracked(true);
    public static final EntityAttribute ADDITIONAL_LIGHTNING_DAMAGE = new ClampedEntityAttribute("attribute.name.generic.additional_lightning_damage", 0.0, -1024.0, 1024.0).setTracked(true);
    public static final EntityAttribute INCREASED_LIGHTNING_DAMAGE = new ClampedEntityAttribute("attribute.name.generic.increased_lightning_damage", 1.0, -1024.0, 1024.0).setTracked(true);
    public static final EntityAttribute LIGHTNING_RESISTANCE = new ClampedEntityAttribute("attribute.name.generic.lightning_resistance", 0.0, -1024.0, 1024.0).setTracked(true);
    public static final EntityAttribute ADDITIONAL_POISON_DAMAGE = new ClampedEntityAttribute("attribute.name.generic.additional_poison_damage", 0.0, -1024.0, 1024.0).setTracked(true);
    public static final EntityAttribute INCREASED_POISON_DAMAGE = new ClampedEntityAttribute("attribute.name.generic.increased_poison_damage", 1.0, -1024.0, 1024.0).setTracked(true);
    public static final EntityAttribute POISON_RESISTANCE = new ClampedEntityAttribute("attribute.name.generic.poison_resistance", 0.0, -1024.0, 1024.0).setTracked(true);

    public static void registerAttributes() {

        Registry.register(Registries.ATTRIBUTE, BetterAdventureMode.identifier("player.active_spell_slot_amount"), ACTIVE_SPELL_SLOT_AMOUNT);
        Registry.register(Registries.ATTRIBUTE, BetterAdventureMode.identifier("player.max_equipment_weight"), MAX_EQUIPMENT_WEIGHT);
        Registry.register(Registries.ATTRIBUTE, BetterAdventureMode.identifier("player.equipment_weight"), EQUIPMENT_WEIGHT);
        Registry.register(Registries.ATTRIBUTE, BetterAdventureMode.identifier("generic.health_regeneration"), HEALTH_REGENERATION);
        Registry.register(Registries.ATTRIBUTE, BetterAdventureMode.identifier("generic.mana_regeneration"), MANA_REGENERATION);
        Registry.register(Registries.ATTRIBUTE, BetterAdventureMode.identifier("generic.stamina_regeneration"), STAMINA_REGENERATION);
        Registry.register(Registries.ATTRIBUTE, BetterAdventureMode.identifier("generic.max_mana"), MAX_MANA);
        Registry.register(Registries.ATTRIBUTE, BetterAdventureMode.identifier("generic.max_stamina"), MAX_STAMINA);
        Registry.register(Registries.ATTRIBUTE, BetterAdventureMode.identifier("generic.additional_bashing_damage"), ADDITIONAL_BASHING_DAMAGE);
        Registry.register(Registries.ATTRIBUTE, BetterAdventureMode.identifier("generic.increased_bashing_damage"), INCREASED_BASHING_DAMAGE);
        Registry.register(Registries.ATTRIBUTE, BetterAdventureMode.identifier("generic.additional_piercing_damage"), ADDITIONAL_PIERCING_DAMAGE);
        Registry.register(Registries.ATTRIBUTE, BetterAdventureMode.identifier("generic.increased_piercing_damage"), INCREASED_PIERCING_DAMAGE);
        Registry.register(Registries.ATTRIBUTE, BetterAdventureMode.identifier("generic.additional_slashing_damage"), ADDITIONAL_SLASHING_DAMAGE);
        Registry.register(Registries.ATTRIBUTE, BetterAdventureMode.identifier("generic.increased_slashing_damage"), INCREASED_SLASHING_DAMAGE);
        Registry.register(Registries.ATTRIBUTE, BetterAdventureMode.identifier("generic.additional_frost_damage"), ADDITIONAL_FROST_DAMAGE);
        Registry.register(Registries.ATTRIBUTE, BetterAdventureMode.identifier("generic.increased_frost_damage"), INCREASED_FROST_DAMAGE);
        Registry.register(Registries.ATTRIBUTE, BetterAdventureMode.identifier("generic.frost_resistance"), FROST_RESISTANCE);
        Registry.register(Registries.ATTRIBUTE, BetterAdventureMode.identifier("generic.additional_fire_damage"), ADDITIONAL_FIRE_DAMAGE);
        Registry.register(Registries.ATTRIBUTE, BetterAdventureMode.identifier("generic.increased_fire_damage"), INCREASED_FIRE_DAMAGE);
        Registry.register(Registries.ATTRIBUTE, BetterAdventureMode.identifier("generic.fire_resistance"), FIRE_RESISTANCE);
        Registry.register(Registries.ATTRIBUTE, BetterAdventureMode.identifier("generic.additional_lightning_damage"), ADDITIONAL_LIGHTNING_DAMAGE);
        Registry.register(Registries.ATTRIBUTE, BetterAdventureMode.identifier("generic.increased_lightning_damage"), INCREASED_LIGHTNING_DAMAGE);
        Registry.register(Registries.ATTRIBUTE, BetterAdventureMode.identifier("generic.lightning_resistance"), LIGHTNING_RESISTANCE);
        Registry.register(Registries.ATTRIBUTE, BetterAdventureMode.identifier("generic.additional_poison_damage"), ADDITIONAL_POISON_DAMAGE);
        Registry.register(Registries.ATTRIBUTE, BetterAdventureMode.identifier("generic.increased_poison_damage"), INCREASED_POISON_DAMAGE);
        Registry.register(Registries.ATTRIBUTE, BetterAdventureMode.identifier("generic.poison_resistance"), POISON_RESISTANCE);
    }

    public static void registerEntityAttributes() {

        FabricDefaultAttributeRegistry.register(EntityRegistry.SPAWNER_BOUND_ENTITY, SpawnerBoundEntity.createLivingAttributes());
        FabricDefaultAttributeRegistry.register(EntityRegistry.MANNEQUIN_ENTITY, MannequinEntity.createLivingAttributes());
    }
}
