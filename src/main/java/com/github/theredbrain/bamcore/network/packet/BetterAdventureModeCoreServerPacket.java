package com.github.theredbrain.bamcore.network.packet;

import com.github.theredbrain.bamcore.BetterAdventureModeCore;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.util.Identifier;

public class BetterAdventureModeCoreServerPacket {

    public static final Identifier SWAP_HAND_ITEMS_PACKET = BetterAdventureModeCore.identifier("swap_hand_items");
    public static final Identifier SWAPPED_HAND_ITEMS_PACKET = BetterAdventureModeCore.identifier("swapped_hand_items");
    public static final Identifier SHEATHE_WEAPONS_PACKET = BetterAdventureModeCore.identifier("sheathe_weapons");
    public static final Identifier TWO_HAND_MAIN_WEAPON_PACKET = BetterAdventureModeCore.identifier("two_hand_main_weapon");
    public static final Identifier TOGGLE_NECKLACE_ABILITY_PACKET = BetterAdventureModeCore.identifier("toggle_necklace_ability_weapon");
//    public static final Identifier SHEATHED_WEAPONS_PACKET = RPGMod.identifier("sheathed_weapons"); // TODO if weapon sheathing is not visible in multiplayer

    public static void init() {
        SwapHandItemsPacketReceiver swapHandItemsPacketReceiver = new SwapHandItemsPacketReceiver();
        ServerPlayNetworking.registerGlobalReceiver(BetterAdventureModeCoreServerPacket.SWAP_HAND_ITEMS_PACKET, swapHandItemsPacketReceiver);

        SheatheWeaponsPacketReceiver sheatheWeaponsPacketReceiver = new SheatheWeaponsPacketReceiver();
        ServerPlayNetworking.registerGlobalReceiver(BetterAdventureModeCoreServerPacket.SHEATHE_WEAPONS_PACKET, sheatheWeaponsPacketReceiver);

        TwoHandMainWeaponPacketReceiver twoHandMainWeaponPacketReceiver = new TwoHandMainWeaponPacketReceiver();
        ServerPlayNetworking.registerGlobalReceiver(BetterAdventureModeCoreServerPacket.TWO_HAND_MAIN_WEAPON_PACKET, twoHandMainWeaponPacketReceiver);

        ToggleNecklaceAbilityPacketReceiver toggleNecklaceAbilityPacketReceiver = new ToggleNecklaceAbilityPacketReceiver();
        ServerPlayNetworking.registerGlobalReceiver(BetterAdventureModeCoreServerPacket.TOGGLE_NECKLACE_ABILITY_PACKET, toggleNecklaceAbilityPacketReceiver);
    }
}
