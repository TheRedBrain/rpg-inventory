package com.github.theredbrain.rpginventory.compat;

import com.github.theredbrain.rpginventory.gui.screen.ingame.RPGInventoryTrinketScreen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;

public class TrinketsClientCompat {

	public static void openRPGInventoryTrinketsScreen(MinecraftClient client, PlayerEntity player) {
		client.setScreen(new RPGInventoryTrinketScreen(player));
	}

}
