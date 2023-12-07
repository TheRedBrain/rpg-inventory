package com.github.theredbrain.bamcore.client.gui.screen.ingame;

import com.github.theredbrain.bamcore.BetterAdventureModeCore;
import com.github.theredbrain.bamcore.block.entity.RelayTriggerBlockBlockEntity;
import com.github.theredbrain.bamcore.network.packet.UpdateRelayTriggerBlockPacket;
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
public class RelayTriggerBlockScreen extends Screen {
    private static final Text NEW_TRIGGERED_BLOCK_POSITION_TEXT = Text.translatable("gui.triggered_block.newTriggeredBlockPositionOffset");
    private static final Text NEW_POSITION_X_FIELD_PLACEHOLDER_TEXT = Text.translatable("gui.triggered_block.triggeredBlockPositionOffsetX.placeholder");
    private static final Text NEW_POSITION_Y_FIELD_PLACEHOLDER_TEXT = Text.translatable("gui.triggered_block.triggeredBlockPositionOffsetY.placeholder");
    private static final Text NEW_POSITION_Z_FIELD_PLACEHOLDER_TEXT = Text.translatable("gui.triggered_block.triggeredBlockPositionOffsetZ.placeholder");
    private static final Text REMOVE_BUTTON_LABEL_TEXT = Text.translatable("gui.remove");
    private static final Text ADD_BUTTON_LABEL_TEXT = Text.translatable("gui.add");
    private static final Identifier TRIGGERED_BLOCKS_LIST_SCROLLER_BACKGROUND_TEXTURE = BetterAdventureModeCore.identifier("scroll_bar/scroll_bar_background_8_116");
    private static final Identifier SCROLLER_TEXTURE = BetterAdventureModeCore.identifier("scroll_bar/scroller_vertical_6_7");
    private final RelayTriggerBlockBlockEntity relayTriggerBlock;
    private ButtonWidget removeListEntryButton0;
    private ButtonWidget removeListEntryButton1;
    private ButtonWidget removeListEntryButton2;
    private ButtonWidget removeListEntryButton3;
    private ButtonWidget removeListEntryButton4;
    private TextFieldWidget newTriggeredBlockPositionOffsetXField;
    private TextFieldWidget newTriggeredBlockPositionOffsetYField;
    private TextFieldWidget newTriggeredBlockPositionOffsetZField;
    private ButtonWidget addNewTriggeredBlockPositionOffsetButton;
    private ButtonWidget saveButton;
    private ButtonWidget cancelButton;
    private List<BlockPos> triggeredBlocks = new ArrayList<>(List.of());
    private int scrollPosition = 0;
    private float scrollAmount = 0.0f;
    private boolean mouseClicked = false;

    public RelayTriggerBlockScreen(RelayTriggerBlockBlockEntity relayTriggerBlock) {
        super(NarratorManager.EMPTY);
        this.relayTriggerBlock = relayTriggerBlock;
    }

