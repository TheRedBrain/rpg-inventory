package com.github.theredbrain.bamcore.client.gui.screen.ingame;

import com.github.theredbrain.bamcore.block.entity.RelayTriggerBlockBlockEntity;
import com.github.theredbrain.bamcore.network.packet.BetterAdventureModCoreServerPacket;
import com.github.theredbrain.bamcore.registry.BlockRegistry;
import io.netty.buffer.Unpooled;
import io.wispforest.owo.ui.base.BaseOwoScreen;
import io.wispforest.owo.ui.component.Components;
import io.wispforest.owo.ui.component.TextBoxComponent;
import io.wispforest.owo.ui.container.Containers;
import io.wispforest.owo.ui.container.FlowLayout;
import io.wispforest.owo.ui.container.ScrollContainer;
import io.wispforest.owo.ui.core.*;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.c2s.play.CustomPayloadC2SPacket;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.glfw.GLFW;

import java.util.List;

@Environment(value= EnvType.CLIENT)
public class RelayTriggerBlockScreen extends BaseOwoScreen<FlowLayout> {
    private final RelayTriggerBlockBlockEntity relayTriggerBlock;

    public RelayTriggerBlockScreen(RelayTriggerBlockBlockEntity relayTriggerBlock) {
        super(Text.translatable(BlockRegistry.RELAY_TRIGGER_BLOCK.getTranslationKey()));
        this.relayTriggerBlock = relayTriggerBlock;
    }

    private void addTriggeredBlockEntry() {
        FlowLayout creativeScreenScrollingContent = component(FlowLayout.class, "creativeScreenScrollingContent");

        creativeScreenScrollingContent.child(
                Containers.horizontalFlow(Sizing.fill(100), Sizing.content())
                        .children(List.of(
                                Components.textBox(Sizing.fill(17), Integer.toString(0))
                                        .tooltip(Text.translatable("gui.triggered_block.triggeredBlockPositionOffsetX.tooltip"))
                                        .margins(Insets.of(1, 1, 1, 1))
                                        .id("triggeredBlockPositionOffsetX"),
                                Components.textBox(Sizing.fill(17), Integer.toString(0))
                                        .tooltip(Text.translatable("gui.triggered_block.triggeredBlockPositionOffsetY.tooltip"))
                                        .margins(Insets.of(1, 1, 1, 1))
                                        .id("triggeredBlockPositionOffsetY"),
                                Components.textBox(Sizing.fill(17), Integer.toString(0))
                                        .tooltip(Text.translatable("gui.triggered_block.triggeredBlockPositionOffsetZ.tooltip"))
                                        .margins(Insets.of(1, 1, 1, 1))
                                        .id("triggeredBlockPositionOffsetZ"),
                                Components.button(Text.translatable("gui.relay_trigger_block.deleteTriggeredBlockEntryButton"), button -> this.deleteTriggeredBlockEntry(button.parent()))
                                        .sizing(Sizing.fill(25), Sizing.fixed(20))
                                        .id("deleteTriggeredBlockEntryButton")
                        ))
                        .verticalAlignment(VerticalAlignment.CENTER)
                        .horizontalAlignment(HorizontalAlignment.LEFT));
    }

    private void deleteTriggeredBlockEntry(Component parent) {
        parent.remove();
    }

    private void done() {
        if (this.updateRelayTriggerBlock()) {
            this.close();
        }
    }

    private void cancel() {
        this.close();
    }

    @Override
    protected @NotNull OwoUIAdapter<FlowLayout> createAdapter() {
        return OwoUIAdapter.create(this, Containers::verticalFlow);
    }

