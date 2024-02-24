package com.github.theredbrain.betteradventuremode.client.gui.screen.ingame;

import com.github.theredbrain.betteradventuremode.util.ItemUtils;
import com.github.theredbrain.betteradventuremode.block.entity.AreaBlockEntity;
import com.github.theredbrain.betteradventuremode.network.packet.UpdateAreaBlockPacket;
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
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;

import java.util.Arrays;
import java.util.Optional;

@Environment(value= EnvType.CLIENT)
public class AreaBlockScreen extends Screen {
    private static final Text TRIGGERED_TRUE_LABEL_TEXT = Text.translatable("gui.area_block.triggered_true_label");
    private static final Text TRIGGERED_FALSE_LABEL_TEXT = Text.translatable("gui.area_block.triggered_false_label");
    private static final Text HIDE_AREA_LABEL_TEXT = Text.translatable("gui.area_block.hide_area_label");
    private static final Text SHOW_AREA_LABEL_TEXT = Text.translatable("gui.area_block.show_area_label");
    private static final Text AREA_DIMENSIONS_LABEL_TEXT = Text.translatable("gui.area_block.area_dimensions_label");
    private static final Text AREA_POSITION_OFFET_LABEL_TEXT = Text.translatable("gui.area_block.area_position_offset_label");
    private static final Text STATUS_EFFECT_IDENTIFIER_LABEL_TEXT = Text.translatable("gui.area_block.status_effect_identifier_label");
    private static final Text STATUS_EFFECT_AMPLIFIER_LABEL_TEXT = Text.translatable("gui.area_block.status_effect_amplifier_label");
    private static final Text AMBIENT_TRUE_LABEL_TEXT = Text.translatable("gui.area_block.ambient_true_label");
    private static final Text AMBIENT_FALSE_LABEL_TEXT = Text.translatable("gui.area_block.ambient_false_label");
    private static final Text HIDE_PARTICLES_LABEL_TEXT = Text.translatable("gui.area_block.hide_particles_label");
    private static final Text SHOW_PARTICLES_LABEL_TEXT = Text.translatable("gui.area_block.show_particles_label");
    private static final Text HIDE_ICON_LABEL_TEXT = Text.translatable("gui.area_block.hide_icon_label");
    private static final Text SHOW_ICON_LABEL_TEXT = Text.translatable("gui.area_block.show_icon_label");
    private static final Text JOIN_MESSAGE_LABEL_TEXT = Text.translatable("gui.area_block.join_message_label");
    private static final Text LEAVE_MESSAGE_LABEL_TEXT = Text.translatable("gui.area_block.leave_message_label");
    private static final Text TRIGGERED_MESSAGE_LABEL_TEXT = Text.translatable("gui.area_block.triggered_message_label");
    private static final Text TRIGGERED_BLOCK_POSITION_TEXT = Text.translatable("gui.triggered_block.triggeredBlockPositionOffset");
    private static final Text PRE_TIMER_LABEL_TEXT = Text.translatable("gui.area_block.pre_timer_label");
    private static final Text POST_TIMER_LABEL_TEXT = Text.translatable("gui.area_block.post_timer_label");
    private final AreaBlockEntity areaBlock;
    private ScreenPage screenPage;
    private CyclingButtonWidget<Boolean> toggleShowAreaButton;
    private TextFieldWidget areaDimensionsXField;
    private TextFieldWidget areaDimensionsYField;
    private TextFieldWidget areaDimensionsZField;
    private TextFieldWidget areaPositionOffsetXField;
    private TextFieldWidget areaPositionOffsetYField;
    private TextFieldWidget areaPositionOffsetZField;
    private boolean showArea;
    
