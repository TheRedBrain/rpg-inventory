package com.github.theredbrain.betteradventuremode.screen;

import com.github.theredbrain.betteradventuremode.registry.ScreenHandlerTypesRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;

public class TeleporterBlockScreenHandler extends ScreenHandler {

    private PlayerInventory playerInventory;
    private Inventory inventory = new SimpleInventory(9) {
        @Override
        public int getMaxCountPerStack() {
            return 999;
        }

        @Override
        public void markDirty() {
            super.markDirty();
//            ShopBlockScreenHandler.this.onContentChanged(this);
//            ShopBlockScreenHandler.this.contentsChangedListener.run();
        }
    };
//    private TeleporterBlockBlockEntity teleporterBlockBlockEntity;

    private BlockPos blockPos;

    private boolean showCreativeTab;
    private final int requiredKeyItemStackSlotX = 66;
    private final int requiredKeyItemStackSlotY = 148;

    public TeleporterBlockScreenHandler(int syncId, PlayerInventory playerInventory, PacketByteBuf buf) {
        this(syncId, playerInventory, playerInventory.player.isCreativeLevelTwoOp());
        this.blockPos = buf.readBlockPos();
    }

    public TeleporterBlockScreenHandler(int syncId, PlayerInventory playerInventory, boolean showCreativeTab) {
        super(ScreenHandlerTypesRegistry.TELEPORTER_BLOCK_SCREEN_HANDLER, syncId);
        // TODO
        // set teleporterBlockBlockEntity
        this.playerInventory = playerInventory;
        this.blockPos = BlockPos.ORIGIN;
        this.showCreativeTab = showCreativeTab;

        int i;
        // hotbar
        for (i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, showCreativeTab ? 273 : 273/*TODO*/)/* {

                @Override
                public boolean canInsert(ItemStack stack) {
                    return stack.isIn(Tags.ADVENTURE_HOTBAR_ITEMS);
                }
            }*/);
        }
        // main inventory
        for (i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(playerInventory, j + (i + 1) * 9, 8 + j * 18, (showCreativeTab ? 215 : 215)/*TODO*/ + i * 18));
            }
        }
        // keyItemStackSlot
        this.addSlot(new Slot(new SimpleInventory(1), 0, 77, 80) {
//            @Override
//            public boolean isEnabled() {
//                return !showCreativeTab && !(inventory.getStack(0).isEmpty());
//            }
        });

//        // requiredKeyItemStackSlot
//        this.addSlot(new Slot(inventory, 0, this.requiredKeyItemStackSlotX, this.requiredKeyItemStackSlotY) {
////            @Override
////            public boolean isEnabled() {
////                return showCreativeTab;
////            }
////
////            @Override
////            public void setStack(ItemStack stack) {
////                super.setStack(stack);
////                this.inventory.setStack(0, stack);
////            }
//        });

        // requiredKeyItemStackSlot in AdventureScreen
//        this.addSlot(new Slot(inventory, 0, 55, 80) {
////            @Override
////            public boolean isEnabled() {
////                return !showCreativeTab && !(inventory.getStack(0).isEmpty());
////            }
//            @Override
//            public boolean canTakeItems(PlayerEntity playerEntity) {
//                return false;
//            }
//            @Override
//            public boolean canInsert(ItemStack stack) {
//                return false;
//            }
//        });
    }

    public BlockPos getBlockPos() {
        return blockPos;
    }

    public int getRequiredKeyItemStackSlotX() {
        return requiredKeyItemStackSlotX;
    }

    public int getRequiredKeyItemStackSlotY() {
        return requiredKeyItemStackSlotY;
    }

    public PlayerInventory getPlayerInventory() {
        return playerInventory;
    }

    public boolean getShowCreativeTab() {
        return this.showCreativeTab;
    }

    @Override
    public ItemStack quickMove(PlayerEntity player, int slot) {
        // TODO
        return null;
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return this.inventory.canPlayerUse(player);
    }

    @Override
    public void onClosed(PlayerEntity player) {
        super.onClosed(player);
        ItemStack itemStack;
        if (player instanceof ServerPlayerEntity && !(itemStack = this.getSlot(36).getStack()).isEmpty()) {
            if (!player.isAlive() || ((ServerPlayerEntity)player).isDisconnected()) {
                player.dropItem(itemStack, false);
            } else {
                player.getInventory().offerOrDrop(itemStack);
            }
        }
    }
}
