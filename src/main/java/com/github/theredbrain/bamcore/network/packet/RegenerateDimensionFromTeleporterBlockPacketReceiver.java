package com.github.theredbrain.bamcore.network.packet;
// TODO move to bamdimensions
//import com.github.theredbrain.bamcore.block.Triggerable;
//import com.github.theredbrain.bamcore.registry.ComponentsRegistry;
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
//import net.minecraft.text.Text;
//import net.minecraft.util.Identifier;
//import net.minecraft.util.math.BlockPos;
//import net.minecraft.world.World;
//
//public class RegenerateDimensionFromTeleporterBlockPacketReceiver implements ServerPlayNetworking.PlayChannelHandler {
//
//    @Override
//    public void receive(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
//
//        String targetDimensionOwnerName = buf.readString();
//
//        String outgoingTeleportDimension = buf.readString();
//
//        String targetDungeonStructureIdentifier = buf.readString();
//        BlockPos targetDungeonStructureStartPosition = buf.readBlockPos();
//
//        int targetDungeonChunkX = buf.readInt();
//        int targetDungeonChunkZ = buf.readInt();
//
//        BlockPos regenerateTargetDungeonTriggerBlockPosition = buf.readBlockPos();
//
//        server.execute(() -> {
//
//            String targetDimensionName;
//            ServerPlayerEntity targetDimensionOwner = server.getPlayerManager().getPlayer(targetDimensionOwnerName);
//
//            if (targetDimensionOwner != null) {
//                targetDimensionName = ComponentsRegistry.PLAYER_SPECIFIC_DIMENSION_IDS.get(targetDimensionOwner).getValue(outgoingTeleportDimension);
//            } else {
//                // this should not happen, as targetDimensionOwner should be coming from a list of existing players
//                targetDimensionName = ComponentsRegistry.PLAYER_SPECIFIC_DIMENSION_IDS.get(player.getName().getString()).getValue(outgoingTeleportDimension);
//            }
//
//            RegistryKey<World> dimensionregistryKey = RegistryKey.of(RegistryKeys.WORLD, new Identifier(targetDimensionName));
//
//            ServerWorld serverWorld = server.getWorld(dimensionregistryKey);
//
//            if (serverWorld == null) {
//                player.sendMessage(Text.translatable("gui.teleporter_block.regenerateDimensionFailed"), false);
//            } else {
//                BlockEntity triggeredBlock = serverWorld.getBlockEntity(regenerateTargetDungeonTriggerBlockPosition);
//
//                if (triggeredBlock instanceof Triggerable triggerable) {
//                    triggerable.trigger();
//                } else {
//                    // forceload add chunks
//                    String forceLoadAddCommand = "execute in " + targetDimensionName + " run forceload add " + (targetDungeonChunkX - 1) + " " + (targetDungeonChunkZ - 1) + " " + (targetDungeonChunkX + 1) + " " + (targetDungeonChunkZ + 2);
////                    RPGMod.LOGGER.info("forceload add command: " + forceLoadAddCommand);
//                    server.getCommandManager().executeWithPrefix(server.getCommandSource(), forceLoadAddCommand);
//
//
//                    // place structure
//                    String placeStructureCommand = "execute in " + targetDimensionName + " run place structure " + targetDungeonStructureIdentifier + " " + targetDungeonStructureStartPosition.getX() + " " + targetDungeonStructureStartPosition.getY() + " " + targetDungeonStructureStartPosition.getZ();
////                    RPGMod.LOGGER.info("place structure command: " + placeStructureCommand);
//                    server.getCommandManager().executeWithPrefix(server.getCommandSource(), placeStructureCommand);
//
//                    // forceload remove chunks
//                    String forceLoadRemoveAllCommand = "execute in " + targetDimensionName + " run forceload remove " + (targetDungeonChunkX - 1) + " " + (targetDungeonChunkZ - 1) + " " + (targetDungeonChunkX + 1) + " " + (targetDungeonChunkZ + 2);
////                    RPGMod.LOGGER.info("forceload remove all command: " + forceLoadRemoveAllCommand);
//                    server.getCommandManager().executeWithPrefix(server.getCommandSource(), forceLoadRemoveAllCommand);
//                }
//            }
//        });
//    }
//}