    private TextFieldWidget appliedStatusEffectIdentifierField;
    private TextFieldWidget appliedStatusEffectAmplifierField;
    private CyclingButtonWidget<Boolean> toggleAppliedStatusEffectAmbientButton;
    private CyclingButtonWidget<Boolean> toggleAppliedStatusEffectShowParticlesButton;
    private CyclingButtonWidget<Boolean> toggleAppliedStatusEffectShowIconButton;
    private boolean appliedStatusEffectAmbient;
    private boolean appliedStatusEffectShowParticles;
    private boolean appliedStatusEffectShowIcon;

    private TextFieldWidget joinMessageField;
    private TextFieldWidget leaveMessageField;
    private TextFieldWidget triggeredMessageField;
    private CyclingButtonWidget<AreaBlockEntity.MessageMode> cycleMessageModeButton;
    private AreaBlockEntity.MessageMode messageMode;

    private TextFieldWidget triggeredBlockPositionOffsetXField;
    private TextFieldWidget triggeredBlockPositionOffsetYField;
    private TextFieldWidget triggeredBlockPositionOffsetZField;
    private CyclingButtonWidget<Boolean> toggleTriggeredBlockResetsButton;
    private CyclingButtonWidget<Boolean> toggleWasTriggeredButton;
    private CyclingButtonWidget<AreaBlockEntity.TriggerMode> cycleTriggerModeButton;
    private CyclingButtonWidget<AreaBlockEntity.TriggeredMode> cycleTriggeredModeButton;
    private TextFieldWidget timerField;
    private boolean wasTriggered;
    private boolean triggeredBlockResets;
    private AreaBlockEntity.TriggerMode triggerMode;
    private AreaBlockEntity.TriggeredMode triggeredMode;
    
    public AreaBlockScreen(AreaBlockEntity areaBlock) {
        super(NarratorManager.EMPTY);
        this.areaBlock = areaBlock;
        this.screenPage = ScreenPage.AREA;
    }

    private void done() {
        if (this.updateAreaBlock()) {
            this.close();
        }
    }

    private void cancel() {
        // TODO
        this.areaBlock.setShowArea(this.showArea);
        this.areaBlock.setAppliedStatusEffectAmbient(this.appliedStatusEffectAmbient);
        this.areaBlock.setAppliedStatusEffectShowParticles(this.appliedStatusEffectShowParticles);
        this.areaBlock.setAppliedStatusEffectShowIcon(this.appliedStatusEffectShowIcon);
        this.areaBlock.setWasTriggered(this.wasTriggered);
        this.close();
    }

