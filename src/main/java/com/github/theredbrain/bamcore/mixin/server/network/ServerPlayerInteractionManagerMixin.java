package com.github.theredbrain.bamcore.mixin.server.network;

import com.github.theredbrain.bamcore.registry.StatusEffectsRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.OperatorBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.network.ServerPlayerInteractionManager;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.GameMode;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = ServerPlayerInteractionManager.class, priority = 950)
public abstract class ServerPlayerInteractionManagerMixin {

    @Shadow @Final protected ServerPlayerEntity player;

    @Shadow protected ServerWorld world;

    @Shadow private GameMode gameMode;

    @Shadow public abstract void finishMining(BlockPos pos, int sequence, String reason);

    @Inject(method = "processBlockBreakingAction", at = @At("HEAD"), cancellable = true)
    public void processBlockBreakingAction(BlockPos pos, PlayerActionC2SPacket.Action action, Direction direction, int worldHeight, int sequence, CallbackInfo ci) {
        if (action == PlayerActionC2SPacket.Action.START_DESTROY_BLOCK) {
            if (this.gameMode == GameMode.ADVENTURE && this.player.hasStatusEffect(StatusEffectsRegistry.ADVENTURE_BUILDING_EFFECT)) {
                this.finishMining(pos, sequence, "creative destroy");
                ci.cancel();
            }
        }

    }

    @Inject(method = "tryBreakBlock", at = @At("HEAD"), cancellable = true)
    public void tryBreakBlock(BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
        if (this.gameMode == GameMode.ADVENTURE && this.player.hasStatusEffect(StatusEffectsRegistry.ADVENTURE_BUILDING_EFFECT)) {
            BlockState blockState = this.world.getBlockState(pos);
            BlockEntity blockEntity = this.world.getBlockEntity(pos);
            Block block = blockState.getBlock();
            if (block instanceof OperatorBlock && !this.player.isCreativeLevelTwoOp()) {
                this.world.updateListeners(pos, blockState, blockState, Block.NOTIFY_ALL);
                cir.setReturnValue(false);
                cir.cancel();
            }
            block.onBreak(this.world, pos, blockState, this.player);
            boolean bl = this.world.removeBlock(pos, false);
            if (bl) {
                block.onBroken(this.world, pos, blockState);
            }
            ItemStack itemStack = this.player.getMainHandStack();
            ItemStack itemStack2 = itemStack.copy();
            boolean bl2 = this.player.canHarvest(blockState);
            itemStack.postMine(this.world, blockState, pos, this.player);
            if (bl && bl2) {
                block.afterBreak(this.world, this.player, pos, blockState, blockEntity, itemStack2);
            }
            cir.setReturnValue(true);
            cir.cancel();
        }
    }
}
