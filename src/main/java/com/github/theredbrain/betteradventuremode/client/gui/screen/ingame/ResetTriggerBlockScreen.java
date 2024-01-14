package com.github.theredbrain.betteradventuremode.client.gui.screen.ingame;

import com.github.theredbrain.betteradventuremode.BetterAdventureMode;
import com.github.theredbrain.betteradventuremode.api.util.ItemUtils;
import com.github.theredbrain.betteradventuremode.block.entity.ResetTriggerBlockEntity;
import com.github.theredbrain.betteradventuremode.network.packet.UpdateResetTriggerBlockPacket;
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
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;

@Environment(value= EnvType.CLIENT)
public class ResetTriggerBlockScreen extends Screen {
    private static final Text NEW_RESET_BLOCK_POSITION_TEXT = Text.translatable("gui.reset_block.newResetBlockPositionOffset");
    private static final Text NEW_POSITION_X_FIELD_PLACEHOLDER_TEXT = Text.translatable("gui.reset_block.resetBlockPositionOffsetX.placeholder");
    private static final Text NEW_POSITION_Y_FIELD_PLACEHOLDER_TEXT = Text.translatable("gui.reset_block.resetBlockPositionOffsetY.placeholder");
    private static final Text NEW_POSITION_Z_FIELD_PLACEHOLDER_TEXT = Text.translatable("gui.reset_block.resetBlockPositionOffsetZ.placeholder");
    private static final Text REMOVE_BUTTON_LABEL_TEXT = Text.translatable("gui.remove");
    private static final Text ADD_BUTTON_LABEL_TEXT = Text.translatable("gui.add");
    private static final Identifier RESET_BLOCKS_LIST_SCROLLER_BACKGROUND_TEXTURE = BetterAdventureMode.identifier("scroll_bar/scroll_bar_background_8_116");
    private static final Identifier SCROLLER_TEXTURE = BetterAdventureMode.identifier("scroll_bar/scroller_vertical_6_7");
    private final ResetTriggerBlockEntity resetTriggerBlock;
    private ButtonWidget removeListEntryButton0;
    private ButtonWidget removeListEntryButton1;
    private ButtonWidget removeListEntryButton2;
    private ButtonWidget removeListEntryButton3;
    private ButtonWidget removeListEntryButton4;
    private TextFieldWidget newResetBlockPositionOffsetXField;
    private TextFieldWidget newResetBlockPositionOffsetYField;
    private TextFieldWidget newResetBlockPositionOffsetZField;
    private ButtonWidget addNewResetBlockPositionOffsetButton;
    private ButtonWidget saveButton;
    private ButtonWidget cancelButton;
    private List<BlockPos> resetBlocks = new ArrayList<>(List.of());
    private int scrollPosition = 0;
    private float scrollAmount = 0.0f;
    private boolean mouseClicked = false;

    public ResetTriggerBlockScreen(ResetTriggerBlockEntity resetTriggerBlock) {
        super(NarratorManager.EMPTY);
        this.resetTriggerBlock = resetTriggerBlock;
    }

    private void addResetBlockEntry(BlockPos newResetBlock) {
        for (BlockPos blockPos : this.resetBlocks) {
            if (blockPos.equals(newResetBlock)) {
                return;
            }
        }
        this.resetBlocks.add(newResetBlock);
        this.scrollPosition = 0;
        this.scrollAmount = 0.0f;
        this.updateWidgets();
    }

    private void deleteResetBlockEntry(int index) {
        if (index + this.scrollPosition < this.resetBlocks.size()) {
            this.resetBlocks.remove(index + this.scrollPosition);
        }
        this.scrollPosition = 0;
        this.scrollAmount = 0.0f;
        this.updateWidgets();
    }

    private void done() {
        this.updateResetTriggerBlock();
        this.close();
    }

    private void cancel() {
        this.close();
    }

