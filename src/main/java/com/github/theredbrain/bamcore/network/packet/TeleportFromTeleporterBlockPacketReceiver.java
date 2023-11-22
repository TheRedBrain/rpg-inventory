package com.github.theredbrain.bamcore.network.packet;

import com.github.theredbrain.bamcore.api.dimensions.PlayerDungeon;
import com.github.theredbrain.bamcore.api.dimensions.PlayerHouse;
import com.github.theredbrain.bamcore.block.entity.DungeonControlBlockEntity;
import com.github.theredbrain.bamcore.block.entity.HousingBlockBlockEntity;
import com.github.theredbrain.bamcore.registry.ComponentsRegistry;
import com.github.theredbrain.bamcore.registry.PlayerDungeonsRegistry;
import com.github.theredbrain.bamcore.registry.PlayerHousesRegistry;
import com.github.theredbrain.bamcore.world.DimensionsManager;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.apache.commons.lang3.tuple.MutablePair;

public class TeleportFromTeleporterBlockPacketReceiver implements ServerPlayNetworking.PlayPacketHandler<TeleportFromTeleporterBlockPacket> {
    @Override
    public void receive(TeleportFromTeleporterBlockPacket packet, ServerPlayerEntity player, PacketSender responseSender) {

        BlockPos teleportBlockPosition = packet.teleportBlockPosition;

        int teleportationMode = packet.teleportationMode;

        BlockPos directTeleportPositionOffset = packet.directTeleportPositionOffset;
        double directTeleportPositionOffsetYaw = packet.directTeleportPositionOffsetYaw;
        double directTeleportPositionOffsetPitch = packet.directTeleportPositionOffsetPitch;

        int specificLocationType = packet.specificLocationType;

        String targetDimensionOwnerName = packet.targetDimensionOwnerName;
        String targetLocation = packet.targetLocation;
        String targetLocationEntrance = packet.targetLocationEntrance;
//            BetterAdventureModCore.LOGGER.info("outgoingTeleportDimension: " + outgoingTeleportDimension);
//            player.sendMessage(Text.literal("server received teleportFromTeleporterBlockPacket"), false);

//            BlockPos indirectTeleportTargetPosition = null;
//            double indirectYaw = 1000.0;
//            double indirectPitch = 1000.0;
//            String targetDimensionName;
//            RegistryKey<World> dimensionregistryKey;
        ServerWorld serverWorld = player.getServerWorld();
        MinecraftServer server = player.server;
//            Identifier targetDimensionId;

        ServerPlayerEntity serverPlayerEntity = server.getPlayerManager().getPlayer(player.getUuid());

        ServerWorld targetWorld = serverWorld;
        BlockPos targetPos = new BlockPos(0, 0, 0);
        double targetYaw = 0.0;
        double targetPitch = 0.0;
        if (serverPlayerEntity != null) {
            targetPos = serverPlayerEntity.getBlockPos();
            targetYaw = serverPlayerEntity.getYaw();
            targetPitch = serverPlayerEntity.getPitch();
        }
//            boolean useDynamicDimensionFallback = false;
        if (teleportationMode == 0) {
            if (serverPlayerEntity != null) {
                serverPlayerEntity.teleport(serverWorld, teleportBlockPosition.getX() + directTeleportPositionOffset.getX() + 0.5, teleportBlockPosition.getY() + directTeleportPositionOffset.getY(), teleportBlockPosition.getZ() + directTeleportPositionOffset.getZ() + 0.5, (float) directTeleportPositionOffsetYaw, (float) directTeleportPositionOffsetPitch);
            }
            return;
        } else if (teleportationMode == 1) {
            Pair<Pair<String, BlockPos>, Boolean> housing_access_pos = ComponentsRegistry.HOUSING_ACCESS_POS.get(player).getValue();
            if (specificLocationType == 2 && housing_access_pos.getRight()) {
                targetWorld = server.getWorld(RegistryKey.of(RegistryKeys.WORLD, new Identifier(housing_access_pos.getLeft().getLeft())));
                targetPos = housing_access_pos.getLeft().getRight();
            } else if (specificLocationType == 1 && serverPlayerEntity != null) {
                targetWorld = server.getWorld(serverPlayerEntity.getSpawnPointDimension());
                targetPos = serverPlayerEntity.getSpawnPointPosition();
            } else {
                targetWorld = server.getOverworld();
                targetPos = server.getOverworld().getSpawnPos();
            }
            if (serverPlayerEntity != null && targetPos != null) {
                serverPlayerEntity.teleport(targetWorld, targetPos.getX(), targetPos.getY(), targetPos.getZ(), 0.0F, 0.0F);
            }
            return;
        } else if (teleportationMode == 2) {
            ServerPlayerEntity targetDimensionOwner = server.getPlayerManager().getPlayer(targetDimensionOwnerName);
            if (targetDimensionOwner == null) {
                targetDimensionOwner = serverPlayerEntity;
            }
            Identifier targetDimensionId = new Identifier(ComponentsRegistry.PLAYER_SPECIFIC_DIMENSION_IDS.get(targetDimensionOwner).getValue("dungeon_dimension"));
            RegistryKey<World> dimensionregistryKey = RegistryKey.of(RegistryKeys.WORLD, targetDimensionId);
            targetWorld = server.getWorld(dimensionregistryKey);

            if (targetWorld == null) {
                DimensionsManager.addAndSaveDynamicDimension(targetDimensionId, server, "dungeon_dimension");
                dimensionregistryKey = RegistryKey.of(RegistryKeys.WORLD, targetDimensionId);
                targetWorld = server.getWorld(dimensionregistryKey);
//                    if (targetDimensionOwner != null) {
                ComponentsRegistry.PLAYER_SPECIFIC_DIMENSION_IDS.get(targetDimensionOwner).setStatus("dungeon_dimension", true);
//                    } else {
//                        // this should not happen, as targetDimensionOwner should be coming from a list of existing players
//                        ComponentsRegistry.PLAYER_SPECIFIC_DIMENSION_IDS.get(player).setStatus("housing_dimension", true);
//                    }
            }

            PlayerDungeon dungeon = PlayerDungeonsRegistry.getDungeon(Identifier.tryParse(targetLocation));
            if (targetWorld != null && dungeon != null && targetWorld.getBlockEntity(dungeon.controlBlockPos()) instanceof DungeonControlBlockEntity dungeonControlBlock) {
//                    MutablePair<BlockPos, MutablePair<Double, Double>> sideEntrance = !Objects.equals(targetLocationEntrance, "") ? dungeonControlBlock.getSideEntrances().get(targetLocationEntrance) : null;
//                    BlockPos targetPos;
//                    if (sideEntrance != null) {
//                        targetPos = sideEntrance.getLeft();
//                        targetYaw = sideEntrance.getRight().getLeft();
//                        targetPitch = sideEntrance.getRight().getRight();
//                    } else {
                MutablePair<BlockPos, MutablePair<Double, Double>> mainEntrance = dungeonControlBlock.getMainEntrance();
                targetPos = mainEntrance.getLeft();
                targetYaw = mainEntrance.getRight().getLeft();
                targetPitch = mainEntrance.getRight().getRight();
//                    }
            }
        } else if (teleportationMode == 3) {
            ServerPlayerEntity targetDimensionOwner = server.getPlayerManager().getPlayer(targetDimensionOwnerName);
            if (targetDimensionOwner == null) {
                targetDimensionOwner = serverPlayerEntity;
            }
            Identifier targetDimensionId = new Identifier(ComponentsRegistry.PLAYER_SPECIFIC_DIMENSION_IDS.get(targetDimensionOwner).getValue("housing_dimension"));
            RegistryKey<World> dimensionregistryKey = RegistryKey.of(RegistryKeys.WORLD, targetDimensionId);
            targetWorld = server.getWorld(dimensionregistryKey);

            if (targetWorld == null) {
                DimensionsManager.addAndSaveDynamicDimension(targetDimensionId, server, "housing_dimension");
                dimensionregistryKey = RegistryKey.of(RegistryKeys.WORLD, targetDimensionId);
                targetWorld = server.getWorld(dimensionregistryKey);
//                    if (targetDimensionOwner != null) {
                ComponentsRegistry.PLAYER_SPECIFIC_DIMENSION_IDS.get(targetDimensionOwner).setStatus("housing_dimension", true);
//                    } else {
//                        // this should not happen, as targetDimensionOwner should be coming from a list of existing players
//                        ComponentsRegistry.PLAYER_SPECIFIC_DIMENSION_IDS.get(player).setStatus("housing_dimension", true);
//                    }
            }

            PlayerHouse playerHouse = PlayerHousesRegistry.getHouse(Identifier.tryParse(targetLocation));
            if (targetWorld != null && playerHouse != null && targetWorld.getBlockEntity(playerHouse.housingBlockPos()) instanceof HousingBlockBlockEntity housingBlock) {
                MutablePair<BlockPos, MutablePair<Double, Double>> entrance = housingBlock.getEntrance();
                targetPos = entrance.getLeft();
                targetYaw = entrance.getRight().getLeft();
                targetPitch = entrance.getRight().getRight();
            }
        }

        if (serverPlayerEntity != null) {
            serverPlayerEntity.teleport(targetWorld, targetPos.getX(), targetPos.getY(), targetPos.getZ(), (float) targetYaw, (float) targetPitch);
        }

//            // static dimension mode, always target specified dimension
////                BetterAdventureModCore.LOGGER.info("static teleportationMode: " + teleportationMode);
//                targetDimensionName = outgoingTeleportDimension;
//                targetDimensionId = new Identifier(targetDimensionName);
//                dimensionregistryKey = RegistryKey.of(RegistryKeys.WORLD, targetDimensionId);
//                serverWorld = server.getWorld(dimensionregistryKey);
//
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
//
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
////                    if (blockEntity instanceof TeleporterBlockBlockEntity teleporterBlockBlockEntity) {
////                        incomingTeleportPositionOffset = teleporterBlockBlockEntity.getIncomingTeleportPositionOffset();
////                        indirectTeleportTargetPosition = teleporterBlockBlockEntity.getPos().add(incomingTeleportPositionOffset);
////                        indirectYaw = teleporterBlockBlockEntity.getIncomingTeleportPositionYaw();
////                        indirectPitch = teleporterBlockBlockEntity.getIncomingTeleportPositionPitch();
////                    }
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
////            server.getPlayerManager().getPlayer("").teleport();
    }
}
