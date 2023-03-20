package com.github.theredbrain.rpgmod.mixin.server;

import com.github.theredbrain.rpgmod.entity.player.DuckPlayerEntityMixin;
import com.mojang.authlib.GameProfile;
import com.mojang.serialization.Dynamic;
import io.netty.buffer.Unpooled;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.s2c.play.*;
import net.minecraft.registry.CombinedDynamicRegistries;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.ServerDynamicRegistryType;
import net.minecraft.registry.tag.TagPacketSerializer;
import net.minecraft.resource.featuretoggle.FeatureFlags;
import net.minecraft.scoreboard.ServerScoreboard;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.UserCache;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraft.world.WorldProperties;
import net.minecraft.world.biome.source.BiomeAccess;
import net.minecraft.world.dimension.DimensionType;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Mixin(PlayerManager.class)
public class PlayerManagerMixin {
//
//    @Shadow
//    @Final
//    private static Logger LOGGER;
//
//    @Shadow
//    @Final
//    private MinecraftServer server;
//
//    @Shadow
//    @Final
//    private List<ServerPlayerEntity> players;
//
//    @Shadow
//    @Final
//    private Map<UUID, ServerPlayerEntity> playerMap;
//
//    @Shadow
//    @Final
//    private CombinedDynamicRegistries<ServerDynamicRegistryType> registryManager;
//
//    @Shadow
//    @Final
//    private DynamicRegistryManager.Immutable syncedRegistryManager;
//
//    @Shadow
//    private int viewDistance;
//
//    @Shadow
//    private int simulationDistance;
//
//    @Shadow
//    protected void sendScoreboard(ServerScoreboard scoreboard, ServerPlayerEntity player) {
//        throw new AssertionError();
//    }
//
//    @Shadow
//    public NbtCompound loadPlayerData(ServerPlayerEntity player) {
//        throw new AssertionError();
//    }
//
//    @Shadow
//    public void sendCommandTree(ServerPlayerEntity player) {
//        throw new AssertionError();
//    }
//
//    @Shadow
//    public void sendToAll(Packet<?> packet) {
//        throw new AssertionError();
//    }
//
//    @Shadow
//    public void sendWorldInfo(ServerPlayerEntity player, ServerWorld world) {
//        throw new AssertionError();
//    }
//
//    @Shadow
//    public int getMaxPlayerCount() {
//        throw new AssertionError();
//    }
//
//    @Shadow
//    public MinecraftServer getServer() {
//        throw new AssertionError();
//    }
//
//    @Shadow
//    public void broadcast(Text message, boolean overlay) {
//        throw new AssertionError();
//    }

//    /**
//     * @author TheRedBrain
//     * @reason use custom inventory
//     */
//    @Overwrite
//    public void onPlayerConnect(ClientConnection connection, ServerPlayerEntity player) {
//        NbtCompound nbtCompound2;
//        Entity entity;
//        ServerWorld serverWorld2;
//        GameProfile gameProfile = player.getGameProfile();
//        UserCache userCache = this.server.getUserCache();
//        Optional<GameProfile> optional = userCache.getByUuid(gameProfile.getId());
//        String string = optional.map(GameProfile::getName).orElse(gameProfile.getName());
//        userCache.add(gameProfile);
//        NbtCompound nbtCompound = this.loadPlayerData(player);
//        RegistryKey<World> registryKey = nbtCompound != null ? DimensionType.worldFromDimensionNbt(new Dynamic<NbtElement>(NbtOps.INSTANCE, nbtCompound.get("Dimension"))).resultOrPartial(LOGGER::error).orElse(World.OVERWORLD) : World.OVERWORLD;
//        ServerWorld serverWorld = this.server.getWorld(registryKey);
//        if (serverWorld == null) {
//            LOGGER.warn("Unknown respawn dimension {}, defaulting to overworld", (Object)registryKey);
//            serverWorld2 = this.server.getOverworld();
//        } else {
//            serverWorld2 = serverWorld;
//        }
//        player.setWorld(serverWorld2);
//        String string2 = "local";
//        if (connection.getAddress() != null) {
//            string2 = connection.getAddress().toString();
//        }
//        LOGGER.info("{}[{}] logged in with entity id {} at ({}, {}, {})", player.getName().getString(), string2, player.getId(), player.getX(), player.getY(), player.getZ());
//        WorldProperties worldProperties = serverWorld2.getLevelProperties();
//        player.setGameMode(nbtCompound);
//        ServerPlayNetworkHandler serverPlayNetworkHandler = new ServerPlayNetworkHandler(this.server, connection, player);
//        GameRules gameRules = serverWorld2.getGameRules();
//        boolean bl = gameRules.getBoolean(GameRules.DO_IMMEDIATE_RESPAWN);
//        boolean bl2 = gameRules.getBoolean(GameRules.REDUCED_DEBUG_INFO);
//        serverPlayNetworkHandler.sendPacket(new GameJoinS2CPacket(player.getId(), worldProperties.isHardcore(), player.interactionManager.getGameMode(), player.interactionManager.getPreviousGameMode(), this.server.getWorldRegistryKeys(), this.syncedRegistryManager, serverWorld2.getDimensionKey(), serverWorld2.getRegistryKey(), BiomeAccess.hashSeed(serverWorld2.getSeed()), this.getMaxPlayerCount(), this.viewDistance, this.simulationDistance, bl2, !bl, serverWorld2.isDebugWorld(), serverWorld2.isFlat(), player.getLastDeathPos()));
//        serverPlayNetworkHandler.sendPacket(new FeaturesS2CPacket(FeatureFlags.FEATURE_MANAGER.toId(serverWorld2.getEnabledFeatures())));
//        serverPlayNetworkHandler.sendPacket(new CustomPayloadS2CPacket(CustomPayloadS2CPacket.BRAND, new PacketByteBuf(Unpooled.buffer()).writeString(this.getServer().getServerModName())));
//        serverPlayNetworkHandler.sendPacket(new DifficultyS2CPacket(worldProperties.getDifficulty(), worldProperties.isDifficultyLocked()));
//        serverPlayNetworkHandler.sendPacket(new PlayerAbilitiesS2CPacket(player.getAbilities()));
//        serverPlayNetworkHandler.sendPacket(new UpdateSelectedSlotS2CPacket(((DuckPlayerEntityMixin)player).getMxtPlayerInventory().selectedSlot));
//        serverPlayNetworkHandler.sendPacket(new SynchronizeRecipesS2CPacket(this.server.getRecipeManager().values()));
//        serverPlayNetworkHandler.sendPacket(new SynchronizeTagsS2CPacket(TagPacketSerializer.serializeTags(this.registryManager)));
//        this.sendCommandTree(player);
//        player.getStatHandler().updateStatSet();
//        player.getRecipeBook().sendInitRecipesPacket(player); // TODO remove?
//        this.sendScoreboard(serverWorld2.getScoreboard(), player);
//        this.server.forcePlayerSampleUpdate();
//        MutableText mutableText = player.getGameProfile().getName().equalsIgnoreCase(string) ? Text.translatable("multiplayer.player.joined", player.getDisplayName()) : Text.translatable("multiplayer.player.joined.renamed", player.getDisplayName(), string);
//        this.broadcast(mutableText.formatted(Formatting.YELLOW), false);
//        serverPlayNetworkHandler.requestTeleport(player.getX(), player.getY(), player.getZ(), player.getYaw(), player.getPitch());
//        player.sendServerMetadata(this.server.getServerMetadata());
//        player.networkHandler.sendPacket(PlayerListS2CPacket.entryFromPlayer(this.players));
//        this.players.add(player);
//        this.playerMap.put(player.getUuid(), player);
//        this.sendToAll(PlayerListS2CPacket.entryFromPlayer(List.of(player)));
//        serverWorld2.onPlayerConnected(player);
//        this.server.getBossBarManager().onPlayerConnect(player);
//        this.sendWorldInfo(player, serverWorld2);
//        this.server.getResourcePackProperties().ifPresent(properties -> player.sendResourcePackUrl(properties.url(), properties.hash(), properties.isRequired(), properties.prompt()));
//        for (StatusEffectInstance statusEffectInstance : player.getStatusEffects()) {
//            serverPlayNetworkHandler.sendPacket(new EntityStatusEffectS2CPacket(player.getId(), statusEffectInstance));
//        }
//        if (nbtCompound != null && nbtCompound.contains("RootVehicle", NbtElement.COMPOUND_TYPE) && (entity = EntityType.loadEntityWithPassengers((nbtCompound2 = nbtCompound.getCompound("RootVehicle")).getCompound("Entity"), serverWorld2, vehicle -> {
//            if (!serverWorld2.tryLoadEntity((Entity)vehicle)) {
//                return null;
//            }
//            return vehicle;
//        })) != null) {
//            UUID uUID = nbtCompound2.containsUuid("Attach") ? nbtCompound2.getUuid("Attach") : null;
//            if (entity.getUuid().equals(uUID)) {
//                player.startRiding(entity, true);
//            } else {
//                for (Entity entity2 : entity.getPassengersDeep()) {
//                    if (!entity2.getUuid().equals(uUID)) continue;
//                    player.startRiding(entity2, true);
//                    break;
//                }
//            }
//            if (!player.hasVehicle()) {
//                LOGGER.warn("Couldn't reattach entity to player");
//                entity.discard();
//                for (Entity entity2 : entity.getPassengersDeep()) {
//                    entity2.discard();
//                }
//            }
//        }
//        player.onSpawn();
//    }

    /**
     * @author TheRedBrain
     * @reason use adventure screenHandler
     */
    @Overwrite
    public void sendPlayerStatus(ServerPlayerEntity player) {
        ((DuckPlayerEntityMixin)player).getAdventureInventoryScreenHandler().syncState();
        player.markHealthDirty();
        player.networkHandler.sendPacket(new UpdateSelectedSlotS2CPacket(player.getInventory().selectedSlot));
    }
}
