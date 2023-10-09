package com.github.theredbrain.bamcore.client.gui.screen.ingame;
//
//import com.github.theredbrain.bamcore.BetterAdventureModeCore;
//import com.github.theredbrain.bamcore.entity.player.DuckPlayerEntityMixin;
//import com.github.theredbrain.bamcore.screen.AdventureInventoryScreenHandler;
//import com.google.common.collect.Ordering;
//import com.mojang.blaze3d.systems.RenderSystem;
//import dev.emi.trinkets.Point;
//import dev.emi.trinkets.TrinketPlayerScreenHandler;
//import dev.emi.trinkets.TrinketScreen;
//import dev.emi.trinkets.TrinketScreenManager;
//import dev.emi.trinkets.api.SlotGroup;
//import net.fabricmc.api.EnvType;
//import net.fabricmc.api.Environment;
//import net.minecraft.client.MinecraftClient;
//import net.minecraft.client.gui.DrawContext;
//import net.minecraft.client.gui.screen.ingame.CreativeInventoryScreen;
//import net.minecraft.client.gui.screen.ingame.HandledScreen;
//import net.minecraft.client.render.DiffuseLighting;
//import net.minecraft.client.render.RenderLayer;
//import net.minecraft.client.render.VertexConsumerProvider;
//import net.minecraft.client.render.entity.EntityRenderDispatcher;
//import net.minecraft.client.texture.Sprite;
//import net.minecraft.client.texture.StatusEffectSpriteManager;
//import net.minecraft.client.util.math.MatrixStack;
//import net.minecraft.client.util.math.Rect2i;
//import net.minecraft.entity.LivingEntity;
//import net.minecraft.entity.effect.StatusEffect;
//import net.minecraft.entity.effect.StatusEffectInstance;
//import net.minecraft.entity.effect.StatusEffectUtil;
//import net.minecraft.entity.player.PlayerEntity;
//import net.minecraft.item.ItemStack;
//import net.minecraft.screen.ScreenTexts;
//import net.minecraft.screen.slot.Slot;
//import net.minecraft.text.MutableText;
//import net.minecraft.text.Text;
//import net.minecraft.util.Identifier;
//import org.joml.Quaternionf;
//
//import java.util.Collection;
//import java.util.Iterator;
//import java.util.List;
//import java.util.Optional;
//
//@Environment(EnvType.CLIENT)
//public class AdventureInventoryScreenBackup extends HandledScreen<AdventureInventoryScreenHandler> implements TrinketScreen {
//    public static final Identifier ADVENTURE_INVENTORY_BACKGROUND_TEXTURE = BetterAdventureModeCore.identifier("textures/gui/container/adventure_inventory_old.png");
//    private float mouseX;
//    private float mouseY;
//
//    public AdventureInventoryScreenBackup(PlayerEntity player) { //AdventureInventoryScreenHandler handler, PlayerInventory inventory, Text title) {
//        super((AdventureInventoryScreenHandler) ((DuckPlayerEntityMixin)player).bamcore$getAdventureInventoryScreenHandler(), player.getInventory(), Text.translatable("bam.gui.adventure_inventory"));
//    }
//
////    @Override
////    public void handledScreenTick() {
////        if (this.client.interactionManager.hasCreativeInventory()) {
////            this.client.setScreen(new CreativeInventoryScreen(this.client.player, this.client.player.networkHandler.getEnabledFeatures(), this.client.options.getOperatorItemsTab().getValue()));
////            return;
////        }
////        this.recipeBook.update();
////    }
//
//    public void handledScreenTick() {
//        if (this.client.interactionManager.hasCreativeInventory()) {
//            this.client.setScreen(new CreativeInventoryScreen(this.client.player, this.client.player.networkHandler.getEnabledFeatures(), (Boolean)this.client.options.getOperatorItemsTab().getValue()));
//        }
//        TrinketScreenManager.tick();
//    }
//
//    @Override
//    protected void init() {
//        TrinketScreenManager.init(this);
//        if (this.client.interactionManager.hasCreativeInventory()) {
//            // TODO call custom CreativeInventoryScreen
//            this.client.setScreen(new CreativeInventoryScreen(this.client.player, this.client.player.networkHandler.getEnabledFeatures(), this.client.options.getOperatorItemsTab().getValue()));
//            return;
//        }
//        this.backgroundWidth = 176;
//        this.backgroundHeight = 202;
//        super.init();
////        this.x = (this.width - this.backgroundWidth) / 2;
////        this.y = (this.height - this.backgroundHeight) / 2;
//        this.setInitialFocus(this);
//    }
//
////    @Override
////    protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
////
////    }
//
//    @Override
//    protected void drawForeground(DrawContext context, int mouseX, int mouseY) {
////        this.textRenderer.draw(context, this.title, (float)this.titleX, (float)this.titleY, 0x404040);
//        TrinketScreenManager.drawActiveGroup(context);
//    }
//
//    @Override
//    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
//        TrinketScreenManager.update(mouseX, mouseY);
//        this.renderBackground(context);
//        this.drawBackground(context, delta, mouseX, mouseY);
//        this.drawStatusEffects(context, mouseX, mouseY);
//        super.render(context, mouseX, mouseY, delta);
//        this.drawMouseoverTooltip(context, mouseX, mouseY);
//        this.mouseX = mouseX;
//        this.mouseY = mouseY;
//    }
//
//    @Override
//    protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
////        RenderSystem.setShader(GameRenderer::getPositionTexProgram);
////        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
////        RenderSystem.setShaderTexture(0, ADVENTURE_INVENTORY_BACKGROUND_TEXTURE);
//        int i = this.x;
//        int j = this.y;
//        context.drawTexture(ADVENTURE_INVENTORY_BACKGROUND_TEXTURE, i, j, 0, 0, this.backgroundWidth, this.backgroundHeight);
//        drawEntity(i + 51, j + 93, 30, (float)(i + 51) - this.mouseX, (float)(j + 93 - 50) - this.mouseY, this.client.player);
////        TrinketScreenManager.drawExtraGroups(context);
//    }
//
//    public static void drawEntity(int x, int y, int size, float mouseX, float mouseY, LivingEntity entity) {
//        float f = (float)Math.atan(mouseX / 40.0f);
//        float g = (float)Math.atan(mouseY / 40.0f);
//        MatrixStack matrixStack = RenderSystem.getModelViewStack();
//        matrixStack.push();
//        matrixStack.translate(x, y, 1050.0f);
//        matrixStack.scale(1.0f, 1.0f, -1.0f);
//        RenderSystem.applyModelViewMatrix();
//        MatrixStack matrixStack2 = new MatrixStack();
//        matrixStack2.translate(0.0f, 0.0f, 1000.0f);
//        matrixStack2.scale(size, size, size);
//        Quaternionf quaternionf = new Quaternionf().rotateZ((float)Math.PI);
//        Quaternionf quaternionf2 = new Quaternionf().rotateX(g * 20.0f * ((float)Math.PI / 180));
//        quaternionf.mul(quaternionf2);
//        matrixStack2.multiply(quaternionf);
//        float h = entity.bodyYaw;
//        float i = entity.getYaw();
//        float j = entity.getPitch();
//        float k = entity.prevHeadYaw;
//        float l = entity.headYaw;
//        entity.bodyYaw = 180.0f + f * 20.0f;
//        entity.setYaw(180.0f + f * 40.0f);
//        entity.setPitch(-g * 20.0f);
//        entity.headYaw = entity.getYaw();
//        entity.prevHeadYaw = entity.getYaw();
//        DiffuseLighting.method_34742();
//        EntityRenderDispatcher entityRenderDispatcher = MinecraftClient.getInstance().getEntityRenderDispatcher();
//        quaternionf2.conjugate();
//        entityRenderDispatcher.setRotation(quaternionf2);
//        entityRenderDispatcher.setRenderShadows(false);
//        VertexConsumerProvider.Immediate immediate = MinecraftClient.getInstance().getBufferBuilders().getEntityVertexConsumers();
//        RenderSystem.runAsFancy(() -> entityRenderDispatcher.render(entity, 0.0, 0.0, 0.0, 0.0f, 1.0f, matrixStack2, immediate, 0xF000F0));
//        immediate.draw();
//        entityRenderDispatcher.setRenderShadows(true);
//        entity.bodyYaw = h;
//        entity.setYaw(i);
//        entity.setPitch(j);
//        entity.prevHeadYaw = k;
//        entity.headYaw = l;
//        matrixStack.pop();
//        RenderSystem.applyModelViewMatrix();
//        DiffuseLighting.enableGuiDepthLighting();
//    }
//
//    @Override
//    public void drawSlot(DrawContext context, Slot slot) {
//        super.drawSlot(context, slot);
//        ItemStack stack = slot.getStack();
//        if (!stack.isEmpty() && stack.isDamageable() && stack.getDamage() >= stack.getMaxDamage() - 1) {
//            drawDisabledItemSlotHighlight(context, slot.x, slot.y, 0);
//        }
//    }
//
//    public static void drawDisabledItemSlotHighlight(DrawContext context, int x, int y, int z) {
//        RenderSystem.disableDepthTest();
//        RenderSystem.colorMask(true, true, true, false);
//        context.fillGradient(RenderLayer.getGuiOverlay(), x, y, x + 16, y + 16, -1602211792, -1602211792, z);
//        RenderSystem.colorMask(true, true, true, true);
//        RenderSystem.enableDepthTest();
//    }
//
////    @Override
////    protected boolean isPointWithinBounds(int x, int y, int width, int height, double pointX, double pointY) {
////        return super.isPointWithinBounds(x, y, width, height, pointX, pointY);
////    }
////
////    @Override
////    public boolean mouseClicked(double mouseX, double mouseY, int button) {
////        return super.mouseClicked(mouseX, mouseY, button);
////    }
////
////    @Override
////    public boolean mouseReleased(double mouseX, double mouseY, int button) {
////        return super.mouseReleased(mouseX, mouseY, button);
////    }
////
//    @Override
//    protected boolean isClickOutsideBounds(double mouseX, double mouseY, int left, int top, int button) {
//        return mouseX < (double)left || mouseY < (double)top || mouseX >= (double)(left + this.backgroundWidth) || mouseY >= (double)(top + this.backgroundHeight) && !TrinketScreenManager.isClickInsideTrinketBounds(mouseX, mouseY);
////        return this.recipeBook.isClickOutsideBounds(mouseX, mouseY, this.x, this.y, this.backgroundWidth, this.backgroundHeight, button) && bl;
//    }
////
////    @Override
////    protected void onMouseClick(Slot slot, int slotId, int button, SlotActionType actionType) {
////        super.onMouseClick(slot, slotId, button, actionType);
////        this.recipeBook.slotClicked(slot);
////    }
//
//    public boolean hideStatusEffectHud() {
//        int i = this.x + this.backgroundWidth + 2;
//        int j = this.width - i;
//        return j >= 32;
//    }
//
//    private void drawStatusEffects(DrawContext context, int mouseX, int mouseY) {
//        int i = this.x + this.backgroundWidth + 2;
//        int j = this.width - i;
//        Collection<StatusEffectInstance> collection = this.client.player.getStatusEffects();
//        if (collection.isEmpty() || j < 32) {
//            return;
//        }
//        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
//        boolean bl = j >= 120;
//        int k = 33;
//        if (collection.size() > 5) {
//            k = 132 / (collection.size() - 1);
//        }
//        List<StatusEffectInstance> iterable = Ordering.natural().sortedCopy(collection);
//        this.drawStatusEffectBackgrounds(context, i, k, iterable, bl);
//        this.drawStatusEffectSprites(context, i, k, iterable, bl);
//        if (bl) {
//            this.drawStatusEffectDescriptions(context, i, k, iterable);
//        } else if (mouseX >= i && mouseX <= i + 33) {
//            int l = this.y;
//            StatusEffectInstance statusEffectInstance = null;
//            for (StatusEffectInstance statusEffectInstance2 : iterable) {
//                if (mouseY >= l && mouseY <= l + k) {
//                    statusEffectInstance = statusEffectInstance2;
//                }
//                l += k;
//            }
//            if (statusEffectInstance != null) {
//                List<Text> list = List.of(this.getStatusEffectDescription(statusEffectInstance), StatusEffectUtil.getDurationText(statusEffectInstance, 1.0f));
//                context.drawTooltip(this.textRenderer, list, Optional.empty(), mouseX, mouseY);
//            }
//        }
//    }
//
//    private void drawStatusEffectBackgrounds(DrawContext context, int x, int height, Iterable<StatusEffectInstance> statusEffects, boolean wide) {
//        int i = this.y;
//
//        for(Iterator var7 = statusEffects.iterator(); var7.hasNext(); i += height) {
//            StatusEffectInstance statusEffectInstance = (StatusEffectInstance)var7.next();
//            if (wide) {
//                context.drawTexture(BACKGROUND_TEXTURE, x, i, 0, 166, 120, 32);
//            } else {
//                context.drawTexture(BACKGROUND_TEXTURE, x, i, 0, 198, 32, 32);
//            }
//        }
//
//    }
//
//    private void drawStatusEffectSprites(DrawContext context, int x, int height, Iterable<StatusEffectInstance> statusEffects, boolean wide) {
//        StatusEffectSpriteManager statusEffectSpriteManager = this.client.getStatusEffectSpriteManager();
//        int i = this.y;
//
//        for(Iterator var8 = statusEffects.iterator(); var8.hasNext(); i += height) {
//            StatusEffectInstance statusEffectInstance = (StatusEffectInstance)var8.next();
//            StatusEffect statusEffect = statusEffectInstance.getEffectType();
//            Sprite sprite = statusEffectSpriteManager.getSprite(statusEffect);
//            context.drawSprite(x + (wide ? 6 : 7), i + 7, 0, 18, 18, sprite);
//        }
//
//    }
//
//    private void drawStatusEffectDescriptions(DrawContext context, int x, int height, Iterable<StatusEffectInstance> statusEffects) {
//        int i = this.y;
//
//        for(Iterator var6 = statusEffects.iterator(); var6.hasNext(); i += height) {
//            StatusEffectInstance statusEffectInstance = (StatusEffectInstance)var6.next();
//            Text text = this.getStatusEffectDescription(statusEffectInstance);
//            context.drawTextWithShadow(this.textRenderer, text, x + 10 + 18, i + 6, 16777215);
//            Text text2 = StatusEffectUtil.getDurationText(statusEffectInstance, 1.0F);
//            context.drawTextWithShadow(this.textRenderer, text2, x + 10 + 18, i + 6 + 10, 8355711);
//        }
//
//    }
//
//    private Text getStatusEffectDescription(StatusEffectInstance statusEffect) {
//        MutableText mutableText = statusEffect.getEffectType().getName().copy();
//        if (statusEffect.getAmplifier() >= 1 && statusEffect.getAmplifier() <= 9) {
//            MutableText var10000 = mutableText.append(ScreenTexts.SPACE);
//            int var10001 = statusEffect.getAmplifier();
//            var10000.append(Text.translatable("enchantment.level." + (var10001 + 1)));
//        }
//
//        return mutableText;
//    }
//
//    @Override
//    public TrinketPlayerScreenHandler trinkets$getHandler() {
//        return (TrinketPlayerScreenHandler) this.handler;
//    }
//
//    @Override
//    public Rect2i trinkets$getGroupRect(SlotGroup group) {
//        Point pos = ((TrinketPlayerScreenHandler) handler).trinkets$getGroupPos(group);
//        if (pos != null) {
//            return new Rect2i(pos.x() - 1, pos.y() - 1, 17, 17);
//        }
//        return new Rect2i(0, 0, 0, 0);
//    }
//
//    @Override
//    public Slot trinkets$getFocusedSlot() {
//        return this.focusedSlot;
//    }
//
//    @Override
//    public int trinkets$getX() {
//        return this.x;
//    }
//
//    @Override
//    public int trinkets$getY() {
//        return this.y;
//    }
//}
