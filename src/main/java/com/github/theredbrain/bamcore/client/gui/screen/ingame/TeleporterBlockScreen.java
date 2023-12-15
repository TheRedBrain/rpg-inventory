package com.github.theredbrain.bamcore.client.gui.screen.ingame;

import com.github.theredbrain.bamcore.BetterAdventureModeCore;
import com.github.theredbrain.bamcore.api.dimensions.PlayerLocation;
import com.github.theredbrain.bamcore.block.entity.TeleporterBlockBlockEntity;
import com.github.theredbrain.bamcore.network.packet.AddStatusEffectPacket;
import com.github.theredbrain.bamcore.network.packet.TeleportFromTeleporterBlockPacket;
import com.github.theredbrain.bamcore.network.packet.UpdateTeleporterBlockPacket;
import com.github.theredbrain.bamcore.registry.PlayerLocationsRegistry;
import com.github.theredbrain.bamcore.registry.StatusEffectsRegistry;
import com.github.theredbrain.bamcore.screen.DuckSlotMixin;
import com.github.theredbrain.bamcore.screen.TeleporterBlockScreenHandler;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.AdvancementEntry;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.CyclingButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.screen.slot.Slot;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.ServerAdvancementLoader;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.StringVisitable;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3i;
import org.lwjgl.glfw.GLFW;

import java.util.*;

@Environment(value= EnvType.CLIENT)
public class TeleporterBlockScreen extends HandledScreen<TeleporterBlockScreenHandler> {
    private static final Text CREATIVE_SCREEN_PAGE_LABEL_TEXT = Text.translatable("gui.teleporter_block.creative_screen_page_label");
    private static final Text HIDE_ADVENTURE_SCREEN_LABEL_TEXT = Text.translatable("gui.teleporter_block.hide_adventure_screen_label");
    private static final Text SHOW_ADVENTURE_SCREEN_LABEL_TEXT = Text.translatable("gui.teleporter_block.show_adventure_screen_label");
    private static final Text HIDE_ACTIVATION_AREA_LABEL_TEXT = Text.translatable("gui.teleporter_block.hide_activation_area_label");
    private static final Text SHOW_ACTIVATION_AREA_LABEL_TEXT = Text.translatable("gui.teleporter_block.show_activation_area_label");
    private static final Text ACTIVATION_AREA_DIMENSIONS_LABEL_TEXT = Text.translatable("gui.teleporter_block.activation_area_dimensions_label");
    private static final Text ACTIVATION_AREA_POSITION_OFFET_LABEL_TEXT = Text.translatable("gui.teleporter_block.activation_area_position_offset_label");
    private static final Text ACCESS_POSITION_OFFET_LABEL_TEXT = Text.translatable("gui.teleporter_block.access_position_offset_label");
    private static final Text SET_ACCESS_POSITION_LABEL_TEXT = Text.translatable("gui.teleporter_block.set_access_position_label");
    private static final Text TELEPORTATION_MODE_LABEL_TEXT = Text.translatable("gui.teleporter_block.teleportation_mode_label");
    private static final Text DIRECT_TELEPORT_POSITION_OFFET_LABEL_TEXT = Text.translatable("gui.teleporter_block.direct_teleport_position_offset_label");
    private static final Text DIRECT_TELEPORT_ORIENTATION_LABEL_TEXT = Text.translatable("gui.teleporter_block.direct_teleport_orientation_label");
    private static final Text SPAWN_POINT_TYPE_LABEL_TEXT = Text.translatable("gui.teleporter_block.spawn_point_type_label");
    private static final Text ADD_NEW_LOCATION_BUTTON_LABEL_TEXT = Text.translatable("gui.teleporter_block.add_new_location_button_label");
    private static final Text REMOVE_LOCATION_BUTTON_LABEL_TEXT = Text.translatable("gui.teleporter_block.remove_location_button_label");
    private static final Text EDIT_BUTTON_LABEL_TEXT = Text.translatable("gui.edit");
    private static final Text CONFIRM_BUTTON_LABEL_TEXT = Text.translatable("gui.confirm");
    private static final Text CANCEL_BUTTON_LABEL_TEXT = Text.translatable("gui.cancel");
    private static final Text CHOOSE_BUTTON_LABEL_TEXT = Text.translatable("gui.choose");
    private static final Text TELEPORTER_NAME_FIELD_LABEL_TEXT = Text.translatable("gui.teleporter_block.teleporter_name_field.label");
    private static final Text TARGET_OWNER_FIELD_LABEL_TEXT = Text.translatable("gui.teleporter_block.target_owner_field.label");
    private static final Text TARGET_IDENTIFIER_FIELD_LABEL_TEXT = Text.translatable("gui.teleporter_block.target_identifier_field.label");
    private static final Text TARGET_ENTRANCE_FIELD_LABEL_TEXT = Text.translatable("gui.teleporter_block.target_entrance_field.label");
    private static final Text TELEPORT_BUTTON_LABEL_TEXT = Text.translatable("gui.teleporter_block.teleport_button.label");
    private static final Text CANCEL_TELEPORT_BUTTON_LABEL_TEXT = Text.translatable("gui.teleporter_block.cancel_teleport_button.label");
    private static final Text CONSUME_KEY_ITEMSTACK_LABEL_TEXT = Text.translatable("gui.teleporter_block.consume_key_itemstack_label");
    private static final Text WIP_LABEL_TEXT = Text.translatable("wip");
    private static final Text EDIT_TELEPORTER_TITLE = Text.translatable("gui.edit_teleporter_title");
    private static final Identifier SCROLL_BAR_BACKGROUND_8_70_TEXTURE = BetterAdventureModeCore.identifier("scroll_bar/scroll_bar_background_8_70");
    private static final Identifier CREATIVE_HOUSING_SCROLLER_BACKGROUND_TEXTURE = BetterAdventureModeCore.identifier("scroll_bar/scroll_bar_background_8_95");
    private static final Identifier SCROLLER_TEXTURE = BetterAdventureModeCore.identifier("scroll_bar/scroller_vertical_6_7");
    public static final Identifier ADVENTURE_TELEPORTER_SCREEN_BACKGROUND_TEXTURE = BetterAdventureModeCore.identifier("textures/gui/container/adventure_teleporter_screen.png");
    public static final Identifier CREATIVE_TELEPORTER_SCREEN_BACKGROUND_TEXTURE = BetterAdventureModeCore.identifier("textures/gui/container/creative_teleporter_screen.png");
    public static final Identifier TELEPORTER_SCREEN_UTILITY_TEXTURE = BetterAdventureModeCore.identifier("textures/gui/container/teleporter_screen_util.png");
    private TeleporterBlockScreenHandler handler;
    private TeleporterBlockBlockEntity teleporterBlock;

    // creative mode
    private CyclingButtonWidget<CreativeScreenPage> creativeScreenPageButton;
    private CyclingButtonWidget<Boolean> toggleShowAdventureScreenButton;
    private CyclingButtonWidget<Boolean> toggleShowActivationAreaButton;
    private TextFieldWidget activationAreaDimensionsXField;
    private TextFieldWidget activationAreaDimensionsYField;
    private TextFieldWidget activationAreaDimensionsZField;
    private TextFieldWidget activationAreaPositionOffsetXField;
    private TextFieldWidget activationAreaPositionOffsetYField;
    private TextFieldWidget activationAreaPositionOffsetZField;
    private TextFieldWidget accessPositionOffsetXField;
    private TextFieldWidget accessPositionOffsetYField;
    private TextFieldWidget accessPositionOffsetZField;
    private CyclingButtonWidget<Boolean> toggleSetAccessPositionButton;
    private CyclingButtonWidget<TeleporterBlockBlockEntity.TeleportationMode> teleportationModeButton;
    private TextFieldWidget directTeleportPositionOffsetXField;
    private TextFieldWidget directTeleportPositionOffsetYField;
    private TextFieldWidget directTeleportPositionOffsetZField;
    private TextFieldWidget directTeleportOrientationYawField;
    private TextFieldWidget directTeleportOrientationPitchField;
    private CyclingButtonWidget<TeleporterBlockBlockEntity.SpawnPointType> spawnPointTypeButton;
    private ButtonWidget removeLocationButton0;
    private ButtonWidget removeLocationButton1;
    private ButtonWidget removeLocationButton2;
    private TextFieldWidget newLocationIdentifierField;
    private TextFieldWidget newLocationEntranceField;
    private ButtonWidget addNewLocationButton;
//    private CyclingButtonWidget<Boolean> consumeKeyItemStackButton;
    private TextFieldWidget teleporterNameField;
    private TextFieldWidget currentTargetOwnerLabelField;
    private TextFieldWidget currentTargetIdentifierLabelField;
    private TextFieldWidget currentTargetEntranceLabelField;
    private TextFieldWidget teleportButtonLabelField;
    private TextFieldWidget cancelTeleportButtonLabelField;
    private ButtonWidget doneButton;
    private ButtonWidget cancelButton;