    @Override
    protected void init() {
        this.resetBlocks.clear();
        this.resetBlocks.addAll(this.resetTriggerBlock.getResetBlocks());
        this.removeListEntryButton0 = this.addDrawableChild(ButtonWidget.builder(REMOVE_BUTTON_LABEL_TEXT, button -> this.deleteResetBlockEntry(0)).dimensions(this.width / 2 + 54, 20, 100, 20).build());
        this.removeListEntryButton1 = this.addDrawableChild(ButtonWidget.builder(REMOVE_BUTTON_LABEL_TEXT, button -> this.deleteResetBlockEntry(1)).dimensions(this.width / 2 + 54, 44, 100, 20).build());
        this.removeListEntryButton2 = this.addDrawableChild(ButtonWidget.builder(REMOVE_BUTTON_LABEL_TEXT, button -> this.deleteResetBlockEntry(2)).dimensions(this.width / 2 + 54, 68, 100, 20).build());
        this.removeListEntryButton3 = this.addDrawableChild(ButtonWidget.builder(REMOVE_BUTTON_LABEL_TEXT, button -> this.deleteResetBlockEntry(3)).dimensions(this.width / 2 + 54, 92, 100, 20).build());
        this.removeListEntryButton4 = this.addDrawableChild(ButtonWidget.builder(REMOVE_BUTTON_LABEL_TEXT, button -> this.deleteResetBlockEntry(4)).dimensions(this.width / 2 + 54, 116, 100, 20).build());
        this.newResetBlockPositionOffsetXField = new TextFieldWidget(this.textRenderer, this.width / 2 - 154, 153, 100, 20, Text.translatable(""));
        this.newResetBlockPositionOffsetXField.setPlaceholder(NEW_POSITION_X_FIELD_PLACEHOLDER_TEXT);
        this.addSelectableChild(this.newResetBlockPositionOffsetXField);
        this.newResetBlockPositionOffsetYField = new TextFieldWidget(this.textRenderer, this.width / 2 - 50, 153, 100, 20, Text.translatable(""));
        this.newResetBlockPositionOffsetYField.setPlaceholder(NEW_POSITION_Y_FIELD_PLACEHOLDER_TEXT);
        this.addSelectableChild(this.newResetBlockPositionOffsetYField);
        this.newResetBlockPositionOffsetZField = new TextFieldWidget(this.textRenderer, this.width / 2 + 54, 153, 100, 20, Text.translatable(""));
        this.newResetBlockPositionOffsetZField.setPlaceholder(NEW_POSITION_Z_FIELD_PLACEHOLDER_TEXT);
        this.addSelectableChild(this.newResetBlockPositionOffsetZField);
        this.addNewResetBlockPositionOffsetButton = this.addDrawableChild(ButtonWidget.builder(ADD_BUTTON_LABEL_TEXT, button -> this.addResetBlockEntry(new BlockPos(ItemUtils.parseInt(this.newResetBlockPositionOffsetXField.getText()), ItemUtils.parseInt(this.newResetBlockPositionOffsetYField.getText()), ItemUtils.parseInt(this.newResetBlockPositionOffsetZField.getText())))).dimensions(this.width / 2 - 4 - 150, 177, 300, 20).build());
        this.saveButton = this.addDrawableChild(ButtonWidget.builder(ScreenTexts.DONE, button -> this.done()).dimensions(this.width / 2 - 4 - 150, 201, 150, 20).build());
        this.cancelButton = this.addDrawableChild(ButtonWidget.builder(ScreenTexts.CANCEL, button -> this.cancel()).dimensions(this.width / 2 + 4, 201, 150, 20).build());
        this.setInitialFocus(this.newResetBlockPositionOffsetXField);
        this.updateWidgets();
    }

    private void updateWidgets() {
        this.removeListEntryButton0.visible = false;
        this.removeListEntryButton1.visible = false;
        this.removeListEntryButton2.visible = false;
        this.removeListEntryButton3.visible = false;
        this.removeListEntryButton4.visible = false;

        this.newResetBlockPositionOffsetXField.setVisible(false);
        this.newResetBlockPositionOffsetYField.setVisible(false);
        this.newResetBlockPositionOffsetZField.setVisible(false);

        this.addNewResetBlockPositionOffsetButton.visible = false;
        this.saveButton.visible = false;
        this.cancelButton.visible = false;
        
        int index = 0;
        for (int i = 0; i < Math.min(5, this.resetBlocks.size()); i++) {
            if (index == 0) {
                this.removeListEntryButton0.visible = true;
            } else if (index == 1) {
                this.removeListEntryButton1.visible = true;
            } else if (index == 2) {
                this.removeListEntryButton2.visible = true;
            } else if (index == 3) {
                this.removeListEntryButton3.visible = true;
            } else if (index == 4) {
                this.removeListEntryButton4.visible = true;
            }
            index++;
        }

        this.newResetBlockPositionOffsetXField.setVisible(true);
        this.newResetBlockPositionOffsetYField.setVisible(true);
        this.newResetBlockPositionOffsetZField.setVisible(true);

        this.addNewResetBlockPositionOffsetButton.visible = true;
        this.saveButton.visible = true;
        this.cancelButton.visible = true;
    }

