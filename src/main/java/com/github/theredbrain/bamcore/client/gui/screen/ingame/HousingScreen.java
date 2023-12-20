package com.github.theredbrain.bamcore.client.gui.screen.ingame;

import com.github.theredbrain.bamcore.BetterAdventureModeCore;
import com.github.theredbrain.bamcore.block.entity.HousingBlockBlockEntity;
import com.github.theredbrain.bamcore.network.packet.*;
import com.github.theredbrain.bamcore.registry.StatusEffectsRegistry;
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
import net.minecraft.registry.Registries;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3i;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.glfw.GLFW;

import java.util.*;

@Environment(EnvType.CLIENT)
public class HousingScreen extends Screen {
    // adventure
    private static final Text TITLE_OWNER_LABEL_TEXT = Text.translatable("gui.housing_screen.title.owner");
    private static final Text TITLE_CO_OWNER_LABEL_TEXT = Text.translatable("gui.housing_screen.title.co_owner");
    private static final Text TITLE_CO_OWNER_LIST_LABEL_TEXT = Text.translatable("gui.housing_screen.co_owner_list.title");
    private static final Text TITLE_CO_OWNER_LIST_DESCRIPTION_LABEL_TEXT = Text.translatable("gui.housing_screen.co_owner_list.description");
    private static final Text TITLE_TRUSTED_LABEL_TEXT = Text.translatable("gui.housing_screen.title.trusted");
    private static final Text TITLE_TRUSTED_LIST_LABEL_TEXT = Text.translatable("gui.housing_screen.trusted_list.title");
    private static final Text TITLE_TRUSTED_LIST_DESCRIPTION_LABEL_TEXT = Text.translatable("gui.housing_screen.trusted_list.description");
    private static final Text TITLE_GUEST_LABEL_TEXT = Text.translatable("gui.housing_screen.title.guest");
    private static final Text TITLE_GUEST_LIST_LABEL_TEXT = Text.translatable("gui.housing_screen.guest_list.title");
    private static final Text TITLE_GUEST_LIST_DESCRIPTION_LABEL_TEXT = Text.translatable("gui.housing_screen.guest_list.description");
    private static final Text TITLE_STRANGER_LABEL_TEXT = Text.translatable("gui.housing_screen.title.stranger");
    private static final Text LEAVE_CURRENT_HOUSE_BUTTON_LABEL_TEXT = Text.translatable("gui.housing_screen.leave_current_house_button_label");
    private static final Text OPEN_RESET_HOUSE_SCREEN_BUTTON_LABEL_TEXT = Text.translatable("gui.housing_screen.open_reset_house_screen_button_label");
    private static final Text TOGGLE_ADVENTURE_BUILDING_OFF_BUTTON_LABEL_TEXT = Text.translatable("gui.housing_screen.toggle_adventure_building_off_button_label");
    private static final Text TOGGLE_ADVENTURE_BUILDING_ON_BUTTON_LABEL_TEXT = Text.translatable("gui.housing_screen.toggle_adventure_building_on_button_label");
    private static final Text UNCLAIM_HOUSE_BUTTON_LABEL_TEXT = Text.translatable("gui.housing_screen.unclaim_house_button_label");
    private static final Text CLAIM_HOUSE_BUTTON_LABEL_TEXT = Text.translatable("gui.housing_screen.claim_house_button_label");
    private static final Text OPEN_CO_OWNER_LIST_BUTTON_LABEL_TEXT = Text.translatable("gui.housing_screen.open_co_owner_list_button_label");
    private static final Text NEW_CO_OWNER_FIELD_PLACEHOLDER_TEXT = Text.translatable("gui.housing_screen.new_co_owner_field.place_holder");
    private static final Text ADD_NEW_CO_OWNER_BUTTON_LABEL_TEXT = Text.translatable("gui.housing_screen.add_new_co_owner_button_label");
    private static final Text OPEN_TRUSTED_PERSONS_LIST_BUTTON_LABEL_TEXT = Text.translatable("gui.housing_screen.open_trusted_list_button_label");
    private static final Text NEW_TRUSTED_PERSON_FIELD_PLACEHOLDER_TEXT = Text.translatable("gui.housing_screen.new_trusted_person_field.place_holder");
    private static final Text ADD_NEW_TRUSTED_PERSON_BUTTON_LABEL_TEXT = Text.translatable("gui.housing_screen.add_new_trusted_person_button_label");
    private static final Text OPEN_GUEST_LIST_BUTTON_LABEL_TEXT = Text.translatable("gui.housing_screen.open_guest_list_button_label");
    private static final Text NEW_GUEST_FIELD_PLACEHOLDER_TEXT = Text.translatable("gui.housing_screen.new_guest_field.place_holder");
    private static final Text ADD_NEW_GUEST_BUTTON_LABEL_TEXT = Text.translatable("gui.housing_screen.add_new_guest_button_label");
    private static final Text REMOVE_LIST_ENTRY_BUTTON_LABEL_TEXT = Text.translatable("gui.housing_screen.remove_list_entry_button_label");

    // creative
    private static final Text HIDE_INFLUENCE_AREA_LABEL_TEXT = Text.translatable("gui.housing_screen.hide_influence_area_label");
    private static final Text SHOW_INFLUENCE_AREA_LABEL_TEXT = Text.translatable("gui.housing_screen.show_influence_area_label");
    private static final Text INFLUENCE_AREA_DIMENSIONS_LABEL_TEXT = Text.translatable("gui.housing_screen.influence_area_dimensions_label");
    private static final Text INFLUENCE_AREA_POSITION_OFFET_LABEL_TEXT = Text.translatable("gui.housing_screen.influence_area_position_offset_label");
    private static final Text RESET_OWNER_BUTTON_LABEL_TEXT = Text.translatable("gui.housing_block.reset_owner_button_label");
    private static final Text TRIGGERED_BLOCK_POSITION_OFFSET_LABEL_TEXT = Text.translatable("gui.triggered_block.triggeredBlockPositionOffset");
    public static final Identifier BACKGROUND_218_215_TEXTURE = BetterAdventureModeCore.identifier("textures/gui/container/generic_218_215_background.png");
    public static final Identifier BACKGROUND_218_95_TEXTURE = BetterAdventureModeCore.identifier("textures/gui/container/generic_218_95_background.png");
    public static final Identifier BACKGROUND_218_71_TEXTURE = BetterAdventureModeCore.identifier("textures/gui/container/generic_218_71_background.png");
    private static final Identifier PLAYER_LISTS_SCROLLER_BACKGROUND_TEXTURE = BetterAdventureModeCore.identifier("container/housing_screen/player_lists_scroller_background");
    private static final Identifier SCROLLER_TEXTURE = BetterAdventureModeCore.identifier("container/scroller");
    @Nullable
    private final HousingBlockBlockEntity housingBlockBlockEntity;

    //region adventure widgets
    private ButtonWidget leaveCurrentHouseButton;

    private ButtonWidget openResetHouseScreenButton;
    private ButtonWidget resetHouseButton;
    private ButtonWidget closeResetHouseScreenButton;

    private ButtonWidget toggleAdventureBuildingEffectButton;
    private ButtonWidget unclaimHouseButton;
    private ButtonWidget claimHouseButton;
    