    // adventure mode
    private PlayerListEntry currentTargetOwner;
    private ButtonWidget openChooseTargetOwnerScreenButton;
    private ButtonWidget cancelChooseTargetOwnerButton;
    private String currentTargetIdentifier;
    private String currentTargetDisplayName;
    private ButtonWidget openChooseTargetIdentifierScreenButton;
    private ButtonWidget confirmChooseTargetIdentifier0Button;
    private ButtonWidget confirmChooseTargetIdentifier1Button;
    private ButtonWidget confirmChooseTargetIdentifier2Button;
    private ButtonWidget confirmChooseTargetIdentifier3Button;
    private ButtonWidget cancelChooseTargetIdentifierButton;
    private String currentTargetEntrance;
    private String currentTargetEntranceDisplayName;
    private ButtonWidget teleportButton;
    private ButtonWidget cancelTeleportButton;
    private ButtonWidget openDungeonRegenerationScreenButton;
    private ButtonWidget confirmDungeonRegenerationButton;
    private ButtonWidget cancelDungeonRegenerationButton;

    private boolean showCreativeTab;
    private CreativeScreenPage creativeScreenPage;
    private boolean showChooseTargetOwnerScreen;
    private boolean showChooseTargetIdentifierScreen;
    private boolean showRegenerationConfirmScreen;
    private boolean showActivationArea;
    private boolean showAdventureScreen;
    private boolean setAccessPosition;

    private TeleporterBlockBlockEntity.TeleportationMode teleportationMode;
    private TeleporterBlockBlockEntity.SpawnPointType spawnPointType;

    List<Pair<String, String>> locationsList = new ArrayList<>();
    List<Pair<String, String>> visibleLocationsList = new ArrayList<>();
    List<Pair<String, String>> unlockedLocationsList = new ArrayList<>();
//    private boolean consumeKeyItemStack;
    private int creativeLocationsListScrollPosition = 0;
    private int teamListScrollPosition = 0;
    private int visibleLocationsListScrollPosition = 0;
    private float creativeLocationsListScrollAmount = 0.0f;
    private float teamListScrollAmount = 0.0f;
    private float visibleLocationsListScrollAmount = 0.0f;
    private boolean creativeLocationsListMouseClicked = false;
    private boolean teamListMouseClicked = false;
    private boolean visibleLocationsListMouseClicked = false;

    private boolean isTeleportButtonActive = true;
    private boolean showCurrentUnlockAdvancement;
    private boolean isCurrentLocationUnlocked;
    private Advancement currentUnlockAdvancement;

    public TeleporterBlockScreen(TeleporterBlockScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
        this.handler = handler;
        this.showCreativeTab = handler.getShowCreativeTab();
        this.creativeScreenPage = CreativeScreenPage.ACTIVATION;
    }

    //region button callbacks
    private void teleport() {
        if (this.tryTeleport()) {
            this.close();
        }
    }

    private void cancelTeleport() {
        this.close();
    }

    private void openChooseCurrentTargetOwnerScreen() {
        this.showChooseTargetOwnerScreen = true;
        this.updateWidgets();
    }

    private void confirmChooseCurrentTargetOwner() {
        this.showChooseTargetOwnerScreen = false;
//        this.thisMethodHandlesTheAdvancementStuff(false);
        this.updateWidgets();
    }

    private void cancelChooseCurrentTargetOwner() {
        this.showChooseTargetOwnerScreen = false;
        this.updateWidgets();
    }

    private void openChooseTargetIdentifierScreen() {
        this.showChooseTargetIdentifierScreen = true;
        this.updateWidgets();
    }

    private void chooseTargetIdentifier(int index) {
//        if (teleportationMode == TeleporterBlockBlockEntity.TeleportationMode.LOCATIONS) {
            this.currentTargetIdentifier = this.visibleLocationsList.get(this.visibleLocationsListScrollPosition + index).getLeft();
            this.currentTargetEntrance = this.visibleLocationsList.get(this.visibleLocationsListScrollPosition + index).getRight();
//        } else if (teleportationMode == TeleporterBlockBlockEntity.TeleportationMode.HOUSING) {
//            this.currentTargetIdentifier = this.visibleHousingLocationsList.get(this.visibleHousingLocationsListScrollPosition + index);
//        }
        this.showChooseTargetIdentifierScreen = false;
        this.thisMethodHandlesTheAdvancementStuffTODORename(false);
        this.updateWidgets();
    }

    private void cancelChooseTargetLocation() {
        this.showChooseTargetIdentifierScreen = false;
        this.updateWidgets();
    }

    private void openDungeonRegenerationConfirmScreen() {
        this.showRegenerationConfirmScreen = true;
        this.updateWidgets();
    }

    private void confirmDungeonRegeneration() {
        if (this.tryDungeonRegeneration()) {
            this.showRegenerationConfirmScreen = false;
            this.updateWidgets();
        }
    }

    private void cancelDungeonRegeneration() {
        this.showRegenerationConfirmScreen = false;
        this.updateWidgets();
    }

    private void done() {
        if (this.updateTeleporterBlock()) {
            this.close();
        }
    }

    private void cancel() {
        this.teleporterBlock.setShowActivationArea(this.showActivationArea);
        this.teleporterBlock.setShowAdventureScreen(this.showAdventureScreen);
        this.teleporterBlock.setSetAccessPosition(this.setAccessPosition);
        this.teleporterBlock.setTeleportationMode(this.teleportationMode);
        this.teleporterBlock.setLocationType(this.spawnPointType);
//        this.teleporterBlock.setConsumeKeyItemStack(this.consumeKeyItemStack);
        this.close();
    }

    @Override
    public void close() {
        this.givePortalResistanceEffect();
        super.close();
    }

    private void addLocationToList(String identifier, String entrance) {
        BetterAdventureModeCore.LOGGER.info(identifier);
        Text message = Text.literal("");
        if (Identifier.isValid(identifier)) {
            PlayerLocation playerLocation = PlayerLocationsRegistry.getLocation(Identifier.tryParse(identifier));
            if (playerLocation != null) {
                if (!playerLocation.hasEntrance(entrance)) {
                    entrance = "";
                }
                boolean bl = false;
                for (Pair<String, String> location : this.locationsList) {
                    if (location.getLeft().equals(identifier) && location.getRight().equals(entrance)) {
                        bl = true;
                        break;
                    }
                }
                if (bl) {
                    message = Text.translatable("gui.teleporter_block.location_already_in_list");
                } else {
                    this.locationsList.add(new Pair<>(identifier, entrance));
                }
            } else {
                message = Text.translatable("gui.teleporter_block.location_not_found");
            }
        } else {
            message = Text.translatable("gui.invalid_identifier");
        }
        if (this.client != null && this.client.player != null && !message.getString().equals("")) {
            this.client.player.sendMessage(message);
        }
        this.updateWidgets();
    }

    private void removeLocationFromLocationList(int index) {
        if (index + this.visibleLocationsListScrollPosition < this.locationsList.size()) {
            this.locationsList.remove(index + this.visibleLocationsListScrollPosition);
        }
        this.updateWidgets();
    }

