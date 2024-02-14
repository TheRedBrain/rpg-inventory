package com.github.theredbrain.betteradventuremode.client.gui.screen.ingame;

import com.github.theredbrain.betteradventuremode.BetterAdventureMode;
import com.github.theredbrain.betteradventuremode.util.ItemUtils;
import com.github.theredbrain.betteradventuremode.client.render.entity.MannequinModelPart;
import com.github.theredbrain.betteradventuremode.entity.decoration.MannequinEntity;
import com.github.theredbrain.betteradventuremode.network.packet.*;
import com.github.theredbrain.betteradventuremode.screen.DuckSlotMixin;
import com.github.theredbrain.betteradventuremode.screen.MannequinScreenHandler;
import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.CyclingButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.math.Vec3d;
import org.joml.Vector3f;

import java.util.Arrays;
import java.util.Optional;

@Environment(value= EnvType.CLIENT)
public class MannequinScreen extends HandledScreen<MannequinScreenHandler> {
    private static final int Y_OFFSET_INCREASE_1 = 13;
    private static final int Y_OFFSET_INCREASE_2 = 22;
    private static final Text UPDATE_EQUIPMENT_BUTTON_LABEL_TEXT = Text.translatable("gui.mannequin_entity.update_equipment_button_label");
    private static final Text EXPORT_EQUIPMENT_BUTTON_LABEL_TEXT = Text.translatable("gui.mannequin_entity.export_equipment_button_label");
    private static final Text IMPORT_EQUIPMENT_BUTTON_LABEL_TEXT = Text.translatable("gui.mannequin_entity.import_equipment_button_label");
    public static final Identifier MANNEQUIN_EQUIPMENT_BACKGROUND_TEXTURE = BetterAdventureMode.identifier("textures/gui/container/mannequin_equipment_background.png");
    public static final Identifier MANNEQUIN_MODEL_PARTS_BACKGROUND_TEXTURE = BetterAdventureMode.identifier("textures/gui/container/mannequin_model_parts_background.png");
    public static final Identifier MANNEQUIN_SETTINGS_BACKGROUND_TEXTURE = BetterAdventureMode.identifier("textures/gui/container/mannequin_settings_background.png");

    private final MannequinEntity mannequinEntity;

    private CyclingButtonWidget<ScreenPage> toggleScreenPageButton;

    private ButtonWidget updateTrinketButton0;
    private ButtonWidget updateTrinketButton1;
    private ButtonWidget updateTrinketButton2;
    private ButtonWidget updateTrinketButton3;
    private ButtonWidget updateTrinketButton4;
    private ButtonWidget updateTrinketButton5;
    private ButtonWidget updateTrinketButton6;
    private ButtonWidget updateTrinketButton7;
    private ButtonWidget updateTrinketButton8;
    private ButtonWidget updateTrinketButton9;
    private ButtonWidget updateTrinketButton10;
    private ButtonWidget updateTrinketButton11;
    private ButtonWidget updateTrinketButton12;
    private ButtonWidget updateTrinketButton13;
    private ButtonWidget updateTrinketButton14;
    private ButtonWidget updateTrinketButton15;
    private ButtonWidget updateTrinketButton16;
    private ButtonWidget updateTrinketButton17;
    private ButtonWidget updateTrinketButton18;
    private ButtonWidget updateTrinketButton19;
    private ButtonWidget updateTrinketButton20;
    private ButtonWidget updateTrinketButton21;
    private ButtonWidget exportEquipmentButton;
    private ButtonWidget importEquipmentButton;

    // region model parts widgets
    private TextFieldWidget headPivotXField;
    private TextFieldWidget headPivotYField;
    private TextFieldWidget headPivotZField;
    private TextFieldWidget headPitchField;
    private TextFieldWidget headYawField;
    private TextFieldWidget headRollField;
    private CyclingButtonWidget<Boolean> toggleInnerHeadVisibilityButton;
    private CyclingButtonWidget<Boolean> toggleOuterHeadVisibilityButton;
    private boolean isInnerHeadVisible;
    private boolean isOuterHeadVisible;
    
    private TextFieldWidget bodyPivotXField;
    private TextFieldWidget bodyPivotYField;
    private TextFieldWidget bodyPivotZField;
    private TextFieldWidget bodyPitchField;
    private TextFieldWidget bodyYawField;
    private TextFieldWidget bodyRollField;
    private CyclingButtonWidget<Boolean> toggleInnerBodyVisibilityButton;
    private CyclingButtonWidget<Boolean> toggleOuterBodyVisibilityButton;
    private boolean isInnerBodyVisible;
    private boolean isOuterBodyVisible;
    
    private TextFieldWidget leftArmPivotXField;
    private TextFieldWidget leftArmPivotYField;
    private TextFieldWidget leftArmPivotZField;
    private TextFieldWidget leftArmPitchField;
    private TextFieldWidget leftArmYawField;
    private TextFieldWidget leftArmRollField;
    private CyclingButtonWidget<Boolean> toggleInnerLeftArmVisibilityButton;
    private CyclingButtonWidget<Boolean> toggleOuterLeftArmVisibilityButton;
    private boolean isInnerLeftArmVisible;
    private boolean isOuterLeftArmVisible;
    
    private TextFieldWidget rightArmPivotXField;
    private TextFieldWidget rightArmPivotYField;
    private TextFieldWidget rightArmPivotZField;
    private TextFieldWidget rightArmPitchField;
    private TextFieldWidget rightArmYawField;
    private TextFieldWidget rightArmRollField;
    private CyclingButtonWidget<Boolean> toggleInnerRightArmVisibilityButton;
    private CyclingButtonWidget<Boolean> toggleOuterRightArmVisibilityButton;
    private boolean isInnerRightArmVisible;
    private boolean isOuterRightArmVisible;
    
    private TextFieldWidget leftLegPivotXField;
    private TextFieldWidget leftLegPivotYField;
    private TextFieldWidget leftLegPivotZField;
    private TextFieldWidget leftLegPitchField;
    private TextFieldWidget leftLegYawField;
    private TextFieldWidget leftLegRollField;
    private CyclingButtonWidget<Boolean> toggleInnerLeftLegVisibilityButton;
    private CyclingButtonWidget<Boolean> toggleOuterLeftLegVisibilityButton;
    private boolean isInnerLeftLegVisible;
    private boolean isOuterLeftLegVisible;
    
    private TextFieldWidget rightLegPivotXField;
    private TextFieldWidget rightLegPivotYField;
    private TextFieldWidget rightLegPivotZField;
    private TextFieldWidget rightLegPitchField;
    private TextFieldWidget rightLegYawField;
    private TextFieldWidget rightLegRollField;
    private CyclingButtonWidget<Boolean> toggleInnerRightLegVisibilityButton;
    private CyclingButtonWidget<Boolean> toggleOuterRightLegVisibilityButton;
    private boolean isInnerRightLegVisible;
    private boolean isOuterRightLegVisible;
    // endregion model parts widgets

    // region settings
    private CyclingButtonWidget<Boolean> toggleIsSneakingButton;
    private CyclingButtonWidget<Boolean> toggleIsPushableButton;
    private CyclingButtonWidget<Boolean> toggleIsUsingItemButton;
    private CyclingButtonWidget<Boolean> toggleIsLeftHandedButton;
    private CyclingButtonWidget<Boolean> toggleIsBabyButton;
    private CyclingButtonWidget<Boolean> toggleHasVisualFireButton;
    private CyclingButtonWidget<Boolean> toggleIsAffectedByPistonsButton;
    private CyclingButtonWidget<Boolean> toggleHasNoGravityButton;
    private CyclingButtonWidget<Boolean> toggleIsCustomNameVisibleButton;
    private TextFieldWidget customNameField;
    private TextFieldWidget textureIdentifierStringField;
    private CyclingButtonWidget<MannequinEntity.SheathedWeaponMode> toggleSheathedWeaponModeButton;
    private TextFieldWidget entityYawField;
    private TextFieldWidget entityPosXField;
    private TextFieldWidget entityPosYField;
    private TextFieldWidget entityPosZField;
    private ButtonWidget killButton;
    private ButtonWidget changeModelButton;
    private boolean isSneaking;
    private boolean isPushable;
    private boolean isUsingItem;
    private boolean isLeftHanded;
    private boolean isBaby;
    private boolean hasVisualFire;
    private boolean isAffectedByPistons;
    private boolean hasNoGravity;
    private boolean isCustomNameVisible;
    private MannequinEntity.SheathedWeaponMode sheathedWeaponMode;
    // endregion settings

    private ScreenPage screenPage;

    /*TODO
        export build/import build (player trinket itemStacks <-> mannequin trinket itemStacks)
        String name
        boolean showName
        boolean leftHanded
        SheathedWeaponMode sheathedWeaponMode
        entityPos
        yaw
        String textureIdentifierString
        boolean isBaby
        boolean affectedByGravity
        boolean hasVisualFire
        boolean isPushable
        boolean isAffectedByPistons
        kill button
        rotation/translation presets
            sneaking
            holding item
            using item
            main arm
        handler.canEdit controls whether edit options are visible
     */

    public MannequinScreen(MannequinScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
        this.mannequinEntity = this.handler.getMannequinEntity();
        if (this.mannequinEntity == null) {
            this.close();
        } else {
            this.isLeftHanded = this.mannequinEntity.isLeftHanded();
            this.isPushable = this.mannequinEntity.isPushable();
            this.isSneaking = this.mannequinEntity.isInSneakingPose();
            this.isUsingItem = this.mannequinEntity.isUsingItem();
            this.isBaby = this.mannequinEntity.isBaby();
            this.hasVisualFire = this.mannequinEntity.hasVisualFire();
            this.isAffectedByPistons = this.mannequinEntity.isAffectedByPistons();
            this.hasNoGravity = this.mannequinEntity.hasNoGravity();
            this.isCustomNameVisible = this.mannequinEntity.isCustomNameVisible();
            this.sheathedWeaponMode = this.mannequinEntity.getSheathedWeaponMode();
        }
        this.screenPage = ScreenPage.EQUIPMENT;
    }

    private void done() {
//        this.updateMannequinEntity();
        this.updateMannequinModelParts();
        this.close();
    }

    private void cancel() {
        this.close();
    }

