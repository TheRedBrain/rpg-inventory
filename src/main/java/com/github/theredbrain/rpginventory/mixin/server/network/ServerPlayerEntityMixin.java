package com.github.theredbrain.rpginventory.mixin.server.network;

import com.github.theredbrain.rpginventory.entity.player.DuckPlayerEntityMixin;
import com.github.theredbrain.rpginventory.entity.player.DuckPlayerInventoryMixin;
import com.github.theredbrain.rpginventory.registry.ItemRegistry;
import com.github.theredbrain.rpginventory.registry.ServerPacketRegistry;
import com.mojang.authlib.GameProfile;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Collection;

@Mixin(value=ServerPlayerEntity.class, priority=950)
public abstract class ServerPlayerEntityMixin extends PlayerEntity implements DuckPlayerEntityMixin {

    @Unique
    ItemStack mainHandSlotStack = ItemStack.EMPTY;

    @Unique
    ItemStack alternateMainHandSlotStack = ItemStack.EMPTY;

    @Unique
    ItemStack offHandSlotStack = ItemStack.EMPTY;

    @Unique
    ItemStack alternateOffHandSlotStack = ItemStack.EMPTY;

    @Unique
    boolean isMainHandWeaponSheathed = false;

    @Unique
    boolean isOffHandWeaponSheathed = false;

    public ServerPlayerEntityMixin(World world, BlockPos pos, float yaw, GameProfile gameProfile) {
        super(world, pos, yaw, gameProfile);
    }

    @Inject(method = "tick", at = @At("TAIL"))
    public void rpginventory$tick(CallbackInfo ci) {
        if (!this.getWorld().isClient) {
            if (!((DuckPlayerInventoryMixin) this.getInventory()).rpginventory$getEmptyMainHand().isOf(ItemRegistry.DEFAULT_EMPTY_HAND_WEAPON)) {
                ((DuckPlayerInventoryMixin) this.getInventory()).rpginventory$setEmptyMainHand(ItemRegistry.DEFAULT_EMPTY_HAND_WEAPON.getDefaultStack());
            }
            if (!((DuckPlayerInventoryMixin) this.getInventory()).rpginventory$getEmptyOffHand().isOf(ItemRegistry.DEFAULT_EMPTY_HAND_WEAPON)) {
                ((DuckPlayerInventoryMixin) this.getInventory()).rpginventory$setEmptyOffHand(ItemRegistry.DEFAULT_EMPTY_HAND_WEAPON.getDefaultStack());
            }
            ItemStack newMainHandStack = ((DuckPlayerInventoryMixin) this.getInventory()).rpginventory$getMainHand();
            ItemStack newAlternativeMainHandStack = ((DuckPlayerInventoryMixin) this.getInventory()).rpginventory$getAlternativeMainHand();
            if (!ItemStack.areItemsEqual(mainHandSlotStack, newMainHandStack) || !ItemStack.areItemsEqual(alternateMainHandSlotStack, newAlternativeMainHandStack)) {
                rpginventory$sendChangedHandSlotsPacket(true);
            }
            mainHandSlotStack = newMainHandStack;
            alternateMainHandSlotStack = newAlternativeMainHandStack;
            ItemStack newOffHandStack = this.getEquippedStack(EquipmentSlot.OFFHAND);
            ItemStack newAlternativeOffHandStack = ((DuckPlayerInventoryMixin) this.getInventory()).rpginventory$getAlternativeOffHand();
            if (!ItemStack.areItemsEqual(offHandSlotStack, newOffHandStack) || !ItemStack.areItemsEqual(alternateOffHandSlotStack, newAlternativeOffHandStack)) {
                rpginventory$sendChangedHandSlotsPacket(false);
            }
            offHandSlotStack = newOffHandStack;
            alternateOffHandSlotStack = newAlternativeOffHandStack;
            boolean isMainHandWeaponSheathed = this.rpginventory$isMainHandStackSheathed();
            if (this.isMainHandWeaponSheathed != isMainHandWeaponSheathed) {
                rpginventory$sendSheathedWeaponsPacket(true, isMainHandWeaponSheathed);
                this.isMainHandWeaponSheathed = isMainHandWeaponSheathed;
            }
            boolean isOffHandWeaponSheathed = this.rpginventory$isOffHandStackSheathed();
            if (this.isOffHandWeaponSheathed != isOffHandWeaponSheathed) {
                rpginventory$sendSheathedWeaponsPacket(false, isOffHandWeaponSheathed);
                this.isOffHandWeaponSheathed = isOffHandWeaponSheathed;
            }
        }
    }

    @Unique
    private void rpginventory$sendChangedHandSlotsPacket(boolean mainHand) {
        Collection<ServerPlayerEntity> players = PlayerLookup.tracking((ServerWorld) this.getWorld(), this.getBlockPos());
        PacketByteBuf data = new PacketByteBuf(Unpooled.buffer());
        data.writeInt(this.getId());
        data.writeBoolean(mainHand);
        players.forEach(player -> ServerPlayNetworking.send(player, ServerPacketRegistry.SWAPPED_HAND_ITEMS_PACKET, data)); // TODO convert to packet
    }

    @Unique
    private void rpginventory$sendSheathedWeaponsPacket(boolean mainHand, boolean isSheathed) {
        Collection<ServerPlayerEntity> players = PlayerLookup.tracking((ServerWorld) this.getWorld(), this.getBlockPos());
        PacketByteBuf data = new PacketByteBuf(Unpooled.buffer());
        data.writeInt(this.getId());
        data.writeBoolean(mainHand);
        data.writeBoolean(isSheathed);
        players.forEach(player -> ServerPlayNetworking.send(player, ServerPacketRegistry.SHEATHED_WEAPONS_PACKET, data)); // TODO convert to packet
    }
}
