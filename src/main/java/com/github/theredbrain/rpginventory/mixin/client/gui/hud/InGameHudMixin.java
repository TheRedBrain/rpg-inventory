package com.github.theredbrain.rpginventory.mixin.client.gui.hud;

import com.github.theredbrain.rpginventory.RPGInventory;
import com.github.theredbrain.rpginventory.RPGInventoryClient;
import com.github.theredbrain.rpginventory.entity.player.DuckPlayerInventoryMixin;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(InGameHud.class)
public abstract class InGameHudMixin {

    @Shadow
    protected abstract PlayerEntity getCameraPlayer();

    @Shadow
    private int scaledWidth;

    @Shadow
    private int scaledHeight;

    @Shadow
    protected abstract void renderHotbarItem(DrawContext context, int x, int y, float f, PlayerEntity player, ItemStack stack, int seed);

    @Unique
    private static final Identifier HOTBAR_HAND_SLOTS_TEXTURE = RPGInventory.identifier("textures/gui/sprites/hud/hotbar_hand_slots.png");
    @Unique
    private static final Identifier HOTBAR_ALTERNATE_HAND_SLOTS_TEXTURE = RPGInventory.identifier("textures/gui/sprites/hud/hotbar_alternate_hand_slots.png");

    @Inject(method = "renderHotbar", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/util/math/MatrixStack;pop()V"))
    private void rpginventory$invoke_renderHotbar(float tickDelta, DrawContext context, CallbackInfo ci) {

        int i = this.scaledWidth / 2;
        // slots for all hand items
//            context.drawGuiTexture(HOTBAR_HAND_SLOTS_TEXTURE, i - 91 - 49, this.scaledHeight - 23 - raisedDistance, 49, 24);
        context.drawTexture(HOTBAR_HAND_SLOTS_TEXTURE, i + RPGInventoryClient.clientConfig.hand_slots_x_offset, this.scaledHeight + RPGInventoryClient.clientConfig.hand_slots_y_offset, 0, 0, 49, 24, 49, 24);
//            context.drawGuiTexture(HOTBAR_ALTERNATE_HAND_SLOTS_TEXTURE, i + 91, this.scaledHeight - 23 - raisedDistance, 49, 24);
        context.drawTexture(HOTBAR_ALTERNATE_HAND_SLOTS_TEXTURE, i + RPGInventoryClient.clientConfig.alternative_hand_slots_x_offset, this.scaledHeight + RPGInventoryClient.clientConfig.alternative_hand_slots_y_offset, 0, 0, 49, 24, 49, 24);

    }

    @Inject(method = "renderHotbar", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderSystem;enableBlend()V"))
    private void rpginventory$post_renderHotbar(float tickDelta, DrawContext context, CallbackInfo ci) {
        PlayerEntity playerEntity = this.getCameraPlayer();
        if (playerEntity != null) {
            ItemStack itemStackMainHand = ((DuckPlayerInventoryMixin) playerEntity.getInventory()).rpginventory$getMainHand();
            ItemStack itemStackOffHand = playerEntity.getEquippedStack(EquipmentSlot.OFFHAND);
            ItemStack itemStackAlternativeMainHand = ((DuckPlayerInventoryMixin) playerEntity.getInventory()).rpginventory$getAlternativeMainHand();
            ItemStack itemStackAlternativeOffHand = ((DuckPlayerInventoryMixin) playerEntity.getInventory()).rpginventory$getAlternativeOffHand();

            int l = 10;

            int x = this.scaledWidth / 2 + RPGInventoryClient.clientConfig.hand_slots_x_offset;
            int y = this.scaledHeight + 4 + RPGInventoryClient.clientConfig.hand_slots_y_offset;
            boolean off_hand_slot_is_right = RPGInventoryClient.clientConfig.off_hand_item_is_right;
            this.renderHotbarItem(context, x + 23, y, tickDelta, playerEntity, off_hand_slot_is_right ? itemStackOffHand : itemStackMainHand, l++);
            this.renderHotbarItem(context, x + 3, y, tickDelta, playerEntity, off_hand_slot_is_right ? itemStackMainHand : itemStackOffHand, l++);

            x = this.scaledWidth / 2 + RPGInventoryClient.clientConfig.alternative_hand_slots_x_offset;
            y = this.scaledHeight + 4 + RPGInventoryClient.clientConfig.alternative_hand_slots_y_offset;
            boolean alternative_off_hand_slot_is_right = RPGInventoryClient.clientConfig.alternative_off_hand_item_is_right;
            this.renderHotbarItem(context, x + 10, y, tickDelta, playerEntity, alternative_off_hand_slot_is_right ? itemStackAlternativeMainHand : itemStackAlternativeOffHand, l++);
            this.renderHotbarItem(context, x + 30, y, tickDelta, playerEntity, alternative_off_hand_slot_is_right ? itemStackAlternativeOffHand : itemStackAlternativeMainHand, l);
        }
    }

    // effectively disables rendering of the normal offhand slot in the HUD
    @Redirect(
            method = "renderHotbar",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/player/PlayerEntity;getOffHandStack()Lnet/minecraft/item/ItemStack;"
            )
    )
    public ItemStack overhauleddamage$redirect_hasVehicle(PlayerEntity instance) {
        return ItemStack.EMPTY;
    }

    // disables rendering of the armor bar when disabled in the client config
    @Redirect(
            method = "renderStatusBars",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/player/PlayerEntity;getArmor()I"
            )
    )
    public int overhauleddamage$redirect_getArmor(PlayerEntity instance) {
        return RPGInventoryClient.clientConfig.show_armor_bar ? instance.getArmor() : 0;
    }

}