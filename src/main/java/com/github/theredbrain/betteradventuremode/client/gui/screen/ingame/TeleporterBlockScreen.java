package com.github.theredbrain.betteradventuremode.client.gui.screen.ingame;

import com.github.theredbrain.betteradventuremode.BetterAdventureMode;
import com.github.theredbrain.betteradventuremode.data.Location;
import com.github.theredbrain.betteradventuremode.network.packet.SetManualResetLocationControlBlockPacket;
import com.github.theredbrain.betteradventuremode.util.ItemUtils;
import com.github.theredbrain.betteradventuremode.block.entity.TeleporterBlockEntity;
import com.github.theredbrain.betteradventuremode.client.network.DuckClientAdvancementManagerMixin;
import com.github.theredbrain.betteradventuremode.network.packet.AddStatusEffectPacket;
import com.github.theredbrain.betteradventuremode.network.packet.TeleportFromTeleporterBlockPacket;
import com.github.theredbrain.betteradventuremode.network.packet.UpdateTeleporterBlockPacket;
import com.github.theredbrain.betteradventuremode.registry.LocationsRegistry;
import com.github.theredbrain.betteradventuremode.registry.StatusEffectsRegistry;
import com.github.theredbrain.betteradventuremode.screen.DuckSlotMixin;
import com.github.theredbrain.betteradventuremode.screen.TeleporterBlockScreenHandler;
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
import net.minecraft.client.network.ClientAdvancementManager;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.scoreboard.Team;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.screen.slot.Slot;
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

@Environment(value = EnvType.CLIENT)
public class TeleporterBlockScreen extends HandledScreen<TeleporterBlockScreenHandler> {
    private static final Text HIDE_ADVENTURE_SCREEN_LABEL_TEXT = Text.translatable("gui.teleporter_block.hide_adventure_screen_label");
    private static final Text SHOW_ADVENTURE_SCREEN_LABEL_TEXT = Text.translatable("gui.teleporter_block.show_adventure_screen_label");
    private static final Text HIDE_ACTIVATION_AREA_LABEL_TEXT = Text.translatable("gui.teleporter_block.hide_activation_area_label");
    private static final Text SHOW_ACTIVATION_AREA_LABEL_TEXT = Text.translatable("gui.teleporter_block.show_activation_area_label");
    private static final Text ACTIVATION_AREA_DIMENSIONS_LABEL_TEXT = Text.translatable("gui.teleporter_block.activation_area_dimensions_label");
    private static final Text ACTIVATION_AREA_POSITION_OFFET_LABEL_TEXT = Text.translatable("gui.teleporter_block.activation_area_position_offset_label");
    private static final Text ACCESS_POSITION_OFFET_LABEL_TEXT = Text.translatable("gui.teleporter_block.access_position_offset_label");
    private static final Text TOGGLE_SET_ACCESS_POSITION_BUTTON_LABEL_TEXT_ON = Text.translatable("gui.teleporter_block.toggle_set_access_position_button_label.on");
    private static final Text TOGGLE_SET_ACCESS_POSITION_BUTTON_LABEL_TEXT_OFF = Text.translatable("gui.teleporter_block.toggle_set_access_position_button_label.off");
    private static final Text TOGGLE_ONLY_TELEPORT_DIMENSION_OWNER_BUTTON_LABEL_TEXT_ON = Text.translatable("gui.teleporter_block.toggle_only_teleport_dimension_owner_button_label.on");
    private static final Text TOGGLE_ONLY_TELEPORT_DIMENSION_OWNER_BUTTON_LABEL_TEXT_OFF = Text.translatable("gui.teleporter_block.toggle_only_teleport_dimension_owner_button_label.off");
    private static final Text TOGGLE_TELEPORT_TEAM_BUTTON_LABEL_TEXT_ON = Text.translatable("gui.teleporter_block.toggle_teleport_team_button_label.on");
    private static final Text TOGGLE_TELEPORT_TEAM_BUTTON_LABEL_TEXT_OFF = Text.translatable("gui.teleporter_block.toggle_teleport_team_button_label.off");
    private static final Text TELEPORTATION_MODE_LABEL_TEXT = Text.translatable("gui.teleporter_block.teleportation_mode_label");
    private static final Text DIRECT_TELEPORT_POSITION_OFFET_LABEL_TEXT = Text.translatable("gui.teleporter_block.direct_teleport_position_offset_label");
    private static final Text DIRECT_TELEPORT_ORIENTATION_LABEL_TEXT = Text.translatable("gui.teleporter_block.direct_teleport_orientation_label");
    private static final Text SPAWN_POINT_TYPE_LABEL_TEXT = Text.translatable("gui.teleporter_block.spawn_point_type_label");
    private static final Text ADD_NEW_LOCATION_BUTTON_LABEL_TEXT = Text.translatable("gui.teleporter_block.add_new_location_button_label");
    private static final Text REMOVE_LOCATION_BUTTON_LABEL_TEXT = Text.translatable("gui.teleporter_block.remove_location_button_label");
    private static final Text EDIT_BUTTON_LABEL_TEXT = Text.translatable("gui.edit");
    private static final Text CANCEL_BUTTON_LABEL_TEXT = Text.translatable("gui.cancel");
    private static final Text CHOOSE_BUTTON_LABEL_TEXT = Text.translatable("gui.choose");
    private static final Text KEY_ITEM_IS_CONSUMED_TEXT = Text.translatable("gui.teleporter_block.key_item_is_consumed");
    private static final Text KEY_ITEM_IS_REQUIRED_TEXT = Text.translatable("gui.teleporter_block.key_item_is_required");
    private static final Text LOCATION_IS_PUBLIC_TEXT = Text.translatable("gui.teleporter_block.location_is_public");
    private static final Identifier SCROLL_BAR_BACKGROUND_8_70_TEXTURE = BetterAdventureMode.identifier("scroll_bar/scroll_bar_background_8_70");
    private static final Identifier CREATIVE_HOUSING_SCROLLER_BACKGROUND_TEXTURE = BetterAdventureMode.identifier("scroll_bar/scroll_bar_background_8_95");
    private static final Identifier SCROLLER_TEXTURE = BetterAdventureMode.identifier("scroll_bar/scroller_vertical_6_7");
    public static final Identifier ADVENTURE_TELEPORTER_SCREEN_BACKGROUND_TEXTURE = BetterAdventureMode.identifier("textures/gui/container/teleporter_block/adventure_teleporter_screen.png");
    public static final Identifier ADVENTURE_TELEPORTER_LOCATIONS_SCREEN_BACKGROUND_TEXTURE = BetterAdventureMode.identifier("textures/gui/container/teleporter_block/adventure_teleporter_locations_screen.png");
    public static final Identifier CREATIVE_TELEPORTER_SCREEN_BACKGROUND_TEXTURE = BetterAdventureMode.identifier("textures/gui/container/creative_teleporter_screen.png");
    public static final Identifier TELEPORTER_SCREEN_UTILITY_TEXTURE = BetterAdventureMode.identifier("textures/gui/container/teleporter_screen_util.png");
    private final TeleporterBlockScreenHandler handler;
    private TeleporterBlockEntity teleporterBlock;

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
    private CyclingButtonWidget<Boolean> toggleOnlyTeleportDimensionOwnerButton;
    private CyclingButtonWidget<Boolean> toggleTeleportTeamButton;
    private CyclingButtonWidget<TeleporterBlockEntity.TeleportationMode> teleportationModeButton;
    private TextFieldWidget directTeleportPositionOffsetXField;
    private TextFieldWidget directTeleportPositionOffsetYField;
    private TextFieldWidget directTeleportPositionOffsetZField;
    private TextFieldWidget directTeleportOrientationYawField;
    private TextFieldWidget directTeleportOrientationPitchField;
    private CyclingButtonWidget<TeleporterBlockEntity.SpawnPointType> spawnPointTypeButton;
    private ButtonWidget removeLocationButton0;
    private ButtonWidget removeLocationButton1;
    private ButtonWidget removeLocationButton2;
    private TextFieldWidget newLocationIdentifierField;
    private TextFieldWidget newLocationEntranceField;
    private ButtonWidget addNewLocationButton;
    private TextFieldWidget teleporterNameField;
    private TextFieldWidget currentTargetOwnerLabelField;
    private TextFieldWidget currentTargetIdentifierLabelField;
    private TextFieldWidget teleportButtonLabelField;
    private TextFieldWidget cancelTeleportButtonLabelField;
    private ButtonWidget doneButton;
    private ButtonWidget cancelButton;

