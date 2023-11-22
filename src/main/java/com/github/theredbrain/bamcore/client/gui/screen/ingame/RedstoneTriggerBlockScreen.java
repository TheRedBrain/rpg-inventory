package com.github.theredbrain.bamcore.client.gui.screen.ingame;

import com.github.theredbrain.bamcore.block.entity.RedstoneTriggerBlockBlockEntity;
import com.github.theredbrain.bamcore.network.packet.UpdateRedstoneTriggerBlockPacket;
import com.github.theredbrain.bamcore.registry.BlockRegistry;
import io.wispforest.owo.ui.base.BaseOwoScreen;
import io.wispforest.owo.ui.component.Components;
import io.wispforest.owo.ui.component.TextBoxComponent;
import io.wispforest.owo.ui.container.Containers;
import io.wispforest.owo.ui.container.FlowLayout;
import io.wispforest.owo.ui.core.*;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.glfw.GLFW;

import java.util.List;

@Environment(value= EnvType.CLIENT)
public class RedstoneTriggerBlockScreen extends BaseOwoScreen<FlowLayout> {
    private final RedstoneTriggerBlockBlockEntity redstoneTriggerBlock;

    public RedstoneTriggerBlockScreen(RedstoneTriggerBlockBlockEntity redstoneTriggerBlock) {
        super(Text.translatable(BlockRegistry.REDSTONE_TRIGGER_BLOCK.getTranslationKey()));
        this.redstoneTriggerBlock = redstoneTriggerBlock;
    }

    private void done() {
        if (this.updateRedstoneTriggerBlock()) {
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
            BlockPos triggeredBlockPositionOffset = this.redstoneTriggerBlock.getTriggeredBlockPositionOffset();
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

    private boolean updateRedstoneTriggerBlock() {
        ClientPlayNetworking.send(new UpdateRedstoneTriggerBlockPacket(
                this.redstoneTriggerBlock.getPos(),
                new BlockPos(
                        this.parseInt(this.component(TextBoxComponent.class, "triggeredBlockPositionOffsetX").getText()),
                        this.parseInt(this.component(TextBoxComponent.class, "triggeredBlockPositionOffsetY").getText()),
                        this.parseInt(this.component(TextBoxComponent.class, "triggeredBlockPositionOffsetZ").getText())
                )
        ));
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
