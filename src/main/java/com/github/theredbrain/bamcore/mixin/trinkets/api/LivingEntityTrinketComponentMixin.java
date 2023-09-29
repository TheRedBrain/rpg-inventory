package com.github.theredbrain.bamcore.mixin.trinkets.api;

import com.github.theredbrain.bamcore.entity.player.DuckPlayerEntityMixin;
import dev.emi.trinkets.TrinketPlayerScreenHandler;
import dev.emi.trinkets.api.LivingEntityTrinketComponent;
import dev.emi.trinkets.api.TrinketInventory;
import net.fabricmc.fabric.api.util.NbtType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.network.PacketByteBuf;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import java.util.Map;

@Mixin(LivingEntityTrinketComponent.class)
public class LivingEntityTrinketComponentMixin {

    /**
     * @author TheRedBrain
     * @reason account for AdventureInventoryScreenHandler
     */
    @Overwrite
    public void applySyncPacket(PacketByteBuf buf) {
        NbtCompound tag = buf.readNbt();

        if (tag != null) {

            for (String groupKey : tag.getKeys()) {
                NbtCompound groupTag = tag.getCompound(groupKey);

                if (groupTag != null) {
                    Map<String, TrinketInventory> groupSlots = ((LivingEntityTrinketComponent) (Object) this).inventory.get(groupKey);

                    if (groupSlots != null) {

                        for (String slotKey : groupTag.getKeys()) {
                            NbtCompound slotTag = groupTag.getCompound(slotKey);
                            NbtList list = slotTag.getList("Items", NbtType.COMPOUND);
                            TrinketInventory inv = groupSlots.get(slotKey);

                            if (inv != null) {
                                inv.applySyncTag(slotTag.getCompound("Metadata"));
                            }

                            for (int i = 0; i < list.size(); i++) {
                                NbtCompound c = list.getCompound(i);
                                ItemStack stack = ItemStack.fromNbt(c);
                                if (inv != null && i < inv.size()) {
                                    inv.setStack(i, stack);
                                }
                            }
                        }
                    }
                }
            }

            if (((LivingEntityTrinketComponent) (Object) this).entity instanceof PlayerEntity player) {
                ((TrinketPlayerScreenHandler) player.playerScreenHandler).trinkets$updateTrinketSlots(false);
                ((TrinketPlayerScreenHandler) ((DuckPlayerEntityMixin)player).bamcore$getInventoryScreenHandler()).trinkets$updateTrinketSlots(false);
            }
        }
    }
}
