package com.github.theredbrain.betteradventuremode.client.gui.screen.ingame;

import com.github.theredbrain.betteradventuremode.BetterAdventureMode;
import com.github.theredbrain.betteradventuremode.util.ItemUtils;
import com.github.theredbrain.betteradventuremode.block.entity.RelayTriggerBlockEntity;
import com.github.theredbrain.betteradventuremode.network.packet.UpdateRelayTriggerBlockPacket;
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
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3i;
import org.apache.commons.lang3.tuple.MutablePair;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;

@Environment(value= EnvType.CLIENT)
public class RelayTriggerBlockScreen extends Screen {
    private static final Text HIDE_AREA_LABEL_TEXT = Text.translatable("gui.relay_trigger_block.hide_area_label");
    private static final Text SHOW_AREA_LABEL_TEXT = Text.translatable("gui.relay_trigger_block.show_area_label");
    private static final Text RESETS_AREA_LABEL_TEXT = Text.translatable("gui.relay_trigger_block.resets_area_label");
    private static final Text TRIGGERS_AREA_LABEL_TEXT = Text.translatable("gui.relay_trigger_block.triggers_area_label");
    private static final Text AREA_DIMENSIONS_LABEL_TEXT = Text.translatable("gui.relay_trigger_block.area_dimensions_label");
    private static final Text AREA_POSITION_OFFET_LABEL_TEXT = Text.translatable("gui.relay_trigger_block.area_position_offset_label");
    private static final Text NEW_TRIGGERED_BLOCK_POSITION_TEXT = Text.translatable("gui.triggered_block.newTriggeredBlockPositionOffset");
    private static final Text NEW_POSITION_X_FIELD_PLACEHOLDER_TEXT = Text.translatable("gui.triggered_block.triggeredBlockPositionOffsetX.placeholder");
    private static final Text NEW_POSITION_Y_FIELD_PLACEHOLDER_TEXT = Text.translatable("gui.triggered_block.triggeredBlockPositionOffsetY.placeholder");
    private static final Text NEW_POSITION_Z_FIELD_PLACEHOLDER_TEXT = Text.translatable("gui.triggered_block.triggeredBlockPositionOffsetZ.placeholder");
    private static final Text NEW_POSITION_CHANCE_FIELD_LABEL_TEXT = Text.translatable("gui.relay_trigger_block.newTriggeredBlockChanceLabel");
    private static final Text REMOVE_BUTTON_LABEL_TEXT = Text.translatable("gui.remove");
    private static final Text ADD_BUTTON_LABEL_TEXT = Text.translatable("gui.add");
    private static final Identifier TRIGGERED_BLOCKS_LIST_SCROLLER_BACKGROUND_TEXTURE = BetterAdventureMode.identifier("scroll_bar/scroll_bar_background_8_92");
    private static final Identifier SCROLLER_TEXTURE = BetterAdventureMode.identifier("scroll_bar/scroller_vertical_6_7");
    private final RelayTriggerBlockEntity relayTriggerBlock;

    private RelayTriggerBlockEntity.SelectionMode selectionMode;

    private CyclingButtonWidget<Boolean> toggleShowAreaButton;
    private CyclingButtonWidget<Boolean> toggleResetsAreaButton;
    private TextFieldWidget areaDimensionsXField;
    private TextFieldWidget areaDimensionsYField;
    private TextFieldWidget areaDimensionsZField;
    private TextFieldWidget areaPositionOffsetXField;
    private TextFieldWidget areaPositionOffsetYField;
    private TextFieldWidget areaPositionOffsetZField;
    private boolean showArea;
    private boolean resetsArea;

