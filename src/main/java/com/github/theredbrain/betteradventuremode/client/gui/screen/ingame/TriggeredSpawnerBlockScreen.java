package com.github.theredbrain.betteradventuremode.client.gui.screen.ingame;

import com.github.theredbrain.betteradventuremode.BetterAdventureMode;
import com.github.theredbrain.betteradventuremode.util.ItemUtils;
import com.github.theredbrain.betteradventuremode.block.entity.TriggeredSpawnerBlockEntity;
import com.github.theredbrain.betteradventuremode.network.packet.UpdateTriggeredSpawnerBlockPacket;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
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
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.registry.Registries;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import org.apache.commons.lang3.tuple.MutablePair;
import org.lwjgl.glfw.GLFW;

import java.util.*;

@Environment(value= EnvType.CLIENT)
public class TriggeredSpawnerBlockScreen extends Screen {
    private static final Text SPAWNER_BOUND_ENTITY_NAME_FIELD_LABLE_TEXT = Text.translatable("gui.triggered_spawner_block.spawner_bound_entity_name_field_label");
    private static final Text SPAWNER_BOUND_ENTITY_MODEL_IDENTIFIER_FIELD_LABLE_TEXT = Text.translatable("gui.triggered_spawner_block.spawner_bound_entity_model_identifier_field_label");
    private static final Text SPAWNER_BOUND_ENTITY_TEXTURE_IDENTIFIER_FIELD_LABLE_TEXT = Text.translatable("gui.triggered_spawner_block.spawner_bound_entity_texture_identifier_field_label");
    private static final Text SPAWNER_BOUND_ENTITY_ANIMATIONS_IDENTIFIER_FIELD_LABLE_TEXT = Text.translatable("gui.triggered_spawner_block.spawner_bound_entity_animations_identifier_field_label");
    private static final Text SPAWNER_BOUND_ENTITY_LOOT_TABLE_BOUNDING_BOX_HEIGHT_FIELD_LABLE_TEXT = Text.translatable("gui.triggered_spawner_block.spawner_bound_entity_bounding_box_height_field_label");
    private static final Text SPAWNER_BOUND_ENTITY_LOOT_TABLE_BOUNDING_BOX_WIDTH_FIELD_LABLE_TEXT = Text.translatable("gui.triggered_spawner_block.spawner_bound_entity_bounding_box_width_field_label");
    private static final Text SPAWNER_BOUND_ENTITY_LOOT_TABLE_IDENTIFIER_FIELD_LABLE_TEXT = Text.translatable("gui.triggered_spawner_block.spawner_bound_entity_loot_table_identifier_field_label");
    private static final Text ENTITY_SPAWN_POSITION_OFFSET_LABEL_TEXT = Text.translatable("gui.triggered_spawner_block.entity_spawn_position_offset_label");
    private static final Text SPAWNING_MODE_LABEL_TEXT = Text.translatable("gui.triggered_spawner_block.spawning_mode_label");
    private static final Text ENTITY_MODE_LABEL_TEXT = Text.translatable("gui.triggered_spawner_block.entity_mode_label");
    private static final Text ENTITY_TYPE_LABEL_TEXT = Text.translatable("gui.triggered_spawner_block.entity_type_label");
    private static final Text OPEN_SPAWNER_BOUND_ENTITY_CONFIG_SCREEN_BUTTON_LABEL_TEXT = Text.translatable("gui.triggered_spawner_block.open_spawner_bound_entity_config_screen_button_label");
    private static final Text REMOVE_BUTTON_LABEL_TEXT = Text.translatable("gui.list_entry.remove");
    private static final Text NEW_ENTITY_ATTRIBUTE_MODIFIER_IDENTIFIER_FIELD_PLACEHOLDER_TEXT = Text.translatable("gui.triggered_spawner_block.new_entity_attribute_modifier_identifier_label");
    private static final Text NEW_ENTITY_ATTRIBUTE_MODIFIER_NAME_FIELD_PLACEHOLDER_TEXT = Text.translatable("gui.triggered_spawner_block.new_entity_attribute_modifier_name_label");
    private static final Text NEW_ENTITY_ATTRIBUTE_MODIFIER_VALUE_FIELD_PLACEHOLDER_TEXT = Text.translatable("gui.triggered_spawner_block.new_entity_attribute_modifier_value_label");
    private static final Text CLOSE_SPAWNER_BOUND_ENTITY_CONFIG_SCREEN_BUTTON_LABEL_TEXT = Text.translatable("gui.close");
    private static final Text USE_RELAY_BLOCK_POSITION_OFFSET_LABEL_TEXT = Text.translatable("gui.triggered_spawner_block.useRelayBlockPositionOffset");
    private static final Text TRIGGERED_BLOCK_POSITION_OFFSET_LABEL_TEXT = Text.translatable("gui.triggered_block.triggeredBlockPositionOffset");

    private static final Identifier SCROLL_BAR_BACKGROUND_8_92_TEXTURE = BetterAdventureMode.identifier("scroll_bar/scroll_bar_background_8_92");
    private static final Identifier SCROLLER_VERTICAL_6_7_TEXTURE = BetterAdventureMode.identifier("scroll_bar/scroller_vertical_6_7");

    private final TriggeredSpawnerBlockEntity triggeredSpawnerBlock;

    private TextFieldWidget spawnerBoundEntityNameField;
    private TextFieldWidget spawnerBoundEntityModelIdentifierField;
    private TextFieldWidget spawnerBoundEntityTextureIdentifierField;
    private TextFieldWidget spawnerBoundEntityAnimationsIdentifierField;
    private TextFieldWidget spawnerBoundEntityBoundingBoxHeightField;
    private TextFieldWidget spawnerBoundEntityBoundingBoxWidthField;
    private TextFieldWidget spawnerBoundEntityLootTableIdentifierField;
    private ButtonWidget closeSpawnerBoundEntityConfigScreenButton;
    
