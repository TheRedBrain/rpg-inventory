package com.github.theredbrain.rpginventory.compat;

import com.github.theredbrain.healthregenerationoverhaul.HealthRegenerationOverhaul;
import com.github.theredbrain.healthregenerationoverhaul.entity.HealthRegeneratingEntity;
import com.github.theredbrain.rpginventory.RPGInventory;
import com.github.theredbrain.rpginventory.config.ServerConfig;
import com.github.theredbrain.rpginventory.registry.StatusEffectsRegistry;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

public class HealthRegenerationOverhaulCompat {
	public static void resetHealth(LivingEntity livingEntity) {
		((HealthRegeneratingEntity) livingEntity).healthregenerationoverhaul$setApplyMaxHealth(true);
	}

	public static void addAttributesToStatusEffects() {
		ServerConfig serverConfig = RPGInventory.SERVER_CONFIG;
		StatusEffectsRegistry.CIVILISATION
				.addAttributeModifier(HealthRegenerationOverhaul.HEALTH_REGENERATION, RPGInventory.identifier("effect.civilisation_effect"), serverConfig.statusEffects.civilisationSection.additional_health_regeneration.get(), AttributeModifier.Operation.ADD_VALUE)
		;
	}
}
