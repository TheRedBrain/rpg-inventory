package com.github.theredbrain.betteradventuremode.registry;

import com.github.theredbrain.betteradventuremode.BetterAdventureMode;
import com.github.theredbrain.betteradventuremode.entity.decoration.MannequinEntity;
import com.github.theredbrain.betteradventuremode.entity.mob.SpawnerBoundEntity;
import com.github.theredbrain.betteradventuremode.entity.passive.SpawnerBoundVillagerEntity;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.entity.attribute.ClampedEntityAttribute;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class EntityAttributesRegistry {

    public static final EntityAttribute ACTIVE_SPELL_SLOT_AMOUNT = new ClampedEntityAttribute("attribute.name.player.active_spell_slot_amount", 0.0, 0.0, 8.0).setTracked(true);
    public static final EntityAttribute EQUIPMENT_WEIGHT = new ClampedEntityAttribute("attribute.name.player.equipment_weight", 0.0, 0.0, 1024.0).setTracked(true);
    public static final EntityAttribute MAX_EQUIPMENT_WEIGHT = new ClampedEntityAttribute("attribute.name.player.max_equipment_weight", 10.0, 1.0, 1024.0).setTracked(true);
    public static final EntityAttribute MAX_FOOD_EFFECTS = new ClampedEntityAttribute("attribute.name.player.max_food_effects", 3.0, 0.0, 1024.0).setTracked(true);
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

    public static final EntityAttribute MAX_BLEEDING_BUILD_UP = new ClampedEntityAttribute("attribute.name.generic.max_bleeding_build_up", 20.0, -1.0, 1024.0).setTracked(true);
    public static final EntityAttribute BLEEDING_DURATION = new ClampedEntityAttribute("attribute.name.generic.bleeding_duration", 201.0, 1.0, 1000000.0).setTracked(true);
    public static final EntityAttribute BLEEDING_TICK_THRESHOLD = new ClampedEntityAttribute("attribute.name.generic.bleeding_tick_threshold", 20.0, 0.0, 1024.0).setTracked(true);
    public static final EntityAttribute BLEEDING_BUILD_UP_REDUCTION = new ClampedEntityAttribute("attribute.name.generic.bleeding_build_up_reduction", 1.0, 0.0, 1024.0).setTracked(true);
    
    public static final EntityAttribute ADDITIONAL_FROST_DAMAGE = new ClampedEntityAttribute("attribute.name.generic.additional_frost_damage", 0.0, -1024.0, 1024.0).setTracked(true);
    public static final EntityAttribute INCREASED_FROST_DAMAGE = new ClampedEntityAttribute("attribute.name.generic.increased_frost_damage", 1.0, -1024.0, 1024.0).setTracked(true);
    public static final EntityAttribute FROST_RESISTANCE = new ClampedEntityAttribute("attribute.name.generic.frost_resistance", 0.0, -1024.0, 1024.0).setTracked(true);
    public static final EntityAttribute MAX_FREEZE_BUILD_UP = new ClampedEntityAttribute("attribute.name.generic.max_freeze_build_up", 20.0, -1.0, 1024.0).setTracked(true);
    public static final EntityAttribute FREEZE_DURATION = new ClampedEntityAttribute("attribute.name.generic.freeze_duration", 200.0, 1.0, 1000000.0).setTracked(true);
    public static final EntityAttribute FREEZE_TICK_THRESHOLD = new ClampedEntityAttribute("attribute.name.generic.freeze_tick_threshold", 20.0, 0.0, 1024.0).setTracked(true);
    public static final EntityAttribute FREEZE_BUILD_UP_REDUCTION = new ClampedEntityAttribute("attribute.name.generic.freeze_build_up_reduction", 1.0, 0.0, 1024.0).setTracked(true);

    public static final EntityAttribute ADDITIONAL_FIRE_DAMAGE = new ClampedEntityAttribute("attribute.name.generic.additional_fire_damage", 0.0, -1024.0, 1024.0).setTracked(true);
    public static final EntityAttribute INCREASED_FIRE_DAMAGE = new ClampedEntityAttribute("attribute.name.generic.increased_fire_damage", 1.0, -1024.0, 1024.0).setTracked(true);
    public static final EntityAttribute FIRE_RESISTANCE = new ClampedEntityAttribute("attribute.name.generic.fire_resistance", 0.0, -1024.0, 1024.0).setTracked(true);
    public static final EntityAttribute MAX_BURN_BUILD_UP = new ClampedEntityAttribute("attribute.name.generic.max_burn_build_up", 20.0, -1.0, 1024.0).setTracked(true);
    public static final EntityAttribute BURN_DURATION = new ClampedEntityAttribute("attribute.name.generic.burn_duration", 351.0, 1.0, 1000000.0).setTracked(true);
    public static final EntityAttribute BURN_TICK_THRESHOLD = new ClampedEntityAttribute("attribute.name.generic.burn_tick_threshold", 20.0, 0.0, 1024.0).setTracked(true);
    public static final EntityAttribute BURN_BUILD_UP_REDUCTION = new ClampedEntityAttribute("attribute.name.generic.burn_build_up_reduction", 1.0, 0.0, 1024.0).setTracked(true);

    public static final EntityAttribute ADDITIONAL_LIGHTNING_DAMAGE = new ClampedEntityAttribute("attribute.name.generic.additional_lightning_damage", 0.0, -1024.0, 1024.0).setTracked(true);
    public static final EntityAttribute INCREASED_LIGHTNING_DAMAGE = new ClampedEntityAttribute("attribute.name.generic.increased_lightning_damage", 1.0, -1024.0, 1024.0).setTracked(true);
    public static final EntityAttribute LIGHTNING_RESISTANCE = new ClampedEntityAttribute("attribute.name.generic.lightning_resistance", 0.0, -1024.0, 1024.0).setTracked(true);
    public static final EntityAttribute MAX_SHOCK_BUILD_UP = new ClampedEntityAttribute("attribute.name.generic.max_shock_build_up", 20.0, -1.0, 1024.0).setTracked(true);
    public static final EntityAttribute SHOCK_DURATION = new ClampedEntityAttribute("attribute.name.generic.shock_duration", 1.0, 1.0, 1000000.0).setTracked(true);
    public static final EntityAttribute SHOCK_TICK_THRESHOLD = new ClampedEntityAttribute("attribute.name.generic.shock_tick_threshold", 20.0, 0.0, 1024.0).setTracked(true);
    public static final EntityAttribute SHOCK_BUILD_UP_REDUCTION = new ClampedEntityAttribute("attribute.name.generic.shock_build_up_reduction", 1.0, 0.0, 1024.0).setTracked(true);

    public static final EntityAttribute ADDITIONAL_POISON_DAMAGE = new ClampedEntityAttribute("attribute.name.generic.additional_poison_damage", 0.0, -1024.0, 1024.0).setTracked(true);
    public static final EntityAttribute INCREASED_POISON_DAMAGE = new ClampedEntityAttribute("attribute.name.generic.increased_poison_damage", 1.0, -1024.0, 1024.0).setTracked(true);
    public static final EntityAttribute POISON_RESISTANCE = new ClampedEntityAttribute("attribute.name.generic.poison_resistance", 0.0, -1024.0, 1024.0).setTracked(true);
    public static final EntityAttribute MAX_POISON_BUILD_UP = new ClampedEntityAttribute("attribute.name.generic.max_poison_build_up", 20.0, -1.0, 1024.0).setTracked(true);
    public static final EntityAttribute POISON_DURATION = new ClampedEntityAttribute("attribute.name.generic.poison_duration", 201.0, 1.0, 1000000.0).setTracked(true);
    public static final EntityAttribute POISON_TICK_THRESHOLD = new ClampedEntityAttribute("attribute.name.generic.poison_tick_threshold", 20.0, 0.0, 1024.0).setTracked(true);
    public static final EntityAttribute POISON_BUILD_UP_REDUCTION = new ClampedEntityAttribute("attribute.name.generic.poison_build_up_reduction", 1.0, 0.0, 1024.0).setTracked(true);

    public static final EntityAttribute MAX_STAGGER_BUILD_UP = new ClampedEntityAttribute("attribute.name.generic.max_stagger_build_up", 20.0, -1.0, 1024.0).setTracked(true);
    public static final EntityAttribute STAGGER_DURATION = new ClampedEntityAttribute("attribute.name.generic.stagger_duration", 200.0, 1.0, 1000000.0).setTracked(true);
    public static final EntityAttribute STAGGER_TICK_THRESHOLD = new ClampedEntityAttribute("attribute.name.generic.stagger_tick_threshold", 20.0, 0.0, 1024.0).setTracked(true);
    public static final EntityAttribute STAGGER_BUILD_UP_REDUCTION = new ClampedEntityAttribute("attribute.name.generic.stagger_build_up_reduction", 1.0, 0.0, 1024.0).setTracked(true);
    
    public static void registerAttributes() {

        Registry.register(Registries.ATTRIBUTE, BetterAdventureMode.identifier("player.active_spell_slot_amount"), ACTIVE_SPELL_SLOT_AMOUNT);
        Registry.register(Registries.ATTRIBUTE, BetterAdventureMode.identifier("player.equipment_weight"), EQUIPMENT_WEIGHT);
        Registry.register(Registries.ATTRIBUTE, BetterAdventureMode.identifier("player.max_equipment_weight"), MAX_EQUIPMENT_WEIGHT);
        Registry.register(Registries.ATTRIBUTE, BetterAdventureMode.identifier("player.max_food_effects"), MAX_FOOD_EFFECTS);
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
        
        Registry.register(Registries.ATTRIBUTE, BetterAdventureMode.identifier("generic.max_bleeding_build_up"), MAX_BLEEDING_BUILD_UP);
        Registry.register(Registries.ATTRIBUTE, BetterAdventureMode.identifier("generic.bleeding_duration"), BLEEDING_DURATION);
        Registry.register(Registries.ATTRIBUTE, BetterAdventureMode.identifier("generic.bleeding_tick_threshold"), BLEEDING_TICK_THRESHOLD);
        Registry.register(Registries.ATTRIBUTE, BetterAdventureMode.identifier("generic.bleeding_build_up_reduction"), BLEEDING_BUILD_UP_REDUCTION);
        
        Registry.register(Registries.ATTRIBUTE, BetterAdventureMode.identifier("generic.additional_frost_damage"), ADDITIONAL_FROST_DAMAGE);
        Registry.register(Registries.ATTRIBUTE, BetterAdventureMode.identifier("generic.increased_frost_damage"), INCREASED_FROST_DAMAGE);
        Registry.register(Registries.ATTRIBUTE, BetterAdventureMode.identifier("generic.frost_resistance"), FROST_RESISTANCE);
        Registry.register(Registries.ATTRIBUTE, BetterAdventureMode.identifier("generic.max_freeze_build_up"), MAX_FREEZE_BUILD_UP);
        Registry.register(Registries.ATTRIBUTE, BetterAdventureMode.identifier("generic.freeze_duration"), FREEZE_DURATION);
        Registry.register(Registries.ATTRIBUTE, BetterAdventureMode.identifier("generic.freeze_tick_threshold"), FREEZE_TICK_THRESHOLD);
        Registry.register(Registries.ATTRIBUTE, BetterAdventureMode.identifier("generic.freeze_build_up_reduction"), FREEZE_BUILD_UP_REDUCTION);
        
        Registry.register(Registries.ATTRIBUTE, BetterAdventureMode.identifier("generic.additional_fire_damage"), ADDITIONAL_FIRE_DAMAGE);
        Registry.register(Registries.ATTRIBUTE, BetterAdventureMode.identifier("generic.increased_fire_damage"), INCREASED_FIRE_DAMAGE);
        Registry.register(Registries.ATTRIBUTE, BetterAdventureMode.identifier("generic.fire_resistance"), FIRE_RESISTANCE);
        Registry.register(Registries.ATTRIBUTE, BetterAdventureMode.identifier("generic.max_burn_build_up"), MAX_BURN_BUILD_UP);
        Registry.register(Registries.ATTRIBUTE, BetterAdventureMode.identifier("generic.burn_duration"), BURN_DURATION);
        Registry.register(Registries.ATTRIBUTE, BetterAdventureMode.identifier("generic.burn_tick_threshold"), BURN_TICK_THRESHOLD);
        Registry.register(Registries.ATTRIBUTE, BetterAdventureMode.identifier("generic.burn_build_up_reduction"), BURN_BUILD_UP_REDUCTION);
        
        Registry.register(Registries.ATTRIBUTE, BetterAdventureMode.identifier("generic.additional_lightning_damage"), ADDITIONAL_LIGHTNING_DAMAGE);
        Registry.register(Registries.ATTRIBUTE, BetterAdventureMode.identifier("generic.increased_lightning_damage"), INCREASED_LIGHTNING_DAMAGE);
        Registry.register(Registries.ATTRIBUTE, BetterAdventureMode.identifier("generic.lightning_resistance"), LIGHTNING_RESISTANCE);
        Registry.register(Registries.ATTRIBUTE, BetterAdventureMode.identifier("generic.max_shock_build_up"), MAX_SHOCK_BUILD_UP);
        Registry.register(Registries.ATTRIBUTE, BetterAdventureMode.identifier("generic.shock_duration"), SHOCK_DURATION);
        Registry.register(Registries.ATTRIBUTE, BetterAdventureMode.identifier("generic.shock_tick_threshold"), SHOCK_TICK_THRESHOLD);
        Registry.register(Registries.ATTRIBUTE, BetterAdventureMode.identifier("generic.shock_build_up_reduction"), SHOCK_BUILD_UP_REDUCTION);
        
        Registry.register(Registries.ATTRIBUTE, BetterAdventureMode.identifier("generic.additional_poison_damage"), ADDITIONAL_POISON_DAMAGE);
        Registry.register(Registries.ATTRIBUTE, BetterAdventureMode.identifier("generic.increased_poison_damage"), INCREASED_POISON_DAMAGE);
        Registry.register(Registries.ATTRIBUTE, BetterAdventureMode.identifier("generic.poison_resistance"), POISON_RESISTANCE);
        Registry.register(Registries.ATTRIBUTE, BetterAdventureMode.identifier("generic.max_poison_build_up"), MAX_POISON_BUILD_UP);
        Registry.register(Registries.ATTRIBUTE, BetterAdventureMode.identifier("generic.poison_duration"), POISON_DURATION);
        Registry.register(Registries.ATTRIBUTE, BetterAdventureMode.identifier("generic.poison_tick_threshold"), POISON_TICK_THRESHOLD);
        Registry.register(Registries.ATTRIBUTE, BetterAdventureMode.identifier("generic.poison_build_up_reduction"), POISON_BUILD_UP_REDUCTION);

        Registry.register(Registries.ATTRIBUTE, BetterAdventureMode.identifier("generic.max_stagger_build_up"), MAX_STAGGER_BUILD_UP);
        Registry.register(Registries.ATTRIBUTE, BetterAdventureMode.identifier("generic.stagger_duration"), STAGGER_DURATION);
        Registry.register(Registries.ATTRIBUTE, BetterAdventureMode.identifier("generic.stagger_tick_threshold"), STAGGER_TICK_THRESHOLD);
        Registry.register(Registries.ATTRIBUTE, BetterAdventureMode.identifier("generic.stagger_build_up_reduction"), STAGGER_BUILD_UP_REDUCTION);
    }

    public static void registerEntityAttributes() {

        FabricDefaultAttributeRegistry.register(EntityRegistry.SPAWNER_BOUND_ENTITY, SpawnerBoundEntity.createLivingAttributes());
        FabricDefaultAttributeRegistry.register(EntityRegistry.SPAWNER_BOUND_VILLAGER_ENTITY, SpawnerBoundVillagerEntity.createMobAttributes());
        FabricDefaultAttributeRegistry.register(EntityRegistry.MANNEQUIN_ENTITY, MannequinEntity.createLivingAttributes());
    }
}
