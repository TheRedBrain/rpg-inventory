package com.github.theredbrain.betteradventuremode.client.gui.screen.ingame;
//
//import com.github.theredbrain.betteradventuremode.block.entity.TriggeredMessageBlockEntity;
//import com.github.theredbrain.betteradventuremode.network.packet.UpdateTriggeredMessageBlockPacket;
//import com.github.theredbrain.betteradventuremode.util.ItemUtils;
//import net.fabricmc.api.EnvType;
//import net.fabricmc.api.Environment;
//import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
//import net.minecraft.client.MinecraftClient;
//import net.minecraft.client.gui.DrawContext;
//import net.minecraft.client.gui.screen.Screen;
//import net.minecraft.client.gui.widget.ButtonWidget;
//import net.minecraft.client.gui.widget.CyclingButtonWidget;
//import net.minecraft.client.gui.widget.TextFieldWidget;
//import net.minecraft.client.util.NarratorManager;
//import net.minecraft.screen.ScreenTexts;
//import net.minecraft.text.Text;
//import net.minecraft.util.math.BlockPos;
//import net.minecraft.util.math.Vec3i;
//import org.lwjgl.glfw.GLFW;
//
//@Environment(value= EnvType.CLIENT)
//public class TriggeredMessageBlockScreen extends Screen {
//    private static final Text TOGGLE_SHOW_MESSAGE_AREA_BUTTON_LABEL_TEXT_ON = Text.translatable("gui.triggered_message_block.toggle_show_message_area_button_label.on");
//    private static final Text TOGGLE_SHOW_MESSAGE_AREA_BUTTON_LABEL_TEXT_OFF = Text.translatable("gui.triggered_message_block.toggle_show_message_area_button_label.off");
//    private static final Text MESSAGE_AREA_DIMENSIONS_LABEL_TEXT = Text.translatable("gui.triggered_message_block.message_area_dimensions_label");
//    private static final Text MESSAGE_AREA_POSITION_OFFET_LABEL_TEXT = Text.translatable("gui.triggered_message_block.message_area_position_offset_label");
//    private static final Text TOGGLE_OVERLAY_BUTTON_LABEL_TEXT_ON = Text.translatable("gui.triggered_message_block.toggle_overlay_button_label.on");
//    private static final Text TOGGLE_OVERLAY_BUTTON_LABEL_TEXT_OFF = Text.translatable("gui.triggered_message_block.toggle_overlay_button_label.off");
//
//    private final TriggeredMessageBlockEntity triggeredMessageBlock;
//
//    private TextFieldWidget messageAreaDimensionsXField;
//    private TextFieldWidget messageAreaDimensionsYField;
//    private TextFieldWidget messageAreaDimensionsZField;
//    private TextFieldWidget messageAreaPositionOffsetXField;
//    private TextFieldWidget messageAreaPositionOffsetYField;
//    private TextFieldWidget messageAreaPositionOffsetZField;
//
//    private TextFieldWidget messageField;
//
//    private boolean showMessageArea;
//    private boolean overlay;
//    private TriggeredMessageBlockEntity.TriggerMode triggerMode;
//
//    public TriggeredMessageBlockScreen(TriggeredMessageBlockEntity triggeredMessageBlock) {
//        super(NarratorManager.EMPTY);
//        this.triggeredMessageBlock = triggeredMessageBlock;
//    }
//
//    private void done() {
//        this.updateTriggeredMessageBlock();
//        this.close();
//    }
//
//    private void cancel() {
//        this.triggeredMessageBlock.setShowMessageArea(this.showMessageArea);
//        this.triggeredMessageBlock.setOverlay(this.overlay);
//        this.triggeredMessageBlock.setTriggerMode(this.triggerMode);
//        this.close();
//    }
//
//    @Override
//    protected void init() {
//        this.showMessageArea = this.triggeredMessageBlock.getShowMessageArea();
//        this.addDrawableChild(CyclingButtonWidget.onOffBuilder(TOGGLE_SHOW_MESSAGE_AREA_BUTTON_LABEL_TEXT_ON, TOGGLE_SHOW_MESSAGE_AREA_BUTTON_LABEL_TEXT_OFF).initially(this.showMessageArea).omitKeyText().build(this.width / 2 - 154, 45, 300, 20, Text.empty(), (button, showMessageArea) -> {
//            this.showMessageArea = showMessageArea;
//        }));
//
//        this.messageAreaDimensionsXField = new TextFieldWidget(this.textRenderer, this.width / 2 - 154, 80, 100, 20, Text.empty());
//        this.messageAreaDimensionsXField.setMaxLength(128);
//        this.messageAreaDimensionsXField.setText(Integer.toString(this.triggeredMessageBlock.getMessageAreaDimensions().getX()));
//        this.addSelectableChild(this.messageAreaDimensionsXField);
//
//        this.messageAreaDimensionsYField = new TextFieldWidget(this.textRenderer, this.width / 2 - 50, 80, 100, 20, Text.empty());
//        this.messageAreaDimensionsYField.setMaxLength(128);
//        this.messageAreaDimensionsYField.setText(Integer.toString(this.triggeredMessageBlock.getMessageAreaDimensions().getY()));
//        this.addSelectableChild(this.messageAreaDimensionsYField);
//
//        this.messageAreaDimensionsZField = new TextFieldWidget(this.textRenderer, this.width / 2 + 54, 80, 100, 20, Text.empty());
//        this.messageAreaDimensionsZField.setMaxLength(128);
//        this.messageAreaDimensionsZField.setText(Integer.toString(this.triggeredMessageBlock.getMessageAreaDimensions().getZ()));
//        this.addSelectableChild(this.messageAreaDimensionsZField);
//
//        this.messageAreaPositionOffsetXField = new TextFieldWidget(this.textRenderer, this.width / 2 - 154, 115, 100, 20, Text.empty());
//        this.messageAreaPositionOffsetXField.setMaxLength(128);
//        this.messageAreaPositionOffsetXField.setText(Integer.toString(this.triggeredMessageBlock.getMessageAreaPositionOffset().getX()));
//        this.addSelectableChild(this.messageAreaPositionOffsetXField);
//
//        this.messageAreaPositionOffsetYField = new TextFieldWidget(this.textRenderer, this.width / 2 - 50, 115, 100, 20, Text.empty());
//        this.messageAreaPositionOffsetYField.setMaxLength(128);
//        this.messageAreaPositionOffsetYField.setText(Integer.toString(this.triggeredMessageBlock.getMessageAreaPositionOffset().getY()));
//        this.addSelectableChild(this.messageAreaPositionOffsetYField);
//
//        this.messageAreaPositionOffsetZField = new TextFieldWidget(this.textRenderer, this.width / 2 + 54, 115, 100, 20, Text.empty());
//        this.messageAreaPositionOffsetZField.setMaxLength(128);
//        this.messageAreaPositionOffsetZField.setText(Integer.toString(this.triggeredMessageBlock.getMessageAreaPositionOffset().getZ()));
//        this.addSelectableChild(this.messageAreaPositionOffsetZField);
//
//        this.messageField = new TextFieldWidget(this.textRenderer, this.width / 2 - 154, 150, 300, 20, Text.empty());
//        this.messageField.setMaxLength(128);
//        this.messageField.setText(this.triggeredMessageBlock.getMessage());
//        this.addSelectableChild(this.messageField);
//
//        this.overlay = this.triggeredMessageBlock.getOverlay();
//        this.addDrawableChild(CyclingButtonWidget.onOffBuilder(TOGGLE_OVERLAY_BUTTON_LABEL_TEXT_ON, TOGGLE_OVERLAY_BUTTON_LABEL_TEXT_OFF).initially(this.overlay).omitKeyText().build(this.width / 2 - 154, 175, 150, 20, Text.empty(), (button, overlay) -> {
//            this.overlay = overlay;
//        }));
//
//        this.triggerMode = this.triggeredMessageBlock.getTriggerMode();
//        this.addDrawableChild(CyclingButtonWidget.builder(TriggeredMessageBlockEntity.TriggerMode::asText).values((TriggeredMessageBlockEntity.TriggerMode[]) TriggeredMessageBlockEntity.TriggerMode.values()).initially(this.triggerMode).omitKeyText().build(this.width / 2 + 4, 175, 150, 20, Text.empty(), (button, triggerMode) -> {
//            this.triggerMode = triggerMode;
//        }));
//
//        this.addDrawableChild(ButtonWidget.builder(ScreenTexts.DONE, button -> this.done()).dimensions(this.width / 2 - 4 - 150, 212, 150, 20).build());
//        this.addDrawableChild(ButtonWidget.builder(ScreenTexts.CANCEL, button -> this.cancel()).dimensions(this.width / 2 + 4, 212, 150, 20).build());
//        this.setInitialFocus(this.messageAreaDimensionsXField);
//    }
//
//    @Override
//    public void resize(MinecraftClient client, int width, int height) {
////        // TODO
////        List<MutablePair<String, EntityAttributeModifier>> list = this.entityAttributeModifiersList;
////        String string = this.entitySpawnPositionOffsetXField.getText();
////        String string1 = this.entitySpawnPositionOffsetYField.getText();
////        String string2 = this.entitySpawnPositionOffsetZField.getText();
////        String string3 = this.spawningMode.asString();
////        String string4 = this.entityTypeIdField.getText();
////        String string5 = this.triggeredBlockPositionOffsetXField.getText();
////        String string6 = this.triggeredBlockPositionOffsetYField.getText();
////        String string7 = this.triggeredBlockPositionOffsetZField.getText();
//        this.init(client, width, height);
////        this.entityAttributeModifiersList = list;
////        this.entitySpawnPositionOffsetXField.setText(string);
////        this.entitySpawnPositionOffsetYField.setText(string1);
////        this.entitySpawnPositionOffsetZField.setText(string2);
////        this.spawningMode = TriggeredSpawnerBlockEntity.SpawningMode.byName(string3).orElseGet(() -> TriggeredSpawnerBlockEntity.SpawningMode.ONCE);
////        this.entityTypeIdField.setText(string4);
////        this.triggeredBlockPositionOffsetXField.setText(string5);
////        this.triggeredBlockPositionOffsetYField.setText(string6);
////        this.triggeredBlockPositionOffsetZField.setText(string7);
//    }
//
//    @Override
//    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
//        if (keyCode == GLFW.GLFW_KEY_ENTER || keyCode == GLFW.GLFW_KEY_KP_ENTER) {
//            this.done();
//            return true;
//        }
//        return super.keyPressed(keyCode, scanCode, modifiers);
//    }
//
//    private void updateTriggeredMessageBlock() {
//
//        ClientPlayNetworking.send(
//                new UpdateTriggeredMessageBlockPacket(
//                        this.triggeredMessageBlock.getPos(),
//                        this.showMessageArea,
//                        new Vec3i(
//                                ItemUtils.parseInt(this.messageAreaDimensionsXField.getText()),
//                                ItemUtils.parseInt(this.messageAreaDimensionsYField.getText()),
//                                ItemUtils.parseInt(this.messageAreaDimensionsZField.getText())
//                        ),
//                        new BlockPos(
//                                ItemUtils.parseInt(this.messageAreaPositionOffsetXField.getText()),
//                                ItemUtils.parseInt(this.messageAreaPositionOffsetYField.getText()),
//                                ItemUtils.parseInt(this.messageAreaPositionOffsetZField.getText())
//                        ),
//                        this.messageField.getText(),
//                        this.overlay,
//                        this.triggerMode.asString()
//                )
//        );
//    }
//
//    @Override
//    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
//
//        super.render(context, mouseX, mouseY, delta);
//
//        context.drawTextWithShadow(this.textRenderer, MESSAGE_AREA_DIMENSIONS_LABEL_TEXT, this.width / 2 - 153, 70, 0xA0A0A0);
//        this.messageAreaDimensionsXField.render(context, mouseX, mouseY, delta);
//        this.messageAreaDimensionsYField.render(context, mouseX, mouseY, delta);
//        this.messageAreaDimensionsZField.render(context, mouseX, mouseY, delta);
//        context.drawTextWithShadow(this.textRenderer, MESSAGE_AREA_POSITION_OFFET_LABEL_TEXT, this.width / 2 - 153, 105, 0xA0A0A0);
//        this.messageAreaPositionOffsetXField.render(context, mouseX, mouseY, delta);
//        this.messageAreaPositionOffsetYField.render(context, mouseX, mouseY, delta);
//        this.messageAreaPositionOffsetZField.render(context, mouseX, mouseY, delta);
//
//        this.messageField.render(context, mouseX, mouseY, delta);
//    }
//
//    @Override
//    public boolean shouldPause() {
//        return false;
//    }
//}