    private void addTriggeredBlockEntry(BlockPos newTriggeredBlock) {
        for (BlockPos blockPos : this.triggeredBlocks) {
            if (blockPos.equals(newTriggeredBlock)) {
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
        this.close();
    }

    @Override
    protected void init() {
        this.triggeredBlocks.clear();
        this.triggeredBlocks.addAll(this.relayTriggerBlock.getTriggeredBlocks());
        this.removeListEntryButton0 = this.addDrawableChild(ButtonWidget.builder(REMOVE_BUTTON_LABEL_TEXT, button -> this.deleteTriggeredBlockEntry(0)).dimensions(this.width / 2 + 54, 20, 100, 20).build());
        this.removeListEntryButton1 = this.addDrawableChild(ButtonWidget.builder(REMOVE_BUTTON_LABEL_TEXT, button -> this.deleteTriggeredBlockEntry(1)).dimensions(this.width / 2 + 54, 44, 100, 20).build());
        this.removeListEntryButton2 = this.addDrawableChild(ButtonWidget.builder(REMOVE_BUTTON_LABEL_TEXT, button -> this.deleteTriggeredBlockEntry(2)).dimensions(this.width / 2 + 54, 68, 100, 20).build());
        this.removeListEntryButton3 = this.addDrawableChild(ButtonWidget.builder(REMOVE_BUTTON_LABEL_TEXT, button -> this.deleteTriggeredBlockEntry(3)).dimensions(this.width / 2 + 54, 92, 100, 20).build());
        this.removeListEntryButton4 = this.addDrawableChild(ButtonWidget.builder(REMOVE_BUTTON_LABEL_TEXT, button -> this.deleteTriggeredBlockEntry(4)).dimensions(this.width / 2 + 54, 116, 100, 20).build());
        this.newTriggeredBlockPositionOffsetXField = new TextFieldWidget(this.textRenderer, this.width / 2 - 154, 153, 100, 20, Text.translatable(""));
        this.newTriggeredBlockPositionOffsetXField.setPlaceholder(NEW_POSITION_X_FIELD_PLACEHOLDER_TEXT);
        this.addSelectableChild(this.newTriggeredBlockPositionOffsetXField);
        this.newTriggeredBlockPositionOffsetYField = new TextFieldWidget(this.textRenderer, this.width / 2 - 50, 153, 100, 20, Text.translatable(""));
        this.newTriggeredBlockPositionOffsetYField.setPlaceholder(NEW_POSITION_Y_FIELD_PLACEHOLDER_TEXT);
        this.addSelectableChild(this.newTriggeredBlockPositionOffsetYField);
        this.newTriggeredBlockPositionOffsetZField = new TextFieldWidget(this.textRenderer, this.width / 2 + 54, 153, 100, 20, Text.translatable(""));
        this.newTriggeredBlockPositionOffsetZField.setPlaceholder(NEW_POSITION_Z_FIELD_PLACEHOLDER_TEXT);
        this.addSelectableChild(this.newTriggeredBlockPositionOffsetZField);
        this.addNewTriggeredBlockPositionOffsetButton = this.addDrawableChild(ButtonWidget.builder(ADD_BUTTON_LABEL_TEXT, button -> this.addTriggeredBlockEntry(new BlockPos(parseInt(this.newTriggeredBlockPositionOffsetXField.getText()), parseInt(this.newTriggeredBlockPositionOffsetYField.getText()), parseInt(this.newTriggeredBlockPositionOffsetZField.getText())))).dimensions(this.width / 2 - 4 - 150, 177, 300, 20).build());
        this.saveButton = this.addDrawableChild(ButtonWidget.builder(ScreenTexts.DONE, button -> this.done()).dimensions(this.width / 2 - 4 - 150, 201, 150, 20).build());
        this.cancelButton = this.addDrawableChild(ButtonWidget.builder(ScreenTexts.CANCEL, button -> this.cancel()).dimensions(this.width / 2 + 4, 201, 150, 20).build());
        this.setInitialFocus(this.newTriggeredBlockPositionOffsetXField);
        this.updateWidgets();
    }

    private void updateWidgets() {
        this.removeListEntryButton0.visible = false;
        this.removeListEntryButton1.visible = false;
        this.removeListEntryButton2.visible = false;
        this.removeListEntryButton3.visible = false;
        this.removeListEntryButton4.visible = false;

        this.newTriggeredBlockPositionOffsetXField.setVisible(false);
        this.newTriggeredBlockPositionOffsetYField.setVisible(false);
        this.newTriggeredBlockPositionOffsetZField.setVisible(false);

        this.addNewTriggeredBlockPositionOffsetButton.visible = false;
        this.saveButton.visible = false;
        this.cancelButton.visible = false;
        
        int index = 0;
        for (int i = 0; i < Math.min(5, this.triggeredBlocks.size()); i++) {
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

        this.newTriggeredBlockPositionOffsetXField.setVisible(true);
        this.newTriggeredBlockPositionOffsetYField.setVisible(true);
        this.newTriggeredBlockPositionOffsetZField.setVisible(true);

        this.addNewTriggeredBlockPositionOffsetButton.visible = true;
        this.saveButton.visible = true;
        this.cancelButton.visible = true;
    }

    @Override
    public void resize(MinecraftClient client, int width, int height) {
        List<BlockPos> list = new ArrayList<>(this.triggeredBlocks);
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
        if (this.triggeredBlocks.size() > 5) {
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
        if (this.triggeredBlocks.size() > 5
                && this.mouseClicked) {
            int i = this.triggeredBlocks.size() - 5;
            float f = (float)deltaY / (float)i;
            this.scrollAmount = MathHelper.clamp(this.scrollAmount + f, 0.0f, 1.0f);
            this.scrollPosition = (int)((double)(this.scrollAmount * (float)i));
        }
        return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        if (this.triggeredBlocks.size() > 5
                && mouseX >= (double)(this.width / 2 - 152) && mouseX <= (double)(this.width / 2 + 50)
                && mouseY >= 20 && mouseY <= 135) {
            int i = this.triggeredBlocks.size() - 5;
            float f = (float)verticalAmount / (float)i;
            this.scrollAmount = MathHelper.clamp(this.scrollAmount - f, 0.0f, 1.0f);
            this.scrollPosition = (int)((double)(this.scrollAmount * (float)i));
        }
        return super.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
    }

//    @Override
//    protected @NotNull OwoUIAdapter<FlowLayout> createAdapter() {
//        return OwoUIAdapter.create(this, Containers::verticalFlow);
//    }
//
//    @Override
//    protected void build(FlowLayout rootComponent) {
//            List<BlockPos> triggeredBlocks = this.relayTriggerBlock.getTriggeredBlocks();
//            rootComponent
//                    .surface(Surface.VANILLA_TRANSLUCENT)
//                    .horizontalAlignment(HorizontalAlignment.CENTER)
//                    .verticalAlignment(VerticalAlignment.CENTER);
//            rootComponent.children(List.of(
//                    Components.label(this.title)
//                            .shadow(true)
//                            .color(Color.ofArgb(0xFFFFFF))
//                            .margins(Insets.of(1, 1, 1, 1))
//                            .sizing(Sizing.content(), Sizing.content()),
//                    // scrolling container
//                    Containers.verticalScroll(Sizing.fill(50), Sizing.fill(50),
//                            Containers.verticalFlow(Sizing.fill(98), Sizing.content())
//                                    .verticalAlignment(VerticalAlignment.CENTER)
//                                    .horizontalAlignment(HorizontalAlignment.CENTER)
//                                    .id("creativeScreenScrollingContent"))
//                            .scrollbar(ScrollContainer.Scrollbar.vanilla())
//                            .verticalAlignment(VerticalAlignment.CENTER)
//                            .horizontalAlignment(HorizontalAlignment.LEFT),
//                    Components.button(Text.translatable("gui.relay_trigger_block.addTriggeredBlockEntryButton"), button -> this.addTriggeredBlockEntry())
//                            .sizing(Sizing.fill(30), Sizing.fixed(20)),
//                    Containers.horizontalFlow(Sizing.fill(30), Sizing.content())
//                            .children(List.of(
//                                    Components.button(Text.translatable("gui.save"), button -> this.done())
//                                            .sizing(Sizing.fill(49), Sizing.fixed(20)),
//                                    Components.button(ScreenTexts.CANCEL, button -> this.cancel())
//                                            .sizing(Sizing.fill(49), Sizing.fixed(20))
//                            ))
//                            .margins(Insets.of(1, 1, 1, 1))
//                            .verticalAlignment(VerticalAlignment.CENTER)
//                            .horizontalAlignment(HorizontalAlignment.CENTER)
//            ));
//            for (BlockPos triggeredBlock : triggeredBlocks) {
//                    rootComponent.childById(FlowLayout.class, "creativeScreenScrollingContent").children(List.of(
//                            Containers.horizontalFlow(Sizing.fill(100), Sizing.content())
//                                    .children(List.of(
//                                            Components.textBox(Sizing.fill(17), Integer.toString(triggeredBlock.getX()))
//                                                    .tooltip(Text.translatable("gui.triggered_block.triggeredBlockPositionOffsetX.tooltip"))
//                                                    .margins(Insets.of(1, 1, 1, 1))
//                                                    .id("triggeredBlockPositionOffset"),
//                                            Components.textBox(Sizing.fill(17), Integer.toString(triggeredBlock.getY()))
//                                                    .tooltip(Text.translatable("gui.triggered_block.triggeredBlockPositionOffsetY.tooltip"))
//                                                    .margins(Insets.of(1, 1, 1, 1))
//                                                    .id("triggeredBlockPositionOffsetY"),
//                                            Components.textBox(Sizing.fill(17), Integer.toString(triggeredBlock.getZ()))
//                                                    .tooltip(Text.translatable("gui.triggered_block.triggeredBlockPositionOffsetZ.tooltip"))
//                                                    .margins(Insets.of(1, 1, 1, 1))
//                                                    .id("triggeredBlockPositionOffsetZ"),
//                                            Components.button(Text.translatable("gui.relay_trigger_block.deleteTriggeredBlockEntryButton"), button -> this.deleteTriggeredBlockEntry(button.parent()))
//                                                    .sizing(Sizing.fill(25), Sizing.fixed(20))
//                                                    .id("deleteTriggeredBlockEntryButton")
//                                    ))
//                                    .verticalAlignment(VerticalAlignment.CENTER)
//                                    .horizontalAlignment(HorizontalAlignment.LEFT)
//                    ));
//            }
//    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == GLFW.GLFW_KEY_ENTER || keyCode == GLFW.GLFW_KEY_KP_ENTER) {
            this.done();
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    private void updateRelayTriggerBlock() {
//        List<Component> list = component(FlowLayout.class, "creativeScreenScrollingContent").children();
//
//        int creativeScreenScrollingContentSize = list.size();
//
//        List<BlockPos> triggeredBlocks = new ArrayList<>();
//
//        for (int i = 0; i < creativeScreenScrollingContentSize; i++) {
//            FlowLayout triggeredBlockEntry = (FlowLayout) list.get(i);
//
//            triggeredBlocks.add(new BlockPos(
//                    this.parseInt(((TextBoxComponent)(triggeredBlockEntry.children().get(0))).getText()),
//                    this.parseInt(((TextBoxComponent)(triggeredBlockEntry.children().get(1))).getText()),
//                    this.parseInt(((TextBoxComponent)(triggeredBlockEntry.children().get(2))).getText())
//            ));
//        }
        ClientPlayNetworking.send(new UpdateRelayTriggerBlockPacket(this.relayTriggerBlock.getPos(), this.triggeredBlocks));
//        return true;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        for (int i = this.scrollPosition; i < Math.min(this.scrollPosition + 5, this.triggeredBlocks.size()); i++) {
            context.drawTextWithShadow(this.textRenderer, this.triggeredBlocks.get(i).toString(), this.width / 2 - 141, 26 + ((i - this.scrollPosition) * 24), 0xA0A0A0);
        }
        if (this.triggeredBlocks.size() > 5) {
            context.drawGuiTexture(TRIGGERED_BLOCKS_LIST_SCROLLER_BACKGROUND_TEXTURE, this.width / 2 - 153, 20, 8, 116);
            int k = (int)(107.0f * this.scrollAmount);
            context.drawGuiTexture(SCROLLER_TEXTURE, this.width / 2 - 152, 20 + 1 + k, 6, 7);
        }
        context.drawTextWithShadow(this.textRenderer, NEW_TRIGGERED_BLOCK_POSITION_TEXT, this.width / 2 - 153, 140, 0xA0A0A0);
        this.newTriggeredBlockPositionOffsetXField.render(context, mouseX, mouseY, delta);
        this.newTriggeredBlockPositionOffsetYField.render(context, mouseX, mouseY, delta);
        this.newTriggeredBlockPositionOffsetZField.render(context, mouseX, mouseY, delta);
    }

    private int parseInt(String string) {
        try {
            return Integer.parseInt(string);
        } catch (NumberFormatException numberFormatException) {
            return 0;
        }
    }
}
