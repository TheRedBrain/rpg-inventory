package com.github.theredbrain.bamcore.mixin.server.network;

import com.github.theredbrain.bamcore.entity.player.DuckPlayerEntityMixin;
import com.github.theredbrain.bamcore.entity.player.DuckPlayerInventoryMixin;
import com.github.theredbrain.bamcore.network.event.PlayerDeathCallback;
import com.github.theredbrain.bamcore.network.packet.BetterAdventureModCoreServerPacket;
import com.github.theredbrain.bamcore.registry.GameRulesRegistry;
import com.mojang.authlib.GameProfile;
import com.mojang.datafixers.util.Either;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.registry.RegistryKey;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.network.ServerPlayerInteractionManager;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.stat.Stats;
import net.minecraft.text.Text;
import net.minecraft.util.Unit;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.GameMode;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Collection;
import java.util.List;

@Mixin(value=ServerPlayerEntity.class, priority=950)
public abstract class ServerPlayerEntityMixin extends PlayerEntity implements DuckPlayerEntityMixin {

    @Unique
    ItemStack mainHandSlotStack = ItemStack.EMPTY;

    @Unique
    ItemStack alternateMainHandSlotStack = ItemStack.EMPTY;

    @Unique
    ItemStack offHandSlotStack = ItemStack.EMPTY;

    @Unique
    ItemStack alternateOffHandSlotStack = ItemStack.EMPTY;

//    @Shadow
//    public ServerPlayNetworkHandler networkHandler;

    @Shadow
    @Final
    public ServerPlayerInteractionManager interactionManager;

//    @Shadow
//    @Final
//    private PlayerAdvancementTracker advancementTracker;
//
//    @Shadow
//    private float lastHealthScore;
//
//    @Shadow
//    private int lastAirScore;
//
//    @Shadow
//    private int lastArmorScore;

//    @Shadow
//    private float syncedHealth;
//
//    @Shadow
//    private int syncedFoodLevel;
//
//    @Shadow
//    private int syncedExperience;
//
//    @Shadow
//    private int joinInvulnerabilityTicks;
//
//    @Shadow
//    public boolean seenCredits;
//
//    @Shadow
//    private Vec3d levitationStartPos;
//
//    @Shadow
//    private int levitationStartTick;
//
//    @Shadow
//    public boolean filterText;

//    @Shadow
//    private SculkShriekerWarningManager sculkShriekerWarningManager;



//    @Shadow
//    public PublicPlayerSession session;

//    @Shadow
//    private int screenHandlerSyncId;
//
//    @Shadow
//    private void onScreenHandlerOpened(ScreenHandler screenHandler) { // 408
//        throw new AssertionError();
//    }

//    @Shadow
//    public void tickFallStartPos() { // 539
//        throw new AssertionError();
//    }
//
//    @Shadow
//    private void updateScores(ScoreboardCriterion criterion, int score2) { // 558
//        throw new AssertionError();
//    }
//
//    @Shadow
//    public void tickVehicleInLavaRiding() { // 545
//        throw new AssertionError();
//    }

//    @Shadow
//    private void incrementScreenHandlerSyncId() { // 932
//        throw new AssertionError();
//    }

//    @Shadow
//    public ServerWorld getWorld() { // 1193
//        throw new AssertionError();
//    }

//    @Shadow
//    public Entity getCameraEntity() { // 1331
//        throw new AssertionError();
//    }
//
//    @Shadow
//    public void setCameraEntity(@Nullable Entity entity) { // 1335
//        throw new AssertionError();
//    }

//    @Shadow public abstract void playerTick();

    @Shadow protected abstract boolean isBedTooFarAway(BlockPos pos, Direction direction);

    @Shadow protected abstract boolean isBedObstructed(BlockPos pos, Direction direction);

    @Shadow public abstract void setSpawnPoint(RegistryKey<World> dimension, @Nullable BlockPos pos, float angle, boolean forced, boolean sendMessage);

    @Shadow public abstract ServerWorld getServerWorld();

