package com.github.theredbrain.bamcore.client.gui.screen.ingame;

import com.github.theredbrain.bamcore.BetterAdventureModeCore;
import com.github.theredbrain.bamcore.block.entity.HousingBlockBlockEntity;
import com.github.theredbrain.bamcore.client.owo.CustomButtonComponent;
import com.github.theredbrain.bamcore.client.owo.ExtendedSurface;
import com.github.theredbrain.bamcore.network.packet.BetterAdventureModeCoreServerPacket;
import com.github.theredbrain.bamcore.registry.ComponentsRegistry;
import com.github.theredbrain.bamcore.registry.StatusEffectsRegistry;
import io.netty.buffer.Unpooled;
import io.wispforest.owo.ui.base.BaseOwoScreen;
import io.wispforest.owo.ui.component.ButtonComponent;
import io.wispforest.owo.ui.component.Components;
import io.wispforest.owo.ui.component.TextBoxComponent;
import io.wispforest.owo.ui.container.Containers;
import io.wispforest.owo.ui.container.FlowLayout;
import io.wispforest.owo.ui.container.ScrollContainer;
import io.wispforest.owo.ui.core.*;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.c2s.play.CustomPayloadC2SPacket;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import net.minecraft.util.Colors;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.glfw.GLFW;

import java.util.List;
import java.util.Set;

@Environment(EnvType.CLIENT)
public class HousingScreen extends BaseOwoScreen<FlowLayout> {
    public static final Identifier INVENTORY_TABS_TEXTURE = BetterAdventureModeCore.identifier("textures/gui/big_tabs.png");
    public static final Identifier INVENTORY_NO_TOP_TEXTURE = BetterAdventureModeCore.identifier("panel/default_no_top");
    public static final Identifier INVENTORY_ONLY_TOP_TEXTURE = BetterAdventureModeCore.identifier("panel/default_only_top");
    @Nullable
    private final HousingBlockBlockEntity housingBlockBlockEntity;
    private final int currentPermissionLevel;
    private final boolean showCreativeTab;
    private boolean showRestrictBlockBreakingArea = false;
    private int currentTab = 0;

    public HousingScreen(@Nullable HousingBlockBlockEntity housingBlockBlockEntity, int currentPermissionLevel, boolean showCreativeTab) {
        this.housingBlockBlockEntity = housingBlockBlockEntity;
        this.currentPermissionLevel = currentPermissionLevel;
        this.showCreativeTab = showCreativeTab;
        if (housingBlockBlockEntity != null) {
            this.showRestrictBlockBreakingArea = housingBlockBlockEntity.getShowRestrictBlockBreakingArea();
        }
    }