    @Override
    protected void init() {
        this.addDrawableChild(CyclingButtonWidget.builder(ScreenPage::asText).values((ScreenPage[]) ScreenPage.values()).initially(this.screenPage).omitKeyText().build(this.width / 2 - 154, 30, 300, 20, Text.empty(), (button, screenPage) -> {
            this.screenPage = screenPage;
            this.updateWidgets();
        }));

        this.showArea = this.areaBlock.showArea();
        this.toggleShowAreaButton = this.addDrawableChild(CyclingButtonWidget.onOffBuilder(HIDE_AREA_LABEL_TEXT, SHOW_AREA_LABEL_TEXT).initially(this.showArea).omitKeyText().build(this.width / 2 - 154, 54, 300, 20, Text.empty(), (button, showApplicationArea) -> {
            this.showArea = showApplicationArea;
        }));

        this.areaDimensionsXField = new TextFieldWidget(this.textRenderer, this.width / 2 - 154, 89, 100, 20, Text.empty());
        this.areaDimensionsXField.setMaxLength(128);
        this.areaDimensionsXField.setText(Integer.toString(this.areaBlock.getAreaDimensions().getX()));
        this.addSelectableChild(this.areaDimensionsXField);

        this.areaDimensionsYField = new TextFieldWidget(this.textRenderer, this.width / 2 - 50, 89, 100, 20, Text.empty());
        this.areaDimensionsYField.setMaxLength(128);
        this.areaDimensionsYField.setText(Integer.toString(this.areaBlock.getAreaDimensions().getY()));
        this.addSelectableChild(this.areaDimensionsYField);

        this.areaDimensionsZField = new TextFieldWidget(this.textRenderer, this.width / 2 + 54, 89, 100, 20, Text.empty());
        this.areaDimensionsZField.setMaxLength(128);
        this.areaDimensionsZField.setText(Integer.toString(this.areaBlock.getAreaDimensions().getZ()));
        this.addSelectableChild(this.areaDimensionsZField);

        this.areaPositionOffsetXField = new TextFieldWidget(this.textRenderer, this.width / 2 - 154, 124, 100, 20, Text.empty());
        this.areaPositionOffsetXField.setMaxLength(128);
        this.areaPositionOffsetXField.setText(Integer.toString(this.areaBlock.getAreaPositionOffset().getX()));
        this.addSelectableChild(this.areaPositionOffsetXField);

        this.areaPositionOffsetYField = new TextFieldWidget(this.textRenderer, this.width / 2 - 50, 124, 100, 20, Text.empty());
        this.areaPositionOffsetYField.setMaxLength(128);
        this.areaPositionOffsetYField.setText(Integer.toString(this.areaBlock.getAreaPositionOffset().getY()));
        this.addSelectableChild(this.areaPositionOffsetYField);

        this.areaPositionOffsetZField = new TextFieldWidget(this.textRenderer, this.width / 2 + 54, 124, 100, 20, Text.empty());
        this.areaPositionOffsetZField.setMaxLength(128);
        this.areaPositionOffsetZField.setText(Integer.toString(this.areaBlock.getAreaPositionOffset().getZ()));
        this.addSelectableChild(this.areaPositionOffsetZField);

        
        this.appliedStatusEffectIdentifierField = new TextFieldWidget(this.textRenderer, this.width / 2 - 154, 65, 300, 20, Text.empty());
        this.appliedStatusEffectIdentifierField.setMaxLength(128);
        this.appliedStatusEffectIdentifierField.setText(this.areaBlock.getAppliedStatusEffectIdentifier());
        this.addSelectableChild(this.appliedStatusEffectIdentifierField);

        this.appliedStatusEffectAmplifierField = new TextFieldWidget(this.textRenderer, this.width / 2 - 154, 100, 150, 20, Text.empty());
        this.appliedStatusEffectAmplifierField.setText(Integer.toString(this.areaBlock.getAppliedStatusEffectAmplifier()));
        this.addSelectableChild(this.appliedStatusEffectAmplifierField);

        this.appliedStatusEffectAmbient = this.areaBlock.getAppliedStatusEffectAmbient();
        this.toggleAppliedStatusEffectAmbientButton = this.addDrawableChild(CyclingButtonWidget.onOffBuilder(AMBIENT_TRUE_LABEL_TEXT, AMBIENT_FALSE_LABEL_TEXT).initially(this.appliedStatusEffectAmbient).omitKeyText().build(this.width / 2 + 4, 100, 150, 20, Text.empty(), (button, appliedStatusEffectAmbient) -> {
            this.appliedStatusEffectAmbient = appliedStatusEffectAmbient;
        }));

        this.appliedStatusEffectShowParticles = this.areaBlock.getAppliedStatusEffectShowParticles();
        this.toggleAppliedStatusEffectShowParticlesButton = this.addDrawableChild(CyclingButtonWidget.onOffBuilder(SHOW_PARTICLES_LABEL_TEXT, HIDE_PARTICLES_LABEL_TEXT).initially(this.appliedStatusEffectShowParticles).omitKeyText().build(this.width / 2 - 154, 124, 150, 20, Text.empty(), (button, appliedStatusEffectShowParticles) -> {
            this.appliedStatusEffectShowParticles = appliedStatusEffectShowParticles;
        }));

        this.appliedStatusEffectShowIcon = this.areaBlock.getAppliedStatusEffectShowIcon();
        this.toggleAppliedStatusEffectShowIconButton = this.addDrawableChild(CyclingButtonWidget.onOffBuilder(SHOW_ICON_LABEL_TEXT, HIDE_ICON_LABEL_TEXT).initially(this.appliedStatusEffectShowIcon).omitKeyText().build(this.width / 2 + 4, 124, 150, 20, Text.empty(), (button, appliedStatusEffectShowIcon) -> {
            this.appliedStatusEffectShowIcon = appliedStatusEffectShowIcon;
        }));


        this.joinMessageField = new TextFieldWidget(this.textRenderer, this.width / 2 - 154, 65, 300, 20, Text.empty());
        this.joinMessageField.setMaxLength(128);
        this.joinMessageField.setText(this.areaBlock.getJoinMessage());
        this.addSelectableChild(this.joinMessageField);

        this.leaveMessageField = new TextFieldWidget(this.textRenderer, this.width / 2 - 154, 100, 300, 20, Text.empty());
        this.leaveMessageField.setMaxLength(128);
        this.leaveMessageField.setText(this.areaBlock.getLeaveMessage());
        this.addSelectableChild(this.leaveMessageField);

        this.triggeredMessageField = new TextFieldWidget(this.textRenderer, this.width / 2 - 154, 135, 300, 20, Text.empty());
        this.triggeredMessageField.setMaxLength(128);
        this.triggeredMessageField.setText(this.areaBlock.getTriggeredMessage());
        this.addSelectableChild(this.triggeredMessageField);

        this.messageMode = this.areaBlock.getMessageMode();
        this.cycleMessageModeButton = this.addDrawableChild(CyclingButtonWidget.builder(AreaBlockEntity.MessageMode::asText).values((AreaBlockEntity.MessageMode[]) AreaBlockEntity.MessageMode.values()).initially(this.messageMode).omitKeyText().build(this.width / 2 - 154, 159, 300, 20, Text.empty(), (button, messageMode) -> {
            this.messageMode = messageMode;
            this.updateWidgets();
        }));

        
        this.triggeredBlockPositionOffsetXField = new TextFieldWidget(this.textRenderer, this.width / 2 - 154, 65, 50, 20, Text.translatable(""));
        this.triggeredBlockPositionOffsetXField.setMaxLength(128);
        this.triggeredBlockPositionOffsetXField.setText(Integer.toString(this.areaBlock.getTriggeredBlock().getLeft().getX()));
        this.addSelectableChild(this.triggeredBlockPositionOffsetXField);
        this.triggeredBlockPositionOffsetYField = new TextFieldWidget(this.textRenderer, this.width / 2 - 100, 65, 50, 20, Text.translatable(""));
        this.triggeredBlockPositionOffsetYField.setMaxLength(128);
        this.triggeredBlockPositionOffsetYField.setText(Integer.toString(this.areaBlock.getTriggeredBlock().getLeft().getY()));
        this.addSelectableChild(this.triggeredBlockPositionOffsetYField);
        this.triggeredBlockPositionOffsetZField = new TextFieldWidget(this.textRenderer, this.width / 2 - 46, 65, 50, 20, Text.translatable(""));
        this.triggeredBlockPositionOffsetZField.setMaxLength(128);
        this.triggeredBlockPositionOffsetZField.setText(Integer.toString(this.areaBlock.getTriggeredBlock().getLeft().getZ()));
        this.addSelectableChild(this.triggeredBlockPositionOffsetZField);
        this.triggeredBlockResets = this.areaBlock.getTriggeredBlock().getRight();
        this.toggleTriggeredBlockResetsButton = this.addDrawableChild(CyclingButtonWidget.onOffBuilder(Text.translatable("gui.triggered_block.toggle_triggered_block_resets_button_label.on"), Text.translatable("gui.triggered_block.toggle_triggered_block_resets_button_label.off")).initially(this.triggeredBlockResets).omitKeyText().build(this.width / 2 + 8, 65, 150, 20, Text.empty(), (button, triggeredBlockResets) -> {
            this.triggeredBlockResets = triggeredBlockResets;
        }));

        this.wasTriggered = this.areaBlock.getWasTriggered();
        this.toggleWasTriggeredButton = this.addDrawableChild(CyclingButtonWidget.onOffBuilder(TRIGGERED_TRUE_LABEL_TEXT, TRIGGERED_FALSE_LABEL_TEXT).initially(this.wasTriggered).omitKeyText().build(this.width / 2 - 154, 89, 300, 20, Text.empty(), (button, triggered) -> {
            this.wasTriggered = triggered;
        }));

        this.triggeredMode = this.areaBlock.getTriggeredMode();
        this.cycleTriggeredModeButton = this.addDrawableChild(CyclingButtonWidget.builder(AreaBlockEntity.TriggeredMode::asText).values((AreaBlockEntity.TriggeredMode[]) AreaBlockEntity.TriggeredMode.values()).initially(this.triggeredMode).omitKeyText().build(this.width / 2 - 154, 113, 300, 20, Text.empty(), (button, triggeredMode) -> {
            this.triggeredMode = triggeredMode;
            this.updateWidgets();
        }));

        this.triggerMode = this.areaBlock.getTriggerMode();
        this.cycleTriggerModeButton = this.addDrawableChild(CyclingButtonWidget.builder(AreaBlockEntity.TriggerMode::asText).values((AreaBlockEntity.TriggerMode[]) AreaBlockEntity.TriggerMode.values()).initially(this.triggerMode).omitKeyText().build(this.width / 2 - 154, 137, 300, 20, Text.empty(), (button, triggerMode) -> {
            this.triggerMode = triggerMode;
            this.updateWidgets();
        }));

        int i = this.textRenderer.getWidth(PRE_TIMER_LABEL_TEXT) + 10;
        this.timerField = new TextFieldWidget(this.textRenderer, this.width / 2 - 152 + i, 161, 50, 20, Text.translatable(""));
        this.timerField.setText(Integer.toString(this.areaBlock.getMaxTimer()));
        this.addSelectableChild(this.timerField);

        this.addDrawableChild(ButtonWidget.builder(ScreenTexts.DONE, button -> this.done()).dimensions(this.width / 2 - 4 - 150, 207, 150, 20).build());
        this.addDrawableChild(ButtonWidget.builder(ScreenTexts.CANCEL, button -> this.cancel()).dimensions(this.width / 2 + 4, 207, 150, 20).build());
        this.updateWidgets();
    }

