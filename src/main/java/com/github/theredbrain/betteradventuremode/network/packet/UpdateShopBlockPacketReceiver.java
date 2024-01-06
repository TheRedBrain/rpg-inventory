package com.github.theredbrain.betteradventuremode.network.packet;

import com.github.theredbrain.betteradventuremode.block.entity.ShopBlockEntity;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class UpdateShopBlockPacketReceiver implements ServerPlayNetworking.PlayPacketHandler<UpdateShopBlockPacket> {

    @Override
    public void receive(UpdateShopBlockPacket packet, ServerPlayerEntity player, PacketSender responseSender) {

        if (!player.isCreativeLevelTwoOp()) {
            return;
        }

        BlockPos shopBlockPosition = packet.shopBlockPosition;
        String shopIdentifer = packet.shopIdentifier;

        World world = player.getWorld();

        boolean updateSuccessful = true;

        BlockEntity blockEntity = world.getBlockEntity(shopBlockPosition);
        BlockState blockState = world.getBlockState(shopBlockPosition);

        if (blockEntity instanceof ShopBlockEntity shopBlockEntity) {

            if (!shopBlockEntity.setShopIdentifier(shopIdentifer)) {
                player.sendMessage(Text.translatable("shop_block.shopIdentifer.invalid"), false);
                updateSuccessful = false;
            }
            if (updateSuccessful) {
                player.sendMessage(Text.translatable("shop_block.update_successful"), true);
            }
            shopBlockEntity.markDirty();
            world.updateListeners(shopBlockPosition, blockState, blockState, Block.NOTIFY_ALL);
        }

    }
}
