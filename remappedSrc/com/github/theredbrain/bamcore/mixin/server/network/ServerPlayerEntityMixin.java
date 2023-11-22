package com.github.theredbrain.bamcore.mixin.server.network;

import com.github.theredbrain.bamcore.entity.player.DuckPlayerEntityMixin;
import com.github.theredbrain.bamcore.entity.player.DuckPlayerInventoryMixin;
import com.github.theredbrain.bamcore.network.event.PlayerDeathCallback;
import com.github.theredbrain.bamcore.network.packet.BetterAdventureModeCoreServerPacket;
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
import net.minecraft.server.MinecraftServer;
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

    @Shadow
    @Final
    public ServerPlayerInteractionManager interactionManager;

    @Shadow protected abstract boolean isBedTooFarAway(BlockPos pos, Direction direction);

    @Shadow protected abstract boolean isBedObstructed(BlockPos pos, Direction direction);

    @Shadow public abstract void setSpawnPoint(RegistryKey<World> dimension, @Nullable BlockPos pos, float angle, boolean forced, boolean sendMessage);

    @Shadow public abstract ServerWorld getServerWorld();

    public ServerPlayerEntityMixin(World world, BlockPos pos, float yaw, GameProfile gameProfile) {
        super(world, pos, yaw, gameProfile);
    }

    @Inject(method = "tick", at = @At("TAIL"))
    public void bamcore$tick(CallbackInfo ci) {
        if (!this.method_48926().isClient) {
            if (!ItemStack.areItemsEqual(mainHandSlotStack, ((DuckPlayerInventoryMixin)this.getInventory()).bamcore$getMainHand()) || !ItemStack.areItemsEqual(alternateMainHandSlotStack, ((DuckPlayerInventoryMixin)this.getInventory()).bamcore$getAlternativeMainHand())) {
                bamcore$sendChangedHandSlotsPacket(true);
            }
            mainHandSlotStack = ((DuckPlayerInventoryMixin)this.getInventory()).bamcore$getMainHand();
            alternateMainHandSlotStack = ((DuckPlayerInventoryMixin)this.getInventory()).bamcore$getAlternativeMainHand();
            if (!ItemStack.areItemsEqual(offHandSlotStack, ((DuckPlayerInventoryMixin)this.getInventory()).bamcore$getOffHand()) || !ItemStack.areItemsEqual(alternateOffHandSlotStack, ((DuckPlayerInventoryMixin)this.getInventory()).bamcore$getAlternativeOffHand())) {
                bamcore$sendChangedHandSlotsPacket(false);
            }
            offHandSlotStack = ((DuckPlayerInventoryMixin)this.getInventory()).bamcore$getOffHand();
            alternateOffHandSlotStack = ((DuckPlayerInventoryMixin)this.getInventory()).bamcore$getAlternativeOffHand();
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
        Direction direction = (Direction)this.method_48926().getBlockState(pos).get(HorizontalFacingBlock.FACING);
        if (!this.isSleeping() && this.isAlive()) {
            if (!this.method_48926().getDimension().natural()) {
                return Either.left(PlayerEntity.SleepFailureReason.NOT_POSSIBLE_HERE);
            } else if (!this.isBedTooFarAway(pos, direction)) {
                return Either.left(PlayerEntity.SleepFailureReason.TOO_FAR_AWAY);
            } else if (this.isBedObstructed(pos, direction)) {
                return Either.left(PlayerEntity.SleepFailureReason.OBSTRUCTED);
            } else {
                // ability to set spawn on bed is now controlled by a gamerule
                if (this.getServerWorld().getGameRules().getBoolean(GameRulesRegistry.CAN_SET_SPAWN_ON_BEDS)) {
                    this.setSpawnPoint(this.method_48926().getRegistryKey(), pos, this.getYaw(), false, true);
                }
                if (this.method_48926().isDay()) {
                    return Either.left(PlayerEntity.SleepFailureReason.NOT_POSSIBLE_NOW);
                } else {
                    if (!this.isCreative()) {
                        double d = 8.0;
                        double e = 5.0;
                        Vec3d vec3d = Vec3d.ofBottomCenter(pos);
                        List<HostileEntity> list = this.method_48926().getEntitiesByClass(HostileEntity.class, new Box(vec3d.getX() - 8.0, vec3d.getY() - 5.0, vec3d.getZ() - 8.0, vec3d.getX() + 8.0, vec3d.getY() + 5.0, vec3d.getZ() + 8.0), (entity) -> {
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

                    ((ServerWorld)this.method_48926()).updateSleepingPlayers();
                    return either;
                }
            }
        } else {
            return Either.left(PlayerEntity.SleepFailureReason.OTHER_PROBLEM);
        }
    }

    @Override
    public boolean bamcore$isAdventure() {
        return this.interactionManager.getGameMode() == GameMode.ADVENTURE;
    }

    private void bamcore$sendChangedHandSlotsPacket(boolean mainHand) {
        Collection<ServerPlayerEntity> players = PlayerLookup.tracking((ServerWorld) this.method_48926(), this.getBlockPos());
        PacketByteBuf data = new PacketByteBuf(Unpooled.buffer());
        data.writeInt(this.getId());
        data.writeBoolean(mainHand);
        players.forEach(player -> ServerPlayNetworking.send(player, BetterAdventureModeCoreServerPacket.SWAPPED_HAND_ITEMS_PACKET, data));
    }
}