    @Override
    protected void init() {
        if (this.client != null && this.client.world != null) {
            BlockEntity blockEntity = this.client.world.getBlockEntity(this.handler.getBlockPos());
            if (blockEntity instanceof TeleporterBlockBlockEntity) {
                this.teleporterBlock = (TeleporterBlockBlockEntity) blockEntity;
            }
            if (this.client.player != null) {
                this.currentTargetOwner = this.client.player.networkHandler.getPlayerListEntry(this.client.player.getUuid());
            }
        }
        this.locationsList.clear();
        this.locationsList.addAll(this.teleporterBlock.getLocationsList());
        this.currentTargetIdentifier = "";
        this.currentTargetDisplayName = "";
        this.currentTargetEntrance = "";
        this.currentTargetEntranceDisplayName = "";
        if (!this.showCreativeTab) {
            this.backgroundWidth = 218;
            if (this.teleporterBlock.getTeleportationMode() == TeleporterBlockBlockEntity.TeleportationMode.LOCATIONS) {
                this.backgroundHeight = 158;
                this.thisMethodHandlesTheAdvancementStuffTODORename(true);
            } else {
                this.backgroundHeight = 47;
            }
        }
        super.init();
        //region adventure screen

        this.openChooseTargetOwnerScreenButton = this.addDrawableChild(ButtonWidget.builder(EDIT_BUTTON_LABEL_TEXT, button -> this.openChooseCurrentTargetOwnerScreen()).dimensions(this.x + this.backgroundWidth - 57, this.y + 33, 50, 20).build());
        this.cancelChooseTargetOwnerButton = this.addDrawableChild(ButtonWidget.builder(CANCEL_BUTTON_LABEL_TEXT, button -> this.cancelChooseCurrentTargetOwner()).dimensions(this.x + 7, this.y + this.backgroundHeight - 27, this.backgroundWidth - 14, 20).build());

        this.openChooseTargetIdentifierScreenButton = this.addDrawableChild(ButtonWidget.builder(EDIT_BUTTON_LABEL_TEXT, button -> this.openChooseTargetIdentifierScreen()).dimensions(this.x + this.backgroundWidth - 57, this.y + 77, 50, 20).build());
        this.confirmChooseTargetIdentifier0Button = this.addDrawableChild(ButtonWidget.builder(CHOOSE_BUTTON_LABEL_TEXT, button -> this.chooseTargetIdentifier(0)).dimensions(this.x + this.backgroundWidth - 57, this.y + 20, 50, 20).build());
        this.confirmChooseTargetIdentifier1Button = this.addDrawableChild(ButtonWidget.builder(CHOOSE_BUTTON_LABEL_TEXT, button -> this.chooseTargetIdentifier(1)).dimensions(this.x + this.backgroundWidth - 57, this.y + 44, 50, 20).build());
        this.confirmChooseTargetIdentifier2Button = this.addDrawableChild(ButtonWidget.builder(CHOOSE_BUTTON_LABEL_TEXT, button -> this.chooseTargetIdentifier(2)).dimensions(this.x + this.backgroundWidth - 57, this.y + 68, 50, 20).build());
        this.confirmChooseTargetIdentifier3Button = this.addDrawableChild(ButtonWidget.builder(CHOOSE_BUTTON_LABEL_TEXT, button -> this.chooseTargetIdentifier(3)).dimensions(this.x + this.backgroundWidth - 57, this.y + 92, 50, 20).build());
        this.cancelChooseTargetIdentifierButton = this.addDrawableChild(ButtonWidget.builder(CANCEL_BUTTON_LABEL_TEXT, button -> this.cancelChooseTargetLocation()).dimensions(this.x + 7, this.y + this.backgroundHeight - 27, this.backgroundWidth - 14, 20).build());

        this.teleportButton = this.addDrawableChild(ButtonWidget.builder(Text.translatable(this.teleporterBlock.getTeleportButtonLabel()), button -> this.teleport()).dimensions(this.x + 7, this.y + this.backgroundHeight - 27, 100, 20).build());
        this.cancelTeleportButton = this.addDrawableChild(ButtonWidget.builder(Text.translatable(this.teleporterBlock.getCancelTeleportButtonLabel()), button -> this.cancelTeleport()).dimensions(this.x + this.backgroundWidth - 107, this.y + this.backgroundHeight - 27, 100, 20).build());

        //endregion adventure screen

        //region creative screen
        this.creativeScreenPageButton = this.addDrawableChild(CyclingButtonWidget.builder(CreativeScreenPage::asText).values((CreativeScreenPage[])CreativeScreenPage.values()).initially(this.creativeScreenPage).omitKeyText().build(this.width / 2 - 154, 20, 300, 20, Text.empty(), (button, creativeScreenPage) -> {
            this.creativeScreenPage = creativeScreenPage;
            this.updateWidgets();
        }));

        // --- activation page ---

        this.showAdventureScreen = this.teleporterBlock.getShowAdventureScreen();
        this.toggleShowAdventureScreenButton = this.addDrawableChild(CyclingButtonWidget.onOffBuilder(HIDE_ADVENTURE_SCREEN_LABEL_TEXT, SHOW_ADVENTURE_SCREEN_LABEL_TEXT).initially(this.showAdventureScreen).omitKeyText().build(this.width / 2 - 154, 45, 150, 20, SHOW_ADVENTURE_SCREEN_LABEL_TEXT, (button, showAdventureScreen) -> {
            this.showAdventureScreen = showAdventureScreen;
        }));

        this.showActivationArea = this.teleporterBlock.getShowActivationArea();
        this.toggleShowActivationAreaButton = this.addDrawableChild(CyclingButtonWidget.onOffBuilder(HIDE_ACTIVATION_AREA_LABEL_TEXT, SHOW_ACTIVATION_AREA_LABEL_TEXT).initially(this.showActivationArea).omitKeyText().build(this.width / 2 + 4, 45, 150, 20, Text.empty(), (button, showActivationArea) -> {
            this.showActivationArea = showActivationArea;
        }));

        this.activationAreaDimensionsXField = new TextFieldWidget(this.textRenderer, this.width / 2 - 154, 80, 100, 20, Text.empty());
        this.activationAreaDimensionsXField.setMaxLength(128);
        this.activationAreaDimensionsXField.setText(Integer.toString(this.teleporterBlock.getActivationAreaDimensions().getX()));
        this.addSelectableChild(this.activationAreaDimensionsXField);

        this.activationAreaDimensionsYField = new TextFieldWidget(this.textRenderer, this.width / 2 - 50, 80, 100, 20, Text.empty());
        this.activationAreaDimensionsYField.setMaxLength(128);
        this.activationAreaDimensionsYField.setText(Integer.toString(this.teleporterBlock.getActivationAreaDimensions().getY()));
        this.addSelectableChild(this.activationAreaDimensionsYField);

        this.activationAreaDimensionsZField = new TextFieldWidget(this.textRenderer, this.width / 2 + 54, 80, 100, 20, Text.empty());
        this.activationAreaDimensionsZField.setMaxLength(128);
        this.activationAreaDimensionsZField.setText(Integer.toString(this.teleporterBlock.getActivationAreaDimensions().getZ()));
        this.addSelectableChild(this.activationAreaDimensionsZField);

        this.activationAreaPositionOffsetXField = new TextFieldWidget(this.textRenderer, this.width / 2 - 154, 115, 100, 20, Text.empty());
        this.activationAreaPositionOffsetXField.setMaxLength(128);
        this.activationAreaPositionOffsetXField.setText(Integer.toString(this.teleporterBlock.getActivationAreaPositionOffset().getX()));
        this.addSelectableChild(this.activationAreaPositionOffsetXField);

        this.activationAreaPositionOffsetYField = new TextFieldWidget(this.textRenderer, this.width / 2 - 50, 115, 100, 20, Text.empty());
        this.activationAreaPositionOffsetYField.setMaxLength(128);
        this.activationAreaPositionOffsetYField.setText(Integer.toString(this.teleporterBlock.getActivationAreaPositionOffset().getY()));
        this.addSelectableChild(this.activationAreaPositionOffsetYField);

        this.activationAreaPositionOffsetZField = new TextFieldWidget(this.textRenderer, this.width / 2 + 54, 115, 100, 20, Text.empty());
        this.activationAreaPositionOffsetZField.setMaxLength(128);
        this.activationAreaPositionOffsetZField.setText(Integer.toString(this.teleporterBlock.getActivationAreaPositionOffset().getZ()));
        this.addSelectableChild(this.activationAreaPositionOffsetZField);

        this.accessPositionOffsetXField = new TextFieldWidget(this.textRenderer, this.width / 2 - 154, 150, 100, 20, Text.empty());
        this.accessPositionOffsetXField.setMaxLength(128);
        this.accessPositionOffsetXField.setText(Integer.toString(this.teleporterBlock.getAccessPositionOffset().getX()));
        this.addSelectableChild(this.accessPositionOffsetXField);

        this.accessPositionOffsetYField = new TextFieldWidget(this.textRenderer, this.width / 2 - 50, 150, 100, 20, Text.empty());
        this.accessPositionOffsetYField.setMaxLength(128);
        this.accessPositionOffsetYField.setText(Integer.toString(this.teleporterBlock.getAccessPositionOffset().getY()));
        this.addSelectableChild(this.accessPositionOffsetYField);

        this.accessPositionOffsetZField = new TextFieldWidget(this.textRenderer, this.width / 2 + 54, 150, 100, 20, Text.empty());
        this.accessPositionOffsetZField.setMaxLength(128);
        this.accessPositionOffsetZField.setText(Integer.toString(this.teleporterBlock.getAccessPositionOffset().getZ()));
        this.addSelectableChild(this.accessPositionOffsetZField);

        int i = this.textRenderer.getWidth(SET_ACCESS_POSITION_LABEL_TEXT) + 10;
        this.setAccessPosition = this.teleporterBlock.getSetAccessPosition();
        this.toggleSetAccessPositionButton = this.addDrawableChild(CyclingButtonWidget.onOffBuilder().initially(this.setAccessPosition).omitKeyText().build(this.width / 2 - 152 + i, 175, 300 - i, 20, Text.empty(), (button, setAccessPosition) -> {
            this.setAccessPosition = setAccessPosition;
        }));


        // --- teleportation mode page ---
        
        this.teleportationMode = this.teleporterBlock.getTeleportationMode();
        i = this.textRenderer.getWidth(TELEPORTATION_MODE_LABEL_TEXT) + 10;
        this.teleportationModeButton = this.addDrawableChild(CyclingButtonWidget.builder(TeleporterBlockBlockEntity.TeleportationMode::asText).values((TeleporterBlockBlockEntity.TeleportationMode[])TeleporterBlockBlockEntity.TeleportationMode.values()).initially(this.teleportationMode).omitKeyText().build(this.width / 2 - 152 + i, 45, 300 - i, 20, TELEPORTATION_MODE_LABEL_TEXT, (button, teleportationMode) -> {
            this.teleportationMode = teleportationMode;
            this.updateWidgets();
        }));

        // teleportation mode: direct
        
        this.directTeleportPositionOffsetXField = new TextFieldWidget(this.textRenderer, this.width / 2 - 154, 80, 100, 20, Text.empty());
        this.directTeleportPositionOffsetXField.setMaxLength(128);
        this.directTeleportPositionOffsetXField.setText(Integer.toString(this.teleporterBlock.getDirectTeleportPositionOffset().getX()));
        this.addSelectableChild(this.directTeleportPositionOffsetXField);

        this.directTeleportPositionOffsetYField = new TextFieldWidget(this.textRenderer, this.width / 2 - 50, 80, 100, 20, Text.empty());
        this.directTeleportPositionOffsetYField.setMaxLength(128);
        this.directTeleportPositionOffsetYField.setText(Integer.toString(this.teleporterBlock.getDirectTeleportPositionOffset().getY()));
        this.addSelectableChild(this.directTeleportPositionOffsetYField);

        this.directTeleportPositionOffsetZField = new TextFieldWidget(this.textRenderer, this.width / 2 + 54, 80, 100, 20, Text.empty());
        this.directTeleportPositionOffsetZField.setMaxLength(128);
        this.directTeleportPositionOffsetZField.setText(Integer.toString(this.teleporterBlock.getDirectTeleportPositionOffset().getZ()));
        this.addSelectableChild(this.directTeleportPositionOffsetZField);

        this.directTeleportOrientationYawField = new TextFieldWidget(this.textRenderer, this.width / 2 - 154, 115, 100, 20, Text.empty());
        this.directTeleportOrientationYawField.setMaxLength(128);
        this.directTeleportOrientationYawField.setText(Double.toString(this.teleporterBlock.getDirectTeleportOrientationYaw()));
        this.addSelectableChild(this.directTeleportOrientationYawField);

        this.directTeleportOrientationPitchField = new TextFieldWidget(this.textRenderer, this.width / 2 - 50, 115, 100, 20, Text.empty());
        this.directTeleportOrientationPitchField.setMaxLength(128);
        this.directTeleportOrientationPitchField.setText(Double.toString(this.teleporterBlock.getDirectTeleportOrientationPitch()));
        this.addSelectableChild(this.directTeleportOrientationPitchField);

        // teleportation mode: saved_locations
        
        this.spawnPointType = this.teleporterBlock.getLocationType();
        i = this.textRenderer.getWidth(SPAWN_POINT_TYPE_LABEL_TEXT) + 10;
        this.spawnPointTypeButton = this.addDrawableChild(CyclingButtonWidget.builder(TeleporterBlockBlockEntity.SpawnPointType::asText).values((TeleporterBlockBlockEntity.SpawnPointType[]) TeleporterBlockBlockEntity.SpawnPointType.values()).initially(this.spawnPointType).omitKeyText().build(this.width / 2 - 152 + i, 70, 300 - i, 20, SPAWN_POINT_TYPE_LABEL_TEXT, (button, locationType) -> {
            this.spawnPointType = locationType;
        }));

        // teleportation mode: dungeons

        this.removeLocationButton0 = this.addDrawableChild(ButtonWidget.builder(REMOVE_LOCATION_BUTTON_LABEL_TEXT, button -> this.removeLocationFromLocationList(0)).dimensions(this.width / 2 + 54, 70, 100, 20).build());
        this.removeLocationButton1 = this.addDrawableChild(ButtonWidget.builder(REMOVE_LOCATION_BUTTON_LABEL_TEXT, button -> this.removeLocationFromLocationList(1)).dimensions(this.width / 2 + 54, 95, 100, 20).build());
        this.removeLocationButton2 = this.addDrawableChild(ButtonWidget.builder(REMOVE_LOCATION_BUTTON_LABEL_TEXT, button -> this.removeLocationFromLocationList(2)).dimensions(this.width / 2 + 54, 120, 100, 20).build());

        this.newLocationIdentifierField = new TextFieldWidget(this.textRenderer, this.width / 2 - 4 - 150, 160, 150, 20, Text.literal(""));
        this.newLocationIdentifierField.setMaxLength(128);
        this.newLocationIdentifierField.setPlaceholder(Text.translatable("gui.teleporter_block.target_identifier_field.place_holder"));
        this.addSelectableChild(this.newLocationIdentifierField);

        this.newLocationEntranceField = new TextFieldWidget(this.textRenderer, this.width / 2 + 4, 160, 150, 20, Text.literal(""));
        this.newLocationEntranceField.setMaxLength(128);
        this.newLocationEntranceField.setPlaceholder(Text.translatable("gui.teleporter_block.target_entrance_field.place_holder"));
        this.addSelectableChild(this.newLocationEntranceField);

        this.addNewLocationButton = this.addDrawableChild(ButtonWidget.builder(ADD_NEW_LOCATION_BUTTON_LABEL_TEXT, button -> this.addLocationToList(this.newLocationIdentifierField.getText(), this.newLocationEntranceField.getText())).dimensions(this.width / 2 - 154, 185, 300, 20).build());

        // --- adventure screen customization page ---

        this.teleporterNameField = new TextFieldWidget(this.textRenderer, this.width / 2 - 154, 44, 300, 20, Text.empty());
        this.teleporterNameField.setMaxLength(128);
        this.teleporterNameField.setPlaceholder(Text.translatable("gui.teleporter_block.teleporter_name_field.place_holder"));
        this.teleporterNameField.setText(this.teleporterBlock.getTeleporterName());
        this.addSelectableChild(this.teleporterNameField);

        this.currentTargetOwnerLabelField = new TextFieldWidget(this.textRenderer, this.width / 2 - 154, 68, 300, 20, Text.empty());
        this.currentTargetOwnerLabelField.setMaxLength(128);
        this.currentTargetOwnerLabelField.setPlaceholder(Text.translatable("gui.teleporter_block.target_owner_field.place_holder"));
        this.currentTargetOwnerLabelField.setText(this.teleporterBlock.getCurrentTargetOwnerLabel());
        this.addSelectableChild(this.currentTargetOwnerLabelField);

        this.currentTargetIdentifierLabelField = new TextFieldWidget(this.textRenderer, this.width / 2 - 154, 92, 300, 20, Text.empty());
        this.currentTargetIdentifierLabelField.setMaxLength(128);
        this.currentTargetIdentifierLabelField.setPlaceholder(Text.translatable("gui.teleporter_block.target_identifier_field.place_holder"));
        this.currentTargetIdentifierLabelField.setText(this.teleporterBlock.getCurrentTargetIdentifierLabel());
        this.addSelectableChild(this.currentTargetIdentifierLabelField);

        this.currentTargetEntranceLabelField = new TextFieldWidget(this.textRenderer, this.width / 2 - 154, 116, 300, 20, Text.empty());
        this.currentTargetEntranceLabelField.setMaxLength(128);
        this.currentTargetEntranceLabelField.setPlaceholder(Text.translatable("gui.teleporter_block.target_entrance_field.place_holder"));
        this.currentTargetEntranceLabelField.setText(this.teleporterBlock.getCurrentTargetEntranceLabel());
        this.addSelectableChild(this.currentTargetEntranceLabelField);

        this.teleportButtonLabelField = new TextFieldWidget(this.textRenderer, this.width / 2 - 154, 140, 300, 20, Text.empty());
        this.teleportButtonLabelField.setMaxLength(128);
        this.teleportButtonLabelField.setPlaceholder(Text.translatable("gui.teleporter_block.teleport_button.place_holder"));
        this.teleportButtonLabelField.setText(this.teleporterBlock.getTeleportButtonLabel());
        this.addSelectableChild(this.teleportButtonLabelField);

        this.cancelTeleportButtonLabelField = new TextFieldWidget(this.textRenderer, this.width / 2 - 154, 164, 300, 20, Text.empty());
        this.cancelTeleportButtonLabelField.setMaxLength(128);
        this.cancelTeleportButtonLabelField.setPlaceholder(Text.translatable("gui.teleporter_block.cancel_teleport_button.place_holder"));
        this.cancelTeleportButtonLabelField.setText(this.teleporterBlock.getCancelTeleportButtonLabel());
        this.addSelectableChild(this.cancelTeleportButtonLabelField);

        this.doneButton = this.addDrawableChild(ButtonWidget.builder(ScreenTexts.DONE, button -> this.done()).dimensions(this.width / 2 - 4 - 150, 210, 150, 20).build());
        this.cancelButton = this.addDrawableChild(ButtonWidget.builder(ScreenTexts.CANCEL, button -> this.cancel()).dimensions(this.width / 2 + 4, 210, 150, 20).build());

        //        this.consumeKeyItemStack = this.teleporterBlock.getConsumeKeyItemStack();
//        i = this.textRenderer.getWidth(CONSUME_KEY_ITEMSTACK_LABEL_TEXT) + 10;
//        this.consumeKeyItemStackButton = this.addDrawableChild(CyclingButtonWidget.onOffBuilder(Text.translatable(""), Text.translatable("")).initially(this.consumeKeyItemStack).omitKeyText().build(this.width / 2 - 152 + i, 215, 300 - i, 20, CONSUME_KEY_ITEMSTACK_LABEL_TEXT, (button, consumeKeyItemStack) -> {
//            this.consumeKeyItemStack = consumeKeyItemStack;
//        }));
        //endregion creative screen

        if (!this.showCreativeTab) {
            this.setInitialFocus(this.teleportButton);
        } else {
            this.setInitialFocus(this.creativeScreenPageButton);
        }
        this.updateWidgets();
    }
    