    private ButtonWidget openCoOwnerListScreenButton;
    private TextFieldWidget newCoOwnerField;
    private ButtonWidget addNewCoOwnerButton;
    private ButtonWidget removeCoOwnerListEntryButton0;
    private ButtonWidget removeCoOwnerListEntryButton1;
    private ButtonWidget removeCoOwnerListEntryButton2;
    private ButtonWidget removeCoOwnerListEntryButton3;
    private ButtonWidget removeCoOwnerListEntryButton4;

    private ButtonWidget openTrustedPersonsListScreenButton;
    private TextFieldWidget newTrustedPersonField;
    private ButtonWidget addNewTrustedPersonButton;
    private ButtonWidget removeTrustedPersonListEntryButton0;
    private ButtonWidget removeTrustedPersonListEntryButton1;
    private ButtonWidget removeTrustedPersonListEntryButton2;
    private ButtonWidget removeTrustedPersonListEntryButton3;
    private ButtonWidget removeTrustedPersonListEntryButton4;

    private ButtonWidget openGuestListScreenButton;
    private TextFieldWidget newGuestField;
    private ButtonWidget addNewGuestButton;
    private ButtonWidget removeGuestListEntryButton0;
    private ButtonWidget removeGuestListEntryButton1;
    private ButtonWidget removeGuestListEntryButton2;
    private ButtonWidget removeGuestListEntryButton3;
    private ButtonWidget removeGuestListEntryButton4;

    private ButtonWidget closeListEditScreensButton;

    private ButtonWidget closeAdventureScreenButton;
    //endregion adventure widgets
    //region creative widgets
    private CyclingButtonWidget<HousingScreen.CreativeScreenPage> creativeScreenPageButton;
    private CyclingButtonWidget<Boolean> showRestrictBlockBreakingAreaButton;
    private TextFieldWidget restrictBlockBreakingAreaDimensionsXField;
    private TextFieldWidget restrictBlockBreakingAreaDimensionsYField;
    private TextFieldWidget restrictBlockBreakingAreaDimensionsZField;
    private TextFieldWidget restrictBlockBreakingAreaPositionOffsetXField;
    private TextFieldWidget restrictBlockBreakingAreaPositionOffsetYField;
    private TextFieldWidget restrictBlockBreakingAreaPositionOffsetZField;
    private TextFieldWidget triggeredBlockPositionOffsetXField;
    private TextFieldWidget triggeredBlockPositionOffsetYField;
    private TextFieldWidget triggeredBlockPositionOffsetZField;
    private CyclingButtonWidget<HousingBlockBlockEntity.OwnerMode> toggleOwnerModeButton;
    private ButtonWidget resetOwnerButton;
    private ButtonWidget saveCreativeButton;
    private ButtonWidget cancelCreativeButton;
    //endregion creative widgets
    private final int currentPermissionLevel;
    private final boolean showCreativeTab;
    private CreativeScreenPage creativeScreenPage;
    private List<String> coOwnerList = new ArrayList<>(List.of());
    private List<String> trustedPersonsList = new ArrayList<>(List.of());
    private List<String> guestList = new ArrayList<>(List.of());
    private boolean showInfluenceArea = false;
    private boolean showResetHouseScreen = false;
    private boolean showCoOwnerListScreen = false;
    private boolean showTrustedListScreen = false;
    private boolean showGuestListScreen = false;
    private int backgroundWidth;
    private int backgroundHeight;
    private int x;
    private int y;
    private int scrollPosition = 0;
    private float scrollAmount = 0.0f;
    private boolean mouseClicked = false;
    private HousingBlockBlockEntity.OwnerMode ownerMode = HousingBlockBlockEntity.OwnerMode.DIMENSION_OWNER;

    public HousingScreen(@Nullable HousingBlockBlockEntity housingBlockBlockEntity, int currentPermissionLevel, boolean showCreativeTab) {
        super(NarratorManager.EMPTY);
        this.housingBlockBlockEntity = housingBlockBlockEntity;
        this.currentPermissionLevel = currentPermissionLevel;
        this.showCreativeTab = showCreativeTab;
        this.creativeScreenPage = CreativeScreenPage.INFLUENCE;
    }

    private void openResetHouseScreen() {
        this.showResetHouseScreen = true;
        this.updateWidgets();
    }

    private void closeResetHouseScreen() {
        this.showResetHouseScreen = false;
        this.updateWidgets();
    }

    private void openListScreen(int listType) {
        if (listType == 0) {
            this.showCoOwnerListScreen = true;
        } else if (listType == 1) {
            this.showTrustedListScreen = true;
        } else if (listType == 2) {
            this.showGuestListScreen = true;
        }
        this.scrollPosition = 0;
        this.scrollAmount = 0.0f;
        this.updateWidgets();
    }

    private void closeListScreens() {
        this.showCoOwnerListScreen = false;
        this.showTrustedListScreen = false;
        this.showGuestListScreen = false;
        this.scrollPosition = 0;
        this.scrollAmount = 0.0f;
        this.updateHousingBlockAdventure();
        this.updateWidgets();
    }

    private void addNewEntryToList(String newEntry, int listType) {
        if (listType == 0) {
            this.coOwnerList.add(newEntry);
        } else if (listType == 1) {
            this.trustedPersonsList.add(newEntry);
        } else if (listType == 2) {
            this.guestList.add(newEntry);
        }
        this.scrollPosition = 0;
        this.scrollAmount = 0.0f;
        this.updateWidgets();
    }

    private void removeEntryFromList(int index, int listType) {
        if (listType == 0 && index + this.scrollPosition < this.coOwnerList.size()) {
            this.coOwnerList.remove(index + this.scrollPosition);
        } else if (listType == 1 && index + this.scrollPosition < this.trustedPersonsList.size()) {
            this.trustedPersonsList.remove(index + this.scrollPosition);
        } else if (listType == 2 && index + this.scrollPosition < this.guestList.size()) {
            this.guestList.remove(index + this.scrollPosition);
        }
        this.scrollPosition = 0;
        this.scrollAmount = 0.0f;
        this.updateWidgets();
    }

    private void saveCreative() {
        if (this.updateHousingBlockCreative()) {
            this.close();
        }
    }

    private void cancel() {
        this.close();
    }

    private void leaveCurrentHouse() {
        ClientPlayNetworking.send(new LeaveHouseFromHousingScreenPacket());
        this.close();
    }

