package com.github.theredbrain.betteradventuremode.network.packet;
//
//import com.github.theredbrain.betteradventuremode.block.entity.TriggeredMessageBlockEntity;
//import net.fabricmc.fabric.api.networking.v1.PacketSender;
//import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
//import net.minecraft.block.Block;
//import net.minecraft.block.BlockState;
//import net.minecraft.block.entity.BlockEntity;
//import net.minecraft.server.network.ServerPlayerEntity;
//import net.minecraft.text.Text;
//import net.minecraft.util.math.BlockPos;
//import net.minecraft.util.math.Vec3i;
//import net.minecraft.world.World;
//
//public class UpdateTriggeredMessageBlockPacketReceiver implements ServerPlayNetworking.PlayPacketHandler<UpdateTriggeredMessageBlockPacket> {
//    @Override
//    public void receive(UpdateTriggeredMessageBlockPacket packet, ServerPlayerEntity player, PacketSender responseSender) {
//
//        if (!player.isCreativeLevelTwoOp()) {
//            return;
//        }
//
//        BlockPos triggeredMessageBlockPosition = packet.triggeredMessageBlockPosition;
//
//        boolean showMessageArea = packet.showMessageArea;
//
//        Vec3i messageAreaDimensions = packet.messageAreaDimensions;
//
//        BlockPos messageAreaPositionOffset = packet.messageAreaPositionOffset;
//
//        String message = packet.message;
//
//        boolean overlay = packet.overlay;
//
//        TriggeredMessageBlockEntity.TriggerMode triggerMode = packet.triggerMode;
//
//        World world = player.getWorld();
//
//        boolean updateSuccessful = true;
//
//        BlockEntity blockEntity = world.getBlockEntity(triggeredMessageBlockPosition);
//        BlockState blockState = world.getBlockState(triggeredMessageBlockPosition);
//
//        if (blockEntity instanceof TriggeredMessageBlockEntity triggeredMessageBlockEntity) {
//            triggeredMessageBlockEntity.setShowMessageArea(showMessageArea);
//            if (!triggeredMessageBlockEntity.setMessageAreaDimensions(messageAreaDimensions)) {
//                player.sendMessage(Text.translatable("triggered_message_block.messageAreaDimensions.invalid"), false);
//                updateSuccessful = false;
//            }
//            if (!triggeredMessageBlockEntity.setMessageAreaPositionOffset(messageAreaPositionOffset)) {
//                player.sendMessage(Text.translatable("triggered_message_block.messageAreaPositionOffset.invalid"), false);
//                updateSuccessful = false;
//            }
//            triggeredMessageBlockEntity.setMessage(message);
//            triggeredMessageBlockEntity.setOverlay(overlay);
//            triggeredMessageBlockEntity.setTriggerMode(triggerMode);
//            if (updateSuccessful) {
//                player.sendMessage(Text.translatable("triggered_message_block.update_successful"), true);
//            }
//            triggeredMessageBlockEntity.markDirty();
//            world.updateListeners(triggeredMessageBlockPosition, blockState, blockState, Block.NOTIFY_ALL);
//        }
//    }
//}
