package com.github.theredbrain.betteradventuremode.client.gui.screen.ingame;

import com.github.theredbrain.betteradventuremode.BetterAdventureMode;
import com.github.theredbrain.betteradventuremode.registry.KeyBindingsRegistry;
import com.github.theredbrain.betteradventuremode.screen.BackpackScreenHandler;
import com.github.theredbrain.betteradventuremode.screen.DuckSlotMixin;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

@Environment(value=EnvType.CLIENT)
public class BackpackScreen
extends HandledScreen<BackpackScreenHandler> {
    public static final Identifier BACKGROUND_TEXTURE = BetterAdventureMode.identifier("textures/gui/container/backpack_background.png");
    public static final Identifier SLOT_TEXTURE = BetterAdventureMode.identifier("textures/gui/container/inventory_slot.png");
    private final int backpackCapacity;

    public BackpackScreen(BackpackScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
        this.backpackCapacity = handler.getBackpackCapacity();
        for (int i = 36; i < this.handler.slots.size(); i++) {
            if (i < 36 + this.backpackCapacity) {
                ((DuckSlotMixin) this.handler.slots.get(i)).betteradventuremode$setDisabledOverride(false);
            } else {
                ((DuckSlotMixin) this.handler.slots.get(i)).betteradventuremode$setDisabledOverride(true);
            }
        }
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (KeyBindingsRegistry.openBackpackScreen.matchesKey(keyCode, scanCode)) {
            this.close();
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderBackground(context);
        super.render(context, mouseX, mouseY, delta);
        this.drawMouseoverTooltip(context, mouseX, mouseY);
    }

    @Override
    protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
        int i = (this.width - this.backgroundWidth) / 2;
        int j = (this.height - this.backgroundHeight) / 2;
        context.drawTexture(BACKGROUND_TEXTURE, i, j, 0, 0, this.backgroundWidth, this.backgroundHeight, this.backgroundWidth, this.backgroundHeight);
        int k;
        if (this.backpackCapacity > 0) {
            for (k = 0; k < Math.min(this.backpackCapacity, 9); ++k) {
                context.drawTexture(SLOT_TEXTURE, i + 7 + k * 18, j + 17, 0, 0, 18, 18, 18, 18);
            }
        }
        if (this.backpackCapacity > 9) {
            for (k = 9; k < Math.min(this.backpackCapacity, 18); ++k) {
                context.drawTexture(SLOT_TEXTURE, i + 7 + (k - 9) * 18, j + 35, 0, 0, 18, 18, 18, 18);
            }
        }
        if (this.backpackCapacity > 18) {
            for (k = 18; k < Math.min(this.backpackCapacity, 27); ++k) {
                context.drawTexture(SLOT_TEXTURE, i + 7 + (k - 18) * 18, j + 53, 0, 0, 18, 18, 18, 18);
            }
        }
    }
}

