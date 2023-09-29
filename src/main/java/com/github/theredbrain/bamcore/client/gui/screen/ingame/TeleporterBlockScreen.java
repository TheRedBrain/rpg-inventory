package com.github.theredbrain.bamcore.client.gui.screen.ingame;

import com.github.theredbrain.bamcore.BetterAdventureModCore;
import com.github.theredbrain.bamcore.block.entity.TeleporterBlockBlockEntity;
import com.github.theredbrain.bamcore.network.packet.BetterAdventureModCoreServerPacket;
import com.github.theredbrain.bamcore.registry.ComponentsRegistry;
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
    public static final Identifier ADVENTURE_TELEPORTER_SCREEN_BACKGROUND_TEXTURE = BetterAdventureModCore.identifier("textures/gui/container/adventure_teleporter_screen.png");
    public static final Identifier CREATIVE_TELEPORTER_SCREEN_BACKGROUND_TEXTURE = BetterAdventureModCore.identifier("textures/gui/container/creative_teleporter_screen.png");
    public static final Identifier TELEPORTER_SCREEN_UTILITY_TEXTURE = BetterAdventureModCore.identifier("textures/gui/container/teleporter_screen_util.png");
    private TeleporterBlockScreenHandler handler;
    private TeleporterBlockBlockEntity teleporterBlock;
    private boolean showCreativeTab;
    private boolean showActivationArea;
    private boolean indirectTeleportationMode;
    private boolean showAdventureScreen;
    private int dimensionMode;
    // TODO change consumeKeyItemStack to a string identifying the item and an int identifying the count
    private boolean consumeKeyItemStack;

    public TeleporterBlockScreen(TeleporterBlockScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
        this.handler = handler;
        this.showCreativeTab = handler.getShowCreativeTab();
    }

    // button callbacks
    //region
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
            this.buildAdventureModeScreen(this.uiAdapter.rootComponent);
        }
    }

    private void cancelDungeonRegeneration() {
        component(OverlayContainer.class, "dungeonRegenerationConfirmScreen").remove();
        this.buildAdventureModeScreen(this.uiAdapter.rootComponent);
    }

    private void updateCurrentTeleportationTargetEntry(PlayerListEntry clientPlayer, PlayerListEntry newTeleportationTargetPlayer) {

        for (Component child : component(FlowLayout.class, "currentTeleportationTargetEntryContainer").children()) {
            child.remove();
        }

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

        if (clientPlayer != newTeleportationTargetPlayer) {
            component(ButtonComponent.class, "regenerateDungeonButton").active = false;
        } else {
            component(ButtonComponent.class, "regenerateDungeonButton").active = ComponentsRegistry.PLAYER_SPECIFIC_DIMENSION_IDS.get(this.client.getServer().getPlayerManager().getPlayer(clientPlayer.getProfile().getId())).getStatus(this.teleporterBlock.getOutgoingTeleportDimension());
        }
    }

    private void done() {
        if (this.updateTeleporterBlock()) {
            this.close();
        }
    }

    private void cancel() {
        this.teleporterBlock.setShowActivationArea(this.showActivationArea);
        this.teleporterBlock.setDimensionMode(this.dimensionMode);
        this.teleporterBlock.setIndirectTeleportationMode(this.indirectTeleportationMode);
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

    private void switchDimensionMode() {
        if (this.dimensionMode == 0) {
            this.dimensionMode = 1;
        } else if (this.dimensionMode == 1) {
            this.dimensionMode = 2;
        } else {
            this.dimensionMode = 0;
        }
        this.component(ButtonComponent.class, "switchDimensionModeButton").setMessage(this.dimensionMode == 0 ? Text.translatable("gui.teleporter_block.switchDimensionModeButton.static") : this.dimensionMode == 1 ? Text.translatable("gui.teleporter_block.switchDimensionModeButton.dynamic") : Text.translatable("gui.teleporter_block.switchDimensionModeButton.current"));
        this.component(ButtonComponent.class, "switchDimensionModeButton").tooltip(this.dimensionMode == 0 ? Text.translatable("gui.teleporter_block.switchDimensionModeButton.static.tooltip") : this.dimensionMode == 1 ? Text.translatable("gui.teleporter_block.switchDimensionModeButton.dynamic.tooltip") : Text.translatable("gui.teleporter_block.switchDimensionModeButton.current.tooltip"));
        this.component(TextBoxComponent.class, "outgoingTeleportDimension").tooltip((this.dimensionMode == 0 || this.dimensionMode == 2) ? Text.translatable("gui.teleporter_block.outgoingTeleportDimension.tooltip") : Text.translatable("gui.teleporter_block.outgoingTeleportDimensionGroup.tooltip"));
    }

    private void toggleIndirectTeleportationMode() {
        if (this.indirectTeleportationMode) {
            this.indirectTeleportationMode = false;
        } else {
            this.indirectTeleportationMode = true;
        }
        this.component(ButtonComponent.class, "toggleIndirectTeleportationModeButton")
                .setMessage(this.indirectTeleportationMode ? Text.translatable("gui.teleporter_block.toggleIndirectTeleportationModeButton.on")
                        : Text.translatable("gui.teleporter_block.toggleIndirectTeleportationModeButton.off"));
        this.component(ButtonComponent.class, "toggleIndirectTeleportationModeButton")
                .tooltip(this.indirectTeleportationMode ? Text.translatable("gui.teleporter_block.toggleIndirectTeleportationModeButton.on.tooltip")
                        : Text.translatable("gui.teleporter_block.toggleIndirectTeleportationModeButton.off.tooltip"));

        this.component(TextBoxComponent.class, "outgoingTeleportTeleporterPositionX").active = this.indirectTeleportationMode;
        this.component(TextBoxComponent.class, "outgoingTeleportTeleporterPositionX").visible = this.indirectTeleportationMode;
        this.component(TextBoxComponent.class, "outgoingTeleportTeleporterPositionY").active = this.indirectTeleportationMode;
        this.component(TextBoxComponent.class, "outgoingTeleportTeleporterPositionY").visible = this.indirectTeleportationMode;
        this.component(TextBoxComponent.class, "outgoingTeleportTeleporterPositionZ").active = this.indirectTeleportationMode;
        this.component(TextBoxComponent.class, "outgoingTeleportTeleporterPositionZ").visible = this.indirectTeleportationMode;
    }

    private void toggleConsumeKeyItemStack() {
        if (this.consumeKeyItemStack) {
            this.consumeKeyItemStack = false;
        } else {
            this.consumeKeyItemStack = true;
        }
        this.component(ButtonComponent.class, "consumeKeyItemStackButton").setMessage(this.consumeKeyItemStack ? Text.translatable("gui.teleporter_block.keyItemButton.yes") : Text.translatable("gui.teleporter_block.keyItemButton.no"));
    }
    //endregion

    @Override
    protected @NotNull OwoUIAdapter<FlowLayout> createAdapter() {
        return OwoUIAdapter.create(this, Containers::verticalFlow);
    }

    @Override
    protected void build(FlowLayout rootComponent) {
        Vec3i activationAreaDimensions = this.teleporterBlock.getActivationAreaDimensions();
        BlockPos activationAreaPositionOffset = this.teleporterBlock.getActivationAreaPositionOffset();
        BlockPos outgoingTeleportTeleporterPosition = this.teleporterBlock.getOutgoingTeleportTeleporterPosition();
        BlockPos incomingTeleportPositionOffset = this.teleporterBlock.getIncomingTeleportPositionOffset();
        BlockPos outgoingTeleportPosition = this.teleporterBlock.getOutgoingTeleportPosition();
        BlockPos targetDungeonStructureStartPosition = this.teleporterBlock.getTargetDungeonStructureStartPosition();
        BlockPos regenerateTargetDungeonTriggerBlockPosition = this.teleporterBlock.getRegenerateTargetDungeonTriggerBlockPosition();
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

                                                        // target dimension
                                                        Components.button(this.dimensionMode == 0 ? Text.translatable("gui.teleporter_block.switchDimensionModeButton.static")
                                                                                : this.dimensionMode == 1 ? Text.translatable("gui.teleporter_block.switchDimensionModeButton.dynamic")
                                                                                : Text.translatable("gui.teleporter_block.switchDimensionModeButton.current")
                                                                        , button -> this.switchDimensionMode())
                                                                .sizing(Sizing.fill(100), Sizing.fixed(20))
                                                                .tooltip(this.dimensionMode == 0 ? Text.translatable("gui.teleporter_block.switchDimensionModeButton.static.tooltip")
                                                                        : this.dimensionMode == 1 ? Text.translatable("gui.teleporter_block.switchDimensionModeButton.dynamic.tooltip")
                                                                        : Text.translatable("gui.teleporter_block.switchDimensionModeButton.current.tooltip"))
                                                                .id("switchDimensionModeButton"),
                                                        Components.textBox(Sizing.fill(100), this.teleporterBlock.getOutgoingTeleportDimension())
                                                                .tooltip((this.dimensionMode == 0 || this.dimensionMode == 2) ? Text.translatable("gui.teleporter_block.outgoingTeleportDimension.tooltip")
                                                                        : Text.translatable("gui.teleporter_block.outgoingTeleportDimensionGroup.tooltip"))
                                                                .margins(Insets.of(1, 1, 1, 1))
                                                                .id("outgoingTeleportDimension"),

                                                        // showAdventureScreen switch
                                                        Components.button(this.showAdventureScreen ? Text.translatable("gui.teleporter_block.toggleShowAdventureScreenButton.on")
                                                                                : Text.translatable("gui.teleporter_block.toggleShowAdventureScreenButton.off"),
                                                                        button -> this.toggleShowAdventureScreen())
                                                                .sizing(Sizing.fill(100), Sizing.fixed(20))
                                                                .tooltip(this.showAdventureScreen ? Text.translatable("gui.teleporter_block.toggleShowAdventureScreenButton.on.tooltip")
                                                                        : Text.translatable("gui.teleporter_block.toggleShowAdventureScreenButton.off.tooltip"))
                                                                .id("toggleShowAdventureScreenButton"),

                                                        // teleportation mode switch / outgoing teleport teleporter position
                                                        Components.button(this.indirectTeleportationMode ? Text.translatable("gui.teleporter_block.toggleIndirectTeleportationModeButton.on")
                                                                                : Text.translatable("gui.teleporter_block.toggleIndirectTeleportationModeButton.off"),
                                                                        button -> this.toggleIndirectTeleportationMode())
                                                                .sizing(Sizing.fill(100), Sizing.fixed(20))
                                                                .tooltip(this.indirectTeleportationMode ? Text.translatable("gui.teleporter_block.toggleIndirectTeleportationModeButton.on.tooltip")
                                                                        : Text.translatable("gui.teleporter_block.toggleIndirectTeleportationModeButton.off.tooltip"))
                                                                .id("toggleIndirectTeleportationModeButton"),
                                                        Containers.horizontalFlow(Sizing.fill(100), Sizing.content())
                                                                .children(List.of(
                                                                        Components.textBox(Sizing.fill(32), Integer.toString(outgoingTeleportTeleporterPosition.getX()))
                                                                                .tooltip(Text.translatable("gui.teleporter_block.outgoingTeleportTeleporterPositionX.tooltip"))
                                                                                .margins(Insets.of(1, 1, 1, 1))
                                                                                .id("outgoingTeleportTeleporterPositionX"),
                                                                        Components.textBox(Sizing.fill(32), Integer.toString(outgoingTeleportTeleporterPosition.getY()))
                                                                                .tooltip(Text.translatable("gui.teleporter_block.outgoingTeleportTeleporterPositionY.tooltip"))
                                                                                .margins(Insets.of(1, 1, 1, 1))
                                                                                .id("outgoingTeleportTeleporterPositionY"),
                                                                        Components.textBox(Sizing.fill(32), Integer.toString(outgoingTeleportTeleporterPosition.getZ()))
                                                                                .tooltip(Text.translatable("gui.teleporter_block.outgoingTeleportTeleporterPositionZ.tooltip"))
                                                                                .margins(Insets.of(1, 1, 1, 1))
                                                                                .id("outgoingTeleportTeleporterPositionZ")
                                                                ))
                                                                .verticalAlignment(VerticalAlignment.CENTER)
                                                                .horizontalAlignment(HorizontalAlignment.CENTER),

                                                        // incoming/outgoing teleport positions
                                                        Containers.horizontalFlow(Sizing.fill(100), Sizing.content())
                                                                .children(List.of(
                                                                        Components.textBox(Sizing.fill(32), Integer.toString(incomingTeleportPositionOffset.getX()))
                                                                                .tooltip(Text.translatable("gui.teleporter_block.incomingTeleportPositionOffsetX.tooltip"))
                                                                                .margins(Insets.of(1, 1, 1, 1))
                                                                                .id("incomingTeleportPositionOffsetX"),
                                                                        Components.textBox(Sizing.fill(32), Integer.toString(incomingTeleportPositionOffset.getY()))
                                                                                .tooltip(Text.translatable("gui.teleporter_block.incomingTeleportPositionOffsetY.tooltip"))
                                                                                .margins(Insets.of(1, 1, 1, 1))
                                                                                .id("incomingTeleportPositionOffsetY"),
                                                                        Components.textBox(Sizing.fill(32), Integer.toString(incomingTeleportPositionOffset.getZ()))
                                                                                .tooltip(Text.translatable("gui.teleporter_block.incomingTeleportPositionOffsetZ.tooltip"))
                                                                                .margins(Insets.of(1, 1, 1, 1))
                                                                                .id("incomingTeleportPositionOffsetZ")
                                                                ))
                                                                .verticalAlignment(VerticalAlignment.CENTER)
                                                                .horizontalAlignment(HorizontalAlignment.CENTER),
                                                        Containers.horizontalFlow(Sizing.fill(100), Sizing.content())
                                                                .children(List.of(
                                                                        Components.textBox(Sizing.fill(32), Integer.toString(outgoingTeleportPosition.getX()))
                                                                                .tooltip(Text.translatable("gui.teleporter_block.outgoingTeleportPositionX.tooltip"))
                                                                                .margins(Insets.of(1, 1, 1, 1))
                                                                                .id("outgoingTeleportPositionX"),
                                                                        Components.textBox(Sizing.fill(32), Integer.toString(outgoingTeleportPosition.getY()))
                                                                                .tooltip(Text.translatable("gui.teleporter_block.outgoingTeleportPositionY.tooltip"))
                                                                                .margins(Insets.of(1, 1, 1, 1))
                                                                                .id("outgoingTeleportPositionY"),
                                                                        Components.textBox(Sizing.fill(32), Integer.toString(outgoingTeleportPosition.getZ()))
                                                                                .tooltip(Text.translatable("gui.teleporter_block.outgoingTeleportPositionZ.tooltip"))
                                                                                .margins(Insets.of(1, 1, 1, 1))
                                                                                .id("outgoingTeleportPositionZ")
                                                                ))
                                                                .verticalAlignment(VerticalAlignment.CENTER)
                                                                .horizontalAlignment(HorizontalAlignment.CENTER),

                                                        // incoming/outgoing teleport yaw/pitch
                                                        Containers.horizontalFlow(Sizing.fill(100), Sizing.content())
                                                                .children(List.of(
                                                                        Components.textBox(Sizing.fill(49), Double.toString(this.teleporterBlock.getIncomingTeleportPositionYaw()))
                                                                                .tooltip(Text.translatable("gui.teleporter_block.incomingTeleportPositionYaw.tooltip"))
                                                                                .margins(Insets.of(1, 1, 1, 1))
                                                                                .id("incomingTeleportPositionYaw"),
                                                                        Components.textBox(Sizing.fill(49), Double.toString(this.teleporterBlock.getIncomingTeleportPositionPitch()))
                                                                                .tooltip(Text.translatable("gui.teleporter_block.incomingTeleportPositionPitch.tooltip"))
                                                                                .margins(Insets.of(1, 1, 1, 1))
                                                                                .id("incomingTeleportPositionPitch")
                                                                ))
                                                                .verticalAlignment(VerticalAlignment.CENTER)
                                                                .horizontalAlignment(HorizontalAlignment.CENTER),
                                                        Containers.horizontalFlow(Sizing.fill(100), Sizing.content())
                                                                .children(List.of(
                                                                        Components.textBox(Sizing.fill(49), Double.toString(this.teleporterBlock.getOutgoingTeleportPositionYaw()))
                                                                                .tooltip(Text.translatable("gui.teleporter_block.outgoingTeleportPositionYaw.tooltip"))
                                                                                .margins(Insets.of(1, 1, 1, 1))
                                                                                .id("outgoingTeleportPositionYaw"),
                                                                        Components.textBox(Sizing.fill(49), Double.toString(this.teleporterBlock.getOutgoingTeleportPositionPitch()))
                                                                                .tooltip(Text.translatable("gui.teleporter_block.outgoingTeleportPositionPitch.tooltip"))
                                                                                .margins(Insets.of(1, 1, 1, 1))
                                                                                .id("outgoingTeleportPositionPitch")
                                                                ))
                                                                .verticalAlignment(VerticalAlignment.CENTER)
                                                                .horizontalAlignment(HorizontalAlignment.CENTER),

                                                        // data for (re-)generating the target dungeon
                                                        Components.textBox(Sizing.fill(100), this.teleporterBlock.getTargetDungeonStructureIdentifier())
                                                                .tooltip(Text.translatable("gui.teleporter_block.targetDungeonStructureIdentifier.tooltip"))
                                                                .margins(Insets.of(1, 1, 1, 1))
                                                                .id("targetDungeonStructureIdentifier"),
                                                        Containers.horizontalFlow(Sizing.fill(100), Sizing.content())
                                                                .children(List.of(
                                                                        Components.textBox(Sizing.fill(32), Integer.toString(targetDungeonStructureStartPosition.getX()))
                                                                                .tooltip(Text.translatable("gui.teleporter_block.targetDungeonStructureStartPositionX.tooltip"))
                                                                                .margins(Insets.of(1, 1, 1, 1))
                                                                                .id("targetDungeonStructureStartPositionX"),
                                                                        Components.textBox(Sizing.fill(32), Integer.toString(targetDungeonStructureStartPosition.getY()))
                                                                                .tooltip(Text.translatable("gui.teleporter_block.targetDungeonStructureStartPositionY.tooltip"))
                                                                                .margins(Insets.of(1, 1, 1, 1))
                                                                                .id("targetDungeonStructureStartPositionY"),
                                                                        Components.textBox(Sizing.fill(32), Integer.toString(targetDungeonStructureStartPosition.getZ()))
                                                                                .tooltip(Text.translatable("gui.teleporter_block.targetDungeonStructureStartPositionZ.tooltip"))
                                                                                .margins(Insets.of(1, 1, 1, 1))
                                                                                .id("targetDungeonStructureStartPositionZ")
                                                                ))
                                                                .verticalAlignment(VerticalAlignment.CENTER)
                                                                .horizontalAlignment(HorizontalAlignment.CENTER),
                                                        Containers.horizontalFlow(Sizing.fill(100), Sizing.content())
                                                                .children(List.of(
                                                                        Components.textBox(Sizing.fill(49), Integer.toString(this.teleporterBlock.getTargetDungeonChunkX()))
                                                                                .tooltip(Text.translatable("gui.teleporter_block.targetDungeonChunkX.tooltip"))
                                                                                .margins(Insets.of(1, 1, 1, 1))
                                                                                .id("targetDungeonChunkX"),
                                                                        Components.textBox(Sizing.fill(49), Integer.toString(this.teleporterBlock.getTargetDungeonChunkZ()))
                                                                                .tooltip(Text.translatable("gui.teleporter_block.targetDungeonChunkZ.tooltip"))
                                                                                .margins(Insets.of(1, 1, 1, 1))
                                                                                .id("targetDungeonChunkZ")
                                                                ))
                                                                .verticalAlignment(VerticalAlignment.CENTER)
                                                                .horizontalAlignment(HorizontalAlignment.CENTER),
                                                        Containers.horizontalFlow(Sizing.fill(100), Sizing.content())
                                                                .children(List.of(
                                                                        Components.textBox(Sizing.fill(32), Integer.toString(regenerateTargetDungeonTriggerBlockPosition.getX()))
                                                                                .tooltip(Text.translatable("gui.teleporter_block.regenerateTargetDungeonTriggerBlockPositionX.tooltip"))
                                                                                .margins(Insets.of(1, 1, 1, 1))
                                                                                .id("regenerateTargetDungeonTriggerBlockPositionX"),
                                                                        Components.textBox(Sizing.fill(32), Integer.toString(regenerateTargetDungeonTriggerBlockPosition.getY()))
                                                                                .tooltip(Text.translatable("gui.teleporter_block.regenerateTargetDungeonTriggerBlockPositionY.tooltip"))
                                                                                .margins(Insets.of(1, 1, 1, 1))
                                                                                .id("regenerateTargetDungeonTriggerBlockPositionY"),
                                                                        Components.textBox(Sizing.fill(32), Integer.toString(regenerateTargetDungeonTriggerBlockPosition.getZ()))
                                                                                .tooltip(Text.translatable("gui.teleporter_block.regenerateTargetDungeonTriggerBlockPositionZ.tooltip"))
                                                                                .margins(Insets.of(1, 1, 1, 1))
                                                                                .id("regenerateTargetDungeonTriggerBlockPositionZ")
                                                                ))
                                                                .verticalAlignment(VerticalAlignment.CENTER)
                                                                .horizontalAlignment(HorizontalAlignment.CENTER),

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
        //endregion
        } else {
        //region adventure mode screen

            if (!this.showAdventureScreen && (this.dimensionMode == 0 || this.dimensionMode == 2)) {
                this.teleport();
            } else {
                this.buildAdventureModeScreen(rootComponent);
            }
        //endregion
        }
    }

    private void buildAdventureModeScreen(FlowLayout rootComponent) {

        if (this.teleporterBlock.getDimensionMode() == 0 || this.teleporterBlock.getDimensionMode() == 2) {
            //region static dimension mode
            rootComponent.child(Containers.verticalFlow(Sizing.fill(100), Sizing.content())
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
        } else {
            //region dynamic dimension mode
            rootComponent.child(Containers.verticalFlow(Sizing.fill(100), Sizing.content())
                    .children(List.of(
                            Components.label(Text.translatable(this.teleporterBlock.getTeleporterName()))
                                    .shadow(true)
                                    .color(Color.ofArgb(0xFFFFFF))
                                    .margins(Insets.of(1, 1, 1, 1))
                                    .sizing(Sizing.content(), Sizing.content())
                                    .id("adventureScreenTitle"),
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
                                                    Containers.collapsible(Sizing.fill(100), Sizing.content(), Text.translatable("gui.teleporter_block.currentTeamTargetEntries.label"), true)
                                                            .verticalAlignment(VerticalAlignment.CENTER)
                                                            .horizontalAlignment(HorizontalAlignment.CENTER)
                                                            .id("currentTeamTargetEntries")
                                                        /*
                                                         Current target
                                                             defaults to current player or owner of current dimension

                                                         lists of viable targets
                                                             current player
                                                             team
                                                             guild (later)
                                                             friends (later)
                                                             public (later)
                                                             clicking on a viable target updates the current target

                                                         target composition
                                                            player skin
                                                            player name
                                                            button "choose as target"
                                                         */

                                            ))
                                            .verticalAlignment(VerticalAlignment.CENTER)
                                            .horizontalAlignment(HorizontalAlignment.CENTER)
                                            .id("player_list_scroll_container_content")
                                    )
                                    .verticalAlignment(VerticalAlignment.CENTER)
                                    .horizontalAlignment(HorizontalAlignment.CENTER)
                                    .id("player_list_scroll_container"),
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
                    .verticalAlignment(VerticalAlignment.CENTER)
                    .horizontalAlignment(HorizontalAlignment.CENTER)
                    .id("adventure_container")
            );
            this.populatePlayerList();
            //endregion
        }

        if (!handler.slots.get(37).getStack().isEmpty()) {
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
                    component(GridLayout.class, "playerInventorySlots_adventure").child(
                                    slotAsComponent(j + (i + 1) * 9)
                                            .margins(Insets.of(1, 1, 1, 1)),
                                    i,
                                    j)
                            .id("slot_" + i + "_" + j);
                }
            }
        } else {
            disableSlot(36);
            disableSlot(38);

            for (int i = 0; i < 3; ++i) {
                for (int j = 0; j < 9; ++j) {
                    disableSlot(j + (i + 1) * 9);
                }
            }
        }
        disableSlot(37);
    }

    private void populatePlayerList() {
        if (this.client != null && this.client.player != null) {

            PlayerListEntry currentPlayer = this.client.player.networkHandler.getPlayerListEntry(this.client.player.getUuid());

            if (currentPlayer != null) {

//                component(FlowLayout.class, "currentTeleportationTargetEntryContainer").children(List.of(
//                        Components.texture(currentPlayer.getSkinTexture(), 8, 8, 8, 8, 64, 64)
//                                .sizing(Sizing.fixed(16), Sizing.fixed(16)),
//                        Components.label(Text.of(currentPlayer.getProfile().getName()))
//                                .shadow(true)
//                                .color(Color.ofArgb(0xFFFFFF))
//                                .margins(Insets.of(1, 1, 1, 1))
//                                .sizing(Sizing.content(), Sizing.content())
//                                .id("currentTeleportationTargetEntryLabel")
//                ));

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

    @Override
    protected void init() {
        if (this.client != null && this.client.world != null) {
            BlockEntity blockEntity = this.client.world.getBlockEntity(this.handler.getBlockPos());
            if (blockEntity instanceof TeleporterBlockBlockEntity) {
                this.teleporterBlock = (TeleporterBlockBlockEntity) blockEntity;
            }
        }
        this.showActivationArea = this.teleporterBlock.getShowActivationArea();
        this.dimensionMode = this.teleporterBlock.getDimensionMode();
        this.showAdventureScreen = this.teleporterBlock.getShowAdventureScreen();
        this.indirectTeleportationMode = this.teleporterBlock.getIndirectTeleportationMode();
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

        buf.writeInt(this.dimensionMode);
        buf.writeString(this.component(TextBoxComponent.class, "outgoingTeleportDimension").getText());

        buf.writeBoolean(this.showAdventureScreen);

        buf.writeBoolean(this.indirectTeleportationMode);
        buf.writeBlockPos(new BlockPos(
                this.parseInt(this.component(TextBoxComponent.class, "outgoingTeleportTeleporterPositionX").getText()),
                this.parseInt(this.component(TextBoxComponent.class, "outgoingTeleportTeleporterPositionY").getText()),
                this.parseInt(this.component(TextBoxComponent.class, "outgoingTeleportTeleporterPositionZ").getText())
        ));
        buf.writeBlockPos(new BlockPos(
                this.parseInt(this.component(TextBoxComponent.class, "incomingTeleportPositionOffsetX").getText()),
                this.parseInt(this.component(TextBoxComponent.class, "incomingTeleportPositionOffsetY").getText()),
                this.parseInt(this.component(TextBoxComponent.class, "incomingTeleportPositionOffsetZ").getText())
        ));

        buf.writeDouble(this.parseDouble(this.component(TextBoxComponent.class, "incomingTeleportPositionYaw").getText()));
        buf.writeDouble(this.parseDouble(this.component(TextBoxComponent.class, "incomingTeleportPositionPitch").getText()));

        buf.writeBlockPos(new BlockPos(
                this.parseInt(this.component(TextBoxComponent.class, "outgoingTeleportPositionX").getText()),
                this.parseInt(this.component(TextBoxComponent.class, "outgoingTeleportPositionY").getText()),
                this.parseInt(this.component(TextBoxComponent.class, "outgoingTeleportPositionZ").getText())
        ));
        buf.writeDouble(this.parseDouble(this.component(TextBoxComponent.class, "outgoingTeleportPositionYaw").getText()));
        buf.writeDouble(this.parseDouble(this.component(TextBoxComponent.class, "outgoingTeleportPositionPitch").getText()));

        buf.writeString(this.component(TextBoxComponent.class, "targetDungeonStructureIdentifier").getText());
        buf.writeBlockPos(new BlockPos(
                this.parseInt(this.component(TextBoxComponent.class, "targetDungeonStructureStartPositionX").getText()),
                this.parseInt(this.component(TextBoxComponent.class, "targetDungeonStructureStartPositionY").getText()),
                this.parseInt(this.component(TextBoxComponent.class, "targetDungeonStructureStartPositionZ").getText())
        ));
        buf.writeInt(this.parseInt(this.component(TextBoxComponent.class, "targetDungeonChunkX").getText()));
        buf.writeInt(this.parseInt(this.component(TextBoxComponent.class, "targetDungeonChunkZ").getText()));
        buf.writeBlockPos(new BlockPos(
                this.parseInt(this.component(TextBoxComponent.class, "regenerateTargetDungeonTriggerBlockPositionX").getText()),
                this.parseInt(this.component(TextBoxComponent.class, "regenerateTargetDungeonTriggerBlockPositionY").getText()),
                this.parseInt(this.component(TextBoxComponent.class, "regenerateTargetDungeonTriggerBlockPositionZ").getText())
        ));

        buf.writeBoolean(this.consumeKeyItemStack);
        buf.writeString(this.component(TextBoxComponent.class, "teleportButtonLabel").getText());
        buf.writeString(this.component(TextBoxComponent.class, "cancelTeleportButtonLabel").getText());

        this.client.getNetworkHandler().sendPacket(new CustomPayloadC2SPacket(BetterAdventureModCoreServerPacket.UPDATE_TELEPORTER_BLOCK, buf));
        return true;
    }

    private boolean tryDungeonRegeneration() {
        PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
//        buf.writeString(component(LabelComponent.class, "currentTeleportationTargetEntryLabel").text().getString());
        buf.writeString(this.client.player.getName().getString());

        buf.writeString(this.teleporterBlock.getOutgoingTeleportDimension());

        buf.writeString(this.teleporterBlock.getTargetDungeonStructureIdentifier());
        buf.writeBlockPos(this.teleporterBlock.getTargetDungeonStructureStartPosition());

        buf.writeInt(this.teleporterBlock.getTargetDungeonChunkX());
        buf.writeInt(this.teleporterBlock.getTargetDungeonChunkZ());
        buf.writeBlockPos(this.teleporterBlock.getRegenerateTargetDungeonTriggerBlockPosition());
//
        this.client.getNetworkHandler().sendPacket(new CustomPayloadC2SPacket(BetterAdventureModCoreServerPacket.REGENERATE_DIMENSION_FROM_TELEPORTER_BLOCK, buf));
        return true;
    }

    private boolean tryTeleport() {
        PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
        if (this.teleporterBlock.getDimensionMode() == 0 || this.teleporterBlock.getDimensionMode() == 2) {
            buf.writeString(this.client.player.getName().getString());
        } else {
            buf.writeString(component(LabelComponent.class, "currentTeleportationTargetEntryLabel").text().getString());
        }

        buf.writeInt(this.teleporterBlock.getDimensionMode());
        buf.writeString(this.teleporterBlock.getOutgoingTeleportDimension());

        buf.writeBoolean(this.teleporterBlock.getIndirectTeleportationMode());
        buf.writeBlockPos(this.teleporterBlock.getOutgoingTeleportTeleporterPosition());

        buf.writeBlockPos(this.teleporterBlock.getOutgoingTeleportPosition());
        buf.writeDouble(this.teleporterBlock.getOutgoingTeleportPositionYaw());
        buf.writeDouble(this.teleporterBlock.getOutgoingTeleportPositionPitch());

        buf.writeString(this.teleporterBlock.getTargetDungeonStructureIdentifier());
        buf.writeBlockPos(this.teleporterBlock.getTargetDungeonStructureStartPosition());

        buf.writeInt(this.teleporterBlock.getTargetDungeonChunkX());
        buf.writeInt(this.teleporterBlock.getTargetDungeonChunkZ());

        this.client.getNetworkHandler().sendPacket(new CustomPayloadC2SPacket(BetterAdventureModCoreServerPacket.TELEPORT_FROM_TELEPORTER_BLOCK, buf));
        return true;
    }

    private void givePortalResistanceEffect() {
        String playerName = this.client.player.getName().getString();
        String command = "effect clear " + playerName + " rpgmod:portal_resistance_effect";
        String command2 = "effect give " + playerName + " rpgmod:portal_resistance_effect 5 0 true";

        this.client.getNetworkHandler().sendCommand(command);
        this.client.getNetworkHandler().sendCommand(command2);
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