    @Override
    protected void init() {
        this.backgroundWidth = 334;
        this.backgroundHeight = 233;
        this.playerInventoryTitleY = 132;
        super.init();

        this.toggleScreenPageButton = this.addDrawableChild(CyclingButtonWidget.builder(ScreenPage::asText).values((ScreenPage[]) ScreenPage.values()).initially(this.screenPage).omitKeyText().build(this.x + 252, this.y + 18, 75, 20, Text.empty(), (button, screenPage) -> {
            this.screenPage = screenPage;
            this.updateWidgets();
        }));

        this.updateTrinketButton0 = this.addDrawableChild(ButtonWidget.builder(UPDATE_EQUIPMENT_BUTTON_LABEL_TEXT, button -> this.updateMannequinEquipment(0)).dimensions(this.x + 86, this.y + 19, 60, 16).build());
        this.updateTrinketButton1 = this.addDrawableChild(ButtonWidget.builder(UPDATE_EQUIPMENT_BUTTON_LABEL_TEXT, button -> this.updateMannequinEquipment(1)).dimensions(this.x + 86, this.y + 37, 60, 16).build());
        this.updateTrinketButton2 = this.addDrawableChild(ButtonWidget.builder(UPDATE_EQUIPMENT_BUTTON_LABEL_TEXT, button -> this.updateMannequinEquipment(2)).dimensions(this.x + 86, this.y + 55, 60, 16).build());
        this.updateTrinketButton3 = this.addDrawableChild(ButtonWidget.builder(UPDATE_EQUIPMENT_BUTTON_LABEL_TEXT, button -> this.updateMannequinEquipment(3)).dimensions(this.x + 86, this.y + 73, 60, 16).build());
        this.updateTrinketButton4 = this.addDrawableChild(ButtonWidget.builder(UPDATE_EQUIPMENT_BUTTON_LABEL_TEXT, button -> this.updateMannequinEquipment(4)).dimensions(this.x + 86, this.y + 91, 60, 16).build());
        this.updateTrinketButton5 = this.addDrawableChild(ButtonWidget.builder(UPDATE_EQUIPMENT_BUTTON_LABEL_TEXT, button -> this.updateMannequinEquipment(5)).dimensions(this.x + 86, this.y + 109, 60, 16).build());
        this.updateTrinketButton6 = this.addDrawableChild(ButtonWidget.builder(UPDATE_EQUIPMENT_BUTTON_LABEL_TEXT, button -> this.updateMannequinEquipment(6)).dimensions(this.x + 169, this.y + 19, 60, 16).build());
        this.updateTrinketButton7 = this.addDrawableChild(ButtonWidget.builder(UPDATE_EQUIPMENT_BUTTON_LABEL_TEXT, button -> this.updateMannequinEquipment(7)).dimensions(this.x + 169, this.y + 37, 60, 16).build());
        this.updateTrinketButton8 = this.addDrawableChild(ButtonWidget.builder(UPDATE_EQUIPMENT_BUTTON_LABEL_TEXT, button -> this.updateMannequinEquipment(8)).dimensions(this.x + 169, this.y + 55, 60, 16).build());
        this.updateTrinketButton9 = this.addDrawableChild(ButtonWidget.builder(UPDATE_EQUIPMENT_BUTTON_LABEL_TEXT, button -> this.updateMannequinEquipment(9)).dimensions(this.x + 169, this.y + 73, 60, 16).build());
        this.updateTrinketButton10 = this.addDrawableChild(ButtonWidget.builder(UPDATE_EQUIPMENT_BUTTON_LABEL_TEXT, button -> this.updateMannequinEquipment(10)).dimensions(this.x + 169, this.y + 91, 60, 16).build());
        this.updateTrinketButton11 = this.addDrawableChild(ButtonWidget.builder(UPDATE_EQUIPMENT_BUTTON_LABEL_TEXT, button -> this.updateMannequinEquipment(11)).dimensions(this.x + 169, this.y + 109, 60, 16).build());
        this.updateTrinketButton12 = this.addDrawableChild(ButtonWidget.builder(UPDATE_EQUIPMENT_BUTTON_LABEL_TEXT, button -> this.updateMannequinEquipment(12)).dimensions(this.x + 7, this.y + 19, 56, 16).build());
        this.updateTrinketButton13 = this.addDrawableChild(ButtonWidget.builder(UPDATE_EQUIPMENT_BUTTON_LABEL_TEXT, button -> this.updateMannequinEquipment(13)).dimensions(this.x + 7, this.y + 37, 56, 16).build());

        // spells
        this.updateTrinketButton14 = this.addDrawableChild(ButtonWidget.builder(UPDATE_EQUIPMENT_BUTTON_LABEL_TEXT, button -> this.updateMannequinEquipment(14)).dimensions(this.x + 7, this.y + 55, 56, 16).build());
        this.updateTrinketButton15 = this.addDrawableChild(ButtonWidget.builder(UPDATE_EQUIPMENT_BUTTON_LABEL_TEXT, button -> this.updateMannequinEquipment(15)).dimensions(this.x + 7, this.y + 73, 56, 16).build());
        this.updateTrinketButton16 = this.addDrawableChild(ButtonWidget.builder(UPDATE_EQUIPMENT_BUTTON_LABEL_TEXT, button -> this.updateMannequinEquipment(16)).dimensions(this.x + 7, this.y + 91, 56, 16).build());
        this.updateTrinketButton17 = this.addDrawableChild(ButtonWidget.builder(UPDATE_EQUIPMENT_BUTTON_LABEL_TEXT, button -> this.updateMannequinEquipment(17)).dimensions(this.x + 7, this.y + 109, 56, 16).build());
        this.updateTrinketButton18 = this.addDrawableChild(ButtonWidget.builder(UPDATE_EQUIPMENT_BUTTON_LABEL_TEXT, button -> this.updateMannequinEquipment(18)).dimensions(this.x + 7, this.y + 127, 56, 16).build());
        this.updateTrinketButton19 = this.addDrawableChild(ButtonWidget.builder(UPDATE_EQUIPMENT_BUTTON_LABEL_TEXT, button -> this.updateMannequinEquipment(19)).dimensions(this.x + 7, this.y + 145, 56, 16).build());
        this.updateTrinketButton20 = this.addDrawableChild(ButtonWidget.builder(UPDATE_EQUIPMENT_BUTTON_LABEL_TEXT, button -> this.updateMannequinEquipment(20)).dimensions(this.x + 7, this.y + 163, 56, 16).build());
        this.updateTrinketButton21 = this.addDrawableChild(ButtonWidget.builder(UPDATE_EQUIPMENT_BUTTON_LABEL_TEXT, button -> this.updateMannequinEquipment(21)).dimensions(this.x + 7, this.y + 181, 56, 16).build());

        this.exportEquipmentButton = this.addDrawableChild(ButtonWidget.builder(EXPORT_EQUIPMENT_BUTTON_LABEL_TEXT, button -> this.exportEquipment()).dimensions(this.x + 86, this.y + 128, 79, 20).build());
        this.importEquipmentButton = this.addDrawableChild(ButtonWidget.builder(IMPORT_EQUIPMENT_BUTTON_LABEL_TEXT, button -> this.importEquipment()).dimensions(this.x + 169, this.y + 128, 79, 20).build());

        int y_offset = 20;
        this.headPivotXField = new TextFieldWidget(this.textRenderer, this.x + 10, this.y + y_offset, 32, 11, Text.empty());
        this.headPivotXField.setText(Float.toString(this.mannequinEntity.getHeadTranslation().x));
        this.headPivotXField.setTooltip(Tooltip.of(Text.translatable("gui.mannequin_entity.head_pivot_x_tooltip")));
        this.headPivotXField.setDrawsBackground(false);
        this.headPivotXField.setChangedListener(target -> this.updateMannequinModelParts());
        this.addSelectableChild(this.headPivotXField);
        this.headPivotYField = new TextFieldWidget(this.textRenderer, this.x + 50, this.y + y_offset, 32, 11, Text.empty());
        this.headPivotYField.setText(Float.toString(this.mannequinEntity.getHeadTranslation().y));
        this.headPivotYField.setTooltip(Tooltip.of(Text.translatable("gui.mannequin_entity.head_pivot_y_tooltip")));
        this.headPivotYField.setDrawsBackground(false);
        this.headPivotYField.setChangedListener(target -> this.updateMannequinModelParts());
        this.addSelectableChild(this.headPivotYField);
        this.headPivotZField = new TextFieldWidget(this.textRenderer, this.x + 90, this.y + y_offset, 32, 11, Text.empty());
        this.headPivotZField.setText(Float.toString(this.mannequinEntity.getHeadTranslation().z));
        this.headPivotZField.setTooltip(Tooltip.of(Text.translatable("gui.mannequin_entity.head_pivot_z_tooltip")));
        this.headPivotZField.setDrawsBackground(false);
        this.headPivotZField.setChangedListener(target -> this.updateMannequinModelParts());
        this.addSelectableChild(this.headPivotZField);
        this.headPitchField = new TextFieldWidget(this.textRenderer, this.x + 133, this.y + y_offset, 32, 11, Text.empty());
        this.headPitchField.setText(Float.toString(this.mannequinEntity.getHeadRotation().getPitch()));
        this.headPitchField.setTooltip(Tooltip.of(Text.translatable("gui.mannequin_entity.head_pitch_tooltip")));
        this.headPitchField.setDrawsBackground(false);
        this.headPitchField.setChangedListener(target -> this.updateMannequinModelParts());
        this.addSelectableChild(this.headPitchField);
        this.headYawField = new TextFieldWidget(this.textRenderer, this.x + 173, this.y + y_offset, 32, 11, Text.empty());
        this.headYawField.setText(Float.toString(this.mannequinEntity.getHeadRotation().getYaw()));
        this.headYawField.setTooltip(Tooltip.of(Text.translatable("gui.mannequin_entity.head_yaw_tooltip")));
        this.headYawField.setDrawsBackground(false);
        this.headYawField.setChangedListener(target -> this.updateMannequinModelParts());
        this.addSelectableChild(this.headYawField);
        this.headRollField = new TextFieldWidget(this.textRenderer, this.x + 213, this.y + y_offset, 32, 11, Text.empty());
        this.headRollField.setText(Float.toString(this.mannequinEntity.getHeadRotation().getRoll()));
        this.headRollField.setTooltip(Tooltip.of(Text.translatable("gui.mannequin_entity.head_roll_tooltip")));
        this.headRollField.setDrawsBackground(false);
        this.headRollField.setChangedListener(target -> this.updateMannequinModelParts());
        this.addSelectableChild(this.headRollField);
        
        y_offset += Y_OFFSET_INCREASE_1;
        this.isInnerHeadVisible = this.mannequinEntity.isModelPartVisible(MannequinModelPart.HEAD);
        this.toggleInnerHeadVisibilityButton = this.addDrawableChild(CyclingButtonWidget.onOffBuilder(Text.translatable("gui.mannequin_entity.inner_head_visible_button_label"), Text.translatable("gui.mannequin_entity.inner_head_invisible_button_label")).initially(this.isInnerHeadVisible).omitKeyText().build(this.x + 7, this.y + y_offset, 117, 16, Text.empty(), (button, isInnerHeadVisible) -> {
            this.isInnerHeadVisible = isInnerHeadVisible;
            this.updateMannequinModelParts();
        }));
        this.isOuterHeadVisible = this.mannequinEntity.isModelPartVisible(MannequinModelPart.HAT);
        this.toggleOuterHeadVisibilityButton = this.addDrawableChild(CyclingButtonWidget.onOffBuilder(Text.translatable("gui.mannequin_entity.outer_head_visible_button_label"), Text.translatable("gui.mannequin_entity.outer_head_invisible_button_label")).initially(this.isOuterHeadVisible).omitKeyText().build(this.x + 130, this.y + y_offset, 117, 16, Text.empty(), (button, isOuterHeadVisible) -> {
            this.isOuterHeadVisible = isOuterHeadVisible;
            this.updateMannequinModelParts();
        }));

        y_offset += Y_OFFSET_INCREASE_2;
        this.bodyPivotXField = new TextFieldWidget(this.textRenderer, this.x + 10, this.y + y_offset, 32, 11, Text.empty());
        this.bodyPivotXField.setText(Float.toString(this.mannequinEntity.getBodyTranslation().x));
        this.bodyPivotXField.setTooltip(Tooltip.of(Text.translatable("gui.mannequin_entity.body_pivot_x_tooltip")));
        this.bodyPivotXField.setDrawsBackground(false);
        this.bodyPivotXField.setChangedListener(target -> this.updateMannequinModelParts());
        this.addSelectableChild(this.bodyPivotXField);
        this.bodyPivotYField = new TextFieldWidget(this.textRenderer, this.x + 50, this.y + y_offset, 32, 11, Text.empty());
        this.bodyPivotYField.setText(Float.toString(this.mannequinEntity.getBodyTranslation().y));
        this.bodyPivotYField.setTooltip(Tooltip.of(Text.translatable("gui.mannequin_entity.body_pivot_y_tooltip")));
        this.bodyPivotYField.setDrawsBackground(false);
        this.bodyPivotYField.setChangedListener(target -> this.updateMannequinModelParts());
        this.addSelectableChild(this.bodyPivotYField);
        this.bodyPivotZField = new TextFieldWidget(this.textRenderer, this.x + 90, this.y + y_offset, 32, 11, Text.empty());
        this.bodyPivotZField.setText(Float.toString(this.mannequinEntity.getBodyTranslation().z));
        this.bodyPivotZField.setTooltip(Tooltip.of(Text.translatable("gui.mannequin_entity.body_pivot_z_tooltip")));
        this.bodyPivotZField.setDrawsBackground(false);
        this.bodyPivotZField.setChangedListener(target -> this.updateMannequinModelParts());
        this.addSelectableChild(this.bodyPivotZField);
        this.bodyPitchField = new TextFieldWidget(this.textRenderer, this.x + 133, this.y + y_offset, 32, 11, Text.empty());
        this.bodyPitchField.setText(Float.toString(this.mannequinEntity.getBodyRotation().getPitch()));
        this.bodyPitchField.setTooltip(Tooltip.of(Text.translatable("gui.mannequin_entity.body_pitch_tooltip")));
        this.bodyPitchField.setDrawsBackground(false);
        this.bodyPitchField.setChangedListener(target -> this.updateMannequinModelParts());
        this.addSelectableChild(this.bodyPitchField);
        this.bodyYawField = new TextFieldWidget(this.textRenderer, this.x + 173, this.y + y_offset, 32, 11, Text.empty());
        this.bodyYawField.setText(Float.toString(this.mannequinEntity.getBodyRotation().getYaw()));
        this.bodyYawField.setTooltip(Tooltip.of(Text.translatable("gui.mannequin_entity.body_yaw_tooltip")));
        this.bodyYawField.setDrawsBackground(false);
        this.bodyYawField.setChangedListener(target -> this.updateMannequinModelParts());
        this.addSelectableChild(this.bodyYawField);
        this.bodyRollField = new TextFieldWidget(this.textRenderer, this.x + 213, this.y + y_offset, 32, 11, Text.empty());
        this.bodyRollField.setText(Float.toString(this.mannequinEntity.getBodyRotation().getRoll()));
        this.bodyRollField.setTooltip(Tooltip.of(Text.translatable("gui.mannequin_entity.body_roll_tooltip")));
        this.bodyRollField.setDrawsBackground(false);
        this.bodyRollField.setChangedListener(target -> this.updateMannequinModelParts());
        this.addSelectableChild(this.bodyRollField);

        y_offset += Y_OFFSET_INCREASE_1;
        this.isInnerBodyVisible = this.mannequinEntity.isModelPartVisible(MannequinModelPart.BODY);
        this.toggleInnerBodyVisibilityButton = this.addDrawableChild(CyclingButtonWidget.onOffBuilder(Text.translatable("gui.mannequin_entity.inner_body_visible_button_label"), Text.translatable("gui.mannequin_entity.inner_body_invisible_button_label")).initially(this.isInnerBodyVisible).omitKeyText().build(this.x + 7, this.y + y_offset, 117, 16, Text.empty(), (button, isInnerBodyVisible) -> {
            this.isInnerBodyVisible = isInnerBodyVisible;
            this.updateMannequinModelParts();
        }));
        this.isOuterBodyVisible = this.mannequinEntity.isModelPartVisible(MannequinModelPart.JACKET);
        this.toggleOuterBodyVisibilityButton = this.addDrawableChild(CyclingButtonWidget.onOffBuilder(Text.translatable("gui.mannequin_entity.outer_body_visible_button_label"), Text.translatable("gui.mannequin_entity.outer_body_invisible_button_label")).initially(this.isOuterBodyVisible).omitKeyText().build(this.x + 130, this.y + y_offset, 117, 16, Text.empty(), (button, isOuterBodyVisible) -> {
            this.isOuterBodyVisible = isOuterBodyVisible;
            this.updateMannequinModelParts();
        }));

        y_offset += Y_OFFSET_INCREASE_2;
        this.leftArmPivotXField = new TextFieldWidget(this.textRenderer, this.x + 10, this.y + y_offset, 32, 11, Text.empty());
        this.leftArmPivotXField.setText(Float.toString(this.mannequinEntity.getLeftArmTranslation().x));
        this.leftArmPivotXField.setTooltip(Tooltip.of(Text.translatable("gui.mannequin_entity.leftArm_pivot_x_tooltip")));
        this.leftArmPivotXField.setDrawsBackground(false);
        this.leftArmPivotXField.setChangedListener(target -> this.updateMannequinModelParts());
        this.addSelectableChild(this.leftArmPivotXField);
        this.leftArmPivotYField = new TextFieldWidget(this.textRenderer, this.x + 50, this.y + y_offset, 32, 11, Text.empty());
        this.leftArmPivotYField.setText(Float.toString(this.mannequinEntity.getLeftArmTranslation().y));
        this.leftArmPivotYField.setTooltip(Tooltip.of(Text.translatable("gui.mannequin_entity.leftArm_pivot_y_tooltip")));
        this.leftArmPivotYField.setDrawsBackground(false);
        this.leftArmPivotYField.setChangedListener(target -> this.updateMannequinModelParts());
        this.addSelectableChild(this.leftArmPivotYField);
        this.leftArmPivotZField = new TextFieldWidget(this.textRenderer, this.x + 90, this.y + y_offset, 32, 11, Text.empty());
        this.leftArmPivotZField.setText(Float.toString(this.mannequinEntity.getLeftArmTranslation().z));
        this.leftArmPivotZField.setTooltip(Tooltip.of(Text.translatable("gui.mannequin_entity.leftArm_pivot_z_tooltip")));
        this.leftArmPivotZField.setDrawsBackground(false);
        this.leftArmPivotZField.setChangedListener(target -> this.updateMannequinModelParts());
        this.addSelectableChild(this.leftArmPivotZField);
        this.leftArmPitchField = new TextFieldWidget(this.textRenderer, this.x + 133, this.y + y_offset, 32, 11, Text.empty());
        this.leftArmPitchField.setText(Float.toString(this.mannequinEntity.getLeftArmRotation().getPitch()));
        this.leftArmPitchField.setTooltip(Tooltip.of(Text.translatable("gui.mannequin_entity.leftArm_pitch_tooltip")));
        this.leftArmPitchField.setDrawsBackground(false);
        this.leftArmPitchField.setChangedListener(target -> this.updateMannequinModelParts());
        this.addSelectableChild(this.leftArmPitchField);
        this.leftArmYawField = new TextFieldWidget(this.textRenderer, this.x + 173, this.y + y_offset, 32, 11, Text.empty());
        this.leftArmYawField.setText(Float.toString(this.mannequinEntity.getLeftArmRotation().getYaw()));
        this.leftArmYawField.setTooltip(Tooltip.of(Text.translatable("gui.mannequin_entity.leftArm_yaw_tooltip")));
        this.leftArmYawField.setDrawsBackground(false);
        this.leftArmYawField.setChangedListener(target -> this.updateMannequinModelParts());
        this.addSelectableChild(this.leftArmYawField);
        this.leftArmRollField = new TextFieldWidget(this.textRenderer, this.x + 213, this.y + y_offset, 32, 11, Text.empty());
        this.leftArmRollField.setText(Float.toString(this.mannequinEntity.getLeftArmRotation().getRoll()));
        this.leftArmRollField.setTooltip(Tooltip.of(Text.translatable("gui.mannequin_entity.leftArm_roll_tooltip")));
        this.leftArmRollField.setDrawsBackground(false);
        this.leftArmRollField.setChangedListener(target -> this.updateMannequinModelParts());
        this.addSelectableChild(this.leftArmRollField);

        y_offset += Y_OFFSET_INCREASE_1;
        this.isInnerLeftArmVisible = this.mannequinEntity.isModelPartVisible(MannequinModelPart.LEFT_ARM);
        this.toggleInnerLeftArmVisibilityButton = this.addDrawableChild(CyclingButtonWidget.onOffBuilder(Text.translatable("gui.mannequin_entity.inner_leftArm_visible_button_label"), Text.translatable("gui.mannequin_entity.inner_leftArm_invisible_button_label")).initially(this.isInnerLeftArmVisible).omitKeyText().build(this.x + 7, this.y + y_offset, 117, 16, Text.empty(), (button, isInnerLeftArmVisible) -> {
            this.isInnerLeftArmVisible = isInnerLeftArmVisible;
            this.updateMannequinModelParts();
        }));
        this.isOuterLeftArmVisible = this.mannequinEntity.isModelPartVisible(MannequinModelPart.LEFT_SLEEVE);
        this.toggleOuterLeftArmVisibilityButton = this.addDrawableChild(CyclingButtonWidget.onOffBuilder(Text.translatable("gui.mannequin_entity.outer_leftArm_visible_button_label"), Text.translatable("gui.mannequin_entity.outer_leftArm_invisible_button_label")).initially(this.isOuterLeftArmVisible).omitKeyText().build(this.x + 130, this.y + y_offset, 117, 16, Text.empty(), (button, isOuterLeftArmVisible) -> {
            this.isOuterLeftArmVisible = isOuterLeftArmVisible;
            this.updateMannequinModelParts();
        }));

        y_offset += Y_OFFSET_INCREASE_2;
        this.rightArmPivotXField = new TextFieldWidget(this.textRenderer, this.x + 10, this.y + y_offset, 32, 11, Text.empty());
        this.rightArmPivotXField.setText(Float.toString(this.mannequinEntity.getRightArmTranslation().x));
        this.rightArmPivotXField.setTooltip(Tooltip.of(Text.translatable("gui.mannequin_entity.rightArm_pivot_x_tooltip")));
        this.rightArmPivotXField.setDrawsBackground(false);
        this.rightArmPivotXField.setChangedListener(target -> this.updateMannequinModelParts());
        this.addSelectableChild(this.rightArmPivotXField);
        this.rightArmPivotYField = new TextFieldWidget(this.textRenderer, this.x + 50, this.y + y_offset, 32, 11, Text.empty());
        this.rightArmPivotYField.setText(Float.toString(this.mannequinEntity.getRightArmTranslation().y));
        this.rightArmPivotYField.setTooltip(Tooltip.of(Text.translatable("gui.mannequin_entity.rightArm_pivot_y_tooltip")));
        this.rightArmPivotYField.setDrawsBackground(false);
        this.rightArmPivotYField.setChangedListener(target -> this.updateMannequinModelParts());
        this.addSelectableChild(this.rightArmPivotYField);
        this.rightArmPivotZField = new TextFieldWidget(this.textRenderer, this.x + 90, this.y + y_offset, 32, 11, Text.empty());
        this.rightArmPivotZField.setText(Float.toString(this.mannequinEntity.getRightArmTranslation().z));
        this.rightArmPivotZField.setTooltip(Tooltip.of(Text.translatable("gui.mannequin_entity.rightArm_pivot_z_tooltip")));
        this.rightArmPivotZField.setDrawsBackground(false);
        this.rightArmPivotZField.setChangedListener(target -> this.updateMannequinModelParts());
        this.addSelectableChild(this.rightArmPivotZField);
        this.rightArmPitchField = new TextFieldWidget(this.textRenderer, this.x + 133, this.y + y_offset, 32, 11, Text.empty());
        this.rightArmPitchField.setText(Float.toString(this.mannequinEntity.getRightArmRotation().getPitch()));
        this.rightArmPitchField.setTooltip(Tooltip.of(Text.translatable("gui.mannequin_entity.rightArm_pitch_tooltip")));
        this.rightArmPitchField.setDrawsBackground(false);
        this.rightArmPitchField.setChangedListener(target -> this.updateMannequinModelParts());
        this.addSelectableChild(this.rightArmPitchField);
        this.rightArmYawField = new TextFieldWidget(this.textRenderer, this.x + 173, this.y + y_offset, 32, 11, Text.empty());
        this.rightArmYawField.setText(Float.toString(this.mannequinEntity.getRightArmRotation().getYaw()));
        this.rightArmYawField.setTooltip(Tooltip.of(Text.translatable("gui.mannequin_entity.rightArm_yaw_tooltip")));
        this.rightArmYawField.setDrawsBackground(false);
        this.rightArmYawField.setChangedListener(target -> this.updateMannequinModelParts());
        this.addSelectableChild(this.rightArmYawField);
        this.rightArmRollField = new TextFieldWidget(this.textRenderer, this.x + 213, this.y + y_offset, 32, 11, Text.empty());
        this.rightArmRollField.setText(Float.toString(this.mannequinEntity.getRightArmRotation().getRoll()));
        this.rightArmRollField.setTooltip(Tooltip.of(Text.translatable("gui.mannequin_entity.rightArm_roll_tooltip")));
        this.rightArmRollField.setDrawsBackground(false);
        this.rightArmRollField.setChangedListener(target -> this.updateMannequinModelParts());
        this.addSelectableChild(this.rightArmRollField);

        y_offset += Y_OFFSET_INCREASE_1;
        this.isInnerRightArmVisible = this.mannequinEntity.isModelPartVisible(MannequinModelPart.RIGHT_ARM);
        this.toggleInnerRightArmVisibilityButton = this.addDrawableChild(CyclingButtonWidget.onOffBuilder(Text.translatable("gui.mannequin_entity.inner_rightArm_visible_button_label"), Text.translatable("gui.mannequin_entity.inner_rightArm_invisible_button_label")).initially(this.isInnerRightArmVisible).omitKeyText().build(this.x + 7, this.y + y_offset, 117, 16, Text.empty(), (button, isInnerRightArmVisible) -> {
            this.isInnerRightArmVisible = isInnerRightArmVisible;
            this.updateMannequinModelParts();
        }));
        this.isOuterRightArmVisible = this.mannequinEntity.isModelPartVisible(MannequinModelPart.RIGHT_SLEEVE);
        this.toggleOuterRightArmVisibilityButton = this.addDrawableChild(CyclingButtonWidget.onOffBuilder(Text.translatable("gui.mannequin_entity.outer_rightArm_visible_button_label"), Text.translatable("gui.mannequin_entity.outer_rightArm_invisible_button_label")).initially(this.isOuterRightArmVisible).omitKeyText().build(this.x + 130, this.y + y_offset, 117, 16, Text.empty(), (button, isOuterRightArmVisible) -> {
            this.isOuterRightArmVisible = isOuterRightArmVisible;
            this.updateMannequinModelParts();
        }));

        y_offset += Y_OFFSET_INCREASE_2;
        this.leftLegPivotXField = new TextFieldWidget(this.textRenderer, this.x + 10, this.y + y_offset, 32, 11, Text.empty());
        this.leftLegPivotXField.setText(Float.toString(this.mannequinEntity.getLeftLegTranslation().x));
        this.leftLegPivotXField.setTooltip(Tooltip.of(Text.translatable("gui.mannequin_entity.leftLeg_pivot_x_tooltip")));
        this.leftLegPivotXField.setDrawsBackground(false);
        this.leftLegPivotXField.setChangedListener(target -> this.updateMannequinModelParts());
        this.addSelectableChild(this.leftLegPivotXField);
        this.leftLegPivotYField = new TextFieldWidget(this.textRenderer, this.x + 50, this.y + y_offset, 32, 11, Text.empty());
        this.leftLegPivotYField.setText(Float.toString(this.mannequinEntity.getLeftLegTranslation().y));
        this.leftLegPivotYField.setTooltip(Tooltip.of(Text.translatable("gui.mannequin_entity.leftLeg_pivot_y_tooltip")));
        this.leftLegPivotYField.setDrawsBackground(false);
        this.leftLegPivotYField.setChangedListener(target -> this.updateMannequinModelParts());
        this.addSelectableChild(this.leftLegPivotYField);
        this.leftLegPivotZField = new TextFieldWidget(this.textRenderer, this.x + 90, this.y + y_offset, 32, 11, Text.empty());
        this.leftLegPivotZField.setText(Float.toString(this.mannequinEntity.getLeftLegTranslation().z));
        this.leftLegPivotZField.setTooltip(Tooltip.of(Text.translatable("gui.mannequin_entity.leftLeg_pivot_z_tooltip")));
        this.leftLegPivotZField.setDrawsBackground(false);
        this.leftLegPivotZField.setChangedListener(target -> this.updateMannequinModelParts());
        this.addSelectableChild(this.leftLegPivotZField);
        this.leftLegPitchField = new TextFieldWidget(this.textRenderer, this.x + 133, this.y + y_offset, 32, 11, Text.empty());
        this.leftLegPitchField.setText(Float.toString(this.mannequinEntity.getLeftLegRotation().getPitch()));
        this.leftLegPitchField.setTooltip(Tooltip.of(Text.translatable("gui.mannequin_entity.leftLeg_pitch_tooltip")));
        this.leftLegPitchField.setDrawsBackground(false);
        this.leftLegPitchField.setChangedListener(target -> this.updateMannequinModelParts());
        this.addSelectableChild(this.leftLegPitchField);
        this.leftLegYawField = new TextFieldWidget(this.textRenderer, this.x + 173, this.y + y_offset, 32, 11, Text.empty());
        this.leftLegYawField.setText(Float.toString(this.mannequinEntity.getLeftLegRotation().getYaw()));
        this.leftLegYawField.setTooltip(Tooltip.of(Text.translatable("gui.mannequin_entity.leftLeg_yaw_tooltip")));
        this.leftLegYawField.setDrawsBackground(false);
        this.leftLegYawField.setChangedListener(target -> this.updateMannequinModelParts());
        this.addSelectableChild(this.leftLegYawField);
        this.leftLegRollField = new TextFieldWidget(this.textRenderer, this.x + 213, this.y + y_offset, 32, 11, Text.empty());
        this.leftLegRollField.setText(Float.toString(this.mannequinEntity.getLeftLegRotation().getRoll()));
        this.leftLegRollField.setTooltip(Tooltip.of(Text.translatable("gui.mannequin_entity.leftLeg_roll_tooltip")));
        this.leftLegRollField.setDrawsBackground(false);
        this.leftLegRollField.setChangedListener(target -> this.updateMannequinModelParts());
        this.addSelectableChild(this.leftLegRollField);

        y_offset += Y_OFFSET_INCREASE_1;
        this.isInnerLeftLegVisible = this.mannequinEntity.isModelPartVisible(MannequinModelPart.LEFT_LEG);
        this.toggleInnerLeftLegVisibilityButton = this.addDrawableChild(CyclingButtonWidget.onOffBuilder(Text.translatable("gui.mannequin_entity.inner_leftLeg_visible_button_label"), Text.translatable("gui.mannequin_entity.inner_leftLeg_invisible_button_label")).initially(this.isInnerLeftLegVisible).omitKeyText().build(this.x + 7, this.y + y_offset, 117, 16, Text.empty(), (button, isInnerLeftLegVisible) -> {
            this.isInnerLeftLegVisible = isInnerLeftLegVisible;
            this.updateMannequinModelParts();
        }));
        this.isOuterLeftLegVisible = this.mannequinEntity.isModelPartVisible(MannequinModelPart.LEFT_PANTS);
        this.toggleOuterLeftLegVisibilityButton = this.addDrawableChild(CyclingButtonWidget.onOffBuilder(Text.translatable("gui.mannequin_entity.outer_leftLeg_visible_button_label"), Text.translatable("gui.mannequin_entity.outer_leftLeg_invisible_button_label")).initially(this.isOuterLeftLegVisible).omitKeyText().build(this.x + 130, this.y + y_offset, 117, 16, Text.empty(), (button, isOuterLeftLegVisible) -> {
            this.isOuterLeftLegVisible = isOuterLeftLegVisible;
            this.updateMannequinModelParts();
        }));

        y_offset += Y_OFFSET_INCREASE_2;
        this.rightLegPivotXField = new TextFieldWidget(this.textRenderer, this.x + 10, this.y + y_offset, 32, 11, Text.empty());
        this.rightLegPivotXField.setText(Float.toString(this.mannequinEntity.getRightLegTranslation().x));
        this.rightLegPivotXField.setTooltip(Tooltip.of(Text.translatable("gui.mannequin_entity.rightLeg_pivot_x_tooltip")));
        this.rightLegPivotXField.setDrawsBackground(false);
        this.rightLegPivotXField.setChangedListener(target -> this.updateMannequinModelParts());
        this.addSelectableChild(this.rightLegPivotXField);
        this.rightLegPivotYField = new TextFieldWidget(this.textRenderer, this.x + 50, this.y + y_offset, 32, 11, Text.empty());
        this.rightLegPivotYField.setText(Float.toString(this.mannequinEntity.getRightLegTranslation().y));
        this.rightLegPivotYField.setTooltip(Tooltip.of(Text.translatable("gui.mannequin_entity.rightLeg_pivot_y_tooltip")));
        this.rightLegPivotYField.setDrawsBackground(false);
        this.rightLegPivotYField.setChangedListener(target -> this.updateMannequinModelParts());
        this.addSelectableChild(this.rightLegPivotYField);
        this.rightLegPivotZField = new TextFieldWidget(this.textRenderer, this.x + 90, this.y + y_offset, 32, 11, Text.empty());
        this.rightLegPivotZField.setText(Float.toString(this.mannequinEntity.getRightLegTranslation().z));
        this.rightLegPivotZField.setTooltip(Tooltip.of(Text.translatable("gui.mannequin_entity.rightLeg_pivot_z_tooltip")));
        this.rightLegPivotZField.setDrawsBackground(false);
        this.rightLegPivotZField.setChangedListener(target -> this.updateMannequinModelParts());
        this.addSelectableChild(this.rightLegPivotZField);
        this.rightLegPitchField = new TextFieldWidget(this.textRenderer, this.x + 133, this.y + y_offset, 32, 11, Text.empty());
        this.rightLegPitchField.setText(Float.toString(this.mannequinEntity.getRightLegRotation().getPitch()));
        this.rightLegPitchField.setTooltip(Tooltip.of(Text.translatable("gui.mannequin_entity.rightLeg_pitch_tooltip")));
        this.rightLegPitchField.setDrawsBackground(false);
        this.rightLegPitchField.setChangedListener(target -> this.updateMannequinModelParts());
        this.addSelectableChild(this.rightLegPitchField);
        this.rightLegYawField = new TextFieldWidget(this.textRenderer, this.x + 173, this.y + y_offset, 32, 11, Text.empty());
        this.rightLegYawField.setText(Float.toString(this.mannequinEntity.getRightLegRotation().getYaw()));
        this.rightLegYawField.setTooltip(Tooltip.of(Text.translatable("gui.mannequin_entity.rightLeg_yaw_tooltip")));
        this.rightLegYawField.setDrawsBackground(false);
        this.rightLegYawField.setChangedListener(target -> this.updateMannequinModelParts());
        this.addSelectableChild(this.rightLegYawField);
        this.rightLegRollField = new TextFieldWidget(this.textRenderer, this.x + 213, this.y + y_offset, 32, 11, Text.empty());
        this.rightLegRollField.setText(Float.toString(this.mannequinEntity.getRightLegRotation().getRoll()));
        this.rightLegRollField.setTooltip(Tooltip.of(Text.translatable("gui.mannequin_entity.rightLeg_roll_tooltip")));
        this.rightLegRollField.setDrawsBackground(false);
        this.rightLegRollField.setChangedListener(target -> this.updateMannequinModelParts());
        this.addSelectableChild(this.rightLegRollField);

        y_offset += Y_OFFSET_INCREASE_1;
        this.isInnerRightLegVisible = this.mannequinEntity.isModelPartVisible(MannequinModelPart.RIGHT_LEG);
        this.toggleInnerRightLegVisibilityButton = this.addDrawableChild(CyclingButtonWidget.onOffBuilder(Text.translatable("gui.mannequin_entity.inner_rightLeg_visible_button_label"), Text.translatable("gui.mannequin_entity.inner_rightLeg_invisible_button_label")).initially(this.isInnerRightLegVisible).omitKeyText().build(this.x + 7, this.y + y_offset, 117, 16, Text.empty(), (button, isInnerRightLegVisible) -> {
            this.isInnerRightLegVisible = isInnerRightLegVisible;
            this.updateMannequinModelParts();
        }));
        this.isOuterRightLegVisible = this.mannequinEntity.isModelPartVisible(MannequinModelPart.RIGHT_PANTS);
        this.toggleOuterRightLegVisibilityButton = this.addDrawableChild(CyclingButtonWidget.onOffBuilder(Text.translatable("gui.mannequin_entity.outer_rightLeg_visible_button_label"), Text.translatable("gui.mannequin_entity.outer_rightLeg_invisible_button_label")).initially(this.isOuterRightLegVisible).omitKeyText().build(this.x + 130, this.y + y_offset, 117, 16, Text.empty(), (button, isOuterRightLegVisible) -> {
            this.isOuterRightLegVisible = isOuterRightLegVisible;
            this.updateMannequinModelParts();
        }));


        this.toggleIsSneakingButton = this.addDrawableChild(CyclingButtonWidget.onOffBuilder(Text.translatable("gui.mannequin_entity.toggle_is_sneaking_button_label.on"), Text.translatable("gui.mannequin_entity.toggle_is_sneaking_button_label.off")).initially(this.isSneaking).omitKeyText().build(this.x + 7, this.y + 18, 118, 20, Text.empty(), (button, isSneaking) -> {
            this.isSneaking = isSneaking;
            this.updateMannequinSettings();
        }));
        this.toggleIsPushableButton = this.addDrawableChild(CyclingButtonWidget.onOffBuilder(Text.translatable("gui.mannequin_entity.toggle_is_pushable_button_label.on"), Text.translatable("gui.mannequin_entity.toggle_is_pushable_button_label.off")).initially(this.isPushable).omitKeyText().build(this.x + 130, this.y + 18, 118, 20, Text.empty(), (button, isPushable) -> {
            this.isPushable = isPushable;
            this.updateMannequinSettings();
        }));
        this.toggleIsUsingItemButton = this.addDrawableChild(CyclingButtonWidget.onOffBuilder(Text.translatable("gui.mannequin_entity.toggle_is_using_item_button_label.on"), Text.translatable("gui.mannequin_entity.toggle_is_using_item_button_label.off")).initially(this.isUsingItem).omitKeyText().build(this.x + 7, this.y + 42, 118, 20, Text.empty(), (button, isUsingItem) -> {
            this.isUsingItem = isUsingItem;
            this.updateMannequinSettings();
        }));
        this.toggleIsLeftHandedButton = this.addDrawableChild(CyclingButtonWidget.onOffBuilder(Text.translatable("gui.mannequin_entity.toggle_is_left_handed_button_label.on"), Text.translatable("gui.mannequin_entity.toggle_is_left_handed_button_label.off")).initially(this.isLeftHanded).omitKeyText().build(this.x + 130, this.y + 42, 118, 20, Text.empty(), (button, isLeftHanded) -> {
            this.isLeftHanded = isLeftHanded;
            this.updateMannequinSettings();
        }));
        this.toggleIsBabyButton = this.addDrawableChild(CyclingButtonWidget.onOffBuilder(Text.translatable("gui.mannequin_entity.toggle_is_baby_button_label.on"), Text.translatable("gui.mannequin_entity.toggle_is_baby_button_label.off")).initially(this.isBaby).omitKeyText().build(this.x + 7, this.y + 66, 118, 20, Text.empty(), (button, isBaby) -> {
            this.isBaby = isBaby;
            this.updateMannequinSettings();
        }));
        this.toggleHasVisualFireButton = this.addDrawableChild(CyclingButtonWidget.onOffBuilder(Text.translatable("gui.mannequin_entity.toggle_has_visual_fire_button_label.on"), Text.translatable("gui.mannequin_entity.toggle_has_visual_fire_button_label.off")).initially(this.hasVisualFire).omitKeyText().build(this.x + 130, this.y + 66, 118, 20, Text.empty(), (button, hasVisualFire) -> {
            this.hasVisualFire = hasVisualFire;
            this.updateMannequinSettings();
        }));
        this.toggleIsAffectedByPistonsButton = this.addDrawableChild(CyclingButtonWidget.onOffBuilder(Text.translatable("gui.mannequin_entity.toggle_is_affected_by_pistons_button_label.on"), Text.translatable("gui.mannequin_entity.toggle_is_affected_by_pistons_button_label.off")).initially(this.isAffectedByPistons).omitKeyText().build(this.x + 7, this.y + 90, 118, 20, Text.empty(), (button, isAffectedByPistons) -> {
            this.isAffectedByPistons = isAffectedByPistons;
            this.updateMannequinSettings();
        }));
        this.toggleHasNoGravityButton = this.addDrawableChild(CyclingButtonWidget.onOffBuilder(Text.translatable("gui.mannequin_entity.toggle_has_no_gravity_button_label.on"), Text.translatable("gui.mannequin_entity.toggle_has_no_gravity_button_label.off")).initially(this.hasNoGravity).omitKeyText().build(this.x + 130, this.y + 90, 118, 20, Text.empty(), (button, hasNoGravity) -> {
            this.hasNoGravity = hasNoGravity;
            this.updateMannequinSettings();
        }));
        this.toggleIsCustomNameVisibleButton = this.addDrawableChild(CyclingButtonWidget.onOffBuilder(Text.translatable("gui.mannequin_entity.toggle_is_custom_name_visible_button_label.on"), Text.translatable("gui.mannequin_entity.toggle_is_custom_name_visible_button_label.off")).initially(this.isCustomNameVisible).omitKeyText().build(this.x + 7, this.y + 114, 118, 20, Text.empty(), (button, isCustomNameVisible) -> {
            this.isCustomNameVisible = isCustomNameVisible;
            this.updateMannequinSettings();
        }));
        this.customNameField = new TextFieldWidget(this.textRenderer, this.x + 10, this.y + 147, 112, 11, Text.translatable(""));
        this.customNameField.setText(this.mannequinEntity.getCustomNameString());
        this.customNameField.setTooltip(Tooltip.of(Text.translatable("gui.mannequin_entity.custom_name_field_tooltip")));
        this.customNameField.setDrawsBackground(false);
        this.customNameField.setChangedListener(target -> this.updateMannequinSettings());
        this.addSelectableChild(this.customNameField);
        
        this.textureIdentifierStringField = new TextFieldWidget(this.textRenderer, this.x + 133, this.y + 147, 112, 11, Text.translatable(""));
        this.textureIdentifierStringField.setText(this.mannequinEntity.getTextureIdentifierString());
        this.textureIdentifierStringField.setTooltip(Tooltip.of(Text.translatable("gui.mannequin_entity.texture_identifier_field_tooltip")));
        this.textureIdentifierStringField.setDrawsBackground(false);
        this.textureIdentifierStringField.setChangedListener(target -> this.updateMannequinSettings());
        this.addSelectableChild(this.textureIdentifierStringField);
        
        this.toggleSheathedWeaponModeButton = this.addDrawableChild(CyclingButtonWidget.builder(MannequinEntity.SheathedWeaponMode::asText).values((MannequinEntity.SheathedWeaponMode[]) MannequinEntity.SheathedWeaponMode.values()).initially(this.sheathedWeaponMode).omitKeyText().build(this.x + 130, this.y + 114, 118, 20, Text.empty(), (button, sheathedWeaponMode) -> {
            this.sheathedWeaponMode = sheathedWeaponMode;
            this.updateMannequinSettings();
        }));

        this.entityYawField = new TextFieldWidget(this.textRenderer, this.x + 10, this.y + 166, 32, 11, Text.translatable(""));
        this.entityYawField.setText(Float.toString(this.mannequinEntity.getYaw()));
        this.entityYawField.setTooltip(Tooltip.of(Text.translatable("gui.mannequin_entity.yaw_field_tooltip")));
        this.entityYawField.setDrawsBackground(false);
        this.entityYawField.setChangedListener(target -> this.updateMannequinSettings());
        this.addSelectableChild(this.entityYawField);

        this.entityPosXField = new TextFieldWidget(this.textRenderer, this.x + 52, this.y + 166, 59, 11, Text.translatable(""));
        this.entityPosXField.setText(Double.toString(this.mannequinEntity.getPos().x));
        this.entityPosXField.setTooltip(Tooltip.of(Text.translatable("gui.mannequin_entity.pos_x_field_tooltip")));
        this.entityPosXField.setDrawsBackground(false);
        this.entityPosXField.setChangedListener(target -> this.updateMannequinSettings());
        this.addSelectableChild(this.entityPosXField);
        
        this.entityPosYField = new TextFieldWidget(this.textRenderer, this.x + 119, this.y + 166, 59, 11, Text.translatable(""));
        this.entityPosYField.setText(Double.toString(this.mannequinEntity.getPos().y));
        this.entityPosYField.setTooltip(Tooltip.of(Text.translatable("gui.mannequin_entity.pos_y_field_tooltip")));
        this.entityPosYField.setDrawsBackground(false);
        this.entityPosYField.setChangedListener(target -> this.updateMannequinSettings());
        this.addSelectableChild(this.entityPosYField);
        
        this.entityPosZField = new TextFieldWidget(this.textRenderer, this.x + 186, this.y + 166, 59, 11, Text.translatable(""));
        this.entityPosZField.setText(Double.toString(this.mannequinEntity.getPos().z));
        this.entityPosZField.setTooltip(Tooltip.of(Text.translatable("gui.mannequin_entity.pos_z_field_tooltip")));
        this.entityPosZField.setDrawsBackground(false);
        this.entityPosZField.setChangedListener(target -> this.updateMannequinSettings());
        this.addSelectableChild(this.entityPosZField);

        this.changeModelButton = this.addDrawableChild(ButtonWidget.builder(Text.translatable("gui.mannequin_entity.change_model_button_label"), button -> this.removeMannequinEntity(1)).dimensions(this.x + 7, this.y + 206, 118, 20).build());
        this.killButton = this.addDrawableChild(ButtonWidget.builder(Text.translatable("gui.mannequin_entity.kill_button_label"), button -> this.removeMannequinEntity(0)).dimensions(this.x + 130, this.y + 206, 118, 20).build());

//        this.updateTrinketButton5 = this.addDrawableChild(ButtonWidget.builder(UPDATE_TRINKET_BUTTON_LABEL_TEXT, button -> this.updateMannequinTrinket(5)).dimensions(this.x + 7, this.y + 111, 36, 16).build());
//        this.activeMimicBlockPositionOffsetXField = new TextFieldWidget(this.textRenderer, this.width / 2 - 154, 80, 100, 20, Text.translatable(""));
//        this.activeMimicBlockPositionOffsetXField.setText(Integer.toString(this.mimicBlock.getActiveMimicBlockPositionOffset().getX()));
//        this.addSelectableChild(this.activeMimicBlockPositionOffsetXField);
//        this.activeMimicBlockPositionOffsetYField = new TextFieldWidget(this.textRenderer, this.width / 2 - 50, 80, 100, 20, Text.translatable(""));
//        this.activeMimicBlockPositionOffsetYField.setText(Integer.toString(this.mimicBlock.getActiveMimicBlockPositionOffset().getY()));
//        this.addSelectableChild(this.activeMimicBlockPositionOffsetYField);
//        this.activeMimicBlockPositionOffsetZField = new TextFieldWidget(this.textRenderer, this.width / 2 + 54, 80, 100, 20, Text.translatable(""));
//        this.activeMimicBlockPositionOffsetZField.setText(Integer.toString(this.mimicBlock.getActiveMimicBlockPositionOffset().getZ()));
//        this.addSelectableChild(this.activeMimicBlockPositionOffsetZField);
//
//        this.inactiveMimicBlockPositionOffsetXField = new TextFieldWidget(this.textRenderer, this.width / 2 - 154, 115, 100, 20, Text.translatable(""));
//        this.inactiveMimicBlockPositionOffsetXField.setText(Integer.toString(this.mimicBlock.getInactiveMimicBlockPositionOffset().getX()));
//        this.addSelectableChild(this.inactiveMimicBlockPositionOffsetXField);
//        this.inactiveMimicBlockPositionOffsetYField = new TextFieldWidget(this.textRenderer, this.width / 2 - 50, 115, 100, 20, Text.translatable(""));
//        this.inactiveMimicBlockPositionOffsetYField.setText(Integer.toString(this.mimicBlock.getInactiveMimicBlockPositionOffset().getY()));
//        this.addSelectableChild(this.inactiveMimicBlockPositionOffsetYField);
//        this.inactiveMimicBlockPositionOffsetZField = new TextFieldWidget(this.textRenderer, this.width / 2 + 54, 115, 100, 20, Text.translatable(""));
//        this.inactiveMimicBlockPositionOffsetZField.setText(Integer.toString(this.mimicBlock.getInactiveMimicBlockPositionOffset().getZ()));
//        this.addSelectableChild(this.inactiveMimicBlockPositionOffsetZField);
//
//        this.addDrawableChild(ButtonWidget.builder(ScreenTexts.DONE, button -> this.done()).dimensions(this.width / 2 - 4 - 150, 139, 150, 20).build());
//        this.addDrawableChild(ButtonWidget.builder(ScreenTexts.CANCEL, button -> this.cancel()).dimensions(this.width / 2 + 4, 139, 150, 20).build());
//        this.setInitialFocus(this.activeMimicBlockPositionOffsetXField);
//        this.addDrawableChild(ButtonWidget.builder(ScreenTexts.DONE, button -> this.done()).dimensions(this.x + 252, this.y + 154, 75, 20).build());
//        this.addDrawableChild(ButtonWidget.builder(ScreenTexts.CANCEL, button -> this.cancel()).dimensions(this.x + 252, this.y + 178, 75, 20).build());

        this.updateWidgets();
    }

