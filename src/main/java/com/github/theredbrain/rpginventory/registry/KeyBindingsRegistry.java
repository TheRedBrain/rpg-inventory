package com.github.theredbrain.rpginventory.registry;

import com.github.theredbrain.rpginventory.network.packet.*;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class KeyBindingsRegistry {

    public static KeyBinding sheatheWeapons;
    public static KeyBinding twoHandMainWeapon;
    public static KeyBinding swapMainHand;
    public static KeyBinding swapOffHand;
    public static boolean sheatheWeaponsBoolean;
    public static boolean twoHandMainWeaponBoolean;
    public static boolean swapMainHandBoolean;
    public static boolean swapOffHandBoolean;

    public static void registerKeyBindings() {
        KeyBindingsRegistry.sheatheWeapons = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.rpginventory.sheatheWeapons",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_G,
                "category.rpginventory.category"
        ));
        KeyBindingsRegistry.twoHandMainWeapon = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.rpginventory.twoHandMainWeapon",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_H,
                "category.rpginventory.category"
        ));
        KeyBindingsRegistry.swapMainHand = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.rpginventory.swapMainHand",
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
            if (KeyBindingsRegistry.swapMainHand.wasPressed()) {
                if (!swapMainHandBoolean) {
                    syncSlotSwapHand(true);
                }
                swapMainHandBoolean = true;
            } else if (swapMainHandBoolean) {
                swapMainHandBoolean = false;
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
            if (KeyBindingsRegistry.twoHandMainWeapon.wasPressed()) {
                if (!twoHandMainWeaponBoolean) {
                    twoHandMainWeapon();
                }
                twoHandMainWeaponBoolean = true;
            } else if (twoHandMainWeaponBoolean) {
                twoHandMainWeaponBoolean = false;
            }
        });
    }
    public static void sheatheWeapons() {
        ClientPlayNetworking.send(new SheatheWeaponsPacket());
    }
    public static void twoHandMainWeapon() {
        ClientPlayNetworking.send(new TwoHandMainWeaponPacket());
    }
    public static void syncSlotSwapHand(boolean mainHand) {
        ClientPlayNetworking.send(new SwapHandItemsPacket(mainHand));
    }
}
