package com.github.theredbrain.bamcore.client.gui.screen.ingame;

import com.github.theredbrain.bamcore.BetterAdventureModeCore;
import com.github.theredbrain.bamcore.block.entity.CraftingBenchBlockEntity;
import com.github.theredbrain.bamcore.block.entity.StructurePlacerBlockBlockEntity;
import com.github.theredbrain.bamcore.network.packet.BetterAdventureModCoreServerPacket;
import com.github.theredbrain.bamcore.registry.BlockRegistry;
import com.github.theredbrain.bamcore.screen.CraftingBenchBlockScreenHandler;
import dev.emi.trinkets.TrinketScreen;
import io.netty.buffer.Unpooled;
import io.wispforest.owo.ui.base.BaseOwoHandledScreen;
import io.wispforest.owo.ui.base.BaseOwoScreen;
import io.wispforest.owo.ui.component.Components;
import io.wispforest.owo.ui.component.TextBoxComponent;
import io.wispforest.owo.ui.container.Containers;
import io.wispforest.owo.ui.container.FlowLayout;
import io.wispforest.owo.ui.container.GridLayout;
import io.wispforest.owo.ui.core.*;
import io.wispforest.owo.util.pond.OwoSlotExtension;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.PageTurnWidget;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.c2s.play.CustomPayloadC2SPacket;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import net.minecraft.util.Colors;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.glfw.GLFW;

import java.util.List;

@Environment(EnvType.CLIENT)
public class CraftingBenchBlockScreen extends BaseOwoHandledScreen<FlowLayout, CraftingBenchBlockScreenHandler> {
    public static final Identifier INVENTORY_SLOT_TEXTURE = BetterAdventureModeCore.identifier("textures/gui/container/inventory_slot_texture.png");

    private CraftingBenchBlockEntity craftingBenchBlock;

    public CraftingBenchBlockScreen(CraftingBenchBlockScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
    }

    @Override
    protected void init() {
        if (this.client != null && this.client.world != null) {
            BlockEntity blockEntity = this.client.world.getBlockEntity(this.handler.getBlockPos());
            if (blockEntity instanceof CraftingBenchBlockEntity) {
                this.craftingBenchBlock = (CraftingBenchBlockEntity) blockEntity;
            }
        }
//        this.showActivationArea = this.teleporterBlock.getShowActivationArea();
//        this.dimensionMode = this.teleporterBlock.getDimensionMode();
//        this.showAdventureScreen = this.teleporterBlock.getShowAdventureScreen();
//        this.indirectTeleportationMode = this.teleporterBlock.getIndirectTeleportationMode();
//        this.consumeKeyItemStack = this.teleporterBlock.getConsumeKeyItemStack();

        super.init();
    }

    @Override
    protected void drawForeground(DrawContext context, int mouseX, int mouseY) {
    }