    private void updateWidgets() {

        this.openChooseTargetOwnerScreenButton.visible = false;
        // TODO
        this.cancelChooseTargetOwnerButton.visible = false;

        this.openChooseTargetIdentifierScreenButton.visible = false;
        this.confirmChooseTargetIdentifier0Button.visible = false;
        this.confirmChooseTargetIdentifier1Button.visible = false;
        this.confirmChooseTargetIdentifier2Button.visible = false;
        this.confirmChooseTargetIdentifier3Button.visible = false;
        this.cancelChooseTargetIdentifierButton.visible = false;

        this.teleportButton.visible = false;
        this.cancelTeleportButton.visible = false;

        this.creativeScreenPageButton.visible = false;

        this.toggleShowAdventureScreenButton.visible = false;
        this.toggleShowActivationAreaButton.visible = false;
        this.activationAreaDimensionsXField.setVisible(false);
        this.activationAreaDimensionsYField.setVisible(false);
        this.activationAreaDimensionsZField.setVisible(false);
        this.activationAreaPositionOffsetXField.setVisible(false);
        this.activationAreaPositionOffsetYField.setVisible(false);
        this.activationAreaPositionOffsetZField.setVisible(false);
        this.accessPositionOffsetXField.setVisible(false);
        this.accessPositionOffsetYField.setVisible(false);
        this.accessPositionOffsetZField.setVisible(false);
        this.toggleSetAccessPositionButton.visible = false;
        
        this.teleportationModeButton.visible = false;
        
        this.directTeleportPositionOffsetXField.setVisible(false);
        this.directTeleportPositionOffsetYField.setVisible(false);
        this.directTeleportPositionOffsetZField.setVisible(false);
        this.directTeleportOrientationYawField.setVisible(false);
        this.directTeleportOrientationPitchField.setVisible(false);

        this.spawnPointTypeButton.visible = false;

        this.removeLocationButton0.visible = false;
        this.removeLocationButton1.visible = false;
        this.removeLocationButton2.visible = false;

        this.newLocationIdentifierField.setVisible(false);
        this.newLocationEntranceField.setVisible(false);
        this.addNewLocationButton.visible = false;

        this.teleporterNameField.setVisible(false);
        this.currentTargetOwnerLabelField.setVisible(false);
        this.currentTargetIdentifierLabelField.setVisible(false);
        this.currentTargetEntranceLabelField.setVisible(false);
        this.teleportButtonLabelField.setVisible(false);
        this.cancelTeleportButtonLabelField.setVisible(false);

        this.doneButton.visible = false;
        this.cancelButton.visible = false;

        for (Slot slot : this.handler.slots) {
            ((DuckSlotMixin)slot).bamcore$setDisabledOverride(true);
        }

        if (this.showCreativeTab) {
            this.creativeScreenPageButton.visible = true;

            if (this.creativeScreenPage == CreativeScreenPage.ACTIVATION) {

                this.toggleShowAdventureScreenButton.visible = true;
                this.toggleShowActivationAreaButton.visible = true;
                this.activationAreaDimensionsXField.setVisible(true);
                this.activationAreaDimensionsYField.setVisible(true);
                this.activationAreaDimensionsZField.setVisible(true);
                this.activationAreaPositionOffsetXField.setVisible(true);
                this.activationAreaPositionOffsetYField.setVisible(true);
                this.activationAreaPositionOffsetZField.setVisible(true);
                this.accessPositionOffsetXField.setVisible(true);
                this.accessPositionOffsetYField.setVisible(true);
                this.accessPositionOffsetZField.setVisible(true);
                this.toggleSetAccessPositionButton.visible = true;

            } else if (this.creativeScreenPage == CreativeScreenPage.TELEPORTATION_MODE) {

                this.teleportationModeButton.visible = true;

                if (this.teleportationMode == TeleporterBlockBlockEntity.TeleportationMode.DIRECT) {

                    this.directTeleportPositionOffsetXField.setVisible(true);
                    this.directTeleportPositionOffsetYField.setVisible(true);
                    this.directTeleportPositionOffsetZField.setVisible(true);
                    this.directTeleportOrientationYawField.setVisible(true);
                    this.directTeleportOrientationPitchField.setVisible(true);

                } else if (this.teleportationMode == TeleporterBlockBlockEntity.TeleportationMode.SPAWN_POINTS) {

                    this.spawnPointTypeButton.visible = true;

                } else if (this.teleportationMode == TeleporterBlockBlockEntity.TeleportationMode.LOCATIONS) {

                    int index = 0;
                    for (int i = 0; i < Math.min(3, this.locationsList.size()); i++) {
                        if (index == 0) {
                            this.removeLocationButton0.visible = true;
                        } else if (index == 1) {
                            this.removeLocationButton1.visible = true;
                        } else if (index == 2) {
                            this.removeLocationButton2.visible = true;
                        }
                        index++;
                    }

                    this.newLocationIdentifierField.setVisible(true);
                    this.newLocationEntranceField.setVisible(true);
                    this.addNewLocationButton.visible = true;

                }
            } else if (this.creativeScreenPage == CreativeScreenPage.ADVENTURE_SCREEN_CUSTOMIZATION) {

                this.teleporterNameField.setVisible(true);
                this.teleportButtonLabelField.setVisible(true);
                this.cancelTeleportButtonLabelField.setVisible(true);

                if (!(this.teleportationMode == TeleporterBlockBlockEntity.TeleportationMode.DIRECT || this.teleportationMode == TeleporterBlockBlockEntity.TeleportationMode.SPAWN_POINTS)) {
                    this.currentTargetOwnerLabelField.setVisible(true);
                    this.currentTargetIdentifierLabelField.setVisible(true);
                    if (this.teleportationMode == TeleporterBlockBlockEntity.TeleportationMode.LOCATIONS) {
                        this.currentTargetEntranceLabelField.setVisible(true);
                    }
                }

            }

            this.doneButton.visible = true;
            this.cancelButton.visible = true;

        } else {

            if (showChooseTargetOwnerScreen) {

                // TODO
                this.cancelChooseTargetOwnerButton.visible = true;

            } else if (showChooseTargetIdentifierScreen) {

                int index = 0;
                for (int i = 0; i < Math.min(4, this.visibleLocationsList.size()); i++) {
                    if (index == 0) {
                        this.confirmChooseTargetIdentifier0Button.visible = true;
                    } else if (index == 1) {
                        this.confirmChooseTargetIdentifier1Button.visible = true;
                    } else if (index == 2) {
                        this.confirmChooseTargetIdentifier2Button.visible = true;
                    } else if (index == 3) {
                        this.confirmChooseTargetIdentifier3Button.visible = true;
                    }
                    index++;
                }

                this.cancelChooseTargetIdentifierButton.visible = true;

            } else if (showRegenerationConfirmScreen) {

                this.confirmDungeonRegenerationButton.visible = true;
                this.cancelDungeonRegenerationButton.visible = true;

            } else {

                if (this.teleportationMode == TeleporterBlockBlockEntity.TeleportationMode.LOCATIONS) {
                    this.openChooseTargetIdentifierScreenButton.visible = true;
                    this.openChooseTargetOwnerScreenButton.visible = true;
                }

                this.teleportButton.visible = true;
                this.cancelTeleportButton.visible = true;

                this.teleportButton.active = this.isTeleportButtonActive;
            }
        }
    }

