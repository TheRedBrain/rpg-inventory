package com.github.theredbrain.bamcore.network.packet;

import com.github.theredbrain.bamcore.BetterAdventureMode;
import net.fabricmc.fabric.api.networking.v1.FabricPacket;
import net.fabricmc.fabric.api.networking.v1.PacketType;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.hit.BlockHitResult;

public class UseBlockViaDialoguePacket implements FabricPacket {
    public static final PacketType<UseBlockViaDialoguePacket> TYPE = PacketType.create(
            BetterAdventureMode.identifier("use_block_via_dialogue"),
            UseBlockViaDialoguePacket::new
    );

    public final BlockHitResult blockHitResult;

    public UseBlockViaDialoguePacket(BlockHitResult blockHitResult) {
        this.blockHitResult = blockHitResult;
    }

    public UseBlockViaDialoguePacket(PacketByteBuf buf) {
        this(buf.readBlockHitResult());
    }
    @Override
    public PacketType<?> getType() {
        return TYPE;
    }
    @Override
    public void write(PacketByteBuf buf) {
        buf.writeBlockHitResult(this.blockHitResult);
    }

}
