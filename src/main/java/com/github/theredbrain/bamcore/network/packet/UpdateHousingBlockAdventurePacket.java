package com.github.theredbrain.bamcore.network.packet;

import com.github.theredbrain.bamcore.BetterAdventureModeCore;
import com.github.theredbrain.bamcore.api.util.PacketByteBufUtils;
import net.fabricmc.fabric.api.networking.v1.FabricPacket;
import net.fabricmc.fabric.api.networking.v1.PacketType;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.math.BlockPos;

import java.util.List;

public class UpdateHousingBlockAdventurePacket implements FabricPacket {
    public static final PacketType<UpdateHousingBlockAdventurePacket> TYPE = PacketType.create(
            BetterAdventureModeCore.identifier("update_housing_block_adventure"),
            UpdateHousingBlockAdventurePacket::new
    );

    public final BlockPos housingBlockPosition;
    public final List<String> coOwnerList;
    public final List<String> trustedList;
    public final List<String> guestList;

    public UpdateHousingBlockAdventurePacket(BlockPos housingBlockPosition, List<String> coOwnerList, List<String> trustedList, List<String> guestList) {
        this.housingBlockPosition = housingBlockPosition;
        this.coOwnerList = coOwnerList;
        this.trustedList = trustedList;
        this.guestList = guestList;
    }

    public UpdateHousingBlockAdventurePacket(PacketByteBuf buf) {
        this(buf.readBlockPos(), buf.readList(new PacketByteBufUtils.StringListReader()), buf.readList(new PacketByteBufUtils.StringListReader()), buf.readList(new PacketByteBufUtils.StringListReader()));
    }
    @Override
    public PacketType<?> getType() {
        return TYPE;
    }
    @Override
    public void write(PacketByteBuf buf) {
        buf.writeBlockPos(this.housingBlockPosition);
        buf.writeCollection(this.coOwnerList, new PacketByteBufUtils.StringListWriter());
        buf.writeCollection(this.trustedList, new PacketByteBufUtils.StringListWriter());
        buf.writeCollection(this.guestList, new PacketByteBufUtils.StringListWriter());
    }

}
