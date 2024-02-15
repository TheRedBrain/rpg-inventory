package com.github.theredbrain.betteradventuremode.client.gui.screen.ingame;

import com.github.theredbrain.betteradventuremode.util.ItemUtils;
import com.github.theredbrain.betteradventuremode.block.entity.JigsawPlacerBlockEntity;
import com.github.theredbrain.betteradventuremode.network.packet.UpdateJigsawPlacerBlockPacket;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.block.JigsawBlock;
import net.minecraft.block.entity.JigsawBlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.CyclingButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.util.NarratorManager;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import org.lwjgl.glfw.GLFW;

@Environment(value= EnvType.CLIENT)
public class JigsawPlacerBlockScreen extends Screen {
    private static final Text JOINT_LABEL_TEXT = Text.translatable("jigsaw_block.joint_label");
    private static final Text POOL_TEXT = Text.translatable("jigsaw_block.pool");
    private static final Text TARGET_TEXT = Text.translatable("jigsaw_block.target");
    private static final Text TRIGGERED_BLOCK_POSITION_TEXT = Text.translatable("gui.triggered_block.triggeredBlockPositionOffset");
    private final JigsawPlacerBlockEntity jigsawPlacerBlock;
    private TextFieldWidget targetField;
    private TextFieldWidget poolField;
    private CyclingButtonWidget<JigsawBlockEntity.Joint> jointRotationButton;
    private TextFieldWidget triggeredBlockPositionOffsetXField;
    private TextFieldWidget triggeredBlockPositionOffsetYField;
    private TextFieldWidget triggeredBlockPositionOffsetZField;
    private CyclingButtonWidget<Boolean> toggleTriggeredBlockResetsButton;
    private ButtonWidget doneButton;
    private JigsawBlockEntity.Joint joint;
    private boolean triggeredBlockResets;

    public JigsawPlacerBlockScreen(JigsawPlacerBlockEntity jigsawPlacerBlock) {
        super(NarratorManager.EMPTY);
        this.jigsawPlacerBlock = jigsawPlacerBlock;
    }

    private void done() {
        if (this.updateJigsawPlacerBlock()) {
            this.close();
        }
    }

    private void cancel() {
        this.close();
    }

    @Override
    protected void init() {
        boolean bl;
        this.poolField = new TextFieldWidget(this.textRenderer, this.width / 2 - 153, 55, 300, 20, Text.translatable("jigsaw_block.pool"));
        this.poolField.setMaxLength(128);
        this.poolField.setText(this.jigsawPlacerBlock.getPool().getValue().toString());
        this.poolField.setChangedListener(pool -> this.updateDoneButtonState());
        this.addSelectableChild(this.poolField);
        this.targetField = new TextFieldWidget(this.textRenderer, this.width / 2 - 153, 90, 300, 20, Text.translatable("jigsaw_block.target"));
        this.targetField.setMaxLength(128);
        this.targetField.setText(this.jigsawPlacerBlock.getTarget().toString());
        this.targetField.setChangedListener(target -> this.updateDoneButtonState());
        this.addSelectableChild(this.targetField);
        this.joint = this.jigsawPlacerBlock.getJoint();
        int i = this.textRenderer.getWidth(JOINT_LABEL_TEXT) + 10;
        this.jointRotationButton = this.addDrawableChild(CyclingButtonWidget.builder(JigsawBlockEntity.Joint::asText).values((JigsawBlockEntity.Joint[])JigsawBlockEntity.Joint.values()).initially(this.joint).omitKeyText().build(this.width / 2 - 152 + i, 115, 300 - i, 20, JOINT_LABEL_TEXT, (button, joint) -> {
            this.joint = joint;
        }));
        this.jointRotationButton.active = bl = JigsawBlock.getFacing(this.jigsawPlacerBlock.getCachedState()).getAxis().isVertical();
        this.jointRotationButton.visible = bl;

        this.triggeredBlockPositionOffsetXField = new TextFieldWidget(this.textRenderer, this.width / 2 - 154, 150, 50, 20, Text.translatable(""));
        this.triggeredBlockPositionOffsetXField.setMaxLength(128);
        this.triggeredBlockPositionOffsetXField.setText(Integer.toString(this.jigsawPlacerBlock.getTriggeredBlock().getLeft().getX()));
        this.addSelectableChild(this.triggeredBlockPositionOffsetXField);
        this.triggeredBlockPositionOffsetYField = new TextFieldWidget(this.textRenderer, this.width / 2 - 100, 150, 50, 20, Text.translatable(""));
        this.triggeredBlockPositionOffsetYField.setMaxLength(128);
        this.triggeredBlockPositionOffsetYField.setText(Integer.toString(this.jigsawPlacerBlock.getTriggeredBlock().getLeft().getY()));
        this.addSelectableChild(this.triggeredBlockPositionOffsetYField);
        this.triggeredBlockPositionOffsetZField = new TextFieldWidget(this.textRenderer, this.width / 2 - 46, 150, 50, 20, Text.translatable(""));
        this.triggeredBlockPositionOffsetZField.setMaxLength(128);
        this.triggeredBlockPositionOffsetZField.setText(Integer.toString(this.jigsawPlacerBlock.getTriggeredBlock().getLeft().getZ()));
        this.addSelectableChild(this.triggeredBlockPositionOffsetZField);
        this.triggeredBlockResets = this.jigsawPlacerBlock.getTriggeredBlock().getRight();
        this.toggleTriggeredBlockResetsButton = this.addDrawableChild(CyclingButtonWidget.onOffBuilder(Text.translatable("gui.triggered_block.toggle_triggered_block_resets_button_label.on"), Text.translatable("gui.triggered_block.toggle_triggered_block_resets_button_label.off")).initially(this.triggeredBlockResets).omitKeyText().build(this.width / 2 + 8, 150, 150, 20, Text.empty(), (button, triggeredBlockResets) -> {
            this.triggeredBlockResets = triggeredBlockResets;
        }));

        this.doneButton = this.addDrawableChild(ButtonWidget.builder(ScreenTexts.DONE, button -> this.done()).dimensions(this.width / 2 - 4 - 150, 180, 150, 20).build());
        this.addDrawableChild(ButtonWidget.builder(ScreenTexts.CANCEL, button -> this.cancel()).dimensions(this.width / 2 + 4, 180, 150, 20).build());
        this.setInitialFocus(this.poolField);
        this.updateDoneButtonState();
    }

