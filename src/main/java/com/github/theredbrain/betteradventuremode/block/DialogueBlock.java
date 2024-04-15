package com.github.theredbrain.betteradventuremode.block;

import com.github.theredbrain.betteradventuremode.BetterAdventureMode;
import com.github.theredbrain.betteradventuremode.block.entity.DialogueBlockEntity;
import com.github.theredbrain.betteradventuremode.screen.DialogueBlockScreenHandler;
import com.mojang.serialization.MapCodec;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.*;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class DialogueBlock extends RotatedBlockWithEntity {

    public DialogueBlock(Settings settings) {
        super(settings);
    }

    // TODO Block Codecs
    public MapCodec<DialogueBlock> getCodec() {
        return null;
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new DialogueBlockEntity(pos, state);
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (world.isClient) {
            return ActionResult.SUCCESS;
        }
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof DialogueBlockEntity) {
            player.openHandledScreen(createDialogueBlockScreenHandlerFactory(state, world, pos, ""));
        }
        return ActionResult.CONSUME;
    }

    public static NamedScreenHandlerFactory createDialogueBlockScreenHandlerFactory(BlockState state, World world, BlockPos pos, String dialogueIdentifierString) {
        return new ExtendedScreenHandlerFactory() {
            @Override
            public void writeScreenOpeningData(ServerPlayerEntity player, PacketByteBuf buf) {
                buf.writeBlockPos(pos);
                buf.writeString(dialogueIdentifierString);
            }

            @Override
            public Text getDisplayName() {
                return Text.empty();
            }

            @Nullable
            @Override
            public ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
                return new DialogueBlockScreenHandler(syncId, playerInventory, player.isCreativeLevelTwoOp(), pos, dialogueIdentifierString);
            }
        };
    }
}