    // adventure mode
    private PlayerListEntry currentTargetOwner;
    private ButtonWidget openChooseTargetOwnerScreenButton;
    private ButtonWidget confirmChoosePublicButton;
    private ButtonWidget confirmChooseCurrentPlayerButton;
    private ButtonWidget confirmChooseTeamMember0Button;
    private ButtonWidget confirmChooseTeamMember1Button;
    private ButtonWidget confirmChooseTeamMember2Button;
    private ButtonWidget confirmChooseTeamMember3Button;
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

    private final boolean showCreativeTab;
    private CreativeScreenPage creativeScreenPage;
    private boolean showChooseTargetOwnerScreen;
    private boolean showChooseTargetIdentifierScreen;
    private boolean showRegenerationConfirmScreen;
    private boolean showActivationArea;
    private boolean canOwnerBeChosen;
    private boolean showAdventureScreen;
    private boolean setAccessPosition;
    private boolean onlyTeleportDimensionOwner;
    private boolean teleportTeam;

    private TeleporterBlockEntity.TeleportationMode teleportationMode;
    private TeleporterBlockEntity.SpawnPointType spawnPointType;

    List<Pair<String, String>> locationsList = new ArrayList<>();
    List<Pair<String, String>> visibleLocationsList = new ArrayList<>();
    List<Pair<String, String>> unlockedLocationsList = new ArrayList<>();
    List<PlayerListEntry> partyMemberList = new ArrayList<>();
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
    private boolean canLocationBeRegenerated = false;
    private boolean isRegenerateButtonActive = true;
    private boolean showCurrentLockAdvancement;
    private boolean showCurrentUnlockAdvancement;
    private boolean isCurrentLocationUnlocked;
    private Advancement currentLockAdvancement;
    private Advancement currentUnlockAdvancement;
    private boolean isCurrentLocationPublic;
    private boolean consumeKeyItem = false;
    private ItemUtils.VirtualItemStack currentKeyVirtualItemStack;

    public TeleporterBlockScreen(TeleporterBlockScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
        this.handler = handler;
        this.showCreativeTab = handler.getShowCreativeTab();
        this.creativeScreenPage = CreativeScreenPage.ACTIVATION;
    }

    //region button callbacks
//    private void teleport() {
//        this.tryTeleport();
//    }

    private void cancelTeleport() {
        this.close();
    }

    private void openChooseCurrentTargetOwnerScreen() {
        this.partyMemberList.clear();
        if (this.client != null && this.client.player != null) {
            Team team = this.client.player.getScoreboardTeam();
            if (team != null) {
                for (String name : team.getPlayerList()) {
                    if (!this.client.player.getName().getString().equals(name)) {
                        this.partyMemberList.add(this.client.player.networkHandler.getPlayerListEntry(name));
                    }
                }
            }
        }
        this.showChooseTargetOwnerScreen = true;
        this.updateWidgets();
    }

//    private void confirmChooseCurrentTargetOwner() {
//        this.showChooseTargetOwnerScreen = false;
////        this.thisMethodHandlesTheAdvancementStuff(false);
//        this.updateWidgets();
//    }

    private void chooseTargetOwner(int index) {
        this.canLocationBeRegenerated = false;
        if (index == -2) {
            this.currentTargetOwner = null;
        } else if (index == -1 && this.client != null && this.client.player != null) {
            this.currentTargetOwner = this.client.player.networkHandler.getPlayerListEntry(this.client.player.getUuid());
            this.canLocationBeRegenerated = true;
        } else {
            this.currentTargetOwner = this.partyMemberList.get(this.teamListScrollPosition + index);
        }
        this.showChooseTargetOwnerScreen = false;
        this.calculateUnlockedAndVisibleLocations(false);
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
        this.currentTargetIdentifier = this.visibleLocationsList.get(this.visibleLocationsListScrollPosition + index).getLeft();
        this.currentTargetEntrance = this.visibleLocationsList.get(this.visibleLocationsListScrollPosition + index).getRight();
        this.showChooseTargetIdentifierScreen = false;
        this.calculateUnlockedAndVisibleLocations(false);
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
        this.updateTeleporterBlock();
        this.close();
    }