    @Override
    protected void init() {
        this.coOwnerList.clear();
        this.trustedPersonsList.clear();
        this.guestList.clear();
        if (this.housingBlockBlockEntity != null) {
            this.coOwnerList.addAll(this.housingBlockBlockEntity.getCoOwnerList());
            this.trustedPersonsList.addAll(this.housingBlockBlockEntity.getTrustedList());
            this.guestList.addAll(this.housingBlockBlockEntity.getGuestList());
            this.showInfluenceArea = housingBlockBlockEntity.getShowInfluenceArea();
            this.ownerMode = housingBlockBlockEntity.getOwnerMode();
        }
        if (this.currentPermissionLevel == 0) {
            this.backgroundWidth = 218;
            this.backgroundHeight = 215;
            this.x = (this.width - this.backgroundWidth) / 2;
            this.y = (this.height - this.backgroundHeight) / 2;
        } else if (this.currentPermissionLevel == 1) {
            this.backgroundWidth = 218;
            this.backgroundHeight = 95;
            this.x = (this.width - this.backgroundWidth) / 2;
            this.y = (this.height - this.backgroundHeight) / 2;
        } else if (this.currentPermissionLevel == 2 || this.currentPermissionLevel == 3) {
            this.backgroundWidth = 218;
            this.backgroundHeight = 71;
            this.x = (this.width - this.backgroundWidth) / 2;
            this.y = (this.height - this.backgroundHeight) / 2;
        } else {
            this.backgroundWidth = 218;
            this.backgroundHeight = this.ownerMode == HousingBlockBlockEntity.OwnerMode.INTERACTION ? 95 : 71;
            this.x = (this.width - this.backgroundWidth) / 2;
            this.y = (this.height - this.backgroundHeight) / 2;
        }
        super.init();
        //region adventure screen

        this.resetHouseButton = this.addDrawableChild(ButtonWidget.builder(ScreenTexts.PROCEED, button -> this.resetHouse()).dimensions(this.x + 7, this.y + this.backgroundHeight - 27, this.backgroundWidth / 2 - 18, 20).build());
        this.closeResetHouseScreenButton = this.addDrawableChild(ButtonWidget.builder(ScreenTexts.CANCEL, button -> this.closeResetHouseScreen()).dimensions(this.x + this.backgroundWidth / 2 - 18 + 7, this.y + this.backgroundHeight - 27, this.backgroundWidth / 2 - 18, 20).build());

        this.removeCoOwnerListEntryButton0 = this.addDrawableChild(ButtonWidget.builder(REMOVE_LIST_ENTRY_BUTTON_LABEL_TEXT, button -> this.removeEntryFromList(0, 0)).dimensions(this.x + this.backgroundWidth - 57, this.y + 33, 50, 20).build());
        this.removeCoOwnerListEntryButton1 = this.addDrawableChild(ButtonWidget.builder(REMOVE_LIST_ENTRY_BUTTON_LABEL_TEXT, button -> this.removeEntryFromList(1, 0)).dimensions(this.x + this.backgroundWidth - 57, this.y + 57, 50, 20).build());
        this.removeCoOwnerListEntryButton2 = this.addDrawableChild(ButtonWidget.builder(REMOVE_LIST_ENTRY_BUTTON_LABEL_TEXT, button -> this.removeEntryFromList(2, 0)).dimensions(this.x + this.backgroundWidth - 57, this.y + 81, 50, 20).build());
        this.removeCoOwnerListEntryButton3 = this.addDrawableChild(ButtonWidget.builder(REMOVE_LIST_ENTRY_BUTTON_LABEL_TEXT, button -> this.removeEntryFromList(3, 0)).dimensions(this.x + this.backgroundWidth - 57, this.y + 105, 50, 20).build());
        this.removeCoOwnerListEntryButton4 = this.addDrawableChild(ButtonWidget.builder(REMOVE_LIST_ENTRY_BUTTON_LABEL_TEXT, button -> this.removeEntryFromList(4, 0)).dimensions(this.x + this.backgroundWidth - 57, this.y + 129, 50, 20).build());
        this.newCoOwnerField = new TextFieldWidget(this.textRenderer, this.x + 7, this.y + this.backgroundHeight - 27 - 24, this.backgroundWidth - 57 - 7 - 4, 20, Text.empty());
        this.newCoOwnerField.setMaxLength(128);
        this.newCoOwnerField.setPlaceholder(NEW_CO_OWNER_FIELD_PLACEHOLDER_TEXT);
        this.addSelectableChild(this.newCoOwnerField);
        this.addNewCoOwnerButton = this.addDrawableChild(ButtonWidget.builder(ADD_NEW_CO_OWNER_BUTTON_LABEL_TEXT, button -> this.addNewEntryToList(this.newCoOwnerField.getText(), 0)).dimensions(this.x + this.backgroundWidth - 57, this.y + this.backgroundHeight - 27 - 24, 50, 20).build());

        this.removeTrustedPersonListEntryButton0 = this.addDrawableChild(ButtonWidget.builder(REMOVE_LIST_ENTRY_BUTTON_LABEL_TEXT, button -> this.removeEntryFromList(0, 1)).dimensions(this.x + this.backgroundWidth - 57, this.y + 33, 50, 20).build());
        this.removeTrustedPersonListEntryButton1 = this.addDrawableChild(ButtonWidget.builder(REMOVE_LIST_ENTRY_BUTTON_LABEL_TEXT, button -> this.removeEntryFromList(1, 1)).dimensions(this.x + this.backgroundWidth - 57, this.y + 57, 50, 20).build());
        this.removeTrustedPersonListEntryButton2 = this.addDrawableChild(ButtonWidget.builder(REMOVE_LIST_ENTRY_BUTTON_LABEL_TEXT, button -> this.removeEntryFromList(2, 1)).dimensions(this.x + this.backgroundWidth - 57, this.y + 81, 50, 20).build());
        this.removeTrustedPersonListEntryButton3 = this.addDrawableChild(ButtonWidget.builder(REMOVE_LIST_ENTRY_BUTTON_LABEL_TEXT, button -> this.removeEntryFromList(3, 1)).dimensions(this.x + this.backgroundWidth - 57, this.y + 105, 50, 20).build());
        this.removeTrustedPersonListEntryButton4 = this.addDrawableChild(ButtonWidget.builder(REMOVE_LIST_ENTRY_BUTTON_LABEL_TEXT, button -> this.removeEntryFromList(4, 1)).dimensions(this.x + this.backgroundWidth - 57, this.y + 129, 50, 20).build());
        this.newTrustedPersonField = new TextFieldWidget(this.textRenderer, this.x + 7, this.y + this.backgroundHeight - 27 - 24, this.backgroundWidth - 57 - 7 - 4, 20, Text.empty());
        this.newTrustedPersonField.setMaxLength(128);
        this.newTrustedPersonField.setPlaceholder(NEW_TRUSTED_PERSON_FIELD_PLACEHOLDER_TEXT);
        this.addSelectableChild(this.newTrustedPersonField);
        this.addNewTrustedPersonButton = this.addDrawableChild(ButtonWidget.builder(ADD_NEW_TRUSTED_PERSON_BUTTON_LABEL_TEXT, button -> this.addNewEntryToList(this.newTrustedPersonField.getText(), 1)).dimensions(this.x + this.backgroundWidth - 57, this.y + this.backgroundHeight - 27 - 24, 50, 20).build());

        this.removeGuestListEntryButton0 = this.addDrawableChild(ButtonWidget.builder(REMOVE_LIST_ENTRY_BUTTON_LABEL_TEXT, button -> this.removeEntryFromList(0, 2)).dimensions(this.x + this.backgroundWidth - 57, this.y + 33, 50, 20).build());
        this.removeGuestListEntryButton1 = this.addDrawableChild(ButtonWidget.builder(REMOVE_LIST_ENTRY_BUTTON_LABEL_TEXT, button -> this.removeEntryFromList(1, 2)).dimensions(this.x + this.backgroundWidth - 57, this.y + 57, 50, 20).build());
        this.removeGuestListEntryButton2 = this.addDrawableChild(ButtonWidget.builder(REMOVE_LIST_ENTRY_BUTTON_LABEL_TEXT, button -> this.removeEntryFromList(2, 2)).dimensions(this.x + this.backgroundWidth - 57, this.y + 81, 50, 20).build());
        this.removeGuestListEntryButton3 = this.addDrawableChild(ButtonWidget.builder(REMOVE_LIST_ENTRY_BUTTON_LABEL_TEXT, button -> this.removeEntryFromList(3, 2)).dimensions(this.x + this.backgroundWidth - 57, this.y + 105, 50, 20).build());
        this.removeGuestListEntryButton4 = this.addDrawableChild(ButtonWidget.builder(REMOVE_LIST_ENTRY_BUTTON_LABEL_TEXT, button -> this.removeEntryFromList(4, 2)).dimensions(this.x + this.backgroundWidth - 57, this.y + 129, 50, 20).build());
        this.newGuestField = new TextFieldWidget(this.textRenderer, this.x + 7, this.y + this.backgroundHeight - 27 - 24, this.backgroundWidth - 57 - 7 - 4, 20, Text.empty());
        this.newGuestField.setMaxLength(128);
        this.newGuestField.setPlaceholder(NEW_GUEST_FIELD_PLACEHOLDER_TEXT);
        this.addSelectableChild(this.newGuestField);
        this.addNewGuestButton = this.addDrawableChild(ButtonWidget.builder(ADD_NEW_GUEST_BUTTON_LABEL_TEXT, button -> this.addNewEntryToList(this.newGuestField.getText(), 2)).dimensions(this.x + this.backgroundWidth - 57, this.y + this.backgroundHeight - 27 - 24, 50, 20).build());

        this.closeListEditScreensButton = this.addDrawableChild(ButtonWidget.builder(ScreenTexts.CANCEL, button -> this.closeListScreens()).dimensions(this.x + 7, this.y + this.backgroundHeight - 27, this.backgroundWidth - 14, 20).build());

        boolean isAdventureBuilding = false;
        if (this.client != null && this.client.player != null) {
            isAdventureBuilding = this.client.player.hasStatusEffect(StatusEffectsRegistry.ADVENTURE_BUILDING_EFFECT);
        }
        this.toggleAdventureBuildingEffectButton = this.addDrawableChild(ButtonWidget.builder(isAdventureBuilding ? TOGGLE_ADVENTURE_BUILDING_OFF_BUTTON_LABEL_TEXT : TOGGLE_ADVENTURE_BUILDING_ON_BUTTON_LABEL_TEXT, button -> this.toggleAdventureBuildingEffect()).dimensions(this.x + 7, this.y + 20, this.backgroundWidth - 14, 20).build());

        this.openCoOwnerListScreenButton = this.addDrawableChild(ButtonWidget.builder(OPEN_CO_OWNER_LIST_BUTTON_LABEL_TEXT, button -> this.openListScreen(0)).dimensions(this.x + 7, this.y + 44, this.backgroundWidth - 14, 20).build());
        this.openTrustedPersonsListScreenButton = this.addDrawableChild(ButtonWidget.builder(OPEN_TRUSTED_PERSONS_LIST_BUTTON_LABEL_TEXT, button -> this.openListScreen(1)).dimensions(this.x + 7, this.y + 68, this.backgroundWidth - 14, 20).build());
        this.openGuestListScreenButton = this.addDrawableChild(ButtonWidget.builder(OPEN_GUEST_LIST_BUTTON_LABEL_TEXT, button -> this.openListScreen(2)).dimensions(this.x + 7, this.y + 92, this.backgroundWidth - 14, 20).build());

        this.openResetHouseScreenButton = this.addDrawableChild(ButtonWidget.builder(OPEN_RESET_HOUSE_SCREEN_BUTTON_LABEL_TEXT, button -> this.openResetHouseScreen()).dimensions(this.x + 7, this.y + 116, this.backgroundWidth - 14, 20).build());

        this.unclaimHouseButton = this.addDrawableChild(ButtonWidget.builder(UNCLAIM_HOUSE_BUTTON_LABEL_TEXT, button -> this.trySetHouseOwner(false)).dimensions(this.x + 7, this.y + this.backgroundHeight - 27 - 48, this.backgroundWidth - 14, 20).build());
        this.claimHouseButton = this.addDrawableChild(ButtonWidget.builder(CLAIM_HOUSE_BUTTON_LABEL_TEXT, button -> this.trySetHouseOwner(true)).dimensions(this.x + 7, this.y + this.backgroundHeight - 27 - 48, this.backgroundWidth - 14, 20).build());

        this.leaveCurrentHouseButton = this.addDrawableChild(ButtonWidget.builder(LEAVE_CURRENT_HOUSE_BUTTON_LABEL_TEXT, button -> this.leaveCurrentHouse()).dimensions(this.x + 7, this.y + this.backgroundHeight - 27 - 24, this.backgroundWidth - 14, 20).build());

        this.closeAdventureScreenButton = this.addDrawableChild(ButtonWidget.builder(ScreenTexts.CANCEL, button -> this.cancel()).dimensions(this.x + 7, this.y + this.backgroundHeight - 27, this.backgroundWidth - 14, 20).build());

        //endregion adventure screen

        //region creative screen
        this.creativeScreenPageButton = this.addDrawableChild(CyclingButtonWidget.builder(HousingScreen.CreativeScreenPage::asText).values((HousingScreen.CreativeScreenPage[]) HousingScreen.CreativeScreenPage.values()).initially(this.creativeScreenPage).omitKeyText().build(this.width / 2 - 154, 20, 300, 20, Text.empty(), (button, creativeScreenPage) -> {
            this.creativeScreenPage = creativeScreenPage;
            this.updateWidgets();
        }));

        // --- influence area page ---

        this.showRestrictBlockBreakingAreaButton = this.addDrawableChild(CyclingButtonWidget.onOffBuilder(HIDE_INFLUENCE_AREA_LABEL_TEXT, SHOW_INFLUENCE_AREA_LABEL_TEXT).initially(this.showInfluenceArea).omitKeyText().build(this.width / 2 - 153, 45, 300, 20, Text.empty(), (button, showInfluenceArea) -> {
            this.showInfluenceArea = showInfluenceArea;
        }));

        this.restrictBlockBreakingAreaDimensionsXField = new TextFieldWidget(this.textRenderer, this.width / 2 - 154, 80, 100, 20, Text.empty());
        this.restrictBlockBreakingAreaDimensionsXField.setMaxLength(128);
        this.restrictBlockBreakingAreaDimensionsXField.setText(Integer.toString(this.housingBlockBlockEntity != null ? this.housingBlockBlockEntity.getInfluenceAreaDimensions().getX() : 0));
        this.addSelectableChild(this.restrictBlockBreakingAreaDimensionsXField);

        this.restrictBlockBreakingAreaDimensionsYField = new TextFieldWidget(this.textRenderer, this.width / 2 - 50, 80, 100, 20, Text.empty());
        this.restrictBlockBreakingAreaDimensionsYField.setMaxLength(128);
        this.restrictBlockBreakingAreaDimensionsYField.setText(Integer.toString(this.housingBlockBlockEntity != null ? this.housingBlockBlockEntity.getInfluenceAreaDimensions().getY() : 0));
        this.addSelectableChild(this.restrictBlockBreakingAreaDimensionsYField);

        this.restrictBlockBreakingAreaDimensionsZField = new TextFieldWidget(this.textRenderer, this.width / 2 + 54, 80, 100, 20, Text.empty());
        this.restrictBlockBreakingAreaDimensionsZField.setMaxLength(128);
        this.restrictBlockBreakingAreaDimensionsZField.setText(Integer.toString(this.housingBlockBlockEntity != null ? this.housingBlockBlockEntity.getInfluenceAreaDimensions().getZ() : 0));
        this.addSelectableChild(this.restrictBlockBreakingAreaDimensionsZField);

        this.restrictBlockBreakingAreaPositionOffsetXField = new TextFieldWidget(this.textRenderer, this.width / 2 - 154, 115, 100, 20, Text.empty());
        this.restrictBlockBreakingAreaPositionOffsetXField.setMaxLength(128);
        this.restrictBlockBreakingAreaPositionOffsetXField.setText(Integer.toString(this.housingBlockBlockEntity != null ? this.housingBlockBlockEntity.getRestrictBlockBreakingAreaPositionOffset().getX() : 0));
        this.addSelectableChild(this.restrictBlockBreakingAreaPositionOffsetXField);

        this.restrictBlockBreakingAreaPositionOffsetYField = new TextFieldWidget(this.textRenderer, this.width / 2 - 50, 115, 100, 20, Text.empty());
        this.restrictBlockBreakingAreaPositionOffsetYField.setMaxLength(128);
        this.restrictBlockBreakingAreaPositionOffsetYField.setText(Integer.toString(this.housingBlockBlockEntity != null ? this.housingBlockBlockEntity.getRestrictBlockBreakingAreaPositionOffset().getY() : 0));
        this.addSelectableChild(this.restrictBlockBreakingAreaPositionOffsetYField);

        this.restrictBlockBreakingAreaPositionOffsetZField = new TextFieldWidget(this.textRenderer, this.width / 2 + 54, 115, 100, 20, Text.empty());
        this.restrictBlockBreakingAreaPositionOffsetZField.setMaxLength(128);
        this.restrictBlockBreakingAreaPositionOffsetZField.setText(Integer.toString(this.housingBlockBlockEntity != null ? this.housingBlockBlockEntity.getRestrictBlockBreakingAreaPositionOffset().getZ() : 0));
        this.addSelectableChild(this.restrictBlockBreakingAreaPositionOffsetZField);

        // --- triggered block page ---

        this.triggeredBlockPositionOffsetXField = new TextFieldWidget(this.textRenderer, this.width / 2 - 154, 80, 100, 20, Text.empty());
        this.triggeredBlockPositionOffsetXField.setMaxLength(128);
        this.triggeredBlockPositionOffsetXField.setText(Integer.toString(this.housingBlockBlockEntity != null ? this.housingBlockBlockEntity.getTriggeredBlockPositionOffset().getX() : 0));
        this.addSelectableChild(this.triggeredBlockPositionOffsetXField);

        this.triggeredBlockPositionOffsetYField = new TextFieldWidget(this.textRenderer, this.width / 2 - 50, 80, 100, 20, Text.empty());
        this.triggeredBlockPositionOffsetYField.setMaxLength(128);
        this.triggeredBlockPositionOffsetYField.setText(Integer.toString(this.housingBlockBlockEntity != null ? this.housingBlockBlockEntity.getTriggeredBlockPositionOffset().getY() : 0));
        this.addSelectableChild(this.triggeredBlockPositionOffsetYField);

        this.triggeredBlockPositionOffsetZField = new TextFieldWidget(this.textRenderer, this.width / 2 + 54, 80, 100, 20, Text.empty());
        this.triggeredBlockPositionOffsetZField.setMaxLength(128);
        this.triggeredBlockPositionOffsetZField.setText(Integer.toString(this.housingBlockBlockEntity != null ? this.housingBlockBlockEntity.getTriggeredBlockPositionOffset().getZ() : 0));
        this.addSelectableChild(this.triggeredBlockPositionOffsetZField);

        // --- owner page ---

        this.toggleOwnerModeButton = this.addDrawableChild(CyclingButtonWidget.builder(HousingBlockBlockEntity.OwnerMode::asText).values((HousingBlockBlockEntity.OwnerMode[])HousingBlockBlockEntity.OwnerMode.values()).initially(this.ownerMode).omitKeyText().build(this.width / 2 - 153, 70, 300, 20, Text.empty(), (button, ownerMode) -> {
            this.ownerMode = ownerMode;
        }));

        this.resetOwnerButton = this.addDrawableChild(ButtonWidget.builder(RESET_OWNER_BUTTON_LABEL_TEXT, button -> this.trySetHouseOwner(false)).dimensions(this.width / 2 - 4 - 150, 94, 300, 20).build());

        this.saveCreativeButton = this.addDrawableChild(ButtonWidget.builder(ScreenTexts.DONE, button -> this.saveCreative()).dimensions(this.width / 2 - 4 - 150, 210, 150, 20).build());
        this.cancelCreativeButton = this.addDrawableChild(ButtonWidget.builder(ScreenTexts.CANCEL, button -> this.cancel()).dimensions(this.width / 2 + 4, 210, 150, 20).build());

        //endregion creative screen
        this.updateWidgets();
    }

