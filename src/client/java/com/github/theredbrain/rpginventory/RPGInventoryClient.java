package com.github.theredbrain.rpginventory;

import com.github.theredbrain.rpginventory.compat.BackpackAttributeClientCompat;
import com.github.theredbrain.rpginventory.compat.BetterCombatClientCompat;
import com.github.theredbrain.rpginventory.compat.CombatRollClientCompat;
import com.github.theredbrain.rpginventory.compat.InventorySizeAttributesClientCompat;
import com.github.theredbrain.rpginventory.compat.NumismaticOverhaulClientCompat;
import com.github.theredbrain.rpginventory.compat.PlayerAttributeScreenClientCompat;
import com.github.theredbrain.rpginventory.compat.RPGCraftingClientCompat;
import com.github.theredbrain.rpginventory.compat.TrinketsClientCompat;
import com.github.theredbrain.rpginventory.config.ClientConfig;
import com.github.theredbrain.rpginventory.gui.screen.ingame.RPGInventoryScreen;
import com.github.theredbrain.rpginventory.gui.screen.ingame.RPGMannequinScreen;
import com.github.theredbrain.rpginventory.gui.screen.ingame.VanillaMannequinScreen;
import com.github.theredbrain.rpginventory.registry.ClientEventsRegistry;
import com.github.theredbrain.rpginventory.registry.ClientPacketRegistry;
import com.github.theredbrain.rpginventory.registry.KeyBindingsRegistry;
import com.github.theredbrain.rpginventory.registry.ScreenHandlerTypesRegistry;
import me.fzzyhmstrs.fzzy_config.api.ConfigApiJava;
import me.fzzyhmstrs.fzzy_config.api.RegisterType;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Player;
import org.apache.commons.lang3.tuple.MutablePair;

import java.util.ArrayList;
import java.util.List;


public class RPGInventoryClient implements ClientModInitializer {
	public static ClientConfig CLIENT_CONFIG;

	public static boolean doesCurrentPlayerStatusPreventHandSlotAction(Minecraft minecraftClient) {
		boolean bl = false;
		if (RPGInventory.isBetterCombatLoaded) {
			bl = BetterCombatClientCompat.doesCurrentPlayerStatusPreventHandSlotAction(minecraftClient);
		}
		if (RPGInventory.isCombatRollLoaded) {
			bl = bl || CombatRollClientCompat.doesCurrentPlayerStatusPreventHandSlotAction(minecraftClient);
		}

		return bl;
	}

	/**
	 * @return Returns the X value of the start position of the hotbar HUD element
	 */
	public static int drawAlternativeHotbar(GuiGraphicsExtractor context, Player player, Identifier hotbarTexture) {
		int activeHotbarSize = RPGInventory.getActiveHotbarSize(player);
		if (activeHotbarSize < 9 && RPGInventory.isInventorySizeAttributesLoaded) {
			return InventorySizeAttributesClientCompat.drawAlternativeHotbar(context, player, hotbarTexture);
		} else {
			context.blitSprite(RenderPipelines.GUI_TEXTURED, hotbarTexture, context.guiWidth() / 2 - 91, context.guiHeight() - 22, 182, 22);
			return context.guiWidth() / 2 - 91;
		}
	}

	public static boolean showInactiveInventorySlots() {
		return !RPGInventory.isInventorySizeAttributesLoaded || InventorySizeAttributesClientCompat.showInactiveInventorySlots();
	}

	public static List<MutablePair<Component, List<Component>>> getPlayerAttributeScreenData(Minecraft client) {
		List<MutablePair<Component, List<Component>>> newData = new ArrayList<>(List.of());
		if (RPGInventory.isPlayerAttributeScreenLoaded) {
			newData.addAll(PlayerAttributeScreenClientCompat.getPlayerAttributeScreenData(client));
		}
		return newData;
	}

	public static void openBackPackScreen(Minecraft client) {
		if (RPGInventory.isBackpackAttributeLoaded) {
			BackpackAttributeClientCompat.openBackpackScreen(client);
		} else if (client.player != null) {
			client.player.sendSystemMessage(Component.translatable("hud.message.backpackAttributesNotInstalled"));
		}
	}

	public static void openHandCraftingScreen(Minecraft client) {
		if (RPGInventory.isRPGCraftingLoaded) {
			RPGCraftingClientCompat.openHandCraftingScreen(client);
		} else if (client.player != null) {
			client.player.sendSystemMessage(Component.translatable("hud.message.rpgCraftingNotInstalled"));
		}
	}

	public static void openRPGInventoryScreen(Minecraft client, Player player) {
		if (RPGInventory.isTrinketsLoaded) {
			TrinketsClientCompat.openRPGInventoryTrinketsScreen(client, player);
		} else {
			client.setScreen(new RPGInventoryScreen(player));
		}
	}

	@Override
	public void onInitializeClient() {
		CLIENT_CONFIG = ConfigApiJava.registerAndLoadConfig(ClientConfig::new, RegisterType.CLIENT);

		// Packets
		ClientPacketRegistry.init();

		// Registry
		ClientEventsRegistry.initializeClientEvents();
		KeyBindingsRegistry.registerKeyBindings();
		HandledScreens.register(ScreenHandlerTypesRegistry.RPG_MANNEQUIN_SCREEN_HANDLER, RPGMannequinScreen::new);
		HandledScreens.register(ScreenHandlerTypesRegistry.VANILLA_MANNEQUIN_SCREEN_HANDLER, VanillaMannequinScreen::new);
		if (RPGInventory.isNumismaticOverhaulLoaded && RPGInventory.isOwoLibLoaded) {
			NumismaticOverhaulClientCompat.init();
		}
	}
}
