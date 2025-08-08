package com.github.theredbrain.rpginventory;

import com.github.theredbrain.rpginventory.compat.BackpackAttributeClientCompat;
import com.github.theredbrain.rpginventory.compat.InventorySizeAttributesClientCompat;
import com.github.theredbrain.rpginventory.compat.PlayerAttributeScreenClientCompat;
import com.github.theredbrain.rpginventory.compat.RPGCraftingClientCompat;
import com.github.theredbrain.rpginventory.compat.TrinketsClientCompat;
import com.github.theredbrain.rpginventory.config.ClientConfig;
import com.github.theredbrain.rpginventory.gui.screen.ingame.MannequinScreen;
import com.github.theredbrain.rpginventory.gui.screen.ingame.RPGInventoryScreen;
import com.github.theredbrain.rpginventory.registry.ClientEventsRegistry;
import com.github.theredbrain.rpginventory.registry.ClientPacketRegistry;
import com.github.theredbrain.rpginventory.registry.KeyBindingsRegistry;
import com.github.theredbrain.rpginventory.registry.ScreenHandlerTypesRegistry;
import me.fzzyhmstrs.fzzy_config.api.ConfigApiJava;
import me.fzzyhmstrs.fzzy_config.api.RegisterType;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import org.apache.commons.lang3.tuple.MutablePair;

import java.util.ArrayList;
import java.util.List;


public class RPGInventoryClient implements ClientModInitializer {
	public static ClientConfig CLIENT_CONFIG;

	public RPGInventoryClient() {
		CLIENT_CONFIG = ConfigApiJava.registerAndLoadConfig(ClientConfig::new, RegisterType.CLIENT);
	}

	public static boolean showInactiveInventorySlots() {
		return !RPGInventory.isInventorySizeAttributesLoaded || InventorySizeAttributesClientCompat.showInactiveInventorySlots();
	}

	public static List<MutablePair<Text, List<Text>>> getPlayerAttributeScreenData(MinecraftClient client) {
		List<MutablePair<Text, List<Text>>> newData = new ArrayList<>(List.of());
		if (RPGInventory.isPlayerAttributeScreenLoaded) {
			newData.addAll(PlayerAttributeScreenClientCompat.getPlayerAttributeScreenData(client));
		}
		return newData;
	}

	public static void openBackPackScreen(MinecraftClient client) {
		if (RPGInventory.isBackpackAttributeLoaded) {
			BackpackAttributeClientCompat.openBackpackScreen(client);
		} else if (client.player != null) {
			client.player.sendMessage(Text.translatable("hud.message.backpackAttributesNotInstalled"));
		}
	}

	public static void openHandCraftingScreen(MinecraftClient client) {
		if (RPGInventory.isRPGCraftingLoaded) {
			RPGCraftingClientCompat.openHandCraftingScreen(client);
		} else if (client.player != null) {
			client.player.sendMessage(Text.translatable("hud.message.rpgCraftingNotInstalled"));
		}
	}

	public static void openRPGInventoryScreen(MinecraftClient client, PlayerEntity player) {
		if (RPGInventory.isTrinketsLoaded) {
			TrinketsClientCompat.openRPGInventoryTrinketsScreen(client, player);
		} else {
			client.setScreen(new RPGInventoryScreen(player));
		}
	}

	@Override
	public void onInitializeClient() {
		// Packets
		ClientPacketRegistry.init();

		// Registry
		ClientEventsRegistry.initializeClientEvents();
		KeyBindingsRegistry.registerKeyBindings();
		HandledScreens.register(ScreenHandlerTypesRegistry.MANNEQUIN_SCREEN_HANDLER, MannequinScreen::new);
	}
}
