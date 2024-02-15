package com.github.theredbrain.betteradventuremode.client.gui.screen.ingame;

import com.github.theredbrain.betteradventuremode.util.ItemUtils;
import com.github.theredbrain.betteradventuremode.block.entity.RedstoneTriggerBlockEntity;
import com.github.theredbrain.betteradventuremode.network.packet.UpdateRedstoneTriggerBlockPacket;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.CyclingButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.util.NarratorManager;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import org.lwjgl.glfw.GLFW;

@Environment(value= EnvType.CLIENT)
public class RedstoneTriggerBlockScreen extends Screen {
    private static final Text TRIGGERED_BLOCK_POSITION_TEXT = Text.translatable("gui.triggered_block.triggeredBlockPositionOffset");
    private final RedstoneTriggerBlockEntity redstoneTriggerBlock;
    private TextFieldWidget triggeredBlockPositionOffsetXField;
    private TextFieldWidget triggeredBlockPositionOffsetYField;
    private TextFieldWidget triggeredBlockPositionOffsetZField;
    private CyclingButtonWidget<Boolean> toggleTriggeredBlockResetsButton;
    private boolean triggeredBlockResets;

    public RedstoneTriggerBlockScreen(RedstoneTriggerBlockEntity redstoneTriggerBlock) {
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

        this.triggeredBlockPositionOffsetXField = new TextFieldWidget(this.textRenderer, this.width / 2 - 154, 115, 50, 20, Text.translatable(""));
        this.triggeredBlockPositionOffsetXField.setMaxLength(128);
        this.triggeredBlockPositionOffsetXField.setText(Integer.toString(this.redstoneTriggerBlock.getTriggeredBlock().getLeft().getX()));
        this.addSelectableChild(this.triggeredBlockPositionOffsetXField);
        this.triggeredBlockPositionOffsetYField = new TextFieldWidget(this.textRenderer, this.width / 2 - 100, 115, 50, 20, Text.translatable(""));
        this.triggeredBlockPositionOffsetYField.setMaxLength(128);
        this.triggeredBlockPositionOffsetYField.setText(Integer.toString(this.redstoneTriggerBlock.getTriggeredBlock().getLeft().getY()));
        this.addSelectableChild(this.triggeredBlockPositionOffsetYField);
        this.triggeredBlockPositionOffsetZField = new TextFieldWidget(this.textRenderer, this.width / 2 - 46, 115, 50, 20, Text.translatable(""));
        this.triggeredBlockPositionOffsetZField.setMaxLength(128);
        this.triggeredBlockPositionOffsetZField.setText(Integer.toString(this.redstoneTriggerBlock.getTriggeredBlock().getLeft().getZ()));
        this.addSelectableChild(this.triggeredBlockPositionOffsetZField);
        this.triggeredBlockResets = this.redstoneTriggerBlock.getTriggeredBlock().getRight();
        this.toggleTriggeredBlockResetsButton = this.addDrawableChild(CyclingButtonWidget.onOffBuilder(Text.translatable("gui.triggered_block.toggle_triggered_block_resets_button_label.on"), Text.translatable("gui.triggered_block.toggle_triggered_block_resets_button_label.off")).initially(this.triggeredBlockResets).omitKeyText().build(this.width / 2 + 8, 115, 150, 20, Text.empty(), (button, triggeredBlockResets) -> {
            this.triggeredBlockResets = triggeredBlockResets;
        }));

        this.addDrawableChild(ButtonWidget.builder(ScreenTexts.DONE, button -> this.done()).dimensions(this.width / 2 - 4 - 150, 145, 150, 20).build());
        this.addDrawableChild(ButtonWidget.builder(ScreenTexts.CANCEL, button -> this.cancel()).dimensions(this.width / 2 + 4, 145, 150, 20).build());
        this.setInitialFocus(this.triggeredBlockPositionOffsetXField);
    }

    @Override
    public void resize(MinecraftClient client, int width, int height) {
        String string2 = this.triggeredBlockPositionOffsetXField.getText();
        String string3 = this.triggeredBlockPositionOffsetYField.getText();
        String string4 = this.triggeredBlockPositionOffsetZField.getText();
        boolean bl = this.triggeredBlockResets;
        this.init(client, width, height);
        this.triggeredBlockPositionOffsetXField.setText(string2);
        this.triggeredBlockPositionOffsetYField.setText(string3);
        this.triggeredBlockPositionOffsetZField.setText(string4);
        this.triggeredBlockResets = bl;
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
                        ItemUtils.parseInt(this.triggeredBlockPositionOffsetXField.getText()),
                        ItemUtils.parseInt(this.triggeredBlockPositionOffsetYField.getText()),
                        ItemUtils.parseInt(this.triggeredBlockPositionOffsetZField.getText())
                ),
                this.triggeredBlockResets
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

    @Override
    public boolean shouldPause() {
        return false;
    }
}
