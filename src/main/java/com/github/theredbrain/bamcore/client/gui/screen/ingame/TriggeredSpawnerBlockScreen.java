package com.github.theredbrain.bamcore.client.gui.screen.ingame;

import com.github.theredbrain.bamcore.block.entity.TriggeredSpawnerBlockEntity;
import com.github.theredbrain.bamcore.network.packet.UpdateTriggeredSpawnerBlockPacket;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.CyclingButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.util.NarratorManager;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import org.lwjgl.glfw.GLFW;

@Environment(value= EnvType.CLIENT)
public class TriggeredSpawnerBlockScreen extends Screen {
    private static final Text SPAWNING_MODE_LABEL_TEXT = Text.translatable("gui.triggered_spawner_block.spawning_mode_label");
    private static final Text ENTITY_SPAWN_POSITION_OFFSET_LABEL_TEXT = Text.translatable("gui.triggered_spawner_block.entity_spawn_position_offset_label");
    private static final Text ENTITY_SPAWN_ORIENTATION_LABEL_TEXT = Text.translatable("gui.triggered_spawner_block.entity_spawn_orientation_label");
    private static final Text ENTITY_TYPE_LABEL_TEXT = Text.translatable("gui.triggered_spawner_block.entity_type_label");
    private final TriggeredSpawnerBlockEntity triggeredSpawnerBlock;
    private TextFieldWidget entityTypeIdField;
    private TextFieldWidget entitySpawnPositionOffsetXField;
    private TextFieldWidget entitySpawnPositionOffsetYField;
    private TextFieldWidget entitySpawnPositionOffsetZField;
//    private TextFieldWidget entitySpawnOrientationYawField;
//    private TextFieldWidget entitySpawnOrientationPitchField;

    private TriggeredSpawnerBlockEntity.SpawningMode spawningMode;

    public TriggeredSpawnerBlockScreen(TriggeredSpawnerBlockEntity triggeredSpawnerBlock) {
        super(NarratorManager.EMPTY);
        this.triggeredSpawnerBlock = triggeredSpawnerBlock;
    }

    private void done() {
        this.updateTriggeredSpawnerBlock();
        this.close();
    }

    private void cancel() {
        this.close();
    }

    @Override
    protected void init() {
        this.entitySpawnPositionOffsetXField = new TextFieldWidget(this.textRenderer, this.width / 2 - 154, 70, 100, 20, Text.translatable(""));
        this.entitySpawnPositionOffsetXField.setText(Integer.toString(this.triggeredSpawnerBlock.getEntitySpawnPositionOffset().getX()));
        this.addSelectableChild(this.entitySpawnPositionOffsetXField);
        this.entitySpawnPositionOffsetYField = new TextFieldWidget(this.textRenderer, this.width / 2 - 50, 70, 100, 20, Text.translatable(""));
        this.entitySpawnPositionOffsetYField.setText(Integer.toString(this.triggeredSpawnerBlock.getEntitySpawnPositionOffset().getY()));
        this.addSelectableChild(this.entitySpawnPositionOffsetYField);
        this.entitySpawnPositionOffsetZField = new TextFieldWidget(this.textRenderer, this.width / 2 + 54, 70, 100, 20, Text.translatable(""));
        this.entitySpawnPositionOffsetZField.setText(Integer.toString(this.triggeredSpawnerBlock.getEntitySpawnPositionOffset().getZ()));
        this.addSelectableChild(this.entitySpawnPositionOffsetZField);

//        this.entitySpawnOrientationYawField = new TextFieldWidget(this.textRenderer, this.width / 2 - 154, 105, 150, 20, Text.translatable(""));
//        this.entitySpawnOrientationYawField.setText(Double.toString(this.triggeredSpawnerBlock.getEntitySpawnOrientationYaw()));
//        this.addSelectableChild(this.entitySpawnOrientationYawField);
//        this.entitySpawnOrientationPitchField = new TextFieldWidget(this.textRenderer, this.width / 2 + 4, 105, 150, 20, Text.translatable(""));
//        this.entitySpawnOrientationPitchField.setText(Double.toString(this.triggeredSpawnerBlock.getEntitySpawnOrientationPitch()));
//        this.addSelectableChild(this.entitySpawnOrientationPitchField);

        this.spawningMode = this.triggeredSpawnerBlock.getSpawningMode();
        int i = this.textRenderer.getWidth(SPAWNING_MODE_LABEL_TEXT) + 10;
        this.addDrawableChild(CyclingButtonWidget.builder(TriggeredSpawnerBlockEntity.SpawningMode::asText).values((TriggeredSpawnerBlockEntity.SpawningMode[]) TriggeredSpawnerBlockEntity.SpawningMode.values()).initially(this.spawningMode).omitKeyText().build(this.width / 2 - 152 + i, 130, 300 - i, 20, Text.empty(), (button, spawningMode) -> {
            this.spawningMode = spawningMode;
        }));

        this.entityTypeIdField = new TextFieldWidget(this.textRenderer, this.width / 2 - 154, 165, 300, 20, Text.translatable(""));
        this.entityTypeIdField.setMaxLength(128);
        this.entityTypeIdField.setText(this.triggeredSpawnerBlock.getEntityTypeId());
        this.addSelectableChild(this.entityTypeIdField);

        this.addDrawableChild(ButtonWidget.builder(ScreenTexts.DONE, button -> this.done()).dimensions(this.width / 2 - 4 - 150, 190, 150, 20).build());
        this.addDrawableChild(ButtonWidget.builder(ScreenTexts.CANCEL, button -> this.cancel()).dimensions(this.width / 2 + 4, 190, 150, 20).build());
        this.setInitialFocus(this.entitySpawnPositionOffsetXField);
    }

