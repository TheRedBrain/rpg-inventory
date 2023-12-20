package com.github.theredbrain.bamcore.network.packet;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContextParameterSet;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.loot.context.LootContextTypes;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;

import java.util.UUID;

public class DialogueGiveItemsFromLootTablePacketReceiver implements ServerPlayNetworking.PlayPacketHandler<DialogueGiveItemsFromLootTablePacket> {

    @Override
    public void receive(DialogueGiveItemsFromLootTablePacket packet, ServerPlayerEntity player, PacketSender responseSender) {

        UUID playerUuid = packet.playerUuid;

        Identifier lootTableIdentifier = packet.lootTableIdentifier;

        MinecraftServer server = player.getServer();

        if (server != null) {
            ServerPlayerEntity playerEntity = server.getPlayerManager().getPlayer(playerUuid);
            if (lootTableIdentifier != null && playerEntity != null) {
                LootContextParameterSet lootContextParameterSet = new LootContextParameterSet.Builder(playerEntity.getServerWorld()).add(LootContextParameters.THIS_ENTITY, playerEntity).add(LootContextParameters.ORIGIN, playerEntity.getPos()).build(LootContextTypes.ADVANCEMENT_REWARD);
                boolean bl = false;
                for (ItemStack itemStack : server.getLootManager().getLootTable(lootTableIdentifier).generateLoot(lootContextParameterSet)) {
                    if (playerEntity.giveItemStack(itemStack)) {
                        playerEntity.getWorld().playSound(null, playerEntity.getX(), playerEntity.getY(), playerEntity.getZ(), SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.PLAYERS, 0.2f, ((playerEntity.getRandom().nextFloat() - playerEntity.getRandom().nextFloat()) * 0.7f + 1.0f) * 2.0f);
                        bl = true;
                        continue;
                    }
                    ItemEntity itemEntity = playerEntity.dropItem(itemStack, false);
                    if (itemEntity == null) continue;
                    itemEntity.resetPickupDelay();
                    itemEntity.setOwner(playerEntity.getUuid());
                }
                if (bl) {
                    playerEntity.currentScreenHandler.sendContentUpdates();
                }
            }
        }
    }
}
