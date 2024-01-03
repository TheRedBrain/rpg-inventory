package com.github.theredbrain.bamcore.client.gui.screen.ingame;

import com.github.theredbrain.bamcore.BetterAdventureMode;
import com.github.theredbrain.bamcore.block.entity.LocationControlBlockEntity;
import com.github.theredbrain.bamcore.network.packet.UpdateLocationControlBlockPacket;
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
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import org.apache.commons.lang3.tuple.MutablePair;
import org.lwjgl.glfw.GLFW;

import java.util.*;

@Environment(EnvType.CLIENT)
public class LocationControlBlockScreen extends Screen {
    private static final Text MAIN_ENTRANCE_POSITION_OFFET_LABEL_TEXT = Text.translatable("gui.location_controller_block.main_entrance.position_offset");
    private static final Text MAIN_ENTRANCE_ORIENTATION_LABEL_TEXT = Text.translatable("gui.location_controller_block.main_entrance.orientation");
    private static final Text REMOVE_LIST_ENTRY_BUTTON_LABEL_TEXT = Text.translatable("gui.housing_screen.remove_list_entry_button_label");
    private static final Text NEW_SIDE_ENTRANCE_POSITION_OFFSET_LABEL_TEXT = Text.translatable("gui.location_controller_block.new_side_entrance.position_offset");
    private static final Text NEW_SIDE_ENTRANCE_NAME_LABEL_TEXT = Text.translatable("gui.location_controller_block.new_side_entrance.name");
    private static final Text NEW_SIDE_ENTRANCE_ORIENTATION_LABEL_TEXT = Text.translatable("gui.location_controller_block.new_side_entrance.orientation");
    private static final Text TRIGGERED_BLOCK_POSITION_OFFSET_LABEL_TEXT = Text.translatable("gui.triggered_block.triggeredBlockPositionOffset");
    private static final Identifier SCROLL_BAR_BACKGROUND_8_70_TEXTURE = BetterAdventureMode.identifier("scroll_bar/scroll_bar_background_8_70");
    private static final Identifier SCROLLER_TEXTURE = BetterAdventureMode.identifier("scroll_bar/scroller_vertical_6_7");
    private final LocationControlBlockEntity locationControlBlock;
    private CyclingButtonWidget<ScreenPage> creativeScreenPageButton;
    private TextFieldWidget mainEntrancePositionOffsetXField;
    private TextFieldWidget mainEntrancePositionOffsetYField;
    private TextFieldWidget mainEntrancePositionOffsetZField;
    private TextFieldWidget mainEntranceOrientationYawField;
    private TextFieldWidget mainEntranceOrientationPitchField;
    private ButtonWidget removeSideEntranceButton0;
    private ButtonWidget removeSideEntranceButton1;
    private ButtonWidget removeSideEntranceButton2;
    private TextFieldWidget newSideEntrancePositionOffsetXField;
    private TextFieldWidget newSideEntrancePositionOffsetYField;
    private TextFieldWidget newSideEntrancePositionOffsetZField;
    private TextFieldWidget newSideEntranceOrientationYawField;
    private TextFieldWidget newSideEntranceOrientationPitchField;
    private TextFieldWidget newSideEntranceNameField;
    private ButtonWidget addNewSideEntranceButton;
    private TextFieldWidget triggeredBlockPositionOffsetXField;
    private TextFieldWidget triggeredBlockPositionOffsetYField;
    private TextFieldWidget triggeredBlockPositionOffsetZField;
    private ButtonWidget saveButton;
    private ButtonWidget cancelButton;
    private ScreenPage screenPage;
    private List<MutablePair<String, MutablePair<BlockPos, MutablePair<Double, Double>>>> sideEntranceList = new ArrayList<>();
    private int scrollPosition = 0;
    private float scrollAmount = 0.0f;
    private boolean mouseClicked = false;