    private void updateWidgets() {

        //region adventure widgets
        this.leaveCurrentHouseButton.visible = false;

        this.openResetHouseScreenButton.visible = false;
        this.resetHouseButton.visible = false;
        this.closeResetHouseScreenButton.visible = false;

        this.toggleAdventureBuildingEffectButton.visible = false;
        this.unclaimHouseButton.visible = false;
        this.claimHouseButton.visible = false;

        this.openCoOwnerListScreenButton.visible = false;
        this.newCoOwnerField.setVisible(false);
        this.addNewCoOwnerButton.visible = false;
        this.removeCoOwnerListEntryButton0.visible = false;
        this.removeCoOwnerListEntryButton1.visible = false;
        this.removeCoOwnerListEntryButton2.visible = false;
        this.removeCoOwnerListEntryButton3.visible = false;
        this.removeCoOwnerListEntryButton4.visible = false;

        this.openTrustedPersonsListScreenButton.visible = false;
        this.newTrustedPersonField.setVisible(false);
        this.addNewTrustedPersonButton.visible = false;
        this.removeTrustedPersonListEntryButton0.visible = false;
        this.removeTrustedPersonListEntryButton1.visible = false;
        this.removeTrustedPersonListEntryButton2.visible = false;
        this.removeTrustedPersonListEntryButton3.visible = false;
        this.removeTrustedPersonListEntryButton4.visible = false;

        this.openGuestListScreenButton.visible = false;
        this.newGuestField.setVisible(false);
        this.addNewGuestButton.visible = false;
        this.removeGuestListEntryButton0.visible = false;
        this.removeGuestListEntryButton1.visible = false;
        this.removeGuestListEntryButton2.visible = false;
        this.removeGuestListEntryButton3.visible = false;
        this.removeGuestListEntryButton4.visible = false;

        this.closeListEditScreensButton.visible = false;

        this.closeAdventureScreenButton.visible = false;
        //endregion adventure widgets
        
        //region creative widgets
        this.creativeScreenPageButton.visible = false;

        this.showRestrictBlockBreakingAreaButton.visible = false;
        this.restrictBlockBreakingAreaDimensionsXField.setVisible(false);
        this.restrictBlockBreakingAreaDimensionsYField.setVisible(false);
        this.restrictBlockBreakingAreaDimensionsZField.setVisible(false);
        this.restrictBlockBreakingAreaPositionOffsetXField.setVisible(false);
        this.restrictBlockBreakingAreaPositionOffsetYField.setVisible(false);
        this.restrictBlockBreakingAreaPositionOffsetZField.setVisible(false);

        this.triggeredBlockPositionOffsetXField.setVisible(false);
        this.triggeredBlockPositionOffsetYField.setVisible(false);
        this.triggeredBlockPositionOffsetZField.setVisible(false);
        
        this.toggleOwnerModeButton.visible = false;
        this.resetOwnerButton.visible = false;
        
        this.saveCreativeButton.visible = false;
        this.cancelCreativeButton.visible = false;

        if (this.showCreativeTab) {
            this.creativeScreenPageButton.visible = true;

            if (this.creativeScreenPage == CreativeScreenPage.INFLUENCE) {

                this.showRestrictBlockBreakingAreaButton.visible = true;
                this.restrictBlockBreakingAreaDimensionsXField.setVisible(true);
                this.restrictBlockBreakingAreaDimensionsYField.setVisible(true);
                this.restrictBlockBreakingAreaDimensionsZField.setVisible(true);
                this.restrictBlockBreakingAreaPositionOffsetXField.setVisible(true);
                this.restrictBlockBreakingAreaPositionOffsetYField.setVisible(true);
                this.restrictBlockBreakingAreaPositionOffsetZField.setVisible(true);

            } else if (this.creativeScreenPage == CreativeScreenPage.TRIGGERED_BLOCK) {

                this.triggeredBlockPositionOffsetXField.setVisible(true);
                this.triggeredBlockPositionOffsetYField.setVisible(true);
                this.triggeredBlockPositionOffsetZField.setVisible(true);

            } else if (this.creativeScreenPage == CreativeScreenPage.OWNER) {

                this.toggleOwnerModeButton.visible = true;
                this.resetOwnerButton.visible = true;

            }

            this.saveCreativeButton.visible = true;
            this.cancelCreativeButton.visible = true;

        } else {
            if (this.showCoOwnerListScreen) {

                this.newCoOwnerField.setVisible(true);
                this.addNewCoOwnerButton.visible = true;
                
                int index = 0;
                for (int i = 0; i < Math.min(5, this.coOwnerList.size()); i++) {
                    if (index == 0) {
                        this.removeCoOwnerListEntryButton0.visible = true;
                    } else if (index == 1) {
                        this.removeCoOwnerListEntryButton1.visible = true;
                    } else if (index == 2) {
                        this.removeCoOwnerListEntryButton2.visible = true;
                    } else if (index == 3) {
                        this.removeCoOwnerListEntryButton3.visible = true;
                    } else if (index == 4) {
                        this.removeCoOwnerListEntryButton4.visible = true;
                    }
                    index++;
                }

                this.closeListEditScreensButton.visible = true;

            } else if (this.showTrustedListScreen) {

                this.newTrustedPersonField.setVisible(true);
                this.addNewTrustedPersonButton.visible = true;
                int index = 0;
                for (int i = 0; i < Math.min(5, this.coOwnerList.size()); i++) {
                    if (index == 0) {
                        this.removeTrustedPersonListEntryButton0.visible = true;
                    } else if (index == 1) {
                        this.removeTrustedPersonListEntryButton1.visible = true;
                    } else if (index == 2) {
                        this.removeTrustedPersonListEntryButton2.visible = true;
                    } else if (index == 3) {
                        this.removeTrustedPersonListEntryButton3.visible = true;
                    } else if (index == 4) {
                        this.removeTrustedPersonListEntryButton4.visible = true;
                    }
                    index++;
                }

                this.closeListEditScreensButton.visible = true;

            } else if (this.showGuestListScreen) {

                this.newGuestField.setVisible(true);
                this.addNewGuestButton.visible = true;

                int index = 0;
                for (int i = 0; i < Math.min(5, this.coOwnerList.size()); i++) {
                    if (index == 0) {
                        this.removeGuestListEntryButton0.visible = true;
                    } else if (index == 1) {
                        this.removeGuestListEntryButton1.visible = true;
                    } else if (index == 2) {
                        this.removeGuestListEntryButton2.visible = true;
                    } else if (index == 3) {
                        this.removeGuestListEntryButton3.visible = true;
                    } else if (index == 4) {
                        this.removeGuestListEntryButton4.visible = true;
                    }
                    index++;
                }

                this.closeListEditScreensButton.visible = true;

            } else if (this.showResetHouseScreen) {

                this.resetHouseButton.visible = true;
                this.closeResetHouseScreenButton.visible = true;

            } else {

                if (this.currentPermissionLevel == 0) {

                    this.toggleAdventureBuildingEffectButton.visible = true;

                    this.openCoOwnerListScreenButton.visible = true;
                    this.openTrustedPersonsListScreenButton.visible = true;
                    this.openGuestListScreenButton.visible = true;

                    this.openResetHouseScreenButton.visible = true;

                    if (this.housingBlockBlockEntity != null && this.housingBlockBlockEntity.getOwnerMode() == HousingBlockBlockEntity.OwnerMode.INTERACTION) {
                        this.unclaimHouseButton.visible = true;
                    }

                } else if (this.currentPermissionLevel == 1) {

                    this.toggleAdventureBuildingEffectButton.visible = true;

                } else if (this.currentPermissionLevel == 4) {

                    if (this.housingBlockBlockEntity != null && this.housingBlockBlockEntity.getOwnerMode() == HousingBlockBlockEntity.OwnerMode.INTERACTION && !this.housingBlockBlockEntity.isOwnerSet()) {
                        this.claimHouseButton.visible = true;
                    }

                }

                this.leaveCurrentHouseButton.visible = true;

                this.closeAdventureScreenButton.visible = true;
            }
        }
    }