    private ButtonWidget removeListEntryButton0;
    private ButtonWidget removeListEntryButton1;
    private ButtonWidget removeListEntryButton2;
    private TextFieldWidget newTriggeredBlockPositionOffsetXField;
    private TextFieldWidget newTriggeredBlockPositionOffsetYField;
    private TextFieldWidget newTriggeredBlockPositionOffsetZField;
    private CyclingButtonWidget<Boolean> toggleNewTriggeredBlockResetsButton;
    private boolean newTriggeredBlockResets;
    private TextFieldWidget newTriggeredBlockChanceField;
    private ButtonWidget addNewTriggeredBlockPositionOffsetButton;
    private CyclingButtonWidget<RelayTriggerBlockEntity.TriggerMode> cycleTriggerModeButton;
    private TextFieldWidget triggerAmountField;
    private List<MutablePair<MutablePair<BlockPos, Boolean>, Integer>> triggeredBlocks = new ArrayList<>(List.of());
    private RelayTriggerBlockEntity.TriggerMode triggerMode;
    private int scrollPosition = 0;
    private float scrollAmount = 0.0f;
    private boolean mouseClicked = false;

    public RelayTriggerBlockScreen(RelayTriggerBlockEntity relayTriggerBlock) {
        super(NarratorManager.EMPTY);
        this.relayTriggerBlock = relayTriggerBlock;
    }

    private void addTriggeredBlockEntry(BlockPos newTriggeredBlockPos, boolean resets, int chance) {
        if (chance > 100) {
            chance = 100;
        } else if (chance < 0) {
            chance = 0;
        }
        MutablePair<MutablePair<BlockPos, Boolean>, Integer> newTriggeredBlock = new MutablePair<>(new MutablePair<>(new BlockPos(newTriggeredBlockPos), resets), chance);
        for (MutablePair<MutablePair<BlockPos, Boolean>, Integer> triggeredBlock : this.triggeredBlocks) {
            if (triggeredBlock.equals(newTriggeredBlock)) {
                return;
            }
        }
        this.triggeredBlocks.add(newTriggeredBlock);
        this.scrollPosition = 0;
        this.scrollAmount = 0.0f;
        this.updateWidgets();
    }

    private void deleteTriggeredBlockEntry(int index) {
        if (index + this.scrollPosition < this.triggeredBlocks.size()) {
            this.triggeredBlocks.remove(index + this.scrollPosition);
        }
        this.scrollPosition = 0;
        this.scrollAmount = 0.0f;
        this.updateWidgets();
    }

    private void done() {
        this.updateRelayTriggerBlock();
        this.close();
    }

    private void cancel() {
        // TODO
        this.close();
    }

