package com.github.theredbrain.bamcore.client.gui.screen.ingame;

import com.github.theredbrain.bamcore.BetterAdventureModeCore;
import com.github.theredbrain.bamcore.block.entity.TeleporterBlockBlockEntity;
import com.github.theredbrain.bamcore.network.packet.BetterAdventureModeCoreServerPacket;
import com.github.theredbrain.bamcore.registry.StatusEffectsRegistry;
import com.github.theredbrain.bamcore.screen.TeleporterBlockScreenHandler;
import io.netty.buffer.Unpooled;
import io.wispforest.owo.ui.base.BaseOwoHandledScreen;
import io.wispforest.owo.ui.component.ButtonComponent;
import io.wispforest.owo.ui.component.Components;
import io.wispforest.owo.ui.component.LabelComponent;
import io.wispforest.owo.ui.component.TextBoxComponent;
import io.wispforest.owo.ui.container.*;
import io.wispforest.owo.ui.core.*;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.c2s.play.CustomPayloadC2SPacket;
import net.minecraft.scoreboard.Team;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.StringVisitable;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.glfw.GLFW;

import java.util.List;
import java.util.Optional;

@Environment(value= EnvType.CLIENT)
public class TeleporterBlockScreen extends BaseOwoHandledScreen<FlowLayout, TeleporterBlockScreenHandler> {
//public class TeleporterBlockScreen extends HandledScreen<TeleporterBlockScreenHandler> {
    private static final Text EDIT_TELEPORTER_TITLE = Text.translatable("gui.edit_teleporter_title");
    public static final Identifier ADVENTURE_TELEPORTER_SCREEN_BACKGROUND_TEXTURE = BetterAdventureModeCore.identifier("textures/gui/container/adventure_teleporter_screen.png");
    public static final Identifier CREATIVE_TELEPORTER_SCREEN_BACKGROUND_TEXTURE = BetterAdventureModeCore.identifier("textures/gui/container/creative_teleporter_screen.png");
    public static final Identifier TELEPORTER_SCREEN_UTILITY_TEXTURE = BetterAdventureModeCore.identifier("textures/gui/container/teleporter_screen_util.png");
    private TeleporterBlockScreenHandler handler;
    private TeleporterBlockBlockEntity teleporterBlock;
    private boolean showCreativeTab;
    private boolean showActivationArea;
    private boolean showAdventureScreen;

    /**
     * 0: direct teleport, 1: specific location, 2: dungeon mode, 3: house mode
     */
    private int teleportationMode;

    /**
     * 0: world spawn, 1: player spawn, 2: housing/dungeon access position
     */
    private int specificLocationType;
    private boolean consumeKeyItemStack;

