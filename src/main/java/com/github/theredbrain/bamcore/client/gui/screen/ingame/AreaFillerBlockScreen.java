package com.github.theredbrain.bamcore.client.gui.screen.ingame;

import com.github.theredbrain.bamcore.block.entity.AreaFillerBlockBlockEntity;
import com.github.theredbrain.bamcore.network.packet.BetterAdventureModeCoreServerPacket;
import com.github.theredbrain.bamcore.registry.BlockRegistry;
import io.netty.buffer.Unpooled;
import io.wispforest.owo.ui.base.BaseOwoScreen;
import io.wispforest.owo.ui.component.Components;
import io.wispforest.owo.ui.component.TextBoxComponent;
import io.wispforest.owo.ui.container.Containers;
import io.wispforest.owo.ui.container.FlowLayout;
import io.wispforest.owo.ui.core.*;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.c2s.play.CustomPayloadC2SPacket;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.glfw.GLFW;

import java.util.List;

@Environment(value= EnvType.CLIENT)
public class AreaFillerBlockScreen extends BaseOwoScreen<FlowLayout> {
    private final AreaFillerBlockBlockEntity areaFillerBlock;

    public AreaFillerBlockScreen(AreaFillerBlockBlockEntity areaFillerBlock) {
        super(Text.translatable(BlockRegistry.AREA_FILLER_BLOCK.getTranslationKey()));
        this.areaFillerBlock = areaFillerBlock;
    }

