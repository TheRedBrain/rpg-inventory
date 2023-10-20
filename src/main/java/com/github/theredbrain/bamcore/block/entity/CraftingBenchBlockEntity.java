package com.github.theredbrain.bamcore.block.entity;

import com.github.theredbrain.bamcore.entity.player.DuckPlayerEntityMixin;
import com.github.theredbrain.bamcore.registry.EntityRegistry;
import com.github.theredbrain.bamcore.screen.CraftingBenchBlockScreenHandler;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class CraftingBenchBlockEntity extends BlockEntity implements ExtendedScreenHandlerFactory {

    private boolean isEnderChestInventoryProviderInReach = false;
    public CraftingBenchBlockEntity(BlockPos pos, BlockState state) {
        super(EntityRegistry.CRAFTING_BENCH_BLOCK_ENTITY, pos, state);
    }

//    public boolean openScreen(PlayerEntity player) {
//        if (!player.isCreativeLevelTwoOp()) {
//            return false;
//        }
//        if (player.getEntityWorld().isClient) {
//            ((DuckPlayerEntityMixin)player).bamcore$openCraftingBenchBlockScreen(this);
//        }
//        return true;
//    }

    @Override
    public void writeScreenOpeningData(ServerPlayerEntity player, PacketByteBuf buf) {
        buf.writeBlockPos(pos);
        updateBlocksInReach();
        buf.writeBoolean(this.isEnderChestInventoryProviderInReach);
        /* TODO

         */

    }

    @Nullable
    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
        return new CraftingBenchBlockScreenHandler(syncId, playerInventory, player.getEnderChestInventory(), ScreenHandlerContext.create(world, pos));
    }

    @Override
    public Text getDisplayName() {
        return Text.literal("WIP");
    }

    private void updateBlocksInReach() {
        int posX = this.pos.getX();
        int posY = this.pos.getY();
        int posZ = this.pos.getZ();
        World world = this.getWorld();

        BlockPos blockPos;
        if (world != null) {
            for (int i = -10; i <= 10; i++) {
                for (int j = -10; j <= 10; j++) {
                    for (int k = -10; k <= 10; k++) {
                        blockPos = new BlockPos(posX + i, posY + j, posZ + k);
                        if (world.getBlockState(blockPos).isOf(Blocks.ENDER_CHEST)) {
                            this.isEnderChestInventoryProviderInReach = true;
                        }
                    }
                }
            }
        }
    }
}
