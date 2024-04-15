package com.github.theredbrain.betteradventuremode.network.packet;

import com.github.theredbrain.betteradventuremode.block.DialogueBlock;
import com.github.theredbrain.betteradventuremode.block.entity.DialogueBlockEntity;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.World;

public class OpenDialogueScreenPacketReceiver implements ServerPlayNetworking.PlayPacketHandler<OpenDialogueScreenPacket> {

    @Override
    public void receive(OpenDialogueScreenPacket packet, ServerPlayerEntity player, PacketSender responseSender) {

        String responseDialogueIdentifier = packet.responseDialogueIdentifier;

        World world = player.getWorld();

        BlockEntity blockEntity = world.getBlockEntity(packet.dialogueBlockPos);

        if (blockEntity instanceof DialogueBlockEntity dialogueBlockEntity) {
            player.openHandledScreen(DialogueBlock.createDialogueBlockScreenHandlerFactory(dialogueBlockEntity.getCachedState(), world, dialogueBlockEntity.getPos(), responseDialogueIdentifier));
        }
    }
}