    @Override
    protected void build(FlowLayout rootComponent) {
            List<BlockPos> triggeredBlocks = this.relayTriggerBlock.getTriggeredBlocks();
            rootComponent
                    .surface(Surface.VANILLA_TRANSLUCENT)
                    .horizontalAlignment(HorizontalAlignment.CENTER)
                    .verticalAlignment(VerticalAlignment.CENTER);
            rootComponent.children(List.of(
                    Components.label(this.title)
                            .shadow(true)
                            .color(Color.ofArgb(0xFFFFFF))
                            .margins(Insets.of(1, 1, 1, 1))
                            .sizing(Sizing.content(), Sizing.content()),
                    // scrolling container
                    Containers.verticalScroll(Sizing.fill(50), Sizing.fill(50),
                            Containers.verticalFlow(Sizing.fill(98), Sizing.content())
                                    .verticalAlignment(VerticalAlignment.CENTER)
                                    .horizontalAlignment(HorizontalAlignment.CENTER)
                                    .id("creativeScreenScrollingContent"))
                            .scrollbar(ScrollContainer.Scrollbar.vanilla())
                            .verticalAlignment(VerticalAlignment.CENTER)
                            .horizontalAlignment(HorizontalAlignment.LEFT),
                    Components.button(Text.translatable("gui.relay_trigger_block.addTriggeredBlockEntryButton"), button -> this.addTriggeredBlockEntry())
                            .sizing(Sizing.fill(30), Sizing.fixed(20)),
                    Containers.horizontalFlow(Sizing.fill(30), Sizing.content())
                            .children(List.of(
                                    Components.button(Text.translatable("gui.save"), button -> this.done())
                                            .sizing(Sizing.fill(49), Sizing.fixed(20)),
                                    Components.button(ScreenTexts.CANCEL, button -> this.cancel())
                                            .sizing(Sizing.fill(49), Sizing.fixed(20))
                            ))
                            .margins(Insets.of(1, 1, 1, 1))
                            .verticalAlignment(VerticalAlignment.CENTER)
                            .horizontalAlignment(HorizontalAlignment.CENTER)
            ));
            for (BlockPos triggeredBlock : triggeredBlocks) {
                    rootComponent.childById(FlowLayout.class, "creativeScreenScrollingContent").children(List.of(
                            Containers.horizontalFlow(Sizing.fill(100), Sizing.content())
                                    .children(List.of(
                                            Components.textBox(Sizing.fill(17), Integer.toString(triggeredBlock.getX()))
                                                    .tooltip(Text.translatable("gui.triggered_block.triggeredBlockPositionOffsetX.tooltip"))
                                                    .margins(Insets.of(1, 1, 1, 1))
                                                    .id("triggeredBlockPositionOffset"),
                                            Components.textBox(Sizing.fill(17), Integer.toString(triggeredBlock.getY()))
                                                    .tooltip(Text.translatable("gui.triggered_block.triggeredBlockPositionOffsetY.tooltip"))
                                                    .margins(Insets.of(1, 1, 1, 1))
                                                    .id("triggeredBlockPositionOffsetY"),
                                            Components.textBox(Sizing.fill(17), Integer.toString(triggeredBlock.getZ()))
                                                    .tooltip(Text.translatable("gui.triggered_block.triggeredBlockPositionOffsetZ.tooltip"))
                                                    .margins(Insets.of(1, 1, 1, 1))
                                                    .id("triggeredBlockPositionOffsetZ"),
                                            Components.button(Text.translatable("gui.relay_trigger_block.deleteTriggeredBlockEntryButton"), button -> this.deleteTriggeredBlockEntry(button.parent()))
                                                    .sizing(Sizing.fill(25), Sizing.fixed(20))
                                                    .id("deleteTriggeredBlockEntryButton")
                                    ))
                                    .verticalAlignment(VerticalAlignment.CENTER)
                                    .horizontalAlignment(HorizontalAlignment.LEFT)
                    ));
            }
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == GLFW.GLFW_KEY_ENTER || keyCode == GLFW.GLFW_KEY_KP_ENTER) {
            this.done();
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    private boolean updateRelayTriggerBlock() {
        PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());

        // teleporter block position
        buf.writeBlockPos(this.relayTriggerBlock.getPos());

        List<Component> list = component(FlowLayout.class, "creativeScreenScrollingContent").children();

        int creativeScreenScrollingContentSize = list.size();

        buf.writeInt(creativeScreenScrollingContentSize);

        for (int i = 0; i < creativeScreenScrollingContentSize; i++) {
            FlowLayout triggeredBlockEntry = (FlowLayout) list.get(i);

            buf.writeBlockPos(new BlockPos(
                    this.parseInt(((TextBoxComponent)(triggeredBlockEntry.children().get(0))).getText()),
                    this.parseInt(((TextBoxComponent)(triggeredBlockEntry.children().get(1))).getText()),
                    this.parseInt(((TextBoxComponent)(triggeredBlockEntry.children().get(2))).getText())
            ));
//            buf.writeInt(this.parseInt(((TextBoxComponent)(triggeredBlockEntry.children().get(3))).getText()));
        }

        this.client.getNetworkHandler().sendPacket(new CustomPayloadC2SPacket(BetterAdventureModCoreServerPacket.UPDATE_RELAY_TRIGGER_BLOCK, buf));
        return true;
    }

    private int parseInt(String string) {
        try {
            return Integer.parseInt(string);
        } catch (NumberFormatException numberFormatException) {
            return 0;
        }
    }
}
