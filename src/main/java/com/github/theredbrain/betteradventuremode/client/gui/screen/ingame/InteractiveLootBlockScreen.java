package com.github.theredbrain.betteradventuremode.client.gui.screen.ingame;

import com.github.theredbrain.betteradventuremode.block.entity.InteractiveLootBlockEntity;
import com.github.theredbrain.betteradventuremode.network.packet.UpdateInteractiveLootBlockPacket;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.util.NarratorManager;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

public class InteractiveLootBlockScreen extends Screen {
    private static final Text LOOT_TABLE_IDENTIFIER_LABEL_TEXT = Text.translatable("gui.interactive_loot_block.loot_table_identifier_label");
    private final InteractiveLootBlockEntity interactiveLootBlockEntity;
    private TextFieldWidget lootTableIdentifierStringField;

    public InteractiveLootBlockScreen(InteractiveLootBlockEntity interactiveLootBlockEntity) {
        super(NarratorManager.EMPTY);
        this.interactiveLootBlockEntity = interactiveLootBlockEntity;
    }

    private void done() {
        this.updateInteractiveLootBlock();
        this.close();
    }

    private void cancel() {
        this.close();
    }

    @Override
    protected void init() {
        this.lootTableIdentifierStringField = new TextFieldWidget(this.textRenderer, this.width / 2 - 154, 80, 300, 20, Text.translatable(""));
        this.lootTableIdentifierStringField.setMaxLength(128);
        this.lootTableIdentifierStringField.setText(this.interactiveLootBlockEntity.getLootTableIdentifierString());
        this.addSelectableChild(this.lootTableIdentifierStringField);

        this.addDrawableChild(ButtonWidget.builder(ScreenTexts.DONE, button -> this.done()).dimensions(this.width / 2 - 4 - 150, 104, 150, 20).build());
        this.addDrawableChild(ButtonWidget.builder(ScreenTexts.CANCEL, button -> this.cancel()).dimensions(this.width / 2 + 4, 104, 150, 20).build());
        this.setInitialFocus(this.lootTableIdentifierStringField);
    }

    @Override
    public void resize(MinecraftClient client, int width, int height) {
        String string = this.lootTableIdentifierStringField.getText();
        this.init(client, width, height);
        this.lootTableIdentifierStringField.setText(string);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == GLFW.GLFW_KEY_ENTER || keyCode == GLFW.GLFW_KEY_KP_ENTER) {
            this.done();
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    private void updateInteractiveLootBlock() {
        ClientPlayNetworking.send(new UpdateInteractiveLootBlockPacket(
                this.interactiveLootBlockEntity.getPos(),
                this.lootTableIdentifierStringField.getText()
        ));
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        context.drawTextWithShadow(this.textRenderer, LOOT_TABLE_IDENTIFIER_LABEL_TEXT, this.width / 2 - 153, 70, 0xA0A0A0);
        this.lootTableIdentifierStringField.render(context, mouseX, mouseY, delta);
    }

    @Override
    public boolean shouldPause() {
        return false;
    }
}
