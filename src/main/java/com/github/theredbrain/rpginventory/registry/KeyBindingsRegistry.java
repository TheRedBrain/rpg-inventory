package com.github.theredbrain.rpginventory.registry;

import com.github.theredbrain.rpginventory.network.packet.SheatheWeaponsPacket;
import com.github.theredbrain.rpginventory.network.packet.SwapHandItemsPacket;
import com.github.theredbrain.rpginventory.network.packet.ToggleTwoHandedStancePacket;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class KeyBindingsRegistry {

	public static KeyBinding sheatheWeapons;
	public static KeyBinding toggleTwoHandedStance;
	public static KeyBinding swapHand;
	public static KeyBinding swapOffHand;
	public static boolean sheatheWeaponsBoolean;
	public static boolean toggleTwoHandedStanceBoolean;
	public static boolean swapHandBoolean;
	public static boolean swapOffHandBoolean;

	public static void registerKeyBindings() {
		KeyBindingsRegistry.sheatheWeapons = KeyBindingHelper.registerKeyBinding(new KeyBinding(
				"key.rpginventory.sheatheWeapons",
				InputUtil.Type.KEYSYM,
				GLFW.GLFW_KEY_G,
				"category.rpginventory.category"
		));
		KeyBindingsRegistry.toggleTwoHandedStance = KeyBindingHelper.registerKeyBinding(new KeyBinding(
				"key.rpginventory.toggleTwoHandedStance",
				InputUtil.Type.KEYSYM,
				GLFW.GLFW_KEY_H,
				"category.rpginventory.category"
		));
		KeyBindingsRegistry.swapHand = KeyBindingHelper.registerKeyBinding(new KeyBinding(
				"key.rpginventory.swapHand",
				InputUtil.Type.KEYSYM,
				GLFW.GLFW_KEY_X,
				"category.rpginventory.category"
		));
		KeyBindingsRegistry.swapOffHand = KeyBindingHelper.registerKeyBinding(new KeyBinding(
				"key.rpginventory.swapOffHand",
				InputUtil.Type.KEYSYM,
				GLFW.GLFW_KEY_Y,
				"category.rpginventory.category"
		));
		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			if (KeyBindingsRegistry.swapHand.wasPressed()) {
				if (!swapHandBoolean) {
					syncSlotSwapHand(true);
				}
				swapHandBoolean = true;
			} else if (swapHandBoolean) {
				swapHandBoolean = false;
			}
			if (KeyBindingsRegistry.swapOffHand.wasPressed()) {
				if (!swapOffHandBoolean) {
					syncSlotSwapHand(false);
				}
				swapOffHandBoolean = true;
			} else if (swapOffHandBoolean) {
				swapOffHandBoolean = false;
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
		ClientPlayNetworking.send(new SheatheWeaponsPacket());
	}

	public static void toggleTwoHandedStance() {
		ClientPlayNetworking.send(new ToggleTwoHandedStancePacket());
	}

	public static void syncSlotSwapHand(boolean mainHand) {
		ClientPlayNetworking.send(new SwapHandItemsPacket(mainHand));
	}
}
