package com.github.theredbrain.betteradventuremode.network.packet;

import com.github.theredbrain.betteradventuremode.block.entity.HousingBlockBlockEntity;
import com.github.theredbrain.betteradventuremode.registry.StatusEffectsRegistry;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Objects;

public class SetHousingBlockOwnerPacketReceiver implements ServerPlayNetworking.PlayPacketHandler<SetHousingBlockOwnerPacket> {
    @Override
    public void receive(SetHousingBlockOwnerPacket packet, ServerPlayerEntity player, PacketSender responseSender) {

        BlockPos housingBlockPosition = packet.housingBlockPosition;

        String owner = packet.owner;

        World world = player.getWorld();

        boolean updateSuccessful = true;

        BlockEntity blockEntity = world.getBlockEntity(housingBlockPosition);
        BlockState blockState = world.getBlockState(housingBlockPosition);

        if (blockEntity instanceof HousingBlockBlockEntity housingBlockBlockEntity) {

            if (!housingBlockBlockEntity.setOwnerUuid(owner)) {
                player.sendMessage(Text.translatable("housing_block.owner.invalid"), false);
                updateSuccessful = false;
            }
            if (updateSuccessful) {
                if (Objects.equals(owner, "")) {
                    housingBlockBlockEntity.setIsOwnerSet(false);
                    player.sendMessage(Text.translatable("housing_block.unclaimed_successful"), true);
                    player.removeStatusEffect(StatusEffectsRegistry.HOUSING_OWNER_EFFECT);
                    player.removeStatusEffect(StatusEffectsRegistry.ADVENTURE_BUILDING_EFFECT);
                } else {
                    housingBlockBlockEntity.setIsOwnerSet(true);
                    player.sendMessage(Text.translatable("housing_block.claimed_successful"), true);
                }
            }
            housingBlockBlockEntity.markDirty();
            world.updateListeners(housingBlockPosition, blockState, blockState, Block.NOTIFY_ALL);
        }
    }
}