    public ServerPlayerEntityMixin(World world, BlockPos pos, float yaw, GameProfile gameProfile) {
        super(world, pos, yaw, gameProfile);
    }

//    @Inject(method = "onSpawn", at = @At("HEAD"), cancellable = true)
//    public void bam$onSpawn(CallbackInfo ci) {
//        if (isAdventure()) {
//            this.onScreenHandlerOpened(this.getAdventureInventoryScreenHandler());
//            ci.cancel();
//        }
//    }

//    /**
//     * @author TheRedBrain
//     * @reason account for AdventureInventoryScreenHandler
//     */
//    @Overwrite
//    public void onSpawn() {
//        this.onScreenHandlerOpened(this.bamcore$getInventoryScreenHandler());
//    }

    @Inject(method = "tick", at = @At("TAIL"))
    public void bamcore$tick(CallbackInfo ci) {
        if (!this.getWorld().isClient) {
//            this.currentScreenHandler = this.bamcore$getAdventureInventoryScreenHandler();
            if (!ItemStack.areItemsEqual(mainHandSlotStack, ((DuckPlayerInventoryMixin)this.getInventory()).bamcore$getMainHand()) || !ItemStack.areItemsEqual(alternateMainHandSlotStack, ((DuckPlayerInventoryMixin)this.getInventory()).bamcore$getAlternativeMainHand())) {
                bamcore$sendChangedHandSlotsPacket(true);
            }
            mainHandSlotStack = ((DuckPlayerInventoryMixin)this.getInventory()).bamcore$getMainHand();
            alternateMainHandSlotStack = ((DuckPlayerInventoryMixin)this.getInventory()).bamcore$getAlternativeMainHand();
            if (!ItemStack.areItemsEqual(offHandSlotStack, ((DuckPlayerInventoryMixin)this.getInventory()).bamcore$getOffHandStack()) || !ItemStack.areItemsEqual(alternateOffHandSlotStack, ((DuckPlayerInventoryMixin)this.getInventory()).bamcore$getAlternativeOffHand())) {
                bamcore$sendChangedHandSlotsPacket(false);
            }
            offHandSlotStack = ((DuckPlayerInventoryMixin)this.getInventory()).bamcore$getOffHandStack();
            alternateOffHandSlotStack = ((DuckPlayerInventoryMixin)this.getInventory()).bamcore$getAlternativeOffHand();
//            if (this.isUsingHotbarItem() != this.wasUsingHotbarItem()) {
//                sendIsUsingHotbarItemClientPacket(this.isUsingHotbarItem());
//                this.setWasUsingHotbarItem(this.isUsingHotbarItem());
//            }
        }
    }

    @Inject(method = "onDeath", at = @At("TAIL"))
    public void bamcore$onDeath(DamageSource damageSource, CallbackInfo ci) {
        ServerPlayerEntity player = (ServerPlayerEntity) (Object) this;
        MinecraftServer server = this.getServer();
        PlayerDeathCallback.EVENT.invoker().kill(player, server, damageSource);
    }

