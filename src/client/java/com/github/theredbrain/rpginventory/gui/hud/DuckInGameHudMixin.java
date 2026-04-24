package com.github.theredbrain.rpginventory.gui.hud;

import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public interface DuckInGameHudMixin {
	@Nullable
	Player rpginventory$cameraPlayerAccessor();

	Minecraft rpginventory$clientAccessor();

	void rpginventory$extractSlot_Invoker(final GuiGraphicsExtractor graphics, final int x, final int y, final DeltaTracker deltaTracker, final Player player, final ItemStack itemStack, final int seed);
}