    private CyclingButtonWidget<CreativeScreenPage> creativeScreenPageButton;
    private TextFieldWidget entitySpawnPositionOffsetXField;
    private TextFieldWidget entitySpawnPositionOffsetYField;
    private TextFieldWidget entitySpawnPositionOffsetZField;
    private CyclingButtonWidget<TriggeredSpawnerBlockEntity.SpawningMode> cycleSpawningModeButton;
    private TriggeredSpawnerBlockEntity.SpawningMode spawningMode;
    private CyclingButtonWidget<TriggeredSpawnerBlockEntity.EntityMode> cycleEntityModeButton;
    private TriggeredSpawnerBlockEntity.EntityMode entityMode;
    
    private TextFieldWidget entityTypeIdField;
    private ButtonWidget openSpawnerBoundEntityConfigScreenButton;

    private ButtonWidget removeListEntryButton0;
    private ButtonWidget removeListEntryButton1;
    private ButtonWidget removeListEntryButton2;
    private TextFieldWidget newEntityAttributeModifierIdentifierField;
    private TextFieldWidget newEntityAttributeModifierNameField;
    private TextFieldWidget newEntityAttributeModifierValueField;
    private ButtonWidget newEntityAttributeModifierOperationButton;
    private ButtonWidget addNewEntityAttributeModifierButton;

    private TextFieldWidget useRelayBlockPositionOffsetXField;
    private TextFieldWidget useRelayBlockPositionOffsetYField;
    private TextFieldWidget useRelayBlockPositionOffsetZField;

    private TextFieldWidget triggeredBlockPositionOffsetXField;
    private TextFieldWidget triggeredBlockPositionOffsetYField;
    private TextFieldWidget triggeredBlockPositionOffsetZField;

    private ButtonWidget doneButton;
    private ButtonWidget cancelButton;

    List<MutablePair<String, EntityAttributeModifier>> entityAttributeModifiersList = new ArrayList<>();
    private CreativeScreenPage creativeScreenPage;
    private EntityAttributeModifier.Operation newEntityAttributeModifierOperation;
    private boolean showSpawnerBoundEntityConfigScreen = false;
    private int scrollPosition = 0;
    private float scrollAmount = 0.0f;
    private boolean mouseClicked = false;

    public TriggeredSpawnerBlockScreen(TriggeredSpawnerBlockEntity triggeredSpawnerBlock) {
        super(NarratorManager.EMPTY);
        this.triggeredSpawnerBlock = triggeredSpawnerBlock;
        this.creativeScreenPage = CreativeScreenPage.MISC;
        this.newEntityAttributeModifierOperation = EntityAttributeModifier.Operation.ADDITION;
    }

    private void deleteListEntry(int index) {
        this.entityAttributeModifiersList.remove(index + this.scrollPosition);
        this.scrollPosition = 0;
        this.scrollAmount = 0.0f;
        updateWidgets();
    }

    private void addNewEntityAttributeModifier() {
        // TODO check for existing entries and validate fields
        this.entityAttributeModifiersList.add(new MutablePair<>(
                this.newEntityAttributeModifierIdentifierField.getText(),
                new EntityAttributeModifier(
                        this.newEntityAttributeModifierNameField.getText(),
                        ItemUtils.parseDouble(this.newEntityAttributeModifierValueField.getText()),
                        this.newEntityAttributeModifierOperation
                )
        ));
        this.scrollPosition = 0;
        this.scrollAmount = 0.0f;
        updateWidgets();
    }

    private void cycleNewEntityAttributeModifierOperationButton() {
        if (this.newEntityAttributeModifierOperation == EntityAttributeModifier.Operation.ADDITION) {
            this.newEntityAttributeModifierOperation = EntityAttributeModifier.Operation.MULTIPLY_BASE;
        } else if (this.newEntityAttributeModifierOperation == EntityAttributeModifier.Operation.MULTIPLY_BASE) {
            this.newEntityAttributeModifierOperation = EntityAttributeModifier.Operation.MULTIPLY_TOTAL;
        } else if (this.newEntityAttributeModifierOperation == EntityAttributeModifier.Operation.MULTIPLY_TOTAL) {
            this.newEntityAttributeModifierOperation = EntityAttributeModifier.Operation.ADDITION;
        }
//        this.newEntityAttributeModifierOperationButton.setMessage(Text.translatable("gui.entity_attribute_modifier.operation." + this.newEntityAttributeModifierOperation.asString()));
        this.newEntityAttributeModifierOperationButton.setMessage(Text.translatable("gui.entity_attribute_modifier.operation." + this.newEntityAttributeModifierOperation.getId()));
        updateWidgets();
    }

    private void openSpawnerBoundEntityConfigScreen() {
        this.showSpawnerBoundEntityConfigScreen = true;
        this.updateWidgets();
    }

    private void closeSpawnerBoundEntityConfigScreen() {
        this.showSpawnerBoundEntityConfigScreen = false;
        this.updateWidgets();
    }

    private void done() {
        this.updateTriggeredSpawnerBlock();
        this.close();
    }

    private void cancel() {
        this.close();
    }

