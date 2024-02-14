package com.github.theredbrain.betteradventuremode.registry;

import com.github.theredbrain.betteradventuremode.BetterAdventureMode;
import com.github.theredbrain.betteradventuremode.network.packet.*;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.util.Identifier;

public class ServerPacketRegistry {

    public static final Identifier SWAPPED_HAND_ITEMS_PACKET = BetterAdventureMode.identifier("swapped_hand_items");
    public static final Identifier CANCEL_ATTACK_PACKET = BetterAdventureMode.identifier("attack_stamina_cost");
    public static final Identifier ADD_STATUS_EFFECT_PACKET = BetterAdventureMode.identifier("add_status_effect");
//    public static final Identifier SHEATHED_WEAPONS_PACKET = RPGMod.identifier("sheathed_weapons"); // TODO if weapon sheathing is not visible in multiplayer

    public static final Identifier SYNC_CONFIG = BetterAdventureMode.identifier("sync_config");
//    public static final Identifier SYNC_PLAYER_HOUSES = BetterAdventureModeCore.identifier("sync_player_houses");
    public static final Identifier SYNC_CRAFTING_RECIPES = BetterAdventureMode.identifier("sync_crafting_recipes");
    public static final Identifier SYNC_DIALOGUES = BetterAdventureMode.identifier("sync_dialogues");
    public static final Identifier SYNC_DIALOGUE_ANSWERS = BetterAdventureMode.identifier("sync_dialogue_answers");
    public static final Identifier SYNC_LOCATIONS = BetterAdventureMode.identifier("sync_locations");
    public static final Identifier SYNC_SHOPS = BetterAdventureMode.identifier("sync_shops");

