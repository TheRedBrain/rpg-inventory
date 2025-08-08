package com.github.theredbrain.rpginventory.compat;

import com.github.theredbrain.rpgcrafting.RPGCraftingClient;
import net.minecraft.client.MinecraftClient;

public class RPGCraftingClientCompat {

	public static void openHandCraftingScreen(MinecraftClient client) {
		RPGCraftingClient.openHandCraftingScreen(client);
	}

}
