package com.github.theredbrain.bamcore.network.packet;

import com.github.theredbrain.bamcore.registry.StatusEffectsRegistry;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;

public class SheatheWeaponsPacketReceiver implements ServerPlayNetworking.PlayChannelHandler {
    @Override
    public void receive(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {

        boolean canSheathe = buf.readBoolean();

        server.execute(() -> {

        String playerName = player.getName().getString();
        String command = "";
        if (player.hasStatusEffect(StatusEffectsRegistry.WEAPONS_SHEATHED_EFFECT)) {
            command = "effect clear " + playerName + " bamcore:weapons_sheathed_effect";
        } else if (canSheathe){
            command = "effect give " + playerName + " bamcore:weapons_sheathed_effect infinite 0 true";
        }
        if (!command.equals("")) {
            server.getCommandManager().executeWithPrefix(server.getCommandSource(), command);
        }
//            int handSlotId;
//            int alternateHandSlotId;
//
//            PlayerInventory playerInventory = player.getInventory();
//
//            if (canSheathe) {
//                handSlotId = 40;
//                alternateHandSlotId = 42;
//            } else {
//                handSlotId = 41;
//                alternateHandSlotId = 43;
//            }
//
//            ItemStack itemStack = playerInventory.getStack(handSlotId);
//            ItemStack alternateItemStack = playerInventory.getStack(alternateHandSlotId);
//
//            if (itemStack.isEmpty() && alternateItemStack.isEmpty()) {
//                return;
//            }
//            playerInventory.setStack(handSlotId, alternateItemStack);
//            playerInventory.setStack(alternateHandSlotId, itemStack);
//            playerInventory.markDirty();
//
//            // TODO play sounds
        });
    }
}
