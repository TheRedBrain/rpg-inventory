package com.github.theredbrain.bamcore.mixin.client.gui.hud;

import com.github.theredbrain.bamcore.BetterAdventureModeCore;
import com.github.theredbrain.bamcore.BetterAdventureModeCoreClient;
import com.github.theredbrain.bamcore.entity.DuckLivingEntityMixin;
import com.github.theredbrain.bamcore.entity.player.DuckPlayerEntityMixin;
import com.github.theredbrain.bamcore.entity.player.DuckPlayerInventoryMixin;
import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.option.AttackIndicator;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Arm;
import net.minecraft.util.Colors;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.spell_engine.client.util.Color;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.awt.*;

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

    @Shadow public abstract TextRenderer getTextRenderer();

    private static final Identifier CUSTOM_WIDGETS_TEXTURE = BetterAdventureModeCore.identifier("textures/gui/custom_widgets.png");
    private static final Identifier CUSTOM_BARS = BetterAdventureModeCore.identifier("textures/gui/custom_bars.png");
    private static final Identifier BARS = new Identifier("textures/gui/bars.png");

    @Inject(method = "renderHotbar", at = @At("HEAD"), cancellable = true)
    private void bamcore$renderHotbar(float tickDelta, DrawContext context, CallbackInfo ci) {
        PlayerEntity playerEntity = this.getCameraPlayer();
        if (playerEntity != null) {
            ItemStack itemStackMainHand = ((DuckPlayerInventoryMixin)playerEntity.getInventory()).bamcore$getMainHand();
            ItemStack itemStackOffHand = ((DuckPlayerInventoryMixin)playerEntity.getInventory()).bamcore$getOffHand();
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
            int poise = MathHelper.ceil(((DuckLivingEntityMixin) playerEntity).bamcore$getPoise());
            double poiseLimitMultiplier = ((DuckLivingEntityMixin) playerEntity).getStaggerLimitMultiplier();

            int attributeBarX = this.scaledWidth / 2 - 91;
            int attributeBarY = this.scaledHeight - 32 + 2;
            int attributeBarNumberX;
            int attributeBarNumberY;
            int normalizedHealthRatio = (int) (((double) health / Math.max(maxHealth, 1)) * 182);
            int normalizedStaminaRatio = (int)(((double) stamina / Math.max(maxStamina, 1)) * 182);
            int normalizedManaRatio = (int)(((double) mana / Math.max(maxMana, 1)) * 182);
            int normalizedPoiseRatio = (int)(((double) poise/* * 10*/ / Math.max(MathHelper.ceil(maxHealth * poiseLimitMultiplier/* * 10*/), 1)) * 62);

            this.client.getProfiler().push("health_bar");
            context.drawTexture(BARS, attributeBarX, attributeBarY, 0, 20, 182, 5);
            if (normalizedHealthRatio > 0) {
                context.drawTexture(BARS, attributeBarX, attributeBarY, 0, 25, normalizedHealthRatio, 5);
            }
            if (BetterAdventureModeCoreClient.clientConfig.show_resource_bar_numbers) {
                this.client.getProfiler().swap("health_bar_number");
                String string = "" + health;
                attributeBarNumberX = (this.scaledWidth - this.getTextRenderer().getWidth(string)) / 2;
                attributeBarNumberY = attributeBarY - 1;
                context.drawText(this.getTextRenderer(), string, attributeBarNumberX + 1, attributeBarNumberY, 0, false);
                context.drawText(this.getTextRenderer(), string, attributeBarNumberX - 1, attributeBarNumberY, 0, false);
                context.drawText(this.getTextRenderer(), string, attributeBarNumberX, attributeBarNumberY + 1, 0, false);
                context.drawText(this.getTextRenderer(), string, attributeBarNumberX, attributeBarNumberY - 1, 0, false);
                context.drawText(this.getTextRenderer(), string, attributeBarNumberX, attributeBarNumberY, BetterAdventureModeCoreClient.clientConfig.health_bar_number_color, false);
            }

            this.client.getProfiler().swap("stamina_bar");
            context.drawTexture(BARS, attributeBarX, attributeBarY - 9, 0, 30, 182, 5);
            if (normalizedStaminaRatio > 0) {
                context.drawTexture(BARS, attributeBarX, attributeBarY - 9, 0, 35, normalizedStaminaRatio, 5);
            }
            if (BetterAdventureModeCoreClient.clientConfig.show_resource_bar_numbers) {
                this.client.getProfiler().swap("stamina_bar_number");
                String string = "" + stamina;
                attributeBarNumberX = (this.scaledWidth - this.getTextRenderer().getWidth(string)) / 2;
                attributeBarNumberY = attributeBarY - 10;
                context.drawText(this.getTextRenderer(), string, attributeBarNumberX + 1, attributeBarNumberY, 0, false);
                context.drawText(this.getTextRenderer(), string, attributeBarNumberX - 1, attributeBarNumberY, 0, false);
                context.drawText(this.getTextRenderer(), string, attributeBarNumberX, attributeBarNumberY + 1, 0, false);
                context.drawText(this.getTextRenderer(), string, attributeBarNumberX, attributeBarNumberY - 1, 0, false);
                context.drawText(this.getTextRenderer(), string, attributeBarNumberX, attributeBarNumberY, BetterAdventureModeCoreClient.clientConfig.stamina_bar_number_color, false);
            }

            if (maxMana > 0) {
                this.client.getProfiler().swap("mana_bar");
                context.drawTexture(BARS, attributeBarX, attributeBarY - 18, 0, 10, 182, 5);
                if (normalizedManaRatio > 0) {
                    context.drawTexture(BARS, attributeBarX, attributeBarY - 18, 0, 15, normalizedManaRatio, 5);
                }
                if (BetterAdventureModeCoreClient.clientConfig.show_resource_bar_numbers) {
                    this.client.getProfiler().swap("mana_bar_number");
                    String string = "" + mana;
                    attributeBarNumberX = (this.scaledWidth - this.getTextRenderer().getWidth(string)) / 2;
                    attributeBarNumberY = attributeBarY - 19;
                    context.drawText(this.getTextRenderer(), string, attributeBarNumberX + 1, attributeBarNumberY, 0, false);
                    context.drawText(this.getTextRenderer(), string, attributeBarNumberX - 1, attributeBarNumberY, 0, false);
                    context.drawText(this.getTextRenderer(), string, attributeBarNumberX, attributeBarNumberY + 1, 0, false);
                    context.drawText(this.getTextRenderer(), string, attributeBarNumberX, attributeBarNumberY - 1, 0, false);
                    context.drawText(this.getTextRenderer(), string, attributeBarNumberX, attributeBarNumberY, BetterAdventureModeCoreClient.clientConfig.mana_bar_number_color, true);
                }
            }

            if (poise > 0/* && !playerEntity.hasStatusEffect(BetterAdventureModeCoreStatusEffects.STAGGERED)*/) {
                this.client.getProfiler().swap("stagger_bar");
                context.drawTexture(CUSTOM_BARS, attributeBarX + 60, this.scaledHeight / 2 - 7 + 25, 0, 40, 62, 5);
                if (normalizedPoiseRatio > 0) {
                    context.drawTexture(CUSTOM_BARS, attributeBarX + 60, this.scaledHeight / 2 - 7 + 25, 0, 45, normalizedPoiseRatio, 5);
                }
            }
            this.client.getProfiler().pop();
            ci.cancel();
        }
    }
}
