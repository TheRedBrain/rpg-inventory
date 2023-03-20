package com.github.theredbrain.rpgmod.client.gui.screen.ingame;

import com.github.theredbrain.rpgmod.RPGMod;
import com.github.theredbrain.rpgmod.entity.player.DuckPlayerEntityMixin;
import com.github.theredbrain.rpgmod.screen.AdventureInventoryScreenHandler;
import com.google.common.collect.Ordering;
import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.CreativeInventoryScreen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.render.DiffuseLighting;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.texture.StatusEffectSpriteManager;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffectUtil;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.minecraft.util.math.MathHelper;
import org.joml.Quaternionf;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Environment(EnvType.CLIENT)
public class AdventureInventoryScreen extends HandledScreen<AdventureInventoryScreenHandler> {
    public static final Identifier ADVENTURE_INVENTORY_BACKGROUND_TEXTURE = new Identifier(RPGMod.MOD_ID, "textures/gui/container/adventure_inventory.png");
    private float mouseX;
    private float mouseY;

    public AdventureInventoryScreen(PlayerEntity player) { //AdventureInventoryScreenHandler handler, PlayerInventory inventory, Text title) {
        super(((DuckPlayerEntityMixin)player).getAdventureInventoryScreenHandler(), player.getInventory(), Text.translatable("bam.gui.adventure_inventory"));
    }

//    @Override
//    public void handledScreenTick() {
//        if (this.client.interactionManager.hasCreativeInventory()) {
//            this.client.setScreen(new CreativeInventoryScreen(this.client.player, this.client.player.networkHandler.getEnabledFeatures(), this.client.options.getOperatorItemsTab().getValue()));
//            return;
//        }
//        this.recipeBook.update();
//    }

    @Override
    protected void init() {
        if (this.client.interactionManager.hasCreativeInventory()) {
            this.client.setScreen(new CreativeInventoryScreen(this.client.player, this.client.player.networkHandler.getEnabledFeatures(), this.client.options.getOperatorItemsTab().getValue()));
            return;
        }
        super.init();
        this.backgroundWidth = 176;
        this.backgroundHeight = 184;
    }

