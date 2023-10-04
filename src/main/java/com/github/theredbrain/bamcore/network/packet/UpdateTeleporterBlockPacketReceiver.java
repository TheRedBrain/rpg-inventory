package com.github.theredbrain.bamcore.network.packet;
// TODO move to bamdimensions
//import com.github.theredbrain.bamcore.block.entity.TeleporterBlockBlockEntity;
//import net.fabricmc.fabric.api.networking.v1.PacketSender;
//import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
//import net.minecraft.block.Block;
//import net.minecraft.block.BlockState;
//import net.minecraft.block.entity.BlockEntity;
//import net.minecraft.network.PacketByteBuf;
//import net.minecraft.server.MinecraftServer;
//import net.minecraft.server.network.ServerPlayNetworkHandler;
//import net.minecraft.server.network.ServerPlayerEntity;
//import net.minecraft.text.Text;
//import net.minecraft.util.math.BlockPos;
//import net.minecraft.util.math.Vec3i;
//import net.minecraft.world.World;
//
//public class UpdateTeleporterBlockPacketReceiver implements ServerPlayNetworking.PlayChannelHandler {
//
//    @Override
//    public void receive(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
//
//        if (!player.isCreativeLevelTwoOp()) {
//            return;
//        }
//        // teleporter block position
//        BlockPos teleporterBlockPos = buf.readBlockPos();
//
//        // teleporter block information
//        String teleporterName = buf.readString();
//        boolean showActivationArea = buf.readBoolean();
//
//        int activationAreaDimensionsX = buf.readInt();
//        int activationAreaDimensionsY = buf.readInt();
//        int activationAreaDimensionsZ = buf.readInt();
//        Vec3i activationAreaDimensions = new Vec3i(activationAreaDimensionsX, activationAreaDimensionsY, activationAreaDimensionsZ);
//        BlockPos activationAreaPositionOffset = buf.readBlockPos();
//
//        int dimensionMode = buf.readInt();
//        String outgoingTeleportDimension = buf.readString();
//
//        boolean showAdventureScreen = buf.readBoolean();
//
//        boolean indirectTeleportationMode = buf.readBoolean();
//        BlockPos outgoingTeleportTeleporterPosition = buf.readBlockPos();
//        BlockPos incomingTeleportPositionOffset = buf.readBlockPos();
//        double incomingTeleportPositionYaw = buf.readDouble();
//        double incomingTeleportPositionPitch = buf.readDouble();
//        BlockPos outgoingTeleportPosition = buf.readBlockPos();
//        double outgoingTeleportPositionYaw = buf.readDouble();
//        double outgoingTeleportPositionPitch = buf.readDouble();
//
//        String targetDungeonStructureIdentifier = buf.readString();
//        BlockPos targetDungeonStructureStartPosition = buf.readBlockPos();
//        int targetDungeonChunkX = buf.readInt();
//        int targetDungeonChunkZ = buf.readInt();
//        BlockPos regenerateTargetDungeonTriggerBlockPosition = buf.readBlockPos();
//
//        boolean consumeKeyItemStack = buf.readBoolean();
//        String teleportButtonLabel = buf.readString();
//        String cancelTeleportButtonLabel = buf.readString();
//
//        server.execute(() -> {
//            World world = player.getWorld();
//
//            boolean updateSuccessful = true;
////            RPGMod.LOGGER.info("showAdventureScreen: " + showAdventureScreen);
//
//            BlockEntity blockEntity = world.getBlockEntity(teleporterBlockPos);
//            BlockState blockState = world.getBlockState(teleporterBlockPos);
//
//            if (blockEntity instanceof TeleporterBlockBlockEntity teleporterBlockBlockEntity) {
//                if (!teleporterBlockBlockEntity.setTeleporterName(teleporterName)) {
//                    player.sendMessage(Text.translatable("teleporter_block.teleporterName.invalid"), false);
//                    updateSuccessful = false;
//                }
//                if (!teleporterBlockBlockEntity.setShowActivationArea(showActivationArea)) {
//                    player.sendMessage(Text.translatable("teleporter_block.showActivationArea.invalid"), false);
//                    updateSuccessful = false;
//                }
//                if (!teleporterBlockBlockEntity.setActivationAreaDimensions(activationAreaDimensions)) {
//                    player.sendMessage(Text.translatable("teleporter_block.activationAreaDimensions.invalid"), false);
//                    updateSuccessful = false;
//                }
//                if (!teleporterBlockBlockEntity.setActivationAreaPositionOffset(activationAreaPositionOffset)) {
//                    player.sendMessage(Text.translatable("teleporter_block.activationAreaPositionOffset.invalid"), false);
//                    updateSuccessful = false;
//                }
//                if (!teleporterBlockBlockEntity.setDimensionMode(dimensionMode)) {
//                    player.sendMessage(Text.translatable("teleporter_block.dimensionMode.invalid"), false);
//                    updateSuccessful = false;
//                }
//                if (!teleporterBlockBlockEntity.setOutgoingTeleportDimension(outgoingTeleportDimension)) {
//                    player.sendMessage(Text.translatable("teleporter_block.outgoingTeleportDimension.invalid"), false);
//                    updateSuccessful = false;
//                }
//                if (!teleporterBlockBlockEntity.setShowAdventureScreen(showAdventureScreen)) {
//                    player.sendMessage(Text.translatable("teleporter_block.showAdventureScreen.invalid"), false);
//                    updateSuccessful = false;
//                }
//                if (!teleporterBlockBlockEntity.setIndirectTeleportationMode(indirectTeleportationMode)) {
//                    player.sendMessage(Text.translatable("teleporter_block.indirectTeleportationMode.invalid"), false);
//                    updateSuccessful = false;
//                }
//                if (!teleporterBlockBlockEntity.setOutgoingTeleportTeleporterPosition(outgoingTeleportTeleporterPosition)) {
//                    player.sendMessage(Text.translatable("teleporter_block.outgoingTeleportTeleporterPosition.invalid"), false);
//                    updateSuccessful = false;
//                }
//                if (!teleporterBlockBlockEntity.setIncomingTeleportPositionOffset(incomingTeleportPositionOffset)) {
//                    player.sendMessage(Text.translatable("teleporter_block.incomingTeleportPositionOffset.invalid"), false);
//                    updateSuccessful = false;
//                }
//                if (!teleporterBlockBlockEntity.setIncomingTeleportPositionYaw(incomingTeleportPositionYaw)) {
//                    player.sendMessage(Text.translatable("teleporter_block.incomingTeleportPositionYaw.invalid"), false);
//                    updateSuccessful = false;
//                }
//                if (!teleporterBlockBlockEntity.setIncomingTeleportPositionPitch(incomingTeleportPositionPitch)) {
//                    player.sendMessage(Text.translatable("teleporter_block.incomingTeleportPositionPitch.invalid"), false);
//                    updateSuccessful = false;
//                }
//                if (!teleporterBlockBlockEntity.setOutgoingTeleportPosition(outgoingTeleportPosition)) {
//                    player.sendMessage(Text.translatable("teleporter_block.outgoingTeleportPosition.invalid"), false);
//                    updateSuccessful = false;
//                }
//                if (!teleporterBlockBlockEntity.setOutgoingTeleportPositionYaw(outgoingTeleportPositionYaw)) {
//                    player.sendMessage(Text.translatable("teleporter_block.outgoingTeleportPositionYaw.invalid"), false);
//                    updateSuccessful = false;
//                }
//                if (!teleporterBlockBlockEntity.setOutgoingTeleportPositionPitch(outgoingTeleportPositionPitch)) {
//                    player.sendMessage(Text.translatable("teleporter_block.outgoingTeleportPositionPitch.invalid"), false);
//                    updateSuccessful = false;
//                }
//                if (!teleporterBlockBlockEntity.setTargetDungeonStructureIdentifier(targetDungeonStructureIdentifier)) {
//                    player.sendMessage(Text.translatable("teleporter_block.targetDungeonStructureIdentifier.invalid"), false);
//                    updateSuccessful = false;
//                }
//                if (!teleporterBlockBlockEntity.setTargetDungeonStructureStartPosition(targetDungeonStructureStartPosition)) {
//                    player.sendMessage(Text.translatable("teleporter_block.targetDungeonStructureStartPosition.invalid"), false);
//                    updateSuccessful = false;
//                }
//                if (!teleporterBlockBlockEntity.setTargetDungeonChunkX(targetDungeonChunkX)) {
//                    player.sendMessage(Text.translatable("teleporter_block.targetDungeonChunkX.invalid"), false);
//                    updateSuccessful = false;
//                }
//                if (!teleporterBlockBlockEntity.setTargetDungeonChunkZ(targetDungeonChunkZ)) {
//                    player.sendMessage(Text.translatable("teleporter_block.targetDungeonChunkZ.invalid"), false);
//                    updateSuccessful = false;
//                }
//                if (!teleporterBlockBlockEntity.setRegenerateTargetDungeonTriggerBlockPosition(regenerateTargetDungeonTriggerBlockPosition)) {
//                    player.sendMessage(Text.translatable("teleporter_block.regenerateTargetDungeonTriggerBlockPosition.invalid"), false);
//                    updateSuccessful = false;
//                }
//                if (!teleporterBlockBlockEntity.setConsumeKeyItemStack(consumeKeyItemStack)) {
//                    player.sendMessage(Text.translatable("teleporter_block.consumeKeyItemStack.invalid"), false);
//                    updateSuccessful = false;
//                }
//                if (!teleporterBlockBlockEntity.setTeleportButtonLabel(teleportButtonLabel)) {
//                    player.sendMessage(Text.translatable("teleporter_block.teleportButtonLabel.invalid"), false);
//                    updateSuccessful = false;
//                }
//                if (!teleporterBlockBlockEntity.setCancelTeleportButtonLabel(cancelTeleportButtonLabel)) {
//                    player.sendMessage(Text.translatable("teleporter_block.cancelTeleportButtonLabel.invalid"), false);
//                    updateSuccessful = false;
//                }
//                if (updateSuccessful) {
//                    player.sendMessage(Text.translatable("teleporter_block.update_successful"), true);
//                }
//                teleporterBlockBlockEntity.markDirty();
//                world.updateListeners(teleporterBlockPos, blockState, blockState, Block.NOTIFY_ALL);
//            }
//        });
//    }
//}
