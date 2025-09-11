package com.github.theredbrain.rpginventory.compat;

import com.github.theredbrain.manaattributes.entity.ManaUsingEntity;
import net.minecraft.entity.LivingEntity;

public class ManaAttributesCompat {
	public static void resetMana(LivingEntity livingEntity) {
		((ManaUsingEntity) livingEntity).manaattributes$setMana(((ManaUsingEntity) livingEntity).manaattributes$getUnreservedMana());
	}
}
