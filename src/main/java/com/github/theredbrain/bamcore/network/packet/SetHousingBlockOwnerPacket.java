package com.github.theredbrain.bamcore.network.packet;

import com.github.theredbrain.bamcore.BetterAdventureModeCore;
import net.fabricmc.fabric.api.networking.v1.FabricPacket;
import net.fabricmc.fabric.api.networking.v1.PacketType;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.math.BlockPos;

public class SetHousingBlockOwnerPacket implements FabricPacket {
    public static final PacketType<SetHousingBlockOwnerPacket> TYPE = PacketType.create(
            BetterAdventureModeCore.identifier("set_housing_block_owner"),
            SetHousingBlockOwnerPacket::new
    );

    public final BlockPos housingBlockPosition;
    public final String owner;

    public SetHousingBlockOwnerPacket(BlockPos housingBlockPosition, String owner) {
        this.housingBlockPosition = housingBlockPosition;
        this.owner = owner;
    }

    public SetHousingBlockOwnerPacket(PacketByteBuf buf) {
        this(buf.readBlockPos(), buf.readString());
    }
    @Override
    public PacketType<?> getType() {
        return TYPE;
    }
    @Override
    public void write(PacketByteBuf buf) {
        buf.writeBlockPos(this.housingBlockPosition);
        buf.writeString(this.owner);
    }

}
