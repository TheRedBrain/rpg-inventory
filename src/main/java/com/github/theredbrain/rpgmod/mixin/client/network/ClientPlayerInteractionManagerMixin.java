package com.github.theredbrain.rpgmod.mixin.client.network;

import com.github.theredbrain.rpgmod.client.network.CustomSequencedPacketCreator;
import com.github.theredbrain.rpgmod.client.network.DuckClientPlayerInteractionManagerMixin;
import com.github.theredbrain.rpgmod.item.DuckItemStackMixin;
import com.github.theredbrain.rpgmod.network.listener.CustomServerPlayPacketListener;
import com.github.theredbrain.rpgmod.network.packet.c2s.play.ConsumeItemC2SPacket;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.network.PendingUpdateManager;
import net.minecraft.client.network.SequencedPacketCreator;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.listener.ServerPlayPacketListener;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.ActionResult;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.GameMode;
import net.minecraft.world.World;
import org.apache.commons.lang3.mutable.MutableObject;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ClientPlayerInteractionManager.class)
public class ClientPlayerInteractionManagerMixin implements DuckClientPlayerInteractionManagerMixin {

    @Shadow
    @Final
    private MinecraftClient client;

    @Shadow
    @Final
    private ClientPlayNetworkHandler networkHandler;

    @Shadow
    private GameMode gameMode;

    @Shadow
    private void syncSelectedSlot() {
        throw new AssertionError();
    }

    @Shadow
    private void sendSequencedPacket(ClientWorld world, SequencedPacketCreator packetCreator) {
        throw new AssertionError();
    }

    public ActionResult consumeItem(PlayerEntity player, World world, ItemStack itemStack) {
        if (!(this.gameMode == GameMode.ADVENTURE)) {
            return ActionResult.PASS;
        }
        this.syncSelectedSlot();
        this.networkHandler.sendPacket(new PlayerMoveC2SPacket.Full(player.getX(), player.getY(), player.getZ(), player.getYaw(), player.getPitch(), player.isOnGround()));
        MutableObject mutableObject = new MutableObject();
//        this.networkHandler.sendPacket(new ConsumeItemC2SPacket(player, world, itemStack);
        this.sendCustomSequencedPacket(this.client.world, sequence -> {
            ConsumeItemC2SPacket consumeItemC2SPacket = new ConsumeItemC2SPacket(sequence);//new PlayerInteractItemC2SPacket(hand, sequence);
//            ItemStack itemStack = player.getInventory().main.get(player.getInventory().selectedSlot);//getStackInHand(hand);
            if (player.getItemCooldownManager().isCoolingDown(itemStack.getItem())) {
                mutableObject.setValue(ActionResult.PASS);
                return consumeItemC2SPacket;
            }
            TypedActionResult<ItemStack> typedActionResult = ((DuckItemStackMixin)(Object) itemStack).adventureUse(this.client.world, player);
            ItemStack itemStack2 = typedActionResult.getValue();
            if (itemStack2 != itemStack) {
                player.getInventory().setStack(player.getInventory().selectedSlot, itemStack2);//setStackInHand(hand, itemStack2);
            }
            mutableObject.setValue(typedActionResult.getResult());
            return consumeItemC2SPacket;
        });
        return (ActionResult)((Object)mutableObject.getValue());
    }

    private void sendCustomSequencedPacket(ClientWorld world, CustomSequencedPacketCreator packetCreator) {
        try (PendingUpdateManager pendingUpdateManager = world.getPendingUpdateManager().incrementSequence();){
            int i = pendingUpdateManager.getSequence();
            Packet<CustomServerPlayPacketListener> packet = packetCreator.predict(i);
            this.networkHandler.sendPacket(packet);
        }
    }
}
