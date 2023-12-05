package com.github.theredbrain.bamcore.client.gui.screen.ingame;

import com.github.theredbrain.bamcore.BetterAdventureModeCore;
import com.github.theredbrain.bamcore.block.entity.HousingBlockBlockEntity;
import com.github.theredbrain.bamcore.block.entity.TeleporterBlockBlockEntity;
import com.github.theredbrain.bamcore.client.owo.CustomButtonComponent;
import com.github.theredbrain.bamcore.client.owo.ExtendedSurface;
import com.github.theredbrain.bamcore.network.packet.*;
import com.github.theredbrain.bamcore.registry.ComponentsRegistry;
import com.github.theredbrain.bamcore.registry.StatusEffectsRegistry;
import com.github.theredbrain.bamcore.screen.DuckSlotMixin;
import io.wispforest.owo.ui.component.ButtonComponent;
import io.wispforest.owo.ui.component.Components;
import io.wispforest.owo.ui.component.TextBoxComponent;
import io.wispforest.owo.ui.container.Containers;
import io.wispforest.owo.ui.container.FlowLayout;
import io.wispforest.owo.ui.container.ScrollContainer;
import io.wispforest.owo.ui.core.*;
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
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.Text;
import net.minecraft.util.Colors;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import org.apache.commons.lang3.tuple.MutablePair;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.glfw.GLFW;

import java.util.*;

@Environment(EnvType.CLIENT)
public class HousingScreen extends Screen {
    private static final Text CREATIVE_SCREEN_PAGE_LABEL_TEXT = Text.translatable("gui.housing_screen.creative_screen_page_label");
    private static final Text HIDE_INFLUENCE_AREA_LABEL_TEXT = Text.translatable("gui.housing_screen.hide_influence_area_label");
    private static final Text SHOW_INFLUENCE_AREA_LABEL_TEXT = Text.translatable("gui.housing_screen.show_influence_area_label");
    private static final Text INFLUENCE_AREA_DIMENSIONS_LABEL_TEXT = Text.translatable("gui.housing_screen.influence_area_dimensions_label");
    private static final Text INFLUENCE_AREA_POSITION_OFFET_LABEL_TEXT = Text.translatable("gui.housing_screen.influence_area_position_offset_label");
    private static final Text ENTRANCE_POSITION_OFFET_LABEL_TEXT = Text.translatable("gui.housing_screen.entrance_position_offset_label");
    private static final Text ENTRANCE_ORIENTATION_LABEL_TEXT = Text.translatable("gui.housing_screen.entrance_orientation_label");
    private static final Text OWNER_MODE_LABEL_TEXT = Text.translatable("gui.housing_block.owner_mode_label");
    private static final Text TRIGGERED_BLOCK_POSITION_OFFSET_LABEL_TEXT = Text.translatable("gui.triggered_block.triggeredBlockPositionOffset");
    public static final Identifier INVENTORY_TABS_TEXTURE = BetterAdventureModeCore.identifier("textures/gui/big_tabs.png");
    public static final Identifier INVENTORY_NO_TOP_TEXTURE = BetterAdventureModeCore.identifier("panel/default_no_top");
    public static final Identifier INVENTORY_ONLY_TOP_TEXTURE = BetterAdventureModeCore.identifier("panel/default_only_top");
    @Nullable
    private final HousingBlockBlockEntity housingBlockBlockEntity;

    //region adventure widgets
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
    private TextFieldWidget entrancePositionOffsetXField;
    private TextFieldWidget entrancePositionOffsetYField;
    private TextFieldWidget entrancePositionOffsetZField;
    private TextFieldWidget entranceOrientationYawField;
    private TextFieldWidget entranceOrientationPitchField;
    private TextFieldWidget triggeredBlockPositionOffsetXField;
    private TextFieldWidget triggeredBlockPositionOffsetYField;
    private TextFieldWidget triggeredBlockPositionOffsetZField;
    private CyclingButtonWidget<HousingBlockBlockEntity.OwnerMode> toggleOwnerModeButton;
    private ButtonWidget resetOwnerButton;
    private ButtonWidget saveCreativeButton;
    private ButtonWidget cancelButton;
    //endregion creative widgets
    private final int currentPermissionLevel;
    private final boolean showCreativeTab;
    private CreativeScreenPage creativeScreenPage;
    private List<String> coOwnerList = new ArrayList<>(List.of());
    private List<String> trustedList = new ArrayList<>(List.of());
    private List<String> guestList = new ArrayList<>(List.of());
    private boolean showInfluenceArea = false;
    private int currentTab = 0;
    private HousingBlockBlockEntity.OwnerMode ownerMode = HousingBlockBlockEntity.OwnerMode.DIMENSION_OWNER; // 0: dimension owner, 1: first interaction

    public HousingScreen(@Nullable HousingBlockBlockEntity housingBlockBlockEntity, int currentPermissionLevel, boolean showCreativeTab) {
        super(NarratorManager.EMPTY);
        this.housingBlockBlockEntity = housingBlockBlockEntity;
        this.currentPermissionLevel = currentPermissionLevel;
        this.showCreativeTab = showCreativeTab;
        this.creativeScreenPage = CreativeScreenPage.INFLUENCE;
    }

    private void saveAdventure() {
        if (this.updateHousingBlockAdventure()) {
            this.close();
        }
    }

    private void saveCreative() {
        if (this.updateHousingBlockCreative()) {
            this.close();
        }
    }

    private void cancel() {
        this.close();
    }

//    private void toggleShowRestrictBlockBreakingArea() {
//        if (this.showRestrictBlockBreakingArea) {
//            this.showRestrictBlockBreakingArea = false;
//        } else {
//            this.showRestrictBlockBreakingArea = true;
//        }
//        this.component(ButtonComponent.class, "toggleShowRestrictBlockBreakingAreaButton").setMessage(this.showRestrictBlockBreakingArea ? Text.translatable("gui.housing_block.toggleShowRestrictBlockBreakingAreaButton.on") : Text.translatable("gui.housing_block.toggleShowRestrictBlockBreakingAreaButton.off"));
//        this.component(ButtonComponent.class, "toggleShowRestrictBlockBreakingAreaButton").tooltip(this.showRestrictBlockBreakingArea ? Text.translatable("gui.housing_block.toggleShowRestrictBlockBreakingAreaButton.on.tooltip") : Text.translatable("gui.housing_block.toggleShowRestrictBlockBreakingAreaButton.off.tooltip"));
//    }

//    private void toggleOwnerMode() {
//        if (this.ownerMode == 0) {
//            this.ownerMode = 1;
//        } else {
//            this.ownerMode = 0;
//        }
//        this.component(ButtonComponent.class, "toggleOwnerModeButton").setMessage(this.ownerMode == 0 ? Text.translatable("gui.housing_block.toggleOwnerModeButton_0") : Text.translatable("gui.housing_block.toggleOwnerModeButton_1"));
//        this.component(ButtonComponent.class, "toggleOwnerModeButton").tooltip(this.ownerMode == 0 ? Text.translatable("gui.housing_block.toggleOwnerModeButton_0.tooltip") : Text.translatable("gui.housing_block.toggleOwnerModeButton_1.tooltip"));
//    }

