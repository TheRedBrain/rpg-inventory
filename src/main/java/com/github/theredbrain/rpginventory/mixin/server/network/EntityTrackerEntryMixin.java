package com.github.theredbrain.rpginventory.mixin.server.network;

import com.github.theredbrain.rpginventory.entity.player.DuckPlayerEntityMixin;
import com.github.theredbrain.rpginventory.entity.player.DuckPlayerInventoryMixin;
import com.github.theredbrain.rpginventory.registry.ServerPacketRegistry;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
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
    public void rpginventory$startTracking(ServerPlayerEntity serverPlayer, CallbackInfo info) {
        if (this.entity instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) entity;
            if (!((DuckPlayerInventoryMixin)serverPlayer.getInventory()).rpginventory$getMainHand().isEmpty() || !((DuckPlayerInventoryMixin)serverPlayer.getInventory()).rpginventory$getAlternativeMainHand().isEmpty()) {
                PacketByteBuf data = new PacketByteBuf(Unpooled.buffer());
                data.writeInt(serverPlayer.getId());
                data.writeBoolean(true);
                ServerPlayNetworking.send((ServerPlayerEntity) player, ServerPacketRegistry.SWAPPED_HAND_ITEMS_PACKET, data); // TODO convert to packet
            }
            if (!serverPlayer.getEquippedStack(EquipmentSlot.OFFHAND).isEmpty() || !((DuckPlayerInventoryMixin)serverPlayer.getInventory()).rpginventory$getAlternativeOffhand().isEmpty()) {
                PacketByteBuf data = new PacketByteBuf(Unpooled.buffer());
                data.writeInt(serverPlayer.getId());
                data.writeBoolean(false);
                ServerPlayNetworking.send((ServerPlayerEntity) player, ServerPacketRegistry.SWAPPED_HAND_ITEMS_PACKET, data); // TODO convert to packet
            }
            if (!((DuckPlayerInventoryMixin)serverPlayer.getInventory()).rpginventory$getMainHand().isEmpty() || !((DuckPlayerInventoryMixin)serverPlayer.getInventory()).rpginventory$getSheathedMainHand().isEmpty()) {
                PacketByteBuf data = new PacketByteBuf(Unpooled.buffer());
                data.writeInt(serverPlayer.getId());
                data.writeBoolean(true);
                data.writeBoolean(((DuckPlayerEntityMixin)player).rpginventory$isMainHandStackSheathed());
                ServerPlayNetworking.send((ServerPlayerEntity) player, ServerPacketRegistry.SHEATHED_WEAPONS_PACKET, data); // TODO convert to packet
            }
            if (!serverPlayer.getEquippedStack(EquipmentSlot.OFFHAND).isEmpty() || !((DuckPlayerInventoryMixin)serverPlayer.getInventory()).rpginventory$getSheathedOffhand().isEmpty()) {
                PacketByteBuf data = new PacketByteBuf(Unpooled.buffer());
                data.writeInt(serverPlayer.getId());
                data.writeBoolean(false);
                data.writeBoolean(((DuckPlayerEntityMixin)player).rpginventory$isOffHandStackSheathed());
                ServerPlayNetworking.send((ServerPlayerEntity) player, ServerPacketRegistry.SHEATHED_WEAPONS_PACKET, data); // TODO convert to packet
            }
        }
    }
}
