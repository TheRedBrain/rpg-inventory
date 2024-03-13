package com.github.theredbrain.betteradventuremode.client.gui.screen.ingame;

import com.github.theredbrain.betteradventuremode.BetterAdventureMode;
import com.github.theredbrain.betteradventuremode.BetterAdventureModeClient;
import com.github.theredbrain.betteradventuremode.effect.FoodStatusEffect;
import com.github.theredbrain.betteradventuremode.registry.EntityAttributesRegistry;
import com.github.theredbrain.betteradventuremode.screen.DuckSlotMixin;
import com.google.common.collect.Ordering;
import com.mojang.blaze3d.systems.RenderSystem;
import dev.emi.trinkets.Point;
import dev.emi.trinkets.TrinketPlayerScreenHandler;
import dev.emi.trinkets.TrinketScreen;
import dev.emi.trinkets.TrinketScreenManager;
import dev.emi.trinkets.api.SlotGroup;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.CreativeInventoryScreen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.texture.StatusEffectSpriteManager;
import net.minecraft.client.util.math.Rect2i;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffectUtil;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Environment(EnvType.CLIENT)
public class AdventureInventoryScreen extends HandledScreen<PlayerScreenHandler> implements TrinketScreen {
    public static final Identifier ADVENTURE_INVENTORY_SIDES_BACKGROUND_TEXTURE = BetterAdventureMode.identifier("textures/gui/container/adventure_inventory/adventure_inventory_sides_background.png");
    private static final Identifier INVENTORY_SLOT_TEXTURE = BetterAdventureMode.identifier("textures/gui/container/inventory_slot.png");
    private static final Identifier EFFECT_BACKGROUND_SMALL_TEXTURE = new Identifier("container/inventory/effect_background_small");
    private static final Identifier EFFECT_SCROLLER_BACKGROUND_TEXTURE = BetterAdventureMode.identifier("container/adventure_inventory/scroller_background");
    private static final Identifier EFFECT_SCROLLER_TEXTURE = BetterAdventureMode.identifier("container/scroller");
    private static final Text CRAFTING_LABEL_TEXT = Text.translatable("gui.adventure_inventory_screen.crafting_label");
    private static final Text SPELLS_LABEL_TEXT = Text.translatable("gui.adventure_inventory_screen.spells_label");
    private float mouseX;
    private float mouseY;
    private boolean showAttributeScreen = false;
    private int oldEffectsListSize = 0;
    private List<StatusEffectInstance> foodEffectsList = new ArrayList<>(Collections.emptyList());
    private List<StatusEffectInstance> negativeEffectsList = new ArrayList<>(Collections.emptyList());
    private List<StatusEffectInstance> positiveEffectsList = new ArrayList<>(Collections.emptyList());
    private List<StatusEffectInstance> neutralEffectsList = new ArrayList<>(Collections.emptyList());
    private int foodScrollPosition = 0;
    private int negativeScrollPosition = 0;
    private int positiveScrollPosition = 0;
    private int neutralScrollPosition = 0;
    private int foodEffectsRowAmount = 1;
    private int negativeEffectsRowAmount = 1;
    private int positiveEffectsRowAmount = 1;
    private int neutralEffectsRowAmount = 1;
    private float foodScrollAmount = 0.0f;
    private float negativeScrollAmount = 0.0f;
    private float positiveScrollAmount = 0.0f;
    private float neutralScrollAmount = 0.0f;
    private boolean foodMouseClicked = false;
    private boolean negativeMouseClicked = false;
    private boolean positiveMouseClicked = false;
    private boolean neutralMouseClicked = false;

    public AdventureInventoryScreen(PlayerEntity player) {
        super(player.playerScreenHandler, player.getInventory(), Text.translatable("gui.adventure_inventory_screen.equipment_label"));
        this.backgroundWidth = 176;
        this.backgroundHeight = 220;
        this.titleX = 8;
        this.titleY = 6;
        this.playerInventoryTitleX = 8;
        this.playerInventoryTitleY = this.backgroundHeight - 93;
    }

    public void handledScreenTick() {
//        PlayerEntity player = this.handler.player();
//        this.updateAttributeScreen(player);
//        this.updateEffectsScreen(player);
        TrinketScreenManager.tick();
    }