    @Override
    protected void init() {

        this.selectionMode = this.relayTriggerBlock.getSelectionMode();
        this.addDrawableChild(CyclingButtonWidget.builder(RelayTriggerBlockEntity.SelectionMode::asText).values((RelayTriggerBlockEntity.SelectionMode[]) RelayTriggerBlockEntity.SelectionMode.values()).initially(this.selectionMode).omitKeyText().build(this.width / 2 - 154, 20, 300, 20, Text.empty(), (button, selectionMode) -> {
            this.selectionMode = selectionMode;
            this.updateWidgets();
        }));
        
        // Area Selection
        this.showArea = this.relayTriggerBlock.getShowArea();
        this.toggleShowAreaButton = this.addDrawableChild(CyclingButtonWidget.onOffBuilder(HIDE_AREA_LABEL_TEXT, SHOW_AREA_LABEL_TEXT).initially(this.showArea).omitKeyText().build(this.width / 2 - 154, 54, 300, 20, Text.empty(), (button, showArea) -> {
            this.showArea = showArea;
        }));

        this.areaDimensionsXField = new TextFieldWidget(this.textRenderer, this.width / 2 - 154, 89, 100, 20, Text.empty());
        this.areaDimensionsXField.setMaxLength(128);
        this.areaDimensionsXField.setText(Integer.toString(this.relayTriggerBlock.getAreaDimensions().getX()));
        this.addSelectableChild(this.areaDimensionsXField);

        this.areaDimensionsYField = new TextFieldWidget(this.textRenderer, this.width / 2 - 50, 89, 100, 20, Text.empty());
        this.areaDimensionsYField.setMaxLength(128);
        this.areaDimensionsYField.setText(Integer.toString(this.relayTriggerBlock.getAreaDimensions().getY()));
        this.addSelectableChild(this.areaDimensionsYField);

        this.areaDimensionsZField = new TextFieldWidget(this.textRenderer, this.width / 2 + 54, 89, 100, 20, Text.empty());
        this.areaDimensionsZField.setMaxLength(128);
        this.areaDimensionsZField.setText(Integer.toString(this.relayTriggerBlock.getAreaDimensions().getZ()));
        this.addSelectableChild(this.areaDimensionsZField);

        this.areaPositionOffsetXField = new TextFieldWidget(this.textRenderer, this.width / 2 - 154, 124, 100, 20, Text.empty());
        this.areaPositionOffsetXField.setMaxLength(128);
        this.areaPositionOffsetXField.setText(Integer.toString(this.relayTriggerBlock.getAreaPositionOffset().getX()));
        this.addSelectableChild(this.areaPositionOffsetXField);

        this.areaPositionOffsetYField = new TextFieldWidget(this.textRenderer, this.width / 2 - 50, 124, 100, 20, Text.empty());
        this.areaPositionOffsetYField.setMaxLength(128);
        this.areaPositionOffsetYField.setText(Integer.toString(this.relayTriggerBlock.getAreaPositionOffset().getY()));
        this.addSelectableChild(this.areaPositionOffsetYField);

        this.areaPositionOffsetZField = new TextFieldWidget(this.textRenderer, this.width / 2 + 54, 124, 100, 20, Text.empty());
        this.areaPositionOffsetZField.setMaxLength(128);
        this.areaPositionOffsetZField.setText(Integer.toString(this.relayTriggerBlock.getAreaPositionOffset().getZ()));
        this.addSelectableChild(this.areaPositionOffsetZField);

        this.resetsArea = this.relayTriggerBlock.getResetsArea();
        this.toggleResetsAreaButton = this.addDrawableChild(CyclingButtonWidget.onOffBuilder(RESETS_AREA_LABEL_TEXT, TRIGGERS_AREA_LABEL_TEXT).initially(this.resetsArea).omitKeyText().build(this.width / 2 - 154, 148, 300, 20, Text.empty(), (button, resetsArea) -> {
            this.resetsArea = resetsArea;
        }));


        // List Selection
        this.triggeredBlocks.clear();
        this.triggeredBlocks.addAll(this.relayTriggerBlock.getTriggeredBlocks());
        this.removeListEntryButton0 = this.addDrawableChild(ButtonWidget.builder(REMOVE_BUTTON_LABEL_TEXT, button -> this.deleteTriggeredBlockEntry(0)).dimensions(this.width / 2 + 54, 44, 100, 20).build());
        this.removeListEntryButton1 = this.addDrawableChild(ButtonWidget.builder(REMOVE_BUTTON_LABEL_TEXT, button -> this.deleteTriggeredBlockEntry(1)).dimensions(this.width / 2 + 54, 68, 100, 20).build());
        this.removeListEntryButton2 = this.addDrawableChild(ButtonWidget.builder(REMOVE_BUTTON_LABEL_TEXT, button -> this.deleteTriggeredBlockEntry(2)).dimensions(this.width / 2 + 54, 92, 100, 20).build());
        this.newTriggeredBlockPositionOffsetXField = new TextFieldWidget(this.textRenderer, this.width / 2 - 154, 129, 100, 20, Text.translatable(""));
        this.newTriggeredBlockPositionOffsetXField.setPlaceholder(NEW_POSITION_X_FIELD_PLACEHOLDER_TEXT);
        this.addSelectableChild(this.newTriggeredBlockPositionOffsetXField);
        this.newTriggeredBlockPositionOffsetYField = new TextFieldWidget(this.textRenderer, this.width / 2 - 50, 129, 100, 20, Text.translatable(""));
        this.newTriggeredBlockPositionOffsetYField.setPlaceholder(NEW_POSITION_Y_FIELD_PLACEHOLDER_TEXT);
        this.addSelectableChild(this.newTriggeredBlockPositionOffsetYField);
        this.newTriggeredBlockPositionOffsetZField = new TextFieldWidget(this.textRenderer, this.width / 2 + 54, 129, 100, 20, Text.translatable(""));
        this.newTriggeredBlockPositionOffsetZField.setPlaceholder(NEW_POSITION_Z_FIELD_PLACEHOLDER_TEXT);
        this.addSelectableChild(this.newTriggeredBlockPositionOffsetZField);

        this.newTriggeredBlockResets = false;
        this.toggleNewTriggeredBlockResetsButton = this.addDrawableChild(CyclingButtonWidget.onOffBuilder(Text.translatable("gui.triggered_block.toggle_triggered_block_resets_button_label.on"), Text.translatable("gui.triggered_block.toggle_triggered_block_resets_button_label.off")).initially(this.newTriggeredBlockResets).omitKeyText().build(this.width / 2 - 154, 153, 100, 20, Text.empty(), (button, triggeredBlockResets) -> {
            this.newTriggeredBlockResets = triggeredBlockResets;
        }));

        int i = this.textRenderer.getWidth(NEW_POSITION_CHANCE_FIELD_LABEL_TEXT) + 4;
        this.newTriggeredBlockChanceField = new TextFieldWidget(this.textRenderer, this.width / 2 - 40 + i, 153, 186 - i, 20, Text.translatable(""));
        this.newTriggeredBlockChanceField.setText(Integer.toString(100));
        this.addSelectableChild(this.newTriggeredBlockChanceField);

        this.addNewTriggeredBlockPositionOffsetButton = this.addDrawableChild(ButtonWidget.builder(ADD_BUTTON_LABEL_TEXT, button -> this.addTriggeredBlockEntry(new BlockPos(ItemUtils.parseInt(this.newTriggeredBlockPositionOffsetXField.getText()), ItemUtils.parseInt(this.newTriggeredBlockPositionOffsetYField.getText()), ItemUtils.parseInt(this.newTriggeredBlockPositionOffsetZField.getText())), this.newTriggeredBlockResets, ItemUtils.parseInt(this.newTriggeredBlockChanceField.getText()))).dimensions(this.width / 2 - 4 - 150, 177, 100, 20).build());

        this.triggerMode = this.relayTriggerBlock.getTriggerMode();
        this.cycleTriggerModeButton = this.addDrawableChild(CyclingButtonWidget.builder(RelayTriggerBlockEntity.TriggerMode::asText).values((RelayTriggerBlockEntity.TriggerMode[]) RelayTriggerBlockEntity.TriggerMode.values()).initially(this.triggerMode).omitKeyText().build(this.width / 2 - 50, 177, 150, 20, Text.empty(), (button, triggerMode) -> {
            this.triggerMode = triggerMode;
        }));

        this.triggerAmountField = new TextFieldWidget(this.textRenderer, this.width / 2 + 104, 177, 50, 20, Text.translatable(""));
        this.triggerAmountField.setText(Integer.toString(this.relayTriggerBlock.getTriggerAmount()));
        this.addSelectableChild(this.triggerAmountField);

        this.addDrawableChild(ButtonWidget.builder(ScreenTexts.DONE, button -> this.done()).dimensions(this.width / 2 - 4 - 150, 201, 150, 20).build());
        this.addDrawableChild(ButtonWidget.builder(ScreenTexts.CANCEL, button -> this.cancel()).dimensions(this.width / 2 + 4, 201, 150, 20).build());
        this.setInitialFocus(this.newTriggeredBlockPositionOffsetXField);
        this.updateWidgets();
    }

