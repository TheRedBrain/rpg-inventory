package com.github.theredbrain.rpginventory.client.compat;

import com.github.theredbrain.backpackattribute.BackpackAttributeClient;
import com.github.theredbrain.playerattributescreen.PlayerAttributeScreenClient;
import com.github.theredbrain.rpginventory.RPGInventory;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import org.apache.commons.lang3.tuple.MutablePair;

import java.util.ArrayList;
import java.util.List;

public class PlayerAttributeScreenClientCompat {

	public static List<MutablePair<Text, List<Text>>> getPlayerAttributeScreenData(MinecraftClient client) {
		return PlayerAttributeScreenClient.getPlayerAttributeScreenData(client);
	}

}
