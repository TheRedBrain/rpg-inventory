package com.github.theredbrain.betteradventuremode.mixin.client.network;

import com.github.theredbrain.betteradventuremode.block.entity.HousingBlockBlockEntity;
import com.github.theredbrain.betteradventuremode.registry.ComponentsRegistry;
import com.github.theredbrain.betteradventuremode.registry.StatusEffectsRegistry;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.network.SequencedPacketCreator;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.fluid.FluidState;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.GameMode;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Objects;

@Environment(value= EnvType.CLIENT)
@Mixin(ClientPlayerInteractionManager.class)
public abstract class ClientPlayerInteractionManagerMixin {

    @Shadow private GameMode gameMode;

    @Shadow @Final private MinecraftClient client;

    @Shadow protected abstract void sendSequencedPacket(ClientWorld world, SequencedPacketCreator packetCreator);

    @Shadow public abstract boolean breakBlock(BlockPos pos);

    @Shadow private int blockBreakingCooldown;

    @Shadow protected abstract void syncSelectedSlot();

    @Inject(method = "breakBlock", at = @At("HEAD"), cancellable = true)
    public void betteradventuremode$breakBlock(BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
        if (this.gameMode == GameMode.ADVENTURE && this.client.player != null && this.client.player.hasStatusEffect(StatusEffectsRegistry.ADVENTURE_BUILDING_EFFECT)) {
            ClientWorld world = this.client.world;
            BlockState blockState = world.getBlockState(pos);
            Block block = blockState.getBlock();
            block.onBreak(world, pos, blockState, this.client.player);
            FluidState fluidState = world.getFluidState(pos);
            boolean bl = world.setBlockState(pos, fluidState.getBlockState(), Block.NOTIFY_ALL | Block.REDRAW_ON_MAIN_THREAD);
            if (bl) {
                block.onBroken(world, pos, blockState);
            }
            cir.setReturnValue(bl);
            cir.cancel();
        }
    }

    @Inject(method = "attackBlock", at = @At("HEAD"), cancellable = true)
    public void betteradventuremode$attackBlock(BlockPos pos, Direction direction, CallbackInfoReturnable<Boolean> cir) {
        if (this.gameMode == GameMode.ADVENTURE && this.client.player != null && this.client.player.hasStatusEffect(StatusEffectsRegistry.ADVENTURE_BUILDING_EFFECT)) {
            BlockPos housingBlockPos = ComponentsRegistry.CURRENT_HOUSING_BLOCK_POS.get(this.client.player).getValue();
            boolean bl = false;
            if (!Objects.equals(housingBlockPos, new BlockPos(0, 0, 0)) && this.client.world != null && this.client.world.getBlockEntity(housingBlockPos) instanceof HousingBlockBlockEntity housingBlockEntity) {
                bl = housingBlockEntity.influenceAreaContains(pos);
            }
            if (bl) {
                BlockState blockState = this.client.world.getBlockState(pos);
                this.client.getTutorialManager().onBlockBreaking(this.client.world, pos, blockState, 1.0f);
                this.sendSequencedPacket(this.client.world, sequence -> {
                    this.breakBlock(pos);
                    return new PlayerActionC2SPacket(PlayerActionC2SPacket.Action.START_DESTROY_BLOCK, pos, direction, sequence);
                });
                this.blockBreakingCooldown = 5;
            }
            cir.setReturnValue(bl);
            cir.cancel();
        }
    }

    @Inject(method = "interactBlock", at = @At("HEAD"), cancellable = true)
    public void betteradventuremode$interactBlock(ClientPlayerEntity player, Hand hand, BlockHitResult hitResult, CallbackInfoReturnable<ActionResult> cir) {
        if (this.gameMode == GameMode.ADVENTURE && player.hasStatusEffect(StatusEffectsRegistry.ADVENTURE_BUILDING_EFFECT)) {
            this.syncSelectedSlot();
            BlockPos housingBlockPos = ComponentsRegistry.CURRENT_HOUSING_BLOCK_POS.get(player).getValue();
            boolean bl = false;
            if (!Objects.equals(housingBlockPos, new BlockPos(0, 0, 0)) && this.client.world != null && this.client.world.getBlockEntity(housingBlockPos) instanceof HousingBlockBlockEntity housingBlockEntity) {
                bl = housingBlockEntity.influenceAreaContains(hitResult.getBlockPos().offset(hitResult.getSide()));
            }
            if (!bl) {
                cir.setReturnValue(ActionResult.FAIL);
                cir.cancel();
            }
        }
    }
}
