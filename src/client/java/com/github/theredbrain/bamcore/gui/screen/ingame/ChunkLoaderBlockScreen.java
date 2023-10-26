package com.github.theredbrain.bamcore.gui.screen.ingame;

import com.github.theredbrain.bamcore.block.entity.ChunkLoaderBlockBlockEntity;
import com.github.theredbrain.bamcore.network.packet.BetterAdventureModeCoreServerPacket;
import com.github.theredbrain.bamcore.registry.BlockRegistry;
import io.netty.buffer.Unpooled;
import io.wispforest.owo.ui.base.BaseOwoScreen;
import io.wispforest.owo.ui.component.ButtonComponent;
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
import org.jetbrains.annotations.NotNull;
import org.lwjgl.glfw.GLFW;

import java.util.List;

@Environment(value= EnvType.CLIENT)
public class ChunkLoaderBlockScreen extends BaseOwoScreen<FlowLayout> {
    private final ChunkLoaderBlockBlockEntity chunkLoaderBlock;
    private boolean loadChunk;

    public ChunkLoaderBlockScreen(ChunkLoaderBlockBlockEntity chunkLoaderBlock) {
        super(Text.translatable(BlockRegistry.CHUNK_LOADER_BLOCK.getTranslationKey()));
        this.chunkLoaderBlock = chunkLoaderBlock;
        this.loadChunk = chunkLoaderBlock.getLoadChunk();
    }

    private void done() {
        if (this.updateChunkLoaderBlock()) {
            this.close();
        }
    }

    private void toggleLoadChunk() {
        if (this.loadChunk) {
            this.loadChunk = false;
        } else {
            this.loadChunk = true;
        }
        this.component(ButtonComponent.class, "loadChunkButton").setMessage(this.loadChunk ? Text.translatable("gui.chunk_loader_block.loadChunkButton.yes") : Text.translatable("gui.chunk_loader_block.loadChunkButton.no"));
        this.component(ButtonComponent.class, "loadChunkButton").tooltip(this.loadChunk ? Text.translatable("gui.chunk_loader_block.loadChunkButton.yes.tooltip") : Text.translatable("gui.chunk_loader_block.loadChunkButton.no.tooltip"));
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
        BlockPos triggeredBlockPositionOffset = this.chunkLoaderBlock.getTriggeredBlockPositionOffset();
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
                Containers.horizontalFlow(Sizing.fill(50), Sizing.content())
                        .children(List.of(
                                Components.textBox(Sizing.fill(49), Integer.toString(chunkLoaderBlock.getStartChunkX()))
                                        .tooltip(Text.translatable("gui.chunk_loader_block.startChunkX.tooltip"))
                                        .margins(Insets.of(1, 1, 1, 1))
                                        .id("startChunkX"),
                                Components.textBox(Sizing.fill(49), Integer.toString(chunkLoaderBlock.getStartChunkZ()))
                                        .tooltip(Text.translatable("gui.chunk_loader_block.startChunkZ.tooltip"))
                                        .margins(Insets.of(1, 1, 1, 1))
                                        .id("startChunkZ")
                        ))
                        .verticalAlignment(VerticalAlignment.CENTER)
                        .horizontalAlignment(HorizontalAlignment.CENTER),
                Containers.horizontalFlow(Sizing.fill(50), Sizing.content())
                        .children(List.of(
                                Components.textBox(Sizing.fill(49), Integer.toString(chunkLoaderBlock.getEndChunkX()))
                                        .tooltip(Text.translatable("gui.chunk_loader_block.endChunkX.tooltip"))
                                        .margins(Insets.of(1, 1, 1, 1))
                                        .id("endChunkX"),
                                Components.textBox(Sizing.fill(49), Integer.toString(chunkLoaderBlock.getEndChunkZ()))
                                        .tooltip(Text.translatable("gui.chunk_loader_block.endChunkZ.tooltip"))
                                        .margins(Insets.of(1, 1, 1, 1))
                                        .id("endChunkZ")
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
                Components.button(this.loadChunk ? Text.translatable("gui.chunk_loader_block.loadChunkButton.yes") : Text.translatable("gui.chunk_loader_block.loadChunkButton.no"),
                                button -> this.toggleLoadChunk())
                        .sizing(Sizing.fill(30), Sizing.content())
                        .tooltip(this.loadChunk ? Text.translatable("gui.chunk_loader_block.loadChunkButton.yes.tooltip") : Text.translatable("gui.chunk_loader_block.loadChunkButton.no.tooltip"))
                        .id("loadChunkButton"),
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

    private boolean updateChunkLoaderBlock() {
        PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());

        buf.writeBlockPos(this.chunkLoaderBlock.getPos());

        buf.writeBoolean(this.loadChunk);

        buf.writeInt(this.parseInt(this.component(TextBoxComponent.class, "startChunkX").getText()));
        buf.writeInt(this.parseInt(this.component(TextBoxComponent.class, "startChunkZ").getText()));
        buf.writeInt(this.parseInt(this.component(TextBoxComponent.class, "endChunkX").getText()));
        buf.writeInt(this.parseInt(this.component(TextBoxComponent.class, "endChunkZ").getText()));

        buf.writeBlockPos(new BlockPos(
                this.parseInt(this.component(TextBoxComponent.class, "triggeredBlockPositionOffsetX").getText()),
                this.parseInt(this.component(TextBoxComponent.class, "triggeredBlockPositionOffsetY").getText()),
                this.parseInt(this.component(TextBoxComponent.class, "triggeredBlockPositionOffsetZ").getText())
        ));

        this.client.getNetworkHandler().sendPacket(new CustomPayloadC2SPacket(BetterAdventureModeCoreServerPacket.UPDATE_CHUNK_LOADER_BLOCK, buf));
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
