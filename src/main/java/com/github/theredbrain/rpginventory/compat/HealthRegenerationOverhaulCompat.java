package com.github.theredbrain.rpginventory.compat;

import com.github.theredbrain.healthregenerationoverhaul.entity.HealthRegeneratingEntity;
import net.minecraft.entity.LivingEntity;

public class HealthRegenerationOverhaulCompat {
	public static void resetHealth(LivingEntity livingEntity) {
		((HealthRegeneratingEntity) livingEntity).healthregenerationoverhaul$setApplyMaxHealth(true);
	}
}