    private void updateWidgets() {

        this.toggleWasTriggeredButton.visible = false;
        
        this.toggleShowAreaButton.visible = false;
        this.areaDimensionsXField.visible = false;
        this.areaDimensionsYField.visible = false;
        this.areaDimensionsZField.visible = false;
        this.areaPositionOffsetXField.visible = false;
        this.areaPositionOffsetYField.visible = false;
        this.areaPositionOffsetZField.visible = false;

        this.appliedStatusEffectIdentifierField.setVisible(false);
        this.appliedStatusEffectAmplifierField.setVisible(false);
        this.toggleAppliedStatusEffectAmbientButton.visible = false;
        this.toggleAppliedStatusEffectShowParticlesButton.visible = false;
        this.toggleAppliedStatusEffectShowIconButton.visible = false;
        
        this.joinMessageField.setVisible(false);
        this.leaveMessageField.setVisible(false);
        this.triggeredMessageField.setVisible(false);
        this.cycleMessageModeButton.visible = false;

        this.triggeredBlockPositionOffsetXField.setVisible(false);
        this.triggeredBlockPositionOffsetYField.setVisible(false);
        this.triggeredBlockPositionOffsetZField.setVisible(false);
        this.toggleTriggeredBlockResetsButton.visible = false;
        this.toggleWasTriggeredButton.visible = false;
        this.cycleTriggerModeButton.visible = false;
        this.cycleTriggeredModeButton.visible = false;
        this.timerField.setVisible(false);

        if (this.screenPage == ScreenPage.AREA) {

            this.toggleShowAreaButton.visible = true;
            this.areaDimensionsXField.visible = true;
            this.areaDimensionsYField.visible = true;
            this.areaDimensionsZField.visible = true;
            this.areaPositionOffsetXField.visible = true;
            this.areaPositionOffsetYField.visible = true;
            this.areaPositionOffsetZField.visible = true;

        } else if (this.screenPage == ScreenPage.APPLIED_EFFECT) {

            this.appliedStatusEffectIdentifierField.setVisible(true);
            this.appliedStatusEffectAmplifierField.setVisible(true);

            this.toggleAppliedStatusEffectAmbientButton.visible = true;
            this.toggleAppliedStatusEffectShowParticlesButton.visible = true;
            this.toggleAppliedStatusEffectShowIconButton.visible = true;

        } else if (this.screenPage == ScreenPage.MESSAGES) {

            this.joinMessageField.setVisible(true);
            this.leaveMessageField.setVisible(true);
            this.triggeredMessageField.setVisible(true);
            this.cycleMessageModeButton.visible = true;
            
        } else if (this.screenPage == ScreenPage.TRIGGER_SETTINGS) {

            this.triggeredBlockPositionOffsetXField.setVisible(true);
            this.triggeredBlockPositionOffsetYField.setVisible(true);
            this.triggeredBlockPositionOffsetZField.setVisible(true);
            this.toggleTriggeredBlockResetsButton.visible = true;
            this.toggleWasTriggeredButton.visible = true;
            this.cycleTriggerModeButton.visible = true;
            this.cycleTriggeredModeButton.visible = true;
            if (this.triggerMode == AreaBlockEntity.TriggerMode.TIMED) {
                this.timerField.setVisible(true);
            }
        }
    }

