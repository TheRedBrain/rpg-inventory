package com.github.theredbrain.betteradventuremode.block.entity;

import com.github.theredbrain.betteradventuremode.data.Shop;
import com.github.theredbrain.betteradventuremode.registry.EntityRegistry;
import com.github.theredbrain.betteradventuremode.registry.ShopsRegistry;
import com.github.theredbrain.betteradventuremode.screen.ShopBlockScreenHandler;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class ShopBlockEntity extends BlockEntity implements ExtendedScreenHandlerFactory {
    private String shopIdentifier = "";

    private List<Integer> stockCountList = new ArrayList<>();
    public ShopBlockEntity(BlockPos pos, BlockState state) {
        super(EntityRegistry.SHOP_BLOCK_ENTITY, pos, state);
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {

        if (!this.shopIdentifier.equals("")) {
            nbt.putString("shopName", this.shopIdentifier);
        }

        super.writeNbt(nbt);
    }

    @Override
    public void readNbt(NbtCompound nbt) {

        if (nbt.contains("shopName")) {
            this.shopIdentifier = nbt.getString("shopName");
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

    public String getShopIdentifier() {
        return shopIdentifier;
    }

    public boolean setShopIdentifier(String newShopIdentifier) {
        Shop shop = null;
        if (Identifier.isValid(newShopIdentifier)) {
            shop = ShopsRegistry.getShop(new Identifier(newShopIdentifier));
        }
        if (newShopIdentifier.equals("") || shop != null) {
            this.shopIdentifier = newShopIdentifier;
            this.stockCountList.clear();

            if (shop != null) {
                List<Shop.Deal> dealList = shop.getDealList();
                for (int i = 0; i < dealList.size(); i++) {
                    this.stockCountList.add(dealList.get(i).getMaxStockCount());
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public void writeScreenOpeningData(ServerPlayerEntity player, PacketByteBuf buf) {
        buf.writeBlockPos(pos);
    }

    @Nullable
    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
        return new ShopBlockScreenHandler(syncId, playerInventory, this.pos, player.isCreativeLevelTwoOp());
    }

    @Override
    public Text getDisplayName() {
        return Text.empty();
    }
}
