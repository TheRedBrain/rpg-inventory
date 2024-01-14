package com.github.theredbrain.betteradventuremode.network.packet;

import com.github.theredbrain.betteradventuremode.block.entity.TriggeredAdvancementCheckerBlockEntity;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class UpdateTriggeredAdvancementCheckerBlockPacketReceiver implements ServerPlayNetworking.PlayPacketHandler<UpdateTriggeredAdvancementCheckerBlockPacket> {
    @Override
    public void receive(UpdateTriggeredAdvancementCheckerBlockPacket packet, ServerPlayerEntity player, PacketSender responseSender) {

        if (!player.isCreativeLevelTwoOp()) {
            return;
        }

        BlockPos triggeredAdvancementCheckerBlockPosition = packet.triggeredAdvancementCheckerBlockPosition;

        BlockPos firstTriggeredBlockPositionOffset = packet.firstTriggeredBlockPositionOffset;

        BlockPos secondTriggeredBlockPositionOffset = packet.secondTriggeredBlockPositionOffset;

        String checkedAdvancementIdentifier = packet.checkedAdvancementIdentifier;

        World world = player.getWorld();

        boolean updateSuccessful = true;

        BlockEntity blockEntity = world.getBlockEntity(triggeredAdvancementCheckerBlockPosition);
        BlockState blockState = world.getBlockState(triggeredAdvancementCheckerBlockPosition);

        if (blockEntity instanceof TriggeredAdvancementCheckerBlockEntity triggeredAdvancementCheckerBlockEntity) {
            if (!triggeredAdvancementCheckerBlockEntity.setFirstTriggeredBlockPositionOffset(firstTriggeredBlockPositionOffset)) {
                player.sendMessage(Text.translatable("triggered_advancement_checker_block.firstTriggeredBlockPositionOffset.invalid"), false);
                updateSuccessful = false;
            }
            if (!triggeredAdvancementCheckerBlockEntity.setSecondTriggeredBlockPositionOffset(secondTriggeredBlockPositionOffset)) {
                player.sendMessage(Text.translatable("triggered_advancement_checker_block.secondTriggeredBlockPositionOffset.invalid"), false);
                updateSuccessful = false;
            }
            if (!triggeredAdvancementCheckerBlockEntity.setCheckedAdvancementIdentifier(checkedAdvancementIdentifier)) {
                player.sendMessage(Text.translatable("triggered_advancement_checker_block.checkedAdvancementIdentifier.invalid"), false);
                updateSuccessful = false;
            }
            if (updateSuccessful) {
                player.sendMessage(Text.translatable("triggered_advancement_checker_block.update_successful"), true);
            }
            triggeredAdvancementCheckerBlockEntity.markDirty();
            world.updateListeners(triggeredAdvancementCheckerBlockPosition, blockState, blockState, Block.NOTIFY_ALL);
        }
    }
}
