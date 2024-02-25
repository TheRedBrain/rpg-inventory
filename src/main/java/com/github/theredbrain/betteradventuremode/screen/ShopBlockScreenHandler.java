package com.github.theredbrain.betteradventuremode.screen;

import com.github.theredbrain.betteradventuremode.BetterAdventureMode;
import com.github.theredbrain.betteradventuremode.data.Shop;
import com.github.theredbrain.betteradventuremode.block.entity.ShopBlockEntity;
import com.github.theredbrain.betteradventuremode.client.network.DuckClientAdvancementManagerMixin;
import com.github.theredbrain.betteradventuremode.network.packet.TradeWithShopPacket;
import com.github.theredbrain.betteradventuremode.registry.ScreenHandlerTypesRegistry;
import com.github.theredbrain.betteradventuremode.registry.ShopsRegistry;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
//import net.minecraft.advancement.AdvancementEntry;
import net.minecraft.advancement.Advancement;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.network.ClientAdvancementManager;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class ShopBlockScreenHandler extends ScreenHandler {

    private PlayerInventory playerInventory;
    private final World world;
    private ShopBlockEntity shopBlockEntity;
    private @Nullable Shop shop;
    private List<Shop.Deal> dealsList = new ArrayList<>(List.of());
    private List<Shop.Deal> unlockedDealsList = new ArrayList<>(List.of());
    private List<Shop.Deal> stockedDealsList = new ArrayList<>(List.of());
    private int unlockedDealsCounter = 0;
    private int stockedDealsCounter = 0;
    Runnable contentsChangedListener = () -> {
    };
    public final Inventory inventory = new SimpleInventory(9) {
        @Override
        public int getMaxCountPerStack() {
            return 999;
        }

        @Override
        public void markDirty() {
            super.markDirty();
            ShopBlockScreenHandler.this.onContentChanged(this);
            ShopBlockScreenHandler.this.contentsChangedListener.run();
        }
    };

    private BlockPos blockPos;

    private boolean showCreativeTab;

    public ShopBlockScreenHandler(int syncId, PlayerInventory playerInventory, PacketByteBuf buf) {
        this(syncId, playerInventory, buf.readBlockPos(), playerInventory.player.isCreativeLevelTwoOp());
    }

    public ShopBlockScreenHandler(int syncId, PlayerInventory playerInventory, BlockPos blockPos, boolean showCreativeTab) {
        super(ScreenHandlerTypesRegistry.SHOP_BLOCK_SCREEN_HANDLER, syncId);
        this.playerInventory = playerInventory;
        this.world = playerInventory.player.getWorld();
        this.blockPos = blockPos;
        this.showCreativeTab = showCreativeTab;
        BlockEntity blockEntity = this.world.getBlockEntity(this.blockPos);
        if (blockEntity instanceof ShopBlockEntity) {
            this.shopBlockEntity = (ShopBlockEntity) blockEntity;
            Shop shop = null;
            String shopIdentifier = this.shopBlockEntity.getShopIdentifier();
            if (!shopIdentifier.equals("")) {
                shop = ShopsRegistry.getShop(new Identifier(shopIdentifier));
            }
            this.shop = shop;
            if (shop != null) {
                this.dealsList = shop.getDealList();
            }
        }
        int i;
        // hotbar
        for (i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 188));
        }
        // main inventory
        for (i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(playerInventory, j + (i + 1) * 9, 8 + j * 18, 130 + i * 18));
            }
        }
        // player offers
        for (i = 0; i < 9; ++i) {
            this.addSlot(new Slot(inventory, i, 8 + i * 18, 98));
        }
    }

    public BlockPos getBlockPos() {
        return this.blockPos;
    }

    public boolean getShowCreativeTab() {
        return this.showCreativeTab;
    }

    public ShopBlockEntity getShopBlockEntity() {
        return this.shopBlockEntity;
    }

    @Nullable
    public Shop getShop() {
        return this.shop;
    }

    public List<Shop.Deal> getDealsList() {
        return this.dealsList;
    }

    public List<Shop.Deal> getUnlockedDealsList() {
        return this.unlockedDealsList;
    }

    public List<Shop.Deal> getStockedDealsList() {
        return this.stockedDealsList;
    }

    public int getUnlockedDealsCounter() {
        return this.unlockedDealsCounter;
    }

    public int getStockedDealsCounter() {
        return this.stockedDealsCounter;
    }

    public Inventory getInventory() {
        return this.inventory;
    }

    public void setContentsChangedListener(Runnable contentsChangedListener) {
        this.contentsChangedListener = contentsChangedListener;
    }

    @Override
    public ItemStack quickMove(PlayerEntity player, int slot) {
        ItemStack itemStack = ItemStack.EMPTY;
        // TODO
//        Slot slot2 = this.slots.get(slot);
//        if (slot2 != null && slot2.hasStack()) {
//            ItemStack itemStack2 = slot2.getStack();
//            itemStack = itemStack2.copy();
//            if (slot == 0) {
//                this.context.run((world, pos) -> itemStack2.getItem().onCraftByPlayer(itemStack2, world, player));
//                if (!this.insertItem(itemStack2, 10, 46, true)) {
//                    return ItemStack.EMPTY;
//                }
//
//                slot2.onQuickTransfer(itemStack2, itemStack);
//            } else if (slot >= 10 && slot < 46) {
//                if (!this.insertItem(itemStack2, 1, 10, false)) {
//                    if (slot < 37) {
//                        if (!this.insertItem(itemStack2, 37, 46, false)) {
//                            return ItemStack.EMPTY;
//                        }
//                    } else if (!this.insertItem(itemStack2, 10, 37, false)) {
//                        return ItemStack.EMPTY;
//                    }
//                }
//            } else if (!this.insertItem(itemStack2, 10, 46, false)) {
//                return ItemStack.EMPTY;
//            }
//
//            if (itemStack2.isEmpty()) {
//                slot2.setStack(ItemStack.EMPTY);
//            } else {
//                slot2.markDirty();
//            }
//
//            if (itemStack2.getCount() == itemStack.getCount()) {
//                return ItemStack.EMPTY;
//            }
//
//            slot2.onTakeItem(player, itemStack2);
//            if (slot == 0) {
//                player.dropItem(itemStack2, false);
//            }
//        }
//
        return itemStack;
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return true;
    }

    @Override
    public boolean onButtonClick(PlayerEntity player, int id) {
        if (id < this.stockedDealsList.size()) {
            Shop.Deal currentDeal = this.stockedDealsList.get(id);
            if (currentDeal != null) {
                ClientPlayNetworking.send(
                        new TradeWithShopPacket(
                                this.shopBlockEntity.getShopIdentifier(),
                                id
                        )
                );
            }
        }
        return true;
    }

    @Override
    public void onClosed(PlayerEntity player) {
        super.onClosed(player);
        if (player instanceof ServerPlayerEntity) {
            this.dropInventory(player, this.inventory);
        }
    }

    public void calculateUnlockedAndStockedDeals() {

        BetterAdventureMode.info("calculateUnlockedAndStockedDeals");

        ClientAdvancementManager advancementHandler = null;
        String lockAdvancementIdentifier;
        String unlockAdvancementIdentifier;

        if (this.world.isClient && this.playerInventory.player instanceof ClientPlayerEntity clientPlayerEntity) {

            BetterAdventureMode.info("this.world.isClient && this.playerInventory.player instanceof ClientPlayerEntity clientPlayerEntity");

            advancementHandler = clientPlayerEntity.networkHandler.getAdvancementHandler();
            this.unlockedDealsList.clear();
            this.unlockedDealsCounter = 0;
        }

        if (advancementHandler != null) {

            BetterAdventureMode.info("advancementHandler != null");

            BetterAdventureMode.info("this.dealsList.size(): " + this.dealsList.size());

            for (int i = 0; i < this.dealsList.size(); i++) {

                Shop.Deal deal = this.dealsList.get(i);
                lockAdvancementIdentifier = deal.getLockAdvancement();
                unlockAdvancementIdentifier = deal.getUnlockAdvancement();

//                AdvancementEntry lockAdvancementEntry = null;
                Advancement lockAdvancement = null;
                if (!lockAdvancementIdentifier.equals("")) {
                    lockAdvancement = advancementHandler.getManager().get(Identifier.tryParse(lockAdvancementIdentifier));
                }
//                AdvancementEntry unlockAdvancementEntry = null;
                Advancement unlockAdvancement = null;
                if (!unlockAdvancementIdentifier.equals("")) {
                    unlockAdvancement = advancementHandler.getManager().get(Identifier.tryParse(unlockAdvancementIdentifier));
                }
                if ((lockAdvancementIdentifier.equals("") || (lockAdvancement != null && !((DuckClientAdvancementManagerMixin) advancementHandler.getManager()).betteradventuremode$getAdvancementProgress(lockAdvancement).isDone())) &&
                        (unlockAdvancementIdentifier.equals("") || (unlockAdvancement != null && ((DuckClientAdvancementManagerMixin) advancementHandler.getManager()).betteradventuremode$getAdvancementProgress(unlockAdvancement).isDone()))) {
                    this.unlockedDealsList.add(deal);
                    this.unlockedDealsCounter++;
                } else {
                    this.unlockedDealsList.add(null);
                }
            }
        }

        this.stockedDealsList.clear();
        this.stockedDealsCounter = 0;
        for (Shop.Deal deal : this.unlockedDealsList) {
            if (deal != null) {
                // TODO keep track of times traded, per player or per block, depending on deal.
//                int dealMaxStockCount = deal.getMaxStockCount();
//                if (dealMaxStockCount == -1) {
//                  this.stockedDealsList.add(deal);
//                }
                this.stockedDealsList.add(deal);
                this.stockedDealsCounter++;
            } else {
                this.stockedDealsList.add(null);
            }
        }
    }
}
