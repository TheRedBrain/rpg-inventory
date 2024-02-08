package com.github.theredbrain.betteradventuremode.network.packet;

import com.github.theredbrain.betteradventuremode.BetterAdventureMode;
import net.fabricmc.fabric.api.networking.v1.FabricPacket;
import net.fabricmc.fabric.api.networking.v1.PacketType;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;

public class UpdateMannequinEquipmentPacket implements FabricPacket {
    public static final PacketType<UpdateMannequinEquipmentPacket> TYPE = PacketType.create(
            BetterAdventureMode.identifier("update_mannequin_equipment"),
            UpdateMannequinEquipmentPacket::new
    );

    public final int mannequinEntityId;
    public final int index;
    public final ItemStack equipmentStack;

    public UpdateMannequinEquipmentPacket(int mannequinEntityId, ItemStack equipmentStack, int index) {
        this.mannequinEntityId = mannequinEntityId;
        this.equipmentStack = equipmentStack;
        this.index = index;
    }

    public UpdateMannequinEquipmentPacket(PacketByteBuf buf) {
        this(buf.readInt(), buf.readItemStack(), buf.readInt());
    }
    @Override
    public PacketType<?> getType() {
        return TYPE;
    }
    @Override
    public void write(PacketByteBuf buf) {
        buf.writeInt(this.mannequinEntityId);
        buf.writeItemStack(this.equipmentStack);
        buf.writeInt(this.index);
    }

}
