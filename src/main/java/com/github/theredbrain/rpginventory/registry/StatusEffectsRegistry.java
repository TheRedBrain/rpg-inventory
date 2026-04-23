package com.github.theredbrain.rpginventory.registry;

import com.github.theredbrain.rpginventory.RPGInventory;
import com.github.theredbrain.rpginventory.effect.RPGInventoryStatusEffect;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;

public class StatusEffectsRegistry {

	public static final MobEffect CIVILISATION = new RPGInventoryStatusEffect(MobEffectCategory.BENEFICIAL);
	public static final MobEffect KEEP_INVENTORY = new RPGInventoryStatusEffect(MobEffectCategory.BENEFICIAL);
	public static final MobEffect NEEDS_TWO_HANDING = new RPGInventoryStatusEffect(MobEffectCategory.NEUTRAL);
	public static final MobEffect NO_ATTACK_ITEM = new RPGInventoryStatusEffect(MobEffectCategory.NEUTRAL);
	public static final MobEffect WILDERNESS = new RPGInventoryStatusEffect(MobEffectCategory.HARMFUL);
	public static final MobEffect PVP = new RPGInventoryStatusEffect(MobEffectCategory.NEUTRAL);

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

	private static Holder<MobEffect> register(String identifierString, MobEffect statusEffect) {
		return Registry.registerForHolder(BuiltInRegistries.MOB_EFFECT, RPGInventory.identifier(identifierString), statusEffect);
	}
}