    public LocationControlBlockScreen(LocationControlBlockEntity locationControlBlock) {
        super(NarratorManager.EMPTY);
        this.locationControlBlock = locationControlBlock;
        this.screenPage = ScreenPage.MAIN_ENTRANCE;
    }

    private void addNewSideEntrance() {
        String newEntranceName = this.newSideEntranceNameField.getText();
        if (newEntranceName.equals("")) {
            return;
        }
        boolean bl = true;
        for (MutablePair<String, MutablePair<BlockPos, MutablePair<Double, Double>>> stringMutablePairMutablePair : this.sideEntranceList) {
            if (stringMutablePairMutablePair.getLeft().equals(newEntranceName)) {
                bl = false;
                break;
            }
        }
        if (bl) {
            this.sideEntranceList.add(new MutablePair<>(
                    this.newSideEntranceNameField.getText(),
                    new MutablePair<>(
                            new BlockPos(
                                    this.parseInt(this.mainEntrancePositionOffsetXField.getText()),
                                    this.parseInt(this.mainEntrancePositionOffsetYField.getText()),
                                    this.parseInt(this.mainEntrancePositionOffsetZField.getText())
                            ),
                            new MutablePair<>(
                                    this.parseDouble(this.mainEntranceOrientationYawField.getText()),
                                    this.parseDouble(this.mainEntranceOrientationPitchField.getText())
                            )
                    )
            ));
            this.scrollPosition = 0;
            this.scrollAmount = 0.0f;
            this.updateWidgets();
        } else if (this.client != null && this.client.player != null) {
            this.client.player.sendMessage(Text.translatable("gui.teleporter_block.location_already_in_list"));
        }
    }

    private void removeSideEntrance(int index) {
        this.sideEntranceList.remove(index + this.scrollPosition);
        this.scrollPosition = 0;
        this.scrollAmount = 0.0f;
        this.updateWidgets();
    }

    private void done() {
        if (this.updateLocationControlBlock()) {
            this.close();
        }
    }

    private void cancel() {
        this.close();
    }