    private void leaveCurrentHouse() { // TODO use a packet?
        if (this.client != null && this.client.player != null && this.client.getServer() != null) {
            PlayerEntity playerEntity = this.client.player;
            Pair<Pair<String, BlockPos>, Boolean> housingAccessPos = ComponentsRegistry.HOUSING_ACCESS_POS.get(playerEntity).getValue();
            if (housingAccessPos.getRight()) {
                BlockPos blockPos = housingAccessPos.getLeft().getRight();
                playerEntity.teleport(this.client.getServer().getWorld(RegistryKey.of(RegistryKeys.WORLD, new Identifier(housingAccessPos.getLeft().getLeft()))), blockPos.getX(), blockPos.getX(), blockPos.getX(), Set.of(), playerEntity.getYaw(), playerEntity.getPitch());
                ComponentsRegistry.HOUSING_ACCESS_POS.get(playerEntity).deactivate();
            }
        }
        this.close();
    }
/*

    @Override
    protected @NotNull OwoUIAdapter<FlowLayout> createAdapter() {
        return OwoUIAdapter.create(this, Containers::verticalFlow);
    }

    @Override
    protected void build(FlowLayout rootComponent) {
        rootComponent
                .surface(Surface.VANILLA_TRANSLUCENT)
                .horizontalAlignment(HorizontalAlignment.CENTER)
                .verticalAlignment(VerticalAlignment.CENTER)
                .id("root");
        if (this.showCreativeTab && this.housingBlockBlockEntity != null) {
            Vec3i restrictBlockBreakingAreaDimensions = this.housingBlockBlockEntity.getInfluenceAreaDimensions();
            BlockPos restrictBlockBreakingAreaPositionOffset = this.housingBlockBlockEntity.getRestrictBlockBreakingAreaPositionOffset();
            MutablePair<BlockPos, MutablePair<Double, Double>> entranceBlockPositionOffset = this.housingBlockBlockEntity.getEntrance();
            BlockPos triggeredBlockPositionOffset = this.housingBlockBlockEntity.getTriggeredBlockPositionOffset();
            rootComponent.children(List.of(
                    Containers.verticalFlow(Sizing.fixed(250), Sizing.fixed(166))
                            .children(List.of(
                                    // toggle debug mode
                                    Components.button(this.showInfluenceArea ? Text.translatable("gui.housing_block.toggleShowRestrictBlockBreakingAreaButton.on")
                                                            : Text.translatable("gui.housing_block.toggleShowRestrictBlockBreakingAreaButton.off")
                                                    , button -> this.toggleShowRestrictBlockBreakingArea())
                                            .sizing(Sizing.fill(100), Sizing.fixed(20))
                                            .tooltip(this.showInfluenceArea ? Text.translatable("gui.housing_block.toggleShowRestrictBlockBreakingAreaButton.on.tooltip")
                                                    : Text.translatable("gui.housing_block.toggleShowRestrictBlockBreakingAreaButton.off.tooltip"))
                                            .id("toggleShowRestrictBlockBreakingAreaButton"),

                                    // activation area
                                    Containers.horizontalFlow(Sizing.fill(100), Sizing.content())
                                            .children(List.of(
                                                    Components.textBox(Sizing.fill(32), Integer.toString(restrictBlockBreakingAreaDimensions.getX()))
                                                            .tooltip(Text.translatable("gui.housing_block.restrictBlockBreakingAreaDimensionsX.tooltip"))
                                                            .margins(Insets.of(1, 1, 1, 1))
                                                            .id("restrictBlockBreakingAreaDimensionsX"),
                                                    Components.textBox(Sizing.fill(32), Integer.toString(restrictBlockBreakingAreaDimensions.getY()))
                                                            .tooltip(Text.translatable("gui.housing_block.restrictBlockBreakingAreaDimensionsY.tooltip"))
                                                            .margins(Insets.of(1, 1, 1, 1))
                                                            .id("restrictBlockBreakingAreaDimensionsY"),
                                                    Components.textBox(Sizing.fill(32), Integer.toString(restrictBlockBreakingAreaDimensions.getZ()))
                                                            .tooltip(Text.translatable("gui.housing_block.restrictBlockBreakingAreaDimensionsZ.tooltip"))
                                                            .margins(Insets.of(1, 1, 1, 1))
                                                            .id("restrictBlockBreakingAreaDimensionsZ")
                                            ))
                                            .verticalAlignment(VerticalAlignment.CENTER)
                                            .horizontalAlignment(HorizontalAlignment.CENTER),
                                    Containers.horizontalFlow(Sizing.fill(100), Sizing.content())
                                            .children(List.of(
                                                    Components.textBox(Sizing.fill(32), Integer.toString(restrictBlockBreakingAreaPositionOffset.getX()))
                                                            .tooltip(Text.translatable("gui.housing_block.restrictBlockBreakingAreaPositionOffsetX.tooltip"))
                                                            .margins(Insets.of(1, 1, 1, 1))
                                                            .id("restrictBlockBreakingAreaPositionOffsetX"),
                                                    Components.textBox(Sizing.fill(32), Integer.toString(restrictBlockBreakingAreaPositionOffset.getY()))
                                                            .tooltip(Text.translatable("gui.housing_block.restrictBlockBreakingAreaPositionOffsetY.tooltip"))
                                                            .margins(Insets.of(1, 1, 1, 1))
                                                            .id("restrictBlockBreakingAreaPositionOffsetY"),
                                                    Components.textBox(Sizing.fill(32), Integer.toString(restrictBlockBreakingAreaPositionOffset.getZ()))
                                                            .tooltip(Text.translatable("gui.housing_block.restrictBlockBreakingAreaPositionOffsetZ.tooltip"))
                                                            .margins(Insets.of(1, 1, 1, 1))
                                                            .id("restrictBlockBreakingAreaPositionOffsetZ")
                                            ))
                                            .verticalAlignment(VerticalAlignment.CENTER)
                                            .horizontalAlignment(HorizontalAlignment.CENTER),

                                    // entrance
                                    Containers.horizontalFlow(Sizing.fill(100), Sizing.content())
                                            .children(List.of(
                                                    Components.textBox(Sizing.fill(32), Integer.toString(entranceBlockPositionOffset.getLeft().getX()))
                                                            .tooltip(Text.translatable("gui.housing_block.entrancePositionOffsetX.tooltip"))
                                                            .margins(Insets.of(1, 1, 1, 1))
                                                            .id("entrancePositionOffsetX"),
                                                    Components.textBox(Sizing.fill(32), Integer.toString(entranceBlockPositionOffset.getLeft().getY()))
                                                            .tooltip(Text.translatable("gui.housing_block.entrancePositionOffsetY.tooltip"))
                                                            .margins(Insets.of(1, 1, 1, 1))
                                                            .id("entrancePositionOffsetY"),
                                                    Components.textBox(Sizing.fill(32), Integer.toString(entranceBlockPositionOffset.getLeft().getZ()))
                                                            .tooltip(Text.translatable("gui.housing_block.entrancePositionOffsetZ.tooltip"))
                                                            .margins(Insets.of(1, 1, 1, 1))
                                                            .id("entrancePositionOffsetZ")
                                            ))
                                            .verticalAlignment(VerticalAlignment.CENTER)
                                            .horizontalAlignment(HorizontalAlignment.CENTER),
                                    Containers.horizontalFlow(Sizing.fill(100), Sizing.content())
                                            .children(List.of(
                                                    Components.textBox(Sizing.fill(50), Double.toString(entranceBlockPositionOffset.getRight().getRight()))
                                                            .tooltip(Text.translatable("gui.housing_block.entranceYaw.tooltip"))
                                                            .margins(Insets.of(1, 1, 1, 1))
                                                            .id("entranceYaw"),
                                                    Components.textBox(Sizing.fill(50), Double.toString(entranceBlockPositionOffset.getRight().getRight()))
                                                            .tooltip(Text.translatable("gui.housing_block.entrancePitch.tooltip"))
                                                            .margins(Insets.of(1, 1, 1, 1))
                                                            .id("entrancePitch")
                                            ))
                                            .verticalAlignment(VerticalAlignment.CENTER)
                                            .horizontalAlignment(HorizontalAlignment.CENTER),

                                    // triggered block
                                    Containers.horizontalFlow(Sizing.fill(100), Sizing.content())
                                            .children(List.of(
                                                    Components.textBox(Sizing.fill(32), Integer.toString(triggeredBlockPositionOffset.getX()))
                                                            .tooltip(Text.translatable("gui.housing_block.triggeredBlockPositionOffsetX.tooltip"))
                                                            .margins(Insets.of(1, 1, 1, 1))
                                                            .id("triggeredBlockPositionOffsetX"),
                                                    Components.textBox(Sizing.fill(32), Integer.toString(triggeredBlockPositionOffset.getY()))
                                                            .tooltip(Text.translatable("gui.housing_block.triggeredBlockPositionOffsetY.tooltip"))
                                                            .margins(Insets.of(1, 1, 1, 1))
                                                            .id("triggeredBlockPositionOffsetY"),
                                                    Components.textBox(Sizing.fill(32), Integer.toString(triggeredBlockPositionOffset.getZ()))
                                                            .tooltip(Text.translatable("gui.housing_block.triggeredBlockPositionOffsetZ.tooltip"))
                                                            .margins(Insets.of(1, 1, 1, 1))
                                                            .id("triggeredBlockPositionOffsetZ")
                                            ))
                                            .verticalAlignment(VerticalAlignment.CENTER)
                                            .horizontalAlignment(HorizontalAlignment.CENTER),

                                    Components.button(this.ownerMode == 0 ? Text.translatable("gui.housing_block.toggleOwnerModeButton_0")
                                                            : Text.translatable("gui.housing_block.toggleOwnerModeButton_1")
                                                    , button -> this.toggleOwnerMode())
                                            .sizing(Sizing.fill(100), Sizing.fixed(20))
                                            .tooltip(this.ownerMode == 0 ? Text.translatable("gui.housing_block.toggleOwnerModeButton_0.tooltip")
                                                    : Text.translatable("gui.housing_block.toggleOwnerModeButton_1.tooltip"))
                                            .id("toggleOwnerModeButton"),

                                    Components.button(Text.translatable("gui.housing_block.resetOwnerButton")
                                                    , button -> this.trySetHouseOwner(false))
                                            .sizing(Sizing.fill(100), Sizing.fixed(20))
                                            .tooltip(Text.translatable("gui.housing_block.resetOwnerButton.tooltip"))
                                            .id("resetOwnerButton"),

                                    // save/close buttons
                                    Containers.horizontalFlow(Sizing.fill(30), Sizing.content())
                                            .children(List.of(
                                                    Components.button(Text.translatable("gui.save"), button -> this.saveCreative())
                                                            .sizing(Sizing.fill(49), Sizing.fixed(20))
                                                            .id("saveCreativeButton"),
                                                    Components.button(ScreenTexts.CANCEL, button -> this.cancel())
                                                            .sizing(Sizing.fill(49), Sizing.fixed(20))
                                                            .id("cancelButton")
                                            ))
                                            .margins(Insets.of(1, 1, 1, 1))
                                            .verticalAlignment(VerticalAlignment.CENTER)
                                            .horizontalAlignment(HorizontalAlignment.CENTER)
                            ))
                            .padding(Insets.of(7, 7, 7, 7))
                            .surface(ExtendedSurface.customPanel(INVENTORY_NO_TOP_TEXTURE))
            ));
        } else {
            rootComponent.children(List.of(
                    Containers.horizontalFlow(Sizing.fixed(250), Sizing.content())
                            .children(List.of(
                                    new CustomButtonComponent(Text.translatable("gui.housing_screen.tabHousesList.button"), button -> this.updateTabContent(0))
                                            .renderer(CustomButtonComponent.Renderer.texture(INVENTORY_TABS_TEXTURE, 0, 0, 256, 256))
                                            .textShadow(false)
                                            .activeTextColor(0x000000)
                                            .inactiveTextColor(0x000000)
                                            .sizing(Sizing.fixed(125), Sizing.fixed(32))
                                            .id("tabHousesListButton"),
                                    new CustomButtonComponent(Text.translatable("gui.housing_screen.tabCurrentHouse.button"), button -> this.updateTabContent(1))
                                            .renderer(CustomButtonComponent.Renderer.texture(INVENTORY_TABS_TEXTURE, 125, 0, 256, 256))
                                            .textShadow(false)
                                            .activeTextColor(0x000000)
                                            .inactiveTextColor(0x000000)
                                            .sizing(Sizing.fixed(125), Sizing.fixed(32))
                                            .id("tabCurrentHouseButton")
                            ))
                            .horizontalAlignment(HorizontalAlignment.CENTER)
                            .verticalAlignment(VerticalAlignment.CENTER)
                            .surface(ExtendedSurface.customPanel(INVENTORY_ONLY_TOP_TEXTURE))
                            .id("tabs_container"),
                    Containers.verticalFlow(Sizing.fixed(250), Sizing.fixed(166))
                            .children(List.of(
                                    Containers.horizontalFlow(Sizing.fill(100), Sizing.content())
                                            .child(Components.label(this.title)
                                                    .color(Color.ofArgb(Colors.BLACK))
                                                    .margins(Insets.of(0, 4, 0, 0))),
                                    Containers.verticalFlow(Sizing.content(), Sizing.content())
                                            .padding(Insets.of(0, 0, 0, 0))
                                            .horizontalAlignment(HorizontalAlignment.CENTER)
                                            .verticalAlignment(VerticalAlignment.CENTER)
                                            .id("tab_content_container")
                            ))
                            .padding(Insets.of(0, 7, 7, 7))
                            .surface(ExtendedSurface.customPanel(INVENTORY_NO_TOP_TEXTURE))
            ));

            this.updateTabContent(this.currentTab);
        }
    }

    private void updateTabContent(int tab) {
        this.component(FlowLayout.class, "tab_content_container").clearChildren();
        if (tab == 1) {
            this.currentTab = tab;
            this.component(CustomButtonComponent.class, "tabHousesListButton").active(true);
            this.component(CustomButtonComponent.class, "tabCurrentHouseButton").active(false);

            // build content container
            this.component(FlowLayout.class, "tab_content_container")
                    .children(List.of(
                            Containers.horizontalFlow(Sizing.content(), Sizing.content())
                                    .id("tab_current_house_title_container"),
                            Containers.verticalFlow(Sizing.content(), Sizing.content())
                                    .horizontalAlignment(HorizontalAlignment.CENTER)
                                    .verticalAlignment(VerticalAlignment.CENTER)
                                    .id("tab_current_house_content_container")
                    ));

            // title
            if (this.currentPermissionLevel == 0) {
                this.component(FlowLayout.class, "tab_current_house_title_container")
                        .child(Components.label(Text.translatable("gui.housing_screen.tabCurrentHouse.title.owner"))
                                .color(Color.ofArgb(Colors.BLACK)));
                this.component(FlowLayout.class, "tab_current_house_content_container")
                        .children(List.of(
                                Components.button(this.client.player.hasStatusEffect(StatusEffectsRegistry.ADVENTURE_BUILDING_EFFECT)
                                                ? Text.translatable("gui.housing_screen.tabCurrentHouse.toggleAdventureBuildingEffectButton.off") : Text.translatable("gui.housing_screen.tabCurrentHouse.toggleAdventureBuildingEffectButton.on"), button -> this.toggleAdventureBuildingEffect())
                                        .sizing(Sizing.content(), Sizing.content())
                                        .id("toggleAdventureBuildingEffectButton"),
                                Components.button(Text.translatable("gui.housing_screen.tabCurrentHouse.editHousingButton"), button -> this.updateTabContent(2))
                                        .sizing(Sizing.content(), Sizing.content())
                                        .id("editHousingButton"),
                                Components.button(Text.translatable("gui.housing_screen.tabCurrentHouse.unclaimHouseButton"), button -> this.trySetHouseOwner(false))
                                        .sizing(Sizing.content(), Sizing.content())
                                        .id("unclaimHouseButton")
                        ));
            } else if (this.currentPermissionLevel == 1) {
                this.component(FlowLayout.class, "tab_current_house_title_container")
                        .child(Components.label(Text.translatable("gui.housing_screen.tabCurrentHouse.title.co_owner"))
                                .color(Color.ofArgb(Colors.BLACK)));
                this.component(FlowLayout.class, "tab_current_house_content_container")
                        .children(List.of(
                                Components.button(this.client.player.hasStatusEffect(StatusEffectsRegistry.ADVENTURE_BUILDING_EFFECT) ? Text.translatable("gui.housing_screen.tabCurrentHouse.toggleAdventureBuildingEffectButton.off") : Text.translatable("gui.housing_screen.tabCurrentHouse.toggleAdventureBuildingEffectButton.on"), button -> this.toggleAdventureBuildingEffect())
                                        .sizing(Sizing.content(), Sizing.content())
                                        .id("toggleAdventureBuildingEffectButton")
                        ));
            } else if (this.currentPermissionLevel == 2) {
                this.component(FlowLayout.class, "tab_current_house_title_container")
                        .child(Components.label(Text.translatable("gui.housing_screen.tabCurrentHouse.title.trusted"))
                                .color(Color.ofArgb(Colors.BLACK)));
            } else if (this.currentPermissionLevel == 3) {
                this.component(FlowLayout.class, "tab_current_house_title_container")
                        .child(Components.label(Text.translatable("gui.housing_screen.tabCurrentHouse.title.guest"))
                                .color(Color.ofArgb(Colors.BLACK)));
            } else {
                this.component(FlowLayout.class, "tab_current_house_title_container")
                        .child(Components.label(Text.translatable("gui.housing_screen.tabCurrentHouse.title.stranger"))
                                .color(Color.ofArgb(Colors.BLACK)));
                if (this.ownerMode == 1 && Objects.equals(this.housingBlockBlockEntity.getOwner(), "")) {
                    this.component(FlowLayout.class, "tab_current_house_content_container")
                            .child(
                                    Components.button(Text.translatable("gui.housing_screen.tabCurrentHouse.claimHouseButton"), button -> this.trySetHouseOwner(true))
                                            .sizing(Sizing.content(), Sizing.content())
                                            .id("claimHouseButton")
                            );
                }
            }

            // content
            this.component(FlowLayout.class, "tab_current_house_content_container")
                    .children(List.of(
                            Components.button(Text.translatable("gui.housing_screen.tabCurrentHouse.leaveCurrentHouseButton"), button -> this.leaveCurrentHouse())
                                    .sizing(Sizing.content(), Sizing.content())
                                    .id("leaveCurrentHouseButton"),
                            Containers.horizontalFlow(Sizing.content(), Sizing.content())
                                    .children(List.of(
                                            Components.button(ScreenTexts.CANCEL, button -> this.cancel())
                                                    .sizing(Sizing.content(), Sizing.content())
                                                    .id("cancelButton")
                                    ))
                                    .padding(Insets.of(0, 0, 0, 0))
                                    .horizontalAlignment(HorizontalAlignment.CENTER)
                                    .verticalAlignment(VerticalAlignment.CENTER)
                    ));
        } else if (tab == 2 && this.currentPermissionLevel == 0 && this.housingBlockBlockEntity != null) {
            this.currentTab = tab;
            this.component(CustomButtonComponent.class, "tabHousesListButton").active(true);
            this.component(CustomButtonComponent.class, "tabCurrentHouseButton").active(true);

            // build content container
            this.component(FlowLayout.class, "tab_content_container")
                    .children(List.of(
                            Containers.horizontalFlow(Sizing.content(), Sizing.content())
                                    .id("tab_edit_house_title_container"),
                            Containers.verticalFlow(Sizing.content(), Sizing.content())
                                    .horizontalAlignment(HorizontalAlignment.CENTER)
                                    .verticalAlignment(VerticalAlignment.CENTER)
                                    .id("tab_edit_house_content_container")
                    ));

            // title
            this.component(FlowLayout.class, "tab_edit_house_title_container")
                    .child(Components.label(Text.translatable("gui.housing_screen.tabEditHouse.title"))
                            .color(Color.ofArgb(Colors.BLACK)));

            // content
            this.component(FlowLayout.class, "tab_edit_house_content_container")
                    .children(List.of(
                            Containers.verticalScroll(Sizing.fixed(236), Sizing.fixed(100),
                                            Containers.verticalFlow(Sizing.fixed(222), Sizing.content())
                                                    .children(List.of(
                                                            Containers.verticalFlow(Sizing.content(), Sizing.content())
                                                                    .children(List.of(
                                                                            Components.label(Text.translatable("gui.housing_screen.tabEditHouse.coOwnerList.title")).color(Color.BLACK),
                                                                            Containers.verticalFlow(Sizing.content(), Sizing.content())
                                                                                    .id("coOwnerListContent"),
                                                                            Containers.horizontalFlow(Sizing.content(), Sizing.content())
                                                                                    .children(List.of(
                                                                                            Components.textBox(Sizing.fill(60), "New Co-Owner"),
                                                                                            Components.button(Text.translatable("gui.housing_screen.tabEditHouse.addEntry"), this::addEntry)
                                                                                    ))
                                                                    )),
                                                            Containers.verticalFlow(Sizing.content(), Sizing.content())
                                                                    .children(List.of(
                                                                            Components.label(Text.translatable("gui.housing_screen.tabEditHouse.trustedList.title")).color(Color.BLACK),
                                                                            Containers.verticalFlow(Sizing.content(), Sizing.content())
                                                                                    .id("trustedListContent"),
                                                                            Containers.horizontalFlow(Sizing.fill(100), Sizing.content())
                                                                                    .children(List.of(
                                                                                            Components.textBox(Sizing.fill(55), "New Trusted Person"),
                                                                                            Components.button(Text.translatable("gui.housing_screen.tabEditHouse.addEntry"), this::addEntry)
                                                                                    ))
                                                                                    .horizontalAlignment(HorizontalAlignment.RIGHT)
                                                                                    .verticalAlignment(VerticalAlignment.CENTER)
                                                                    )),
                                                            Containers.verticalFlow(Sizing.content(), Sizing.content())
                                                                    .children(List.of(
                                                                            Components.label(Text.translatable("gui.housing_screen.tabEditHouse.guestList.title")).color(Color.BLACK),
                                                                            Containers.verticalFlow(Sizing.content(), Sizing.content())
                                                                                    .id("guestListContent"),
                                                                            Containers.horizontalFlow(Sizing.content(), Sizing.content())
                                                                                    .children(List.of(
                                                                                            Components.textBox(Sizing.fill(55), "New Guest"),
                                                                                            Components.button(Text.translatable("gui.housing_screen.tabEditHouse.addEntry"), this::addEntry)
                                                                                    ))
                                                                    )),
                                                            Components.button(ScreenTexts.CANCEL, button -> this.updateTabContent(1))
                                                                    .sizing(Sizing.content(), Sizing.content())
                                                                    .id("cancelEditHousingButton"),
                                                            Components.button(Text.translatable("gui.housing_screen.tabEditHouse.resetHouse"), button -> this.resetHouse())
                                                    ))
                                    )
                                    .scrollbar(ScrollContainer.Scrollbar.vanilla())
                                    .scrollbarThiccness(14)
                                    .scrollStep(18)
                                    .fixedScrollbarLength(17)
                                    .allowOverflow(false),
                            Containers.horizontalFlow(Sizing.content(), Sizing.content())
                                    .children(List.of(
                                            Components.button(Text.translatable("gui.save"), button -> this.saveAdventure())
                                                    .sizing(Sizing.content(), Sizing.content())
                                                    .id("updateHousingBlockButton"),
                                            Components.button(ScreenTexts.CANCEL, button -> this.updateTabContent(1))
                                                    .sizing(Sizing.content(), Sizing.content())
                                                    .id("cancelEditHousingButton")
                                    ))
                                    .id("tab_edit_house_close_buttons_container")
                    ));

            // populate co owner list
            for (String coOwner : this.housingBlockBlockEntity.getCoOwnerList()) {
                this.component(FlowLayout.class, "coOwnerListContent").child(
                        Containers.horizontalFlow(Sizing.content(), Sizing.content())
                                .children(List.of(
                                        Containers.verticalFlow(Sizing.content(), Sizing.content())
                                                .child(
                                                        Components.label(Text.literal(coOwner))
                                                ),
                                        Components.button(Text.translatable("gui.delete"), this::deleteEntry)
                                ))
                );
            }

            // populate trusted list
            for (String trusted : this.housingBlockBlockEntity.getTrustedList()) {
                this.component(FlowLayout.class, "trustedListContent").child(
                        Containers.horizontalFlow(Sizing.content(), Sizing.content())
                                .children(List.of(
                                        Containers.verticalFlow(Sizing.content(), Sizing.content())
                                                .child(
                                                        Components.label(Text.literal(trusted))
                                                ),
                                        Components.button(Text.translatable("gui.delete"), this::deleteEntry)
                                ))
                );
            }

            // populate guest list
            for (String guest : this.housingBlockBlockEntity.getGuestList()) {
                this.component(FlowLayout.class, "guestListContent").child(
                        Containers.horizontalFlow(Sizing.content(), Sizing.content())
                                .children(List.of(
                                        Containers.verticalFlow(Sizing.content(), Sizing.content())
                                                .child(
                                                        Components.label(Text.literal(guest))
                                                ),
                                        Components.button(Text.translatable("gui.delete"), this::deleteEntry)
                                ))
                );
            }

        } else {
            this.currentTab = 0;
            this.component(CustomButtonComponent.class, "tabHousesListButton").active(false);
            this.component(CustomButtonComponent.class, "tabCurrentHouseButton").active(this.currentPermissionLevel <= 4);
            this.component(FlowLayout.class, "tab_content_container")
                    .children(List.of(
                            Containers.horizontalFlow(Sizing.content(), Sizing.content())
                                    .child(Components.label(Text.translatable("gui.housing_screen.tabHousesList.title"))
                                            .color(Color.ofArgb(Colors.BLACK))
                                            .margins(Insets.of(0, 4, 0, 0)))
                    ));
        }
    }
*/