    private void updateWidgets() {
        this.toggleShowAreaButton.visible = false;

        this.areaDimensionsXField.setVisible(false);
        this.areaDimensionsYField.setVisible(false);
        this.areaDimensionsZField.setVisible(false);

        this.areaPositionOffsetXField.setVisible(false);
        this.areaPositionOffsetYField.setVisible(false);
        this.areaPositionOffsetZField.setVisible(false);

        this.toggleResetsAreaButton.visible = false;
        
        
        this.removeListEntryButton0.visible = false;
        this.removeListEntryButton1.visible = false;
        this.removeListEntryButton2.visible = false;

        this.newTriggeredBlockPositionOffsetXField.setVisible(false);
        this.newTriggeredBlockPositionOffsetYField.setVisible(false);
        this.newTriggeredBlockPositionOffsetZField.setVisible(false);

        this.toggleNewTriggeredBlockResetsButton.visible = false;

        this.newTriggeredBlockChanceField.setVisible(false);
        
        this.addNewTriggeredBlockPositionOffsetButton.visible = false;
        this.cycleTriggerModeButton.visible = false;

        this.triggerAmountField.setVisible(false);

        if (this.selectionMode == RelayTriggerBlockEntity.SelectionMode.AREA) {
            this.toggleShowAreaButton.visible = true;

            this.areaDimensionsXField.setVisible(true);
            this.areaDimensionsYField.setVisible(true);
            this.areaDimensionsZField.setVisible(true);

            this.areaPositionOffsetXField.setVisible(true);
            this.areaPositionOffsetYField.setVisible(true);
            this.areaPositionOffsetZField.setVisible(true);

            this.toggleResetsAreaButton.visible = true;

        } else if (this.selectionMode == RelayTriggerBlockEntity.SelectionMode.LIST) {
            int index = 0;
            for (int i = 0; i < Math.min(3, this.triggeredBlocks.size()); i++) {
                if (index == 0) {
                    this.removeListEntryButton0.visible = true;
                } else if (index == 1) {
                    this.removeListEntryButton1.visible = true;
                } else if (index == 2) {
                    this.removeListEntryButton2.visible = true;
                }
                index++;
            }

            this.newTriggeredBlockPositionOffsetXField.setVisible(true);
            this.newTriggeredBlockPositionOffsetYField.setVisible(true);
            this.newTriggeredBlockPositionOffsetZField.setVisible(true);

            this.toggleNewTriggeredBlockResetsButton.visible = true;

            this.newTriggeredBlockChanceField.setVisible(true);

            this.addNewTriggeredBlockPositionOffsetButton.visible = true;
            this.cycleTriggerModeButton.visible = true;

            this.triggerAmountField.setVisible(true);
        }
    }

