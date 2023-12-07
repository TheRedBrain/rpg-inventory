package com.github.theredbrain.bamcore.network.packet;

import com.github.theredbrain.bamcore.BetterAdventureModeCore;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.util.Identifier;

public class BetterAdventureModeCoreServerPacket {

    public static final Identifier SWAPPED_HAND_ITEMS_PACKET = BetterAdventureModeCore.identifier("swapped_hand_items");
    public static final Identifier CANCEL_ATTACK_PACKET = BetterAdventureModeCore.identifier("attack_stamina_cost");
    public static final Identifier ADD_STATUS_EFFECT_PACKET = BetterAdventureModeCore.identifier("add_status_effect");
//    public static final Identifier SHEATHED_WEAPONS_PACKET = RPGMod.identifier("sheathed_weapons"); // TODO if weapon sheathing is not visible in multiplayer

    public static final Identifier SYNC_CONFIG = BetterAdventureModeCore.identifier("sync_config");
    public static final Identifier SYNC_PLAYER_HOUSES = BetterAdventureModeCore.identifier("sync_player_houses");
    public static final Identifier SYNC_PLAYER_DUNGEONS = BetterAdventureModeCore.identifier("sync_player_dungeons");

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

        ServerPlayNetworking.registerGlobalReceiver(UpdateJigsawPlacerBlockPacket.TYPE, new UpdateJigsawPlacerBlockPacketReceiver());

        ServerPlayNetworking.registerGlobalReceiver(UpdateRedstoneTriggerBlockPacket.TYPE, new UpdateRedstoneTriggerBlockPacketReceiver());

        ServerPlayNetworking.registerGlobalReceiver(UpdateRelayTriggerBlockPacket.TYPE, new UpdateRelayTriggerBlockPacketReceiver());

        ServerPlayNetworking.registerGlobalReceiver(UpdateDelayTriggerBlockPacket.TYPE, new UpdateDelayTriggerBlockPacketReceiver());

        ServerPlayNetworking.registerGlobalReceiver(UpdateUseRelayBlockPacket.TYPE, new UpdateUseRelayBlockPacketReceiver());

        ServerPlayNetworking.registerGlobalReceiver(TeleportFromTeleporterBlockPacket.TYPE, new TeleportFromTeleporterBlockPacketReceiver());

        ServerPlayNetworking.registerGlobalReceiver(LeaveHouseFromHousingScreenPacket.TYPE, new LeaveHouseFromHousingScreenPacketReceiver());

//        ServerPlayNetworking.registerGlobalReceiver(BetterAdventureModeCoreServerPacket.REGENERATE_DIMENSION_FROM_TELEPORTER_BLOCK, new RegenerateDimensionFromTeleporterBlockPacketReceiver());
    }
}