    @Override
    public void resize(MinecraftClient client, int width, int height) {
        List<String> list = new ArrayList<>(this.coOwnerList);
        List<String> list1 = new ArrayList<>(this.trustedPersonsList);
        List<String> list2 = new ArrayList<>(this.guestList);
        boolean bool = this.showInfluenceArea;
        HousingBlockBlockEntity.OwnerMode var = this.ownerMode;
        int number = this.scrollPosition;
        float number1 =  this.scrollAmount;
        String string = this.newCoOwnerField.getText();
        String string1 = this.newTrustedPersonField.getText();
        String string2 = this.newGuestField.getText();
        String string3 = this.restrictBlockBreakingAreaDimensionsXField.getText();
        String string4 = this.restrictBlockBreakingAreaDimensionsYField.getText();
        String string5 = this.restrictBlockBreakingAreaDimensionsZField.getText();
        String string6 = this.restrictBlockBreakingAreaPositionOffsetXField.getText();
        String string7 = this.restrictBlockBreakingAreaPositionOffsetYField.getText();
        String string8 = this.restrictBlockBreakingAreaPositionOffsetZField.getText();
        String string9 = this.triggeredBlockPositionOffsetXField.getText();
        String string10 = this.triggeredBlockPositionOffsetYField.getText();
        String string11 = this.triggeredBlockPositionOffsetZField.getText();
        this.init(client, width, height);
        this.coOwnerList.clear();
        this.trustedPersonsList.clear();
        this.guestList.clear();
        this.coOwnerList.addAll(list);
        this.trustedPersonsList.addAll(list1);
        this.guestList.addAll(list2);
        this.showInfluenceArea = bool;
        this.ownerMode = var;
        this.scrollPosition = number;
        this.scrollAmount = number1;
        this.newCoOwnerField.setText(string);
        this.newTrustedPersonField.setText(string1);
        this.newGuestField.setText(string2);
        this.restrictBlockBreakingAreaDimensionsXField.setText(string3);
        this.restrictBlockBreakingAreaDimensionsYField.setText(string4);
        this.restrictBlockBreakingAreaDimensionsZField.setText(string5);
        this.restrictBlockBreakingAreaPositionOffsetXField.setText(string6);
        this.restrictBlockBreakingAreaPositionOffsetYField.setText(string7);
        this.restrictBlockBreakingAreaPositionOffsetZField.setText(string8);
        this.triggeredBlockPositionOffsetXField.setText(string9);
        this.triggeredBlockPositionOffsetYField.setText(string10);
        this.triggeredBlockPositionOffsetZField.setText(string11);
        this.updateWidgets();
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        this.mouseClicked = false;
        if (!this.showCreativeTab
                && ((this.showCoOwnerListScreen && this.coOwnerList.size() > 5)
                || (this.showTrustedListScreen && this.coOwnerList.size() > 5)
                || (this.showGuestListScreen && this.coOwnerList.size() > 5))) {
            int i = this.x + 8;
            int j = this.y + 34;
            if (mouseX >= (double)i && mouseX < (double)(i + 6) && mouseY >= (double)j && mouseY < (double)(j + 115)) {
                this.mouseClicked = true;
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        if (!this.showCreativeTab
                && this.showCoOwnerListScreen
                && this.coOwnerList.size() > 5
                && this.mouseClicked) {
            int i = this.coOwnerList.size() - 5;
            float f = (float)deltaY / (float)i;
            this.scrollAmount = MathHelper.clamp(this.scrollAmount + f, 0.0f, 1.0f);
            this.scrollPosition = (int)((double)(this.scrollAmount * (float)i));
        }
        if (!this.showCreativeTab
                && this.showTrustedListScreen
                && this.trustedPersonsList.size() > 5
                && this.mouseClicked) {
            int i = this.trustedPersonsList.size() - 5;
            float f = (float)deltaY / (float)i;
            this.scrollAmount = MathHelper.clamp(this.scrollAmount + f, 0.0f, 1.0f);
            this.scrollPosition = (int)((double)(this.scrollAmount * (float)i));
        }
        if (!this.showCreativeTab
                && this.showGuestListScreen
                && this.guestList.size() > 5
                && this.mouseClicked) {
            int i = this.guestList.size() - 5;
            float f = (float)deltaY / (float)i;
            this.scrollAmount = MathHelper.clamp(this.scrollAmount + f, 0.0f, 1.0f);
            this.scrollPosition = (int)((double)(this.scrollAmount * (float)i));
        }
        return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        if (!this.showCreativeTab
                && this.showCoOwnerListScreen
                && this.coOwnerList.size() > 5
                && mouseX >= (double)(this.x + 7) && mouseX <= (double)(this.x + this.backgroundWidth - 61)
                && mouseY >= 34 && mouseY <= 148) {
            int i = this.coOwnerList.size() - 5;
            float f = (float)verticalAmount / (float)i;
            this.scrollAmount = MathHelper.clamp(this.scrollAmount - f, 0.0f, 1.0f);
            this.scrollPosition = (int)((double)(this.scrollAmount * (float)i));
        }
        if (!this.showCreativeTab
                && this.showTrustedListScreen
                && this.trustedPersonsList.size() > 5
                && mouseX >= (double)(this.x + 7) && mouseX <= (double)(this.x + this.backgroundWidth - 61)
                && mouseY >= 34 && mouseY <= 148) {
            int i = this.trustedPersonsList.size() - 5;
            float f = (float)verticalAmount / (float)i;
            this.scrollAmount = MathHelper.clamp(this.scrollAmount - f, 0.0f, 1.0f);
            this.scrollPosition = (int)((double)(this.scrollAmount * (float)i));
        }
        if (!this.showCreativeTab
                && this.showGuestListScreen
                && this.guestList.size() > 5
                && mouseX >= (double)(this.x + 7) && mouseX <= (double)(this.x + this.backgroundWidth - 61)
                && mouseY >= 34 && mouseY <= 148) {
            int i = this.guestList.size() - 5;
            float f = (float)verticalAmount / (float)i;
            this.scrollAmount = MathHelper.clamp(this.scrollAmount - f, 0.0f, 1.0f);
            this.scrollPosition = (int)((double)(this.scrollAmount * (float)i));
        }
        return super.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (this.showCreativeTab && (keyCode == GLFW.GLFW_KEY_ENTER || keyCode == GLFW.GLFW_KEY_KP_ENTER)) {
            this.saveCreative();
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        if (this.showCreativeTab) {
            if (this.creativeScreenPage == CreativeScreenPage.INFLUENCE) {
                context.drawTextWithShadow(this.textRenderer, INFLUENCE_AREA_DIMENSIONS_LABEL_TEXT, this.width / 2 - 153, 70, 0xA0A0A0);
                this.restrictBlockBreakingAreaDimensionsXField.render(context, mouseX, mouseY, delta);
                this.restrictBlockBreakingAreaDimensionsYField.render(context, mouseX, mouseY, delta);
                this.restrictBlockBreakingAreaDimensionsZField.render(context, mouseX, mouseY, delta);
                context.drawTextWithShadow(this.textRenderer, INFLUENCE_AREA_POSITION_OFFET_LABEL_TEXT, this.width / 2 - 153, 105, 0xA0A0A0);
                this.restrictBlockBreakingAreaPositionOffsetXField.render(context, mouseX, mouseY, delta);
                this.restrictBlockBreakingAreaPositionOffsetYField.render(context, mouseX, mouseY, delta);
                this.restrictBlockBreakingAreaPositionOffsetZField.render(context, mouseX, mouseY, delta);
            } else if (this.creativeScreenPage == CreativeScreenPage.TRIGGERED_BLOCK) {
                context.drawTextWithShadow(this.textRenderer, TRIGGERED_BLOCK_POSITION_OFFSET_LABEL_TEXT, this.width / 2 - 153, 70, 0xA0A0A0);
                this.triggeredBlockPositionOffsetXField.render(context, mouseX, mouseY, delta);
                this.triggeredBlockPositionOffsetYField.render(context, mouseX, mouseY, delta);
                this.triggeredBlockPositionOffsetZField.render(context, mouseX, mouseY, delta);
            }
        } else {
            if (this.showResetHouseScreen) {
            } else if (this.showCoOwnerListScreen) {
                context.drawText(this.textRenderer, TITLE_CO_OWNER_LIST_LABEL_TEXT, this.x + 8, this.y + 7, 0x404040, false);
                context.drawText(this.textRenderer, TITLE_CO_OWNER_LIST_DESCRIPTION_LABEL_TEXT, this.x + 8, this.y + 20, 0x404040, false);
                for (int i = this.scrollPosition; i < Math.min(this.scrollPosition + 5, this.coOwnerList.size()); i++) {
                    String text = this.coOwnerList.get(i);
                    context.drawText(this.textRenderer, text, this.x + 19, this.y + 39 + ((i - this.scrollPosition) * 24), 0x404040, false);
                }
                if (this.coOwnerList.size() > 5) {
                    context.drawGuiTexture(PLAYER_LISTS_SCROLLER_BACKGROUND_TEXTURE, this.x + 7, this.y + 33, 8, 116);
                    int k = (int)(107.0f * this.scrollAmount);
                    context.drawGuiTexture(SCROLLER_TEXTURE, this.x + 8, this.y + 33 + 1 + k, 6, 7);
                }
                this.newCoOwnerField.render(context, mouseX, mouseY, delta);
            } else if (this.showTrustedListScreen) {
                context.drawText(this.textRenderer, TITLE_TRUSTED_LIST_LABEL_TEXT, this.x + 8, this.y + 7, 0x404040, false);
                context.drawText(this.textRenderer, TITLE_TRUSTED_LIST_DESCRIPTION_LABEL_TEXT, this.x + 8, this.y + 20, 0x404040, false);
                for (int i = this.scrollPosition; i < Math.min(this.scrollPosition + 5, this.trustedPersonsList.size()); i++) {
                    String text = this.trustedPersonsList.get(i);
                    context.drawText(this.textRenderer, text, this.x + 19, this.y + 39 + ((i - this.scrollPosition) * 24), 0x404040, false);
                }
                if (this.trustedPersonsList.size() > 5) {
                    context.drawGuiTexture(PLAYER_LISTS_SCROLLER_BACKGROUND_TEXTURE, this.x + 7, this.y + 33, 8, 116);
                    int k = (int)(107.0f * this.scrollAmount);
                    context.drawGuiTexture(SCROLLER_TEXTURE, this.x + 8, this.y + 33 + 1 + k, 6, 7);
                }
                this.newTrustedPersonField.render(context, mouseX, mouseY, delta);
            } else if (this.showGuestListScreen) {
                context.drawText(this.textRenderer, TITLE_GUEST_LIST_LABEL_TEXT, this.x + 8, this.y + 7, 0x404040, false);
                context.drawText(this.textRenderer, TITLE_GUEST_LIST_DESCRIPTION_LABEL_TEXT, this.x + 8, this.y + 20, 0x404040, false);
                for (int i = this.scrollPosition; i < Math.min(this.scrollPosition + 5, this.guestList.size()); i++) {
                    String text = this.guestList.get(i);
                    context.drawText(this.textRenderer, text, this.x + 19, this.y + 39 + ((i - this.scrollPosition) * 24), 0x404040, false);
                }
                if (this.guestList.size() > 5) {
                    context.drawGuiTexture(PLAYER_LISTS_SCROLLER_BACKGROUND_TEXTURE, this.x + 7, this.y + 33, 8, 116);
                    int k = (int)(107.0f * this.scrollAmount);
                    context.drawGuiTexture(SCROLLER_TEXTURE, this.x + 8, this.y + 33 + 1 + k, 6, 7);
                }
                this.newGuestField.render(context, mouseX, mouseY, delta);
            } else {
                if (this.currentPermissionLevel == 0) {
                    context.drawText(this.textRenderer, TITLE_OWNER_LABEL_TEXT, this.x + 8, this.y + 7, 0x404040, false);
                } else if (this.currentPermissionLevel == 1) {
                    context.drawText(this.textRenderer, TITLE_CO_OWNER_LABEL_TEXT, this.x + 8, this.y + 7, 0x404040, false);
                } else if (this.currentPermissionLevel == 2) {
                    context.drawText(this.textRenderer, TITLE_TRUSTED_LABEL_TEXT, this.x + 8, this.y + 7, 0x404040, false);
                } else if (this.currentPermissionLevel == 3) {
                    context.drawText(this.textRenderer, TITLE_GUEST_LABEL_TEXT, this.x + 8, this.y + 7, 0x404040, false);
                } else if (this.currentPermissionLevel == 4) {
                    context.drawText(this.textRenderer, TITLE_STRANGER_LABEL_TEXT, this.x + 8, this.y + 7, 0x404040, false);
                }
            }
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
        if (!this.showCreativeTab) {
            int i = this.x;
            int j = this.y;
            if (this.currentPermissionLevel == 0) {
                context.drawTexture(BACKGROUND_218_215_TEXTURE, i, j, 0, 0, this.backgroundWidth, this.backgroundHeight, this.backgroundWidth, this.backgroundHeight);
            } else if (this.currentPermissionLevel == 1 || this.ownerMode == HousingBlockBlockEntity.OwnerMode.INTERACTION) {
                context.drawTexture(BACKGROUND_218_95_TEXTURE, i, j, 0, 0, this.backgroundWidth, this.backgroundHeight, this.backgroundWidth, this.backgroundHeight);
            } else {
                context.drawTexture(BACKGROUND_218_71_TEXTURE, i, j, 0, 0, this.backgroundWidth, this.backgroundHeight, this.backgroundWidth, this.backgroundHeight);
            }
        }
    }

    private boolean updateHousingBlockCreative() {
        BlockPos housingBlockPos = new BlockPos(0, 0, 0);
        if (this.housingBlockBlockEntity != null) {
            housingBlockPos = this.housingBlockBlockEntity.getPos();
        }
        ClientPlayNetworking.send(new UpdateHousingBlockCreativePacket(
                housingBlockPos,
                this.showInfluenceArea,
                new Vec3i(
                        this.parseInt(this.restrictBlockBreakingAreaDimensionsXField.getText()),
                        this.parseInt(this.restrictBlockBreakingAreaDimensionsYField.getText()),
                        this.parseInt(this.restrictBlockBreakingAreaDimensionsZField.getText())
                ),
                new BlockPos(
                        this.parseInt(this.restrictBlockBreakingAreaPositionOffsetXField.getText()),
                        this.parseInt(this.restrictBlockBreakingAreaPositionOffsetYField.getText()),
                        this.parseInt(this.restrictBlockBreakingAreaPositionOffsetZField.getText())
                ),
                new BlockPos(
                        this.parseInt(this.triggeredBlockPositionOffsetXField.getText()),
                        this.parseInt(this.triggeredBlockPositionOffsetYField.getText()),
                        this.parseInt(this.triggeredBlockPositionOffsetZField.getText())
                ),
                this.ownerMode.asString()
        ));
        return true;
    }

    private void updateHousingBlockAdventure() {
        if (this.housingBlockBlockEntity != null) {
            BetterAdventureModeCore.info("this.housingBlockBlockEntity != null");
            ClientPlayNetworking.send(new UpdateHousingBlockAdventurePacket(
                    this.housingBlockBlockEntity.getPos(),
                    this.coOwnerList,
                    this.trustedPersonsList,
                    this.guestList
            ));
        }
        BetterAdventureModeCore.info("this.housingBlockBlockEntity == null");
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

    private void toggleAdventureBuildingEffect() {
        ClientPlayNetworking.send(new AddStatusEffectPacket(
                Registries.STATUS_EFFECT.getId(StatusEffectsRegistry.ADVENTURE_BUILDING_EFFECT),
                -1,
                0,
                false,
                false,
                true,
                true
        ));
        this.close();
    }

    private void resetHouse() {
        if (this.housingBlockBlockEntity != null) {
            ClientPlayNetworking.send(new ResetHouseHousingBlockPacket(
                    this.housingBlockBlockEntity.getPos()
            ));
        }
        this.close();
    }

    private void trySetHouseOwner(boolean claim) {
        if (this.housingBlockBlockEntity != null && this.client != null && this.client.player != null) {
            ClientPlayNetworking.send(new SetHousingBlockOwnerPacket(
                    this.housingBlockBlockEntity.getPos(),
                    claim ? this.client.player.getUuidAsString() : ""
            ));
        }
        this.close();
    }

    public static enum CreativeScreenPage implements StringIdentifiable
    {
        INFLUENCE("influence"),
        TRIGGERED_BLOCK("triggered_block"),
        OWNER("owner");

        private final String name;

        private CreativeScreenPage(String name) {
            this.name = name;
        }

        @Override
        public String asString() {
            return this.name;
        }

        public static Optional<HousingScreen.CreativeScreenPage> byName(String name) {
            return Arrays.stream(HousingScreen.CreativeScreenPage.values()).filter(creativeScreenPage -> creativeScreenPage.asString().equals(name)).findFirst();
        }

        public Text asText() {
            return Text.translatable("gui.housing_screen.creativeScreenPage." + this.name);
        }
    }
}
