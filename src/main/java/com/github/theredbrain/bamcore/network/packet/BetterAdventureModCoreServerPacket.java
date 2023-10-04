package com.github.theredbrain.bamcore.network.packet;

import com.github.theredbrain.bamcore.BetterAdventureModeCore;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.util.Identifier;

public class BetterAdventureModCoreServerPacket {

    public static final Identifier SWAP_HAND_ITEMS_PACKET = BetterAdventureModeCore.identifier("swap_hand_items");
    public static final Identifier SWAPPED_HAND_ITEMS_PACKET = BetterAdventureModeCore.identifier("swapped_hand_items");
    public static final Identifier SHEATHE_WEAPONS_PACKET = BetterAdventureModeCore.identifier("sheathe_weapons");
//    public static final Identifier SHEATHED_WEAPONS_PACKET = RPGMod.identifier("sheathed_weapons"); // TODO if weapon sheathing is not visible in multiplayer
    public static final Identifier UPDATE_TELEPORTER_BLOCK = BetterAdventureModeCore.identifier("update_teleporter_block");
    public static final Identifier UPDATE_AREA_FILLER_BLOCK = BetterAdventureModeCore.identifier("update_area_filler_block");
    public static final Identifier UPDATE_STRUCTURE_PLACER_BLOCK = BetterAdventureModeCore.identifier("update_structure_placer_block");
    public static final Identifier UPDATE_REDSTONE_TRIGGER_BLOCK = BetterAdventureModeCore.identifier("update_redstone_trigger_block");
    public static final Identifier UPDATE_RELAY_TRIGGER_BLOCK = BetterAdventureModeCore.identifier("update_relay_trigger_block");
    public static final Identifier UPDATE_DELAY_TRIGGER_BLOCK = BetterAdventureModeCore.identifier("update_delay_trigger_block");
    public static final Identifier UPDATE_CHUNK_LOADER_BLOCK = BetterAdventureModeCore.identifier("update_chunk_loader_block");
    public static final Identifier TELEPORT_FROM_TELEPORTER_BLOCK = BetterAdventureModeCore.identifier("teleport_from_teleporter_block");
    public static final Identifier REGENERATE_DIMENSION_FROM_TELEPORTER_BLOCK = BetterAdventureModeCore.identifier("regenerate_dimension_from_teleporter_block");

    public static void init() {
        SwapHandItemsPacketReceiver swapHandItemsPacketReceiver = new SwapHandItemsPacketReceiver();
        ServerPlayNetworking.registerGlobalReceiver(BetterAdventureModCoreServerPacket.SWAP_HAND_ITEMS_PACKET, swapHandItemsPacketReceiver);

        SheatheWeaponsPacketReceiver sheatheWeaponsPacketReceiver = new SheatheWeaponsPacketReceiver();
        ServerPlayNetworking.registerGlobalReceiver(BetterAdventureModCoreServerPacket.SHEATHE_WEAPONS_PACKET, sheatheWeaponsPacketReceiver);

//        IsUsingHotbarItemServerPacketReceiver isUsingHotbarItemServerPacketReceiver = new IsUsingHotbarItemServerPacketReceiver();
//        ServerPlayNetworking.registerGlobalReceiver(RPGModServerPacket.IS_USING_HOTBAR_ITEM_SERVER_PACKET, isUsingHotbarItemServerPacketReceiver);

        // TODO move to bamdimensions
//        UpdateTeleporterBlockPacketReceiver updateTeleporterBlockPacketReceiver = new UpdateTeleporterBlockPacketReceiver();
//        ServerPlayNetworking.registerGlobalReceiver(BetterAdventureModCoreServerPacket.UPDATE_TELEPORTER_BLOCK, updateTeleporterBlockPacketReceiver);

        UpdateAreaFillerBlockPacketReceiver updateAreaFillerBlockPacketReceiver = new UpdateAreaFillerBlockPacketReceiver();
        ServerPlayNetworking.registerGlobalReceiver(BetterAdventureModCoreServerPacket.UPDATE_AREA_FILLER_BLOCK, updateAreaFillerBlockPacketReceiver);

        UpdateStructurePlacerBlockPacketReceiver updateStructurePlacerBlockPacketReceiver = new UpdateStructurePlacerBlockPacketReceiver();
        ServerPlayNetworking.registerGlobalReceiver(BetterAdventureModCoreServerPacket.UPDATE_STRUCTURE_PLACER_BLOCK, updateStructurePlacerBlockPacketReceiver);

        UpdateRedstoneTriggerBlockPacketReceiver updateRedstoneTriggerBlockPacketReceiver = new UpdateRedstoneTriggerBlockPacketReceiver();
        ServerPlayNetworking.registerGlobalReceiver(BetterAdventureModCoreServerPacket.UPDATE_REDSTONE_TRIGGER_BLOCK, updateRedstoneTriggerBlockPacketReceiver);

        UpdateRelayTriggerBlockPacketReceiver updateRelayTriggerBlockPacketReceiver = new UpdateRelayTriggerBlockPacketReceiver();
        ServerPlayNetworking.registerGlobalReceiver(BetterAdventureModCoreServerPacket.UPDATE_RELAY_TRIGGER_BLOCK, updateRelayTriggerBlockPacketReceiver);

        UpdateDelayTriggerBlockPacketReceiver updateDelayTriggerBlockPacketReceiver = new UpdateDelayTriggerBlockPacketReceiver();
        ServerPlayNetworking.registerGlobalReceiver(BetterAdventureModCoreServerPacket.UPDATE_DELAY_TRIGGER_BLOCK, updateDelayTriggerBlockPacketReceiver);

        UpdateChunkLoaderBlockPacketReceiver updateChunkLoaderBlockPacketReceiver = new UpdateChunkLoaderBlockPacketReceiver();
        ServerPlayNetworking.registerGlobalReceiver(BetterAdventureModCoreServerPacket.UPDATE_CHUNK_LOADER_BLOCK, updateChunkLoaderBlockPacketReceiver);

        // TODO move to bamdimensions
//        TeleportFromTeleporterBlockPacketReceiver teleportFromTeleporterBlockPacketReceiver = new TeleportFromTeleporterBlockPacketReceiver();
//        ServerPlayNetworking.registerGlobalReceiver(BetterAdventureModCoreServerPacket.TELEPORT_FROM_TELEPORTER_BLOCK, teleportFromTeleporterBlockPacketReceiver);

        // TODO move to bamdimensions
//        RegenerateDimensionFromTeleporterBlockPacketReceiver regenerateDimensionFromTeleporterBlockPacketReceiver = new RegenerateDimensionFromTeleporterBlockPacketReceiver();
//        ServerPlayNetworking.registerGlobalReceiver(BetterAdventureModCoreServerPacket.REGENERATE_DIMENSION_FROM_TELEPORTER_BLOCK, regenerateDimensionFromTeleporterBlockPacketReceiver);
    }
}