    private void updateWidgets() {

        this.updateTrinketButton0.visible = false;
        this.updateTrinketButton1.visible = false;
        this.updateTrinketButton2.visible = false;
        this.updateTrinketButton3.visible = false;
        this.updateTrinketButton4.visible = false;
        this.updateTrinketButton5.visible = false;
        this.updateTrinketButton6.visible = false;
        this.updateTrinketButton7.visible = false;
        this.updateTrinketButton8.visible = false;
        this.updateTrinketButton9.visible = false;
        this.updateTrinketButton10.visible = false;
        this.updateTrinketButton11.visible = false;
        this.updateTrinketButton12.visible = false;
        this.updateTrinketButton13.visible = false;
        this.updateTrinketButton14.visible = false;
        this.updateTrinketButton15.visible = false;
        this.updateTrinketButton16.visible = false;
        this.updateTrinketButton17.visible = false;
        this.updateTrinketButton18.visible = false;
        this.updateTrinketButton19.visible = false;
        this.updateTrinketButton20.visible = false;
        this.updateTrinketButton21.visible = false;

        this.exportEquipmentButton.visible = false;
        this.importEquipmentButton.visible = false;

        this.headPivotXField.setVisible(false);
        this.headPivotYField.setVisible(false);
        this.headPivotZField.setVisible(false);
        this.headPitchField.setVisible(false);
        this.headYawField.setVisible(false);
        this.headRollField.setVisible(false);
        this.toggleInnerHeadVisibilityButton.visible = false;
        this.toggleOuterHeadVisibilityButton.visible = false;

        this.bodyPivotXField.setVisible(false);
        this.bodyPivotYField.setVisible(false);
        this.bodyPivotZField.setVisible(false);
        this.bodyPitchField.setVisible(false);
        this.bodyYawField.setVisible(false);
        this.bodyRollField.setVisible(false);
        this.toggleInnerBodyVisibilityButton.visible = false;
        this.toggleOuterBodyVisibilityButton.visible = false;

        this.leftArmPivotXField.setVisible(false);
        this.leftArmPivotYField.setVisible(false);
        this.leftArmPivotZField.setVisible(false);
        this.leftArmPitchField.setVisible(false);
        this.leftArmYawField.setVisible(false);
        this.leftArmRollField.setVisible(false);
        this.toggleInnerLeftArmVisibilityButton.visible = false;
        this.toggleOuterLeftArmVisibilityButton.visible = false;

        this.rightArmPivotXField.setVisible(false);
        this.rightArmPivotYField.setVisible(false);
        this.rightArmPivotZField.setVisible(false);
        this.rightArmPitchField.setVisible(false);
        this.rightArmYawField.setVisible(false);
        this.rightArmRollField.setVisible(false);
        this.toggleInnerRightArmVisibilityButton.visible = false;
        this.toggleOuterRightArmVisibilityButton.visible = false;

        this.leftLegPivotXField.setVisible(false);
        this.leftLegPivotYField.setVisible(false);
        this.leftLegPivotZField.setVisible(false);
        this.leftLegPitchField.setVisible(false);
        this.leftLegYawField.setVisible(false);
        this.leftLegRollField.setVisible(false);
        this.toggleInnerLeftLegVisibilityButton.visible = false;
        this.toggleOuterLeftLegVisibilityButton.visible = false;

        this.rightLegPivotXField.setVisible(false);
        this.rightLegPivotYField.setVisible(false);
        this.rightLegPivotZField.setVisible(false);
        this.rightLegPitchField.setVisible(false);
        this.rightLegYawField.setVisible(false);
        this.rightLegRollField.setVisible(false);
        this.toggleInnerRightLegVisibilityButton.visible = false;
        this.toggleOuterRightLegVisibilityButton.visible = false;

        this.toggleIsSneakingButton.visible = false;
        this.toggleIsPushableButton.visible = false;
        this.toggleIsUsingItemButton.visible = false;
        this.toggleIsLeftHandedButton.visible = false;
        this.toggleIsBabyButton.visible = false;
        this.toggleHasVisualFireButton.visible = false;
        this.toggleIsAffectedByPistonsButton.visible = false;
        this.toggleHasNoGravityButton.visible = false;
        this.toggleIsCustomNameVisibleButton.visible = false;
        this.customNameField.setVisible(false);
        this.textureIdentifierStringField.setVisible(false);
        this.toggleSheathedWeaponModeButton.visible = false;
        this.entityYawField.setVisible(false);
        this.entityPosXField.setVisible(false);
        this.entityPosYField.setVisible(false);
        this.entityPosZField.setVisible(false);
        this.killButton.visible = false;
        this.changeModelButton.visible = false;

        if (this.screenPage == ScreenPage.EQUIPMENT) {
            for (Slot slot : this.handler.slots) {
                
                ((DuckSlotMixin) slot).betteradventuremode$setDisabledOverride(false);
                
                this.updateTrinketButton0.visible = true;
                this.updateTrinketButton1.visible = true;
                this.updateTrinketButton2.visible = true;
                this.updateTrinketButton3.visible = true;
                this.updateTrinketButton4.visible = true;
                this.updateTrinketButton5.visible = true;
                this.updateTrinketButton6.visible = true;
                this.updateTrinketButton7.visible = true;
                this.updateTrinketButton8.visible = true;
                this.updateTrinketButton9.visible = true;
                this.updateTrinketButton10.visible = true;
                this.updateTrinketButton11.visible = true;
                this.updateTrinketButton12.visible = true;
                this.updateTrinketButton13.visible = true;
                this.updateTrinketButton14.visible = true;
                this.updateTrinketButton15.visible = true;
                this.updateTrinketButton16.visible = true;
                this.updateTrinketButton17.visible = true;
                this.updateTrinketButton18.visible = true;
                this.updateTrinketButton19.visible = true;
                this.updateTrinketButton20.visible = true;
                this.updateTrinketButton21.visible = true;

                this.exportEquipmentButton.visible = true;
                this.importEquipmentButton.visible = true;
                
            }
        } else if (this.screenPage == ScreenPage.MODEL_PARTS) {
            
            for (Slot slot : this.handler.slots) {
                ((DuckSlotMixin) slot).betteradventuremode$setDisabledOverride(true);
            }

            this.headPivotXField.setVisible(true);
            this.headPivotYField.setVisible(true);
            this.headPivotZField.setVisible(true);
            this.headPitchField.setVisible(true);
            this.headYawField.setVisible(true);
            this.headRollField.setVisible(true);
            this.toggleInnerHeadVisibilityButton.visible = true;
            this.toggleOuterHeadVisibilityButton.visible = true;

            this.bodyPivotXField.setVisible(true);
            this.bodyPivotYField.setVisible(true);
            this.bodyPivotZField.setVisible(true);
            this.bodyPitchField.setVisible(true);
            this.bodyYawField.setVisible(true);
            this.bodyRollField.setVisible(true);
            this.toggleInnerBodyVisibilityButton.visible = true;
            this.toggleOuterBodyVisibilityButton.visible = true;

            this.leftArmPivotXField.setVisible(true);
            this.leftArmPivotYField.setVisible(true);
            this.leftArmPivotZField.setVisible(true);
            this.leftArmPitchField.setVisible(true);
            this.leftArmYawField.setVisible(true);
            this.leftArmRollField.setVisible(true);
            this.toggleInnerLeftArmVisibilityButton.visible = true;
            this.toggleOuterLeftArmVisibilityButton.visible = true;

            this.rightArmPivotXField.setVisible(true);
            this.rightArmPivotYField.setVisible(true);
            this.rightArmPivotZField.setVisible(true);
            this.rightArmPitchField.setVisible(true);
            this.rightArmYawField.setVisible(true);
            this.rightArmRollField.setVisible(true);
            this.toggleInnerRightArmVisibilityButton.visible = true;
            this.toggleOuterRightArmVisibilityButton.visible = true;

            this.leftLegPivotXField.setVisible(true);
            this.leftLegPivotYField.setVisible(true);
            this.leftLegPivotZField.setVisible(true);
            this.leftLegPitchField.setVisible(true);
            this.leftLegYawField.setVisible(true);
            this.leftLegRollField.setVisible(true);
            this.toggleInnerLeftLegVisibilityButton.visible = true;
            this.toggleOuterLeftLegVisibilityButton.visible = true;

            this.rightLegPivotXField.setVisible(true);
            this.rightLegPivotYField.setVisible(true);
            this.rightLegPivotZField.setVisible(true);
            this.rightLegPitchField.setVisible(true);
            this.rightLegYawField.setVisible(true);
            this.rightLegRollField.setVisible(true);
            this.toggleInnerRightLegVisibilityButton.visible = true;
            this.toggleOuterRightLegVisibilityButton.visible = true;

        } else if (this.screenPage == ScreenPage.SETTINGS) {

            for (Slot slot : this.handler.slots) {
                ((DuckSlotMixin) slot).betteradventuremode$setDisabledOverride(true);
            }

            this.toggleIsSneakingButton.visible = true;
            this.toggleIsPushableButton.visible = true;
            this.toggleIsUsingItemButton.visible = true;
            this.toggleIsLeftHandedButton.visible = true;
            this.toggleIsBabyButton.visible = true;
            this.toggleHasVisualFireButton.visible = true;
            this.toggleIsAffectedByPistonsButton.visible = true;
            this.toggleHasNoGravityButton.visible = true;
            this.toggleIsCustomNameVisibleButton.visible = true;
            this.customNameField.setVisible(true);
            this.textureIdentifierStringField.setVisible(true);
            this.toggleSheathedWeaponModeButton.visible = true;
            this.entityYawField.setVisible(true);
            this.entityPosXField.setVisible(true);
            this.entityPosYField.setVisible(true);
            this.entityPosZField.setVisible(true);
            this.killButton.visible = true;
            this.changeModelButton.visible = true;

        }
    }