    private void updateDoneButtonState() {
        this.doneButton.active = Identifier.isValid(this.targetField.getText()) && Identifier.isValid(this.poolField.getText());
    }

    @Override
    public void resize(MinecraftClient client, int width, int height) {
        String string = this.targetField.getText();
        String string1 = this.poolField.getText();
        String string2 = this.triggeredBlockPositionOffsetXField.getText();
        String string3 = this.triggeredBlockPositionOffsetYField.getText();
        String string4 = this.triggeredBlockPositionOffsetZField.getText();
        JigsawBlockEntity.Joint joint = this.joint;
        boolean bl = this.triggeredBlockResets;
        this.init(client, width, height);
        this.targetField.setText(string);
        this.poolField.setText(string1);
        this.triggeredBlockPositionOffsetXField.setText(string2);
        this.triggeredBlockPositionOffsetYField.setText(string3);
        this.triggeredBlockPositionOffsetZField.setText(string4);
        this.joint = joint;
        this.jointRotationButton.setValue(joint);
        this.triggeredBlockResets = bl;
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (super.keyPressed(keyCode, scanCode, modifiers)) {
            return true;
        }
        if (this.doneButton.active && (keyCode == GLFW.GLFW_KEY_ENTER || keyCode == GLFW.GLFW_KEY_KP_ENTER)) {
            this.done();
            return true;
        }
        return false;
    }

    private boolean updateJigsawPlacerBlock() {
        ClientPlayNetworking.send(new UpdateJigsawPlacerBlockPacket(
                this.jigsawPlacerBlock.getPos(),
                this.targetField.getText(),
                this.poolField.getText(),
                this.joint,
                new BlockPos(
                        ItemUtils.parseInt(this.triggeredBlockPositionOffsetXField.getText()),
                        ItemUtils.parseInt(this.triggeredBlockPositionOffsetYField.getText()),
                        ItemUtils.parseInt(this.triggeredBlockPositionOffsetZField.getText())
                ),
                this.triggeredBlockResets
        ));
        return true;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        context.drawTextWithShadow(this.textRenderer, POOL_TEXT, this.width / 2 - 153, 45, 0xA0A0A0);
        this.poolField.render(context, mouseX, mouseY, delta);
        context.drawTextWithShadow(this.textRenderer, TARGET_TEXT, this.width / 2 - 153, 80, 0xA0A0A0);
        this.targetField.render(context, mouseX, mouseY, delta);
        if (JigsawBlock.getFacing(this.jigsawPlacerBlock.getCachedState()).getAxis().isVertical()) {
            context.drawTextWithShadow(this.textRenderer, JOINT_LABEL_TEXT, this.width / 2 - 153, 121, 0xFFFFFF);
        }
        context.drawTextWithShadow(this.textRenderer, TRIGGERED_BLOCK_POSITION_TEXT, this.width / 2 - 153, 140, 0xA0A0A0);
        this.triggeredBlockPositionOffsetXField.render(context, mouseX, mouseY, delta);
        this.triggeredBlockPositionOffsetYField.render(context, mouseX, mouseY, delta);
        this.triggeredBlockPositionOffsetZField.render(context, mouseX, mouseY, delta);
    }

    @Override
    public boolean shouldPause() {
        return false;
    }
}
