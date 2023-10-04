package com.github.theredbrain.bamcore.network.packet;
// TODO move to bamdimensions
//import com.github.theredbrain.bamcore.block.entity.TeleporterBlockBlockEntity;
//import com.github.theredbrain.bamcore.registry.ComponentsRegistry;
//import com.github.theredbrain.bamcore.world.DimensionsManager;
//import net.fabricmc.fabric.api.networking.v1.PacketSender;
//import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
//import net.minecraft.block.entity.BlockEntity;
//import net.minecraft.network.PacketByteBuf;
//import net.minecraft.registry.RegistryKey;
//import net.minecraft.registry.RegistryKeys;
//import net.minecraft.server.MinecraftServer;
//import net.minecraft.server.network.ServerPlayNetworkHandler;
//import net.minecraft.server.network.ServerPlayerEntity;
//import net.minecraft.server.world.ServerWorld;
//import net.minecraft.util.Identifier;
//import net.minecraft.util.math.BlockPos;
//import net.minecraft.world.World;
//
//public class TeleportFromTeleporterBlockPacketReceiver implements ServerPlayNetworking.PlayChannelHandler {
//
//    @Override
//    public void receive(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
//
//        String targetDimensionOwnerName = buf.readString();
//
//        int dimensionMode = buf.readInt();
//        String outgoingTeleportDimension = buf.readString();
//
//        boolean indirectTeleportationMode = buf.readBoolean();
//        BlockPos outgoingTeleportTeleporterPosition = buf.readBlockPos();
//
//        BlockPos directTeleportTargetPosition = buf.readBlockPos();
//        double outgoingTeleportPositionYaw = buf.readDouble();
//        double outgoingTeleportPositionPitch = buf.readDouble();
//
////        boolean regenerateDungeon = buf.readBoolean(); // TODO remove
//
//        // TODO convert to a map of multiple pairs for bigger dungeons
//        String targetDungeonStructureIdentifier = buf.readString();
//        BlockPos targetDungeonStructureStartPosition = buf.readBlockPos();
//
//        int targetDungeonChunkX = buf.readInt();
//        int targetDungeonChunkZ = buf.readInt();
////        BlockPos regenerateTargetDungeonTriggerBlockPosition = buf.readBlockPos(); // TODO remove
//
//        server.execute(() -> {
////            BetterAdventureModCore.LOGGER.info("outgoingTeleportDimension: " + outgoingTeleportDimension);
////            player.sendMessage(Text.literal("server received teleportFromTeleporterBlockPacket"), false);
//            BlockPos indirectTeleportTargetPosition = null;
//            double indirectYaw = 1000.0;
//            double indirectPitch = 1000.0;
//            String targetDimensionName;
//            RegistryKey<World> dimensionregistryKey;
//            ServerWorld serverWorld;
//            Identifier targetDimensionId;
//            boolean useDynamicDimensionFallback = false;
//            if (dimensionMode == 0 || dimensionMode == 2) {
//                // static dimension mode, always target specified dimension
////                BetterAdventureModCore.LOGGER.info("static dimensionMode: " + dimensionMode);
//                targetDimensionName = outgoingTeleportDimension;
//                targetDimensionId = new Identifier(targetDimensionName);
//                dimensionregistryKey = RegistryKey.of(RegistryKeys.WORLD, targetDimensionId);
//                serverWorld = server.getWorld(dimensionregistryKey);
//            } else {
//                // dynamic dimension mode, use specified string to get dimension from player
//                ServerPlayerEntity targetDimensionOwner = server.getPlayerManager().getPlayer(targetDimensionOwnerName);
//                if (targetDimensionOwner != null) {
//                    targetDimensionName = ComponentsRegistry.PLAYER_SPECIFIC_DIMENSION_IDS.get(targetDimensionOwner).getValue(outgoingTeleportDimension);
////                    BetterAdventureModCore.LOGGER.info(outgoingTeleportDimension + " of " + targetDimensionOwner.getName().getString() + ": " + ComponentsRegistry.PLAYER_SPECIFIC_DIMENSION_IDS.get(targetDimensionOwner).getValue(outgoingTeleportDimension) + " is loaded:" + ComponentsRegistry.PLAYER_SPECIFIC_DIMENSION_IDS.get(targetDimensionOwner).getStatus(outgoingTeleportDimension));
//                } else {
//                    // this should not happen, as targetDimensionOwner should be coming from a list of existing players
//                    targetDimensionName = ComponentsRegistry.PLAYER_SPECIFIC_DIMENSION_IDS.get(player).getValue(outgoingTeleportDimension);
////                    BetterAdventureModCore.LOGGER.info("This should not have happened");
////                    BetterAdventureModCore.LOGGER.info(outgoingTeleportDimension + " of " + player.getName().getString() + ": " + ComponentsRegistry.PLAYER_SPECIFIC_DIMENSION_IDS.get(player).getValue(outgoingTeleportDimension) + " is loaded:" + ComponentsRegistry.PLAYER_SPECIFIC_DIMENSION_IDS.get(player).getStatus(outgoingTeleportDimension));
//                }
//                targetDimensionId = new Identifier(targetDimensionName);
//
//                if (targetDimensionId.getPath().equals("")) {
//                    dimensionregistryKey = World.OVERWORLD;
//                    useDynamicDimensionFallback = true;
//                    targetDimensionName = dimensionregistryKey.getValue().getPath();
//                } else {
//                    dimensionregistryKey = RegistryKey.of(RegistryKeys.WORLD, targetDimensionId);
//                }
//
//                serverWorld = server.getWorld(dimensionregistryKey);
//
//                if (serverWorld == null) {
//                    DimensionsManager.addAndSaveDynamicDimension(targetDimensionId, server);
//                    dimensionregistryKey = RegistryKey.of(RegistryKeys.WORLD, targetDimensionId);
//                    serverWorld = server.getWorld(dimensionregistryKey);
//                    if (targetDimensionOwner != null) {
//                        ComponentsRegistry.PLAYER_SPECIFIC_DIMENSION_IDS.get(targetDimensionOwner).setStatus(outgoingTeleportDimension, true);
//                    } else {
//                        // this should not happen, as targetDimensionOwner should be coming from a list of existing players
//                        ComponentsRegistry.PLAYER_SPECIFIC_DIMENSION_IDS.get(player).setStatus(outgoingTeleportDimension, true);
//                    }
//                }
//            }
////            BetterAdventureModCore.LOGGER.info("targetDimensionName: " + targetDimensionName);
//            if (indirectTeleportationMode && !useDynamicDimensionFallback) {
////                BetterAdventureModCore.LOGGER.info("indirectTeleportationMode && !useDynamicDimensionFallback");
//                if (serverWorld != null) {
////                    BetterAdventureModCore.LOGGER.info("serverWorld != null");
//                    BlockPos incomingTeleportPositionOffset;
//                    BlockEntity blockEntity = serverWorld.getBlockEntity(outgoingTeleportTeleporterPosition);
//                    if (!(blockEntity instanceof TeleporterBlockBlockEntity)) {
//
////                        // client chunk load
////                        ChunkLoader chunkLoader = new ChunkLoader(
////                                new DimensionalChunkPos(
////                                        dimensionregistryKey,
////                                        targetDungeonChunkX, // chunk x
////                                        targetDungeonChunkZ // chunk z
////                                ),
////                                2 // radius in chunks
////                        );
////                        PortalAPI.addChunkLoaderForPlayer(player, chunkLoader);
//
//                        // force load chunks
////                        String placeStructureCommand = "execute as " + player.getName().getString() + " in " + targetDimensionName + " run place structure " + targetDungeonStructureIdentifier + " " + targetDungeonStructureStartPosition.getX() + " " + targetDungeonStructureStartPosition.getY() + " " + targetDungeonStructureStartPosition.getZ();
//                        String forceLoadAddCommand = "execute in " + targetDimensionName + " run forceload add " + (targetDungeonChunkX - 1) + " " + (targetDungeonChunkZ - 1) + " " + (targetDungeonChunkX + 1) + " " + (targetDungeonChunkZ + 2);
////                        BetterAdventureModCore.LOGGER.info("forceload add command: " + forceLoadAddCommand);
//                        server.getCommandManager().executeWithPrefix(server.getCommandSource(), forceLoadAddCommand);
//
//
//                        // place structure
////                        String placeStructureCommand = "execute as " + player.getName().getString() + " in " + targetDimensionName + " run place structure " + targetDungeonStructureIdentifier + " " + targetDungeonStructureStartPosition.getX() + " " + targetDungeonStructureStartPosition.getY() + " " + targetDungeonStructureStartPosition.getZ();
//                        String placeStructureCommand = "execute in " + targetDimensionName + " run place structure " + targetDungeonStructureIdentifier + " " + targetDungeonStructureStartPosition.getX() + " " + targetDungeonStructureStartPosition.getY() + " " + targetDungeonStructureStartPosition.getZ();
////                        BetterAdventureModCore.LOGGER.info("place structure command: " + placeStructureCommand);
//                        server.getCommandManager().executeWithPrefix(server.getCommandSource(), placeStructureCommand);
//
//                        // force load chunks
////                        String placeStructureCommand = "execute as " + player.getName().getString() + " in " + targetDimensionName + " run place structure " + targetDungeonStructureIdentifier + " " + targetDungeonStructureStartPosition.getX() + " " + targetDungeonStructureStartPosition.getY() + " " + targetDungeonStructureStartPosition.getZ();
//                        String forceLoadRemoveAllCommand = "execute in " + targetDimensionName + " run forceload remove " + (targetDungeonChunkX - 1) + " " + (targetDungeonChunkZ - 1) + " " + (targetDungeonChunkX + 1) + " " + (targetDungeonChunkZ + 2);
////                        BetterAdventureModCore.LOGGER.info("forceload remove all command: " + forceLoadRemoveAllCommand);
//                        server.getCommandManager().executeWithPrefix(server.getCommandSource(), forceLoadRemoveAllCommand);
//
////                        // client chunk unload
////                        PortalAPI.removeChunkLoaderForPlayer(player, chunkLoader);
//
//                        blockEntity = serverWorld.getBlockEntity(outgoingTeleportTeleporterPosition);
//                    }
//                    if (blockEntity instanceof TeleporterBlockBlockEntity teleporterBlockBlockEntity) {
//                        incomingTeleportPositionOffset = teleporterBlockBlockEntity.getIncomingTeleportPositionOffset();
//                        indirectTeleportTargetPosition = teleporterBlockBlockEntity.getPos().add(incomingTeleportPositionOffset);
//                        indirectYaw = teleporterBlockBlockEntity.getIncomingTeleportPositionYaw();
//                        indirectPitch = teleporterBlockBlockEntity.getIncomingTeleportPositionPitch();
//                    }
//                }
//            }
//            String command = "";
//            if (indirectTeleportTargetPosition != null && indirectYaw != 1000.0 && indirectPitch != 1000.0) {
////                BetterAdventureModCore.LOGGER.info("indirect teleport");
//                command = "/execute as " + player.getName().getString() + " in " + targetDimensionName + " run teleport " + player.getName().getString() + " " + indirectTeleportTargetPosition.getX() + " " + indirectTeleportTargetPosition.getY() + " " + indirectTeleportTargetPosition.getZ() + " " + indirectYaw + " " + indirectPitch;
////                command = "/execute in " + outgoingTeleportDimension + " run teleport " + player.getName() + " " + indirectTeleportTargetPosition.getX() + " " + indirectTeleportTargetPosition.getY() + " " + indirectTeleportTargetPosition.getZ() + " " + indirectYaw + " " + indirectPitch;
//            } else {
////                BetterAdventureModCore.LOGGER.info("direct teleport");
//                command = "execute as " + player.getName().getString() + " in " + targetDimensionName + " run teleport " + player.getName().getString() + " " + directTeleportTargetPosition.getX() + " " + directTeleportTargetPosition.getY() + " " + directTeleportTargetPosition.getZ() + " " + outgoingTeleportPositionYaw + " " + outgoingTeleportPositionPitch;
////                command = "execute in " + outgoingTeleportDimension + " run teleport " + player.getName().getString() + " " + directTeleportTargetPosition.getX() + " " + directTeleportTargetPosition.getY() + " " + directTeleportTargetPosition.getZ() + " " + outgoingTeleportPositionYaw + " " + outgoingTeleportPositionPitch;
//            }
////            BetterAdventureModCore.LOGGER.info("teleport command: " + command);
//            server.getCommandManager().executeWithPrefix(server.getCommandSource(), command);
//        });
//    }
//}