    @Override
    public void resize(MinecraftClient client, int width, int height) {
        List<BlockPos> list = new ArrayList<>(this.resetBlocks);
        String string = this.newResetBlockPositionOffsetXField.getText();
        String string1 = this.newResetBlockPositionOffsetYField.getText();
        String string2 = this.newResetBlockPositionOffsetZField.getText();
        this.init(client, width, height);
        this.resetBlocks.clear();
        this.resetBlocks.addAll(list);
        this.newResetBlockPositionOffsetXField.setText(string);
        this.newResetBlockPositionOffsetYField.setText(string1);
        this.newResetBlockPositionOffsetZField.setText(string2);
        this.updateWidgets();
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        this.mouseClicked = false;
        if (this.resetBlocks.size() > 5) {
            int i = this.width / 2 - 152;
            int j = 21;
            if (mouseX >= (double)i && mouseX < (double)(i + 6) && mouseY >= (double)j && mouseY < (double)(j + 114)) {
                this.mouseClicked = true;
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        if (this.resetBlocks.size() > 5
                && this.mouseClicked) {
            int i = this.resetBlocks.size() - 5;
            float f = (float)deltaY / (float)i;
            this.scrollAmount = MathHelper.clamp(this.scrollAmount + f, 0.0f, 1.0f);
            this.scrollPosition = (int)((double)(this.scrollAmount * (float)i));
        }
        return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        if (this.resetBlocks.size() > 5
                && mouseX >= (double)(this.width / 2 - 152) && mouseX <= (double)(this.width / 2 + 50)
                && mouseY >= 20 && mouseY <= 135) {
            int i = this.resetBlocks.size() - 5;
            float f = (float)verticalAmount / (float)i;
            this.scrollAmount = MathHelper.clamp(this.scrollAmount - f, 0.0f, 1.0f);
            this.scrollPosition = (int)((double)(this.scrollAmount * (float)i));
        }
        return super.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == GLFW.GLFW_KEY_ENTER || keyCode == GLFW.GLFW_KEY_KP_ENTER) {
            this.done();
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    private void updateResetTriggerBlock() {
        ClientPlayNetworking.send(new UpdateResetTriggerBlockPacket(this.resetTriggerBlock.getPos(), this.resetBlocks));
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        for (int i = this.scrollPosition; i < Math.min(this.scrollPosition + 5, this.resetBlocks.size()); i++) {
            context.drawTextWithShadow(this.textRenderer, this.resetBlocks.get(i).toString(), this.width / 2 - 141, 26 + ((i - this.scrollPosition) * 24), 0xA0A0A0);
        }
        if (this.resetBlocks.size() > 5) {
            context.drawGuiTexture(RESET_BLOCKS_LIST_SCROLLER_BACKGROUND_TEXTURE, this.width / 2 - 153, 20, 8, 116);
            int k = (int)(107.0f * this.scrollAmount);
            context.drawGuiTexture(SCROLLER_TEXTURE, this.width / 2 - 152, 20 + 1 + k, 6, 7);
        }
        context.drawTextWithShadow(this.textRenderer, NEW_RESET_BLOCK_POSITION_TEXT, this.width / 2 - 153, 140, 0xA0A0A0);
        this.newResetBlockPositionOffsetXField.render(context, mouseX, mouseY, delta);
        this.newResetBlockPositionOffsetYField.render(context, mouseX, mouseY, delta);
        this.newResetBlockPositionOffsetZField.render(context, mouseX, mouseY, delta);
    }

    @Override
    public boolean shouldPause() {
        return false;
    }
}
