package com.github.theredbrain.rpgmod.mixin.server.network;

import com.github.theredbrain.rpgmod.entity.player.DuckPlayerEntityMixin;
import com.github.theredbrain.rpgmod.entity.player.DuckPlayerInventoryMixin;
import com.github.theredbrain.rpgmod.network.listener.CustomServerPlayPacketListener;
import com.github.theredbrain.rpgmod.network.packet.c2s.play.ConsumeItemC2SPacket;
import com.github.theredbrain.rpgmod.network.packet.c2s.play.CustomPlayerActionC2SPacket;
import com.github.theredbrain.rpgmod.registry.Tags;
import com.github.theredbrain.rpgmod.server.network.DuckServerPlayerInteractionManagerMixin;
import net.minecraft.item.ItemStack;
import net.minecraft.network.NetworkThreadUtils;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ServerPlayNetworkHandler.class)
public abstract class ServerPlayNetworkHandlerMixin implements CustomServerPlayPacketListener {

    @Shadow
    public ServerPlayerEntity player;


    @Override
    public void onCustomPlayerAction(CustomPlayerActionC2SPacket packet) {
        NetworkThreadUtils.forceMainThread(packet, ((ServerPlayNetworkHandler) (Object) this), this.player.getWorld());
        this.player.updateLastActionTime();
        CustomPlayerActionC2SPacket.Action action = packet.getAction();
        switch (action) {
            case SWAP_MAINHAND_ITEMS: {
                if (!this.player.isSpectator()) {
                    ItemStack itemStack = ((DuckPlayerInventoryMixin)this.player.getInventory()).bam$getMainHand();
                    ((DuckPlayerInventoryMixin)this.player.getInventory()).bam$setMainHand(((DuckPlayerInventoryMixin)this.player.getInventory()).bam$getAlternativeMainHand());
                    ((DuckPlayerInventoryMixin)this.player.getInventory()).bam$setAlternativeMainHand(itemStack);
                    this.player.clearActiveItem();
                }
                return;
            }
            case SWAP_OFFHAND_ITEMS: {
                if (!this.player.isSpectator()) {
                    ItemStack itemStack = this.player.getInventory().offHand.get(0);
                    this.player.getInventory().offHand.set(0, ((DuckPlayerInventoryMixin)this.player.getInventory()).bam$getAlternativeOffHand());
                    ((DuckPlayerInventoryMixin)this.player.getInventory()).bam$setAlternativeOffHand(itemStack);
                    this.player.clearActiveItem();
                }
                return;
            }
        }
        throw new IllegalArgumentException("Invalid player action");
    }

    public void onConsumeItem(ConsumeItemC2SPacket var1) {

        if (((DuckPlayerEntityMixin)this.player).isAdventure()) {
            ServerWorld serverWorld = this.player.getWorld();
            ItemStack itemStack = this.player.getInventory().main.get(this.player.getInventory().selectedSlot);
            this.player.updateLastActionTime();
            if (itemStack.isEmpty() || !itemStack.isItemEnabled(serverWorld.getEnabledFeatures())) {
                return;
            }
            if (itemStack.isIn(Tags.ADVENTURE_HOTBAR_ITEMS)) {
                ActionResult actionResult = ((DuckServerPlayerInteractionManagerMixin)this.player.interactionManager).consumeItem(this.player, serverWorld, itemStack); // TODO
                if (actionResult.shouldSwingHand()) {
                    this.player.swingHand(Hand.MAIN_HAND, true);
                }
                this.player.clearActiveItem();
            }
        }
    }

}
//        NetworkThreadUtils.forceMainThread(packet, this, this.player.getWorld());
//                this.updateSequence(packet.getSequence());
//                ServerWorld serverWorld = this.player.getWorld();
//                Hand hand = packet.getHand();
//                ItemStack itemStack = this.player.getStackInHand(hand);
//                this.player.updateLastActionTime();
//                if (itemStack.isEmpty() || !itemStack.isItemEnabled(serverWorld.getEnabledFeatures())) {
//                return;
//                }
//                ActionResult actionResult = this.player.interactionManager.interactItem(this.player, serverWorld, itemStack, hand);
//                if (actionResult.shouldSwingHand()) {
//                this.player.swingHand(hand, true);
//                }
