package com.github.theredbrain.bamcore.client.gui.screen.ingame;

import com.github.theredbrain.bamcore.BetterAdventureModeCore;
import com.github.theredbrain.bamcore.api.json_files_backend.Dialogue;
import com.github.theredbrain.bamcore.block.entity.DialogueBlockEntity;
import com.github.theredbrain.bamcore.client.network.DuckClientAdvancementManagerMixin;
import com.github.theredbrain.bamcore.network.packet.UpdateDialogueBlockPacket;
import com.github.theredbrain.bamcore.registry.DialoguesRegistry;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.advancement.AdvancementEntry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.CyclingButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.network.ClientAdvancementManager;
import net.minecraft.client.util.NarratorManager;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import org.apache.commons.lang3.tuple.MutablePair;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Environment(value= EnvType.CLIENT)
public class DialogueBlockScreen extends Screen {
    private static final Text ADD_ENTRY_BUTTON_LABEL_TEXT = Text.translatable("gui.list_entry.add");
    private static final Text REMOVE_ENTRY_BUTTON_LABEL_TEXT = Text.translatable("gui.list_entry.remove");
    public static final Identifier BACKGROUND_218_197_TEXTURE = BetterAdventureModeCore.identifier("textures/gui/container/generic_218_197_background.png");
    private static final Identifier SCROLL_BAR_BACKGROUND_8_35_TEXTURE = BetterAdventureModeCore.identifier("scroll_bar/scroll_bar_background_8_35");
    private static final Identifier SCROLL_BAR_BACKGROUND_8_87_TEXTURE = BetterAdventureModeCore.identifier("scroll_bar/scroll_bar_background_8_87");
    private static final Identifier SCROLL_BAR_BACKGROUND_8_92_TEXTURE = BetterAdventureModeCore.identifier("scroll_bar/scroll_bar_background_8_92");
    private static final Identifier SCROLL_BAR_BACKGROUND_8_96_TEXTURE = BetterAdventureModeCore.identifier("scroll_bar/scroll_bar_background_8_96");
    private static final Identifier SCROLLER_VERTICAL_6_7_TEXTURE = BetterAdventureModeCore.identifier("scroll_bar/scroller_vertical_6_7");
    private final DialogueBlockEntity dialogueBlockEntity;

    //region adventure widgets
    private ButtonWidget answerButton0;
    private ButtonWidget answerButton1;
    private ButtonWidget answerButton2;
    private ButtonWidget answerButton3;
    //endregion adventure widgets
    //region creative widgets
    private CyclingButtonWidget<CreativeScreenPage> creativeScreenPageButton;

    private ButtonWidget removeDialogueUsedBlockEntryButton0;
    private ButtonWidget removeDialogueUsedBlockEntryButton1;
    private ButtonWidget removeDialogueUsedBlockEntryButton2;
    private ButtonWidget removeDialogueUsedBlockEntryButton3;
    private TextFieldWidget newDialogueUsedBlockIdentifierField;
    private TextFieldWidget newDialogueUsedBlockPositionOffsetXField;
    private TextFieldWidget newDialogueUsedBlockPositionOffsetYField;
    private TextFieldWidget newDialogueUsedBlockPositionOffsetZField;
    private ButtonWidget addDialogueUsedBlockButton;

    private ButtonWidget removeStartingDialogueEntryButton;
    private TextFieldWidget newStartingDialogueIdentifierField;
    private TextFieldWidget newStartingDialogueLockAdvancementIdentifierField;
    private TextFieldWidget newStartingDialogueUnlockAdvancementIdentifierField;
    private ButtonWidget addStartingDialogueButton;

    private ButtonWidget saveCreativeButton;
    private ButtonWidget cancelCreativeButton;
    //endregion creative widgets
    private final @Nullable Dialogue dialogue;
    private final boolean showCreativeScreen;
    private CreativeScreenPage creativeScreenPage;
    private List<MutablePair<String, BlockPos>> dialogueUsedBlocksList = new ArrayList<>(List.of());
    private List<MutablePair<String, MutablePair<String, String>>> startingDialogueList = new ArrayList<>(List.of());
    private List<Dialogue.Answer> unlockedAnswersList = new ArrayList<>(List.of());
    private List<Dialogue.Answer> visibleAnswersList = new ArrayList<>(List.of());
    private List<String> dialogueTextList = new ArrayList<>(List.of());
    private int backgroundWidth;
    private int backgroundHeight;
    private int x;
    private int y;
    private int dialogueTextScrollPosition = 0;
    private float dialogueTextScrollAmount = 0.0f;
    private boolean dialogueTextMouseClicked = false;
    private int answersScrollPosition = 0;
    private float answersScrollAmount = 0.0f;
    private boolean answersMouseClicked = false;
    private int scrollPosition = 0;
    private float scrollAmount = 0.0f;
    private boolean mouseClicked = false;

    public DialogueBlockScreen(DialogueBlockEntity dialogueBlockEntity, @Nullable Dialogue dialogue, boolean showCreativeScreen) {
        super(NarratorManager.EMPTY);
        this.dialogueBlockEntity = dialogueBlockEntity;
        this.dialogue = dialogue;
        this.showCreativeScreen = showCreativeScreen;
        this.creativeScreenPage = CreativeScreenPage.STARTING_DIALOGUES;
    }