    @Override
    public void resize(MinecraftClient client, int width, int height) {
        String string = this.entitySpawnPositionOffsetXField.getText();
        String string1 = this.entitySpawnPositionOffsetYField.getText();
        String string2 = this.entitySpawnPositionOffsetZField.getText();
//        String string3 = this.entitySpawnOrientationYawField.getText();
//        String string4 = this.entitySpawnOrientationPitchField.getText();
        String string5 = this.spawningMode.asString();
        String string6 = this.entityTypeIdField.getText();
        this.init(client, width, height);
        this.entitySpawnPositionOffsetXField.setText(string);
        this.entitySpawnPositionOffsetYField.setText(string1);
        this.entitySpawnPositionOffsetZField.setText(string2);
//        this.entitySpawnOrientationYawField.setText(string3);
//        this.entitySpawnOrientationPitchField.setText(string4);
        this.spawningMode = TriggeredSpawnerBlockEntity.SpawningMode.byName(string5).orElseGet(() -> TriggeredSpawnerBlockEntity.SpawningMode.ONCE);
        this.entityTypeIdField.setText(string6);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == GLFW.GLFW_KEY_ENTER || keyCode == GLFW.GLFW_KEY_KP_ENTER) {
            this.done();
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    private void updateTriggeredSpawnerBlock() {
        ClientPlayNetworking.send(
                new UpdateTriggeredSpawnerBlockPacket(
                        this.triggeredSpawnerBlock.getPos(),
                        new BlockPos(
                                this.parseInt(this.entitySpawnPositionOffsetXField.getText()),
                                this.parseInt(this.entitySpawnPositionOffsetYField.getText()),
                                this.parseInt(this.entitySpawnPositionOffsetZField.getText())
                        ),
//                        this.parseDouble(this.entitySpawnOrientationYawField.getText()),
//                        this.parseDouble(this.entitySpawnOrientationPitchField.getText()),
                        this.spawningMode.asString(),
                        this.entityTypeIdField.getText()
                )
        );
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {

        super.render(context, mouseX, mouseY, delta);

        context.drawTextWithShadow(this.textRenderer, ENTITY_SPAWN_POSITION_OFFSET_LABEL_TEXT, this.width / 2 - 153, 60, 0xA0A0A0);
        this.entitySpawnPositionOffsetXField.render(context, mouseX, mouseY, delta);
        this.entitySpawnPositionOffsetYField.render(context, mouseX, mouseY, delta);
        this.entitySpawnPositionOffsetZField.render(context, mouseX, mouseY, delta);

        context.drawTextWithShadow(this.textRenderer, ENTITY_SPAWN_ORIENTATION_LABEL_TEXT, this.width / 2 - 153, 95, 0xA0A0A0);
//        this.entitySpawnOrientationYawField.render(context, mouseX, mouseY, delta);
//        this.entitySpawnOrientationPitchField.render(context, mouseX, mouseY, delta);

        context.drawTextWithShadow(this.textRenderer, SPAWNING_MODE_LABEL_TEXT, this.width / 2 - 153, 136, 0xA0A0A0);

        context.drawTextWithShadow(this.textRenderer, ENTITY_TYPE_LABEL_TEXT, this.width / 2 - 153, 155, 0xA0A0A0);
        this.entityTypeIdField.render(context, mouseX, mouseY, delta);
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
