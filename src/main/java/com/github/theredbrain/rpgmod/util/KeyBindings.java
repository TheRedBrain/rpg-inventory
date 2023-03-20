package com.github.theredbrain.rpgmod.util;

import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class KeyBindings {

    public static KeyBinding consumeItem;
    public static KeyBinding swapMainHand;
    public static KeyBinding swapOffHand;

    public static void registerKeyBindings() {
        KeyBindings.consumeItem = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.rpgmod.consumeItem",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_F,
                "category.rpgmod.category"
        ));
        KeyBindings.swapMainHand = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.rpgmod.swapMainHand",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_Y,
                "category.rpgmod.category"
        ));
        KeyBindings.swapOffHand = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.rpgmod.swapOffHand",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_X,
                "category.rpgmod.category"
        ));
    }
}