    @Override
    protected void init() {
        this.entityAttributeModifiersList.clear();
        Multimap<EntityAttribute, EntityAttributeModifier> entityAttributeModifiers = Multimaps.newMultimap(Maps.newLinkedHashMap(), ArrayList::new);
        entityAttributeModifiers.putAll(this.triggeredSpawnerBlock.getEntityAttributeModifiers());
        List<EntityAttribute> entityAttributeModifiersKeys = new ArrayList<>(entityAttributeModifiers.keySet());
        for (EntityAttribute key : entityAttributeModifiersKeys) {
            Collection<EntityAttributeModifier> modifierCollection = entityAttributeModifiers.get(key);
            List<EntityAttributeModifier> modifierList = modifierCollection.stream().toList();
            for (EntityAttributeModifier entityAttributeModifier : modifierList) {
                this.entityAttributeModifiersList.add(new MutablePair<>(String.valueOf(Registries.ATTRIBUTE.getId(key)), entityAttributeModifier));
            }
        }
        // --- spawner bound entity config ---
        
        this.spawnerBoundEntityNameField = new TextFieldWidget(this.textRenderer, this.width / 2 - 154, 20, 300, 20, Text.empty());
        this.spawnerBoundEntityNameField.setMaxLength(128);
        this.spawnerBoundEntityNameField.setText(this.triggeredSpawnerBlock.getSpawnerBoundEntityName());
        this.addSelectableChild(this.spawnerBoundEntityNameField);
        Identifier spawnerBoundEntityModelIdentifier = this.triggeredSpawnerBlock.getSpawnerBoundEntityModelIdentifier();
        this.spawnerBoundEntityModelIdentifierField = new TextFieldWidget(this.textRenderer, this.width / 2 - 154, 55, 300, 20, Text.empty());
        this.spawnerBoundEntityModelIdentifierField.setMaxLength(128);
        this.spawnerBoundEntityModelIdentifierField.setText(spawnerBoundEntityModelIdentifier != null ? spawnerBoundEntityModelIdentifier.toString() : "");
        this.addSelectableChild(this.spawnerBoundEntityModelIdentifierField);
        Identifier spawnerBoundEntityTextureIdentifier = this.triggeredSpawnerBlock.getSpawnerBoundEntityTextureIdentifier();
        this.spawnerBoundEntityTextureIdentifierField = new TextFieldWidget(this.textRenderer, this.width / 2 - 154, 90, 300, 20, Text.empty());
        this.spawnerBoundEntityTextureIdentifierField.setMaxLength(128);
        this.spawnerBoundEntityTextureIdentifierField.setText(spawnerBoundEntityTextureIdentifier != null ? spawnerBoundEntityTextureIdentifier.toString() : "");
        this.addSelectableChild(this.spawnerBoundEntityTextureIdentifierField);
        Identifier spawnerBoundEntityAnimationsIdentifier = this.triggeredSpawnerBlock.getSpawnerBoundEntityAnimationsIdentifier();
        this.spawnerBoundEntityAnimationsIdentifierField = new TextFieldWidget(this.textRenderer, this.width / 2 - 154, 125, 300, 20, Text.empty());
        this.spawnerBoundEntityAnimationsIdentifierField.setMaxLength(128);
        this.spawnerBoundEntityAnimationsIdentifierField.setText(spawnerBoundEntityAnimationsIdentifier != null ? spawnerBoundEntityAnimationsIdentifier.toString() : "");
        this.addSelectableChild(this.spawnerBoundEntityAnimationsIdentifierField);
        this.spawnerBoundEntityBoundingBoxHeightField = new TextFieldWidget(this.textRenderer, this.width / 2 - 154, 160, 150, 20, Text.empty());
        this.spawnerBoundEntityBoundingBoxHeightField.setMaxLength(128);
        this.spawnerBoundEntityBoundingBoxHeightField.setText(Double.toString(this.triggeredSpawnerBlock.getSpawnerBoundEntityBoundingBoxHeight()));
        this.addSelectableChild(this.spawnerBoundEntityBoundingBoxHeightField);
        this.spawnerBoundEntityBoundingBoxWidthField = new TextFieldWidget(this.textRenderer, this.width / 2 + 4, 160, 150, 20, Text.empty());
        this.spawnerBoundEntityBoundingBoxWidthField.setMaxLength(128);
        this.spawnerBoundEntityBoundingBoxWidthField.setText(Double.toString(this.triggeredSpawnerBlock.getSpawnerBoundEntityBoundingBoxWidth()));
        this.addSelectableChild(this.spawnerBoundEntityBoundingBoxWidthField);
        Identifier spawnerBoundEntityLootTableIdentifier = this.triggeredSpawnerBlock.getSpawnerBoundEntityLootTableIdentifier();
        this.spawnerBoundEntityLootTableIdentifierField = new TextFieldWidget(this.textRenderer, this.width / 2 - 154, 195, 300, 20, Text.empty());
        this.spawnerBoundEntityLootTableIdentifierField.setMaxLength(128);
        this.spawnerBoundEntityLootTableIdentifierField.setText(spawnerBoundEntityLootTableIdentifier != null ? spawnerBoundEntityLootTableIdentifier.toString() : "");
        this.addSelectableChild(this.spawnerBoundEntityLootTableIdentifierField);
        this.closeSpawnerBoundEntityConfigScreenButton = this.addDrawableChild(ButtonWidget.builder(CLOSE_SPAWNER_BOUND_ENTITY_CONFIG_SCREEN_BUTTON_LABEL_TEXT, button -> this.closeSpawnerBoundEntityConfigScreen()).dimensions(this.width / 2 - 154, 219, 300, 20).build());

        this.creativeScreenPageButton = this.addDrawableChild(CyclingButtonWidget.builder(CreativeScreenPage::asText).values((CreativeScreenPage[]) CreativeScreenPage.values()).initially(this.creativeScreenPage).omitKeyText().build(this.width / 2 - 154, 20, 300, 20, Text.empty(), (button, creativeScreenPage) -> {
            this.creativeScreenPage = creativeScreenPage;
            this.updateWidgets();
        }));

        // --- misc page ---

        this.entitySpawnPositionOffsetXField = new TextFieldWidget(this.textRenderer, this.width / 2 - 154, 95, 100, 20, Text.translatable(""));
        this.entitySpawnPositionOffsetXField.setText(Integer.toString(this.triggeredSpawnerBlock.getEntitySpawnPositionOffset().getX()));
        this.addSelectableChild(this.entitySpawnPositionOffsetXField);
        this.entitySpawnPositionOffsetYField = new TextFieldWidget(this.textRenderer, this.width / 2 - 50, 95, 100, 20, Text.translatable(""));
        this.entitySpawnPositionOffsetYField.setText(Integer.toString(this.triggeredSpawnerBlock.getEntitySpawnPositionOffset().getY()));
        this.addSelectableChild(this.entitySpawnPositionOffsetYField);
        this.entitySpawnPositionOffsetZField = new TextFieldWidget(this.textRenderer, this.width / 2 + 54, 95, 100, 20, Text.translatable(""));
        this.entitySpawnPositionOffsetZField.setText(Integer.toString(this.triggeredSpawnerBlock.getEntitySpawnPositionOffset().getZ()));
        this.addSelectableChild(this.entitySpawnPositionOffsetZField);

        this.spawningMode = this.triggeredSpawnerBlock.getSpawningMode();
        this.cycleSpawningModeButton = this.addDrawableChild(CyclingButtonWidget.builder(TriggeredSpawnerBlockEntity.SpawningMode::asText).values((TriggeredSpawnerBlockEntity.SpawningMode[]) TriggeredSpawnerBlockEntity.SpawningMode.values()).initially(this.spawningMode).omitKeyText().build(this.width / 2 - 154, 130, 150, 20, Text.empty(), (button, spawningMode) -> {
            this.spawningMode = spawningMode;
        }));

        this.entityMode = this.triggeredSpawnerBlock.getEntityMode();
        this.cycleEntityModeButton = this.addDrawableChild(CyclingButtonWidget.builder(TriggeredSpawnerBlockEntity.EntityMode::asText).values((TriggeredSpawnerBlockEntity.EntityMode[]) TriggeredSpawnerBlockEntity.EntityMode.values()).initially(this.entityMode).omitKeyText().build(this.width / 2 + 4, 130, 150, 20, Text.empty(), (button, entityMode) -> {
            this.entityMode = entityMode;
            this.updateWidgets();
        }));

        this.openSpawnerBoundEntityConfigScreenButton = this.addDrawableChild(ButtonWidget.builder(OPEN_SPAWNER_BOUND_ENTITY_CONFIG_SCREEN_BUTTON_LABEL_TEXT, button -> this.openSpawnerBoundEntityConfigScreen()).dimensions(this.width / 2 - 154, 165, 300, 20).build());
        
        this.entityTypeIdField = new TextFieldWidget(this.textRenderer, this.width / 2 - 154, 165, 300, 20, Text.translatable(""));
        this.entityTypeIdField.setMaxLength(128);
        this.entityTypeIdField.setText(this.triggeredSpawnerBlock.getEntityTypeId());
        this.addSelectableChild(this.entityTypeIdField);

        // --- entity attribute modifier page ---
        this.removeListEntryButton0 = this.addDrawableChild(ButtonWidget.builder(REMOVE_BUTTON_LABEL_TEXT, button -> this.deleteListEntry(0)).dimensions(this.width / 2 + 54, 47, 100, 20).build());
        this.removeListEntryButton1 = this.addDrawableChild(ButtonWidget.builder(REMOVE_BUTTON_LABEL_TEXT, button -> this.deleteListEntry(1)).dimensions(this.width / 2 + 54, 81, 100, 20).build());
        this.removeListEntryButton2 = this.addDrawableChild(ButtonWidget.builder(REMOVE_BUTTON_LABEL_TEXT, button -> this.deleteListEntry(2)).dimensions(this.width / 2 + 54, 115, 100, 20).build());
        this.newEntityAttributeModifierIdentifierField = new TextFieldWidget(this.textRenderer, this.width / 2 - 154, 140, 300, 20, Text.translatable(""));
        this.newEntityAttributeModifierIdentifierField.setPlaceholder(NEW_ENTITY_ATTRIBUTE_MODIFIER_IDENTIFIER_FIELD_PLACEHOLDER_TEXT);
        this.addSelectableChild(this.newEntityAttributeModifierIdentifierField);
        this.newEntityAttributeModifierNameField = new TextFieldWidget(this.textRenderer, this.width / 2 - 154, 164, 300, 20, Text.translatable(""));
        this.newEntityAttributeModifierNameField.setPlaceholder(NEW_ENTITY_ATTRIBUTE_MODIFIER_NAME_FIELD_PLACEHOLDER_TEXT);
        this.addSelectableChild(this.newEntityAttributeModifierNameField);
        this.newEntityAttributeModifierValueField = new TextFieldWidget(this.textRenderer, this.width / 2 - 154, 188, 100, 20, Text.translatable(""));
        this.newEntityAttributeModifierValueField.setPlaceholder(NEW_ENTITY_ATTRIBUTE_MODIFIER_VALUE_FIELD_PLACEHOLDER_TEXT);
        this.addSelectableChild(this.newEntityAttributeModifierValueField);
//        this.newEntityAttributeModifierOperationButton = this.addDrawableChild(ButtonWidget.builder(Text.translatable("gui.entity_attribute_modifier.operation." + this.newEntityAttributeModifierOperation.asString()), button -> this.cycleNewEntityAttributeModifierOperationButton()).dimensions(this.width / 2 - 50, 188, 100, 20).build());
        this.newEntityAttributeModifierOperationButton = this.addDrawableChild(ButtonWidget.builder(Text.translatable("gui.entity_attribute_modifier.operation." + this.newEntityAttributeModifierOperation.getId()), button -> this.cycleNewEntityAttributeModifierOperationButton()).dimensions(this.width / 2 - 50, 188, 100, 20).build());
        this.addNewEntityAttributeModifierButton = this.addDrawableChild(ButtonWidget.builder(Text.translatable("gui.list_entry.add"), button -> this.addNewEntityAttributeModifier()).dimensions(this.width / 2 + 54, 188, 100, 20).build());

        // --- triggered block page ---

        this.useRelayBlockPositionOffsetXField = new TextFieldWidget(this.textRenderer, this.width / 2 - 154, 95, 100, 20, Text.empty());
        this.useRelayBlockPositionOffsetXField.setMaxLength(128);
        this.useRelayBlockPositionOffsetXField.setText(Integer.toString(this.triggeredSpawnerBlock.getUseRelayBlockPositionOffset().getX()));
        this.addSelectableChild(this.useRelayBlockPositionOffsetXField);
        this.useRelayBlockPositionOffsetYField = new TextFieldWidget(this.textRenderer, this.width / 2 - 50, 95, 100, 20, Text.empty());
        this.useRelayBlockPositionOffsetYField.setMaxLength(128);
        this.useRelayBlockPositionOffsetYField.setText(Integer.toString(this.triggeredSpawnerBlock.getUseRelayBlockPositionOffset().getY()));
        this.addSelectableChild(this.useRelayBlockPositionOffsetYField);
        this.useRelayBlockPositionOffsetZField = new TextFieldWidget(this.textRenderer, this.width / 2 + 54, 95, 100, 20, Text.empty());
        this.useRelayBlockPositionOffsetZField.setMaxLength(128);
        this.useRelayBlockPositionOffsetZField.setText(Integer.toString(this.triggeredSpawnerBlock.getUseRelayBlockPositionOffset().getZ()));
        this.addSelectableChild(this.useRelayBlockPositionOffsetZField);

        // --- triggered block page ---

        this.triggeredBlockPositionOffsetXField = new TextFieldWidget(this.textRenderer, this.width / 2 - 154, 95, 100, 20, Text.empty());
        this.triggeredBlockPositionOffsetXField.setMaxLength(128);
        this.triggeredBlockPositionOffsetXField.setText(Integer.toString(this.triggeredSpawnerBlock.getTriggeredBlockPositionOffset().getX()));
        this.addSelectableChild(this.triggeredBlockPositionOffsetXField);
        this.triggeredBlockPositionOffsetYField = new TextFieldWidget(this.textRenderer, this.width / 2 - 50, 95, 100, 20, Text.empty());
        this.triggeredBlockPositionOffsetYField.setMaxLength(128);
        this.triggeredBlockPositionOffsetYField.setText(Integer.toString(this.triggeredSpawnerBlock.getTriggeredBlockPositionOffset().getY()));
        this.addSelectableChild(this.triggeredBlockPositionOffsetYField);
        this.triggeredBlockPositionOffsetZField = new TextFieldWidget(this.textRenderer, this.width / 2 + 54, 95, 100, 20, Text.empty());
        this.triggeredBlockPositionOffsetZField.setMaxLength(128);
        this.triggeredBlockPositionOffsetZField.setText(Integer.toString(this.triggeredSpawnerBlock.getTriggeredBlockPositionOffset().getZ()));
        this.addSelectableChild(this.triggeredBlockPositionOffsetZField);

        this.doneButton = this.addDrawableChild(ButtonWidget.builder(ScreenTexts.DONE, button -> this.done()).dimensions(this.width / 2 - 4 - 150, 212, 150, 20).build());
        this.cancelButton = this.addDrawableChild(ButtonWidget.builder(ScreenTexts.CANCEL, button -> this.cancel()).dimensions(this.width / 2 + 4, 212, 150, 20).build());
        this.setInitialFocus(this.entitySpawnPositionOffsetXField);
        this.updateWidgets();
    }