    @Override
    public void resize(MinecraftClient client, int width, int height) {
        // TODO
        String string = this.areaDimensionsXField.getText();
        String string1 = this.areaDimensionsYField.getText();
        String string2 = this.areaDimensionsZField.getText();
        String string3 = this.areaPositionOffsetXField.getText();
        String string4 = this.areaPositionOffsetYField.getText();
        String string5 = this.areaPositionOffsetZField.getText();
        String string6 = this.appliedStatusEffectIdentifierField.getText();
        String string7 = this.appliedStatusEffectAmplifierField.getText();
        this.init(client, width, height);
        this.areaDimensionsXField.setText(string);
        this.areaDimensionsYField.setText(string1);
        this.areaDimensionsZField.setText(string2);
        this.areaPositionOffsetXField.setText(string3);
        this.areaPositionOffsetYField.setText(string4);
        this.areaPositionOffsetZField.setText(string5);
        this.appliedStatusEffectIdentifierField.setText(string6);
        this.appliedStatusEffectAmplifierField.setText(string7);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        if (this.screenPage == ScreenPage.AREA) {
            context.drawTextWithShadow(this.textRenderer, AREA_DIMENSIONS_LABEL_TEXT, this.width / 2 - 153, 79, 0xA0A0A0);
            this.areaDimensionsXField.render(context, mouseX, mouseY, delta);
            this.areaDimensionsYField.render(context, mouseX, mouseY, delta);
            this.areaDimensionsZField.render(context, mouseX, mouseY, delta);
            context.drawTextWithShadow(this.textRenderer, AREA_POSITION_OFFET_LABEL_TEXT, this.width / 2 - 153, 114, 0xA0A0A0);
            this.areaPositionOffsetXField.render(context, mouseX, mouseY, delta);
            this.areaPositionOffsetYField.render(context, mouseX, mouseY, delta);
            this.areaPositionOffsetZField.render(context, mouseX, mouseY, delta);
        } else if (this.screenPage == ScreenPage.APPLIED_EFFECT) {
            context.drawTextWithShadow(this.textRenderer, STATUS_EFFECT_IDENTIFIER_LABEL_TEXT, this.width / 2 - 153, 55, 0xA0A0A0);
            this.appliedStatusEffectIdentifierField.render(context, mouseX, mouseY, delta);
            context.drawTextWithShadow(this.textRenderer, STATUS_EFFECT_AMPLIFIER_LABEL_TEXT, this.width / 2 - 153, 90, 0xA0A0A0);
            this.appliedStatusEffectAmplifierField.render(context, mouseX, mouseY, delta);
        } else if (this.screenPage == ScreenPage.MESSAGES) {
            context.drawTextWithShadow(this.textRenderer, JOIN_MESSAGE_LABEL_TEXT, this.width / 2 - 153, 55, 0xA0A0A0);
            this.joinMessageField.render(context, mouseX, mouseY, delta);
            context.drawTextWithShadow(this.textRenderer, LEAVE_MESSAGE_LABEL_TEXT, this.width / 2 - 153, 90, 0xA0A0A0);
            this.leaveMessageField.render(context, mouseX, mouseY, delta);
            context.drawTextWithShadow(this.textRenderer, TRIGGERED_MESSAGE_LABEL_TEXT, this.width / 2 - 153, 125, 0xA0A0A0);
            this.triggeredMessageField.render(context, mouseX, mouseY, delta);
        } else if (this.screenPage == ScreenPage.TRIGGER_SETTINGS) {
            context.drawTextWithShadow(this.textRenderer, TRIGGERED_BLOCK_POSITION_TEXT, this.width / 2 - 153, 55, 0xA0A0A0);
            this.triggeredBlockPositionOffsetXField.render(context, mouseX, mouseY, delta);
            this.triggeredBlockPositionOffsetYField.render(context, mouseX, mouseY, delta);
            this.triggeredBlockPositionOffsetZField.render(context, mouseX, mouseY, delta);
            if (this.triggerMode == AreaBlockEntity.TriggerMode.TIMED) {
                context.drawTextWithShadow(this.textRenderer, PRE_TIMER_LABEL_TEXT, this.width / 2 - 153, 167, 0xA0A0A0);
                this.timerField.render(context, mouseX, mouseY, delta);
                int i = this.timerField.getX() + 60;
                context.drawTextWithShadow(this.textRenderer, POST_TIMER_LABEL_TEXT, i, 167, 0xA0A0A0);
            }
        }
    }