    public TeleporterBlockScreen(TeleporterBlockScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
        this.handler = handler;
        this.showCreativeTab = handler.getShowCreativeTab();
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

    private void openDungeonRegenerationConfirmScreen() {

        this.uiAdapter.rootComponent.childById(FlowLayout.class, "adventure_container").remove();

        this.uiAdapter.rootComponent.child(
                Containers.overlay(Containers.verticalFlow(Sizing.fill(50), Sizing.content())
                        .children(List.of(
                                Components.label(Text.translatable("gui.teleporter_block.regeneration_confirm_screen.title"))
                                        .shadow(true)
                                        .color(Color.ofArgb(0xFFFFFF))
                                        .margins(Insets.of(0, 4, 0, 0))
                                        .sizing(Sizing.content(), Sizing.content()),
                                Containers.horizontalFlow(Sizing.fill(100), Sizing.content())
                                        .children(List.of(
                                                Containers.horizontalFlow(Sizing.fill(50), Sizing.content())
                                                        .child(
                                                                Components.button(Text.translatable("gui.teleporter_block.confirmDungeonRegenerationButton"), button -> this.confirmDungeonRegeneration())
                                                                        .sizing(Sizing.fill(90), Sizing.fixed(20))
//                                                                        .tooltip(Text.translatable("gui.teleporter_block.confirmDungeonRegenerationButton.tooltip"))
                                                                        .id("confirmDungeonRegenerationButton")
                                                        )
                                                        .verticalAlignment(VerticalAlignment.CENTER)
                                                        .horizontalAlignment(HorizontalAlignment.CENTER),
                                                Containers.horizontalFlow(Sizing.fill(50), Sizing.content())
                                                        .child(
                                                                Components.button(Text.translatable("gui.teleporter_block.cancelDungeonRegenerationButton"), button -> this.cancelDungeonRegeneration())
                                                                        .sizing(Sizing.fill(90), Sizing.fixed(20))
//                                                                        .tooltip(Text.translatable("gui.teleporter_block.cancelDungeonRegenerationButton.tooltip"))
                                                                        .id("cancelDungeonRegenerationButton")
                                                        )
                                                        .verticalAlignment(VerticalAlignment.CENTER)
                                                        .horizontalAlignment(HorizontalAlignment.CENTER)
                                        ))
                                        .verticalAlignment(VerticalAlignment.CENTER)
                                        .horizontalAlignment(HorizontalAlignment.CENTER)
                        ))
                        .surface(Surface.PANEL)
                        .padding(Insets.of(8, 8, 8, 8))
                        .verticalAlignment(VerticalAlignment.CENTER)
                        .horizontalAlignment(HorizontalAlignment.CENTER)
                )
                .closeOnClick(false)
                .sizing(Sizing.fill(100))
                .id("dungeonRegenerationConfirmScreen")
        );
    }

    private void confirmDungeonRegeneration() {
        if (this.tryDungeonRegeneration()) {
            component(OverlayContainer.class, "dungeonRegenerationConfirmScreen").remove();
            this.buildAdventureModeScreen();
        }
    }

    private void cancelDungeonRegeneration() {
        component(OverlayContainer.class, "dungeonRegenerationConfirmScreen").remove();
        this.buildAdventureModeScreen();
    }

    private void updateCurrentTeleportationTargetEntry(PlayerListEntry clientPlayer, PlayerListEntry newTeleportationTargetPlayer) {

        component(FlowLayout.class, "currentTeleportationTargetEntryContainer").clearChildren();

        component(FlowLayout.class, "currentTeleportationTargetEntryContainer").children(List.of(
                Components.texture(newTeleportationTargetPlayer.getSkinTexture(), 8, 8, 8, 8, 64, 64)
                        .sizing(Sizing.fixed(16), Sizing.fixed(16)),
                Components.label(Text.of(newTeleportationTargetPlayer.getProfile().getName()))
                        .shadow(true)
                        .color(Color.ofArgb(0xFFFFFF))
                        .margins(Insets.of(1, 1, 1, 1))
                        .sizing(Sizing.content(), Sizing.content())
                        .id("currentTeleportationTargetEntryLabel")
        ));

//        if (clientPlayer != newTeleportationTargetPlayer) {
//            component(ButtonComponent.class, "regenerateDungeonButton").active = false;
//        } else {
//            component(ButtonComponent.class, "regenerateDungeonButton").active = ComponentsRegistry.PLAYER_SPECIFIC_DIMENSION_IDS.get(this.client.getServer().getPlayerManager().getPlayer(clientPlayer.getProfile().getId())).getStatus(this.teleporterBlock.getOutgoingTeleportDimension());
//        }
    }

    private void done() {
        if (this.updateTeleporterBlock()) {
            this.close();
        }
    }

    private void cancel() {
        this.teleporterBlock.setShowActivationArea(this.showActivationArea);
        this.teleporterBlock.setTeleportationMode(this.teleportationMode);
        this.teleporterBlock.setSpecificLocationType(this.specificLocationType);
        this.teleporterBlock.setConsumeKeyItemStack(this.consumeKeyItemStack);
        this.close();
    }

    @Override
    public void close() {
        this.givePortalResistanceEffect();
        super.close();
    }

    private void toggleShowActivationArea() {
        if (this.showActivationArea) {
            this.showActivationArea = false;
        } else {
            this.showActivationArea = true;
        }
        this.component(ButtonComponent.class, "toggleShowActivationAreaButton").setMessage(this.showActivationArea ? Text.translatable("gui.teleporter_block.toggleShowActivationAreaButton.on") : Text.translatable("gui.teleporter_block.toggleShowActivationAreaButton.off"));
        this.component(ButtonComponent.class, "toggleShowActivationAreaButton").tooltip(this.showActivationArea ? Text.translatable("gui.teleporter_block.toggleShowActivationAreaButton.on.tooltip") : Text.translatable("gui.teleporter_block.toggleShowActivationAreaButton.off.tooltip"));
    }

    private void toggleShowAdventureScreen() {
        if (this.showAdventureScreen) {
            this.showAdventureScreen = false;
        } else {
            this.showAdventureScreen = true;
        }
        this.component(ButtonComponent.class, "toggleShowAdventureScreenButton").setMessage(this.showAdventureScreen ? Text.translatable("gui.teleporter_block.toggleShowAdventureScreenButton.on") : Text.translatable("gui.teleporter_block.toggleShowAdventureScreenButton.off"));
        this.component(ButtonComponent.class, "toggleShowAdventureScreenButton").tooltip(this.showAdventureScreen ? Text.translatable("gui.teleporter_block.toggleShowAdventureScreenButton.on.tooltip") : Text.translatable("gui.teleporter_block.toggleShowAdventureScreenButton.off.tooltip"));
    }

    private void switchTeleportationMode() {
        if (this.teleportationMode == 0) {
            this.teleportationMode = 1;
        } else if (this.teleportationMode == 1) {
            this.teleportationMode = 2;
        } else if (this.teleportationMode == 2) {
            this.teleportationMode = 3;
        } else {
            this.teleportationMode = 0;
        }
        this.component(ButtonComponent.class, "switchTeleportationModeButton")
                .setMessage(this.teleportationMode == 1 ? Text.translatable("gui.teleporter_block.switchTeleportationModeButton.1")
                        : this.teleportationMode == 2 ? Text.translatable("gui.teleporter_block.switchTeleportationModeButton.2")
                        : this.teleportationMode == 3 ? Text.translatable("gui.teleporter_block.switchTeleportationModeButton.3")
                        : Text.translatable("gui.teleporter_block.switchTeleportationModeButton.0"));
        this.component(ButtonComponent.class, "switchTeleportationModeButton")
                .tooltip(this.teleportationMode == 1 ? Text.translatable("gui.teleporter_block.switchTeleportationModeButton.1.tooltip")
                        : this.teleportationMode == 2 ? Text.translatable("gui.teleporter_block.switchTeleportationModeButton.2.tooltip")
                        : this.teleportationMode == 3 ? Text.translatable("gui.teleporter_block.switchTeleportationModeButton.3.tooltip")
                        : Text.translatable("gui.teleporter_block.switchTeleportationModeButton.0.tooltip"));
        buildTeleportationModeSettings();
    }

    private void addLocationToLocationList(String identifier, String entrance) {
        this.component(FlowLayout.class, "locationsListContainer").child(
                Containers.horizontalFlow(Sizing.fill(100), Sizing.content())
                        .children(List.of(
                                Components.label(Text.of(identifier))
                                        .shadow(true)
                                        .color(Color.ofArgb(0xFFFFFF))
                                        .margins(Insets.of(1, 1, 1, 1))
                                        .sizing(Sizing.fill(40), Sizing.content()),
                                Components.label(Text.of(entrance))
                                        .shadow(true)
                                        .color(Color.ofArgb(0xFFFFFF))
                                        .margins(Insets.of(1, 1, 1, 1))
                                        .sizing(Sizing.fill(40), Sizing.content()),
                                Components.button(Text.translatable("gui.teleporter_block.deleteLocationsListEntryButton"), button -> button.parent().remove())
                                        .sizing(Sizing.fill(20), Sizing.content())
                        ))
        );
    }

    private void switchSpecificLocationType() {
        if (this.teleportationMode == 3) {
            if (this.specificLocationType == 0) {
                this.specificLocationType = 1;
            } else if (this.specificLocationType == 1) {
                this.specificLocationType = 2;
            } else {
                this.specificLocationType = 0;
            }
            this.component(ButtonComponent.class, "switchSpecificLocationTypeButton")
                    .setMessage(this.specificLocationType == 1 ? Text.translatable("gui.teleporter_block.switchSpecificLocationTypeButton.1")
                            : this.specificLocationType == 2 ? Text.translatable("gui.teleporter_block.switchSpecificLocationTypeButton.2")
                            : Text.translatable("gui.teleporter_block.switchSpecificLocationTypeButton.0"));
            this.component(ButtonComponent.class, "switchSpecificLocationTypeButton")
                    .tooltip(this.specificLocationType == 1 ? Text.translatable("gui.teleporter_block.switchSpecificLocationTypeButton.1.tooltip")
                            : this.specificLocationType == 2 ? Text.translatable("gui.teleporter_block.switchSpecificLocationTypeButton.2.tooltip")
                            : Text.translatable("gui.teleporter_block.switchSpecificLocationTypeButton.0.tooltip"));
        }
    }

    private void toggleConsumeKeyItemStack() {
        if (this.consumeKeyItemStack) {
            this.consumeKeyItemStack = false;
        } else {
            this.consumeKeyItemStack = true;
        }
        this.component(ButtonComponent.class, "consumeKeyItemStackButton").setMessage(this.consumeKeyItemStack ? Text.translatable("gui.teleporter_block.keyItemButton.yes") : Text.translatable("gui.teleporter_block.keyItemButton.no"));
    }
    //endregion button callbacks

    @Override
    protected @NotNull OwoUIAdapter<FlowLayout> createAdapter() {
        return OwoUIAdapter.create(this, Containers::verticalFlow);
    }

    @Override
    protected void build(FlowLayout rootComponent) {
        Vec3i activationAreaDimensions = this.teleporterBlock.getActivationAreaDimensions();
        BlockPos activationAreaPositionOffset = this.teleporterBlock.getActivationAreaPositionOffset();
        rootComponent
                .surface(Surface.VANILLA_TRANSLUCENT)
                .horizontalAlignment(HorizontalAlignment.CENTER)
                .verticalAlignment(VerticalAlignment.CENTER)
                .id("root");
        if (this.showCreativeTab) {
        //region creative mode screen
                rootComponent.child(Containers.verticalFlow(Sizing.fill(100), Sizing.content())
                        .children(List.of(
                                // screen title
                                Components.label(Text.translatable("gui.edit_teleporter_title"))
                                        .shadow(true)
                                        .color(Color.ofArgb(0xFFFFFF))
                                        .margins(Insets.of(1, 1, 1, 1))
                                        .sizing(Sizing.content(), Sizing.content()),
                                // scrolling container
                                Containers.verticalScroll(Sizing.fill(50), Sizing.fill(50),
                                        Containers.verticalFlow(Sizing.fill(98), Sizing.content())
                                                .children(List.of(
                                                        // toggle debug mode
                                                        Components.button(this.showActivationArea ? Text.translatable("gui.teleporter_block.toggleShowActivationAreaButton.on")
                                                                                : Text.translatable("gui.teleporter_block.toggleShowActivationAreaButton.off")
                                                                        , button -> this.toggleShowActivationArea())
                                                                .sizing(Sizing.fill(100), Sizing.fixed(20))
                                                                .tooltip(this.showActivationArea ? Text.translatable("gui.teleporter_block.toggleShowActivationAreaButton.on.tooltip")
                                                                        : Text.translatable("gui.teleporter_block.toggleShowActivationAreaButton.off.tooltip"))
                                                                .id("toggleShowActivationAreaButton"),

                                                        // activation area
                                                        Containers.horizontalFlow(Sizing.fill(100), Sizing.content())
                                                                .children(List.of(
                                                                        Components.textBox(Sizing.fill(32), Integer.toString(activationAreaDimensions.getX()))
                                                                                .tooltip(Text.translatable("gui.teleporter_block.activationAreaDimensionsX.tooltip"))
                                                                                .margins(Insets.of(1, 1, 1, 1))
                                                                                .id("activationAreaDimensionsX"),
                                                                        Components.textBox(Sizing.fill(32), Integer.toString(activationAreaDimensions.getY()))
                                                                                .tooltip(Text.translatable("gui.teleporter_block.activationAreaDimensionsY.tooltip"))
                                                                                .margins(Insets.of(1, 1, 1, 1))
                                                                                .id("activationAreaDimensionsY"),
                                                                        Components.textBox(Sizing.fill(32), Integer.toString(activationAreaDimensions.getZ()))
                                                                                .tooltip(Text.translatable("gui.teleporter_block.activationAreaDimensionsZ.tooltip"))
                                                                                .margins(Insets.of(1, 1, 1, 1))
                                                                                .id("activationAreaDimensionsZ")
                                                                ))
                                                                .verticalAlignment(VerticalAlignment.CENTER)
                                                                .horizontalAlignment(HorizontalAlignment.CENTER),
                                                        Containers.horizontalFlow(Sizing.fill(100), Sizing.content())
                                                                .children(List.of(
                                                                        Components.textBox(Sizing.fill(32), Integer.toString(activationAreaPositionOffset.getX()))
                                                                                .tooltip(Text.translatable("gui.teleporter_block.activationAreaPositionOffsetX.tooltip"))
                                                                                .margins(Insets.of(1, 1, 1, 1))
                                                                                .id("activationAreaPositionOffsetX"),
                                                                        Components.textBox(Sizing.fill(32), Integer.toString(activationAreaPositionOffset.getY()))
                                                                                .tooltip(Text.translatable("gui.teleporter_block.activationAreaPositionOffsetY.tooltip"))
                                                                                .margins(Insets.of(1, 1, 1, 1))
                                                                                .id("activationAreaPositionOffsetY"),
                                                                        Components.textBox(Sizing.fill(32), Integer.toString(activationAreaPositionOffset.getZ()))
                                                                                .tooltip(Text.translatable("gui.teleporter_block.activationAreaPositionOffsetZ.tooltip"))
                                                                                .margins(Insets.of(1, 1, 1, 1))
                                                                                .id("activationAreaPositionOffsetZ")
                                                                ))
                                                                .verticalAlignment(VerticalAlignment.CENTER)
                                                                .horizontalAlignment(HorizontalAlignment.CENTER),

                                                        // showAdventureScreen switch
                                                        Components.button(this.showAdventureScreen ? Text.translatable("gui.teleporter_block.toggleShowAdventureScreenButton.on")
                                                                                : Text.translatable("gui.teleporter_block.toggleShowAdventureScreenButton.off"),
                                                                        button -> this.toggleShowAdventureScreen())
                                                                .sizing(Sizing.fill(100), Sizing.fixed(20))
                                                                .tooltip(this.showAdventureScreen ? Text.translatable("gui.teleporter_block.toggleShowAdventureScreenButton.on.tooltip")
                                                                        : Text.translatable("gui.teleporter_block.toggleShowAdventureScreenButton.off.tooltip"))
                                                                .id("toggleShowAdventureScreenButton"),

                                                        // teleportation mode
                                                        Components.button(this.teleportationMode == 1 ? Text.translatable("gui.teleporter_block.switchTeleportationModeButton.1")
                                                                                : this.teleportationMode == 2 ? Text.translatable("gui.teleporter_block.switchTeleportationModeButton.2")
                                                                                : this.teleportationMode == 3 ? Text.translatable("gui.teleporter_block.switchTeleportationModeButton.3")
                                                                                : Text.translatable("gui.teleporter_block.switchTeleportationModeButton.0")
                                                                        , button -> this.switchTeleportationMode())
                                                                .sizing(Sizing.fill(100), Sizing.fixed(20))
                                                                .tooltip(this.teleportationMode == 1 ? Text.translatable("gui.teleporter_block.switchTeleportationModeButton.1.tooltip")
                                                                        : this.teleportationMode == 2 ? Text.translatable("gui.teleporter_block.switchTeleportationModeButton.2.tooltip")
                                                                        : this.teleportationMode == 3 ? Text.translatable("gui.teleporter_block.switchTeleportationModeButton.3.tooltip")
                                                                        : Text.translatable("gui.teleporter_block.switchTeleportationModeButton.0.tooltip"))
                                                                .id("switchTeleportationModeButton"),
                                                        Containers.verticalFlow(Sizing.content(), Sizing.content())
                                                                .id("teleportationModeSettingsContainer"),

                                                        // adventure screen customization
                                                        Components.textBox(Sizing.fill(100), this.teleporterBlock.getTeleporterName())
                                                                .tooltip(Text.translatable("gui.teleporter_block.teleporterName.tooltip"))
                                                                .margins(Insets.of(1, 1, 1, 1))
                                                                .id("teleporterName"),
                                                        Components.textBox(Sizing.fill(100), this.teleporterBlock.getTeleportButtonLabel())
                                                                .tooltip(Text.translatable("gui.teleporter_block.teleportButtonLabel.tooltip"))
                                                                .margins(Insets.of(1, 1, 1, 1))
                                                                .id("teleportButtonLabel"),
                                                        Components.textBox(Sizing.fill(100), this.teleporterBlock.getCancelTeleportButtonLabel())
                                                                .tooltip(Text.translatable("gui.teleporter_block.cancelTeleportButtonLabel.tooltip"))
                                                                .margins(Insets.of(1, 1, 1, 1))
                                                                .id("cancelTeleportButtonLabel"),

                                                        slotAsComponent(37),
                                                        Components.button(this.consumeKeyItemStack ? Text.translatable("gui.teleporter_block.keyItemButton.yes")
                                                                                : Text.translatable("gui.teleporter_block.keyItemButton.no"),
                                                                        button -> this.toggleConsumeKeyItemStack())
                                                                .sizing(Sizing.fill(100), Sizing.fixed(20))
                                                                .tooltip(this.consumeKeyItemStack ? Text.translatable("gui.teleporter_block.keyItemButton.yes.tooltip")
                                                                        : Text.translatable("gui.teleporter_block.keyItemButton.no.tooltip"))
                                                                .id("consumeKeyItemStackButton"),
                                                        Containers.grid(Sizing.content(), Sizing.content(), 3, 9)
                                                                .id("playerInventorySlots_creative")
                                                        ))
                                                .verticalAlignment(VerticalAlignment.CENTER)
                                                .horizontalAlignment(HorizontalAlignment.CENTER)
                                                .id("creativeScreenScrollingContent"))
                                        .scrollbar(ScrollContainer.Scrollbar.vanillaFlat())
                                        .verticalAlignment(VerticalAlignment.CENTER)
                                        .horizontalAlignment(HorizontalAlignment.LEFT),

                                // save/close buttons
                                Containers.horizontalFlow(Sizing.fill(30), Sizing.content())
                                        .children(List.of(
                                                Components.button(Text.translatable("gui.save"), button -> this.done())
                                                        .sizing(Sizing.fill(49), Sizing.fixed(20))
                                                        .id("doneButton"),
                                                Components.button(ScreenTexts.CANCEL, button -> this.cancel())
                                                        .sizing(Sizing.fill(49), Sizing.fixed(20))
                                                        .id("cancelButton")
                                        ))
                                        .margins(Insets.of(1, 1, 1, 1))
                                        .verticalAlignment(VerticalAlignment.CENTER)
                                        .horizontalAlignment(HorizontalAlignment.CENTER)
                        ))
                        .verticalAlignment(VerticalAlignment.CENTER)
                        .horizontalAlignment(HorizontalAlignment.CENTER)
                        .id("creative_container")
                );
            buildTeleportationModeSettings();

            for (int i = 0; i < 3; ++i) {
                for (int j = 0; j < 9; ++j) {
                    component(GridLayout.class, "playerInventorySlots_creative").child(
                            slotAsComponent(j + (i + 1) * 9)
                                    .margins(Insets.of(1, 1, 1, 1)),
                            i,
                            j);
                }
            }
            disableSlot(36);
            disableSlot(38);
        //endregion creative mode screen
        } else {
        //region adventure mode screen

            if (!this.showAdventureScreen && (this.teleportationMode == 0 || this.teleportationMode == 1)) {
                this.teleport();
            } else {
                rootComponent.child(Containers.verticalFlow(Sizing.fill(100), Sizing.content())
                        .verticalAlignment(VerticalAlignment.CENTER)
                        .horizontalAlignment(HorizontalAlignment.CENTER)
                        .id("adventure_container"));
                this.buildAdventureModeScreen();
            }
        //endregion adventure mode screen
        }
    }

    //region build screen components
    private void buildAdventureModeScreen() {
        this.component(FlowLayout.class, "adventure_container").clearChildren();
        disableSlot(36);
        disableSlot(37);
        disableSlot(38);

        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                disableSlot(j + (i + 1) * 9);
            }
        }