    private void updateWidgets() {

        this.spawnerBoundEntityNameField.setVisible(false);
        this.spawnerBoundEntityModelIdentifierField.setVisible(false);
        this.spawnerBoundEntityTextureIdentifierField.setVisible(false);
        this.spawnerBoundEntityAnimationsIdentifierField.setVisible(false);
        this.spawnerBoundEntityBoundingBoxHeightField.setVisible(false);
        this.spawnerBoundEntityBoundingBoxWidthField.setVisible(false);
        this.spawnerBoundEntityLootTableIdentifierField.setVisible(false);
        this.closeSpawnerBoundEntityConfigScreenButton.visible = false;
        
        this.creativeScreenPageButton.visible = false;
        
        this.entitySpawnPositionOffsetXField.setVisible(false);
        this.entitySpawnPositionOffsetYField.setVisible(false);
        this.entitySpawnPositionOffsetZField.setVisible(false);

        this.cycleSpawningModeButton.visible = false;

        this.cycleEntityModeButton.visible = false;

        this.entityTypeIdField.setVisible(false);
        this.openSpawnerBoundEntityConfigScreenButton.visible = false;
        
        this.removeListEntryButton0.visible = false;
        this.removeListEntryButton1.visible = false;
        this.removeListEntryButton2.visible = false;
        this.newEntityAttributeModifierIdentifierField.setVisible(false);
        this.newEntityAttributeModifierNameField.setVisible(false);
        this.newEntityAttributeModifierValueField.setVisible(false);
        this.newEntityAttributeModifierOperationButton.visible = false;
        this.addNewEntityAttributeModifierButton.visible = false;

        this.useRelayBlockPositionOffsetXField.setVisible(false);
        this.useRelayBlockPositionOffsetYField.setVisible(false);
        this.useRelayBlockPositionOffsetZField.setVisible(false);

        this.triggeredBlockPositionOffsetXField.setVisible(false);
        this.triggeredBlockPositionOffsetYField.setVisible(false);
        this.triggeredBlockPositionOffsetZField.setVisible(false);

        this.doneButton.visible = false;
        this.cancelButton.visible = false;

        if (this.showSpawnerBoundEntityConfigScreen) {

            this.spawnerBoundEntityNameField.setVisible(true);
            this.spawnerBoundEntityModelIdentifierField.setVisible(true);
            this.spawnerBoundEntityTextureIdentifierField.setVisible(true);
            this.spawnerBoundEntityAnimationsIdentifierField.setVisible(true);
            this.spawnerBoundEntityBoundingBoxHeightField.setVisible(true);
            this.spawnerBoundEntityBoundingBoxWidthField.setVisible(true);
            this.spawnerBoundEntityLootTableIdentifierField.setVisible(true);
            this.closeSpawnerBoundEntityConfigScreenButton.visible = true;

        } else {

            this.creativeScreenPageButton.visible = true;

            if (this.creativeScreenPage == CreativeScreenPage.MISC) {

                this.entitySpawnPositionOffsetXField.setVisible(true);
                this.entitySpawnPositionOffsetYField.setVisible(true);
                this.entitySpawnPositionOffsetZField.setVisible(true);

                this.cycleSpawningModeButton.visible = true;

                this.cycleEntityModeButton.visible = true;

                if (this.entityMode == TriggeredSpawnerBlockEntity.EntityMode.IDENTIFIER) {

                    this.entityTypeIdField.setVisible(true);

                } else if (this.entityMode == TriggeredSpawnerBlockEntity.EntityMode.SPAWNER_BOUND_ENTITY) {

                    this.openSpawnerBoundEntityConfigScreenButton.visible = true;

                }

            } else if (this.creativeScreenPage == CreativeScreenPage.ENTITY_ATTRIBUTE_MODIFIER) {

                int index = 0;
                for (int i = 0; i < Math.min(3, this.entityAttributeModifiersList.size()); i++) {
                    if (index == 0) {
                        this.removeListEntryButton0.visible = true;
                    } else if (index == 1) {
                        this.removeListEntryButton1.visible = true;
                    } else if (index == 2) {
                        this.removeListEntryButton2.visible = true;
                    }
                    index++;
                }

                this.newEntityAttributeModifierIdentifierField.setVisible(true);
                this.newEntityAttributeModifierNameField.setVisible(true);
                this.newEntityAttributeModifierValueField.setVisible(true);
                this.newEntityAttributeModifierOperationButton.visible = true;
                this.addNewEntityAttributeModifierButton.visible = true;

            } else if (this.creativeScreenPage == CreativeScreenPage.USE_RELAY_BLOCK) {

                this.useRelayBlockPositionOffsetXField.setVisible(true);
                this.useRelayBlockPositionOffsetYField.setVisible(true);
                this.useRelayBlockPositionOffsetZField.setVisible(true);

            } else if (this.creativeScreenPage == CreativeScreenPage.TRIGGERED_BLOCK) {

                this.triggeredBlockPositionOffsetXField.setVisible(true);
                this.triggeredBlockPositionOffsetYField.setVisible(true);
                this.triggeredBlockPositionOffsetZField.setVisible(true);

            }

            this.doneButton.visible = true;
            this.cancelButton.visible = true;

        }
    }

