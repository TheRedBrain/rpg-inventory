package com.github.theredbrain.bamcore.mixin.server;

import com.github.theredbrain.bamcore.entity.player.DuckPlayerEntityMixin;
import com.github.theredbrain.bamcore.network.event.PlayerFirstJoinCallback;
import com.github.theredbrain.bamcore.network.event.PlayerJoinCallback;
import dev.emi.trinkets.TrinketPlayerScreenHandler;
import dev.emi.trinkets.data.EntitySlotLoader;
import net.minecraft.network.ClientConnection;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.stat.Stats;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = PlayerManager.class, priority = 1050)
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

//    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/server/network/ServerPlayerEntity;onSpawn()V"), method = "onPlayerConnect")
//    private void bam$updateTrinketsInAdventureInventory(ClientConnection connection, ServerPlayerEntity player, CallbackInfo cb) {
//        EntitySlotLoader.SERVER.sync(player);
//        ((TrinketPlayerScreenHandler) ((DuckPlayerEntityMixin) player).bamcore$getAdventureInventoryScreenHandler()).trinkets$updateTrinketSlots(false);
//    }

    // TODO temporary until PlayerEvents is updated
    @Inject(at = @At(value = "TAIL"), method = "onPlayerConnect")
    private void onPlayerJoin(ClientConnection connection, ServerPlayerEntity player, CallbackInfo info) {
        PlayerJoinCallback.EVENT.invoker().joinServer(player, player.getServer());
        if (player.getStatHandler().getStat(Stats.CUSTOM.getOrCreateStat(Stats.LEAVE_GAME)) < 1) {
            PlayerFirstJoinCallback.EVENT.invoker().joinServerForFirstTime(player, player.getServer());
        }
    }

//    @Inject(method = "sendPlayerStatus", at = @At("HEAD"))
//    public void bamcore$sendPlayerStatus(ServerPlayerEntity player, CallbackInfo ci) {
//        ((DuckPlayerEntityMixin)player).bamcore$getAdventureInventoryScreenHandler().syncState();
//    }
}
