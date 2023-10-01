package com.github.theredbrain.bamcore.mixin.client.gui.hud;

import com.github.theredbrain.bamcore.BetterAdventureModeCore;
import com.github.theredbrain.bamcore.entity.player.DuckPlayerEntityMixin;
import com.github.theredbrain.bamcore.entity.player.DuckPlayerInventoryMixin;
import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.option.AttackIndicator;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.HungerManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.util.Arm;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(InGameHud.class)
public abstract class InGameHudMixin {

    @Shadow protected abstract PlayerEntity getCameraPlayer();

    @Shadow private int scaledWidth;

    @Shadow private int scaledHeight;

    @Shadow @Final private MinecraftClient client;

    @Shadow protected abstract void renderHotbarItem(DrawContext context, int x, int y, float f, PlayerEntity player, ItemStack stack, int seed);

    @Shadow @Final private static Identifier WIDGETS_TEXTURE;

    @Shadow @Final private static Identifier ICONS;

    private static final Identifier CUSTOM_WIDGETS_TEXTURE = BetterAdventureModeCore.identifier("textures/gui/custom_widgets.png");
    private static final Identifier CUSTOM_ICONS = BetterAdventureModeCore.identifier("textures/gui/custom_icons.png");

    @Inject(method = "renderHotbar", at = @At("HEAD"), cancellable = true)
    private void bamcore$renderHotbar(float tickDelta, DrawContext context, CallbackInfo ci) {
        PlayerEntity playerEntity = this.getCameraPlayer();
        if (playerEntity != null) {
            ItemStack itemStackMainHand = ((DuckPlayerInventoryMixin)playerEntity.getInventory()).bamcore$getMainHand();
            ItemStack itemStackOffHand = playerEntity.getInventory().offHand.get(0);
            ItemStack itemStackAlternativeMainHand = ((DuckPlayerInventoryMixin)playerEntity.getInventory()).bamcore$getAlternativeMainHand();
            ItemStack itemStackAlternativeOffHand = ((DuckPlayerInventoryMixin)playerEntity.getInventory()).bamcore$getAlternativeOffHand();
            Arm arm = playerEntity.getMainArm().getOpposite();
            int i = this.scaledWidth / 2;

            // compatibility for Raised
            int raisedDistance = 0;
            if (FabricLoader.getInstance().getObjectShare().get("raised:hud") instanceof Integer distance) {
                raisedDistance = distance;
            }

            context.getMatrices().push();
            context.getMatrices().translate(0.0F, 0.0F, -90.0F);
            context.drawTexture(WIDGETS_TEXTURE, i - 91, this.scaledHeight - 22 - raisedDistance, 0, 0, 182, 22);
            context.drawTexture(WIDGETS_TEXTURE, i - 91 - 1 + playerEntity.getInventory().selectedSlot * 20, this.scaledHeight - 22 - 1 - raisedDistance, 0, 22, 24, 24);

            // slots for all hand items
            context.drawTexture(CUSTOM_WIDGETS_TEXTURE, i - 91 - 49, this.scaledHeight - 23 - raisedDistance, 0, 0, 49, 24);
            context.drawTexture(CUSTOM_WIDGETS_TEXTURE, i + 91, this.scaledHeight - 23 - raisedDistance, 49, 0, 49, 24);

            context.getMatrices().pop();
            int l = 1;

            int m;
            int n;
            int o;
            for(m = 0; m < 9; ++m) {
                n = i - 90 + m * 20 + 2;
                o = this.scaledHeight - 16 - 3 - raisedDistance;
                this.renderHotbarItem(context, n, o, tickDelta, playerEntity, (ItemStack)playerEntity.getInventory().main.get(m), l++);
            }

            m = this.scaledHeight - 16 - 3 - raisedDistance;
            if (arm == Arm.LEFT) {
                this.renderHotbarItem(context, i - 91 - 26, m, tickDelta, playerEntity, itemStackOffHand, l);
                this.renderHotbarItem(context, i - 91 - 46, m, tickDelta, playerEntity, itemStackMainHand, l);
                this.renderHotbarItem(context, i + 91 + 10, m, tickDelta, playerEntity, itemStackAlternativeMainHand, l);
                this.renderHotbarItem(context, i + 91 + 30, m, tickDelta, playerEntity, itemStackAlternativeOffHand, l);
            } else {
                // TODO wait for feedback
                this.renderHotbarItem(context, i - 91 - 26, m, tickDelta, playerEntity, itemStackAlternativeMainHand, l);
                this.renderHotbarItem(context, i - 91 - 46, m, tickDelta, playerEntity, itemStackAlternativeOffHand, l);
                this.renderHotbarItem(context, i + 91 + 10, m, tickDelta, playerEntity, itemStackOffHand, l);
                this.renderHotbarItem(context, i + 91 + 30, m, tickDelta, playerEntity, itemStackMainHand, l);
            }

            RenderSystem.enableBlend();
            if (this.client.options.getAttackIndicator().getValue() == AttackIndicator.HOTBAR) {
                float f = this.client.player.getAttackCooldownProgress(0.0F);
                if (f < 1.0F) {
                    n = this.scaledHeight - 20 - raisedDistance; // TODO necessary?
                    o = i + 91 + 6;
                    if (arm == Arm.RIGHT) {
                        o = i - 91 - 22;
                    }

                    int p = (int)(f * 19.0F);
                    context.drawTexture(ICONS, o, n, 0, 94, 18, 18);
                    context.drawTexture(ICONS, o, n + 18 - p, 18, 112 - p, 18, p);
                }
            }

            RenderSystem.disableBlend();
        }
        ci.cancel();
    }

