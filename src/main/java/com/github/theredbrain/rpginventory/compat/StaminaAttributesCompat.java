package com.github.theredbrain.rpginventory.compat;

import com.github.theredbrain.rpginventory.RPGInventory;
import com.github.theredbrain.rpginventory.config.ServerConfig;
import com.github.theredbrain.rpginventory.registry.StatusEffectsRegistry;
import com.github.theredbrain.staminaattributes.StaminaAttributes;
import com.github.theredbrain.staminaattributes.entity.StaminaUsingEntity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

public class StaminaAttributesCompat {
	public static float getCurrentStamina(LivingEntity livingEntity) {
		return ((StaminaUsingEntity) livingEntity).staminaattributes$getStamina();
	}

	public static void addStamina(LivingEntity livingEntity, float amount) {
		((StaminaUsingEntity) livingEntity).staminaattributes$addStamina(amount);
	}

	public static void resetStamina(LivingEntity livingEntity) {
		((StaminaUsingEntity) livingEntity).staminaattributes$setDelayedMaxValueApplication(true);
	}

	public static void addAttributesToStatusEffects() {
		ServerConfig serverConfig = RPGInventory.SERVER_CONFIG;
		StatusEffectsRegistry.CIVILISATION
				.addAttributeModifier(StaminaAttributes.STAMINA_REGENERATION, RPGInventory.identifier("effect.civilisation_effect"), serverConfig.statusEffects.civilisationSection.additional_stamina_regeneration.get(), AttributeModifier.Operation.ADD_VALUE)
		;
	}
}