    @Override
    public void resize(MinecraftClient client, int width, int height) {
//        String string = this.activeMimicBlockPositionOffsetXField.getText();
//        String string1 = this.activeMimicBlockPositionOffsetYField.getText();
//        String string2 = this.activeMimicBlockPositionOffsetZField.getText();
//        String string3 = this.inactiveMimicBlockPositionOffsetXField.getText();
//        String string4 = this.inactiveMimicBlockPositionOffsetYField.getText();
//        String string5 = this.inactiveMimicBlockPositionOffsetZField.getText();
        this.init(client, width, height);
//        this.activeMimicBlockPositionOffsetXField.setText(string);
//        this.activeMimicBlockPositionOffsetYField.setText(string1);
//        this.activeMimicBlockPositionOffsetZField.setText(string2);
//        this.inactiveMimicBlockPositionOffsetXField.setText(string3);
//        this.inactiveMimicBlockPositionOffsetYField.setText(string4);
//        this.inactiveMimicBlockPositionOffsetZField.setText(string5);
    }

//    @Override
//    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
//        if (keyCode == GLFW.GLFW_KEY_ENTER || keyCode == GLFW.GLFW_KEY_KP_ENTER) {
//            this.done();
//            return true;
//        }
//        return super.keyPressed(keyCode, scanCode, modifiers);
//    }

