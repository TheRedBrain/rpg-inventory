package com.github.theredbrain.betteradventuremode.network.packet;

import com.github.theredbrain.betteradventuremode.data.DialogueAnswer;
import com.github.theredbrain.betteradventuremode.util.ItemUtils;
import com.github.theredbrain.betteradventuremode.block.Triggerable;
import com.github.theredbrain.betteradventuremode.block.entity.DialogueBlockEntity;
import com.github.theredbrain.betteradventuremode.registry.DialogueAnswersRegistry;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.advancement.AdvancementEntry;
import net.minecraft.entity.ItemEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContextParameterSet;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.loot.context.LootContextTypes;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.apache.commons.lang3.tuple.MutablePair;

import java.util.ArrayList;
import java.util.List;

public class DialogueAnswerPacketReceiver implements ServerPlayNetworking.PlayPacketHandler<DialogueAnswerPacket> {

    @Override
    public void receive(DialogueAnswerPacket packet, ServerPlayerEntity player, PacketSender responseSender) {

        BlockPos dialogueBlockPos = packet.dialogueBlockPos;

        Identifier answerIdentifier = packet.answerIdentifier;

        DialogueAnswer dialogueAnswer = DialogueAnswersRegistry.getDialogueAnswer(answerIdentifier);

        MinecraftServer server = player.getServer();

        if (dialogueAnswer != null && server != null && player.getWorld().getBlockEntity(dialogueBlockPos) instanceof DialogueBlockEntity dialogueBlockEntity) {

            List<ItemUtils.VirtualItemStack> virtualItemStacks = dialogueAnswer.getItemCost();
            if (virtualItemStacks != null) {

                int playerInventorySize = player.getInventory().size();
                Inventory playerInventoryCopy = new SimpleInventory(playerInventorySize);
                ItemStack itemStack;

                for (int k = 0; k < playerInventorySize; k++) {
                    playerInventoryCopy.setStack(k, player.getInventory().getStack(k).copy());
                }

                for (ItemUtils.VirtualItemStack ingredient : virtualItemStacks) {
                    Item virtualItem = ItemUtils.getItemStackFromVirtualItemStack(ingredient).getItem();
                    int ingredientCount = ingredient.getCount();

                    for (int j = 0; j < playerInventorySize; j++) {
                        if (playerInventoryCopy.getStack(j).isOf(virtualItem)) {
                            itemStack = playerInventoryCopy.getStack(j).copy();
                            int stackCount = itemStack.getCount();
                            if (stackCount >= ingredientCount) {
                                itemStack.setCount(stackCount - ingredientCount);
                                playerInventoryCopy.setStack(j, itemStack);
                                ingredientCount = 0;
                                break;
                            } else {
                                playerInventoryCopy.setStack(j, ItemStack.EMPTY);
                                ingredientCount = ingredientCount - stackCount;
                            }
                        }
                    }
                    if (ingredientCount > 0) {
                        player.sendMessage(Text.translatable("gui.dialogue_screen.item_cost_too_high"));
                        return;
                    }
                }

                for (ItemUtils.VirtualItemStack ingredient : virtualItemStacks) {
                    Item virtualItem = ItemUtils.getItemStackFromVirtualItemStack(ingredient).getItem();
                    int ingredientCount = ingredient.getCount();

                    for (int j = 0; j < playerInventorySize; j++) {
                        if (player.getInventory().getStack(j).isOf(virtualItem)) {
                            itemStack = player.getInventory().getStack(j).copy();
                            int stackCount = itemStack.getCount();
                            if (stackCount >= ingredientCount) {
                                itemStack.setCount(stackCount - ingredientCount);
                                player.getInventory().setStack(j, itemStack);
                                ingredientCount = 0;
                                break;
                            } else {
                                player.getInventory().setStack(j, ItemStack.EMPTY);
                                ingredientCount = ingredientCount - stackCount;
                            }
                        }
                    }
                    if (ingredientCount > 0) {
                        return;
                    }
                }
            }

            // loot_table
            Identifier lootTableIdentifier = dialogueAnswer.getLootTable();
            if (lootTableIdentifier != null) {
                LootContextParameterSet lootContextParameterSet = new LootContextParameterSet.Builder(player.getServerWorld()).add(LootContextParameters.THIS_ENTITY, player).add(LootContextParameters.ORIGIN, player.getPos()).build(LootContextTypes.ADVANCEMENT_REWARD);
                boolean bl = false;
                for (ItemStack itemStack : server.getLootManager().getLootTable(lootTableIdentifier).generateLoot(lootContextParameterSet)) {
                    if (player.giveItemStack(itemStack)) {
                        player.getWorld().playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.PLAYERS, 0.2f, ((player.getRandom().nextFloat() - player.getRandom().nextFloat()) * 0.7f + 1.0f) * 2.0f);
                        bl = true;
                        continue;
                    }
                    ItemEntity itemEntity = player.dropItem(itemStack, false);
                    if (itemEntity == null) continue;
                    itemEntity.resetPickupDelay();
                    itemEntity.setOwner(player.getUuid());
                }
                if (bl) {
                    player.currentScreenHandler.sendContentUpdates();
                }
            }

            // advancement
            Identifier advancementIdentifier = dialogueAnswer.getGrantedAdvancement();
            String criterionName = dialogueAnswer.getCriterionName();
            if (advancementIdentifier != null && criterionName != null) {
                AdvancementEntry advancementEntry = server.getAdvancementLoader().get(advancementIdentifier);
                if (advancementEntry != null) {
                    player.getAdvancementTracker().grantCriterion(advancementEntry, criterionName);
                }
            }

            ServerPlayNetworking.send(player, new OpenDialogueScreenPacket(dialogueBlockPos, dialogueAnswer.getResponseDialogue()));


            // trigger block
            String triggeredBlock = dialogueAnswer.getTriggeredBlock();
            if (triggeredBlock != null) {
                List<MutablePair<String, BlockPos>> dialogueTriggeredBlocksList = new ArrayList<>(List.of());
                List<String> keyList = new ArrayList<>(dialogueBlockEntity.getDialogueTriggeredBlocks().keySet());
                for (String key : keyList) {
                    dialogueTriggeredBlocksList.add(new MutablePair<>(key, dialogueBlockEntity.getDialogueTriggeredBlocks().get(key)));
                }
                for (MutablePair<String, BlockPos> entry : dialogueTriggeredBlocksList) {
                    if (entry.getLeft().equals(triggeredBlock)) {

                        BlockPos blockPos = entry.getRight().add(dialogueBlockPos);

                        if (player.getWorld().getBlockEntity(blockPos) instanceof Triggerable triggerable) {
                            triggerable.trigger();
                        }
                        break;
                    }
                }
            }

            // use block
            String usedBlock = dialogueAnswer.getUsedBlock();
            if (usedBlock != null) {
                List<MutablePair<String, BlockPos>> dialogueUsedBlocksList = new ArrayList<>(List.of());
                List<String> keyList = new ArrayList<>(dialogueBlockEntity.getDialogueUsedBlocks().keySet());
                for (String key : keyList) {
                    dialogueUsedBlocksList.add(new MutablePair<>(key, dialogueBlockEntity.getDialogueUsedBlocks().get(key)));
                }
                for (MutablePair<String, BlockPos> entry : dialogueUsedBlocksList) {
                    if (entry.getLeft().equals(usedBlock)) {
                        BlockHitResult blockHitResult = new BlockHitResult(player.getPos(), Direction.UP, entry.getRight().add(dialogueBlockPos), false);
                        World world = player.getWorld();
                        Hand hand = player.getActiveHand();
                        ItemStack itemStack = player.getStackInHand(hand);

                        player.interactionManager.interactBlock(player, world, itemStack, hand, blockHitResult);
                    }
                }
            }
        }
    }
}