    @Override
    public boolean shouldPause() {
        return false;
    }

    private boolean updateAreaBlock() {
        ClientPlayNetworking.send(new UpdateAreaBlockPacket(
                this.areaBlock.getPos(),
                this.showArea,
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
                this.appliedStatusEffectIdentifierField.getText(),
                ItemUtils.parseInt(this.appliedStatusEffectAmplifierField.getText()),
                this.appliedStatusEffectAmbient,
                this.appliedStatusEffectShowParticles,
                this.appliedStatusEffectShowIcon,
                new BlockPos(
                        ItemUtils.parseInt(this.triggeredBlockPositionOffsetXField.getText()),
                        ItemUtils.parseInt(this.triggeredBlockPositionOffsetYField.getText()),
                        ItemUtils.parseInt(this.triggeredBlockPositionOffsetZField.getText())
                ),
                this.triggeredBlockResets,
                this.wasTriggered,
                this.joinMessageField.getText(),
                this.leaveMessageField.getText(),
                this.triggeredMessageField.getText(),
                this.messageMode.asString(),
                this.triggerMode.asString(),
                this.triggeredMode.asString(),
                ItemUtils.parseInt(this.timerField.getText())
        ));
        return true;
    }

    public static enum ScreenPage implements StringIdentifiable {
        AREA("area"),
        APPLIED_EFFECT("applied_effect"),
        MESSAGES("messages"),
        TRIGGER_SETTINGS("trigger_settings");

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
            return Text.translatable("gui.area_block.screenPage." + this.name);
        }
    }
}
