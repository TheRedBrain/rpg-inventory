package com.github.theredbrain.rpginventory.compat;

import com.github.theredbrain.overhauleddamage.entity.DataAttachmentHelper;
import net.minecraft.world.entity.player.Player;

public class OverhauledDamageCompat {

	public static void resetPlayerStatus(Player playerEntity) {
		DataAttachmentHelper.setBleedingBuildUp(playerEntity, 0.0F);
		DataAttachmentHelper.setBurnBuildUp(playerEntity, 0.0F);
		DataAttachmentHelper.setFreezeBuildUp(playerEntity, 0.0F);
		DataAttachmentHelper.setPoisonBuildUp(playerEntity, 0.0F);
		DataAttachmentHelper.setShockBuildUp(playerEntity, 0.0F);
		DataAttachmentHelper.setStaggerBuildUp(playerEntity, 0.0F);
	}
}
