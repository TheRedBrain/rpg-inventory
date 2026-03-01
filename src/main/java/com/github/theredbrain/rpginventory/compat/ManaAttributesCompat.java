package com.github.theredbrain.rpginventory.compat;

import com.github.theredbrain.manaattributes.ManaAttributes;
import com.github.theredbrain.manaattributes.entity.ManaUsingEntity;
import com.github.theredbrain.rpginventory.RPGInventory;
import com.github.theredbrain.rpginventory.config.ServerConfig;
import com.github.theredbrain.rpginventory.registry.StatusEffectsRegistry;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributeModifier;

public class ManaAttributesCompat {
	public static void resetMana(LivingEntity livingEntity) {
		((ManaUsingEntity) livingEntity).manaattributes$setApplyMaxMana(true);
	}

	public static void addAttributesToStatusEffects() {
		ServerConfig serverConfig = RPGInventory.SERVER_CONFIG;
		StatusEffectsRegistry.CIVILISATION
				.addAttributeModifier(ManaAttributes.MANA_REGENERATION, RPGInventory.identifier("effect.civilisation_effect"), serverConfig.statusEffects.civilisationSection.additional_mana_regeneration.get(), EntityAttributeModifier.Operation.ADD_VALUE)
		;
	}
}