    private void done() {
        if (this.updateAreaFillerBlock()) {
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
        Vec3i filledAreaDimensions = this.areaFillerBlock.getFilledAreaDimensions();
        BlockPos filledAreaPositionOffset = this.areaFillerBlock.getFilledAreaPositionOffset();
        BlockPos triggeredBlockPositionOffset = this.areaFillerBlock.getTriggeredBlockPositionOffset();
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
                Components.textBox(Sizing.fill(30), this.areaFillerBlock.getFillerBlockIdentifier())
                        .tooltip(Text.translatable("gui.area_filler_block.fillerBlockIdentifier.tooltip"))
                        .margins(Insets.of(1, 1, 1, 1))
                        .id("fillerBlockIdentifier"),
                Containers.horizontalFlow(Sizing.fill(50), Sizing.content())
                        .children(List.of(
                                Components.textBox(Sizing.fill(32), Integer.toString(filledAreaDimensions.getX()))
                                        .tooltip(Text.translatable("gui.area_filler_block.filledAreaDimensionsX.tooltip"))
                                        .margins(Insets.of(1, 1, 1, 1))
                                        .id("filledAreaDimensionsX"),
                                Components.textBox(Sizing.fill(32), Integer.toString(filledAreaDimensions.getY()))
                                        .tooltip(Text.translatable("gui.area_filler_block.filledAreaDimensionsY.tooltip"))
                                        .margins(Insets.of(1, 1, 1, 1))
                                        .id("filledAreaDimensionsY"),
                                Components.textBox(Sizing.fill(32), Integer.toString(filledAreaDimensions.getZ()))
                                        .tooltip(Text.translatable("gui.area_filler_block.filledAreaDimensionsZ.tooltip"))
                                        .margins(Insets.of(1, 1, 1, 1))
                                        .id("filledAreaDimensionsZ")
                        ))
                        .verticalAlignment(VerticalAlignment.CENTER)
                        .horizontalAlignment(HorizontalAlignment.CENTER),
                Containers.horizontalFlow(Sizing.fill(50), Sizing.content())
                        .children(List.of(
                                Components.textBox(Sizing.fill(32), Integer.toString(filledAreaPositionOffset.getX()))
                                        .tooltip(Text.translatable("gui.area_filler_block.filledAreaPositionOffsetX.tooltip"))
                                        .margins(Insets.of(1, 1, 1, 1))
                                        .id("filledAreaPositionOffsetX"),
                                Components.textBox(Sizing.fill(32), Integer.toString(filledAreaPositionOffset.getY()))
                                        .tooltip(Text.translatable("gui.area_filler_block.filledAreaPositionOffsetY.tooltip"))
                                        .margins(Insets.of(1, 1, 1, 1))
                                        .id("filledAreaPositionOffsetY"),
                                Components.textBox(Sizing.fill(32), Integer.toString(filledAreaPositionOffset.getZ()))
                                        .tooltip(Text.translatable("gui.area_filler_block.filledAreaPositionOffsetZ.tooltip"))
                                        .margins(Insets.of(1, 1, 1, 1))
                                        .id("filledAreaPositionOffsetZ")
                        ))
                        .verticalAlignment(VerticalAlignment.CENTER)
                        .horizontalAlignment(HorizontalAlignment.CENTER),
                Containers.horizontalFlow(Sizing.fill(50), Sizing.content())
                        .children(List.of(
                                Components.textBox(Sizing.fill(32), Integer.toString(triggeredBlockPositionOffset.getX()))
                                        .tooltip(Text.translatable("gui.triggered_block.triggeredBlockPositionOffsetX.tooltip"))
                                        .margins(Insets.of(1, 1, 1, 1))
                                        .id("triggeredBlockPositionOffsetX"),
                                Components.textBox(Sizing.fill(32), Integer.toString(triggeredBlockPositionOffset.getY()))
                                        .tooltip(Text.translatable("gui.triggered_block.triggeredBlockPositionOffsetY.tooltip"))
                                        .margins(Insets.of(1, 1, 1, 1))
                                        .id("triggeredBlockPositionOffsetY"),
                                Components.textBox(Sizing.fill(32), Integer.toString(triggeredBlockPositionOffset.getZ()))
                                        .tooltip(Text.translatable("gui.triggered_block.triggeredBlockPositionOffsetZ.tooltip"))
                                        .margins(Insets.of(1, 1, 1, 1))
                                        .id("triggeredBlockPositionOffsetZ")
                        ))
                        .verticalAlignment(VerticalAlignment.CENTER)
                        .horizontalAlignment(HorizontalAlignment.CENTER),
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
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == GLFW.GLFW_KEY_ENTER || keyCode == GLFW.GLFW_KEY_KP_ENTER) {
            this.done();
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    private boolean updateAreaFillerBlock() {
        PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());

        buf.writeBlockPos(this.areaFillerBlock.getPos());

        buf.writeString(this.component(TextBoxComponent.class, "fillerBlockIdentifier").getText());

        buf.writeInt(this.parseInt(this.component(TextBoxComponent.class, "filledAreaDimensionsX").getText()));
        buf.writeInt(this.parseInt(this.component(TextBoxComponent.class, "filledAreaDimensionsY").getText()));
        buf.writeInt(this.parseInt(this.component(TextBoxComponent.class, "filledAreaDimensionsZ").getText()));

        buf.writeBlockPos(new BlockPos(
                this.parseInt(this.component(TextBoxComponent.class, "filledAreaPositionOffsetX").getText()),
                this.parseInt(this.component(TextBoxComponent.class, "filledAreaPositionOffsetY").getText()),
                this.parseInt(this.component(TextBoxComponent.class, "filledAreaPositionOffsetZ").getText())
        ));

        buf.writeBlockPos(new BlockPos(
                this.parseInt(this.component(TextBoxComponent.class, "triggeredBlockPositionOffsetX").getText()),
                this.parseInt(this.component(TextBoxComponent.class, "triggeredBlockPositionOffsetY").getText()),
                this.parseInt(this.component(TextBoxComponent.class, "triggeredBlockPositionOffsetZ").getText())
        ));

        this.client.getNetworkHandler().sendPacket(new CustomPayloadC2SPacket(BetterAdventureModeCoreServerPacket.UPDATE_AREA_FILLER_BLOCK, buf));
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