    @Override
    protected void init() {
//        if (this.client != null && this.client.world != null) {
//            BlockEntity blockEntity = this.client.world.getBlockEntity(this.handler.getBlockPos());
//            if (blockEntity instanceof TeleporterBlockBlockEntity) {
//                this.teleporterBlock = (TeleporterBlockBlockEntity) blockEntity;
//            }
//            if (this.client.player != null) {
//                this.currentTargetOwner = this.client.player.networkHandler.getPlayerListEntry(this.client.player.getUuid());
//            }
//        }
        this.coOwnerList.clear();
        this.trustedList.clear();
        this.guestList.clear();
        if (this.housingBlockBlockEntity != null) {
            this.coOwnerList.addAll(this.housingBlockBlockEntity.getCoOwnerList());
            this.trustedList.addAll(this.housingBlockBlockEntity.getTrustedList());
            this.guestList.addAll(this.housingBlockBlockEntity.getGuestList());
            this.showInfluenceArea = housingBlockBlockEntity.getShowInfluenceArea();
            this.ownerMode = housingBlockBlockEntity.getOwnerMode();
        }
//        this.currentTargetIdentifier = "";
//        this.currentTargetDisplayName = "";
//        this.currentTargetEntrance = "";
//        this.currentTargetEntranceDisplayName = "";
//        if (!this.showCreativeTab) {
//            this.backgroundWidth = 218;
//            if (this.teleporterBlock.getTeleportationMode() == TeleporterBlockBlockEntity.TeleportationMode.DUNGEONS) {
//                this.backgroundHeight = 158;
//            } else if (this.teleporterBlock.getTeleportationMode() == TeleporterBlockBlockEntity.TeleportationMode.HOUSING) {
//                this.backgroundHeight = 158;
//            } else {
//                this.backgroundHeight = 47;
//            }
//            this.thisMethodHandlesTheAdvancementStuffTODORename(true);
//        }
        super.init();
        //region adventure screen

//        this.openChooseTargetOwnerScreenButton = this.addDrawableChild(ButtonWidget.builder(EDIT_BUTTON_LABEL_TEXT, button -> this.openChooseCurrentTargetOwnerScreen()).dimensions(this.x + this.backgroundWidth - 57, this.y + 33, 50, 20).build());
////        this.confirmChooseCurrentTargetOwnerButton = this.addDrawableChild(ButtonWidget.builder(CONFIRM_BUTTON_LABEL_TEXT, button -> this.confirmChooseCurrentTargetOwner()).dimensions(this.x + 7, this.y + this.backgroundHeight - 27, 100, 20).build());
//        this.cancelChooseTargetOwnerButton = this.addDrawableChild(ButtonWidget.builder(CANCEL_BUTTON_LABEL_TEXT, button -> this.cancelChooseCurrentTargetOwner()).dimensions(this.x + 7, this.y + this.backgroundHeight - 27, this.backgroundWidth - 14, 20).build());
//
//        this.openChooseTargetIdentifierScreenButton = this.addDrawableChild(ButtonWidget.builder(EDIT_BUTTON_LABEL_TEXT, button -> this.openChooseTargetIdentifierScreen()).dimensions(this.x + this.backgroundWidth - 57, this.y + 77, 50, 20).build());
//        this.confirmChooseTargetIdentifier0Button = this.addDrawableChild(ButtonWidget.builder(CHOOSE_BUTTON_LABEL_TEXT, button -> this.chooseTargetIdentifier(0)).dimensions(this.x + this.backgroundWidth - 57, this.y + 20, 50, 20).build());
//        this.confirmChooseTargetIdentifier1Button = this.addDrawableChild(ButtonWidget.builder(CHOOSE_BUTTON_LABEL_TEXT, button -> this.chooseTargetIdentifier(1)).dimensions(this.x + this.backgroundWidth - 57, this.y + 44, 50, 20).build());
//        this.confirmChooseTargetIdentifier2Button = this.addDrawableChild(ButtonWidget.builder(CHOOSE_BUTTON_LABEL_TEXT, button -> this.chooseTargetIdentifier(2)).dimensions(this.x + this.backgroundWidth - 57, this.y + 68, 50, 20).build());
//        this.confirmChooseTargetIdentifier3Button = this.addDrawableChild(ButtonWidget.builder(CHOOSE_BUTTON_LABEL_TEXT, button -> this.chooseTargetIdentifier(3)).dimensions(this.x + this.backgroundWidth - 57, this.y + 92, 50, 20).build());
//        this.cancelChooseTargetIdentifierButton = this.addDrawableChild(ButtonWidget.builder(CANCEL_BUTTON_LABEL_TEXT, button -> this.cancelChooseTargetLocation()).dimensions(this.x + 7, this.y + this.backgroundHeight - 27, this.backgroundWidth - 14, 20).build());

//        this.openChooseTargetEntranceScreenButton = this.addDrawableChild(ButtonWidget.builder(EDIT_BUTTON_LABEL_TEXT, button -> this.openChooseCurrentTargetEntranceScreen()).dimensions(this.x + this.backgroundWidth - 57, this.y + 107, 50, 20).build());
//        this.confirmChooseCurrentTargetEntranceButton = this.addDrawableChild(ButtonWidget.builder(CONFIRM_BUTTON_LABEL_TEXT, button -> this.confirmChooseCurrentTargetEntrance()).dimensions(this.x + 7, this.y + this.backgroundHeight - 27, 100, 20).build());
//        this.cancelChooseTargetEntranceButton = this.addDrawableChild(ButtonWidget.builder(CANCEL_BUTTON_LABEL_TEXT, button -> this.cancelChooseCurrentTargetEntrance()).dimensions(this.x + 7, this.y + this.backgroundHeight - 27, this.backgroundWidth - 14, 20).build());

//        this.teleportButton = this.addDrawableChild(ButtonWidget.builder(Text.translatable(this.teleporterBlock.getTeleportButtonLabel()), button -> this.teleport()).dimensions(this.x + 7, this.y + this.backgroundHeight - 27, 100, 20).build());
//        this.cancelTeleportButton = this.addDrawableChild(ButtonWidget.builder(Text.translatable(this.teleporterBlock.getCancelTeleportButtonLabel()), button -> this.cancelTeleport()).dimensions(this.x + this.backgroundWidth - 107, this.y + this.backgroundHeight - 27, 100, 20).build());

//        if (this.teleporterBlock.getTeleportationMode() == TeleporterBlockBlockEntity.TeleportationMode.DUNGEONS || this.teleporterBlock.getTeleportationMode() == TeleporterBlockBlockEntity.TeleportationMode.HOUSING) {
//            this.cancelTeleportButton.setPosition(this.x + this.backgroundWidth - 107, this.y + this.backgroundHeight - 27);
//            this.teleportButton.setWidth(100);
//            this.cancelTeleportButton.setWidth(100);
//        }
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

//        this.showActivationArea = this.teleporterBlock.getShowActivationArea();
//        i = this.textRenderer.getWidth(SHOW_ADVENTURE_SCREEN_LABEL_TEXT) + 10;
//        this.showAdventureScreenButton = this.addDrawableChild(CyclingButtonWidget.onOffBuilder(/*Text.translatable(""), Text.translatable("")*/).initially(this.showAdventureScreen).omitKeyText().build(this.width / 2 - 152 + i, 140, 300 - i, 20, SHOW_ADVENTURE_SCREEN_LABEL_TEXT, (button, showAdventureScreen) -> {
//            this.showAdventureScreen = showAdventureScreen;
//        }));


        // --- entrance page ---

        this.entrancePositionOffsetXField = new TextFieldWidget(this.textRenderer, this.width / 2 - 154, 80, 100, 20, Text.empty());
        this.entrancePositionOffsetXField.setMaxLength(128);
        this.entrancePositionOffsetXField.setText(Integer.toString(this.housingBlockBlockEntity != null ? this.housingBlockBlockEntity.getEntrance().getLeft().getX() : 0));
        this.addSelectableChild(this.entrancePositionOffsetXField);

        this.entrancePositionOffsetYField = new TextFieldWidget(this.textRenderer, this.width / 2 - 50, 80, 100, 20, Text.empty());
        this.entrancePositionOffsetYField.setMaxLength(128);
        this.entrancePositionOffsetYField.setText(Integer.toString(this.housingBlockBlockEntity != null ? this.housingBlockBlockEntity.getEntrance().getLeft().getY() : 0));
        this.addSelectableChild(this.entrancePositionOffsetYField);

        this.entrancePositionOffsetZField = new TextFieldWidget(this.textRenderer, this.width / 2 + 54, 80, 100, 20, Text.empty());
        this.entrancePositionOffsetZField.setMaxLength(128);
        this.entrancePositionOffsetZField.setText(Integer.toString(this.housingBlockBlockEntity != null ? this.housingBlockBlockEntity.getEntrance().getLeft().getZ() : 0));
        this.addSelectableChild(this.entrancePositionOffsetZField);

        this.entranceOrientationYawField = new TextFieldWidget(this.textRenderer, this.width / 2 - 154, 115, 100, 20, Text.empty());
        this.entranceOrientationYawField.setMaxLength(128);
        this.entranceOrientationYawField.setText(Double.toString(this.housingBlockBlockEntity != null ? this.housingBlockBlockEntity.getEntrance().getRight().getLeft() : 0));
        this.addSelectableChild(this.entranceOrientationYawField);

        this.entranceOrientationPitchField = new TextFieldWidget(this.textRenderer, this.width / 2 - 50, 115, 100, 20, Text.empty());
        this.entranceOrientationPitchField.setMaxLength(128);
        this.entranceOrientationPitchField.setText(Double.toString(this.housingBlockBlockEntity != null ? this.housingBlockBlockEntity.getEntrance().getRight().getRight() : 0));
        this.addSelectableChild(this.entranceOrientationPitchField);

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

//        i = this.textRenderer.getWidth(OWNER_MODE_LABEL_TEXT) + 10;
        this.toggleOwnerModeButton = this.addDrawableChild(CyclingButtonWidget.builder(HousingBlockBlockEntity.OwnerMode::asText).values((HousingBlockBlockEntity.OwnerMode[])HousingBlockBlockEntity.OwnerMode.values()).initially(this.ownerMode).omitKeyText().build(this.width / 2 - 153, 70, 300, 20, Text.empty(), (button, ownerMode) -> {
            this.ownerMode = ownerMode;
        }));

        this.resetOwnerButton = this.addDrawableChild(ButtonWidget.builder(ScreenTexts.DONE, button -> this.trySetHouseOwner(false)).dimensions(this.width / 2 - 4 - 150, 210, 150, 20).build());

//        // teleportation mode: dungeons
//
//        this.removeDungeonLocationButton0 = this.addDrawableChild(ButtonWidget.builder(REMOVE_LOCATION_BUTTON_LABEL_TEXT, button -> this.removeLocationFromLocationList(0, true)).dimensions(this.width / 2 + 54, 70, 100, 20).build());
//        this.removeDungeonLocationButton1 = this.addDrawableChild(ButtonWidget.builder(REMOVE_LOCATION_BUTTON_LABEL_TEXT, button -> this.removeLocationFromLocationList(1, true)).dimensions(this.width / 2 + 54, 95, 100, 20).build());
//        this.removeDungeonLocationButton2 = this.addDrawableChild(ButtonWidget.builder(REMOVE_LOCATION_BUTTON_LABEL_TEXT, button -> this.removeLocationFromLocationList(2, true)).dimensions(this.width / 2 + 54, 120, 100, 20).build());
//
//        this.newDungeonLocationIdentifierField = new TextFieldWidget(this.textRenderer, this.width / 2 - 4 - 150, 160, 150, 20, Text.literal(""));
//        this.newDungeonLocationIdentifierField.setMaxLength(128);
//        this.newDungeonLocationIdentifierField.setPlaceholder(Text.translatable("gui.teleporter_block.target_identifier_field.place_holder"));
//        this.addSelectableChild(this.newDungeonLocationIdentifierField);
//
//        this.newDungeonLocationEntranceField = new TextFieldWidget(this.textRenderer, this.width / 2 + 4, 160, 150, 20, Text.literal(""));
//        this.newDungeonLocationEntranceField.setMaxLength(128);
//        this.newDungeonLocationEntranceField.setPlaceholder(Text.translatable("gui.teleporter_block.target_entrance_field.place_holder"));
//        this.addSelectableChild(this.newDungeonLocationEntranceField);
//
//        this.addNewDungeonLocationButton = this.addDrawableChild(ButtonWidget.builder(ADD_NEW_LOCATION_BUTTON_LABEL_TEXT, button -> this.addDungeonToDungeonsList(this.newDungeonLocationIdentifierField.getText(), this.newDungeonLocationEntranceField.getText())).dimensions(this.width / 2 - 154, 185, 300, 20).build());
//
//        // teleportation mode: housing
//
//        this.removeHousingLocationButton0 = this.addDrawableChild(ButtonWidget.builder(REMOVE_LOCATION_BUTTON_LABEL_TEXT, button -> this.removeLocationFromLocationList(0, false)).dimensions(this.width / 2 + 54, 70, 100, 20).build());
//        this.removeHousingLocationButton1 = this.addDrawableChild(ButtonWidget.builder(REMOVE_LOCATION_BUTTON_LABEL_TEXT, button -> this.removeLocationFromLocationList(1, false)).dimensions(this.width / 2 + 54, 95, 100, 20).build());
//        this.removeHousingLocationButton2 = this.addDrawableChild(ButtonWidget.builder(REMOVE_LOCATION_BUTTON_LABEL_TEXT, button -> this.removeLocationFromLocationList(2, false)).dimensions(this.width / 2 + 54, 120, 100, 20).build());
//        this.removeHousingLocationButton3 = this.addDrawableChild(ButtonWidget.builder(REMOVE_LOCATION_BUTTON_LABEL_TEXT, button -> this.removeLocationFromLocationList(3, false)).dimensions(this.width / 2 + 54, 145, 100, 20).build());
//
//        this.newHousingLocationIdentifierField = new TextFieldWidget(this.textRenderer, this.width / 2 - 4 - 150, 185, 150, 20, Text.literal("new location identifier"));
//        this.newHousingLocationIdentifierField.setMaxLength(128);
//        this.newHousingLocationIdentifierField.setPlaceholder(Text.translatable("gui.teleporter_block.target_identifier_field.place_holder"));
//        this.addSelectableChild(this.newHousingLocationIdentifierField);
//
//        this.addNewHousingLocationButton = this.addDrawableChild(ButtonWidget.builder(ADD_NEW_LOCATION_BUTTON_LABEL_TEXT, button -> this.addHouseToHousesList(this.newHousingLocationIdentifierField.getText())).dimensions(this.width / 2  + 4, 185, 150, 20).build());
//
//
//        // --- adventure screen customization page ---
//
//        this.teleporterNameField = new TextFieldWidget(this.textRenderer, this.width / 2 - 154, 44, 300, 20, Text.empty());
//        this.teleporterNameField.setMaxLength(128);
//        this.teleporterNameField.setPlaceholder(Text.translatable("gui.teleporter_block.teleporter_name_field.place_holder"));
//        this.teleporterNameField.setText(this.teleporterBlock.getTeleporterName());
//        this.addSelectableChild(this.teleporterNameField);
//
//        this.currentTargetOwnerLabelField = new TextFieldWidget(this.textRenderer, this.width / 2 - 154, 68, 300, 20, Text.empty());
//        this.currentTargetOwnerLabelField.setMaxLength(128);
//        this.currentTargetOwnerLabelField.setPlaceholder(Text.translatable("gui.teleporter_block.target_owner_field.place_holder"));
//        this.currentTargetOwnerLabelField.setText(this.teleporterBlock.getCurrentTargetOwnerLabel());
//        this.addSelectableChild(this.currentTargetOwnerLabelField);
//
//        this.currentTargetIdentifierLabelField = new TextFieldWidget(this.textRenderer, this.width / 2 - 154, 92, 300, 20, Text.empty());
//        this.currentTargetIdentifierLabelField.setMaxLength(128);
//        this.currentTargetIdentifierLabelField.setPlaceholder(Text.translatable("gui.teleporter_block.target_identifier_field.place_holder"));
//        this.currentTargetIdentifierLabelField.setText(this.teleporterBlock.getCurrentTargetIdentifierLabel());
//        this.addSelectableChild(this.currentTargetIdentifierLabelField);
//
//        this.currentTargetEntranceLabelField = new TextFieldWidget(this.textRenderer, this.width / 2 - 154, 116, 300, 20, Text.empty());
//        this.currentTargetEntranceLabelField.setMaxLength(128);
//        this.currentTargetEntranceLabelField.setPlaceholder(Text.translatable("gui.teleporter_block.target_entrance_field.place_holder"));
//        this.currentTargetEntranceLabelField.setText(this.teleporterBlock.getCurrentTargetEntranceLabel());
//        this.addSelectableChild(this.currentTargetEntranceLabelField);
//
//        this.teleportButtonLabelField = new TextFieldWidget(this.textRenderer, this.width / 2 - 154, 140, 300, 20, Text.empty());
//        this.teleportButtonLabelField.setMaxLength(128);
//        this.teleportButtonLabelField.setPlaceholder(Text.translatable("gui.teleporter_block.teleport_button.place_holder"));
//        this.teleportButtonLabelField.setText(this.teleporterBlock.getTeleportButtonLabel());
//        this.addSelectableChild(this.teleportButtonLabelField);
//
//        this.cancelTeleportButtonLabelField = new TextFieldWidget(this.textRenderer, this.width / 2 - 154, 164, 300, 20, Text.empty());
//        this.cancelTeleportButtonLabelField.setMaxLength(128);
//        this.cancelTeleportButtonLabelField.setPlaceholder(Text.translatable("gui.teleporter_block.cancel_teleport_button.place_holder"));
//        this.cancelTeleportButtonLabelField.setText(this.teleporterBlock.getCancelTeleportButtonLabel());
//        this.addSelectableChild(this.cancelTeleportButtonLabelField);

        this.saveCreativeButton = this.addDrawableChild(ButtonWidget.builder(ScreenTexts.DONE, button -> this.saveCreative()).dimensions(this.width / 2 - 4 - 150, 210, 150, 20).build());
        this.cancelButton = this.addDrawableChild(ButtonWidget.builder(ScreenTexts.CANCEL, button -> this.cancel()).dimensions(this.width / 2 + 4, 210, 150, 20).build());

        //        this.consumeKeyItemStack = this.teleporterBlock.getConsumeKeyItemStack();
//        i = this.textRenderer.getWidth(CONSUME_KEY_ITEMSTACK_LABEL_TEXT) + 10;
//        this.consumeKeyItemStackButton = this.addDrawableChild(CyclingButtonWidget.onOffBuilder(Text.translatable(""), Text.translatable("")).initially(this.consumeKeyItemStack).omitKeyText().build(this.width / 2 - 152 + i, 215, 300 - i, 20, CONSUME_KEY_ITEMSTACK_LABEL_TEXT, (button, consumeKeyItemStack) -> {
//            this.consumeKeyItemStack = consumeKeyItemStack;
//        }));
        //endregion creative screen
        this.updateWidgets();
    }

