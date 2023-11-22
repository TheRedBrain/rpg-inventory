package com.github.theredbrain.bamcore.network.packet;

import com.github.theredbrain.bamcore.api.item.BetterAdventureMode_BasicWeaponItem;
import com.github.theredbrain.bamcore.entity.player.DuckPlayerEntityMixin;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;

public class AttackStaminaCostPacketReceiver implements ServerPlayNetworking.PlayChannelHandler {
    @Override
    public void receive(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {

        ItemStack attackHandItemStack = buf.readItemStack();

        server.execute(() -> {


            if (((DuckPlayerEntityMixin) player).bamcore$getStamina() <= 0) {
                PacketByteBuf data = new PacketByteBuf(Unpooled.buffer());
                data.writeInt(player.getId());
                ServerPlayNetworking.send(player, BetterAdventureModeCoreServerPacket.CANCEL_ATTACK_PACKET, data);
            } else {
                if (attackHandItemStack.getItem() instanceof BetterAdventureMode_BasicWeaponItem weaponItem) {
                    ((DuckPlayerEntityMixin) player).bamcore$addStamina(-(weaponItem.getStaminaCost()));
                }
            }
        });
    }
}