    @Override
    protected void drawForeground(MatrixStack matrices, int mouseX, int mouseY) {
        this.textRenderer.draw(matrices, this.title, (float)this.titleX, (float)this.titleY, 0x404040);
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);
        this.drawBackground(matrices, delta, mouseX, mouseY);
        this.drawStatusEffects(matrices, mouseX, mouseY);
        super.render(matrices, mouseX, mouseY, delta);
        this.drawMouseoverTooltip(matrices, mouseX, mouseY);
        this.mouseX = mouseX;
        this.mouseY = mouseY;
    }

    @Override
    protected void drawBackground(MatrixStack matrices, float delta, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexProgram);
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
        RenderSystem.setShaderTexture(0, ADVENTURE_INVENTORY_BACKGROUND_TEXTURE);
        int i = this.x;
        int j = this.y;
        this.drawTexture(matrices, i, j, 0, 0, this.backgroundWidth, this.backgroundHeight);
        drawEntity(i + 51, j + 93, 30, (float)(i + 51) - this.mouseX, (float)(j + 93 - 50) - this.mouseY, this.client.player);
    }

    public static void drawEntity(int x, int y, int size, float mouseX, float mouseY, LivingEntity entity) {
        float f = (float)Math.atan(mouseX / 40.0f);
        float g = (float)Math.atan(mouseY / 40.0f);
        MatrixStack matrixStack = RenderSystem.getModelViewStack();
        matrixStack.push();
        matrixStack.translate(x, y, 1050.0f);
        matrixStack.scale(1.0f, 1.0f, -1.0f);
        RenderSystem.applyModelViewMatrix();
        MatrixStack matrixStack2 = new MatrixStack();
        matrixStack2.translate(0.0f, 0.0f, 1000.0f);
        matrixStack2.scale(size, size, size);
        Quaternionf quaternionf = new Quaternionf().rotateZ((float)Math.PI);
        Quaternionf quaternionf2 = new Quaternionf().rotateX(g * 20.0f * ((float)Math.PI / 180));
        quaternionf.mul(quaternionf2);
        matrixStack2.multiply(quaternionf);
        float h = entity.bodyYaw;
        float i = entity.getYaw();
        float j = entity.getPitch();
        float k = entity.prevHeadYaw;
        float l = entity.headYaw;
        entity.bodyYaw = 180.0f + f * 20.0f;
        entity.setYaw(180.0f + f * 40.0f);
        entity.setPitch(-g * 20.0f);
        entity.headYaw = entity.getYaw();
        entity.prevHeadYaw = entity.getYaw();
        DiffuseLighting.method_34742();
        EntityRenderDispatcher entityRenderDispatcher = MinecraftClient.getInstance().getEntityRenderDispatcher();
        quaternionf2.conjugate();
        entityRenderDispatcher.setRotation(quaternionf2);
        entityRenderDispatcher.setRenderShadows(false);
        VertexConsumerProvider.Immediate immediate = MinecraftClient.getInstance().getBufferBuilders().getEntityVertexConsumers();
        RenderSystem.runAsFancy(() -> entityRenderDispatcher.render(entity, 0.0, 0.0, 0.0, 0.0f, 1.0f, matrixStack2, immediate, 0xF000F0));
        immediate.draw();
        entityRenderDispatcher.setRenderShadows(true);
        entity.bodyYaw = h;
        entity.setYaw(i);
        entity.setPitch(j);
        entity.prevHeadYaw = k;
        entity.headYaw = l;
        matrixStack.pop();
        RenderSystem.applyModelViewMatrix();
        DiffuseLighting.enableGuiDepthLighting();
    }

    @Override
    public final void drawSlot(MatrixStack matrices, Slot slot) {
        super.drawSlot(matrices, slot);
        ItemStack stack = slot.getStack();
        if (!stack.isEmpty() && stack.isDamageable() && stack.getDamage() >= stack.getMaxDamage() - 1) {
            drawDisabledItemSlotHighlight(matrices, slot.x, slot.y, this.getZOffset());
        }
    }

    public static void drawDisabledItemSlotHighlight(MatrixStack matrices, int x, int y, int z) {
        RenderSystem.disableDepthTest();
        RenderSystem.colorMask(true, true, true, false);
        HandledScreen.fillGradient(matrices, x, y, x + 16, y + 16, -1602211792, -1602211792, z);
        RenderSystem.colorMask(true, true, true, true);
        RenderSystem.enableDepthTest();
    }

