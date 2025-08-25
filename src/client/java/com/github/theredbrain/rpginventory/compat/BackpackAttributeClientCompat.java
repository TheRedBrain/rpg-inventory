package com.github.theredbrain.rpginventory.compat;

import com.github.theredbrain.backpackattribute.BackpackAttributeClient;
import net.minecraft.client.MinecraftClient;

public class BackpackAttributeClientCompat {

	public static void openBackpackScreen(MinecraftClient client) {
		BackpackAttributeClient.openBackpackScreen(client);
	}

}