    @Override
    public boolean shouldPause() {
        return false;
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

    private void toggleShowRestrictBlockBreakingArea() {
        if (this.showRestrictBlockBreakingArea) {
            this.showRestrictBlockBreakingArea = false;
        } else {
            this.showRestrictBlockBreakingArea = true;
        }
        this.component(ButtonComponent.class, "toggleShowRestrictBlockBreakingAreaButton").setMessage(this.showRestrictBlockBreakingArea ? Text.translatable("gui.housing_block.toggleShowRestrictBlockBreakingAreaButton.on") : Text.translatable("gui.housing_block.toggleShowRestrictBlockBreakingAreaButton.off"));
        this.component(ButtonComponent.class, "toggleShowRestrictBlockBreakingAreaButton").tooltip(this.showRestrictBlockBreakingArea ? Text.translatable("gui.housing_block.toggleShowRestrictBlockBreakingAreaButton.on.tooltip") : Text.translatable("gui.housing_block.toggleShowRestrictBlockBreakingAreaButton.off.tooltip"));
    }

    private void leaveCurrentHouse() {
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
            Vec3i restrictBlockBreakingAreaDimensions = this.housingBlockBlockEntity.getRestrictBlockBreakingAreaDimensions();
            BlockPos restrictBlockBreakingAreaPositionOffset = this.housingBlockBlockEntity.getRestrictBlockBreakingAreaPositionOffset();
            rootComponent.children(List.of(
                    Containers.verticalFlow(Sizing.fixed(250), Sizing.fixed(166))
                            .children(List.of(
                                    // toggle debug mode
                                    Components.button(this.showRestrictBlockBreakingArea ? Text.translatable("gui.housing_block.toggleShowRestrictBlockBreakingAreaButton.on")
                                                            : Text.translatable("gui.housing_block.toggleShowRestrictBlockBreakingAreaButton.off")
                                                    , button -> this.toggleShowRestrictBlockBreakingArea())
                                            .sizing(Sizing.fill(100), Sizing.fixed(20))
                                            .tooltip(this.showRestrictBlockBreakingArea ? Text.translatable("gui.housing_block.toggleShowRestrictBlockBreakingAreaButton.on.tooltip")
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
                                        .id("editHousingButton")
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
                                                                    ))
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

    private boolean updateHousingBlockCreative() {
        PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());

        buf.writeBlockPos(this.housingBlockBlockEntity.getPos());

        buf.writeBoolean(this.showRestrictBlockBreakingArea);

        buf.writeInt(this.parseInt(this.component(TextBoxComponent.class, "restrictBlockBreakingAreaDimensionsX").getText()));
        buf.writeInt(this.parseInt(this.component(TextBoxComponent.class, "restrictBlockBreakingAreaDimensionsY").getText()));
        buf.writeInt(this.parseInt(this.component(TextBoxComponent.class, "restrictBlockBreakingAreaDimensionsZ").getText()));
        buf.writeBlockPos(new BlockPos(
                this.parseInt(this.component(TextBoxComponent.class, "restrictBlockBreakingAreaPositionOffsetX").getText()),
                this.parseInt(this.component(TextBoxComponent.class, "restrictBlockBreakingAreaPositionOffsetY").getText()),
                this.parseInt(this.component(TextBoxComponent.class, "restrictBlockBreakingAreaPositionOffsetZ").getText())
        ));

        this.client.getNetworkHandler().sendPacket(new CustomPayloadC2SPacket(BetterAdventureModeCoreServerPacket.UPDATE_HOUSING_BLOCK_CREATIVE, buf));
        return true;
    }

    private boolean updateHousingBlockAdventure() {
        PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());

        buf.writeBlockPos(this.housingBlockBlockEntity.getPos());

        List<Component> coOwnerList = component(FlowLayout.class, "coOwnerListContent").children();
        int coOwnerListSize = coOwnerList.size();
        buf.writeInt(coOwnerListSize);
        for (int i = 0; i < coOwnerListSize; i++) {
            buf.writeString(((TextBoxComponent)(((FlowLayout)coOwnerList.get(i)).children().get(0))).getText());
        }

        List<Component> trustedList = component(FlowLayout.class, "trustedListContent").children();
        int trustedListSize = trustedList.size();
        buf.writeInt(trustedListSize);
        for (int i = 0; i < trustedListSize; i++) {
            buf.writeString(((TextBoxComponent)(((FlowLayout)trustedList.get(i)).children().get(0))).getText());
        }

        List<Component> guestList = component(FlowLayout.class, "guestListContent").children();
        int guestListSize = guestList.size();
        buf.writeInt(guestListSize);
        for (int i = 0; i < guestListSize; i++) {
            buf.writeString(((TextBoxComponent)(((FlowLayout)guestList.get(i)).children().get(0))).getText());
        }

        this.client.getNetworkHandler().sendPacket(new CustomPayloadC2SPacket(BetterAdventureModeCoreServerPacket.UPDATE_HOUSING_BLOCK_ADVENTURE, buf));
        return true;
    }

    private int parseInt(String string) {
        try {
            return Integer.parseInt(string);
        } catch (NumberFormatException numberFormatException) {
            return 0;
        }
    }

    private void toggleAdventureBuildingEffect() {
        PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());

        buf.writeInt(StatusEffect.getRawId(StatusEffectsRegistry.ADVENTURE_BUILDING_EFFECT));
        buf.writeInt(-1);
        buf.writeInt(0);
        buf.writeBoolean(false);
        buf.writeBoolean(false);
        buf.writeBoolean(false);
        buf.writeBoolean(true);

        if (this.client != null && this.client.getNetworkHandler() != null) {
            this.client.getNetworkHandler().sendPacket(new CustomPayloadC2SPacket(BetterAdventureModeCoreServerPacket.ADD_STATUS_EFFECT_PACKET, buf));
        }
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
}