    private void answer(int index) {
        if (index + this.answersScrollPosition < this.visibleAnswersList.size()) {
            Dialogue.Answer currentAnswer = this.visibleAnswersList.get(index + this.answersScrollPosition);
            
            // loot_table
            Identifier lootTableIdentifier = currentAnswer.getLootTable();
            if (lootTableIdentifier != null && this.client != null && this.client.player != null) {
                this.dialogueBlockEntity.dialogueGiveItemsFromLootTable(this.client.player, lootTableIdentifier);
            }
            
            // advancement
            Identifier advancementIdentifier = currentAnswer.getGrantedAdvancement();
            String criterionName = currentAnswer.getCriterionName();
            if (advancementIdentifier != null && criterionName != null && this.client != null && this.client.player != null) {
                this.dialogueBlockEntity.dialogueGrantAdvancement(this.client.player, advancementIdentifier, criterionName);
            }

            String responseDialogueIdentifier = currentAnswer.getResponseDialogue();
            if (responseDialogueIdentifier.equals("")) {
                if (this.client != null) {
                    this.client.setScreen(null);
                }
            } else {
                Dialogue responseDialogue = DialoguesRegistry.getDialogue(Identifier.tryParse(responseDialogueIdentifier));
                if (this.client != null) {
                    this.client.setScreen(new DialogueBlockScreen(this.dialogueBlockEntity, responseDialogue, false));
                }
            } 

            // use block
            String usedBlock = currentAnswer.getUsedBlock();
            if (usedBlock != null) {
                for (MutablePair<String, BlockPos> entry : this.dialogueUsedBlocksList) {
                    if (entry.getLeft().equals(usedBlock)) {
                        if (this.client != null && this.client.player != null) {
                            this.dialogueBlockEntity.dialogueUseBlock(this.client.player, usedBlock);
                            return;
                        }
                    }
                }
            }
        }
    }

    private void calculateUnlockedAndVisibleAnswers(List<Dialogue.Answer> answerList) {
        ClientAdvancementManager advancementHandler = null;
        String lockAdvancementIdentifier;
        String unlockAdvancementIdentifier;
        boolean showLockedAnswer;

        if (this.client != null && this.client.player != null) {
            advancementHandler = this.client.player.networkHandler.getAdvancementHandler();
        }

        for (Dialogue.Answer answer: answerList) {
            
            lockAdvancementIdentifier = answer.getLockAdvancement();
            unlockAdvancementIdentifier = answer.getUnlockAdvancement();
            showLockedAnswer = answer.showLockedAnswer();

            if (advancementHandler != null) {
                AdvancementEntry lockAdvancementEntry = null;
                if (!lockAdvancementIdentifier.equals("")) {
                    lockAdvancementEntry = advancementHandler.get(Identifier.tryParse(lockAdvancementIdentifier));
                }
                AdvancementEntry unlockAdvancementEntry = null;
                if (!unlockAdvancementIdentifier.equals("")) {
                    unlockAdvancementEntry = advancementHandler.get(Identifier.tryParse(unlockAdvancementIdentifier));
                }
                if ((lockAdvancementIdentifier.equals("") || (lockAdvancementEntry != null && !((DuckClientAdvancementManagerMixin)advancementHandler.getManager()).bamcore$getAdvancementProgress(lockAdvancementEntry).isDone())) &&
                    (unlockAdvancementIdentifier.equals("") || (unlockAdvancementEntry != null && ((DuckClientAdvancementManagerMixin)advancementHandler.getManager()).bamcore$getAdvancementProgress(unlockAdvancementEntry).isDone()))) {
                    this.unlockedAnswersList.add(answer);
                    this.visibleAnswersList.add(answer);
                } else if (showLockedAnswer) {
                    this.visibleAnswersList.add(answer);
                }
            }
        }
    }

    private void removeDialogueUsedBlockEntry(int index) {
        if (index + this.scrollPosition < this.dialogueUsedBlocksList.size()) {
            this.dialogueUsedBlocksList.remove(index + this.scrollPosition);
        }
        this.scrollPosition = 0;
        this.scrollAmount = 0.0f;
        this.updateWidgets();
    }

    private void addDialogueUsedBlockEntry() {
        String newDialogueUsedBlockIdentifier = this.newDialogueUsedBlockIdentifierField.getText();
        for (MutablePair<String, BlockPos> entry : this.dialogueUsedBlocksList) {
            if (entry.getLeft().equals(newDialogueUsedBlockIdentifier)) {
                if (this.client != null && this.client.player != null) {
                    this.client.player.sendMessage(Text.translatable("gui.dialogue_block.entry_already_in_list"));
                }
                return;
            }
        }
        this.dialogueUsedBlocksList.add(
                new MutablePair<>(newDialogueUsedBlockIdentifier,
                        new BlockPos(
                                this.parseInt(this.newDialogueUsedBlockPositionOffsetXField.getText()),
                                this.parseInt(this.newDialogueUsedBlockPositionOffsetYField.getText()),
                                this.parseInt(this.newDialogueUsedBlockPositionOffsetZField.getText())
                        )
                )
        );
        this.scrollPosition = 0;
        this.scrollAmount = 0.0f;
        this.updateWidgets();
    }

