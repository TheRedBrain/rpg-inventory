package com.github.theredbrain.betteradventuremode.client.gui.screen.ingame;

import com.github.theredbrain.betteradventuremode.block.entity.TeleporterBlockEntity;
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
    private static final Text HIDE__LABEL_TEXT = Text.translatable("gui.area_block.hide_area_label");
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
    private final AreaBlockEntity areaBlock;
    private CyclingButtonWidget<Boolean> toggleTriggeredButton;
    private CyclingButtonWidget<Boolean> toggleShowAreaButton;
    private TextFieldWidget areaDimensionsXField;
    private TextFieldWidget areaDimensionsYField;
    private TextFieldWidget areaDimensionsZField;
    private TextFieldWidget areaPositionOffsetXField;
    private TextFieldWidget areaPositionOffsetYField;
    private TextFieldWidget areaPositionOffsetZField;
    private TextFieldWidget appliedStatusEffectIdentifierField;
    private TextFieldWidget appliedStatusEffectAmplifierField;
    private CyclingButtonWidget<Boolean> toggleAppliedStatusEffectAmbientButton;
    private CyclingButtonWidget<Boolean> toggleAppliedStatusEffectShowParticlesButton;
    private CyclingButtonWidget<Boolean> toggleAppliedStatusEffectShowIconButton;
    private ScreenPage screenPage;
    private boolean showArea;
    private boolean appliedStatusEffectAmbient;
    private boolean appliedStatusEffectShowParticles;
    private boolean appliedStatusEffectShowIcon;
    private boolean triggered;

    public AreaBlockScreen(AreaBlockEntity areaBlock) {
        super(NarratorManager.EMPTY);
        this.areaBlock = areaBlock;
    }

    private void done() {
        if (this.updateStatusEffectApplierBlock()) {
            this.close();
        }
    }

    private void cancel() {
        this.areaBlock.setShowArea(this.showArea);
        this.areaBlock.setAppliedStatusEffectAmbient(this.appliedStatusEffectAmbient);
        this.areaBlock.setAppliedStatusEffectShowParticles(this.appliedStatusEffectShowParticles);
        this.areaBlock.setAppliedStatusEffectShowIcon(this.appliedStatusEffectShowIcon);
        this.areaBlock.setWasTriggered(this.triggered);
        this.close();
    }

    @Override
    protected void init() {
        this.addDrawableChild(CyclingButtonWidget.builder(ScreenPage::asText).values((ScreenPage[]) ScreenPage.values()).initially(this.screenPage).omitKeyText().build(this.width / 2 - 154, 30, 300, 20, Text.empty(), (button, screenPage) -> {
            this.screenPage = screenPage;
            this.updateWidgets();
        }));

        this.areaDimensionsXField = new TextFieldWidget(this.textRenderer, this.width / 2 - 154, 65, 100, 20, Text.empty());
        this.areaDimensionsXField.setMaxLength(128);
        this.areaDimensionsXField.setText(Integer.toString(this.areaBlock.getAreaDimensions().getX()));
        this.addSelectableChild(this.areaDimensionsXField);

        this.areaDimensionsYField = new TextFieldWidget(this.textRenderer, this.width / 2 - 50, 65, 100, 20, Text.empty());
        this.areaDimensionsYField.setMaxLength(128);
        this.areaDimensionsYField.setText(Integer.toString(this.areaBlock.getAreaDimensions().getY()));
        this.addSelectableChild(this.areaDimensionsYField);

        this.areaDimensionsZField = new TextFieldWidget(this.textRenderer, this.width / 2 + 54, 65, 100, 20, Text.empty());
        this.areaDimensionsZField.setMaxLength(128);
        this.areaDimensionsZField.setText(Integer.toString(this.areaBlock.getAreaDimensions().getZ()));
        this.addSelectableChild(this.areaDimensionsZField);

        this.areaPositionOffsetXField = new TextFieldWidget(this.textRenderer, this.width / 2 - 154, 100, 100, 20, Text.empty());
        this.areaPositionOffsetXField.setMaxLength(128);
        this.areaPositionOffsetXField.setText(Integer.toString(this.areaBlock.getAreaPositionOffset().getX()));
        this.addSelectableChild(this.areaPositionOffsetXField);

        this.areaPositionOffsetYField = new TextFieldWidget(this.textRenderer, this.width / 2 - 50, 100, 100, 20, Text.empty());
        this.areaPositionOffsetYField.setMaxLength(128);
        this.areaPositionOffsetYField.setText(Integer.toString(this.areaBlock.getAreaPositionOffset().getY()));
        this.addSelectableChild(this.areaPositionOffsetYField);

        this.areaPositionOffsetZField = new TextFieldWidget(this.textRenderer, this.width / 2 + 54, 100, 100, 20, Text.empty());
        this.areaPositionOffsetZField.setMaxLength(128);
        this.areaPositionOffsetZField.setText(Integer.toString(this.areaBlock.getAreaPositionOffset().getZ()));
        this.addSelectableChild(this.areaPositionOffsetZField);

        this.triggered = this.areaBlock.getWasTriggered();
        this.toggleTriggeredButton = this.addDrawableChild(CyclingButtonWidget.onOffBuilder(TRIGGERED_TRUE_LABEL_TEXT, TRIGGERED_FALSE_LABEL_TEXT).initially(this.triggered).omitKeyText().build(this.width / 2 - 154, 124, 150, 20, Text.empty(), (button, triggered) -> {
            this.triggered = triggered;
        }));

        this.showArea = this.areaBlock.showArea();
        this.toggleShowAreaButton = this.addDrawableChild(CyclingButtonWidget.onOffBuilder(HIDE__LABEL_TEXT, SHOW_AREA_LABEL_TEXT).initially(this.showArea).omitKeyText().build(this.width / 2 + 4, 124, 150, 20, Text.empty(), (button, showApplicationArea) -> {
            this.showArea = showApplicationArea;
        }));

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
        this.toggleAppliedStatusEffectShowParticlesButton = this.addDrawableChild(CyclingButtonWidget.onOffBuilder(HIDE_PARTICLES_LABEL_TEXT, SHOW_PARTICLES_LABEL_TEXT).initially(this.appliedStatusEffectShowParticles).omitKeyText().build(this.width / 2 - 154, 135, 150, 20, Text.empty(), (button, appliedStatusEffectShowParticles) -> {
            this.appliedStatusEffectShowParticles = appliedStatusEffectShowParticles;
        }));

        this.appliedStatusEffectShowIcon = this.areaBlock.getAppliedStatusEffectShowIcon();
        this.toggleAppliedStatusEffectShowIconButton = this.addDrawableChild(CyclingButtonWidget.onOffBuilder(HIDE_ICON_LABEL_TEXT, SHOW_ICON_LABEL_TEXT).initially(this.appliedStatusEffectShowIcon).omitKeyText().build(this.width / 2 + 4, 135, 150, 20, Text.empty(), (button, appliedStatusEffectShowIcon) -> {
            this.appliedStatusEffectShowIcon = appliedStatusEffectShowIcon;
        }));

        this.addDrawableChild(ButtonWidget.builder(ScreenTexts.DONE, button -> this.done()).dimensions(this.width / 2 - 4 - 150, 207, 150, 20).build());
        this.addDrawableChild(ButtonWidget.builder(ScreenTexts.CANCEL, button -> this.cancel()).dimensions(this.width / 2 + 4, 207, 150, 20).build());
    }

    private void updateWidgets() {

        this.toggleTriggeredButton.visible = false;
        
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

        if (this.screenPage == ScreenPage.AREA) {

            this.toggleTriggeredButton.visible = true;

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

        }
    }

    @Override
    public void resize(MinecraftClient client, int width, int height) {
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
            context.drawTextWithShadow(this.textRenderer, AREA_DIMENSIONS_LABEL_TEXT, this.width / 2 - 153, 20, 0xA0A0A0);
            this.areaDimensionsXField.render(context, mouseX, mouseY, delta);
            this.areaDimensionsYField.render(context, mouseX, mouseY, delta);
            this.areaDimensionsZField.render(context, mouseX, mouseY, delta);
            context.drawTextWithShadow(this.textRenderer, AREA_POSITION_OFFET_LABEL_TEXT, this.width / 2 - 153, 55, 0xA0A0A0);
            this.areaPositionOffsetXField.render(context, mouseX, mouseY, delta);
            this.areaPositionOffsetYField.render(context, mouseX, mouseY, delta);
            this.areaPositionOffsetZField.render(context, mouseX, mouseY, delta);
        } else if (this.screenPage == ScreenPage.APPLIED_EFFECT) {
            context.drawTextWithShadow(this.textRenderer, STATUS_EFFECT_IDENTIFIER_LABEL_TEXT, this.width / 2 - 153, 114, 0xA0A0A0);
            this.appliedStatusEffectIdentifierField.render(context, mouseX, mouseY, delta);
            context.drawTextWithShadow(this.textRenderer, STATUS_EFFECT_AMPLIFIER_LABEL_TEXT, this.width / 2 - 153, 149, 0xA0A0A0);
            this.appliedStatusEffectAmplifierField.render(context, mouseX, mouseY, delta);
        }
    }

    @Override
    public boolean shouldPause() {
        return false;
    }

    private boolean updateStatusEffectApplierBlock() {
        ClientPlayNetworking.send(new UpdateAreaBlockPacket(
                this.areaBlock.getPos(),
                this.triggered,
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
                this.appliedStatusEffectShowIcon
        ));
        return true;
    }

    public static enum ScreenPage implements StringIdentifiable {
        AREA("area"),
        APPLIED_EFFECT("applied_effect");

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
