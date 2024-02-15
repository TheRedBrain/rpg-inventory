package com.github.theredbrain.betteradventuremode.network.packet;

import com.github.theredbrain.betteradventuremode.BetterAdventureModeClient;
import com.github.theredbrain.betteradventuremode.data.Location;
import com.github.theredbrain.betteradventuremode.util.ItemUtils;
import com.github.theredbrain.betteradventuremode.block.entity.EntranceDelegationBlockEntity;
import com.github.theredbrain.betteradventuremode.block.entity.LocationControlBlockEntity;
import com.github.theredbrain.betteradventuremode.block.entity.TeleporterBlockBlockEntity;
import com.github.theredbrain.betteradventuremode.registry.ComponentsRegistry;
import com.github.theredbrain.betteradventuremode.registry.LocationsRegistry;
import com.github.theredbrain.betteradventuremode.util.UUIDUtilities;
import com.github.theredbrain.betteradventuremode.world.DimensionsManager;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.scoreboard.Team;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.apache.commons.lang3.tuple.MutablePair;

import java.util.UUID;

import static net.fabricmc.fabric.impl.networking.client.ClientNetworkingImpl.createC2SPacket;

public class TeleportFromTeleporterBlockPacketReceiver implements ServerPlayNetworking.PlayPacketHandler<TeleportFromTeleporterBlockPacket> {
    @Override
    public void receive(TeleportFromTeleporterBlockPacket packet, ServerPlayerEntity serverPlayerEntity, PacketSender responseSender) {

        BlockPos teleportBlockPosition = packet.teleportBlockPosition;

        String accessPositionDimension = packet.accessPositionDimension;
        BlockPos accessPositionOffset = packet.accessPositionOffset;
        boolean setAccessPosition = packet.setAccessPosition;

        boolean teleportTeam = packet.teleportTeam;

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
        boolean playerHadKeyItem = true;
        boolean locationWasReset = false;

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
                targetYaw = serverPlayerEntity.getSpawnAngle();
            } else {
                targetWorld = server.getOverworld();
                targetPos = server.getOverworld().getSpawnPos();
                targetYaw = server.getOverworld().getSpawnAngle();
            }
        } else if (teleportationMode == TeleporterBlockBlockEntity.TeleportationMode.LOCATIONS) {

            Location location = LocationsRegistry.getLocation(Identifier.tryParse(targetLocation));

            ServerPlayerEntity targetDimensionOwner = server.getPlayerManager().getPlayer(targetDimensionOwnerName);

//            BetterAdventureMode.info("targetLocation: " + targetLocation);

            if (location != null) {

                if (location.isPublic()) {
                    targetWorld = server.getOverworld();
                } else if (targetDimensionOwner != null) {
//                BetterAdventureMode.info("targetDimensionOwner: " + targetDimensionOwner);
                    Identifier targetDimensionId = Identifier.tryParse(targetDimensionOwner.getUuidAsString());
//                BetterAdventureMode.info("targetDimensionId: " + targetDimensionId);
                    RegistryKey<World> dimensionregistryKey = RegistryKey.of(RegistryKeys.WORLD, targetDimensionId);
                    targetWorld = server.getWorld(dimensionregistryKey);

                    if (targetWorld == null) {
                        if (targetDimensionOwner.getUuid() == serverPlayerEntity.getUuid()) {
//                        BetterAdventureMode.info("targetDimensionOwner.getUuid() == serverPlayerEntity.getUuid()");
                            DimensionsManager.addAndSaveDynamicDimension(targetDimensionId, server);
                            dimensionregistryKey = RegistryKey.of(RegistryKeys.WORLD, targetDimensionId);
                            targetWorld = server.getWorld(dimensionregistryKey);
                        } else {
                            locationWasGeneratedByOwner = false;
                        }

                    }
                }
//                BetterAdventureMode.info("targetWorld: " + targetWorld);

                if (targetWorld != null) {
//                    BetterAdventureMode.info("targetWorld != null && location != null");

                    BlockPos blockPos = location.controlBlockPos();

                    BlockEntity blockEntity = targetWorld.getBlockEntity(blockPos);

                    if (!(blockEntity instanceof LocationControlBlockEntity)) {

//                        BetterAdventureMode.info("!(blockEntity instanceof LocationControlBlockEntity)");

                        String forceLoadAddCommand = "execute in " + targetWorld.getRegistryKey().getValue() + " run forceload add " + (blockPos.getX() - 16) + " " + (blockPos.getZ() - 16) + " " + (blockPos.getX() + 31) + " " + (blockPos.getZ() + 31);
                        server.getCommandManager().executeWithPrefix(server.getCommandSource(), forceLoadAddCommand);

//                        server.getCommandManager().executeWithPrefix(server.getCommandSource(), "forceload query");

                        String placeStructureCommand = "execute in " + targetWorld.getRegistryKey().getValue() + " run place structure " + location.getStructureIdentifier() + " " + blockPos.getX() + " " + blockPos.getY() + " " + blockPos.getZ();
                        server.getCommandManager().executeWithPrefix(server.getCommandSource(), placeStructureCommand);

                        String forceLoadRemoveAllCommand = "execute in " + targetWorld.getRegistryKey().getValue() + " run forceload remove " + (blockPos.getX() - 16) + " " + (blockPos.getZ() - 16) + " " + (blockPos.getX() + 31) + " " + (blockPos.getZ() + 31);
                        server.getCommandManager().executeWithPrefix(server.getCommandSource(), forceLoadRemoveAllCommand);

                        blockEntity = targetWorld.getBlockEntity(blockPos);
                    }

//                    BetterAdventureMode.info("controlBlockPos: " + blockPos);
//                    BetterAdventureMode.info("block at controlBlockPos: " + targetWorld.getBlockState(blockPos).getBlock());

                    if (blockEntity instanceof LocationControlBlockEntity locationControlBlock) {
                        if (locationControlBlock.shouldReset()) {

                            String forceLoadAddCommand = "execute in " + targetWorld.getRegistryKey().getValue() + " run forceload add " + (blockPos.getX() - 16) + " " + (blockPos.getZ() - 16) + " " + (blockPos.getX() + 31) + " " + (blockPos.getZ() + 31);
                            server.getCommandManager().executeWithPrefix(server.getCommandSource(), forceLoadAddCommand);

                            locationControlBlock.trigger();

                            String forceLoadRemoveAllCommand = "execute in " + targetWorld.getRegistryKey().getValue() + " run forceload remove " + (blockPos.getX() - 16) + " " + (blockPos.getZ() - 16) + " " + (blockPos.getX() + 31) + " " + (blockPos.getZ() + 31);
                            server.getCommandManager().executeWithPrefix(server.getCommandSource(), forceLoadRemoveAllCommand);

//                            blockEntity = null;
                            locationWasReset = true;
                        }
                    }

                    if (blockEntity instanceof LocationControlBlockEntity locationControlBlock) {

                        MutablePair<BlockPos, MutablePair<Double, Double>> entrance = locationControlBlock.getTargetEntrance(targetLocationEntrance);
                        targetPos = entrance.getLeft();
                        targetYaw = entrance.getRight().getLeft();
                        targetPitch = entrance.getRight().getRight();

                        boolean consumeKey = location.consumeKeyAtEntrance(targetLocationEntrance);
                        ItemUtils.VirtualItemStack virtualKey = location.getKeyForEntrance(targetLocationEntrance);
                        if (virtualKey != null) {
                            ItemStack keyItemStack = ItemUtils.getItemStackFromVirtualItemStack(virtualKey);
                            int keyCount = keyItemStack.getCount();
                            PlayerInventory playerInventory = serverPlayerEntity.getInventory();

                            for (int i = 0; i < playerInventory.size(); i++) {
                                ItemStack currentItemStack = playerInventory.getStack(i);
                                if (ItemStack.canCombine(keyItemStack, currentItemStack)) {
                                    ItemStack currentItemStackCopy = currentItemStack.copy();
                                    int currentItemStackCount = currentItemStackCopy.getCount();
                                    if (currentItemStackCount >= keyCount) {
                                        currentItemStackCopy.setCount(currentItemStackCount - keyCount);
                                        if (consumeKey) {
                                            playerInventory.setStack(i, currentItemStackCopy);
                                        }
                                        keyCount = 0;
                                        break;
                                    } else {
                                        if (consumeKey) {
                                            playerInventory.setStack(i, ItemStack.EMPTY);
                                        }
                                        keyCount = keyCount - currentItemStackCount;
                                    }
                                }
                            }
                            playerHadKeyItem = keyCount <= 0;
                        }

                        if (targetWorld.getBlockEntity(targetPos) instanceof EntranceDelegationBlockEntity entranceDelegationBlockEntity) {
                            MutablePair<BlockPos, MutablePair<Double, Double>> delegatedEntrance = entranceDelegationBlockEntity.getDelegatedEntrance();
                            targetPos = new BlockPos(delegatedEntrance.getLeft().getX() + entranceDelegationBlockEntity.getPos().getX(), delegatedEntrance.getLeft().getY() + entranceDelegationBlockEntity.getPos().getY(), delegatedEntrance.getLeft().getZ() + entranceDelegationBlockEntity.getPos().getZ());
                            targetYaw = delegatedEntrance.getRight().getLeft();
                            targetPitch = delegatedEntrance.getRight().getRight();
                        }

                        if (setAccessPosition && Identifier.isValid(accessPositionDimension)) {
                            ComponentsRegistry.PLAYER_LOCATION_ACCESS_POS.get(serverPlayerEntity).setValue(new Pair<>(new Pair<>(accessPositionDimension, teleportBlockPosition.add(accessPositionOffset.getX(), accessPositionOffset.getY(), accessPositionOffset.getZ())), true));
                        }
                    }
                }
            }
        }

        if (targetWorld != null && targetPos != null && playerHadKeyItem) {

            serverPlayerEntity.fallDistance = 0;
            serverPlayerEntity.teleport(targetWorld, (targetPos.getX() + 0.5), (targetPos.getY() + 0.01), (targetPos.getZ() + 0.5), (float) targetYaw, (float) targetPitch);
            if (BetterAdventureModeClient.clientConfig.show_debug_messages) {
                serverPlayerEntity.sendMessage(Text.of("Teleport to world: " + targetWorld.getRegistryKey().getValue() + " at position: " + (targetPos.getX() + 0.5) + ", " + (targetPos.getY() + 0.01) + ", " + (targetPos.getZ() + 0.5) + ", with yaw: " + targetYaw + " and pitch: " + targetPitch));
                if (targetWorld != server.getOverworld()) {
                    serverPlayerEntity.sendMessage(Text.of("World owned by: " + targetDimensionOwnerName));
                }
            }
            ClientPlayNetworking.send(new SuccessfulTeleportPacket());

            if (teleportTeam) {
                Team team = serverPlayerEntity.getScoreboardTeam();
                if (team != null) {
                    for (String playerString : team.getPlayerList()) {
                        if (UUIDUtilities.isStringValidUUID(playerString)) {
                            Entity entity = serverWorld.getEntity(UUID.fromString(playerString));
                            if (entity instanceof ServerPlayerEntity teamServerPlayerEntity) {
                                ServerPlayNetworking.send(teamServerPlayerEntity, new TeleportToTeamPacket(targetWorld.getRegistryKey().getValue(), targetPos, targetYaw, targetPitch));
                            }
                        }
                    }
                }
            }
        } else {
            if (BetterAdventureModeClient.clientConfig.show_debug_messages) {
                serverPlayerEntity.sendMessage(Text.of("Teleport failed"));
                if (targetWorld == null) {
                    serverPlayerEntity.sendMessage(Text.of("targetWorld == null"));
                }
                if (targetPos == null) {
                    serverPlayerEntity.sendMessage(Text.of("targetPos == null"));
                }
            }

            if (locationWasReset) {
                serverPlayerEntity.sendMessage(Text.translatable("gui.teleporter_block.location_was_reset"));
            } else {
                if (!playerHadKeyItem) {
                    serverPlayerEntity.sendMessage(Text.translatable("gui.teleporter_block.key_item_required"));
                } else {
                    if (!locationWasGeneratedByOwner) {
                        serverPlayerEntity.sendMessage(Text.translatable("gui.teleporter_block.location_not_visited_by_owner"));
                    }
                }
            }
        }
    }
}