    private void removeStartingDialogueEntry(int index) {
        if (index + this.scrollPosition < this.startingDialogueList.size()) {
            this.startingDialogueList.remove(index + this.scrollPosition);
        }
        this.scrollPosition = 0;
        this.scrollAmount = 0.0f;
        this.updateWidgets();
    }

    private void addStartingDialogueEntry() {
        String message = "";
        String newStartingDialogueIdentifier = this.newStartingDialogueIdentifierField.getText();
        if (DialoguesRegistry.getDialogue(Identifier.tryParse(newStartingDialogueIdentifier)) == null) {
            message = "gui.dialogue_block.invalid_dialogue_identifier";
        }
        if (message.equals("")) {
            for (MutablePair<String, MutablePair<String, String>> entry : this.startingDialogueList) {
                if (entry.getLeft().equals(newStartingDialogueIdentifier)) {
                    message = "gui.dialogue_block.entry_already_in_list";
                    break;
                }
            }
        }
        if (message.equals("")) {
            ClientAdvancementManager advancementHandler = null;

            if (this.client != null && this.client.player != null) {
                advancementHandler = this.client.player.networkHandler.getAdvancementHandler();
            }
            Identifier newStartingDialogueLockAdvancementIdentifier = Identifier.tryParse(this.newStartingDialogueLockAdvancementIdentifierField.getText());
            Identifier newStartingDialogueUnlockAdvancementIdentifier = Identifier.tryParse(this.newStartingDialogueUnlockAdvancementIdentifierField.getText());
            if (advancementHandler != null) {
                if (newStartingDialogueLockAdvancementIdentifier != null) {
                    AdvancementEntry advancementEntry = advancementHandler.get(newStartingDialogueLockAdvancementIdentifier);
                    if (advancementEntry == null && !this.newStartingDialogueLockAdvancementIdentifierField.getText().equals("")) {
                        message = "gui.dialogue_block.newStartingDialogueLockAdvancementIdentifier_invalid";
                    }
                }
                if (newStartingDialogueUnlockAdvancementIdentifier != null) {
                    AdvancementEntry advancementEntry = advancementHandler.get(newStartingDialogueUnlockAdvancementIdentifier);
                    if (advancementEntry == null && !this.newStartingDialogueUnlockAdvancementIdentifierField.getText().equals("")) {
                        message = "gui.dialogue_block.newStartingDialogueUnlockAdvancementIdentifier_invalid";
                    }
                }
            } else {
                message = "gui.dialogue_block.advancementHandler_null";
            }
        }
        if (this.client != null && this.client.player != null && !message.equals("")) {
            this.client.player.sendMessage(Text.translatable(message));
            return;
        }
        this.startingDialogueList.add(
                new MutablePair<>(newStartingDialogueIdentifier,
                        new MutablePair<>(
                                this.newStartingDialogueLockAdvancementIdentifierField.getText(),
                                this.newStartingDialogueLockAdvancementIdentifierField.getText()
                        ))
        );
        this.scrollPosition = 0;
        this.scrollAmount = 0.0f;
        this.updateWidgets();
    }

    private void saveCreative() {
        if (this.updateDialogueBlockCreative()) {
            this.close();
        }
    }

    private void cancel() {
        this.close();
    }