        if (this.teleporterBlock.getTeleportationMode() == 2) {
            //region dungeon mode
            this.component(FlowLayout.class, "adventure_container").child(Containers.verticalFlow(Sizing.fill(100), Sizing.content())
                    .children(List.of(
                            Components.label(Text.translatable(this.teleporterBlock.getTeleporterName()))
                                    .shadow(true)
                                    .color(Color.ofArgb(0xFFFFFF))
                                    .margins(Insets.of(1, 1, 1, 1))
                                    .sizing(Sizing.content(), Sizing.content())
                                    .id("adventureScreenTitle"),
                            Containers.verticalFlow(Sizing.fill(100), Sizing.content())
                                    .id("playerListContainer"),
                            Containers.verticalFlow(Sizing.fill(100), Sizing.content())
                                    .verticalAlignment(VerticalAlignment.CENTER)
                                    .horizontalAlignment(HorizontalAlignment.CENTER)
                                    .id("keyItemContainer"),
                            Components.button(Text.translatable("gui.teleporter_block.regenerateDungeonButton"), button -> this.openDungeonRegenerationConfirmScreen())
                                    .sizing(Sizing.fill(25), Sizing.content())
                                    .id("regenerateDungeonButton"),
                            Containers.horizontalFlow(Sizing.fill(30), Sizing.content())
                                    .children(List.of(
                                            Components.button(Text.translatable(this.teleporterBlock.getTeleportButtonLabel()), button -> this.teleport())
                                                    .sizing(Sizing.fill(49), Sizing.content())
                                                    .id("teleportButton"),
                                            Components.button(Text.translatable(this.teleporterBlock.getCancelTeleportButtonLabel()), button -> this.cancelTeleport())
                                                    .sizing(Sizing.fill(49), Sizing.content())
                                                    .id("cancelTeleportButton")
                                    ))
                                    .margins(Insets.of(1, 1, 1, 1))
                                    .verticalAlignment(VerticalAlignment.CENTER)
                                    .horizontalAlignment(HorizontalAlignment.CENTER)
                                    .id("adventureScreenButtons")
                    ))
            );
            this.buildPlayerList();

            if (!handler.slots.get(37).getStack().isEmpty()) {
                BetterAdventureModeCore.LOGGER.info("slots enabled");
                enableSlot(36);
                enableSlot(38);
                component(FlowLayout.class, "keyItemContainer").children(List.of(
                        Containers.horizontalFlow(Sizing.fill(100), Sizing.content()).children(List.of(
                                        slotAsComponent(38),
                                        slotAsComponent(36)
                                ))
                                .margins(Insets.of(1, 1, 1, 1))
                                .verticalAlignment(VerticalAlignment.CENTER)
                                .horizontalAlignment(HorizontalAlignment.CENTER)
                                .id("keyItemSlots"),
                        Containers.grid(Sizing.content(), Sizing.content(), 3, 9)
                                .padding(Insets.of(4))
                                .id("playerInventorySlots_adventure")
                ));
                for (int i = 0; i < 3; ++i) {
                    for (int j = 0; j < 9; ++j) {
                        enableSlot(j + (i + 1) * 9);
                        component(GridLayout.class, "playerInventorySlots_adventure").child(
                                        slotAsComponent(j + (i + 1) * 9)
                                                .margins(Insets.of(1, 1, 1, 1)),
                                        i,
                                        j)
                                .id("slot_" + i + "_" + j);
                    }
                }
            }
            //endregion
        } else if (this.teleporterBlock.getTeleportationMode() == 3) {
            //region house mode
            this.component(FlowLayout.class, "adventure_container").child(Containers.verticalFlow(Sizing.fill(100), Sizing.content())
                    .children(List.of(
                            Components.label(Text.translatable(this.teleporterBlock.getTeleporterName()))
                                    .shadow(true)
                                    .color(Color.ofArgb(0xFFFFFF))
                                    .margins(Insets.of(1, 1, 1, 1))
                                    .sizing(Sizing.content(), Sizing.content())
                                    .id("adventureScreenTitle"),
                            Containers.verticalFlow(Sizing.fill(100), Sizing.content())
                                    .id("playerListContainer"),
                            Containers.horizontalFlow(Sizing.fill(30), Sizing.content())
                                    .children(List.of(
                                            Components.button(Text.translatable(this.teleporterBlock.getTeleportButtonLabel()), button -> this.teleport())
                                                    .sizing(Sizing.fill(49), Sizing.content())
                                                    .id("teleportButton"),
                                            Components.button(Text.translatable(this.teleporterBlock.getCancelTeleportButtonLabel()), button -> this.cancelTeleport())
                                                    .sizing(Sizing.fill(49), Sizing.content())
                                                    .id("cancelTeleportButton")
                                    ))
                                    .margins(Insets.of(1, 1, 1, 1))
                                    .verticalAlignment(VerticalAlignment.CENTER)
                                    .horizontalAlignment(HorizontalAlignment.CENTER)
                                    .id("adventureScreenButtons")
                    ))
            );
            this.buildPlayerList();
            //endregion
        } else {
            //region static dimension mode
            this.component(FlowLayout.class, "adventure_container").child(Containers.verticalFlow(Sizing.fill(100), Sizing.content())
                    .children(List.of(
                            Components.label(Text.translatable(this.teleporterBlock.getTeleporterName()))
                                    .shadow(true)
                                    .color(Color.ofArgb(0xFFFFFF))
                                    .margins(Insets.of(1, 1, 1, 1))
                                    .sizing(Sizing.content(), Sizing.content())
                                    .id("adventureScreenTitle"),
                            Containers.horizontalFlow(Sizing.fill(30), Sizing.content())
                                    .children(List.of(
                                            Components.button(Text.translatable(this.teleporterBlock.getTeleportButtonLabel()), button -> this.teleport())
                                                    .sizing(Sizing.fill(49), Sizing.content())
                                                    .id("teleportButton"),
                                            Components.button(Text.translatable(this.teleporterBlock.getCancelTeleportButtonLabel()), button -> this.cancelTeleport())
                                                    .sizing(Sizing.fill(49), Sizing.content())
                                                    .id("cancelTeleportButton")
                                    ))
                                    .margins(Insets.of(1, 1, 1, 1))
                                    .verticalAlignment(VerticalAlignment.CENTER)
                                    .horizontalAlignment(HorizontalAlignment.CENTER)
                                    .id("adventureScreenButtons")
                    ))
                    .verticalAlignment(VerticalAlignment.CENTER)
                    .horizontalAlignment(HorizontalAlignment.CENTER)
                    .id("adventure_container")
            );
            //endregion
        }
    }

    private void buildPlayerList() {
        this.component(FlowLayout.class, "playerListContainer").children(List.of(
                Components.label(Text.translatable("gui.teleporter_block.currentTeleportationTargetEntry.label"))
                        .shadow(true)
                        .color(Color.ofArgb(0xFFFFFF))
                        .margins(Insets.of(1, 1, 1, 1))
                        .sizing(Sizing.content(), Sizing.content())
                        .id("currentTeleportationTargetEntryLabel"),
                Containers.horizontalFlow(Sizing.fixed(200), Sizing.content())
                        .verticalAlignment(VerticalAlignment.CENTER)
                        .horizontalAlignment(HorizontalAlignment.CENTER)
                        .id("currentTeleportationTargetEntryContainer"),
                Containers.verticalScroll(Sizing.fixed(200), Sizing.fixed(100), Containers.verticalFlow(Sizing.fill(100), Sizing.fill(100))
                                .children(List.of(

                                        Containers.horizontalFlow(Sizing.fill(100), Sizing.content())
                                                .verticalAlignment(VerticalAlignment.CENTER)
                                                .horizontalAlignment(HorizontalAlignment.CENTER)
                                                .id("currentPlayerTargetEntryContainer"),
                                        Containers.verticalFlow(Sizing.fill(100), Sizing.content())
                                                .verticalAlignment(VerticalAlignment.CENTER)
                                                .horizontalAlignment(HorizontalAlignment.CENTER)
                                                .id("currentTeamTargetEntries")
                                ))
                                .verticalAlignment(VerticalAlignment.CENTER)
                                .horizontalAlignment(HorizontalAlignment.CENTER)
                                .id("player_list_scroll_container_content")
                        )
                        .verticalAlignment(VerticalAlignment.CENTER)
                        .horizontalAlignment(HorizontalAlignment.CENTER)
                        .id("player_list_scroll_container")
        ));
        this.populatePlayerList();
    }

    private void populatePlayerList() {
        if (this.client != null && this.client.player != null) {

            PlayerListEntry currentPlayer = this.client.player.networkHandler.getPlayerListEntry(this.client.player.getUuid());

            if (currentPlayer != null) {

                component(FlowLayout.class, "currentPlayerTargetEntryContainer").children(List.of(
                        Components.texture(currentPlayer.getSkinTexture(), 8, 8, 8, 8, 64, 64)
                                .sizing(Sizing.fixed(16), Sizing.fixed(16)),
                        Components.label(Text.of(currentPlayer.getProfile().getName()))
                                .shadow(true)
                                .color(Color.ofArgb(0xFFFFFF))
                                .margins(Insets.of(1, 1, 1, 1))
                                .sizing(Sizing.content(), Sizing.content())
                                .id("currentTeleportationTargetEntryLabel"),
                        Components.button(Text.translatable("gui.teleporter_block.updateCurrentTeleportationTargetEntryButton"), button -> this.updateCurrentTeleportationTargetEntry(currentPlayer, currentPlayer))
                                .sizing(Sizing.fill(50), Sizing.content())
                                .id("updateCurrentTeleportationTargetEntryButton")
                ));

                Team currentPlayerTeam = currentPlayer.getScoreboardTeam();

                if (currentPlayerTeam != null) {
                    PlayerListEntry teamPlayer;
                    for (String player : currentPlayerTeam.getPlayerList()) {
                        teamPlayer = this.client.player.networkHandler.getPlayerListEntry(player);
                        if (teamPlayer != null && currentPlayer != teamPlayer) {
//                            RPGMod.LOGGER.info(player);
                            PlayerListEntry finalTeamPlayer = teamPlayer;
                            component(CollapsibleContainer.class, "currentTeamTargetEntries")
                                    .child(Containers.horizontalFlow(Sizing.fill(100), Sizing.content())
                                            .children(List.of(
                                                    Components.texture(teamPlayer.getSkinTexture(), 8, 8, 8, 8, 64, 64)
                                                            .sizing(Sizing.fixed(16), Sizing.fixed(16)),
                                                    Components.label(Text.of(teamPlayer.getProfile().getName()))
                                                            .shadow(true)
                                                            .color(Color.ofArgb(0xFFFFFF))
                                                            .margins(Insets.of(1, 1, 1, 1))
                                                            .sizing(Sizing.fill(25), Sizing.content())
                                                            .id("currentTeleportationTargetEntryLabel"),
                                                    Components.button(Text.translatable("gui.teleporter_block.updateCurrentTeleportationTargetEntryButton"), button -> this.updateCurrentTeleportationTargetEntry(currentPlayer, finalTeamPlayer))
                                                            .sizing(Sizing.fill(50), Sizing.content())
                                                            .id("updateCurrentTeleportationTargetEntryButton")
                                            ))
                                            .verticalAlignment(VerticalAlignment.CENTER)
                                            .horizontalAlignment(HorizontalAlignment.CENTER));
                        }
                    }
                }
                this.updateCurrentTeleportationTargetEntry(currentPlayer, currentPlayer);
            }
        }
    }

    private void buildTeleportationModeSettings() {
        this.component(FlowLayout.class, "teleportationModeSettingsContainer").clearChildren();
        if (this.teleportationMode == 1) {
            this.component(FlowLayout.class, "teleportationModeSettingsContainer")
                    .children(List.of(
                            Components.button(this.specificLocationType == 1 ? Text.translatable("gui.teleporter_block.switchSpecificLocationTypeButton.1")
                                                    : this.specificLocationType == 2 ? Text.translatable("gui.teleporter_block.switchSpecificLocationTypeButton.2")
                                                    : Text.translatable("gui.teleporter_block.switchSpecificLocationTypeButton.0")
                                            , button -> this.switchSpecificLocationType())
                                    .sizing(Sizing.fill(100), Sizing.fixed(20))
                                    .tooltip(this.specificLocationType == 1 ? Text.translatable("gui.teleporter_block.switchSpecificLocationTypeButton.1.tooltip")
                                            : this.specificLocationType == 2 ? Text.translatable("gui.teleporter_block.switchSpecificLocationTypeButton.2.tooltip")
                                            : Text.translatable("gui.teleporter_block.switchSpecificLocationTypeButton.0.tooltip"))
                                    .id("switchSpecificLocationTypeButton")
                    ));
        } else if (this.teleportationMode == 2 || this.teleportationMode == 3) {
            this.component(FlowLayout.class, "teleportationModeSettingsContainer")
                    .children(List.of(
                            Containers.horizontalFlow(Sizing.fill(100), Sizing.content())
                                    .id("locationsListContainer"),
                            Containers.horizontalFlow(Sizing.fill(100), Sizing.content())
                                    .children(List.of(
                                            Components.textBox(Sizing.fill(25), ""),
                                            Components.textBox(Sizing.fill(25), ""),
                                            Components.button(Text.translatable("gui.teleporter_block.addLocationsListEntryButton"), button -> this.addLocationToLocationList(((TextBoxComponent)button.parent().children().get(0)).getText(), ((TextBoxComponent)button.parent().children().get(1)).getText()))
                                                    .sizing(Sizing.fill(50), Sizing.content())
                                    ))
                    ));
            buildLocationsList();
        } else {
            BlockPos directTeleportPositionOffset = this.teleporterBlock.getDirectTeleportPositionOffset();
            this.component(FlowLayout.class, "teleportationModeSettingsContainer")
                    .children(List.of(
                            Containers.horizontalFlow(Sizing.fill(100), Sizing.content())
                                    .children(List.of(
                                            Components.textBox(Sizing.fill(32), Integer.toString(directTeleportPositionOffset.getX()))
                                                    .tooltip(Text.translatable("gui.teleporter_block.directTeleportPositionOffsetX.tooltip"))
                                                    .margins(Insets.of(1, 1, 1, 1))
                                                    .id("directTeleportPositionOffsetX"),
                                            Components.textBox(Sizing.fill(32), Integer.toString(directTeleportPositionOffset.getY()))
                                                    .tooltip(Text.translatable("gui.teleporter_block.directTeleportPositionOffsetY.tooltip"))
                                                    .margins(Insets.of(1, 1, 1, 1))
                                                    .id("directTeleportPositionOffsetY"),
                                            Components.textBox(Sizing.fill(32), Integer.toString(directTeleportPositionOffset.getZ()))
                                                    .tooltip(Text.translatable("gui.teleporter_block.directTeleportPositionOffsetZ.tooltip"))
                                                    .margins(Insets.of(1, 1, 1, 1))
                                                    .id("directTeleportPositionOffsetZ")
                                    ))
                                    .verticalAlignment(VerticalAlignment.CENTER)
                                    .horizontalAlignment(HorizontalAlignment.CENTER),
                            Containers.horizontalFlow(Sizing.fill(100), Sizing.content())
                                    .children(List.of(
                                            Components.textBox(Sizing.fill(49), Double.toString(this.teleporterBlock.getDirectTeleportPositionOffsetYaw()))
                                                    .tooltip(Text.translatable("gui.teleporter_block.directTeleportPositionOffsetYaw.tooltip"))
                                                    .margins(Insets.of(1, 1, 1, 1))
                                                    .id("directTeleportPositionOffsetYaw"),
                                            Components.textBox(Sizing.fill(49), Double.toString(this.teleporterBlock.getDirectTeleportPositionOffsetPitch()))
                                                    .tooltip(Text.translatable("gui.teleporter_block.directTeleportPositionOffsetPitch.tooltip"))
                                                    .margins(Insets.of(1, 1, 1, 1))
                                                    .id("directTeleportPositionOffsetPitch")
                                    ))
                                    .verticalAlignment(VerticalAlignment.CENTER)
                                    .horizontalAlignment(HorizontalAlignment.CENTER)
                    ));
        }
    }

    private void buildLocationsList() {
        int locationsListSize = this.teleporterBlock.getLocationsList().size();
        this.component(FlowLayout.class, "locationsListContainer").clearChildren();
        for (int i = 0; i < locationsListSize; i++) {
            this.component(FlowLayout.class, "locationsListContainer").child(
                    Containers.horizontalFlow(Sizing.fill(100), Sizing.content())
                            .children(List.of(
                                    Components.label(Text.of(this.teleporterBlock.getLocationsList().get(i).getLeft()))
                                            .shadow(true)
                                            .color(Color.ofArgb(0xFFFFFF))
                                            .margins(Insets.of(1, 1, 1, 1))
                                            .sizing(Sizing.fill(25), Sizing.content()),
                                    Components.label(Text.of(this.teleporterBlock.getLocationsList().get(i).getRight()))
                                            .shadow(true)
                                            .color(Color.ofArgb(0xFFFFFF))
                                            .margins(Insets.of(1, 1, 1, 1))
                                            .sizing(Sizing.fill(25), Sizing.content()),
                                    Components.button(Text.translatable("gui.teleporter_block.deleteLocationsListEntryButton"), button -> button.parent().remove())
                                            .sizing(Sizing.fill(50), Sizing.content())
                            ))
            );
        }
    }
    //endregion build screen components

    @Override
    protected void init() {
        if (this.client != null && this.client.world != null) {
            BlockEntity blockEntity = this.client.world.getBlockEntity(this.handler.getBlockPos());
            if (blockEntity instanceof TeleporterBlockBlockEntity) {
                this.teleporterBlock = (TeleporterBlockBlockEntity) blockEntity;
            }
        }
        this.showActivationArea = this.teleporterBlock.getShowActivationArea();
        this.teleportationMode = this.teleporterBlock.getTeleportationMode();
        this.showAdventureScreen = this.teleporterBlock.getShowAdventureScreen();
        this.consumeKeyItemStack = this.teleporterBlock.getConsumeKeyItemStack();

        super.init();
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
    protected void drawForeground(DrawContext context, int mouseX, int mouseY) {}

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
        PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());

        // teleporter block position
        buf.writeBlockPos(this.teleporterBlock.getPos());

        // teleporter block information
        buf.writeString(this.component(TextBoxComponent.class, "teleporterName").getText());
        buf.writeBoolean(this.showActivationArea);

        buf.writeInt(this.parseInt(this.component(TextBoxComponent.class, "activationAreaDimensionsX").getText()));
        buf.writeInt(this.parseInt(this.component(TextBoxComponent.class, "activationAreaDimensionsY").getText()));
        buf.writeInt(this.parseInt(this.component(TextBoxComponent.class, "activationAreaDimensionsZ").getText()));
        buf.writeBlockPos(new BlockPos(
                this.parseInt(this.component(TextBoxComponent.class, "activationAreaPositionOffsetX").getText()),
                this.parseInt(this.component(TextBoxComponent.class, "activationAreaPositionOffsetY").getText()),
                this.parseInt(this.component(TextBoxComponent.class, "activationAreaPositionOffsetZ").getText())
        ));

        buf.writeBoolean(this.showAdventureScreen);

        buf.writeInt(this.teleportationMode);

        if (this.teleportationMode == 0) {
            buf.writeBlockPos(new BlockPos(
                    this.parseInt(this.component(TextBoxComponent.class, "directTeleportPositionOffsetX").getText()),
                    this.parseInt(this.component(TextBoxComponent.class, "directTeleportPositionOffsetY").getText()),
                    this.parseInt(this.component(TextBoxComponent.class, "directTeleportPositionOffsetZ").getText())
            ));

            buf.writeDouble(this.parseDouble(this.component(TextBoxComponent.class, "directTeleportPositionOffsetYaw").getText()));
            buf.writeDouble(this.parseDouble(this.component(TextBoxComponent.class, "directTeleportPositionOffsetPitch").getText()));
        } else {
            buf.writeBlockPos(new BlockPos(0, 0, 0));
            buf.writeDouble(0.0);
            buf.writeDouble(0.0);
        }
        if (this.teleportationMode == 1) {

            buf.writeInt(this.specificLocationType);

        } else {
            buf.writeInt(-1);
        }
        if (this.teleportationMode == 2 || this.teleportationMode == 3) {
            int locationsListSize = this.component(FlowLayout.class, "locationsListContainer").children().size();
            buf.writeInt(locationsListSize);
            for (int i = 0; i < locationsListSize; i++) {
                buf.writeString(((LabelComponent)((FlowLayout)this.component(FlowLayout.class, "locationsListContainer").children().get(i)).children().get(0)).text().getString());
                buf.writeString(((LabelComponent)((FlowLayout)this.component(FlowLayout.class, "locationsListContainer").children().get(i)).children().get(1)).text().getString());
            }
        } else {
            buf.writeInt(0);
        }

        buf.writeBoolean(this.consumeKeyItemStack);

        buf.writeString(this.component(TextBoxComponent.class, "teleportButtonLabel").getText());
        buf.writeString(this.component(TextBoxComponent.class, "cancelTeleportButtonLabel").getText());

        this.client.getNetworkHandler().sendPacket(new CustomPayloadC2SPacket(BetterAdventureModeCoreServerPacket.UPDATE_TELEPORTER_BLOCK, buf));
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
        PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());

        buf.writeBlockPos(this.teleporterBlock.getPos());

        buf.writeInt(this.teleporterBlock.getTeleportationMode());

        buf.writeBlockPos(this.teleporterBlock.getDirectTeleportPositionOffset());
        buf.writeDouble(this.teleporterBlock.getDirectTeleportPositionOffsetYaw());
        buf.writeDouble(this.teleporterBlock.getDirectTeleportPositionOffsetPitch());

        buf.writeInt(this.teleporterBlock.getSpecificLocationType());

        if (this.teleporterBlock.getTeleportationMode() == 2 || this.teleporterBlock.getTeleportationMode() == 3) {
            buf.writeString(component(LabelComponent.class, "currentTeleportationTargetEntryLabel").text().getString());
            buf.writeString(""); // TODO get chosen dungeon/house
            buf.writeString(""); // TODO get chosen dungeon/house entrance
        } else {
            buf.writeString("");
            buf.writeString("");
            buf.writeString("");
        }

        this.client.getNetworkHandler().sendPacket(new CustomPayloadC2SPacket(BetterAdventureModeCoreServerPacket.TELEPORT_FROM_TELEPORTER_BLOCK, buf));
        return true;
    }

    private void givePortalResistanceEffect() {
        PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());

        buf.writeInt(StatusEffect.getRawId(StatusEffectsRegistry.PORTAL_RESISTANCE_EFFECT));
        buf.writeInt(5);
        buf.writeInt(0);
        buf.writeBoolean(false);
        buf.writeBoolean(false);
        buf.writeBoolean(false);
        buf.writeBoolean(true);

        if (this.client != null && this.client.getNetworkHandler() != null) {
            this.client.getNetworkHandler().sendPacket(new CustomPayloadC2SPacket(BetterAdventureModeCoreServerPacket.ADD_STATUS_EFFECT_PACKET, buf));
        }
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
}
