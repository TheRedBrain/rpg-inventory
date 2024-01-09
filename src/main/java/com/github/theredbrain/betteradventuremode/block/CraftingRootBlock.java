package com.github.theredbrain.betteradventuremode.block;

import com.github.theredbrain.betteradventuremode.api.util.PacketByteBufUtils;
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
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class CraftingRootBlock extends Block {

    public static final int REACH_RADIUS = 10;

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
        player.openHandledScreen(createCraftingRootBlockScreenHandlerFactory(state, world, pos, Tab.CRAFTING_BENCH));
//        player.incrementStat(Stats.INTERACT_WITH_CRAFTING_TABLE); // TODO stats
        return ActionResult.CONSUME;
    }

    public static NamedScreenHandlerFactory createCraftingRootBlockScreenHandlerFactory(BlockState state, World world, BlockPos pos, CraftingRootBlock.Tab initialTab) {
        int posX = pos.getX();
        int posY = pos.getY();
        int posZ = pos.getZ();
        Set<String> craftingBenchLevelProviders = new HashSet<>();
        Set<String> smithyLevelProviders = new HashSet<>();
        boolean isStorageTabProviderInReach = false;
        boolean isSmithyTabProviderInReach = false;
        Map<String, Integer> tabLevels = new HashMap<>();

        BlockState blockState;
        if (world != null) {
            for (int i = -REACH_RADIUS; i <= REACH_RADIUS; i++) {
                for (int j = -REACH_RADIUS; j <= REACH_RADIUS; j++) {
                    for (int k = -REACH_RADIUS; k <= REACH_RADIUS; k++) {
                        blockState = world.getBlockState(new BlockPos(posX + i, posY + j, posZ + k));
                        if (blockState.isOf(BlockRegistry.STORAGE_PROVIDER_BLOCK)) {
                            isStorageTabProviderInReach = true;
                        }
                        if (blockState.isOf(BlockRegistry.SMITHY_PROVIDER_BLOCK)) {
                            isSmithyTabProviderInReach = true;
                        }
                        if (blockState.isIn(Tags.PROVIDES_CRAFTING_BENCH_LEVEL)) {
                            craftingBenchLevelProviders.add(blockState.getBlock().getTranslationKey());
                        }
                        if (blockState.isIn(Tags.PROVIDES_SMITHY_LEVEL)) {
                            smithyLevelProviders.add(blockState.getBlock().getTranslationKey());
                        }
                    }
                }
            }
        }

        tabLevels.put(Tab.CRAFTING_BENCH.asString(), craftingBenchLevelProviders.size());
        tabLevels.put(Tab.SMITHY.asString(), smithyLevelProviders.size());

        boolean finalIsStorageTabProviderInReach = isStorageTabProviderInReach;
        boolean finalIsSmithyTabProviderInReach = isSmithyTabProviderInReach;
        return new ExtendedScreenHandlerFactory() {
            @Override
            public void writeScreenOpeningData(ServerPlayerEntity player, PacketByteBuf buf) {
                buf.writeBlockPos(pos);
                buf.writeString(initialTab.asString());
                buf.writeBoolean(finalIsStorageTabProviderInReach);
                buf.writeBoolean(finalIsSmithyTabProviderInReach);
                buf.writeMap(tabLevels, PacketByteBuf::writeString, PacketByteBuf::writeInt);
            }

            @Override
            public Text getDisplayName() {
                return Text.literal("WIP");
            }

            @Nullable
            @Override
            public ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
                return new CraftingBenchBlockScreenHandler(syncId, playerInventory, player.getEnderChestInventory(), pos, initialTab, finalIsStorageTabProviderInReach, finalIsSmithyTabProviderInReach, tabLevels);
            }
        };
    }

    public static enum Tab implements StringIdentifiable {
        CRAFTING_BENCH("crafting_bench"),
        SMITHY("smithy"),
        STORAGE("storage");

        private final String name;

        private Tab(String name) {
            this.name = name;
        }

        @Override
        public String asString() {
            return this.name;
        }

        public static Optional<Tab> byName(String name) {
            return Arrays.stream(Tab.values()).filter(tab -> tab.asString().equals(name)).findFirst();
        }

        public Text asText() {
            return Text.translatable("gui.crafting_bench_block.tab." + this.name);
        }
    }
}
