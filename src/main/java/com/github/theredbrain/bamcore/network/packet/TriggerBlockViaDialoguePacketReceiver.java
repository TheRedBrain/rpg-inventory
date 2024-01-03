package com.github.theredbrain.bamcore.network.packet;

import com.github.theredbrain.bamcore.block.Triggerable;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TriggerBlockViaDialoguePacketReceiver implements ServerPlayNetworking.PlayPacketHandler<TriggerBlockViaDialoguePacket> {

    @Override
    public void receive(TriggerBlockViaDialoguePacket packet, ServerPlayerEntity player, PacketSender responseSender) {

        BlockPos blockPos = packet.blockPos;

        if (player.getWorld().getBlockEntity(blockPos) instanceof Triggerable triggerable) {
            triggerable.trigger();
        }
    }
}