    @Override
    public void resize(MinecraftClient client, int width, int height) {
        List<MutablePair<MutablePair<BlockPos, Boolean>, Integer>> list = new ArrayList<>(this.triggeredBlocks);
        String string = this.newTriggeredBlockPositionOffsetXField.getText();
        String string1 = this.newTriggeredBlockPositionOffsetYField.getText();
        String string2 = this.newTriggeredBlockPositionOffsetZField.getText();
        this.init(client, width, height);
        this.triggeredBlocks.clear();
        this.triggeredBlocks.addAll(list);
        this.newTriggeredBlockPositionOffsetXField.setText(string);
        this.newTriggeredBlockPositionOffsetYField.setText(string1);
        this.newTriggeredBlockPositionOffsetZField.setText(string2);
        this.updateWidgets();
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        this.mouseClicked = false;
        if (this.triggeredBlocks.size() > 3) {
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
        if (this.triggeredBlocks.size() > 3
                && this.mouseClicked) {
            int i = this.triggeredBlocks.size() - 3;
            float f = (float)deltaY / (float)i;
            this.scrollAmount = MathHelper.clamp(this.scrollAmount + f, 0.0f, 1.0f);
            this.scrollPosition = (int)((double)(this.scrollAmount * (float)i));
        }
        return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY/*, double horizontalAmount*/, double verticalAmount) {
        if (this.triggeredBlocks.size() > 3
                && mouseX >= (double)(this.width / 2 - 152) && mouseX <= (double)(this.width / 2 + 50)
                && mouseY >= 20 && mouseY <= 135) {
            int i = this.triggeredBlocks.size() - 3;
            float f = (float)verticalAmount / (float)i;
            this.scrollAmount = MathHelper.clamp(this.scrollAmount - f, 0.0f, 1.0f);
            this.scrollPosition = (int)((double)(this.scrollAmount * (float)i));
        }
        return super.mouseScrolled(mouseX, mouseY/*, horizontalAmount*/, verticalAmount);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == GLFW.GLFW_KEY_ENTER || keyCode == GLFW.GLFW_KEY_KP_ENTER) {
            this.done();
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    private void updateRelayTriggerBlock() {
        ClientPlayNetworking.send(
                new UpdateRelayTriggerBlockPacket(
                        this.relayTriggerBlock.getPos(),
                        this.selectionMode.asString(),
                        this.showArea,
                        this.resetsArea,
                        new Vec3i(
                                ItemUtils.parseInt(this.areaDimensionsXField.getText()),
                                ItemUtils.parseInt(this.areaDimensionsYField.getText()),
                                ItemUtils.parseInt(this.areaDimensionsZField.getText())
                        ),
                        new BlockPos(
                                ItemUtils.parseInt(this.areaPositionOffsetXField.getText()),
                                ItemUtils.parseInt(this.areaPositionOffsetYField.getText()),
                                ItemUtils.parseInt(this.areaPositionOffsetZField.getText())
                        ),
                        this.triggeredBlocks,
                        this.triggerMode.asString(),
                        ItemUtils.parseInt(this.triggerAmountField.getText())
                )
        );
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        if (this.selectionMode == RelayTriggerBlockEntity.SelectionMode.AREA) {

            context.drawTextWithShadow(this.textRenderer, AREA_DIMENSIONS_LABEL_TEXT, this.width / 2 - 153, 79, 0xA0A0A0);
            this.areaDimensionsXField.render(context, mouseX, mouseY, delta);
            this.areaDimensionsYField.render(context, mouseX, mouseY, delta);
            this.areaDimensionsZField.render(context, mouseX, mouseY, delta);

            context.drawTextWithShadow(this.textRenderer, AREA_POSITION_OFFET_LABEL_TEXT, this.width / 2 - 153, 114, 0xA0A0A0);
            this.areaPositionOffsetXField.render(context, mouseX, mouseY, delta);
            this.areaPositionOffsetYField.render(context, mouseX, mouseY, delta);
            this.areaPositionOffsetZField.render(context, mouseX, mouseY, delta);

        } else if (this.selectionMode == RelayTriggerBlockEntity.SelectionMode.LIST) {
            for (int i = this.scrollPosition; i < Math.min(this.scrollPosition + 3, this.triggeredBlocks.size()); i++) {
                context.drawTextWithShadow(this.textRenderer, this.triggeredBlocks.get(i).toString(), this.width / 2 - 141, 50 + ((i - this.scrollPosition) * 24), 0xA0A0A0);
            }
            if (this.triggeredBlocks.size() > 3) {
//            context.drawGuiTexture(TRIGGERED_BLOCKS_LIST_SCROLLER_BACKGROUND_TEXTURE, this.width / 2 - 153, 20, 8, 116);
                context.drawTexture(TRIGGERED_BLOCKS_LIST_SCROLLER_BACKGROUND_TEXTURE, this.width / 2 - 153, 44, 0, 0, 8, 92);
                int k = (int) (83.0f * this.scrollAmount);
//            context.drawGuiTexture(SCROLLER_TEXTURE, this.width / 2 - 152, 20 + 1 + k, 6, 7);
                context.drawTexture(SCROLLER_TEXTURE, this.width / 2 - 152, 20 + 1 + k, 0, 0, 6, 7);
            }
            context.drawTextWithShadow(this.textRenderer, NEW_TRIGGERED_BLOCK_POSITION_TEXT, this.width / 2 - 153, 116, 0xA0A0A0);
            this.newTriggeredBlockPositionOffsetXField.render(context, mouseX, mouseY, delta);
            this.newTriggeredBlockPositionOffsetYField.render(context, mouseX, mouseY, delta);
            this.newTriggeredBlockPositionOffsetZField.render(context, mouseX, mouseY, delta);

            context.drawTextWithShadow(this.textRenderer, NEW_POSITION_CHANCE_FIELD_LABEL_TEXT, this.width / 2 - 40, 159, 0xA0A0A0);
            this.newTriggeredBlockChanceField.render(context, mouseX, mouseY, delta);

            this.triggerAmountField.render(context, mouseX, mouseY, delta);
        }
    }

    @Override
    public boolean shouldPause() {
        return false;
    }
}
