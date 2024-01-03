package com.github.theredbrain.betteradventuremode.network.packet;

import com.github.theredbrain.betteradventuremode.block.entity.DialogueBlockEntity;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.apache.commons.lang3.tuple.MutablePair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class UpdateDialogueBlockPacketReceiver implements ServerPlayNetworking.PlayPacketHandler<UpdateDialogueBlockPacket> {

    @Override
    public void receive(UpdateDialogueBlockPacket packet, ServerPlayerEntity player, PacketSender responseSender) {

        if (!player.isCreativeLevelTwoOp()) {
            return;
        }

        BlockPos dialogueBlockPosition = packet.dialogueBlockPosition;

        List<MutablePair<String, BlockPos>> dialogueUsedBlocksList = packet.dialogueUsedBlocksList;
        HashMap<String, BlockPos> dialogueUsedBlocksMap = new HashMap<>();
        for (MutablePair<String, BlockPos> usedBlock : dialogueUsedBlocksList) {
            dialogueUsedBlocksMap.put(usedBlock.getLeft(), usedBlock.getRight());
        }

        List<MutablePair<String, BlockPos>> dialogueTriggeredBlocksList = packet.dialogueTriggeredBlocksList;
        HashMap<String, BlockPos> dialogueTriggeredBlocksMap = new HashMap<>();
        for (MutablePair<String, BlockPos> triggeredBlock : dialogueTriggeredBlocksList) {
            dialogueTriggeredBlocksMap.put(triggeredBlock.getLeft(), triggeredBlock.getRight());
        }

        List<MutablePair<String, MutablePair<String, String>>> startingDialogueList = new ArrayList<>(packet.startingDialogueList);

        World world = player.getWorld();

        boolean updateSuccessful = true;

        BlockEntity blockEntity = world.getBlockEntity(dialogueBlockPosition);
        BlockState blockState = world.getBlockState(dialogueBlockPosition);

        if (blockEntity instanceof DialogueBlockEntity dialogueBlockEntity) {

            if (!dialogueBlockEntity.setDialogueUsedBlocks(dialogueUsedBlocksMap)) {
                player.sendMessage(Text.translatable("dialogue_block.dialogueUsedBlocksList.invalid"), false);
                updateSuccessful = false;
            }
            if (!dialogueBlockEntity.setDialogueTriggeredBlocks(dialogueTriggeredBlocksMap)) {
                player.sendMessage(Text.translatable("dialogue_block.dialogueTriggeredBlocksList.invalid"), false);
                updateSuccessful = false;
            }
            if (!dialogueBlockEntity.setStartingDialogueList(startingDialogueList)) {
                player.sendMessage(Text.translatable("dialogue_block.startingDialogueList.invalid"), false);
                updateSuccessful = false;
            }
            if (updateSuccessful) {
                player.sendMessage(Text.translatable("dialogue_block.update_successful"), true);
            }
            dialogueBlockEntity.markDirty();
            world.updateListeners(dialogueBlockPosition, blockState, blockState, Block.NOTIFY_ALL);
        }

    }
}
