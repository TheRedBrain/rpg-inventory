package com.github.theredbrain.betteradventuremode.client.gui.screen.ingame;

import com.github.theredbrain.betteradventuremode.api.util.ItemUtils;
import com.github.theredbrain.betteradventuremode.block.entity.TriggeredAdvancementCheckerBlockEntity;
import com.github.theredbrain.betteradventuremode.network.packet.UpdateTriggeredAdvancementCheckerBlockPacket;
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

@Environment(value= EnvType.CLIENT)
public class TriggeredAdvancementCheckerBlockScreen extends Screen {
    private static final Text FIRST_TRIGGERED_BLOCK_POSITION_OFFSET_LABEL_TEXT = Text.translatable("gui.triggered_advancement_checker_block.first_triggered_block_position_offset_label");
    private static final Text SECOND_TRIGGERED_BLOCK_POSITION_OFFSET_LABEL_TEXT = Text.translatable("gui.triggered_advancement_checker_block.second_triggered_block_position_offset_label");
    private static final Text CHECKED_STATUS_EFFECT_IDENTIFIER_LABEL_TEXT = Text.translatable("gui.triggered_advancement_checker_block.checked_status_effect_identifier_label");
    private final TriggeredAdvancementCheckerBlockEntity triggeredAdvancementCheckerBlock;
    private TextFieldWidget firstTriggeredBlockPositionOffsetXField;
    private TextFieldWidget firstTriggeredBlockPositionOffsetYField;
    private TextFieldWidget firstTriggeredBlockPositionOffsetZField;
    private TextFieldWidget secondTriggeredBlockPositionOffsetXField;
    private TextFieldWidget secondTriggeredBlockPositionOffsetYField;
    private TextFieldWidget secondTriggeredBlockPositionOffsetZField;
    private TextFieldWidget checkedAdvancementIdentifierField;

    public TriggeredAdvancementCheckerBlockScreen(TriggeredAdvancementCheckerBlockEntity triggeredAdvancementCheckerBlock) {
        super(NarratorManager.EMPTY);
        this.triggeredAdvancementCheckerBlock = triggeredAdvancementCheckerBlock;
    }

    private void done() {
        if (this.updateTriggeredAdvancementCheckerBlock()) {
            this.close();
        }
    }

    private void cancel() {
        this.close();
    }

    @Override
    protected void init() {
        this.firstTriggeredBlockPositionOffsetXField = new TextFieldWidget(this.textRenderer, this.width / 2 - 154, 70, 100, 20, Text.translatable(""));
        this.firstTriggeredBlockPositionOffsetXField.setMaxLength(128);
        this.firstTriggeredBlockPositionOffsetXField.setText(Integer.toString(this.triggeredAdvancementCheckerBlock.getFirstTriggeredBlockPositionOffset().getX()));
        this.addSelectableChild(this.firstTriggeredBlockPositionOffsetXField);
        this.firstTriggeredBlockPositionOffsetYField = new TextFieldWidget(this.textRenderer, this.width / 2 - 50, 70, 100, 20, Text.translatable(""));
        this.firstTriggeredBlockPositionOffsetYField.setMaxLength(128);
        this.firstTriggeredBlockPositionOffsetYField.setText(Integer.toString(this.triggeredAdvancementCheckerBlock.getFirstTriggeredBlockPositionOffset().getY()));
        this.addSelectableChild(this.firstTriggeredBlockPositionOffsetYField);
        this.firstTriggeredBlockPositionOffsetZField = new TextFieldWidget(this.textRenderer, this.width / 2 + 54, 70, 100, 20, Text.translatable(""));
        this.firstTriggeredBlockPositionOffsetZField.setMaxLength(128);
        this.firstTriggeredBlockPositionOffsetZField.setText(Integer.toString(this.triggeredAdvancementCheckerBlock.getFirstTriggeredBlockPositionOffset().getZ()));
        this.addSelectableChild(this.firstTriggeredBlockPositionOffsetZField);
        this.secondTriggeredBlockPositionOffsetXField = new TextFieldWidget(this.textRenderer, this.width / 2 - 154, 105, 100, 20, Text.translatable(""));
        this.secondTriggeredBlockPositionOffsetXField.setMaxLength(128);
        this.secondTriggeredBlockPositionOffsetXField.setText(Integer.toString(this.triggeredAdvancementCheckerBlock.getSecondTriggeredBlockPositionOffset().getX()));
        this.addSelectableChild(this.secondTriggeredBlockPositionOffsetXField);
        this.secondTriggeredBlockPositionOffsetYField = new TextFieldWidget(this.textRenderer, this.width / 2 - 50, 105, 100, 20, Text.translatable(""));
        this.secondTriggeredBlockPositionOffsetYField.setMaxLength(128);
        this.secondTriggeredBlockPositionOffsetYField.setText(Integer.toString(this.triggeredAdvancementCheckerBlock.getSecondTriggeredBlockPositionOffset().getY()));
        this.addSelectableChild(this.secondTriggeredBlockPositionOffsetYField);
        this.secondTriggeredBlockPositionOffsetZField = new TextFieldWidget(this.textRenderer, this.width / 2 + 54, 105, 100, 20, Text.translatable(""));
        this.secondTriggeredBlockPositionOffsetZField.setMaxLength(128);
        this.secondTriggeredBlockPositionOffsetZField.setText(Integer.toString(this.triggeredAdvancementCheckerBlock.getSecondTriggeredBlockPositionOffset().getZ()));
        this.addSelectableChild(this.secondTriggeredBlockPositionOffsetZField);
        this.checkedAdvancementIdentifierField = new TextFieldWidget(this.textRenderer, this.width / 2 - 154, 140, 300, 20, Text.translatable(""));
        this.checkedAdvancementIdentifierField.setMaxLength(128);
        this.checkedAdvancementIdentifierField.setText(this.triggeredAdvancementCheckerBlock.getCheckedAdvancementIdentifier());
        this.addSelectableChild(this.checkedAdvancementIdentifierField);
        this.addDrawableChild(ButtonWidget.builder(ScreenTexts.DONE, button -> this.done()).dimensions(this.width / 2 - 4 - 150, 164, 150, 20).build());
        this.addDrawableChild(ButtonWidget.builder(ScreenTexts.CANCEL, button -> this.cancel()).dimensions(this.width / 2 + 4, 164, 150, 20).build());
        this.setInitialFocus(this.firstTriggeredBlockPositionOffsetXField);
    }

