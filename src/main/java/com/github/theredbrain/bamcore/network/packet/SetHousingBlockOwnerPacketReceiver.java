package com.github.theredbrain.bamcore.network.packet;

import com.github.theredbrain.bamcore.block.entity.HousingBlockBlockEntity;
import com.github.theredbrain.bamcore.registry.StatusEffectsRegistry;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Objects;

public class SetHousingBlockOwnerPacketReceiver implements ServerPlayNetworking.PlayChannelHandler {

    @Override
    public void receive(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {

        BlockPos housingBlockPos = buf.readBlockPos();

        String owner = buf.readString();

        server.execute(() -> {
            World world = player.getWorld();

            boolean updateSuccessful = true;

            BlockEntity blockEntity = world.getBlockEntity(housingBlockPos);
            BlockState blockState = world.getBlockState(housingBlockPos);

            if (blockEntity instanceof HousingBlockBlockEntity housingBlockBlockEntity) {

                if (!housingBlockBlockEntity.setOwner(owner)) {
                    player.sendMessage(Text.translatable("housing_block.owner.invalid"), false);
                    updateSuccessful = false;
                } else {
                    if (Objects.equals(owner, "")) {
                        housingBlockBlockEntity.setIsOwnerSet(false);
                    } else {
                    }
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
                world.updateListeners(housingBlockPos, blockState, blockState, Block.NOTIFY_ALL);
            }
        });
    }
}
