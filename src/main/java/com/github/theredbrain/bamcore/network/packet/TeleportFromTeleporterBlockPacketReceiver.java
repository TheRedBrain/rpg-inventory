package com.github.theredbrain.bamcore.network.packet;

import com.github.theredbrain.bamcore.BetterAdventureModeCore;
import com.github.theredbrain.bamcore.BetterAdventureModeCoreClient;
import com.github.theredbrain.bamcore.api.json_files_backend.PlayerLocation;
import com.github.theredbrain.bamcore.block.entity.LocationControlBlockEntity;
import com.github.theredbrain.bamcore.block.entity.TeleporterBlockBlockEntity;
import com.github.theredbrain.bamcore.registry.ComponentsRegistry;
import com.github.theredbrain.bamcore.registry.PlayerLocationsRegistry;
import com.github.theredbrain.bamcore.world.DimensionsManager;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
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

        TeleporterBlockBlockEntity.SpawnPointType spawnPointType = packet.spawnPointType;

        String targetDimensionOwnerName = packet.targetDimensionOwnerName;
        String targetLocation = packet.targetLocation;
        String targetLocationEntrance = packet.targetLocationEntrance;

        ServerWorld serverWorld = serverPlayerEntity.getServerWorld();
        MinecraftServer server = serverPlayerEntity.server;

        ServerWorld targetWorld = null;
        BlockPos targetPos = null;
        double targetYaw = 0.0;
        double targetPitch = 0.0;

        boolean locationWasGeneratedByOwner = true;
        if (teleportationMode == TeleporterBlockBlockEntity.TeleportationMode.DIRECT) {
            targetWorld = serverWorld;
            targetPos = new BlockPos(teleportBlockPosition.getX() + directTeleportPositionOffset.getX(), teleportBlockPosition.getY() + directTeleportPositionOffset.getY(), teleportBlockPosition.getZ() + directTeleportPositionOffset.getZ());
            targetYaw = directTeleportOrientationYaw;
            targetPitch = directTeleportOrientationPitch;
        } else if (teleportationMode == TeleporterBlockBlockEntity.TeleportationMode.SPAWN_POINTS) {
            Pair<Pair<String, BlockPos>, Boolean> housing_access_pos = ComponentsRegistry.PLAYER_LOCATION_ACCESS_POS.get(serverPlayerEntity).getValue();
            if (spawnPointType == TeleporterBlockBlockEntity.SpawnPointType.PLAYER_LOCATION_ACCESS_POSITION && housing_access_pos.getRight()) {
                targetWorld = server.getWorld(RegistryKey.of(RegistryKeys.WORLD, new Identifier(housing_access_pos.getLeft().getLeft())));
                targetPos = housing_access_pos.getLeft().getRight();
                if (targetWorld != null && targetPos != null) {
                    ComponentsRegistry.PLAYER_LOCATION_ACCESS_POS.get(serverPlayerEntity).deactivate();
                }
            } else if (spawnPointType == TeleporterBlockBlockEntity.SpawnPointType.PLAYER_SPAWN) {
                targetWorld = server.getWorld(serverPlayerEntity.getSpawnPointDimension());
                targetPos = serverPlayerEntity.getSpawnPointPosition();
            } else {
                targetWorld = server.getOverworld();
                targetPos = server.getOverworld().getSpawnPos();
            }
//        } else if (teleportationMode == TeleporterBlockBlockEntity.TeleportationMode.LOCATIONS) {
//            // TODO
//            ServerPlayerEntity targetDimensionOwner = server.getPlayerManager().getPlayer(targetDimensionOwnerName);
//
//            if (targetDimensionOwner != null) {
//                BetterAdventureModeCore.info("targetDimensionOwner: " + targetDimensionOwner);
//                Identifier targetDimensionId = Identifier.tryParse(targetDimensionOwner.getUuidAsString()/*ComponentsRegistry.PLAYER_SPECIFIC_DIMENSION_IDS.get(targetDimensionOwner).getValue("dungeon_dimension")*/);
//                BetterAdventureModeCore.info("targetDimensionId: " + targetDimensionId);
//                RegistryKey<World> dimensionregistryKey = RegistryKey.of(RegistryKeys.WORLD, targetDimensionId);
//                targetWorld = server.getWorld(dimensionregistryKey);
//
//                if (targetWorld == null) {
//                    DimensionsManager.addAndSaveDynamicDimension(targetDimensionId, server, true);
//                    dimensionregistryKey = RegistryKey.of(RegistryKeys.WORLD, targetDimensionId);
//                    targetWorld = server.getWorld(dimensionregistryKey);
////                    if (targetDimensionOwner != null) {
//                    ComponentsRegistry.PLAYER_SPECIFIC_DIMENSION_IDS.get(targetDimensionOwner).setStatus("dungeon_dimension", true);
////                    } else {
////                        // this should not happen, as targetDimensionOwner should be coming from a list of existing players
////                        ComponentsRegistry.PLAYER_SPECIFIC_DIMENSION_IDS.get(serverPlayerEntity).setStatus("housing_dimension", true);
////                    }
//                }
//
//                PlayerLocation playerLocation = PlayerLocationsRegistry.getLocation(Identifier.tryParse(targetLocation));
//
//                BetterAdventureModeCore.info("playerDungeon: " + playerLocation);
//                if (targetWorld != null && playerLocation != null && targetWorld.getBlockEntity(playerLocation.controlBlockPos()) instanceof LocationControlBlockEntity locationControlBlock) {
//                    MutablePair<BlockPos, MutablePair<Double, Double>> targetEntrance = locationControlBlock.getTargetEntrance(targetLocationEntrance);
//                    targetPos = targetEntrance.getLeft();
//                    targetYaw = targetEntrance.getRight().getLeft();
//                    targetPitch = targetEntrance.getRight().getRight();
//                }
//            }
        } else if (teleportationMode == TeleporterBlockBlockEntity.TeleportationMode.PLAYER_LOCATIONS) {
            ServerPlayerEntity targetDimensionOwner = server.getPlayerManager().getPlayer(targetDimensionOwnerName);

            BetterAdventureModeCore.info("targetLocation: " + targetLocation);

            if (targetDimensionOwner != null) {
                BetterAdventureModeCore.info("targetDimensionOwner: " + targetDimensionOwner);
//                Identifier targetDimensionId = null;
//                String[] strings = targetLocation.split("/", 2);
//                if (strings[0].equals("player_dungeons")) {
                Identifier targetDimensionId = Identifier.tryParse(targetDimensionOwner.getUuidAsString()/*ComponentsRegistry.PLAYER_SPECIFIC_DIMENSION_IDS.get(targetDimensionOwner).getValue("dungeon_dimension")*/);
//                } else if (strings[0].equals("player_houses")) {
//                    targetDimensionId = Identifier.tryParse(ComponentsRegistry.PLAYER_SPECIFIC_DIMENSION_IDS.get(targetDimensionOwner).getValue("housing_dimension"));
//                }
                BetterAdventureModeCore.info("targetDimensionId: " + targetDimensionId);
                RegistryKey<World> dimensionregistryKey = RegistryKey.of(RegistryKeys.WORLD, targetDimensionId);
                targetWorld = server.getWorld(dimensionregistryKey);

                if (targetWorld == null) {
                    // TODO only if player is dimensionOwner, message instead
                    if (targetDimensionOwner.getUuid() == serverPlayerEntity.getUuid()) {
                        BetterAdventureModeCore.info("targetDimensionOwner.getUuid() == serverPlayerEntity.getUuid()");
                        DimensionsManager.addAndSaveDynamicDimension(targetDimensionId, server);
                        dimensionregistryKey = RegistryKey.of(RegistryKeys.WORLD, targetDimensionId);
                        targetWorld = server.getWorld(dimensionregistryKey);
                    } else {
                        locationWasGeneratedByOwner = false;
                    }

//                    ComponentsRegistry.PLAYER_SPECIFIC_DIMENSION_IDS.get(targetDimensionOwner).setStatus("housing_dimension", true);
                }

                BetterAdventureModeCore.info("targetWorld: " + targetWorld);
                PlayerLocation playerLocation = PlayerLocationsRegistry.getLocation(Identifier.tryParse(targetLocation));

                if (targetWorld != null && playerLocation != null) {
                    BetterAdventureModeCore.info("targetWorld != null && playerLocation != null");

                    BlockPos blockPos = playerLocation.controlBlockPos();

                    BlockEntity blockEntity = targetWorld.getBlockEntity(blockPos);

                    if (!(blockEntity instanceof LocationControlBlockEntity)) {

                        BetterAdventureModeCore.info("!(blockEntity instanceof LocationControlBlockEntity)");

                        String forceLoadAddCommand = "execute in " + targetWorld.getRegistryKey().getValue() + " run forceload add " + (blockPos.getX() - 16) + " " + (blockPos.getZ() - 16) + " " + (blockPos.getX() + 31) + " " + (blockPos.getZ() + 31);
                        server.getCommandManager().executeWithPrefix(server.getCommandSource(), forceLoadAddCommand);

//                        server.getCommandManager().executeWithPrefix(server.getCommandSource(), "forceload query");

                        String placeStructureCommand = "execute in " + targetWorld.getRegistryKey().getValue() + " run place structure " + playerLocation.getStructureIdentifier() + " " + blockPos.getX() + " " + blockPos.getY() + " " + blockPos.getZ();
                        server.getCommandManager().executeWithPrefix(server.getCommandSource(), placeStructureCommand);

                        String forceLoadRemoveAllCommand = "execute in " + targetWorld.getRegistryKey().getValue() + " run forceload remove " + (blockPos.getX() - 16) + " " + (blockPos.getZ() - 16) + " " + (blockPos.getX() + 31) + " " + (blockPos.getZ() + 31);
                        server.getCommandManager().executeWithPrefix(server.getCommandSource(), forceLoadRemoveAllCommand);

                        blockEntity = targetWorld.getBlockEntity(blockPos);
                    }

                    BetterAdventureModeCore.info("controlBlockPos: " + blockPos);
                    BetterAdventureModeCore.info("block at controlBlockPos: " + targetWorld.getBlockState(blockPos).getBlock());

                    if (blockEntity instanceof LocationControlBlockEntity locationControlBlock) {
                        MutablePair<BlockPos, MutablePair<Double, Double>> entrance = locationControlBlock.getTargetEntrance(targetLocationEntrance);
                        targetPos = entrance.getLeft();
                        targetYaw = entrance.getRight().getLeft();
                        targetPitch = entrance.getRight().getRight();

                        BetterAdventureModeCore.info("targetPos: " + targetPos);

                        if (setAccessPosition && Identifier.isValid(accessPositionDimension)) {
                            ComponentsRegistry.PLAYER_LOCATION_ACCESS_POS.get(serverPlayerEntity).setValue(new Pair<>(new Pair<>(accessPositionDimension, teleportBlockPosition.add(accessPositionOffset.getX(), accessPositionOffset.getY(), accessPositionOffset.getZ())), true));
                        }
                    }
                }
            }
        }

        if (targetWorld != null && targetPos != null) {

            serverPlayerEntity.teleport(targetWorld, (targetPos.getX() + 0.5), (targetPos.getY() + 0.5), (targetPos.getZ() + 0.5), (float) targetYaw, (float) targetPitch);
            if (BetterAdventureModeCoreClient.clientConfig.show_debug_log) {
                serverPlayerEntity.sendMessage(Text.of("Teleport to world: " + targetWorld.getRegistryKey().getValue() + " at position: " + (targetPos.getX() + 0.5) + ", " + (targetPos.getY() + 0.5) + ", " + (targetPos.getZ() + 0.5) + ", with yaw: " + targetYaw + " and pitch: " + targetPitch));
            }
            // TODO send S2C packet which removes PortalResistanceEffect and setScreen(null)
        } else {
            if (BetterAdventureModeCoreClient.clientConfig.show_debug_log) {
                serverPlayerEntity.sendMessage(Text.of("Teleport failed"));
                if (targetWorld == null) {
                    serverPlayerEntity.sendMessage(Text.of("targetWorld == null"));
                }
                if (targetPos == null) {
                    serverPlayerEntity.sendMessage(Text.of("targetPos == null"));
                }
            }

            if (!locationWasGeneratedByOwner) {
                serverPlayerEntity.sendMessage(Text.translatable("gui.teleporter_block.location_not_visited_by_owner"));
            } else {
                serverPlayerEntity.sendMessage(Text.translatable("gui.teleporter_block.first_location_visit_by_owner"));
            }
        }
    }
}
