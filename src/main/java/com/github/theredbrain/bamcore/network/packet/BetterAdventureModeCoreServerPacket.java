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
    public static final Identifier ATTACK_STAMINA_COST_PACKET = BetterAdventureModeCore.identifier("attack_stamina_cost");
    public static final Identifier CANCEL_ATTACK_PACKET = BetterAdventureModeCore.identifier("attack_stamina_cost");
    public static final Identifier ADD_STATUS_EFFECT_PACKET = BetterAdventureModeCore.identifier("add_status_effect");
//    public static final Identifier SHEATHED_WEAPONS_PACKET = RPGMod.identifier("sheathed_weapons"); // TODO if weapon sheathing is not visible in multiplayer

    public static final Identifier SYNC_CONFIG = BetterAdventureModeCore.identifier("sync_config");
    public static final Identifier SYNC_PLAYER_HOUSES = BetterAdventureModeCore.identifier("sync_player_houses");
    public static final Identifier SYNC_PLAYER_DUNGEONS = BetterAdventureModeCore.identifier("sync_player_dungeons");
    public static final Identifier UPDATE_HOUSING_BLOCK_ADVENTURE = BetterAdventureModeCore.identifier("update_housing_block_adventure");
    public static final Identifier UPDATE_HOUSING_BLOCK_CREATIVE = BetterAdventureModeCore.identifier("update_housing_block_creative");
    public static final Identifier SET_HOUSING_OWNER_BLOCK = BetterAdventureModeCore.identifier("set_housing_owner_block");
    public static final Identifier RESET_HOUSE_HOUSING_BLOCK = BetterAdventureModeCore.identifier("reset_house_housing_block");
    public static final Identifier UPDATE_TELEPORTER_BLOCK = BetterAdventureModeCore.identifier("update_teleporter_block");
    public static final Identifier UPDATE_AREA_FILLER_BLOCK = BetterAdventureModeCore.identifier("update_area_filler_block");
    public static final Identifier UPDATE_JIGSAW_PLACER_BLOCK = BetterAdventureModeCore.identifier("update_jigsaw_placer_block");
    public static final Identifier UPDATE_REDSTONE_TRIGGER_BLOCK = BetterAdventureModeCore.identifier("update_redstone_trigger_block");
    public static final Identifier UPDATE_RELAY_TRIGGER_BLOCK = BetterAdventureModeCore.identifier("update_relay_trigger_block");
    public static final Identifier UPDATE_DELAY_TRIGGER_BLOCK = BetterAdventureModeCore.identifier("update_delay_trigger_block");
    public static final Identifier UPDATE_CHUNK_LOADER_BLOCK = BetterAdventureModeCore.identifier("update_chunk_loader_block");
    public static final Identifier TELEPORT_FROM_TELEPORTER_BLOCK = BetterAdventureModeCore.identifier("teleport_from_teleporter_block");
    public static final Identifier REGENERATE_DIMENSION_FROM_TELEPORTER_BLOCK = BetterAdventureModeCore.identifier("regenerate_dimension_from_teleporter_block");

    public static void init() {
        ServerPlayNetworking.registerGlobalReceiver(SwapHandItemsPacket.TYPE, new SwapHandItemsPacketReceiver());

        ServerPlayNetworking.registerGlobalReceiver(SheatheWeaponsPacket.TYPE, new SheatheWeaponsPacketReceiver());

        ServerPlayNetworking.registerGlobalReceiver(TwoHandMainWeaponPacket.TYPE, new TwoHandMainWeaponPacketReceiver());

        ServerPlayNetworking.registerGlobalReceiver(ToggleNecklaceAbilityPacket.TYPE, new ToggleNecklaceAbilityPacketReceiver());

        ServerPlayNetworking.registerGlobalReceiver(AttackStaminaCostPacket.TYPE, new AttackStaminaCostPacketReceiver());

        ServerPlayNetworking.registerGlobalReceiver(AddStatusEffectPacket.TYPE, new AddStatusEffectPacketReceiver());


        ServerPlayNetworking.registerGlobalReceiver(UpdateHousingBlockAdventurePacket.TYPE, new UpdateHousingBlockAdventurePacketReceiver());

        ServerPlayNetworking.registerGlobalReceiver(UpdateHousingBlockCreativePacket.TYPE, new UpdateHousingBlockCreativePacketReceiver());

        ServerPlayNetworking.registerGlobalReceiver(SetHousingBlockOwnerPacket.TYPE, new SetHousingBlockOwnerPacketReceiver());

        ServerPlayNetworking.registerGlobalReceiver(ResetHouseHousingBlockPacket.TYPE, new ResetHouseHousingBlockPacketReceiver());

        ServerPlayNetworking.registerGlobalReceiver(UpdateTeleporterBlockPacket.TYPE, new UpdateTeleporterBlockPacketReceiver());

        ServerPlayNetworking.registerGlobalReceiver(UpdateAreaFillerBlockPacket.TYPE, new UpdateAreaFillerBlockPacketReceiver());

        ServerPlayNetworking.registerGlobalReceiver(UpdateJigsawPlacerBlockPacket.TYPE, new UpdateJigsawPlacerBlockPacketReceiver());

        ServerPlayNetworking.registerGlobalReceiver(UpdateRedstoneTriggerBlockPacket.TYPE, new UpdateRedstoneTriggerBlockPacketReceiver());

        ServerPlayNetworking.registerGlobalReceiver(UpdateRelayTriggerBlockPacket.TYPE, new UpdateRelayTriggerBlockPacketReceiver());

        ServerPlayNetworking.registerGlobalReceiver(UpdateDelayTriggerBlockPacket.TYPE, new UpdateDelayTriggerBlockPacketReceiver());

        ServerPlayNetworking.registerGlobalReceiver(UpdateChunkLoaderBlockPacket.TYPE, new UpdateChunkLoaderBlockPacketReceiver());
        ServerPlayNetworking.registerGlobalReceiver(UpdateUseRelayBlockPacket.TYPE, new UpdateUseRelayBlockPacketReceiver());

        ServerPlayNetworking.registerGlobalReceiver(TeleportFromTeleporterBlockPacket.TYPE, new TeleportFromTeleporterBlockPacketReceiver());

//        ServerPlayNetworking.registerGlobalReceiver(BetterAdventureModeCoreServerPacket.REGENERATE_DIMENSION_FROM_TELEPORTER_BLOCK, new RegenerateDimensionFromTeleporterBlockPacketReceiver());
    }
}