    private void thisMethodHandlesTheAdvancementStuffTODORename(boolean shouldInit) {

        ServerPlayerEntity serverPlayerEntity = null;
        ServerAdvancementLoader serverAdvancementLoader = null;
        MinecraftServer server = null;
        if (this.client != null) {
            server = this.client.getServer();
        }
        if (server != null && this.client.player != null) {
            serverPlayerEntity = server.getPlayerManager().getPlayer(this.currentTargetOwner.getProfile().getId());
            serverAdvancementLoader = server.getAdvancementLoader();
        }

        Identifier unlockAdvancementIdentifier = null;
        boolean showLockedLocation;

        if (shouldInit) {
            if (this.teleporterBlock.getTeleportationMode() == TeleporterBlockBlockEntity.TeleportationMode.LOCATIONS) {
                for (int i = 0; i < this.locationsList.size(); i++) {
                    PlayerLocation playerLocation = PlayerLocationsRegistry.getLocation(new Identifier(this.locationsList.get(i).getLeft()));
                    unlockAdvancementIdentifier = playerLocation.unlockAdvancement();
                    showLockedLocation = playerLocation.showLockedDungeon();

                    if (serverAdvancementLoader != null) {
                        AdvancementEntry advancementEntry = serverAdvancementLoader.get(unlockAdvancementIdentifier);
                        if (serverPlayerEntity != null && advancementEntry != null) {
                            if (serverPlayerEntity.getAdvancementTracker().getProgress(advancementEntry).isDone()) {
                                this.unlockedLocationsList.add(this.locationsList.get(i));
                                this.visibleLocationsList.add(this.locationsList.get(i));
                            } else if (showLockedLocation) {
                                this.visibleLocationsList.add(this.locationsList.get(i));
                            }
                        }
                    }
                }
                if (!this.visibleLocationsList.isEmpty()) {
                    this.currentTargetIdentifier = this.visibleLocationsList.get(0).getLeft();
                    this.currentTargetEntrance = this.visibleLocationsList.get(0).getRight();
                }
            }
        }

        PlayerLocation playerLocation = PlayerLocationsRegistry.getLocation(Identifier.tryParse(this.currentTargetIdentifier));
        if (playerLocation != null) {
            unlockAdvancementIdentifier = playerLocation.unlockAdvancement();
            this.showCurrentUnlockAdvancement = playerLocation.showUnlockAdvancement();
            this.currentTargetDisplayName = playerLocation.getDisplayName();
            this.currentTargetEntranceDisplayName = playerLocation.getEntranceDisplayName(this.currentTargetEntrance);
        }

        for (Pair<String, String> dungeonLocation : this.unlockedLocationsList) {
            if (Objects.equals(dungeonLocation.getLeft(), this.currentTargetIdentifier)) {
                this.isCurrentLocationUnlocked = true;
            }
        }
        if (serverAdvancementLoader != null && unlockAdvancementIdentifier != null) {
            AdvancementEntry advancementEntry = serverAdvancementLoader.get(unlockAdvancementIdentifier);
            if (advancementEntry != null) {
                this.currentUnlockAdvancement = advancementEntry.value();
                if (serverPlayerEntity != null) {
                    this.isTeleportButtonActive = serverPlayerEntity.getAdvancementTracker().getProgress(advancementEntry).isDone();
                }
            }
        }
    }

