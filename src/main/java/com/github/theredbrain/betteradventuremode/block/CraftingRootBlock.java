package com.github.theredbrain.betteradventuremode.block;

import com.github.theredbrain.betteradventuremode.registry.BlockRegistry;
import com.github.theredbrain.betteradventuremode.registry.Tags;
import com.github.theredbrain.betteradventuremode.screen.CraftingBenchBlockScreenHandler;
import com.mojang.serialization.MapCodec;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class CraftingRootBlock extends Block {

    public static final int REACH_RADIUS = 10;
    public static final int CRAFTING_TAB_AMOUNT = 4;

    public CraftingRootBlock(Settings settings) {
        super(settings);
    }

    // TODO Block Codecs
    public MapCodec<CraftingRootBlock> getCodec() {
        return null;
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (world.isClient) {
            return ActionResult.SUCCESS;
        }
        player.openHandledScreen(createCraftingRootBlockScreenHandlerFactory(state, world, pos, 0));
//        player.incrementStat(Stats.INTERACT_WITH_CRAFTING_TABLE); // TODO stats
        return ActionResult.CONSUME;
    }

    public static NamedScreenHandlerFactory createCraftingRootBlockScreenHandlerFactory(BlockState state, World world, BlockPos pos, int initialTab) {
        int posX = pos.getX();
        int posY = pos.getY();
        int posZ = pos.getZ();
        Set<String> craftingTab0LevelProviders = new HashSet<>();
        Set<String> craftingTab1LevelProviders = new HashSet<>();
        Set<String> craftingTab2LevelProviders = new HashSet<>();
        Set<String> craftingTab3LevelProviders = new HashSet<>();
        boolean isStorageTabProviderInReach = false;
        boolean isCraftingTab1ProviderInReach = false;
        boolean isCraftingTab2ProviderInReach = false;
        boolean isCraftingTab3ProviderInReach = false;
        int[] tabLevels = new int[CRAFTING_TAB_AMOUNT];
        byte tabProvidersInReach = 0;

        BlockState blockState;
        if (world != null) {
            for (int i = -REACH_RADIUS; i <= REACH_RADIUS; i++) {
                for (int j = -REACH_RADIUS; j <= REACH_RADIUS; j++) {
                    for (int k = -REACH_RADIUS; k <= REACH_RADIUS; k++) {
                        blockState = world.getBlockState(new BlockPos(posX + i, posY + j, posZ + k));
                        if (blockState.isOf(BlockRegistry.STORAGE_PROVIDER_BLOCK)) {
                            isStorageTabProviderInReach = true;
                        }
                        if (blockState.isOf(BlockRegistry.CRAFTING_TAB_1_PROVIDER_BLOCK)) {
                            isCraftingTab1ProviderInReach = true;
                        }
                        if (blockState.isOf(BlockRegistry.CRAFTING_TAB_2_PROVIDER_BLOCK)) {
                            isCraftingTab2ProviderInReach = true;
                        }
                        if (blockState.isOf(BlockRegistry.CRAFTING_TAB_3_PROVIDER_BLOCK)) {
                            isCraftingTab3ProviderInReach = true;
                        }
                        if (blockState.isIn(Tags.PROVIDES_CRAFTING_TAB_0_LEVEL)) {
                            craftingTab0LevelProviders.add(blockState.getBlock().getTranslationKey());
                        }
                        if (blockState.isIn(Tags.PROVIDES_CRAFTING_TAB_1_LEVEL)) {
                            craftingTab1LevelProviders.add(blockState.getBlock().getTranslationKey());
                        }
                        if (blockState.isIn(Tags.PROVIDES_CRAFTING_TAB_2_LEVEL)) {
                            craftingTab2LevelProviders.add(blockState.getBlock().getTranslationKey());
                        }
                        if (blockState.isIn(Tags.PROVIDES_CRAFTING_TAB_3_LEVEL)) {
                            craftingTab3LevelProviders.add(blockState.getBlock().getTranslationKey());
                        }
                    }
                }
            }
        }

        tabLevels[0] = craftingTab0LevelProviders.size();
        tabLevels[1] = craftingTab1LevelProviders.size();
        tabLevels[2] = craftingTab2LevelProviders.size();
        tabLevels[3] = craftingTab3LevelProviders.size();

        tabProvidersInReach = (byte)(isStorageTabProviderInReach ? tabProvidersInReach | 1 << 0 : tabProvidersInReach & ~(1 << 0));
        tabProvidersInReach = (byte)(isCraftingTab1ProviderInReach ? tabProvidersInReach | 1 << 1 : tabProvidersInReach & ~(1 << 1));
        tabProvidersInReach = (byte)(isCraftingTab2ProviderInReach ? tabProvidersInReach | 1 << 2 : tabProvidersInReach & ~(1 << 2));
        tabProvidersInReach = (byte)(isCraftingTab3ProviderInReach ? tabProvidersInReach | 1 << 3 : tabProvidersInReach & ~(1 << 3));

        byte finalTabProvidersInReach = tabProvidersInReach;
        return new ExtendedScreenHandlerFactory() {
            @Override
            public void writeScreenOpeningData(ServerPlayerEntity player, PacketByteBuf buf) {
                buf.writeBlockPos(pos);
                buf.writeInt(initialTab);
                buf.writeByte(finalTabProvidersInReach);
                buf.writeIntArray(tabLevels);
            }

            @Override
            public Text getDisplayName() {
                return Text.literal("WIP");
            }

            @Nullable
            @Override
            public ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
                return new CraftingBenchBlockScreenHandler(syncId, playerInventory, player.getEnderChestInventory(), pos, initialTab, finalTabProvidersInReach, tabLevels);
            }
        };
    }
}