    private void updateWidgets() {

        //region adventure widgets
//        this.openChooseTargetOwnerScreenButton.visible = false;
////        this.confirmChooseCurrentTargetOwnerButton.visible = false;
//        this.cancelChooseTargetOwnerButton.visible = false;
//
//        this.openChooseTargetIdentifierScreenButton.visible = false;
////        this.confirmChooseCurrentTargetIdentifierButton.visible = false;
//        this.confirmChooseTargetIdentifier0Button.visible = false;
//        this.confirmChooseTargetIdentifier1Button.visible = false;
//        this.confirmChooseTargetIdentifier2Button.visible = false;
//        this.confirmChooseTargetIdentifier3Button.visible = false;
//        this.cancelChooseTargetIdentifierButton.visible = false;
//
////        this.openChooseTargetEntranceScreenButton.visible = false;
////        this.confirmChooseCurrentTargetEntranceButton.visible = false;
////        this.cancelChooseTargetEntranceButton.visible = false;
//
//        this.teleportButton.visible = false;
//        this.cancelTeleportButton.visible = false;
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
        
        this.entrancePositionOffsetXField.setVisible(false);
        this.entrancePositionOffsetYField.setVisible(false);
        this.entrancePositionOffsetZField.setVisible(false);
        this.entranceOrientationYawField.setVisible(false);
        this.entranceOrientationPitchField.setVisible(false);
        
        this.triggeredBlockPositionOffsetXField.setVisible(false);
        this.triggeredBlockPositionOffsetYField.setVisible(false);
        this.triggeredBlockPositionOffsetZField.setVisible(false);
        
        this.toggleOwnerModeButton.visible = false;
        this.resetOwnerButton.visible = false;
        
        this.saveCreativeButton.visible = false;
        this.cancelButton.visible = false;

//        this.directTeleportPositionOffsetXField.setVisible(false);
//        this.directTeleportPositionOffsetYField.setVisible(false);
//        this.directTeleportPositionOffsetZField.setVisible(false);
//        this.directTeleportOrientationYawField.setVisible(false);
//        this.directTeleportOrientationPitchField.setVisible(false);
//
//        this.locationTypeButton.visible = false;
//
//        this.removeDungeonLocationButton0.visible = false;
//        this.removeDungeonLocationButton1.visible = false;
//        this.removeDungeonLocationButton2.visible = false;
//
//        this.newDungeonLocationIdentifierField.setVisible(false);
//        this.newDungeonLocationEntranceField.setVisible(false);
//        this.addNewDungeonLocationButton.visible = false;
//
//        this.removeHousingLocationButton0.visible = false;
//        this.removeHousingLocationButton1.visible = false;
//        this.removeHousingLocationButton2.visible = false;
//        this.removeHousingLocationButton3.visible = false;
//
//        this.newHousingLocationIdentifierField.setVisible(false);
//        this.addNewHousingLocationButton.visible = false;
//
//        this.teleporterNameField.setVisible(false);
//        this.currentTargetOwnerLabelField.setVisible(false);
//        this.currentTargetIdentifierLabelField.setVisible(false);
//        this.currentTargetEntranceLabelField.setVisible(false);
//        this.teleportButtonLabelField.setVisible(false);
//        this.cancelTeleportButtonLabelField.setVisible(false);
//
//        this.doneButton.visible = false;
//        this.cancelButton.visible = false;
//
//        for (Slot slot : this.handler.slots) {
//            ((DuckSlotMixin)slot).bamcore$setDisabledOverride(true);
//        }

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

            } else if (this.creativeScreenPage == CreativeScreenPage.ENTRANCE) {

                this.entrancePositionOffsetXField.setVisible(true);
                this.entrancePositionOffsetYField.setVisible(true);
                this.entrancePositionOffsetZField.setVisible(true);
                this.entranceOrientationYawField.setVisible(true);
                this.entranceOrientationPitchField.setVisible(true);

            } else if (this.creativeScreenPage == CreativeScreenPage.TRIGGERED_BLOCK) {

                this.triggeredBlockPositionOffsetXField.setVisible(true);
                this.triggeredBlockPositionOffsetYField.setVisible(true);
                this.triggeredBlockPositionOffsetZField.setVisible(true);

            } else if (this.creativeScreenPage == CreativeScreenPage.OWNER) {

                this.toggleOwnerModeButton.visible = true;
                this.resetOwnerButton.visible = true;

            }