    @Inject(method = "renderExperienceBar", at = @At("HEAD"), cancellable = true)
    public void bamcore$renderExperienceBar(DrawContext context, int x, CallbackInfo ci) {
        PlayerEntity playerEntity = this.getCameraPlayer();
        if (playerEntity != null && ((DuckPlayerEntityMixin)playerEntity).bamcore$isAdventure()) {
            ci.cancel();
        }
    }

    /**
     * @author TheRedBrain
     * @reason
     */
    @Inject(method = "renderStatusBars", at = @At("HEAD"), cancellable = true)
    private void bamcore$renderStatusBars(DrawContext context, CallbackInfo ci) {
        PlayerEntity playerEntity = this.getCameraPlayer();
        if (playerEntity != null && ((DuckPlayerEntityMixin)playerEntity).bamcore$isAdventure()) {
            int health = MathHelper.ceil(playerEntity.getHealth());
            int maxHealth = MathHelper.ceil(playerEntity.getMaxHealth());
            int stamina = MathHelper.ceil(((DuckPlayerEntityMixin)playerEntity).bamcore$getStamina());
            int maxStamina = MathHelper.ceil(((DuckPlayerEntityMixin)playerEntity).bamcore$getMaxStamina());
            int mana = MathHelper.ceil(((DuckPlayerEntityMixin)playerEntity).bamcore$getMana());
            int maxMana = MathHelper.ceil(((DuckPlayerEntityMixin)playerEntity).bamcore$getMaxMana());

            int attributeBarX = this.scaledWidth / 2 - 91;
            int attributeBarY = this.scaledHeight - 32 + 3;
            int normalizedHealthRatio = (int)((double) health / Math.max(maxHealth, 1) * 182);
            int normalizedStaminaRatio = (int)((double) stamina / Math.max(maxStamina, 1) * 182);
            int normalizedManaRatio = (int)((double) mana / Math.max(maxMana, 1) * 182);

            this.client.getProfiler().push("health_bar");
            context.drawTexture(CUSTOM_ICONS, attributeBarX, attributeBarY, 0, 0, 182, 5);
            if (normalizedHealthRatio > 0) {
                context.drawTexture(CUSTOM_ICONS, attributeBarX, attributeBarY, 0, 15, normalizedHealthRatio, 5);
            }

            this.client.getProfiler().swap("stamina_bar");
            context.drawTexture(CUSTOM_ICONS, attributeBarX, attributeBarY - 7, 0, 0, 182, 5);
            if (normalizedStaminaRatio > 0) {
                context.drawTexture(CUSTOM_ICONS, attributeBarX, attributeBarY - 7, 0, 5, normalizedStaminaRatio, 5);
            }

            if (maxMana > 0) {
                this.client.getProfiler().swap("mana_bar");
                context.drawTexture(CUSTOM_ICONS, attributeBarX, attributeBarY - 14, 0, 0, 182, 5);
                if (normalizedManaRatio > 0) {
                    context.drawTexture(CUSTOM_ICONS, attributeBarX, attributeBarY - 14, 0, 10, normalizedManaRatio, 5);
                }
            }
            this.client.getProfiler().pop();
            ci.cancel();
        }
    }
}