    @Override
    protected void init() {
        this.sideEntranceList.clear();
        List<String> keyList = new ArrayList<>(this.locationControlBlock.getSideEntrances().keySet());
        for (String key : keyList) {
            this.sideEntranceList.add(new MutablePair<>(key, this.locationControlBlock.getSideEntrances().get(key)));
        }
        super.init();

        this.creativeScreenPageButton = this.addDrawableChild(CyclingButtonWidget.builder(ScreenPage::asText).values((ScreenPage[]) ScreenPage.values()).initially(this.screenPage).omitKeyText().build(this.width / 2 - 154, 18, 308, 20, Text.empty(), (button, screenPage) -> {
            this.screenPage = screenPage;
            this.updateWidgets();
        }));

        // --- main entrance page ---

        this.mainEntrancePositionOffsetXField = new TextFieldWidget(this.textRenderer, this.width / 2 - 154, 80, 100, 20, Text.empty());
        this.mainEntrancePositionOffsetXField.setMaxLength(128);
        this.mainEntrancePositionOffsetXField.setText(Integer.toString(this.locationControlBlock.getMainEntrance().getLeft().getX()));
        this.addSelectableChild(this.mainEntrancePositionOffsetXField);

        this.mainEntrancePositionOffsetYField = new TextFieldWidget(this.textRenderer, this.width / 2 - 50, 80, 100, 20, Text.empty());
        this.mainEntrancePositionOffsetYField.setMaxLength(128);
        this.mainEntrancePositionOffsetYField.setText(Integer.toString(this.locationControlBlock.getMainEntrance().getLeft().getY()));
        this.addSelectableChild(this.mainEntrancePositionOffsetYField);

        this.mainEntrancePositionOffsetZField = new TextFieldWidget(this.textRenderer, this.width / 2 + 54, 80, 100, 20, Text.empty());
        this.mainEntrancePositionOffsetZField.setMaxLength(128);
        this.mainEntrancePositionOffsetZField.setText(Integer.toString(this.locationControlBlock.getMainEntrance().getLeft().getZ()));
        this.addSelectableChild(this.mainEntrancePositionOffsetZField);

        this.mainEntranceOrientationYawField = new TextFieldWidget(this.textRenderer, this.width / 2 - 154, 115, 100, 20, Text.empty());
        this.mainEntranceOrientationYawField.setMaxLength(128);
        this.mainEntranceOrientationYawField.setText(Double.toString(this.locationControlBlock.getMainEntrance().getRight().getLeft()));
        this.addSelectableChild(this.mainEntranceOrientationYawField);

        this.mainEntranceOrientationPitchField = new TextFieldWidget(this.textRenderer, this.width / 2 - 50, 115, 100, 20, Text.empty());
        this.mainEntranceOrientationPitchField.setMaxLength(128);
        this.mainEntranceOrientationPitchField.setText(Double.toString(this.locationControlBlock.getMainEntrance().getRight().getRight()));
        this.addSelectableChild(this.mainEntranceOrientationPitchField);

        // --- side entrances page ---

        this.removeSideEntranceButton0 = this.addDrawableChild(ButtonWidget.builder(REMOVE_LIST_ENTRY_BUTTON_LABEL_TEXT, button -> this.removeSideEntrance(0)).dimensions(this.width / 2 + 104, 44, 50, 20).build());
        this.removeSideEntranceButton1 = this.addDrawableChild(ButtonWidget.builder(REMOVE_LIST_ENTRY_BUTTON_LABEL_TEXT, button -> this.removeSideEntrance(1)).dimensions(this.width / 2 + 104, 68, 50, 20).build());
        this.removeSideEntranceButton2 = this.addDrawableChild(ButtonWidget.builder(REMOVE_LIST_ENTRY_BUTTON_LABEL_TEXT, button -> this.removeSideEntrance(2)).dimensions(this.width / 2 + 104, 92, 50, 20).build());

        this.newSideEntrancePositionOffsetXField = new TextFieldWidget(this.textRenderer, this.width / 2 - 154, 127, 100, 20, Text.empty());
        this.newSideEntrancePositionOffsetXField.setMaxLength(128);
        this.newSideEntrancePositionOffsetXField.setText(Integer.toString(this.locationControlBlock.getMainEntrance().getLeft().getX()));
        this.addSelectableChild(this.newSideEntrancePositionOffsetXField);

        this.newSideEntrancePositionOffsetYField = new TextFieldWidget(this.textRenderer, this.width / 2 - 50, 127, 100, 20, Text.empty());
        this.newSideEntrancePositionOffsetYField.setMaxLength(128);
        this.newSideEntrancePositionOffsetYField.setText(Integer.toString(this.locationControlBlock.getMainEntrance().getLeft().getY()));
        this.addSelectableChild(this.newSideEntrancePositionOffsetYField);

        this.newSideEntrancePositionOffsetZField = new TextFieldWidget(this.textRenderer, this.width / 2 + 54, 127, 100, 20, Text.empty());
        this.newSideEntrancePositionOffsetZField.setMaxLength(128);
        this.newSideEntrancePositionOffsetZField.setText(Integer.toString(this.locationControlBlock.getMainEntrance().getLeft().getZ()));
        this.addSelectableChild(this.newSideEntrancePositionOffsetZField);

        this.newSideEntranceNameField = new TextFieldWidget(this.textRenderer, this.width / 2 - 154, 162, 204, 20, Text.empty());
        this.newSideEntranceNameField.setMaxLength(128);
        this.newSideEntranceNameField.setText(Double.toString(this.locationControlBlock.getMainEntrance().getRight().getRight()));
        this.addSelectableChild(this.newSideEntranceNameField);

        this.newSideEntranceOrientationYawField = new TextFieldWidget(this.textRenderer, this.width / 2 + 54, 162, 48, 20, Text.empty());
        this.newSideEntranceOrientationYawField.setMaxLength(128);
        this.newSideEntranceOrientationYawField.setText(Double.toString(this.locationControlBlock.getMainEntrance().getRight().getLeft()));
        this.addSelectableChild(this.newSideEntranceOrientationYawField);

        this.newSideEntranceOrientationPitchField = new TextFieldWidget(this.textRenderer, this.width / 2 + 106, 162, 48, 20, Text.empty());
        this.newSideEntranceOrientationPitchField.setMaxLength(128);
        this.newSideEntranceOrientationPitchField.setText(Double.toString(this.locationControlBlock.getMainEntrance().getRight().getRight()));
        this.addSelectableChild(this.newSideEntranceOrientationPitchField);

        this.addNewSideEntranceButton = this.addDrawableChild(ButtonWidget.builder(ScreenTexts.DONE, button -> this.addNewSideEntrance()).dimensions(this.width / 2 - 4 - 150, 186, 308, 20).build());

        // --- triggered block page ---

        this.triggeredBlockPositionOffsetXField = new TextFieldWidget(this.textRenderer, this.width / 2 - 154, 80, 100, 20, Text.empty());
        this.triggeredBlockPositionOffsetXField.setMaxLength(128);
        this.triggeredBlockPositionOffsetXField.setText(Integer.toString(this.locationControlBlock.getTriggeredBlockPositionOffset().getX()));
        this.addSelectableChild(this.triggeredBlockPositionOffsetXField);

        this.triggeredBlockPositionOffsetYField = new TextFieldWidget(this.textRenderer, this.width / 2 - 50, 80, 100, 20, Text.empty());
        this.triggeredBlockPositionOffsetYField.setMaxLength(128);
        this.triggeredBlockPositionOffsetYField.setText(Integer.toString(this.locationControlBlock.getTriggeredBlockPositionOffset().getY()));
        this.addSelectableChild(this.triggeredBlockPositionOffsetYField);

        this.triggeredBlockPositionOffsetZField = new TextFieldWidget(this.textRenderer, this.width / 2 + 54, 80, 100, 20, Text.empty());
        this.triggeredBlockPositionOffsetZField.setMaxLength(128);
        this.triggeredBlockPositionOffsetZField.setText(Integer.toString(this.locationControlBlock.getTriggeredBlockPositionOffset().getZ()));
        this.addSelectableChild(this.triggeredBlockPositionOffsetZField);


        this.saveButton = this.addDrawableChild(ButtonWidget.builder(ScreenTexts.DONE, button -> this.done()).dimensions(this.width / 2 - 4 - 150, 210, 150, 20).build());
        this.cancelButton = this.addDrawableChild(ButtonWidget.builder(ScreenTexts.CANCEL, button -> this.cancel()).dimensions(this.width / 2 + 4, 210, 150, 20).build());

        this.updateWidgets();
    }