    private void removeMannequinEntity(int flag) {
        ClientPlayNetworking.send(new RemoveMannequinPacket(
                this.mannequinEntity.getId(),
                flag
        ));
    }

    private void updateMannequinSettings() {
        ClientPlayNetworking.send(new UpdateMannequinSettingsPacket(
                this.mannequinEntity.getId(),
                this.isLeftHanded,
                this.isPushable,
                this.isSneaking,
                this.isUsingItem,
                this.isBaby,
                this.hasVisualFire,
                this.isAffectedByPistons,
                this.hasNoGravity,
                this.isCustomNameVisible,
                this.customNameField.getText(),
                this.textureIdentifierStringField.getText(),
                this.sheathedWeaponMode.asString(),
                ItemUtils.parseFloat(this.entityYawField.getText()),
                new Vec3d(
                        ItemUtils.parseDouble(this.entityPosXField.getText()),
                        ItemUtils.parseDouble(this.entityPosYField.getText()),
                        ItemUtils.parseDouble(this.entityPosZField.getText())

                )
        ));
    }

    private void updateMannequinModelParts() {
        ClientPlayNetworking.send(new UpdateMannequinModelPartsPacket(
                this.mannequinEntity.getId(),
                new Vector3f(
                        Float.parseFloat(this.headPitchField.getText()),
                        Float.parseFloat(this.headYawField.getText()),
                        Float.parseFloat(this.headRollField.getText())
                        ),
                new Vector3f(
                        Float.parseFloat(this.bodyPitchField.getText()),
                        Float.parseFloat(this.bodyYawField.getText()),
                        Float.parseFloat(this.bodyRollField.getText())
                        ),
                new Vector3f(
                        Float.parseFloat(this.leftArmPitchField.getText()),
                        Float.parseFloat(this.leftArmYawField.getText()),
                        Float.parseFloat(this.leftArmRollField.getText())
                        ),
                new Vector3f(
                        Float.parseFloat(this.rightArmPitchField.getText()),
                        Float.parseFloat(this.rightArmYawField.getText()),
                        Float.parseFloat(this.rightArmRollField.getText())
                        ),
                new Vector3f(
                        Float.parseFloat(this.leftLegPitchField.getText()),
                        Float.parseFloat(this.leftLegYawField.getText()),
                        Float.parseFloat(this.leftLegRollField.getText())
                        ),
                new Vector3f(
                        Float.parseFloat(this.rightLegPitchField.getText()),
                        Float.parseFloat(this.rightLegYawField.getText()),
                        Float.parseFloat(this.rightLegRollField.getText())
                        ),
                new Vector3f(
                        Float.parseFloat(this.headPivotXField.getText()),
                        Float.parseFloat(this.headPivotYField.getText()),
                        Float.parseFloat(this.headPivotZField.getText())
                ),
                new Vector3f(
                        Float.parseFloat(this.bodyPivotXField.getText()),
                        Float.parseFloat(this.bodyPivotYField.getText()),
                        Float.parseFloat(this.bodyPivotZField.getText())
                ),
                new Vector3f(
                        Float.parseFloat(this.leftArmPivotXField.getText()),
                        Float.parseFloat(this.leftArmPivotYField.getText()),
                        Float.parseFloat(this.leftArmPivotZField.getText())
                ),
                new Vector3f(
                        Float.parseFloat(this.rightArmPivotXField.getText()),
                        Float.parseFloat(this.rightArmPivotYField.getText()),
                        Float.parseFloat(this.rightArmPivotZField.getText())
                ),
                new Vector3f(
                        Float.parseFloat(this.leftLegPivotXField.getText()),
                        Float.parseFloat(this.leftLegPivotYField.getText()),
                        Float.parseFloat(this.leftLegPivotZField.getText())
                ),
                new Vector3f(
                        Float.parseFloat(this.rightLegPivotXField.getText()),
                        Float.parseFloat(this.rightLegPivotYField.getText()),
                        Float.parseFloat(this.rightLegPivotZField.getText())
                ),
                this.isInnerHeadVisible,
                this.isOuterHeadVisible,
                this.isInnerBodyVisible,
                this.isOuterBodyVisible,
                this.isInnerLeftArmVisible,
                this.isOuterLeftArmVisible,
                this.isInnerRightArmVisible,
                this.isOuterRightArmVisible,
                this.isInnerLeftLegVisible,
                this.isOuterLeftLegVisible,
                this.isInnerRightLegVisible,
                this.isOuterRightLegVisible
        ));
    }

