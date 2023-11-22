package com.github.theredbrain.bamcore.client.gui.screen.ingame;

import com.github.theredbrain.bamcore.block.entity.JigsawPlacerBlockBlockEntity;
import com.github.theredbrain.bamcore.client.owo.CustomTextBoxComponent;
import com.github.theredbrain.bamcore.network.packet.UpdateJigsawPlacerBlockPacket;
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
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.block.JigsawBlock;
import net.minecraft.block.entity.JigsawBlockEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.glfw.GLFW;

import java.util.List;

@Environment(value= EnvType.CLIENT)
public class JigsawPlacerBlockScreen extends BaseOwoScreen<FlowLayout> {
    private final JigsawPlacerBlockBlockEntity jigsawPlacerBlock;
    private JigsawBlockEntity.Joint joint;

    public JigsawPlacerBlockScreen(JigsawPlacerBlockBlockEntity jigsawPlacerBlock) {
        super(Text.translatable(BlockRegistry.JIGSAW_PLACER_BLOCK.getTranslationKey()));
        this.jigsawPlacerBlock = jigsawPlacerBlock;
        this.joint = jigsawPlacerBlock.getJoint();
    }

    private void done() {
        if (this.updateJigsawPlacerBlock()) {
            this.close();
        }
    }

    private void cancel() {
        this.close();
    }

    private void toggleJoint() {
        if (this.joint.equals(JigsawBlockEntity.Joint.ALIGNED)) {
            this.joint = JigsawBlockEntity.Joint.ROLLABLE;
        } else {
            this.joint = JigsawBlockEntity.Joint.ALIGNED;
        }
        this.component(ButtonComponent.class, "toggleJointButton").setMessage(this.joint.asText());
    }

    @Override
    protected @NotNull OwoUIAdapter<FlowLayout> createAdapter() {
        return OwoUIAdapter.create(this, Containers::verticalFlow);
    }

    @Override
    protected void build(FlowLayout rootComponent) {
        BlockPos triggeredBlockPositionOffset = this.jigsawPlacerBlock.getTriggeredBlockPositionOffset();
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
                new CustomTextBoxComponent(Sizing.fill(30), 128)
                        .text(this.jigsawPlacerBlock.getTarget().toString())
                        .tooltip(Text.translatable("gui.jigsaw_placer_block.target.tooltip"))
                        .margins(Insets.of(1, 1, 1, 1))
                        .id("targetTextBox"),
                new CustomTextBoxComponent(Sizing.fill(30), 128)
                        .text(this.jigsawPlacerBlock.getPool().getValue().toString())
                        .tooltip(Text.translatable("gui.jigsaw_placer_block.pool.tooltip"))
                        .margins(Insets.of(1, 1, 1, 1))
                        .id("poolTextBox"),
                Containers.horizontalFlow(Sizing.content(), Sizing.content())
                        .id("toggleJointButtonContainer"),
                Containers.horizontalFlow(Sizing.fill(30), Sizing.content())
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
        if (JigsawBlock.getFacing(this.jigsawPlacerBlock.getCachedState()).getAxis().isVertical()) {
            this.component(FlowLayout.class, "toggleJointButtonContainer").child(
                    Components.button(this.joint.asText(), button -> this.toggleJoint())
                            .sizing(Sizing.fill(30), Sizing.fixed(20))
                            .id("toggleJointButton")
            );
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

    private boolean updateJigsawPlacerBlock() {
        PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());

        buf.writeBlockPos(this.jigsawPlacerBlock.getPos());

        buf.writeString(this.component(CustomTextBoxComponent.class, "targetTextBox").getText());
        buf.writeString(this.component(CustomTextBoxComponent.class, "poolTextBox").getText());

        buf.writeString(this.joint.asString());

        buf.writeBlockPos(new BlockPos(
                this.parseInt(this.component(TextBoxComponent.class, "triggeredBlockPositionOffsetX").getText()),
                this.parseInt(this.component(TextBoxComponent.class, "triggeredBlockPositionOffsetY").getText()),
                this.parseInt(this.component(TextBoxComponent.class, "triggeredBlockPositionOffsetZ").getText())
        ));

        ClientPlayNetworking.send(new UpdateJigsawPlacerBlockPacket(
                this.jigsawPlacerBlock.getPos(),
                this.component(CustomTextBoxComponent.class, "targetTextBox").getText(),
                this.component(CustomTextBoxComponent.class, "poolTextBox").getText(),
                this.joint,
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