    @Override
    protected void init() {
        if (!this.showCreativeScreen && this.dialogue == null && this.client != null) {
            this.client.setScreen(null);
            return;
        }
        this.dialogueUsedBlocksList.clear();
        this.startingDialogueList.clear();
        this.unlockedAnswersList.clear();
        this.visibleAnswersList.clear();
        this.dialogueTextList.clear();
        List<String> keyList = new ArrayList<>(this.dialogueBlockEntity.getDialogueUsedBlocks().keySet());
        for (String key : keyList) {
            this.dialogueUsedBlocksList.add(new MutablePair<>(key, this.dialogueBlockEntity.getDialogueUsedBlocks().get(key)));
        }
        this.startingDialogueList.addAll(this.dialogueBlockEntity.getStartingDialogueList());
        if (this.dialogue != null) {
            this.calculateUnlockedAndVisibleAnswers(this.dialogue.getAnswerList());
            this.dialogueTextList = this.dialogue.getDialogueTextList();
        }
        this.backgroundWidth = 218;
        this.backgroundHeight = 197;
        this.x = (this.width - this.backgroundWidth) / 2;
        this.y = (this.height - this.backgroundHeight) / 2;
        
        super.init();
        //region adventure screen

        this.answerButton0 = this.addDrawableChild(ButtonWidget.builder(Text.empty(), button -> this.answer(0)).dimensions(this.x + 7, this.y + 98, this.backgroundWidth - 14, 20).build());
        this.answerButton1 = this.addDrawableChild(ButtonWidget.builder(Text.empty(), button -> this.answer(1)).dimensions(this.x + 7, this.y + 122, this.backgroundWidth - 14, 20).build());
        this.answerButton2 = this.addDrawableChild(ButtonWidget.builder(Text.empty(), button -> this.answer(2)).dimensions(this.x + 7, this.y + 146, this.backgroundWidth - 14, 20).build());
        this.answerButton3 = this.addDrawableChild(ButtonWidget.builder(Text.empty(), button -> this.answer(3)).dimensions(this.x + 7, this.y + 170, this.backgroundWidth - 14, 20).build());

        if (this.visibleAnswersList.size() > 4) {
            this.answerButton0.setWidth(this.backgroundWidth - 26);
            this.answerButton1.setWidth(this.backgroundWidth - 26);
            this.answerButton2.setWidth(this.backgroundWidth - 26);
            this.answerButton3.setWidth(this.backgroundWidth - 26);
        }
        //endregion adventure screen

        //region creative screen
        this.creativeScreenPageButton = this.addDrawableChild(CyclingButtonWidget.builder(CreativeScreenPage::asText).values((CreativeScreenPage[]) CreativeScreenPage.values()).initially(this.creativeScreenPage).omitKeyText().build(this.width / 2 - 154, 20, 300, 20, Text.empty(), (button, creativeScreenPage) -> {
            this.creativeScreenPage = creativeScreenPage;
            this.updateWidgets();
        }));
        
        // --- dialogue used blocks page ---

        this.removeDialogueUsedBlockEntryButton0 = this.addDrawableChild(ButtonWidget.builder(REMOVE_ENTRY_BUTTON_LABEL_TEXT, button -> this.removeDialogueUsedBlockEntry(0)).dimensions(this.width / 2 + 54, 42, 100, 20).build());
        this.removeDialogueUsedBlockEntryButton1 = this.addDrawableChild(ButtonWidget.builder(REMOVE_ENTRY_BUTTON_LABEL_TEXT, button -> this.removeDialogueUsedBlockEntry(0)).dimensions(this.width / 2 + 54, 66, 100, 20).build());
        this.removeDialogueUsedBlockEntryButton2 = this.addDrawableChild(ButtonWidget.builder(REMOVE_ENTRY_BUTTON_LABEL_TEXT, button -> this.removeDialogueUsedBlockEntry(0)).dimensions(this.width / 2 + 54, 90, 100, 20).build());
        this.removeDialogueUsedBlockEntryButton3 = this.addDrawableChild(ButtonWidget.builder(REMOVE_ENTRY_BUTTON_LABEL_TEXT, button -> this.removeDialogueUsedBlockEntry(0)).dimensions(this.width / 2 + 54, 114, 100, 20).build());

        this.newDialogueUsedBlockIdentifierField = new TextFieldWidget(this.textRenderer, this.width / 2 - 154, 138, 300, 20, Text.empty());
        this.newDialogueUsedBlockIdentifierField.setMaxLength(128);
        this.addSelectableChild(this.newDialogueUsedBlockIdentifierField);

        this.newDialogueUsedBlockPositionOffsetXField = new TextFieldWidget(this.textRenderer, this.width / 2 - 154, 162, 100, 20, Text.empty());
        this.newDialogueUsedBlockPositionOffsetXField.setMaxLength(128);
        this.addSelectableChild(this.newDialogueUsedBlockPositionOffsetXField);

        this.newDialogueUsedBlockPositionOffsetYField = new TextFieldWidget(this.textRenderer, this.width / 2 - 50, 162, 100, 20, Text.empty());
        this.newDialogueUsedBlockPositionOffsetYField.setMaxLength(128);
        this.addSelectableChild(this.newDialogueUsedBlockPositionOffsetYField);

        this.newDialogueUsedBlockPositionOffsetZField = new TextFieldWidget(this.textRenderer, this.width / 2 + 54, 162, 100, 20, Text.empty());
        this.newDialogueUsedBlockPositionOffsetZField.setMaxLength(128);
        this.addSelectableChild(this.newDialogueUsedBlockPositionOffsetZField);

        this.addDialogueUsedBlockButton = this.addDrawableChild(ButtonWidget.builder(ADD_ENTRY_BUTTON_LABEL_TEXT, button -> this.addDialogueUsedBlockEntry()).dimensions(this.width / 2 - 4 - 150, 186, 300, 20).build());

        // --- starting dialogues page ---

        this.removeStartingDialogueEntryButton = this.addDrawableChild(ButtonWidget.builder(REMOVE_ENTRY_BUTTON_LABEL_TEXT, button -> this.removeStartingDialogueEntry(0)).dimensions(this.width / 2 + 54, 78, 100, 20).build());

        this.newStartingDialogueIdentifierField = new TextFieldWidget(this.textRenderer, this.width / 2 - 154, 114, 300, 20, Text.empty());
        this.newStartingDialogueIdentifierField.setMaxLength(128);
        this.addSelectableChild(this.newStartingDialogueIdentifierField);

        this.newStartingDialogueLockAdvancementIdentifierField = new TextFieldWidget(this.textRenderer, this.width / 2 - 154, 138, 300, 20, Text.empty());
        this.newStartingDialogueLockAdvancementIdentifierField.setMaxLength(128);
        this.addSelectableChild(this.newStartingDialogueLockAdvancementIdentifierField);

        this.newStartingDialogueUnlockAdvancementIdentifierField = new TextFieldWidget(this.textRenderer, this.width / 2 - 154, 162, 300, 20, Text.empty());
        this.newStartingDialogueUnlockAdvancementIdentifierField.setMaxLength(128);
        this.addSelectableChild(this.newStartingDialogueUnlockAdvancementIdentifierField);

        this.addStartingDialogueButton = this.addDrawableChild(ButtonWidget.builder(ADD_ENTRY_BUTTON_LABEL_TEXT, button -> this.addStartingDialogueEntry()).dimensions(this.width / 2 - 4 - 150, 186, 300, 20).build());

        this.saveCreativeButton = this.addDrawableChild(ButtonWidget.builder(ScreenTexts.DONE, button -> this.saveCreative()).dimensions(this.width / 2 - 4 - 150, 210, 150, 20).build());
        this.cancelCreativeButton = this.addDrawableChild(ButtonWidget.builder(ScreenTexts.CANCEL, button -> this.cancel()).dimensions(this.width / 2 + 4, 210, 150, 20).build());

        //endregion creative screen
        this.updateWidgets();
    }

