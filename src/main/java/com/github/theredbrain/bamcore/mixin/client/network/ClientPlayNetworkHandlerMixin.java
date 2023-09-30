package com.github.theredbrain.bamcore.mixin.client.network;

import com.github.theredbrain.bamcore.entity.player.DuckPlayerEntityMixin;
import com.github.theredbrain.bamcore.screen.AdventureInventoryScreenHandler;
import dev.emi.trinkets.TrinketPlayerScreenHandler;
import dev.emi.trinkets.api.TrinketInventory;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.CreativeInventoryScreen;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.NetworkThreadUtils;
import net.minecraft.network.packet.s2c.play.InventoryS2CPacket;
import net.minecraft.network.packet.s2c.play.PlayerRespawnS2CPacket;
import net.minecraft.network.packet.s2c.play.ScreenHandlerSlotUpdateS2CPacket;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Environment(EnvType.CLIENT)
@Mixin(ClientPlayNetworkHandler.class)
public abstract class ClientPlayNetworkHandlerMixin {

    @Shadow
    @Final
    private MinecraftClient client;

    /**
     * @author TheRedBrain
     * @reason use AdventureInventoryScreenHandler if in adventure mode
     */
    @Overwrite
    public void onScreenHandlerSlotUpdate(ScreenHandlerSlotUpdateS2CPacket packet) {
        NetworkThreadUtils.forceMainThread(packet, (ClientPlayNetworkHandler) (Object) this, this.client);
        ClientPlayerEntity playerEntity = this.client.player;
        ItemStack itemStack = packet.getItemStack();
        int i = packet.getSlot();
        this.client.getTutorialManager().onSlotUpdate(itemStack);
        if (packet.getSyncId() == ScreenHandlerSlotUpdateS2CPacket.UPDATE_CURSOR_SYNC_ID) {
            if (!(this.client.currentScreen instanceof CreativeInventoryScreen)) {
                playerEntity.currentScreenHandler.setCursorStack(itemStack);
            }
        } else if (packet.getSyncId() == ScreenHandlerSlotUpdateS2CPacket.UPDATE_PLAYER_INVENTORY_SYNC_ID) {
            playerEntity.getInventory().setStack(i, itemStack);
        } else {
            boolean bl = false;
            Screen screen = this.client.currentScreen;
            if (screen instanceof CreativeInventoryScreen) {
                CreativeInventoryScreen creativeInventoryScreen = (CreativeInventoryScreen)screen;
                boolean bl2 = bl = !creativeInventoryScreen.isInventoryTabSelected();
            }

            if (packet.getSyncId() == 0 && AdventureInventoryScreenHandler.isInHotbar(i)) {
                ItemStack itemStack2;
                if (!itemStack.isEmpty() && ((itemStack2 = ((DuckPlayerEntityMixin)playerEntity).bamcore$getInventoryScreenHandler().getSlot(i).getStack()).isEmpty() || itemStack2.getCount() < itemStack.getCount())) {
                    itemStack.setBobbingAnimationTime(5);
                }
                ((DuckPlayerEntityMixin)playerEntity).bamcore$getInventoryScreenHandler().setStackInSlot(i, packet.getRevision(), itemStack);
            } else if (!(packet.getSyncId() != playerEntity.currentScreenHandler.syncId || packet.getSyncId() == 0 && bl)) {
                playerEntity.currentScreenHandler.setStackInSlot(i, packet.getRevision(), itemStack);
            }
        }
    }

    /**
     * @author TheRedBrain
     * @reason use AdventureInventoryScreenHandler if in adventure mode
     */
    @Overwrite
    public void onInventory(InventoryS2CPacket packet) {
        NetworkThreadUtils.forceMainThread(packet, (ClientPlayNetworkHandler) (Object) this, this.client);
        ClientPlayerEntity playerEntity = this.client.player;
        if (packet.getSyncId() == 0) {
            ((DuckPlayerEntityMixin)playerEntity).bamcore$getInventoryScreenHandler().updateSlotStacks(packet.getRevision(), packet.getContents(), packet.getCursorStack());
        } else if (packet.getSyncId() == playerEntity.currentScreenHandler.syncId) {
            playerEntity.currentScreenHandler.updateSlotStacks(packet.getRevision(), packet.getContents(), packet.getCursorStack());
        }
    }

    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerEntity;setId(I)V"), method = "onPlayerRespawn", locals = LocalCapture.CAPTURE_FAILSOFT)
    private void onPlayerRespawn(PlayerRespawnS2CPacket packet, CallbackInfo ci, RegistryKey<?> registryKey, RegistryEntry<?> registryEntry, ClientPlayerEntity clientPlayerEntity, int i, String string, ClientPlayerEntity clientPlayerEntity2) {
        if (packet.hasFlag(PlayerRespawnS2CPacket.KEEP_ATTRIBUTES)) {
            TrinketInventory.copyFrom(clientPlayerEntity, clientPlayerEntity2);
            ((TrinketPlayerScreenHandler) ((DuckPlayerEntityMixin)clientPlayerEntity2).bamcore$getInventoryScreenHandler()).trinkets$updateTrinketSlots(false);
        }
    }
}
