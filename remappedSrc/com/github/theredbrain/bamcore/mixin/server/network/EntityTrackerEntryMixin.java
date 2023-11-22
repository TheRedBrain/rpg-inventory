package com.github.theredbrain.bamcore.mixin.server.network;

import com.github.theredbrain.bamcore.entity.player.DuckPlayerInventoryMixin;
import com.github.theredbrain.bamcore.network.packet.BetterAdventureModeCoreServerPacket;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.EntityTrackerEntry;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityTrackerEntry.class)
public class EntityTrackerEntryMixin {

    @Shadow @Final private Entity entity;

    @Inject(method = "startTracking", at = @At(value = "TAIL"))
    public void bam$startTrackingAdditionalHandSlots(ServerPlayerEntity serverPlayer, CallbackInfo info) {
        if (this.entity instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) entity;
            if (!((DuckPlayerInventoryMixin)serverPlayer.getInventory()).bamcore$getMainHand().isEmpty() || !((DuckPlayerInventoryMixin)serverPlayer.getInventory()).bamcore$getAlternativeMainHand().isEmpty()) {
                PacketByteBuf data = new PacketByteBuf(Unpooled.buffer());
                data.writeInt(serverPlayer.getId());
                data.writeBoolean(true);
                ServerPlayNetworking.send((ServerPlayerEntity) player, BetterAdventureModeCoreServerPacket.SWAPPED_HAND_ITEMS_PACKET, data);
            }
            if (!((DuckPlayerInventoryMixin)serverPlayer.getInventory()).bamcore$getOffHand().isEmpty() || !((DuckPlayerInventoryMixin)serverPlayer.getInventory()).bamcore$getAlternativeOffHand().isEmpty()) {
                PacketByteBuf data = new PacketByteBuf(Unpooled.buffer());
                data.writeInt(serverPlayer.getId());
                data.writeBoolean(false);
                ServerPlayNetworking.send((ServerPlayerEntity) player, BetterAdventureModeCoreServerPacket.SWAPPED_HAND_ITEMS_PACKET, data);
            }
        }
    }
}
