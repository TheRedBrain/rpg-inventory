package com.github.theredbrain.bamcore.client.gui.screen.ingame;

import com.github.theredbrain.bamcore.block.entity.RedstoneTriggerBlockBlockEntity;
import com.github.theredbrain.bamcore.network.packet.UpdateRedstoneTriggerBlockPacket;
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
public class RedstoneTriggerBlockScreen extends Screen {
    private static final Text TRIGGERED_BLOCK_POSITION_TEXT = Text.translatable("gui.triggered_block.triggeredBlockPositionOffset");
    private final RedstoneTriggerBlockBlockEntity redstoneTriggerBlock;
    private TextFieldWidget triggeredBlockPositionOffsetXField;
    private TextFieldWidget triggeredBlockPositionOffsetYField;
    private TextFieldWidget triggeredBlockPositionOffsetZField;

    public RedstoneTriggerBlockScreen(RedstoneTriggerBlockBlockEntity redstoneTriggerBlock) {
        super(NarratorManager.EMPTY);
        this.redstoneTriggerBlock = redstoneTriggerBlock;
    }

    private void done() {
        if (this.updateRedstoneTriggerBlock()) {
            this.close();
        }
    }

    private void cancel() {
        this.close();
    }

    @Override
    protected void init() {
        this.triggeredBlockPositionOffsetXField = new TextFieldWidget(this.textRenderer, this.width / 2 - 154, 115, 100, 20, Text.translatable(""));
        this.triggeredBlockPositionOffsetXField.setMaxLength(128);
        this.triggeredBlockPositionOffsetXField.setText(Integer.toString(this.redstoneTriggerBlock.getTriggeredBlockPositionOffset().getX()));
        this.addSelectableChild(this.triggeredBlockPositionOffsetXField);
        this.triggeredBlockPositionOffsetYField = new TextFieldWidget(this.textRenderer, this.width / 2 - 50, 115, 100, 20, Text.translatable(""));
        this.triggeredBlockPositionOffsetYField.setMaxLength(128);
        this.triggeredBlockPositionOffsetYField.setText(Integer.toString(this.redstoneTriggerBlock.getTriggeredBlockPositionOffset().getY()));
        this.addSelectableChild(this.triggeredBlockPositionOffsetYField);
        this.triggeredBlockPositionOffsetZField = new TextFieldWidget(this.textRenderer, this.width / 2 + 54, 115, 100, 20, Text.translatable(""));
        this.triggeredBlockPositionOffsetZField.setMaxLength(128);
        this.triggeredBlockPositionOffsetZField.setText(Integer.toString(this.redstoneTriggerBlock.getTriggeredBlockPositionOffset().getZ()));
        this.addSelectableChild(this.triggeredBlockPositionOffsetZField);
        this.addDrawableChild(ButtonWidget.builder(ScreenTexts.DONE, button -> this.done()).dimensions(this.width / 2 - 4 - 150, 145, 150, 20).build());
        this.addDrawableChild(ButtonWidget.builder(ScreenTexts.CANCEL, button -> this.cancel()).dimensions(this.width / 2 + 4, 145, 150, 20).build());
        this.setInitialFocus(this.triggeredBlockPositionOffsetXField);
    }

    @Override
    public void resize(MinecraftClient client, int width, int height) {
        String string2 = this.triggeredBlockPositionOffsetXField.getText();
        String string3 = this.triggeredBlockPositionOffsetYField.getText();
        String string4 = this.triggeredBlockPositionOffsetZField.getText();
        this.init(client, width, height);
        this.triggeredBlockPositionOffsetXField.setText(string2);
        this.triggeredBlockPositionOffsetYField.setText(string3);
        this.triggeredBlockPositionOffsetZField.setText(string4);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == GLFW.GLFW_KEY_ENTER || keyCode == GLFW.GLFW_KEY_KP_ENTER) {
            this.done();
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    private boolean updateRedstoneTriggerBlock() {
        ClientPlayNetworking.send(new UpdateRedstoneTriggerBlockPacket(
                this.redstoneTriggerBlock.getPos(),
                new BlockPos(
                        parseInt(this.triggeredBlockPositionOffsetXField.getText()),
                        parseInt(this.triggeredBlockPositionOffsetYField.getText()),
                        parseInt(this.triggeredBlockPositionOffsetZField.getText())
                )
        ));
        return true;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        context.drawTextWithShadow(this.textRenderer, TRIGGERED_BLOCK_POSITION_TEXT, this.width / 2 - 153, 105, 0xA0A0A0);
        this.triggeredBlockPositionOffsetXField.render(context, mouseX, mouseY, delta);
        this.triggeredBlockPositionOffsetYField.render(context, mouseX, mouseY, delta);
        this.triggeredBlockPositionOffsetZField.render(context, mouseX, mouseY, delta);
    }

    private int parseInt(String string) {
        try {
            return Integer.parseInt(string);
        } catch (NumberFormatException numberFormatException) {
            return 0;
        }
    }
}
