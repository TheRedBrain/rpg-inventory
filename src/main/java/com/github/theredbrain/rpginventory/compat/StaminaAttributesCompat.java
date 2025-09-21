package com.github.theredbrain.rpginventory.compat;

import com.github.theredbrain.staminaattributes.entity.StaminaUsingEntity;
import net.minecraft.entity.LivingEntity;

public class StaminaAttributesCompat {
	public static float getCurrentStamina(LivingEntity livingEntity) {
		return ((StaminaUsingEntity) livingEntity).staminaattributes$getStamina();
	}

	public static void addStamina(LivingEntity livingEntity, float amount) {
		((StaminaUsingEntity) livingEntity).staminaattributes$addStamina(amount);
	}

	public static void resetStamina(LivingEntity livingEntity) {
		((StaminaUsingEntity) livingEntity).staminaattributes$setApplyMaxStamina(true);
	}
}
