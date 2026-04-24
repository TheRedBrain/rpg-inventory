package com.github.theredbrain.rpginventory.registry;

import com.github.theredbrain.rpginventory.RPGInventory;
import com.github.theredbrain.rpginventory.RPGInventoryClient;
import com.github.theredbrain.rpginventory.network.packet.SheatheWeaponsPacket;
import com.github.theredbrain.rpginventory.network.packet.SwapHandItemsPacket;
import com.github.theredbrain.rpginventory.network.packet.ToggleTwoHandedStancePacket;
import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import org.lwjgl.glfw.GLFW;

public class KeyBindingsRegistry {

	public static KeyMapping sheatheWeapons;
	public static KeyMapping toggleTwoHandedStance;
	public static KeyMapping swapHand;
	public static KeyMapping swapOffHand;
	public static KeyMapping swapBothHands;
	public static boolean sheatheWeaponsBoolean;
	public static boolean toggleTwoHandedStanceBoolean;
	public static boolean swapHandBoolean;
	public static boolean swapOffHandBoolean;
	public static boolean swapBothHandsBoolean;
	public static KeyMapping.Category RPG_INVENTORY;

	public static void registerKeyBindings() {
		KeyBindingsRegistry.sheatheWeapons = KeyMappingHelper.registerKeyMapping(new KeyMapping(
				"key.rpginventory.sheatheWeapons",
				InputConstants.Type.KEYSYM,
				GLFW.GLFW_KEY_G,
				RPG_INVENTORY
		));
		KeyBindingsRegistry.toggleTwoHandedStance = KeyMappingHelper.registerKeyMapping(new KeyMapping(
				"key.rpginventory.toggleTwoHandedStance",
				InputConstants.Type.KEYSYM,
				GLFW.GLFW_KEY_H,
				RPG_INVENTORY
		));
		KeyBindingsRegistry.swapHand = KeyMappingHelper.registerKeyMapping(new KeyMapping(
				"key.rpginventory.swapHand",
				InputConstants.Type.KEYSYM,
				GLFW.GLFW_KEY_X,
				RPG_INVENTORY
		));
		KeyBindingsRegistry.swapOffHand = KeyMappingHelper.registerKeyMapping(new KeyMapping(
				"key.rpginventory.swapOffHand",
				InputConstants.Type.KEYSYM,
				GLFW.GLFW_KEY_Y,
				RPG_INVENTORY
		));
		KeyBindingsRegistry.swapBothHands = KeyMappingHelper.registerKeyMapping(new KeyMapping(
				"key.rpginventory.swapBothHands",
				InputConstants.Type.KEYSYM,
				GLFW.GLFW_KEY_C,
				RPG_INVENTORY
		));
		ClientTickEvents.END_CLIENT_TICK.register(minecraft -> {
			if (KeyBindingsRegistry.swapHand.isDown()) {
				if (!swapHandBoolean) {
					syncSlotSwapHand(minecraft, true, false);
				}
				swapHandBoolean = true;
			} else if (swapHandBoolean) {
				swapHandBoolean = false;
			}
			if (KeyBindingsRegistry.swapOffHand.isDown()) {
				if (!swapOffHandBoolean) {
					syncSlotSwapHand(minecraft, false, true);
				}
				swapOffHandBoolean = true;
			} else if (swapOffHandBoolean) {
				swapOffHandBoolean = false;
			}
			if (KeyBindingsRegistry.swapBothHands.isDown()) {
				if (!swapBothHandsBoolean) {
					syncSlotSwapHand(minecraft, true, true);
				}
				swapBothHandsBoolean = true;
			} else if (swapBothHandsBoolean) {
				swapBothHandsBoolean = false;
			}
			if (KeyBindingsRegistry.sheatheWeapons.isDown()) {
				if (!sheatheWeaponsBoolean) {
					sheatheWeapons(minecraft);
				}
				sheatheWeaponsBoolean = true;
			} else if (sheatheWeaponsBoolean) {
				sheatheWeaponsBoolean = false;
			}
			if (KeyBindingsRegistry.toggleTwoHandedStance.isDown()) {
				if (!toggleTwoHandedStanceBoolean) {
					toggleTwoHandedStance(minecraft);
				}
				toggleTwoHandedStanceBoolean = true;
			} else if (toggleTwoHandedStanceBoolean) {
				toggleTwoHandedStanceBoolean = false;
			}
		});
	}

	public static void sheatheWeapons(Minecraft minecraft) {
		if (RPGInventoryClient.doesCurrentPlayerStatusPreventHandSlotAction(minecraft)) {
			if (minecraft.player != null) {
				minecraft.player.sendOverlayMessage(Component.translatable("hud.message.handSlotActionWasPrevented"));
				return;
			}
		}
		ClientPlayNetworking.send(new SheatheWeaponsPacket());
	}

	public static void toggleTwoHandedStance(Minecraft minecraft) {
		if (RPGInventoryClient.doesCurrentPlayerStatusPreventHandSlotAction(Minecraft.getInstance())) {
			if (minecraft.player != null) {
				minecraft.player.sendOverlayMessage(Component.translatable("hud.message.handSlotActionWasPrevented"));
				return;
			}
		}
		ClientPlayNetworking.send(new ToggleTwoHandedStancePacket());
	}

	public static void syncSlotSwapHand(Minecraft minecraft, boolean mainHand, boolean offHand) {
		if (RPGInventoryClient.doesCurrentPlayerStatusPreventHandSlotAction(Minecraft.getInstance())) {
			if (minecraft.player != null) {
				minecraft.player.sendOverlayMessage(Component.translatable("hud.message.handSlotActionWasPrevented"));
				return;
			}
		}
		ClientPlayNetworking.send(new SwapHandItemsPacket(mainHand, offHand));
	}

	static {
		RPG_INVENTORY = KeyMapping.Category.register(RPGInventory.identifier("key_binding_category"));
	}

}
