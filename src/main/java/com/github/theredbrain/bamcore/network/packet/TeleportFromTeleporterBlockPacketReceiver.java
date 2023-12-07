package com.github.theredbrain.bamcore.network.packet;

import com.github.theredbrain.bamcore.BetterAdventureModeCore;
import com.github.theredbrain.bamcore.BetterAdventureModeCoreClient;
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

        TeleporterBlockBlockEntity.LocationType locationType = packet.locationType;

        String targetDimensionOwnerName = packet.targetDimensionOwnerName;
        String targetLocation = packet.targetLocation;
        String targetLocationEntrance = packet.targetLocationEntrance;

        ServerWorld serverWorld = serverPlayerEntity.getServerWorld();
        MinecraftServer server = serverPlayerEntity.server;

        ServerWorld targetWorld = null;
        BlockPos targetPos = null;
        double targetYaw = 0.0;
        double targetPitch = 0.0;
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
            // TODO
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

                    ComponentsRegistry.PLAYER_SPECIFIC_DIMENSION_IDS.get(targetDimensionOwner).setStatus("housing_dimension", true);
                }

                PlayerHouse playerHouse = PlayerHousesRegistry.getHouse(Identifier.tryParse(targetLocation));

                if (targetWorld != null && playerHouse != null) {
                    BlockEntity blockEntity = targetWorld.getBlockEntity(playerHouse.housingBlockPos());
                    if (!(blockEntity instanceof HousingBlockBlockEntity)) {

                        BlockPos blockPos = playerHouse.housingBlockPos();

                        String forceLoadAddCommand = "execute in " + targetWorld.getRegistryKey().getValue() + " run forceload add " + (blockPos.getX() - 16) + " " + (blockPos.getZ() - 16) + " " + (blockPos.getX() + 31) + " " + (blockPos.getZ() + 31);
                        server.getCommandManager().executeWithPrefix(server.getCommandSource(), forceLoadAddCommand);

                        server.getCommandManager().executeWithPrefix(server.getCommandSource(), "forceload query");

                        String placeStructureCommand = "execute in " + targetWorld.getRegistryKey().getValue() + " run place structure " + playerHouse.getStructureIdentifier() + " " + playerHouse.housingBlockPos().getX() + " " + playerHouse.housingBlockPos().getY() + " " + playerHouse.housingBlockPos().getZ();
                        server.getCommandManager().executeWithPrefix(server.getCommandSource(), placeStructureCommand);

                        String forceLoadRemoveAllCommand = "execute in " + targetWorld.getRegistryKey().getValue() + " run forceload remove " + (blockPos.getX() - 16) + " " + (blockPos.getZ() - 16) + " " + (blockPos.getX() + 31) + " " + (blockPos.getZ() + 31);
                        server.getCommandManager().executeWithPrefix(server.getCommandSource(), forceLoadRemoveAllCommand);

                        blockEntity = targetWorld.getBlockEntity(playerHouse.housingBlockPos());
                    }
                    if (blockEntity instanceof HousingBlockBlockEntity housingBlock) {
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

        if (targetWorld != null && targetPos != null) {

            serverPlayerEntity.teleport(targetWorld, (targetPos.getX() + 0.5), (targetPos.getY() + 0.5), (targetPos.getZ() + 0.5), (float) targetYaw, (float) targetPitch);
            if (BetterAdventureModeCoreClient.clientConfig.show_debug_log) {
                serverPlayerEntity.sendMessage(Text.of("Teleport to world: " + targetWorld.getRegistryKey().getValue() + " at position: " + (targetPos.getX() + 0.5) + ", " + (targetPos.getY() + 0.5) + ", " + (targetPos.getZ() + 0.5) + ", with yaw: " + targetYaw + " and pitch: " + targetPitch));
            }
            // TODO send S2C packet which removes PortalResistanceEffect and setScreen(null)
        } else if (BetterAdventureModeCoreClient.clientConfig.show_debug_log) {
            serverPlayerEntity.sendMessage(Text.of("Teleport failed"));
            if (targetWorld == null) {
                serverPlayerEntity.sendMessage(Text.of("targetWorld == null"));
            }
            if (targetPos == null) {
                serverPlayerEntity.sendMessage(Text.of("targetPos == null"));
            }
        }
    }
}