//    @Override
//    protected boolean isPointWithinBounds(int x, int y, int width, int height, double pointX, double pointY) {
//        return super.isPointWithinBounds(x, y, width, height, pointX, pointY);
//    }
//
//    @Override
//    public boolean mouseClicked(double mouseX, double mouseY, int button) {
//        return super.mouseClicked(mouseX, mouseY, button);
//    }
//
//    @Override
//    public boolean mouseReleased(double mouseX, double mouseY, int button) {
//        return super.mouseReleased(mouseX, mouseY, button);
//    }
//
//    @Override
//    protected boolean isClickOutsideBounds(double mouseX, double mouseY, int left, int top, int button) {
//        boolean bl = mouseX < (double)left || mouseY < (double)top || mouseX >= (double)(left + this.backgroundWidth) || mouseY >= (double)(top + this.backgroundHeight);
//        return this.recipeBook.isClickOutsideBounds(mouseX, mouseY, this.x, this.y, this.backgroundWidth, this.backgroundHeight, button) && bl;
//    }
//
//    @Override
//    protected void onMouseClick(Slot slot, int slotId, int button, SlotActionType actionType) {
//        super.onMouseClick(slot, slotId, button, actionType);
//        this.recipeBook.slotClicked(slot);
//    }

    public boolean hideStatusEffectHud() {
        int i = this.x + this.backgroundWidth + 2;
        int j = this.width - i;
        return j >= 32;
    }

    private void drawStatusEffects(MatrixStack matrices, int mouseX, int mouseY) {
        int i = this.x + this.backgroundWidth + 2;
        int j = this.width - i;
        Collection<StatusEffectInstance> collection = this.client.player.getStatusEffects();
        if (collection.isEmpty() || j < 32) {
            return;
        }
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
        boolean bl = j >= 120;
        int k = 33;
        if (collection.size() > 5) {
            k = 132 / (collection.size() - 1);
        }
        List<StatusEffectInstance> iterable = Ordering.natural().sortedCopy(collection);
        this.drawStatusEffectBackgrounds(matrices, i, k, iterable, bl);
        this.drawStatusEffectSprites(matrices, i, k, iterable, bl);
        if (bl) {
            this.drawStatusEffectDescriptions(matrices, i, k, iterable);
        } else if (mouseX >= i && mouseX <= i + 33) {
            int l = this.y;
            StatusEffectInstance statusEffectInstance = null;
            for (StatusEffectInstance statusEffectInstance2 : iterable) {
                if (mouseY >= l && mouseY <= l + k) {
                    statusEffectInstance = statusEffectInstance2;
                }
                l += k;
            }
            if (statusEffectInstance != null) {
                List<Text> list = List.of(this.getStatusEffectDescription(statusEffectInstance), Text.literal(StatusEffectUtil.durationToString(statusEffectInstance, 1.0f)));
                this.renderTooltip(matrices, list, Optional.empty(), mouseX, mouseY);
            }
        }
    }

    private void drawStatusEffectBackgrounds(MatrixStack matrices, int x, int height, Iterable<StatusEffectInstance> statusEffects, boolean wide) {
        RenderSystem.setShaderTexture(0, BACKGROUND_TEXTURE);
        int i = this.y;
        for (StatusEffectInstance statusEffectInstance : statusEffects) {
            RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
            if (wide) {
                this.drawTexture(matrices, x, i, 0, 166, 120, 32);
            } else {
                this.drawTexture(matrices, x, i, 0, 198, 32, 32);
            }
            i += height;
        }
    }

    private void drawStatusEffectSprites(MatrixStack matrices, int x, int height, Iterable<StatusEffectInstance> statusEffects, boolean wide) {
        StatusEffectSpriteManager statusEffectSpriteManager = this.client.getStatusEffectSpriteManager();
        int i = this.y;
        for (StatusEffectInstance statusEffectInstance : statusEffects) {
            StatusEffect statusEffect = statusEffectInstance.getEffectType();
            Sprite sprite = statusEffectSpriteManager.getSprite(statusEffect);
            RenderSystem.setShaderTexture(0, sprite.getAtlasId());
            drawSprite(matrices, x + (wide ? 6 : 7), i + 7, this.getZOffset(), 18, 18, sprite);
            i += height;
        }
    }

    private void drawStatusEffectDescriptions(MatrixStack matrices, int x, int height, Iterable<StatusEffectInstance> statusEffects) {
        int i = this.y;
        for (StatusEffectInstance statusEffectInstance : statusEffects) {
            Text text = this.getStatusEffectDescription(statusEffectInstance);
            this.textRenderer.drawWithShadow(matrices, text, (float)(x + 10 + 18), (float)(i + 6), 0xFFFFFF);
            String string = StatusEffectUtil.durationToString(statusEffectInstance, 1.0f);
            this.textRenderer.drawWithShadow(matrices, string, (float)(x + 10 + 18), (float)(i + 6 + 10), 0x7F7F7F);
            i += height;
        }
    }

    private Text getStatusEffectDescription(StatusEffectInstance statusEffect) {
        MutableText mutableText = statusEffect.getEffectType().getName().copy();
        if (statusEffect.getAmplifier() >= 1 && statusEffect.getAmplifier() <= 9) {
            mutableText.append(" ").append(Text.translatable("enchantment.level." + (statusEffect.getAmplifier() + 1)));
        }
        return mutableText;
    }
}
