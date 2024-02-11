package com.github.theredbrain.betteradventuremode.network.packet;

import com.github.theredbrain.betteradventuremode.api.json_files_backend.Dialogue;
import com.github.theredbrain.betteradventuremode.block.entity.DialogueBlockEntity;
import com.github.theredbrain.betteradventuremode.entity.player.DuckPlayerEntityMixin;
import com.github.theredbrain.betteradventuremode.registry.DialoguesRegistry;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public class OpenDialogueScreenPacketReceiver implements ClientPlayNetworking.PlayPacketHandler<OpenDialogueScreenPacket> {

    @Override
    public void receive(OpenDialogueScreenPacket packet, ClientPlayerEntity player, PacketSender responseSender) {

        String responseDialogueIdentifier = packet.responseDialogueIdentifier;

        World world = player.getWorld();

        BlockEntity blockEntity = world.getBlockEntity(packet.dialogueBlockPos);

        if (blockEntity instanceof DialogueBlockEntity dialogueBlockEntity) {
            Dialogue responseDialogue = DialoguesRegistry.getDialogue(Identifier.tryParse(responseDialogueIdentifier));
            ((DuckPlayerEntityMixin)player).betteradventuremode$openDialogueScreen(dialogueBlockEntity, responseDialogue);
        }
    }
}