    private void updateWidgets() {

        //region adventure widgets
        
        this.answerButton0.visible = false;
        this.answerButton1.visible = false;
        this.answerButton2.visible = false;
        this.answerButton3.visible = false;
        
        //endregion adventure widgets

        //region creative widgets
        
        this.creativeScreenPageButton.visible = false;
        
        this.removeDialogueUsedBlockEntryButton0.visible = false;
        this.removeDialogueUsedBlockEntryButton1.visible = false;
        this.removeDialogueUsedBlockEntryButton2.visible = false;
        this.removeDialogueUsedBlockEntryButton3.visible = false;

        this.newDialogueUsedBlockIdentifierField.setVisible(false);
        this.newDialogueUsedBlockPositionOffsetXField.setVisible(false);
        this.newDialogueUsedBlockPositionOffsetYField.setVisible(false);
        this.newDialogueUsedBlockPositionOffsetZField.setVisible(false);

        this.addDialogueUsedBlockButton.visible = false;

        this.removeStartingDialogueEntryButton.visible = false;
        
        this.newStartingDialogueIdentifierField.setVisible(false);
        this.newStartingDialogueLockAdvancementIdentifierField.setVisible(false);
        this.newStartingDialogueUnlockAdvancementIdentifierField.setVisible(false);

        this.addStartingDialogueButton.visible = false;

        this.saveCreativeButton.visible = false;
        this.cancelCreativeButton.visible = false;

        //endregion creative widgets

        if (this.showCreativeScreen) {
            this.creativeScreenPageButton.visible = true;

            if (this.creativeScreenPage == CreativeScreenPage.DIALOGUE_USED_BLOCKS) {

                int index = 0;
                for (int i = 0; i < Math.min(4, this.dialogueUsedBlocksList.size()); i++) {
                    if (index == 0) {
                        this.removeDialogueUsedBlockEntryButton0.visible = true;
                    } else if (index == 1) {
                        this.removeDialogueUsedBlockEntryButton1.visible = true;
                    } else if (index == 2) {
                        this.removeDialogueUsedBlockEntryButton2.visible = true;
                    } else if (index == 3) {
                        this.removeDialogueUsedBlockEntryButton3.visible = true;
                    }
                    index++;
                }

                this.newDialogueUsedBlockIdentifierField.setVisible(true);
                this.newDialogueUsedBlockPositionOffsetXField.setVisible(true);
                this.newDialogueUsedBlockPositionOffsetYField.setVisible(true);
                this.newDialogueUsedBlockPositionOffsetZField.setVisible(true);

                this.addDialogueUsedBlockButton.visible = true;

            } else if (this.creativeScreenPage == CreativeScreenPage.STARTING_DIALOGUES) {

                this.removeStartingDialogueEntryButton.visible = true;

                this.newStartingDialogueIdentifierField.setVisible(true);
                this.newStartingDialogueLockAdvancementIdentifierField.setVisible(true);
                this.newStartingDialogueUnlockAdvancementIdentifierField.setVisible(true);

                this.addStartingDialogueButton.visible = true;

            }

            this.saveCreativeButton.visible = true;
            this.cancelCreativeButton.visible = true;

        } else {

            int index = 0;
            for (int i = 0; i < Math.min(4, this.visibleAnswersList.size()); i++) {
                if (index == 0) {
                    this.answerButton0.visible = true;
                } else if (index == 1) {
                    this.answerButton1.visible = true;
                } else if (index == 2) {
                    this.answerButton2.visible = true;
                } else if (index == 3) {
                    this.answerButton3.visible = true;
                }
                index++;
            }
        }
        this.dialogueTextScrollPosition = 0;
        this.dialogueTextScrollAmount = 0.0f;
        this.answersScrollPosition = 0;
        this.answersScrollAmount = 0.0f;
        this.scrollPosition = 0;
        this.scrollAmount = 0.0f;
    }

