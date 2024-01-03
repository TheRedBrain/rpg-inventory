package com.github.theredbrain.betteradventuremode.client.gui.screen.ingame;

import com.github.theredbrain.betteradventuremode.block.entity.MimicBlockEntity;
import com.github.theredbrain.betteradventuremode.network.packet.UpdateMimicBlockPacket;
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
public class MimicBlockScreen extends Screen {
    private static final Text ACTIVE_MIMIC_BLOCK_POSITION_LABEL_TEXT = Text.translatable("gui.mimic_block.activeMimicBlockPositionOffset");
    private static final Text INACTIVE_MIMIC_BLOCK_POSITION_LABEL_TEXT = Text.translatable("gui.mimic_block.inactiveMimicBlockPositionOffset");
    private final MimicBlockEntity mimicBlock;
    private TextFieldWidget activeMimicBlockPositionOffsetXField;
    private TextFieldWidget activeMimicBlockPositionOffsetYField;
    private TextFieldWidget activeMimicBlockPositionOffsetZField;
    private TextFieldWidget inactiveMimicBlockPositionOffsetXField;
    private TextFieldWidget inactiveMimicBlockPositionOffsetYField;
    private TextFieldWidget inactiveMimicBlockPositionOffsetZField;

    public MimicBlockScreen(MimicBlockEntity mimicBlock) {
        super(NarratorManager.EMPTY);
        this.mimicBlock = mimicBlock;
    }

    private void done() {
        this.updateMimicBlock();
        this.close();
    }

    private void cancel() {
        this.close();
    }

    @Override
    protected void init() {
        this.activeMimicBlockPositionOffsetXField = new TextFieldWidget(this.textRenderer, this.width / 2 - 154, 80, 100, 20, Text.translatable(""));
        this.activeMimicBlockPositionOffsetXField.setText(Integer.toString(this.mimicBlock.getActiveMimicBlockPositionOffset().getX()));
        this.addSelectableChild(this.activeMimicBlockPositionOffsetXField);
        this.activeMimicBlockPositionOffsetYField = new TextFieldWidget(this.textRenderer, this.width / 2 - 50, 80, 100, 20, Text.translatable(""));
        this.activeMimicBlockPositionOffsetYField.setText(Integer.toString(this.mimicBlock.getActiveMimicBlockPositionOffset().getY()));
        this.addSelectableChild(this.activeMimicBlockPositionOffsetYField);
        this.activeMimicBlockPositionOffsetZField = new TextFieldWidget(this.textRenderer, this.width / 2 + 54, 80, 100, 20, Text.translatable(""));
        this.activeMimicBlockPositionOffsetZField.setText(Integer.toString(this.mimicBlock.getActiveMimicBlockPositionOffset().getZ()));
        this.addSelectableChild(this.activeMimicBlockPositionOffsetZField);
        
        this.inactiveMimicBlockPositionOffsetXField = new TextFieldWidget(this.textRenderer, this.width / 2 - 154, 115, 100, 20, Text.translatable(""));
        this.inactiveMimicBlockPositionOffsetXField.setText(Integer.toString(this.mimicBlock.getInactiveMimicBlockPositionOffset().getX()));
        this.addSelectableChild(this.inactiveMimicBlockPositionOffsetXField);
        this.inactiveMimicBlockPositionOffsetYField = new TextFieldWidget(this.textRenderer, this.width / 2 - 50, 115, 100, 20, Text.translatable(""));
        this.inactiveMimicBlockPositionOffsetYField.setText(Integer.toString(this.mimicBlock.getInactiveMimicBlockPositionOffset().getY()));
        this.addSelectableChild(this.inactiveMimicBlockPositionOffsetYField);
        this.inactiveMimicBlockPositionOffsetZField = new TextFieldWidget(this.textRenderer, this.width / 2 + 54, 115, 100, 20, Text.translatable(""));
        this.inactiveMimicBlockPositionOffsetZField.setText(Integer.toString(this.mimicBlock.getInactiveMimicBlockPositionOffset().getZ()));
        this.addSelectableChild(this.inactiveMimicBlockPositionOffsetZField);
        
        this.addDrawableChild(ButtonWidget.builder(ScreenTexts.DONE, button -> this.done()).dimensions(this.width / 2 - 4 - 150, 139, 150, 20).build());
        this.addDrawableChild(ButtonWidget.builder(ScreenTexts.CANCEL, button -> this.cancel()).dimensions(this.width / 2 + 4, 139, 150, 20).build());
        this.setInitialFocus(this.activeMimicBlockPositionOffsetXField);
    }

    @Override
    public void resize(MinecraftClient client, int width, int height) {
        String string = this.activeMimicBlockPositionOffsetXField.getText();
        String string1 = this.activeMimicBlockPositionOffsetYField.getText();
        String string2 = this.activeMimicBlockPositionOffsetZField.getText();
        String string3 = this.inactiveMimicBlockPositionOffsetXField.getText();
        String string4 = this.inactiveMimicBlockPositionOffsetYField.getText();
        String string5 = this.inactiveMimicBlockPositionOffsetZField.getText();
        this.init(client, width, height);
        this.activeMimicBlockPositionOffsetXField.setText(string);
        this.activeMimicBlockPositionOffsetYField.setText(string1);
        this.activeMimicBlockPositionOffsetZField.setText(string2);
        this.inactiveMimicBlockPositionOffsetXField.setText(string3);
        this.inactiveMimicBlockPositionOffsetYField.setText(string4);
        this.inactiveMimicBlockPositionOffsetZField.setText(string5);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == GLFW.GLFW_KEY_ENTER || keyCode == GLFW.GLFW_KEY_KP_ENTER) {
            this.done();
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    private void updateMimicBlock() {
        ClientPlayNetworking.send(new UpdateMimicBlockPacket(
                this.mimicBlock.getPos(),
                new BlockPos(
                        parseInt(this.activeMimicBlockPositionOffsetXField.getText()),
                        parseInt(this.activeMimicBlockPositionOffsetYField.getText()),
                        parseInt(this.activeMimicBlockPositionOffsetZField.getText())
                ),
                new BlockPos(
                        parseInt(this.inactiveMimicBlockPositionOffsetXField.getText()),
                        parseInt(this.inactiveMimicBlockPositionOffsetYField.getText()),
                        parseInt(this.inactiveMimicBlockPositionOffsetZField.getText())
                )
        ));
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        context.drawTextWithShadow(this.textRenderer, ACTIVE_MIMIC_BLOCK_POSITION_LABEL_TEXT, this.width / 2 - 153, 71, 0xA0A0A0);
        this.activeMimicBlockPositionOffsetXField.render(context, mouseX, mouseY, delta);
        this.activeMimicBlockPositionOffsetYField.render(context, mouseX, mouseY, delta);
        this.activeMimicBlockPositionOffsetZField.render(context, mouseX, mouseY, delta);

        context.drawTextWithShadow(this.textRenderer, INACTIVE_MIMIC_BLOCK_POSITION_LABEL_TEXT, this.width / 2 - 153, 104, 0xA0A0A0);
        this.inactiveMimicBlockPositionOffsetXField.render(context, mouseX, mouseY, delta);
        this.inactiveMimicBlockPositionOffsetYField.render(context, mouseX, mouseY, delta);
        this.inactiveMimicBlockPositionOffsetZField.render(context, mouseX, mouseY, delta);
    }

    private int parseInt(String string) {
        try {
            return Integer.parseInt(string);
        } catch (NumberFormatException numberFormatException) {
            return 0;
        }
    }
}
