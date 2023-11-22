package com.github.theredbrain.bamcore.network.packet;

import com.github.theredbrain.bamcore.block.entity.HousingBlockBlockEntity;
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

import java.util.ArrayList;
import java.util.List;

public class UpdateHousingBlockAdventurePacketReceiver implements ServerPlayNetworking.PlayChannelHandler {

    @Override
    public void receive(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {

        if (!player.isCreativeLevelTwoOp()) {
            return;
        }

        BlockPos housingBlockPos = buf.readBlockPos();

        List<String> coOwnerList = new ArrayList<>(List.of());
        int coOwnerListSize = buf.readInt();
        for (int i = 0; i < coOwnerListSize; i++) {
            coOwnerList.add(buf.readString());
        }
        List<String> trustedList = new ArrayList<>(List.of());
        int trustedListSize = buf.readInt();
        for (int i = 0; i < trustedListSize; i++) {
            trustedList.add(buf.readString());
        }

        List<String> guestList = new ArrayList<>(List.of());
        int guestListSize = buf.readInt();
        for (int i = 0; i < guestListSize; i++) {
            guestList.add(buf.readString());
        }

        server.execute(() -> {
            World world = player.method_48926();

            boolean updateSuccessful = true;

            BlockEntity blockEntity = world.getBlockEntity(housingBlockPos);
            BlockState blockState = world.getBlockState(housingBlockPos);

            if (blockEntity instanceof HousingBlockBlockEntity housingBlockBlockEntity) {

                if (!housingBlockBlockEntity.setCoOwnerList(coOwnerList)) {
                    player.sendMessage(Text.translatable("housing_block.coOwnerList.invalid"), false);
                    updateSuccessful = false;
                }
                if (!housingBlockBlockEntity.setTrustedList(trustedList)) {
                    player.sendMessage(Text.translatable("housing_block.trustedList.invalid"), false);
                    updateSuccessful = false;
                }
                if (!housingBlockBlockEntity.setGuestList(guestList)) {
                    player.sendMessage(Text.translatable("housing_block.guestList.invalid"), false);
                    updateSuccessful = false;
                }
                if (updateSuccessful) {
                    player.sendMessage(Text.translatable("housing_block.update_successful"), true);
                }
                housingBlockBlockEntity.markDirty();
                world.updateListeners(housingBlockPos, blockState, blockState, Block.NOTIFY_ALL);
            }
        });
    }
}