    @Override
    public void resize(MinecraftClient client, int width, int height) {
        // TODO
//        String string = this.targetField.getText();
//        String string1 = this.poolField.getText();
//        String string2 = this.triggeredBlockPositionOffsetXField.getText();
//        String string3 = this.triggeredBlockPositionOffsetYField.getText();
//        String string4 = this.triggeredBlockPositionOffsetZField.getText();
//        JigsawBlockEntity.Joint joint = this.joint;
        this.init(client, width, height);
//        this.targetField.setText(string);
//        this.poolField.setText(string1);
//        this.triggeredBlockPositionOffsetXField.setText(string2);
//        this.triggeredBlockPositionOffsetYField.setText(string3);
//        this.triggeredBlockPositionOffsetZField.setText(string4);
//        this.joint = joint;
//        this.jointRotationButton.setValue(joint);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        // TODO
        this.creativeLocationsListMouseClicked = false;
        this.teamListMouseClicked = false;
        this.visibleLocationsListMouseClicked = false;
        int i;
        int j;
        if (this.showCreativeTab
                && this.creativeScreenPage == CreativeScreenPage.TELEPORTATION_MODE
                && this.teleportationMode == TeleporterBlockBlockEntity.TeleportationMode.LOCATIONS
                && this.locationsList.size() > 3) {
            i = this.width / 2 - 152;
            j = 71;
            if (mouseX >= (double)i && mouseX < (double)(i + 6) && mouseY >= (double)j && mouseY < (double)(j + 68)) {
                this.creativeLocationsListMouseClicked = true;
            }
        }
        // TODO team list
        if (this.showChooseTargetOwnerScreen) {
            i = this.x - 13;
            j = this.y + 134;
            if (mouseX >= (double)i && mouseX < (double)(i + 6) && mouseY >= (double)j && mouseY < (double)(j + 30)) {
                this.teamListMouseClicked = true;
            }
        }
        if (!this.showCreativeTab
                && this.showChooseTargetIdentifierScreen
                && this.teleportationMode == TeleporterBlockBlockEntity.TeleportationMode.LOCATIONS
                && this.visibleLocationsList.size() > 4) {
            i = this.x + 8;
            j = this.y + 21;
            if (mouseX >= (double)i && mouseX < (double)(i + 6) && mouseY >= (double)j && mouseY < (double)(j + 90)) {
                this.visibleLocationsListMouseClicked = true;
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        if (this.showCreativeTab
                && this.creativeScreenPage == CreativeScreenPage.TELEPORTATION_MODE
                && this.teleportationMode == TeleporterBlockBlockEntity.TeleportationMode.LOCATIONS
                && this.locationsList.size() > 3
                && this.creativeLocationsListMouseClicked) {
            int i = this.locationsList.size() - 3;
            float f = (float)deltaY / (float)i;
            this.creativeLocationsListScrollAmount = MathHelper.clamp(this.creativeLocationsListScrollAmount + f, 0.0f, 1.0f);
            this.creativeLocationsListScrollPosition = (int)((double)(this.creativeLocationsListScrollAmount * (float)i));
        }
        // TODO team list
        if (!this.showCreativeTab
                && this.showChooseTargetIdentifierScreen
                && this.teleportationMode == TeleporterBlockBlockEntity.TeleportationMode.LOCATIONS
                && this.visibleLocationsList.size() > 4
                && this.visibleLocationsListMouseClicked) {
            int i = this.visibleLocationsList.size() - 4;
            float f = (float)deltaY / (float)i;
            this.visibleLocationsListScrollAmount = MathHelper.clamp(this.visibleLocationsListScrollAmount + f, 0.0f, 1.0f);
            this.visibleLocationsListScrollPosition = (int)((double)(this.visibleLocationsListScrollAmount * (float)i));
        }
        return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        // TODO
        if (this.showCreativeTab
                && this.creativeScreenPage == CreativeScreenPage.TELEPORTATION_MODE
                && this.teleportationMode == TeleporterBlockBlockEntity.TeleportationMode.LOCATIONS
                && this.locationsList.size() > 3
                && mouseX >= (double)(this.width / 2 - 152) && mouseX <= (double)(this.width / 2 + 50)
                && mouseY >= 70 && mouseY <= 140) {
            int i = this.locationsList.size() - 3;
            float f = (float)verticalAmount / (float)i;
            this.creativeLocationsListScrollAmount = MathHelper.clamp(this.creativeLocationsListScrollAmount - f, 0.0f, 1.0f);
            this.creativeLocationsListScrollPosition = (int)((double)(this.creativeLocationsListScrollAmount * (float)i));
        }
        // TODO team list
        if (!this.showCreativeTab
                && this.showChooseTargetIdentifierScreen
                && this.teleportationMode == TeleporterBlockBlockEntity.TeleportationMode.LOCATIONS
                && this.visibleLocationsList.size() > 4
                && mouseX >= this.x + 7 && mouseX <= this.x + this.backgroundWidth - 61 && mouseY >= this.y + 20 && mouseY <= this.y + 112) {
            int i = this.visibleLocationsList.size() - 4;
            float f = (float)verticalAmount / (float)i;
            this.visibleLocationsListScrollAmount = MathHelper.clamp(this.visibleLocationsListScrollAmount - f, 0.0f, 1.0f);
            this.visibleLocationsListScrollPosition = (int)((double)(this.visibleLocationsListScrollAmount * (float)i));
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

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        if (this.showCreativeTab) {
            if (this.creativeScreenPage == CreativeScreenPage.ACTIVATION) {
                context.drawTextWithShadow(this.textRenderer, ACTIVATION_AREA_DIMENSIONS_LABEL_TEXT, this.width / 2 - 153, 70, 0xA0A0A0);
                this.activationAreaDimensionsXField.render(context, mouseX, mouseY, delta);
                this.activationAreaDimensionsYField.render(context, mouseX, mouseY, delta);
                this.activationAreaDimensionsZField.render(context, mouseX, mouseY, delta);
                context.drawTextWithShadow(this.textRenderer, ACTIVATION_AREA_POSITION_OFFET_LABEL_TEXT, this.width / 2 - 153, 105, 0xA0A0A0);
                this.activationAreaPositionOffsetXField.render(context, mouseX, mouseY, delta);
                this.activationAreaPositionOffsetYField.render(context, mouseX, mouseY, delta);
                this.activationAreaPositionOffsetZField.render(context, mouseX, mouseY, delta);
                context.drawTextWithShadow(this.textRenderer, ACCESS_POSITION_OFFET_LABEL_TEXT, this.width / 2 - 153, 140, 0xA0A0A0);
                this.accessPositionOffsetXField.render(context, mouseX, mouseY, delta);
                this.accessPositionOffsetYField.render(context, mouseX, mouseY, delta);
                this.accessPositionOffsetZField.render(context, mouseX, mouseY, delta);
                context.drawTextWithShadow(this.textRenderer, SET_ACCESS_POSITION_LABEL_TEXT, this.width / 2 - 153, 181, 0xA0A0A0);
            } else if (this.creativeScreenPage == CreativeScreenPage.TELEPORTATION_MODE) {
                context.drawTextWithShadow(this.textRenderer, TELEPORTATION_MODE_LABEL_TEXT, this.width / 2 - 153, 51, 0xA0A0A0);
                if (this.teleportationMode == TeleporterBlockBlockEntity.TeleportationMode.DIRECT) {
                    context.drawTextWithShadow(this.textRenderer, DIRECT_TELEPORT_POSITION_OFFET_LABEL_TEXT, this.width / 2 - 153, 70, 0xA0A0A0);
                    this.directTeleportPositionOffsetXField.render(context, mouseX, mouseY, delta);
                    this.directTeleportPositionOffsetYField.render(context, mouseX, mouseY, delta);
                    this.directTeleportPositionOffsetZField.render(context, mouseX, mouseY, delta);
                    context.drawTextWithShadow(this.textRenderer, DIRECT_TELEPORT_ORIENTATION_LABEL_TEXT, this.width / 2 - 153, 105, 0xA0A0A0);
                    this.directTeleportOrientationYawField.render(context, mouseX, mouseY, delta);
                    this.directTeleportOrientationPitchField.render(context, mouseX, mouseY, delta);
                } else if (this.teleportationMode == TeleporterBlockBlockEntity.TeleportationMode.SPAWN_POINTS) {
                    context.drawTextWithShadow(this.textRenderer, SPAWN_POINT_TYPE_LABEL_TEXT, this.width / 2 - 153, 76, 0xA0A0A0);
                } else if (this.teleportationMode == TeleporterBlockBlockEntity.TeleportationMode.LOCATIONS) {
                    for (int i = this.creativeLocationsListScrollPosition; i < Math.min(this.creativeLocationsListScrollPosition + 3, this.locationsList.size()); i++) {
                        String text = this.locationsList.get(i).getLeft();
                        if (!this.locationsList.get(i).getRight().equals("")) {
                            text = this.locationsList.get(i).getLeft() + ", " + this.locationsList.get(i).getRight();
                        }
                        context.drawTextWithShadow(this.textRenderer, text, this.width / 2 - 141, 76 + ((i - this.creativeLocationsListScrollPosition) * 25), 0xA0A0A0);
                    }
                    if (this.locationsList.size() > 3) {
                        context.drawGuiTexture(SCROLL_BAR_BACKGROUND_8_70_TEXTURE, this.width / 2 - 153, 70, 8, 70);
                        int k = (int)(61.0f * this.creativeLocationsListScrollAmount);
                        context.drawGuiTexture(SCROLLER_TEXTURE, this.width / 2 - 152, 70 + 1 + k, 6, 7);
                    }
                    this.newLocationIdentifierField.render(context, mouseX, mouseY, delta);
                    this.newLocationEntranceField.render(context, mouseX, mouseY, delta);
                }
            } else if (this.creativeScreenPage == CreativeScreenPage.ADVENTURE_SCREEN_CUSTOMIZATION) {

                this.teleporterNameField.render(context, mouseX, mouseY, delta);

                if (!(this.teleportationMode == TeleporterBlockBlockEntity.TeleportationMode.DIRECT || this.teleportationMode == TeleporterBlockBlockEntity.TeleportationMode.SPAWN_POINTS)) {

                    this.currentTargetOwnerLabelField.render(context, mouseX, mouseY, delta);
                    this.currentTargetIdentifierLabelField.render(context, mouseX, mouseY, delta);

                    if (this.teleportationMode == TeleporterBlockBlockEntity.TeleportationMode.LOCATIONS) {

                        this.currentTargetEntranceLabelField.render(context, mouseX, mouseY, delta);

                    }
                }

                this.teleportButtonLabelField.render(context, mouseX, mouseY, delta);
                this.cancelTeleportButtonLabelField.render(context, mouseX, mouseY, delta);
            }
        } else {

            TeleporterBlockBlockEntity.TeleportationMode mode = this.teleporterBlock.getTeleportationMode();

            if (this.showChooseTargetOwnerScreen) {

            } else if (this.showChooseTargetIdentifierScreen) {

                for (int i = this.visibleLocationsListScrollPosition; i < Math.min(this.visibleLocationsListScrollPosition + 4, this.visibleLocationsList.size()); i++) {
                    context.drawText(this.textRenderer, this.visibleLocationsList.get(i).getLeft(), this.x + 19, this.y + 26 + ((i - this.visibleLocationsListScrollPosition) * 24), 0x404040, false);
                }
                if (this.visibleLocationsList.size() > 4) {
                    context.drawGuiTexture(CREATIVE_HOUSING_SCROLLER_BACKGROUND_TEXTURE, this.x + 7, this.y + 20, 8, 92);
                    int k = (int) (83.0f * this.visibleLocationsListScrollAmount);
                    context.drawGuiTexture(SCROLLER_TEXTURE, this.x + 8, this.y + 20 + 1 + k, 6, 7);
                }

            } else if (this.showRegenerationConfirmScreen) {

            } else {

                Text teleporterName = Text.translatable(this.teleporterBlock.getTeleporterName());
                int teleporterNameOffset = this.backgroundWidth / 2 - this.textRenderer.getWidth(teleporterName) / 2;
                if (this.currentTargetOwner != null) {

                    context.drawText(this.textRenderer, teleporterName, this.x + teleporterNameOffset, this.y + 7, 0x404040, false);

                    if (!(mode == TeleporterBlockBlockEntity.TeleportationMode.DIRECT || mode == TeleporterBlockBlockEntity.TeleportationMode.SPAWN_POINTS)) {

                        context.drawText(this.textRenderer, Text.translatable(this.teleporterBlock.getCurrentTargetOwnerLabel()), this.x + 8, this.y + 20, 0x404040, false);

                        context.drawTexture(currentTargetOwner.getSkinTextures().texture(), this.x + 7, this.y + 39, 8, 8, 8, 8, 8, 8, 64, 64);
                        context.drawText(this.textRenderer, currentTargetOwner.getProfile().getName(), this.x + 19, this.y + 39, 0x404040, false);

                        context.drawText(this.textRenderer, Text.translatable(this.teleporterBlock.getCurrentTargetIdentifierLabel()), this.x + 8, this.y + 57, 0x404040, false);

                        if (mode == TeleporterBlockBlockEntity.TeleportationMode.LOCATIONS) {

                            if (!Objects.equals(this.currentTargetEntrance, "")) {

                                context.drawText(this.textRenderer, Text.translatable(this.currentTargetEntranceDisplayName), this.x + 8, this.y + 76, 0x404040, false);
                                context.drawText(this.textRenderer, Text.translatable(this.currentTargetDisplayName), this.x + 8, this.y + 89, 0x404040, false);

                            } else {

                                context.drawText(this.textRenderer, Text.translatable(this.currentTargetDisplayName), this.x + 8, this.y + 83, 0x404040, false);

                            }

                        }
                    }
                }
            }
        }
//        context.drawTextWithShadow(this.textRenderer, CONSUME_KEY_ITEMSTACK_LABEL_TEXT, this.width / 2 - 153, 221, 0x404040);
    }

    @Override
    protected void drawForeground(DrawContext context, int mouseX, int mouseY) {}

    @Override
    protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
        if (!this.showCreativeTab) {
            int i = this.x;
            int j = this.y;
            if (this.teleporterBlock.getTeleportationMode() == TeleporterBlockBlockEntity.TeleportationMode.LOCATIONS) {
                context.drawTexture(BetterAdventureModeCore.identifier("textures/gui/container/adventure_teleporter_locations_screen.png"), i, j, 0, 0, this.backgroundWidth, this.backgroundHeight, this.backgroundWidth, this.backgroundHeight);
            } else {
                context.drawTexture(BetterAdventureModeCore.identifier("textures/gui/container/adventure_teleporter_screen.png"), i, j, 0, 0, this.backgroundWidth, this.backgroundHeight, this.backgroundWidth, this.backgroundHeight);
            }
        }
    }

    protected boolean isValidCharacterForDisplayName(String name, char character, int cursorPos) {
        int i = name.indexOf(58);
        int j = name.indexOf(47);
        if (character == ':') {
            return (j == -1 || cursorPos <= j) && i == -1;
        }
        if (character == '/') {
            return cursorPos > i;
        }
        return character == '_' || character == '-' || character >= 'a' && character <= 'z' || character >= 'A' && character <= 'Z' || character >= '0' && character <= '9' || character == '.' || character == ' ';
    }

    private void renderSlotTooltip(DrawContext context, int mouseX, int mouseY) {
        Optional<Text> optional = Optional.empty();
        if (this.focusedSlot != null) {
            ItemStack itemStack = this.handler.getSlot(37).getStack();
            if (itemStack.isEmpty()) {
                if (this.focusedSlot.id == 37) {
                    optional = Optional.of(Text.translatable("gui.teleporter_block.requiredItemStackSlot.tooltip"));
                }
            }
        }
        optional.ifPresent(text -> context.drawOrderedTooltip(this.textRenderer, this.textRenderer.wrapLines((StringVisitable)text, 115), mouseX, mouseY));
    }

    private boolean updateTeleporterBlock() {

        BlockPos directTeleportPositionOffset;
        double directTeleportPositionOffsetYaw;
        double directTeleportPositionOffsetPitch;
        if (this.teleportationMode == TeleporterBlockBlockEntity.TeleportationMode.DIRECT) {
            directTeleportPositionOffset = new BlockPos(
                    this.parseInt(this.directTeleportPositionOffsetXField.getText()),
                    this.parseInt(this.directTeleportPositionOffsetYField.getText()),
                    this.parseInt(this.directTeleportPositionOffsetZField.getText())
            );

            directTeleportPositionOffsetYaw = this.parseDouble(this.directTeleportOrientationYawField.getText());
            directTeleportPositionOffsetPitch = this.parseDouble(this.directTeleportOrientationPitchField.getText());
        } else {
            directTeleportPositionOffset = new BlockPos(0, 0, 0);
            directTeleportPositionOffsetYaw = 0.0;
            directTeleportPositionOffsetPitch = 0.0;
        }
        TeleporterBlockBlockEntity.SpawnPointType spawnPointType;
        if (this.teleportationMode == TeleporterBlockBlockEntity.TeleportationMode.SPAWN_POINTS) {

            spawnPointType = this.spawnPointType;

        } else {
            spawnPointType = TeleporterBlockBlockEntity.SpawnPointType.WORLD_SPAWN;
        }

        List<Pair<String, String>> locationsList = new ArrayList<Pair<String, String>>();
        if (this.teleportationMode == TeleporterBlockBlockEntity.TeleportationMode.LOCATIONS) {
            locationsList = this.locationsList;
        }

        ClientPlayNetworking.send(new UpdateTeleporterBlockPacket(
                this.teleporterBlock.getPos(),
                this.showActivationArea,
                this.showAdventureScreen,
                new Vec3i(
                        this.parseInt(this.activationAreaDimensionsXField.getText()),
                        this.parseInt(this.activationAreaDimensionsYField.getText()),
                        this.parseInt(this.activationAreaDimensionsZField.getText())
                ),
                new BlockPos(
                        this.parseInt(this.activationAreaPositionOffsetXField.getText()),
                        this.parseInt(this.activationAreaPositionOffsetYField.getText()),
                        this.parseInt(this.activationAreaPositionOffsetZField.getText())
                ),
                new BlockPos(
                        this.parseInt(this.accessPositionOffsetXField.getText()),
                        this.parseInt(this.accessPositionOffsetYField.getText()),
                        this.parseInt(this.accessPositionOffsetZField.getText())
                ),
                this.setAccessPosition,
                this.teleportationMode.asString(),
                directTeleportPositionOffset,
                directTeleportPositionOffsetYaw,
                directTeleportPositionOffsetPitch,
                spawnPointType.asString(),
                locationsList,
                false/*this.consumeKeyItemStack*/,
                this.teleporterNameField.getText(),
                this.currentTargetOwnerLabelField.getText(),
                this.currentTargetIdentifierLabelField.getText(),
                this.currentTargetEntranceLabelField.getText(),
                this.teleportButtonLabelField.getText(),
                this.cancelTeleportButtonLabelField.getText()
        ));
        return true;
    }

    private boolean tryDungeonRegeneration() {
//        PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
////        buf.writeString(component(LabelComponent.class, "currentTeleportationTargetEntryLabel").text().getString());
//        buf.writeString(this.client.player.getName().getString());
//
//        buf.writeString(this.teleporterBlock.getOutgoingTeleportDimension());
//
//        buf.writeString(this.teleporterBlock.getTargetDungeonStructureIdentifier());
//        buf.writeBlockPos(this.teleporterBlock.getTargetDungeonStructureStartPosition());
//
//        buf.writeInt(this.teleporterBlock.getTargetDungeonChunkX());
//        buf.writeInt(this.teleporterBlock.getTargetDungeonChunkZ());
//        buf.writeBlockPos(this.teleporterBlock.getRegenerateTargetDungeonTriggerBlockPosition());
////
//        this.client.getNetworkHandler().sendPacket(new CustomPayloadC2SPacket(BetterAdventureModeCoreServerPacket.REGENERATE_DIMENSION_FROM_TELEPORTER_BLOCK, buf));
        return true;
    }

    private boolean tryTeleport() {
        String currentWorld = "";
        if (this.teleporterBlock.getWorld() != null) {
            currentWorld = this.teleporterBlock.getWorld().getRegistryKey().getValue().toString();
        }
        ClientPlayNetworking.send(new TeleportFromTeleporterBlockPacket(
                this.teleporterBlock.getPos(),
                currentWorld,
                this.teleporterBlock.getAccessPositionOffset(),
                this.teleporterBlock.getSetAccessPosition(),
                this.teleporterBlock.getTeleportationMode().asString(),
                this.teleporterBlock.getDirectTeleportPositionOffset(),
                this.teleporterBlock.getDirectTeleportOrientationYaw(),
                this.teleporterBlock.getDirectTeleportOrientationPitch(),
                this.teleporterBlock.getLocationType().asString(),
                this.currentTargetOwner.getProfile().getName(),
                this.currentTargetIdentifier,
                this.currentTargetEntrance)
        );
        return true;
    }

    private void givePortalResistanceEffect() {
        ClientPlayNetworking.send(new AddStatusEffectPacket(
                Registries.STATUS_EFFECT.getId(StatusEffectsRegistry.PORTAL_RESISTANCE_EFFECT),
                5,
                0,
                false,
                false,
                false,
                true)
        );
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

    public static enum CreativeScreenPage implements StringIdentifiable
    {
        ACTIVATION("activation"),
        TELEPORTATION_MODE("teleportation_mode"),
        ADVENTURE_SCREEN_CUSTOMIZATION("adventure_screen_customization");

        private final String name;

        private CreativeScreenPage(String name) {
            this.name = name;
        }

        @Override
        public String asString() {
            return this.name;
        }

        public static Optional<CreativeScreenPage> byName(String name) {
            return Arrays.stream(CreativeScreenPage.values()).filter(creativeScreenPage -> creativeScreenPage.asString().equals(name)).findFirst();
        }

        public Text asText() {
            return Text.translatable("gui.teleporter_block.creativeScreenPage." + this.name);
        }
    }
}
