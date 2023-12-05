package com.github.theredbrain.bamcore.network.packet;

import com.github.theredbrain.bamcore.BetterAdventureModeCore;
import com.github.theredbrain.bamcore.api.dimensions.PlayerDungeon;
import com.github.theredbrain.bamcore.api.dimensions.PlayerHouse;
import com.github.theredbrain.bamcore.block.entity.DungeonControlBlockEntity;
import com.github.theredbrain.bamcore.block.entity.HousingBlockBlockEntity;
import com.github.theredbrain.bamcore.block.entity.TeleporterBlockBlockEntity;
import com.github.theredbrain.bamcore.registry.ComponentsRegistry;
import com.github.theredbrain.bamcore.registry.PlayerDungeonsRegistry;
import com.github.theredbrain.bamcore.registry.PlayerHousesRegistry;
import com.github.theredbrain.bamcore.world.DimensionsManager;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import org.apache.commons.lang3.tuple.MutablePair;

public class TeleportFromTeleporterBlockPacketReceiver implements ServerPlayNetworking.PlayPacketHandler<TeleportFromTeleporterBlockPacket> {
    @Override
    public void receive(TeleportFromTeleporterBlockPacket packet, ServerPlayerEntity serverPlayerEntity, PacketSender responseSender) {

        BlockPos teleportBlockPosition = packet.teleportBlockPosition;

        String accessPositionDimension = packet.accessPositionDimension;
        BlockPos accessPositionOffset = packet.accessPositionOffset;
        boolean setAccessPosition = packet.setAccessPosition;

        TeleporterBlockBlockEntity.TeleportationMode teleportationMode = packet.teleportationMode;

        BlockPos directTeleportPositionOffset = packet.directTeleportPositionOffset;
        double directTeleportOrientationYaw = packet.directTeleportOrientationYaw;
        double directTeleportOrientationPitch = packet.directTeleportOrientationPitch;

        TeleporterBlockBlockEntity.LocationType locationType = packet.locationType;

        String targetDimensionOwnerName = packet.targetDimensionOwnerName;
        String targetLocation = packet.targetLocation;
        String targetLocationEntrance = packet.targetLocationEntrance;
//            BetterAdventureModCore.LOGGER.info("outgoingTeleportDimension: " + outgoingTeleportDimension);
//            serverPlayerEntity.sendMessage(Text.literal("server received teleportFromTeleporterBlockPacket"), false);

//            BlockPos indirectTeleportTargetPosition = null;
//            double indirectYaw = 1000.0;
//            double indirectPitch = 1000.0;
//            String targetDimensionName;
//            RegistryKey<World> dimensionregistryKey;
        ServerWorld serverWorld = serverPlayerEntity.getServerWorld();
        MinecraftServer server = serverPlayerEntity.server;
//            Identifier targetDimensionId;

        ServerWorld targetWorld = null;
        BlockPos targetPos = null;
        double targetYaw = 0.0;
        double targetPitch = 0.0;
        BlockState testBlock = null;
        if (teleportationMode == TeleporterBlockBlockEntity.TeleportationMode.DIRECT) {
            targetWorld = serverWorld;
            targetPos = new BlockPos(teleportBlockPosition.getX() + directTeleportPositionOffset.getX(), teleportBlockPosition.getY() + directTeleportPositionOffset.getY(), teleportBlockPosition.getZ() + directTeleportPositionOffset.getZ());
            targetYaw = directTeleportOrientationYaw;
            targetPitch = directTeleportOrientationPitch;
        } else if (teleportationMode == TeleporterBlockBlockEntity.TeleportationMode.SAVED_LOCATIONS) {
            Pair<Pair<String, BlockPos>, Boolean> housing_access_pos = ComponentsRegistry.HOUSING_ACCESS_POS.get(serverPlayerEntity).getValue();
            if (locationType == TeleporterBlockBlockEntity.LocationType.DIMENSION_ACCESS_POSITION && housing_access_pos.getRight()) {
                targetWorld = server.getWorld(RegistryKey.of(RegistryKeys.WORLD, new Identifier(housing_access_pos.getLeft().getLeft())));
                targetPos = housing_access_pos.getLeft().getRight();
                if (targetWorld != null && targetPos != null) {
                    ComponentsRegistry.HOUSING_ACCESS_POS.get(serverPlayerEntity).deactivate();
                }
            } else if (locationType == TeleporterBlockBlockEntity.LocationType.PLAYER_SPAWN) {
                targetWorld = server.getWorld(serverPlayerEntity.getSpawnPointDimension());
                targetPos = serverPlayerEntity.getSpawnPointPosition();
            } else {
                targetWorld = server.getOverworld();
                targetPos = server.getOverworld().getSpawnPos();
            }
        } else if (teleportationMode == TeleporterBlockBlockEntity.TeleportationMode.DUNGEONS) {
            ServerPlayerEntity targetDimensionOwner = server.getPlayerManager().getPlayer(targetDimensionOwnerName);

            if (targetDimensionOwner != null) {
                BetterAdventureModeCore.LOGGER.info("targetDimensionOwner: " + targetDimensionOwner);
                Identifier targetDimensionId = Identifier.tryParse(ComponentsRegistry.PLAYER_SPECIFIC_DIMENSION_IDS.get(targetDimensionOwner).getValue("dungeon_dimension"));
                BetterAdventureModeCore.LOGGER.info("targetDimensionId: " + targetDimensionId);
                RegistryKey<World> dimensionregistryKey = RegistryKey.of(RegistryKeys.WORLD, targetDimensionId);
                targetWorld = server.getWorld(dimensionregistryKey);

                if (targetWorld == null) {
                    DimensionsManager.addAndSaveDynamicDimension(targetDimensionId, server, true);
                    dimensionregistryKey = RegistryKey.of(RegistryKeys.WORLD, targetDimensionId);
                    targetWorld = server.getWorld(dimensionregistryKey);
//                    if (targetDimensionOwner != null) {
                    ComponentsRegistry.PLAYER_SPECIFIC_DIMENSION_IDS.get(targetDimensionOwner).setStatus("dungeon_dimension", true);
//                    } else {
//                        // this should not happen, as targetDimensionOwner should be coming from a list of existing players
//                        ComponentsRegistry.PLAYER_SPECIFIC_DIMENSION_IDS.get(serverPlayerEntity).setStatus("housing_dimension", true);
//                    }
                }

                PlayerDungeon playerDungeon = PlayerDungeonsRegistry.getDungeon(Identifier.tryParse(targetLocation));

                BetterAdventureModeCore.LOGGER.info("playerDungeon: " + playerDungeon);
                if (targetWorld != null && playerDungeon != null && targetWorld.getBlockEntity(playerDungeon.controlBlockPos()) instanceof DungeonControlBlockEntity dungeonControlBlock) {
                    MutablePair<BlockPos, MutablePair<Double, Double>> targetEntrance = dungeonControlBlock.getTargetEntrance(targetLocationEntrance);
                    targetPos = targetEntrance.getLeft();
                    targetYaw = targetEntrance.getRight().getLeft();
                    targetPitch = targetEntrance.getRight().getRight();
                }
            }
        } else if (teleportationMode == TeleporterBlockBlockEntity.TeleportationMode.HOUSING) {
            ServerPlayerEntity targetDimensionOwner = server.getPlayerManager().getPlayer(targetDimensionOwnerName);

            if (targetDimensionOwner != null) {
                BetterAdventureModeCore.LOGGER.info("targetDimensionOwner: " + targetDimensionOwner);
                Identifier targetDimensionId = Identifier.tryParse(ComponentsRegistry.PLAYER_SPECIFIC_DIMENSION_IDS.get(targetDimensionOwner).getValue("housing_dimension"));
                BetterAdventureModeCore.LOGGER.info("targetDimensionId: " + targetDimensionId);
                RegistryKey<World> dimensionregistryKey = RegistryKey.of(RegistryKeys.WORLD, targetDimensionId);
                targetWorld = server.getWorld(dimensionregistryKey);

                if (targetWorld == null) {
                    DimensionsManager.addAndSaveDynamicDimension(targetDimensionId, server, false);
                    dimensionregistryKey = RegistryKey.of(RegistryKeys.WORLD, targetDimensionId);
                    targetWorld = server.getWorld(dimensionregistryKey);
//                    if (targetDimensionOwner != null) {
                    ComponentsRegistry.PLAYER_SPECIFIC_DIMENSION_IDS.get(targetDimensionOwner).setStatus("housing_dimension", true);
//                    } else {
//                        // this should not happen, as targetDimensionOwner should be coming from a list of existing players
//                        ComponentsRegistry.PLAYER_SPECIFIC_DIMENSION_IDS.get(serverPlayerEntity).setStatus("housing_dimension", true);
//                    }
                }

                PlayerHouse playerHouse = PlayerHousesRegistry.getHouse(Identifier.tryParse(targetLocation));
                BetterAdventureModeCore.LOGGER.info("playerHouse: " + playerHouse);
                if (targetWorld != null && playerHouse != null) {
//                    if (targetWorld.getBlockEntity(playerHouse.housingBlockPos()) instanceof HousingBlockBlockEntity housingBlock) {
//                    }
//                    BetterAdventureModeCore.LOGGER.info("targetWorld biome: " + server.getWorld(RegistryKey.of(RegistryKeys.WORLD, new Identifier("the_nether"))).getBlockState(playerHouse.housingBlockPos()) + " at " + playerHouse.housingBlockPos());
//                    BetterAdventureModeCore.LOGGER.info("targetWorld biome: " + targetWorld.getBiome(playerHouse.housingBlockPos()) + " at " + playerHouse.housingBlockPos());
//                    BetterAdventureModeCore.LOGGER.info("targetWorld getStructureReferences: " + targetWorld.getStructureAccessor().getStructureReferences(playerHouse.housingBlockPos()) + " at " + playerHouse.housingBlockPos());
//                    BetterAdventureModeCore.LOGGER.info("targetWorld hasStructureReferences at " + playerHouse.housingBlockPos() + ": " + targetWorld.getStructureAccessor().hasStructureReferences(playerHouse.housingBlockPos()));
//                    BetterAdventureModeCore.LOGGER.info("before client loaded chunk blockState: " + targetWorld.getBlockState(playerHouse.housingBlockPos()) + " at " + playerHouse.housingBlockPos());
                    BlockEntity blockEntity = targetWorld.getBlockEntity(playerHouse.housingBlockPos());
                    if (!(blockEntity instanceof HousingBlockBlockEntity)) {

                        BetterAdventureModeCore.LOGGER.info("before client loaded chunk blockState: " + targetWorld.getBlockState(playerHouse.housingBlockPos()) + " at " + playerHouse.housingBlockPos());
//                        BetterAdventureModeCore.LOGGER.info("overworld blockState: " + server.getWorld(RegistryKey.of(RegistryKeys.WORLD, new Identifier("the_nether"))).getBlockState(playerHouse.housingBlockPos()) + " at " + playerHouse.housingBlockPos());

//                        // client chunk load
//                        ChunkLoader chunkLoader = new ChunkLoader(
//                                new DimensionalChunkPos(
//                                        dimensionregistryKey,
//                                        targetWorld.getChunk(playerHouse.housingBlockPos()).getPos()//,//targetDungeonChunkX, // chunk x
////                                        targetDungeonChunkZ // chunk z
//                                ),
//                                2 // radius in chunks
//                        );
//                        PortalAPI.addChunkLoaderForPlayer(serverPlayerEntity, chunkLoader);

                        ChunkPos targetDungeonChunkPos = targetWorld.getChunk(playerHouse.housingBlockPos()).getPos();
//                        // force load chunks
////                        String placeStructureCommand = "execute as " + serverPlayerEntity.getName().getString() + " in " + targetDimensionName + " run place structure " + targetDungeonStructureIdentifier + " " + targetDungeonStructureStartPosition.getX() + " " + targetDungeonStructureStartPosition.getY() + " " + targetDungeonStructureStartPosition.getZ();
                        String forceLoadAddCommand = "execute in " + targetWorld.getRegistryKey().getValue() + " run forceload add " + (targetDungeonChunkPos.x - 1) + " " + (targetDungeonChunkPos.z - 1) + " " + (targetDungeonChunkPos.x + 1) + " " + (targetDungeonChunkPos.z + 2);
                        BetterAdventureModeCore.LOGGER.info("forceload add command: " + forceLoadAddCommand);
                        server.getCommandManager().executeWithPrefix(server.getCommandSource(), forceLoadAddCommand);
//
//
////                        // place structure
//////                        String placeStructureCommand = "execute as " + serverPlayerEntity.getName().getString() + " in " + targetDimensionName + " run place structure " + targetDungeonStructureIdentifier + " " + targetDungeonStructureStartPosition.getX() + " " + targetDungeonStructureStartPosition.getY() + " " + targetDungeonStructureStartPosition.getZ();
                        String placeStructureCommand = "execute in " + targetWorld.getRegistryKey().getValue() + " run place structure " + playerHouse.getStructureIdentifier() + " " + playerHouse.housingBlockPos().getX() + " " + playerHouse.housingBlockPos().getY() + " " + playerHouse.housingBlockPos().getZ();
                        BetterAdventureModeCore.LOGGER.info("place structure command: " + placeStructureCommand);
                        server.getCommandManager().executeWithPrefix(server.getCommandSource(), placeStructureCommand);
//
                        BetterAdventureModeCore.LOGGER.info("client loaded chunk blockState: " + targetWorld.getBlockState(playerHouse.housingBlockPos()));

//                        // force load chunks
////                        String placeStructureCommand = "execute as " + serverPlayerEntity.getName().getString() + " in " + targetDimensionName + " run place structure " + targetDungeonStructureIdentifier + " " + targetDungeonStructureStartPosition.getX() + " " + targetDungeonStructureStartPosition.getY() + " " + targetDungeonStructureStartPosition.getZ();
                        String forceLoadRemoveAllCommand = "execute in " + targetWorld.getRegistryKey().getValue() + " run forceload remove " + (targetDungeonChunkPos.x - 1) + " " + (targetDungeonChunkPos.z - 1) + " " + (targetDungeonChunkPos.x + 1) + " " + (targetDungeonChunkPos.z + 2);
                        BetterAdventureModeCore.LOGGER.info("forceload remove all command: " + forceLoadRemoveAllCommand);
                        server.getCommandManager().executeWithPrefix(server.getCommandSource(), forceLoadRemoveAllCommand);

//                        // client chunk unload
//                        PortalAPI.removeChunkLoaderForPlayer(serverPlayerEntity, chunkLoader);

                        BetterAdventureModeCore.LOGGER.info("client unloaded chunk blockState: " + targetWorld.getBlockState(playerHouse.housingBlockPos()));

                        BetterAdventureModeCore.LOGGER.info("before client loaded chunk blockState: " + targetWorld.getBlockState(playerHouse.housingBlockPos()) + " at " + playerHouse.housingBlockPos());
                        blockEntity = targetWorld.getBlockEntity(playerHouse.housingBlockPos());
                    }
                    BetterAdventureModeCore.LOGGER.info("blockState: " + targetWorld.getBlockState(playerHouse.housingBlockPos()) + " at " + playerHouse.housingBlockPos());
                    if (blockEntity instanceof HousingBlockBlockEntity housingBlock) {
//                        incomingTeleportPositionOffset = teleporterBlockBlockEntity.getIncomingTeleportPositionOffset();
//                        indirectTeleportTargetPosition = teleporterBlockBlockEntity.getPos().add(incomingTeleportPositionOffset);
//                        indirectYaw = teleporterBlockBlockEntity.getIncomingTeleportPositionYaw();
//                        indirectPitch = teleporterBlockBlockEntity.getIncomingTeleportPositionPitch();
                        MutablePair<BlockPos, MutablePair<Double, Double>> entrance = housingBlock.getTargetEntrance();
                        targetPos = entrance.getLeft();
                        targetYaw = entrance.getRight().getLeft();
                        targetPitch = entrance.getRight().getRight();

                        if (setAccessPosition && Identifier.isValid(accessPositionDimension)) {
                            ComponentsRegistry.HOUSING_ACCESS_POS.get(serverPlayerEntity).setValue(new Pair<>(new Pair<>(accessPositionDimension, teleportBlockPosition.add(accessPositionOffset.getX(), accessPositionOffset.getY(), accessPositionOffset.getZ())), true));
                        }
                    }
                }
            }
        }

//        BetterAdventureModeCore.LOGGER.info("targetWorld: " + targetWorld.getRegistryKey().getValue().getPath());
        if (targetWorld != null && targetPos != null) {

//            serverPlayerEntity.teleport(targetWorld, targetPos.getX(), targetPos.getY(), targetPos.getZ(), (float) targetYaw, (float) targetPitch);
            serverPlayerEntity.sendMessage(Text.of("Teleport to world: " + targetWorld + " at position: " + targetPos.getX() + ", " + targetPos.getY() + ", " + targetPos.getZ() + ", with yaw: " + targetYaw + " and pitch: " + targetPitch));
            // TODO send S2C packet which removes PortalResistanceEffect and setScreen(null)
        } else {
            serverPlayerEntity.sendMessage(Text.of("Teleport failed"));
            if (targetWorld == null) {
                serverPlayerEntity.sendMessage(Text.of("targetWorld == null"));
            }
            if (targetPos == null) {
                serverPlayerEntity.sendMessage(Text.of("targetPos == null"));
            }
        }

//            // static dimension mode, always target specified dimension
////                BetterAdventureModCore.LOGGER.info("static teleportationMode: " + teleportationMode);
//                targetDimensionName = outgoingTeleportDimension;
//                targetDimensionId = new Identifier(targetDimensionName);
//                dimensionregistryKey = RegistryKey.of(RegistryKeys.WORLD, targetDimensionId);
//                serverWorld = server.getWorld(dimensionregistryKey);
//
//                // dynamic dimension mode, use specified string to get dimension from serverPlayerEntity
//                ServerPlayerEntity targetDimensionOwner = server.getPlayerManager().getPlayer(targetDimensionOwnerName);
//                if (targetDimensionOwner != null) {
//                    targetDimensionName = ComponentsRegistry.PLAYER_SPECIFIC_DIMENSION_IDS.get(targetDimensionOwner).getValue(outgoingTeleportDimension);
////                    BetterAdventureModCore.LOGGER.info(outgoingTeleportDimension + " of " + targetDimensionOwner.getName().getString() + ": " + ComponentsRegistry.PLAYER_SPECIFIC_DIMENSION_IDS.get(targetDimensionOwner).getValue(outgoingTeleportDimension) + " is loaded:" + ComponentsRegistry.PLAYER_SPECIFIC_DIMENSION_IDS.get(targetDimensionOwner).getStatus(outgoingTeleportDimension));
//                } else {
//                    // this should not happen, as targetDimensionOwner should be coming from a list of existing players
//                    targetDimensionName = ComponentsRegistry.PLAYER_SPECIFIC_DIMENSION_IDS.get(serverPlayerEntity).getValue(outgoingTeleportDimension);
////                    BetterAdventureModCore.LOGGER.info("This should not have happened");
////                    BetterAdventureModCore.LOGGER.info(outgoingTeleportDimension + " of " + serverPlayerEntity.getName().getString() + ": " + ComponentsRegistry.PLAYER_SPECIFIC_DIMENSION_IDS.get(serverPlayerEntity).getValue(outgoingTeleportDimension) + " is loaded:" + ComponentsRegistry.PLAYER_SPECIFIC_DIMENSION_IDS.get(serverPlayerEntity).getStatus(outgoingTeleportDimension));
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
//                        ComponentsRegistry.PLAYER_SPECIFIC_DIMENSION_IDS.get(serverPlayerEntity).setStatus(outgoingTeleportDimension, true);
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
////                        PortalAPI.addChunkLoaderForPlayer(serverPlayerEntity, chunkLoader);
//
//                        // force load chunks
////                        String placeStructureCommand = "execute as " + serverPlayerEntity.getName().getString() + " in " + targetDimensionName + " run place structure " + targetDungeonStructureIdentifier + " " + targetDungeonStructureStartPosition.getX() + " " + targetDungeonStructureStartPosition.getY() + " " + targetDungeonStructureStartPosition.getZ();
//                        String forceLoadAddCommand = "execute in " + targetDimensionName + " run forceload add " + (targetDungeonChunkX - 1) + " " + (targetDungeonChunkZ - 1) + " " + (targetDungeonChunkX + 1) + " " + (targetDungeonChunkZ + 2);
////                        BetterAdventureModCore.LOGGER.info("forceload add command: " + forceLoadAddCommand);
//                        server.getCommandManager().executeWithPrefix(server.getCommandSource(), forceLoadAddCommand);
//
//
//                        // place structure
////                        String placeStructureCommand = "execute as " + serverPlayerEntity.getName().getString() + " in " + targetDimensionName + " run place structure " + targetDungeonStructureIdentifier + " " + targetDungeonStructureStartPosition.getX() + " " + targetDungeonStructureStartPosition.getY() + " " + targetDungeonStructureStartPosition.getZ();
//                        String placeStructureCommand = "execute in " + targetDimensionName + " run place structure " + targetDungeonStructureIdentifier + " " + targetDungeonStructureStartPosition.getX() + " " + targetDungeonStructureStartPosition.getY() + " " + targetDungeonStructureStartPosition.getZ();
////                        BetterAdventureModCore.LOGGER.info("place structure command: " + placeStructureCommand);
//                        server.getCommandManager().executeWithPrefix(server.getCommandSource(), placeStructureCommand);
//
//                        // force load chunks
////                        String placeStructureCommand = "execute as " + serverPlayerEntity.getName().getString() + " in " + targetDimensionName + " run place structure " + targetDungeonStructureIdentifier + " " + targetDungeonStructureStartPosition.getX() + " " + targetDungeonStructureStartPosition.getY() + " " + targetDungeonStructureStartPosition.getZ();
//                        String forceLoadRemoveAllCommand = "execute in " + targetDimensionName + " run forceload remove " + (targetDungeonChunkX - 1) + " " + (targetDungeonChunkZ - 1) + " " + (targetDungeonChunkX + 1) + " " + (targetDungeonChunkZ + 2);
////                        BetterAdventureModCore.LOGGER.info("forceload remove all command: " + forceLoadRemoveAllCommand);
//                        server.getCommandManager().executeWithPrefix(server.getCommandSource(), forceLoadRemoveAllCommand);
//
////                        // client chunk unload
////                        PortalAPI.removeChunkLoaderForPlayer(serverPlayerEntity, chunkLoader);
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
//                command = "/execute as " + serverPlayerEntity.getName().getString() + " in " + targetDimensionName + " run teleport " + serverPlayerEntity.getName().getString() + " " + indirectTeleportTargetPosition.getX() + " " + indirectTeleportTargetPosition.getY() + " " + indirectTeleportTargetPosition.getZ() + " " + indirectYaw + " " + indirectPitch;
////                command = "/execute in " + outgoingTeleportDimension + " run teleport " + serverPlayerEntity.getName() + " " + indirectTeleportTargetPosition.getX() + " " + indirectTeleportTargetPosition.getY() + " " + indirectTeleportTargetPosition.getZ() + " " + indirectYaw + " " + indirectPitch;
//            } else {
////                BetterAdventureModCore.LOGGER.info("direct teleport");
//                command = "execute as " + serverPlayerEntity.getName().getString() + " in " + targetDimensionName + " run teleport " + serverPlayerEntity.getName().getString() + " " + directTeleportTargetPosition.getX() + " " + directTeleportTargetPosition.getY() + " " + directTeleportTargetPosition.getZ() + " " + outgoingTeleportPositionYaw + " " + outgoingTeleportPositionPitch;
////                command = "execute in " + outgoingTeleportDimension + " run teleport " + serverPlayerEntity.getName().getString() + " " + directTeleportTargetPosition.getX() + " " + directTeleportTargetPosition.getY() + " " + directTeleportTargetPosition.getZ() + " " + outgoingTeleportPositionYaw + " " + outgoingTeleportPositionPitch;
//            }
////            BetterAdventureModCore.LOGGER.info("teleport command: " + command);
//            server.getCommandManager().executeWithPrefix(server.getCommandSource(), command);
////            server.getPlayerManager().getPlayer("").teleport();
    }
}
