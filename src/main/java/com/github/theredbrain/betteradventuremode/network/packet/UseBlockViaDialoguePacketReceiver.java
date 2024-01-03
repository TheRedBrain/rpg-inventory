package com.github.theredbrain.betteradventuremode.network.packet;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.world.World;

public class UseBlockViaDialoguePacketReceiver implements ServerPlayNetworking.PlayPacketHandler<UseBlockViaDialoguePacket> {

    @Override
    public void receive(UseBlockViaDialoguePacket packet, ServerPlayerEntity player, PacketSender responseSender) {

        BlockHitResult blockHitResult = packet.blockHitResult;
        World world = player.getWorld();
        Hand hand = player.getActiveHand();
        ItemStack itemStack = player.getStackInHand(hand);

        player.interactionManager.interactBlock(player, world, itemStack, hand, blockHitResult);
    }
}
