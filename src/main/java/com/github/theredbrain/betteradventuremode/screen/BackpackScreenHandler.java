package com.github.theredbrain.betteradventuremode.screen;

import com.github.theredbrain.betteradventuremode.entity.player.DuckPlayerEntityMixin;
import com.github.theredbrain.betteradventuremode.inventory.AdditionalPlayerInventory;
import com.github.theredbrain.betteradventuremode.registry.ScreenHandlerTypesRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;

public class BackpackScreenHandler extends ScreenHandler {

    private final PlayerInventory playerInventory;
    private final AdditionalPlayerInventory backpackInventory;
    private final int backpackCapacity;

    public BackpackScreenHandler(int syncId, PlayerInventory playerInventory) {
        super(ScreenHandlerTypesRegistry.BACKPACK_SCREEN_HANDLER, syncId);
        this.playerInventory = playerInventory;
        this.backpackInventory = ((DuckPlayerEntityMixin) playerInventory.player).betteradventuremode$getBackpackInventory();
        this.backpackCapacity = ((DuckPlayerEntityMixin) playerInventory.player).betteradventuremode$getBackpackCapacity();

        int i;
        // hotbar 0 - 8
        for (i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 142));
        }
        // main inventory 9 - 35
        for (i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }
        // backpack 36 - 62
        for (i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(backpackInventory, j + i * 9, 8 + j * 18, 18 + i * 18));
            }
        }
    }

    @Override
    public ItemStack quickMove(PlayerEntity player, int slot) { // TODO
//        ItemStack itemStack = ItemStack.EMPTY;
//        Slot slot2 = (Slot)this.slots.get(slot);
//        if (slot2 != null && slot2.hasStack()) {
//            ItemStack itemStack2 = slot2.getStack();
//            itemStack = itemStack2.copy();
//            if (slot < this.inventory.size() ? !this.insertItem(itemStack2, this.inventory.size(), this.slots.size(), true) : !this.insertItem(itemStack2, 0, this.inventory.size(), false)) {
//                return ItemStack.EMPTY;
//            }
//            if (itemStack2.isEmpty()) {
//                slot2.setStack(ItemStack.EMPTY);
//            } else {
//                slot2.markDirty();
//            }
//        }
//        return itemStack;
        return ItemStack.EMPTY;
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return true;
    }

    public AdditionalPlayerInventory getBackpackInventory() {
        return this.backpackInventory;
    }

    public PlayerInventory getPlayerInventory() {
        return this.playerInventory;
    }

    public int getBackpackCapacity() {
        return backpackCapacity;
    }
}