    private void updateWidgets() {

        this.creativeScreenPageButton.visible = false;

        this.mainEntrancePositionOffsetXField.setVisible(false);
        this.mainEntrancePositionOffsetYField.setVisible(false);
        this.mainEntrancePositionOffsetZField.setVisible(false);
        this.mainEntranceOrientationYawField.setVisible(false);
        this.mainEntranceOrientationPitchField.setVisible(false);

        this.removeSideEntranceButton0.visible = false;
        this.removeSideEntranceButton1.visible = false;
        this.removeSideEntranceButton2.visible = false;

        this.newSideEntrancePositionOffsetXField.setVisible(false);
        this.newSideEntrancePositionOffsetYField.setVisible(false);
        this.newSideEntrancePositionOffsetZField.setVisible(false);
        this.newSideEntranceOrientationYawField.setVisible(false);
        this.newSideEntranceOrientationPitchField.setVisible(false);
        this.newSideEntranceNameField.setVisible(false);
        this.addNewSideEntranceButton.visible = false;

        this.triggeredBlockPositionOffsetXField.setVisible(false);
        this.triggeredBlockPositionOffsetYField.setVisible(false);
        this.triggeredBlockPositionOffsetZField.setVisible(false);

        this.saveButton.visible = false;
        this.cancelButton.visible = false;

        this.creativeScreenPageButton.visible = true;

        if (this.screenPage == ScreenPage.MAIN_ENTRANCE) {

            this.mainEntrancePositionOffsetXField.setVisible(true);
            this.mainEntrancePositionOffsetYField.setVisible(true);
            this.mainEntrancePositionOffsetZField.setVisible(true);
            this.mainEntranceOrientationYawField.setVisible(true);
            this.mainEntranceOrientationPitchField.setVisible(true);

        } else if (this.screenPage == ScreenPage.SIDE_ENTRANCES) {

            int index = 0;
            for (int i = 0; i < Math.min(3, this.sideEntranceList.size()); i++) {
                if (index == 0) {
                    this.removeSideEntranceButton0.visible = true;
                } else if (index == 1) {
                    this.removeSideEntranceButton1.visible = true;
                } else if (index == 2) {
                    this.removeSideEntranceButton2.visible = true;
                }
                index++;
            }

            this.newSideEntrancePositionOffsetXField.setVisible(true);
            this.newSideEntrancePositionOffsetYField.setVisible(true);
            this.newSideEntrancePositionOffsetZField.setVisible(true);
            this.newSideEntranceOrientationYawField.setVisible(true);
            this.newSideEntranceOrientationPitchField.setVisible(true);
            this.newSideEntranceNameField.setVisible(true);
            this.addNewSideEntranceButton.visible = true;

        } else if (this.screenPage == ScreenPage.TRIGGERED_BLOCK) {

            this.triggeredBlockPositionOffsetXField.setVisible(true);
            this.triggeredBlockPositionOffsetYField.setVisible(true);
            this.triggeredBlockPositionOffsetZField.setVisible(true);

        }

        this.saveButton.visible = true;
        this.cancelButton.visible = true;

    }

