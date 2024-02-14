package com.github.theredbrain.betteradventuremode.client.gui.screen.ingame;

import com.github.theredbrain.betteradventuremode.util.ItemUtils;
import com.github.theredbrain.betteradventuremode.block.entity.StatusEffectApplierBlockEntity;
import com.github.theredbrain.betteradventuremode.network.packet.UpdateStatusEffectApplierBlockPacket;
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
import net.minecraft.util.math.Vec3i;

@Environment(value= EnvType.CLIENT)
public class StatusEffectApplierBlockScreen extends Screen {
    private static final Text TRIGGERED_TRUE_LABEL_TEXT = Text.translatable("gui.status_effect_applier_block.triggered_true_label");
    private static final Text TRIGGERED_FALSE_LABEL_TEXT = Text.translatable("gui.status_effect_applier_block.triggered_false_label");
    private static final Text HIDE_APPLICATION_AREA_LABEL_TEXT = Text.translatable("gui.status_effect_applier_block.hide_application_area_label");
    private static final Text SHOW_APPLICATION_AREA_LABEL_TEXT = Text.translatable("gui.status_effect_applier_block.show_application_area_label");
    private static final Text APPLICATION_AREA_DIMENSIONS_LABEL_TEXT = Text.translatable("gui.status_effect_applier_block.application_area_dimensions_label");
    private static final Text APPLICATION_AREA_POSITION_OFFET_LABEL_TEXT = Text.translatable("gui.status_effect_applier_block.application_area_position_offset_label");
    private static final Text STATUS_EFFECT_IDENTIFIER_LABEL_TEXT = Text.translatable("gui.status_effect_applier_block.status_effect_identifier_label");
    private static final Text STATUS_EFFECT_AMPLIFIER_LABEL_TEXT = Text.translatable("gui.status_effect_applier_block.status_effect_amplifier_label");
    private static final Text AMBIENT_TRUE_LABEL_TEXT = Text.translatable("gui.status_effect_applier_block.ambient_true_label");
    private static final Text AMBIENT_FALSE_LABEL_TEXT = Text.translatable("gui.status_effect_applier_block.ambient_false_label");
    private static final Text HIDE_PARTICLES_LABEL_TEXT = Text.translatable("gui.status_effect_applier_block.hide_particles_label");
    private static final Text SHOW_PARTICLES_LABEL_TEXT = Text.translatable("gui.status_effect_applier_block.show_particles_label");
    private static final Text HIDE_ICON_LABEL_TEXT = Text.translatable("gui.status_effect_applier_block.hide_icon_label");
    private static final Text SHOW_ICON_LABEL_TEXT = Text.translatable("gui.status_effect_applier_block.show_icon_label");
    private final StatusEffectApplierBlockEntity statusEffectApplierBlock;
    private CyclingButtonWidget<Boolean> toggleTriggeredButton;
    private CyclingButtonWidget<Boolean> toggleShowApplicationAreaButton;
    private TextFieldWidget applicationAreaDimensionsXField;
    private TextFieldWidget applicationAreaDimensionsYField;
    private TextFieldWidget applicationAreaDimensionsZField;
    private TextFieldWidget applicationAreaPositionOffsetXField;
    private TextFieldWidget applicationAreaPositionOffsetYField;
    private TextFieldWidget applicationAreaPositionOffsetZField;
    private TextFieldWidget appliedStatusEffectIdentifierField;
    private TextFieldWidget appliedStatusEffectAmplifierField;
    private CyclingButtonWidget<Boolean> toggleAppliedStatusEffectAmbientButton;
    private CyclingButtonWidget<Boolean> toggleAppliedStatusEffectShowParticlesButton;
    private CyclingButtonWidget<Boolean> toggleAppliedStatusEffectShowIconButton;
    private boolean showApplicationArea;
    private boolean appliedStatusEffectAmbient;
    private boolean appliedStatusEffectShowParticles;
    private boolean appliedStatusEffectShowIcon;
    private boolean triggered;