    /**
     * @author TheRedBrain
     * @reason
     */
    public Either<SleepFailureReason, Unit> trySleep(BlockPos pos) {
        Direction direction = (Direction)this.getWorld().getBlockState(pos).get(HorizontalFacingBlock.FACING);
        if (!this.isSleeping() && this.isAlive()) {
            if (!this.getWorld().getDimension().natural()) {
                return Either.left(PlayerEntity.SleepFailureReason.NOT_POSSIBLE_HERE);
            } else if (!this.isBedTooFarAway(pos, direction)) {
                return Either.left(PlayerEntity.SleepFailureReason.TOO_FAR_AWAY);
            } else if (this.isBedObstructed(pos, direction)) {
                return Either.left(PlayerEntity.SleepFailureReason.OBSTRUCTED);
            } else {
                // ability to set spawn on bed is now controlled by a gamerule
                if (this.getServerWorld().getGameRules().getBoolean(GameRulesRegistry.CAN_SET_SPAWN_ON_BEDS)) {
                    this.setSpawnPoint(this.getWorld().getRegistryKey(), pos, this.getYaw(), false, true);
                }
                if (this.getWorld().isDay()) {
                    return Either.left(PlayerEntity.SleepFailureReason.NOT_POSSIBLE_NOW);
                } else {
                    if (!this.isCreative()) {
                        double d = 8.0;
                        double e = 5.0;
                        Vec3d vec3d = Vec3d.ofBottomCenter(pos);
                        List<HostileEntity> list = this.getWorld().getEntitiesByClass(HostileEntity.class, new Box(vec3d.getX() - 8.0, vec3d.getY() - 5.0, vec3d.getZ() - 8.0, vec3d.getX() + 8.0, vec3d.getY() + 5.0, vec3d.getZ() + 8.0), (entity) -> {
                            return entity.isAngryAt(this);
                        });
                        if (!list.isEmpty()) {
                            return Either.left(PlayerEntity.SleepFailureReason.NOT_SAFE);
                        }
                    }

                    Either<PlayerEntity.SleepFailureReason, Unit> either = super.trySleep(pos).ifRight((unit) -> {
                        this.incrementStat(Stats.SLEEP_IN_BED);
                        Criteria.SLEPT_IN_BED.trigger((ServerPlayerEntity) (Object) this);
                    });
                    if (!this.getServerWorld().isSleepingEnabled()) {
                        this.sendMessage(Text.translatable("sleep.not_possible"), true);
                    }

                    ((ServerWorld)this.getWorld()).updateSleepingPlayers();
                    return either;
                }
            }
        } else {
            return Either.left(PlayerEntity.SleepFailureReason.OTHER_PROBLEM);
        }
    }

//    /**
//     * @author TheRedBrain
//     * @reason account for AdventureInventoryScreenHandler
//     */
//    @Overwrite
//    public OptionalInt openHandledScreen(@Nullable NamedScreenHandlerFactory factory) {
//        if (factory == null) {
//            return OptionalInt.empty();
//        }
//        if (this.currentScreenHandler != this.bamcore$getInventoryScreenHandler()) {
//            this.closeHandledScreen();
//        }
//        this.incrementScreenHandlerSyncId();
//        ScreenHandler screenHandler = factory.createMenu(this.screenHandlerSyncId, this.getInventory(), this);
//        if (screenHandler == null) {
//            if (this.isSpectator()) {
//                this.sendMessage(Text.translatable("container.spectatorCantOpen").formatted(Formatting.RED), true);
//            }
//            return OptionalInt.empty();
//        }
//        this.networkHandler.sendPacket(new OpenScreenS2CPacket(screenHandler.syncId, screenHandler.getType(), factory.getDisplayName()));
//        this.onScreenHandlerOpened(screenHandler);
//        this.currentScreenHandler = screenHandler;
//        return OptionalInt.of(this.screenHandlerSyncId);
//    }

//    /**
//     * @author TheRedBrain
//     * @reason account for AdventureInventoryScreenHandler
//     */
//    @Overwrite
//    public void onHandledScreenClosed() {
//        this.currentScreenHandler.onClosed(this);
//        this.playerScreenHandler.copySharedSlots(this.currentScreenHandler);
//        this.bamcore$getAdventureInventoryScreenHandler().copySharedSlots(this.currentScreenHandler);
//        this.currentScreenHandler = this.playerScreenHandler;
//    }

    @Override
    public boolean bamcore$isAdventure() {
        return this.interactionManager.getGameMode() == GameMode.ADVENTURE;
    }

    private void bamcore$sendChangedHandSlotsPacket(boolean mainHand) {
        Collection<ServerPlayerEntity> players = PlayerLookup.tracking((ServerWorld) this.getWorld(), this.getBlockPos());
//        Collection<ServerPlayerEntity> players = PlayerLookup.tracking(this);
//        players.remove(((ServerPlayerEntity) (Object) this));
        PacketByteBuf data = new PacketByteBuf(Unpooled.buffer());
        data.writeInt(this.getId());
        data.writeBoolean(mainHand);
        players.forEach(player -> ServerPlayNetworking.send(player, BetterAdventureModCoreServerPacket.SWAPPED_HAND_ITEMS_PACKET, data));
    }

//    private void sendIsUsingHotbarItemClientPacket(boolean isUsingHotbarItem) {
//        Collection<ServerPlayerEntity> players = PlayerLookup.tracking((ServerWorld) this.getWorld(), this.getBlockPos());
////        Collection<ServerPlayerEntity> players = PlayerLookup.tracking(this);
////        players.remove(((ServerPlayerEntity) (Object) this));
//        PacketByteBuf data = new PacketByteBuf(Unpooled.buffer());
//        data.writeInt(this.getId());
//        data.writeBoolean(isUsingHotbarItem);
//        players.forEach(player -> ServerPlayNetworking.send(player, RPGModServerPacket.IS_USING_HOTBAR_ITEM_CLIENT_PACKET, data));
//    }
}