    @Override
    protected @NotNull OwoUIAdapter<FlowLayout> createAdapter() {
        return OwoUIAdapter.create(this, Containers::verticalFlow);
    }

//    @Override
//    protected void build(FlowLayout rootComponent) {
////        BlockPos placementPositionOffset = this.structurePlacerBlock.getPlacementPositionOffset();
////        BlockPos triggeredBlockPositionOffset = this.structurePlacerBlock.getTriggeredBlockPositionOffset();
//        rootComponent
//                .surface(Surface.VANILLA_TRANSLUCENT)
//                .horizontalAlignment(HorizontalAlignment.CENTER)
//                .verticalAlignment(VerticalAlignment.CENTER);
//        rootComponent.children(List.of(
//                Components.label(this.title)
//                        .shadow(true)
//                        .color(Color.ofArgb(0xFFFFFF))
//                        .margins(Insets.of(1, 1, 1, 1))
//                        .sizing(Sizing.content(), Sizing.content()),
//                Components.textBox(Sizing.fill(30), this.structurePlacerBlock.getPlacedStructureIdentifier())
//                        .tooltip(Text.translatable("gui.structure_placer_block.placedStructureIdentifier.tooltip"))
//                        .margins(Insets.of(1, 1, 1, 1))
//                        .id("placedStructureIdentifier"),
//                Containers.horizontalFlow(Sizing.fill(50), Sizing.content())
//                        .children(List.of(
//                                Components.textBox(Sizing.fill(32), Integer.toString(placementPositionOffset.getX()))
//                                        .tooltip(Text.translatable("gui.structure_placer_block.placementPositionOffsetX.tooltip"))
//                                        .margins(Insets.of(1, 1, 1, 1))
//                                        .id("placementPositionOffsetX"),
//                                Components.textBox(Sizing.fill(32), Integer.toString(placementPositionOffset.getY()))
//                                        .tooltip(Text.translatable("gui.structure_placer_block.placementPositionOffsetY.tooltip"))
//                                        .margins(Insets.of(1, 1, 1, 1))
//                                        .id("placementPositionOffsetY"),
//                                Components.textBox(Sizing.fill(32), Integer.toString(placementPositionOffset.getZ()))
//                                        .tooltip(Text.translatable("gui.structure_placer_block.placementPositionOffsetZ.tooltip"))
//                                        .margins(Insets.of(1, 1, 1, 1))
//                                        .id("placementPositionOffsetZ")
//                        ))
//                        .verticalAlignment(VerticalAlignment.CENTER)
//                        .horizontalAlignment(HorizontalAlignment.CENTER),
//                Containers.horizontalFlow(Sizing.fill(50), Sizing.content())
//                        .children(List.of(
//                                Components.textBox(Sizing.fill(32), Integer.toString(triggeredBlockPositionOffset.getX()))
//                                        .tooltip(Text.translatable("gui.triggered_block.triggeredBlockPositionOffsetX.tooltip"))
//                                        .margins(Insets.of(1, 1, 1, 1))
//                                        .id("triggeredBlockPositionOffsetX"),
//                                Components.textBox(Sizing.fill(32), Integer.toString(triggeredBlockPositionOffset.getY()))
//                                        .tooltip(Text.translatable("gui.triggered_block.triggeredBlockPositionOffsetY.tooltip"))
//                                        .margins(Insets.of(1, 1, 1, 1))
//                                        .id("triggeredBlockPositionOffsetY"),
//                                Components.textBox(Sizing.fill(32), Integer.toString(triggeredBlockPositionOffset.getZ()))
//                                        .tooltip(Text.translatable("gui.triggered_block.triggeredBlockPositionOffsetZ.tooltip"))
//                                        .margins(Insets.of(1, 1, 1, 1))
//                                        .id("triggeredBlockPositionOffsetZ")
//                        ))
//                        .verticalAlignment(VerticalAlignment.CENTER)
//                        .horizontalAlignment(HorizontalAlignment.CENTER),
//                Containers.horizontalFlow(Sizing.fill(30), Sizing.content())
//                        .children(List.of(
//                                Components.button(Text.translatable("gui.save"), button -> this.done())
//                                        .sizing(Sizing.fill(49), Sizing.fixed(20)),
//                                Components.button(ScreenTexts.CANCEL, button -> this.cancel())
//                                        .sizing(Sizing.fill(49), Sizing.fixed(20))
//                        ))
//                        .margins(Insets.of(1, 1, 1, 1))
//                        .verticalAlignment(VerticalAlignment.CENTER)
//                        .horizontalAlignment(HorizontalAlignment.CENTER)
//        ));
//    }

