package com.github.theredbrain.betteradventuremode.client.gui.screen.ingame;

import com.github.theredbrain.betteradventuremode.util.ItemUtils;
import com.github.theredbrain.betteradventuremode.block.entity.UseRelayBlockEntity;
import com.github.theredbrain.betteradventuremode.network.packet.UpdateUseRelayBlockPacket;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.util.NarratorManager;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import org.lwjgl.glfw.GLFW;

@Environment(value= EnvType.CLIENT)
public class UseRelayBlockScreen extends Screen {
    private static final Text RELAY_BLOCK_POSITION_OFFSET_LABEL_TEXT = Text.translatable("gui.use_relay_block.relayBlockPositionOffset");
    private final UseRelayBlockEntity useRelayBlock;
    private TextFieldWidget relayBlockPositionOffsetXField;
    private TextFieldWidget relayBlockPositionOffsetYField;
    private TextFieldWidget relayBlockPositionOffsetZField;

    public UseRelayBlockScreen(UseRelayBlockEntity useRelayBlock) {
        super(NarratorManager.EMPTY);
        this.useRelayBlock = useRelayBlock;
    }

    private void done() {
        if (this.updateUseRelayBlock()) {
            this.close();
        }
    }

    private void cancel() {
        this.close();
    }

    @Override
    protected void init() {
        this.relayBlockPositionOffsetXField = new TextFieldWidget(this.textRenderer, this.width / 2 - 154, 80, 100, 20, Text.translatable(""));
        this.relayBlockPositionOffsetXField.setMaxLength(128);
        this.relayBlockPositionOffsetXField.setText(Integer.toString(this.useRelayBlock.getRelayBlockPositionOffset().getX()));
        this.addSelectableChild(this.relayBlockPositionOffsetXField);
        this.relayBlockPositionOffsetYField = new TextFieldWidget(this.textRenderer, this.width / 2 - 50, 80, 100, 20, Text.translatable(""));
        this.relayBlockPositionOffsetYField.setMaxLength(128);
        this.relayBlockPositionOffsetYField.setText(Integer.toString(this.useRelayBlock.getRelayBlockPositionOffset().getY()));
        this.addSelectableChild(this.relayBlockPositionOffsetYField);
        this.relayBlockPositionOffsetZField = new TextFieldWidget(this.textRenderer, this.width / 2 + 54, 80, 100, 20, Text.translatable(""));
        this.relayBlockPositionOffsetZField.setMaxLength(128);
        this.relayBlockPositionOffsetZField.setText(Integer.toString(this.useRelayBlock.getRelayBlockPositionOffset().getZ()));
        this.addSelectableChild(this.relayBlockPositionOffsetZField);
        this.addDrawableChild(ButtonWidget.builder(ScreenTexts.DONE, button -> this.done()).dimensions(this.width / 2 - 4 - 150, 145, 150, 20).build());
        this.addDrawableChild(ButtonWidget.builder(ScreenTexts.CANCEL, button -> this.cancel()).dimensions(this.width / 2 + 4, 145, 150, 20).build());
        this.setInitialFocus(this.relayBlockPositionOffsetXField);
    }

    @Override
    public void resize(MinecraftClient client, int width, int height) {
        String string = this.relayBlockPositionOffsetXField.getText();
        String string1 = this.relayBlockPositionOffsetYField.getText();
        String string2 = this.relayBlockPositionOffsetZField.getText();
        this.init(client, width, height);
        this.relayBlockPositionOffsetXField.setText(string);
        this.relayBlockPositionOffsetYField.setText(string1);
        this.relayBlockPositionOffsetZField.setText(string2);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == GLFW.GLFW_KEY_ENTER || keyCode == GLFW.GLFW_KEY_KP_ENTER) {
            this.done();
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    private boolean updateUseRelayBlock() {
        ClientPlayNetworking.send(new UpdateUseRelayBlockPacket(
                this.useRelayBlock.getPos(),
                new BlockPos(
                        ItemUtils.parseInt(this.relayBlockPositionOffsetXField.getText()),
                        ItemUtils.parseInt(this.relayBlockPositionOffsetYField.getText()),
                        ItemUtils.parseInt(this.relayBlockPositionOffsetZField.getText())
                )
        ));
        return true;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        context.drawTextWithShadow(this.textRenderer, RELAY_BLOCK_POSITION_OFFSET_LABEL_TEXT, this.width / 2 - 153, 70, 0xA0A0A0);
        this.relayBlockPositionOffsetXField.render(context, mouseX, mouseY, delta);
        this.relayBlockPositionOffsetYField.render(context, mouseX, mouseY, delta);
        this.relayBlockPositionOffsetZField.render(context, mouseX, mouseY, delta);
    }

    @Override
    public boolean shouldPause() {
        return false;
    }
}
