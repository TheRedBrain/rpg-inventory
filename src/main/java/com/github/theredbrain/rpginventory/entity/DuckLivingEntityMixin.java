package com.github.theredbrain.rpginventory.entity;

import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.item.ItemStack;

public interface DuckLivingEntityMixin {

	ItemStack rpginventory$getSheathedItemStackByArm(HumanoidArm arm);

	boolean rpginventory$isHandStackSheathed();

	void rpginventory$setIsHandStackSheathed(boolean isHandStackSheathed);

	boolean rpginventory$isOffhandStackSheathed();

	void rpginventory$setIsOffhandStackSheathed(boolean isOffhandStackSheathed);

}
