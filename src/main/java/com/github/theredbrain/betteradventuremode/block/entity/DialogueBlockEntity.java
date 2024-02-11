package com.github.theredbrain.betteradventuremode.block.entity;

import com.github.theredbrain.betteradventuremode.api.json_files_backend.Dialogue;
import com.github.theredbrain.betteradventuremode.api.util.BlockRotationUtils;
import com.github.theredbrain.betteradventuremode.block.RotatedBlockWithEntity;
import com.github.theredbrain.betteradventuremode.client.network.DuckClientAdvancementManagerMixin;
import com.github.theredbrain.betteradventuremode.entity.player.DuckPlayerEntityMixin;
import com.github.theredbrain.betteradventuremode.network.packet.*;
import com.github.theredbrain.betteradventuremode.registry.DialoguesRegistry;
import com.github.theredbrain.betteradventuremode.registry.EntityRegistry;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.advancement.AdvancementEntry;
import net.minecraft.block.BlockState;
import net.minecraft.client.network.ClientAdvancementManager;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import org.apache.commons.lang3.tuple.MutablePair;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class DialogueBlockEntity extends RotatedBlockEntity {

    private HashMap<String, BlockPos> dialogueUsedBlocks = new HashMap<>();
    private HashMap<String, BlockPos> dialogueTriggeredBlocks = new HashMap<>();

    // TODO convert to a map?
    private List<MutablePair<String, MutablePair<String, String>>> startingDialogueList = new ArrayList<>();
    public DialogueBlockEntity(BlockPos pos, BlockState state) {
        super(EntityRegistry.DIALOGUE_BLOCK_ENTITY, pos, state);
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        List<String> dialogueUsedBlocksKeys = new ArrayList<>(this.dialogueUsedBlocks.keySet());
        nbt.putInt("dialogueUsedBlocksKeysSize", dialogueUsedBlocksKeys.size());
        for (int i = 0; i < dialogueUsedBlocksKeys.size(); i++) {
            String key = dialogueUsedBlocksKeys.get(i);
            nbt.putString("dialogueUsedBlocks_key_" + i, key);
            nbt.putInt("dialogueUsedBlocks_entry_X_" + i, this.dialogueUsedBlocks.get(key).getX());
            nbt.putInt("dialogueUsedBlocks_entry_Y_" + i, this.dialogueUsedBlocks.get(key).getY());
            nbt.putInt("dialogueUsedBlocks_entry_Z_" + i, this.dialogueUsedBlocks.get(key).getZ());
        }

        List<String> dialogueTriggeredBlocksKeys = new ArrayList<>(this.dialogueTriggeredBlocks.keySet());
        nbt.putInt("dialogueTriggeredBlocksKeysSize", dialogueTriggeredBlocksKeys.size());
        for (int i = 0; i < dialogueTriggeredBlocksKeys.size(); i++) {
            String key = dialogueTriggeredBlocksKeys.get(i);
            nbt.putString("dialogueTriggeredBlocks_key_" + i, key);
            nbt.putInt("dialogueTriggeredBlocks_entry_X_" + i, this.dialogueTriggeredBlocks.get(key).getX());
            nbt.putInt("dialogueTriggeredBlocks_entry_Y_" + i, this.dialogueTriggeredBlocks.get(key).getY());
            nbt.putInt("dialogueTriggeredBlocks_entry_Z_" + i, this.dialogueTriggeredBlocks.get(key).getZ());
        }

        nbt.putInt("startingDialogueListSize", this.startingDialogueList.size());
        for (int i = 0; i < this.startingDialogueList.size(); i++) {
            nbt.putString("startingDialogueList_name_" + i, this.startingDialogueList.get(i).getLeft());
            nbt.putString("startingDialogueList_lockAdvancement_" + i, this.startingDialogueList.get(i).getRight().getLeft());
            nbt.putString("startingDialogueList_unlockAdvancement_" + i, this.startingDialogueList.get(i).getRight().getRight());
        }

        super.writeNbt(nbt);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        this.dialogueUsedBlocks.clear();
        int dialogueUsedBlocksKeysSize = nbt.getInt("dialogueUsedBlocksKeysSize");
        for (int i = 0; i < dialogueUsedBlocksKeysSize; i++) {
            this.dialogueUsedBlocks.put(nbt.getString("dialogueUsedBlocks_key_" + i), new BlockPos(
                    MathHelper.clamp(nbt.getInt("dialogueUsedBlocks_entry_X_" + i), -48, 48),
                    MathHelper.clamp(nbt.getInt("dialogueUsedBlocks_entry_Y_" + i), -48, 48),
                    MathHelper.clamp(nbt.getInt("dialogueUsedBlocks_entry_Z_" + i), -48, 48)
            ));
        }

        this.dialogueTriggeredBlocks.clear();
        int dialogueTriggeredBlocksKeysSize = nbt.getInt("dialogueTriggeredBlocksKeysSize");
        for (int i = 0; i < dialogueTriggeredBlocksKeysSize; i++) {
            this.dialogueTriggeredBlocks.put(nbt.getString("dialogueTriggeredBlocks_key_" + i), new BlockPos(
                    MathHelper.clamp(nbt.getInt("dialogueTriggeredBlocks_entry_X_" + i), -48, 48),
                    MathHelper.clamp(nbt.getInt("dialogueTriggeredBlocks_entry_Y_" + i), -48, 48),
                    MathHelper.clamp(nbt.getInt("dialogueTriggeredBlocks_entry_Z_" + i), -48, 48)
            ));
        }

        this.startingDialogueList.clear();
        int startingDialogueListSize = nbt.getInt("startingDialogueListSize");
        for (int i = 0; i < startingDialogueListSize; i++) {
            this.startingDialogueList.add(
                    new MutablePair<>(
                            nbt.getString("startingDialogueList_name_" + i),
                            new MutablePair<>(
                                    nbt.getString("startingDialogueList_lockAdvancement_" + i),
                                    nbt.getString("startingDialogueList_unlockAdvancement_" + i)
                            )
                    )
            );
        }

        super.readNbt(nbt);
    }

    public BlockEntityUpdateS2CPacket toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    @Override
    public NbtCompound toInitialChunkDataNbt() {
        return this.createNbt();
    }

    public boolean openScreen(PlayerEntity player) {
        if (player.getEntityWorld().isClient) {
            ((DuckPlayerEntityMixin)player).betteradventuremode$openDialogueScreen(this, getStartingDialogue(player, this));
        }
        return true;
    }

    private static @Nullable Dialogue getStartingDialogue(PlayerEntity player, DialogueBlockEntity dialogueBlockEntity) {
        if (dialogueBlockEntity.startingDialogueList.size() < 1) {
            return null;
        }
        ClientAdvancementManager advancementHandler = null;

        if (player instanceof ClientPlayerEntity clientPlayerEntity) {
            advancementHandler = clientPlayerEntity.networkHandler.getAdvancementHandler();
        }
        Dialogue startingDialogue = null;
        String lockAdvancement;
        String unlockAdvancement;

        for (MutablePair<String, MutablePair<String, String>> dialogueEntry : dialogueBlockEntity.startingDialogueList) {
            lockAdvancement = dialogueEntry.getRight().getLeft();
            unlockAdvancement = dialogueEntry.getRight().getRight();

            if (advancementHandler != null) {
                AdvancementEntry lockAdvancementEntry = null;
                if (!lockAdvancement.equals("")) {
                    lockAdvancementEntry = advancementHandler.get(Identifier.tryParse(lockAdvancement));
                }
                AdvancementEntry unlockAdvancementEntry = null;
                if (!unlockAdvancement.equals("")) {
                    unlockAdvancementEntry = advancementHandler.get(Identifier.tryParse(unlockAdvancement));
                }
                if ((lockAdvancement.equals("") || (lockAdvancementEntry != null && !((DuckClientAdvancementManagerMixin)advancementHandler.getManager()).betteradventuremode$getAdvancementProgress(lockAdvancementEntry).isDone())) && (unlockAdvancement.equals("") || (unlockAdvancementEntry != null && ((DuckClientAdvancementManagerMixin)advancementHandler.getManager()).betteradventuremode$getAdvancementProgress(unlockAdvancementEntry).isDone()))) {
                    startingDialogue = DialoguesRegistry.getDialogue(Identifier.tryParse(dialogueEntry.getLeft()));
                    if (startingDialogue != null) {
                        break;
                    }
                }
            }
        }
        return startingDialogue;
    }

    public void answer(Identifier answerIdentifier) {
        ClientPlayNetworking.send(new DialogueAnswerPacket(
                this.pos,
                answerIdentifier
        ));
    }

    public HashMap<String, BlockPos> getDialogueUsedBlocks() {
        return this.dialogueUsedBlocks;
    }

    public boolean setDialogueUsedBlocks(HashMap<String, BlockPos> dialogueUsedBlocks) {
        this.dialogueUsedBlocks = dialogueUsedBlocks;
        return true;
    }

    public HashMap<String, BlockPos> getDialogueTriggeredBlocks() {
        return this.dialogueTriggeredBlocks;
    }

    // TODO check if input is valid
    public boolean setDialogueTriggeredBlocks(HashMap<String, BlockPos> dialogueTriggeredBlocks) {
        this.dialogueTriggeredBlocks = dialogueTriggeredBlocks;
        return true;
    }

    public List<MutablePair<String, MutablePair<String, String>>> getStartingDialogueList() {
        return this.startingDialogueList;
    }

    // TODO check if input is valid
    public boolean setStartingDialogueList(List<MutablePair<String, MutablePair<String, String>>> startingDialogueList) {
        this.startingDialogueList = startingDialogueList;
        return true;
    }

    @Override
    protected void onRotate(BlockState state) {
        if (state.getBlock() instanceof RotatedBlockWithEntity) {
            if (state.get(RotatedBlockWithEntity.ROTATED) != this.rotated) {
                BlockRotation blockRotation = BlockRotationUtils.calculateRotationFromDifferentRotatedStates(state.get(RotatedBlockWithEntity.ROTATED), this.rotated);
                List<String> keys = new ArrayList<>(this.dialogueUsedBlocks.keySet());
                for (String key : keys) {
                    BlockPos oldBlockPos = this.dialogueUsedBlocks.get(key);
                    this.dialogueUsedBlocks.put(key, BlockRotationUtils.rotateOffsetBlockPos(oldBlockPos, blockRotation));
                }
                keys = new ArrayList<>(this.dialogueTriggeredBlocks.keySet());
                for (String key : keys) {
                    BlockPos oldBlockPos = this.dialogueTriggeredBlocks.get(key);
                    this.dialogueTriggeredBlocks.put(key, BlockRotationUtils.rotateOffsetBlockPos(oldBlockPos, blockRotation));
                }
                this.rotated = state.get(RotatedBlockWithEntity.ROTATED);
            }
            if (state.get(RotatedBlockWithEntity.X_MIRRORED) != this.x_mirrored) {
                List<String> keys = new ArrayList<>(this.dialogueUsedBlocks.keySet());
                for (String key : keys) {
                    BlockPos oldBlockPos = this.dialogueUsedBlocks.get(key);
                    this.dialogueUsedBlocks.put(key, BlockRotationUtils.mirrorOffsetBlockPos(oldBlockPos, BlockMirror.FRONT_BACK));
                }
                keys = new ArrayList<>(this.dialogueTriggeredBlocks.keySet());
                for (String key : keys) {
                    BlockPos oldBlockPos = this.dialogueTriggeredBlocks.get(key);
                    this.dialogueTriggeredBlocks.put(key, BlockRotationUtils.mirrorOffsetBlockPos(oldBlockPos, BlockMirror.FRONT_BACK));
                }
                this.x_mirrored = state.get(RotatedBlockWithEntity.X_MIRRORED);
            }
            if (state.get(RotatedBlockWithEntity.Z_MIRRORED) != this.z_mirrored) {
                List<String> keys = new ArrayList<>(this.dialogueUsedBlocks.keySet());
                for (String key : keys) {
                    BlockPos oldBlockPos = this.dialogueUsedBlocks.get(key);
                    this.dialogueUsedBlocks.put(key, BlockRotationUtils.mirrorOffsetBlockPos(oldBlockPos, BlockMirror.LEFT_RIGHT));
                }
                keys = new ArrayList<>(this.dialogueTriggeredBlocks.keySet());
                for (String key : keys) {
                    BlockPos oldBlockPos = this.dialogueTriggeredBlocks.get(key);
                    this.dialogueTriggeredBlocks.put(key, BlockRotationUtils.mirrorOffsetBlockPos(oldBlockPos, BlockMirror.LEFT_RIGHT));
                }
                this.z_mirrored = state.get(RotatedBlockWithEntity.Z_MIRRORED);
            }
        }
    }
}
