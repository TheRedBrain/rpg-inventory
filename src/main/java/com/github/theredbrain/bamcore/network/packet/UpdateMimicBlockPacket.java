package com.github.theredbrain.bamcore.network.packet;

import com.github.theredbrain.bamcore.BetterAdventureMode;
import net.fabricmc.fabric.api.networking.v1.FabricPacket;
import net.fabricmc.fabric.api.networking.v1.PacketType;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.math.BlockPos;

public class UpdateMimicBlockPacket implements FabricPacket {
    public static final PacketType<UpdateMimicBlockPacket> TYPE = PacketType.create(
            BetterAdventureMode.identifier("update_mimic_block"),
            UpdateMimicBlockPacket::new
    );

    public final BlockPos mimicBlockPosition;

    public final BlockPos activeMimicBlockPositionOffset;
    public final BlockPos inactiveMimicBlockPositionOffset;

    public UpdateMimicBlockPacket(BlockPos mimicBlockPosition, BlockPos activeMimicBlockPositionOffset, BlockPos inactiveMimicBlockPositionOffset) {
        this.mimicBlockPosition = mimicBlockPosition;
        this.activeMimicBlockPositionOffset = activeMimicBlockPositionOffset;
        this.inactiveMimicBlockPositionOffset = inactiveMimicBlockPositionOffset;
    }

    public UpdateMimicBlockPacket(PacketByteBuf buf) {
        this(buf.readBlockPos(), buf.readBlockPos(), buf.readBlockPos());
    }
    @Override
    public PacketType<?> getType() {
        return TYPE;
    }
    @Override
    public void write(PacketByteBuf buf) {
        buf.writeBlockPos(this.mimicBlockPosition);
        buf.writeBlockPos(this.activeMimicBlockPositionOffset);
        buf.writeBlockPos(this.inactiveMimicBlockPositionOffset);
    }

}