    private void cancel() {
        this.teleporterBlock.setShowActivationArea(this.showActivationArea);
        this.teleporterBlock.setShowAdventureScreen(this.showAdventureScreen);
        this.teleporterBlock.setSetAccessPosition(this.setAccessPosition);
        this.teleporterBlock.setOnlyTeleportDimensionOwner(this.onlyTeleportDimensionOwner);
        this.teleporterBlock.setTeleportTeam(this.teleportTeam);
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
        BetterAdventureMode.LOGGER.info(identifier);
        Text message = Text.literal("");
        if (Identifier.isValid(identifier)) {
            Location location = LocationsRegistry.getLocation(Identifier.tryParse(identifier));
            if (location != null) {
                if (!location.hasEntrance(entrance)) {
                    entrance = "";
                }
                boolean bl = false;
                for (Pair<String, String> locationsListEntry : this.locationsList) {
                    if (locationsListEntry.getLeft().equals(identifier) && locationsListEntry.getRight().equals(entrance)) {
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
            if (blockEntity instanceof TeleporterBlockEntity) {
                this.teleporterBlock = (TeleporterBlockEntity) blockEntity;
            }
            if (this.client.player != null) {
                this.currentTargetOwner = this.client.player.networkHandler.getPlayerListEntry(this.client.player.getUuid());
                this.canLocationBeRegenerated = true;
            }
        }
        this.locationsList.clear();
        this.locationsList.addAll(this.teleporterBlock.getLocationsList());
        this.showAdventureScreen = this.teleporterBlock.getShowAdventureScreen();
        this.teleportationMode = this.teleporterBlock.getTeleportationMode();
        this.currentTargetIdentifier = "";
        this.currentTargetDisplayName = "";
        this.currentTargetEntrance = "";
        this.currentTargetEntranceDisplayName = "";
        if (!this.showCreativeTab) {
            if ((this.teleportationMode == TeleporterBlockEntity.TeleportationMode.DIRECT || this.teleportationMode == TeleporterBlockEntity.TeleportationMode.SPAWN_POINTS) && !this.showAdventureScreen) {
                this.teleport();
            }
            this.backgroundWidth = 218;
            if (this.teleporterBlock.getTeleportationMode() == TeleporterBlockEntity.TeleportationMode.LOCATIONS) {
                this.backgroundHeight = 171;//147;
                this.calculateUnlockedAndVisibleLocations(true);
                if (!this.showAdventureScreen) {
                    this.teleport();
                }
            } else {
                this.backgroundHeight = 47;
            }
        }
        super.init();
        //region adventure screen

        this.openChooseTargetIdentifierScreenButton = this.addDrawableChild(ButtonWidget.builder(EDIT_BUTTON_LABEL_TEXT, button -> this.openChooseTargetIdentifierScreen()).dimensions(this.x + this.backgroundWidth - 57, this.y + 21, 50, 20).build());
        this.confirmChooseTargetIdentifier0Button = this.addDrawableChild(ButtonWidget.builder(CHOOSE_BUTTON_LABEL_TEXT, button -> this.chooseTargetIdentifier(0)).dimensions(this.x + this.backgroundWidth - 57, this.y + 20, 50, 20).build());
        this.confirmChooseTargetIdentifier1Button = this.addDrawableChild(ButtonWidget.builder(CHOOSE_BUTTON_LABEL_TEXT, button -> this.chooseTargetIdentifier(1)).dimensions(this.x + this.backgroundWidth - 57, this.y + 44, 50, 20).build());
        this.confirmChooseTargetIdentifier2Button = this.addDrawableChild(ButtonWidget.builder(CHOOSE_BUTTON_LABEL_TEXT, button -> this.chooseTargetIdentifier(2)).dimensions(this.x + this.backgroundWidth - 57, this.y + 68, 50, 20).build());
        this.confirmChooseTargetIdentifier3Button = this.addDrawableChild(ButtonWidget.builder(CHOOSE_BUTTON_LABEL_TEXT, button -> this.chooseTargetIdentifier(3)).dimensions(this.x + this.backgroundWidth - 57, this.y + 92, 50, 20).build());
        this.cancelChooseTargetIdentifierButton = this.addDrawableChild(ButtonWidget.builder(CANCEL_BUTTON_LABEL_TEXT, button -> this.cancelChooseTargetLocation()).dimensions(this.x + 7, this.y + this.backgroundHeight - 27, this.backgroundWidth - 14, 20).build());

        this.openChooseTargetOwnerScreenButton = this.addDrawableChild(ButtonWidget.builder(EDIT_BUTTON_LABEL_TEXT, button -> this.openChooseCurrentTargetOwnerScreen()).dimensions(this.x + this.backgroundWidth - 57, this.y + 71, 50, 20).build());
        this.confirmChoosePublicButton = this.addDrawableChild(ButtonWidget.builder(CHOOSE_BUTTON_LABEL_TEXT, button -> this.chooseTargetOwner(-2)).dimensions(this.x + this.backgroundWidth - 57, this.y + 20, 50, 20).build());
        this.confirmChooseCurrentPlayerButton = this.addDrawableChild(ButtonWidget.builder(CHOOSE_BUTTON_LABEL_TEXT, button -> this.chooseTargetOwner(-1)).dimensions(this.x + this.backgroundWidth - 57, this.y + 44, 50, 20).build());
        this.confirmChooseTeamMember0Button = this.addDrawableChild(ButtonWidget.builder(CHOOSE_BUTTON_LABEL_TEXT, button -> this.chooseTargetOwner(0)).dimensions(this.x + this.backgroundWidth - 57, this.y + 68, 50, 20).build());
        this.confirmChooseTeamMember1Button = this.addDrawableChild(ButtonWidget.builder(CHOOSE_BUTTON_LABEL_TEXT, button -> this.chooseTargetOwner(1)).dimensions(this.x + this.backgroundWidth - 57, this.y + 92, 50, 20).build());
        this.confirmChooseTeamMember2Button = this.addDrawableChild(ButtonWidget.builder(CHOOSE_BUTTON_LABEL_TEXT, button -> this.chooseTargetOwner(2)).dimensions(this.x + this.backgroundWidth - 57, this.y + 116, 50, 20).build());
        this.confirmChooseTeamMember3Button = this.addDrawableChild(ButtonWidget.builder(CHOOSE_BUTTON_LABEL_TEXT, button -> this.chooseTargetOwner(3)).dimensions(this.x + this.backgroundWidth - 57, this.y + 140, 50, 20).build());
        this.cancelChooseTargetOwnerButton = this.addDrawableChild(ButtonWidget.builder(CANCEL_BUTTON_LABEL_TEXT, button -> this.cancelChooseCurrentTargetOwner()).dimensions(this.x + 7, this.y + this.backgroundHeight - 27, this.backgroundWidth - 14, 20).build());

        this.openDungeonRegenerationScreenButton = this.addDrawableChild(ButtonWidget.builder(EDIT_BUTTON_LABEL_TEXT, button -> this.openDungeonRegenerationConfirmScreen()).dimensions(this.x + 7, this.y + this.backgroundHeight - 51, this.backgroundWidth - 14, 20).build());
        this.confirmDungeonRegenerationButton = this.addDrawableChild(ButtonWidget.builder(CHOOSE_BUTTON_LABEL_TEXT, button -> this.confirmDungeonRegeneration()).dimensions(this.x + 7, this.y + this.backgroundHeight - 27, (this.backgroundWidth - 18) / 2, 20).build());
        this.cancelDungeonRegenerationButton = this.addDrawableChild(ButtonWidget.builder(CANCEL_BUTTON_LABEL_TEXT, button -> this.cancelDungeonRegeneration()).dimensions(this.x + this.backgroundWidth / 2 + 2, this.y + this.backgroundHeight - 27, (this.backgroundWidth - 18) / 2, 20).build());

        this.teleportButton = this.addDrawableChild(ButtonWidget.builder(Text.translatable(this.teleporterBlock.getTeleportButtonLabel()), button -> this.teleport()).dimensions(this.x + 7, this.y + this.backgroundHeight - 27, 100, 20).build());
        this.cancelTeleportButton = this.addDrawableChild(ButtonWidget.builder(Text.translatable(this.teleporterBlock.getCancelTeleportButtonLabel()), button -> this.cancelTeleport()).dimensions(this.x + this.backgroundWidth - 107, this.y + this.backgroundHeight - 27, 100, 20).build());

        //endregion adventure screen

        //region creative screen
        this.creativeScreenPageButton = this.addDrawableChild(CyclingButtonWidget.builder(CreativeScreenPage::asText).values((CreativeScreenPage[]) CreativeScreenPage.values()).initially(this.creativeScreenPage).omitKeyText().build(this.width / 2 - 154, 20, 300, 20, Text.empty(), (button, creativeScreenPage) -> {
            this.creativeScreenPage = creativeScreenPage;
            this.updateWidgets();
        }));

        // --- activation page ---

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

        this.accessPositionOffsetXField = new TextFieldWidget(this.textRenderer, this.width / 2 - 154, 150, 50, 20, Text.empty());
        this.accessPositionOffsetXField.setMaxLength(128);
        this.accessPositionOffsetXField.setText(Integer.toString(this.teleporterBlock.getAccessPositionOffset().getX()));
        this.addSelectableChild(this.accessPositionOffsetXField);

        this.accessPositionOffsetYField = new TextFieldWidget(this.textRenderer, this.width / 2 - 100, 150, 50, 20, Text.empty());
        this.accessPositionOffsetYField.setMaxLength(128);
        this.accessPositionOffsetYField.setText(Integer.toString(this.teleporterBlock.getAccessPositionOffset().getY()));
        this.addSelectableChild(this.accessPositionOffsetYField);

        this.accessPositionOffsetZField = new TextFieldWidget(this.textRenderer, this.width / 2 - 46, 150, 50, 20, Text.empty());
        this.accessPositionOffsetZField.setMaxLength(128);
        this.accessPositionOffsetZField.setText(Integer.toString(this.teleporterBlock.getAccessPositionOffset().getZ()));
        this.addSelectableChild(this.accessPositionOffsetZField);

//        int i = this.textRenderer.getWidth(SET_ACCESS_POSITION_LABEL_TEXT) + 10;
        this.setAccessPosition = this.teleporterBlock.getSetAccessPosition();
        this.toggleSetAccessPositionButton = this.addDrawableChild(CyclingButtonWidget.onOffBuilder(TOGGLE_SET_ACCESS_POSITION_BUTTON_LABEL_TEXT_ON, TOGGLE_SET_ACCESS_POSITION_BUTTON_LABEL_TEXT_OFF).initially(this.setAccessPosition).omitKeyText().build(this.width / 2 + 8, 150, 150, 20, Text.empty(), (button, setAccessPosition) -> {
            this.setAccessPosition = setAccessPosition;
        }));

        this.onlyTeleportDimensionOwner = this.teleporterBlock.onlyTeleportDimensionOwner();
        this.toggleOnlyTeleportDimensionOwnerButton = this.addDrawableChild(CyclingButtonWidget.onOffBuilder(TOGGLE_ONLY_TELEPORT_DIMENSION_OWNER_BUTTON_LABEL_TEXT_ON, TOGGLE_ONLY_TELEPORT_DIMENSION_OWNER_BUTTON_LABEL_TEXT_OFF).initially(this.onlyTeleportDimensionOwner).omitKeyText().build(this.width / 2 - 154, 175, 150, 20, Text.empty(), (button, onlyTeleportDimensionOwner) -> {
            this.onlyTeleportDimensionOwner = onlyTeleportDimensionOwner;
        }));

        this.teleportTeam = this.teleporterBlock.teleportTeam();
        this.toggleTeleportTeamButton = this.addDrawableChild(CyclingButtonWidget.onOffBuilder(TOGGLE_TELEPORT_TEAM_BUTTON_LABEL_TEXT_ON, TOGGLE_TELEPORT_TEAM_BUTTON_LABEL_TEXT_OFF).initially(this.teleportTeam).omitKeyText().build(this.width / 2 + 4, 175, 150, 20, Text.empty(), (button, teleportTeam) -> {
            this.teleportTeam = teleportTeam;
        }));


        // --- teleportation mode page ---

        int i = this.textRenderer.getWidth(TELEPORTATION_MODE_LABEL_TEXT) + 10;
        this.teleportationModeButton = this.addDrawableChild(CyclingButtonWidget.builder(TeleporterBlockEntity.TeleportationMode::asText).values((TeleporterBlockEntity.TeleportationMode[]) TeleporterBlockEntity.TeleportationMode.values()).initially(this.teleportationMode).omitKeyText().build(this.width / 2 - 152 + i, 45, 300 - i, 20, TELEPORTATION_MODE_LABEL_TEXT, (button, teleportationMode) -> {
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

        // teleportation mode: spawn_points

        this.spawnPointType = this.teleporterBlock.getLocationType();
        i = this.textRenderer.getWidth(SPAWN_POINT_TYPE_LABEL_TEXT) + 10;
        this.spawnPointTypeButton = this.addDrawableChild(CyclingButtonWidget.builder(TeleporterBlockEntity.SpawnPointType::asText).values((TeleporterBlockEntity.SpawnPointType[]) TeleporterBlockEntity.SpawnPointType.values()).initially(this.spawnPointType).omitKeyText().build(this.width / 2 - 152 + i, 70, 300 - i, 20, SPAWN_POINT_TYPE_LABEL_TEXT, (button, locationType) -> {
            this.spawnPointType = locationType;
        }));

        // teleportation mode: player_locations

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

        this.openChooseTargetIdentifierScreenButton.visible = false;
        this.confirmChooseTargetIdentifier0Button.visible = false;
        this.confirmChooseTargetIdentifier1Button.visible = false;
        this.confirmChooseTargetIdentifier2Button.visible = false;
        this.confirmChooseTargetIdentifier3Button.visible = false;
        this.cancelChooseTargetIdentifierButton.visible = false;

        this.openChooseTargetOwnerScreenButton.visible = false;
        this.confirmChoosePublicButton.visible = false;
        this.confirmChooseCurrentPlayerButton.visible = false;
        this.confirmChooseTeamMember0Button.visible = false;
        this.confirmChooseTeamMember1Button.visible = false;
        this.confirmChooseTeamMember2Button.visible = false;
        this.confirmChooseTeamMember3Button.visible = false;
        this.cancelChooseTargetOwnerButton.visible = false;

        this.openDungeonRegenerationScreenButton.visible = false;
        this.confirmDungeonRegenerationButton.visible = false;
        this.cancelDungeonRegenerationButton.visible = false;

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
        this.toggleOnlyTeleportDimensionOwnerButton.visible = false;
        this.toggleTeleportTeamButton.visible = false;

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
        this.currentTargetIdentifierLabelField.setVisible(false);
        this.currentTargetOwnerLabelField.setVisible(false);
        this.teleportButtonLabelField.setVisible(false);
        this.cancelTeleportButtonLabelField.setVisible(false);

        this.doneButton.visible = false;
        this.cancelButton.visible = false;

        for (Slot slot : this.handler.slots) {
            ((DuckSlotMixin) slot).betteradventuremode$setDisabledOverride(true);
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
                this.toggleOnlyTeleportDimensionOwnerButton.visible = true;
                this.toggleTeleportTeamButton.visible = true;

            } else if (this.creativeScreenPage == CreativeScreenPage.TELEPORTATION_MODE) {

                this.teleportationModeButton.visible = true;

                if (this.teleportationMode == TeleporterBlockEntity.TeleportationMode.DIRECT) {

                    this.directTeleportPositionOffsetXField.setVisible(true);
                    this.directTeleportPositionOffsetYField.setVisible(true);
                    this.directTeleportPositionOffsetZField.setVisible(true);
                    this.directTeleportOrientationYawField.setVisible(true);
                    this.directTeleportOrientationPitchField.setVisible(true);

                } else if (this.teleportationMode == TeleporterBlockEntity.TeleportationMode.SPAWN_POINTS) {

                    this.spawnPointTypeButton.visible = true;

                } else if (this.teleportationMode == TeleporterBlockEntity.TeleportationMode.LOCATIONS) {

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

                if (this.teleportationMode == TeleporterBlockEntity.TeleportationMode.LOCATIONS) {
                    this.currentTargetIdentifierLabelField.setVisible(true);
                    this.currentTargetOwnerLabelField.setVisible(true);
                }

            }

            this.doneButton.visible = true;
            this.cancelButton.visible = true;

        } else {

            if (this.showChooseTargetIdentifierScreen) {

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

            } else if (this.showChooseTargetOwnerScreen) {

                if (this.isCurrentLocationPublic) {
                    this.confirmChooseCurrentPlayerButton.setY(this.y + 44);
                    this.confirmChooseTeamMember0Button.setY(this.y + 68);
                    this.confirmChooseTeamMember1Button.setY(this.y + 92);
                    this.confirmChooseTeamMember2Button.setY(this.y + 116);
                    this.confirmChooseTeamMember3Button.setY(this.y + 140);
                    this.confirmChoosePublicButton.visible = true;
                } else {
                    this.confirmChooseCurrentPlayerButton.setY(this.y + 20);
                    this.confirmChooseTeamMember0Button.setY(this.y + 44);
                    this.confirmChooseTeamMember1Button.setY(this.y + 68);
                    this.confirmChooseTeamMember2Button.setY(this.y + 92);
                    this.confirmChooseTeamMember3Button.setY(this.y + 116);
                }
                if (!this.isCurrentLocationPublic) {
                    this.confirmChooseCurrentPlayerButton.visible = true;

                    int index = 0;
                    for (int i = 0; i < Math.min(4, this.partyMemberList.size()); i++) {
                        if (index == 0) {
                            this.confirmChooseTeamMember0Button.visible = true;
                        } else if (index == 1) {
                            this.confirmChooseTeamMember1Button.visible = true;
                        } else if (index == 2) {
                            this.confirmChooseTeamMember2Button.visible = true;
                        } else if (index == 3) {
                            this.confirmChooseTeamMember3Button.visible = true;
                        }
                        index++;
                    }

                }
                this.cancelChooseTargetOwnerButton.visible = true;

            } else if (this.showRegenerationConfirmScreen) {

                this.confirmDungeonRegenerationButton.visible = true;
                this.cancelDungeonRegenerationButton.visible = true;

            } else if (this.showAdventureScreen) {

                if (this.teleportationMode == TeleporterBlockEntity.TeleportationMode.LOCATIONS) {

                    this.openChooseTargetIdentifierScreenButton.visible = this.visibleLocationsList.size() > 1;

                    if (!this.isCurrentLocationPublic) {
                        this.openChooseTargetOwnerScreenButton.visible = this.canOwnerBeChosen;
                    }

                    this.openDungeonRegenerationScreenButton.visible = true;
                }

                this.teleportButton.visible = true;
                this.cancelTeleportButton.visible = true;

                this.openDungeonRegenerationScreenButton.active = this.isRegenerateButtonActive;
                this.teleportButton.active = this.isTeleportButtonActive;
            }
        }
    }

    private void calculateUnlockedAndVisibleLocations(boolean shouldInit) {

        ClientAdvancementManager advancementHandler = null;
        String lockAdvancementIdentifier;
        String unlockAdvancementIdentifier;
        boolean showLockedLocation;

        if (this.client != null && this.client.player != null) {
            advancementHandler = this.client.player.networkHandler.getAdvancementHandler();
        }

        if (shouldInit) {
            if (this.teleporterBlock.getTeleportationMode() == TeleporterBlockEntity.TeleportationMode.LOCATIONS) {
                this.unlockedLocationsList.clear();
                this.visibleLocationsList.clear();
                for (Pair<String, String> stringStringPair : this.locationsList) {
                    Location location = LocationsRegistry.getLocation(new Identifier(stringStringPair.getLeft()));
                    lockAdvancementIdentifier = location.lockAdvancement();
                    unlockAdvancementIdentifier = location.unlockAdvancement();
                    showLockedLocation = location.showLockedDungeon();

                    if (advancementHandler != null) {
                        AdvancementEntry lockAdvancementEntry = null;
                        if (!lockAdvancementIdentifier.equals("")) {
                            lockAdvancementEntry = advancementHandler.get(Identifier.tryParse(lockAdvancementIdentifier));
                        }
                        AdvancementEntry unlockAdvancementEntry = null;
                        if (!unlockAdvancementIdentifier.equals("")) {
                            unlockAdvancementEntry = advancementHandler.get(Identifier.tryParse(unlockAdvancementIdentifier));
                        }
                        if ((lockAdvancementIdentifier.equals("") || (lockAdvancementEntry != null && !((DuckClientAdvancementManagerMixin) advancementHandler).betteradventuremode$getAdvancementProgress(lockAdvancementEntry).isDone())) && (unlockAdvancementIdentifier.equals("") || (unlockAdvancementEntry != null && ((DuckClientAdvancementManagerMixin) advancementHandler).betteradventuremode$getAdvancementProgress(unlockAdvancementEntry).isDone()))) {
                            this.unlockedLocationsList.add(stringStringPair);
                            this.visibleLocationsList.add(stringStringPair);
                        } else if (showLockedLocation) {
                            this.visibleLocationsList.add(stringStringPair);
                        }
                    }
                }
                if (!this.visibleLocationsList.isEmpty()) {
                    this.currentTargetIdentifier = this.visibleLocationsList.get(0).getLeft();
                    this.currentTargetEntrance = this.visibleLocationsList.get(0).getRight();
                }
            }
        }

        for (Pair<String, String> dungeonLocation : this.unlockedLocationsList) {
            if (Objects.equals(dungeonLocation.getLeft(), this.currentTargetIdentifier)) {
                this.isCurrentLocationUnlocked = true;
            }
        }

        Location location = LocationsRegistry.getLocation(Identifier.tryParse(this.currentTargetIdentifier));
        if (location != null) {
            lockAdvancementIdentifier = location.lockAdvancement();
            unlockAdvancementIdentifier = location.unlockAdvancement();
            this.showCurrentLockAdvancement = location.showLockAdvancement();
            this.showCurrentUnlockAdvancement = location.showUnlockAdvancement();
            this.currentTargetDisplayName = location.getDisplayName();
            this.currentTargetEntranceDisplayName = location.getEntranceDisplayName(this.currentTargetEntrance);
            this.isCurrentLocationPublic = location.isPublic();
//            this.isCurrentLocationPlayer = location.playerLocation();
            this.consumeKeyItem = location.consumeKeyAtEntrance(this.currentTargetEntrance);
            this.currentKeyVirtualItemStack = location.getKeyForEntrance(this.currentTargetEntrance);
            this.canOwnerBeChosen = location.canOwnerBeChosen();
//            if (location.stayInCurrentDimension()) {
//                if (this.teleporterBlock.getWorld() != null) {
//                    String currentWorld = this.teleporterBlock.getWorld().getRegistryKey().getValue().toString();
//                    if (UUIDUtilities.isStringValidUUID(currentWorld)) {
//                        UUID newTargetOwnerUuid = UUID.fromString(currentWorld);
//                        if (this.client != null && this.client.player != null) {
//                            this.currentTargetOwner = this.client.player.networkHandler.getPlayerListEntry(newTargetOwnerUuid);
//                        }
//                    }
//                }
//                this.canOwnerBeChosen = false;
//            }
            this.showAdventureScreen = location.showAdventureScreen();
            if (advancementHandler != null) {
                AdvancementEntry lockAdvancementEntry = null;
                if (!lockAdvancementIdentifier.equals("")) {
                    lockAdvancementEntry = advancementHandler.get(Identifier.tryParse(lockAdvancementIdentifier));
                }
                AdvancementEntry unlockAdvancementEntry = null;
                if (!unlockAdvancementIdentifier.equals("")) {
                    unlockAdvancementEntry = advancementHandler.get(Identifier.tryParse(unlockAdvancementIdentifier));
                }
                if (lockAdvancementEntry != null) {
                    this.currentLockAdvancement = lockAdvancementEntry.value();
                }
                if (unlockAdvancementEntry != null) {
                    this.currentUnlockAdvancement = unlockAdvancementEntry.value();
                }

                Inventory inventory = new SimpleInventory(36);
                for (int k = 0; k < 36; k++) {
                    ItemStack itemStack = this.handler.getPlayerInventory().getStack(k);
                    inventory.setStack(k, itemStack.copy());
                }
                boolean bl = true;
                if (this.currentKeyVirtualItemStack != null) {
                    ItemStack currentKeyItemStack = ItemUtils.getItemStackFromVirtualItemStack(this.currentKeyVirtualItemStack);
                    int keyCount = currentKeyItemStack.getCount();
                    for (int j = 0; j < inventory.size(); j++) {
                        ItemStack itemStack = inventory.getStack(j);
                        if (ItemStack.canCombine(currentKeyItemStack, itemStack)) {
                            int stackCount = inventory.getStack(j).getCount();
                            if (stackCount >= keyCount) {
                                inventory.removeStack(j, keyCount);
                                keyCount = 0;
                                break;
                            } else {
                                inventory.setStack(j, ItemStack.EMPTY);
                                keyCount = keyCount - stackCount;
                            }
                        }
                    }
                    if (keyCount > 0) {
                        bl = false;
                    }
                }

                this.isRegenerateButtonActive = this.isCurrentLocationUnlocked && this.canLocationBeRegenerated && this.currentTargetOwner != null;

                this.isTeleportButtonActive = this.isCurrentLocationUnlocked && bl;
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
                && this.teleportationMode == TeleporterBlockEntity.TeleportationMode.LOCATIONS
                && this.locationsList.size() > 3) {
            i = this.width / 2 - 152;
            j = 71;
            if (mouseX >= (double) i && mouseX < (double) (i + 6) && mouseY >= (double) j && mouseY < (double) (j + 68)) {
                this.creativeLocationsListMouseClicked = true;
            }
        }
        // TODO team list
        if (this.showChooseTargetOwnerScreen) {
            i = this.x - 13;
            j = this.y + 134;
            if (mouseX >= (double) i && mouseX < (double) (i + 6) && mouseY >= (double) j && mouseY < (double) (j + 30)) {
                this.teamListMouseClicked = true;
            }
        }
        if (!this.showCreativeTab
                && this.showChooseTargetIdentifierScreen
                && this.teleportationMode == TeleporterBlockEntity.TeleportationMode.LOCATIONS
                && this.visibleLocationsList.size() > 4) {
            i = this.x + 8;
            j = this.y + 21;
            if (mouseX >= (double) i && mouseX < (double) (i + 6) && mouseY >= (double) j && mouseY < (double) (j + 90)) {
                this.visibleLocationsListMouseClicked = true;
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        if (this.showCreativeTab
                && this.creativeScreenPage == CreativeScreenPage.TELEPORTATION_MODE
                && this.teleportationMode == TeleporterBlockEntity.TeleportationMode.LOCATIONS
                && this.locationsList.size() > 3
                && this.creativeLocationsListMouseClicked) {
            int i = this.locationsList.size() - 3;
            float f = (float) deltaY / (float) i;
            this.creativeLocationsListScrollAmount = MathHelper.clamp(this.creativeLocationsListScrollAmount + f, 0.0f, 1.0f);
            this.creativeLocationsListScrollPosition = (int) ((double) (this.creativeLocationsListScrollAmount * (float) i));
        }
        // TODO team list
        if (!this.showCreativeTab
                && this.showChooseTargetIdentifierScreen
                && this.teleportationMode == TeleporterBlockEntity.TeleportationMode.LOCATIONS
                && this.visibleLocationsList.size() > 4
                && this.visibleLocationsListMouseClicked) {
            int i = this.visibleLocationsList.size() - 4;
            float f = (float) deltaY / (float) i;
            this.visibleLocationsListScrollAmount = MathHelper.clamp(this.visibleLocationsListScrollAmount + f, 0.0f, 1.0f);
            this.visibleLocationsListScrollPosition = (int) ((double) (this.visibleLocationsListScrollAmount * (float) i));
        }
        return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        // TODO
        if (this.showCreativeTab
                && this.creativeScreenPage == CreativeScreenPage.TELEPORTATION_MODE
                && this.teleportationMode == TeleporterBlockEntity.TeleportationMode.LOCATIONS
                && this.locationsList.size() > 3
                && mouseX >= (double) (this.width / 2 - 152) && mouseX <= (double) (this.width / 2 + 50)
                && mouseY >= 70 && mouseY <= 140) {
            int i = this.locationsList.size() - 3;
            float f = (float) verticalAmount / (float) i;
            this.creativeLocationsListScrollAmount = MathHelper.clamp(this.creativeLocationsListScrollAmount - f, 0.0f, 1.0f);
            this.creativeLocationsListScrollPosition = (int) ((double) (this.creativeLocationsListScrollAmount * (float) i));
        }
        // TODO team list
        if (!this.showCreativeTab
                && this.showChooseTargetIdentifierScreen
                && this.teleportationMode == TeleporterBlockEntity.TeleportationMode.LOCATIONS
                && this.visibleLocationsList.size() > 4
                && mouseX >= this.x + 7 && mouseX <= this.x + this.backgroundWidth - 61 && mouseY >= this.y + 20 && mouseY <= this.y + 112) {
            int i = this.visibleLocationsList.size() - 4;
            float f = (float) verticalAmount / (float) i;
            this.visibleLocationsListScrollAmount = MathHelper.clamp(this.visibleLocationsListScrollAmount - f, 0.0f, 1.0f);
            this.visibleLocationsListScrollPosition = (int) ((double) (this.visibleLocationsListScrollAmount * (float) i));
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
//                context.drawTextWithShadow(this.textRenderer, ACCESS_POSITION_OFFET_LABEL_TEXT, this.width / 2 - 153, 140, 0xA0A0A0);
                this.accessPositionOffsetXField.render(context, mouseX, mouseY, delta);
                this.accessPositionOffsetYField.render(context, mouseX, mouseY, delta);
                this.accessPositionOffsetZField.render(context, mouseX, mouseY, delta);
//                context.drawTextWithShadow(this.textRenderer, SET_ACCESS_POSITION_LABEL_TEXT, this.width / 2 + 59, 140, 0xA0A0A0);
            } else if (this.creativeScreenPage == CreativeScreenPage.TELEPORTATION_MODE) {
                context.drawTextWithShadow(this.textRenderer, TELEPORTATION_MODE_LABEL_TEXT, this.width / 2 - 153, 51, 0xA0A0A0);
                if (this.teleportationMode == TeleporterBlockEntity.TeleportationMode.DIRECT) {
                    context.drawTextWithShadow(this.textRenderer, DIRECT_TELEPORT_POSITION_OFFET_LABEL_TEXT, this.width / 2 - 153, 70, 0xA0A0A0);
                    this.directTeleportPositionOffsetXField.render(context, mouseX, mouseY, delta);
                    this.directTeleportPositionOffsetYField.render(context, mouseX, mouseY, delta);
                    this.directTeleportPositionOffsetZField.render(context, mouseX, mouseY, delta);
                    context.drawTextWithShadow(this.textRenderer, DIRECT_TELEPORT_ORIENTATION_LABEL_TEXT, this.width / 2 - 153, 105, 0xA0A0A0);
                    this.directTeleportOrientationYawField.render(context, mouseX, mouseY, delta);
                    this.directTeleportOrientationPitchField.render(context, mouseX, mouseY, delta);
                } else if (this.teleportationMode == TeleporterBlockEntity.TeleportationMode.SPAWN_POINTS) {
                    context.drawTextWithShadow(this.textRenderer, SPAWN_POINT_TYPE_LABEL_TEXT, this.width / 2 - 153, 76, 0xA0A0A0);
                } else if (this.teleportationMode == TeleporterBlockEntity.TeleportationMode.LOCATIONS) {
                    for (int i = this.creativeLocationsListScrollPosition; i < Math.min(this.creativeLocationsListScrollPosition + 3, this.locationsList.size()); i++) {
                        String text = this.locationsList.get(i).getLeft();
                        if (!this.locationsList.get(i).getRight().equals("")) {
                            text = this.locationsList.get(i).getLeft() + ", " + this.locationsList.get(i).getRight();
                        }
                        context.drawTextWithShadow(this.textRenderer, text, this.width / 2 - 141, 76 + ((i - this.creativeLocationsListScrollPosition) * 25), 0xA0A0A0);
                    }
                    if (this.locationsList.size() > 3) {
                        context.drawGuiTexture(SCROLL_BAR_BACKGROUND_8_70_TEXTURE, this.width / 2 - 153, 70, 8, 70);
                        int k = (int) (61.0f * this.creativeLocationsListScrollAmount);
                        context.drawGuiTexture(SCROLLER_TEXTURE, this.width / 2 - 152, 70 + 1 + k, 6, 7);
                    }
                    this.newLocationIdentifierField.render(context, mouseX, mouseY, delta);
                    this.newLocationEntranceField.render(context, mouseX, mouseY, delta);
                }
            } else if (this.creativeScreenPage == CreativeScreenPage.ADVENTURE_SCREEN_CUSTOMIZATION) {

                this.teleporterNameField.render(context, mouseX, mouseY, delta);

                if (this.teleportationMode == TeleporterBlockEntity.TeleportationMode.LOCATIONS) {

                    this.currentTargetIdentifierLabelField.render(context, mouseX, mouseY, delta);
                    this.currentTargetOwnerLabelField.render(context, mouseX, mouseY, delta);

                }

                this.teleportButtonLabelField.render(context, mouseX, mouseY, delta);
                this.cancelTeleportButtonLabelField.render(context, mouseX, mouseY, delta);
            }
        } else {

            TeleporterBlockEntity.TeleportationMode mode = this.teleporterBlock.getTeleportationMode();

            if (this.showChooseTargetIdentifierScreen) {

                for (int i = this.visibleLocationsListScrollPosition; i < Math.min(this.visibleLocationsListScrollPosition + 4, this.visibleLocationsList.size()); i++) {
                    context.drawText(this.textRenderer, this.visibleLocationsList.get(i).getLeft(), this.x + 19, this.y + 26 + ((i - this.visibleLocationsListScrollPosition) * 24), 0x404040, false);
                }
                if (this.visibleLocationsList.size() > 4) {
                    context.drawGuiTexture(CREATIVE_HOUSING_SCROLLER_BACKGROUND_TEXTURE, this.x + 7, this.y + 20, 8, 92);
                    int k = (int) (83.0f * this.visibleLocationsListScrollAmount);
                    context.drawGuiTexture(SCROLLER_TEXTURE, this.x + 8, this.y + 20 + 1 + k, 6, 7);
                }

            } else if (this.showChooseTargetOwnerScreen) {
                if (this.isCurrentLocationPublic) {
                    // TODO
                } else {
                    // TODO
                }
            } else if (this.showRegenerationConfirmScreen) {
                // TODO
            } else if (this.showAdventureScreen) {

                Text teleporterName = Text.translatable(this.teleporterBlock.getTeleporterName());
                int teleporterNameOffset = this.backgroundWidth / 2 - this.textRenderer.getWidth(teleporterName) / 2;
//                if (this.currentTargetOwner != null) {

                context.drawText(this.textRenderer, teleporterName, this.x + teleporterNameOffset, this.y + 7, 0x404040, false);

                if (mode == TeleporterBlockEntity.TeleportationMode.LOCATIONS) {

//                        context.drawText(this.textRenderer, Text.translatable(this.teleporterBlock.getCurrentTargetIdentifierLabel()), this.x + 8, this.y + 20, 0x404040, false);

                    if (!this.currentTargetEntrance.equals("")) {

                        context.drawText(this.textRenderer, Text.translatable(this.currentTargetEntranceDisplayName), this.x + 8, this.y + 20, 0x404040, false);
                        context.drawText(this.textRenderer, Text.translatable(this.currentTargetDisplayName), this.x + 8, this.y + 33, 0x404040, false);

                    } else {

                        context.drawText(this.textRenderer, Text.translatable(this.currentTargetDisplayName), this.x + 8, this.y + 27, 0x404040, false);

                    }

                    if (this.isCurrentLocationPublic) {
                        context.drawText(this.textRenderer, LOCATION_IS_PUBLIC_TEXT, this.x + 19, this.y + 77, 0x404040, false);
                    } else if (this.currentTargetOwner != null) {
                        context.drawText(this.textRenderer, Text.translatable(this.teleporterBlock.getCurrentTargetOwnerLabel()), this.x + 8, this.y + 58, 0x404040, false);

                        context.drawTexture(currentTargetOwner.getSkinTextures().texture(), this.x + 7, this.y + 77, 8, 8, 8, 8, 8, 8, 64, 64);
                        context.drawText(this.textRenderer, currentTargetOwner.getProfile().getName(), this.x + 19, this.y + 77, 0x404040, false);
                    }

                    if (this.currentKeyVirtualItemStack != null) {
                        ItemStack currentKey = ItemUtils.getItemStackFromVirtualItemStack(this.currentKeyVirtualItemStack);
//                            BetterAdventureMode.info("should draw item: " + currentKey.toString());
                        int x = this.x + 8;
                        int y = this.y + 95;
                        int k = x + y * this.backgroundWidth;
                        context.drawItemWithoutEntity(currentKey, x, y, k);
                        context.drawItemInSlot(this.textRenderer, currentKey, x, y);
                        context.drawText(this.textRenderer, this.consumeKeyItem ? KEY_ITEM_IS_CONSUMED_TEXT : KEY_ITEM_IS_REQUIRED_TEXT, x + 18, y + 5, 0x404040, false);
                    }
                }
//                }
            }
        }
//        context.drawTextWithShadow(this.textRenderer, CONSUME_KEY_ITEMSTACK_LABEL_TEXT, this.width / 2 - 153, 221, 0x404040);
    }

    @Override
    protected void drawForeground(DrawContext context, int mouseX, int mouseY) {
    }

    @Override
    protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
        if (!this.showCreativeTab && this.showAdventureScreen) {
            int i = this.x;
            int j = this.y;
            if (this.teleporterBlock.getTeleportationMode() == TeleporterBlockEntity.TeleportationMode.LOCATIONS) {
                context.drawTexture(ADVENTURE_TELEPORTER_LOCATIONS_SCREEN_BACKGROUND_TEXTURE, i, j, 0, 0, this.backgroundWidth, this.backgroundHeight, this.backgroundWidth, this.backgroundHeight);
            } else {
                context.drawTexture(ADVENTURE_TELEPORTER_SCREEN_BACKGROUND_TEXTURE, i, j, 0, 0, this.backgroundWidth, this.backgroundHeight, this.backgroundWidth, this.backgroundHeight);
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
        optional.ifPresent(text -> context.drawOrderedTooltip(this.textRenderer, this.textRenderer.wrapLines((StringVisitable) text, 115), mouseX, mouseY));
    }

    private void updateTeleporterBlock() {

        BlockPos directTeleportPositionOffset;
        double directTeleportPositionOffsetYaw;
        double directTeleportPositionOffsetPitch;
        if (this.teleportationMode == TeleporterBlockEntity.TeleportationMode.DIRECT) {
            directTeleportPositionOffset = new BlockPos(
                    ItemUtils.parseInt(this.directTeleportPositionOffsetXField.getText()),
                    ItemUtils.parseInt(this.directTeleportPositionOffsetYField.getText()),
                    ItemUtils.parseInt(this.directTeleportPositionOffsetZField.getText())
            );

            directTeleportPositionOffsetYaw = ItemUtils.parseDouble(this.directTeleportOrientationYawField.getText());
            directTeleportPositionOffsetPitch = ItemUtils.parseDouble(this.directTeleportOrientationPitchField.getText());
        } else {
            directTeleportPositionOffset = new BlockPos(0, 0, 0);
            directTeleportPositionOffsetYaw = 0.0;
            directTeleportPositionOffsetPitch = 0.0;
        }
        TeleporterBlockEntity.SpawnPointType spawnPointType;
        if (this.teleportationMode == TeleporterBlockEntity.TeleportationMode.SPAWN_POINTS) {

            spawnPointType = this.spawnPointType;

        } else {
            spawnPointType = TeleporterBlockEntity.SpawnPointType.WORLD_SPAWN;
        }

        List<Pair<String, String>> locationsList = new ArrayList<Pair<String, String>>();
        if (this.teleportationMode == TeleporterBlockEntity.TeleportationMode.LOCATIONS) {
            locationsList = this.locationsList;
        }

        ClientPlayNetworking.send(new UpdateTeleporterBlockPacket(
                this.teleporterBlock.getPos(),
                this.showActivationArea,
                this.showAdventureScreen,
                new Vec3i(
                        ItemUtils.parseInt(this.activationAreaDimensionsXField.getText()),
                        ItemUtils.parseInt(this.activationAreaDimensionsYField.getText()),
                        ItemUtils.parseInt(this.activationAreaDimensionsZField.getText())
                ),
                new BlockPos(
                        ItemUtils.parseInt(this.activationAreaPositionOffsetXField.getText()),
                        ItemUtils.parseInt(this.activationAreaPositionOffsetYField.getText()),
                        ItemUtils.parseInt(this.activationAreaPositionOffsetZField.getText())
                ),
                new BlockPos(
                        ItemUtils.parseInt(this.accessPositionOffsetXField.getText()),
                        ItemUtils.parseInt(this.accessPositionOffsetYField.getText()),
                        ItemUtils.parseInt(this.accessPositionOffsetZField.getText())
                ),
                this.setAccessPosition,
                this.onlyTeleportDimensionOwner,
                this.teleportTeam,
                this.teleportationMode.asString(),
                directTeleportPositionOffset,
                directTeleportPositionOffsetYaw,
                directTeleportPositionOffsetPitch,
                spawnPointType.asString(),
                locationsList,
                this.teleporterNameField.getText(),
                this.currentTargetIdentifierLabelField.getText(),
                this.currentTargetOwnerLabelField.getText(),
                this.teleportButtonLabelField.getText(),
                this.cancelTeleportButtonLabelField.getText()
        ));
    }

    private boolean tryDungeonRegeneration() {
        if (this.canLocationBeRegenerated) {
            Location location = LocationsRegistry.getLocation(Identifier.tryParse(this.currentTargetIdentifier));
            if (location != null) {
                ClientPlayNetworking.send(new SetManualResetLocationControlBlockPacket(
                        location.controlBlockPos(),
                        true
                ));
                return true;
            }
        }
        return false;
    }

    private void teleport() {
        String currentWorld = "";
        String currentTargetOwnerName = "";
        if (this.teleporterBlock.getWorld() != null) {
            currentWorld = this.teleporterBlock.getWorld().getRegistryKey().getValue().toString();
        }
        if (this.currentTargetOwner != null) {
            currentTargetOwnerName = this.currentTargetOwner.getProfile().getName();
        }
        if (this.isCurrentLocationPublic) {
            currentTargetOwnerName = "";
        }
        ClientPlayNetworking.send(new TeleportFromTeleporterBlockPacket(
                this.teleporterBlock.getPos(),
                currentWorld,
                this.teleporterBlock.getAccessPositionOffset(),
                this.teleporterBlock.getSetAccessPosition(),
                this.teleporterBlock.teleportTeam(),
                this.teleporterBlock.getTeleportationMode().asString(),
                this.teleporterBlock.getDirectTeleportPositionOffset(),
                this.teleporterBlock.getDirectTeleportOrientationYaw(),
                this.teleporterBlock.getDirectTeleportOrientationPitch(),
                this.teleporterBlock.getLocationType().asString(),
                currentTargetOwnerName,
                this.currentTargetIdentifier,
                this.currentTargetEntrance)
        );
    }

    private void givePortalResistanceEffect() {
        ClientPlayNetworking.send(new AddStatusEffectPacket(
                Registries.STATUS_EFFECT.getId(StatusEffectsRegistry.PORTAL_RESISTANCE_EFFECT),
                40,
                0,
                false,
                false,
                false,
                false)
        );
    }

    public static enum CreativeScreenPage implements StringIdentifiable {
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
