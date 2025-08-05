package com.github.theredbrain.rpginventory.client.compat;

import com.github.theredbrain.backpackattribute.BackpackAttributeClient;
import com.github.theredbrain.rpgcrafting.RPGCraftingClient;
import net.minecraft.client.MinecraftClient;

public class BackpackAttributeClientCompat {

	public static void openBackpackScreen(MinecraftClient client) {
		BackpackAttributeClient.openBackpackScreen(client);
	}

}
