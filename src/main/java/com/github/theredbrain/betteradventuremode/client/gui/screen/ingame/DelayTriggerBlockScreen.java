package com.github.theredbrain.betteradventuremode.client.gui.screen.ingame;

import com.github.theredbrain.betteradventuremode.util.ItemUtils;
import com.github.theredbrain.betteradventuremode.block.entity.DelayTriggerBlockEntity;
import com.github.theredbrain.betteradventuremode.network.packet.UpdateDelayTriggerBlockPacket;
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
public class DelayTriggerBlockScreen extends Screen {
    private static final Text TRIGGERED_BLOCK_POSITION_OFFSET_LABEL_TEXT = Text.translatable("gui.triggered_block.triggeredBlockPositionOffset");
    private static final Text TRIGGER_DELAY_LABEL_TEXT = Text.translatable("gui.delay_trigger_block.triggeredBlockTriggerDelay");
    private final DelayTriggerBlockEntity delayTriggerBlock;
    private TextFieldWidget triggeredBlockPositionOffsetXField;
    private TextFieldWidget triggeredBlockPositionOffsetYField;
    private TextFieldWidget triggeredBlockPositionOffsetZField;
    private TextFieldWidget triggerDelayField;

    public DelayTriggerBlockScreen(DelayTriggerBlockEntity delayTriggerBlock) {
        super(NarratorManager.EMPTY);
        this.delayTriggerBlock = delayTriggerBlock;
    }

    private void done() {
        if (this.updateDelayTriggerBlock()) {
            this.close();
        }
    }

    private void cancel() {
        this.close();
    }

    @Override
    protected void init() {
        this.triggeredBlockPositionOffsetXField = new TextFieldWidget(this.textRenderer, this.width / 2 - 154, 80, 100, 20, Text.translatable(""));
        this.triggeredBlockPositionOffsetXField.setMaxLength(128);
        this.triggeredBlockPositionOffsetXField.setText(Integer.toString(this.delayTriggerBlock.getTriggeredBlockPositionOffset().getX()));
        this.addSelectableChild(this.triggeredBlockPositionOffsetXField);
        this.triggeredBlockPositionOffsetYField = new TextFieldWidget(this.textRenderer, this.width / 2 - 50, 80, 100, 20, Text.translatable(""));
        this.triggeredBlockPositionOffsetYField.setMaxLength(128);
        this.triggeredBlockPositionOffsetYField.setText(Integer.toString(this.delayTriggerBlock.getTriggeredBlockPositionOffset().getY()));
        this.addSelectableChild(this.triggeredBlockPositionOffsetYField);
        this.triggeredBlockPositionOffsetZField = new TextFieldWidget(this.textRenderer, this.width / 2 + 54, 80, 100, 20, Text.translatable(""));
        this.triggeredBlockPositionOffsetZField.setMaxLength(128);
        this.triggeredBlockPositionOffsetZField.setText(Integer.toString(this.delayTriggerBlock.getTriggeredBlockPositionOffset().getZ()));
        this.addSelectableChild(this.triggeredBlockPositionOffsetZField);
        this.triggerDelayField = new TextFieldWidget(this.textRenderer, this.width / 2 - 50, 115, 100, 20, Text.translatable(""));
        this.triggerDelayField.setMaxLength(128);
        this.triggerDelayField.setText(Integer.toString(this.delayTriggerBlock.getTriggerDelay()));
        this.addSelectableChild(this.triggerDelayField);
        this.addDrawableChild(ButtonWidget.builder(ScreenTexts.DONE, button -> this.done()).dimensions(this.width / 2 - 4 - 150, 145, 150, 20).build());
        this.addDrawableChild(ButtonWidget.builder(ScreenTexts.CANCEL, button -> this.cancel()).dimensions(this.width / 2 + 4, 145, 150, 20).build());
        this.setInitialFocus(this.triggeredBlockPositionOffsetXField);
    }

    @Override
    public void resize(MinecraftClient client, int width, int height) {
        String string = this.triggeredBlockPositionOffsetXField.getText();
        String string1 = this.triggeredBlockPositionOffsetYField.getText();
        String string2 = this.triggeredBlockPositionOffsetZField.getText();
        String string3 = this.triggerDelayField.getText();
        this.init(client, width, height);
        this.triggeredBlockPositionOffsetXField.setText(string);
        this.triggeredBlockPositionOffsetYField.setText(string1);
        this.triggeredBlockPositionOffsetZField.setText(string2);
        this.triggerDelayField.setText(string3);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == GLFW.GLFW_KEY_ENTER || keyCode == GLFW.GLFW_KEY_KP_ENTER) {
            this.done();
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    private boolean updateDelayTriggerBlock() {
        ClientPlayNetworking.send(new UpdateDelayTriggerBlockPacket(
                this.delayTriggerBlock.getPos(),
                new BlockPos(
                        ItemUtils.parseInt(this.triggeredBlockPositionOffsetXField.getText()),
                        ItemUtils.parseInt(this.triggeredBlockPositionOffsetYField.getText()),
                        ItemUtils.parseInt(this.triggeredBlockPositionOffsetZField.getText())
                ),
                ItemUtils.parseInt(this.triggerDelayField.getText())
        ));
        return true;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        context.drawTextWithShadow(this.textRenderer, TRIGGERED_BLOCK_POSITION_OFFSET_LABEL_TEXT, this.width / 2 - 153, 70, 0xA0A0A0);
        this.triggeredBlockPositionOffsetXField.render(context, mouseX, mouseY, delta);
        this.triggeredBlockPositionOffsetYField.render(context, mouseX, mouseY, delta);
        this.triggeredBlockPositionOffsetZField.render(context, mouseX, mouseY, delta);
        context.drawTextWithShadow(this.textRenderer, TRIGGER_DELAY_LABEL_TEXT, this.width / 2 - 49, 105, 0xA0A0A0);
        this.triggerDelayField.render(context, mouseX, mouseY, delta);
    }

    @Override
    public boolean shouldPause() {
        return false;
    }
}
