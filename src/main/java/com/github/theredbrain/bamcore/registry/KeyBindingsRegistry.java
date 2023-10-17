package com.github.theredbrain.bamcore.registry;

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

public class KeyBindingsRegistry {

    public static KeyBinding sheatheWeapons;
    public static KeyBinding twoHandMainWeapon;
    public static KeyBinding swapMainHand;
    public static KeyBinding swapOffHand;
    public static KeyBinding toggleNecklaceAbility;
    public static boolean sheatheWeaponsBoolean;
    public static boolean twoHandMainWeaponBoolean;
    public static boolean swapMainHandBoolean;
    public static boolean swapOffHandBoolean;
    public static boolean toggleNecklaceAbilityBoolean;

    public static void registerKeyBindings() {
        KeyBindingsRegistry.sheatheWeapons = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.bamcore.sheatheWeapons",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_F,
                "category.bamcore.category"
        ));
        KeyBindingsRegistry.twoHandMainWeapon = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.bamcore.twoHandMainWeapon",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_G,
                "category.bamcore.category"
        ));
        KeyBindingsRegistry.swapMainHand = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.bamcore.swapMainHand",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_E,
                "category.bamcore.category"
        ));
        KeyBindingsRegistry.swapOffHand = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.bamcore.swapOffHand",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_Q,
                "category.bamcore.category"
        ));
        KeyBindingsRegistry.toggleNecklaceAbility = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.bamcore.toggleNecklaceAbility",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_Z,
                "category.bamcore.category"
        ));
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (KeyBindingsRegistry.swapMainHand.wasPressed()) {
                if (!swapMainHandBoolean) {
                    syncSlotSwapHand(client, true);
                }
                swapMainHandBoolean = true;
            } else if (swapMainHandBoolean) {
                swapMainHandBoolean = false;
            }
            if (KeyBindingsRegistry.swapOffHand.wasPressed()) {
                if (!swapOffHandBoolean) {
                    syncSlotSwapHand(client, false);
                }
                swapOffHandBoolean = true;
            } else if (swapOffHandBoolean) {
                swapOffHandBoolean = false;
            }
            if (KeyBindingsRegistry.sheatheWeapons.wasPressed()) {
                if (!sheatheWeaponsBoolean) {
                    sheatheWeapons(client);
                }
                sheatheWeaponsBoolean = true;
            } else if (sheatheWeaponsBoolean) {
                sheatheWeaponsBoolean = false;
            }
            if (KeyBindingsRegistry.twoHandMainWeapon.wasPressed()) {
                if (!twoHandMainWeaponBoolean) {
                    twoHandMainWeapon(client);
                }
                twoHandMainWeaponBoolean = true;
            } else if (twoHandMainWeaponBoolean) {
                twoHandMainWeaponBoolean = false;
            }
            if (KeyBindingsRegistry.toggleNecklaceAbility.wasPressed()) {
                if (!toggleNecklaceAbilityBoolean) {
                    toggleNecklaceAbility(client);
                }
                toggleNecklaceAbilityBoolean = true;
            } else if (toggleNecklaceAbilityBoolean) {
                toggleNecklaceAbilityBoolean = false;
            }
        });
    }
    public static void sheatheWeapons(MinecraftClient client) {
        PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
        client.getNetworkHandler().sendPacket(new CustomPayloadC2SPacket(BetterAdventureModCoreServerPacket.SHEATHE_WEAPONS_PACKET, buf));
    }
    public static void twoHandMainWeapon(MinecraftClient client) {
        PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
        client.getNetworkHandler().sendPacket(new CustomPayloadC2SPacket(BetterAdventureModCoreServerPacket.TWO_HAND_MAIN_WEAPON_PACKET, buf));
    }
    public static void syncSlotSwapHand(MinecraftClient client, boolean mainHand) {
        PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
        buf.writeBoolean(mainHand);
        client.getNetworkHandler().sendPacket(new CustomPayloadC2SPacket(BetterAdventureModCoreServerPacket.SWAP_HAND_ITEMS_PACKET, buf));
    }
    public static void toggleNecklaceAbility(MinecraftClient client) {
        PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
        client.getNetworkHandler().sendPacket(new CustomPayloadC2SPacket(BetterAdventureModCoreServerPacket.TOGGLE_NECKLACE_ABILITY_PACKET, buf));
    }
}