    @Override
    public void resize(MinecraftClient client, int width, int height) {
        // TODO
        List<MutablePair<String, EntityAttributeModifier>> list = this.entityAttributeModifiersList;
        String string = this.entitySpawnPositionOffsetXField.getText();
        String string1 = this.entitySpawnPositionOffsetYField.getText();
        String string2 = this.entitySpawnPositionOffsetZField.getText();
        String string3 = this.spawningMode.asString();
        String string4 = this.entityTypeIdField.getText();
        String string5 = this.useRelayBlockPositionOffsetXField.getText();
        String string6 = this.useRelayBlockPositionOffsetYField.getText();
        String string7 = this.useRelayBlockPositionOffsetZField.getText();
        String string8 = this.triggeredBlockPositionOffsetXField.getText();
        String string9 = this.triggeredBlockPositionOffsetYField.getText();
        String string10 = this.triggeredBlockPositionOffsetZField.getText();
        this.init(client, width, height);
        this.entityAttributeModifiersList = list;
        this.entitySpawnPositionOffsetXField.setText(string);
        this.entitySpawnPositionOffsetYField.setText(string1);
        this.entitySpawnPositionOffsetZField.setText(string2);
        this.spawningMode = TriggeredSpawnerBlockEntity.SpawningMode.byName(string3).orElseGet(() -> TriggeredSpawnerBlockEntity.SpawningMode.ONCE);
        this.entityTypeIdField.setText(string4);
        this.useRelayBlockPositionOffsetXField.setText(string5);
        this.useRelayBlockPositionOffsetYField.setText(string6);
        this.useRelayBlockPositionOffsetZField.setText(string7);
        this.triggeredBlockPositionOffsetXField.setText(string8);
        this.triggeredBlockPositionOffsetYField.setText(string9);
        this.triggeredBlockPositionOffsetZField.setText(string10);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        this.mouseClicked = false;
        if (this.creativeScreenPage == CreativeScreenPage.ENTITY_ATTRIBUTE_MODIFIER
                && this.entityAttributeModifiersList.size() > 3) {
            int i = this.width / 2 - 152;
            int j = 46;
            if (mouseX >= (double)i && mouseX < (double)(i + 6) && mouseY >= (double)j && mouseY < (double)(j + 90)) {
                this.mouseClicked = true;
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        if (this.creativeScreenPage == CreativeScreenPage.ENTITY_ATTRIBUTE_MODIFIER
                && this.entityAttributeModifiersList.size() > 3
                && this.mouseClicked) {
            int i = this.entityAttributeModifiersList.size() - 3;
            float f = (float)deltaY / (float)i;
            this.scrollAmount = MathHelper.clamp(this.scrollAmount + f, 0.0f, 1.0f);
            this.scrollPosition = (int)((double)(this.scrollAmount * (float)i));
        }
        return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY/*, double horizontalAmount*/, double verticalAmount) {
        if (this.creativeScreenPage == CreativeScreenPage.ENTITY_ATTRIBUTE_MODIFIER
                && this.entityAttributeModifiersList.size() > 3
                && mouseX >= (double)(this.width / 2 - 152) && mouseX <= (double)(this.width / 2 + 50)
                && mouseY >= 45 && mouseY <= 137) {
            int i = this.entityAttributeModifiersList.size() - 3;
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

    private void updateTriggeredSpawnerBlock() {

        ClientPlayNetworking.send(
                new UpdateTriggeredSpawnerBlockPacket(
                        this.triggeredSpawnerBlock.getPos(),
                        this.spawnerBoundEntityNameField.getText(),
                        this.spawnerBoundEntityModelIdentifierField.getText(),
                        this.spawnerBoundEntityTextureIdentifierField.getText(),
                        this.spawnerBoundEntityAnimationsIdentifierField.getText(),
                        ItemUtils.parseFloat(this.spawnerBoundEntityBoundingBoxHeightField.getText()),
                        ItemUtils.parseFloat(this.spawnerBoundEntityBoundingBoxWidthField.getText()),
                        this.spawnerBoundEntityLootTableIdentifierField.getText(),
                        new BlockPos(
                                ItemUtils.parseInt(this.entitySpawnPositionOffsetXField.getText()),
                                ItemUtils.parseInt(this.entitySpawnPositionOffsetYField.getText()),
                                ItemUtils.parseInt(this.entitySpawnPositionOffsetZField.getText())
                        ),
                        this.spawningMode.asString(),
                        this.entityMode.asString(),
                        this.entityTypeIdField.getText(),
                        this.entityAttributeModifiersList,
                        new BlockPos(
                                ItemUtils.parseInt(this.triggeredBlockPositionOffsetXField.getText()),
                                ItemUtils.parseInt(this.triggeredBlockPositionOffsetYField.getText()),
                                ItemUtils.parseInt(this.triggeredBlockPositionOffsetZField.getText())
                        ),
                        new BlockPos(
                                ItemUtils.parseInt(this.useRelayBlockPositionOffsetXField.getText()),
                                ItemUtils.parseInt(this.useRelayBlockPositionOffsetYField.getText()),
                                ItemUtils.parseInt(this.useRelayBlockPositionOffsetZField.getText())
                        )
                )
        );
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {

        super.render(context, mouseX, mouseY, delta);

        if (this.showSpawnerBoundEntityConfigScreen) {

            context.drawTextWithShadow(this.textRenderer, SPAWNER_BOUND_ENTITY_NAME_FIELD_LABLE_TEXT, this.width / 2 - 153, 10, 0xA0A0A0);
            this.spawnerBoundEntityNameField.render(context, mouseX, mouseY, delta);
            context.drawTextWithShadow(this.textRenderer, SPAWNER_BOUND_ENTITY_MODEL_IDENTIFIER_FIELD_LABLE_TEXT, this.width / 2 - 153, 45, 0xA0A0A0);
            this.spawnerBoundEntityModelIdentifierField.render(context, mouseX, mouseY, delta);
            context.drawTextWithShadow(this.textRenderer, SPAWNER_BOUND_ENTITY_TEXTURE_IDENTIFIER_FIELD_LABLE_TEXT, this.width / 2 - 153, 80, 0xA0A0A0);
            this.spawnerBoundEntityTextureIdentifierField.render(context, mouseX, mouseY, delta);
            context.drawTextWithShadow(this.textRenderer, SPAWNER_BOUND_ENTITY_ANIMATIONS_IDENTIFIER_FIELD_LABLE_TEXT, this.width / 2 - 153, 115, 0xA0A0A0);
            this.spawnerBoundEntityAnimationsIdentifierField.render(context, mouseX, mouseY, delta);
            context.drawTextWithShadow(this.textRenderer, SPAWNER_BOUND_ENTITY_LOOT_TABLE_BOUNDING_BOX_HEIGHT_FIELD_LABLE_TEXT, this.width / 2 - 153, 150, 0xA0A0A0);
            this.spawnerBoundEntityBoundingBoxHeightField.render(context, mouseX, mouseY, delta);
            context.drawTextWithShadow(this.textRenderer, SPAWNER_BOUND_ENTITY_LOOT_TABLE_BOUNDING_BOX_WIDTH_FIELD_LABLE_TEXT, this.width / 2 + 5, 150, 0xA0A0A0);
            this.spawnerBoundEntityBoundingBoxWidthField.render(context, mouseX, mouseY, delta);
            context.drawTextWithShadow(this.textRenderer, SPAWNER_BOUND_ENTITY_LOOT_TABLE_IDENTIFIER_FIELD_LABLE_TEXT, this.width / 2 - 153, 185, 0xA0A0A0);
            this.spawnerBoundEntityLootTableIdentifierField.render(context, mouseX, mouseY, delta);

        } else {
            if (this.creativeScreenPage == CreativeScreenPage.MISC) {
                context.drawTextWithShadow(this.textRenderer, ENTITY_SPAWN_POSITION_OFFSET_LABEL_TEXT, this.width / 2 - 153, 85, 0xA0A0A0);
                this.entitySpawnPositionOffsetXField.render(context, mouseX, mouseY, delta);
                this.entitySpawnPositionOffsetYField.render(context, mouseX, mouseY, delta);
                this.entitySpawnPositionOffsetZField.render(context, mouseX, mouseY, delta);

                context.drawTextWithShadow(this.textRenderer, SPAWNING_MODE_LABEL_TEXT, this.width / 2 - 153, 120, 0xA0A0A0);
                context.drawTextWithShadow(this.textRenderer, ENTITY_MODE_LABEL_TEXT, this.width / 2 + 5, 120, 0xA0A0A0);

                if (this.entityMode == TriggeredSpawnerBlockEntity.EntityMode.IDENTIFIER) {
                    context.drawTextWithShadow(this.textRenderer, ENTITY_TYPE_LABEL_TEXT, this.width / 2 - 153, 155, 0xA0A0A0);
                    this.entityTypeIdField.render(context, mouseX, mouseY, delta);
                }
            } else if (this.creativeScreenPage == CreativeScreenPage.ENTITY_ATTRIBUTE_MODIFIER) {

                for (int i = this.scrollPosition; i < Math.min(this.scrollPosition + 3, this.entityAttributeModifiersList.size()); i++) {
                    EntityAttributeModifier entityAttributeModifier = this.entityAttributeModifiersList.get(i).getRight();

                    context.drawTextWithShadow(this.textRenderer, this.entityAttributeModifiersList.get(i).getLeft() + ": ", this.width / 2 - 141, 46 + ((i - this.scrollPosition) * 34), 0xA0A0A0);
                    context.drawTextWithShadow(this.textRenderer, entityAttributeModifier.getValue() + ", " + entityAttributeModifier.getOperation(), this.width / 2 - 141, 59 + ((i - this.scrollPosition) * 34), 0xA0A0A0);
                }
                if (this.entityAttributeModifiersList.size() > 3) {
//                    context.drawGuiTexture(SCROLL_BAR_BACKGROUND_8_92_TEXTURE, this.width / 2 - 153, 45, 8, 92);
                    context.drawTexture(SCROLL_BAR_BACKGROUND_8_92_TEXTURE, this.width / 2 - 153, 45, 0, 0, 8, 92);
                    int k = (int)(83.0f * this.scrollAmount);
//                    context.drawGuiTexture(SCROLLER_VERTICAL_6_7_TEXTURE, this.width / 2 - 152, 45 + 1 + k, 6, 7);
                    context.drawTexture(SCROLLER_VERTICAL_6_7_TEXTURE, this.width / 2 - 152, 45 + 1 + k, 0, 0, 6, 7);
                }

                this.newEntityAttributeModifierIdentifierField.render(context, mouseX, mouseY, delta);
                this.newEntityAttributeModifierNameField.render(context, mouseX, mouseY, delta);
                this.newEntityAttributeModifierValueField.render(context, mouseX, mouseY, delta);

            } else if (this.creativeScreenPage == CreativeScreenPage.USE_RELAY_BLOCK) {
                context.drawTextWithShadow(this.textRenderer, USE_RELAY_BLOCK_POSITION_OFFSET_LABEL_TEXT, this.width / 2 - 153, 85, 0xA0A0A0);
                this.useRelayBlockPositionOffsetXField.render(context, mouseX, mouseY, delta);
                this.useRelayBlockPositionOffsetYField.render(context, mouseX, mouseY, delta);
                this.useRelayBlockPositionOffsetZField.render(context, mouseX, mouseY, delta);
            } else if (this.creativeScreenPage == CreativeScreenPage.TRIGGERED_BLOCK) {
                context.drawTextWithShadow(this.textRenderer, TRIGGERED_BLOCK_POSITION_OFFSET_LABEL_TEXT, this.width / 2 - 153, 85, 0xA0A0A0);
                this.triggeredBlockPositionOffsetXField.render(context, mouseX, mouseY, delta);
                this.triggeredBlockPositionOffsetYField.render(context, mouseX, mouseY, delta);
                this.triggeredBlockPositionOffsetZField.render(context, mouseX, mouseY, delta);
            }
        }
    }

    @Override
    public boolean shouldPause() {
        return false;
    }

    public static enum CreativeScreenPage implements StringIdentifiable
    {
        MISC("misc"),
        ENTITY_ATTRIBUTE_MODIFIER("entity_attribute_modifier"),
        USE_RELAY_BLOCK("use_relay_block"),
        TRIGGERED_BLOCK("triggered_block");

        private final String name;

        private CreativeScreenPage(String name) {
            this.name = name;
        }

        @Override
        public String asString() {
            return this.name;
        }

        public static Optional<TriggeredSpawnerBlockScreen.CreativeScreenPage> byName(String name) {
            return Arrays.stream(TriggeredSpawnerBlockScreen.CreativeScreenPage.values()).filter(creativeScreenPage -> creativeScreenPage.asString().equals(name)).findFirst();
        }

        public Text asText() {
            return Text.translatable("gui.triggered_spawner_block.creativeScreenPage." + this.name);
        }
    }
}