    private void updateEffectsLists(PlayerEntity player) {
        List<StatusEffectInstance> effectsList = Ordering.natural().sortedCopy(player.getStatusEffects());
        List<StatusEffectInstance> visibleEffectsList = new ArrayList<>(Collections.emptyList());
        for (StatusEffectInstance statusEffectInstance : effectsList) {
            if (statusEffectInstance.shouldShowIcon()) {
                visibleEffectsList.add(statusEffectInstance);
            }
        }
        int visibleEffectsListSize = visibleEffectsList.size();
        if (visibleEffectsListSize == 0) {
            this.oldEffectsListSize = 0;
            this.foodEffectsList.clear();
            this.negativeEffectsList.clear();
            this.positiveEffectsList.clear();
            this.neutralEffectsList.clear();
            return;
        }
        boolean bl = false;

        // determine if the UI should be updated
        if (visibleEffectsListSize != this.oldEffectsListSize) {
            bl = true;
            this.oldEffectsListSize = visibleEffectsListSize;
        }

        if (bl) {
            this.foodEffectsList.clear();
            this.negativeEffectsList.clear();
            this.positiveEffectsList.clear();
            this.neutralEffectsList.clear();

            this.foodScrollPosition = 0;
            this.negativeScrollPosition = 0;
            this.positiveScrollPosition = 0;
            this.neutralScrollPosition = 0;

            this.foodScrollAmount = 0.0f;
            this.negativeScrollAmount = 0.0f;
            this.positiveScrollAmount = 0.0f;
            this.neutralScrollAmount = 0.0f;

            for (StatusEffectInstance statusEffectInstance : visibleEffectsList) {
                if (statusEffectInstance.getEffectType() instanceof FoodStatusEffect) {
                    this.foodEffectsList.add(statusEffectInstance);
                } else if (statusEffectInstance.getEffectType().getCategory() == StatusEffectCategory.HARMFUL) {
                    this.negativeEffectsList.add(statusEffectInstance);
                } else if (statusEffectInstance.getEffectType().getCategory() == StatusEffectCategory.BENEFICIAL) {
                    this.positiveEffectsList.add(statusEffectInstance);
                } else if (statusEffectInstance.getEffectType().getCategory() == StatusEffectCategory.NEUTRAL) {
                    this.neutralEffectsList.add(statusEffectInstance);
                }
            }
            this.foodEffectsRowAmount = Math.max(1, this.calculateStatusEffectRowAmount(this.foodEffectsList.size()));
            this.negativeEffectsRowAmount = Math.max(1, this.calculateStatusEffectRowAmount(this.negativeEffectsList.size()));
            this.positiveEffectsRowAmount = Math.max(1, this.calculateStatusEffectRowAmount(this.positiveEffectsList.size()));
            this.neutralEffectsRowAmount = Math.max(1, this.calculateStatusEffectRowAmount(this.neutralEffectsList.size()));
        }
        visibleEffectsList.clear();
    }

    private int calculateStatusEffectRowAmount(int statusEffectListSize) {
        return statusEffectListSize / 3 + (statusEffectListSize % 3 > 0 ? 1 : 0);
    }

    @Override
    protected void init() {
        TrinketScreenManager.init(this);
        if (this.client.interactionManager.hasCreativeInventory()) {
            this.client.setScreen(new CreativeInventoryScreen(this.client.player, this.client.player.networkHandler.getEnabledFeatures(), this.client.options.getOperatorItemsTab().getValue()));
            return;
        }
        super.init();

//        // disable vanilla crafting slots
//        ((DuckSlotMixin)this.handler.slots.get(0)).betteradventuremode$setDisabledOverride(true);
//        ((DuckSlotMixin)this.handler.slots.get(1)).betteradventuremode$setDisabledOverride(true);
//        ((DuckSlotMixin)this.handler.slots.get(2)).betteradventuremode$setDisabledOverride(true);
//        ((DuckSlotMixin)this.handler.slots.get(3)).betteradventuremode$setDisabledOverride(true);
//        ((DuckSlotMixin)this.handler.slots.get(4)).betteradventuremode$setDisabledOverride(true);
//
//        // disable vanilla armor slots
//        ((DuckSlotMixin)this.handler.slots.get(5)).betteradventuremode$setDisabledOverride(true);
//        ((DuckSlotMixin)this.handler.slots.get(6)).betteradventuremode$setDisabledOverride(true);
//        ((DuckSlotMixin)this.handler.slots.get(7)).betteradventuremode$setDisabledOverride(true);
//        ((DuckSlotMixin)this.handler.slots.get(8)).betteradventuremode$setDisabledOverride(true);
//
//        // disable vanilla offhand slot
//        ((DuckSlotMixin)this.handler.slots.get(45)).betteradventuremode$setDisabledOverride(true);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        TrinketScreenManager.update(mouseX, mouseY);
        super.render(context, mouseX, mouseY, delta);
        this.drawStatusEffects(context, mouseX, mouseY);
        this.drawMouseoverTooltip(context, mouseX, mouseY);
        this.mouseX = mouseX;
        this.mouseY = mouseY;
    }

