package com.github.theredbrain.rpginventory.registry;

import com.github.theredbrain.rpginventory.RPGInventoryClient;
import com.github.theredbrain.rpginventory.network.packet.SheatheWeaponsPacket;
import com.github.theredbrain.rpginventory.network.packet.SwapHandItemsPacket;
import com.github.theredbrain.rpginventory.network.packet.ToggleTwoHandedStancePacket;
import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
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

	public static void registerKeyBindings() {
		KeyBindingsRegistry.sheatheWeapons = KeyBindingHelper.registerKeyBinding(new KeyMapping(
				"key.rpginventory.sheatheWeapons",
				InputConstants.Type.KEYSYM,
				GLFW.GLFW_KEY_G,
				"category.rpginventory.category"
		));
		KeyBindingsRegistry.toggleTwoHandedStance = KeyBindingHelper.registerKeyBinding(new KeyMapping(
				"key.rpginventory.toggleTwoHandedStance",
				InputConstants.Type.KEYSYM,
				GLFW.GLFW_KEY_H,
				"category.rpginventory.category"
		));
		KeyBindingsRegistry.swapHand = KeyBindingHelper.registerKeyBinding(new KeyMapping(
				"key.rpginventory.swapHand",
				InputConstants.Type.KEYSYM,
				GLFW.GLFW_KEY_X,
				"category.rpginventory.category"
		));
		KeyBindingsRegistry.swapOffHand = KeyBindingHelper.registerKeyBinding(new KeyMapping(
				"key.rpginventory.swapOffHand",
				InputConstants.Type.KEYSYM,
				GLFW.GLFW_KEY_Y,
				"category.rpginventory.category"
		));
		KeyBindingsRegistry.swapBothHands = KeyBindingHelper.registerKeyBinding(new KeyMapping(
				"key.rpginventory.swapBothHands",
				InputConstants.Type.KEYSYM,
				GLFW.GLFW_KEY_C,
				"category.rpginventory.category"
		));
		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			if (KeyBindingsRegistry.swapHand.wasPressed()) {
				if (!swapHandBoolean) {
					syncSlotSwapHand(true, false);
				}
				swapHandBoolean = true;
			} else if (swapHandBoolean) {
				swapHandBoolean = false;
			}
			if (KeyBindingsRegistry.swapOffHand.wasPressed()) {
				if (!swapOffHandBoolean) {
					syncSlotSwapHand(false, true);
				}
				swapOffHandBoolean = true;
			} else if (swapOffHandBoolean) {
				swapOffHandBoolean = false;
			}
			if (KeyBindingsRegistry.swapBothHands.wasPressed()) {
				if (!swapBothHandsBoolean) {
					syncSlotSwapHand(true, true);
				}
				swapBothHandsBoolean = true;
			} else if (swapBothHandsBoolean) {
				swapBothHandsBoolean = false;
			}
			if (KeyBindingsRegistry.sheatheWeapons.wasPressed()) {
				if (!sheatheWeaponsBoolean) {
					sheatheWeapons();
				}
				sheatheWeaponsBoolean = true;
			} else if (sheatheWeaponsBoolean) {
				sheatheWeaponsBoolean = false;
			}
			if (KeyBindingsRegistry.toggleTwoHandedStance.wasPressed()) {
				if (!toggleTwoHandedStanceBoolean) {
					toggleTwoHandedStance();
				}
				toggleTwoHandedStanceBoolean = true;
			} else if (toggleTwoHandedStanceBoolean) {
				toggleTwoHandedStanceBoolean = false;
			}
		});
	}

	public static void sheatheWeapons() {
		if (RPGInventoryClient.doesCurrentPlayerStatusPreventHandSlotAction(Minecraft.getInstance())) {
			if (Minecraft.getInstance().player != null) {
				Minecraft.getInstance().player.displayClientMessage(Component.translatable("hud.message.handSlotActionWasPrevented"), true);
				return;
			}
		}
		ClientPlayNetworking.send(new SheatheWeaponsPacket());
	}

	public static void toggleTwoHandedStance() {
		if (RPGInventoryClient.doesCurrentPlayerStatusPreventHandSlotAction(Minecraft.getInstance())) {
			if (Minecraft.getInstance().player != null) {
				Minecraft.getInstance().player.displayClientMessage(Component.translatable("hud.message.handSlotActionWasPrevented"), true);
				return;
			}
		}
		ClientPlayNetworking.send(new ToggleTwoHandedStancePacket());
	}

	public static void syncSlotSwapHand(boolean mainHand, boolean offHand) {
		if (RPGInventoryClient.doesCurrentPlayerStatusPreventHandSlotAction(Minecraft.getInstance())) {
			if (Minecraft.getInstance().player != null) {
				Minecraft.getInstance().player.displayClientMessage(Component.translatable("hud.message.handSlotActionWasPrevented"), true);
				return;
			}
		}
		ClientPlayNetworking.send(new SwapHandItemsPacket(mainHand, offHand));
	}
}
