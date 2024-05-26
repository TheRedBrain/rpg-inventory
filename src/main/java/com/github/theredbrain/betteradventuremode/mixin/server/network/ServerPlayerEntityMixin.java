package com.github.theredbrain.betteradventuremode.mixin.server.network;

import com.github.theredbrain.betteradventuremode.entity.player.DuckPlayerEntityMixin;
import com.github.theredbrain.betteradventuremode.entity.player.DuckPlayerInventoryMixin;
import com.github.theredbrain.betteradventuremode.registry.ItemRegistry;
import com.github.theredbrain.betteradventuremode.registry.ServerPacketRegistry;
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
    public void betteradventuremode$tick(CallbackInfo ci) {
        if (!this.getWorld().isClient) {
            if (!((DuckPlayerInventoryMixin) this.getInventory()).betteradventuremode$getEmptyMainHand().isOf(ItemRegistry.DEFAULT_EMPTY_HAND_WEAPON)) {
                ((DuckPlayerInventoryMixin) this.getInventory()).betteradventuremode$setEmptyMainHand(ItemRegistry.DEFAULT_EMPTY_HAND_WEAPON.getDefaultStack());
            }
            if (!((DuckPlayerInventoryMixin) this.getInventory()).betteradventuremode$getEmptyOffHand().isOf(ItemRegistry.DEFAULT_EMPTY_HAND_WEAPON)) {
                ((DuckPlayerInventoryMixin) this.getInventory()).betteradventuremode$setEmptyOffHand(ItemRegistry.DEFAULT_EMPTY_HAND_WEAPON.getDefaultStack());
            }
            ItemStack newMainHandStack = ((DuckPlayerInventoryMixin) this.getInventory()).betteradventuremode$getMainHand();
            ItemStack newAlternativeMainHandStack = ((DuckPlayerInventoryMixin) this.getInventory()).betteradventuremode$getAlternativeMainHand();
            if (!ItemStack.areItemsEqual(mainHandSlotStack, newMainHandStack) || !ItemStack.areItemsEqual(alternateMainHandSlotStack, newAlternativeMainHandStack)) {
                betteradventuremode$sendChangedHandSlotsPacket(true);
            }
            mainHandSlotStack = newMainHandStack;
            alternateMainHandSlotStack = newAlternativeMainHandStack;
            ItemStack newOffHandStack = this.getEquippedStack(EquipmentSlot.OFFHAND);
            ItemStack newAlternativeOffHandStack = ((DuckPlayerInventoryMixin) this.getInventory()).betteradventuremode$getAlternativeOffHand();
            if (!ItemStack.areItemsEqual(offHandSlotStack, newOffHandStack) || !ItemStack.areItemsEqual(alternateOffHandSlotStack, newAlternativeOffHandStack)) {
                betteradventuremode$sendChangedHandSlotsPacket(false);
            }
            offHandSlotStack = newOffHandStack;
            alternateOffHandSlotStack = newAlternativeOffHandStack;
            boolean isMainHandWeaponSheathed = this.betteradventuremode$isMainHandStackSheathed();
            if (this.isMainHandWeaponSheathed != isMainHandWeaponSheathed) {
                betteradventuremode$sendSheathedWeaponsPacket(true, isMainHandWeaponSheathed);
                this.isMainHandWeaponSheathed = isMainHandWeaponSheathed;
            }
            boolean isOffHandWeaponSheathed = this.betteradventuremode$isOffHandStackSheathed();
            if (this.isOffHandWeaponSheathed != isOffHandWeaponSheathed) {
                betteradventuremode$sendSheathedWeaponsPacket(false, isOffHandWeaponSheathed);
                this.isOffHandWeaponSheathed = isOffHandWeaponSheathed;
            }
        }
    }

    @Unique
    private void betteradventuremode$sendChangedHandSlotsPacket(boolean mainHand) {
        Collection<ServerPlayerEntity> players = PlayerLookup.tracking((ServerWorld) this.getWorld(), this.getBlockPos());
        PacketByteBuf data = new PacketByteBuf(Unpooled.buffer());
        data.writeInt(this.getId());
        data.writeBoolean(mainHand);
        players.forEach(player -> ServerPlayNetworking.send(player, ServerPacketRegistry.SWAPPED_HAND_ITEMS_PACKET, data)); // TODO convert to packet
    }

    @Unique
    private void betteradventuremode$sendSheathedWeaponsPacket(boolean mainHand, boolean isSheathed) {
        Collection<ServerPlayerEntity> players = PlayerLookup.tracking((ServerWorld) this.getWorld(), this.getBlockPos());
        PacketByteBuf data = new PacketByteBuf(Unpooled.buffer());
        data.writeInt(this.getId());
        data.writeBoolean(mainHand);
        data.writeBoolean(isSheathed);
        players.forEach(player -> ServerPlayNetworking.send(player, ServerPacketRegistry.SHEATHED_WEAPONS_PACKET, data)); // TODO convert to packet
    }
}