    @Override
    protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
        int i = this.x;
        int j = this.y;
        int activeSpellSlotAmount = 0;
        if (this.client != null && this.client.player != null) {
            activeSpellSlotAmount = (int) this.client.player.getAttributeInstance(EntityAttributesRegistry.ACTIVE_SPELL_SLOT_AMOUNT).getValue();
            updateEffectsLists(this.client.player);
        }
        context.drawTexture(BetterAdventureMode.identifier("textures/gui/container/adventure_inventory/adventure_inventory_main_background.png"), i, j, 0, 0, this.backgroundWidth, this.backgroundHeight, this.backgroundWidth, this.backgroundHeight);
        if (!BetterAdventureMode.serverConfig.disable_inventory_crafting_slots) {
            context.drawText(this.textRenderer, CRAFTING_LABEL_TEXT, i + 97, j + 31, 4210752, false);
            context.drawTexture(BetterAdventureMode.identifier("textures/gui/container/adventure_inventory/adventure_inventory_crafting_background.png"), i + 96, j + 41, 0, 0, 74, 36, 74, 36);
        }
        if (activeSpellSlotAmount > 0) {
            context.drawText(this.textRenderer, SPELLS_LABEL_TEXT, i + 98, j + 79, 4210752, false);
            int width = activeSpellSlotAmount < 5 ? (activeSpellSlotAmount % 5) * 18 : 72;
            int height = activeSpellSlotAmount < 5 ? 18 : 36;
            context.drawTexture(BetterAdventureMode.identifier("textures/gui/container/adventure_inventory/adventure_inventory_spells_background_" + activeSpellSlotAmount + ".png"), i + 97, j + 89, 0, 0, width, height, width, height);

        }
        if (this.oldEffectsListSize > 0) {
            context.drawTexture(ADVENTURE_INVENTORY_SIDES_BACKGROUND_TEXTURE, i - 130, j, 0, 0, 130, this.backgroundHeight, 130, this.backgroundHeight);
        }
        if (this.client != null && this.client.player != null) {
//            InventoryScreen.drawEntity(context, i + 26, j + 36, i + 75, j + 106, 30, 0.0625f, this.mouseX, this.mouseY, this.client.player);
            InventoryScreen.drawEntity(context, i + 51, j + 103, 30, (float)(i + 51) - this.mouseX, (float)(j + 103 - 50) - this.mouseY, this.client.player);
        }
    }

    @Override
    public void drawSlot(DrawContext context, Slot slot) {
        super.drawSlot(context, slot);

        // draw slot overlay
        ItemStack stack = slot.getStack();
        if (!stack.isEmpty() && stack.isDamageable() && stack.getDamage() >= stack.getMaxDamage() - 1) {
            drawDisabledItemSlotHighlight(context, slot.x, slot.y, 0);
        }
    }

    public static void drawDisabledItemSlotHighlight(DrawContext context, int x, int y, int z) {
        RenderSystem.disableDepthTest();
        RenderSystem.colorMask(true, true, true, false);
        context.fillGradient(RenderLayer.getGuiOverlay(), x, y, x + 16, y + 16, -1602211792, -1602211792, z);
        RenderSystem.colorMask(true, true, true, true);
        RenderSystem.enableDepthTest();
    }

    private void drawStatusEffects(DrawContext context, int mouseX, int mouseY) {
        int i = this.x - 123;
        int j = this.y + 7;
        if (this.oldEffectsListSize > 0) {
            context.drawText(this.textRenderer, Text.translatable("gui.adventureInventory.status_effects"), i + 1, j, 0x404040, false);
        }
        j += 13;
        if (this.foodEffectsList.size() > 0) {

            context.drawText(this.textRenderer, Text.translatable("gui.adventureInventory.status_effects.food_effects"), i + 1, j, 0x404040, false);
            j += 13;
            for (int k = 3 * this.foodScrollPosition; k < Math.min(this.foodEffectsList.size(), (3 * (1 + this.foodScrollPosition))); k++) {
                drawStatusEffectTexturesAndToolTips(context, i, j, k, mouseX, mouseY, this.foodEffectsList.get(k));
            }
            if (this.foodEffectsRowAmount > 1) {
//                context.drawGuiTexture(EFFECT_SCROLLER_BACKGROUND_TEXTURE, i + 109, j, 8, 32);
                context.drawTexture(EFFECT_SCROLLER_BACKGROUND_TEXTURE, i + 109, j, 0, 0, 8, 32);
                int k = (int)(23.0f * this.foodScrollAmount);
//                context.drawGuiTexture(EFFECT_SCROLLER_TEXTURE, i + 110, j + 1 + k, 6, 7);
                context.drawTexture(EFFECT_SCROLLER_TEXTURE, i + 110, j + 1 + k, 0, 0, 6, 7);
            }
            j += 37;
        } else {
            j += 50;
        }
        if (this.negativeEffectsList.size() > 0) {

            context.drawText(this.textRenderer, Text.translatable("gui.adventureInventory.status_effects.negative_effects"), i + 1, j, 0x404040, false);
            j += 13;
            for (int k = 3 * this.negativeScrollPosition; k < Math.min(this.negativeEffectsList.size(), (3 * (1 + this.negativeScrollPosition))); k++) {
                drawStatusEffectTexturesAndToolTips(context, i, j, k, mouseX, mouseY, this.negativeEffectsList.get(k));
            }
            if (this.negativeEffectsRowAmount > 1) {
//                context.drawGuiTexture(EFFECT_SCROLLER_BACKGROUND_TEXTURE, i + 109, j, 8, 32);
                context.drawTexture(EFFECT_SCROLLER_BACKGROUND_TEXTURE, i + 109, j, 0, 0, 8, 32);
                int k = (int)(23.0f * this.negativeScrollAmount);
//                context.drawGuiTexture(EFFECT_SCROLLER_TEXTURE, i + 110, j + 1 + k, 6, 7);
                context.drawTexture(EFFECT_SCROLLER_TEXTURE, i + 110, j + 1 + k, 0, 0, 6, 7);
            }
            j += 37;
        } else {
            j += 50;
        }
        if (this.positiveEffectsList.size() > 0) {

            context.drawText(this.textRenderer, Text.translatable("gui.adventureInventory.status_effects.positive_effects"), i + 1, j, 0x404040, false);
            j += 13;
            for (int k = 3 * this.positiveScrollPosition; k < Math.min(this.positiveEffectsList.size(), (3 * (1 + this.positiveScrollPosition))); k++) {
                drawStatusEffectTexturesAndToolTips(context, i, j, k, mouseX, mouseY, this.positiveEffectsList.get(k));
            }
            if (this.positiveEffectsRowAmount > 1) {
//                context.drawGuiTexture(EFFECT_SCROLLER_BACKGROUND_TEXTURE, i + 109, j, 8, 32);
                context.drawTexture(EFFECT_SCROLLER_BACKGROUND_TEXTURE, i + 109, j, 0, 0, 8, 32);
                int k = (int)(23.0f * this.positiveScrollAmount);
//                context.drawGuiTexture(EFFECT_SCROLLER_TEXTURE, i + 110, j + 1 + k, 6, 7);
                context.drawTexture(EFFECT_SCROLLER_TEXTURE, i + 110, j + 1 + k, 0, 0, 6, 7);
            }
            j += 37;
        } else {
            j += 50;
        }
        if (this.neutralEffectsList.size() > 0) {

            context.drawText(this.textRenderer, Text.translatable("gui.adventureInventory.status_effects.neutral_effects"), i + 1, j, 0x404040, false);
            j += 13;
            for (int k = 3 * this.neutralScrollPosition; k < Math.min(this.neutralEffectsList.size(), (3 * (1 + this.neutralScrollPosition))); k++) {
                drawStatusEffectTexturesAndToolTips(context, i, j, k, mouseX, mouseY, this.neutralEffectsList.get(k));
            }
            if (this.neutralEffectsRowAmount > 1) {
//                context.drawGuiTexture(EFFECT_SCROLLER_BACKGROUND_TEXTURE, i + 109, j, 8, 32);
                context.drawTexture(EFFECT_SCROLLER_BACKGROUND_TEXTURE, i + 109, j, 0, 0, 8, 32);
                int k = (int)(23.0f * this.neutralScrollAmount);
//                context.drawGuiTexture(EFFECT_SCROLLER_TEXTURE, i + 110, j + 1 + k, 6, 7);
                context.drawTexture(EFFECT_SCROLLER_TEXTURE, i + 110, j + 1 + k, 0, 0, 6, 7);
            }
        }

    }

    private void drawStatusEffectTexturesAndToolTips(DrawContext context, int x, int y, int z, int mouseX, int mouseY, StatusEffectInstance statusEffectInstance) {
        StatusEffectSpriteManager statusEffectSpriteManager = this.client.getStatusEffectSpriteManager();
        int i = x + 3 + ((z % 3) * 35);
//        context.drawGuiTexture(EFFECT_BACKGROUND_SMALL_TEXTURE, i, y, 32, 32);
        context.drawTexture(EFFECT_BACKGROUND_SMALL_TEXTURE, i, y, 0, 0, 32, 32);
        Sprite sprite = statusEffectSpriteManager.getSprite(statusEffectInstance.getEffectType());
        context.drawSprite(i + 7, y + 7, 0, 18, 18, sprite);
        if (mouseX >= i && mouseX <= i + 32 && mouseY >= y && mouseY <= y + 32) {
            List<Text> list = getStatusEffectTooltip(statusEffectInstance);
            context.drawTooltip(this.textRenderer, list, Optional.empty(), mouseX, mouseY);
        }
    }

    private List<Text> getStatusEffectTooltip(StatusEffectInstance statusEffectInstance) {
        if (statusEffectInstance.isInfinite()) {
            return List.of(getStatusEffectName(statusEffectInstance), getStatusEffectDescription(statusEffectInstance));
        }
        return List.of(getStatusEffectName(statusEffectInstance), StatusEffectUtil.getDurationText(statusEffectInstance, 1.0f/*, this.client.world.getTickManager().getTickRate()*/), getStatusEffectDescription(statusEffectInstance));
    }

    private Text getStatusEffectName(StatusEffectInstance statusEffectInstance) {
        MutableText mutableText = statusEffectInstance.getEffectType().getName().copy();
        if (statusEffectInstance.getAmplifier() >= 1 && statusEffectInstance.getAmplifier() <= 9) {
            mutableText.append(ScreenTexts.SPACE).append(Text.translatable("enchantment.level." + (statusEffectInstance.getAmplifier() + 1)));
        }
        return mutableText;
    }

    private Text getStatusEffectDescription(StatusEffectInstance statusEffectInstance) {
        return Text.translatable(statusEffectInstance.getEffectType().getTranslationKey() + ".description");
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        this.foodMouseClicked = false;
        this.negativeMouseClicked = false;
        this.positiveMouseClicked = false;
        this.neutralMouseClicked = false;
        int i = this.x - 13;
        int j;
        if (this.foodEffectsRowAmount > 1) {
            j = this.y + 34;
            if (mouseX >= (double)i && mouseX < (double)(i + 6) && mouseY >= (double)j && mouseY < (double)(j + 30)) {
                this.foodMouseClicked = true;
            }
        }
        if (this.negativeEffectsRowAmount > 1) {
            j = this.y + 84;
            if (mouseX >= (double)i && mouseX < (double)(i + 6) && mouseY >= (double)j && mouseY < (double)(j + 30)) {
                this.negativeMouseClicked = true;
            }
        }
        if (this.positiveEffectsRowAmount > 1) {
            j = this.y + 134;
            if (mouseX >= (double)i && mouseX < (double)(i + 6) && mouseY >= (double)j && mouseY < (double)(j + 30)) {
                this.positiveMouseClicked = true;
            }
        }
        if (this.neutralEffectsRowAmount > 1) {
            j = this.y + 184;
            if (mouseX >= (double)i && mouseX < (double)(i + 6) && mouseY >= (double)j && mouseY < (double)(j + 30)) {
                this.neutralMouseClicked = true;
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        if (this.foodEffectsRowAmount > 1 && this.foodMouseClicked) {
            int i = this.foodEffectsRowAmount - 1;
            float f = (float)deltaY / (float)i;
            this.foodScrollAmount = MathHelper.clamp(this.foodScrollAmount + f, 0.0f, 1.0f);
            this.foodScrollPosition = (int)((double)(this.foodScrollAmount * (float)i));
        }
        if (this.negativeEffectsRowAmount > 1 && this.negativeMouseClicked) {
            int i = this.negativeEffectsRowAmount - 1;
            float f = (float)deltaY / (float)i;
            this.negativeScrollAmount = MathHelper.clamp(this.negativeScrollAmount + f, 0.0f, 1.0f);
            this.negativeScrollPosition = (int)((double)(this.negativeScrollAmount * (float)i));
        }
        if (this.positiveEffectsRowAmount > 1 && this.positiveMouseClicked) {
            int i = this.positiveEffectsRowAmount - 1;
            float f = (float)deltaY / (float)i;
            this.positiveScrollAmount = MathHelper.clamp(this.positiveScrollAmount + f, 0.0f, 1.0f);
            this.positiveScrollPosition = (int)((double)(this.positiveScrollAmount * (float)i));
        }
        if (this.neutralEffectsRowAmount > 1 && this.neutralMouseClicked) {
            int i = this.neutralEffectsRowAmount - 1;
            float f = (float)deltaY / (float)i;
            this.neutralScrollAmount = MathHelper.clamp(this.neutralScrollAmount + f, 0.0f, 1.0f);
            this.neutralScrollPosition = (int)((double)(this.neutralScrollAmount * (float)i));
        }
        return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY/*, double horizontalAmount*/, double verticalAmount) {
        if (this.foodEffectsRowAmount > 1 && mouseX >= this.x - 123 && mouseX <= this.x - 4 && mouseY >= this.y + 33 && mouseY <= this.y + 65) {
            int i = this.foodEffectsRowAmount - 1;
            float f = (float)verticalAmount / (float)i;
            this.foodScrollAmount = MathHelper.clamp(this.foodScrollAmount - f, 0.0f, 1.0f);
            this.foodScrollPosition = (int)((double)(this.foodScrollAmount * (float)i));
        }
        if (this.negativeEffectsRowAmount > 1 && mouseX >= this.x - 123 && mouseX <= this.x - 4 && mouseY >= this.y + 83 && mouseY <= this.y + 115) {
            int i = this.negativeEffectsRowAmount - 1;
            float f = (float)verticalAmount / (float)i;
            this.negativeScrollAmount = MathHelper.clamp(this.negativeScrollAmount - f, 0.0f, 1.0f);
            this.negativeScrollPosition = (int)((double)(this.negativeScrollAmount * (float)i));
        }
        if (this.positiveEffectsRowAmount > 1 && mouseX >= this.x - 123 && mouseX <= this.x - 4 && mouseY >= this.y + 133 && mouseY <= this.y + 165) {
            int i = this.positiveEffectsRowAmount - 1;
            float f = (float)verticalAmount / (float)i;
            this.positiveScrollAmount = MathHelper.clamp(this.positiveScrollAmount - f, 0.0f, 1.0f);
            this.positiveScrollPosition = (int)((double)(this.positiveScrollAmount * (float)i));
        }
        if (this.neutralEffectsRowAmount > 1 && mouseX >= this.x - 123 && mouseX <= this.x - 4 && mouseY >= this.y + 183 && mouseY <= this.y + 215) {
            int i = this.neutralEffectsRowAmount - 1;
            float f = (float)verticalAmount / (float)i;
            this.neutralScrollAmount = MathHelper.clamp(this.neutralScrollAmount - f, 0.0f, 1.0f);
            this.neutralScrollPosition = (int)((double)(this.neutralScrollAmount * (float)i));
        }
        return true;
    }

    @Override
    public TrinketPlayerScreenHandler trinkets$getHandler() {
        return ((TrinketPlayerScreenHandler)this.handler);
    }

    @Override
    public Rect2i trinkets$getGroupRect(SlotGroup group) {
        Point pos = ((TrinketPlayerScreenHandler) handler).trinkets$getGroupPos(group);
        if (pos != null) {
            return new Rect2i(pos.x() - 1, pos.y() - 1, 17, 17);
        }
        return new Rect2i(0, 0, 0, 0);
    }

    @Override
    public Slot trinkets$getFocusedSlot() {
        return this.focusedSlot;
    }

    @Override
    public int trinkets$getX() {
        return this.x;
    }

    @Override
    public int trinkets$getY() {
        return this.y;
    }
}
