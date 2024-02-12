package com.github.theredbrain.betteradventuremode.mixin.client.gui.hud;

import com.github.theredbrain.betteradventuremode.BetterAdventureMode;
import com.github.theredbrain.betteradventuremode.BetterAdventureModeClient;
import com.github.theredbrain.betteradventuremode.entity.DuckLivingEntityMixin;
import com.github.theredbrain.betteradventuremode.entity.player.DuckPlayerInventoryMixin;
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
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
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

    @Shadow @Final private static Identifier HOTBAR_TEXTURE;
    @Shadow @Final private static Identifier HOTBAR_SELECTION_TEXTURE;
    @Shadow @Final private static Identifier HOTBAR_ATTACK_INDICATOR_BACKGROUND_TEXTURE;
    @Shadow @Final private static Identifier HOTBAR_ATTACK_INDICATOR_PROGRESS_TEXTURE;

    @Shadow public abstract TextRenderer getTextRenderer();

    @Unique
    private static final Identifier BLEEDING_BUILD_UP_BAR_BACKGROUND_TEXTURE = BetterAdventureMode.identifier("boss_bar/short_red_background");
    @Unique
    private static final Identifier BLEEDING_BUILD_UP_BAR_PROGRESS_TEXTURE = BetterAdventureMode.identifier("boss_bar/short_red_progress");
    @Unique
    private static final Identifier BURNING_BUILD_UP_BAR_BACKGROUND_TEXTURE = BetterAdventureMode.identifier("boss_bar/short_red_background");
    @Unique
    private static final Identifier BURNING_BUILD_UP_BAR_PROGRESS_TEXTURE = BetterAdventureMode.identifier("boss_bar/short_red_progress");
    @Unique
    private static final Identifier FREEZE_BUILD_UP_BAR_BACKGROUND_TEXTURE = BetterAdventureMode.identifier("boss_bar/short_blue_background");
    @Unique
    private static final Identifier FREEZE_BUILD_UP_BAR_PROGRESS_TEXTURE = BetterAdventureMode.identifier("boss_bar/short_blue_progress");
    @Unique
    private static final Identifier HOTBAR_HAND_SLOTS_TEXTURE = BetterAdventureMode.identifier("hud/hotbar_hand_slots");
    @Unique
    private static final Identifier HOTBAR_ALTERNATE_HAND_SLOTS_TEXTURE = BetterAdventureMode.identifier("hud/hotbar_alternate_hand_slots");
    @Unique
    private static final Identifier HEALTH_BAR_BACKGROUND_TEXTURE = new Identifier("boss_bar/red_background");
    @Unique
    private static final Identifier HEALTH_BAR_PROGRESS_TEXTURE = new Identifier("boss_bar/red_progress");
    @Unique
    private static final Identifier MANA_BAR_BACKGROUND_TEXTURE = new Identifier("boss_bar/blue_background");
    @Unique
    private static final Identifier MANA_BAR_PROGRESS_TEXTURE = new Identifier("boss_bar/blue_progress");
    @Unique
    private static final Identifier POISON_BUILD_UP_BAR_BACKGROUND_TEXTURE = BetterAdventureMode.identifier("boss_bar/short_green_background");
    @Unique
    private static final Identifier POISON_BUILD_UP_BAR_PROGRESS_TEXTURE = BetterAdventureMode.identifier("boss_bar/short_green_progress");
    @Unique
    private static final Identifier SHOCK_BUILD_UP_BAR_BACKGROUND_TEXTURE = BetterAdventureMode.identifier("boss_bar/short_white_background");
    @Unique
    private static final Identifier SHOCK_BUILD_UP_BAR_PROGRESS_TEXTURE = BetterAdventureMode.identifier("boss_bar/short_white_progress");
    @Unique
    private static final Identifier STAGGER_BAR_BACKGROUND_TEXTURE = BetterAdventureMode.identifier("boss_bar/short_yellow_background");
    @Unique
    private static final Identifier STAGGER_BAR_PROGRESS_TEXTURE = BetterAdventureMode.identifier("boss_bar/short_yellow_progress");
    @Unique
    private static final Identifier STAMINA_BAR_BACKGROUND_TEXTURE = new Identifier("boss_bar/green_background");
    @Unique
    private static final Identifier STAMINA_BAR_PROGRESS_TEXTURE = new Identifier("boss_bar/green_progress");

    @Inject(method = "renderHotbar", at = @At("HEAD"), cancellable = true)
    private void betteradventuremode$renderHotbar(float tickDelta, DrawContext context, CallbackInfo ci) {
        PlayerEntity playerEntity = this.getCameraPlayer();
        if (playerEntity != null) {
            ItemStack itemStackMainHand = ((DuckPlayerInventoryMixin)playerEntity.getInventory()).betteradventuremode$getMainHand();
            ItemStack itemStackOffHand = ((DuckPlayerInventoryMixin)playerEntity.getInventory()).betteradventuremode$getOffHand();
            ItemStack itemStackAlternativeMainHand = ((DuckPlayerInventoryMixin)playerEntity.getInventory()).betteradventuremode$getAlternativeMainHand();
            ItemStack itemStackAlternativeOffHand = ((DuckPlayerInventoryMixin)playerEntity.getInventory()).betteradventuremode$getAlternativeOffHand();
            Arm arm = playerEntity.getMainArm().getOpposite();
            int i = this.scaledWidth / 2;

            // compatibility for Raised
            int raisedDistance = 0;
            if (FabricLoader.getInstance().getObjectShare().get("raised:hud") instanceof Integer distance) {
                raisedDistance = distance;
            }

            context.getMatrices().push();
            context.getMatrices().translate(0.0F, 0.0F, -90.0F);
            context.drawGuiTexture(HOTBAR_TEXTURE, i - 91, this.scaledHeight - 22 - raisedDistance, 182, 22);
            context.drawGuiTexture(HOTBAR_SELECTION_TEXTURE, i - 91 - 1 + playerEntity.getInventory().selectedSlot * 20, this.scaledHeight - 22 - 1 - raisedDistance, 24, 24);

            // slots for all hand items
            context.drawGuiTexture(HOTBAR_HAND_SLOTS_TEXTURE, i - 91 - 49, this.scaledHeight - 23 - raisedDistance, 49, 24);
            context.drawGuiTexture(HOTBAR_ALTERNATE_HAND_SLOTS_TEXTURE, i + 91, this.scaledHeight - 23 - raisedDistance, 49, 24);

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
//                    context.drawTexture(ICONS, o, n, 0, 94, 18, 18);
//                    context.drawTexture(ICONS, o, n + 18 - p, 18, 112 - p, 18, p);
                    context.drawGuiTexture(HOTBAR_ATTACK_INDICATOR_BACKGROUND_TEXTURE, o, n, 18, 18);
                    context.drawGuiTexture(HOTBAR_ATTACK_INDICATOR_PROGRESS_TEXTURE, 18, 18, 0, 18 - p, o, n + 18 - p, 18, p);
                }
            }

            RenderSystem.disableBlend();
        }
        ci.cancel();
    }

    @Inject(method = "renderExperienceBar", at = @At("HEAD"), cancellable = true)
    public void betteradventuremode$renderExperienceBar(DrawContext context, int x, CallbackInfo ci) {
        PlayerEntity playerEntity = this.getCameraPlayer();
        if (playerEntity != null && BetterAdventureModeClient.clientConfig.show_adventure_hud) {
            ci.cancel();
        }
    }

    @Inject(method = "renderStatusBars", at = @At("HEAD"), cancellable = true)
    private void betteradventuremode$renderStatusBars(DrawContext context, CallbackInfo ci) {
        PlayerEntity playerEntity = this.getCameraPlayer();
        if (playerEntity != null && BetterAdventureModeClient.clientConfig.show_adventure_hud) {
            int health = MathHelper.ceil(playerEntity.getHealth());
            int maxHealth = MathHelper.ceil(playerEntity.getMaxHealth());
            int stamina = MathHelper.ceil(((DuckLivingEntityMixin)playerEntity).betteradventuremode$getStamina());
            int maxStamina = MathHelper.ceil(((DuckLivingEntityMixin)playerEntity).betteradventuremode$getMaxStamina());
            int mana = MathHelper.ceil(((DuckLivingEntityMixin)playerEntity).betteradventuremode$getMana());
            int maxMana = MathHelper.ceil(((DuckLivingEntityMixin)playerEntity).betteradventuremode$getMaxMana());
            int bleedingBuildUp = MathHelper.ceil(((DuckLivingEntityMixin) playerEntity).betteradventuremode$getBleedingBuildUp());
            int maxBleedingBuildUp = MathHelper.ceil(((DuckLivingEntityMixin) playerEntity).betteradventuremode$getMaxBleedingBuildUp());
            int burnBuildUp = MathHelper.ceil(((DuckLivingEntityMixin) playerEntity).betteradventuremode$getBurnBuildUp());
            int maxBurnBuildUp = MathHelper.ceil(((DuckLivingEntityMixin) playerEntity).betteradventuremode$getMaxBurnBuildUp());
            int freezeBuildUp = MathHelper.ceil(((DuckLivingEntityMixin) playerEntity).betteradventuremode$getFreezeBuildUp());
            int maxFreezeBuildUp = MathHelper.ceil(((DuckLivingEntityMixin) playerEntity).betteradventuremode$getMaxFreezeBuildUp());
            int staggerBuildUp = MathHelper.ceil(((DuckLivingEntityMixin) playerEntity).betteradventuremode$getStaggerBuildUp());
            int maxStaggerBuildUp = MathHelper.ceil(((DuckLivingEntityMixin) playerEntity).betteradventuremode$getMaxStaggerBuildUp());
            int poisonBuildUp = MathHelper.ceil(((DuckLivingEntityMixin) playerEntity).betteradventuremode$getPoisonBuildUp());
            int maxPoisonBuildUp = MathHelper.ceil(((DuckLivingEntityMixin) playerEntity).betteradventuremode$getMaxPoisonBuildUp());
            int shockBuildUp = MathHelper.ceil(((DuckLivingEntityMixin) playerEntity).betteradventuremode$getShockBuildUp());
            int maxShockBuildUp = MathHelper.ceil(((DuckLivingEntityMixin) playerEntity).betteradventuremode$getMaxShockBuildUp());

            int attributeBarX = this.scaledWidth / 2 - 91;
            int attributeBarY = this.scaledHeight - 32 + 2;
            int attributeBarNumberX;
            int attributeBarNumberY;
            int normalizedHealthRatio = (int) (((double) health / Math.max(maxHealth, 1)) * 182);
            int normalizedStaminaRatio = (int) (((double) stamina / Math.max(maxStamina, 1)) * 182);
            int normalizedManaRatio = (int) (((double) mana / Math.max(maxMana, 1)) * 182);
            int normalizedBleedingBuildUpRatio = (int) (((double) bleedingBuildUp / Math.max(maxBleedingBuildUp, 1)) * 62);
            int normalizedBurnBuildUpRatio = (int) (((double) burnBuildUp / Math.max(maxBurnBuildUp, 1)) * 62);
            int normalizedFreezeBuildUpRatio = (int) (((double) freezeBuildUp / Math.max(maxFreezeBuildUp, 1)) * 62);
            int normalizedStaggerBuildUpRatio = (int) (((double) staggerBuildUp / Math.max(maxStaggerBuildUp, 1)) * 62);
            int normalizedPoisonBuildUpRatio = (int) (((double) poisonBuildUp / Math.max(maxPoisonBuildUp, 1)) * 62);
            int normalizedShockBuildUpRatio = (int) (((double) shockBuildUp / Math.max(maxShockBuildUp, 1)) * 62);

            this.client.getProfiler().push("health_bar");
            context.drawGuiTexture(HEALTH_BAR_BACKGROUND_TEXTURE, attributeBarX, attributeBarY, 182, 5);
            if (normalizedHealthRatio > 0) {
                context.drawGuiTexture(HEALTH_BAR_PROGRESS_TEXTURE, attributeBarX, attributeBarY, normalizedHealthRatio, 5);
            }
            if (BetterAdventureModeClient.clientConfig.show_resource_bar_numbers) {
                this.client.getProfiler().swap("health_bar_number");
                String string = String.valueOf(health);
                attributeBarNumberX = (this.scaledWidth - this.getTextRenderer().getWidth(string)) / 2;
                attributeBarNumberY = attributeBarY - 1;
                context.drawText(this.getTextRenderer(), string, attributeBarNumberX + 1, attributeBarNumberY, 0, false);
                context.drawText(this.getTextRenderer(), string, attributeBarNumberX - 1, attributeBarNumberY, 0, false);
                context.drawText(this.getTextRenderer(), string, attributeBarNumberX, attributeBarNumberY + 1, 0, false);
                context.drawText(this.getTextRenderer(), string, attributeBarNumberX, attributeBarNumberY - 1, 0, false);
                context.drawText(this.getTextRenderer(), string, attributeBarNumberX, attributeBarNumberY, BetterAdventureModeClient.clientConfig.health_bar_number_color, false);
            }

            this.client.getProfiler().swap("stamina_bar");
            context.drawGuiTexture(STAMINA_BAR_BACKGROUND_TEXTURE, attributeBarX, attributeBarY - 9, 182, 5);
            if (normalizedStaminaRatio > 0) {
                context.drawGuiTexture(STAMINA_BAR_PROGRESS_TEXTURE, attributeBarX, attributeBarY - 9, normalizedStaminaRatio, 5);
            }
            if (BetterAdventureModeClient.clientConfig.show_resource_bar_numbers) {
                this.client.getProfiler().swap("stamina_bar_number");
                String string = String.valueOf(stamina);
                attributeBarNumberX = (this.scaledWidth - this.getTextRenderer().getWidth(string)) / 2;
                attributeBarNumberY = attributeBarY - 10;
                context.drawText(this.getTextRenderer(), string, attributeBarNumberX + 1, attributeBarNumberY, 0, false);
                context.drawText(this.getTextRenderer(), string, attributeBarNumberX - 1, attributeBarNumberY, 0, false);
                context.drawText(this.getTextRenderer(), string, attributeBarNumberX, attributeBarNumberY + 1, 0, false);
                context.drawText(this.getTextRenderer(), string, attributeBarNumberX, attributeBarNumberY - 1, 0, false);
                context.drawText(this.getTextRenderer(), string, attributeBarNumberX, attributeBarNumberY, BetterAdventureModeClient.clientConfig.stamina_bar_number_color, false);
            }

            if (maxMana > 0) {
                this.client.getProfiler().swap("mana_bar");
                context.drawGuiTexture(MANA_BAR_BACKGROUND_TEXTURE, attributeBarX, attributeBarY - 18, 182, 5);
                if (normalizedManaRatio > 0) {
                    context.drawGuiTexture(MANA_BAR_PROGRESS_TEXTURE, attributeBarX, attributeBarY - 18, normalizedManaRatio, 5);
                }
                if (BetterAdventureModeClient.clientConfig.show_resource_bar_numbers) {
                    this.client.getProfiler().swap("mana_bar_number");
                    String string = String.valueOf(mana);
                    attributeBarNumberX = (this.scaledWidth - this.getTextRenderer().getWidth(string)) / 2;
                    attributeBarNumberY = attributeBarY - 19;
                    context.drawText(this.getTextRenderer(), string, attributeBarNumberX + 1, attributeBarNumberY, 0, false);
                    context.drawText(this.getTextRenderer(), string, attributeBarNumberX - 1, attributeBarNumberY, 0, false);
                    context.drawText(this.getTextRenderer(), string, attributeBarNumberX, attributeBarNumberY + 1, 0, false);
                    context.drawText(this.getTextRenderer(), string, attributeBarNumberX, attributeBarNumberY - 1, 0, false);
                    context.drawText(this.getTextRenderer(), string, attributeBarNumberX, attributeBarNumberY, BetterAdventureModeClient.clientConfig.mana_bar_number_color, true);
                }
            }

            attributeBarX = attributeBarX + 60;
            attributeBarY = this.scaledHeight / 2 - 7 + 25;
            if (staggerBuildUp > 0) {
                this.client.getProfiler().swap("stagger_bar");
                context.drawGuiTexture(STAGGER_BAR_BACKGROUND_TEXTURE, attributeBarX, attributeBarY, 62, 5);
                if (normalizedStaggerBuildUpRatio > 0) {
                    context.drawGuiTexture(STAGGER_BAR_PROGRESS_TEXTURE, attributeBarX, attributeBarY, normalizedStaggerBuildUpRatio, 5);
                }
                attributeBarY = attributeBarY + 6;
            }

            // bleeding build up
            if (bleedingBuildUp > 0) {
                this.client.getProfiler().swap("bleeding_build_up_bar");
                context.drawGuiTexture(BLEEDING_BUILD_UP_BAR_BACKGROUND_TEXTURE, attributeBarX, attributeBarY, 62, 5);
                if (normalizedBleedingBuildUpRatio > 0) {
                    context.drawGuiTexture(BLEEDING_BUILD_UP_BAR_PROGRESS_TEXTURE, attributeBarX, attributeBarY, normalizedBleedingBuildUpRatio, 5);
                }
                attributeBarY = attributeBarY + 6;
            }

            // burn build up
            if (burnBuildUp > 0) {
                this.client.getProfiler().swap("burn_build_up_bar");
                context.drawGuiTexture(BURNING_BUILD_UP_BAR_BACKGROUND_TEXTURE, attributeBarX, attributeBarY, 62, 5);
                if (normalizedBurnBuildUpRatio > 0) {
                    context.drawGuiTexture(BURNING_BUILD_UP_BAR_PROGRESS_TEXTURE, attributeBarX, attributeBarY, normalizedBurnBuildUpRatio, 5);
                }
                attributeBarY = attributeBarY + 6;
            }

            // freeze build up
            if (freezeBuildUp > 0) {
                this.client.getProfiler().swap("freeze_build_up_bar");
                context.drawGuiTexture(FREEZE_BUILD_UP_BAR_BACKGROUND_TEXTURE, attributeBarX, attributeBarY, 62, 5);
                if (normalizedFreezeBuildUpRatio > 0) {
                    context.drawGuiTexture(FREEZE_BUILD_UP_BAR_PROGRESS_TEXTURE, attributeBarX, attributeBarY, normalizedFreezeBuildUpRatio, 5);
                }
                attributeBarY = attributeBarY + 6;
            }

            // poison build up
            if (poisonBuildUp > 0) {
                this.client.getProfiler().swap("poison_build_up_bar");
                context.drawGuiTexture(POISON_BUILD_UP_BAR_BACKGROUND_TEXTURE, attributeBarX, attributeBarY, 62, 5);
                if (normalizedPoisonBuildUpRatio > 0) {
                    context.drawGuiTexture(POISON_BUILD_UP_BAR_PROGRESS_TEXTURE, attributeBarX, attributeBarY, normalizedPoisonBuildUpRatio, 5);
                }
                attributeBarY = attributeBarY + 6;
            }

            // shock build up
            if (shockBuildUp > 0) {
                this.client.getProfiler().swap("shock_build_up_bar");
                context.drawGuiTexture(SHOCK_BUILD_UP_BAR_BACKGROUND_TEXTURE, attributeBarX, attributeBarY, 62, 5);
                if (normalizedShockBuildUpRatio > 0) {
                    context.drawGuiTexture(SHOCK_BUILD_UP_BAR_PROGRESS_TEXTURE, attributeBarX, attributeBarY, normalizedShockBuildUpRatio, 5);
                }
            }
            this.client.getProfiler().pop();
            ci.cancel();
        }
    }
}