    @Override
    public void resize(MinecraftClient client, int width, int height) {
        List<MutablePair<String, MutablePair<BlockPos, MutablePair<Double, Double>>>> list = new ArrayList<>(this.sideEntranceList);
        ScreenPage var = this.screenPage;
        int number = this.scrollPosition;
        float number1 =  this.scrollAmount;
        String string = this.mainEntrancePositionOffsetXField.getText();
        String string1 = this.mainEntrancePositionOffsetYField.getText();
        String string2 = this.mainEntrancePositionOffsetZField.getText();
        String string3 = this.mainEntranceOrientationYawField.getText();
        String string4 = this.mainEntranceOrientationPitchField.getText();
        String string5 = this.newSideEntrancePositionOffsetXField.getText();
        String string6 = this.newSideEntrancePositionOffsetYField.getText();
        String string7 = this.newSideEntrancePositionOffsetZField.getText();
        String string8 = this.newSideEntranceOrientationYawField.getText();
        String string9 = this.newSideEntranceOrientationPitchField.getText();
        String string10 = this.newSideEntranceNameField.getText();
        String string11 = this.triggeredBlockPositionOffsetXField.getText();
        String string12 = this.triggeredBlockPositionOffsetYField.getText();
        String string13 = this.triggeredBlockPositionOffsetZField.getText();
        this.init(client, width, height);
        this.sideEntranceList.clear();
        this.sideEntranceList.addAll(list);
        this.screenPage = var;
        this.scrollPosition = number;
        this.scrollAmount = number1;
        this.mainEntrancePositionOffsetXField.setText(string);
        this.mainEntrancePositionOffsetYField.setText(string1);
        this.mainEntrancePositionOffsetZField.setText(string2);
        this.mainEntranceOrientationYawField.setText(string3);
        this.mainEntranceOrientationPitchField.setText(string4);
        this.newSideEntrancePositionOffsetXField.setText(string5);
        this.newSideEntrancePositionOffsetYField.setText(string6);
        this.newSideEntrancePositionOffsetZField.setText(string7);
        this.newSideEntranceOrientationYawField.setText(string8);
        this.newSideEntranceOrientationPitchField.setText(string9);
        this.newSideEntranceNameField.setText(string10);
        this.triggeredBlockPositionOffsetXField.setText(string11);
        this.triggeredBlockPositionOffsetYField.setText(string12);
        this.triggeredBlockPositionOffsetZField.setText(string13);
        this.updateWidgets();
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        this.mouseClicked = false;
        if (this.screenPage == ScreenPage.SIDE_ENTRANCES
                && this.sideEntranceList.size() > 3) {
            int i = this.width / 2 - 152;
            int j = 45;
            if (mouseX >= (double)i && mouseX < (double)(i + 6) && mouseY >= (double)j && mouseY < (double)(j + 68)) {
                this.mouseClicked = true;
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        if (this.screenPage == ScreenPage.SIDE_ENTRANCES
                && this.sideEntranceList.size() > 3
                && this.mouseClicked) {
            int i = this.sideEntranceList.size() - 3;
            float f = (float)deltaY / (float)i;
            this.scrollAmount = MathHelper.clamp(this.scrollAmount + f, 0.0f, 1.0f);
            this.scrollPosition = (int)((double)(this.scrollAmount * (float)i));
        }
        return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        if (this.screenPage == ScreenPage.SIDE_ENTRANCES
                && this.sideEntranceList.size() > 3
                && mouseX >= (double)(this.width / 2 - 152) && mouseX <= (double)(this.width / 2 + 100)
                && mouseY >= 44 && mouseY <= 114) {
            int i = this.sideEntranceList.size() - 3;
            float f = (float)verticalAmount / (float)i;
            this.scrollAmount = MathHelper.clamp(this.scrollAmount - f, 0.0f, 1.0f);
            this.scrollPosition = (int)((double)(this.scrollAmount * (float)i));
        }
        return super.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if ((keyCode == GLFW.GLFW_KEY_ENTER || keyCode == GLFW.GLFW_KEY_KP_ENTER)) {
            this.done();
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        if (this.screenPage == ScreenPage.MAIN_ENTRANCE) {
            context.drawTextWithShadow(this.textRenderer, MAIN_ENTRANCE_POSITION_OFFET_LABEL_TEXT, this.width / 2 - 153, 70, 0xA0A0A0);
            this.mainEntrancePositionOffsetXField.render(context, mouseX, mouseY, delta);
            this.mainEntrancePositionOffsetYField.render(context, mouseX, mouseY, delta);
            this.mainEntrancePositionOffsetZField.render(context, mouseX, mouseY, delta);
            context.drawTextWithShadow(this.textRenderer, MAIN_ENTRANCE_ORIENTATION_LABEL_TEXT, this.width / 2 - 153, 105, 0xA0A0A0);
            this.mainEntranceOrientationYawField.render(context, mouseX, mouseY, delta);
            this.mainEntranceOrientationPitchField.render(context, mouseX, mouseY, delta);
        } else if (this.screenPage == ScreenPage.SIDE_ENTRANCES) {
            for (int i = this.scrollPosition; i < Math.min(this.scrollPosition + 3, this.sideEntranceList.size()); i++) {
                String text = this.sideEntranceList.get(i).getLeft();
                if (!this.sideEntranceList.get(i).getLeft().equals("")) {
                    text = this.sideEntranceList.get(i).getLeft() + ", " + this.sideEntranceList.get(i).getRight();
                }
                context.drawTextWithShadow(this.textRenderer, text, this.width / 2 - 141, 50 + ((i - this.scrollPosition) * 24), 0xA0A0A0);
            }
            if (this.sideEntranceList.size() > 3) {
                context.drawGuiTexture(SCROLL_BAR_BACKGROUND_8_70_TEXTURE, this.width / 2 - 153, 44, 8, 70);
                int k = (int)(61.0f * this.scrollAmount);
                context.drawGuiTexture(SCROLLER_TEXTURE, this.width / 2 - 152, 44 + 1 + k, 6, 7);
            }
            context.drawTextWithShadow(this.textRenderer, NEW_SIDE_ENTRANCE_POSITION_OFFSET_LABEL_TEXT, this.width / 2 - 153, 116, 0xA0A0A0);
            this.newSideEntrancePositionOffsetXField.render(context, mouseX, mouseY, delta);
            this.newSideEntrancePositionOffsetYField.render(context, mouseX, mouseY, delta);
            this.newSideEntrancePositionOffsetZField.render(context, mouseX, mouseY, delta);
            context.drawTextWithShadow(this.textRenderer, NEW_SIDE_ENTRANCE_NAME_LABEL_TEXT, this.width / 2 - 153, 151, 0xA0A0A0);
            context.drawTextWithShadow(this.textRenderer, NEW_SIDE_ENTRANCE_ORIENTATION_LABEL_TEXT, this.width / 2 + 55, 151, 0xA0A0A0);
            this.newSideEntranceOrientationYawField.render(context, mouseX, mouseY, delta);
            this.newSideEntranceOrientationPitchField.render(context, mouseX, mouseY, delta);
            this.newSideEntranceNameField.render(context, mouseX, mouseY, delta);
        } else if (this.screenPage == ScreenPage.TRIGGERED_BLOCK) {
            context.drawTextWithShadow(this.textRenderer, TRIGGERED_BLOCK_POSITION_OFFSET_LABEL_TEXT, this.width / 2 - 153, 70, 0xA0A0A0);
            this.triggeredBlockPositionOffsetXField.render(context, mouseX, mouseY, delta);
            this.triggeredBlockPositionOffsetYField.render(context, mouseX, mouseY, delta);
            this.triggeredBlockPositionOffsetZField.render(context, mouseX, mouseY, delta);
        }
    }

    @Override
    public boolean shouldPause() {
        return false;
    }

    @Override
    public void renderBackground(DrawContext context, int mouseX, int mouseY, float delta) {
        super.renderBackground(context, mouseX, mouseY, delta);
        this.drawBackground(context, delta, mouseX, mouseY);
    }

    public void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
    }

    private boolean updateLocationControlBlock() {
        ClientPlayNetworking.send(new UpdateLocationControlBlockPacket(
                this.locationControlBlock.getPos(),
                new BlockPos(
                        this.parseInt(this.mainEntrancePositionOffsetXField.getText()),
                        this.parseInt(this.mainEntrancePositionOffsetYField.getText()),
                        this.parseInt(this.mainEntrancePositionOffsetZField.getText())
                ),
                this.parseDouble(this.mainEntranceOrientationYawField.getText()),
                this.parseDouble(this.mainEntranceOrientationPitchField.getText()),
                this.sideEntranceList,
                new BlockPos(
                        this.parseInt(this.triggeredBlockPositionOffsetXField.getText()),
                        this.parseInt(this.triggeredBlockPositionOffsetYField.getText()),
                        this.parseInt(this.triggeredBlockPositionOffsetZField.getText())
                )
        ));
        return true;
    }

    private int parseInt(String string) {
        try {
            return Integer.parseInt(string);
        } catch (NumberFormatException numberFormatException) {
            return 0;
        }
    }

    private double parseDouble(String string) {
        try {
            return Double.parseDouble(string);
        } catch (NumberFormatException numberFormatException) {
            return 0.0;
        }
    }

    public static enum ScreenPage implements StringIdentifiable
    {
        MAIN_ENTRANCE("main_entrance"),
        SIDE_ENTRANCES("side_entrances"),
        TRIGGERED_BLOCK("triggered_block");

        private final String name;

        private ScreenPage(String name) {
            this.name = name;
        }

        @Override
        public String asString() {
            return this.name;
        }

        public static Optional<ScreenPage> byName(String name) {
            return Arrays.stream(ScreenPage.values()).filter(screenPage -> screenPage.asString().equals(name)).findFirst();
        }

        public Text asText() {
            return Text.translatable("gui.location_controller_block.screenPage." + this.name);
        }
    }
}
