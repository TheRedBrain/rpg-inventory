package com.github.theredbrain.rpginventory.compat;

import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import org.apache.commons.lang3.tuple.MutablePair;

import java.util.List;

public class PlayerAttributeScreenClientCompat {

	public static List<MutablePair<Component, List<Component>>> getPlayerAttributeScreenData(Minecraft client) {
//		return PlayerAttributeScreenClient.getPlayerAttributeScreenData(client);
		return List.of();
	}

}