    @Override
    protected void build(FlowLayout rootComponent) {
        rootComponent
                .surface(Surface.VANILLA_TRANSLUCENT)
                .horizontalAlignment(HorizontalAlignment.CENTER)
                .verticalAlignment(VerticalAlignment.CENTER)
                .id("root");
        rootComponent.child(Containers.horizontalFlow(Sizing.content(), Sizing.content())
                .children(List.of(
                        Containers.verticalFlow(Sizing.fixed(130), Sizing.fixed(228))
                                .id("additional_inventory_screen_left"),
                        Containers.verticalFlow(Sizing.content(), Sizing.content())
                                .children(List.of(
                                        Containers.horizontalFlow(Sizing.content(), Sizing.content())
                                                .children(List.of(
                                                        Containers.verticalFlow(Sizing.fixed(87), Sizing.content())
                                                                .children(List.of(
                                                                        Containers.horizontalFlow(Sizing.fill(100), Sizing.content())
                                                                                .children(List.of(
                                                                                        Components.label(Text.translatable("gui.adventureInventory.equipmentSlots"))
                                                                                                .color(Color.ofArgb(Colors.BLACK))
                                                                                                .margins(Insets.of(0, 4, 0, 0))
                                                                                )),
                                                                        Containers.horizontalFlow(Sizing.fill(100), Sizing.content())
                                                                                .children(List.of( // TODO replace grid with verticalFlow
                                                                                        Containers.grid(Sizing.content(), Sizing.content(), 1, 1)
                                                                                                .child(Containers.verticalFlow(Sizing.fixed(18), Sizing.fixed(18))
                                                                                                        .id("necklaces_slot_container"), 0, 0)
//                                                                                                .child(slotAsComponent(46) // custom helmet
//                                                                                                        .margins(Insets.of(1, 1, 1, 1)), 0, 0)
                                                                                                .surface(Surface.tiled(INVENTORY_SLOT_TEXTURE, 18, 18)),
                                                                                        Containers.grid(Sizing.content(), Sizing.content(), 1, 1)
                                                                                                .child(Containers.verticalFlow(Sizing.fixed(18), Sizing.fixed(18))
                                                                                                        .id("necklaces_slot_container"), 0, 0)
                                                                                                .margins(Insets.of(0, 0, 1, 0))
                                                                                                .surface(Surface.tiled(INVENTORY_SLOT_TEXTURE, 18, 18))
                                                                                ))
                                                                                .padding(Insets.of(0, 0, 25, 25))
                                                                                .id("equipment_slots_top_row"),
                                                                        Containers.horizontalFlow(Sizing.content(), Sizing.content())
                                                                                .children(List.of(
                                                                                        Containers.verticalFlow(Sizing.content(), Sizing.content())
                                                                                                .children(List.of(
                                                                                                        Containers.verticalFlow(Sizing.fixed(18), Sizing.fixed(18))
                                                                                                                .id("shoulders_slot_container"),
                                                                                                        Containers.verticalFlow(Sizing.fixed(18), Sizing.fixed(18))
                                                                                                                .id("shoulders_slot_container"),
//                                                                                                        slotAsComponent(47) // custom chestplate
//                                                                                                                .margins(Insets.of(1, 1, 1, 1)),
                                                                                                        Containers.verticalFlow(Sizing.fixed(18), Sizing.fixed(18))
                                                                                                                .id("belts_slot_container"),
                                                                                                        Containers.verticalFlow(Sizing.fixed(18), Sizing.fixed(18))
                                                                                                                .id("belts_slot_container")
//                                                                                                        slotAsComponent(48) // custom leggings
//                                                                                                                .margins(Insets.of(1, 1, 1, 1))
                                                                                                ))
                                                                                                .surface(Surface.tiled(INVENTORY_SLOT_TEXTURE, 18, 18)),
                                                                                        Containers.verticalFlow(Sizing.fixed(51), Sizing.fixed(72)),
//                                                                                                .surface(Surface.tiled(CHARACTER_BACKGROUND_TEXTURE, 51, 72)),
                                                                                        Containers.verticalFlow(Sizing.content(), Sizing.content())
                                                                                                .children(List.of(
                                                                                                        Containers.verticalFlow(Sizing.fixed(18), Sizing.fixed(18))
                                                                                                                .id("rings_1_slot_container"),
                                                                                                        Containers.verticalFlow(Sizing.fixed(18), Sizing.fixed(18))
                                                                                                                .id("rings_2_slot_container"),
                                                                                                        Containers.verticalFlow(Sizing.fixed(18), Sizing.fixed(18))
                                                                                                                .id("gloves_slot_container"),
                                                                                                        Containers.verticalFlow(Sizing.fixed(18), Sizing.fixed(18))
                                                                                                                .id("gloves_slot_container")
//                                                                                                        slotAsComponent(49) // custom boots
//                                                                                                                .margins(Insets.of(1, 1, 1, 1))
                                                                                                ))
                                                                                                .surface(Surface.tiled(INVENTORY_SLOT_TEXTURE, 18, 18))
                                                                                ))
                                                                                .id("equipment_slots_middle_row"),
                                                                        Containers.horizontalFlow(Sizing.fill(100), Sizing.content())
                                                                                .children(List.of(
                                                                                        Containers.horizontalFlow(Sizing.content(), Sizing.content())
                                                                                                .children(List.of( // TODO replace grid with verticalFlow
                                                                                                        Containers.grid(Sizing.content(), Sizing.content(), 1, 2)
                                                                                                                .child(Containers.verticalFlow(Sizing.fixed(18), Sizing.fixed(18))
                                                                                                                        .id("main_hand_slot_container"), 0, 0)
                                                                                                                .child(Containers.verticalFlow(Sizing.fixed(18), Sizing.fixed(18))
                                                                                                                        .id("main_hand_slot_container"), 0, 0)
//                                                                                                                .child(slotAsComponent(50) // custom offhand
//                                                                                                                        .margins(Insets.of(1, 1, 1, 1)), 0, 1)
                                                                                                                .surface(Surface.tiled(INVENTORY_SLOT_TEXTURE, 18, 18))
                                                                                                )),
                                                                                        Containers.verticalFlow(Sizing.fixed(15), Sizing.fixed(18)),
                                                                                        Containers.horizontalFlow(Sizing.content(), Sizing.content())
                                                                                                .children(List.of( // TODO replace grid with verticalFlow
                                                                                                        Containers.grid(Sizing.content(), Sizing.content(), 1, 2)
                                                                                                                .child(Containers.verticalFlow(Sizing.fixed(18), Sizing.fixed(18))
                                                                                                                        .id("alternative_main_hand_slot_container"), 0, 0)
                                                                                                                .child(Containers.verticalFlow(Sizing.fixed(18), Sizing.fixed(18))
                                                                                                                        .id("alternative_off_hand_slot_container"), 0, 1)
                                                                                                                .surface(Surface.tiled(INVENTORY_SLOT_TEXTURE, 18, 18))
                                                                                                ))
                                                                                ))
                                                                                .verticalAlignment(VerticalAlignment.CENTER)
                                                                                .horizontalAlignment(HorizontalAlignment.CENTER)
                                                                                .id("equipment_slots_bottom_row")
                                                                ))
                                                                .id("equipment_slots_container_left"),
                                                        Containers.verticalFlow(Sizing.fixed(72), Sizing.content())
                                                                .children(List.of(
                                                                        Containers.horizontalFlow(Sizing.fill(100), Sizing.content())
                                                                                .children(List.of(
                                                                                        Containers.verticalFlow(Sizing.fixed(23), Sizing.fixed(13))//23, 13
//                                                                                                .child(
//                                                                                                        Components.wrapVanillaWidget(new PageTurnWidget(0, 0, !this.showAttributeScreen, button -> this.toggleAttributeScreen(), true))
//                                                                                                )
                                                                                                .id("toggleAttributeScreenButtonContainer")
                                                                                ))
                                                                                .horizontalAlignment(HorizontalAlignment.RIGHT),
                                                                        Containers.verticalFlow(Sizing.fill(100), Sizing.fixed(54)),
                                                                        Containers.verticalFlow(Sizing.fill(100), Sizing.fixed(18)).child(
                                                                                Components.label(Text.translatable("gui.adventureInventory.spellSlots"))
                                                                                        .color(Color.ofArgb(Colors.BLACK))
                                                                                        .margins(Insets.of(4, 5, 0, 0))
                                                                        ),
                                                                        Containers.verticalFlow(Sizing.fill(100), Sizing.fixed(36))
                                                                                .id("spell_slots_container")
                                                                ))
                                                                .margins(Insets.of(0, 0, 3, 0))
                                                                .id("equipment_slots_container_right")
                                                )),
                                        Containers.horizontalFlow(Sizing.fixed(162), Sizing.content())
                                                .child(Components.label(this.title)
                                                        .color(Color.ofArgb(Colors.BLACK))
                                                        .margins(Insets.of(4, 0, 0, 0))),
                                        Containers.verticalFlow(Sizing.content(), Sizing.content())
                                                .children(List.of(
                                                        // main inventory
                                                        Containers.grid(Sizing.content(), Sizing.fixed(54), 3, 9)
                                                                .surface(Surface.tiled(INVENTORY_SLOT_TEXTURE, 18, 18))
                                                                .margins(Insets.of(0, 4, 0, 0))
                                                                .id("main_inventory_slots_container"),
                                                        // hotbar
                                                        Containers.grid(Sizing.content(), Sizing.fixed(18), 1, 9)
                                                                .surface(Surface.tiled(INVENTORY_SLOT_TEXTURE, 18, 18))
                                                                .id("hotbar_slots_container")
                                                ))
                                                .padding(Insets.of(4, 0, 0, 0))
                                                .id("inventory_slots_container")
                                ))
                                .padding(Insets.of(7, 7, 7, 7))
                                .surface(Surface.PANEL)
                                .id("main_inventory"),
                        Containers.verticalFlow(Sizing.fixed(130), Sizing.fixed(228))
                                .id("additional_inventory_screen_right")
                ))
        );

        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                component(GridLayout.class, "main_inventory_slots_container").child(
                        slotAsComponent(j + (i + 1) * 9)
                                .margins(Insets.of(1, 1, 1, 1)),
                        i,
                        j);
            }
        }
        for (int i = 0; i < 9; ++i) {
            component(GridLayout.class, "hotbar_slots_container").child(
                    slotAsComponent(i)
                            .margins(Insets.of(1, 1, 1, 1)),
                    0,
                    i);
        }

        updateTab();

        // disable vanilla crafting slots
