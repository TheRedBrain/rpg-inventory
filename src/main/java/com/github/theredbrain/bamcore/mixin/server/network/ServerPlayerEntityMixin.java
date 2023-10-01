package com.github.theredbrain.bamcore.mixin.server.network;

import com.github.theredbrain.bamcore.entity.player.DuckPlayerEntityMixin;
import com.github.theredbrain.bamcore.network.event.PlayerDeathCallback;
import com.github.theredbrain.bamcore.network.packet.BetterAdventureModCoreServerPacket;
import com.mojang.authlib.GameProfile;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.s2c.play.OpenScreenS2CPacket;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.network.ServerPlayerInteractionManager;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameMode;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Collection;
import java.util.OptionalInt;

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

    @Shadow
    public ServerPlayNetworkHandler networkHandler;

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

    @Shadow
    private int screenHandlerSyncId;

    @Shadow
    private void onScreenHandlerOpened(ScreenHandler screenHandler) { // 408
        throw new AssertionError();
    }

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

    @Shadow
    private void incrementScreenHandlerSyncId() { // 932
        throw new AssertionError();
    }

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

    @Shadow public abstract void playerTick();

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

    /**
     * @author TheRedBrain
     * @reason account for AdventureInventoryScreenHandler
     */
    @Overwrite
    public void onSpawn() {
        this.onScreenHandlerOpened(this.bamcore$getInventoryScreenHandler());
    }

    @Inject(method = "tick", at = @At("TAIL"))
    public void bamcore$tick(CallbackInfo ci) {
        if (!this.getWorld().isClient) {
            this.currentScreenHandler = this.bamcore$getInventoryScreenHandler();
            if (!ItemStack.areItemsEqual(mainHandSlotStack, this.getInventory().getStack(40)) || !ItemStack.areItemsEqual(alternateMainHandSlotStack, this.getInventory().getStack(42))) {
                bamcore$sendChangedHandSlotsPacket(true);
            }
            mainHandSlotStack = this.getInventory().getStack(40);
            alternateMainHandSlotStack = this.getInventory().getStack(42);
            if (!ItemStack.areItemsEqual(offHandSlotStack, this.getInventory().getStack(41)) || !ItemStack.areItemsEqual(alternateOffHandSlotStack, this.getInventory().getStack(43))) {
                bamcore$sendChangedHandSlotsPacket(false);
            }
            offHandSlotStack = this.getInventory().getStack(41);
            alternateOffHandSlotStack = this.getInventory().getStack(43);
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
     * @reason account for AdventureInventoryScreenHandler
     */
    @Overwrite
    public OptionalInt openHandledScreen(@Nullable NamedScreenHandlerFactory factory) {
        if (factory == null) {
            return OptionalInt.empty();
        }
        if (this.currentScreenHandler != this.bamcore$getInventoryScreenHandler()) {
            this.closeHandledScreen();
        }
        this.incrementScreenHandlerSyncId();
        ScreenHandler screenHandler = factory.createMenu(this.screenHandlerSyncId, this.getInventory(), this);
        if (screenHandler == null) {
            if (this.isSpectator()) {
                this.sendMessage(Text.translatable("container.spectatorCantOpen").formatted(Formatting.RED), true);
            }
            return OptionalInt.empty();
        }
        this.networkHandler.sendPacket(new OpenScreenS2CPacket(screenHandler.syncId, screenHandler.getType(), factory.getDisplayName()));
        this.onScreenHandlerOpened(screenHandler);
        this.currentScreenHandler = screenHandler;
        return OptionalInt.of(this.screenHandlerSyncId);
    }

    /**
     * @author TheRedBrain
     * @reason account for AdventureInventoryScreenHandler
     */
    @Overwrite
    public void onHandledScreenClosed() {
        this.currentScreenHandler.onClosed(this);
        this.bamcore$getInventoryScreenHandler().copySharedSlots(this.currentScreenHandler);
        this.currentScreenHandler = this.bamcore$getInventoryScreenHandler();
    }

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
