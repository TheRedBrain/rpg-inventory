package com.github.theredbrain.betteradventuremode.inventory;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;

import java.util.List;

public class AdditionalPlayerInventory extends SimpleInventory {
    public final PlayerEntity player;

    public AdditionalPlayerInventory(int size, PlayerEntity player) {
        super(size);
        this.player = player;
    }

    @Override
    public void readNbtList(NbtList nbtList) {
        int i;
        for (i = 0; i < this.size(); ++i) {
            this.setStack(i, ItemStack.EMPTY);
        }
        for (i = 0; i < nbtList.size(); ++i) {
            NbtCompound nbtCompound = nbtList.getCompound(i);
            int j = nbtCompound.getByte("Slot") & 0xFF;
            if (j < 0 || j >= this.size()) continue;
            this.setStack(j, ItemStack.fromNbt(nbtCompound));
        }
    }

    @Override
    public NbtList toNbtList() {
        NbtList nbtList = new NbtList();
        for (int i = 0; i < this.size(); ++i) {
            ItemStack itemStack = this.getStack(i);
            if (itemStack.isEmpty()) continue;
            NbtCompound nbtCompound = new NbtCompound();
            nbtCompound.putByte("Slot", (byte)i);
            itemStack.writeNbt(nbtCompound);
            nbtList.add(nbtCompound);
        }
        return nbtList;
    }

    public void dropAll() {
        for (int i = 0; i < this.stacks.size(); ++i) {
            ItemStack itemStack = this.stacks.get(i);
            if (itemStack.isEmpty()) continue;
            this.player.dropItem(itemStack, true, false);
            this.stacks.set(i, ItemStack.EMPTY);
        }
    }
}
