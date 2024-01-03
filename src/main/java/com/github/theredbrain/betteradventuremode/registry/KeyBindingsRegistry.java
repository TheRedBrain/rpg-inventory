package com.github.theredbrain.betteradventuremode.registry;

import com.github.theredbrain.betteradventuremode.entity.player.DuckPlayerEntityMixin;
import com.github.theredbrain.betteradventuremode.network.packet.*;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class KeyBindingsRegistry {

    public static KeyBinding sheatheWeapons;
    public static KeyBinding twoHandMainWeapon;
    public static KeyBinding swapMainHand;
    public static KeyBinding swapOffHand;
    public static KeyBinding toggleNecklaceAbility;
    public static KeyBinding openHousingScreen;
    public static boolean sheatheWeaponsBoolean;
    public static boolean twoHandMainWeaponBoolean;
    public static boolean swapMainHandBoolean;
    public static boolean swapOffHandBoolean;
    public static boolean toggleNecklaceAbilityBoolean;
    public static boolean openHousingScreenBoolean;

    public static void registerKeyBindings() {
        KeyBindingsRegistry.sheatheWeapons = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.betteradventuremode.sheatheWeapons",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_F,
                "category.betteradventuremode.category"
        ));
        KeyBindingsRegistry.twoHandMainWeapon = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.betteradventuremode.twoHandMainWeapon",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_G,
                "category.betteradventuremode.category"
        ));
        KeyBindingsRegistry.swapMainHand = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.betteradventuremode.swapMainHand",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_E,
                "category.betteradventuremode.category"
        ));
        KeyBindingsRegistry.swapOffHand = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.betteradventuremode.swapOffHand",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_Q,
                "category.betteradventuremode.category"
        ));
        KeyBindingsRegistry.toggleNecklaceAbility = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.betteradventuremode.toggleNecklaceAbility",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_Z,
                "category.betteradventuremode.category"
        ));
        KeyBindingsRegistry.openHousingScreen = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.betteradventuremode.housingScreen",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_H,
                "category.betteradventuremode.category"
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
            if (KeyBindingsRegistry.toggleNecklaceAbility.wasPressed()) {
                if (!toggleNecklaceAbilityBoolean) {
                    toggleNecklaceAbility();
                }
                toggleNecklaceAbilityBoolean = true;
            } else if (toggleNecklaceAbilityBoolean) {
                toggleNecklaceAbilityBoolean = false;
            }
            if (KeyBindingsRegistry.openHousingScreen.wasPressed()) {
                if (!openHousingScreenBoolean) {
                    openHousingScreen(client);
                }
                openHousingScreenBoolean = true;
            } else if (openHousingScreenBoolean) {
                openHousingScreenBoolean = false;
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
    public static void toggleNecklaceAbility() {
        ClientPlayNetworking.send(new ToggleNecklaceAbilityPacket());
    }
    public static void openHousingScreen(MinecraftClient client) {
        if (client.player != null) {
            ((DuckPlayerEntityMixin)client.player).betteradventuremode$openHousingScreen();
        }
    }
}
