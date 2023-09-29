package com.github.theredbrain.bamcore.util;

import com.github.theredbrain.bamcore.network.packet.BetterAdventureModCoreServerPacket;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.c2s.play.CustomPayloadC2SPacket;
import org.lwjgl.glfw.GLFW;

public class KeyBindings {

    public static KeyBinding sheatheWeapons;
    public static KeyBinding swapMainHand;
    public static KeyBinding swapOffHand;
    public static boolean sheatheWeaponsBoolean;
    public static boolean swapMainHandBoolean;
    public static boolean swapOffHandBoolean;

    public static void registerKeyBindings() {
        KeyBindings.sheatheWeapons = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.bamcore.sheatheWeapons",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_F,
                "category.bamcore.category"
        ));
        KeyBindings.swapMainHand = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.bamcore.swapMainHand",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_E,
                "category.bamcore.category"
        ));
        KeyBindings.swapOffHand = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.bamcore.swapOffHand",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_Q,
                "category.bamcore.category"
        ));
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (KeyBindings.swapMainHand.wasPressed()) {
                if (!swapMainHandBoolean) {
                    syncSlotSwapHand(client, true);
                }
                swapMainHandBoolean = true;
            } else if (swapMainHandBoolean) {
                swapMainHandBoolean = false;
            }
            if (KeyBindings.swapOffHand.wasPressed()) {
                if (!swapOffHandBoolean) {
                    syncSlotSwapHand(client, false);
                }
                swapOffHandBoolean = true;
            } else if (swapOffHandBoolean) {
                swapOffHandBoolean = false;
            }
            if (KeyBindings.sheatheWeapons.wasPressed()) {
                if (!sheatheWeaponsBoolean) {
                    sheatheWeapons(client, true);
                }
                sheatheWeaponsBoolean = true;
            } else if (sheatheWeaponsBoolean) {
                sheatheWeaponsBoolean = false;
            }
        });
    }
    public static void sheatheWeapons(MinecraftClient client, boolean canSheathe) {
        PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
        buf.writeBoolean(canSheathe);
        client.getNetworkHandler().sendPacket(new CustomPayloadC2SPacket(BetterAdventureModCoreServerPacket.SHEATHE_WEAPONS_PACKET, buf));
    }

    public static void syncSlotSwapHand(MinecraftClient client, boolean mainHand) {
        PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
        buf.writeBoolean(mainHand);
        client.getNetworkHandler().sendPacket(new CustomPayloadC2SPacket(BetterAdventureModCoreServerPacket.SWAP_HAND_ITEMS_PACKET, buf));
    }
}