            this.saveCreativeButton.visible = true;
            this.cancelButton.visible = true;

        } else {
//
//            if (showChooseTargetOwnerScreen) {
//
////                this.confirmChooseCurrentTargetOwnerButton.visible = true;
//                this.cancelChooseTargetOwnerButton.visible = true;
//
//            } else if (showChooseTargetIdentifierScreen) {
//
////                this.confirmChooseCurrentTargetIdentifierButton.visible = true;
//                int index = 0;
//                if (this.teleportationMode == TeleporterBlockBlockEntity.TeleportationMode.DUNGEONS) {
//                    for (int i = 0; i < Math.min(4, this.visibleDungeonLocationsList.size()); i++) {
//                        if (index == 0) {
//                            this.confirmChooseTargetIdentifier0Button.visible = true;
//                        } else if (index == 1) {
//                            this.confirmChooseTargetIdentifier1Button.visible = true;
//                        } else if (index == 2) {
//                            this.confirmChooseTargetIdentifier2Button.visible = true;
//                        } else if (index == 3) {
//                            this.confirmChooseTargetIdentifier3Button.visible = true;
//                        }
//                        index++;
//                    }
//                } else {
//                    for (int i = 0; i < Math.min(4, this.visibleHousingLocationsList.size()); i++) {
//                        if (index == 0) {
//                            this.confirmChooseTargetIdentifier0Button.visible = true;
//                        } else if (index == 1) {
//                            this.confirmChooseTargetIdentifier1Button.visible = true;
//                        } else if (index == 2) {
//                            this.confirmChooseTargetIdentifier2Button.visible = true;
//                        } else if (index == 3) {
//                            this.confirmChooseTargetIdentifier3Button.visible = true;
//                        }
//                        index++;
//                    }
//                }
//
//                this.cancelChooseTargetIdentifierButton.visible = true;
//
////            } else if (showChooseTargetEntranceScreen) {
//
////                this.confirmChooseCurrentTargetEntranceButton.visible = true;
////                this.cancelChooseTargetEntranceButton.visible = true;
//
//            } else if (showRegenerationConfirmScreen) {
//
//                this.confirmDungeonRegenerationButton.visible = true;
//                this.cancelDungeonRegenerationButton.visible = true;
//
//            } else {
//
//                if (!(this.teleportationMode == TeleporterBlockBlockEntity.TeleportationMode.DIRECT || this.teleportationMode == TeleporterBlockBlockEntity.TeleportationMode.SAVED_LOCATIONS)) {
//                    this.openChooseTargetIdentifierScreenButton.visible = true;
//                    this.openChooseTargetOwnerScreenButton.visible = true;
////                    if (this.teleportationMode == TeleporterBlockBlockEntity.TeleportationMode.DUNGEONS) {
////                        this.openChooseTargetEntranceScreenButton.visible = true;
////                    }
//                }
//
//                this.teleportButton.visible = true;
//                this.cancelTeleportButton.visible = true;
//
//                this.teleportButton.active = this.isTeleportButtonActive;
//            }
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
//        this.creativeDungeonsLocationsListMouseClicked = false;
//        this.creativeHousingLocationsListMouseClicked = false;
//        this.teamListMouseClicked = false;
//        this.visibleDungeonsLocationsListMouseClicked = false;
//        this.visibleHousingLocationsListMouseClicked = false;
//        int i;
//        int j;
//        if (this.showCreativeTab
//                && this.creativeScreenPage == TeleporterBlockScreen.CreativeScreenPage.TELEPORTATION_MODE
//                && this.teleportationMode == TeleporterBlockBlockEntity.TeleportationMode.DUNGEONS
//                && this.dungeonLocationsList.size() > 3) {
//            i = this.width / 2 - 152;
//            j = 71;
//            if (mouseX >= (double)i && mouseX < (double)(i + 6) && mouseY >= (double)j && mouseY < (double)(j + 68)) {
//                this.creativeDungeonsLocationsListMouseClicked = true;
//            }
//        }
//        if (this.showCreativeTab
//                && this.creativeScreenPage == TeleporterBlockScreen.CreativeScreenPage.TELEPORTATION_MODE
//                && this.teleportationMode == TeleporterBlockBlockEntity.TeleportationMode.HOUSING
//                && this.housingLocationsList.size() > 4) {
//            i = this.width / 2 - 152;
//            j = 71;
//            if (mouseX >= (double)i && mouseX < (double)(i + 6) && mouseY >= (double)j && mouseY < (double)(j + 93)) {
//                this.creativeHousingLocationsListMouseClicked = true;
//            }
//        }
//        // TODO team list
//        if (this.showChooseTargetOwnerScreen) {
//            i = this.x - 13;
//            j = this.y + 134;
//            if (mouseX >= (double)i && mouseX < (double)(i + 6) && mouseY >= (double)j && mouseY < (double)(j + 30)) {
//                this.teamListMouseClicked = true;
//            }
//        }
//        if (!this.showCreativeTab
//                && this.showChooseTargetIdentifierScreen
//                && this.teleportationMode == TeleporterBlockBlockEntity.TeleportationMode.DUNGEONS
//                && this.visibleDungeonLocationsList.size() > 4) {
//            i = this.x + 8;
//            j = this.y + 21;
//            if (mouseX >= (double)i && mouseX < (double)(i + 6) && mouseY >= (double)j && mouseY < (double)(j + 90)) {
//                this.visibleDungeonsLocationsListMouseClicked = true;
//            }
//        }
//        if (!this.showCreativeTab
//                && this.showChooseTargetIdentifierScreen
//                && this.teleportationMode == TeleporterBlockBlockEntity.TeleportationMode.HOUSING
//                && this.visibleHousingLocationsList.size() > 4) {
//            i = this.x + 8;
//            j = this.y + 21;
//            if (mouseX >= (double)i && mouseX < (double)(i + 6) && mouseY >= (double)j && mouseY < (double)(j + 90)) {
//                this.visibleHousingLocationsListMouseClicked = true;
//            }
//        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
//        if (this.showCreativeTab
//                && this.creativeScreenPage == TeleporterBlockScreen.CreativeScreenPage.TELEPORTATION_MODE
//                && this.teleportationMode == TeleporterBlockBlockEntity.TeleportationMode.DUNGEONS
//                && this.dungeonLocationsList.size() > 3
//                && this.creativeDungeonsLocationsListMouseClicked) {
//            int i = this.dungeonLocationsList.size() - 3;
//            float f = (float)deltaY / (float)i;
//            this.creativeDungeonsLocationsListScrollAmount = MathHelper.clamp(this.creativeDungeonsLocationsListScrollAmount + f, 0.0f, 1.0f);
//            this.creativeDungeonsLocationsListScrollPosition = (int)((double)(this.creativeDungeonsLocationsListScrollAmount * (float)i));
//        }
//        if (this.showCreativeTab
//                && this.creativeScreenPage == TeleporterBlockScreen.CreativeScreenPage.TELEPORTATION_MODE
//                && this.teleportationMode == TeleporterBlockBlockEntity.TeleportationMode.HOUSING
//                && this.housingLocationsList.size() > 4
//                && this.creativeHousingLocationsListMouseClicked) {
//            int i = this.housingLocationsList.size() - 4;
//            float f = (float)deltaY / (float)i;
//            this.creativeHousingLocationsListScrollAmount = MathHelper.clamp(this.creativeHousingLocationsListScrollAmount + f, 0.0f, 1.0f);
//            this.creativeHousingLocationsListScrollPosition = (int)((double)(this.creativeHousingLocationsListScrollAmount * (float)i));
//        }
//        // TODO team list
//        if (!this.showCreativeTab
//                && this.showChooseTargetIdentifierScreen
//                && this.teleportationMode == TeleporterBlockBlockEntity.TeleportationMode.DUNGEONS
//                && this.visibleDungeonLocationsList.size() > 4
//                && this.visibleDungeonsLocationsListMouseClicked) {
//            int i = this.visibleDungeonLocationsList.size() - 4;
//            float f = (float)deltaY / (float)i;
//            this.visibleDungeonsLocationsListScrollAmount = MathHelper.clamp(this.visibleDungeonsLocationsListScrollAmount + f, 0.0f, 1.0f);
//            this.visibleDungeonsLocationsListScrollPosition = (int)((double)(this.visibleDungeonsLocationsListScrollAmount * (float)i));
//        }
//        if (!this.showCreativeTab
//                && this.showChooseTargetIdentifierScreen
//                && this.teleportationMode == TeleporterBlockBlockEntity.TeleportationMode.HOUSING
//                && this.visibleHousingLocationsList.size() > 4
//                && this.visibleHousingLocationsListMouseClicked) {
//            int i = this.visibleHousingLocationsList.size() - 4;
//            float f = (float)deltaY / (float)i;
//            this.visibleHousingLocationsListScrollAmount = MathHelper.clamp(this.visibleHousingLocationsListScrollAmount + f, 0.0f, 1.0f);
//            this.visibleHousingLocationsListScrollPosition = (int)((double)(this.visibleHousingLocationsListScrollAmount * (float)i));
//        }
        return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        // TODO
//        if (this.showCreativeTab
//                && this.creativeScreenPage == TeleporterBlockScreen.CreativeScreenPage.TELEPORTATION_MODE
//                && this.teleportationMode == TeleporterBlockBlockEntity.TeleportationMode.DUNGEONS
//                && this.dungeonLocationsList.size() > 3
//                && mouseX >= (double)(this.width / 2 - 152) && mouseX <= (double)(this.width / 2 + 50)
//                && mouseY >= 70 && mouseY <= 140) {
//            int i = this.dungeonLocationsList.size() - 3;
//            float f = (float)verticalAmount / (float)i;
//            this.creativeDungeonsLocationsListScrollAmount = MathHelper.clamp(this.creativeDungeonsLocationsListScrollAmount - f, 0.0f, 1.0f);
//            this.creativeDungeonsLocationsListScrollPosition = (int)((double)(this.creativeDungeonsLocationsListScrollAmount * (float)i));
//        }
//        if (this.showCreativeTab
//                && this.creativeScreenPage == TeleporterBlockScreen.CreativeScreenPage.TELEPORTATION_MODE
//                && this.teleportationMode == TeleporterBlockBlockEntity.TeleportationMode.HOUSING
//                && this.housingLocationsList.size() > 4
//                && mouseX >= (double)(this.width / 2 - 152) && mouseX <= (double)(this.width / 2 + 50)
//                && mouseY >= 70 && mouseY <= 165) {
//            int i = this.housingLocationsList.size() - 4;
//            float f = (float)verticalAmount / (float)i;
//            this.creativeHousingLocationsListScrollAmount = MathHelper.clamp(this.creativeHousingLocationsListScrollAmount - f, 0.0f, 1.0f);
//            this.creativeHousingLocationsListScrollPosition = (int)((double)(this.creativeHousingLocationsListScrollAmount * (float)i));
//        }
//        // TODO team list
//        if (!this.showCreativeTab
//                && this.showChooseTargetIdentifierScreen
//                && this.teleportationMode == TeleporterBlockBlockEntity.TeleportationMode.DUNGEONS
//                && this.visibleDungeonLocationsList.size() > 4
//                && mouseX >= this.x + 7 && mouseX <= this.x + this.backgroundWidth - 61 && mouseY >= this.y + 20 && mouseY <= this.y + 112) {
//            int i = this.visibleDungeonLocationsList.size() - 4;
//            float f = (float)verticalAmount / (float)i;
//            this.visibleDungeonsLocationsListScrollAmount = MathHelper.clamp(this.visibleDungeonsLocationsListScrollAmount - f, 0.0f, 1.0f);
//            this.visibleDungeonsLocationsListScrollPosition = (int)((double)(this.visibleDungeonsLocationsListScrollAmount * (float)i));
//        }
//        if (!this.showCreativeTab
//                && this.showChooseTargetIdentifierScreen
//                && this.teleportationMode == TeleporterBlockBlockEntity.TeleportationMode.HOUSING
//                && this.visibleHousingLocationsList.size() > 4
//                && mouseX >= this.x + 7 && mouseX <= this.x + this.backgroundWidth - 61 && mouseY >= this.y + 20 && mouseY <= this.y + 112) {
//            int i = this.visibleHousingLocationsList.size() - 4;
//            float f = (float)verticalAmount / (float)i;
//            this.visibleHousingLocationsListScrollAmount = MathHelper.clamp(this.visibleHousingLocationsListScrollAmount - f, 0.0f, 1.0f);
//            this.visibleHousingLocationsListScrollPosition = (int)((double)(this.visibleHousingLocationsListScrollAmount * (float)i));
//        }
        return super.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (!this.showCreativeTab && this.currentTab == 2 && (keyCode == GLFW.GLFW_KEY_ENTER || keyCode == GLFW.GLFW_KEY_KP_ENTER)) {
            this.saveAdventure();
            return true;
        }
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
//                context.drawTextWithShadow(this.textRenderer, SHOW_INFLUENCE_AREA_LABEL_TEXT, this.width / 2 - 153, 51, 0xA0A0A0);
                context.drawTextWithShadow(this.textRenderer, INFLUENCE_AREA_DIMENSIONS_LABEL_TEXT, this.width / 2 - 153, 70, 0xA0A0A0);
                this.restrictBlockBreakingAreaDimensionsXField.render(context, mouseX, mouseY, delta);
                this.restrictBlockBreakingAreaDimensionsYField.render(context, mouseX, mouseY, delta);
                this.restrictBlockBreakingAreaDimensionsZField.render(context, mouseX, mouseY, delta);
                context.drawTextWithShadow(this.textRenderer, INFLUENCE_AREA_POSITION_OFFET_LABEL_TEXT, this.width / 2 - 153, 105, 0xA0A0A0);
                this.restrictBlockBreakingAreaPositionOffsetXField.render(context, mouseX, mouseY, delta);
                this.restrictBlockBreakingAreaPositionOffsetYField.render(context, mouseX, mouseY, delta);
                this.restrictBlockBreakingAreaPositionOffsetZField.render(context, mouseX, mouseY, delta);
//                context.drawTextWithShadow(this.textRenderer, ACCESS_POSITION_OFFET_LABEL_TEXT, this.width / 2 - 153, 140, 0xA0A0A0);
//                this.accessPositionOffsetXField.render(context, mouseX, mouseY, delta);
//                this.accessPositionOffsetYField.render(context, mouseX, mouseY, delta);
//                this.accessPositionOffsetZField.render(context, mouseX, mouseY, delta);
//                context.drawTextWithShadow(this.textRenderer, SET_ACCESS_POSITION_LABEL_TEXT, this.width / 2 - 153, 181, 0xA0A0A0);
            } else if (this.creativeScreenPage == CreativeScreenPage.ENTRANCE) {
//                context.drawTextWithShadow(this.textRenderer, TELEPORTATION_MODE_LABEL_TEXT, this.width / 2 - 153, 51, 0xA0A0A0);
//                if (this.teleportationMode == TeleporterBlockBlockEntity.TeleportationMode.DIRECT) {
                    context.drawTextWithShadow(this.textRenderer, ENTRANCE_POSITION_OFFET_LABEL_TEXT, this.width / 2 - 153, 70, 0xA0A0A0);
                    this.entrancePositionOffsetXField.render(context, mouseX, mouseY, delta);
                    this.entrancePositionOffsetYField.render(context, mouseX, mouseY, delta);
                    this.entrancePositionOffsetZField.render(context, mouseX, mouseY, delta);
                    context.drawTextWithShadow(this.textRenderer, ENTRANCE_ORIENTATION_LABEL_TEXT, this.width / 2 - 153, 105, 0xA0A0A0);
                    this.entranceOrientationYawField.render(context, mouseX, mouseY, delta);
                    this.entranceOrientationPitchField.render(context, mouseX, mouseY, delta);
//                } else if (this.teleportationMode == TeleporterBlockBlockEntity.TeleportationMode.SAVED_LOCATIONS) {
//                    context.drawTextWithShadow(this.textRenderer, LOCATION_TYPE_LABEL_TEXT, this.width / 2 - 153, 76, 0xA0A0A0);
//                } else if (this.teleportationMode == TeleporterBlockBlockEntity.TeleportationMode.DUNGEONS) {
//                    for (int i = this.creativeDungeonsLocationsListScrollPosition; i < Math.min(this.creativeDungeonsLocationsListScrollPosition + 3, this.dungeonLocationsList.size()); i++) {
//                        String text = this.dungeonLocationsList.get(i).getLeft();
//                        if (!this.dungeonLocationsList.get(i).getRight().equals("")) {
//                            text = this.dungeonLocationsList.get(i).getLeft() + ", " + this.dungeonLocationsList.get(i).getRight();
//                        }
//                        context.drawTextWithShadow(this.textRenderer, text, this.width / 2 - 141, 76 + ((i - this.creativeDungeonsLocationsListScrollPosition) * 25), 0xA0A0A0);
//                    }
//                    if (this.dungeonLocationsList.size() > 3) {
//                        context.drawGuiTexture(CREATIVE_DUNGEONS_SCROLLER_BACKGROUND_TEXTURE, this.width / 2 - 153, 70, 8, 70);
//                        int k = (int)(61.0f * this.creativeDungeonsLocationsListScrollAmount);
//                        context.drawGuiTexture(SCROLLER_TEXTURE, this.width / 2 - 152, 70 + 1 + k, 6, 7);
//                    }
//                    this.newDungeonLocationIdentifierField.render(context, mouseX, mouseY, delta);
//                    this.newDungeonLocationEntranceField.render(context, mouseX, mouseY, delta);
//                } else if (this.teleportationMode == TeleporterBlockBlockEntity.TeleportationMode.HOUSING) {
//                    for (int i = this.creativeHousingLocationsListScrollPosition; i < Math.min(this.creativeHousingLocationsListScrollPosition + 4, this.housingLocationsList.size()); i++) {
//                        context.drawTextWithShadow(this.textRenderer, this.housingLocationsList.get(i), this.width / 2 - 141, 76 + ((i - this.creativeHousingLocationsListScrollPosition) * 25), 0xA0A0A0);
//                    }
//                    if (this.housingLocationsList.size() > 4) {
//                        context.drawGuiTexture(CREATIVE_HOUSING_SCROLLER_BACKGROUND_TEXTURE, this.width / 2 - 153, 70, 8, 95);
//                        int k = (int)(86.0f * this.creativeHousingLocationsListScrollAmount);
//                        context.drawGuiTexture(SCROLLER_TEXTURE, this.width / 2 - 152, 70 + 1 + k, 6, 7);
//                    }
//                    this.newHousingLocationIdentifierField.render(context, mouseX, mouseY, delta);
//                }
            } else if (this.creativeScreenPage == CreativeScreenPage.TRIGGERED_BLOCK) {
//                context.drawTextWithShadow(this.textRenderer, TELEPORTER_NAME_FIELD_LABEL_TEXT, this.width / 2 - 153, 50, 0xA0A0A0);
//                this.teleporterNameField.render(context, mouseX, mouseY, delta);
//
//                if (!(this.teleportationMode == TeleporterBlockBlockEntity.TeleportationMode.DIRECT || this.teleportationMode == TeleporterBlockBlockEntity.TeleportationMode.SAVED_LOCATIONS)) {

                    context.drawTextWithShadow(this.textRenderer, TRIGGERED_BLOCK_POSITION_OFFSET_LABEL_TEXT, this.width / 2 - 153, 70, 0xA0A0A0);
                    this.triggeredBlockPositionOffsetXField.render(context, mouseX, mouseY, delta);
                    this.triggeredBlockPositionOffsetYField.render(context, mouseX, mouseY, delta);
                    this.triggeredBlockPositionOffsetZField.render(context, mouseX, mouseY, delta);
//                    context.drawTextWithShadow(this.textRenderer, TARGET_IDENTIFIER_FIELD_LABEL_TEXT, this.width / 2 - 153, 98, 0xA0A0A0);
//                    this.currentTargetIdentifierLabelField.render(context, mouseX, mouseY, delta);

//                    if (this.teleportationMode == TeleporterBlockBlockEntity.TeleportationMode.DUNGEONS) {
//
////                        context.drawTextWithShadow(this.textRenderer, TARGET_ENTRANCE_FIELD_LABEL_TEXT, this.width / 2 - 153, 122, 0xA0A0A0);
//                        this.currentTargetEntranceLabelField.render(context, mouseX, mouseY, delta);
//
//                    }
//                }

//                context.drawTextWithShadow(this.textRenderer, TELEPORT_BUTTON_LABEL_TEXT, this.width / 2 - 153, 146, 0xA0A0A0);
//                this.teleportButtonLabelField.render(context, mouseX, mouseY, delta);
////                context.drawTextWithShadow(this.textRenderer, CANCEL_TELEPORT_BUTTON_LABEL_TEXT, this.width / 2 - 153, 170, 0xA0A0A0);
//                this.cancelTeleportButtonLabelField.render(context, mouseX, mouseY, delta);
            }
        } else {
//            context.drawTextWithShadow(this.textRenderer, OWNER_MODE_LABEL_TEXT, this.width / 2 - 153, 74, 0xA0A0A0);
//            context.drawText(this.textRenderer, OWNER_MODE_LABEL_TEXT, this.x + 19, this.y + 26 + ((i - this.visibleDungeonsLocationsListScrollPosition) * 24), 0x404040, false);

//            TeleporterBlockBlockEntity.TeleportationMode mode = this.teleporterBlock.getTeleportationMode();
//
//            if (this.showChooseTargetOwnerScreen) {
//
//            } else if (this.showChooseTargetIdentifierScreen) {
//
//                if (mode == TeleporterBlockBlockEntity.TeleportationMode.DUNGEONS) {
//
//                    for (int i = this.visibleDungeonsLocationsListScrollPosition; i < Math.min(this.visibleDungeonsLocationsListScrollPosition + 4, this.visibleDungeonLocationsList.size()); i++) {
//                        context.drawText(this.textRenderer, this.visibleDungeonLocationsList.get(i).getLeft(), this.x + 19, this.y + 26 + ((i - this.visibleDungeonsLocationsListScrollPosition) * 24), 0x404040, false);
//                    }
//                    if (this.visibleDungeonLocationsList.size() > 4) {
//                        context.drawGuiTexture(CREATIVE_HOUSING_SCROLLER_BACKGROUND_TEXTURE, this.x + 7, this.y + 20, 8, 92);
//                        int k = (int) (83.0f * this.visibleDungeonsLocationsListScrollAmount);
//                        context.drawGuiTexture(SCROLLER_TEXTURE, this.x + 8, this.y + 20 + 1 + k, 6, 7);
//                    }
//
//                } else {
//
//                    for (int i = this.visibleHousingLocationsListScrollPosition; i < Math.min(this.visibleHousingLocationsListScrollPosition + 4, this.visibleHousingLocationsList.size()); i++) {
//                        context.drawText(this.textRenderer, this.visibleHousingLocationsList.get(i), this.x + 19, this.y + 26 + ((i - this.visibleHousingLocationsListScrollPosition) * 24), 0x404040, false);
//                    }
//                    if (this.visibleHousingLocationsList.size() > 4) {
//                        context.drawGuiTexture(CREATIVE_HOUSING_SCROLLER_BACKGROUND_TEXTURE, this.x + 7, this.y + 20, 8, 92);
//                        int k = (int) (83.0f * this.visibleHousingLocationsListScrollAmount);
//                        context.drawGuiTexture(SCROLLER_TEXTURE, this.x + 8, this.y + 20 + 1 + k, 6, 7);
//                    }
//
//                }
//
////            } else if (this.showChooseTargetEntranceScreen) {
//
//            } else if (this.showRegenerationConfirmScreen) {
//
//            } else {
//
//                Text teleporterName = Text.translatable(this.teleporterBlock.getTeleporterName());
//                int teleporterNameOffset = this.backgroundWidth / 2 - this.textRenderer.getWidth(teleporterName) / 2;
//                if (this.currentTargetOwner != null) {
//
//                    context.drawText(this.textRenderer, teleporterName, this.x + teleporterNameOffset, this.y + 7, 0x404040, false);
//
//                    if (!(mode == TeleporterBlockBlockEntity.TeleportationMode.DIRECT || mode == TeleporterBlockBlockEntity.TeleportationMode.SAVED_LOCATIONS)) {
//
//                        context.drawText(this.textRenderer, Text.translatable(this.teleporterBlock.getCurrentTargetOwnerLabel()), this.x + 8, this.y + 20, 0x404040, false);
//
//                        context.drawTexture(currentTargetOwner.getSkinTextures().texture(), this.x + 7, this.y + 39, 8, 8, 8, 8, 8, 8, 64, 64);
//                        context.drawText(this.textRenderer, currentTargetOwner.getProfile().getName(), this.x + 19, this.y + 39, 0x404040, false);
//
//                        context.drawText(this.textRenderer, Text.translatable(this.teleporterBlock.getCurrentTargetIdentifierLabel()), this.x + 8, this.y + 57, 0x404040, false);
//
//                        if (mode == TeleporterBlockBlockEntity.TeleportationMode.DUNGEONS) {
//
//                            if (!Objects.equals(this.currentTargetEntrance, "")) {
//
//                                context.drawText(this.textRenderer, Text.translatable(this.currentTargetEntranceDisplayName), this.x + 8, this.y + 76, 0x404040, false);
//                                context.drawText(this.textRenderer, Text.translatable(this.currentTargetDisplayName), this.x + 8, this.y + 89, 0x404040, false);
//
//                            } else {
//
//                                context.drawText(this.textRenderer, Text.translatable(this.currentTargetDisplayName), this.x + 8, this.y + 83, 0x404040, false);
//
//                            }
////                            context.drawText(this.textRenderer, Text.translatable(this.teleporterBlock.getCurrentTargetEntranceLabel()), this.x + 8, this.y + 94, 0x404040, false);
////
////                            context.drawText(this.textRenderer, "Current Target Location Entrance", this.x + 8, this.y + 113, 0x404040, false);
//
//                        } else if (mode == TeleporterBlockBlockEntity.TeleportationMode.HOUSING) {
//
//                            context.drawText(this.textRenderer, Text.translatable(this.currentTargetDisplayName), this.x + 8, this.y + 83, 0x404040, false);
//
//                        }
//                    }
//                }
//            }
        }
