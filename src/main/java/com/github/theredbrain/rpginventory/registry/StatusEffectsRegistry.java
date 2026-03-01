package com.github.theredbrain.rpginventory.registry;

import com.github.theredbrain.rpginventory.RPGInventory;
import com.github.theredbrain.rpginventory.effect.RPGInventoryStatusEffect;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;

public class StatusEffectsRegistry {

	public static final StatusEffect CIVILISATION = new RPGInventoryStatusEffect(StatusEffectCategory.BENEFICIAL);
	public static final StatusEffect KEEP_INVENTORY = new RPGInventoryStatusEffect(StatusEffectCategory.BENEFICIAL);
	public static final StatusEffect NEEDS_TWO_HANDING = new RPGInventoryStatusEffect(StatusEffectCategory.NEUTRAL);
	public static final StatusEffect NO_ATTACK_ITEM = new RPGInventoryStatusEffect(StatusEffectCategory.NEUTRAL);
	public static final StatusEffect WILDERNESS = new RPGInventoryStatusEffect(StatusEffectCategory.HARMFUL);
	public static final StatusEffect PVP = new RPGInventoryStatusEffect(StatusEffectCategory.NEUTRAL);

	public static void init() {
		// --- Attribute Modifiers ---
		RPGInventory.addModdedAttributesToEffects();

		// --- Configuration ---
		RPGInventory.configureEffects();

		// --- Registration ---
		RPGInventory.CIVILISATION = register("civilisation", CIVILISATION);
		RPGInventory.KEEP_INVENTORY = register("keep_inventory", KEEP_INVENTORY);
		RPGInventory.NEEDS_TWO_HANDING = register("needs_two_handing", NEEDS_TWO_HANDING);
		RPGInventory.NO_ATTACK_ITEM = register("no_attack_item", NO_ATTACK_ITEM);
		RPGInventory.WILDERNESS = register("wilderness", WILDERNESS);
		RPGInventory.PVP = register("pvp", PVP);
	}

	private static RegistryEntry<StatusEffect> register(String identifierString, StatusEffect statusEffect) {
		return Registry.registerReference(Registries.STATUS_EFFECT, RPGInventory.identifier(identifierString), statusEffect);
	}
}