    public StatusEffectApplierBlockScreen(StatusEffectApplierBlockEntity statusEffectApplierBlock) {
        super(NarratorManager.EMPTY);
        this.statusEffectApplierBlock = statusEffectApplierBlock;
    }

    private void done() {
        if (this.updateStatusEffectApplierBlock()) {
            this.close();
        }
    }

    private void cancel() {
        this.statusEffectApplierBlock.setShowApplicationArea(this.showApplicationArea);
        this.statusEffectApplierBlock.setAppliedStatusEffectAmbient(this.appliedStatusEffectAmbient);
        this.statusEffectApplierBlock.setAppliedStatusEffectShowParticles(this.appliedStatusEffectShowParticles);
        this.statusEffectApplierBlock.setAppliedStatusEffectShowIcon(this.appliedStatusEffectShowIcon);
        this.statusEffectApplierBlock.setTriggered(this.triggered);
        this.close();
    }

    @Override
    protected void init() {
        this.applicationAreaDimensionsXField = new TextFieldWidget(this.textRenderer, this.width / 2 - 154, 30, 100, 20, Text.empty());
        this.applicationAreaDimensionsXField.setMaxLength(128);
        this.applicationAreaDimensionsXField.setText(Integer.toString(this.statusEffectApplierBlock.getApplicationAreaDimensions().getX()));
        this.addSelectableChild(this.applicationAreaDimensionsXField);

        this.applicationAreaDimensionsYField = new TextFieldWidget(this.textRenderer, this.width / 2 - 50, 30, 100, 20, Text.empty());
        this.applicationAreaDimensionsYField.setMaxLength(128);
        this.applicationAreaDimensionsYField.setText(Integer.toString(this.statusEffectApplierBlock.getApplicationAreaDimensions().getY()));
        this.addSelectableChild(this.applicationAreaDimensionsYField);

        this.applicationAreaDimensionsZField = new TextFieldWidget(this.textRenderer, this.width / 2 + 54, 30, 100, 20, Text.empty());
        this.applicationAreaDimensionsZField.setMaxLength(128);
        this.applicationAreaDimensionsZField.setText(Integer.toString(this.statusEffectApplierBlock.getApplicationAreaDimensions().getZ()));
        this.addSelectableChild(this.applicationAreaDimensionsZField);

        this.applicationAreaPositionOffsetXField = new TextFieldWidget(this.textRenderer, this.width / 2 - 154, 65, 100, 20, Text.empty());
        this.applicationAreaPositionOffsetXField.setMaxLength(128);
        this.applicationAreaPositionOffsetXField.setText(Integer.toString(this.statusEffectApplierBlock.getApplicationAreaPositionOffset().getX()));
        this.addSelectableChild(this.applicationAreaPositionOffsetXField);

        this.applicationAreaPositionOffsetYField = new TextFieldWidget(this.textRenderer, this.width / 2 - 50, 65, 100, 20, Text.empty());
        this.applicationAreaPositionOffsetYField.setMaxLength(128);
        this.applicationAreaPositionOffsetYField.setText(Integer.toString(this.statusEffectApplierBlock.getApplicationAreaPositionOffset().getY()));
        this.addSelectableChild(this.applicationAreaPositionOffsetYField);

        this.applicationAreaPositionOffsetZField = new TextFieldWidget(this.textRenderer, this.width / 2 + 54, 65, 100, 20, Text.empty());
        this.applicationAreaPositionOffsetZField.setMaxLength(128);
        this.applicationAreaPositionOffsetZField.setText(Integer.toString(this.statusEffectApplierBlock.getApplicationAreaPositionOffset().getZ()));
        this.addSelectableChild(this.applicationAreaPositionOffsetZField);

        this.triggered = this.statusEffectApplierBlock.getTriggered();
        this.toggleTriggeredButton = this.addDrawableChild(CyclingButtonWidget.onOffBuilder(TRIGGERED_TRUE_LABEL_TEXT, TRIGGERED_FALSE_LABEL_TEXT).initially(this.triggered).omitKeyText().build(this.width / 2 - 154, 89, 150, 20, Text.empty(), (button, triggered) -> {
            this.triggered = triggered;
        }));

        this.showApplicationArea = this.statusEffectApplierBlock.getShowApplicationArea();
        this.toggleShowApplicationAreaButton = this.addDrawableChild(CyclingButtonWidget.onOffBuilder(HIDE_APPLICATION_AREA_LABEL_TEXT, SHOW_APPLICATION_AREA_LABEL_TEXT).initially(this.showApplicationArea).omitKeyText().build(this.width / 2 + 4, 89, 150, 20, Text.empty(), (button, showApplicationArea) -> {
            this.showApplicationArea = showApplicationArea;
        }));

        this.appliedStatusEffectIdentifierField = new TextFieldWidget(this.textRenderer, this.width / 2 - 154, 124, 300, 20, Text.empty());
        this.appliedStatusEffectIdentifierField.setMaxLength(128);
        this.appliedStatusEffectIdentifierField.setText(this.statusEffectApplierBlock.getAppliedStatusEffectIdentifier());
        this.addSelectableChild(this.appliedStatusEffectIdentifierField);

        this.appliedStatusEffectAmplifierField = new TextFieldWidget(this.textRenderer, this.width / 2 - 154, 159, 150, 20, Text.empty());
        this.appliedStatusEffectAmplifierField.setText(Integer.toString(this.statusEffectApplierBlock.getAppliedStatusEffectAmplifier()));
        this.addSelectableChild(this.appliedStatusEffectAmplifierField);

        this.appliedStatusEffectAmbient = this.statusEffectApplierBlock.getAppliedStatusEffectAmbient();
        this.toggleAppliedStatusEffectAmbientButton = this.addDrawableChild(CyclingButtonWidget.onOffBuilder(AMBIENT_TRUE_LABEL_TEXT, AMBIENT_FALSE_LABEL_TEXT).initially(this.appliedStatusEffectAmbient).omitKeyText().build(this.width / 2 + 4, 159, 150, 20, Text.empty(), (button, appliedStatusEffectAmbient) -> {
            this.appliedStatusEffectAmbient = appliedStatusEffectAmbient;
        }));

        this.appliedStatusEffectShowParticles = this.statusEffectApplierBlock.getAppliedStatusEffectShowParticles();
        this.toggleAppliedStatusEffectShowParticlesButton = this.addDrawableChild(CyclingButtonWidget.onOffBuilder(HIDE_PARTICLES_LABEL_TEXT, SHOW_PARTICLES_LABEL_TEXT).initially(this.appliedStatusEffectShowParticles).omitKeyText().build(this.width / 2 - 154, 183, 150, 20, Text.empty(), (button, appliedStatusEffectShowParticles) -> {
            this.appliedStatusEffectShowParticles = appliedStatusEffectShowParticles;
        }));

        this.appliedStatusEffectShowIcon = this.statusEffectApplierBlock.getAppliedStatusEffectShowIcon();
        this.toggleAppliedStatusEffectShowIconButton = this.addDrawableChild(CyclingButtonWidget.onOffBuilder(HIDE_ICON_LABEL_TEXT, SHOW_ICON_LABEL_TEXT).initially(this.appliedStatusEffectShowIcon).omitKeyText().build(this.width / 2 + 4, 183, 150, 20, Text.empty(), (button, appliedStatusEffectShowIcon) -> {
            this.appliedStatusEffectShowIcon = appliedStatusEffectShowIcon;
        }));

        this.addDrawableChild(ButtonWidget.builder(ScreenTexts.DONE, button -> this.done()).dimensions(this.width / 2 - 4 - 150, 207, 150, 20).build());
        this.addDrawableChild(ButtonWidget.builder(ScreenTexts.CANCEL, button -> this.cancel()).dimensions(this.width / 2 + 4, 207, 150, 20).build());
    }

