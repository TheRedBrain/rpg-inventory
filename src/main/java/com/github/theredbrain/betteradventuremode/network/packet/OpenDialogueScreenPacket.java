package com.github.theredbrain.betteradventuremode.network.packet;

import com.github.theredbrain.betteradventuremode.BetterAdventureMode;
import net.fabricmc.fabric.api.networking.v1.FabricPacket;
import net.fabricmc.fabric.api.networking.v1.PacketType;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.math.BlockPos;

public class OpenDialogueScreenPacket implements FabricPacket {
    public static final PacketType<OpenDialogueScreenPacket> TYPE = PacketType.create(
            BetterAdventureMode.identifier("open_dialogue_screen"),
            OpenDialogueScreenPacket::new
    );

    public final BlockPos dialogueBlockPos;
    public final String responseDialogueIdentifier;

    public OpenDialogueScreenPacket(BlockPos dialogueBlockPos, String responseDialogueIdentifier) {
        this.dialogueBlockPos = dialogueBlockPos;
        this.responseDialogueIdentifier = responseDialogueIdentifier;
    }

    public OpenDialogueScreenPacket(PacketByteBuf buf) {
        this(buf.readBlockPos(), buf.readString());
    }
    @Override
    public PacketType<?> getType() {
        return TYPE;
    }
    @Override
    public void write(PacketByteBuf buf) {
        buf.writeBlockPos(this.dialogueBlockPos);
        buf.writeString(this.responseDialogueIdentifier);
    }
}