//        context.drawTextWithShadow(this.textRenderer, CONSUME_KEY_ITEMSTACK_LABEL_TEXT, this.width / 2 - 153, 221, 0x404040);
    }

    @Override
    public boolean shouldPause() {
        return false;
    }

//    @Override
//    protected void drawForeground(DrawContext context, int mouseX, int mouseY) {}
//
//    @Override
//    protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
//    }

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
                        this.parseInt(this.entrancePositionOffsetXField.getText()),
                        this.parseInt(this.entrancePositionOffsetYField.getText()),
                        this.parseInt(this.entrancePositionOffsetZField.getText())
                ),
                this.parseDouble(this.entranceOrientationYawField.getText()),
                this.parseDouble(this.entranceOrientationPitchField.getText()),
                new BlockPos(
                        this.parseInt(this.triggeredBlockPositionOffsetXField.getText()),
                        this.parseInt(this.triggeredBlockPositionOffsetYField.getText()),
                        this.parseInt(this.triggeredBlockPositionOffsetZField.getText())
                ),
                this.ownerMode.asString()
        ));
        return true;
    }

    private boolean updateHousingBlockAdventure() {
        if (this.housingBlockBlockEntity != null) {
//            List<String> coOwnerList = new ArrayList<>();
//            for (int i = 0; i < component(FlowLayout.class, "coOwnerListContent").children().size(); i++) {
//                coOwnerList.add((((TextBoxComponent) (((FlowLayout) component(FlowLayout.class, "coOwnerListContent").children().get(i)).children().get(0))).getText()));
//            }
//
//            List<String> trustedList = new ArrayList<>();
//            for (int i = 0; i < component(FlowLayout.class, "trustedListContent").children().size(); i++) {
//                trustedList.add(((TextBoxComponent) (((FlowLayout) component(FlowLayout.class, "trustedListContent").children().get(i)).children().get(0))).getText());
//            }
//
//            List<String> guestList = new ArrayList<>();
//            for (int i = 0; i < component(FlowLayout.class, "guestListContent").children().size(); i++) {
//                guestList.add(((TextBoxComponent) (((FlowLayout) component(FlowLayout.class, "guestListContent").children().get(i)).children().get(0))).getText());
//            }

            ClientPlayNetworking.send(new UpdateHousingBlockAdventurePacket(
                    this.housingBlockBlockEntity.getPos(),
                    this.coOwnerList,
                    this.trustedList,
                    this.guestList
            ));
            return true;
        }
        return false;
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
                false,
                true
        ));
        this.close();
    }

    private void deleteEntry(ButtonComponent buttonComponent) {
        if (buttonComponent.parent() != null) {
            buttonComponent.parent().remove();
        }
    }

    private void addEntry(ButtonComponent buttonComponent) {
        if (buttonComponent.parent() != null) {
            FlowLayout buttonContainer = (FlowLayout) buttonComponent.parent();
            if (buttonContainer.parent() != null) {
                String newEntryString = ((TextBoxComponent) buttonContainer.children().get(0)).getText();
                ((FlowLayout) ((FlowLayout) buttonContainer.parent()).children().get(1)).child(
                        Containers.horizontalFlow(Sizing.content(), Sizing.content())
                                .children(List.of(
                                        Containers.verticalFlow(Sizing.content(), Sizing.content())
                                                .child(
                                                        Components.label(Text.literal(newEntryString))
                                                ),
                                        Components.button(Text.translatable("gui.delete"), this::deleteEntry)
                                ))
                );
            }
        }
    }

    private void resetHouse() {
        if (this.housingBlockBlockEntity != null && this.housingBlockBlockEntity.getOwnerMode() == HousingBlockBlockEntity.OwnerMode.INTERACTION) {
            ClientPlayNetworking.send(new ResetHouseHousingBlockPacket(
                    this.housingBlockBlockEntity.getPos()
            ));
        }
        this.close();
    }

    private void trySetHouseOwner(boolean claim) {
        if (this.housingBlockBlockEntity != null && this.housingBlockBlockEntity.getOwnerMode() == HousingBlockBlockEntity.OwnerMode.INTERACTION && this.client != null && this.client.player != null) {
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
        ENTRANCE("entrance"),
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
