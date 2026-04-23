package com.github.theredbrain.rpginventory.compat;

import com.github.theredbrain.backpackattribute.BackpackAttributeClient;
import net.minecraft.client.Minecraft;

public class BackpackAttributeClientCompat {

	public static void openBackpackScreen(Minecraft client) {
		BackpackAttributeClient.openBackpackScreen(client);
	}

}