    @Override
    public void resize(MinecraftClient client, int width, int height) {
        String string = this.applicationAreaDimensionsXField.getText();
        String string1 = this.applicationAreaDimensionsYField.getText();
        String string2 = this.applicationAreaDimensionsZField.getText();
        String string3 = this.applicationAreaPositionOffsetXField.getText();
        String string4 = this.applicationAreaPositionOffsetYField.getText();
        String string5 = this.applicationAreaPositionOffsetZField.getText();
        String string6 = this.appliedStatusEffectIdentifierField.getText();
        String string7 = this.appliedStatusEffectAmplifierField.getText();
        this.init(client, width, height);
        this.applicationAreaDimensionsXField.setText(string);
        this.applicationAreaDimensionsYField.setText(string1);
        this.applicationAreaDimensionsZField.setText(string2);
        this.applicationAreaPositionOffsetXField.setText(string3);
        this.applicationAreaPositionOffsetYField.setText(string4);
        this.applicationAreaPositionOffsetZField.setText(string5);
        this.appliedStatusEffectIdentifierField.setText(string6);
        this.appliedStatusEffectAmplifierField.setText(string7);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        context.drawTextWithShadow(this.textRenderer, APPLICATION_AREA_DIMENSIONS_LABEL_TEXT, this.width / 2 - 153, 20, 0xA0A0A0);
        this.applicationAreaDimensionsXField.render(context, mouseX, mouseY, delta);
        this.applicationAreaDimensionsYField.render(context, mouseX, mouseY, delta);
        this.applicationAreaDimensionsZField.render(context, mouseX, mouseY, delta);
        context.drawTextWithShadow(this.textRenderer, APPLICATION_AREA_POSITION_OFFET_LABEL_TEXT, this.width / 2 - 153, 55, 0xA0A0A0);
        this.applicationAreaPositionOffsetXField.render(context, mouseX, mouseY, delta);
        this.applicationAreaPositionOffsetYField.render(context, mouseX, mouseY, delta);
        this.applicationAreaPositionOffsetZField.render(context, mouseX, mouseY, delta);
        context.drawTextWithShadow(this.textRenderer, STATUS_EFFECT_IDENTIFIER_LABEL_TEXT, this.width / 2 - 153, 114, 0xA0A0A0);
        this.appliedStatusEffectIdentifierField.render(context, mouseX, mouseY, delta);
        context.drawTextWithShadow(this.textRenderer, STATUS_EFFECT_AMPLIFIER_LABEL_TEXT, this.width / 2 - 153, 149, 0xA0A0A0);
        this.appliedStatusEffectAmplifierField.render(context, mouseX, mouseY, delta);
    }

    @Override
    public boolean shouldPause() {
        return false;
    }

    private boolean updateStatusEffectApplierBlock() {
        ClientPlayNetworking.send(new UpdateStatusEffectApplierBlockPacket(
                this.statusEffectApplierBlock.getPos(),
                this.triggered,
                this.showApplicationArea,
                new Vec3i(
                        ItemUtils.parseInt(this.applicationAreaDimensionsXField.getText()),
                        ItemUtils.parseInt(this.applicationAreaDimensionsYField.getText()),
                        ItemUtils.parseInt(this.applicationAreaDimensionsZField.getText())
                ),
                new BlockPos(
                        ItemUtils.parseInt(this.applicationAreaPositionOffsetXField.getText()),
                        ItemUtils.parseInt(this.applicationAreaPositionOffsetYField.getText()),
                        ItemUtils.parseInt(this.applicationAreaPositionOffsetZField.getText())
                ),
                this.appliedStatusEffectIdentifierField.getText(),
                ItemUtils.parseInt(this.appliedStatusEffectAmplifierField.getText()),
                this.appliedStatusEffectAmbient,
                this.appliedStatusEffectShowParticles,
                this.appliedStatusEffectShowIcon
        ));
        return true;
    }
}