//        ((OwoSlotExtension)handler.slots.get(0)).owo$setDisabledOverride(true);
//        ((OwoSlotExtension)handler.slots.get(1)).owo$setDisabledOverride(true);
//        ((OwoSlotExtension)handler.slots.get(2)).owo$setDisabledOverride(true);
//        ((OwoSlotExtension)handler.slots.get(3)).owo$setDisabledOverride(true);
//        ((OwoSlotExtension)handler.slots.get(4)).owo$setDisabledOverride(true);
//
//        // disable vanilla armor slots
//        ((OwoSlotExtension)handler.slots.get(5)).owo$setDisabledOverride(true);
//        ((OwoSlotExtension)handler.slots.get(6)).owo$setDisabledOverride(true);
//        ((OwoSlotExtension)handler.slots.get(7)).owo$setDisabledOverride(true);
//        ((OwoSlotExtension)handler.slots.get(8)).owo$setDisabledOverride(true);
//
//        // disable vanilla offhand slot
//        ((OwoSlotExtension)handler.slots.get(45)).owo$setDisabledOverride(true);

//        this.buildTrinketSlots();
    }

    private void updateTab() {

    }

//    @Override
//    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
//        if (keyCode == GLFW.GLFW_KEY_ENTER || keyCode == GLFW.GLFW_KEY_KP_ENTER) {
//            this.done();
//            return true;
//        }
//        return super.keyPressed(keyCode, scanCode, modifiers);
//    }
//
//    private boolean updateStructurePlacerBlock() {
//        PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
//
//        buf.writeBlockPos(this.structurePlacerBlock.getPos());
//
//        buf.writeString(this.component(TextBoxComponent.class, "placedStructureIdentifier").getText());
//
//        buf.writeBlockPos(new BlockPos(
//                this.parseInt(this.component(TextBoxComponent.class, "placementPositionOffsetX").getText()),
//                this.parseInt(this.component(TextBoxComponent.class, "placementPositionOffsetY").getText()),
//                this.parseInt(this.component(TextBoxComponent.class, "placementPositionOffsetZ").getText())
//        ));
//
//        buf.writeBlockPos(new BlockPos(
//                this.parseInt(this.component(TextBoxComponent.class, "triggeredBlockPositionOffsetX").getText()),
//                this.parseInt(this.component(TextBoxComponent.class, "triggeredBlockPositionOffsetY").getText()),
//                this.parseInt(this.component(TextBoxComponent.class, "triggeredBlockPositionOffsetZ").getText())
//        ));
//
//        this.client.getNetworkHandler().sendPacket(new CustomPayloadC2SPacket(BetterAdventureModCoreServerPacket.UPDATE_STRUCTURE_PLACER_BLOCK, buf));
//        return true;
//    }
//
//    private int parseInt(String string) {
//        try {
//            return Integer.parseInt(string);
//        } catch (NumberFormatException numberFormatException) {
//            return 0;
//        }
//    }
}