    @Override
    public void resize(MinecraftClient client, int width, int height) {
        String string = this.firstTriggeredBlockPositionOffsetXField.getText();
        String string1 = this.firstTriggeredBlockPositionOffsetYField.getText();
        String string2 = this.firstTriggeredBlockPositionOffsetZField.getText();
        String string3 = this.secondTriggeredBlockPositionOffsetXField.getText();
        String string4 = this.secondTriggeredBlockPositionOffsetYField.getText();
        String string5 = this.secondTriggeredBlockPositionOffsetZField.getText();
        String string6 = this.checkedAdvancementIdentifierField.getText();
        this.init(client, width, height);
        this.firstTriggeredBlockPositionOffsetXField.setText(string);
        this.firstTriggeredBlockPositionOffsetYField.setText(string1);
        this.firstTriggeredBlockPositionOffsetZField.setText(string2);
        this.secondTriggeredBlockPositionOffsetXField.setText(string3);
        this.secondTriggeredBlockPositionOffsetYField.setText(string4);
        this.secondTriggeredBlockPositionOffsetZField.setText(string5);
        this.checkedAdvancementIdentifierField.setText(string6);
    }

    private boolean updateTriggeredAdvancementCheckerBlock() {
        ClientPlayNetworking.send(new UpdateTriggeredAdvancementCheckerBlockPacket(
                this.triggeredAdvancementCheckerBlock.getPos(),
                new BlockPos(
                        ItemUtils.parseInt(this.firstTriggeredBlockPositionOffsetXField.getText()),
                        ItemUtils.parseInt(this.firstTriggeredBlockPositionOffsetYField.getText()),
                        ItemUtils.parseInt(this.firstTriggeredBlockPositionOffsetZField.getText())
                ),
                new BlockPos(
                        ItemUtils.parseInt(this.secondTriggeredBlockPositionOffsetXField.getText()),
                        ItemUtils.parseInt(this.secondTriggeredBlockPositionOffsetYField.getText()),
                        ItemUtils.parseInt(this.secondTriggeredBlockPositionOffsetZField.getText())
                ),
                this.checkedAdvancementIdentifierField.getText()
        ));
        return true;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        context.drawTextWithShadow(this.textRenderer, FIRST_TRIGGERED_BLOCK_POSITION_OFFSET_LABEL_TEXT, this.width / 2 - 153, 60, 0xA0A0A0);
        this.firstTriggeredBlockPositionOffsetXField.render(context, mouseX, mouseY, delta);
        this.firstTriggeredBlockPositionOffsetYField.render(context, mouseX, mouseY, delta);
        this.firstTriggeredBlockPositionOffsetZField.render(context, mouseX, mouseY, delta);
        context.drawTextWithShadow(this.textRenderer, SECOND_TRIGGERED_BLOCK_POSITION_OFFSET_LABEL_TEXT, this.width / 2 - 153, 95, 0xA0A0A0);
        this.secondTriggeredBlockPositionOffsetXField.render(context, mouseX, mouseY, delta);
        this.secondTriggeredBlockPositionOffsetYField.render(context, mouseX, mouseY, delta);
        this.secondTriggeredBlockPositionOffsetZField.render(context, mouseX, mouseY, delta);
        context.drawTextWithShadow(this.textRenderer, CHECKED_STATUS_EFFECT_IDENTIFIER_LABEL_TEXT, this.width / 2 - 153, 130, 0xA0A0A0);
        this.checkedAdvancementIdentifierField.render(context, mouseX, mouseY, delta);
    }

    @Override
    public boolean shouldPause() {
        return false;
    }
}
