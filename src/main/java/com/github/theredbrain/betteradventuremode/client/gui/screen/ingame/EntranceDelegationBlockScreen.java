package com.github.theredbrain.betteradventuremode.client.gui.screen.ingame;

import com.github.theredbrain.betteradventuremode.util.ItemUtils;
import com.github.theredbrain.betteradventuremode.block.entity.EntranceDelegationBlockEntity;
import com.github.theredbrain.betteradventuremode.network.packet.UpdateEntranceDelegationBlockPacket;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.util.NarratorManager;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;

@Environment(value= EnvType.CLIENT)
public class EntranceDelegationBlockScreen extends Screen {
    private static final Text DELEGATED_ENTRANCE_POSITION_OFFET_LABEL_TEXT = Text.translatable("gui.location_controller_block.main_entrance.position_offset");
    private static final Text DELEGATED_ENTRANCE_ORIENTATION_LABEL_TEXT = Text.translatable("gui.location_controller_block.main_entrance.orientation");
    private final EntranceDelegationBlockEntity entranceDelegationBlock;
    private TextFieldWidget mainEntrancePositionOffsetXField;
    private TextFieldWidget mainEntrancePositionOffsetYField;
    private TextFieldWidget mainEntrancePositionOffsetZField;
    private TextFieldWidget mainEntranceOrientationYawField;
    private TextFieldWidget mainEntranceOrientationPitchField;

    public EntranceDelegationBlockScreen(EntranceDelegationBlockEntity entranceDelegationBlock) {
        super(NarratorManager.EMPTY);
        this.entranceDelegationBlock = entranceDelegationBlock;
    }

    private void done() {
        if (this.updateEntranceDelegationBlock()) {
            this.close();
        }
    }

    private void cancel() {
        this.close();
    }

    @Override
    protected void init() {
        this.mainEntrancePositionOffsetXField = new TextFieldWidget(this.textRenderer, this.width / 2 - 154, 80, 100, 20, Text.empty());
        this.mainEntrancePositionOffsetXField.setMaxLength(128);
        this.mainEntrancePositionOffsetXField.setText(Integer.toString(this.entranceDelegationBlock.getDelegatedEntrance().getLeft().getX()));
        this.addSelectableChild(this.mainEntrancePositionOffsetXField);

        this.mainEntrancePositionOffsetYField = new TextFieldWidget(this.textRenderer, this.width / 2 - 50, 80, 100, 20, Text.empty());
        this.mainEntrancePositionOffsetYField.setMaxLength(128);
        this.mainEntrancePositionOffsetYField.setText(Integer.toString(this.entranceDelegationBlock.getDelegatedEntrance().getLeft().getY()));
        this.addSelectableChild(this.mainEntrancePositionOffsetYField);

        this.mainEntrancePositionOffsetZField = new TextFieldWidget(this.textRenderer, this.width / 2 + 54, 80, 100, 20, Text.empty());
        this.mainEntrancePositionOffsetZField.setMaxLength(128);
        this.mainEntrancePositionOffsetZField.setText(Integer.toString(this.entranceDelegationBlock.getDelegatedEntrance().getLeft().getZ()));
        this.addSelectableChild(this.mainEntrancePositionOffsetZField);

        this.mainEntranceOrientationYawField = new TextFieldWidget(this.textRenderer, this.width / 2 - 154, 115, 100, 20, Text.empty());
        this.mainEntranceOrientationYawField.setMaxLength(128);
        this.mainEntranceOrientationYawField.setText(Double.toString(this.entranceDelegationBlock.getDelegatedEntrance().getRight().getLeft()));
        this.addSelectableChild(this.mainEntranceOrientationYawField);

        this.mainEntranceOrientationPitchField = new TextFieldWidget(this.textRenderer, this.width / 2 - 50, 115, 100, 20, Text.empty());
        this.mainEntranceOrientationPitchField.setMaxLength(128);
        this.mainEntranceOrientationPitchField.setText(Double.toString(this.entranceDelegationBlock.getDelegatedEntrance().getRight().getRight()));
        this.addSelectableChild(this.mainEntranceOrientationPitchField);

        this.addDrawableChild(ButtonWidget.builder(ScreenTexts.DONE, button -> this.done()).dimensions(this.width / 2 - 4 - 150, 210, 150, 20).build());
        this.addDrawableChild(ButtonWidget.builder(ScreenTexts.CANCEL, button -> this.cancel()).dimensions(this.width / 2 + 4, 210, 150, 20).build());
    }

    @Override
    public void resize(MinecraftClient client, int width, int height) {
        String string = this.mainEntrancePositionOffsetXField.getText();
        String string1 = this.mainEntrancePositionOffsetYField.getText();
        String string2 = this.mainEntrancePositionOffsetZField.getText();
        String string3 = this.mainEntranceOrientationYawField.getText();
        String string4 = this.mainEntranceOrientationPitchField.getText();
        this.init(client, width, height);
        this.mainEntrancePositionOffsetXField.setText(string);
        this.mainEntrancePositionOffsetYField.setText(string1);
        this.mainEntrancePositionOffsetZField.setText(string2);
        this.mainEntranceOrientationYawField.setText(string3);
        this.mainEntranceOrientationPitchField.setText(string4);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        context.drawTextWithShadow(this.textRenderer, DELEGATED_ENTRANCE_POSITION_OFFET_LABEL_TEXT, this.width / 2 - 153, 70, 0xA0A0A0);
        this.mainEntrancePositionOffsetXField.render(context, mouseX, mouseY, delta);
        this.mainEntrancePositionOffsetYField.render(context, mouseX, mouseY, delta);
        this.mainEntrancePositionOffsetZField.render(context, mouseX, mouseY, delta);
        context.drawTextWithShadow(this.textRenderer, DELEGATED_ENTRANCE_ORIENTATION_LABEL_TEXT, this.width / 2 - 153, 105, 0xA0A0A0);
        this.mainEntranceOrientationYawField.render(context, mouseX, mouseY, delta);
        this.mainEntranceOrientationPitchField.render(context, mouseX, mouseY, delta);
    }

    @Override
    public boolean shouldPause() {
        return false;
    }

    private boolean updateEntranceDelegationBlock() {
        ClientPlayNetworking.send(new UpdateEntranceDelegationBlockPacket(
                this.entranceDelegationBlock.getPos(),
                new BlockPos(
                        ItemUtils.parseInt(this.mainEntrancePositionOffsetXField.getText()),
                        ItemUtils.parseInt(this.mainEntrancePositionOffsetYField.getText()),
                        ItemUtils.parseInt(this.mainEntrancePositionOffsetZField.getText())
                ),
                ItemUtils.parseDouble(this.mainEntranceOrientationYawField.getText()),
                ItemUtils.parseDouble(this.mainEntranceOrientationPitchField.getText())
        ));
        return true;
    }
}