    private void updateMannequinEquipment(int index) {
        if (index < 22 && index >= 0) {
            ClientPlayNetworking.send(new UpdateMannequinEquipmentPacket(
                    this.mannequinEntity.getId(),
                    this.handler.slots.get(36 + index).getStack().copy(),
                    index
            ));
        }
    }

    private void exportEquipment() {
        ClientPlayNetworking.send(new ExportImportMannequinEquipmentPacket(
                this.mannequinEntity.getId(),
                true
        ));
    }

    private void importEquipment() {
        ClientPlayNetworking.send(new ExportImportMannequinEquipmentPacket(
                this.mannequinEntity.getId(),
                false
        ));
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);

        if (this.screenPage == ScreenPage.MODEL_PARTS) {
            this.headPivotXField.render(context, mouseX, mouseY, delta);
            this.headPivotYField.render(context, mouseX, mouseY, delta);
            this.headPivotZField.render(context, mouseX, mouseY, delta);
            this.headPitchField.render(context, mouseX, mouseY, delta);
            this.headYawField.render(context, mouseX, mouseY, delta);
            this.headRollField.render(context, mouseX, mouseY, delta);

            this.bodyPivotXField.render(context, mouseX, mouseY, delta);
            this.bodyPivotYField.render(context, mouseX, mouseY, delta);
            this.bodyPivotZField.render(context, mouseX, mouseY, delta);
            this.bodyPitchField.render(context, mouseX, mouseY, delta);
            this.bodyYawField.render(context, mouseX, mouseY, delta);
            this.bodyRollField.render(context, mouseX, mouseY, delta);

            this.leftArmPivotXField.render(context, mouseX, mouseY, delta);
            this.leftArmPivotYField.render(context, mouseX, mouseY, delta);
            this.leftArmPivotZField.render(context, mouseX, mouseY, delta);
            this.leftArmPitchField.render(context, mouseX, mouseY, delta);
            this.leftArmYawField.render(context, mouseX, mouseY, delta);
            this.leftArmRollField.render(context, mouseX, mouseY, delta);

            this.rightArmPivotXField.render(context, mouseX, mouseY, delta);
            this.rightArmPivotYField.render(context, mouseX, mouseY, delta);
            this.rightArmPivotZField.render(context, mouseX, mouseY, delta);
            this.rightArmPitchField.render(context, mouseX, mouseY, delta);
            this.rightArmYawField.render(context, mouseX, mouseY, delta);
            this.rightArmRollField.render(context, mouseX, mouseY, delta);

            this.leftLegPivotXField.render(context, mouseX, mouseY, delta);
            this.leftLegPivotYField.render(context, mouseX, mouseY, delta);
            this.leftLegPivotZField.render(context, mouseX, mouseY, delta);
            this.leftLegPitchField.render(context, mouseX, mouseY, delta);
            this.leftLegYawField.render(context, mouseX, mouseY, delta);
            this.leftLegRollField.render(context, mouseX, mouseY, delta);

            this.rightLegPivotXField.render(context, mouseX, mouseY, delta);
            this.rightLegPivotYField.render(context, mouseX, mouseY, delta);
            this.rightLegPivotZField.render(context, mouseX, mouseY, delta);
            this.rightLegPitchField.render(context, mouseX, mouseY, delta);
            this.rightLegYawField.render(context, mouseX, mouseY, delta);
            this.rightLegRollField.render(context, mouseX, mouseY, delta);

        } else if (this.screenPage == ScreenPage.EQUIPMENT) {
            int slotX;
            int slotY;
            for (int i = 0; i < 22; i++) {
                Slot slot = this.handler.slots.get(36 + i);
                slotX = slot.x;
                slotY = slot.y;
                ItemStack handlerItemStack = slot.getStack().copy();
                ItemStack entityItemStack = this.mannequinEntity.getTrinketItemStackByIndex(i).copy();
                if (handlerItemStack.isEmpty()) {
                    if (entityItemStack.isEmpty()) {
                        context.drawTexture(this.getSlotBackGroundByIndex(i), this.x + slotX, this.y + slotY, 0, 0, 16, 16, 16, 16);
                    } else {
                        context.drawItem(entityItemStack, this.x + slotX, this.y + slotY);
                        drawSavedItemSlotHighlight(context, this.x + slotX, this.y + slotY, 0);
                    }
                }
            }
        } else if (this.screenPage == ScreenPage.SETTINGS) {
            this.customNameField.render(context, mouseX, mouseY, delta);
            this.textureIdentifierStringField.render(context, mouseX, mouseY, delta);
            this.entityYawField.render(context, mouseX, mouseY, delta);
            this.entityPosXField.render(context, mouseX, mouseY, delta);
            this.entityPosYField.render(context, mouseX, mouseY, delta);
            this.entityPosZField.render(context, mouseX, mouseY, delta);
        }

    }

    public static void drawSavedItemSlotHighlight(DrawContext context, int x, int y, int z) {
        RenderSystem.disableDepthTest();
        RenderSystem.colorMask(true, true, true, false);
        context.fillGradient(RenderLayer.getGuiOverlay(), x, y, x + 16, y + 16, -2130706433, -2130706433, z); // TODO change color
        RenderSystem.colorMask(true, true, true, true);
        RenderSystem.enableDepthTest();
    }

    @Override
    protected void drawForeground(DrawContext context, int mouseX, int mouseY) {
    }
    
    @Override
    protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
        int i = this.x;
        int j = this.y;

        if (this.screenPage == ScreenPage.EQUIPMENT) {
            context.drawTexture(MANNEQUIN_EQUIPMENT_BACKGROUND_TEXTURE, i, j, 0, 0, this.backgroundWidth, this.backgroundHeight, this.backgroundWidth, this.backgroundHeight);
        } else if (this.screenPage == ScreenPage.MODEL_PARTS) {
            context.drawTexture(MANNEQUIN_MODEL_PARTS_BACKGROUND_TEXTURE, i, j, 0, 0, this.backgroundWidth, this.backgroundHeight, this.backgroundWidth, this.backgroundHeight);
        } else if (this.screenPage == ScreenPage.SETTINGS) {
            context.drawTexture(MANNEQUIN_SETTINGS_BACKGROUND_TEXTURE, i, j, 0, 0, this.backgroundWidth, this.backgroundHeight, this.backgroundWidth, this.backgroundHeight);
        }
        this.mannequinEntity.setCustomNameVisible(false);
        InventoryScreen.drawEntity(context, i + 253, j + 43, i + 326, j + 160, 50, 0.0625f, mouseX, mouseY, this.mannequinEntity);
    }

    private Identifier getSlotBackGroundByIndex(int index) {
        return switch (index) {
            case 0 -> BetterAdventureMode.identifier("textures/item/empty_armor_slot_head.png");
            case 1 -> BetterAdventureMode.identifier("textures/item/empty_armor_slot_shoulders.png");
            case 2 -> BetterAdventureMode.identifier("textures/item/empty_armor_slot_chest.png");
            case 3 -> BetterAdventureMode.identifier("textures/item/empty_armor_slot_belt.png");
            case 4 -> BetterAdventureMode.identifier("textures/item/empty_armor_slot_legs.png");
            case 5, 12 -> BetterAdventureMode.identifier("textures/item/empty_armor_slot_sword.png");
            case 6 -> BetterAdventureMode.identifier("textures/item/empty_armor_slot_necklace.png");
            case 7, 8 -> BetterAdventureMode.identifier("textures/item/empty_armor_slot_ring.png");
            case 9 -> BetterAdventureMode.identifier("textures/item/empty_armor_slot_gloves.png");
            case 10 -> BetterAdventureMode.identifier("textures/item/empty_armor_slot_feet.png");
            case 11, 13 -> new Identifier("textures/item/empty_armor_slot_shield.png");
            default -> BetterAdventureMode.identifier("textures/item/empty_slot_spell.png");
        };
    }

    public static enum ScreenPage implements StringIdentifiable {
        EQUIPMENT("equipment"),
        MODEL_PARTS("model_parts"),
        SETTINGS("settings");

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
            return Text.translatable("gui.mannequin_entity.screenPage." + this.name);
        }
    }
}