    public static void init() {
        ServerPlayNetworking.registerGlobalReceiver(SwapHandItemsPacket.TYPE, new SwapHandItemsPacketReceiver());

        ServerPlayNetworking.registerGlobalReceiver(SheatheWeaponsPacket.TYPE, new SheatheWeaponsPacketReceiver());

        ServerPlayNetworking.registerGlobalReceiver(TwoHandMainWeaponPacket.TYPE, new TwoHandMainWeaponPacketReceiver());

        ServerPlayNetworking.registerGlobalReceiver(ToggleNecklaceAbilityPacket.TYPE, new ToggleNecklaceAbilityPacketReceiver());

        ServerPlayNetworking.registerGlobalReceiver(AttackStaminaCostPacket.TYPE, new AttackStaminaCostPacketReceiver());

        ServerPlayNetworking.registerGlobalReceiver(AddStatusEffectPacket.TYPE, new AddStatusEffectPacketReceiver());

        ServerPlayNetworking.registerGlobalReceiver(SuccessfulTeleportPacket.TYPE, new SuccessfulTeleportPacketReceiver());


        ServerPlayNetworking.registerGlobalReceiver(UpdateHousingBlockAdventurePacket.TYPE, new UpdateHousingBlockAdventurePacketReceiver());

        ServerPlayNetworking.registerGlobalReceiver(UpdateHousingBlockCreativePacket.TYPE, new UpdateHousingBlockCreativePacketReceiver());

        ServerPlayNetworking.registerGlobalReceiver(SetHousingBlockOwnerPacket.TYPE, new SetHousingBlockOwnerPacketReceiver());

        ServerPlayNetworking.registerGlobalReceiver(ResetHouseHousingBlockPacket.TYPE, new ResetHouseHousingBlockPacketReceiver());

        ServerPlayNetworking.registerGlobalReceiver(UpdateTeleporterBlockPacket.TYPE, new UpdateTeleporterBlockPacketReceiver());

        ServerPlayNetworking.registerGlobalReceiver(UpdateJigsawPlacerBlockPacket.TYPE, new UpdateJigsawPlacerBlockPacketReceiver());

        ServerPlayNetworking.registerGlobalReceiver(UpdateRedstoneTriggerBlockPacket.TYPE, new UpdateRedstoneTriggerBlockPacketReceiver());

        ServerPlayNetworking.registerGlobalReceiver(UpdateRelayTriggerBlockPacket.TYPE, new UpdateRelayTriggerBlockPacketReceiver());

        ServerPlayNetworking.registerGlobalReceiver(UpdateTriggeredCounterBlockPacket.TYPE, new UpdateTriggeredCounterBlockPacketReceiver());

        ServerPlayNetworking.registerGlobalReceiver(UpdateResetTriggerBlockPacket.TYPE, new UpdateResetTriggerBlockPacketReceiver());

        ServerPlayNetworking.registerGlobalReceiver(UpdateDelayTriggerBlockPacket.TYPE, new UpdateDelayTriggerBlockPacketReceiver());

        ServerPlayNetworking.registerGlobalReceiver(UpdateUseRelayBlockPacket.TYPE, new UpdateUseRelayBlockPacketReceiver());

        ServerPlayNetworking.registerGlobalReceiver(TeleportFromTeleporterBlockPacket.TYPE, new TeleportFromTeleporterBlockPacketReceiver());

        ServerPlayNetworking.registerGlobalReceiver(LeaveHouseFromHousingScreenPacket.TYPE, new LeaveHouseFromHousingScreenPacketReceiver());

        ServerPlayNetworking.registerGlobalReceiver(UpdateMimicBlockPacket.TYPE, new UpdateMimicBlockPacketReceiver());

        ServerPlayNetworking.registerGlobalReceiver(RemoveMannequinPacket.TYPE, new RemoveMannequinPacketReceiver());

        ServerPlayNetworking.registerGlobalReceiver(UpdateMannequinSettingsPacket.TYPE, new UpdateMannequinSettingsPacketReceiver());

        ServerPlayNetworking.registerGlobalReceiver(UpdateMannequinEquipmentPacket.TYPE, new UpdateMannequinEquipmentPacketReceiver());

        ServerPlayNetworking.registerGlobalReceiver(UpdateMannequinModelPartsPacket.TYPE, new UpdateMannequinModelPartsPacketReceiver());

        ServerPlayNetworking.registerGlobalReceiver(ExportImportMannequinEquipmentPacket.TYPE, new ExportImportMannequinEquipmentPacketReceiver());

        ServerPlayNetworking.registerGlobalReceiver(UpdateTriggeredSpawnerBlockPacket.TYPE, new UpdateTriggeredSpawnerBlockPacketReceiver());

        ServerPlayNetworking.registerGlobalReceiver(UpdateLocationControlBlockPacket.TYPE, new UpdateLocationControlBlockPacketReceiver());

        ServerPlayNetworking.registerGlobalReceiver(SetManualResetLocationControlBlockPacket.TYPE, new SetManualResetLocationControlBlockPacketReceiver());

        ServerPlayNetworking.registerGlobalReceiver(CraftFromCraftingBenchPacket.TYPE, new CraftFromCraftingBenchPacketReceiver());

        ServerPlayNetworking.registerGlobalReceiver(ToggleUseStashForCraftingPacket.TYPE, new ToggleUseStashForCraftingPacketReceiver());

        ServerPlayNetworking.registerGlobalReceiver(UpdateCraftingBenchScreenHandlerPropertyPacket.TYPE, new UpdateCraftingBenchScreenHandlerPropertyPacketReceiver());

        ServerPlayNetworking.registerGlobalReceiver(UpdateEntranceDelegationBlockPacket.TYPE, new UpdateEntranceDelegationBlockPacketReceiver());

        ServerPlayNetworking.registerGlobalReceiver(UpdateStatusEffectApplierBlockPacket.TYPE, new UpdateStatusEffectApplierBlockPacketReceiver());

        ServerPlayNetworking.registerGlobalReceiver(UpdateTriggeredAdvancementCheckerBlockPacket.TYPE, new UpdateTriggeredAdvancementCheckerBlockPacketReceiver());

        // --- shop packets

        ServerPlayNetworking.registerGlobalReceiver(UpdateShopBlockPacket.TYPE, new UpdateShopBlockPacketReceiver());

        ServerPlayNetworking.registerGlobalReceiver(TradeWithShopPacket.TYPE, new TradeWithShopPacketReceiver());

        // --- dialogue packets

        ServerPlayNetworking.registerGlobalReceiver(UpdateDialogueBlockPacket.TYPE, new UpdateDialogueBlockPacketReceiver());

        ServerPlayNetworking.registerGlobalReceiver(DialogueAnswerPacket.TYPE, new DialogueAnswerPacketReceiver());
    }
}
