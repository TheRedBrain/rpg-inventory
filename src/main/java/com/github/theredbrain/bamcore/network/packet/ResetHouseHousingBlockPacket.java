package com.github.theredbrain.bamcore.network.packet;

import com.github.theredbrain.bamcore.BetterAdventureModeCore;
import net.fabricmc.fabric.api.networking.v1.FabricPacket;
import net.fabricmc.fabric.api.networking.v1.PacketType;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.math.BlockPos;

public class ResetHouseHousingBlockPacket implements FabricPacket {
    public static final PacketType<ResetHouseHousingBlockPacket> TYPE = PacketType.create(
            BetterAdventureModeCore.identifier("reset_house_housing_block"),
            ResetHouseHousingBlockPacket::new
    );

    public final BlockPos housingBlockPosition;

    public ResetHouseHousingBlockPacket(BlockPos housingBlockPosition) {
        this.housingBlockPosition = housingBlockPosition;
    }

    public ResetHouseHousingBlockPacket(PacketByteBuf buf) {
        this(buf.readBlockPos());
    }
    @Override
    public PacketType<?> getType() {
        return TYPE;
    }
    @Override
    public void write(PacketByteBuf buf) {
        buf.writeBlockPos(this.housingBlockPosition);
    }

}
