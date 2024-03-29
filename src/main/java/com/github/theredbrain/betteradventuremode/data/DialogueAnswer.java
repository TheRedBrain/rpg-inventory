package com.github.theredbrain.betteradventuremode.data;

import com.github.theredbrain.betteradventuremode.util.ItemUtils;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public final class DialogueAnswer {
    private final String answerText;

    /**
     * if responseDialogue is valid identifier of a dialogue, opens that dialogue screen
     * else close dialogue screen
     */
    private final String responseDialogue;

    private final String lockAdvancement;

    private final String unlockAdvancement;

    private final boolean showLockedAnswer;

    private final boolean showUnaffordableAnswer;

    /**
     * if grantedAdvancement is valid identifier for an advancement, grants that advancement
     */
    private final @Nullable String grantedAdvancement;

    /**
     * if criterionName is valid identifier for an advancement criterion, grants that criterion
     */
    private final @Nullable String criterionName;

    /**
     * if lootTable is valid identifier for a loot_table, generates that loot_table and offerOrDrop it to the player
     */
    private final @Nullable String lootTable;

    /**
     * if the current DialogueBlock has a BlockPos mapped under usedBlock, player uses the block at that BlockPos
     */
    private final @Nullable String usedBlock;

    /**
     * if the current DialogueBlock has a BlockPos mapped under triggeredBlock, the block at that BlockPos is triggered
     */
    private final @Nullable String triggeredBlock;

    /**
     * displays this string as localized text in the players overlay
     */
    private final @Nullable String overlayMessage;

    /**
     * player inventory is checked for specified items, if all are found they are removed from player inventory, otherwise the answer fails
     */
    private final @Nullable List<ItemUtils.VirtualItemStack> itemCost;

    public DialogueAnswer(String answerText, String responseDialogue, String lockAdvancement, String unlockAdvancement, boolean showLockedAnswer, boolean showUnaffordableAnswer, @Nullable String grantedAdvancement, @Nullable String criterionName, @Nullable String lootTable, @Nullable String usedBlock, @Nullable String triggeredBlock, @Nullable String overlayMessage, @Nullable List<ItemUtils.VirtualItemStack> itemCost) {
        this.answerText = answerText;
        this.responseDialogue = responseDialogue;
        this.lockAdvancement = lockAdvancement;
        this.unlockAdvancement = unlockAdvancement;
        this.showLockedAnswer = showLockedAnswer;
        this.showUnaffordableAnswer = showUnaffordableAnswer;
        this.grantedAdvancement = grantedAdvancement;
        this.criterionName = criterionName;
        this.lootTable = lootTable;
        this.usedBlock = usedBlock;
        this.triggeredBlock = triggeredBlock;
        this.overlayMessage = overlayMessage;
        this.itemCost = itemCost;
    }

    public String getAnswerText() {
        return this.answerText;
    }

    public String getResponseDialogue() {
        if (Identifier.isValid(this.responseDialogue)) {
            return this.responseDialogue;
        }
        return "";
    }

    public String getLockAdvancement() {
        if (Identifier.isValid(this.lockAdvancement)) {
            return this.lockAdvancement;
        }
        return "";
    }

    public String getUnlockAdvancement() {
        if (Identifier.isValid(this.unlockAdvancement)) {
            return this.unlockAdvancement;
        }
        return "";
    }

    public boolean showLockedAnswer() {
        return this.showLockedAnswer;
    }

    public boolean showUnaffordableAnswer() {
        return this.showUnaffordableAnswer;
    }

    @Nullable
    public Identifier getGrantedAdvancement() {
        if (this.grantedAdvancement != null) {
            return new Identifier(this.grantedAdvancement);
        }
        return null;
    }

    @Nullable
    public String getCriterionName() {
        return this.criterionName;
    }

    @Nullable
    public String getOverlayMessage() {
        return this.overlayMessage;
    }

    @Nullable
    public Identifier getLootTable() {
        if (this.lootTable != null) {
            return new Identifier(this.lootTable);
        }
        return null;
    }

    @Nullable
    public String getUsedBlock() {
        return this.usedBlock;
    }

    @Nullable
    public String getTriggeredBlock() {
        return this.triggeredBlock;
    }

    @Nullable
    public List<ItemUtils.VirtualItemStack> getItemCost() {
        return this.itemCost;
    }
}
