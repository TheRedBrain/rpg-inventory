package com.github.theredbrain.rpginventory.compat;

import com.github.theredbrain.overhauleddamage.entity.DuckLivingEntityMixin;
import net.minecraft.entity.player.PlayerEntity;

public class OverhauledDamageCompat {

	public static void resetPlayerStatus(PlayerEntity playerEntity) {
		((DuckLivingEntityMixin) playerEntity).overhauleddamage$setBleedingBuildUp(0.0F);
		((DuckLivingEntityMixin) playerEntity).overhauleddamage$setBurnBuildUp(0.0F);
		((DuckLivingEntityMixin) playerEntity).overhauleddamage$setFreezeBuildUp(0.0F);
		((DuckLivingEntityMixin) playerEntity).overhauleddamage$setPoisonBuildUp(0.0F);
		((DuckLivingEntityMixin) playerEntity).overhauleddamage$setShockBuildUp(0.0F);
		((DuckLivingEntityMixin) playerEntity).overhauleddamage$setStaggerBuildUp(0.0F);
	}
}
