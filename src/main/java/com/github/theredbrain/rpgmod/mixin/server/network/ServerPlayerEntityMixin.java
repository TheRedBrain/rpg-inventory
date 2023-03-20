package com.github.theredbrain.rpgmod.mixin.server.network;

import com.github.theredbrain.rpgmod.entity.player.DuckPlayerEntityMixin;
import com.mojang.authlib.GameProfile;
import net.minecraft.advancement.PlayerAdvancementTracker;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.encryption.PublicPlayerSession;
import net.minecraft.network.packet.s2c.play.OpenScreenS2CPacket;
import net.minecraft.scoreboard.ScoreboardCriterion;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.network.ServerPlayerInteractionManager;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.GameMode;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.OptionalInt;

@Mixin(value=ServerPlayerEntity.class, priority=950)
public abstract class ServerPlayerEntityMixin extends PlayerEntity implements DuckPlayerEntityMixin {

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

    @Shadow
    public ServerWorld getWorld() { // 1193
        throw new AssertionError();
    }

//    @Shadow
//    public Entity getCameraEntity() { // 1331
//        throw new AssertionError();
//    }
//
//    @Shadow
//    public void setCameraEntity(@Nullable Entity entity) { // 1335
//        throw new AssertionError();
//    }

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
     */
    @Overwrite
    public void onSpawn() {
        this.onScreenHandlerOpened(this.getAdventureInventoryScreenHandler());
    }

    @Inject(method = "tick", at = @At("TAIL"))
    public void bam$tick(CallbackInfo ci) {
        if (!this.world.isClient) {
            this.currentScreenHandler = this.getAdventureInventoryScreenHandler();
        }
    }

    /**
     * @author TheRedBrain
     */
    @Overwrite
    public OptionalInt openHandledScreen(@Nullable NamedScreenHandlerFactory factory) {
        if (factory == null) {
            return OptionalInt.empty();
        }
        if (this.currentScreenHandler != this.getAdventureInventoryScreenHandler()) {
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
     */
    @Overwrite
    public void closeScreenHandler() {
        this.currentScreenHandler.close(this);
        this.getAdventureInventoryScreenHandler().copySharedSlots(this.currentScreenHandler);
        this.currentScreenHandler = this.getAdventureInventoryScreenHandler();
    }

    @Override
    public boolean isAdventure() {
        return this.interactionManager.getGameMode() == GameMode.ADVENTURE;
    }
}