    @Override
    public void resize(MinecraftClient client, int width, int height) {
        List<MutablePair<String, BlockPos>> list = new ArrayList<>(this.dialogueUsedBlocksList);
        List<MutablePair<String, MutablePair<String, String>>> list1 = new ArrayList<>(this.startingDialogueList);
        List<Dialogue.Answer> list2 = new ArrayList<>(this.unlockedAnswersList);
        List<Dialogue.Answer> list3 = new ArrayList<>(this.visibleAnswersList);
        List<String> list4 = new ArrayList<>(this.dialogueTextList);
        int number = this.dialogueTextScrollPosition;
        float number1 =  this.dialogueTextScrollAmount;
        int number2 = this.answersScrollPosition;
        float number3 =  this.answersScrollAmount;
        int number4 = this.scrollPosition;
        float number5 =  this.scrollAmount;
        CreativeScreenPage var = this.creativeScreenPage;
        String string = this.newDialogueUsedBlockIdentifierField.getText();
        String string1 = this.newDialogueUsedBlockPositionOffsetXField.getText();
        String string2 = this.newDialogueUsedBlockPositionOffsetYField.getText();
        String string3 = this.newDialogueUsedBlockPositionOffsetZField.getText();
        String string4 = this.newStartingDialogueIdentifierField.getText();
        String string5 = this.newStartingDialogueLockAdvancementIdentifierField.getText();
        String string6 = this.newStartingDialogueUnlockAdvancementIdentifierField.getText();
        this.init(client, width, height);
        this.dialogueUsedBlocksList.clear();
        this.startingDialogueList.clear();
        this.unlockedAnswersList.clear();
        this.visibleAnswersList.clear();
        this.dialogueTextList.clear();
        this.dialogueUsedBlocksList.addAll(list);
        this.startingDialogueList.addAll(list1);
        this.unlockedAnswersList.addAll(list2);
        this.visibleAnswersList.addAll(list3);
        this.dialogueTextList.addAll(list4);
        this.dialogueTextScrollPosition = number;
        this.dialogueTextScrollAmount = number1;
        this.answersScrollPosition = number2;
        this.answersScrollAmount = number3;
        this.scrollPosition = number4;
        this.scrollAmount = number5;
        this.creativeScreenPage = var;
        this.newDialogueUsedBlockIdentifierField.setText(string);
        this.newDialogueUsedBlockPositionOffsetXField.setText(string1);
        this.newDialogueUsedBlockPositionOffsetYField.setText(string2);
        this.newDialogueUsedBlockPositionOffsetZField.setText(string3);
        this.newStartingDialogueIdentifierField.setText(string4);
        this.newStartingDialogueLockAdvancementIdentifierField.setText(string5);
        this.newStartingDialogueUnlockAdvancementIdentifierField.setText(string6);
        this.updateWidgets();
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        this.dialogueTextMouseClicked = false;
        this.answersMouseClicked = false;
        if (!this.showCreativeScreen && this.dialogueTextList.size() > 7) {
            int i = this.x + this.backgroundWidth - 14;
            int j = this.y + 8;
            if (mouseX >= (double)i && mouseX < (double)(i + 6) && mouseY >= (double)j && mouseY < (double)(j + 87)) {
                this.dialogueTextMouseClicked = true;
            }
        }
        if (!this.showCreativeScreen && this.visibleAnswersList.size() > 4) {
            int i = this.x + this.backgroundWidth - 14;
            int j = this.y + 99;
            if (mouseX >= (double)i && mouseX < (double)(i + 6) && mouseY >= (double)j && mouseY < (double)(j + 90)) {
                this.answersMouseClicked = true;
            }
        }
        if (this.showCreativeScreen
                && this.creativeScreenPage == CreativeScreenPage.DIALOGUE_USED_BLOCKS
                && this.dialogueUsedBlocksList.size() > 4) {
            int i = this.width / 2 - 153;
            int j = 43;
            if (mouseX >= (double)i && mouseX < (double)(i + 6) && mouseY >= (double)j && mouseY < (double)(j + 94)) {
                this.mouseClicked = true;
            }
        }
        if (this.showCreativeScreen
                && this.creativeScreenPage == CreativeScreenPage.STARTING_DIALOGUES
                && this.startingDialogueList.size() > 1) {
            int i = this.width / 2 - 153;
            int j = 72;
            if (mouseX >= (double)i && mouseX < (double)(i + 6) && mouseY >= (double)j && mouseY < (double)(j + 33)) {
                this.mouseClicked = true;
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        if (!this.showCreativeScreen
                && this.dialogueTextList.size() > 7
                && this.dialogueTextMouseClicked) {
            int i = this.dialogueTextList.size() - 7;
            float f = (float)deltaY / (float)i;
            this.dialogueTextScrollAmount = MathHelper.clamp(this.dialogueTextScrollAmount + f, 0.0f, 1.0f);
            this.dialogueTextScrollPosition = (int)((double)(this.dialogueTextScrollAmount * (float)i));
        }
        if (!this.showCreativeScreen
                && this.visibleAnswersList.size() > 4
                && this.answersMouseClicked) {
            int i = this.visibleAnswersList.size() - 4;
            float f = (float)deltaY / (float)i;
            this.answersScrollAmount = MathHelper.clamp(this.answersScrollAmount + f, 0.0f, 1.0f);
            this.answersScrollPosition = (int)((double)(this.answersScrollAmount * (float)i));
        }
        if (this.showCreativeScreen
                && this.creativeScreenPage == CreativeScreenPage.DIALOGUE_USED_BLOCKS
                && this.dialogueUsedBlocksList.size() > 4
                && this.mouseClicked) {
            int i = this.dialogueUsedBlocksList.size() - 4;
            float f = (float)deltaY / (float)i;
            this.scrollAmount = MathHelper.clamp(this.scrollAmount + f, 0.0f, 1.0f);
            this.scrollPosition = (int)((double)(this.scrollAmount * (float)i));
        }
        if (this.showCreativeScreen
                && this.creativeScreenPage == CreativeScreenPage.STARTING_DIALOGUES
                && this.startingDialogueList.size() > 1
                && this.mouseClicked) {
            int i = this.startingDialogueList.size() - 1;
            float f = (float)deltaY / (float)i;
            this.scrollAmount = MathHelper.clamp(this.scrollAmount + f, 0.0f, 1.0f);
            this.scrollPosition = (int)((double)(this.scrollAmount * (float)i));
        }
        return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        if (!this.showCreativeScreen
                && this.dialogueTextList.size() > 7
                && mouseX >= (double)(this.x + 7) && mouseX <= (double)(this.x + this.backgroundWidth - 7)
                && mouseY >= (double)(this.y + 7) && mouseY <= (double)(this.y + 94)) {
            int i = this.dialogueTextList.size() - 7;
            float f = (float)verticalAmount / (float)i;
            this.dialogueTextScrollAmount = MathHelper.clamp(this.dialogueTextScrollAmount - f, 0.0f, 1.0f);
            this.dialogueTextScrollPosition = (int)((double)(this.dialogueTextScrollAmount * (float)i));
        }
        if (!this.showCreativeScreen
                && this.visibleAnswersList.size() > 4
                && mouseX >= (double)(this.x + 7) && mouseX <= (double)(this.x + this.backgroundWidth - 7)
                && mouseY >= (double)(this.y + 98) && mouseY <= (double)(this.y + 190)) {
            int i = this.visibleAnswersList.size() - 4;
            float f = (float)verticalAmount / (float)i;
            this.answersScrollAmount = MathHelper.clamp(this.answersScrollAmount - f, 0.0f, 1.0f);
            this.answersScrollPosition = (int)((double)(this.answersScrollAmount * (float)i));
        }
        if (this.showCreativeScreen
                && this.creativeScreenPage == CreativeScreenPage.DIALOGUE_USED_BLOCKS
                && this.dialogueUsedBlocksList.size() > 4
                && mouseX >= (double)(this.width / 2 - 154) && mouseX <= (double)(this.width / 2 + 50)
                && mouseY >= (double)(42) && mouseY <= (double)(138)) {
            int i = this.dialogueUsedBlocksList.size() - 4;
            float f = (float)verticalAmount / (float)i;
            this.scrollAmount = MathHelper.clamp(this.scrollAmount - f, 0.0f, 1.0f);
            this.scrollPosition = (int)((double)(this.scrollAmount * (float)i));
        }
        if (this.showCreativeScreen
                && this.creativeScreenPage == CreativeScreenPage.STARTING_DIALOGUES
                && this.startingDialogueList.size() > 1
                && mouseX >= (double)(this.width / 2 - 154) && mouseX <= (double)(this.width / 2 + 50)
                && mouseY >= (double)(71) && mouseY <= (double)(106)) {
            int i = this.startingDialogueList.size() - 1;
            float f = (float)verticalAmount / (float)i;
            this.scrollAmount = MathHelper.clamp(this.scrollAmount - f, 0.0f, 1.0f);
            this.scrollPosition = (int)((double)(this.scrollAmount * (float)i));
        }
        return super.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (this.showCreativeScreen && (keyCode == GLFW.GLFW_KEY_ENTER || keyCode == GLFW.GLFW_KEY_KP_ENTER)) {
            this.saveCreative();
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        if (this.showCreativeScreen) {
            if (this.creativeScreenPage == CreativeScreenPage.DIALOGUE_USED_BLOCKS) {
                int x = this.dialogueUsedBlocksList.size() > 4 ? this.width / 2 - 142 : this.width / 2 - 153;
                for (int i = this.scrollPosition; i < Math.min(this.scrollPosition + 4, this.dialogueUsedBlocksList.size()); i++) {
                    context.drawTextWithShadow(this.textRenderer, this.dialogueUsedBlocksList.get(i).getLeft() + ": " + this.dialogueUsedBlocksList.get(i).getRight().toString(), x, 48 + ((i - this.scrollPosition) * 24), 0xA0A0A0);
                }
                if (this.dialogueUsedBlocksList.size() > 4) {
                    context.drawGuiTexture(SCROLL_BAR_BACKGROUND_8_96_TEXTURE, this.width / 2 - 154, 42, 8, 96);
                    int k = (int)(85.0f * this.scrollAmount);
                    context.drawGuiTexture(SCROLLER_VERTICAL_6_7_TEXTURE, this.width / 2 - 153, 42 + 1 + k, 6, 7);
                }
                this.newDialogueUsedBlockIdentifierField.render(context, mouseX, mouseY, delta);
                this.newDialogueUsedBlockPositionOffsetXField.render(context, mouseX, mouseY, delta);
                this.newDialogueUsedBlockPositionOffsetYField.render(context, mouseX, mouseY, delta);
                this.newDialogueUsedBlockPositionOffsetZField.render(context, mouseX, mouseY, delta);
            } else if (this.creativeScreenPage == CreativeScreenPage.STARTING_DIALOGUES) {
                int x = this.startingDialogueList.size() > 1 ? this.width / 2 - 142 : this.width / 2 - 153;
                for (int i = this.scrollPosition; i < Math.min(this.scrollPosition + 1, this.startingDialogueList.size()); i++) {
                    context.drawTextWithShadow(this.textRenderer, Text.translatable(this.startingDialogueList.get(i).getLeft()), x, 71, 0xA0A0A0);
                    context.drawTextWithShadow(this.textRenderer, Text.translatable(this.startingDialogueList.get(i).getRight().getLeft()), x, 84, 0xA0A0A0);
                    context.drawTextWithShadow(this.textRenderer, Text.translatable(this.startingDialogueList.get(i).getRight().getRight()), x, 97, 0xA0A0A0);
                }
                if (this.startingDialogueList.size() > 1) {
                    context.drawGuiTexture(SCROLL_BAR_BACKGROUND_8_35_TEXTURE, this.width / 2 - 154, 71, 8, 35);
                    int k = (int)(26.0f * this.scrollAmount);
                    context.drawGuiTexture(SCROLLER_VERTICAL_6_7_TEXTURE, this.width / 2 - 153, 71 + 1 + k, 6, 7);
                }
                this.newStartingDialogueIdentifierField.render(context, mouseX, mouseY, delta);
                this.newStartingDialogueLockAdvancementIdentifierField.render(context, mouseX, mouseY, delta);
                this.newStartingDialogueUnlockAdvancementIdentifierField.render(context, mouseX, mouseY, delta);
            }
        } else if (this.dialogue != null) {

            for (int i = this.dialogueTextScrollPosition; i < Math.min(this.dialogueTextScrollPosition + 7, this.dialogueTextList.size()); i++) {
                String text = this.dialogueTextList.get(i);
                context.drawText(this.textRenderer, Text.translatable(text), this.x + 8, this.y + 7 + ((i - this.dialogueTextScrollPosition) * 13), 0x404040, false);
            }
            if (this.dialogueTextList.size() > 7) {
                context.drawGuiTexture(SCROLL_BAR_BACKGROUND_8_87_TEXTURE, this.x + this.backgroundWidth - 15, this.y + 7, 8, 87);
                int k = (int)(78.0f * this.dialogueTextScrollAmount);
                context.drawGuiTexture(SCROLLER_VERTICAL_6_7_TEXTURE, this.x + this.backgroundWidth - 14, this.y + 7 + 1 + k, 6, 7);
            }
            int index = 0;
            for (int i = this.answersScrollPosition; i < Math.min(this.answersScrollPosition + 4, this.visibleAnswersList.size()); i++) {
                String text = this.visibleAnswersList.get(i).getAnswerText();
                if (index == 0) {
                    this.answerButton0.setMessage(Text.translatable(text));
                } else if (index == 1) {
                    this.answerButton1.setMessage(Text.translatable(text));
                } else if (index == 2) {
                    this.answerButton2.setMessage(Text.translatable(text));
                } else if (index == 3) {
                    this.answerButton3.setMessage(Text.translatable(text));
                }
                index++;
            }
            if (this.visibleAnswersList.size() > 4) {
                context.drawGuiTexture(SCROLL_BAR_BACKGROUND_8_92_TEXTURE, this.x + this.backgroundWidth - 15, this.y + 98, 8, 92);
                int k = (int)(83.0f * this.answersScrollAmount);
                context.drawGuiTexture(SCROLLER_VERTICAL_6_7_TEXTURE, this.x + this.backgroundWidth - 14, this.y + 98 + 1 + k, 6, 7);
            }
        }
    }

    @Override
    public boolean shouldPause() {
        return false;
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return (this.dialogue != null && this.dialogue.isCancellable()) || this.showCreativeScreen || this.dialogue == null;
    }

    @Override
    public void renderBackground(DrawContext context, int mouseX, int mouseY, float delta) {
        super.renderBackground(context, mouseX, mouseY, delta);
        this.drawBackground(context, delta, mouseX, mouseY);
    }

    public void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
        if (!this.showCreativeScreen) {
            int i = this.x;
            int j = this.y;
            context.drawTexture(BACKGROUND_218_197_TEXTURE, i, j, 0, 0, this.backgroundWidth, this.backgroundHeight, this.backgroundWidth, this.backgroundHeight);
        }
    }

    private boolean updateDialogueBlockCreative() {
        ClientPlayNetworking.send(new UpdateDialogueBlockPacket(
                this.dialogueBlockEntity.getPos(),
                this.dialogueUsedBlocksList,
                this.startingDialogueList
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

    public static enum CreativeScreenPage implements StringIdentifiable
    {
        DIALOGUE_USED_BLOCKS("dialogue_used_blocks"),
        STARTING_DIALOGUES("starting_dialogues");

        private final String name;

        private CreativeScreenPage(String name) {
            this.name = name;
        }

        @Override
        public String asString() {
            return this.name;
        }

        public static Optional<CreativeScreenPage> byName(String name) {
            return Arrays.stream(CreativeScreenPage.values()).filter(creativeScreenPage -> creativeScreenPage.asString().equals(name)).findFirst();
        }

        public Text asText() {
            return Text.translatable("gui.dialogue_screen.creativeScreenPage." + this.name);
        }
    }
}
